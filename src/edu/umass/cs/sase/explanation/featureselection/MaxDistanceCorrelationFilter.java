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
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;
/**
 * Optimization formula: 
 * @author haopeng
 *
 */
public class MaxDistanceCorrelationFilter extends FeatureFilter{
	double lambda;
	
	double finalTotalDistance;
	double finalPenalty;
	
	ArrayList<TimeSeriesFeaturePair> preFilteredFeatures;
	
	double[][] correlationMatrix;
	RealMatrix correlationRealMatrix;
	
	double maxScore;
	ArrayList<Integer> maxPath;
	
	int enumerationCount;
	
	int specialCaseCount;
	public MaxDistanceCorrelationFilter(ArrayList<TimeSeriesFeaturePair> list) {
		super(list);
	}
	
	
	public void filter(double lambda) {
		this.preFilterWithoutCorrelations(lambda);
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
		this.preFilteredFeatures.remove(3);//for debug, has to be commeted when running experiments
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
	
	public double computeMultipleCorrelation(ArrayList<Integer> path, int index) {
		int length = path.size();
		//get c
		double[][] cArray = new double[length - 1][1];
		double[] sourceRow = this.correlationRealMatrix.getRow(path.get(index));
		for (int i = 0; i < length; i ++) {
			if (i < index) {
				cArray[i][0] = sourceRow[path.get(i)];
			} else if (i > index) {
				cArray[i - 1][0] = sourceRow[path.get(i)];
			}
		}
		RealMatrix cMatrix = MatrixUtils.createRealMatrix(cArray);
		this.printMatrix(cMatrix, "cMatrix for " + index);
		
		//get r
		double[][] rArray = new double[length - 1][length - 1];
		for (int i = 0; i < length; i ++) {
			if (i < index) {
				this.copyArray(rArray, i, this.correlationRealMatrix.getRow(path.get(i)), path, index);
			} else if (i > index) {
				this.copyArray(rArray, i - 1, this.correlationRealMatrix.getRow(path.get(i)), path, index);
			}
		}
		
		RealMatrix rMatrix = MatrixUtils.createRealMatrix(rArray);
		this.printMatrix(rMatrix, "rMatrix for " + index);
		
		//compute r^2
		RealMatrix rSquare = cMatrix.transpose().multiply(MatrixUtils.inverse(rMatrix)).multiply(cMatrix);
		
		//root
		double r = Math.sqrt(rSquare.getEntry(0, 0));
		this.printMatrix(rSquare, "result matrix");
		System.out.println("multiple correlation=" + r + "\t!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		return r;
	}
	
	public void copyArray(double[][] destArray, int destRow, double[] sourceArray, ArrayList<Integer> path, int avoidIndex) {
		int length = path.size();
		for (int i = 0; i < length; i ++) {
			if (i < avoidIndex) {
				destArray[destRow][i] = sourceArray[path.get(i)];
			} else if (i > avoidIndex) {
				destArray[destRow][i - 1] = sourceArray[path.get(i)];
			}
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
	public double scorePath(ArrayList<Integer> path) {
		//debug
		this.enumerationCount ++;
		System.out.print(this.enumerationCount + "\t Feature numbers:");
		for (Integer i: path) {
			System.out.print(i + "\t");
		}
		System.out.println();
		
		
		double score = 0.0;
		int size = path.size();
		//distance
		for (int i = 0; i < size; i ++) {
			TimeSeriesFeaturePair pair = this.preFilteredFeatures.get(path.get(i));
			score += pair.getRecentDistance();
		}
		//correlation
		double correlationPenalty = 0;
		
		if (path.size() == 1) {
			correlationPenalty = 0;
		} else {
			for (int i = 0; i < path.size(); i ++) {
				double multipleCorrelation = this.computeMultipleCorrelation(path, i);
				//correlationPenalty += multipleCorrelation * (this.preFilteredFeatures.get(path.get(i)).getRecentDistance() - this.lambda); 
				correlationPenalty += multipleCorrelation;
			}
		}
		
		score = score - correlationPenalty*0.2  - this.lambda * path.size(); //write this into tex
		//score = score - correlationPenalty - this.lambda * path.size();//use lambda or not?
		
		
		//debug
		
		System.out.print(this.enumerationCount + ": score=" + score + "\t Feature numbers:");
		for (Integer i: path) {
			System.out.print(i + "\t");
		}
		System.out.println();
		
		return score;

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

	
	public double scorePathOld(ArrayList<Integer> path) {
		
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
	
	
	public double computeFeatureCorrelation(TimeSeriesFeature f1, TimeSeriesFeature f2) {
		//first check size if equal
		int size;
		if (f1.getValues().size() == f2.getValues().size()) {
			size = f1.getValues().size();
		} else {
			//size = Math.min(f1.getValues().size(), f2.getValues().size());
			return 0.0;//chagne this later:todo
		}
		//f1
		double sum1 = 0.0;
		for (Double d : f1.getValues()) {
			sum1 += d;
		}
		
		double mean1 = sum1 / (double)size;
		//mean square root sum
		//http://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient
		double total1 = 0.0;
		for (Double d: f1.getValues()) {
			double xi = d - mean1;
			total1 += xi * xi;
		}
		double meanSquareRoot1 = Math.sqrt(total1);
		
		
		//f2
		double sum2 = 0.0;
		for (Double d : f2.getValues()) {
			sum1 += d;
		}
		
		double mean2 = sum2 / (double)size;
		//mean square root sum
		//http://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient
		double total2 = 0.0;
		for (Double d: f2.getValues()) {
			double xi = d - mean1;
			total2 += xi * xi;
		}
		double meanSquareRoot2 = Math.sqrt(total2);
		
		//compute correlation, follow the formula on wikipedia:
		//https://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient
		double xMean = mean1;
		double yMean = mean2;
		
		ArrayList<Double> xValues = f1.getValues();
		ArrayList<Double> yValues = f2.getValues();
		
		//System.out.println("============Correlation==============");
		//System.out.println(p1.getFeatureName() + "\t" + p2.getFeatureName());
		
		double numerator = 0;
		for (int i = 0; i < size; i ++) {
			double xi = xValues.get(i);
			double yi = yValues.get(i);
			numerator += (xi - xMean) * (yi - yMean);
			//System.out.println(xi + "\t" + yi);//debug
		}
		
		double denominator = meanSquareRoot1 * meanSquareRoot2;
		
		//debug

		double correlation = 0.0;
		
		if (denominator > 0.0) {
			// http://www.vbforums.com/showthread.php?425748-RESOLVED-What-s-the-correlation-coefficient-i-fthe-standard-deviation-is-zero
			// in case of the denominator being zero
			correlation = numerator / denominator;
		} else {
			this.specialCaseCount ++;
			//System.out.println("#######Special case: \t" + this.specialCaseCount + "\t##########");
		}
		
		//System.out.println("Correlation:\t" + correlation);
		
		return correlation;
	}
	
	public double computePairCorrelation(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
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
			//System.out.println(xi + "\t" + yi);//debug
		}
		
		double denominator = p1.getMeanSquareRootForSize(size) * p2.getMeanSquareRootForSize(size);
		
		//debug

		double correlation = 0.0;
		
		if (denominator > 0.0) {
			// http://www.vbforums.com/showthread.php?425748-RESOLVED-What-s-the-correlation-coefficient-i-fthe-standard-deviation-is-zero
			// in case of the denominator being zero
			correlation = numerator / denominator;
		} else {
			this.specialCaseCount ++;
			//System.out.println("#######Special case: \t" + this.specialCaseCount + "\t##########");
		}
		
		//System.out.println("Correlation:\t" + correlation);
		
		return correlation;
	}
	
	public void preFilterWithoutCorrelations(double lambda) {
		//first preFilter by not considering correlations
		this.lambda = lambda;
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
		System.out.println("Settings:lambda = " + this.lambda);
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
