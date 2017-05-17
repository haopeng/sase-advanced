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

public class WindowTimeSeriesGenerator {
	ArrayList<TimeSeriesFeature> timeSeriesRawFeatures;
	HashSet<String> frequencyEventType;
	long windowSize; //in milliseconds
	LabelType label;
	
	ArrayList<TimeSeriesFeature> windowTimeSeriesFeatures;
	public WindowTimeSeriesGenerator(ArrayList<TimeSeriesFeature> timeSeriesRawFeatures, long windowSize, LabelType label) {
		this.timeSeriesRawFeatures = timeSeriesRawFeatures;
		this.windowSize = windowSize;
		this.windowTimeSeriesFeatures = new ArrayList<TimeSeriesFeature>();
		this.frequencyEventType = new HashSet<String>();
		this.label = label;
		
		this.generateWindowTimeSeries();
		if (ExplanationSettings.printResult) {
			this.printInfo();			
		}

		
		
	}
	
	public void printInfo() {
		System.out.println("Debug: generated WindowTimeSeriesFeature:" + this.windowTimeSeriesFeatures.size());
		
		if (ExplanationSettings.debugMode) {
			int count = 0;
			for (int i = 0; i < this.windowTimeSeriesFeatures.size(); i ++) {
				System.out.println(count ++ + ":" + this.windowTimeSeriesFeatures.get(i).getFeatureName());
			}
		}
		System.out.println();
	}
	
	public void generateWindowTimeSeries() {
		for (TimeSeriesFeature tsFeature: timeSeriesRawFeatures) {
			this.processRawTimeSeries(tsFeature);
		}
		
	}
	
