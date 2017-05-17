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

package edu.umass.cs.sase.explanation.distancefunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.evaluation.DataPoint;
import edu.umass.cs.sase.explanation.featuregeneration.FeatureType;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.LabeledValue;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;
import edu.umass.cs.sase.interval.ValueInterval;
import edu.umass.cs.sase.util.csv.PointLabel;
import edu.umass.cs.sase.util.csv.SamplePoint;

public class TimeSeriesFeaturePair {
	TimeSeriesFeature tsFeature1;
	TimeSeriesFeature tsFeature2;
	
	String featureName;
	
	double euclideanDistance;
	double manhattanDistance;
	double dtwDistance;
	double lcssDistance;
	double edrDistance;
	double swaleDistance;
	double erpDistance;
	double tquestDistance;
	double spadeDistance;
	
	double entropyDistance;
	double entropyDistanceSampled;
	double priorClassEntropy;
	double postClassentropy;
	double postIntervalEntropy;
	boolean isValuable;
	
	
	
	double recentDistance; // record the most recent computed distance, updated after each computation
	boolean isGroundTruth; // used in result evaluation
	
	
	//the following features are prepared for compute correlations
	boolean prepared;
	ArrayList<Double> allValues;
	ArrayList<Long> allTimestamps;
	int allValueSize;

	HashMap<Integer, Double> indexedMeans; //from size to mean
	HashMap<Integer, Double> indexedMeanSquareRoot;//from size to mean square root
	HashMap<Integer, ArrayList<Double>> indexedValues; //from size to list of values; for sampled features;
	HashMap<Integer, ArrayList<Long>> indexedTimestamps; // from size to list of timestamps; for sampled features;
	
	double temporalCorrelation;
	
	
	double maxMultipleCorrelation; //cannot decrease as the increased size of variables
	
	//features for cross-validation
	ArrayList<LabeledValue> labeledValues;
	ArrayList<ValueInterval> intervals;
	
	boolean invalidated;
	
	//for cross validation
	double mergedDistance;
	
	
	//for fusion
	double p;
	double r;
	double q;
	double alpha = 0.5;
	
	
	public TimeSeriesFeaturePair(TimeSeriesFeature tsFeature1, TimeSeriesFeature tsFeature2) {
		this.tsFeature1 = tsFeature1;
		this.tsFeature2 = tsFeature2;
		this.featureName = this.tsFeature1.getFeatureName();
		this.normalize(this.tsFeature1, this.tsFeature2);
	}
	
	/**
	 * Used for partition classification
	 * @param tsFeature1
	 * @param tsFeature2
	 * @param featureName
	 * @throws Exception 
	 */
	public TimeSeriesFeaturePair(TimeSeriesFeature tsFeature1, TimeSeriesFeature tsFeature2, String featureName) throws Exception {
		this.tsFeature1 = tsFeature1;
		this.tsFeature1.setLabel(LabelType.Abnormal);
		this.tsFeature2 = tsFeature2;
		this.tsFeature2.setLabel(LabelType.Reference);
		this.featureName = featureName;
		
		
		//this.normalize(this.tsFeature1, this.tsFeature2);
		
		//this.computeDistancesForPartitionClassification();
	}
	
