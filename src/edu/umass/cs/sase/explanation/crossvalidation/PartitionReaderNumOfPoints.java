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

import java.util.ArrayList;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.featuregeneration.FeatureType;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;

public class PartitionReaderNumOfPoints {
	
	//generate frequency and mean-value
	public static ArrayList<TimeSeriesFeature> computeWindowFeatures(TimeSeriesFeature tsFeature) {
		
		int windowSize = 30000;//milliseconds
		//initialize features
		
		
		//frequency
		TimeSeriesFeature frequencyFeature = new TimeSeriesFeature(FeatureType.TimeSeriesFrequency, tsFeature.getRawEventType() + "-TimeSeriesFrequency", windowSize, LabelType.Mixed);
		frequencyFeature.setRawEventType(tsFeature.getRawEventType());
		
		
		//mean
		TimeSeriesFeature meanFeature = new TimeSeriesFeature(FeatureType.TimeSeriesMeanValue, tsFeature.getFeatureName() + "-TimeSeriesMean", windowSize, LabelType.Mixed);
		meanFeature.setRawEventType(tsFeature.getRawEventType());
		
		
		
		//sliding window
		long startTime = tsFeature.getTimestamps().get(0);
		long endTime = tsFeature.getTimestamps().get(tsFeature.getTimestamps().size() - 1);
		
		
		long lower = startTime;
		long upper = lower + windowSize;
		int pointer = 0;
		
		int count = 0;
		double sum = 0;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		
		
		while(lower <= endTime) {
			//each sliding window (]
			upper = lower += windowSize;
			//empty window
			if (pointer >= tsFeature.getTimestamps().size() || tsFeature.getTimestamps().get(pointer) > upper) {
				frequencyFeature.addPoint(lower, 0.0);
				
				
				if (ExplanationSettings.addZeroPointForEmptyWindow) {
					Double d = 0.0;
					
					meanFeature.addPoint(lower, d);
				}
				
			} else {
				//moving forward
				while(pointer < tsFeature.getTimestamps().size() && tsFeature.getTimestamps().get(pointer) <= upper) {
					double value = tsFeature.getValues().get(pointer);
					count ++;
					sum += value;
					max = Math.max(max, value);
					min = Math.min(min, value);
					
					pointer ++;
				}
				//add to time series
				
				meanFeature.addPoint(lower, sum /(double) count);
				frequencyFeature.addPoint(lower, (double)count / (windowSize / 1000));
			}
			
			lower = upper;
			count = 0;
			sum = 0;
			max = Double.MIN_VALUE;
			min = Double.MAX_VALUE;
		}
		

		ArrayList<TimeSeriesFeature> result = new ArrayList<TimeSeriesFeature>();
		result.add(frequencyFeature);
		result.add(meanFeature);
		
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
		//b3_2, labeled high memory, wc-frequent users
		String b3_2Path = "/Users/haopeng/Dropbox/research/3rd/experiment/b3-2/b3-2.csv";
		PartitionReader b3_2 = new PartitionReader(b3_2Path, "b3-2");
		long b3_2CutTimestamp= 593711L;
		PartitionReader b3_2First = new PartitionReader(b3_2, b3_2CutTimestamp, true);
		PartitionReader b3_2Second = new PartitionReader(b3_2, b3_2CutTimestamp, false);
		
		ArrayList<TimeSeriesFeature> b3_2FirstFeatures = computeWindowFeatures(b3_2First.getPartition());
		ArrayList<TimeSeriesFeature> b3_2SecondFeatures = computeWindowFeatures(b3_2Second.getPartition());
		
		int totalNumOfPoints = b3_2.getNumOfPoints();
		int abnormalNumOfPoints = b3_2First.getNumOfPoints();
		int referenceNumOfPoints = b3_2Second.getNumOfPoints();
		double abnormalPercent = (double)abnormalNumOfPoints / (double) totalNumOfPoints;
		
		//long entireLength = b3_2.getEntireIntervalLength();
		//long abnormalLength = b3_2First.getEntireIntervalLength();
		//double abnormalPercent = (double)abnormalLength / (double)entireLength;
		
		//b3_3, unlabeled high memory
		String b3_3Path = "/Users/haopeng/Dropbox/research/3rd/experiment/b3-3/b3-3.csv";
		PartitionReader b3_3 = new PartitionReader(b3_3Path, "b3-3");
		PartitionReader b3_3First = new PartitionReader(b3_3, true, 0, abnormalNumOfPoints - 1);
		PartitionReader b3_3Second = new PartitionReader(b3_3, false, abnormalNumOfPoints, totalNumOfPoints - 1);
		
		ArrayList<TimeSeriesFeature> b3_3FirstFeatures = computeWindowFeatures(b3_3First.getPartition());
		ArrayList<TimeSeriesFeature> b3_3SecondFeatures = computeWindowFeatures(b3_3Second.getPartition());
		
		//b0_1, unlabeled reference
		String b0_1Path = "/Users/haopeng/Dropbox/research/3rd/experiment/b0-1/b0-1.csv";
		PartitionReader b0_1 = new PartitionReader(b0_1Path, "b0-1");
		PartitionReader b0_1First = new PartitionReader(b0_1, true, 0, abnormalNumOfPoints - 1);
		PartitionReader b0_1Second = new PartitionReader(b0_1, false,abnormalNumOfPoints, totalNumOfPoints - 1);
		
		ArrayList<TimeSeriesFeature> b0_1Features = computeWindowFeatures(b0_1.getPartition());
		ArrayList<TimeSeriesFeature> b0_1FirstFeatures = computeWindowFeatures(b0_1First.getPartition());
		ArrayList<TimeSeriesFeature> b0_1SecondFeatures = computeWindowFeatures(b0_1Second.getPartition());
		
		//b0_2, unlabeled reference
		String b0_2Path = "/Users/haopeng/Dropbox/research/3rd/experiment/b0-2/b0-2.csv";
		PartitionReader b0_2 = new PartitionReader(b0_2Path, "b0-2");
		PartitionReader b0_2First = new PartitionReader(b0_2, true, 0, abnormalNumOfPoints - 1);
		PartitionReader b0_2Second = new PartitionReader(b0_2, false, abnormalNumOfPoints, totalNumOfPoints - 1);
		
		ArrayList<TimeSeriesFeature> b0_2Features = computeWindowFeatures(b0_2.getPartition());
		ArrayList<TimeSeriesFeature> b0_2FirstFeatures = computeWindowFeatures(b0_2First.getPartition());
		ArrayList<TimeSeriesFeature> b0_2SecondFeatures = computeWindowFeatures(b0_2Second.getPartition());
		
		//c0_1, different job--wc-sessions
		/*
		String c0_1Path = "/Users/haopeng/Dropbox/research/3rd/experiment/c0-1/c0-1.csv";
		long c0_1CutTimestamp = 0L;
		PartitionReader c0_1 = new PartitionReader(c0_1Path, "c0_1");
		PartitionReader c0_1First = new PartitionReader(c0_1, c0_1CutTimestamp, true);
		PartitionReader c0_1Second = new PartitionReader(c0_1, c0_1CutTimestamp, false);
		*/
		//pairs
		
		//unlabeled reference vs unlabeled reference, expected distance: small|| quite similar two partitions
		TimeSeriesFeaturePair b0_1_b_02 = new TimeSeriesFeaturePair(b0_1.getPartition(), b0_2.getPartition(), "b0_1_b_02"); //useless
		
		TimeSeriesFeaturePair b0_1_b_02Frequency = new TimeSeriesFeaturePair(b0_1Features.get(0), b0_2Features.get(0), "b0_1_b_02-frequency"); //useless
		TimeSeriesFeaturePair b0_1_b_02Mean = new TimeSeriesFeaturePair(b0_1Features.get(1), b0_2Features.get(1), "b0_1_b_02-mean"); //useless
		
		
		
		//b3_2 vs b0_1
		TimeSeriesFeaturePair b3_2First_b0_1First = new TimeSeriesFeaturePair(b3_2First.getPartition(), b0_1First.getPartition(), "b3_2First_b0_1First");//expect: large distance
		
		TimeSeriesFeaturePair b3_2First_b0_1FirstFrequency = new TimeSeriesFeaturePair(b3_2FirstFeatures.get(0), b0_1FirstFeatures.get(0), "b3_2First_b0_1First-frequency"); 
		TimeSeriesFeaturePair b3_2First_b0_1FirstMean = new TimeSeriesFeaturePair(b3_2FirstFeatures.get(1), b0_1FirstFeatures.get(1), "b3_2First_b0_1First-mean"); 
		
		
		
		
		TimeSeriesFeaturePair b3_2Second_b0_1Second = new TimeSeriesFeaturePair(b3_2Second.getPartition(), b0_1Second.getPartition(), "b3_2Second_b0_1Second");//expect: small distance
		
		TimeSeriesFeaturePair b3_2Second_b0_1SecondFrequency = new TimeSeriesFeaturePair(b3_2SecondFeatures.get(0), b0_1SecondFeatures.get(0), "b3_2Second_b0_1Second-frequency"); 
		TimeSeriesFeaturePair b3_2Second_b0_1SecondMean = new TimeSeriesFeaturePair(b3_2SecondFeatures.get(1), b0_1SecondFeatures.get(1), "b3_2Second_b0_1Second-mean"); 
		
		//b3_2 vs b0_2
		TimeSeriesFeaturePair b3_2First_b0_2First = new TimeSeriesFeaturePair(b3_2First.getPartition(), b0_2First.getPartition(), "b3_2First_b0_2First");//expect: large distance
		
		TimeSeriesFeaturePair b3_2First_b0_2FirstFrequency = new TimeSeriesFeaturePair(b3_2FirstFeatures.get(0), b0_2FirstFeatures.get(0), "b3_2First_b0_2First-frequency"); 
		TimeSeriesFeaturePair b3_2First_b0_2FirstMean = new TimeSeriesFeaturePair(b3_2FirstFeatures.get(1), b0_2FirstFeatures.get(1), "b3_2First_b0_2First-mean"); 

		
		TimeSeriesFeaturePair b3_2Second_b0_2Second = new TimeSeriesFeaturePair(b3_2Second.getPartition(), b0_2Second.getPartition(), "b3_2Second_b0_2Second");//expect: small distance
		
		TimeSeriesFeaturePair b3_2Second_b0_2SecondFrequency = new TimeSeriesFeaturePair(b3_2SecondFeatures.get(0), b0_2SecondFeatures.get(0), "b3_2Second_b0_2Second-frequency"); 
		TimeSeriesFeaturePair b3_2Second_b0_2SecondMean = new TimeSeriesFeaturePair(b3_2SecondFeatures.get(1), b0_2SecondFeatures.get(1), "b3_2Second_b0_2Second-mean"); 
		
		//b3_2 vs b3_3
		TimeSeriesFeaturePair b3_2First_b3_3First = new TimeSeriesFeaturePair(b3_2First.getPartition(), b3_3First.getPartition(), "b3_2First_b3_3First"); // expect: small distance
		
		TimeSeriesFeaturePair b3_2First_b3_3FirstFrequency = new TimeSeriesFeaturePair(b3_2FirstFeatures.get(0), b3_3FirstFeatures.get(0), "b3_2First_b3_3First-frequency"); 
		TimeSeriesFeaturePair b3_2First_b3_3FirstMean = new TimeSeriesFeaturePair(b3_2FirstFeatures.get(1), b3_3FirstFeatures.get(1), "b3_2First_b3_3First-mean"); 
		
		
		TimeSeriesFeaturePair b3_2Second_b3_3Second = new TimeSeriesFeaturePair(b3_2Second.getPartition(), b3_3Second.getPartition(), "b3_2Second_b3_3Second");//expect: small distance
		
		TimeSeriesFeaturePair b3_2Second_b3_3SecondFrequency = new TimeSeriesFeaturePair(b3_2SecondFeatures.get(0), b3_3SecondFeatures.get(0), "b3_2Second_b3_3Second-frequency"); 
		TimeSeriesFeaturePair b3_2Second_b3_3SecondMean = new TimeSeriesFeaturePair(b3_2SecondFeatures.get(1), b3_3SecondFeatures.get(1), "b3_2Second_b3_3Second-mean"); 
		
		//TimeSeriesFeaturePair b3_2_b3_3 = new TimeSeriesFeaturePair(b3_2.getPartition(), b3_3.getPartition(), "b3_2_b3_3");//compare entire and see
		
		//test distances, existing ones
		System.out.println();
		/*
		System.out.println("~~~~~~~~~~~");
		System.out.println("PairName\t NumOfPoint1\t NumOfPoint2\tFrequency1\tFrequency2\tManhattanDistance\t EuclideanDistance\t DTW\t LCSS\t EDR\t Swale\t ERP\tTquest\tSpade\tEntropy\tEntropyOfSampled");
		
		b0_1_b_02.printDistancesForPartitionClassification();
		
		//b3_2 vs b0_1
		b3_2First_b0_1First.printDistancesForPartitionClassification();
		
		b3_2Second_b0_1Second.printDistancesForPartitionClassification();
		
		//b3_2 vs b0_2
		b3_2First_b0_2First.printDistancesForPartitionClassification();		
		b3_2Second_b0_2Second.printDistancesForPartitionClassification();
		
		//b3_2 vs b3_3
		b3_2First_b3_3First.printDistancesForPartitionClassification();
		b3_2Second_b3_3Second.printDistancesForPartitionClassification();
		//b3_2_b3_3.printDistancesForPartitionClassification();
		*/
		
		//Entropy distance for windowed frequency and mean-values
		
		System.out.println();
		System.out.println();
		System.out.println("PairName\tFrequencyEntropyDistance\tMean-valueEntropyDistance");
		
		b0_1_b_02Frequency.computeEntropyDistance();
		b0_1_b_02Mean.computeEntropyDistance();
		System.out.println(b0_1_b_02Frequency.getFeatureName() + "\t" + b0_1_b_02Frequency.getEntropyDistance() + "\t" + b0_1_b_02Mean.getEntropyDistance());
		
		//b3_2 vs b0_1
		b3_2First_b0_1FirstFrequency.computeEntropyDistance();
		b3_2First_b0_1FirstMean.computeEntropyDistance();
		System.out.println(b3_2First_b0_1FirstFrequency.getFeatureName() + "\t" + b3_2First_b0_1FirstFrequency.getEntropyDistance() + "\t" + b3_2First_b0_1FirstMean.getEntropyDistance());
		
		b3_2Second_b0_1SecondFrequency.computeEntropyDistance();
		b3_2Second_b0_1SecondMean.computeEntropyDistance();
		System.out.println(b3_2Second_b0_1SecondFrequency.getFeatureName() + "\t" + b3_2Second_b0_1SecondFrequency.getEntropyDistance() + "\t" + b3_2Second_b0_1SecondMean.getEntropyDistance());
		
		//b3_2 vs b0_2
		b3_2First_b0_2FirstFrequency.computeEntropyDistance();
		b3_2First_b0_2FirstMean.computeEntropyDistance();
		System.out.println(b3_2First_b0_2FirstFrequency.getFeatureName() + "\t" + b3_2First_b0_2FirstFrequency.getEntropyDistance() + "\t" + b3_2First_b0_2FirstMean.getEntropyDistance());
		
		b3_2Second_b0_2SecondFrequency.computeEntropyDistance();
		b3_2Second_b0_2SecondMean.computeEntropyDistance();
		System.out.println(b3_2Second_b0_2SecondFrequency.getFeatureName() + "\t" + b3_2Second_b0_2SecondFrequency.getEntropyDistance() + "\t" + b3_2Second_b0_2SecondMean.getEntropyDistance());

		//b3_2 vs b3_3
		b3_2First_b3_3FirstFrequency.computeEntropyDistance();
		b3_2First_b3_3FirstMean.computeEntropyDistance();
		System.out.println(b3_2First_b3_3FirstFrequency.getFeatureName() + "\t" + b3_2First_b3_3FirstFrequency.getEntropyDistance() + "\t" + b3_2First_b3_3FirstMean.getEntropyDistance());
		
		b3_2Second_b3_3SecondFrequency.computeEntropyDistance();
		b3_2Second_b3_3SecondMean.computeEntropyDistance();
		System.out.println(b3_2Second_b3_3SecondFrequency.getFeatureName() + "\t" + b3_2Second_b3_3SecondFrequency.getEntropyDistance() + "\t" + b3_2Second_b3_3SecondMean.getEntropyDistance());
		
	}
}
