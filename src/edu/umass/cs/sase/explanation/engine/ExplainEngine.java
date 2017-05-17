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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.crossvalidation.FeatureValidationEngine;
import edu.umass.cs.sase.explanation.crossvalidation.ValidationEngine;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeatureComparator;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.distancefunction.ValueFeatureComparator;
import edu.umass.cs.sase.explanation.evaluation.DataPoint;
import edu.umass.cs.sase.explanation.evaluation.EvaluationEngine;
import edu.umass.cs.sase.explanation.evaluation.PredictionEngine;
import edu.umass.cs.sase.explanation.featuregeneration.IntervalFeatureGenerator;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.OptimizedRawEventReader;
import edu.umass.cs.sase.explanation.featuregeneration.RawEventReader;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesRawGenerator;
import edu.umass.cs.sase.explanation.featuregeneration.WindowTimeSeriesGenerator;

/**
 * Everything is controlled by the engine
 * @author haopeng
 *
 */
public class ExplainEngine {
	
	//properties from input file******
	String name;
	long abnormalStart;
	long abnormalEnd;
	long referenceStart;
	long referenceEnd;
	long currentPartitionId;
	
	String inputFolder;
	String outputFolder;
	String outputFile;
	
	String groundTruthFilePath;
	
	int topK;
	
	String predictionPropertyFile;
	//end of properties*************
	
	int timeWindowSize = 30000;//milliseconds
	String inputPropertiesFile;
	
	
	//variables 
	RawEventReader reader;
	//generate raw time series
	TimeSeriesRawGenerator abnormalTSRawGenerator;
	TimeSeriesRawGenerator referenceTSRawGenerator;
	
	//generate aggregations over entire interval
	IntervalFeatureGenerator abnormalIFGenerator;
	IntervalFeatureGenerator referenceIFGenerator;
	
	//aggregations over sliding windows
	WindowTimeSeriesGenerator abnormalWTSGenerator;
	WindowTimeSeriesGenerator referenceWTSGenerator;

	ValueFeatureComparator valueFeatureComparator;
	TimeSeriesFeatureComparator tsFeatureComparator;
	//
	ValidationEngine validationEngine;
	
	//Evaluation
	EvaluationEngine evaluationEngine;
	PredictionEngine predictionEngine;
	
	
	
	public ExplainEngine(String inputPropertiesFile) throws IOException {
		this.inputPropertiesFile = inputPropertiesFile;
		this.getProperties();
	}
	
	public void runEngine() throws Exception {
		//read raw events
		this.readRawEvents();
		
		
		// generate features
		this.generateFeatures();
		//compute distance and sort
		this.computeDistance();
		//Label related partitions and recompute features over broader dataset
		this.validateFeatures();
		//evaluate results vs. ground truth
		this.evaluteResult();
		//evaluate by prediction
		this.evaluateByPrediction();
	}
	
	public void runEngineForSimulation() throws Exception {
		//read raw events
		this.readRawEvents();
		// generate features
		this.generateFeatures();
		//compute distance and sort
		this.computeDistance();
		//Label related partitions and recompute features over broader dataset
		this.validateFeatures();

	}

	public void evaluateByPrediction() throws Exception {
		//ArrayList<DataPoint> dataPoints = this.tsFeatureComparator.generateDataPoints();//the annotated partition
		ArrayList<DataPoint> dataPoints = this.generateDataPointsForPrediction();//another partition
		FeatureValidationEngine featureValidationEngine= this.validationEngine.getFeatureValidationEngine();
		ArrayList<HashSet<String>> clusterList = featureValidationEngine.getClusterList();
		HashMap<String, TimeSeriesFeaturePair> topFeatureIndex = featureValidationEngine.getTopfeatureIndex();
		this.predictionEngine = new PredictionEngine(dataPoints, clusterList, topFeatureIndex);
		this.predictionEngine.runEngine();
	}
	
	
	public ArrayList<DataPoint> generateDataPointsForPrediction() throws Exception {
		PredictionPartition predictionPartition = new PredictionPartition(this.predictionPropertyFile);
		return predictionPartition.generateDataPoints();
	}
	
	
	public void evaluteResult() throws IOException {
		this.evaluationEngine = new EvaluationEngine(this.groundTruthFilePath);
		FeatureValidationEngine featureValidationEngine= this.validationEngine.getFeatureValidationEngine();
		ArrayList<HashSet<String>> clusterList = featureValidationEngine.getClusterList();
		HashMap<String, TimeSeriesFeaturePair> topFeatureIndex = featureValidationEngine.getTopfeatureIndex();
		//int totalFeatures = this.tsFeatureComparator.getRawAndAggFeaturePairs().size();
		int totalFeatures = this.tsFeatureComparator.getAggFeaturePairs().size();
		this.evaluationEngine.evaluateResult(clusterList, topFeatureIndex, totalFeatures);
	}
	
