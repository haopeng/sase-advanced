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

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;

public class MaxValueInequalPenaltyFilter extends FeatureFilter{
	double lambda;
	
	double finalTotalValue;
	double finalTotalCost;
	double finalPenalty;
	
	public MaxValueInequalPenaltyFilter(ArrayList<TimeSeriesFeaturePair> list) {
		super(list);
	}

	public void filter(double lambda) {
		this.lambda = lambda;
		double oldTotalValue = 0.0;
		double oldTotalCost = 0.0;
		double oldPenalty = 0.0;
		
		for (int i = 0; i < this.sortedFeatureList.size(); i ++) {
			TimeSeriesFeaturePair pair = this.sortedFeatureList.get(i);
			double value = pair.getPriorClassEntropy() - pair.getPostClassentropy();
			//double cost = pair.getPostIntervalEntropy() - pair.getPriorClassEntropy();//
			double cost = pair.getPostIntervalEntropy();//use this because 1. zero cost causes problem in division  2. priosClassEntropy is the same for every feature
			
			double newTotalValue = oldTotalValue + value;
			double newTotalCost = oldTotalCost + cost;
			double newPenalty = lambda / newTotalCost;
			
			if (newTotalValue + newPenalty <= oldTotalValue + oldPenalty) {
				this.finalTotalValue = oldTotalValue;
				this.finalTotalCost = oldTotalCost;
				this.finalPenalty = oldPenalty;
				return;
			} else {
				this.selectedFeatures.add(pair);
				oldTotalValue = newTotalValue;
				oldTotalCost = newTotalCost;
				oldPenalty = newPenalty;
			}
		}
	}
	
	
	@Override
	public void print() {
		System.out.println("~~~~~~~~~~~~Selected features by MaximizeValueInequalPenaltyFilter:~~~~~~~~~~~");
		System.out.println("Settings:lambda = " + this.lambda);
		System.out.println("No.\tFeatureName\tValue\tAccumulatedValue\tCost\tAccumulatedCost\tPenalty\tSumOfDistanceAndPenalty");
		int count = 0;
		double accuValue = 0;
		double accuCost = 0;
		for (TimeSeriesFeaturePair pair: this.selectedFeatures) {
			count ++;
			accuValue += (pair.getPriorClassEntropy() - pair.getPostClassentropy());
			accuCost += pair.getPostIntervalEntropy();
			double penalty = lambda / accuCost;
			double sum = accuValue + penalty;
			System.out.println(count + "\t" + pair.getFeatureName() + "\t" + (pair.getPriorClassEntropy() - pair.getPostClassentropy()) +"\t" +
					accuValue+ "\t" + (pair.getPostIntervalEntropy()) + "\t" + accuCost + "\t" + penalty + "\t" + sum);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
	}
	
	public void printSimple() {
		System.out.println(this.lambda + "\t" + this.selectedFeatures.size());
	}

}
