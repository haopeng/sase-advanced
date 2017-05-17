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
package edu.umass.cs.sase.interval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import edu.umass.cs.sase.util.csv.ComputedFeature;
import edu.umass.cs.sase.util.csv.DistanceBasedFeature;
import edu.umass.cs.sase.util.csv.DistanceBasedPredictor;
import edu.umass.cs.sase.util.csv.PointLabel;
import edu.umass.cs.sase.util.csv.SamplePoint;

/**
 * @author haopeng
 *
 */
public class IntervalBasedPredictor extends DistanceBasedPredictor {
	
	ArrayList<ValueInterval> intervals;
	ArrayList<ValueInterval> adjustedIntervals;
	double separationScore;
	double weightedScore;
	
	int featureIndex;
	String featureName;
	ArrayList<SamplePoint> testPoints;
	double recall;
	double precision;
	double accuracy;
	
	double recallAdjusted;
	double precisionAdjusted;
	double accuracyAdjusted;
	
	boolean singlePointRemoved;
	
	public IntervalBasedPredictor(String filePath) throws IOException {
		super(filePath);
		// TODO Auto-generated constructor stub
	}
	
	public void buildIntervalsByAttribute(int attrIndex, ArrayList<SamplePoint> points) {
		this.singlePointRemoved = false;
		this.points = points;
		//System.out.println("Feature name:" + this.features.get(attrIndex).getFeatureSignature());
		this.featureIndex = attrIndex;
		this.featureName = this.features.get(featureIndex).getFeatureSignature();
		this.setPointFeaturedValuesBySingleFeatureOriginalValue(attrIndex);
		this.computeInterval();
		this.computeSeparation();
		//index
		//predict

		
	}
	
	/**
	 * This method removes intervals containing only a single event
	 * That interval would be divided equally to its previous and following interval
	 * @throws CloneNotSupportedException 
	 */
	public void removeSinglePoints(){
		this.adjustedIntervals = new ArrayList<ValueInterval>();
		ValueInterval toBeMergedInterval = null;
		for (int i = 0; i < this.intervals.size(); i ++) {
			ValueInterval currentInterval = (ValueInterval)this.intervals.get(i).clone();
			
			if (toBeMergedInterval != null) {
				this.mergeIntervals(toBeMergedInterval, currentInterval);
				toBeMergedInterval = null;
			}
			
			if (currentInterval.getAbnormalCount() + currentInterval.getNormalCount() > 2) { // 1, 2, 3, test different values, attention
				this.addIntervalToAdjustedIntervals(currentInterval);
				continue;
			}
			
			if (this.adjustedIntervals.size() == 0) {
				toBeMergedInterval = currentInterval;
				continue;
			}
			//split
			ValueInterval leftSplitInterval = new ValueInterval();
			ValueInterval rightSplitInterval = new ValueInterval();
			leftSplitInterval.lower = currentInterval.lower;
			leftSplitInterval.upper = (currentInterval.lower + currentInterval.upper) / 2;
			rightSplitInterval.lower = (currentInterval.lower + currentInterval.upper) / 2;
			rightSplitInterval.upper = currentInterval.upper;
			
			ValueInterval lastInterval = this.adjustedIntervals.get(this.adjustedIntervals.size() - 1);
			this.mergeIntervals(leftSplitInterval, lastInterval);
			
			toBeMergedInterval = rightSplitInterval;
		}
		this.singlePointRemoved = true;
	}
	
	public void addIntervalToAdjustedIntervals(ValueInterval interval){
		if (this.adjustedIntervals.size() == 0) {
			this.adjustedIntervals.add(interval);
		} else {
			ValueInterval lastInterval = this.adjustedIntervals.get(this.adjustedIntervals.size() - 1);
			if (lastInterval.label == interval.label) {
				this.mergeIntervals(interval, lastInterval);
			} else {
				this.adjustedIntervals.add(interval);
			}
		}
	}
	
