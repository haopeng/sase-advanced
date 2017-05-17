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

public class MaxValueCorrelationFilter extends MaxDistanceCorrelationFilter{
	double lambda;
	
	//ArrayList<TimeSeriesFeaturePair> preFilteredFeatures;
	
	double finalTotalValue;
	double finalTotalCost;
	double finalPenalty;
	
	public MaxValueCorrelationFilter(ArrayList<TimeSeriesFeaturePair> list) {
		super(list);
	}


	@Override
	
	public double scorePath(ArrayList<Integer> path) {
		
		double score = 0.0;
		int size = path.size();
		//value
		for (int i = 0; i < size; i ++) {
			TimeSeriesFeaturePair pair = this.preFilteredFeatures.get(path.get(i));
			score += (pair.getPriorClassEntropy() - pair.getPostClassentropy());
		}
		//correlation
		double penalty = 0;
		for (int i = 0; i < size; i ++) {
			for (int j = i; j < size; j ++) {
				int a = path.get(i);
				int b = path.get(j);
				
				TimeSeriesFeaturePair pairA = this.preFilteredFeatures.get(a);
				TimeSeriesFeaturePair pairB = this.preFilteredFeatures.get(b);
				
				double costA = pairA.getPostIntervalEntropy() - pairA.getPriorClassEntropy();
				double costB = pairB.getPostIntervalEntropy() - pairB.getPriorClassEntropy();
				double avgCost = (costA + costB) / 2.0;
				
				penalty += Math.abs(this.correlationMatrix[a][b] * avgCost);
			}
		}
		
		score = score - this.lambda * penalty;
		
		//debug
		this.enumerationCount ++;
		System.out.print(this.enumerationCount + ": score=" + score + "\t Feature numbers:");
		for (Integer i: path) {
			System.out.print(i + "\t");
		}
		System.out.println();
		
		return score;
	}
	@Override
	public void preFilterWithoutCorrelations(double lambda) {
		this.lambda = lambda;
		this.preFilteredFeatures = new ArrayList<TimeSeriesFeaturePair>();
		
		double oldTotalValue = 0.0;
		double oldTotalCost = 0.0;
		double oldPenalty = 0.0;
		
		for (int i = 0; i < this.sortedFeatureList.size(); i ++) {
			TimeSeriesFeaturePair pair = this.sortedFeatureList.get(i);
			double value = pair.getPriorClassEntropy() - pair.getPostClassentropy();
			double cost = pair.getPostIntervalEntropy() - pair.getPriorClassEntropy();
			
			double newTotalValue = oldTotalValue + value;
			double newTotalCost = oldTotalCost + cost;
			double newPenalty = lambda * newTotalCost;
			
			if (newTotalValue - newPenalty <= oldTotalValue - oldPenalty) {
				this.finalTotalValue = oldTotalValue;
				this.finalTotalCost = oldTotalCost;
				this.finalPenalty = oldPenalty;
				return;
			} else {
				this.preFilteredFeatures.add(pair);
				oldTotalValue = newTotalValue;
				oldTotalCost = newTotalCost;
				oldPenalty = newPenalty;
			}
		}
	}
	
	
	@Override
	public void print() {
		System.out.println("~~~~~~~~~~~~Selected features by MaximizeValueCorrelationFilter:~~~~~~~~~~~");
		System.out.println("Settings:lambda = " + this.lambda);
		System.out.println("Score = " + this.maxScore);
		System.out.println("No.\tFeatureName\tValue\tAccumulatedValue\tCost\tAccumulatedCost");
		int count = 0;
		double accuValue = 0;
		double accuCost = 0;
		for (TimeSeriesFeaturePair pair: this.selectedFeatures) {
			count ++;
			accuValue += (pair.getPriorClassEntropy() - pair.getPostClassentropy());
			accuCost += (pair.getPostIntervalEntropy() - pair.getPriorClassEntropy());
			double penalty = lambda * accuCost;
			double sum = accuValue - penalty;
			System.out.println(count + "\t" + pair.getFeatureName() + "\t" + (pair.getPriorClassEntropy() - pair.getPostClassentropy()) +"\t" +
					accuValue+ "\t" + (pair.getPostIntervalEntropy() - pair.getPriorClassEntropy()) + "\t" + accuCost + "\t" + penalty + "\t" + sum);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
	}

}