	public boolean allTheSameValue() {
		double value = tsFeature1.getValues().get(0);
		
		for (Double d: tsFeature1.getValues()) {
			if (d != value) {
				return false;
			}
		}
		
		for (Double d: tsFeature2.getValues()) {
			if (d != value) {
				return false;
			}
		}
		return true;
	}
	public void computeTemporalCorrelation() {
		int size = this.allValues.size();
		// compute mean
		double sumOfValue = 0.0;
		double sumOfTimestamps = 0.0;
		for (Double d: this.allValues) {
			sumOfValue += d;
		}
		for (Long t : this.allTimestamps) {
			sumOfTimestamps += t;
		}
		
		double xMean = sumOfValue / (double) size;
		double yMean = sumOfTimestamps / (double) size;
		
		
		//compute correlation, follow the formula on wikipedia:
		//https://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient
		
		double numerator = 0;
		for (int i = 0; i < size; i ++) {
			double xi = this.allValues.get(i);
			double yi = (double)this.allTimestamps.get(i);
			numerator += (xi - xMean) * (yi - yMean);
		} 
		
		//compute mean square root
		double totalValue = 0.0;
		for (Double d: this.allValues) {
			double xi = d - xMean;
			totalValue += xi * xi;
		}
		double xMeanSquareRoot = Math.sqrt(totalValue);
		
		double totalTimestamps = 0.0;
		for (Long t: this.allTimestamps) {
			double yi = (double)t - yMean;
			totalTimestamps += yi * yi;
		}
		double yMeanSquareRoot = Math.sqrt(totalTimestamps);

		
		double denominator = xMeanSquareRoot * yMeanSquareRoot;
		
		this.temporalCorrelation = numerator / denominator;
	}
	
	/**
	 * Used for prepare the data for correlation computation
	 */
	public void prepareForCorrelation() {
		if (this.prepared) {
			return;
		}
		
		this.allValues = new ArrayList<Double>();
		this.allValues.addAll(this.tsFeature1.getValues());
		this.allValues.addAll(this.tsFeature2.getValues());
		
		this.allTimestamps = new ArrayList<Long>();
		this.allTimestamps.addAll(this.tsFeature1.getTimestamps());
		this.allTimestamps.addAll(this.tsFeature2.getTimestamps());
		
		this.allValueSize = this.allValues.size();
		
		this.indexedValues = new HashMap<Integer, ArrayList<Double>>();
		this.indexedValues.put(this.allValueSize, this.allValues);
		
		this.indexedTimestamps = new HashMap<Integer, ArrayList<Long>>();
		this.indexedTimestamps.put(this.allValueSize, this.allTimestamps);
		
		
		this.indexedMeans = new HashMap<Integer, Double>();
		this.indexedMeanSquareRoot = new HashMap<Integer, Double>();
		
		prepared = true;
	}
	
	public void computeMean(int size) {
		ArrayList<Double> valueList = this.indexedValues.get(size);
		if (valueList == null) {
			this.sampleValue(size);
			valueList = this.indexedValues.get(size);
		}
		
		double sum = 0.0;
		for (Double d : valueList) {
			sum += d;
		}
		
		double mean = sum / (double)size;
		this.indexedMeans.put(size, mean);
		
		
		//mean square root sum
		//http://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient
		double total = 0.0;
		for (Double d: valueList) {
			double xi = d - mean;
			total += xi * xi;
		}
		double meanSquareRoot = Math.sqrt(total);
		this.indexedMeanSquareRoot.put(size, meanSquareRoot);
	}
	
	public void sampleValue(int size) {
		//sample, borrow code from alignment, TimeSeriesFeature.java
		ArrayList<Double> valueList = new ArrayList<Double>();
		ArrayList<Long> timestampList = new ArrayList<Long>();
		int toReduce = this.allValueSize - size;
		int stepSize = this.allValueSize / toReduce;
		
		for (int i = 0; i < this.allValueSize; i ++) {
			if (i % stepSize == 0 && toReduce > 0) {
				toReduce --;
				continue;
			}
			timestampList.add(this.allTimestamps.get(i));
			valueList.add(this.allValues.get(i));
		}
		
		//index
		this.indexedTimestamps.put(size, timestampList);
		this.indexedValues.put(size, valueList);
	}
	
	public double getMeanForSize(int size) {
		return this.indexedMeans.get(size);
	}
	
	public double getMeanSquareRootForSize(int size) {
		return this.indexedMeanSquareRoot.get(size);
	}
	
	public ArrayList<Double> getValueListForSize(int size) {
		return this.indexedValues.get(size);
	}
	
