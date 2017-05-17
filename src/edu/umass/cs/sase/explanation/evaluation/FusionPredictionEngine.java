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

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;

public class FusionPredictionEngine extends PredictionEngine{
	ArrayList<DataPoint> trainingPoints;
	double alpha = 0.5;
	public FusionPredictionEngine(ArrayList<DataPoint> dataPoints, ArrayList<TimeSeriesFeaturePair> featureList, ArrayList<DataPoint> trainingPoints) {
		super();
		this.dataPoints = dataPoints;
		this.featuresForPrediction = featureList;
		this.trainingPoints = trainingPoints;
	}
	
	public void runEngine() {
		//build intervals
		this.buildIntervals();
		//compute statistics for each feature
		this.computeQualityForEachFeature();
		//filter features: remove features with invalid quality numbers
		this.filterFeatures();
		//predict
		this.predictPoints();
		//print
		this.printResult(); //todo
	}
	
	public void filterFeatures() {
		ArrayList<TimeSeriesFeaturePair> featureCandidates = new ArrayList<TimeSeriesFeaturePair>();
		int count = 0;
		for (TimeSeriesFeaturePair pair : this.featuresForPrediction) {
			if (pair.getQ() > 0.0 && pair.getQ() < 1.0) {
				featureCandidates.add(pair);
			} else {
				System.out.println("Feature is removed, q=" + pair.getQ() + "\t" + pair.getFeatureName() + "\t entropyDistance=" + pair.getEntropyDistance());
				count ++;
			}
		}
		//System.out.println(count + "\t features are removed due to invalidility out of total " + this.featuresForPrediction.size() + "\t features");
		this.featuresForPrediction = featureCandidates;
		/*
		System.out.println("Left features:");
		for (TimeSeriesFeaturePair pair : this.featuresForPrediction) {
			System.out.println(pair.getFeatureName() + "\t" + pair.getEntropyDistance());
		}
		*/
	}
	
	public void predictPoints() {
		int tp = 0; //true label = abnormal; predicted = abnormal
		int fp = 0;// true label = reference; predicted != reference (abnormal or mixed)
		int tn = 0;//true label = reference; predicted = reference
		int fn = 0;//true label = abnormal; predicted != abnormal (reference or mixed)
		
		LabelType predictedLabel = LabelType.Mixed;
		
		for (DataPoint point : this.dataPoints) {
			double u = 1.0;
			for (TimeSeriesFeaturePair pair: this.featuresForPrediction) {
				LabelType label = pair.predictPoint(point);
				if (label == LabelType.Abnormal) {
					u = u * pair.getR() / pair.getQ();
				} else {
					u = u * (1 - pair.getR()) / (1 - pair.getQ());
				}
			}
			
			double p = 1.0 / (1 + (1 - this.alpha) / this.alpha / u);
			if (p > this.alpha) {
				predictedLabel = LabelType.Abnormal;
			} else {
				predictedLabel = LabelType.Reference;
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

	
	public void computeQualityForEachFeature() {
		for (TimeSeriesFeaturePair pair : this.featuresForPrediction) {
			pair.computeFeatureQuality(this.trainingPoints);
		}
	}
	public void buildIntervals() {
		for (TimeSeriesFeaturePair pair : this.featuresForPrediction) {
			pair.buildIntervals();
		}
	}
}
