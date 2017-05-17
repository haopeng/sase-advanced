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
import java.util.Arrays;
import java.util.Collections;

import core.Point;// used to compute distance using imported distance functions
import edu.umass.cs.sase.explanation.UI.ExplanationSettings;

public class TimeSeriesFeature extends Feature{
	
	ArrayList<Long> timestamps;//denote the starting timestamps of a time window; if the window size is 0, means there is no window applied. And the timestamp is the instant time for that value.
	ArrayList<Double> values;
	ArrayList<Double> normalizedValues;
	
	ArrayList<Long> alignedTimestamps;
	ArrayList<Double> alignedValues;
	//ArrayList<Double> alignedNormalizedValues;
	
	ArrayList<Point> pointRepresentation;
	ArrayList<Point> alignedPointRepresentation;//compute this using aligned values
	
	double maxValue;
	double minValue;
	
	
	long windowSize; // in milliseconds; if it is zero, means no window. 
	
	TimeSeriesFeature sampledFeature;
	
	//following are used in cross validation
	LabelType classifiedLabel;
	LabelType intervalLabel;
	public TimeSeriesFeature(FeatureType featureType, String featureName, long windowSize, LabelType label) {
		super(featureType, featureName, label);
		this.timestamps = new ArrayList<Long>();
		this.windowSize = windowSize;
		this.values = new ArrayList<Double>();
		this.maxValue = Double.MIN_VALUE;
		this.minValue = Double.MAX_VALUE;
	}
	
	public TimeSeriesFeature(TimeSeriesFeature copyFrom) {
		super(copyFrom.getFeatureType(), copyFrom.getFeatureName(), copyFrom.getLabel());
		this.windowSize = copyFrom.getWindowSize();
		this.maxValue = Double.MIN_VALUE;
		this.minValue = Double.MAX_VALUE;
		
		this.timestamps = new ArrayList<Long>();
		this.values = new ArrayList<Double>();
		this.addPoints(copyFrom.getTimestamps(), copyFrom.getValues());
	}
	
	public void normalizeValues(double max, double min) {
		this.normalizedValues = new ArrayList<Double>();
		for (Double d: this.values) {
			if (d == null) {
				this.normalizedValues.add(d);
			}else if (max == min) {
				this.normalizedValues.add(0.5);
			} else {
				double v = (d - min) / (max - min);
				this.normalizedValues.add(v);
			}
		}
	}
	
	/**
	 * Align the time series into a specific size by dropping some points uniformally distributed
	 * @param size
	 */
	public void alignValues(int size) {
		
		if (size >= this.values.size()) {
			this.alignedTimestamps = this.timestamps;
			this.alignedValues = this.normalizedValues;
			return;
		} 
		
		this.alignedTimestamps = new ArrayList<Long>();
		this.alignedValues = new ArrayList<Double>();
		int stepSize = this.normalizedValues.size() / (this.normalizedValues.size() - size);
		
		int toReduce = this.normalizedValues.size() - size;
		
		for (int i = 0; i < this.normalizedValues.size(); i ++) {
			if (i % stepSize == 0 && toReduce > 0) {
				toReduce --;
				continue;
			}
			this.alignedTimestamps.add(this.timestamps.get(i));
			this.alignedValues.add(this.normalizedValues.get(i));
		}
		
	}
	
	/**
	 * generate sampled feature to the percentage size
	 * @param percentage
	 */
	public void generateSampleFeature(double percentage, int minSize, int maxSize) {
		this.sampledFeature = new TimeSeriesFeature(this.featureType, this.featureName + "-sampled", this.windowSize, this.label);
		
		if (this.normalizedValues.size() <= minSize) {
			this.sampledFeature.addPoints(this.timestamps, this.normalizedValues);
		} else {
			int size = (int)(this.normalizedValues.size() * percentage);
			
			size = Math.min(size, maxSize);
			
			size = Math.max(size, minSize);
			
			System.out.println(this.featureName + "Sample rate: " + percentage + " " + size + " pionts from " + this.values.size());
			
			if (this.normalizedValues.size() > 0) {
				int stepSize = this.normalizedValues.size() / (this.normalizedValues.size() - size);
				
				int toReduce = this.normalizedValues.size() - size;
				
				for (int i = 0; i < this.normalizedValues.size(); i ++) {
					if (i % stepSize == 0 && toReduce > 0) {
						toReduce --;
						continue;
					}
					this.sampledFeature.addPoint(this.timestamps.get(i), this.values.get(i));
				}
			} 

		}
		
		
		
		
		
		this.sampledFeature.setNormalizedValues(this.sampledFeature.getValues());
		
	}
	
	
	public void convertToPointRepresentation() {
		if (this.pointRepresentation != null) {
			return;
		}
		
		this.pointRepresentation = new ArrayList<Point>();
		
		for (int i = 0; i < this.timestamps.size(); i ++) {
			double[] coords = {this.normalizedValues.get(i), 0.0, (double)this.timestamps.get(i)};
			this.pointRepresentation.add(new Point(coords));
		}
	}
	
