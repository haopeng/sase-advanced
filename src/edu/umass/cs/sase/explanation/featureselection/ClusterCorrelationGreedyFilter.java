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
package edu.umass.cs.sase.explanation.featureselection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;

/**
 * If two featuers are in the same cluster, then their correlation set to 1
 * Otherwise it is 0
 * @author haopeng
 *
 */
public class ClusterCorrelationGreedyFilter extends GreedyIncreaseFilter{
	HashMap<TimeSeriesFeaturePair, HashSet<TimeSeriesFeaturePair>> clustersIndex;
	ArrayList<HashSet<TimeSeriesFeaturePair>> clusterList;
	public ClusterCorrelationGreedyFilter(ArrayList<TimeSeriesFeaturePair> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}
	
	public void filter(int iteration) throws IOException {
		this.iteration = iteration;
		
		this.preFilter();
		
		//remove jobIds and taskIds from top 30 features
		

		this.preFilteredFeatures.remove(35);
		this.preFilteredFeatures.remove(34);
		this.preFilteredFeatures.remove(33);
		this.preFilteredFeatures.remove(28);
		this.preFilteredFeatures.remove(27);
		this.preFilteredFeatures.remove(26);
		this.preFilteredFeatures.remove(14);
		this.preFilteredFeatures.remove(10);
		
		//debug, manual remove
		
		/*
		this.preFilteredFeatures.remove(20);
		this.preFilteredFeatures.remove(19);
		this.preFilteredFeatures.remove(18);
		this.preFilteredFeatures.remove(17);
		this.preFilteredFeatures.remove(16);
		this.preFilteredFeatures.remove(14);
		this.preFilteredFeatures.remove(12);
		this.preFilteredFeatures.remove(11);
		this.preFilteredFeatures.remove(10);
		this.preFilteredFeatures.remove(8);
		this.preFilteredFeatures.remove(7);
		this.preFilteredFeatures.remove(6);
		this.preFilteredFeatures.remove(3);
		*/
		
		System.out.println("~~~~~~~PreFilter without correlations result: " + this.preFilteredFeatures.size() + " features are selected~~~~~~~~~");
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			System.out.println(i + "\t" + this.preFilteredFeatures.get(i).getFeatureName() + "\t" + this.preFilteredFeatures.get(i).getRecentDistance());
		}
		this.computeCorrelationIndex();
		this.computeClusterCorrelation();
		
		this.linesToWrite = new ArrayList<String[]>();
		this.linesToWriteSimple = new ArrayList<String[]>();
		for (int i = 0; i < this.iteration; i ++) {
			this.resetMultilpleCorrelations();
			this.fastestSelect(i);
		}
		
		this.visualize("Distance Gain of Greedy Selection");
		this.saveToCSV();

	}
	
	public void computeClusterCorrelation() {
		int topK = Math.min(155, this.preFilteredFeatures.size());
		double threshold = 0.75;
		
		this.clustersIndex = new HashMap<TimeSeriesFeaturePair, HashSet<TimeSeriesFeaturePair>>();
		this.clusterList = new ArrayList<HashSet<TimeSeriesFeaturePair>>();
		
		//initialize, build a cluster for each attribute
		for (int i = 0; i < topK; i ++) {
			TimeSeriesFeaturePair feature = this.preFilteredFeatures.get(i);
			HashSet<TimeSeriesFeaturePair> cluster = new HashSet<TimeSeriesFeaturePair>();
			cluster.add(feature);
			this.clustersIndex.put(feature, cluster);
			this.clusterList.add(cluster);
		}
		
		//build the edges and merge clusters
		int removeCount = 0;
		for (int i = 0; i < topK; i ++) {
			TimeSeriesFeaturePair sI = this.preFilteredFeatures.get(i);
			HashSet<TimeSeriesFeaturePair> clusterI = this.clustersIndex.get(sI);
			for (int j = i + 1; j < topK; j ++) {
				TimeSeriesFeaturePair sJ = this.preFilteredFeatures.get(j);
				HashSet<TimeSeriesFeaturePair> clusterJ = this.clustersIndex.get(sJ);
				
				String key = sI.getFeatureName() + "-" + sJ.getFeatureName();
				if (Math.abs(this.correlationIndex.get(key)) >= threshold && clusterI != clusterJ) {
					//merge cluster J to cluster I
					for (TimeSeriesFeaturePair f : clusterJ) {
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
	}
	
	
	
	public double computeIncreaseGainWithTreshold(TimeSeriesFeaturePair pair, ArrayList<TimeSeriesFeaturePair> selectedPairs, double threshold) {
		for (TimeSeriesFeaturePair f: selectedPairs) {
			if (this.clustersIndex.get(pair) == this.clustersIndex.get(f)) {
				return 0;
			} 
		}
		
		return pair.getRecentDistance();		
	}
	
}
