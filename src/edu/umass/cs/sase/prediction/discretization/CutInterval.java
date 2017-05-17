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

package edu.umass.cs.sase.prediction.discretization;

import edu.umass.cs.sase.util.csv.PointLabel;
import edu.umass.cs.sase.util.csv.SamplePoint;

public class CutInterval {
	double lower;
	double upper;
	boolean lowerInclusive;
	boolean upperInclusive;
	
	int numberOfNormal;
	int numberOfAbnormal;
	PointLabel majorityLabel;
	int numberOfInconsistency;
	
	public CutInterval(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;
		this.lowerInclusive = true;
		this.upperInclusive = false;
	}
	
	public void addPoint(SamplePoint point) {
		//System.out.println("lower= " + this.lower + ", upper=" + this.upper);
		//System.out.println(point.getFeaturedValue());
		if (point.getFeaturedValue() < this.lower || point.getFeaturedValue() >= this.upper) {
			return;
		}
		
		if (point.getTrueLabel() == PointLabel.Abnormal) {
			this.numberOfAbnormal ++;
		} else {
			this.numberOfNormal ++;
		}
	}
	
	public void summarize() {
		if (this.numberOfNormal > this.numberOfAbnormal) {
			this.majorityLabel = PointLabel.Normal;
			this.numberOfInconsistency = this.numberOfAbnormal;
		} else {
			this.majorityLabel = PointLabel.Abnormal;
			this.numberOfInconsistency = this.numberOfNormal;
		}		
	}
	
	public PointLabel predictValue(double value) {
		if (value >= this.lower && value < this.upper) {
			return this.majorityLabel;
		}
		
		return null;
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

	public boolean isLowerInclusive() {
		return lowerInclusive;
	}

	public void setLowerInclusive(boolean lowerInclusive) {
		this.lowerInclusive = lowerInclusive;
	}

	public boolean isUpperInclusive() {
		return upperInclusive;
	}

	public void setUpperInclusive(boolean upperInclusive) {
		this.upperInclusive = upperInclusive;
	}

	public int getNumberOfNormal() {
		return numberOfNormal;
	}

	public void setNumberOfNormal(int numberOfNormal) {
		this.numberOfNormal = numberOfNormal;
	}

	public int getNumberOfAbnormal() {
		return numberOfAbnormal;
	}

	public void setNumberOfAbnormal(int numberOfAbnormal) {
		this.numberOfAbnormal = numberOfAbnormal;
	}

	public PointLabel getMajorityLabel() {
		return majorityLabel;
	}

	public void setMajorityLabel(PointLabel majorityLabel) {
		this.majorityLabel = majorityLabel;
	}

	public int getNumberOfInconsistency() {
		return numberOfInconsistency;
	}

	public void setNumberOfInconsistency(int numberOfInconsistency) {
		this.numberOfInconsistency = numberOfInconsistency;
	}
	
	
	

}
