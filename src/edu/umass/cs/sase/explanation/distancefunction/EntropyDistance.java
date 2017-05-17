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

package edu.umass.cs.sase.explanation.distancefunction;

import java.util.ArrayList;
import java.util.Collections;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.LabeledValue;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;


/*
 * 
 * Special case: one value from each time series. 
 * Treat as value features.
 */
public class EntropyDistance extends DistanceFunction{
	double distance;
	double priorClassEntropy;
	double postClassEntropy;
	double postIntervalEntropy;
	boolean isValuable;
	
	double totalSize;
	@Override
	public double getTimeSeiresDistance(TimeSeriesFeature f1,
			TimeSeriesFeature f2) throws Exception {
		//edge cases
		if (f1.getValues().size() == 0 || f2.getValues().size() == 0 || f1.getValues().get(0) == null || f2.getValues().get(0) == null) {
			this.isValuable = false;
			
			return 0;//ignore this feature because they are not comparable
		}
		
		return this.getTimeSeriesDistance2(f1, f2);
		//return this.getTimeSeiresDistanceOld(f1, f2);
	}
	
	public double getTimeSeriesDistance2(TimeSeriesFeature f1, TimeSeriesFeature f2) {
		//debug
		if (ExplanationSettings.debugMode) {
			this.printTimeSeires(f1, f2);
		}
		//sepcial case
		if (f1.getValues().size() == 1 && f2.getValues().size() == 1) {
			f1.setValue(f1.getValues().get(0));
			f2.setValue(f2.getValues().get(0));
			FeaturePair pair = new FeaturePair(f1, f2);
			pair.computeDistance();
			//todo later
			//this.distance = pair.getNormalizedDistance();
			
			this.isValuable = false;
			
			return pair.getNormalizedDistance();
		}
		
		
		//convert values to labeled values
		ArrayList<LabeledValue> labeledValues = new ArrayList<LabeledValue>();
		for (Double d: f1.getValues()) {
			labeledValues.add(new LabeledValue(d, f1.getLabel()));
		}
		
		for (Double d: f2.getValues()) {
			labeledValues.add(new LabeledValue(d, f2.getLabel()));
		}
		
		//sort
		Collections.sort(labeledValues);
		

		
		int total = labeledValues.size();
		double entropy = 0.0;
		//initialize counters
		
		
		LabelType previousLabel = labeledValues.get(0).getLabel();
		double previousValue = labeledValues.get(0).getValue();
		int abnormalCount = 0;
		int referenceCount = 0;
		int currentValueAbnormalCount = 0;
		int currentValueReferenceCount = 0;
		int pointer = 0;
		boolean isMixed = false;
		
		this.postClassEntropy = 0.0;
		this.totalSize = f1.getValues().size() + f2.getValues().size();
		
		
		while (pointer < total) {
			LabeledValue lv = labeledValues.get(pointer);
			if (lv.getValue() == previousValue) {
				if (lv.getLabel() == previousLabel || (lv.getLabel() != previousLabel && isMixed)) {
					if (lv.getLabel() == LabelType.Abnormal) {
						abnormalCount ++;
						currentValueAbnormalCount ++;
					} else {
						referenceCount ++;
						currentValueReferenceCount ++;
					}
					pointer ++;
				} else {
					entropy += this.computeEntropyOfPureInterval(total, abnormalCount - currentValueAbnormalCount, referenceCount - currentValueReferenceCount);
					
					abnormalCount = currentValueAbnormalCount;
					referenceCount = currentValueReferenceCount;
					isMixed = true;
				}
			} else {
				if (isMixed) {
					entropy += this.computeEntropyOfMixedInterval(total, currentValueAbnormalCount, currentValueReferenceCount);
					
					//post class
					this.postClassEntropy += this.computeEntropy(currentValueAbnormalCount, total) * (double) total / (double) totalSize;//weighted
					this.postClassEntropy += this.computeEntropy(currentValueReferenceCount, total) * (double) total/ (double) totalSize;
					
					previousLabel = lv.getLabel();
					previousValue = lv.getValue();
					abnormalCount = 0;
					referenceCount = 0;
					currentValueAbnormalCount = 0;
					currentValueReferenceCount = 0;
					isMixed = false;
				} else {
					if (lv.getLabel() == previousLabel) {
						
						previousValue = lv.getValue();
						currentValueAbnormalCount = 0;
						currentValueReferenceCount = 0;
						
						if (lv.getLabel() == LabelType.Abnormal) {
							abnormalCount ++;
							currentValueAbnormalCount ++;
						} else {
							referenceCount ++;
							currentValueReferenceCount ++;
						}
						pointer ++;
					} else {
						entropy += this.computeEntropyOfPureInterval(total, abnormalCount, referenceCount);
						
						previousLabel = lv.getLabel();
						previousValue = lv.getValue();
						abnormalCount = 0;
						referenceCount = 0;
						currentValueAbnormalCount = 0;
						currentValueReferenceCount = 0;
						isMixed = false;
					}
				}
			}
		}
		
		
		//compute entropy for the last interval
		
		if (isMixed) {
			entropy += this.computeEntropyOfMixedInterval(total, currentValueAbnormalCount, currentValueReferenceCount);
			//post class
			this.postClassEntropy += this.computeEntropy(currentValueAbnormalCount, total)  * (double) total/ (double) totalSize;
			this.postClassEntropy += this.computeEntropy(currentValueReferenceCount, total)  * (double) total/ (double) totalSize;
		} else {
			entropy += this.computeEntropyOfPureInterval(total, abnormalCount, referenceCount);
		}
		
		this.postIntervalEntropy = entropy;
		
		//normalize
		double originalEntropy = 0;
		originalEntropy += this.computeEntropy(f1.getValues().size(), labeledValues.size());
		originalEntropy += this.computeEntropy(f2.getValues().size(), labeledValues.size());
		
		this.priorClassEntropy = originalEntropy;
		
		//return 1.0/ entropy;
		
		double distance = originalEntropy / entropy;
		
		//debug
		if (ExplanationSettings.debugMode) {
			System.out.println("Entropy distance = " +  distance + "\tOriginal entropy=" + originalEntropy + "\t Interval entropy=" + entropy);
			//ExplanationSettings.out.println("Entropy distance = " +  distance + "\tOriginal entropy=" + originalEntropy + "\t Interval entropy=" + entropy);
		}
		
		this.decideValuable();
		
		return distance;
		
	}
	
