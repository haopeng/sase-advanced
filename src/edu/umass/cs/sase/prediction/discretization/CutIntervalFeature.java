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

import java.util.ArrayList;
import java.util.List;

import edu.umass.cs.sase.util.csv.PointLabel;
import edu.umass.cs.sase.util.csv.SamplePoint;


/**
 * This class is used to represent a feature
 * @author haopeng
 *
 */
public class CutIntervalFeature {
	List<CutInterval> intervals;
	String featureSignature;
	int featureIndex;
	
	double tp;
	double tn;
	double fp;
	double fn;
	
	double recall;
	double precision;
	double accuracy;
	
	
	int total;
	int abnormalCount;
	double alpha;
	
	int inconsistent;
	
	double falsePositiveRate; // borrowed from Alexandra's paper, qi
	
	double trueU;
	double falseU;
	
	public CutIntervalFeature(List<CutInterval> intervals, String featureSignature, int featureIndex) {
		this.intervals = intervals;
		this.featureSignature = featureSignature;
		this.featureIndex = featureIndex;
		
		for (CutInterval i : intervals) {
			this.inconsistent += i.getNumberOfInconsistency();
		}
	}
	
	public PointLabel predictPoint(SamplePoint point) {
		point.setValueByFeature(this.featureSignature);
		for (CutInterval interval: this.intervals) {
			PointLabel label = interval.predictValue(point.getFeaturedValue());
			if (label != null) {
				return label;
			}
		}
		
		return null;
	}
	
	public double predictPointReturnValue(SamplePoint point) {
		point.setValueByFeature(this.featureSignature);
		for (CutInterval interval: this.intervals) {
			PointLabel label = interval.predictValue(point.getFeaturedValue());
			if (label != null) {
				if (label == PointLabel.Abnormal) {
					return 1.0;
				} else {
					return 0.0;
				}
			}
		}
		
		return 0.0;
	}
	
	public void predictPoints(ArrayList<SamplePoint> points) {
		this.total = points.size();
		this.abnormalCount = 0;
		
		for (SamplePoint point: points) {
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
			
			if (point.getTrueLabel() == PointLabel.Abnormal) {
				this.abnormalCount ++;
			}
		}
		
		
		if (this.tp + this.fn != 0) {
			this.recall = this.tp / (this.tp + this.fn) * 100;
		} else {
			this.recall = 0;
		}
		
		if (this.tp + this.fp != 0) {
			this.precision = this.tp / (this.tp + this.fp) * 100;
		} else {
			this.precision = 0;
		}
		

		this.accuracy = (this.tp + this.tn) / (this.tp + this.tn + this.fp + this.fn) * 100;
		
		this.alpha = (double)abnormalCount / (double)total;
		this.falsePositiveRate = alpha * (1 - alpha) * (1 - precision / 100) * precision / 100 * recall / 100;
		
		if (this.falsePositiveRate != 0) {
			this.trueU = this.recall / 100 / this.falsePositiveRate;
		} else {
			this.trueU = Double.MAX_VALUE;
		}
		
		if (this.falsePositiveRate != 1) {
			this.falseU = (1 - this.recall / 100) / (1 - this.falsePositiveRate);
		} else {
			this.falseU = Double.MAX_VALUE;
		}

	}
	
	public void printHeader() {
		System.out.println("No.\tName\tNumOfIntervals\tPrecision\tRecall\tAccuracy\tFalsePositiveRate\tr\\q\t(1-r)\\(1-q)");
	}
	public void printInformation() {
		System.out.println(this.featureIndex + "\t" + this.featureSignature  + "\t" + this.intervals.size() 
				+ "\t" + this.precision + "\t" + this.recall + "\t" + this.accuracy + "\t" + this.falsePositiveRate
				+ "\t" + this.trueU + "\t" + this.falseU);
	}

	public List<CutInterval> getIntervals() {
		return intervals;
	}

	public void setIntervals(List<CutInterval> intervals) {
		this.intervals = intervals;
	}

	public String getFeatureSignature() {
		return featureSignature;
	}

	public void setFeatureSignature(String featureSignature) {
		this.featureSignature = featureSignature;
	}

	public int getFeatureIndex() {
		return featureIndex;
	}

	public void setFeatureIndex(int featureIndex) {
		this.featureIndex = featureIndex;
	}

	public double getTp() {
		return tp;
	}

	public void setTp(double tp) {
		this.tp = tp;
	}

	public double getTn() {
		return tn;
	}

	public void setTn(double tn) {
		this.tn = tn;
	}

	public double getFp() {
		return fp;
	}

	public void setFp(double fp) {
		this.fp = fp;
	}

	public double getFn() {
		return fn;
	}

	public void setFn(double fn) {
		this.fn = fn;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getAbnormalCount() {
		return abnormalCount;
	}

	public void setAbnormalCount(int abnormalCount) {
		this.abnormalCount = abnormalCount;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public int getInconsistent() {
		return inconsistent;
	}

	public void setInconsistent(int inconsistent) {
		this.inconsistent = inconsistent;
	}

	public double getFalsePositiveRate() {
		return falsePositiveRate;
	}

	public void setFalsePositiveRate(double falsePositiveRate) {
		this.falsePositiveRate = falsePositiveRate;
	}

	public double getTrueU() {
		return trueU;
	}

	public void setTrueU(double trueU) {
		this.trueU = trueU;
	}

	public double getFalseU() {
		return falseU;
	}

	public void setFalseU(double falseU) {
		this.falseU = falseU;
	}
	

}
