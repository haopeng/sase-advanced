/*
 * Copyright (c) 2017, Regents of the University of Massachusetts Amherst 
 * All rights reserved.

 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:

 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 * 		and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 * 		and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *   * Neither the name of the University of Massachusetts Amherst nor the names of its contributors 
 * 		may be used to endorse or promote products derived from this software without specific prior written 
 * 		permission.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package edu.umass.cs.sase.explanation.crossvalidation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import au.com.bytecode.opencsv.CSVWriter;
import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeatureComparator;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.engine.ProfilingEngine;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.OptimizedRawEventReader;
import edu.umass.cs.sase.explanation.featuregeneration.PropertyReader;
import edu.umass.cs.sase.explanation.featuregeneration.RawEventReader;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesRawGenerator;
import edu.umass.cs.sase.explanation.featuregeneration.WindowTimeSeriesGenerator;


/**
 * Input: features over annoated periods; labeled partitions
 * Output: validated features
 * @author haopeng
 *
 */
public class FeatureValidationEngine {
	String inputFolder; // logs path
	ArrayList<PartitionReader> abnormalPeriods;
	ArrayList<PartitionReader> referencePeriods;
	long timeWindowSize;
	TimeSeriesFeatureComparator tsFeatureComparator;//features from annotated periods
	
	
	TimeSeriesFeatureComparator mergedFeatureComparator;
	ArrayList<HashMap<String, TimeSeriesFeature>> abnormalFeatureIndexList;
	ArrayList<HashMap<String, TimeSeriesFeature>> referenceFeatureIndexList;
	
	ArrayList<TimeSeriesFeaturePair> sortedFeatures;
	ArrayList<TimeSeriesFeaturePair> topFeatures;
	double suddenDropThreshold = 0.20;//0.25
	double distanceThreshold = 0.6;
	
	//full correlation
	HashMap<String, Double> fullCorrelationIndex;
	
	
	//correlation cluster
	HashMap<String, Double> correlationIndex;
	int specialCaseCount;//??
	
	HashMap<String, TimeSeriesFeaturePair> topfeatureIndex;
	HashMap<String, HashSet<String>> clustersIndex;
	ArrayList<HashSet<String>> clusterList;
	
	double correlationThreshold = 0.65;//0.75;
	//String correlationIndexOutputPath = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/9.csv";
	String correlationIndexOutputPath = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/m12.csv";
	public FeatureValidationEngine(String inputFolder, ArrayList<PartitionReader> abnormalPeriods, ArrayList<PartitionReader> referencePeriods, long timeWindowSize, TimeSeriesFeatureComparator tsFeatureComparator) {
		this.inputFolder = inputFolder;
		this.abnormalPeriods = abnormalPeriods;
		this.referencePeriods = referencePeriods;		
		this.timeWindowSize = timeWindowSize;
		this.tsFeatureComparator = tsFeatureComparator;
	}
	
	public void runEngine() throws Exception {
		//read data and generate feature and index
		ProfilingEngine.readAndGenerateStart = System.currentTimeMillis();
		this.readAndGenerateFeatures();
		ProfilingEngine.readAndGenerateEnd = System.currentTimeMillis();
		//merge 
		ProfilingEngine.mergeStart = System.currentTimeMillis();
		this.mergeFeatures();
		ProfilingEngine.mergeEnd = System.currentTimeMillis();
		//recompute and sort
		ProfilingEngine.recomputeStart = System.currentTimeMillis();
		this.recomputeEntropyDistance();
		ProfilingEngine.recomputeEnd = System.currentTimeMillis();
		//detect sudden drop and cut
		ProfilingEngine.cutStart = System.currentTimeMillis();
		this.cutFeatures();
		this.indexTopFeatures();
		
		if (ExplanationSettings.printResult) {
			this.printTopFeatures();			
		}

		ProfilingEngine.cutEnd = System.currentTimeMillis();
		//compute correlation matrix/index
		ProfilingEngine.clusterStart = System.currentTimeMillis();
		this.computeCorrelationIndex();
		//for later verification
		this.computeFullCorrelationIndex();
		this.writeCorrelationIndexToCSV(correlationIndexOutputPath);
		
		
		if (ExplanationSettings.printResult) {
			this.printCorrelationMatrix();			
		}

		//cluster based on correlation
		this.buildClusters(this.correlationThreshold);
		ProfilingEngine.clusterEnd = System.currentTimeMillis();

	}
	