	public ArrayList<Long> getTimestampListForSize(int size) {
		return this.indexedTimestamps.get(size);
	}
	
	/**
	 * Normalize the values before computing distance
	 * Reference: http://stn.spotfire.com/spotfire_client_help/norm/norm_scale_between_0_and_1.htm
	 * e = (e - e_min)/(e_max-e_min)
	 * e = 0.5 if e_max == e_min
	 */
	public void normalize(TimeSeriesFeature ts1, TimeSeriesFeature ts2) {
		//compute max, min
		double max = Math.max(ts1.getMaxValue(), ts2.getMaxValue());
		double min = Math.min(ts1.getMinValue(), ts2.getMinValue());
		//normliaze
		ts1.normalizeValues(max, min);
		ts2.normalizeValues(max, min);
	}
	public void computeEntropyDistance() throws Exception {
		EntropyDistance entroDistance = new EntropyDistance();
		this.entropyDistance = entroDistance.getTimeSeiresDistance(this.tsFeature1, this.tsFeature2);
		this.recentDistance = this.entropyDistance;
		
		this.priorClassEntropy = entroDistance.getPriorClassEntropy();
		this.postClassentropy = entroDistance.getPostClassEntropy();
		this.postIntervalEntropy = entroDistance.getPostIntervalEntropy();
		this.isValuable = entroDistance.isValuable();
		//to do later: maybe add a variable to indicate whether it is special case or not
	}
	
	public void computeEntropyDistanceWithSampledFeature() throws Exception {
		this.tsFeature1.getSampledFeature().setLabel(LabelType.Abnormal);
		this.tsFeature2.getSampledFeature().setLabel(LabelType.Reference);
		
		EntropyDistance entroDistance = new EntropyDistance();
		this.entropyDistanceSampled = entroDistance.getTimeSeiresDistance(this.tsFeature1.getSampledFeature(), this.tsFeature2.getSampledFeature());
		
		/*
		this.recentDistance = this.entropyDistance;
		
		this.priorClassEntropy = entroDistance.getPriorClassEntropy();
		this.postClassentropy = entroDistance.getPostClassEntropy();
		this.postIntervalEntropy = entroDistance.getPostIntervalEntropy();
		this.isValuable = entroDistance.isValuable();
		*/
	}
	
	public void computeEuclideanDistance() {
		EuclideanDistance eDistance = new EuclideanDistance();
		this.euclideanDistance = eDistance.getTimeSeiresDistance(this.tsFeature1, this.tsFeature2);
		this.recentDistance = this.euclideanDistance;
	}
	
	public void computeManhattanDistance() {
		ManhattanDistance hDistance = new ManhattanDistance();
		this.manhattanDistance = hDistance.getTimeSeiresDistance(this.tsFeature1, this.tsFeature2);
		this.recentDistance = this.manhattanDistance;
	}
	
	public void computeDtwDistance() {
		if (this.tsFeature1.getFeatureType() == FeatureType.TimeSeriesRaw && ExplanationSettings.usingSampleForExpensiveDistance) {
			this.computeDtwDistanceWithSampledFeature();
		} else {
			DTWDistance dtw = new DTWDistance();
			this.dtwDistance = dtw.getTimeSeiresDistance(this.tsFeature1, this.tsFeature2);
			this.recentDistance = this.dtwDistance;
		}

	}
	
	public void computeDtwDistanceWithSampledFeature() {
		DTWDistance dtw = new DTWDistance();
		
		this.dtwDistance = dtw.getTimeSeiresDistance(this.tsFeature1.getSampledFeature(), this.tsFeature2.getSampledFeature());
		this.recentDistance = this.dtwDistance;
	}
	
	
	
