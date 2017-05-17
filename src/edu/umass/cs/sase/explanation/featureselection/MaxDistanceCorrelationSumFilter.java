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
import java.util.HashSet;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
/**
 * Optimization formula:
 * sum of all pairwise correlations 
 * @author haopeng
 *
 */
public class MaxDistanceCorrelationSumFilter extends FeatureFilter{
	double lambda1;
	double lambda2;
	
	double finalTotalDistance;
	double finalPenalty;
	
	ArrayList<TimeSeriesFeaturePair> preFilteredFeatures;
	
	double[][] correlationMatrix;
	RealMatrix correlationRealMatrix;
	
	double maxScore;
	ArrayList<Integer> maxPath;
	
	int enumerationCount;
	public MaxDistanceCorrelationSumFilter(ArrayList<TimeSeriesFeaturePair> list) {
		super(list);
	}
	
	
	public void filter(double lambda1, double lambda2) {
		this.lambda2 = lambda2;
		this.preFilterWithoutCorrelations(lambda1);
		System.out.println("~~~~~~~PreFilter without correlations result: " + this.preFilteredFeatures.size() + " features are selected~~~~~~~~~");
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			System.out.println(i + "\t" + this.preFilteredFeatures.get(i).getFeatureName() + "\t" + this.preFilteredFeatures.get(i).getRecentDistance());
		}
		
		this.computeCorrelationMatrix();
				
