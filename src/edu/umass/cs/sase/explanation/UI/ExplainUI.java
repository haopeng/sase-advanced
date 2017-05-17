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
package edu.umass.cs.sase.explanation.UI;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import org.jfree.data.xy.XYSeriesCollection;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeatureComparator;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.distancefunction.ValueFeatureComparator;
import edu.umass.cs.sase.explanation.evaluation.EvaluationManager;
import edu.umass.cs.sase.explanation.featuregeneration.IntervalFeatureGenerator;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.RawEventReader;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesRawGenerator;
import edu.umass.cs.sase.explanation.featuregeneration.WindowTimeSeriesGenerator;
import edu.umass.cs.sase.explanation.featureselection.ClusterCorrelationGreedyFilter;
import edu.umass.cs.sase.explanation.featureselection.GreedyIncreaseFilter;
import edu.umass.cs.sase.explanation.featureselection.MaxDistanceBudgetKFilter;
import edu.umass.cs.sase.explanation.featureselection.MaxDistanceCorrelationSumFilter;
import edu.umass.cs.sase.explanation.featureselection.MaxDistanceEqualPenaltyFilter;
import edu.umass.cs.sase.explanation.featureselection.MaxDistanceCorrelationFilter;
import edu.umass.cs.sase.explanation.featureselection.MaxDistanceInequalPenaltyFilter;
import edu.umass.cs.sase.explanation.featureselection.MaxValueBudgetCostFilter;
import edu.umass.cs.sase.explanation.featureselection.MaxValueCorrelationFilter;
import edu.umass.cs.sase.explanation.featureselection.MaxValueEqualPenaltyFilter;
import edu.umass.cs.sase.explanation.featureselection.MaxValueInequalPenaltyFilter;
import edu.umass.cs.sase.explanation.featureselection.MinimizeCostValueGoalFilter;
import edu.umass.cs.sase.explanation.featureselection.RandomIncreaseFilter;
import edu.umass.cs.sase.explanation.featureselection.AntiGreedyIncreaseFilter;
import edu.umass.cs.sase.explanation.featureselection.ValueCostFilter;
import edu.umass.cs.sase.visualize.XYLineChart;

public class ExplainUI {
	//public static PrintWriter out;
	
	public static String name;
	public static long abnormalStart;
	public static long abnormalEnd;
	public static long referenceStart;
	public static long referenceEnd;
	public static long currentPartitionId;
	
	public static String inputFolder;
	public static String outputFolder;
	public static String outputFile;
	
	public static String groundTruthFilePath;
	
	public static int topK;
	
