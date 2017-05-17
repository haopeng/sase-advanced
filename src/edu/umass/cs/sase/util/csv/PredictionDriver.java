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
package edu.umass.cs.sase.util.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import au.com.bytecode.opencsv.CSVWriter;



public class PredictionDriver {
	public String modelPath;
	public String pointsFolder;
	public DistanceBasedPredictor predictor;
	public ArrayList<SamplePoint> points;
	
	
	
	public PredictionDriver(String modelPath, String pFolder) throws IOException {
		this.modelPath = modelPath;
		this.pointsFolder = pFolder;
		this.points = new ArrayList<SamplePoint>();
		this.readData();
	}
	public void readData() throws IOException{
		this.predictor = new DistanceBasedPredictor(this.modelPath);
		//ArrayList<SamplePoint> points = new ArrayList<SamplePoint>();
		File folder = new File(pointsFolder);
		File[] files = folder.listFiles();
		for(File f: files){
			SamplePoint point = new SamplePoint(f.getAbsolutePath());
			this.points.add(point);
		}
	}
	
	public void predictTopK(){
		for(int i = 0; i < this.predictor.features.size(); i ++){
			int correct = 0;
			int incorrect = 0;
			for(SamplePoint p: this.points){
				boolean result = this.predictor.predictUsingTopKFeature(i + 1, p);
				//System.out.println("Prediction correct:" + result);
				if(result){
					correct ++;
				}else{
					incorrect ++;
				}
			}
			double precision = (double)correct/(double)(correct + incorrect) * 100;
			//System.out.println((i+1) + "\t" + precision);
			System.out.println( precision);

		}
	}
	

	
	public void predictSingle(){
		for(int i = 0; i < this.predictor.features.size(); i ++){
			int correct = 0;
			int incorrect = 0;
			for(SamplePoint p: this.points){
				boolean result = this.predictor.predictUsingSingleFeature(i, p);
				//System.out.println("Prediction correct:" + result);
				if(result){
					correct ++;
				}else{
					incorrect ++;
				}
			}
			double precision = (double)correct/(double)(correct + incorrect) * 100;
			//System.out.println((i+1) + "\t" + precision);
			System.out.println( precision);

		}
	}
	
	public void predictSinglePrecision(){
		for(int i = 0; i < this.predictor.features.size(); i ++){
			double tp = 0.0;
			double  tn = 0.0;
			double fp = 0.0;
			double fn = 0.0;
			for(SamplePoint p: this.points){
				Precision result = this.predictor.predictUsingSingleFeaturePrecision(i, p);
				//System.out.println("Prediction correct:" + result);
				switch(result){
					case TruePositive:
						tp += 1.0;
						break;
					case TrueNegative:
						tn += 1.0;
						break;
					case FalsePositive:
						fp += 1.0;
						break;
					case FalseNegative:
						fn += 1.0;
						break;
				}
				
			}
			double precision = tp / (tp + fp) * 100;
			double recall = tp/ (tp + fn) * 100;
			
			System.out.println(i + ":" + tp + "\t" + fp + "\t" + fn + "\t" + precision + "\t" + recall);

		}
	}
	public void predictSinglePrecisionInrementally(){
		//for(int i = 0; i < this.predictor.features.size(); i ++){
		/*
		for(int i = 1; i < 2; i ++){
			this.predictor.predictUsingSingleFeatureIncrementally(i, points);// 
		}
		*/
		Collections.shuffle(this.points, new Random(System.nanoTime()));
		this.predictor.predictUsingSingleFeatureIncrementally(153, points);//
	}
	
	
	public void predictSingleFeatureByValueInrementally(boolean increasingOrder){
		//for(int i = 0; i < this.predictor.features.size(); i ++){
		/*
		for(int i = 1; i < 2; i ++){
			this.predictor.predictUsingSingleFeatureIncrementally(i, points);// 
		}
		*/
		Collections.shuffle(this.points, new Random(System.nanoTime()));
		this.predictor.predictUsingSingleFeatureByValueIncrementally(1, points, increasingOrder);//
	}
	
	public void computeAvePrecision(){//by original feature values
		
		
		for(int i = 0; i < this.predictor.features.size(); i ++){
			Collections.shuffle(this.points, new Random(System.nanoTime()));
			this.predictor.getAvePrecisionUsingSingleFeatureIncrementally(i, points);//original values
		}
		//this.predictor.getAvePrecisionUsingSingleFeatureIncrementally(0, points);
	}
	
