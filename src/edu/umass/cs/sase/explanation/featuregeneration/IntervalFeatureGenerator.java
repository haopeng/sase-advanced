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
package edu.umass.cs.sase.explanation.featuregeneration;

import java.util.ArrayList;
import java.util.HashSet;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;


/**
 * This class generates aggregation features over the entire interval, like avg(), frequency
 * Input: timeSeriesRawGenerator
 * Output: IntervalFeatures
 * @author haopeng
 *
 */
public class IntervalFeatureGenerator {
	ArrayList<TimeSeriesFeature> timeSeriesRawFeatures;
	ArrayList<Feature> intervalFeatures;
	LabelType label;
	
	HashSet<String> frequencyEventTypes;//this set remembers the event type which has produced count/frequency, in order to avoid repeative calculation 
	
	public IntervalFeatureGenerator(ArrayList<TimeSeriesFeature> timeSeriesRawFeatures, LabelType label) {
		this.timeSeriesRawFeatures = timeSeriesRawFeatures;
		this.intervalFeatures = new ArrayList<Feature>();
		this.frequencyEventTypes = new HashSet<String>();
		this.label = label;
		
		this.generateFeatures();
		if (ExplanationSettings.printResult) {
			this.printInfo();			
		}

	}
	
	public void generateFeatures() {
		for (TimeSeriesFeature tsFeature : this.timeSeriesRawFeatures) {
			this.computeAggregationIntervals(tsFeature);
		}
	}
	
	public void computeAggregationIntervals(TimeSeriesFeature tsFeature) {

		//count
		Feature countFeature = new Feature(FeatureType.CountValue, tsFeature.getRawEventType() + "-CountValue", this.label);
		countFeature.setStartTime(tsFeature.getStartTime());
		countFeature.setEndTime(tsFeature.getEndTime());
		countFeature.setRawEventType(tsFeature.getRawEventType());
		
		//frequency
		Feature frequencyFeature = new Feature(FeatureType.Frequency, tsFeature.getRawEventType() + "-Frequency", this.label);
		frequencyFeature.setStartTime(tsFeature.getStartTime());
		frequencyFeature.setEndTime(tsFeature.getEndTime());
		frequencyFeature.setRawEventType(tsFeature.getRawEventType());
		
		if (!this.frequencyEventTypes.contains(tsFeature.getRawEventType())) {
			//count
			if (ExplanationSettings.countEnable) {
				this.intervalFeatures.add(countFeature);
			}
			//frequency
			this.intervalFeatures.add(frequencyFeature);
			
			this.frequencyEventTypes.add(tsFeature.getRawEventType());
		}
		
		//sum
		Feature sumFeature = new Feature(FeatureType.SumValue, tsFeature.getFeatureName() + "-SumValue", this.label);
		sumFeature.setNull(true);
		sumFeature.setStartTime(tsFeature.getStartTime());
		sumFeature.setEndTime(tsFeature.getEndTime());
		sumFeature.setRawEventType(tsFeature.getRawEventType());
		if (ExplanationSettings.sumEnable) {
			this.intervalFeatures.add(sumFeature);
		}
	
		
		//max
		Feature maxFeature = new Feature(FeatureType.MaxValue, tsFeature.getFeatureName() + "-MaxValue", this.label);
		maxFeature.setNull(true);
		maxFeature.setStartTime(tsFeature.getStartTime());
		maxFeature.setEndTime(tsFeature.getEndTime());
		maxFeature.setRawEventType(tsFeature.getRawEventType());
		if (ExplanationSettings.maxEnable) {
			this.intervalFeatures.add(maxFeature);
		}

		
		//min
		Feature minFeature = new Feature(FeatureType.MinValue, tsFeature.getFeatureName() + "-MinValue", this.label);
		minFeature.setNull(true);
		minFeature.setStartTime(tsFeature.getStartTime());
		minFeature.setEndTime(tsFeature.getEndTime());
		minFeature.setRawEventType(tsFeature.getRawEventType());
		if (ExplanationSettings.minEnable) {
			this.intervalFeatures.add(minFeature);
		}
		
		//mean
		Feature meanFeature = new Feature(FeatureType.MeanValue, tsFeature.getFeatureName() + "-MeanValue", this.label);
		meanFeature.setNull(true);
		meanFeature.setStartTime(tsFeature.getStartTime());
		meanFeature.setEndTime(tsFeature.getEndTime());
		meanFeature.setRawEventType(tsFeature.getRawEventType());
		this.intervalFeatures.add(meanFeature);

		
		

		
		if (tsFeature.getValues().size() > 0) {
			countFeature.setValue(tsFeature.getValues().size());
			frequencyFeature.setValue(tsFeature.getValues().size() / ((tsFeature.getEndTime() - tsFeature.getStartTime()) / 1000));
			
			double sum = 0;
			double max = Double.MIN_VALUE;
			double min = Double.MAX_VALUE;
			
			for (Double d: tsFeature.getValues()) {
				sum += d;
				max = Math.max(max, d);
				min = Math.min(min, d);
			}
			
			sumFeature.setValue(sum);
			sumFeature.setNull(false);
			
			minFeature.setValue(min);
			minFeature.setNull(false);
			
			maxFeature.setValue(max);
			maxFeature.setNull(false);
			
			meanFeature.setValue(sum / tsFeature.getValues().size());
			meanFeature.setNull(false);
	
		}
	}
	
	/*
	public void computeMeanValueForInterval(TimeSeriesFeature tsFeature) {
		this.featureType = FeatureType.MeanValue;
		this.featureName = tsFeature.getFeatureName() + "-MeanValue";
		
		this.startTime = tsFeature.getStartTime();
		this.endTime = tsFeature.getEndTime();
		
		double sum = 0;
		
		for (Double value: tsFeature.getValues()) {
			sum += value;
		}
		
		this.value = sum / tsFeature.getValues().size();
		
		System.out.println("Debug: new feature generated. Name:" + this.getFeatureName() + ", value =" + this.value);
	}

	public void computeFrequencyForInterval(TimeSeriesFeature tsFeature) {
		this.featureType = FeatureType.Frequency;
		this.featureName = tsFeature.getFeatureName() + "-Frequency";
		
		
		this.startTime = tsFeature.getStartTime();
		this.endTime = tsFeature.getEndTime();
		
		this.value = tsFeature.getValues().size() / ((this.endTime - this.startTime) / 1000);// Assume the timestamps are in millisecons
		
		System.out.println("Debug: new feature generated. Name:" + this.getFeatureName() + ", value =" + this.value);
	}
	*/
	
	public void printInfo() {
		System.out.println("Debug: total interval features generated:" + this.intervalFeatures.size());
		if (ExplanationSettings.debugMode) {
			int count = 0;
			for (Feature f: this.intervalFeatures) {
				System.out.println(count ++ + ":" + f.getFeatureName());
			}
		}
		
	}

	public ArrayList<TimeSeriesFeature> getTimeSeriesRawFeatures() {
		return timeSeriesRawFeatures;
	}

	public void setTimeSeriesRawFeatures(
			ArrayList<TimeSeriesFeature> timeSeriesRawFeatures) {
		this.timeSeriesRawFeatures = timeSeriesRawFeatures;
	}

	public ArrayList<Feature> getIntervalFeatures() {
		return intervalFeatures;
	}

	public void setIntervalFeatures(ArrayList<Feature> intervalFeatures) {
		this.intervalFeatures = intervalFeatures;
	}
	
	

}