		this.preFilterWithCorrelations();
		System.out.println("~~~~~~~PreFilter with correlations result: " + this.preFilteredFeatures.size() + " features are selected~~~~~~~~~");
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			System.out.println(i + "\t" + this.preFilteredFeatures.get(i).getFeatureName()+ "\t" + this.preFilteredFeatures.get(i).getRecentDistance() + "\tTemporalCorrelation=" + this.preFilteredFeatures.get(i).getTemporalCorrelation());
		}
		System.out.println("Filtering by correlation...");
		
		//this.preFilteredFeatures.remove(3);//for debug, has to be commeted when running experiments
		
		this.computeCorrelationMatrix(); //compute again
		
		this.enumerationSearch();
	}
	
	public void enumerationSearch() {
		this.maxScore = Double.MIN_VALUE;
		ArrayList<Integer> path = new ArrayList<Integer>();
		enumerationHelper(path, 0, this.preFilteredFeatures.size());
		
		//add final plan to the selected features
		for (Integer i : this.maxPath) {
			this.selectedFeatures.add(this.preFilteredFeatures.get(i));
		}
	}
	
	public void enumerationHelper(ArrayList<Integer> path, int start, int size) {
		if (path.size() > 0) {
			double score = this.scorePath(path);
			if (score > this.maxScore) {
				this.maxScore = score;
				this.maxPath = new ArrayList<Integer>(path);
			}
		}
		
		for (int i = start; i < size; i ++) {
			path.add(i);
			enumerationHelper(path, i + 1, size);
			path.remove(path.size() - 1);
		}
		
	}
	
	
	
	
	public void printMatrix(RealMatrix m, String name) {
		System.out.println("~~~~~~" + name + "~~~~~~");
		int r = m.getRowDimension();
		int c = m.getColumnDimension();
		
		for (int i = 0; i < r; i ++) {
			for (int j = 0; j < c; j ++) {
				System.out.print(m.getEntry(i, j) + "\t");
			}
			System.out.println();
		}
		
		System.out.println("~~~~~~END~~~~~~");
	}
	
	public double scorePathOld2(ArrayList<Integer> path) {
		
		double score = 0.0;
		int size = path.size();
		//distance
		for (int i = 0; i < size; i ++) {
			TimeSeriesFeaturePair pair = this.preFilteredFeatures.get(path.get(i));
			score += pair.getRecentDistance();
		}
		//correlation
		double correlationPenalty = 0;
		for (int i = 0; i < size; i ++) {
			for (int j = i + 1; j < size; j ++) {
				int a = path.get(i);
				int b = path.get(j);
				
				TimeSeriesFeaturePair pairA = this.preFilteredFeatures.get(a);
				TimeSeriesFeaturePair pairB = this.preFilteredFeatures.get(b);
				
				correlationPenalty += Math.abs(this.correlationMatrix[a][b]) * (pairA.getRecentDistance() + pairB.getRecentDistance()) / 2;
			}
		}
		
		//score = score - correlationPenalty - this.lambda * path.size();
		score = score - correlationPenalty;
		
		//debug
		this.enumerationCount ++;
		System.out.print(this.enumerationCount + ": score=" + score + "\t Feature numbers:");
		for (Integer i: path) {
			System.out.print(i + "\t");
		}
		System.out.println();
		
		return score;
	}

	
	public double scorePath(ArrayList<Integer> path) {
		
		double score = 0.0;
		int size = path.size();
		//distance
		for (int i = 0; i < size; i ++) {
			TimeSeriesFeaturePair pair = this.preFilteredFeatures.get(path.get(i));
			score += pair.getRecentDistance();
		}
		//correlation
		double penalty = 0;
		for (int i = 0; i < size; i ++) {
			for (int j = i; j < size; j ++) {
				int a = path.get(i);
				int b = path.get(j);
				penalty += Math.abs(this.correlationMatrix[a][b]);
			}
		}
		
		score = score - this.lambda2 * penalty - this.lambda1 * path.size();
		
		//debug
		this.enumerationCount ++;
		System.out.print(this.enumerationCount + ": score=" + score + "\t Feature numbers:");
		for (Integer i: path) {
			System.out.print(i + "\t");
		}
		System.out.println();
		
		return score;
	}
	public void computeCorrelationMatrix() {
		int length = this.preFilteredFeatures.size();
		this.correlationMatrix = new double[length][length];
		
		for (int i = 0; i < length; i ++) {
			
			for (int j = 0; j < length; j ++) {
				correlationMatrix[i][j] = this.computePairCorrelation(this.preFilteredFeatures.get(i), this.preFilteredFeatures.get(j));
			}
			this.preFilteredFeatures.get(i).computeTemporalCorrelation();
		}
		
		this.correlationRealMatrix = MatrixUtils.createRealMatrix(this.correlationMatrix);
		printMatrix(this.correlationRealMatrix, "CorrelationMatrix");
	}
	
	public double computePairCorrelation(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
		//TODO
		p1.prepareForCorrelation();
		p2.prepareForCorrelation();
		//first check size if equal
		int size;
		if (p1.getAllValueSize() == p2.getAllValueSize()) {
			size = p1.getAllValueSize();
		} else {
			size = Math.min(p1.getAllValueSize(), p2.getAllValueSize());
		}
		
		p1.computeMean(size);
		p2.computeMean(size);
		
		//compute correlation, follow the formula on wikipedia:
		//https://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient
		double xMean = p1.getMeanForSize(size);
		double yMean = p2.getMeanForSize(size);
		
		ArrayList<Double> xValues = p1.getValueListForSize(size);
		ArrayList<Double> yValues = p2.getValueListForSize(size);
		
		//System.out.println("============Correlation==============");
		//System.out.println(p1.getFeatureName() + "\t" + p2.getFeatureName());
		
		double numerator = 0;
		for (int i = 0; i < size; i ++) {
			double xi = xValues.get(i);
			double yi = yValues.get(i);
			numerator += (xi - xMean) * (yi - yMean);
			//System.out.println(xi + "\t" + yi);
		}
		
		double denominator = p1.getMeanSquareRootForSize(size) * p2.getMeanSquareRootForSize(size);
		
		//debug
		/*
		System.out.println("~~~~~DEBUG CORRELATION~~~~~");
		for (int i = 0; i < size; i ++) {
			System.out.println(i + "\t" + xValues.get(i) + "\t" + yValues.get(i));
		}
		System.out.println("Correlation=" + numerator/denominator);
		*/
		double correlation = numerator / denominator;
		
		//System.out.println("Correlation:\t" + correlation);
		
		return correlation;
	}
	
	public void preFilterWithoutCorrelations(double lambda) {
		//first preFilter by not considering correlations
		this.lambda1 = lambda;
		this.preFilteredFeatures = new ArrayList<TimeSeriesFeaturePair>();
		
		double oldTotalDistance = 0;
		double oldPenalty = 0;
		
		for (int i = 0; i < this.sortedFeatureList.size(); i ++) {
			TimeSeriesFeaturePair pair = this.sortedFeatureList.get(i);
			double newTotalDistance = oldTotalDistance + pair.getRecentDistance();
			double newPenalty = lambda * (i + 1);
			
			if (newTotalDistance - newPenalty <= oldTotalDistance - oldPenalty) {
				this.finalTotalDistance = oldTotalDistance;
				this.finalPenalty = oldPenalty;
				return;
			} else {
				this.preFilteredFeatures.add(pair);
				oldTotalDistance = newTotalDistance;
				oldPenalty = newPenalty;
			}
		}
	}
	
	
	public void preFilterWithCorrelations() {
		//filters correlation = 1 / 0.999, which means the two features are 100% correlated
		HashSet<TimeSeriesFeaturePair> removed = new HashSet<TimeSeriesFeaturePair>();
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			for (int j = i + 1; j < this.preFilteredFeatures.size(); j ++) {
				if (Math.abs(this.correlationRealMatrix.getEntry(i, j)) > 0.999) {
					removed.add(this.preFilteredFeatures.get(j));
				}
			}
		}
		
		ArrayList<TimeSeriesFeaturePair> leftPairs = new ArrayList<TimeSeriesFeaturePair>();
		for (TimeSeriesFeaturePair pair: this.preFilteredFeatures) {
			if (!removed.contains(pair)) {
				leftPairs.add(pair);
			}
		}
		
		this.preFilteredFeatures = leftPairs;
	}
	

	@Override
	public void print() {
		System.out.println("~~~~~~~~~~~~Selected features by MaximizeDistanceCorrelationFilter:~~~~~~~~~~~");
		System.out.println("Settings:lambda1 = " + this.lambda1 + "\tlambda2=" + this.lambda2);
		System.out.println("Score = " + this.maxScore);
		System.out.println("No.\tFeatureName\tDistance\tAccumulatedDistance");
		int count = 0;
		double accuDistance = 0;
		for (TimeSeriesFeaturePair pair: this.selectedFeatures) {
			count ++;
			accuDistance += pair.getEntropyDistance();
			System.out.println(count + "\t" + pair.getFeatureName() + "\t" +pair.getEntropyDistance() +"\t" + accuDistance);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");		
	}

}