	/**
	 * This method merge the toBeMergedInterval into the mainInterval
	 * The lower/upper would be expanded
	 * The count would be the sum
	 * @param toBeMergedInterval
	 * @param mainInterval
	 */
	public void mergeIntervals(ValueInterval toBeMergedInterval, ValueInterval mainInterval) {
		mainInterval.lower = Math.min(toBeMergedInterval.lower, mainInterval.lower);
		mainInterval.upper = Math.max(toBeMergedInterval.upper, mainInterval.upper);
		mainInterval.abnormalCount += toBeMergedInterval.abnormalCount;
		mainInterval.normalCount += toBeMergedInterval.normalCount;
	}
	public void computeInterval() {
		this.intervals = new ArrayList<ValueInterval>();
		//ValueInterval previousInterval = null;
		SamplePoint previousPoint = null;
		ValueInterval currentInterval = null;
		double lower = -Double.MIN_VALUE;
		int pointIndex = 0;
		int count = 0;
		while (pointIndex < this.points.size()) {
			currentInterval = new ValueInterval();
			currentInterval.lower = lower;
			boolean extend = true;
			while (extend && pointIndex < this.points.size()) {
				SamplePoint currentPoint = this.points.get(pointIndex);
				if( previousPoint == null || currentPoint.getFeaturedValue() == previousPoint.getFeaturedValue() || currentPoint.getTrueLabel() == currentInterval.label) {
					currentInterval.addPoint(currentPoint);
					previousPoint = currentPoint;
					pointIndex++;
				} else {
					previousPoint = null;
					extend = false;
					currentInterval.upper = (currentPoint.getFeaturedValue() + currentInterval.upper) / 2;
					this.intervals.add(currentInterval);
					count++;
					//System.out.println(count + "." + currentInterval.toString());					
					lower = currentInterval.upper;
				}

			}
		}
		currentInterval.setUpper(Double.MAX_VALUE);
		this.intervals.add(currentInterval);
		count++;
		//System.out.println(count + "." + currentInterval.toString());
	}
	public void computeSeparation(){
		int separatedCount = 0;
		int weightedCount = 0;
		for (ValueInterval interval: this.intervals) {
			if (interval.label != PointLabel.Uncertain) {
				separatedCount += interval.getAbnormalCount();
				separatedCount += interval.getNormalCount();
				weightedCount += interval.getAbnormalCount() * interval.getAbnormalCount();
				weightedCount += interval.getNormalCount() * interval.getNormalCount();
			}
		}
		this.separationScore = separatedCount / this.points.size() * 100;
		this.weightedScore = weightedCount / this.points.size();
		//System.out.println("Separation Score:" + this.separationScore);
	}
	
	public void predictPoints(ArrayList<SamplePoint> testPoints) {
		//set the feature value;
		this.testPoints = testPoints;
		this.setPointFeaturedValuesBySingleFeatureOriginalValue(this.testPoints);
		//interative, and count
		this.tp = 0.0;
		this.tn = 0.0;
		this.fp = 0.0;
		this.fn = 0.0;
		for (SamplePoint point : this.testPoints) {
			PointLabel result = this.predictPoint(point);
			if (result == point.getTrueLabel()) {
				if (result == PointLabel.Abnormal) {
					this.tp++;
				} else {
					this.tn++;
				}
			} else {
				if (point.getTrueLabel() == PointLabel.Abnormal) {
					this.fn ++;
				} else {
					this.fp ++;
				}
			}
		}
		
		this.recall = this.tp / (this.tp + this.fn) * 100;
		this.precision = this.tp / (this.tp + this.fp) * 100;
		this.accuracy = (this.tp + this.tn) / (this.tp + this.tn + this.fp + this.fn) * 100;
		//System.out.println("Recall = " + recall + "\tPrecision = " + precision + "\tAccuracy = " + accuracy);
		
	}
	public void predictPointsByAdjustedIntervals(ArrayList<SamplePoint> testPoints) {
		//set the feature value;
		this.testPoints = testPoints;
		this.setPointFeaturedValuesBySingleFeatureOriginalValue(this.testPoints);
		//interative, and count
		this.tp = 0.0;
		this.tn = 0.0;
		this.fp = 0.0;
		this.fn = 0.0;
		for (SamplePoint point : this.testPoints) {
			PointLabel result = this.predictPointByAdjustedIntervals(point);
			if (result == point.getTrueLabel()) {
				if (result == PointLabel.Abnormal) {
					this.tp++;
				} else {
					this.tn++;
				}
			} else {
				if (point.getTrueLabel() == PointLabel.Abnormal) {
					this.fn ++;
				} else {
					this.fp ++;
				}
			}
		}
		
		this.recallAdjusted = this.tp / (this.tp + this.fn) * 100;
		this.precisionAdjusted = this.tp / (this.tp + this.fp) * 100;
		this.accuracyAdjusted = (this.tp + this.tn) / (this.tp + this.tn + this.fp + this.fn) * 100;
		//System.out.println("Recall = " + recall + "\tPrecision = " + precision + "\tAccuracy = " + accuracy);
		
	}
	private PointLabel predictPointByAdjustedIntervals(SamplePoint point) {
		PointLabel result = PointLabel.Uncertain;
		for (ValueInterval interval: this.adjustedIntervals) {
			double value = point.getFeaturedValue();
			double low = interval.lower;
			double up = interval.upper;
			if(value >= low && value < up) {
				result = interval.label;
				if(result == PointLabel.Uncertain) {
					if (interval.getAbnormalCount() > interval.getNormalCount()) {
						result = PointLabel.Abnormal;
					} else {
						result = PointLabel.Normal;
					}
				}
				return result;
			}
		}
		return result;
	}
	
	public PointLabel predictSinglePointByAdjustedIntervals(SamplePoint point) {
		
		PointLabel result = PointLabel.Uncertain;
		for (ValueInterval interval: this.adjustedIntervals) {
			DistanceBasedFeature f = this.features.get(this.featureIndex);
			ComputedFeature feature = point.features.get(f.featureSignature);
			double value = feature.getFeatureValue();
			double low = interval.lower;
			double up = interval.upper;
			if(value >= low && value < up) {
				result = interval.label;
				if(result == PointLabel.Uncertain) {
					if (interval.getAbnormalCount() > interval.getNormalCount()) {
						result = PointLabel.Abnormal;
					} else {
						result = PointLabel.Normal;
					}
				}
				return result;
			}
		}
		return result;
	}
	
	
	
