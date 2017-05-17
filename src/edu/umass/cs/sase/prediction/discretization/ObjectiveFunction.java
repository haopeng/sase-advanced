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
import java.util.Arrays;

import edu.umass.cs.sase.util.csv.PointLabel;
import edu.umass.cs.sase.util.csv.SamplePoint;

public class ObjectiveFunction {
	//features
	ArrayList<CutIntervalFeature> features;
	double[] betas;
	ArrayList<SamplePoint> trainingPoints;
	
	double stepSize;
	double costThreshold;
	
	double ySqureSum;
	double betaSum;
	
	int[][] predictMatrix;
	
	int[] yiDotXi;
	int yiDotXiSum;
	double[] betaYiXi;
	double betaYiXiSum;
	
	double[] betaXi;
	double betaXiSum;
	
	double currentCost;
	
	
	double tp;
	double tn;
	double fp;
	double fn;
	double recall;
	double precision;
	double accuracy;
	
	public ObjectiveFunction(ArrayList<CutIntervalFeature> features, String trainingPointsFolder) throws IOException {
		this.features = features;
		this.trainingPoints = SamplePoint.readFolder(trainingPointsFolder);
		this.betas = new double[features.size()];
		Arrays.fill(betas, 1.0); // initialize as majority voting
		
		this.stepSize = 0.000001;
		this.costThreshold = 10;
	}
	
