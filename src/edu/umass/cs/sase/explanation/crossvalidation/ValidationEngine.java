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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.math3.stat.descriptive.moment.Variance;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeatureComparator;
import edu.umass.cs.sase.explanation.engine.ProfilingEngine;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;

/**
 * Input: annotated periods; related partitions; features from annotated periods;
 * Output: validated features across related partitions
 * @author haopeng
 *
 */
public class ValidationEngine {
	String inputPropertiesFile;
	String inputFolder;//folder where all logs are stored
	TimeSeriesFeatureComparator annotatedTSFeatureComparator;
	long timeWindow;
	
	
	
	String partitionFolder; // a folder stores all partitions, stores the visualized metrics, named by partition id.csv
	
	long abnormalPartitionId;
	long abnormalStart;
	long abnormalEnd;
	
	long referencePartitionId;
	long referenceStart;
	long referenceEnd;
	
	ArrayList<Long> relatedPartitionIds;
	
	//////////
	
	HashMap<Long, PartitionReader> partitionIndex; // stores all entire partitions
	
	
	
	boolean alignByPoint;
	boolean alignByTime;
	
	PartitionReader abnormalPeriod; // the cutted abnormal period
	PartitionReader referencePeriod; // the cutted reference period
	
	ArrayList<PartitionReader> abnormalGroupPeriods; // the unlabled cutted periods, aligned with abnormal period
	ArrayList<PartitionReader> referenceGroupPeriods; // the unlabled cutted periods, aligned with reference period
	
	//ArrayList<PartitionReader> abnormalPeriods;//labeled abnormal periods
	//ArrayList<PartitionReader> referencePeriods;//labeled reference periods
	
	
	
	//for clustering
	ClusterAnalyzer cluster;
	double threshold = 0.15; //distance <= threshold: the same label; distance >= 1-threshold, the oppositive label; otherwise, mixed label
	ArrayList<PartitionReader> labeledAbnormal;
	ArrayList<PartitionReader> labeledReference;
	
	FeatureValidationEngine featureValidationEngine;
	public ValidationEngine(String propertiesFile, String inputFolder, TimeSeriesFeatureComparator tsComparator, long timeWindow) throws IOException {
		this.inputPropertiesFile = propertiesFile;
		this.inputFolder = inputFolder;
		this.annotatedTSFeatureComparator = tsComparator;
		this.timeWindow = timeWindow;
		//read property files
		this.getProperties();
	}
	
	public void runEngine() throws Exception {
		//read partitions
		ProfilingEngine.readPartitionStart = System.currentTimeMillis();
		this.readAllPartitions();
		ProfilingEngine.readPartitionEnd = System.currentTimeMillis();
		//decide how to align partitions
		ProfilingEngine.decideAlignmentStart = System.currentTimeMillis();
		this.decideAlignment();
		ProfilingEngine.decideAlignmentEnd = System.currentTimeMillis();
		//alignment
		ProfilingEngine.alignmentStart = System.currentTimeMillis();
		this.alignUnlabeledPartitions();
		ProfilingEngine.alignmentEnd = System.currentTimeMillis();
		//clustering and assign labels
		ProfilingEngine.labelStart = System.currentTimeMillis();
		this.assignLabelByClustering();
		ProfilingEngine.labelEnd = System.currentTimeMillis();
		//recompute features and detect sudden drop //cluster selected features and build clusters based on correlations
		ProfilingEngine.validateStart = System.currentTimeMillis();
		this.validateFeaturesAndClusterFeatures();
		ProfilingEngine.validateEnd = System.currentTimeMillis();
	}
	
	public void validateFeaturesAndClusterFeatures() throws Exception{
		//#FeatureValidationEngine(String inputFolder, ArrayList<PartitionReader> abnormalPeriods, ArrayList<PartitionReader> referencePeriods, long timeWindowSize, TimeSeriesFeatureComparator tsFeatureComparator)
		this.featureValidationEngine = new FeatureValidationEngine(inputFolder, this.labeledAbnormal, this.labeledReference, timeWindow, this.annotatedTSFeatureComparator);
		this.featureValidationEngine.runEngine();
	}
	
	
	public void assignLabelByClustering() throws Exception {
		this.cluster = new ClusterAnalyzer(this.threshold);
		this.labeledAbnormal = new ArrayList<PartitionReader>();
		this.labeledReference = new ArrayList<PartitionReader>();
		//assign labels to abnormal periods
		this.assignLabelToPeriodList(this.abnormalGroupPeriods, this.abnormalPeriod);
		//assign labels to reference periods
		this.assignLabelToPeriodList(this.referenceGroupPeriods, this.referencePeriod);
	}
	