	public void computeLCSSDistance() throws Exception {
		if (this.tsFeature1.getFeatureType() == FeatureType.TimeSeriesRaw && ExplanationSettings.usingSampleForExpensiveDistance) {
			this.computeLCSSDistanceWithSampledFeature();
		} else {
			LCSSDistance lcss = new LCSSDistance();
			this.lcssDistance = lcss.getTimeSeiresDistance(this.tsFeature1, this.tsFeature2);
			this.recentDistance = this.lcssDistance;
		}
	}
	
	public void computeLCSSDistanceWithSampledFeature() throws Exception {
		LCSSDistance lcss = new LCSSDistance();
		this.lcssDistance = lcss.getTimeSeiresDistance(this.tsFeature1.getSampledFeature(), this.tsFeature2.getSampledFeature());
		this.recentDistance = this.lcssDistance;
	}

	public void computeEDRDistance() throws Exception {
		if (this.tsFeature1.getFeatureType() == FeatureType.TimeSeriesRaw && ExplanationSettings.usingSampleForExpensiveDistance) {
			this.computeEDRDistanceWithSampledFeature();
		} else {
			EDRDistance edr = new EDRDistance();
			this.edrDistance = edr.getTimeSeiresDistance(this.tsFeature1, tsFeature2);
			this.recentDistance = this.edrDistance;
		}

	}
	
	public void computeEDRDistanceWithSampledFeature() throws Exception {
		EDRDistance edr = new EDRDistance();
		this.edrDistance = edr.getTimeSeiresDistance(this.tsFeature1.getSampledFeature(), tsFeature2.getSampledFeature());
		this.recentDistance = this.edrDistance;
	}
	
	public void computeSwaleDistance() throws Exception {
			System.out.println("Swale for " + this.featureName + "\tSize1=" + this.tsFeature1.getCount() + "\tSize2=" + this.tsFeature2.getCount());
		
			if (this.tsFeature1.getFeatureType() == FeatureType.TimeSeriesRaw && ExplanationSettings.usingSampleForExpensiveDistance) {
				this.computeSwaleDistanceWithSampledFeature();
			} else {
				SwaleDistance swale = new SwaleDistance();
				this.swaleDistance = swale.getTimeSeiresDistance(this.tsFeature1, this.tsFeature2);
				this.recentDistance = this.swaleDistance;

			}


	}
	
	
	public void computeSwaleDistanceWithSampledFeature() throws Exception {
		SwaleDistance swale = new SwaleDistance();
		this.swaleDistance = swale.getTimeSeiresDistance(this.tsFeature1.getSampledFeature(), this.tsFeature2.getSampledFeature());
		this.recentDistance = this.swaleDistance;
	}
	
	public void computeERPDistance() throws Exception {
		if (this.tsFeature1.getFeatureType() == FeatureType.TimeSeriesRaw && ExplanationSettings.usingSampleForExpensiveDistance) {
			this.computeERPDistanceWithSampledFeature();
		} else {
			ERPDistance erp = new ERPDistance();
			this.erpDistance = erp.getTimeSeiresDistance(this.tsFeature1, this.tsFeature2);
			this.recentDistance = this.erpDistance;
		}
	}
	
	public void computeERPDistanceWithSampledFeature() throws Exception {
		ERPDistance erp = new ERPDistance();
		this.erpDistance = erp.getTimeSeiresDistance(this.tsFeature1.getSampledFeature(), this.tsFeature2.getSampledFeature());
		this.recentDistance = this.erpDistance;
	}
	
	public void computeTquestDistance() throws Exception {
		TQuESTDistance tquest = new TQuESTDistance();
		this.tquestDistance = tquest.getTimeSeiresDistance(this.tsFeature1, this.tsFeature2);
		this.recentDistance = this.tquestDistance;
	}
	
	public void computeSpadeDistance() throws Exception {
		SpADEDistance spade = new SpADEDistance();
		this.spadeDistance = spade.getTimeSeiresDistance(tsFeature1, tsFeature2);
		this.recentDistance = this.spadeDistance;
	}
	
