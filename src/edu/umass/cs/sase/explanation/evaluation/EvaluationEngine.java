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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;

/**
 * Used to evaluate the ground truth
 * Input: ground truth; results from some algorithms
 * Output: the precision/recall/f-measure of the results
 * @author haopeng
 *
 */
public class EvaluationEngine {
	String groundTruthFilePath;
	HashSet<String> groundTruth;
	
	ArrayList<HashSet<String>> clusterList;
	HashMap<String, TimeSeriesFeaturePair> topFeatureIndex;
	int totalFeatureSpaceSize;
	
	//set
	double setPrecision;
	double setRecall;
	double setFMeasure;
	double setAccuracy;
	//cluster
	int goodCluster;
	int badCluster;
	double clusterPrecision;
	double clusterRecall;
	double clusterFMeasure;
	double clusterAccuracy;
	
	public EvaluationEngine() {
		
	}
	
	public EvaluationEngine(String groundTruthFilePath) throws IOException {
		this.groundTruthFilePath = groundTruthFilePath;
		this.readGroundTruth();
	}
	
	/**
	 * 
	 * @param clusterList the clustered results
	 * @param featureSet all features in one set
	 */
	public void evaluateResult(ArrayList<HashSet<String>> clusterList, HashMap<String, TimeSeriesFeaturePair> topFeatureIndex, int totalFeatures) {
		this.clusterList = clusterList;
		this.topFeatureIndex = topFeatureIndex;
		this.totalFeatureSpaceSize = totalFeatures;
		//evaluate precision recall for top features
		this.evaluateForFeatureSet();
		//count good clusters, and compute precision/recall for cluster
		this.evaluateForCluster();
		//print results
		this.printResult();
		
	}
	
	
	public void printResult() {
		System.out.println("TotalNumOfFeatures\tSelectedFeatures\tNumOfClusters\tNumOfFeaturesInGroundTruth\t" 
							+ "SetPrecision\tSetRecall\tSetF-Measure\tSetAccuracy\t"
							+ "NumOfGoodCluster\tNumOfBadCluster\tClusterPrecision");
		
		System.out.println(this.totalFeatureSpaceSize + "\t" + this.topFeatureIndex.size() + "\t" + this.clusterList.size() + "\t" + this.groundTruth.size() + "\t"
							+ this.setPrecision + "\t" + this.setRecall + "\t" + this.setFMeasure + "\t" + this.setAccuracy + "\t"
							+ this.goodCluster + "\t" + this.badCluster + "\t" + this.clusterPrecision);
	}
	
	/**
	 * How to define precision/recall for cluster??
	 */
	public void evaluateForCluster() {
		this.goodCluster = 0;
		for (HashSet<String> cluster : this.clusterList) {
			for (String str : cluster) {
				if (this.groundTruth.contains(str)) {
					goodCluster ++;
					break;
				}
			}
		}
		
		this.badCluster = this.clusterList.size() - this.goodCluster;
		
		int tp = this.goodCluster;
		int fp = this.badCluster;
		
		this.clusterPrecision = (double)tp/(double)(tp + fp);
	}

	
	public void evaluateForFeatureSet() {
		int tp = 0;//in ground truth; in result
		int fp = 0;//not in ground truth; in result
		int tn = 0;//not in ground truth; not in result
		int fn = 0;//in ground truth; not in result
		
		for (String str: this.topFeatureIndex.keySet()) {
			if (this.groundTruth.contains(str)) {
				tp ++;
			} else {
				fp ++;
			}
		}
		
		fn = this.groundTruth.size() - tp;
		tn = this.totalFeatureSpaceSize - this.topFeatureIndex.size();
		
		//precision:https://en.wikipedia.org/wiki/Precision_and_recall
		this.setPrecision = (double) tp / (double)(tp + fp);
		//recall: \mathit{TPR} = \mathit{TP} / P = \mathit{TP} / (\mathit{TP}+\mathit{FN})
		this.setRecall = (double) tp / (double)(tp + fn);
		//f-measure
		this.setFMeasure = 2 * (double) tp / (double)(2 * tp + fp + fn);
		//accuracy
		this.setAccuracy = (double)(tp + tn)/(double)this.totalFeatureSpaceSize;
	}
	
	
	
	public void readGroundTruth() throws IOException {
		this.groundTruth = new HashSet<String>();
		

		BufferedReader reader = new BufferedReader(new FileReader(this.groundTruthFilePath));
		String line;
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("#")) {
				this.groundTruth.add(line);				
			}
		}
		reader.close();
		
		System.out.println("~~~~~~~~" + this.groundTruth.size() + " features are read as ground truth~~~~~~~~");
		for (String str : groundTruth) {
			System.out.println(str);
		}
		System.out.println("~~~~~~~End of ground truth~~~~~~~~~~");
	}
	
	public static void main(String[] args) throws IOException {
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3.truth";
		EvaluationEngine engine = new EvaluationEngine(groundTruth);
	}
}