	public void assignLabelToPeriodList(ArrayList<PartitionReader> periodList, PartitionReader labeledPeriod) throws Exception {
		for (PartitionReader period: periodList) {
			LabelType label = this.cluster.assignLabel(labeledPeriod, period);
			period.getPartition().setLabel(label);
			switch(label) {
				case Abnormal:
					if (ExplanationSettings.printResult) {
						System.out.println("Abnormal label is assigned to partition " + period.getPartitionId() + "-" + period.getAlignLabel());	
					}
					labeledAbnormal.add(period);
					break;
				case Reference:
					if (ExplanationSettings.printResult) {
						System.out.println("Reference label is assigned to partition " + period.getPartitionId() + "-" + period.getAlignLabel());	
					}
					labeledReference.add(period);
					break;
				default:
					if (ExplanationSettings.printResult) {
						System.out.println("Mixed label is assigned to partition" + period.getPartitionId() + "-" + period.getAlignLabel());	
					}
					break;
			}
		}
	}
	
	
	public void alignUnlabeledPartitions() {
		this.abnormalGroupPeriods = new ArrayList<PartitionReader>();
		this.referenceGroupPeriods = new ArrayList<PartitionReader>();
		
		this.abnormalPeriod= new PartitionReader(this.partitionIndex.get(this.abnormalPartitionId), this.abnormalStart, this.abnormalEnd);
		this.abnormalPeriod.getPartition().setLabel(LabelType.Abnormal);
		
		this.referencePeriod = new PartitionReader(this.partitionIndex.get(this.referencePartitionId), this.referenceStart, this.referenceEnd);
		this.referencePeriod.getPartition().setLabel(LabelType.Reference);
		
		for (Long id: this.relatedPartitionIds) {
			PartitionReader unlabeledPartition = this.partitionIndex.get(id);
			PartitionReader alignWithAbnormal = this.alignOnePartition(abnormalPeriod, this.partitionIndex.get(this.abnormalPartitionId), unlabeledPartition, LabelType.Abnormal);
			this.abnormalGroupPeriods.add(alignWithAbnormal);
			
			if (ExplanationSettings.printResult) {
				System.out.println("Aligned with abnormal" + alignWithAbnormal.getPartitionId() + "-" + alignWithAbnormal.getStartTimestamp() + "\t" + alignWithAbnormal.getEndTimestamp());				
			}

			
			PartitionReader alignWithReference = this.alignOnePartition(referencePeriod, this.partitionIndex.get(this.referencePartitionId), unlabeledPartition, LabelType.Reference);
			this.referenceGroupPeriods.add(alignWithReference);
		
			if (ExplanationSettings.printResult) {
				System.out.println("Aligned with reference" + alignWithReference.getPartitionId() + "-" + alignWithReference.getStartTimestamp() + "\t" + alignWithReference.getEndTimestamp());				
			}

		}
	}
	
	public PartitionReader alignOnePartition(PartitionReader alignedPeriod, PartitionReader originalPeriod, PartitionReader unlabeledPartition, LabelType alignLabel) {
		long start = 0L;
		long end = 0L;
		if (this.alignByPoint) {
			long periodStart = alignedPeriod.startTimestamp;//0k
			long periodEnd = alignedPeriod.endTimestamp; //0k
			int startIndexInPartition = originalPeriod.getPartition().indexOfTimestamp(periodStart);
			int endIndexInPartition = originalPeriod.getPartition().indexOfTimestamp(periodEnd);
			int totalPoints = originalPeriod.getCountOfPoints();
			double startPercentage = (double)startIndexInPartition/(double)totalPoints;
			double endPercentage = (double)endIndexInPartition/(double)totalPoints;
			
			//long unlabeledStart = unlabeledPartition.startTimestamp;
			//long unlabeledEnd = unlabeledPartition.endTimestamp;
			double totalPointsForUnlabel = unlabeledPartition.getCountOfPoints();
			int startIndex = (int)(totalPointsForUnlabel * startPercentage);
			int endIndex = (int)(totalPointsForUnlabel * endPercentage);
			start = unlabeledPartition.getPartition().timestampForIndex(startIndex);
			end = unlabeledPartition.getPartition().timestampForIndex(endIndex);
		}else if (this.alignByTime) {
			long periodStart = alignedPeriod.startTimestamp;//0k
			long periodEnd = alignedPeriod.endTimestamp; //0k
			long startRelative = periodStart - originalPeriod.startTimestamp;
			long endRelative= periodEnd - originalPeriod.startTimestamp;
			long partitionLength = originalPeriod.getIntervalLength();
			
			double startPercentage = (double)startRelative/(double)partitionLength;
			double endPercentage = (double)endRelative/(double)partitionLength;
			
			long unlabelPartitionLength = unlabeledPartition.getIntervalLength();
			long startRelativeUnlabeled = (long)(unlabelPartitionLength * startPercentage);
			long endRelativeUnlabeled = (long)(unlabelPartitionLength * endPercentage);
			
			long unlabelStart = unlabeledPartition.startTimestamp;
			start = unlabelStart + startRelativeUnlabeled;
			end = unlabelStart + endRelativeUnlabeled;
		}
		PartitionReader period = new PartitionReader(unlabeledPartition, start, end);
		period.setAlignLabel(alignLabel);
		
		return period;
	}
	