	public PointLabel predictPoint(SamplePoint point) {
		PointLabel result = PointLabel.Uncertain;
		for (ValueInterval interval: this.intervals) {
			double value = point.getFeaturedValue();
			double low = interval.lower;
			double up = interval.upper;
			if(value >= low && value < up) {
				result = interval.label;
				if(result == PointLabel.Uncertain) {
					if (interval.getAbnormalCount() > interval.getNormalCount()) {
						result = PointLabel.Abnormal;
					} else {
						result = PointLabel.Normal;
					}
				}
				return result;
			}
		}
		return result;
	}
	
	public void setPointFeaturedValuesBySingleFeatureOriginalValue(ArrayList<SamplePoint> pointList) {
		DistanceBasedFeature f = this.features.get(this.featureIndex);
		for(SamplePoint point: pointList){
			ComputedFeature feature = point.features.get(f.featureSignature);
			double value = feature.getFeatureValue();
			point.setFeaturedValue(value);
		}
		Collections.sort(pointList);
	}
	
	public void printHeader() {
		System.out.println("No.\tName\tSeperationScore\tNo.Of.Section\tWeightedSeperation\tPrecision\tRecall\tAccuracy\tAdjustedSection\tAdjustedPrecision\tAdjustedRecall\tAdjustedAccuracy");
	}
	public void printInformation() {
		System.out.println(this.featureIndex + "\t" + this.features.get(this.featureIndex).getFeatureSignature() 
				+ "\t" + this.separationScore + "\t" + this.intervals.size() 
				+ "\t" + this.weightedScore +"\t" + this.precision + "\t" + this.recall + "\t" + this.accuracy
				+ "\t" + this.adjustedIntervals.size() + "\t" + this.precisionAdjusted + "\t" + this.recallAdjusted
				+ "\t" + this.accuracyAdjusted);
	}
	
	public void printOriginalIntervals() {
		this.printIntervals(this.intervals);
	}
	
	public void printAdjustedIntervals() {
		this.printIntervals(this.adjustedIntervals);
	}
	public void printIntervals(ArrayList<ValueInterval> intervals) {
		System.out.println("Feature:" + this.getFeatureName());
		System.out.println(ValueInterval.toHeaderString());
		//abnormal
		for (ValueInterval interval: intervals) {
			if (interval.label == PointLabel.Abnormal) {
				System.out.println(interval.toString());
			}
		}
		System.out.println();
		//normal
		for (ValueInterval interval: intervals) {
			if (interval.label == PointLabel.Normal) {
				System.out.println(interval.toString());
			}
		}
		
		//uncertain
		System.out.println();
		for (ValueInterval interval: intervals) {
			if (interval.label == PointLabel.Uncertain) {
				System.out.println(interval.toString());
			}
		}
	}

	public ArrayList<ValueInterval> getIntervals() {
		return intervals;
	}

	public void setIntervals(ArrayList<ValueInterval> intervals) {
		this.intervals = intervals;
	}

	public ArrayList<ValueInterval> getAdjustedIntervals() {
		return adjustedIntervals;
	}

	public void setAdjustedIntervals(ArrayList<ValueInterval> adjustedIntervals) {
		this.adjustedIntervals = adjustedIntervals;
	}

	public double getSeparationScore() {
		return separationScore;
	}

	public void setSeparationScore(double separationScore) {
		this.separationScore = separationScore;
	}

	public double getWeightedScore() {
		return weightedScore;
	}

	public void setWeightedScore(double weightedScore) {
		this.weightedScore = weightedScore;
	}

	public int getFeatureIndex() {
		return featureIndex;
	}

	public void setFeatureIndex(int featureIndex) {
		this.featureIndex = featureIndex;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public ArrayList<SamplePoint> getTestPoints() {
		return testPoints;
	}

	public void setTestPoints(ArrayList<SamplePoint> testPoints) {
		this.testPoints = testPoints;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public double getRecallAdjusted() {
		return recallAdjusted;
	}

	public void setRecallAdjusted(double recallAdjusted) {
		this.recallAdjusted = recallAdjusted;
	}

	public double getPrecisionAdjusted() {
		return precisionAdjusted;
	}

	public void setPrecisionAdjusted(double precisionAdjusted) {
		this.precisionAdjusted = precisionAdjusted;
	}

	public double getAccuracyAdjusted() {
		return accuracyAdjusted;
	}

	public void setAccuracyAdjusted(double accuracyAdjusted) {
		this.accuracyAdjusted = accuracyAdjusted;
	}

	public boolean isSinglePointRemoved() {
		return singlePointRemoved;
	}

	public void setSinglePointRemoved(boolean singlePointRemoved) {
		this.singlePointRemoved = singlePointRemoved;
	}
	
	
}
