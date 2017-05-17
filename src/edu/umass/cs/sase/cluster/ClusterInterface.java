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

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Input: a list of ids
 * 			The data file
 * Output: the clusters, and elements in each cluster
 * @author haopeng
 *
 */
public class ClusterInterface {
	
	String dataFile;
	double threshold;
	List<Integer> ids;
	CorrelationClusterFromScratch correlation;
	LinkedList<HashSet<Integer>> clusters;
	HashMap<Integer, HashSet<Integer>> clusterIndex;
	
	
	public ClusterInterface(String dataFile, double threshold, List<Integer> ids) throws IOException {
		this.dataFile = dataFile;
		this.threshold = threshold;
		this.ids = ids;
		this.correlation = new CorrelationClusterFromScratch(this.dataFile);
		
		this.buildClusters();
	}
	
	
	public void buildClusters() throws IOException {
		//initialize clusters and cluster index
		this.clusters = new LinkedList<HashSet<Integer>>();
		this.clusterIndex = new HashMap<Integer, HashSet<Integer>>();
		
		// build a cluster for each feature
		for (Integer i : this.ids) {
			HashSet<Integer> cluster = new HashSet<Integer>();
			cluster.add(i);
			clusters.add(cluster);
			clusterIndex.put(i, cluster);
		}
		
		//read edges and cluster
		
		for (int i = 0; i < this.ids.size(); i ++) {
			for (int j = i + 1; j < this.ids.size(); j ++) {
				int id1 = this.ids.get(i);
				int id2 = this.ids.get(j);
				double weight = this.correlation.getCorrelation(id1, id2);
				
				if (weight >= this.threshold) {
					HashSet<Integer> cluster1 = this.clusterIndex.get(id1);
					HashSet<Integer> cluster2 = this.clusterIndex.get(id2);
					
					if (cluster1 != cluster2) {
						//merge cluster2 into cluster1
						for (Integer integer : cluster2) {
							cluster1.add(integer);
							this.clusterIndex.put(integer, cluster1);
						}
						this.clusters.remove(cluster2);
						
					}
					
				}
			}
		}
		

	}
	
	public void outputClusters() {
		
		System.out.println("Total clusters:\t" + this.clusters.size() + "\t given threshold " + this.threshold);
		
		for (HashSet<Integer> cluster: this.clusters) {
			System.out.println("~~~~~~~~~~~~~");
			for (Integer i: cluster) {
				System.out.println(i + "-" + this.correlation.getAllDataSets().get(i).getFeatureName() + "\t");
			}
			System.out.println("~~~~~~~~~~~~~~");
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
		String dataFile = "/Users/haopeng/Dropbox/research/3rd/data/allPoints.csv";
		//int[] idArray = {7, 8, 9, 10, 11, 12,13, 27};//8
		int[] idArray = {34,132,7,8,9,10,14,15,145,21,22,24,27};//13
		//int[] idArray = {0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 27, 138, 141, 153};//17
		
		List<Integer> ids = new LinkedList<Integer>();
		for (int i : idArray) {
			ids.add(i);
		}
		double threshold = 0.4;//make it  as default value
		
		ClusterInterface ci = new ClusterInterface(dataFile, threshold, ids);
		//ci.buildClusters();
		ci.outputClusters();
	}

}