	public void indexTopFeatures() {
		this.topfeatureIndex = new HashMap<String, TimeSeriesFeaturePair>();
		for (TimeSeriesFeaturePair pair: this.topFeatures) {
			this.topfeatureIndex.put(pair.getFeatureName(), pair);
		}
	}
	
	
	/**
	 * Borrowed from edu.umass.cs.sase.cluster.TextCluster
	 * @param threshold
	 * @param topK
	 * @return
	 */
	public int buildClusters(double threshold) {

		this.clustersIndex = new HashMap<String, HashSet<String>>();
		this.clusterList = new ArrayList<HashSet<String>>();
		
		//initialize, build a cluster for each attribute
		for (int i = 0; i < this.topFeatures.size(); i ++) {
			String feature = this.topFeatures.get(i).getFeatureName();
			HashSet<String> cluster = new HashSet<String>();
			cluster.add(feature);
			this.clustersIndex.put(feature, cluster);
			this.clusterList.add(cluster);
		}
		
		//build the edges and merge clusters
		int removeCount = 0;
		for (int i = 0; i < this.topFeatures.size(); i ++) {
			String sI = this.topFeatures.get(i).getFeatureName();
			HashSet<String> clusterI = this.clustersIndex.get(sI);
			for (int j = i + 1; j < this.topFeatures.size(); j ++) {
				String sJ = this.topFeatures.get(j).getFeatureName();
				HashSet<String> clusterJ = this.clustersIndex.get(sJ);
				
				String key = sI + "-" + sJ;
				if (Math.abs(this.correlationIndex.get(key)) >= threshold && clusterI != clusterJ) {
					//merge cluster J to cluster I
					for (String f : clusterJ) {
						clusterI.add(f);
						this.clustersIndex.put(f, clusterI);
					}
					this.clusterList.remove(clusterJ);
					removeCount ++;
					//System.out.println("Remove " + sJ + " for " + sI + "  :" + (removeCount) + " " + Math.abs(this.correlationIndex.get(sI + "-" + sJ)) + " " + this.clusterList.size());
				}
			}
		}
		
		if (ExplanationSettings.printResult) {
			System.out.println(this.topFeatures.size() + "\t" + threshold + "\t" + this.clusterList.size());
			
			
			//print structure
			for (int i = 0; i < this.clusterList.size(); i ++) {
				HashSet<String> cluster = this.clusterList.get(i);
				System.out.print(i + " \t" + cluster.size() + ":\t");
				for (String s : cluster) {
					System.out.print(s + "\t");
				}
				System.out.println();
			}
		}
		return this.clusterList.size();
	}
	/**
	 * borrowed from package edu.umass.cs.sase.explanation.featureselection.MaxDistanceCorrelationFilter 
	 */
	public void computeCorrelationIndex() {
		this.correlationIndex = new HashMap<String, Double>();
		for (int i = 0; i < this.topFeatures.size(); i ++) {
			TimeSeriesFeaturePair p1 = this.topFeatures.get(i);
			this.correlationIndex.put(p1.getFeatureName() + "-" + p1.getFeatureName(), 1.0);
			for (int j = i + 1; j < this.topFeatures.size(); j ++) {
				
				TimeSeriesFeaturePair p2 = this.topFeatures.get(j);
				double correlation = this.computePairCorrelation(p1, p2);
				
				this.correlationIndex.put(p1.getFeatureName() + "-" + p2.getFeatureName(), correlation);
				this.correlationIndex.put(p2.getFeatureName() + "-" + p1.getFeatureName(), correlation);
			}
		}
	}
	
