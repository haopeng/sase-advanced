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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import edu.umass.cs.sase.cluster.ClusterInterface;
import edu.umass.cs.sase.util.csv.SamplePoint;

public class ScriptInterface {
	public static boolean printDetails = false;
	public static boolean printSelectedFeatures = true;
	
	public static void main(String args[]) throws IOException {
		double alpha1 = 0;
		double alpha2 = 0;
		double alpha3 = 20;
		/*windows
		String featureFilePath = "G:\\Dropbox\\research\\3rd\\code\\keel\\my data\\m14\\featureIndex.csv";
		String intervalFilePath = "C:\\users\\haopeng\\Desktop\\extract\\mantarasdistance.txt";
		String trainingPointFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m14\\all";
		String testPointFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m16\\all";
		String groundTruthPath = "G:\\Dropbox\\research\\3rd\\Results\\GroundTruth\\GroundTruth.csv";
		*/
		String featureFilePath = "/Users/haopeng/Dropbox/research/3rd/code/keel/my data/m14/featureIndex.csv";
		String intervalFilePath = "/Users/haopeng/Dropbox/research/3rd/Results/Discretization/mantarasdistance.txt";
		String trainingPointFolder = "/Users/haopeng/Copy/Data/2013/slidingwindows/m14/all";
		String testPointFolder = "/Users/haopeng/Copy/Data/2013/slidingwindows/m16/all";
		String groundTruthPath = "/Users/haopeng/Dropbox/research/3rd/Results/GroundTruth/GroundTruth.csv";
		

		
		
		
		if (args.length >= 8) {
			alpha1 = Double.parseDouble(args[0]);
			alpha2 = Double.parseDouble(args[1]);
			alpha3 = Double.parseDouble(args[2]);
			
			featureFilePath = args[3];
			intervalFilePath = args[4];
			trainingPointFolder = args[5];
			testPointFolder = args[6];
			groundTruthPath = args[7];
			
			
				
		}
		if (alpha1 == 0 && alpha2 == 0 && alpha3 == 0) {
			System.out.println("alpha1\talpha2\talpha3\tNumOfIterations\tNumOfSelectedFeatures\tPredictionPrecision\tPredictionRecall\tPredictionAccuracy\tSizeOfGroundTruth\tNoOfSelectedGroundTruth\tGTRecall\tGTPrecision\tGTFmeasure");
		}
		
		
		System.out.print(alpha1 + "\t" + alpha2 + "\t" + alpha3 + "\t");
		
		
		
		
		
		//String intervalFilePath = "C:\\users\\haopeng\\Desktop\\extract\\fayyad.txt";
		//String intervalFilePath = "C:\\users\\haopeng\\Desktop\\extract\\modl.txt";
		
		//String intervalFilePath = args[4];
		//String intervalFilePath = "C:\\users\\haopeng\\Desktop\\extract\\mantarasdistance.txt";
		
		
		
		
		
		
		
		
		
		ArrayList<SamplePoint> testPoints = SamplePoint.readFolder(testPointFolder);
		
		CutIntervalPredictor predictor = new CutIntervalPredictor(featureFilePath, intervalFilePath, trainingPointFolder);
		
		predictor.countInconsistency();//necessary for building majority label
		
		predictor.buildFeatures();
		
		
		predictor.predictByFeaturesSeperately();
		
		predictor.filterBestAndWorstFeatures();
		
		//filtered features
		NewObjectiveFunctionWithMultiPenalty function = new NewObjectiveFunctionWithMultiPenalty(alpha1, alpha2, alpha3, predictor.selectedFeaturesForFusion, trainingPointFolder);
		
		//full features
		//NewObjectiveFunctionWithMultiPenalty function = new NewObjectiveFunctionWithMultiPenalty(alpha1, alpha2, alpha3, predictor.features, trainingPointFolder);
		
		//NewObjectiveFunctionWithMultiPenalty function = new NewObjectiveFunctionWithMultiPenalty(predictor.selectedFeaturesForFusion, trainingPointFolder);
		
		
		
		
		
		
		function.updateCost();
		
		function.updateUntilConvergence();
		
		//System.out.println("~~~~~~~~~~~~~~Test results~~~~~~~~~~");
		function.predictPoints(testPoints);
		
		
		
		
		HashSet<Integer>finalResults = function.compareWithGroundTruth(groundTruthPath);
		
		
		String dataFile = "/Users/haopeng/Dropbox/research/3rd/data/allPoints.csv";
		double threshold = 0.4;
		List<Integer> ids = new LinkedList<Integer>(finalResults);
		ClusterInterface ci = new ClusterInterface(dataFile, threshold, ids);
		ci.outputClusters();
		
		
		System.out.println();
		
	}

}