	public void writeAllPointsToOneFile(String outputFile) throws IOException{
		CSVWriter writer = new CSVWriter(new FileWriter(outputFile), ',',CSVWriter.NO_QUOTE_CHARACTER);
		LinkedList<DistanceBasedFeature> features = this.predictor.getFeatures();
		String[] featureName = new String[features.size() + 1];
		featureName[0] = "Label";
		for(int i = 0; i < features.size(); i ++){
			featureName[i + 1] = features.get(i).featureSignature;
		}
		String[] line = featureName;
		writer.writeNext(line);
		for(SamplePoint p: this.points){
			HashMap<String, ComputedFeature> pointFeatures = p.getFeatures();
			line = new String[featureName.length + 1];
			if(p.getTrueLabel() == PointLabel.Abnormal){
				line[0] = "Abnormal";
			}else{
				line[0] = "Normal";
			}
			for(int i = 1; i < featureName.length; i ++){
				line[i] = pointFeatures.get(featureName[i]).getFeatureValue() + "";
			}
			writer.writeNext(line);
		}
		writer.close();
	}
	

	public void printBasicInfo(){
		System.out.println("total points:" + this.points.size());
		this.predictor.printFeaturesByDistanceOrder();
		
	}
	
	/**
	 * 
	 * @param outputFolder specifies the outputFolder, this method will output two files into this folder, normal.csv and abnormal.csv
	 * @param topN specifies how many top features will be output
	 * @throws IOException 
	 */
	public void computeValueDistribution(String outputFolder, int topN) throws IOException{
		ArrayList<SamplePoint> abnormalPoints = new ArrayList<SamplePoint>();
		ArrayList<SamplePoint> normalPoints = new ArrayList<SamplePoint>();
		for(SamplePoint p: this.points){
			if(p.getTrueLabel() == PointLabel.Abnormal){
				abnormalPoints.add(p);
			}else{
				normalPoints.add(p);
			}
		}
		this.predictor.computeValueDistribution(abnormalPoints, outputFolder +"abnormal-top" +topN+ ".csv", topN);
		this.predictor.computeValueDistribution(normalPoints, outputFolder +"normal-top" + topN + ".csv", topN);
		
 	}
	
	public void computeDistanceDistribution(String outputFolder, int topN) throws IOException{
		ArrayList<SamplePoint> abnormalPoints = new ArrayList<SamplePoint>();
		ArrayList<SamplePoint> normalPoints = new ArrayList<SamplePoint>();
		for(SamplePoint p: this.points){
			if(p.getTrueLabel() == PointLabel.Abnormal){
				abnormalPoints.add(p);
			}else{
				normalPoints.add(p);
			}
		}
		this.predictor.computeDistanceDistribution(abnormalPoints, outputFolder +"abnormal-top" +topN+ ".csv", topN);
		this.predictor.computeDistanceDistribution(normalPoints, outputFolder +"normal-top" + topN + ".csv", topN);
		
 	}
	public void computeTupleDistribution(String outputFolder, int topN) throws IOException{
		File f = new File(outputFolder);
		if(!f.exists()){
			f.mkdirs();
		}
		this.predictor.computeTupleDistribution(points, outputFolder, topN);
 	}
	

	public static void main(String args[]) throws IOException{
		String modelPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\normalizedFeatures3-morevalues.csv";
		//String pointFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m12\\all";//about 105 points
		String pointFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m17\\all-new";//about 105 points
		PredictionDriver driver = new PredictionDriver(modelPath, pointFolder);
		
		//merge all points
		//String outputFolder = "G:\\Dropbox\\research\\3rd\\visualization\\distribution\\value\\m12\\allPoints.csv";
		String outputFolder = "G:\\Dropbox\\research\\3rd\\visualization\\distribution\\value\\m17";
		File folder = new File(outputFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String outputFile = outputFolder + "\\allPoints-new.csv";
		driver.writeAllPointsToOneFile(outputFile);
		System.out.println("Merge completed");
		
		
		
		//driver.printBasicInfo();
		//driver.predictSingle();
		 //driver.predictTopK();
		
		//driver.predictSinglePrecision();
		
		//driver.predictSinglePrecisionInrementally();//draw the curve
		
		//compute value distribution
		//String outputFolder = "G:\\Dropbox\\research\\3rd\\visualization\\distribution\\value\\m16\\";
		//driver.computeValueDistribution(outputFolder, 20);
		
		
		//compute tuple distribution for visualization
		/*
		int n = 154;
		String outputFolder = "G:\\Dropbox\\research\\3rd\\visualization\\distribution\\tuple\\m12\\top" + n + "\\";
		
		driver.computeTupleDistribution(outputFolder, n);
		*/
		//merge all points
		//String outputFolder = "G:\\Dropbox\\research\\3rd\\visualization\\distribution\\value\\m14\\allPoints.csv";
		//driver.writeAllPointsToOneFile(outputFolder);
		
		//compute average precision
		//driver.computeAvePrecision();//by original feature values
		//driver.predictSingleFeatureByValueInrementally(true);//increasing or not
		
		
		
		
	}
}