	public TimeSeriesFeature getTsFeature1() {
		return tsFeature1;
	}

	public void setTsFeature1(TimeSeriesFeature tsFeature1) {
		this.tsFeature1 = tsFeature1;
	}

	public TimeSeriesFeature getTsFeature2() {
		return tsFeature2;
	}

	public void setTsFeature2(TimeSeriesFeature tsFeature2) {
		this.tsFeature2 = tsFeature2;
	}

	public double getEuclideanDistance() {
		return euclideanDistance;
	}

	public void setEuclideanDistance(double euclideanDistance) {
		this.euclideanDistance = euclideanDistance;
	}

	public double getManhattanDistance() {
		return manhattanDistance;
	}

	public void setManhattanDistance(double manhattanDistance) {
		this.manhattanDistance = manhattanDistance;
	}

	public double getDtwDistance() {
		return dtwDistance;
	}

	public void setDtwDistance(double dtwDistance) {
		this.dtwDistance = dtwDistance;
	}

	public double getLcssDistance() {
		return lcssDistance;
	}

	public void setLcssDistance(double lcssDistance) {
		this.lcssDistance = lcssDistance;
	}

	public double getEdrDistance() {
		return edrDistance;
	}

	public void setEdrDistance(double edrDistance) {
		this.edrDistance = edrDistance;
	}

	public double getSwaleDistance() {
		return swaleDistance;
	}

	public void setSwaleDistance(double swaleDistance) {
		this.swaleDistance = swaleDistance;
	}

	public double getErpDistance() {
		return erpDistance;
	}

	public void setErpDistance(double erpDistance) {
		this.erpDistance = erpDistance;
	}

	public double getTquestDistance() {
		return tquestDistance;
	}

	public void setTquestDistance(double tquestDistance) {
		this.tquestDistance = tquestDistance;
	}

	public double getSpadeDistance() {
		return spadeDistance;
	}

	public void setSpadeDistance(double spadeDistance) {
		this.spadeDistance = spadeDistance;
	}


	public double getEntropyDistance() {
		return entropyDistance;
	}


	public void setEntropyDistance(double entropyDistance) {
		this.entropyDistance = entropyDistance;
	}