	public void decideValuable() {
		if (this.postIntervalEntropy < 2 * this.priorClassEntropy - this.postClassEntropy) {
			this.isValuable = true;
		} else {
			this.isValuable = false;
		}
	}
	public double getTimeSeiresDistanceOld(TimeSeriesFeature f1,
			TimeSeriesFeature f2) throws Exception {
		//debug
		if (ExplanationSettings.debugMode) {
			this.printTimeSeires(f1, f2);
		}
		//convert values to labeled values
		ArrayList<LabeledValue> labeledValues = new ArrayList<LabeledValue>();
		for (Double d: f1.getValues()) {
			labeledValues.add(new LabeledValue(d, f1.getLabel()));
		}
		
		for (Double d: f2.getValues()) {
			labeledValues.add(new LabeledValue(d, f2.getLabel()));
		}
		
		//sort
		Collections.sort(labeledValues);
		//count and compute the entropy
		double entropy = 0;
		int pointer = 0;
		LabelType currentLabel = labeledValues.get(0).getLabel();
		int count = 0;
		while (pointer < labeledValues.size()) {
			LabeledValue v = labeledValues.get(pointer);
			if (v.getLabel() == currentLabel) {
				count ++;
				pointer ++;
			} else {
				//compute entorpy
				double ent = this.computeEntropy(count, labeledValues.size());
				entropy += ent;
				//reset
				count = 0;
				currentLabel = v.getLabel();
			}
		}
		
		//compute entropy for last interval
		double ent = this.computeEntropy(count, labeledValues.size());
		entropy += ent;
		
		//normalize
		double originalEntropy = 0;
		originalEntropy += this.computeEntropy(f1.getValues().size(), labeledValues.size());
		originalEntropy += this.computeEntropy(f2.getValues().size(), labeledValues.size());
		
		
		//return 1.0/ entropy;
		
		double distance = originalEntropy / entropy;
		
		//debug
		if (ExplanationSettings.debugMode) {
			System.out.println("Entropy distance = " +  distance + "\tOriginal entropy=" + originalEntropy + "\t Interval entropy=" + entropy);
			//ExplanationSettings.out.println("Entropy distance = " +  distance + "\tOriginal entropy=" + originalEntropy + "\t Interval entropy=" + entropy);
		}
		return distance;
	}
	
	public double computeEntropy(int count, int total) {
		double p = (double)count / (double)total;
		double ent = Math.log(p) / Math.log(2) * p * (-1);
		//debug
		if (ExplanationSettings.debugMode) {
			System.out.println("count=" + count + "\t total=" + total + "\t entropy =" + ent);
			//ExplanationSettings.out.println("count=" + count + "\t total=" + total + "\t entropy =" + ent);
		}
		return ent;
	}
	
