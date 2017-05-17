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
package edu.umass.cs.sase.explanation.crossvalidation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featureselection.ClusterCorrelationGreedyFilter;
import edu.umass.cs.sase.explanation.featureselection.GreedyIncreaseFilter;


/**
 * This class is used to validate correlations, no use right now.
 * @author haopeng
 *
 */
public class CorrelationValidation extends FeatureValidation{
	HashMap<String, Double> labeledIndex;
	HashMap<String, Double> classifiedIndex;
	
	HashMap<String, Double> labeledAbnormalIndex;
	HashMap<String, Double> classifiedAbnormalIndex;
	
	HashMap<String, Double> labeledReferenceIndex;
	HashMap<String, Double> classifiedReferenceIndex;
	
	
	GreedyIncreaseFilter labeledFilter;
	GreedyIncreaseFilter classifiedFilter;
	public CorrelationValidation(String labeled, String classified)
			throws Exception {
		super(labeled, classified);
		this.buildCorrelationIndex();
	}
	
	public void buildCorrelationIndex() throws Exception {
		this.labeledComparator.computeEntropyDistance(700);
		ArrayList<TimeSeriesFeaturePair> labeledSortedFeatures = this.labeledComparator.returnAllTimeSeriesFeatureListRanked();
		labeledFilter = new GreedyIncreaseFilter(labeledSortedFeatures);
		labeledFilter.preFilter();
		labeledFilter.computeCorrelationIndex();
		labeledFilter.computeAbnormalCorrelationIndex();
		labeledFilter.computeReferenceCorrelationIndex();
		labeledIndex = labeledFilter.getCorrelationIndex();
		labeledAbnormalIndex = labeledFilter.getAbnormalCorrelationIndex();
		labeledReferenceIndex = labeledFilter.getReferenceCorrelationIndex();
				
		this.classifiedComparator.computeEntropyDistance(700);
		ArrayList<TimeSeriesFeaturePair> classifiedSortedFeatures = this.classifiedComparator.returnAllTimeSeriesFeatureListRanked();
		classifiedFilter = new GreedyIncreaseFilter(classifiedSortedFeatures);
		classifiedFilter.preFilter();
		classifiedFilter.computeCorrelationIndex();
		classifiedFilter.computeAbnormalCorrelationIndex();
		classifiedFilter.computeReferenceCorrelationIndex();
		classifiedIndex = classifiedFilter.getCorrelationIndex();
		classifiedAbnormalIndex = classifiedFilter.getAbnormalCorrelationIndex();
		classifiedReferenceIndex = classifiedFilter.getReferenceCorrelationIndex();
	}
	
	public void validateCorrelations() throws Exception {
		ArrayList<String> validatedFeatures = new ArrayList<String>();
		ArrayList<String> invalidatedFeatures = new ArrayList<String>();
		for (String s: labeledIndex.keySet()) {
			Double v1 = labeledIndex.get(s);
			Double v2 = classifiedIndex.get(s);
			if (v1 < 0.75) {
				continue;
			}
			if (v2 == null || Math.abs(v1 - v2) > 0.25) {
				invalidatedFeatures.add(s);
				System.out.println("Invalidated\tv1=\t" + v1 + "\tv2=\t" + v2 );
			} else {
				validatedFeatures.add(s);
				System.out.println("Validated\tv1=\t" + v1 + "\tv2=\t" + v2 );
			}
			
		}
		
		System.out.println("~~~~~~~Validated~~~~~~~");
		for (int i = 0; i < validatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + validatedFeatures.get(i));
		}
		
