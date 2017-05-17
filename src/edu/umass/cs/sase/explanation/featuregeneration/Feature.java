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

public class Feature {
	FeatureType featureType;
	LabelType label; //true label, or original label
	String featureName;
	long startTime;
	long endTime;
	double value;
	boolean isNull;// sometimes, there might be no valid points for the feature to be computed, and this isNull will be labeled as True. For null value comparision, we should make special rules.
	// if either feature being compared is null, computed distance is 0. This works for this application because the system tends to select larger distance, and zero distances are ignored.
	
	String rawEventType;
	
	public Feature() {
		this.isNull = false;
	}
	
	public Feature(FeatureType featureType,String name, LabelType label) {
		this.featureType = featureType;
		this.featureName = name;
		this.label = label;
		this.isNull = false;
	}


	
	public FeatureType getFeatureType() {
		return featureType;
	}


	public void setFeatureType(FeatureType featureType) {
		this.featureType = featureType;
	}


	public String getFeatureName() {
		if (this.featureName != null) {
			return featureName;
		}
		return "NoFeatureName";
	}


	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}


	public long getStartTime() {
		return startTime;
	}


	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}


	public long getEndTime() {
		return endTime;
	}


	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public String getRawEventType() {
		return rawEventType;
	}

	public void setRawEventType(String rawEventType) {
		this.rawEventType = rawEventType;
	}

	public LabelType getLabel() {
		return label;
	}

	public void setLabel(LabelType label) {
		this.label = label;
	}

		
	

}
