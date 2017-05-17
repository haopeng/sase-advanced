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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class is used to cluster features using correlation graph
 * The output format is text in console
 * To see visualization, please use GephiCluster.java
 * @author haopeng
 *
 */
public class TextCluster extends GephiCluster{
	HashMap<String, HashSet<String>> clustersIndex;
	ArrayList<HashSet<String>> clusterList;
	public TextCluster(String featureCSV, String correlationCSV)
			throws IOException {
		super(featureCSV, correlationCSV);
	}
	
	public int buildClusters(double threshold, int topK) {
		this.clustersIndex = new HashMap<String, HashSet<String>>();
		this.clusterList = new ArrayList<HashSet<String>>();
		
		//initialize, build a cluster for each attribute
		for (int i = 0; i < topK; i ++) {
			String feature = this.features.get(i);
			HashSet<String> cluster = new HashSet<String>();
			cluster.add(feature);
			this.clustersIndex.put(feature, cluster);
			this.clusterList.add(cluster);
		}
		
		//build the edges and merge clusters
		int removeCount = 0;
		for (int i = 0; i < topK; i ++) {
			String sI = this.features.get(i);
			HashSet<String> clusterI = this.clustersIndex.get(sI);
			for (int j = i + 1; j < topK; j ++) {
				String sJ = this.features.get(j);
				HashSet<String> clusterJ = this.clustersIndex.get(sJ);
				
				String key = sI + "-" + sJ;
				if (Math.abs(this.correlationIndex.get(key)) >= threshold && clusterI != clusterJ) {
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
		
		System.out.println(topK + "\t" + threshold + "\t" + this.clusterList.size());
		
		
		//print structure
		for (int i = 0; i < this.clusterList.size(); i ++) {
			HashSet<String> cluster = this.clusterList.get(i);
			System.out.print(i + " \t" + cluster.size() + ":\t");
			for (String s : cluster) {
				System.out.print(s + "\t");
			}
			System.out.println();
		}
		
		
		return this.clusterList.size();
	}
	
	
	public void buildMatrix(int maxK, double stepSize, int steps) {
		double[][] sizeMatrix =new double[maxK + 1][steps + 1];
		//header
		for (int s = 1; s <= steps; s ++) {
			sizeMatrix[0][s] = stepSize * (double)s;
		}
		for (int k = 1; k <= maxK; k ++) {
			sizeMatrix[k][0] = k;
			for (int s = 1; s <= steps; s ++) {
				int size = this.buildClusters(stepSize * (double)s, k);
				sizeMatrix[k][s] = size;
			}
		}
		
		
		System.out.println("~~~~~~~~~~~~~~~~~Size Matrix~~~~~~~~~~~~~~~~~");
		for (int i = 0; i <= maxK; i ++) {
			for (int j = 0; j <= steps; j ++) {
				System.out.print(sizeMatrix[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println("~~~~~~~~~~~~~~End of Size Matrix~~~~~~~~~~~~~~");
	}
	
	
	public void buildClusterForSelectedFeatures(ArrayList<String> selectedFeatures) {
		this.features = selectedFeatures;
		this.buildClusters(0.75, selectedFeatures.size());
	}
	
	public static void main(String[] args) throws IOException {
		String featureCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/features.csv";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/correlationindex-full.csv";
		
		
		TextCluster cluster = new TextCluster(featureCSV, correlationCSV);
		//cluster.buildMatrix(155, 0.05, 20);
		
		//cluster.buildClusters(0.75, 29);
		
		/*
		for (int i = 1; i <= 162; i ++) {
			for (double t = 0.05; t <=1.03; t += 0.05) {
				cluster.buildClusters(t, i);
			}
		}
		*/
		
		
		/*
		//selected features
		
		//remove
		String[] strs2 = {"HadoopDataActivity-TimeSeriesFrequency",
				"MapStart-nodeNumber-TimeSeriesMean",
				"mem_free_event-value-TimeSeriesMean",
				"RequestStart-attemptID-TimeSeriesMean",
				"MapFinish-nodeNumber-TimeSeriesMean",
				"cpu_idle_event-value-TimeSeriesMean",
				"balance-value",
				"MergeFinish-TimeSeriesFrequency",
				"RequestStart-jobId-TimeSeriesMean",
				"balance-value-TimeSeriesMean"};
		//full
		String[] strs3 = {"proc_total_event-value-TimeSeriesMean",
				"RequestFinish-jobId-TimeSeriesMean",
				"pkts_out_event-value-TimeSeriesMean",
				"mem_free_event-value-TimeSeriesMean",
				"MapStart-nodeNumber-TimeSeriesMean",
				"MapFinish-nodeNumber-TimeSeriesMean",
				"MapPeriod-value-TimeSeriesMean",
				"PullStart-TimeSeriesFrequency",
				"balance-value",
				"MergeStart-TimeSeriesFrequency"};
		
		//top 8
		String[] strs8 = {
				"RequestFinish-jobId-TimeSeriesMean",
				"pkts_out_event-value-TimeSeriesMean",
				"mem_free_event-value-TimeSeriesMean",
				"MapPeriod-value-TimeSeriesMean",
				"MapStart-nodeNumber-TimeSeriesMean",
				"balance-value",
				"MergeStart-TimeSeriesFrequency",
				"MapPeriod-nodeNumber-TimeSeriesMean",
				"HadoopDataActivity-jobId-TimeSeriesMean"
		};
		
		
		//all top10 with threshold
		String[] strs ={
				"MergeStart-TimeSeriesFrequency",
				"balance-value-TimeSeriesMean",
				"MapPeriod-nodeNumber-TimeSeriesMean",
				"MapPeriod-value-TimeSeriesMean",
				"RequestFinish-jobId-TimeSeriesMean",
				"mem_free_event-value-TimeSeriesMean",
				"MapStart-nodeNumber-TimeSeriesMean",
				"pkts_out_event-value-TimeSeriesMean",
				"HadoopDataActivity-jobId-TimeSeriesMean",
				"ReduceStart-TimeSeriesFrequency",
				"swap_free_event-value-TimeSeriesMean",
				"PullStart-TimeSeriesFrequency",
				"HadoopDataActivity-TimeSeriesFrequency",
				"cpu_idle_event-value-TimeSeriesMean",
				"HadoopDataActivity-value-TimeSeriesMean",
				"proc_total_event-value-TimeSeriesMean",
				"PullFinish-TimeSeriesFrequency",
				"PullPeriod-TimeSeriesFrequency",
				"bytes_in_event-value-TimeSeriesMean"
		};
		
		
		ArrayList<String> selected = new ArrayList<String>();
		for (String s : strs) {
			selected.add(s);
		}
		
		cluster.buildClusterForSelectedFeatures(selected);
		*/
		
		/*
		 
		 //progress
		String[] strs ={
				"HadoopDataActivity-TimeSeriesFrequency",
				"MapPeriod-value-TimeSeriesMean",
				"mem_free_event-value-TimeSeriesMean",
				"swap_free_event-value-TimeSeriesMean",
				"RequestFinish-TimeSeriesFrequency",
				"mem_buffers_event-value-TimeSeriesMean",
				"proc_total_event-value-TimeSeriesMean"
		};
		*/
		//percentage
		String[] strs ={
				"proc_total_event-value-TimeSeriesMean",
				"HadoopDataActivity-TimeSeriesFrequency",
				"mem_free_event-value-TimeSeriesMean",
				"swap_free_event-value-TimeSeriesMean",
				"mem_cached_event-value-TimeSeriesMean",
				"mem_buffers_event-value-TimeSeriesMean"

		};
		
		ArrayList<String> selected = new ArrayList<String>();
		for (String s : strs) {
			selected.add(s);
		}
		
		cluster.buildClusterForSelectedFeatures(selected);
		
	}
	


}
