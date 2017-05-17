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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import au.com.bytecode.opencsv.CSVReader;

public class LogisticRegressionEvaluationEngine extends EvaluationEngine{
	String logisticRegressionResultCSV;
	ArrayList<String> LRSelectedFeatures;
	
	String correlationCSV;
	HashMap<String, Double> correlationIndex;
	
	HashMap<String, HashSet<String>> clustersIndex;
	ArrayList<HashSet<String>> clusterList;

	double clusterThreshold = 0.65;
	
	
	//ground truth
	ArrayList<String> groundTruthFeatures;
	HashMap<String, HashSet<String>> gClustersIndex;
	ArrayList<HashSet<String>> gClusterList;

	
	public LogisticRegressionEvaluationEngine() {
		
	}
	
	public LogisticRegressionEvaluationEngine(String GTPath, String LRPath, String correlationCSV) throws IOException {
		this.groundTruthFilePath = GTPath;
		this.readGroundTruth();
		
		this.logisticRegressionResultCSV = LRPath;
		this.readLRResult();
		
		this.correlationCSV = correlationCSV;
		this.readCorrelations();
	}

	
	public LogisticRegressionEvaluationEngine(String GTPath, String LRPath) throws IOException {
		this.groundTruthFilePath = GTPath;
		this.readGroundTruth();
		
		this.logisticRegressionResultCSV = LRPath;
		this.readLRResult();
	}
	
	public void runEngine() {
		this.evaluate();
		this.printResult();
	}
	
	
	public void runClusterEngine() {
		this.buildClusters(this.clusterThreshold);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		this.buildGroundTruthClusters(this.clusterThreshold);
		this.evaluateForCluster();
	}
	
	public void readLRResult() throws IOException {
		//read 
		CSVReader reader = new CSVReader(new FileReader(this.logisticRegressionResultCSV));
		String[] header = reader.readNext();
		String[] values = reader.readNext();
		this.totalFeatureSpaceSize = header.length - 1;
		//filter, index
		this.LRSelectedFeatures = new ArrayList<String>();
		for (int i = 1; i < values.length; i ++) {
			if (Double.parseDouble(values[i]) != 0.0) {
				String featureName = header[i].replace('.', '-');
				this.LRSelectedFeatures.add(featureName);
				System.out.println(featureName + "\t" + values[i]);
			}
		}
		reader.close();
		
		System.out.println("Total features:" + this.LRSelectedFeatures.size());
	}
	
	public void evaluate() {
		int tp = 0;//in ground truth; in result
		int fp = 0;//not in ground truth; in result
		int tn = 0;//not in ground truth; not in result
		int fn = 0;//in ground truth; not in result
		
		for (String str: this.LRSelectedFeatures) {
			if (this.groundTruth.contains(str)) {
				tp ++;
			} else {
				fp ++;
			}
		}
		
		fn = this.groundTruth.size() - tp;
		tn = this.totalFeatureSpaceSize - this.LRSelectedFeatures.size();
		
		//precision:https://en.wikipedia.org/wiki/Precision_and_recall
		this.setPrecision = (double) tp / (double)(tp + fp);
		//recall: \mathit{TPR} = \mathit{TP} / P = \mathit{TP} / (\mathit{TP}+\mathit{FN})
		this.setRecall = (double) tp / (double)(tp + fn);
		//f-measure
		this.setFMeasure = 2 * (double) tp / (double)(2 * tp + fp + fn);
		//accuracy
		this.setAccuracy = (double)(tp + tn)/(double)this.totalFeatureSpaceSize;
	}
	
	
	public void printResult() {
		System.out.println("TotalNumOfFeatures\tSelectedFeatures\tNumOfFeaturesInGroundTruth\t" 
							+ "SetPrecision\tSetRecall\tSetF-Measure\tSetAccuracy\t");
		
		System.out.println(this.totalFeatureSpaceSize + "\t" + this.LRSelectedFeatures.size() + "\t" + this.groundTruth.size() + "\t"
							+ this.setPrecision + "\t" + this.setRecall + "\t" + this.setFMeasure + "\t" + this.setAccuracy + "\t");
	}
	//read correlations
	public void readCorrelations() throws IOException {
		this.correlationIndex = new HashMap<String, Double>();
		
		CSVReader reader = new CSVReader(new FileReader(this.correlationCSV));
		String[] line = null;
	
		int count = 0;
		while ((line = reader.readNext()) != null) {
			String key = line[0];
			double value = Double.parseDouble(line[1]);
			this.correlationIndex.put(key, value);
			//System.out.println(count ++ + ":" + key + ":" + value);
		}
		reader.close();
	}
	