	public void validateFeatures() throws Exception {
		this.validationEngine = new ValidationEngine(this.inputPropertiesFile, this.inputFolder, this.tsFeatureComparator, this.timeWindowSize);
		this.validationEngine.runEngine();
	}
	public void computeDistance() throws Exception {
		//compute distance for value features, and output top 10
		this.valueFeatureComparator = new ValueFeatureComparator(abnormalIFGenerator.getIntervalFeatures(), referenceIFGenerator.getIntervalFeatures());
		if (ExplanationSettings.printResult) {
			valueFeatureComparator.printTop(500);
		}
		
		
		
		//compute distance for time series features
		this.tsFeatureComparator = new TimeSeriesFeatureComparator(abnormalTSRawGenerator.getTimeSeriesRawFeatures(), referenceTSRawGenerator.getTimeSeriesRawFeatures(), 
				abnormalWTSGenerator.getWindowTimeSeriesFeatures(), referenceWTSGenerator.getWindowTimeSeriesFeatures());
		
		int k = 1000;
		//tsFeatureComparator.computeEntropyDistanceForRawAndAgg(k);//both agg and raw
		//tsFeatureComparator.computeEntropyDistance(k);
		tsFeatureComparator.computeEntropyDistance(k);
	
	}
	public void generateFeatures() {
		//generate raw time series
		abnormalTSRawGenerator = new TimeSeriesRawGenerator(reader.getSchemas(), reader.getAbnormalRawEventLists(), reader.getAbnormalStart(), reader.getAbnormalEnd(), LabelType.Abnormal);
		referenceTSRawGenerator = new TimeSeriesRawGenerator(reader.getSchemas(), reader.getReferenceRawEventLists(), reader.getReferenceStart(), reader.getReferenceEnd(), LabelType.Reference);
		
		//generate aggregations over entire interval
		abnormalIFGenerator = new IntervalFeatureGenerator(abnormalTSRawGenerator.getTimeSeriesRawFeatures(), LabelType.Abnormal);
		referenceIFGenerator = new IntervalFeatureGenerator(referenceTSRawGenerator.getTimeSeriesRawFeatures(), LabelType.Reference);
		
		//aggregations over sliding windows
		abnormalWTSGenerator = new WindowTimeSeriesGenerator(abnormalTSRawGenerator.getTimeSeriesRawFeatures(), timeWindowSize, LabelType.Abnormal); // 30seconds(30000 milliseconds)
		referenceWTSGenerator = new WindowTimeSeriesGenerator(referenceTSRawGenerator.getTimeSeriesRawFeatures(), timeWindowSize, LabelType.Reference);
	}
	
	public void readRawEvents() throws IOException {
		if (ExplanationSettings.printResult) {
			System.out.println("Reading raw events...");	
		}
		if (ExplanationSettings.useOptimizedRawReader) {
			this.reader = new OptimizedRawEventReader(inputFolder, abnormalStart, abnormalEnd, referenceStart, referenceEnd, currentPartitionId);		
		} else {
			this.reader = new RawEventReader(inputFolder, abnormalStart, abnormalEnd, referenceStart, referenceEnd, currentPartitionId);					
		}
		if (ExplanationSettings.printResult) {
			reader.printRawEventsInformation();
		}
		
	}
	
	public void getProperties() throws IOException {
		Properties prop = new Properties();
 		InputStream inputStream = new FileInputStream(this.inputPropertiesFile);
 		prop.load(inputStream);
 
		// get the property value and print it out
 		name = prop.getProperty("name");
 		
		abnormalStart = Long.parseLong(prop.getProperty("abnormalStart"));
		abnormalEnd = Long.parseLong(prop.getProperty("abnormalEnd"));
		referenceStart = Long.parseLong(prop.getProperty("referenceStart"));
		referenceEnd = Long.parseLong(prop.getProperty("referenceEnd"));
		currentPartitionId= Long.parseLong(prop.getProperty("currentPartitionId"));
		
		inputFolder= prop.getProperty("inputFolder");
		outputFolder= prop.getProperty("outputFolder");
		groundTruthFilePath = prop.getProperty("groundTruthFilePath");
		
		topK = Integer.parseInt(prop.getProperty("topK")); 
		
		this.predictionPropertyFile = prop.getProperty("predictionPropertyFile");
		
		if (prop.getProperty("excludePartition").equalsIgnoreCase("true")) {
			ExplanationSettings.excludePartition = true;
		} else {
			ExplanationSettings.excludePartition = false;
		}
		
		if (ExplanationSettings.printResult) {
			System.out.println("Experiment name=\t" + name);
			System.out.println("abnormalStart=\t" + abnormalStart);
			System.out.println("abnormalEnd=\t" + abnormalEnd);
			System.out.println("referenceStart=\t" + referenceStart);
			System.out.println("referenceEnd=\t" + referenceEnd);
			
			System.out.println("currentPartitionId=\t" + currentPartitionId);
			System.out.println("inputFolder=\t" + inputFolder);
			System.out.println("outputFolder=\t" + outputFolder);

		}
		
		// create the output file, write configurations to this file
		/*
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HHmmss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		outputFile = outputFolder + name + "-" + strDate + ".txt"; 
		*/
		if (ExplanationSettings.excludePartition) {
			if (ExplanationSettings.printResult) {
				System.out.println("Exclude partition");	
			}
		} else {
			if (ExplanationSettings.printResult) {
				System.out.println("Include partition");				
			}
		}
	}
	
	
	public void buildFeaturePairs() throws Exception {
		this.tsFeatureComparator = new TimeSeriesFeatureComparator(abnormalTSRawGenerator.getTimeSeriesRawFeatures(), referenceTSRawGenerator.getTimeSeriesRawFeatures(), 
				abnormalWTSGenerator.getWindowTimeSeriesFeatures(), referenceWTSGenerator.getWindowTimeSeriesFeatures());
	}

}