	public void printTimeSeires(TimeSeriesFeature f1, TimeSeriesFeature f2) {
		System.out.println("Debug: print time series details=====:" + f1.getFeatureName());
		//ExplanationSettings.out.println("Debug: print time series details=====:" + f1.getFeatureName());
		System.out.println("Label:" + f1.getLabel());
		//ExplanationSettings.out.println("Label:" + f1.getLabel());
		for (int i = 0; i < f1.getValues().size(); i ++) {
			System.out.println(i + "\t" + f1.getTimestamps().get(i) + "\t "+ f1.getValues().get(i));
			//ExplanationSettings.out.println(i + "\t" + f1.getTimestamps().get(i) + "\t "+f1.getValues().get(i));
		}
		
		System.out.println();
		System.out.println("Label:" + f2.getLabel());
		//ExplanationSettings.out.println("\nLabel:" + f2.getLabel());
		for (int i = 0; i < f2.getValues().size(); i ++) {
			System.out.println(i + "\t" + f2.getTimestamps().get(i) + "\t "+f2.getValues().get(i));
			//ExplanationSettings.out.println(i + "\t" + f2.getTimestamps().get(i) + "\t "+f2.getValues().get(i));
		}
	}
	
	
	
	
	
	public double computeEntropyOfPureInterval(int total, int abnormalCount, int referenceCount) {
		if (abnormalCount == 0 && referenceCount == 0) {
			return 0;
		}
		
		if (abnormalCount != 0) {
			return this.computeEntropy(abnormalCount, total);
		} else {
			return this.computeEntropy(referenceCount, total);
		}
	}
	
	public double computeEntropyOfMixedIntervalOld(int total, int abnormalCount, int referenceCount) {
		if (abnormalCount == 0 && referenceCount == 0) {
			return 0;
		}
		if (abnormalCount == referenceCount) {
			double singlePointEntropy = this.computeEntropy(1, total);
			return singlePointEntropy * (abnormalCount + referenceCount);
		}
		
		int large = (abnormalCount > referenceCount? abnormalCount : referenceCount);
		int small = (abnormalCount < referenceCount? abnormalCount : referenceCount);
		
		double entropy = 0.0;
		double singlePointEntropy = this.computeEntropy(1, total);
		entropy += (small * singlePointEntropy);
		
		int stepSize = large / (small + 1);
		int remainder = large % (small + 1); 
		double stepEntropy = this.computeEntropy(stepSize, total);
		double largeStepEntropy = this.computeEntropy(stepSize + 1, total);
		entropy += ((small + 1 - remainder) * stepEntropy);
		entropy += (remainder * largeStepEntropy);
		
		return entropy;
	}

	
	public double computeEntropyOfMixedIntervalOld2(int total, int abnormalCount, int referenceCount) {
		if (abnormalCount == 0 && referenceCount == 0) {
			return 0;
		}


		//interval entropy: external entropy
		double externalEntropy = this.computeEntropy(abnormalCount + referenceCount, total);
		
		//percentage entropy: internal entropy
		int intervalTotal = abnormalCount + referenceCount;
		double internalEntropy = this.computeEntropy(abnormalCount, intervalTotal) + this.computeEntropy(referenceCount, intervalTotal);
				
		return externalEntropy + internalEntropy;
	}

	public double computeEntropyOfMixedIntervalOld3(int total, int abnormalCount, int referenceCount) {
		if (abnormalCount == 0 && referenceCount == 0) {
			return 0;
		}


		//interval entropy: external entropy
		double externalEntropy = this.computeEntropy(abnormalCount + referenceCount, total);
		
		//percentage entropy: internal entropy
		int intervalTotal = abnormalCount + referenceCount;
		double internalEntropy = (this.computeEntropy(abnormalCount, intervalTotal) + this.computeEntropy(referenceCount, intervalTotal)) * (double)intervalTotal;
				
		return externalEntropy + internalEntropy;
	}
	
	public double computeEntropyOfMixedInterval(int total, int abnormalCount, int referenceCount) {
		if (abnormalCount == 0 && referenceCount == 0) {
			return 0;
		}


		//interval entropy: external entropy
		double externalEntropy = this.computeEntropy(abnormalCount + referenceCount, total);
		
		//percentage entropy: internal entropy
		int intervalTotal = abnormalCount + referenceCount;
		double internalEntropy = this.computeEntropyOfMixedIntervalOld(intervalTotal, abnormalCount, referenceCount);//? or use total?
				
		return externalEntropy + internalEntropy;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getPriorClassEntropy() {
		return priorClassEntropy;
	}

	public void setPriorClassEntropy(double priorClassEntropy) {
		this.priorClassEntropy = priorClassEntropy;
	}

	public double getPostClassEntropy() {
		return postClassEntropy;
	}

	public void setPostClassEntropy(double postClassEntropy) {
		this.postClassEntropy = postClassEntropy;
	}

	public double getPostIntervalEntropy() {
		return postIntervalEntropy;
	}

	public void setPostIntervalEntropy(double postIntervalEntropy) {
		this.postIntervalEntropy = postIntervalEntropy;
	}

	public boolean isValuable() {
		return isValuable;
	}

	public void setValuable(boolean isValuable) {
		this.isValuable = isValuable;
	}


}