	//build cluster
	
	public int buildClusters(double threshold) {

		this.clustersIndex = new HashMap<String, HashSet<String>>();
		this.clusterList = new ArrayList<HashSet<String>>();
		
		//initialize, build a cluster for each attribute
		for (int i = 0; i < this.LRSelectedFeatures.size(); i ++) {
			String feature = this.LRSelectedFeatures.get(i);
			HashSet<String> cluster = new HashSet<String>();
			cluster.add(feature);
			this.clustersIndex.put(feature, cluster);
			this.clusterList.add(cluster);
		}
		
		//build the edges and merge clusters
		int removeCount = 0;
		for (int i = 0; i < this.LRSelectedFeatures.size(); i ++) {
			String sI = this.LRSelectedFeatures.get(i);
			HashSet<String> clusterI = this.clustersIndex.get(sI);
			for (int j = i + 1; j < this.LRSelectedFeatures.size(); j ++) {
				String sJ = this.LRSelectedFeatures.get(j);
				HashSet<String> clusterJ = this.clustersIndex.get(sJ);
				
				String key = sI + "-" + sJ;
				//System.out.println("key=" + key);
				if (this.correlationIndex.get(key) != null && Math.abs(this.correlationIndex.get(key)) >= threshold && clusterI != clusterJ) {
					//merge cluster J to cluster I
					for (String f : clusterJ) {
						clusterI.add(f);
						this.clustersIndex.put(f, clusterI);
					}
					this.clusterList.remove(clusterJ);
					removeCount ++;
					//System.out.println("Remove " + sJ + " for " + sI + "  :" + (removeCount) + " " + Math.abs(this.correlationIndex.get(sI + "-" + sJ)) + " " + this.clusterList.size());
				}
			}
		}
		
		if (true) {
			System.out.println(this.LRSelectedFeatures.size() + "\t" + threshold + "\t" + this.clusterList.size());
			
			
			//print structure
			for (int i = 0; i < this.clusterList.size(); i ++) {
				HashSet<String> cluster = this.clusterList.get(i);
				System.out.print(i + " \t" + cluster.size() + ":\t");
				for (String s : cluster) {
					System.out.print(s + "\t");
				}
				System.out.println();
			}
		}
		return this.clusterList.size();
	}
	public int buildGroundTruthClusters(double threshold) {

		this.gClustersIndex = new HashMap<String, HashSet<String>>();
		this.gClusterList = new ArrayList<HashSet<String>>();
		this.groundTruthFeatures = new ArrayList<String>();
		
		for (String str: this.groundTruth) {
			groundTruthFeatures.add(str);
		}
		
		
		//initialize, build a cluster for each attribute
		for (int i = 0; i < this.groundTruthFeatures.size(); i ++) {
			String feature = this.groundTruthFeatures.get(i);
			HashSet<String> cluster = new HashSet<String>();
			cluster.add(feature);
			this.gClustersIndex.put(feature, cluster);
			this.gClusterList.add(cluster);
		}
		
		//build the edges and merge clusters
		int removeCount = 0;
		for (int i = 0; i < this.groundTruthFeatures.size(); i ++) {
			String sI = this.groundTruthFeatures.get(i);
			HashSet<String> clusterI = this.gClustersIndex.get(sI);
			for (int j = i + 1; j < this.groundTruthFeatures.size(); j ++) {
				String sJ = this.groundTruthFeatures.get(j);
				HashSet<String> clusterJ = this.gClustersIndex.get(sJ);
				
				String key = sI + "-" + sJ;
				//System.out.println("key=" + key);
				if (this.correlationIndex.get(key) != null && Math.abs(this.correlationIndex.get(key)) >= threshold && clusterI != clusterJ) {
					//merge cluster J to cluster I
					System.out.println(key + ":" + this.correlationIndex.get(key));
					for (String f : clusterJ) {
						clusterI.add(f);
						this.gClustersIndex.put(f, clusterI);
					}
					this.gClusterList.remove(clusterJ);
					removeCount ++;
					//System.out.println("Remove " + sJ + " for " + sI + "  :" + (removeCount) + " " + Math.abs(this.correlationIndex.get(sI + "-" + sJ)) + " " + this.clusterList.size());
				}
			}
		}
		
		if (true) {
			System.out.println(this.groundTruthFeatures.size() + "\t" + threshold + "\t" + this.gClusterList.size());
			
			
			//print structure
			for (int i = 0; i < this.gClusterList.size(); i ++) {
				HashSet<String> cluster = this.gClusterList.get(i);
				System.out.print(i + " \t" + cluster.size() + ":\t");
				for (String s : cluster) {
					System.out.print(s + "\t");
				}
				System.out.println();
			}
		}
		return this.gClusterList.size();
	}
	