	public void processRawTimeSeries(TimeSeriesFeature tsFeature) {
		//initialize features
		if (ExplanationSettings.debugMode) {
			System.out.println("Processing feature:" + tsFeature.getFeatureName());
		}
		//count
		TimeSeriesFeature countFeature = new TimeSeriesFeature(FeatureType.TimeSeriesCount, tsFeature.getRawEventType() + "-TimeSeriesCount", this.windowSize, this.label);
		countFeature.setRawEventType(tsFeature.getRawEventType());
		
		//frequency
		TimeSeriesFeature frequencyFeature = new TimeSeriesFeature(FeatureType.TimeSeriesFrequency, tsFeature.getRawEventType() + "-TimeSeriesFrequency", this.windowSize, this.label);
		frequencyFeature.setRawEventType(tsFeature.getRawEventType());
		
		if (!this.frequencyEventType.contains(tsFeature.getRawEventType())) {
			if (ExplanationSettings.countEnable) {
				this.windowTimeSeriesFeatures.add(countFeature);
			}
			this.windowTimeSeriesFeatures.add(frequencyFeature);
			
			this.frequencyEventType.add(tsFeature.getRawEventType());
		}
		
		//max
		TimeSeriesFeature maxFeature = new TimeSeriesFeature(FeatureType.TimeSeriesMaxValue, tsFeature.getFeatureName() + "-TimeSeriesMax", this.windowSize, this.label);
		maxFeature.setRawEventType(tsFeature.getRawEventType());
		if (ExplanationSettings.maxEnable) {
			this.windowTimeSeriesFeatures.add(maxFeature);
		}
		//min
		TimeSeriesFeature minFeature = new TimeSeriesFeature(FeatureType.TimeSeriesMinValue, tsFeature.getFeatureName() + "-TimeSeriesMin", this.windowSize, this.label);
		minFeature.setRawEventType(tsFeature.getRawEventType());
		if (ExplanationSettings.minEnable) {
			this.windowTimeSeriesFeatures.add(minFeature);
		}
		//mean
		TimeSeriesFeature meanFeature = new TimeSeriesFeature(FeatureType.TimeSeriesMeanValue, tsFeature.getFeatureName() + "-TimeSeriesMean", this.windowSize, this.label);
		meanFeature.setRawEventType(tsFeature.getRawEventType());
		this.windowTimeSeriesFeatures.add(meanFeature);
		
		//sum
		TimeSeriesFeature sumFeature = new TimeSeriesFeature(FeatureType.TimeSeriesSumValue, tsFeature.getFeatureName() + "-TimeSeriesSum", this.windowSize, this.label);
		sumFeature.setRawEventType(tsFeature.getRawEventType());
		if (ExplanationSettings.sumEnable) {
			this.windowTimeSeriesFeatures.add(sumFeature);
		}
		
		
		
		//sliding window
		
		long lower = tsFeature.getStartTime();
		long upper = lower + this.windowSize;
		int pointer = 0;
		
		int count = 0;
		double sum = 0;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		
		
		while(lower <= tsFeature.getEndTime()) {
			//each sliding window (]
			upper = lower += this.windowSize;
			//empty window
			if (pointer >= tsFeature.getTimestamps().size() || tsFeature.getTimestamps().get(pointer) > upper) {
				frequencyFeature.addPoint(lower, 0.0);
				countFeature.addPoint(lower, 0.0);
				
				if (ExplanationSettings.addZeroPointForEmptyWindow) {
					Double d = 0.0;
					sumFeature.addPoint(lower, d);
					maxFeature.addPoint(lower, d);
					minFeature.addPoint(lower, d);
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
				countFeature.addPoint(lower, (double)count);
				sumFeature.addPoint(lower, sum);
				maxFeature.addPoint(lower, max);
				minFeature.addPoint(lower, min);
				meanFeature.addPoint(lower, sum /(double) count);
				frequencyFeature.addPoint(lower, (double)count / (this.windowSize / 1000));
			}
			
			lower = upper;
			count = 0;
			sum = 0;
			max = Double.MIN_VALUE;
			min = Double.MAX_VALUE;
		}
		

		
	}

	public void processRawTimeSeriesNew(TimeSeriesFeature tsFeature) {
		if (tsFeature.getFeatureName().equals("JobFinish-id")) {
			System.out.println();//debug
		}
		
		//initialize features
		if (ExplanationSettings.debugMode) {
			System.out.println("Processing feature:" + tsFeature.getFeatureName());
		}
		//count
		TimeSeriesFeature countFeature = new TimeSeriesFeature(FeatureType.TimeSeriesCount, tsFeature.getRawEventType() + "-TimeSeriesCount", this.windowSize, this.label);
		countFeature.setRawEventType(tsFeature.getRawEventType());
		
		//frequency
		TimeSeriesFeature frequencyFeature = new TimeSeriesFeature(FeatureType.TimeSeriesFrequency, tsFeature.getRawEventType() + "-TimeSeriesFrequency", this.windowSize, this.label);
		frequencyFeature.setRawEventType(tsFeature.getRawEventType());
		
		if (!this.frequencyEventType.contains(tsFeature.getRawEventType())) {
			if (ExplanationSettings.countEnable) {
				this.windowTimeSeriesFeatures.add(countFeature);
			}
			this.windowTimeSeriesFeatures.add(frequencyFeature);
			
			this.frequencyEventType.add(tsFeature.getRawEventType());
		}
		
		//max
		TimeSeriesFeature maxFeature = new TimeSeriesFeature(FeatureType.TimeSeriesMaxValue, tsFeature.getFeatureName() + "-TimeSeriesMax", this.windowSize, this.label);
		maxFeature.setRawEventType(tsFeature.getRawEventType());
		if (ExplanationSettings.maxEnable) {
			this.windowTimeSeriesFeatures.add(maxFeature);
		}
		//min
		TimeSeriesFeature minFeature = new TimeSeriesFeature(FeatureType.TimeSeriesMinValue, tsFeature.getFeatureName() + "-TimeSeriesMin", this.windowSize, this.label);
		minFeature.setRawEventType(tsFeature.getRawEventType());
		if (ExplanationSettings.minEnable) {
			this.windowTimeSeriesFeatures.add(minFeature);
		}
		//mean
		TimeSeriesFeature meanFeature = new TimeSeriesFeature(FeatureType.TimeSeriesMeanValue, tsFeature.getFeatureName() + "-TimeSeriesMean", this.windowSize, this.label);
		meanFeature.setRawEventType(tsFeature.getRawEventType());
		this.windowTimeSeriesFeatures.add(meanFeature);
		
		//sum
		TimeSeriesFeature sumFeature = new TimeSeriesFeature(FeatureType.TimeSeriesSumValue, tsFeature.getFeatureName() + "-TimeSeriesSum", this.windowSize, this.label);
		sumFeature.setRawEventType(tsFeature.getRawEventType());
		if (ExplanationSettings.sumEnable) {
			this.windowTimeSeriesFeatures.add(sumFeature);
		}
		
		
		
		//sliding window
		
		long lower = tsFeature.getStartTime();
		long upper = lower + this.windowSize;
		int pointer = 0;
		
		int count = 0;
		double sum = 0;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		
		
		while(lower <= tsFeature.getEndTime()) {
			//each sliding window
			upper = lower += this.windowSize;
			//empty window
			if (pointer >= tsFeature.getTimestamps().size() || tsFeature.getTimestamps().get(pointer) > upper) {
				frequencyFeature.addPoint(lower, 0.0);
				countFeature.addPoint(lower, 0.0);
				
				if (ExplanationSettings.addZeroPointForEmptyWindow) {
					Double d = 0.0;
					sumFeature.addPoint(lower, d);
					maxFeature.addPoint(lower, d);
					minFeature.addPoint(lower, d);
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
				countFeature.addPoint(lower, (double)count);
				sumFeature.addPoint(lower, sum);
				maxFeature.addPoint(lower, max);
				minFeature.addPoint(lower, min);
				meanFeature.addPoint(lower, sum /(double) count);
				frequencyFeature.addPoint(lower, (double)count / (this.windowSize / 1000));
			}
			
			lower = upper;
			count = 0;
			sum = 0;
			max = Double.MIN_VALUE;
			min = Double.MAX_VALUE;
		}
		

		
	}
	public void old(TimeSeriesFeature tsFeature) {
		//initialize features
				if (ExplanationSettings.debugMode) {
					System.out.println("Processing feature:" + tsFeature.getFeatureName());
				}
				//count
				TimeSeriesFeature countFeature = new TimeSeriesFeature(FeatureType.TimeSeriesCount, tsFeature.getRawEventType() + "-TimeSeriesCount", this.windowSize, this.label);
				countFeature.setRawEventType(tsFeature.getRawEventType());
				
				//frequency
				TimeSeriesFeature frequencyFeature = new TimeSeriesFeature(FeatureType.TimeSeriesFrequency, tsFeature.getRawEventType() + "-TimeSeriesFrequency", this.windowSize, this.label);
				frequencyFeature.setRawEventType(tsFeature.getRawEventType());
				
				if (!this.frequencyEventType.contains(tsFeature.getRawEventType())) {
					if (ExplanationSettings.countEnable) {
						this.windowTimeSeriesFeatures.add(countFeature);
					}
					this.windowTimeSeriesFeatures.add(frequencyFeature);
					
					this.frequencyEventType.add(tsFeature.getRawEventType());
				}
				
				//max
				TimeSeriesFeature maxFeature = new TimeSeriesFeature(FeatureType.TimeSeriesMaxValue, tsFeature.getFeatureName() + "-TimeSeriesMax", this.windowSize, this.label);
				maxFeature.setRawEventType(tsFeature.getRawEventType());
				if (ExplanationSettings.maxEnable) {
					this.windowTimeSeriesFeatures.add(maxFeature);
				}
				//min
				TimeSeriesFeature minFeature = new TimeSeriesFeature(FeatureType.TimeSeriesMinValue, tsFeature.getFeatureName() + "-TimeSeriesMin", this.windowSize, this.label);
				minFeature.setRawEventType(tsFeature.getRawEventType());
				if (ExplanationSettings.minEnable) {
					this.windowTimeSeriesFeatures.add(minFeature);
				}
				//mean
				TimeSeriesFeature meanFeature = new TimeSeriesFeature(FeatureType.TimeSeriesMeanValue, tsFeature.getFeatureName() + "-TimeSeriesMean", this.windowSize, this.label);
				meanFeature.setRawEventType(tsFeature.getRawEventType());
				this.windowTimeSeriesFeatures.add(meanFeature);
				
				//sum
				TimeSeriesFeature sumFeature = new TimeSeriesFeature(FeatureType.TimeSeriesSumValue, tsFeature.getFeatureName() + "-TimeSeriesSum", this.windowSize, this.label);
				sumFeature.setRawEventType(tsFeature.getRawEventType());
				if (ExplanationSettings.sumEnable) {
					this.windowTimeSeriesFeatures.add(sumFeature);
				}
				
				
				
				//sliding window
				
				long lower = tsFeature.getStartTime();
				long upper = lower + this.windowSize;
				int pointer = 0;
				
				int count = 0;
				double sum = 0;
				double max = Double.MIN_VALUE;
				double min = Double.MAX_VALUE;
				
		/*
		if (tsFeature.getTimestamps().size() == 0) {
			while(lower <= tsFeature.getEndTime()) {
				frequencyFeature.addPoint(lower, 0.0);
				lower += this.windowSize;
			}
			
			return;
		}
		
		*/
		
		
		while (pointer < tsFeature.getTimestamps().size()) {
			if (tsFeature.getTimestamps().get(pointer) <= upper) {
				double value = tsFeature.getValues().get(pointer);
				count ++;
				sum += value;
				max = Math.max(max, value);
				min = Math.min(min, value);
				
				pointer ++;
			} else {
				// add new point to features
				if (count > 0) {
					countFeature.addPoint(lower, (double)count);
					sumFeature.addPoint(lower, sum);
					maxFeature.addPoint(lower, max);
					minFeature.addPoint(lower, min);
					meanFeature.addPoint(lower, sum /(double) count);
					frequencyFeature.addPoint(lower, (double)count / (this.windowSize / 1000));
				} else {
					//add null values for some aggregations
					//treat as 0
					countFeature.addPoint(lower, 0.0);
					frequencyFeature.addPoint(lower, 0.0);

					//Double d = null;
					//other features as null point, nothing to add
					/*
					Double d = 0.0;
					sumFeature.addPoint(lower, d);
					maxFeature.addPoint(lower, d);
					minFeature.addPoint(lower, d);
					meanFeature.addPoint(lower, d);
					*/
				}
				
				// initialize values
				lower = upper;
				upper = lower + this.windowSize;
				count = 0;
				sum = 0;
				max = Double.MIN_VALUE;
				min = Double.MAX_VALUE;
				
			}
			
		}
		
		// add last window to features
		
		if (count > 0) {
			countFeature.addPoint(lower, (double)count);
			sumFeature.addPoint(lower, sum);
			maxFeature.addPoint(lower, max);
			minFeature.addPoint(lower, min);
			meanFeature.addPoint(lower, sum /(double) count);
			frequencyFeature.addPoint(lower, (double)count / (this.windowSize / 1000));
		} else {
			//add null values for some aggregations
			countFeature.addPoint(lower, 0.0);
			frequencyFeature.addPoint(lower, 0.0);
			
			/*
			Double d = null;
			sumFeature.addPoint(lower, d);
			sumFeature.setNull(true);
			maxFeature.addPoint(lower, d);
			maxFeature.setNull(true);
			minFeature.addPoint(lower, d);
			minFeature.setNull(true);
			meanFeature.addPoint(lower, d);
			meanFeature.setNull(true);
			*/
		}
	}
	
	public ArrayList<TimeSeriesFeature> getTimeSeriesRawFeatures() {
		return timeSeriesRawFeatures;
	}

	public void setTimeSeriesRawFeatures(
			ArrayList<TimeSeriesFeature> timeSeriesRawFeatures) {
		this.timeSeriesRawFeatures = timeSeriesRawFeatures;
	}

	public long getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(long windowSize) {
		this.windowSize = windowSize;
	}

	public ArrayList<TimeSeriesFeature> getWindowTimeSeriesFeatures() {
		return windowTimeSeriesFeatures;
	}

	public void setWindowTimeSeriesFeatures(
			ArrayList<TimeSeriesFeature> windowTimeSeriesFeatures) {
		this.windowTimeSeriesFeatures = windowTimeSeriesFeatures;
	}

}