	public void computeFullCorrelationIndex() {
		System.out.println("Building full correlations");
		this.fullCorrelationIndex = new HashMap<String, Double>();
		ArrayList<TimeSeriesFeaturePair> allFeatures = this.sortedFeatures;
		int count = 0;
		for (int i = 0; i < allFeatures.size(); i ++) {
			TimeSeriesFeaturePair p1 = allFeatures.get(i);
			this.fullCorrelationIndex.put(p1.getFeatureName() + "-" + p1.getFeatureName(), 1.0);
			for (int j = i + 1; j < allFeatures.size(); j ++) {
				
				TimeSeriesFeaturePair p2 = allFeatures.get(j);
				double correlation = this.computePairCorrelation(p1, p2);
				
				this.fullCorrelationIndex.put(p1.getFeatureName() + "-" + p2.getFeatureName(), correlation);
				this.fullCorrelationIndex.put(p2.getFeatureName() + "-" + p1.getFeatureName(), correlation);
				count ++;
				//System.out.println(count + ":" + p2.getFeatureName() + "-" + p1.getFeatureName() + "-" + correlation);
			}
		}
		
		System.out.println("Building full correlations done");
	}
	
	public void writeCorrelationIndexToCSV(String filePath) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(filePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String[] line = new String[2];
		for (String key: this.fullCorrelationIndex.keySet()) {
				double correlation = this.fullCorrelationIndex.get(key);
				line[0] = key;
				line[1] = correlation + "";
				writer.writeNext(line);
		}
		writer.close();
		System.out.println("Written to " + filePath);
	}
	
	public void printCorrelationMatrix() {
		for (int i = 0; i < this.topFeatures.size(); i ++) {
			TimeSeriesFeaturePair fi = this.topFeatures.get(i);
			for (int j = 0; j < this.topFeatures.size(); j ++) {
				TimeSeriesFeaturePair fj = this.topFeatures.get(j);
				String key = fi.getFeatureName() + "-" + fj.getFeatureName();
				System.out.print(this.correlationIndex.get(key) + "\t");
			}
			System.out.println();
		}
	}
	
	
	public double computePairCorrelation(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
		p1.prepareForCorrelation();
		p2.prepareForCorrelation();
		//first check size if equal
		int size;
		if (p1.getAllValueSize() == p2.getAllValueSize()) {
			size = p1.getAllValueSize();
		} else {
			size = Math.min(p1.getAllValueSize(), p2.getAllValueSize());
		}
		
		p1.computeMean(size);
		p2.computeMean(size);
		
		//compute correlation, follow the formula on wikipedia:
		//https://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient
		double xMean = p1.getMeanForSize(size);
		double yMean = p2.getMeanForSize(size);
		
		ArrayList<Double> xValues = p1.getValueListForSize(size);
		ArrayList<Double> yValues = p2.getValueListForSize(size);
		
		//System.out.println("============Correlation==============");
		//System.out.println(p1.getFeatureName() + "\t" + p2.getFeatureName());
		
		double numerator = 0;
		for (int i = 0; i < size; i ++) {
			double xi = xValues.get(i);
			double yi = yValues.get(i);
			numerator += (xi - xMean) * (yi - yMean);
			//System.out.println(xi + "\t" + yi);//debug
		}
		
		double denominator = p1.getMeanSquareRootForSize(size) * p2.getMeanSquareRootForSize(size);
		
		//debug

		double correlation = 0.0;
		
		if (denominator > 0.0) {
			// http://www.vbforums.com/showthread.php?425748-RESOLVED-What-s-the-correlation-coefficient-i-fthe-standard-deviation-is-zero
			// in case of the denominator being zero
			correlation = numerator / denominator;
		} else {
			this.specialCaseCount ++;
			//System.out.println("#######Special case: \t" + this.specialCaseCount + "\t##########");
		}
		
		//System.out.println("Correlation:\t" + correlation);
		
		return correlation;
	}
	
	
	public void printTopFeatures() {
		System.out.println("Top features size: " + this.topFeatures.size());
		for (int i = 0; i < this.topFeatures.size(); i ++) {
			System.out.println(i + "\t" + this.topFeatures.get(i).getFeatureName());
		}
	}
	public void cutFeatures() {
		this.topFeatures = new ArrayList<TimeSeriesFeaturePair>();
		this.topFeatures.add(sortedFeatures.get(0));
		TimeSeriesFeaturePair previousPair = sortedFeatures.get(0);
		
		for (int i = 1; i < this.sortedFeatures.size(); i ++) {
			TimeSeriesFeaturePair currentPair = this.sortedFeatures.get(i);
			double v = (previousPair.getEntropyDistance() - currentPair.getEntropyDistance()) / previousPair.getEntropyDistance();
			
			if (ExplanationSettings.printResult) {
				System.out.println(v);				
			}

			if (currentPair.getEntropyDistance() >= this.distanceThreshold || (previousPair.getEntropyDistance() - currentPair.getEntropyDistance()) / previousPair.getEntropyDistance() <= this.suddenDropThreshold) {
				this.topFeatures.add(currentPair);
				previousPair = currentPair;
				//System.out.println();
			} else {
				return;
			}
		}
		
		
	}
	
