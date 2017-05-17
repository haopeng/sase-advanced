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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;



public class DistanceBasedPredictor {



	public String modelFilePath;
	public LinkedList<DistanceBasedFeature> features;
	
	public ArrayList<SamplePoint> points;
	
	public double tp;
	public double tn;
	public double fp;
	public double fn;
	
	public double aveP;
	
	public double lastRecall;
	
	
	public DistanceBasedPredictor(String filePath) throws IOException{
		this.modelFilePath = filePath;
		this.features = new LinkedList<DistanceBasedFeature>();
		this.readFeatures();
	}
	

	
	
	
	public void readFeatures() throws IOException{
		CSVReader reader = new CSVReader(new FileReader(this.modelFilePath));
		String nextLine[] = reader.readNext();
		while((nextLine = reader.readNext()) != null){
			String featureSignature = nextLine[0];
			double abnormalValue = Double.parseDouble(nextLine[1]); 
			double normalValue = Double.parseDouble(nextLine[2]);
			double normalizedAbnormal = Double.parseDouble(nextLine[3]);
			double normalizedNormal = Double.parseDouble(nextLine[4]); 
			double mDistance = Double.parseDouble(nextLine[5]);
			double abnormalMax = Double.parseDouble(nextLine[7]);
			double abnormalMin = Double.parseDouble(nextLine[8]);
			double normalMax = Double.parseDouble(nextLine[9]); 
			double normalMin = Double.parseDouble(nextLine[10]);
			DistanceBasedFeature f = new DistanceBasedFeature(featureSignature, abnormalValue, normalValue, normalizedAbnormal, normalizedNormal, mDistance, abnormalMax, abnormalMin, normalMax, normalMin);
			this.features.add(f);
		}
		Collections.sort(this.features, Collections.reverseOrder());
		reader.close();
	}
	public void printFeaturesByDistanceOrder(){
		int count = 1;
		for(DistanceBasedFeature f: this.features){
			System.out.println(count + "\t" + f.featureSignature + "\t" + f.manhantanDistance);
			count ++;
		}
	}
	
	public boolean predictUsingTopKFeature(int k, SamplePoint point){
		int count = 0;
		int label = (int)point.features.get("Label").getFeatureValue();
		for(int i = 0; i < k; i ++){
			DistanceBasedFeature f = this.features.get(i);
			ComputedFeature feature = point.features.get(f.featureSignature);
			int result = f.predictClass(feature.getFeatureValue(), feature.getMaxValue(), feature.getMinValue());
			if(result == label){
				count ++;
			}
			//System.out.println("Label:" + label + "\t Class:" + result);
		}
		
		if(count >= k - count){
			
			return true;//means predicted correct
		}else{
			return false;
		}
	}
	
	public Precision predictUsingTopKFeaturePrecision(int k, SamplePoint point){
		int count = 0;
		int label = (int)point.features.get("Label").getFeatureValue();
		
		for(int i = 0; i < k; i ++){
			DistanceBasedFeature f = this.features.get(i);
			ComputedFeature feature = point.features.get(f.featureSignature);
			int result = f.predictClass(feature.getFeatureValue(), feature.getMaxValue(), feature.getMinValue());
			if(result == label){
				count ++;
			}
			//System.out.println("Label:" + label + "\t Class:" + result);
		}
		if(label == 0){//abnormal case
			if(count >= k - count){
				
				return Precision.TruePositive;//means predicted correct
			}else{
				return Precision.FalsePositive;
			}
		}else{//normal case
			if(count >= k - count){
				
				return Precision.TrueNegative;//means predicted correct
			}else{
				return Precision.FalseNegative;//predicted incorrect
			}
		}

	}

