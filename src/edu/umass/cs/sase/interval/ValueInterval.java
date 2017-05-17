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

import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.LabeledValue;
import edu.umass.cs.sase.util.csv.PointLabel;
import edu.umass.cs.sase.util.csv.SamplePoint;

public class ValueInterval implements Cloneable{
	double lower;
	double upper;
	
	int abnormalCount;
	int normalCount;
	
	PointLabel label;
	LabelType valueLable;// for cross validation usage
	public ValueInterval(){
		this.label = PointLabel.Uncertain;
		this.valueLable = LabelType.Mixed;
	}
	public void addPoint(SamplePoint point) {
		if(point.getTrueLabel() == PointLabel.Abnormal) {
			this.abnormalCount++;
		} else {
			this.normalCount++;
		}
		//this.lower = Math.min(this.lower, point.getFeaturedValue());
		this.upper = Math.max(this.upper, point.getFeaturedValue());
		if (this.abnormalCount == 0) {
			this.label = PointLabel.Normal;
		} else if (this.normalCount == 0) {
			this.label = PointLabel.Abnormal;
		} else {
			this.label = PointLabel.Uncertain;
		}
	}
	
	public void addLabeledValue(LabeledValue value) {
		if(value.getLabel() == LabelType.Abnormal) {
			this.abnormalCount++;
		} else {
			this.normalCount++;
		}
		this.upper = Math.max(this.upper, value.getValue());
		if (this.abnormalCount == 0) {
			this.valueLable = LabelType.Reference;
		} else if (this.normalCount == 0) {
			this.valueLable = LabelType.Abnormal;
		} else {
			this.valueLable = LabelType.Mixed;
		}
	}
	
	public static String toHeaderString() {
		// label, lower, upper, abnormalcount, normal count
		StringBuilder sb = new StringBuilder();
		sb.append("Label\t");
		sb.append("Lower\t");
		sb.append("Upper\t");
		sb.append("AbnormalCount\t");
		sb.append("NormalCount\t");
		return sb.toString();
	}
	
	public String toString() {
		// label, lower, upper, abnormalcount, normal count
		StringBuilder sb = new StringBuilder();
		sb.append(this.label + "\t");
		sb.append(this.lower + "\t");
		sb.append(this.upper + "\t");
		sb.append(this.abnormalCount + "\t");
		sb.append(this.normalCount + "\t");
		return sb.toString();
	}
	
	public Object clone() {
		Object object = null;
		try {
			object = super.clone();
		} catch (CloneNotSupportedException e){
			e.printStackTrace();
		} 
		return object;
	}
	
	public double getLower() {
		return lower;
	}
	public void setLower(double lower) {
		this.lower = lower;
	}
	public double getUpper() {
		return upper;
	}
	public void setUpper(double upper) {
		this.upper = upper;
	}
	public int getAbnormalCount() {
		return abnormalCount;
	}
	public void setAbnormalCount(int abnormalCount) {
		this.abnormalCount = abnormalCount;
	}
	public int getNormalCount() {
		return normalCount;
	}
	public void setNormalCount(int normalCount) {
		this.normalCount = normalCount;
	}
	public PointLabel getLabel() {
		return label;
	}
	public void setLabel(PointLabel label) {
		this.label = label;
	}
	public LabelType getValueLable() {
		return valueLable;
	}
	public void setValueLable(LabelType valueLable) {
		this.valueLable = valueLable;
	}
	
	

}