	public void convertToAlignedPointRepresentation() {
		if (this.alignedPointRepresentation != null) {
			return;
		}
		
		this.alignedPointRepresentation = new ArrayList<Point>();
		
		for (int i = 0; i < this.alignedTimestamps.size(); i ++) {
			double[] coords = {this.alignedValues.get(i), 0.0, (double)this.alignedTimestamps.get(i)};
			this.alignedPointRepresentation.add(new Point(coords));
		}
	}

	
	public void printDebugInfo() {
		System.out.println(this.featureName);
		for (Double d : this.values) {
			System.out.println(d);
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Feature name:\t" + this.featureName + "\tNumberOfPoints:\t" + this.values.size() + "\n");
		return sb.toString();
	}
	
	
	public void addPoints(ArrayList<Long>tsList, ArrayList<Double> valueList) {
		for (int i = 0; i < tsList.size() && i < valueList.size(); i ++) {
			long ts = tsList.get(i);
			double value = valueList.get(i);
			this.addPoint(ts, value);
		}
	}
	
	public void addPoint(Long ts, Double value) {
		this.timestamps.add(ts);
		this.values.add(value);
		
		if (value != null) {
			this.maxValue = Math.max(this.maxValue, value);
			this.minValue = Math.min(this.minValue, value);

		}
	}
	
	/**
	 * Delete points in the index range, included.
	 * @param startIndex
	 * @param endIndex
	 */
	public void deletePointsInRange(int startIndex, int endIndex) {
		for (int i = endIndex; i >= startIndex; i --) {
			this.timestamps.remove(i);
			this.values.remove(i);
		}
	}
	
	public void deletePointsInList(ArrayList<Integer> toDeleteIndex) {
		Collections.sort(toDeleteIndex, Collections.reverseOrder());
		for (int i = 0; i < toDeleteIndex.size(); i ++) {
			int index = toDeleteIndex.get(i);
			if (index < this.values.size()) {
				this.timestamps.remove(index);
				this.values.remove(index);				
			}

		}
		
	}
	
	
	
	
	public ArrayList<Long> getTimestamps() {
		return timestamps;
	}

	public void setTimestamps(ArrayList<Long> timestamps) {
		this.timestamps = timestamps;
	}

	public ArrayList<Double> getValues() {
		return values;
	}

	public void setValues(ArrayList<Double> values) {
		this.values = values;
	}

	public ArrayList<Double> getAlignedValues() {
		return alignedValues;
	}

	public void setAlignedValues(ArrayList<Double> alignedValues) {
		this.alignedValues = alignedValues;
	}

	public long getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(long windowSize) {
		this.windowSize = windowSize;
	}

	public ArrayList<Long> getAlignedTimestamps() {
		return alignedTimestamps;
	}

	public void setAlignedTimestamps(ArrayList<Long> alignedTimestamps) {
		this.alignedTimestamps = alignedTimestamps;
	}

	public ArrayList<Point> getPointRepresentation() {
		return pointRepresentation;
	}

	public void setPointRepresentation(ArrayList<Point> pointRepresentation) {
		this.pointRepresentation = pointRepresentation;
	}

	public ArrayList<Point> getAlignedPointRepresentation() {
		return alignedPointRepresentation;
	}

	public void setAlignedPointRepresentation(
			ArrayList<Point> alignedPointRepresentation) {
		this.alignedPointRepresentation = alignedPointRepresentation;
	}

	public ArrayList<Double> getNormalizedValues() {
		return normalizedValues;
	}

	public void setNormalizedValues(ArrayList<Double> normalizedValues) {
		this.normalizedValues = normalizedValues;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public TimeSeriesFeature getSampledFeature() {
		if (this.sampledFeature == null) {
			this.generateSampleFeature(ExplanationSettings.sampleRate, ExplanationSettings.minSampleSize, ExplanationSettings.maxSampleSize);
		}
		return sampledFeature;
	}

	public void setSampledFeature(TimeSeriesFeature sampledFeature) {
		this.sampledFeature = sampledFeature;
	}
	
	public double getFrequency() {
		double size = (double)this.values.size();
		double period = (this.timestamps.get(this.timestamps.size() - 1) - this.timestamps.get(0)) / (1000.0); // convert to second
		if (size== 0.0) {
			return 0;
		}
		return size / period;
	}

	public LabelType getIntervalLabel() {
		return intervalLabel;
	}

	public void setIntervalLabel(LabelType intervalLabel) {
		this.intervalLabel = intervalLabel;
	}

	public LabelType getClassifiedLabel() {
		return classifiedLabel;
	}

	public void setClassifiedLabel(LabelType classifiedLabel) {
		this.classifiedLabel = classifiedLabel;
	}
	
	
	public int indexOfTimestamp(long ts) {
		for (int i = 0;  i < this.timestamps.size(); i ++) {
			if (ts <= this.timestamps.get(i)) {
				return i;
			}
		}
		return this.timestamps.size() - 1;
	}
	
	public long timestampForIndex(int index) {
		return this.timestamps.get(index);
	}
	
	public int getCount() {
		return this.timestamps.size();
	}
	public long getTimeStampAtIndex(int index) {
		return this.timestamps.get(index);
	}
	public double getValueAtIndex(int index) {
		//System.out.println("Debug:" + this.featureName + "\t" + index + "\t size:" + this.values.size());
		if (index < this.values.size()) {
			return this.values.get(index);			
		}
		return 0.0;
	}
	
}
