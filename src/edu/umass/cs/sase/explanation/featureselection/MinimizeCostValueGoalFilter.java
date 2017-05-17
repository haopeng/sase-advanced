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

public class MinimizeCostValueGoalFilter extends FeatureFilter{
	double valueGoal;
	public MinimizeCostValueGoalFilter(ArrayList<TimeSeriesFeaturePair> list) {
		super(list);
	}
	
	public void filter(double valueGoal) {
		this.valueGoal = valueGoal;
		double totalValue = 0;
		for (TimeSeriesFeaturePair pair : this.sortedFeatureList) {
			double value = pair.getPriorClassEntropy() - pair.getPostClassentropy();
			totalValue += value;
			this.selectedFeatures.add(pair);
			
			if (totalValue >= valueGoal) {
				return;
			}
		}
	}

	@Override
	public void print() {
		System.out.println("~~~~~~~~~~~~Selected features by MinimizeCostValueGoalFilter:~~~~~~~~~~~");
		System.out.println("Settings:valueGoal = " + this.valueGoal);
		System.out.println("No.\tFeatureName\tDistance\tValue\tAccumulatedValue\tCost\tAccumulatedCost");
		int count = 0;
		double accuCost = 0;
		double accuValue = 0;
		for (TimeSeriesFeaturePair pair: this.selectedFeatures) {
			count ++;
			double value = pair.getPriorClassEntropy() - pair.getPostClassentropy();
			double cost = pair.getPostIntervalEntropy() - pair.getPriorClassEntropy();
			accuCost += cost;
			accuValue += value;
			System.out.println(count + "\t" + pair.getFeatureName() + "\t" +pair.getEntropyDistance() +"\t" +
					value + "\t" + accuValue + "\t" + cost + "\t" +accuCost + "\t");
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
	}
	
}