	public Precision predictUsingSingleFeaturePrecision(int index, SamplePoint point){
		int label = (int)point.features.get("Label").getFeatureValue();
		
			DistanceBasedFeature f = this.features.get(index);
			ComputedFeature feature = point.features.get(f.featureSignature);
			int result = f.predictClass(feature.getFeatureValue(), feature.getMaxValue(), feature.getMinValue());
			if(label == 0){//abnormal case
				if(result == label){
					
					return Precision.TruePositive;//means predicted correct
				}else{
					return Precision.FalsePositive;
				}
			}else{//normal case
				if(result == label){
					
					return Precision.TrueNegative;//means predicted correct
				}else{
					return Precision.FalseNegative;//predicted incorrect
				}
			}

		
		
	}
	
	public boolean predictUsingSingleFeature(int index, SamplePoint point){
		int label = (int)point.features.get("Label").getFeatureValue();
		
			DistanceBasedFeature f = this.features.get(index);
			ComputedFeature feature = point.features.get(f.featureSignature);
			int result = f.predictClass(feature.getFeatureValue(), feature.getMaxValue(), feature.getMinValue());
			if(result == label){
				return true;// correct
			}else{
				return false;
			}
			//System.out.println("Label:" + label + "\t Class:" + result);
		
		
	}
	public void predictUsingSingleFeatureIncrementally(int index, ArrayList<SamplePoint> points){
		this.points = points;
		// set point featured values
		// sort by featured values
		this.setPointFeaturedValuesBySingleFeature(index);
		//compute the first time 
		this.initializeStatistics();
		//incrementally compute
		this.getPrecisionRecallIncrementally();
	}
	
	public void setPointFeaturedValuesBySingleFeature(int index){
		DistanceBasedFeature f = this.features.get(index);
		
		for(SamplePoint point: this.points){
			ComputedFeature feature = point.features.get(f.featureSignature);
			double distance = f.getDistance(feature.getFeatureValue(), feature.getMaxValue(), feature.getMinValue());
			point.setFeaturedValue(distance);
		}
		Collections.sort(this.points);
		
		/*
		for(int i = 0; i < this.points.size(); i ++){
			System.out.println(i + ":"+ f.featureSignature + this.points.get(i).getFeaturedValue());
		}
		*/
	}
	
	public void setPointFeaturedValuesBySingleFeatureOriginalValue(int index){
		DistanceBasedFeature f = this.features.get(index);
		
		for(SamplePoint point: this.points){
			ComputedFeature feature = point.features.get(f.featureSignature);
			double distance = feature.getFeatureValue();
			point.setFeaturedValue(distance);
		}
		Collections.sort(this.points);
		
		/*
		for(int i = 0; i < this.points.size(); i ++){
			System.out.println(i + ":"+ f.featureSignature + this.points.get(i).getFeaturedValue());
		}
		*/
	}
	public void printPrecisionRecall(int lineNum, double featureValue){
		double recall = this.tp/ (this.tp + this.fn) * 100;
		double precision = this.tp/ (this.tp + this.fp) * 100;
		
		int relk = recall - lastRecall > 0? 1: 0;
		double precisionK = relk == 1? precision: 0;
		double fmeasure = 2* (precision * recall)/(precision + recall);
		
		this.lastRecall = recall;
		
		System.out.println(lineNum + "\t" + recall + "\t" + precision + "\t" + featureValue +"\t"+ relk + "\t" + precisionK + "\t" + fmeasure);
	}
	public void getPrecisionRecallIncrementally(){
		//this.printPrecisionRecall(-1, 0);
		this.lastRecall = 0.0;
		System.out.println("TupleNo.\tRecall\tPrecision\tDistance\tRel(k)\tPrecision*Rel(k)\tF-measure");
		for(int i = 0; i < this.points.size(); i ++){
			SamplePoint p = this.points.get(i);
			this.updateStatistice(p, -1.0);
			p.setPredictedLabel(PointLabel.Abnormal);
			this.updateStatistice(p, 1.0);
			this.printPrecisionRecall(i, p.getFeaturedValue());
		}
	}
	public void getAvePrecisionUsingSingleFeatureIncrementally(int index, ArrayList<SamplePoint> points){
		//by original feature values
		this.points = points;
		// set point featured values
		// sort by featured values
		String featureName = this.features.get(index).featureSignature;
		//decreasing
		this.setPointFeaturedValuesBySingleFeature(index, true);
		//compute the first time 
		this.initializeStatistics();
		//incrementally compute
		this.getAvePrecisionIncrementally();
		double avp = this.aveP / (this.tp + fn);
		
		System.out.println(index + "\t" + featureName + "\t"  + avp + "\t" +"Increasing");
		
		
		//increasing
		//decreasing
		this.setPointFeaturedValuesBySingleFeature(index, false);
		//compute the first time 
		this.initializeStatistics();
		//incrementally compute
		this.getAvePrecisionIncrementally();
		avp = this.aveP / (this.tp + fn);
		System.out.println(index + "\t" + featureName + "\t" + avp + "\t" + "Decreasing");

	}
	//this.predictor.predictUsingSingleFeatureByValueIncrementally(153, points, increasingOrder);
	public void predictUsingSingleFeatureByValueIncrementally(int index, ArrayList<SamplePoint> points, boolean increasingOrder){
		//by original feature values
		this.points = points;
		// set point featured values
		// sort by featured values
		String featureName = this.features.get(index).featureSignature;
		//decreasing
		this.setPointFeaturedValuesBySingleFeature(index, increasingOrder);
		//compute the first time 
		this.initializeStatistics();
		//incrementally predict
		this.getPrecisionRecallIncrementally();//todo
		
		
		//double avp = this.aveP / (this.tp + fn);
		
		//System.out.println(index + "\t" + featureName + "\t"  + avp + "\t" +"Increasing");
	}
	
	
	
