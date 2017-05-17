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

public class ComputedFeature implements Cloneable{
	String filePath;
	String eventType;
	String attributeName;
	String computation;
	String featureSignature;
	double featureValue;
	double maxValue;// used for mean only
	double minValue;// used for mean only
	
	public ComputedFeature(String featureSignature, double value, double max, double min){
		this.featureSignature = featureSignature;
		this.featureValue = value;
		this.maxValue = max;
		this.minValue = min;

	}
	
	public ComputedFeature(String filePath, String eventType, String attributeName, String computation,  double featureValue){
		this.filePath = filePath;
		this.eventType = eventType;
		this.attributeName = attributeName;
		this.computation = computation;
		this.featureSignature = eventType + "-" + attributeName + "-" + computation;
		this.featureValue = featureValue;
	}
	
	public ComputedFeature clone(){
		
		ComputedFeature f = new ComputedFeature(this.filePath, this.eventType, this.attributeName, this.computation, this.featureValue);
		f.maxValue = this.maxValue;
		f.minValue = this.minValue;
		return f;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getComputation() {
		return computation;
	}
	public void setComputation(String computation) {
		this.computation = computation;
	}
	public String getFeatureSignature() {
		return featureSignature;
	}
	public void setFeatureSignature(String featureSignature) {
		this.featureSignature = featureSignature;
	}
	public double getFeatureValue() {
		return featureValue;
	}
	public void setFeatureValue(double featureValue) {
		this.featureValue = featureValue;
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

	public String getSchema(){
		StringBuilder sb = new StringBuilder();
		sb.append("filePath\t");
		sb.append("eventType\t");
		sb.append("attributeName\t");
		sb.append("computation\t");
		sb.append("featureSignature\t");
		sb.append("featureValue\t");
		return sb.toString();
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.filePath + "\t");
		sb.append(this.eventType + "\t");
		sb.append(this.attributeName + "\t");
		sb.append(this.computation + "\t");
		sb.append(this.featureSignature + "\t");
		sb.append(this.featureValue + "\t");
		return sb.toString();
		
	}
	
	
}
