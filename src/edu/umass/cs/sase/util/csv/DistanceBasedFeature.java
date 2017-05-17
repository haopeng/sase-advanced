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
package edu.umass.cs.sase.util.csv;

public class DistanceBasedFeature implements Comparable<DistanceBasedFeature>{
	public String featureSignature;
	double abnormalValue;
	double normalValue;
	double normalizedAbnormal;
	double normalizedNormal;
	double manhantanDistance;
	double abnormalMaxValue;
	double abnormalMinValue;
	double normalMaxValue;
	double normalMinValue;
	double threshold;// test different values
	public DistanceBasedFeature(String featureSignature, double abnormalValue, double normalValue, double normalizedAbnormal, double normalizedNormal, double mDistance, double abnormalMax, double abnormalMin, double normalMax, double normalMin){
		this.featureSignature = featureSignature;
		this.abnormalValue = abnormalValue;
		this.normalValue = normalValue;
		this.normalizedAbnormal = normalizedAbnormal;
		this.normalizedNormal = normalizedNormal;
		this.manhantanDistance = mDistance;
		this.abnormalMaxValue = abnormalMax;
		this.abnormalMinValue = abnormalMin;
		this.normalMaxValue = normalMax;
		this.normalMinValue = normalMin;
		
		this.threshold = 0.9;
		
	}
	/**
	 * Seems not right
	 * @param valueToBePredicted
	 * @param maxV
	 * @param minV
	 * @return
	 */
	public double getDistance(double valueToBePredicted, double maxV, double minV){
		double abnormalValueNormalized = 0.0;
		double normalValueNormalized = 0.0;
		if(this.featureSignature.contains("frequency")){
			double max = Math.max(this.abnormalValue, valueToBePredicted);
			if (max== 0.0){
				max = 1.0;
			}
			abnormalValueNormalized = abnormalValue / max;
			normalValueNormalized = valueToBePredicted / max;
		}else{
			
			double minValue = Math.min(this.abnormalMinValue, minV);
			double maxValue = Math.max(this.abnormalMaxValue, maxV);
			double range = maxValue - minValue;
			if(range == 0){
				range = 1.0;
			}
			abnormalValueNormalized = (abnormalValue - minValue) / range;
			normalValueNormalized = (valueToBePredicted - minValue) / range;
		}
		double newDistance = Math.abs(abnormalValueNormalized - normalValueNormalized);
		if(newDistance > 1){
			//System.out.println();
		}
		return newDistance;
	}

	
	
	
	public double getDistanceOld(double valueToBePredicted, double maxV, double minV){
		double abnormalValueNormalized = 0.0;
		double normalValueNormalized = 0.0;
		if(this.featureSignature.contains("frequency")){
			double max = Math.max(this.abnormalValue, valueToBePredicted);
			if (max== 0.0){
				max = 1.0;
			}
			abnormalValueNormalized = abnormalValue / max;
			normalValueNormalized = normalValue / max;
		}else{
			
			double minValue = Math.min(this.abnormalMinValue, minV);
			double maxValue = Math.max(this.abnormalMaxValue, maxV);
			double range = maxValue - minValue;
			if(range == 0){
				range = 1.0;
			}
			abnormalValueNormalized = (abnormalValue - minValue) / range;
			normalValueNormalized = (normalValue - minValue) / range;
		}
		double newDistance = Math.abs(abnormalValueNormalized - normalValueNormalized);
		if(newDistance > 1){
			//System.out.println();
		}
		return newDistance;
	}
	public int predictClass(double valueToBePredicted, double maxV, double minV){
		double abnormalValueNormalized = 0.0;
		double normalValueNormalized = 0.0;
		if(this.featureSignature.contains("frequency")){
			double max = Math.max(this.abnormalValue, valueToBePredicted);
			if (max== 0.0){
				max = 1.0;
			}
			abnormalValueNormalized = abnormalValue / max;
			normalValueNormalized = normalValue / max;
		}else{
			
			double minValue = Math.min(this.abnormalMinValue, minV);
			double maxValue = Math.max(this.abnormalMaxValue, maxV);
			double range = maxValue - minValue;
			if(range == 0){
				range = 1.0;
			}
			abnormalValueNormalized = (abnormalValue - minValue) / range;
			normalValueNormalized = (normalValue - minValue) / range;
		}
		double newDistance = Math.abs(abnormalValueNormalized - normalValueNormalized);
		if(newDistance > this.threshold * this.manhantanDistance){
			return 1;//normal
		}else{
			return 0;//abnormal;
		}

		
	}
	@Override
	public int compareTo(DistanceBasedFeature anotherFeature) {
		// TODO Auto-generated method stub
		double diff = this.manhantanDistance - anotherFeature.manhantanDistance;
		if(diff > 0){
			return 1;
		}
		if(diff == 0){
			return 0;
		}
		return -1;

	}
	public String getFeatureSignature() {
		return featureSignature;
	}
	public void setFeatureSignature(String featureSignature) {
		this.featureSignature = featureSignature;
	}
	public double getAbnormalValue() {
		return abnormalValue;
	}
	public void setAbnormalValue(double abnormalValue) {
		this.abnormalValue = abnormalValue;
	}
	public double getNormalValue() {
		return normalValue;
	}
	public void setNormalValue(double normalValue) {
		this.normalValue = normalValue;
	}
	public double getNormalizedAbnormal() {
		return normalizedAbnormal;
	}
	public void setNormalizedAbnormal(double normalizedAbnormal) {
		this.normalizedAbnormal = normalizedAbnormal;
	}
	public double getNormalizedNormal() {
		return normalizedNormal;
	}
	public void setNormalizedNormal(double normalizedNormal) {
		this.normalizedNormal = normalizedNormal;
	}
	public double getManhantanDistance() {
		return manhantanDistance;
	}
	public void setManhantanDistance(double manhantanDistance) {
		this.manhantanDistance = manhantanDistance;
	}
	public double getAbnormalMaxValue() {
		return abnormalMaxValue;
	}
	public void setAbnormalMaxValue(double abnormalMaxValue) {
		this.abnormalMaxValue = abnormalMaxValue;
	}
	public double getAbnormalMinValue() {
		return abnormalMinValue;
	}
	public void setAbnormalMinValue(double abnormalMinValue) {
		this.abnormalMinValue = abnormalMinValue;
	}
	public double getNormalMaxValue() {
		return normalMaxValue;
	}
	public void setNormalMaxValue(double normalMaxValue) {
		this.normalMaxValue = normalMaxValue;
	}
	public double getNormalMinValue() {
		return normalMinValue;
	}
	public void setNormalMinValue(double normalMinValue) {
		this.normalMinValue = normalMinValue;
	}
	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	
	
}