		System.out.println("~~~~~~~Invalidated~~~~~~~");
		for (int i = 0; i < invalidatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + invalidatedFeatures.get(i));
		}
		
		System.out.println("Validated correlations:\t" + validatedFeatures.size());
		System.out.println("Invalidated correlations:\t" + invalidatedFeatures.size());
		System.out.println("labeld sorted features\t" + labeledFilter.getPreFilteredFeatures().size());
		System.out.println("classified sorted features\t" + classifiedFilter.getPreFilteredFeatures().size());
		System.out.println("labeled index size\t" + labeledIndex.size());
		System.out.println("classified index size\t" + classifiedIndex.size());
		double vPercentage = (double)validatedFeatures.size()/(double)(validatedFeatures.size() + invalidatedFeatures.size());
		System.out.println("Validated percentage:\t" + vPercentage);
	}
	
	public void validateAbnormalCorrelations() throws Exception {
		ArrayList<String> validatedFeatures = new ArrayList<String>();
		ArrayList<String> invalidatedFeatures = new ArrayList<String>();
		for (String s: labeledAbnormalIndex.keySet()) {
			Double v1 = labeledAbnormalIndex.get(s);
			Double v2 = classifiedAbnormalIndex.get(s);
			if (v1 < 0.75) {
				continue;
			}
			if (v2 == null || Math.abs(v1 - v2) > 0.25) {
				invalidatedFeatures.add(s);
				System.out.println("Invalidated\tv1=\t" + v1 + "\tv2=\t" + v2 );
			} else {
				validatedFeatures.add(s);
				System.out.println("Validated\tv1=\t" + v1 + "\tv2=\t" + v2 );
			}
			
		}
		
		System.out.println("~~~~~~~Validated~~~~~~~");
		for (int i = 0; i < validatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + validatedFeatures.get(i));
		}
		
		System.out.println("~~~~~~~Invalidated~~~~~~~");
		for (int i = 0; i < invalidatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + invalidatedFeatures.get(i));
		}
		
		System.out.println("Validated correlations:\t" + validatedFeatures.size());
		System.out.println("Invalidated correlations:\t" + invalidatedFeatures.size());
		System.out.println("labeld sorted features\t" + labeledFilter.getPreFilteredFeatures().size());
		System.out.println("classified sorted features\t" + classifiedFilter.getPreFilteredFeatures().size());
		double vPercentage = (double)validatedFeatures.size()/(double)(validatedFeatures.size() + invalidatedFeatures.size());
		System.out.println("AbnormalValidated percentage:\t" + vPercentage);
	}
	
	public void validateReferenceCorrelations() throws Exception {
		ArrayList<String> validatedFeatures = new ArrayList<String>();
		ArrayList<String> invalidatedFeatures = new ArrayList<String>();
		for (String s: labeledReferenceIndex.keySet()) {
			Double v1 = labeledReferenceIndex.get(s);
			Double v2 = classifiedReferenceIndex.get(s);
			if (v1 < 0.75) {
				continue;
			}
			if (v2 == null || Math.abs(v1 - v2) > 0.25) {
				invalidatedFeatures.add(s);
				System.out.println("Invalidated\tv1=\t" + v1 + "\tv2=\t" + v2 );
			} else {
				validatedFeatures.add(s);
				System.out.println("Validated\tv1=\t" + v1 + "\tv2=\t" + v2 );
			}
			
		}
		
		System.out.println("~~~~~~~Validated~~~~~~~");
		for (int i = 0; i < validatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + validatedFeatures.get(i));
		}
		
		System.out.println("~~~~~~~Invalidated~~~~~~~");
		for (int i = 0; i < invalidatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + invalidatedFeatures.get(i));
		}
		
		System.out.println("Validated correlations:\t" + validatedFeatures.size());
		System.out.println("Invalidated correlations:\t" + invalidatedFeatures.size());
		System.out.println("labeld sorted features\t" + labeledFilter.getPreFilteredFeatures().size());
		System.out.println("classified sorted features\t" + classifiedFilter.getPreFilteredFeatures().size());
		double vPercentage = (double)validatedFeatures.size()/(double)(validatedFeatures.size() + invalidatedFeatures.size());
		System.out.println("Reference Validated percentage:\t" + vPercentage);
	}
	
	
	public void validateSpecificCorrelations(ArrayList<String> features) {
		ArrayList<String> validatedFeatures = new ArrayList<String>();
		ArrayList<String> invalidatedFeatures = new ArrayList<String>();
		
		for (int i = 0; i < features.size(); i ++) {
			String key1 = features.get(i);
			for (int j = i + 1; j < features.size(); j ++) {
				String key2 = features.get(j);
				String s = key1 + "-" + key2;
				
				Double v1 = labeledIndex.get(s);
				Double v2 = classifiedIndex.get(s);
				if (v1 < 0.75) {
					continue;
				}
				if (v2 == null || Math.abs(v2) < 0.75) {
					invalidatedFeatures.add(s);
					System.out.println("Invalidated\tv1=\t" + v1 + "\tv2=\t" + v2 + "\t" + s);
				} else {
					validatedFeatures.add(s);
					System.out.println("Validated\tv1=\t" + v1 + "\tv2=\t" + v2  + "\t" + s);
				}
				
			}
		}
		
		System.out.println("~~~~~~~Validated~~~~~~~");
		for (int i = 0; i < validatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + validatedFeatures.get(i));
		}
		
		System.out.println("~~~~~~~Invalidated~~~~~~~");
		for (int i = 0; i < invalidatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + invalidatedFeatures.get(i));
		}
		
		System.out.println("Validated correlations:\t" + validatedFeatures.size());
		System.out.println("Invalidated correlations:\t" + invalidatedFeatures.size());
		double vPercentage = (double)validatedFeatures.size()/(double)(validatedFeatures.size() + invalidatedFeatures.size());
		System.out.println("Validated percentage:\t" + vPercentage);
	}
	
	public void validateSpecificAbnormalCorrelations(ArrayList<String> features) {
		ArrayList<String> validatedFeatures = new ArrayList<String>();
		ArrayList<String> invalidatedFeatures = new ArrayList<String>();
		
		for (int i = 0; i < features.size(); i ++) {
			String key1 = features.get(i);
			for (int j = i + 1; j < features.size(); j ++) {
				String key2 = features.get(j);
				String s = key1 + "-" + key2;
				
				Double v1 = labeledAbnormalIndex.get(s);
				Double v2 = classifiedAbnormalIndex.get(s);
				if (v1 < 0.75) {
					continue;
				}
				if (v2 == null || Math.abs(v2) < 0.75) {
					invalidatedFeatures.add(s);
					System.out.println("Invalidated\tv1=\t" + v1 + "\tv2=\t" + v2 + "\t" + s);
				} else {
					validatedFeatures.add(s);
					System.out.println("Validated\tv1=\t" + v1 + "\tv2=\t" + v2  + "\t" + s);
				}
				
			}
		}
		
		System.out.println("~~~~~~~Validated~~~~~~~");
		for (int i = 0; i < validatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + validatedFeatures.get(i));
		}
		
		System.out.println("~~~~~~~Invalidated~~~~~~~");
		for (int i = 0; i < invalidatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + invalidatedFeatures.get(i));
		}
		
		System.out.println("Validated correlations:\t" + validatedFeatures.size());
		System.out.println("Invalidated correlations:\t" + invalidatedFeatures.size());
		double vPercentage = (double)validatedFeatures.size()/(double)(validatedFeatures.size() + invalidatedFeatures.size());
		System.out.println("Abnormal Validated percentage:\t" + vPercentage);
	}
	
	public void validateSpecificReferenceCorrelations(ArrayList<String> features) {
		ArrayList<String> validatedFeatures = new ArrayList<String>();
		ArrayList<String> invalidatedFeatures = new ArrayList<String>();
		
		for (int i = 0; i < features.size(); i ++) {
			String key1 = features.get(i);
			for (int j = i + 1; j < features.size(); j ++) {
				String key2 = features.get(j);
				String s = key1 + "-" + key2;
				
				Double v1 = labeledReferenceIndex.get(s);
				Double v2 = classifiedReferenceIndex.get(s);
				if (v1 < 0.75) {
					continue;
				}
				if (v2 == null || Math.abs(v2) < 0.75) {
					invalidatedFeatures.add(s);
					System.out.println("Invalidated\tv1=\t" + v1 + "\tv2=\t" + v2 + "\t" + s);
				} else {
					validatedFeatures.add(s);
					System.out.println("Validated\tv1=\t" + v1 + "\tv2=\t" + v2  + "\t" + s);
				}
				
			}
		}
		
		System.out.println("~~~~~~~Validated~~~~~~~");
		for (int i = 0; i < validatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + validatedFeatures.get(i));
		}
		
		System.out.println("~~~~~~~Invalidated~~~~~~~");
		for (int i = 0; i < invalidatedFeatures.size(); i ++) {
			System.out.println(i + "\t" + invalidatedFeatures.get(i));
		}
		
		System.out.println("Validated correlations:\t" + validatedFeatures.size());
		System.out.println("Invalidated correlations:\t" + invalidatedFeatures.size());
		double vPercentage = (double)validatedFeatures.size()/(double)(validatedFeatures.size() + invalidatedFeatures.size());
		System.out.println("Reference Validated percentage:\t" + vPercentage);
	}
	
	public static void main(String[] args) throws Exception {
		//compare b3_2 and b0_1
		
		/*
		String labelPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-2f.properties";//high memory
		String classifiedPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b0-1f.properties";//no anomalies
		CorrelationValidation validation = new CorrelationValidation(labelPartitionPath, classifiedPartitionPath);
		validation.validateCorrelations();
		*/
		
		
		
		//compare b3_2 and b0_2
		
		String labelPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-2f.properties";//high memory
		String classifiedPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b0-2f.properties";//no anomalies
		CorrelationValidation validation = new CorrelationValidation(labelPartitionPath, classifiedPartitionPath);
		validation.validateCorrelations();
		
		//compare b3_2 and b3_3
		/*
		String labelPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-2f.properties";//high memory
		String classifiedPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-3f.properties";//no anomalies
		CorrelationValidation validation = new CorrelationValidation(labelPartitionPath, classifiedPartitionPath);
		validation.validateCorrelations();
		*/
		
		/*
		String[] strs ={
				"HadoopDataActivity-TimeSeriesFrequency",
				"MapPeriod-value-TimeSeriesMean",
				"mem_free_event-value-TimeSeriesMean",
				"swap_free_event-value-TimeSeriesMean",
				"RequestFinish-TimeSeriesFrequency",
				"mem_buffers_event-value-TimeSeriesMean",
				"proc_total_event-value-TimeSeriesMean"
		};
		
		
		ArrayList<String> selected = new ArrayList<String>();
		for (String s : strs) {
			selected.add(s);
		}
		validation.validateSpecificCorrelations(selected);
		validation.validateSpecificAbnormalCorrelations(selected);
		validation.validateSpecificReferenceCorrelations(selected);
		*/
	}

}
