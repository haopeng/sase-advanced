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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

public class DecisionTreeEvaluationEngine extends LogisticRegressionEvaluationEngine{
	String decisionTreePath;
	HashSet<String> DTSelectedFeatures;
	int treeNode;
	
	public DecisionTreeEvaluationEngine(String GTPath, String DTPath, String correlationCSV) throws IOException {
		this.groundTruthFilePath = GTPath;
		this.readGroundTruth();
		
		this.decisionTreePath= DTPath;
		this.readDecisionTree();
		
		this.correlationCSV = correlationCSV;
		this.readCorrelations();
	}
	
	public DecisionTreeEvaluationEngine(String GTPath, String DTPath) throws IOException {
		this.groundTruthFilePath = GTPath;
		this.readGroundTruth();
		
		this.decisionTreePath = DTPath;
		this.readDecisionTree();
	}
	
	public void runEngine() {
		this.evaluate();//todo
		this.printResult();
	}
	public void printResult() {
		System.out.println("TotalNumOfFeatures\tNumOfTreeNodes\tSelectedFeatures\tNumOfFeaturesInGroundTruth\t" 
							+ "SetPrecision\tSetRecall\tSetF-Measure\tSetAccuracy\t");
		
		System.out.println(this.totalFeatureSpaceSize + "\t" + this.treeNode + "\t" + this.LRSelectedFeatures.size() + "\t" + this.groundTruth.size() + "\t"
							+ this.setPrecision + "\t" + this.setRecall + "\t" + this.setFMeasure + "\t" + this.setAccuracy + "\t");
	}

	
	public void readDecisionTree() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(this.decisionTreePath));
		String line;
		
		this.DTSelectedFeatures = new HashSet<String>();
		this.treeNode = 1;
		while ((line = reader.readLine()) != null) {
			String feature = this.parseLine(line);
			this.DTSelectedFeatures.add(feature);
			treeNode ++;
			//System.out.println(feature);
		}
		reader.close();
		
		
		this.LRSelectedFeatures = new ArrayList<String>();
		for (String str: this.DTSelectedFeatures) {
			this.LRSelectedFeatures.add(str);
		}
		
		System.out.println("Total features: " + this.LRSelectedFeatures.size());
		for (String str: this.LRSelectedFeatures) {
			System.out.println(str);
		}
	}
	
	public String parseLine(String line) {
		//|   |   PullFinish-attemptID-TimeSeriesMean < 0.2
		
		StringTokenizer st = new StringTokenizer(line);
		
		String feature = null;
		while(st.hasMoreTokens()) {
			feature = st.nextToken();
			if (!feature.equalsIgnoreCase("|")) {
				return feature;
			}
		}
		
		return feature;
	}
	
	
	public static void main(String[] args) throws IOException {
		//1 use case 1
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3.truth";
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-raw.truth";
		//String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/1-tree.txt";
		String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/1-raw-tree.txt";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/1.csv";
		
		//2 use case 2
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case2/c3.truth";
		//String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/2-tree.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/2.csv";
		
		//3 use case 4
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2.truth";
		//String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/3-tree.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/3.csv";
		
		//4 use case 7
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4.truth";
		//String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/4-tree.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/4.csv";
		
		//5 use case 8
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case8/c4.truth";
		//String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/5-tree.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/5.csv";
		
		//6 use case 9
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case9/a4.truth";
		//String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/6-tree.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/6.csv";
		
		//7 use case 10
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case10/b5.truth";
		//String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/7-tree.txt";

		//8 use case 11
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case11/c5.truth";
		//String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/8-tree.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/8.csv";
		
		//9 use case 12
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5.truth";
		String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/9-tree.txt";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/9.csv";
		*/
		// use case e7
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/environment-monitoring-missing/e7/e7.truth";
		String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/supplychain/e7-tree.txt";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/e7.csv";
		*/
		// use case e8
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/environment-monitoring-missing/e8/e8.truth";
		String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/supplychain/e8-tree.txt";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/e8.csv";
		*/
		// use case e9
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/environment-monitoring-missing/e9/e9.truth";
		String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/supplychain/e9-tree.txt";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/e9.csv";
		*/
		// use case m10
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/material-below-standard/m10/m10.truth";
		String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/supplychain/m10-tree.txt";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/m10.csv";		
		*/
		
		// use case m11
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/material-below-standard/m11/m11.truth";
		//String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/supplychain/m11-tree.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/m11.csv";
		
		// use case m12
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/material-below-standard/m12/m12.truth";
		String DTResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/decision-tree/supplychain/m12-tree.txt";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/m12.csv";
		*/
		
		//old feature list
		/*
		DecisionTreeEvaluationEngine engine = new DecisionTreeEvaluationEngine(groundTruth, DTResult);
		engine.runEngine();
		*/
		
		
		DecisionTreeEvaluationEngine engine = new DecisionTreeEvaluationEngine(groundTruth, DTResult, correlationCSV);
		engine.runClusterEngine();
		
		
	}
}
