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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeatureComparator;
import edu.umass.cs.sase.explanation.distancefunction.ValueFeatureComparator;
import edu.umass.cs.sase.explanation.evaluation.EvaluationManager;
import edu.umass.cs.sase.explanation.featuregeneration.IntervalFeatureGenerator;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.RawEventReader;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesRawGenerator;
import edu.umass.cs.sase.explanation.featuregeneration.WindowTimeSeriesGenerator;


/**
 * This class connects every component of the system
 * Input: annotated partition, related partitions
 * Output: clustered results (Maybe measurements later)
 * @author haopeng
 *
 */
public class AllInOneUI {
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

		if (ExplanationSettings.excludePartition) {
			System.out.println("Exclude partition");
		} else {
			System.out.println("Include partition");
		}
	}
	
	
public static void main(String[] args) throws Exception {
		
	
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-2f.properties";//high memory||Usecase NO.1
		String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f-cross.properties";//high memory||Usecase NO.1, cross
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
		
		EvaluationManager evaluationManager = new EvaluationManager(groundTruthFilePath, topK);//evaluate results, todo
		
		ExplanationProfiling profiler = ExplanationProfiling.getInstance();
		profiler.setOverallStart(System.currentTimeMillis());
		//read raw events
		profiler.setRawEventReadingStart(System.currentTimeMillis());
		RawEventReader reader = new RawEventReader(inputFolder, abnormalStart, abnormalEnd, referenceStart, referenceEnd, currentPartitionId);		
		reader.printRawEventsInformation();
		profiler.setRawEventReadingEnd(System.currentTimeMillis());
		
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
		
		
		
		//compute distance for time series features
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
		
		profiler.setTimeseriesFeatureCompareEnd(System.currentTimeMillis());
		
		profiler.setOverallEnd(System.currentTimeMillis());
		profiler.printProfiling();

	}

	
}
