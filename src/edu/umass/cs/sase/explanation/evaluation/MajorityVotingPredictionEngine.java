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
package edu.umass.cs.sase.explanation.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;

public class MajorityVotingPredictionEngine extends PredictionEngine{
	
	
	public MajorityVotingPredictionEngine(ArrayList<DataPoint> dataPoints, ArrayList<TimeSeriesFeaturePair> featureList) {
		super();
		this.dataPoints = dataPoints;
		this.featuresForPrediction= featureList;
	}
	
	public void runEngine() {
		//build intervals
		this.buildIntervals();
		//predict
		this.predictPoints();//todo
		//print
		this.printResult();
	}
	
	public void buildIntervals() {
		for (TimeSeriesFeaturePair pair : this.featuresForPrediction) {
			pair.buildIntervals();
		}
	}
	
	
	public void predictPoints() {
		int tp = 0; //true label = abnormal; predicted = abnormal
		int fp = 0;// true label = reference; predicted != reference (abnormal or mixed)
		int tn = 0;//true label = reference; predicted = reference
		int fn = 0;//true label = abnormal; predicted != abnormal (reference or mixed)
		
		LabelType predictedLabel = LabelType.Mixed;
		for (DataPoint point : this.dataPoints) {
			int abnormalCount = 0;
			int referenceCount = 0;
			int mixedCount = 0;
			for (TimeSeriesFeaturePair pair: this.featuresForPrediction) {
				predictedLabel = pair.predictPoint(point);
				if (predictedLabel == LabelType.Abnormal) {
					abnormalCount ++;
				} else if (predictedLabel == LabelType.Reference) {
					referenceCount ++;
				} else {
					mixedCount ++;
				}
			}
			/*
			if (abnormalCount >= referenceCount && abnormalCount >= mixedCount) {
				predictedLabel = LabelType.Abnormal;
			} else if (referenceCount >= mixedCount){
				predictedLabel = LabelType.Reference;
			} else {
				predictedLabel = LabelType.Mixed;
				System.out.println("Mixed, AbnormalCount=" + abnormalCount + "\tReferenceCount=" + referenceCount + "\tMixedCount" + mixedCount);
			}
			*/
			
			if (abnormalCount > referenceCount) {
				predictedLabel = LabelType.Abnormal;
			} else {
				predictedLabel = LabelType.Reference;
				//System.out.println("Mixed, AbnormalCount=" + abnormalCount + "\tReferenceCount=" + referenceCount + "\tMixedCount" + mixedCount);
			}
			if (point.trueLabel == LabelType.Abnormal) {
				if (predictedLabel == point.trueLabel) {
					tp ++;
				} else {
					fn ++;
				}
			} else {
				if (predictedLabel == point.trueLabel) {
					tn ++;
				} else {
					fp ++;
				}
			}
			
		}
		
		//https://en.wikipedia.org/wiki/Precision_and_recall
		this.precision =(double)tp / (double)(tp + fp);
		this.recall = (double)tp/ (double)(tp + fn);
		this.fMeasure = 2 * (double)tp / (double)(2 * tp + fp + fn);
		this.accuracy = (double)(tp + tn) / (double)this.dataPoints.size();
	}

}