	public void setPointFeaturedValuesBySingleFeature(int index, boolean increasing){
		DistanceBasedFeature f = this.features.get(index);
		
		for(SamplePoint point: this.points){
			ComputedFeature feature = point.features.get(f.featureSignature);
			double value = feature.getFeatureValue();
			point.setFeaturedValue(value);
			
		}
		if(increasing){
			Collections.sort(this.points);

		}else{
			Collections.sort(this.points,Collections.reverseOrder());

		}
		
		/*
		for(int i = 0; i < this.points.size(); i ++){
			System.out.println(i + ":"+ f.featureSignature + this.points.get(i).getFeaturedValue());
		}
		*/
	}
	
	public void getAvePrecisionIncrementally(){
		//this.printPrecisionRecall(-1, 0);
		for(int i = 0; i < this.points.size(); i ++){
			SamplePoint p = this.points.get(i);
			this.updateStatistice(p, -1.0);
			p.setPredictedLabel(PointLabel.Abnormal);
			this.updateStatistice(p, 1.0);
			//this.printPrecisionRecall(i, p.getFeaturedValue());
		}
	}
	public void initializeStatistics(){
		this.tp = 0.0;
		this.tn = 0.0;
		this.fp = 0.0;
		this.fn = 0.0;
		this.aveP = 0.0;
		for(SamplePoint point: this.points){
			//set all to normal
			point.setPredictedLabel(PointLabel.Normal);
			this.updateStatistice(point, 1.0);
		}
		
	}
	
	public void updateStatistice(SamplePoint point, double change){
		switch(point.statisticalClass){
			case TruePositive:
				this.tp += change;
				double precision = this.tp/ (this.tp + this.fp) * 100;
				this.aveP += precision;
				break;
			case TrueNegative:
				this.tn += change;
				break;
			case FalsePositive:
				this.fp += change;
				break;
			case FalseNegative:
				this.fn += change;
				break;
		}
	}
	