	public void decideAlignment() {
		 
		//compute variance of counts/time length
		double[] counts = new double[this.partitionIndex.size()];
		double[] lengths = new double[this.partitionIndex.size()];
		int count = 0;
		for (PartitionReader reader : this.partitionIndex.values()) {
			counts[count] = reader.getCountOfPoints();
			lengths[count] = reader.getIntervalLength();
			count ++;
		}
		//compute variance
		Variance v = new Variance();
		double countVariance = v.evaluate(counts);
		double lengthVariance = v.evaluate(lengths);
		
		if (ExplanationSettings.printResult) {
			System.out.println("Point variance=" + countVariance);
			System.out.println("Length variance=" + lengthVariance);	
		}
		
		//make decision
		if (countVariance <= lengthVariance) {
			this.alignByPoint = true;
			this.alignByTime = false;
			
			if (ExplanationSettings.printResult) {
				System.out.println("Decision: align by point");
			}
			
		} else {
			this.alignByPoint = false;
			this.alignByTime = true;
			
			if (ExplanationSettings.printResult) {
				System.out.println("Decision: align by time");
			}
			
		}
	}
	
	public void readAllPartitions() throws IOException {
		this.partitionIndex = new HashMap<Long, PartitionReader>();
		//read abnormal partition
		this.readOnePartition(this.abnormalPartitionId);
		this.readOnePartition(this.referencePartitionId);
		for (Long id: this.relatedPartitionIds) {
			this.readOnePartition(id);
		}
	}
	
	public void readOnePartition(Long id) throws IOException {
		if (ExplanationSettings.printResult) {
			System.out.println("Reading visualized metric for partition " + id);			
		}

		if (this.partitionIndex.containsKey(id)) {
			return;
		}
		String filePath = this.partitionFolder + id + ".csv";
		PartitionReader reader = new PartitionReader(filePath, id + "", id);
		this.partitionIndex.put(id, reader);
	}
	
	public void getProperties() throws IOException {
		Properties prop = new Properties();
 		InputStream inputStream = new FileInputStream(this.inputPropertiesFile);
 		prop.load(inputStream);
 
		// get the property value and print it out
 		
		abnormalStart = Long.parseLong(prop.getProperty("abnormalStart"));
		abnormalEnd = Long.parseLong(prop.getProperty("abnormalEnd"));
		referenceStart = Long.parseLong(prop.getProperty("referenceStart"));
		referenceEnd = Long.parseLong(prop.getProperty("referenceEnd"));
		abnormalPartitionId = Long.parseLong(prop.getProperty("currentPartitionId"));
		referencePartitionId= Long.parseLong(prop.getProperty("referencePartitionId"));
		
		if (abnormalPartitionId != referencePartitionId) {
			ExplanationSettings.singlePartitionAnnotated = false;
		} else {
			ExplanationSettings.singlePartitionAnnotated = true;
		}
		
		partitionFolder = prop.getProperty("partitionFolder");
		
		this.parsePartitionIds(prop.getProperty("relatedPartitionIds"));
		
		if (ExplanationSettings.printResult) {
			System.out.println("abnormalPartitionId=\t" + abnormalPartitionId);
			System.out.println("abnormalStart=\t" + abnormalStart);
			System.out.println("abnormalEnd=\t" + abnormalEnd);
			
			System.out.println();
			System.out.println("referencePartitionId=\t" + referencePartitionId);
			System.out.println("referenceStart=\t" + referenceStart);
			System.out.println("referenceEnd=\t" + referenceEnd);
			
			System.out.println();
			System.out.println("partitionFolder=\t" + partitionFolder);
			System.out.println("related partition ids(" + this.relatedPartitionIds.size() + " in total):");
			for (Long id : this.relatedPartitionIds) {
				System.out.println(id);
			}			
		}
	}

	public void parsePartitionIds(String str) {
		StringTokenizer st = new StringTokenizer(str, ";");
		this.relatedPartitionIds = new ArrayList<Long>();
		while(st.hasMoreTokens()) {
			this.relatedPartitionIds.add(Long.parseLong(st.nextToken().trim()));
		}
	}
	
	
	
	
	public String getInputPropertiesFile() {
		return inputPropertiesFile;
	}

	public void setInputPropertiesFile(String inputPropertiesFile) {
		this.inputPropertiesFile = inputPropertiesFile;
	}

	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public TimeSeriesFeatureComparator getAnnotatedTSFeatureComparator() {
		return annotatedTSFeatureComparator;
	}

