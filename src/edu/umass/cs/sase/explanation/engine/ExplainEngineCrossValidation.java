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

package edu.umass.cs.sase.explanation.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import edu.umass.cs.sase.explanation.crossvalidation.FeatureValidationEngine;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.evaluation.DataPoint;
import edu.umass.cs.sase.explanation.evaluation.PredictionEngine;

public class ExplainEngineCrossValidation extends ExplainEngine{
	int fold;
	ArrayList<DataPoint> testPoints;
	
	ArrayList<Double> precisionList;
	ArrayList<Double> recallList;
	ArrayList<Double> fmeasureList;
	
	int abnormalCount;
	int referenceCount;
	int[] shuffleForAbnormal;
	int[] shuffleForReference;
	public ExplainEngineCrossValidation(String inputPropertiesFile)
			throws IOException {
		super(inputPropertiesFile);
	}
	public void initialize() {
		this.precisionList = new ArrayList<Double>();
		this.recallList = new ArrayList<Double>();
		this.fmeasureList = new ArrayList<Double>();
	}
	public void runEngine(int fold) throws Exception {
		this.fold = fold;
		//initialize
		this.initialize();
		//read raw events
		this.readRawEvents();
		//generate features
		this.generateFeatures();
		//compute distance
		this.computeDistance();
		//get count
		this.getPointCount();
		//prepare shuffle array
		this.prepareShuffle(abnormalCount, referenceCount);
		//Label related partitions and recompute features over broader dataset
		this.validateFeatures();
		//evaluate results vs. ground truth
		this.evaluteResult();
		//iteration
		for (int i = 0; i < fold; i ++) {
			System.out.println("Cross validation " + i + " of " + fold + " iterations");
			//generate features
			this.generateFeatures();
			//compute distance
			this.computeDistance();
			//sample test point
			this.samplePoints(i);
			//validate features
			this.validateFeatures();
			//predict using test points
			this.evaluateByPrediction(this.testPoints);

		}
		this.printResults();
	}
	
	public void getPointCount() {
		this.abnormalCount = this.tsFeatureComparator.getAbnormalAggPointCount();
		this.referenceCount = this.tsFeatureComparator.getReferenceAggPointCount();
	}
	public void prepareShuffle(int abnormalCount, int referenceCount) {
		this.shuffleForAbnormal = this.generateShuffle(abnormalCount);
		this.shuffleForReference = this.generateShuffle(referenceCount);
	}
	
	public int[] generateShuffle(int size) {
		int[] array = new int[size];
		for (int i = 0; i < size; i ++) {
			array[i] = i;
		}
		Random rgen = new Random(5);
		//Random rgen = new Random(4);
		for (int i=0; i<array.length; i++) {
		    int randomPosition = rgen.nextInt(array.length);
		    int temp = array[i];
		    array[i] = array[randomPosition];
		    array[randomPosition] = temp;
		}
		return array;
	}
	
	public void samplePoints(int i) {
		//this.testPoints = this.tsFeatureComparator.sampleDataPoints(fold, i);
		this.testPoints = this.tsFeatureComparator.sampleDataPoints(this.fold, i, this.shuffleForAbnormal, this.shuffleForReference);
	}
	
	
	/**
	 * Print the precision/recall/fmeasure
	 */
	public void printResults() {
		//header
		System.out.println("IterationNo.\tPrecision\tRecall\tFMeasure");
		for (int i = 0; i < this.precisionList.size(); i ++) {
			System.out.println(i + "\t" + this.precisionList.get(i) + "\t" + this.recallList.get(i) + "\t" + this.fmeasureList.get(i));
		}
		System.out.println("Mean\t" + this.getMeanOfList(this.precisionList) + "\t" 
									+ this.getMeanOfList(this.recallList) + "\t"
									+ this.getMeanOfList(this.fmeasureList));
		
	}
	
	public double getMeanOfList(ArrayList<Double> valueList) {
		double sum = 0.0;
		for (Double d: valueList) {
			sum += d;
		}
		return sum / (double)valueList.size();
	}
	
	public void evaluateByPrediction(ArrayList<DataPoint> testPoints) {
		FeatureValidationEngine featureValidationEngine= this.validationEngine.getFeatureValidationEngine();
		ArrayList<HashSet<String>> clusterList = featureValidationEngine.getClusterList();
		HashMap<String, TimeSeriesFeaturePair> topFeatureIndex = featureValidationEngine.getTopfeatureIndex();
		this.predictionEngine = new PredictionEngine(testPoints, clusterList, topFeatureIndex);
		this.predictionEngine.runEngine();
		
		this.precisionList.add(this.predictionEngine.getPrecision());
		this.recallList.add(this.predictionEngine.getRecall());
		this.fmeasureList.add(this.predictionEngine.getfMeasure());
	}
	

}