	public String getFeatureName() {
		return featureName;
	}


	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}


	public double getRecentDistance() {
		return recentDistance;
	}


	public void setRecentDistance(double recentDistance) {
		this.recentDistance = recentDistance;
	}


	public boolean isGroundTruth() {
		return isGroundTruth;
	}


	public void setGroundTruth(boolean isGroundTruth) {
		this.isGroundTruth = isGroundTruth;
	}


	public double getPriorClassEntropy() {
		return priorClassEntropy;
	}


	public void setPriorClassEntropy(double priorClassEntropy) {
		this.priorClassEntropy = priorClassEntropy;
	}


	public double getPostClassentropy() {
		return postClassentropy;
	}


	public void setPostClassentropy(double postClassentropy) {
		this.postClassentropy = postClassentropy;
	}


	public double getPostIntervalEntropy() {
		return postIntervalEntropy;
	}


	public void setPostIntervalEntropy(double postIntervalEntropy) {
		this.postIntervalEntropy = postIntervalEntropy;
	}


	public boolean isValuable() {
		return isValuable;
	}


	public void setValuable(boolean isValuable) {
		this.isValuable = isValuable;
	}

	public boolean isPrepared() {
		return prepared;
	}

	public void setPrepared(boolean prepared) {
		this.prepared = prepared;
	}

	public ArrayList<Double> getAllValues() {
		return allValues;
	}

	public void setAllValues(ArrayList<Double> allValues) {
		this.allValues = allValues;
	}

	public ArrayList<Long> getAllTimestamps() {
		return allTimestamps;
	}

	public void setAllTimestamps(ArrayList<Long> allTimestamps) {
		this.allTimestamps = allTimestamps;
	}

	public int getAllValueSize() {
		return allValueSize;
	}

	public void setAllValueSize(int allValueSize) {
		this.allValueSize = allValueSize;
	}

	public HashMap<Integer, Double> getIndexedMeans() {
		return indexedMeans;
	}

	public void setIndexedMeans(HashMap<Integer, Double> indexedMeans) {
		this.indexedMeans = indexedMeans;
	}

	public HashMap<Integer, ArrayList<Double>> getIndexedValues() {
		return indexedValues;
	}

	public void setIndexedValues(HashMap<Integer, ArrayList<Double>> indexedValues) {
		this.indexedValues = indexedValues;
	}

	public HashMap<Integer, ArrayList<Long>> getIndexedTimestamps() {
		return indexedTimestamps;
	}

	public void setIndexedTimestamps(
			HashMap<Integer, ArrayList<Long>> indexedTimestamps) {
		this.indexedTimestamps = indexedTimestamps;
	}

	public HashMap<Integer, Double> getIndexedMeanSquareRoot() {
		return indexedMeanSquareRoot;
	}

	public void setIndexedMeanSquareRoot(
			HashMap<Integer, Double> indexedMeanSquareRoot) {
		this.indexedMeanSquareRoot = indexedMeanSquareRoot;
	}

	public double getTemporalCorrelation() {
		return temporalCorrelation;
	}

	public void setTemporalCorrelation(double temporalCorrelation) {
		this.temporalCorrelation = temporalCorrelation;
	}

	public double getMaxMultipleCorrelation() {
		return maxMultipleCorrelation;
	}

	public void setMaxMultipleCorrelation(double maxMultipleCorrelation) {
		this.maxMultipleCorrelation = maxMultipleCorrelation;
	}
	
	public void kFoldValidation(int k) {
		this.prepareForCorrelation();
		for (int i = 0; i < k; i ++) {
			for (int j = 0; j < k; j ++) {
				ArrayList<Double> abnormalValuesCopy = new ArrayList<Double>(this.tsFeature1.getValues());
				ArrayList<Double> referenceValuesCopy = new ArrayList<Double>(this.tsFeature2.getValues());
				ArrayList<Double> abnormalTakeOut = this.takeOutValues(k, i, abnormalValuesCopy);
				ArrayList<Double> referenceTakeOut = this.takeOutValues(k, j, referenceValuesCopy);
				
				
			}
		}
	}
	
	public ArrayList<Double> takeOutValues(int k, int position, ArrayList<Double> values) {
		ArrayList<Double> takeOut = new ArrayList<Double>();
		int count = values.size() / k;
		for (int i = count * position; i < count * (position + 1); i ++) {
			takeOut.add(values.get(0));
			values.remove(0);
		}
		return takeOut;
	}
	
	
	
	public void computeDistancesForPartitionClassification() throws Exception {
		this.tsFeature1.generateSampleFeature(0.09, ExplanationSettings.minSampleSize, ExplanationSettings.maxSampleSize);
		this.tsFeature2.generateSampleFeature(0.09, ExplanationSettings.minSampleSize, ExplanationSettings.maxSampleSize);
		this.normalize(this.tsFeature1.getSampledFeature(), this.tsFeature2.getSampledFeature());
		
		
		
		System.out.println("computing manhattan distance...");
		this.computeManhattanDistance();
		System.out.println("computing euclidean distance...");
		this.computeEuclideanDistance();
		
		System.out.println("computing dtw distance...");
		this.computeDtwDistanceWithSampledFeature();// heap space
		
		
		System.out.println("computing LCSS distance...");
		//this.computeLCSSDistanceWithSampledFeature();//heap space
		
		System.out.println("computing EDR distance...");
		//this.computeEDRDistanceWithSampledFeature();//heap space
		
		System.out.println("computing Swale distance...");
		//this.computeSwaleDistance();
		
		
		System.out.println("computing ERP distance...");
		//this.computeERPDistance();//heahp space
		//this.computeERPDistanceWithSampledFeature();
		
		System.out.println("computing Tquest distance...");
		//this.computeTquestDistance();
		System.out.println("computing Spade distance...");
		//this.computeSpadeDistance();//training set not ready
		
		//set two different labels to compute distance, which are not necessarily the same as their true labels
		this.tsFeature1.setLabel(LabelType.Abnormal);
		this.tsFeature2.setLabel(LabelType.Reference);
		System.out.println("computing entropy distance...");
		this.computeEntropyDistance();
		this.computeEntropyDistanceWithSampledFeature();
	}
	
	public void printDistancesForPartitionClassification() {
		System.out.println(this.featureName + "\t" 
				+ this.tsFeature1.getValues().size() + "\t"
				+ this.tsFeature2.getValues().size() + "\t"
				+ this.tsFeature1.getFrequency() + "\t"
				+ this.tsFeature2.getFrequency() + "\t"
				+ this.manhattanDistance + "\t"
				+ this.euclideanDistance + "\t"
				+ this.dtwDistance + "\t"
				+ this.lcssDistance + "\t"
				+ this.edrDistance + "\t"
				+ this.swaleDistance + "\t"
				+ this.erpDistance + "\t"
				+ this.tquestDistance + "\t"
				+ this.spadeDistance + "\t"
				+ this.entropyDistance + "\t"
				+ this.entropyDistanceSampled + "\t"
				);
	}
	
	//validate feature methods
	
	//build intervals
	
	public void buildIntervals() {
		System.out.println("Building intervals for " + this.featureName);
		//convert values to labeled values, ignore timestamps
		this.labeledValues = new ArrayList<LabeledValue>();
		for (Double d: this.tsFeature1.getValues()) {
			labeledValues.add(new LabeledValue(d, this.tsFeature1.getLabel()));
		}
		
		for (Double d: this.tsFeature2.getValues()) {
			labeledValues.add(new LabeledValue(d, this.tsFeature2.getLabel()));
		}
		
		//sort
		Collections.sort(labeledValues);
		
		//build intervals
		this.intervals = new ArrayList<ValueInterval>();
		LabeledValue previousPoint = null;
		ValueInterval currentInterval = null;
		double lower = -Double.MIN_VALUE;
		int pointIndex = 0;
		int count = 0;
		
		while(pointIndex < this.labeledValues.size()) {
			currentInterval = new ValueInterval();
			currentInterval.setLower(lower);
			boolean extend = true;
			while (extend && pointIndex < this.labeledValues.size()) {
				LabeledValue currentPoint = this.labeledValues.get(pointIndex);
				if (previousPoint == null || currentPoint.getValue() == previousPoint.getValue() || currentPoint.getLabel() == currentInterval.getValueLable()) {
					currentInterval.addLabeledValue(currentPoint);
					previousPoint = currentPoint;
					pointIndex ++;
				} else {
					previousPoint = null;
					extend = false;
					currentInterval.setUpper((currentPoint.getValue() + currentInterval.getUpper()) / 2);// why?
					this.intervals.add(currentInterval);
					count ++;
					lower = currentInterval.getUpper();
				}
			}
		}
		currentInterval.setUpper(Double.MAX_VALUE);
		this.intervals.add(currentInterval);
		count ++;
	}
	
	//predict a data point
	public LabelType predictPoint(DataPoint dataPoint) {
		LabelType result = LabelType.Mixed;
		for (ValueInterval interval: this.intervals) {
			double value = dataPoint.getFeatureValue(this.featureName);
			double low = interval.getLower();
			double up = interval.getUpper();
			if(value >= low && value < up) {
				result = interval.getValueLable();
				/*
				if(result == LabelType.Mixed) {// should I do this?
					if (interval.getAbnormalCount() > interval.getNormalCount()) {
						result = LabelType.Abnormal;
					} else {
						result = LabelType.Reference;
					}
				}
				//in the invalidation process, tend to remove more?
				*/
				return result;
			}
		}
		return result;
		//leave it here, later if needed, modify this.
	}
	
	
	//label a value
	public LabelType predictPoint(LabeledValue point) {
		LabelType result = LabelType.Mixed;
		for (ValueInterval interval: this.intervals) {
			double value = point.getValue();
			double low = interval.getLower();
			double up = interval.getUpper();
			if(value >= low && value < up) {
				result = interval.getValueLable();
				/*
				if(result == LabelType.Mixed) {// should I do this?
					if (interval.getAbnormalCount() > interval.getNormalCount()) {
						result = LabelType.Abnormal;
					} else {
						result = LabelType.Reference;
					}
				}
				//in the invalidation process, tend to remove more?
				*/
				return result;
			}
		}
		return result;
		//leave it here, later if needed, modify this.
	}
	
	//validate by another feature
	public boolean validateByFeature(TimeSeriesFeature validationFeature) {
		LabelType fType = validationFeature.getClassifiedLabel();
		int correct = 0;
		int conflict = 0;
		for (Double d: validationFeature.getValues()) {
			LabelType result = this.predictPoint(new LabeledValue(d, fType));
			if (result == fType) {
				correct ++;
			} else {
				conflict ++;
			}
		}
		if (conflict > correct) {
			this.invalidated = true;
		} else {
			this.invalidated = false;
		}
		System.out.println(invalidated + "\tConflicted\t" + conflict + "\tCorrect\t" + correct);
		return this.invalidated;
	}
	
	/**
	 * Used for fusion techniques
	 * @param trainingPoints
	 */
	public void computeFeatureQuality(ArrayList<DataPoint> trainingPoints) {
		int tp = 0; //true label = abnormal; predicted = abnormal
		int fp = 0;// true label = reference; predicted != reference (abnormal or mixed)
		int tn = 0;//true label = reference; predicted = reference
		int fn = 0;//true label = abnormal; predicted != abnormal (reference or mixed)
		
		LabelType predictedLabel = LabelType.Mixed;
		for (DataPoint point : trainingPoints) {
			predictedLabel = this.predictPoint(point);
			
			if (point.getTrueLabel() == LabelType.Abnormal) {
				if (predictedLabel == point.getTrueLabel()) {
					tp ++;
				} else {
					fn ++;
				}
			} else {
				if (predictedLabel == point.getTrueLabel()) {
					tn ++;
				} else {
					fp ++;
				}
			}
		}
		
		//https://en.wikipedia.org/wiki/Precision_and_recall
		this.p =(double)tp / (double)(tp + fp);
		this.r = (double)tp/ (double)(tp + fn);
		this.q = this.alpha / (1.0 - this.alpha) * (1 - this.p) / p * r;
	}

	public double getEntropyDistanceSampled() {
		return entropyDistanceSampled;
	}

	public void setEntropyDistanceSampled(double entropyDistanceSampled) {
		this.entropyDistanceSampled = entropyDistanceSampled;
	}

	public ArrayList<LabeledValue> getLabeledValues() {
		return labeledValues;
	}

	public void setLabeledValues(ArrayList<LabeledValue> labeledValues) {
		this.labeledValues = labeledValues;
	}

	public ArrayList<ValueInterval> getIntervals() {
		return intervals;
	}

	public void setIntervals(ArrayList<ValueInterval> intervals) {
		this.intervals = intervals;
	}

	public boolean isInvalidated() {
		return invalidated;
	}

	public void setInvalidated(boolean invalidated) {
		this.invalidated = invalidated;
	}

	public double getMergedDistance() {
		return mergedDistance;
	}

	public void setMergedDistance(double mergedDistance) {
		this.mergedDistance = mergedDistance;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public double getQ() {
		return q;
	}

	public void setQ(double q) {
		this.q = q;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	
	
}
