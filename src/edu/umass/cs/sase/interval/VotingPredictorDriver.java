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
package edu.umass.cs.sase.interval;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import au.com.bytecode.opencsv.CSVWriter;
import edu.umass.cs.sase.util.csv.PointLabel;
import edu.umass.cs.sase.util.csv.SamplePoint;

/**
 * This class contains the information
 * @author haopeng
 *
 */

public class VotingPredictorDriver extends IntervalPredictionDriver{
	ArrayList<IntervalBasedPredictor> votingPredictors;
	int[] featureIndex;
	
	public VotingPredictorDriver(String modelPath, String pFolder)
			throws IOException {
		super(modelPath, pFolder);
	}
	
	public VotingPredictorDriver(String modelPath, String trainingFolder, String testFolder, int[] featureIndex) throws IOException{
		super(modelPath, trainingFolder);
		
		this.testFolder = testFolder;
		this.testPoints = new ArrayList<SamplePoint>();
		this.readTestData();
		
		//predictors
		this.featureIndex = featureIndex;
		this.votingPredictors = new ArrayList<IntervalBasedPredictor>();
		this.trainPredictors();
	}
	
	public void trainPredictors() throws IOException{
		for (int i = 0; i < this.featureIndex.length; i ++) {
			int feature = this.featureIndex[i];
			IntervalBasedPredictor newPredictor = new IntervalBasedPredictor(this.modelPath);
			this.votingPredictors.add(newPredictor);
			newPredictor.buildIntervalsByAttribute(feature, this.points);
			newPredictor.removeSinglePoints();
		}
	}
	
	public void votingPredict(String outputFile) throws IOException{
		CSVWriter writer = new CSVWriter(new FileWriter(outputFile), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String[] outputLine = new String[this.featureIndex.length + 4];
		outputLine[0] = "PointNumber";
		outputLine[1] = "TrueLabel";
		outputLine[2] = "PredictionResult";
		outputLine[3] = "TrueOrFalse";
		
		for (int i = 0; i < this.votingPredictors.size(); i ++) {
			outputLine[4 + i] = this.votingPredictors.get(i).getFeatureName();
			System.out.println(outputLine[4 + i]);
		}
		writer.writeNext(outputLine);
		//String[] outputLine = {"PointNumber", "PointTrueLabel", "PredictionResult"};
		double tp = 0.0;
		double tn = 0.0;
		double fp = 0.0;
		double fn = 0.0;
		int pointCount = 0;
		for (SamplePoint point : this.testPoints) {
			pointCount ++;
			int abnormalCount = 0;
			int normalCount = 0;
			outputLine[0] = pointCount + "";
			outputLine[1] = point.getTrueLabel() + "";
			int predictorCount = 0;
			for (IntervalBasedPredictor ibPredictor : this.votingPredictors) {
				predictorCount ++;
				PointLabel label = ibPredictor.predictSinglePointByAdjustedIntervals(point);
				switch(label){
					case Abnormal:
						abnormalCount ++;
						outputLine[3 + predictorCount] = "1";//abnormal
						break;
					case Normal:
						normalCount ++;
						outputLine[3 + predictorCount] = "0";//normal
						break;
					default:
						break;
				}
			}
			
			if (abnormalCount >= normalCount) {
				outputLine[2] = "Abnormal";
				if (point.getTrueLabel() == PointLabel.Abnormal) {
					tp += 1.0;
					outputLine[3] = "True";
				} else {
					fp += 1.0;
					outputLine[3] = "False";
				}
			} else {
				outputLine[2] = "Normal";
				if (point.getTrueLabel() == PointLabel.Normal) {
					tn += 1.0;
					outputLine[3] = "True";
				} else {
					fn += 1.0;
					outputLine[3] = "False";
				}
			}
			
			writer.writeNext(outputLine);
		}
		
		outputLine = new String[2];
		
		double recall = tp / (tp + fn);
		double precision = tp / (tp + fp);
		double accuracy = (tp + tn) / (tp + tn + fp + fn);
		System.out.println("TP=" + tp + "\tTN=" + tn + "\tFP=" + fp + "\tFN=" + fn);
		System.out.println("Recall=" + recall);
		System.out.println("Precision=" + precision);
		System.out.println("Accuracy=" + accuracy);
		
		
		outputLine[0] = "TP";
		outputLine[1] = tp + "";
		writer.writeNext(outputLine);
		
		outputLine[0] = "TN";
		outputLine[1] = tn + "";
		writer.writeNext(outputLine);
		
		outputLine[0] = "FP";
		outputLine[1] = fp + "";
		writer.writeNext(outputLine);
		
		outputLine[0] = "FN";
		outputLine[1] = fn + "";
		writer.writeNext(outputLine);
		
		outputLine[0] = "Recall";
		outputLine[1] = recall + "";
		writer.writeNext(outputLine);
		
		outputLine[0] = "Precision";
		outputLine[1] = precision + "";
		writer.writeNext(outputLine);
		
		outputLine[0] = "Accuracy";
		outputLine[1] = accuracy + "";
		writer.writeNext(outputLine);
		
		
		writer.close();
	}
	
	public  void printIntervals(){
		for (int i = 0; i < this.votingPredictors.size(); i ++ ) {
			System.out.println("~~~~~~~~~~~~~~`Predictor " + i + "~~~~~~~~~~~~~~~~~~~~");
			IntervalBasedPredictor predictor = this.votingPredictors.get(i);
			predictor.printAdjustedIntervals();
			
		}
		
	}
	
	public static void main(String[] args) throws IOException{
		String modelPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\normalizedFeatures3-morevalues.csv";
		String trainingFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m12\\all";//training data
		//String testFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m13\\all";//test data
		String testFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m17\\all-new";//test data
		String outputFile = "G:\\Dropbox\\research\\3rd\\IntervalBased\\voting\\12-17-60-new.csv";
		int[] featureIndex = new int[3];
		for (int i = 0; i < featureIndex.length; i ++) {
			featureIndex[i] = i;
		}
		
		VotingPredictorDriver driver = new VotingPredictorDriver(modelPath, trainingFolder, testFolder, featureIndex);
		driver.votingPredict(outputFile);
		//driver.printIntervals();
	}
	
}