	public void computeValueDistribution(ArrayList<SamplePoint> points, String outputFile, int topN) throws IOException{
		CSVWriter writer = new CSVWriter(new FileWriter(outputFile), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String[] line = new String[2];
		line[0] = "Feature";
		line[1] = "Value";
		
		writer.writeNext(line);
		for(int i = 0; i < topN; i ++){
			//iterative on features
			DistanceBasedFeature f = this.features.get(i);
			for(SamplePoint p: points){
				//output the value;
				ComputedFeature feature = p.features.get(f.featureSignature);
				line[0] = i + "";
				line[1] = feature.getFeatureValue() + "";
				writer.writeNext(line);
			}
		}
		writer.close();
	}
	
	/*
	 * 
	 * DistanceBasedFeature f = this.features.get(index);
		
		for(SamplePoint point: this.points){
			ComputedFeature feature = point.features.get(f.featureSignature);
			double distance = f.getDistance(feature.getFeatureValue(), feature.getMaxValue(), feature.getMinValue());
			point.setFeaturedValue(distance);
		}
	 */
	
	
	public void computeDistanceDistribution(ArrayList<SamplePoint> points, String outputFile, int topN) throws IOException{
		CSVWriter writer = new CSVWriter(new FileWriter(outputFile), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String[] line = new String[2];
		line[0] = "Feature";
		line[1] = "Distance";
		writer.writeNext(line);
		for(int i = 0; i < topN; i ++){
			//iterative on features
			DistanceBasedFeature f = this.features.get(i);
			for(SamplePoint p: points){
				//compute the value
				ComputedFeature feature = p.features.get(f.featureSignature);
				
				double normalizeValue;
				double range = feature.getMaxValue() - feature.getMinValue();
				if(range == 0){
					normalizeValue = 1;
				}else{
					normalizeValue = feature.getFeatureValue() / range;
				}
				
				
				//double distance = f.getDistance(feature.getFeatureValue(), feature.getMaxValue(), feature.getMinValue());

				//output the value;
				line[0] = i + "";
				//line[1] = distance + "";
				line[1] = normalizeValue + "";
				writer.writeNext(line);
			}
		}
		writer.close();
	}
	public void computeTupleDistribution(ArrayList<SamplePoint> points, String outputFolder, int topN) throws IOException{
		CSVWriter abnormalWriter = new CSVWriter(new FileWriter(outputFolder + "abnormal-top"+topN+".csv"), ',',CSVWriter.NO_QUOTE_CHARACTER);
		CSVWriter normalWriter = new CSVWriter(new FileWriter(outputFolder + "normal-top"+topN+".csv"), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String[] line = new String[2];
		line[0] = "Feature";
		line[1] = "TuplePosition";//the position in the feature-based sorted tuples
		abnormalWriter.writeNext(line);
		normalWriter.writeNext(line);
		this.points = points;
		for(int i = 0; i < topN; i ++){
			//iterative on features
			//shuffle
			Collections.shuffle(this.points, new Random(System.nanoTime()));
			//sort the attributes based on the feature
			
			this.setPointFeaturedValuesBySingleFeatureOriginalValue(i);
			System.out.println(this.features.get(i).featureSignature);
			/*
			if(i == 4){
				for(int j = 0; j < this.points.size(); j ++){
					System.out.println(j + "\t" + this.points.get(j).getFeaturedValue() +"\t" + this.points.get(j).trueLabel);
				}
			}
			*/
			//output the specified label
			for(int j = 0; j < this.points.size(); j ++){
				line[0] = i + "";// the feature
				line[1] = j + "";// the tuple position
				if(this.points.get(j).trueLabel == PointLabel.Abnormal){
					abnormalWriter.writeNext(line);
				}else{
					normalWriter.writeNext(line);
				}
			}
			
		}
		abnormalWriter.close();
		normalWriter.close();
	}

	
	public String getModelFilePath() {
		return modelFilePath;
	}

	public void setModelFilePath(String modelFilePath) {
		this.modelFilePath = modelFilePath;
	}

	public LinkedList<DistanceBasedFeature> getFeatures() {
		return features;
	}

	public void setFeatures(LinkedList<DistanceBasedFeature> features) {
		this.features = features;
	}
	
	
}