	public static void getProperties(String propFilePath) throws IOException {
		Properties prop = new Properties();
 		InputStream inputStream = new FileInputStream(propFilePath);
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
		
		if (prop.getProperty("excludePartition").equalsIgnoreCase("true")) {
			ExplanationSettings.excludePartition = true;
		} else {
			ExplanationSettings.excludePartition = false;
		}
		
		System.out.println("Experiment name=\t" + name);
		System.out.println("abnormalStart=\t" + abnormalStart);
		System.out.println("abnormalEnd=\t" + abnormalEnd);
		System.out.println("referenceStart=\t" + referenceStart);
		System.out.println("referenceEnd=\t" + referenceEnd);
		
		System.out.println("currentPartitionId=\t" + currentPartitionId);
		System.out.println("inputFolder=\t" + inputFolder);
		System.out.println("outputFolder=\t" + outputFolder);
		
		// create the output file, write configurations to this file
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HHmmss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
		outputFile = outputFolder + name + "-" + strDate + ".txt"; 
		
		/*
		ExplanationSettings.out= new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
		
		ExplanationSettings.out.println("Start time=\t" + strDate);
		ExplanationSettings.out.println("Experiment name=\t" + name);
		ExplanationSettings.out.println("abnormalStart=\t" + abnormalStart);
		ExplanationSettings.out.println("abnormalEnd=\t" + abnormalEnd);
		ExplanationSettings.out.println("referenceStart=\t" + referenceStart);
		ExplanationSettings.out.println("referenceEnd=\t" + referenceEnd);
		
		ExplanationSettings.out.println("currentPartitionId=\t" + currentPartitionId);
		ExplanationSettings.out.println("inputFolder=\t" + inputFolder);
		ExplanationSettings.out.println("outputFolder=\t" + outputFolder);
		*/
		
		if (ExplanationSettings.excludePartition) {
			System.out.println("Exclude partition");
			//ExplanationSettings.out.println("Exclude partition");
		} else {
			System.out.println("Include partition");
			//ExplanationSettings.out.println("Include partition");
		}
		

		//ExplanationSettings.out.close();
	}
	

	
	public static void main(String[] args) throws Exception {
		
		
		
		//String inputFolder = "/Users/haopeng/Copy/Data/test/input";
		//String inputFolder = "/Users/haopeng/Copy/Data/test2013/input/test";
		//String inputFolder = "/Users/haopeng/Copy/Data/test2013/input/";
		
		//String inputFolder = "/Users/haopeng/Copy/Data/test2013/input/test2";

		//m16
		/*
		abnormalStart = 1373658550307L;
		abnormalEnd = 1373659789027L;
		referenceStart = 1373659789027L;
		referenceEnd = 1373661672953L;
		currentPartitionId = 1115040023;
		*/
		
		//m14
		/*
		long abnormalStart = 1373647741800L;
		long abnormalEnd = 1373649648791L;
		long referenceStart = 1373649648791L;
		long referenceEnd = 1373651635718L;
		long currentPartitionId = 1115040021;
		*/
		
		//m12
		/*
		long abnormalStart = 1373637583030L;
		long abnormalEnd = 1373639906229L;
		long referenceStart = 1373639906229L;
		long referenceEnd = 1373642072543L;
		long currentPartitionId = 1115040019;
		*/
		
		//test
		/*
		long abnormalStart = 1000L;
		long abnormalEnd = 5000L;
		long referenceStart = 6000L;
		long referenceEnd = 10000L;
		
		int timeWindowSize = 2000;
		*/
		
		String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-2f.properties";//high memory||Usecase NO.1
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f-chunk.properties";//high memory||Usecase NO.1
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f-cross.properties";//high memory||Usecase NO.1, cross
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case2/c3-1f.properties";//high memory||Usecase No.2
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case2/c3-1-cross.properties";//high memory||Usecase No.2, cross
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2-1f.properties";//writing disk||Usecase No.4
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2-1f-cross.properties";//writing disk||Usecase No.4, cross
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case5/c2-1f.properties";//writing disk||Usecase No.5
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case5/c2-1f-cross.properties";//writing disk||Usecase No.5, cross
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case6/a2-1f.properties";//writing disk||Usecase No.6
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case6/a2-1f-cross.properties";//writing disk||Usecase No.6, cross
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//cpu busy||Usecase No.7
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f-cross.properties";//cpu busy||Usecase No.7, cross
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case8/c4-1f.properties";//cpu busy||Usecase No.8
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case8/c4-1f-cross.properties";//cpu busy||Usecase No.8, cross
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case9/a4-1f.properties";//cpu busy||Usecase No.9
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case9/a4-1f-cross.properties";//cpu busy||Usecase No.9, cross
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case10/b5-1f.properties";//cpu busy||Usecase No.10
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case10/b5-1f-cross.properties";//cpu busy||Usecase No.10, cross
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case11/c5-1f.properties";//cpu busy||Usecase No.11
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case11/c5-1f-cross.properties";//cpu busy||Usecase No.11, cross
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5-1f.properties";//cpu busy||Usecase No.12
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5-1f-cross.properties";//cpu busy||Usecase No.12, cross
		
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/m12f.properties";
		
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b0-1-test.properties";
		
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-3f.properties";//high memory
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/c3-1f.properties";
		
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b0-1f.properties";//no anomalies
		
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/c0-2f.properties";
		if (args.length > 0) {
			inputPropertiesFile = args[0];
		}
		
		getProperties(inputPropertiesFile);
		
		int timeWindowSize = 30000;//milliseconds
		
		
		
		EvaluationManager evaluationManager = new EvaluationManager(groundTruthFilePath, topK);//todo
		
		
		ExplanationProfiling profiler = ExplanationProfiling.getInstance();
		profiler.setOverallStart(System.currentTimeMillis());
		
		
		
		
		profiler.setRawEventReadingStart(System.currentTimeMillis());
		RawEventReader reader = new RawEventReader(inputFolder, abnormalStart, abnormalEnd, referenceStart, referenceEnd, currentPartitionId);		
		profiler.setRawEventReadingEnd(System.currentTimeMillis());
		
		reader.printRawEventsInformation();
		
		//generate raw time series
		profiler.setTimeSeriesRawGenerationStart(System.currentTimeMillis());
		TimeSeriesRawGenerator abnormalTSRawGenerator = new TimeSeriesRawGenerator(reader.getSchemas(), reader.getAbnormalRawEventLists(), reader.getAbnormalStart(), reader.getAbnormalEnd(), LabelType.Abnormal);
		TimeSeriesRawGenerator referenceTSRawGenerator = new TimeSeriesRawGenerator(reader.getSchemas(), reader.getReferenceRawEventLists(), reader.getReferenceStart(), reader.getReferenceEnd(), LabelType.Reference);
		profiler.setTimeSeriesRawGenerationEnd(System.currentTimeMillis());
		
		//generate aggregations over entire interval
		profiler.setIntervalFeatureGenerationStart(System.currentTimeMillis());
		IntervalFeatureGenerator abnormalIFGenerator = new IntervalFeatureGenerator(abnormalTSRawGenerator.getTimeSeriesRawFeatures(), LabelType.Abnormal);
		IntervalFeatureGenerator referenceIFGenerator = new IntervalFeatureGenerator(referenceTSRawGenerator.getTimeSeriesRawFeatures(), LabelType.Reference);
		profiler.setIntervalFeatureGenerationEnd(System.currentTimeMillis());
		
		//aggregations over sliding windows
		profiler.setWindowTimeSeriesGenerationStart(System.currentTimeMillis());
		WindowTimeSeriesGenerator abnormalWTSGenerator = new WindowTimeSeriesGenerator(abnormalTSRawGenerator.getTimeSeriesRawFeatures(), timeWindowSize, LabelType.Abnormal); // 60 secons(60000 milliseconds)
		WindowTimeSeriesGenerator referenceWTSGenerator = new WindowTimeSeriesGenerator(referenceTSRawGenerator.getTimeSeriesRawFeatures(), timeWindowSize, LabelType.Reference);
		profiler.setWindowTimeSeriesGenerationEnd(System.currentTimeMillis());
		
		
		//compute distance for value features, and output top 10
		profiler.setValueFeatureCompareStart(System.currentTimeMillis());
		ValueFeatureComparator valueFeatureComparator = new ValueFeatureComparator(abnormalIFGenerator.getIntervalFeatures(), referenceIFGenerator.getIntervalFeatures());
		valueFeatureComparator.printTop(500);
		profiler.setValueFeatureCompareEnd(System.currentTimeMillis());
		
		
		
		//compute distance for time series features,and output top 10
		profiler.setTimeseriesFeatureCompareStart(System.currentTimeMillis());
		TimeSeriesFeatureComparator tsFeatureComparator = new TimeSeriesFeatureComparator(abnormalTSRawGenerator.getTimeSeriesRawFeatures(), referenceTSRawGenerator.getTimeSeriesRawFeatures(), 
				abnormalWTSGenerator.getWindowTimeSeriesFeatures(), referenceWTSGenerator.getWindowTimeSeriesFeatures());
		
		
		int k = 500;
		
		
		tsFeatureComparator.computeEuclideanDistance(k);
		//evaluationManager.evaluate(tsFeatureComparator.getAggFeaturePairs(), topK, "EuclideanDistance");
		evaluationManager.evaluate(tsFeatureComparator, topK, "EuclideanDistance");
		
		
		
		tsFeatureComparator.computeManhattanDistance(k);
		//evaluationManager.evaluate(tsFeatureComparator.getAggFeaturePairs(), topK, "ManhattanDistance");
		evaluationManager.evaluate(tsFeatureComparator, topK, "ManhattanDistance");
		
		
		tsFeatureComparator.computeEntropyDistance(k);
		//evaluationManager.evaluate(tsFeatureComparator.getAggFeaturePairs(), topK, "EntropyDistance");
		evaluationManager.evaluate(tsFeatureComparator, topK, "EntropyDistance");
		
		
		// optimization functions
		
		ArrayList<TimeSeriesFeaturePair> sortedFeatures = tsFeatureComparator.returnAllTimeSeriesFeatureListRanked();
		
		/*
		//filter by value cost filter
		ValueCostFilter tFilter = new ValueCostFilter(sortedFeatures);
		tFilter.filter();
		tFilter.print();
		
		//filter by top-k filter
		MaxDistanceBudgetKFilter kFilter = new MaxDistanceBudgetKFilter(sortedFeatures);
		kFilter.filter(6);
		kFilter.print();
		
		
		//filter by cost budget filter
		MaxValueBudgetCostFilter cFilter = new MaxValueBudgetCostFilter(sortedFeatures);
		cFilter.filter(0);
		cFilter.print();
		
		//filter by value goal filter
		MinimizeCostValueGoalFilter vFilter = new MinimizeCostValueGoalFilter(sortedFeatures);
		vFilter.filter(5);
		vFilter.print();
		
		//filter by inequal penalty on size filter
		MaxDistanceInequalPenaltyFilter iFilter = new MaxDistanceInequalPenaltyFilter(sortedFeatures);
		iFilter.filter(2);
		iFilter.print();
		*/
		
		/* 
		 //tuning parameter
		for (int i = 0; i <= 1000; i ++) {
			MaxDistanceInequalPenaltyFilter ipFilter = new MaxDistanceInequalPenaltyFilter(sortedFeatures);
			double lambda = 0.01 * (double)i;
			ipFilter.filter(lambda);
			ipFilter.printSimple();
		
		}
		*/
		
		
		
		//filter by equal penalty on size filter
		/*
		MaxDistanceEqualPenaltyFilter eFilter = new MaxDistanceEqualPenaltyFilter(sortedFeatures);
		eFilter.filter(0.9);
		eFilter.print();
		*/
		
		/*
		for (int i = 0; i <= 2000; i ++) {
			MaxDistanceEqualPenaltyFilter epFilter = new MaxDistanceEqualPenaltyFilter(sortedFeatures);
			double lambda = 0.001 * (double) i;
			epFilter.filter(lambda);
			epFilter.printSimple();
		}
		*/
		
		
		/*
		//filter by inequal penalty on cost filter
		MaxValueInequalPenaltyFilter viFilter = new MaxValueInequalPenaltyFilter(sortedFeatures);
		viFilter.filter(1.9); // value below 2 get 40 features, while value >= 2 get only 1 feature
		viFilter.print();
		*/
		/*
		for (int i = 0; i <= 1000; i ++) {
			MaxValueInequalPenaltyFilter vipFilter = new MaxValueInequalPenaltyFilter(sortedFeatures);
			double lambda = 0.01 * (double)i;
			vipFilter.filter(lambda); // value below 2 get 40 features, while value >= 2 get only 1 feature
			vipFilter.printSimple();
		
		}
		*/
		
		
		//filter by equal penalty on cost filter
		/*
		MaxValueEqualPenaltyFilter veFilter = new MaxValueEqualPenaltyFilter(sortedFeatures);
		veFilter.filter(1);//1 is like requires value > cost
		veFilter.print();
		*/
		/*
		for (int i = 0; i <= 1000; i ++) {
			MaxValueEqualPenaltyFilter vepFilter = new MaxValueEqualPenaltyFilter(sortedFeatures);
			double lambda = 0.01 * (double)i;
			vepFilter.filter(lambda);//1 is like requires value > cost
			vepFilter.printSimple();
		}
		*/
		
		/*
		MaxDistanceCorrelationSumFilter dcsFilter = new MaxDistanceCorrelationSumFilter(sortedFeatures);
		double lambda1 = 0.9;
		double lambda2 = 0.05;
		dcsFilter.filter(lambda1, lambda2);
		dcsFilter.print();
		*/
		
		
		/*
		// maximized gain
		FastestIncreaseFilter ffFilter = new FastestIncreaseFilter(sortedFeatures);
		ffFilter.filter();		
		ffFilter.print();
		*/
		
		/*
		GreedyIncreaseFilter gFilter = new GreedyIncreaseFilter(sortedFeatures);
		gFilter.preFilter();
		gFilter.computeCorrelationIndex();
		*/
		//gFilter.visualizeCluster(10, 0.5);
		
		//write features and correlations to csv files
		//gFilter.writePreSelectedFeaturesToCSV("/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/features.csv");
		//gFilter.writeCorrelationIndexToCSV("/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/correlationindex-full.csv");
		
		//find common features among the top features of greedy
		
		
		/*
		GreedyIncreaseFilter gFilter = new GreedyIncreaseFilter(sortedFeatures);
		gFilter.filterAndFindCommonFeatures(10);
		*/
		
		/*
		ClusterCorrelationGreedyFilter ccFilter = new ClusterCorrelationGreedyFilter(sortedFeatures);
		ccFilter.filter(10);
		*/
		
		//validated features try greedy!
		//progress
		/*
		String[] strs ={
				"HadoopDataActivity-TimeSeriesFrequency",
				"proc_total_event-value-TimeSeriesMean",
				"mem_free_event-value-TimeSeriesMean",
				"swap_free_event-value-TimeSeriesMean",
				"RequestFinish-TimeSeriesFrequency",
				"mem_buffers_event-value-TimeSeriesMean",
				"proc_total_event-value-TimeSeriesMean"
		};
		*/
		
		
		/*
		//greedy
		String[] strs ={
				"proc_total_event-value-TimeSeriesMean",
				"HadoopDataActivity-TimeSeriesFrequency",
				"mem_free_event-value-TimeSeriesMean",
				"swap_free_event-value-TimeSeriesMean",
				"RequestFinish-TimeSeriesFrequency",
				"mem_cached_event-value-TimeSeriesMean",
				"mem_buffers_event-value-TimeSeriesMean"

		};
		HashSet<String> validatedFeatureNames = new HashSet<String>();
		for (String s : strs) {
			validatedFeatureNames.add(s);
		}
		
		GreedyIncreaseFilter gFilter = new GreedyIncreaseFilter(sortedFeatures);
		gFilter.filterWithValidatedFeatures(validatedFeatureNames, 5);
		*/
		
		//random gain
		RandomIncreaseFilter rFilter = new RandomIncreaseFilter(sortedFeatures);
		rFilter.filter(10);
		
		
		AntiGreedyIncreaseFilter sFilter = new AntiGreedyIncreaseFilter(sortedFeatures);
		//sFilter.filter(162);
		sFilter.filter(10);
		
		GreedyIncreaseFilter gFilter = new GreedyIncreaseFilter(sortedFeatures);
		gFilter.filter(10);
		
		//visualize into one plot
		ArrayList<XYSeriesCollection> datasetList = new ArrayList<XYSeriesCollection>();
		datasetList.add(sFilter.getDataset());
		datasetList.add(rFilter.getDataset());
		datasetList.add(gFilter.getDataset());
		String visualizeFileFolder = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/dimishing return/";
		String fileName = rFilter.getSaveFilePrefix() + ".png";
		String chartTitle = "Gain of Selected Features";
		String xLabel = "Number of Features Selected";
		String yLabel = "Accumulative Distance Gain";
		XYLineChart chart = new XYLineChart(visualizeFileFolder + fileName, datasetList, chartTitle, xLabel, yLabel);
		chart.visualize();
		
		
		
		
		//filter by max distance with correlation filter
		//MaxDistanceCorrelationFilter dcFilter = new MaxDistanceCorrelationFilter(sortedFeatures);
		//dcFilter.filter(0.9);
		//dcFilter.print();
		
		//filter by max value with correlation filter
		//MaxValueCorrelationFilter vcFilter = new MaxValueCorrelationFilter(sortedFeatures);
		//vcFilter.filter(0.9);
		//vcFilter.print();
		
		
		//tFilter.filterFeaturesUsingEntropyThreshold(tsFeatureComparator);
		//evaluation again
		//evaluationManager.evaluate(tFilter.getValuableFeatures(), tFilter.getValuableFeatures().size() + 1, "FilteredEntropyDistance");
		
		/*
		tsFeatureComparator.computeDTWDistance(k);
		//evaluationManager.evaluate(tsFeatureComparator.getAggFeaturePairs(), topK, "DTWDistance");
		evaluationManager.evaluate(tsFeatureComparator, topK, "DTWDistance");
		
		tsFeatureComparator.computeLCSSDistance(k);
		//evaluationManager.evaluate(tsFeatureComparator.getAggFeaturePairs(), topK, "LCSSDistance");
		evaluationManager.evaluate(tsFeatureComparator, topK, "LCSSDistance");
		
		tsFeatureComparator.computeEDRDistance(k);
		//evaluationManager.evaluate(tsFeatureComparator.getAggFeaturePairs(), topK, "EDRDistance");
		evaluationManager.evaluate(tsFeatureComparator, topK, "EDRDistance");
		
		
		
		tsFeatureComparator.computeERPDistance(k);
		//evaluationManager.evaluate(tsFeatureComparator.getAggFeaturePairs(), topK, "ERPDistance");
		evaluationManager.evaluate(tsFeatureComparator, topK, "ERPDistance");
		
		//tsFeatureComparator.computeTQuESTDistance(k);//null pointer
		//evaluationManager.evaluate(tsFeatureComparator.getAggFeaturePairs(), topK, "TQuESTDistance");
		
		//tsFeatureComparator.computeSpADeDistance(k);//training set not ready
		//evaluationManager.evaluate(tsFeatureComparator.getAggFeaturePairs(), topK, "SpADeDistance");
		
		tsFeatureComparator.computeSwaleDistance(k);
		evaluationManager.evaluate(tsFeatureComparator.getAggFeaturePairs(), topK, "SwaleDistance");
		*/
		
		profiler.setTimeseriesFeatureCompareEnd(System.currentTimeMillis());
		
		//evaluationManager.printResult();
		
		profiler.setOverallEnd(System.currentTimeMillis());
		profiler.printProfiling();
		
		//ExplanationSettings.out.flush();
		//ExplanationSettings.out.close();
	}

}
