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
package edu.umass.cs.sase.cluster;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import au.com.bytecode.opencsv.CSVReader;


/**
 * Input: the correlation between different features
 * 			The threshold of correlation
 * 			the feature names and ids
 * Output: the cluster between these features
 * @author haopeng
 *
 */
public class CorrelationClusterAnalyzerFromExcelResults {
	String featureFile;
	String correlationFile;
	double threshold;
	
	HashMap<Integer, String> features;// id to feature name
	LinkedList<HashSet<Integer>> clusters;
	HashMap<Integer, HashSet<Integer>> clusterIndex;
	
	public CorrelationClusterAnalyzerFromExcelResults(String featureFile, String correlationFile, double threshold) throws IOException {
		this.featureFile = featureFile;
		this.correlationFile = correlationFile;
		this.threshold = threshold;
		
		this.readFeatures();
		this.readCorrelation();
		
		
	}
	
	public void readFeatures() throws IOException {
		CSVReader reader = new CSVReader(new FileReader(this.featureFile));
		reader.readNext();
		this.features = new HashMap<Integer, String>();
		
		String[] line;
		while((line = reader.readNext()) != null) {
			int id = Integer.parseInt(line[0]);
			String name = line[1];
			
			features.put(id, name);
		}
		
		reader.close();
		

		
	}
	
	public void readCorrelation() throws IOException {
		//initialize clusters and cluster index
		this.clusters = new LinkedList<HashSet<Integer>>();
		this.clusterIndex = new HashMap<Integer, HashSet<Integer>>();
		
		// build a cluster for each feature
		for (Integer i : features.keySet()) {
			HashSet<Integer> cluster = new HashSet<Integer>();
			cluster.add(i);
			clusters.add(cluster);
			clusterIndex.put(i, cluster);
		}
		
		//read edges and cluster
		
		CSVReader reader = new CSVReader(new FileReader(this.correlationFile));
		reader.readNext();
		String[] line;
		
		while ((line = reader.readNext()) != null) {
			int id1 = Integer.parseInt(line[0]);
			int id2 = Integer.parseInt(line[1]);
			double weight = Double.parseDouble(line[3]);
			
			if (weight >= this.threshold) {
				HashSet<Integer> cluster1 = this.clusterIndex.get(id1);
				HashSet<Integer> cluster2 = this.clusterIndex.get(id2);
				
				if (cluster1 != cluster2) {
					//merge cluster2 into cluster1
					for (Integer i : cluster2) {
						cluster1.add(i);
						this.clusterIndex.put(i, cluster1);
					}
					this.clusters.remove(cluster2);
					
				}
				
			}else {
				reader.close();
				return;
			}
			
		}
		
		reader.close();
	}
	
	
	public void outputClusters() {
		
		System.out.println("Total clusters:\t" + this.clusters.size() + "\t given threshold " + this.threshold);
		
		for (HashSet<Integer> cluster: this.clusters) {
			System.out.println("~~~~~~~~~~~~~");
			for (Integer i: cluster) {
				System.out.println(i + "-" + this.features.get(i) + "\t");
			}
			System.out.println("~~~~~~~~~~~~~~");
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		//8
		//String featureFile = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/1.visualization/correlation/groundTruth8/nodes.csv";
		//String correlationFile = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/1.visualization/correlation/groundTruth8/edges.csv";
		
		//17
		//String featureFile = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/1.visualization/correlation/groundTruth17/nodes.csv";
		//String correlationFile = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/1.visualization/correlation/groundTruth17/edges.csv";

		//all
		String featureFile = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/1.visualization/correlation/all/nodes.csv";
		String correlationFile = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/1.visualization/correlation/all/edges.csv";
		
		
		double threshold = 0.5;
		
		CorrelationClusterAnalyzerFromExcelResults analyzer = new CorrelationClusterAnalyzerFromExcelResults(featureFile, correlationFile, threshold);
		
		analyzer.outputClusters();
		
	}
}