	public void recomputeEntropyDistance() throws Exception {
		//this.mergedFeatureComparator.computeEntropyDistanceForRawAndAgg(10000);//both agg and raw
		this.mergedFeatureComparator.computeEntropyDistance(10000);
		this.sortedFeatures = this.mergedFeatureComparator.returnAggTimeSeriesFeatureListRanked();
		
	}
	public void mergeFeatures() throws Exception {
		//TimeSeriesFeatureComparator(ArrayList<TimeSeriesFeature> aTSRaw, ArrayList<TimeSeriesFeature> rTSRaw, ArrayList<TimeSeriesFeature> aTSA, ArrayList<TimeSeriesFeature> rTSA) 
		ArrayList<TimeSeriesFeature> aTSRawMerged = this.mergeFeatureList(this.tsFeatureComparator.getAbnormalTimeSeriesRawFeatures(), this.abnormalFeatureIndexList);
		ArrayList<TimeSeriesFeature> rTSRawMerged = this.mergeFeatureList(this.tsFeatureComparator.getReferenceTimeSeriesRawFeatures(), this.referenceFeatureIndexList);
		
		ArrayList<TimeSeriesFeature> aTSAMerged = this.mergeFeatureList(this.tsFeatureComparator.getAbnormalTimeSeriesAggFeatures(), this.abnormalFeatureIndexList);
		ArrayList<TimeSeriesFeature> rTSAMerged = this.mergeFeatureList(this.tsFeatureComparator.getReferenceTimeSeriesAggFeatures(), this.referenceFeatureIndexList);
		
		this.mergedFeatureComparator = new TimeSeriesFeatureComparator(aTSRawMerged, rTSRawMerged, aTSAMerged, rTSAMerged);
		
	}
	
	public ArrayList<TimeSeriesFeature> mergeFeatureList(ArrayList<TimeSeriesFeature> originalList, ArrayList<HashMap<String, TimeSeriesFeature>> toBeMerged) {
		ArrayList<TimeSeriesFeature> mergedList = new ArrayList<TimeSeriesFeature>();
		
		for (TimeSeriesFeature f: originalList) {
			
			String key = f.getFeatureName();
			TimeSeriesFeature m = new TimeSeriesFeature(f);
			mergedList.add(m);
			
			
			for (HashMap<String, TimeSeriesFeature> index: toBeMerged) {
				
				TimeSeriesFeature toBeMergeFeature = index.get(key);
				long start = System.currentTimeMillis();
				
				if (toBeMergeFeature != null) {
					
					m.addPoints(toBeMergeFeature.getTimestamps(), toBeMergeFeature.getValues());
					
				}
				ProfilingEngine.addPointTime += System.currentTimeMillis() - start;
			}
			
			
		}
		
		
		return mergedList;
	}
	
