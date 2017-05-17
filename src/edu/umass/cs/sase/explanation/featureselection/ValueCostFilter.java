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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeatureComparator;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;

/**
 * This filter use a simple formula: value > cost to filter features
 * Value = PriorClassEntropy - PostClassEntropy
 * Cost = PostIntervalEntropy - PriorClassEntropy
 * @author haopeng
 *
 */
public class ValueCostFilter extends FeatureFilter{
	public ValueCostFilter(ArrayList<TimeSeriesFeaturePair> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}
	ArrayList<TimeSeriesFeaturePair> valuableFeatures;
	
	public void filter() {
		for (TimeSeriesFeaturePair pair: this.sortedFeatureList) {
			if (pair.getPriorClassEntropy() - pair.getPostClassentropy() > pair.getPostIntervalEntropy() - pair.getPriorClassEntropy()) {
				this.selectedFeatures.add(pair);
			}
		}
	}
	

	public ArrayList<TimeSeriesFeaturePair> getValuableFeatures() {
		return valuableFeatures;
	}
	public void setValuableFeatures(
			ArrayList<TimeSeriesFeaturePair> valuableFeatures) {
		this.valuableFeatures = valuableFeatures;
	}
	@Override
	public void print() {
		System.out.println("~~~~~~Selected features by ValueCostFilter:~~~~~~~~");
		int count = 0;
		System.out.println("No.\tFeatureName\tDistance\tValue\tCost\t");
		for (TimeSeriesFeaturePair pair: this.selectedFeatures) {
			count ++;
			System.out.println(count + "\t" + pair.getFeatureName() + "\t" +pair.getEntropyDistance() + "\t" 
			   + (pair.getPriorClassEntropy() - pair.getPostClassentropy()) + "\t" + (pair.getPostIntervalEntropy() - pair.getPriorClassEntropy()) + "\t");
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
	
	/**
	 * retired code
	 * @param tsFeatureComparator
	 */
	public void filterFeaturesUsingEntropyThreshold(TimeSeriesFeatureComparator tsFeatureComparator) {
		// initialize
		this.valuableFeatures = new ArrayList<TimeSeriesFeaturePair>();
		
		//go over all raw time series
		for (TimeSeriesFeaturePair pair : tsFeatureComparator.getRawFeaturePairs()) {
			if (pair.isValuable()) {
				this.valuableFeatures.add(pair);
			}
		}
		
		//go over all agg time series
		for (TimeSeriesFeaturePair pair : tsFeatureComparator.getAggFeaturePairs()) {
			if (pair.isValuable()) {
				this.valuableFeatures.add(pair);
			}
		}
		
		//sort
		Collections.sort(this.valuableFeatures, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getRecentDistance();
				double v2 = p2.getRecentDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				return 0;
			}
		});
		//print
		System.out.println("Valuable features:");
		int count = 0;
		for (TimeSeriesFeaturePair pair: this.valuableFeatures) {
			count ++;
			System.out.println(count + "\t" + pair.getFeatureName() + "\t" +pair.getEntropyDistance());
		}

		
	}
	
}
