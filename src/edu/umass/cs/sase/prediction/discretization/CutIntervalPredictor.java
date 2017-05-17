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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.umass.cs.sase.util.csv.PointLabel;
import edu.umass.cs.sase.util.csv.SamplePoint;


/*
 * This class would build predictors based on cut intervals
 * 
 * interval tree! later
 * 
 * main goals
 * 1. compute number of inconsistencies
 * 2. compute precision
 * 3. compute recall
 */
public class CutIntervalPredictor {
	FeatureNameIndex index;
	List<List<CutInterval>> intervalList;
	ArrayList<SamplePoint> trainingPoints;
	ArrayList<SamplePoint> testPoints;
	
	ArrayList<CutIntervalFeature> features;
	HashMap<String, CutIntervalFeature> featureHM;
	
	ArrayList<CutIntervalFeature> selectedFeaturesForFusion; // filter the best and worst features, because they would dominate the results
	
	
	double tp;
	double tn;
	double fp;
	double fn;
	double recall;
	double precision;
	double accuracy;
	
	public CutIntervalPredictor(String featureFilePath, String intervalFilePath, String trainingPointFolder) throws IOException {
		this.index = new FeatureNameIndex(featureFilePath);
		this.intervalList = (new IntervalExtractor()).extractIntervals(intervalFilePath);
		this.trainingPoints = SamplePoint.readFolder(trainingPointFolder);
	}
	
	public void countInconsistency() {
		for (int i = 0; i <= this.index.getMaxIndex(); i ++) {
			
			String attrName = index.getFeatureNameByIndex(i);
			List<CutInterval> attrIntervals = intervalList.get(i);
			
			for (SamplePoint point: this.trainingPoints) {
				point.setValueByFeature(attrName);
				for (CutInterval interval: attrIntervals) {
					interval.addPoint(point);
				}
			}
			
			int countInconsistency = 0;
			for (CutInterval interval: attrIntervals) {
				interval.summarize();
				countInconsistency += interval.getNumberOfInconsistency();
			}
			
			//System.out.println(i + "\tNumberOfIntervals\t" + attrIntervals.size() +"\tNumberOfInconsistencies:" + countInconsistency);
			
			//System.out.println(i + "\t" + attrIntervals.size() +"\t" + countInconsistency);
		}
		

		
		
	}
	
	public void buildFeatures() {
		if (ScriptInterface.printDetails){
			System.out.println("building features...");
		}
		//CutIntervalFeature(List<CutInterval> intervals, String featureSignature, int featureIndex)
		this.features = new ArrayList<CutIntervalFeature>();
		this.featureHM = new HashMap<String, CutIntervalFeature>();
		
		for (int i = 0; i <= index.getMaxIndex(); i ++) {
			CutIntervalFeature f = new CutIntervalFeature(this.intervalList.get(i), index.getFeatureNameByIndex(i), i);
			this.features.add(f);
			this.featureHM.put(index.getFeatureNameByIndex(i), f);
		}
		
		if (ScriptInterface.printDetails) {
			System.out.println("building features done.");
		}
	}
	
	public void predictByFeaturesSeperately() {
		if (this.features == null) {
			this.buildFeatures();
		}
		
		if (ScriptInterface.printDetails) {
			features.get(0).printHeader();
		}
		
		for (CutIntervalFeature f: this.features) {
			f.predictPoints(this.trainingPoints);
			if (ScriptInterface.printDetails) {
				f.printInformation();
			}
		}
	}
	
	public void filterBestAndWorstFeatures() {
		if (ScriptInterface.printDetails) {
			System.out.println("~~~~~~~~~~~~Selected features~~~~~~~~~~~~~");

		}
		this.selectedFeaturesForFusion = new ArrayList<CutIntervalFeature>();
		
		for (CutIntervalFeature f : this.features) {
			if (f.falseU == Double.MAX_VALUE || f.trueU == Double.MAX_VALUE) {
				
				if (ScriptInterface.printDetails) {
					System.out.println("Feature removed:" + f.getFeatureIndex() + "." + f.getFeatureSignature());
				}
				continue;
			}
			
			this.selectedFeaturesForFusion.add(f);
			//f.printInformation();
		}
		
		if (ScriptInterface.printDetails) {
		System.out.println("\nSelected features: " + this.selectedFeaturesForFusion.size());
	
		}
	}		
	