	public void readAndGenerateFeatures() throws IOException {
		this.abnormalFeatureIndexList = new ArrayList<HashMap<String, TimeSeriesFeature>>();
		for (PartitionReader partition : this.abnormalPeriods) {
			long start = System.currentTimeMillis();
			RawEventReader reader = null;
					
			if (ExplanationSettings.useOptimizedRawReader) {
				reader = new OptimizedRawEventReader(inputFolder, partition.startTimestamp, partition.endTimestamp, -1, -1, 0);
			} else {
				reader = new RawEventReader(inputFolder, partition.startTimestamp, partition.endTimestamp, -1, -1, 0);	
			}
			
			ProfilingEngine.readTime += System.currentTimeMillis() - start;
			
			long generateStart = System.currentTimeMillis();
			HashMap<String, TimeSeriesFeature> index = this.generateFeaturesForOnePartition(partition, reader, LabelType.Abnormal);
			ProfilingEngine.generateTime += System.currentTimeMillis() - generateStart;
			this.abnormalFeatureIndexList.add(index);
		}
		
		this.referenceFeatureIndexList = new ArrayList<HashMap<String, TimeSeriesFeature>>();
		for (PartitionReader partition : this.referencePeriods) {
			long start = System.currentTimeMillis();
			RawEventReader reader = null;
			
			if (ExplanationSettings.useOptimizedRawReader) {
				reader = new OptimizedRawEventReader(inputFolder, -1, -1, partition.startTimestamp, partition.endTimestamp, 0);
			} else {
				reader = new RawEventReader(inputFolder, -1, -1, partition.startTimestamp, partition.endTimestamp, 0);	
			}
			
			
			
			ProfilingEngine.readTime += System.currentTimeMillis() - start;
			
			long generateStart = System.currentTimeMillis();
			HashMap<String, TimeSeriesFeature> index = this.generateFeaturesForOnePartition(partition, reader, LabelType.Reference);
			ProfilingEngine.generateTime += System.currentTimeMillis() - generateStart;
			
			this.referenceFeatureIndexList.add(index);
		}

	}
	
	/*
	public RawEventReader readEventsForAbnormalPartition(PartitionReader partition) throws IOException {
		RawEventReader reader = new RawEventReader(inputFolder, partition.startTimestamp, partition.endTimestamp, -1, -1, 0);
		return reader;
	}
	*/
	public HashMap<String, TimeSeriesFeature> generateFeaturesForOnePartition(PartitionReader partition, RawEventReader reader, LabelType label) throws IOException {
		HashMap<String, TimeSeriesFeature> index = new HashMap<String, TimeSeriesFeature>();
		
		if (label == LabelType.Abnormal) {
			//raw
			TimeSeriesRawGenerator abnormalTSRawGenerator = new TimeSeriesRawGenerator(reader.getSchemas(), reader.getAbnormalRawEventLists(), reader.getAbnormalStart(), reader.getAbnormalEnd(), LabelType.Abnormal);
			this.indexFeatureList(index, abnormalTSRawGenerator.getTimeSeriesRawFeatures());
			//aggregations
			WindowTimeSeriesGenerator abnormalWTSGenerator = new WindowTimeSeriesGenerator(abnormalTSRawGenerator.getTimeSeriesRawFeatures(), timeWindowSize, LabelType.Abnormal); // 60 secons(60000 milliseconds)
			this.indexFeatureList(index, abnormalWTSGenerator.getWindowTimeSeriesFeatures());
			
		} else {
			//raw
			TimeSeriesRawGenerator referenceTSRawGenerator = new TimeSeriesRawGenerator(reader.getSchemas(), reader.getReferenceRawEventLists(), reader.getReferenceStart(), reader.getReferenceEnd(), LabelType.Reference);
			this.indexFeatureList(index, referenceTSRawGenerator.getTimeSeriesRawFeatures());
			//aggregations
			WindowTimeSeriesGenerator referenceWTSGenerator = new WindowTimeSeriesGenerator(referenceTSRawGenerator.getTimeSeriesRawFeatures(), timeWindowSize, LabelType.Reference);
			this.indexFeatureList(index, referenceWTSGenerator.getWindowTimeSeriesFeatures());
		}
		
		return index;
	}
	
	public void indexFeatureList(HashMap<String,TimeSeriesFeature> index, ArrayList<TimeSeriesFeature> featureList) {
		for (TimeSeriesFeature f: featureList) {
			index.put(f.getFeatureName(), f);
		}

	}

	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public ArrayList<PartitionReader> getAbnormalPeriods() {
		return abnormalPeriods;
	}

