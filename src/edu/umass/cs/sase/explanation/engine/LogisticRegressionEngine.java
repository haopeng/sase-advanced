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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVWriter;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.evaluation.DataPoint;


/**
 * This engine doesn't do the analysis
 * It only output data for logistic regression, which is done in R.
 * Output format: csv files
 * @author haopeng
 *
 */
public class LogisticRegressionEngine extends ExplainEngineCrossValidation{
	String outputCSVFilePath;
	ArrayList<DataPoint> trainingPoints;
	ArrayList<DataPoint> trainingPointsForRawFeatures;
	String outputCSVFilePathForRawFeatures;
	public LogisticRegressionEngine(String inputPropertiesFile, String outputCSVFilePath)
			throws IOException {
		super(inputPropertiesFile);
		this.outputCSVFilePath = outputCSVFilePath;
	}

	public LogisticRegressionEngine(String inputPropertiesFile, String outputCSVFilePath, String outputCSVFilePathForRawFeatures)
			throws IOException {
		super(inputPropertiesFile);
		this.outputCSVFilePath = outputCSVFilePath;
		this.outputCSVFilePathForRawFeatures = outputCSVFilePathForRawFeatures;
	}

	public void runEngine(int fold) throws Exception {
		this.fold = fold;
		this.initialize();
		//read raw events
		this.readRawEvents();
		// generate features
		this.generateFeatures();	
		//compute distance
		this.computeDistance();
		//get count
		this.getPointCount();
		//prepare shuffle array
		this.prepareShuffle(abnormalCount, referenceCount);
		for (int i = 0; i < fold; i ++) {
			System.out.println("Cross validation " + i + " of " + fold + " iterations");
			//generate features
			this.generateFeatures();
			//compute distance
			this.computeDistance();
			//sample test point
			this.samplePoints(i);
			//get training point
			this.prepareDataPoints();
			//write out data points
			this.writeDataPointsToCSV(this.trainingPoints, this.outputCSVFilePath + "training-" + (i + 1) + ".csv");
			this.writeDataPointsToCSV(this.testPoints, this.outputCSVFilePath + "test-" + (i + 1) + ".csv");
		}
		
		System.out.println("All " + fold + " data points are written to " + this.outputCSVFilePath);
	}
	
	
	public void runEngine() throws Exception {
		//read raw events
		this.readRawEvents();
		// generate features
		this.generateFeatures();	
		//build pairs
		//this.buildFeaturePairs();
		this.computeDistance();
		//generate datapoints
		this.prepareDataPoints();		
		//output datapoints to csv file, 
		//sample file: /Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/value/m12/m12.csv
		this.writeDataPointsToCSV(this.trainingPoints, this.outputCSVFilePath);
		
	}
	
	public void runEngineWithRawFeatures() throws Exception {
		//read raw events
		this.readRawEvents();
		// generate features
		this.generateFeatures();	
		//build pairs
		//this.buildFeaturePairs();
		this.computeDistance();
		//generate datapoints
		this.prepareDataPointsWithRawFeatures();
		//output datapoints to csv file, 
		//sample file: /Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/value/m12/m12.csv
		this.writeDataPointsToCSV(this.trainingPoints, this.outputCSVFilePath);
		this.writeDataPointsToCSVRaw(this.trainingPointsForRawFeatures, this.outputCSVFilePathForRawFeatures);
	}
	
	public void writeDataPointsToCSV(ArrayList<DataPoint> points, String csvPath) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(csvPath));
		ArrayList<TimeSeriesFeaturePair> sortedFeatures = this.tsFeatureComparator.returnAggFeatureListRanked();//used to make a deterministic order of features
		System.out.println("sortedFeature size=" + sortedFeatures.size());
		//output header
		String[] header = new String[sortedFeatures.size() + 2];
		header[0] = "No.";
		header[1] = "Label";
		for (int i = 0; i < sortedFeatures.size(); i ++) {
			header[i + 2] = sortedFeatures.get(i).getFeatureName();
		}
		writer.writeNext(header);
		//output content: no. label, attribute values
		String[] str = new String[header.length];
		if (points == null) {
			System.out.println("null");
		}
		for (int i = 0; i < points.size(); i ++) {
			DataPoint point = points.get(i);
			str[0] = "" + (i + 1);
			str[1] = "" + point.getTrueLabel();
			HashMap<String, Double> values = point.getValues();
			for (int j = 0; j < sortedFeatures.size(); j ++) {
				str[j + 2] = values.get(header[j + 2]) + "";
			}
			writer.writeNext(str);
		}
		
		writer.close();
	}
	
	public void writeDataPointsToCSVRaw(ArrayList<DataPoint> points, String csvPath) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(csvPath));
		ArrayList<TimeSeriesFeaturePair> sortedFeatures = this.tsFeatureComparator.returnRawFeatureListRanked();//used to make a deterministic order of features
		System.out.println("sortedFeature size=" + sortedFeatures.size());
		//output header
		String[] header = new String[sortedFeatures.size() + 2];
		header[0] = "No.";
		header[1] = "Label";
		for (int i = 0; i < sortedFeatures.size(); i ++) {
			header[i + 2] = sortedFeatures.get(i).getFeatureName();
		}
		writer.writeNext(header);
		//output content: no. label, attribute values
		String[] str = new String[header.length];
		if (points == null) {
			System.out.println("null");
		}
		for (int i = 0; i < points.size(); i ++) {
			DataPoint point = points.get(i);
			str[0] = "" + (i + 1);
			str[1] = "" + point.getTrueLabel();
			HashMap<String, Double> values = point.getValues();
			for (int j = 0; j < sortedFeatures.size(); j ++) {
				str[j + 2] = values.get(header[j + 2]) + "";
			}
			writer.writeNext(str);
		}
		
		writer.close();
	}
	
	
	public void prepareDataPoints() {
		this.trainingPoints = this.tsFeatureComparator.generateDataPoints();
	}
	
	public void prepareDataPointsWithRawFeatures() {
		this.trainingPoints = this.tsFeatureComparator.generateDataPoints();
		this.trainingPointsForRawFeatures = this.tsFeatureComparator.generateDataPointsWithRawFeatures();
	}

}