	public void predictUsingSelectedFeaturesByMajorityVoting(String testFolder) throws IOException {
		System.out.println("Predicting...");
		//read test points
		this.testPoints = SamplePoint.readFolder(testFolder);
		//predict point using fusion

		//initilize stats
		this.tp = 0;
		this.tn = 0;
		this.fp = 0;
		this.fn = 0;
		
		for (SamplePoint point : this.testPoints) {
			double sum = 0;;
			for (CutIntervalFeature f : this.selectedFeaturesForFusion) {
				double predictValue = f.predictPointReturnValue(point);
				sum += predictValue;
			}
			
			double averagePredict = sum / this.selectedFeaturesForFusion.size();
			if (averagePredict >= 0.5) { //predict as abnormal
				if (point.getTrueLabel() == PointLabel.Abnormal) {
					this.tp ++;
				} else {
					this.fp ++;
				}
				System.out.println("Predicted as abnormal, the true label is:" + point.getTrueLabel() );
			} else {
				if (point.getTrueLabel() == PointLabel.Abnormal) {
					this.fn ++;
				} else {
					this.tn ++;
				}
				System.out.println("Predicted as normal, the true label is:" + point.getTrueLabel() );
			}
		}
		//todo
		// add precision, recall, accuracy statistics!
		
		
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
		
		System.out.println("Precision:\t" + this.precision);
		System.out.println("Recall:\t" + this.recall);
		System.out.println("Accuracy:\t" + this.accuracy);
	}
	public void predictUsingSelectedFeaturesByFusion(String testFolder) throws IOException {//fusion style
		System.out.println("Predicting...");
		//read test points
		this.testPoints = SamplePoint.readFolder(testFolder);
		//predict point using fusion

		//initilize stats
		this.tp = 0;
		this.tn = 0;
		this.fp = 0;
		this.fn = 0;
		
		for (SamplePoint point : this.testPoints) {
			double u = 1;
			for (CutIntervalFeature f : this.selectedFeaturesForFusion) {
				PointLabel predict = f.predictPoint(point);
				if (predict == PointLabel.Abnormal) {
					u *= f.trueU;
				} else {
					u *= f.falseU;
				}
			}
			
			if (u >= 0.5) {
				if (point.getTrueLabel() == PointLabel.Abnormal) {
					this.tp ++;
				} else {
					this.fp ++;
				}
				System.out.println("Predicted as abnormal, the true label is:" + point.getTrueLabel() );
			} else {
				if (point.getTrueLabel() == PointLabel.Abnormal) {
					this.fn ++;
				} else {
					this.tn ++;
				}
				System.out.println("Predicted as normal, the true label is:" + point.getTrueLabel() );
			}
		}
		//todo
		// add precision, recall, accuracy statistics!
		
		
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
		
		System.out.println("Precision:\t" + this.precision);
		System.out.println("Recall:\t" + this.recall);
		System.out.println("Accuracy:\t" + this.accuracy);
	}
	
	public void trainObjectiveFunctionUsingGradientDescent() {
		//first, filter the best features
		//train the objective function
		//todo
	}
	
	public void predictUsingObjectiveFunction() {
		//read test points
		//predict points using objective function
		//todo
	}
	
	public static void main(String[] args) throws IOException {
		String featureFilePath = "G:\\Dropbox\\research\\3rd\\code\\keel\\my data\\m14\\featureIndex.csv";
		//String intervalFilePath = "C:\\users\\haopeng\\Desktop\\extract\\fayyad.txt";
		//String intervalFilePath = "C:\\users\\haopeng\\Desktop\\extract\\modl.txt";
		
		String intervalFilePath = "C:\\users\\haopeng\\Desktop\\extract\\mantarasdistance.txt";
		
		String trainingPointFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m14\\all";
		String testPointFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m12\\all";
		
		CutIntervalPredictor predictor = new CutIntervalPredictor(featureFilePath, intervalFilePath, trainingPointFolder);
		predictor.countInconsistency();//necessary for building majority label
		predictor.predictByFeaturesSeperately();
		
		predictor.filterBestAndWorstFeatures();
		
		//predictor.predictUsingSelectedFeaturesByFusion(testPointFolder);
		predictor.predictUsingSelectedFeaturesByMajorityVoting(testPointFolder);
	}
	
}