	public void setAbnormalPeriods(ArrayList<PartitionReader> abnormalPeriods) {
		this.abnormalPeriods = abnormalPeriods;
	}

	public ArrayList<PartitionReader> getReferencePeriods() {
		return referencePeriods;
	}

	public void setReferencePeriods(ArrayList<PartitionReader> referencePeriods) {
		this.referencePeriods = referencePeriods;
	}

	public long getTimeWindowSize() {
		return timeWindowSize;
	}

	public void setTimeWindowSize(long timeWindowSize) {
		this.timeWindowSize = timeWindowSize;
	}

	public TimeSeriesFeatureComparator getTsFeatureComparator() {
		return tsFeatureComparator;
	}

	public void setTsFeatureComparator(
			TimeSeriesFeatureComparator tsFeatureComparator) {
		this.tsFeatureComparator = tsFeatureComparator;
	}

	public TimeSeriesFeatureComparator getMergedFeatureComparator() {
		return mergedFeatureComparator;
	}

	public void setMergedFeatureComparator(
			TimeSeriesFeatureComparator mergedFeatureComparator) {
		this.mergedFeatureComparator = mergedFeatureComparator;
	}

	public ArrayList<HashMap<String, TimeSeriesFeature>> getAbnormalFeatureIndexList() {
		return abnormalFeatureIndexList;
	}

	public void setAbnormalFeatureIndexList(
			ArrayList<HashMap<String, TimeSeriesFeature>> abnormalFeatureIndexList) {
		this.abnormalFeatureIndexList = abnormalFeatureIndexList;
	}

	public ArrayList<HashMap<String, TimeSeriesFeature>> getReferenceFeatureIndexList() {
		return referenceFeatureIndexList;
	}

	public void setReferenceFeatureIndexList(
			ArrayList<HashMap<String, TimeSeriesFeature>> referenceFeatureIndexList) {
		this.referenceFeatureIndexList = referenceFeatureIndexList;
	}

	public ArrayList<TimeSeriesFeaturePair> getSortedFeatures() {
		return sortedFeatures;
	}

	public void setSortedFeatures(ArrayList<TimeSeriesFeaturePair> sortedFeatures) {
		this.sortedFeatures = sortedFeatures;
	}

	public ArrayList<TimeSeriesFeaturePair> getTopFeatures() {
		return topFeatures;
	}

	public void setTopFeatures(ArrayList<TimeSeriesFeaturePair> topFeatures) {
		this.topFeatures = topFeatures;
	}

	public double getSuddenDropThreshold() {
		return suddenDropThreshold;
	}

	public void setSuddenDropThreshold(double suddenDropThreshold) {
		this.suddenDropThreshold = suddenDropThreshold;
	}

	public HashMap<String, Double> getCorrelationIndex() {
		return correlationIndex;
	}

	public void setCorrelationIndex(HashMap<String, Double> correlationIndex) {
		this.correlationIndex = correlationIndex;
	}

	public int getSpecialCaseCount() {
		return specialCaseCount;
	}

	public void setSpecialCaseCount(int specialCaseCount) {
		this.specialCaseCount = specialCaseCount;
	}

	public HashMap<String, TimeSeriesFeaturePair> getTopfeatureIndex() {
		return topfeatureIndex;
	}

	public void setTopfeatureIndex(
			HashMap<String, TimeSeriesFeaturePair> topfeatureIndex) {
		this.topfeatureIndex = topfeatureIndex;
	}

	public HashMap<String, HashSet<String>> getClustersIndex() {
		return clustersIndex;
	}

	public void setClustersIndex(HashMap<String, HashSet<String>> clustersIndex) {
		this.clustersIndex = clustersIndex;
	}

	public ArrayList<HashSet<String>> getClusterList() {
		return clusterList;
	}

	public void setClusterList(ArrayList<HashSet<String>> clusterList) {
		this.clusterList = clusterList;
	}

	public double getCorrelationThreshold() {
		return correlationThreshold;
	}

	public void setCorrelationThreshold(double correlationThreshold) {
		this.correlationThreshold = correlationThreshold;
	}
	

}
