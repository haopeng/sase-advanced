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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.evaluation.DataPoint;
import edu.umass.cs.sase.explanation.evaluation.EvaluationManager;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;

public class TimeSeriesFeatureComparator {
	
	ArrayList<TimeSeriesFeature> abnormalTimeSeriesRawFeatures;
	ArrayList<TimeSeriesFeature> referenceTimeSeriesRawFeatures;
	
	ArrayList<TimeSeriesFeature> abnormalTimeSeriesAggFeatures;
	ArrayList<TimeSeriesFeature> referenceTimeSeriesAggFeatures;
	
	ArrayList<TimeSeriesFeaturePair> rawFeaturePairs;
	ArrayList<TimeSeriesFeaturePair> aggFeaturePairs;
	
	ArrayList<TimeSeriesFeaturePair> rawAndAggFeaturePairs;
	
	
	
	String outputFilePath;
	//PrintWriter out;
	
	
	HashMap<String, TimeSeriesFeature> abnormalIndex;
	HashMap<String, TimeSeriesFeature> referenceIndex;
	public TimeSeriesFeatureComparator(ArrayList<TimeSeriesFeature> aTSRaw, ArrayList<TimeSeriesFeature> rTSRaw, ArrayList<TimeSeriesFeature> aTSA, ArrayList<TimeSeriesFeature> rTSA) {
		this.abnormalTimeSeriesRawFeatures = aTSRaw;
		this.referenceTimeSeriesRawFeatures = rTSRaw;
		this.abnormalTimeSeriesAggFeatures = aTSA;
		this.referenceTimeSeriesAggFeatures = rTSA;
		
		this.outputFilePath = outputFilePath;
		
		
		this.rawAndAggFeaturePairs = new ArrayList<TimeSeriesFeaturePair>();
		this.rawFeaturePairs = new ArrayList<TimeSeriesFeaturePair>();
		for (int i = 0; i < this.abnormalTimeSeriesRawFeatures.size(); i ++) {
			TimeSeriesFeaturePair pair = new TimeSeriesFeaturePair(this.abnormalTimeSeriesRawFeatures.get(i), this.referenceTimeSeriesRawFeatures.get(i));
			this.rawFeaturePairs.add(pair);
			this.rawAndAggFeaturePairs.add(pair);
		}
		
		
		this.aggFeaturePairs = new ArrayList<TimeSeriesFeaturePair>();
		for (int i = 0; i < this.abnormalTimeSeriesAggFeatures.size(); i ++) {
			TimeSeriesFeaturePair pair = new TimeSeriesFeaturePair(this.abnormalTimeSeriesAggFeatures.get(i), this.referenceTimeSeriesAggFeatures.get(i));
			this.aggFeaturePairs.add(pair);
			this.rawAndAggFeaturePairs.add(pair);
		}
		
	}
	
	public int getAbnormalAggPointCount () {
		return this.abnormalTimeSeriesAggFeatures.get(0).getCount();
	}
	
	public int getReferenceAggPointCount() {
		return this.referenceTimeSeriesAggFeatures.get(0).getCount();
	}
	
	
	public ArrayList<DataPoint> generateDataPoints() {
		ArrayList<DataPoint> pointList = new ArrayList<DataPoint>();
		
		ArrayList<DataPoint> abnormalPoints = this.convertFeatureListToDataPoints(this.abnormalTimeSeriesAggFeatures, LabelType.Abnormal);
		pointList.addAll(abnormalPoints);
		
		ArrayList<DataPoint> referencePoints = this.convertFeatureListToDataPoints(this.referenceTimeSeriesAggFeatures, LabelType.Reference);
		pointList.addAll(referencePoints);
		
		return pointList;
	}
	
	public ArrayList<DataPoint> generateDataPointsWithRawFeatures() {
		ArrayList<DataPoint> pointList = new ArrayList<DataPoint>();
		
		ArrayList<DataPoint> abnormalPoints = this.convertFeatureListToDataPoints(this.abnormalTimeSeriesRawFeatures, LabelType.Abnormal);
		pointList.addAll(abnormalPoints);
		
		ArrayList<DataPoint> referencePoints = this.convertFeatureListToDataPoints(this.referenceTimeSeriesRawFeatures, LabelType.Reference);
		pointList.addAll(referencePoints);
		
		return pointList;
	}
	