	public void setAnnotatedTSFeatureComparator(
			TimeSeriesFeatureComparator annotatedTSFeatureComparator) {
		this.annotatedTSFeatureComparator = annotatedTSFeatureComparator;
	}

	public long getTimeWindow() {
		return timeWindow;
	}

	public void setTimeWindow(long timeWindow) {
		this.timeWindow = timeWindow;
	}

	public String getPartitionFolder() {
		return partitionFolder;
	}

	public void setPartitionFolder(String partitionFolder) {
		this.partitionFolder = partitionFolder;
	}

	public long getAbnormalPartitionId() {
		return abnormalPartitionId;
	}

	public void setAbnormalPartitionId(long abnormalPartitionId) {
		this.abnormalPartitionId = abnormalPartitionId;
	}

	public long getAbnormalStart() {
		return abnormalStart;
	}

	public void setAbnormalStart(long abnormalStart) {
		this.abnormalStart = abnormalStart;
	}

	public long getAbnormalEnd() {
		return abnormalEnd;
	}

	public void setAbnormalEnd(long abnormalEnd) {
		this.abnormalEnd = abnormalEnd;
	}

	public long getReferencePartitionId() {
		return referencePartitionId;
	}

	public void setReferencePartitionId(long referencePartitionId) {
		this.referencePartitionId = referencePartitionId;
	}

	public long getReferenceStart() {
		return referenceStart;
	}

	public void setReferenceStart(long referenceStart) {
		this.referenceStart = referenceStart;
	}

	public long getReferenceEnd() {
		return referenceEnd;
	}

	public void setReferenceEnd(long referenceEnd) {
		this.referenceEnd = referenceEnd;
	}

	public ArrayList<Long> getRelatedPartitionIds() {
		return relatedPartitionIds;
	}

	public void setRelatedPartitionIds(ArrayList<Long> relatedPartitionIds) {
		this.relatedPartitionIds = relatedPartitionIds;
	}

	public HashMap<Long, PartitionReader> getPartitionIndex() {
		return partitionIndex;
	}

	public void setPartitionIndex(HashMap<Long, PartitionReader> partitionIndex) {
		this.partitionIndex = partitionIndex;
	}

	public boolean isAlignByPoint() {
		return alignByPoint;
	}

	public void setAlignByPoint(boolean alignByPoint) {
		this.alignByPoint = alignByPoint;
	}

	public boolean isAlignByTime() {
		return alignByTime;
	}

	public void setAlignByTime(boolean alignByTime) {
		this.alignByTime = alignByTime;
	}

	public PartitionReader getAbnormalPeriod() {
		return abnormalPeriod;
	}

	public void setAbnormalPeriod(PartitionReader abnormalPeriod) {
		this.abnormalPeriod = abnormalPeriod;
	}

	public PartitionReader getReferencePeriod() {
		return referencePeriod;
	}

	public void setReferencePeriod(PartitionReader referencePeriod) {
		this.referencePeriod = referencePeriod;
	}

	public ArrayList<PartitionReader> getAbnormalGroupPeriods() {
		return abnormalGroupPeriods;
	}

	public void setAbnormalGroupPeriods(
			ArrayList<PartitionReader> abnormalGroupPeriods) {
		this.abnormalGroupPeriods = abnormalGroupPeriods;
	}

	public ArrayList<PartitionReader> getReferenceGroupPeriods() {
		return referenceGroupPeriods;
	}

	public void setReferenceGroupPeriods(
			ArrayList<PartitionReader> referenceGroupPeriods) {
		this.referenceGroupPeriods = referenceGroupPeriods;
	}

	public ClusterAnalyzer getCluster() {
		return cluster;
	}

	public void setCluster(ClusterAnalyzer cluster) {
		this.cluster = cluster;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public ArrayList<PartitionReader> getLabeledAbnormal() {
		return labeledAbnormal;
	}

	public void setLabeledAbnormal(ArrayList<PartitionReader> labeledAbnormal) {
		this.labeledAbnormal = labeledAbnormal;
	}

	public ArrayList<PartitionReader> getLabeledReference() {
		return labeledReference;
	}

	public void setLabeledReference(ArrayList<PartitionReader> labeledReference) {
		this.labeledReference = labeledReference;
	}

	public FeatureValidationEngine getFeatureValidationEngine() {
		return featureValidationEngine;
	}

	public void setFeatureValidationEngine(
			FeatureValidationEngine featureValidationEngine) {
		this.featureValidationEngine = featureValidationEngine;
	}

	public static void main(String[] args) throws Exception {
		//test
		
		String inputPropertyFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f-cross.properties";
		//ValidationEngine ve = new ValidationEngine(inputPropertyFile);
		//ve.runEngine();
	}

}
