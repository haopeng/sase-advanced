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

import edu.umass.cs.sase.util.csv.SamplePoint;

public class NewObjectiveFunctionWithPenaltyOnSize extends NewObjectiveFunction{
	
	double alpha1;

	public NewObjectiveFunctionWithPenaltyOnSize(
			ArrayList<CutIntervalFeature> features, String trainingPointsFolder)
			throws IOException {
		super(features, trainingPointsFolder);
		// TODO Auto-generated constructor stub
		
		this.alpha1 = 0;
	}
	
	
	public double computeCurrentCost() {
		double cost = 0;
		//first part
		double firstPart = 0;
		firstPart = ySqureSum;
		
		//second part
		double secondPart = 0;
		secondPart += this.betaYiXiSum;			
		secondPart *= -2;
		
		
		//third part
		double thirdPart = 0;
		
		for (int i = 0; i < this.betaXi.length; i ++) {
			thirdPart += betaXi[i] * betaXi[i];
		}
		//total
		
		
		double fourthPart = this.alpha1 * this.betaSum;
		
		cost = firstPart + secondPart + thirdPart + fourthPart;
		
		if (ScriptInterface.printDetails){
			System.out.println("Cost = " + cost);
		}

		//update current cost
		
		//System.out.println("hi?????????????????????????????????????????????????????????????????????????????????????????????????");

		
		return cost;
	}
	
	public void updateBeta() {
		double[] newBetas = new double[this.betas.length];
		
		for (int i = 0; i < newBetas.length; i ++) {
			double firstPart = 0;
			
			double secondPart = 0;
			this.computeYiDotXi();
			secondPart = this.yiDotXi[i];
			secondPart *= -2;
			
			double thirdPart = 0;
			this.computeBetaXi();
			thirdPart = 2 * this.betaXiSum;
			
			double fourthPart = this.alpha1;
			
			double gradient = firstPart + secondPart + thirdPart + fourthPart;
			
			newBetas[i] = betas[i] - gradient * this.stepSize;
			newBetas[i] = Math.max(0, newBetas[i]); // todo: write in paper, and explain!
			
			if (i < 1000) {
				System.out.println(i + ": gradient = " + gradient + ", old beta=" + betas[i] + ", newBeta=" + newBetas[i]);
			}
			
			

		}
		
		this.betas = newBetas;
		
		int nonZeroCount = 0;
		for (double d : this.betas) {
			if (d > 0) {
				nonZeroCount ++;
			}
		}
		System.out.println("~~~~~~~~~~~~~~" + nonZeroCount + " features selected from " + this.features.size() + " features");

		
		//System.out.println("hi!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
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
		
		NewObjectiveFunctionWithPenaltyOnSize function = new NewObjectiveFunctionWithPenaltyOnSize(predictor.features, trainingPointFolder);
		
		//NewObjectiveFunctionWithPenaltyOnSize function = new NewObjectiveFunctionWithPenaltyOnSize(predictor.selectedFeaturesForFusion, trainingPointFolder);
		
		function.updateCost();
		//function.updateBeta();
		function.updateUntilConvergence();
		
		System.out.println("~~~~~~~~~~~~~~Test results~~~~~~~~~~");
		function.predictPoints(testPoints);
		
		
		String groundTruthPath = "G:\\Dropbox\\research\\3rd\\Results\\GroundTruth\\GroundTruth.csv";
		function.compareWithGroundTruth(groundTruthPath);
		
	}

}
