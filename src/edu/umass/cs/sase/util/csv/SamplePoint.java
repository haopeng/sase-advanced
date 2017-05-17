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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVReader;

public class SamplePoint implements Comparable<SamplePoint>{
	//represents a period, which is consited of a set of computed features
	String filePath;
	public HashMap<String, ComputedFeature> features;
	
	PointLabel trueLabel;//abnormal/normal
	PointLabel predictedLabel;//abnormal/normal
	Precision statisticalClass;//truePositive/trueNegative/falsePositive/falseNegative
	
	double featuredValue;//set the value as selected feature(s) for the convenience of sorting
	
	public SamplePoint() {
		
	}
	
	
	
	public SamplePoint(String filePath) throws IOException{
		this.filePath = filePath;
		this.features = new HashMap<String, ComputedFeature>();
		this.readFeatures();
		this.setTrueLabel();
	}
	public void setTrueLabel(){
		int label = (int)features.get("Label").getFeatureValue();
		if(label == 0){
			this.trueLabel = PointLabel.Abnormal;
		}else{
			this.trueLabel = PointLabel.Normal;
		}
	}
	
	public void readFeatures() throws IOException{
		CSVReader reader = new CSVReader(new FileReader(this.filePath));
		String nextLine[] = reader.readNext();
		
		nextLine[0] = "FeatureName";//signature
		nextLine[1] = "FeatureValue";
		nextLine[2] = "MaxValue";
		nextLine[3] = "MinValue";
		
		while ((nextLine = reader.readNext()) != null) {
			String featureSignature = nextLine[0];
			double featureValue = Double.parseDouble(nextLine[1]);
			double max = Double.parseDouble(nextLine[2]);
			double min = Double.parseDouble(nextLine[3]);
			
			ComputedFeature f = new ComputedFeature(featureSignature, featureValue, max, min);
			
			this.features.put(featureSignature, f);
		}
		
		reader.close();
	}
	
	
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public HashMap<String, ComputedFeature> getFeatures() {
		return features;
	}
	public void setFeatures(HashMap<String, ComputedFeature> features) {
		this.features = features;
	}
	public PointLabel getTrueLabel() {
		return trueLabel;
	}
	public void setTrueLabel(PointLabel trueLabel) {
		this.trueLabel = trueLabel;
	}
	public PointLabel getPredictedLabel() {
		return predictedLabel;
	}
	public void setPredictedLabel(PointLabel predictedLabel) {
		this.predictedLabel = predictedLabel;
		if(this.trueLabel == PointLabel.Abnormal){
			if(this.predictedLabel == PointLabel.Abnormal){
				this.statisticalClass = Precision.TruePositive;
			}else{
				this.statisticalClass = Precision.FalseNegative;
			}
		}else{
			if(this.predictedLabel == PointLabel.Abnormal){
				this.statisticalClass = Precision.FalsePositive;
			}else{
				this.statisticalClass = Precision.TrueNegative;
			}
		}
	}
	public Precision getStatisticalClass() {
		return statisticalClass;
	}
	public void setStatisticalClass(Precision statisticalClass) {
		this.statisticalClass = statisticalClass;
	}
	public double getFeaturedValue() {
		return featuredValue;
	}
	public void setFeaturedValue(double featuredValue) {
		this.featuredValue = featuredValue;
	}
	@Override
	public int compareTo(SamplePoint anotherPoint) {
		if(this.featuredValue - anotherPoint.featuredValue > 0){
			return 1;
		}
		if(this.featuredValue - anotherPoint.featuredValue < 0){
			return -1;
		}
		return 0;
	}
	
	public void setValueByFeature(String featureSignature) {
		this.setFeaturedValue(this.features.get(featureSignature).getFeatureValue());
	}
	
	
	/*
	 * Used to read sample points from a folder
	 * 
	 */
	public static ArrayList<SamplePoint> readFolder(String folderPath) throws IOException {
		ArrayList<SamplePoint> points = new ArrayList<SamplePoint>();
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		for (File f: files) {
			SamplePoint point = new SamplePoint(f.getAbsolutePath());
			points.add(point);
		}
		
		return points;
	}
	
	public static void main(String[] args) throws IOException {
		String folderPath = "I:\\Copy\\Data\\2013\\slidingwindows\\m14\\all";
		
		ArrayList<SamplePoint> points = SamplePoint.readFolder(folderPath);
		
		System.out.println("Total points:" + points.size());
	}
}