	public void updateCost() {
		this.computeYSqureSum();
		this.computeBetaSum();
		this.computePredictMatrix();
		this.computeYiDotXi();
		this.computeBetaYiXi();
		this.computeBetaXi();
		
		this.computeCurrentCost();
	}
	
	
	public void updateUntilConvergence() {
		/*
		for (int i = 0; i < 10; i ++) {
			System.out.println("Iteration " + i + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			this.updateBeta();
		}
		*/
		double delta = Double.MAX_VALUE;
		
		int count = 0;
		while (delta > this.costThreshold) {
			count ++;
			
			System.out.println("Iteration " + count + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			this.updateBeta();
			
			double newCost = this.computeCurrentCost();
			
			delta = Math.abs(newCost - this.currentCost);
			System.out.println("===========Cost delta:" + delta );
			
			this.currentCost = newCost;
			
		}
		
		
	}
	
	public void trainObjectiveFunction() {
		double cost = Double.MAX_VALUE;
		do {
			//compute the new betas
			//recompute cost
		} while (cost > this.costThreshold);
	}
	
	public void computeYSqureSum() {
		this.ySqureSum = 0.0;
		for (SamplePoint point: this.trainingPoints) {
			this.ySqureSum += point.getTrueLabel().getLabeValue();
		}
		
		//System.out.println("ySquareSum=" + this.ySqureSum);
		//passed test
	}
	
	public void computeBetaSum() {
		this.betaSum = 0;
		for (double d : betas) {
			betaSum += d;
		}
		
		//System.out.println("betaSum=" + this.betaSum);
		//past test
	}
	
	public void computePredictMatrix() {
		this.predictMatrix = new int[this.trainingPoints.size()][this.features.size()];
		for (int i = 0; i < this.trainingPoints.size(); i ++) {
			SamplePoint point = this.trainingPoints.get(i);
			for (int j = 0; j < this.features.size(); j ++) {
				predictMatrix[i][j] = features.get(j).predictPoint(point).getLabeValue();
			}
		}
		/*
		for (int i = 0; i < this.trainingPoints.size(); i ++) {
			System.out.println("Point No." + i + "," + this.trainingPoints.get(i).getTrueLabel() + "\t");
			for (int j = 0; j < this.features.size(); j ++) {
				System.out.print(j + ":" + predictMatrix[i][j] + "\t");
			}
			System.out.println();
		}
		*/
	}
	
	public void computeYiDotXi() {
		this.yiDotXi = new int[this.features.size()];
		this.yiDotXiSum = 0;
		
		for (int i = 0; i < yiDotXi.length; i ++) {
			for (int j = 0; j < this.trainingPoints.size(); j ++) {
				yiDotXi[i] += this.trainingPoints.get(j).getTrueLabel().getLabeValue() * this.predictMatrix[j][i];
			}
			//System.out.println("yiXi[" + i + "]=" + yiDotXi[i]);
			this.yiDotXiSum += yiDotXi[i];
		}
	}
	
	public void computeBetaYiXi() {
		this.betaYiXi = new double[this.features.size()];
		this.betaYiXiSum = 0;
		
		for (int i = 0; i < betaYiXi.length; i ++) {
			betaYiXi[i] = betas[i] * yiDotXi[i];
			betaYiXiSum += betaYiXi[i];
			//System.out.println("betaYiXi[" + i + "]=" + betaYiXi[i]);
		}
	}
	
	
	public void computeBetaXi() {//todo
		this.betaXi = new double[this.features.size()];
		this.betaXiSum = 0;
		for (int i = 0; i < this.betaXi.length; i ++) {
			for (int j = 0; j < this.trainingPoints.size(); j ++) {
				betaXi[i] += this.predictMatrix[j][i];
			}
			this.betaXiSum += betaXi[i];
		}
		
	}
	public double computeCurrentCost() {
		double cost = 0;
		//first part
		double firstPart = 0;
		firstPart = betaSum * betaSum * ySqureSum;
		
		//second part
		double secondPart = 0;
		secondPart *= betaSum;
		secondPart *= this.betaYiXiSum;
		
		secondPart *= -2;
		
		
		//third part
		double thirdPart = 0;
		
		for (int i = 0; i < this.betaXi.length; i ++) {
			thirdPart += betaXi[i] * betaXi[i];
		}
		
		
		//total
		
		cost = firstPart + secondPart + thirdPart;
		
		System.out.println("Cost = " + cost);
		//update current cost
		
		return cost;
	}
	
	
	public void updateBeta() {
		double[] newBetas = new double[this.betas.length];
		
		for (int i = 0; i < newBetas.length; i ++) {
			double firstPart = 0;
			this.computeYSqureSum();
			this.computeBetaSum();
			firstPart = 2 * this.ySqureSum * this.betaSum;
			
			double secondPart = 0;
			secondPart = this.yiDotXi[i] * betas[i] * 2;
			//this.computeBetaYiXi();
			secondPart = secondPart + this.betaYiXiSum - this.betaYiXi[i];
			//this.computeYiDotXi();
			secondPart = secondPart + (betaSum - betas[i]) * this.yiDotXi[i];
			secondPart *= -2;
			
			double thirdPart = 0;
			this.computeBetaXi();
			thirdPart = 2 * this.betaXiSum;
			
			double gradient = firstPart + secondPart + thirdPart;
			
			newBetas[i] = betas[i] - gradient * this.stepSize;
			newBetas[i] = Math.max(0, newBetas[i]);
			
			if (i < 1000) {
				System.out.println(i + ": gradient = " + gradient + ", old beta=" + betas[i] + ", newBeta=" + newBetas[i]);
			}

		}
		
		this.betas = newBetas;
		
	}
	
	public PointLabel predictOnePoint(SamplePoint point) {
		//double predictedValue[] = new double[this.betas.length];
		
		double sum = 0;
		
		for (int i = 0; i < this.features.size(); i ++) {
			double predict = this.features.get(i).predictPoint(point).getLabeValue();
			sum += predict * this.betas[i];
		}
		
		sum /= this.betaSum;
		
		if (sum >= 0.5) {
			return PointLabel.Abnormal;
		} else {
			return PointLabel.Normal;
		}
		

	}
	
	public void predictPoints(ArrayList<SamplePoint> points) {
		System.out.println("Predicting...");
		//this.testPoints = SamplePoint.readFolder(testFolder);
		
		//initilize stats
		this.tp = 0;
		this.tn = 0;
		this.fp = 0;
		this.fn = 0;
		
		for (SamplePoint point : points) {
			PointLabel predicted = this.predictOnePoint(point);
			
		
			if (predicted == PointLabel.Abnormal) { //predict as abnormal
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
	
	public static void main(String args[]) throws IOException {
		String featureFilePath = "G:\\Dropbox\\research\\3rd\\code\\keel\\my data\\m14\\featureIndex.csv";
		//String intervalFilePath = "C:\\users\\haopeng\\Desktop\\extract\\fayyad.txt";
		//String intervalFilePath = "C:\\users\\haopeng\\Desktop\\extract\\modl.txt";
		
		String intervalFilePath = "C:\\users\\haopeng\\Desktop\\extract\\mantarasdistance.txt";
		
		String trainingPointFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m14\\all";
		String testPointFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m12\\all";
		ArrayList<SamplePoint> testPoints = SamplePoint.readFolder(testPointFolder);
		
		CutIntervalPredictor predictor = new CutIntervalPredictor(featureFilePath, intervalFilePath, trainingPointFolder);
		predictor.countInconsistency();//necessary for building majority label
		predictor.buildFeatures();
		
		
		predictor.predictByFeaturesSeperately();
		predictor.filterBestAndWorstFeatures();
		
		//ObjectiveFunction function = new ObjectiveFunction(predictor.features, trainingPointFolder);
		
		ObjectiveFunction function = new ObjectiveFunction(predictor.selectedFeaturesForFusion, trainingPointFolder);
		
		function.updateCost();
		//function.updateBeta();
		function.updateUntilConvergence();
		
		System.out.println("~~~~~~~~~~~~~~Test results~~~~~~~~~~");
		//function.predictPoints(testPoints);
		
	}

}