	/**
	 * 
	 * @param fold
	 * @param index
	 * @return
	 */
	public ArrayList<DataPoint> sampleDataPoints(int fold, int index) {
		ArrayList<DataPoint> pointList = new ArrayList<DataPoint>();
		
		ArrayList<DataPoint> abnormalPoints = this.sampleTestDataPoints(this.abnormalTimeSeriesAggFeatures, LabelType.Abnormal, fold, index);
		pointList.addAll(abnormalPoints);
		
		ArrayList<DataPoint> referencePoints = this.sampleTestDataPoints(this.referenceTimeSeriesAggFeatures, LabelType.Reference, fold, index);
		pointList.addAll(referencePoints);
		
		return pointList;
	}
	
	public ArrayList<DataPoint> sampleDataPoints(int fold, int index, int[] shuffleForAbnormal, int[] shuffleForReference) {
		
		ArrayList<DataPoint> pointList = new ArrayList<DataPoint>();
		
		ArrayList<DataPoint> abnormalPoints = this.sampleTestDataPoints(this.abnormalTimeSeriesAggFeatures, LabelType.Abnormal, fold, index, shuffleForAbnormal);
		pointList.addAll(abnormalPoints);
		
		ArrayList<DataPoint> referencePoints = this.sampleTestDataPoints(this.referenceTimeSeriesAggFeatures, LabelType.Reference, fold, index, shuffleForReference);
		pointList.addAll(referencePoints);
		
		return pointList;
	}
	