	public boolean matchClusterPair(HashSet<String> clusterA, HashSet<String> clusterB) {
		for (String str: clusterA) {
			if (clusterB.contains(str)) {
				return true;
			}
		}
		return false;
	}
	
	
	//evaluate cluster
	public void evaluateForCluster() {
		int totalGCluster = this.gClusterList.size();
		this.goodCluster = 0;
		for (HashSet<String> cluster : this.clusterList) {
			for (HashSet<String> gCluster : this.gClusterList) {
				if (this.matchClusterPair(cluster, gCluster)) {
					goodCluster ++;
					this.gClusterList.remove(gCluster);
					break;
				}
			}
		}
		
		this.badCluster = this.clusterList.size() - this.goodCluster;
		
		int tp = this.goodCluster;
		int fp = this.badCluster;
		
		this.clusterPrecision = (double)tp/(double)(tp + fp);
		this.clusterRecall = (double)tp/(double)totalGCluster;
		this.clusterFMeasure = 2.0 * this.clusterPrecision * this.clusterRecall / (this.clusterPrecision + this.clusterRecall);
		System.out.println("Ground Truth size\tResult size\tPrecision\tRecall\tFMeasure");
		System.out.println(totalGCluster + "\t" + this.clusterList.size() + "\t" + this.clusterPrecision + "\t" + this.clusterRecall + "\t" + this.clusterFMeasure);
	}
	
	
	
	public static void main(String[] args) throws IOException {
		//use case 1
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3.truth";
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-raw.truth";
		//String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/1.csv";
		String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/1-raw.csv";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/1.csv";
		
		//use case 2
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case2/c3.truth";
		//String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/2.csv";
 		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/2.csv";
		
		//3 use case 4
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2.truth";
		//String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/3.csv";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/3.csv";
		
		//4 use case 7
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4.truth";
		//String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/4.csv";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/4.csv";
		
		//5 use case 8
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case8/c4.truth";
		//String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/5.csv";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/5.csv";
		
		//6 use case 9
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case9/a4.truth";
		//String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/6.csv";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/6.csv";
		
		//7 use case 10
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case10/b5.truth";
		//String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/7.csv";

		
		//8 use case 11
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case11/c5.truth";
		//String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/8.csv";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/8.csv";
		
		
		//9 use case 12
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5.truth";
		String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/9.csv";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/9.csv";
		*/
		//use case e7
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/environment-monitoring-missing/e7/e7.truth";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/e7.csv";
		String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/supplychain/e7.csv";
		*/
		//use case e8
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/environment-monitoring-missing/e8/e8.truth";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/e8.csv";
		String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/supplychain/e8.csv";		//use case e8
		*/
		//use case e9
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/environment-monitoring-missing/e9/e9.truth";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/e9.csv";
		String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/supplychain/e9.csv";
		*/
		//use case m10
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/material-below-standard/m10/m10.truth";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/m10.csv";
		String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/supplychain/m10.csv";
		*/
		//use case m11
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/material-below-standard/m11/m11.truth";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/m11.csv";
		String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/supplychain/m11.csv";
		*/
		//use case m12
		/*
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/material-below-standard/m12/m12.truth";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/m12.csv";
		String LRResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/logistic-regression/supplychain/m12.csv";
		*/
		/*
		//old
		LogisticRegressionEvaluationEngine engine = new LogisticRegressionEvaluationEngine(groundTruth, LRResult);
		engine.runEngine();
		*/
		
		LogisticRegressionEvaluationEngine engine = new LogisticRegressionEvaluationEngine(groundTruth, LRResult, correlationCSV);
		engine.runClusterEngine();
		
	}

}