	public ArrayList<DataPoint> sampleTestDataPoints(ArrayList<TimeSeriesFeature> featureList, LabelType trueLabel, int fold, int index, int[] shuffle) {
		ArrayList<DataPoint> pointList = new ArrayList<DataPoint>();
		int count = featureList.get(0).getCount();
		int size = count / fold;
		int startIndex = size * index;
		int endIndex = size * (index + 1) - 1;
		ArrayList<Integer> toDeleteIndex = new ArrayList<Integer>();
		
		for (int i = startIndex; i <= endIndex; i ++) {
			int position = shuffle[i];
			toDeleteIndex.add(position);
			long timestamp = featureList.get(0).getTimeStampAtIndex(position);
			HashMap<String, Double> values = new HashMap<String, Double>();
			for (TimeSeriesFeature f: featureList) {
				values.put(f.getFeatureName(), f.getValueAtIndex(position));
			}
			DataPoint point = new DataPoint(timestamp, trueLabel, values);
			pointList.add(point);
		}
		
		//delete those points from features
		
		for (TimeSeriesFeature f: featureList) {
			//delete points in a range(including)
			f.deletePointsInList(toDeleteIndex);
		}
		
		
		return pointList;
	}
	
	
	/**
	 * Sample points from agg features
	 * Those points will be removed from agg features for training
	 * @param featureList
	 * @param trueLabel
	 * @param fold: how many folds are used in the cross validation
	 * @param index: which index is this in the fold
	 * @return
	 */
	public ArrayList<DataPoint> sampleTestDataPoints(ArrayList<TimeSeriesFeature> featureList, LabelType trueLabel, int fold, int index) {
		ArrayList<DataPoint> pointList = new ArrayList<DataPoint>();
		int count = featureList.get(0).getCount();
		int size = count / fold;
		int startIndex = size * index;
		int endIndex = size * (index + 1) - 1;
		
		for (int i = startIndex; i <= endIndex; i ++) {
			long timestamp = featureList.get(0).getTimeStampAtIndex(i);
			HashMap<String, Double> values = new HashMap<String, Double>();
			for (TimeSeriesFeature f: featureList) {
				values.put(f.getFeatureName(), f.getValueAtIndex(i));
			}
			DataPoint point = new DataPoint(timestamp, trueLabel, values);
			pointList.add(point);
		}
		
		//delete those points from features
		
		for (TimeSeriesFeature f: featureList) {
			//delete points in a range(including)
			f.deletePointsInRange(startIndex, endIndex);
		}
		
		
		return pointList;
	}
	
	
	public ArrayList<DataPoint> convertFeatureListToDataPoints(ArrayList<TimeSeriesFeature> featureList, LabelType trueLabel) {
		ArrayList<DataPoint> pointList = new ArrayList<DataPoint>();
		//assume all features in the list has the same number of values
		int count = featureList.get(0).getCount();
		
		for (int i = 0; i < count; i ++) {
			long timestamp = featureList.get(0).getTimeStampAtIndex(i);
			HashMap<String, Double> values = new HashMap<String, Double>();
			for (TimeSeriesFeature f: featureList) {
				if (f.getCount() > i) {
					values.put(f.getFeatureName(), f.getValueAtIndex(i));					
				} else {
					values.put(f.getFeatureName(), 0.0);
				}

			}
			DataPoint point = new DataPoint(timestamp, trueLabel, values);
			pointList.add(point);
		}
		
		return pointList;
	}
	
	
	public void assignClassifiedLabel(LabelType firstLabel, LabelType secondLabel) {
		for (TimeSeriesFeature f: this.abnormalTimeSeriesRawFeatures) {
			f.setClassifiedLabel(firstLabel);
			f.setLabel(LabelType.Abnormal);//hard code, for this use case
		}
		for (TimeSeriesFeature f: this.abnormalTimeSeriesAggFeatures) {
			f.setClassifiedLabel(firstLabel);
			f.setLabel(LabelType.Abnormal);//hard code, for this use case
		}
		
		for (TimeSeriesFeature f: this.referenceTimeSeriesRawFeatures) {
			f.setClassifiedLabel(secondLabel);
			f.setLabel(LabelType.Reference);
		}
		for (TimeSeriesFeature f: this.referenceTimeSeriesAggFeatures) {
			f.setClassifiedLabel(secondLabel);
			f.setLabel(LabelType.Reference);
		}
	}
	/**
	 * 
	 * @return The sroted agg features
	 */
	public ArrayList<TimeSeriesFeaturePair> returnAggFeatureListRanked() {
		ArrayList<TimeSeriesFeaturePair> sortedList = new ArrayList<TimeSeriesFeaturePair>();
		
		sortedList.addAll(this.aggFeaturePairs);
		
		//sort by decreasing order of recent distance
		
		Collections.sort(sortedList, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getRecentDistance();
				double v2 = p2.getRecentDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				return 0;
			}
		});
		
		return sortedList;
	}
	
	public ArrayList<TimeSeriesFeaturePair> returnRawFeatureListRanked() {
		ArrayList<TimeSeriesFeaturePair> sortedList = new ArrayList<TimeSeriesFeaturePair>();
		
		sortedList.addAll(this.rawFeaturePairs);
		
		//sort by decreasing order of recent distance
		
		Collections.sort(sortedList, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getRecentDistance();
				double v2 = p2.getRecentDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				return 0;
			}
		});
		
		return sortedList;
	}
	/**
	 * @return the sorted list of all time series features, ranked by the recent computed distance
	 */
	public ArrayList<TimeSeriesFeaturePair> returnAllTimeSeriesFeatureListRanked() {
		ArrayList<TimeSeriesFeaturePair> sortedList = new ArrayList<TimeSeriesFeaturePair>();
		
		sortedList.addAll(this.rawFeaturePairs);
		sortedList.addAll(this.aggFeaturePairs);
		
		//sort by decreasing order of recent distance
		
		Collections.sort(sortedList, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getRecentDistance();
				double v2 = p2.getRecentDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				return 0;
			}
		});
		
		return sortedList;
	}
	
	/**
	 * @return the sorted list of all time series features, ranked by the recent computed distance
	 */
	public ArrayList<TimeSeriesFeaturePair> returnAggTimeSeriesFeatureListRanked() {
		ArrayList<TimeSeriesFeaturePair> sortedList = new ArrayList<TimeSeriesFeaturePair>();
		
		//sortedList.addAll(this.rawFeaturePairs);
		sortedList.addAll(this.aggFeaturePairs);
		
		//sort by decreasing order of recent distance
		
		Collections.sort(sortedList, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getRecentDistance();
				double v2 = p2.getRecentDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				return 0;
			}
		});
		
		return sortedList;
	}
	
	/*
	public void getWriter() throws IOException {
		if (this.out == null) {
			//this.out= ExplanationSettings.out;//append
		}

		//return this.out;
	}
	*/
	
	public void computeEntropyDistance(int top) throws Exception {
		//this.getWriter();
		
		if (!ExplanationSettings.aggFeatureOnly) {
			if (ExplanationSettings.printResult) {
				System.out.println("~~~~~~~~~~~EntropyDistance for raw features~~~~~~~~");	
			}
			
			//out.println("~~~~~~~~~~~EntropyDistance for raw features~~~~~~~~");
			this.computeEntropyDistanceForPairs(top, this.rawFeaturePairs);
		}
		
		if (ExplanationSettings.printResult) {
			System.out.println("~~~~~~~~~~~EntropyDistance for agg features~~~~~~~~~");	
		}
		
		//out.println("~~~~~~~~~~~EntropyDistance for raw features~~~~~~~~");
		this.computeEntropyDistanceForPairs(top, this.aggFeaturePairs);
		
		//out.flush();
	}
	
	public void computeEntropyDistanceForRawAndAgg(int top) throws Exception {
		System.out.println("~~~~~~~~~~~EntropyDistance for raw and agg features~~~~~~~~");
		this.computeEntropyDistanceForPairs(top, this.rawAndAggFeaturePairs);
		
	}
	//public void computeE
	
	public void computeEntropyDistanceForPairs(int top, ArrayList<TimeSeriesFeaturePair> pairs) throws Exception {
		for (TimeSeriesFeaturePair pair: pairs) {
			pair.computeEntropyDistance();
		}
		
		Collections.sort(pairs, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getEntropyDistance();
				double v2 = p2.getEntropyDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				return 0;
			}
		});
		
		if (ExplanationSettings.printResult) {
			System.out.println("=====Top " + top + " entropy distances=====");
			//out.println("=====Top " + top + " entropy distances=====");
			for (int i = 0; i <= top && i < pairs.size(); i ++) {
				System.out.println(i + ":\t" + pairs.get(i).getEntropyDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
				//out.println(i + ":\t" + pairs.get(i).getEntropyDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
			}
		}
	}
	
	
	public void computeEuclideanDistance(int top) throws Exception {
		//this.getWriter();
		if (!ExplanationSettings.aggFeatureOnly) {
			System.out.println("~~~~~~~~~~~EuclideanDistance for raw features~~~~~~~~");
			//out.println("~~~~~~~~~~~EuclideanDistance for raw features~~~~~~~~");
			this.computeEuclideanDistanceForPairs(top, this.rawFeaturePairs);
		}
		
		
		System.out.println("~~~~~~~~~~~EuclideanDistance for agg features~~~~~~~~~");
		//out.println("~~~~~~~~~~~EuclideanDistance for agg features~~~~~~~~~");
		this.computeEuclideanDistanceForPairs(top, this.aggFeaturePairs);
		
		//out.flush();
	}

	public void computeEuclideanDistanceForPairs(int top, ArrayList<TimeSeriesFeaturePair> pairs) throws IOException {
		for (TimeSeriesFeaturePair pair: pairs) {
			pair.computeEuclideanDistance();
		}
		
		Collections.sort(pairs, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getEuclideanDistance();
				double v2 = p2.getEuclideanDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				
				return 0;
			}
		});
		
		//this.getWriter();
		
		System.out.println("=====Top " + top + " euclidean distances=====");
		//out.println("=====Top " + top + " euclidean distances=====");
		for (int i = 0; i <= top && i < pairs.size(); i ++) {
			System.out.println(i + ":\t" + pairs.get(i).getEuclideanDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
			//out.println(i + ":\t" + pairs.get(i).getEuclideanDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
		}
		
		//out.flush();
	}
	
	
	public void computeManhattanDistance(int top) throws Exception {
		//this.getWriter();
		if (!ExplanationSettings.aggFeatureOnly) {
			System.out.println("~~~~~~~~~~~ManhattanDistance for raw features~~~~~~~~");
			//out.println("~~~~~~~~~~~ManhattanDistance for raw features~~~~~~~~");
			this.computeManhattanDistanceForPairs(top, this.rawFeaturePairs);
			
		}
		
		System.out.println("~~~~~~~~~~~ManhattanDistance for agg features~~~~~~~~~");
		//out.println("~~~~~~~~~~~ManhattanDistance for agg features~~~~~~~~~");
		this.computeManhattanDistanceForPairs(top, this.aggFeaturePairs);
		
		//out.flush();
	}

	
	public void computeManhattanDistanceForPairs(int top, ArrayList<TimeSeriesFeaturePair> pairs) throws IOException {
		for (TimeSeriesFeaturePair pair: pairs) {
			pair.computeManhattanDistance();
		}
		
		Collections.sort(pairs, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getManhattanDistance();
				double v2 = p2.getManhattanDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				
				return 0;
			}
		});
		
		//this.getWriter();
		
		System.out.println("=====Top " + top + " manhattan distances=====");
		//out.println("=====Top " + top + " manhattan distances=====");
		for (int i = 0; i <= top && i < pairs.size(); i ++) {
			System.out.println(i + ":\t" + pairs.get(i).getManhattanDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
			//out.println(i + ":\t" + pairs.get(i).getManhattanDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
		}
		
		//out.flush();
	}
	
	
	
	public void computeDTWDistance(int top) throws Exception {
		//this.getWriter();
		if (!ExplanationSettings.aggFeatureOnly) {
			System.out.println("~~~~~~~~~~~DTWDistance for raw features~~~~~~~~");
			//out.println("~~~~~~~~~~~DTWDistance for raw features~~~~~~~~");
			this.computeDTWDistanceForPairs(top, this.rawFeaturePairs);

		}
		
		System.out.println("~~~~~~~~~~~DTWDistance for agg features~~~~~~~~~");
		//out.println("~~~~~~~~~~~DTWDistance for agg features~~~~~~~~~");
		this.computeDTWDistanceForPairs(top, this.aggFeaturePairs);
		
		//out.flush();
	}

	
	public void computeDTWDistanceForPairs(int top, ArrayList<TimeSeriesFeaturePair> pairs) throws IOException {
		for (TimeSeriesFeaturePair pair: pairs) {
			pair.computeDtwDistance();
		}
		
		Collections.sort(pairs, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getDtwDistance();
				double v2 = p2.getDtwDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				
				
				return 0;
			}
		});
		
		//this.getWriter();
		
		System.out.println("=====Top " + top + " dtw distances=====");
		//out.println("=====Top " + top + " dtw distances=====");
		for (int i = 0; i <= top && i < pairs.size(); i ++) {
			System.out.println(i + ":\t" + pairs.get(i).getDtwDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
			//out.println(i + ":\t" + pairs.get(i).getDtwDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
		}
		
		//out.flush();
	}
	
	public void computeLCSSDistance(int top) throws Exception {
		//this.getWriter();
		if (!ExplanationSettings.aggFeatureOnly) {
			System.out.println("~~~~~~~~~~~LCSSDistance for raw features~~~~~~~~");
			//out.println("~~~~~~~~~~~LCSSDistance for raw features~~~~~~~~");
			this.computeLCSSDistanceForPairs(top, this.rawFeaturePairs);

		}
		
		System.out.println("~~~~~~~~~~~LCSSDistance for agg features~~~~~~~~~");
		//out.println("~~~~~~~~~~~LCSSDistance for agg features~~~~~~~~~");
		this.computeLCSSDistanceForPairs(top, this.aggFeaturePairs);
		
		//out.flush();
	}

	
	public void computeLCSSDistanceForPairs(int top, ArrayList<TimeSeriesFeaturePair> pairs) throws Exception {
		for (TimeSeriesFeaturePair pair: pairs) {
			pair.computeLCSSDistance();
		}
		
		Collections.sort(pairs, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getLcssDistance();
				double v2 = p2.getLcssDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				
				
				return 0;
			}
		});
		
		//this.getWriter();
		
		System.out.println("=====Top " + top + " LCSS distances=====");
		//out.println("=====Top " + top + " LCSS distances=====");
		for (int i = 0; i <= top && i < pairs.size(); i ++) {
			System.out.println(i + ":\t" + pairs.get(i).getLcssDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
			//out.println(i + ":\t" + pairs.get(i).getLcssDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
		}
		
		//out.flush();
	}
	
	public void computeEDRDistance(int top) throws Exception {
		//this.getWriter();
		if (!ExplanationSettings.aggFeatureOnly) {
			System.out.println("~~~~~~~~~~~EDRDistance for raw features~~~~~~~~");
			//out.println("~~~~~~~~~~~EDRDistance for raw features~~~~~~~~");
			this.computeEDRDistanceForPairs(top, this.rawFeaturePairs);

		}
		
		
		System.out.println("~~~~~~~~~~~EDRDistance for agg features~~~~~~~~~");
		//out.println("~~~~~~~~~~~EDRDistance for agg features~~~~~~~~~");
		this.computeEDRDistanceForPairs(top, this.aggFeaturePairs);
		
		//out.flush();
	}

	
	public void computeEDRDistanceForPairs(int top, ArrayList<TimeSeriesFeaturePair> pairs) throws Exception {
		for (TimeSeriesFeaturePair pair: pairs) {
			pair.computeEDRDistance();
		}
		
		Collections.sort(pairs, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getEdrDistance();
				double v2 = p2.getEdrDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
	
				return 0;
			}
		});
		
		//this.getWriter();
		
		System.out.println("=====Top " + top + " EDR distances=====");
		//out.println("=====Top " + top + " EDR distances=====");
		for (int i = 0; i <= top && i < pairs.size(); i ++) {
			System.out.println(i + ":\t" + pairs.get(i).getEdrDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
			//out.println(i + ":\t" + pairs.get(i).getEdrDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
		}
		
		//out.flush();
	}
	
	
	public void computeSwaleDistance(int top) throws Exception {
		//this.getWriter();
		if (!ExplanationSettings.aggFeatureOnly) {
			System.out.println("~~~~~~~~~~~SwaleDistance for raw features~~~~~~~~");
			//out.println("~~~~~~~~~~~SwaleDistance for raw features~~~~~~~~");
			this.computeSwaleDistanceForPairs(top, this.rawFeaturePairs);
		}
		
		System.out.println("~~~~~~~~~~~SwaleDistance for agg features~~~~~~~~~");
		//out.println("~~~~~~~~~~~SwaleDistance for agg features~~~~~~~~~");
		this.computeSwaleDistanceForPairs(top, this.aggFeaturePairs);
		
		//out.flush();
	}

	
	public void computeSwaleDistanceForPairs(int top, ArrayList<TimeSeriesFeaturePair> pairs) throws Exception {
		for (TimeSeriesFeaturePair pair: pairs) {
			pair.computeSwaleDistance();
		}
		
		Collections.sort(pairs, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getSwaleDistance();
				double v2 = p2.getSwaleDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
	
				return 0;
			}
		});
		
		//this.getWriter();
		
		System.out.println("=====Top " + top + " Swale distances=====");
		//out.println("=====Top " + top + " Swale distances=====");
		for (int i = 0; i <= top && i < pairs.size(); i ++) {
			System.out.println(i + ":\t" + pairs.get(i).getSwaleDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
			//out.println(i + ":\t" + pairs.get(i).getSwaleDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
		}
		
		//out.flush();
	}

	public void computeERPDistance(int top) throws Exception {
		//this.getWriter();
		if (!ExplanationSettings.aggFeatureOnly) {
			System.out.println("~~~~~~~~~~~ERPDistance for raw features~~~~~~~~");
			//out.println("~~~~~~~~~~~ERPDistance for raw features~~~~~~~~");
			this.computeERPDistanceForPairs(top, this.rawFeaturePairs);
		}
		
		System.out.println("~~~~~~~~~~~ERPDistance for agg features~~~~~~~~~");
		//out.println("~~~~~~~~~~~ERPDistance for agg features~~~~~~~~~");
		this.computeERPDistanceForPairs(top, this.aggFeaturePairs);
		
		//out.flush();
	}

	public void computeERPDistanceForPairs(int top, ArrayList<TimeSeriesFeaturePair> pairs) throws Exception {
		for (TimeSeriesFeaturePair pair: pairs) {
			pair.computeERPDistance();
		}
		
		Collections.sort(pairs, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getErpDistance();
				double v2 = p2.getErpDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
	
				return 0;
			}
		});
		
		//this.getWriter();
		
		System.out.println("=====Top " + top + " ERP distances=====");
		//out.println("=====Top " + top + " ERP distances=====");
		for (int i = 0; i <= top && i < pairs.size(); i ++) {
			System.out.println(i + ":\t" + pairs.get(i).getErpDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
			//out.println(i + ":\t" + pairs.get(i).getErpDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
		}
		
		//out.flush();
	}
	
	public void computeTQuESTDistance(int top) throws Exception {
		//this.getWriter();
		if (!ExplanationSettings.aggFeatureOnly) {
			System.out.println("~~~~~~~~~~~TQuESTDistance for raw features~~~~~~~~");
			//out.println("~~~~~~~~~~~TQuESTDistance for raw features~~~~~~~~");
			this.computeTQuESTDistanceForPairs(top, this.rawFeaturePairs);

		}
		
		System.out.println("~~~~~~~~~~~TQuESTDistance for agg features~~~~~~~~~");
		//out.println("~~~~~~~~~~~TQuESTDistance for agg features~~~~~~~~~");
		this.computeTQuESTDistanceForPairs(top, this.aggFeaturePairs);
		
		//out.flush();
	}

	public void computeTQuESTDistanceForPairs(int top, ArrayList<TimeSeriesFeaturePair> pairs) throws Exception {
		for (TimeSeriesFeaturePair pair: pairs) {
			pair.computeTquestDistance();
		}
		
		Collections.sort(pairs, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getTquestDistance();
				double v2 = p2.getTquestDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
	
				return 0;
			}
		});
		
		//this.getWriter();
		
		System.out.println("=====Top " + top + " TQuEST distances=====");
		//out.println("=====Top " + top + " TQuEST distances=====");
		for (int i = 0; i <= top && i < pairs.size(); i ++) {
			System.out.println(i + ":\t" + pairs.get(i).getTquestDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
			//out.println(i + ":\t" + pairs.get(i).getTquestDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
		}
		
		//out.flush();
	}
	
	
	public void computeSpADeDistance(int top) throws Exception {
		//this.getWriter();
		if (!ExplanationSettings.aggFeatureOnly) {
			System.out.println("~~~~~~~~~~~SpADeDistance for raw features~~~~~~~~");
			//out.println("~~~~~~~~~~~SpADeDistance for raw features~~~~~~~~");
			this.computeSpADeDistanceForPairs(top, this.rawFeaturePairs);

		}
		
		System.out.println("~~~~~~~~~~~SpADeDistance for agg features~~~~~~~~~");
		//out.println("~~~~~~~~~~~SpADeDistance for agg features~~~~~~~~~");
		this.computeSpADeDistanceForPairs(top, this.aggFeaturePairs);
		
		//out.flush();
	}

	public void computeSpADeDistanceForPairs(int top, ArrayList<TimeSeriesFeaturePair> pairs) throws Exception {
		for (TimeSeriesFeaturePair pair: pairs) {
			pair.computeSpadeDistance();
		}
		
		Collections.sort(pairs, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getSpadeDistance();
				double v2 = p2.getSpadeDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
	
				return 0;
			}
		});
		
		//this.getWriter();
		
		System.out.println("=====Top " + top + " SpADe distances=====");
		//out.println("=====Top " + top + " SpADe distances=====");
		for (int i = 0; i <= top && i < pairs.size(); i ++) {
			System.out.println(i + ":\t" + pairs.get(i).getSpadeDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
			//out.println(i + ":\t" + pairs.get(i).getSpadeDistance() + "\t" + pairs.get(i).getTsFeature1().getFeatureName());
		}
		
		//out.flush();
	}

	public ArrayList<TimeSeriesFeature> getAbnormalTimeSeriesRawFeatures() {
		return abnormalTimeSeriesRawFeatures;
	}

	public void setAbnormalTimeSeriesRawFeatures(
			ArrayList<TimeSeriesFeature> abnormalTimeSeriesRawFeatures) {
		this.abnormalTimeSeriesRawFeatures = abnormalTimeSeriesRawFeatures;
	}

	public ArrayList<TimeSeriesFeature> getReferenceTimeSeriesRawFeatures() {
		return referenceTimeSeriesRawFeatures;
	}

	public void setReferenceTimeSeriesRawFeatures(
			ArrayList<TimeSeriesFeature> referenceTimeSeriesRawFeatures) {
		this.referenceTimeSeriesRawFeatures = referenceTimeSeriesRawFeatures;
	}

	public ArrayList<TimeSeriesFeature> getAbnormalTimeSeriesAggFeatures() {
		return abnormalTimeSeriesAggFeatures;
	}

	public void setAbnormalTimeSeriesAggFeatures(
			ArrayList<TimeSeriesFeature> abnormalTimeSeriesAggFeatures) {
		this.abnormalTimeSeriesAggFeatures = abnormalTimeSeriesAggFeatures;
	}

	public ArrayList<TimeSeriesFeature> getReferenceTimeSeriesAggFeatures() {
		return referenceTimeSeriesAggFeatures;
	}

	public void setReferenceTimeSeriesAggFeatures(
			ArrayList<TimeSeriesFeature> referenceTimeSeriesAggFeatures) {
		this.referenceTimeSeriesAggFeatures = referenceTimeSeriesAggFeatures;
	}

	public ArrayList<TimeSeriesFeaturePair> getRawFeaturePairs() {
		return rawFeaturePairs;
	}

	public void setRawFeaturePairs(ArrayList<TimeSeriesFeaturePair> rawFeaturePairs) {
		this.rawFeaturePairs = rawFeaturePairs;
	}

	public ArrayList<TimeSeriesFeaturePair> getAggFeaturePairs() {
		return aggFeaturePairs;
	}

	public void setAggFeaturePairs(ArrayList<TimeSeriesFeaturePair> aggFeaturePairs) {
		this.aggFeaturePairs = aggFeaturePairs;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}




	public void indexFeatures() {
		this.abnormalIndex = new HashMap<String, TimeSeriesFeature>();
		for (TimeSeriesFeature f: this.abnormalTimeSeriesRawFeatures) {
			abnormalIndex.put(f.getFeatureName(), f);
		}
		for (TimeSeriesFeature f: this.abnormalTimeSeriesAggFeatures) {
			abnormalIndex.put(f.getFeatureName(), f);
		}
		
		this.referenceIndex = new HashMap<String, TimeSeriesFeature>();
		for (TimeSeriesFeature f: this.referenceTimeSeriesRawFeatures) {
			this.referenceIndex.put(f.getFeatureName(), f);
		}
		for (TimeSeriesFeature f: this.referenceTimeSeriesAggFeatures) {
			this.referenceIndex.put(f.getFeatureName(), f);
		}
	}
	
	public TimeSeriesFeature getAbnormalFeature(String featureName){
		return this.abnormalIndex.get(featureName);
	}
	
	public TimeSeriesFeature getReferenceFeature(String featureName){
		return this.referenceIndex.get(featureName);
	}
	
	
	public ArrayList<TimeSeriesFeaturePair> getRawAndAggFeaturePairs() {
		return rawAndAggFeaturePairs;
	}



	public void setRawAndAggFeaturePairs(
			ArrayList<TimeSeriesFeaturePair> rawAndAggFeaturePairs) {
		this.rawAndAggFeaturePairs = rawAndAggFeaturePairs;
	}



	public HashMap<String, TimeSeriesFeature> getAbnormalIndex() {
		return abnormalIndex;
	}



	public void setAbnormalIndex(HashMap<String, TimeSeriesFeature> abnormalIndex) {
		this.abnormalIndex = abnormalIndex;
	}



	public HashMap<String, TimeSeriesFeature> getReferenceIndex() {
		return referenceIndex;
	}



	public void setReferenceIndex(HashMap<String, TimeSeriesFeature> referenceIndex) {
		this.referenceIndex = referenceIndex;
	}



	public void buildIntervals() {
		for (TimeSeriesFeaturePair p: this.rawFeaturePairs) {
			p.buildIntervals();
		}
		for (TimeSeriesFeaturePair p: this.aggFeaturePairs) {
			p.buildIntervals();
		}
	}
	/**
	public void printTop(int top) {
		System.out.println("=====Top " + top + " distances=====");
		for (int i = 0; i <= top && i < this.featurePairs.size(); i ++) {
			System.out.println(i + ":\t" + this.featurePairs.get(i) + "\t" + this.featurePairs.get(i).getFeature1().getFeatureName());
		}
	}
	*/
}
