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

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVWriter;

public class FeatureNormalizationComputer {
	String abnormalFolderPath;
	String normalFolderPath;
	
	long abnormalStartTime;
	long abnormalEndTime;
	
	long normalStartTime;
	long normalEndTime;
	
	
	HashMap<String, ComputedFeature> abnormalFeatures;
	HashMap<String, ComputedFeature> normalFeatures;
	HashMap<String, ComputedFeature> abnormalFeaturesNormalized;
	HashMap<String, ComputedFeature> normalFeaturesNormalized;
	
	public FeatureNormalizationComputer(String abnormalFolderPath, String normalFolderPath, long abnormalStartTime, long abnormalEndTime, long normalStartTime, long normalEndTime){
		this.abnormalFolderPath = abnormalFolderPath;
		this.normalFolderPath = normalFolderPath;
		
		this.abnormalStartTime = abnormalStartTime;
		this.abnormalEndTime = abnormalEndTime;
		
		this.normalStartTime = normalStartTime;
		this.normalEndTime = normalEndTime;
	}
	public void computeFeatures() throws IOException{
		FolderFeatureComputer abnormalFolderComputer = new FolderFeatureComputer(this.abnormalFolderPath, this.abnormalStartTime, this.abnormalEndTime);
		abnormalFolderComputer.computeFeatures();
		this.abnormalFeatures = abnormalFolderComputer.getFeatureIndex();
		
		FolderFeatureComputer normalFolderComputer = new FolderFeatureComputer(this.normalFolderPath, this.normalStartTime, this.normalEndTime);
		normalFolderComputer.computeFeatures();
		this.normalFeatures = normalFolderComputer.getFeatureIndex();
	}
	public void normalizeFeatures(){
		this.abnormalFeaturesNormalized = new HashMap<String, ComputedFeature>();
		this.normalFeaturesNormalized = new HashMap<String, ComputedFeature>();
		for(String key: this.abnormalFeatures.keySet()){
			/*
			if(key.equalsIgnoreCase("RequestStart.csv-jobId-avg")){//RequestFinish.csv-jobId-avg
				System.out.println();
			}*/
			if(key.equalsIgnoreCase("RequestFinish.csv-jobId-avg")){//RequestFinish.csv-jobId-avg
				System.out.println();
			}
			ComputedFeature abnormalFeature = this.abnormalFeatures.get(key);
			ComputedFeature normalFeature = this.normalFeatures.get(key); 
			if(abnormalFeature != null && normalFeature != null){
				double abnormalValue = abnormalFeature.getFeatureValue();
				double normalValue = normalFeature.getFeatureValue();
				double abnormalValueNormalized = 0.0;
				double normalValueNormalized = 0.0;
				if(abnormalFeature.computation.equalsIgnoreCase("frequency")){
					double maxValue = Math.max(abnormalValue, normalValue);
					if (maxValue == 0.0){
						maxValue = 1;
					}
					abnormalValueNormalized = abnormalValue / maxValue;
					normalValueNormalized = normalValue / maxValue;
				}else{
					
					double minValue = Math.min(abnormalFeature.getMinValue(), normalFeature.getMinValue());
					double maxValue = Math.max(abnormalFeature.getMaxValue(), normalFeature.getMaxValue());
					double range = maxValue - minValue;
					if(range == 0){
						range = 1.0;
					}
					abnormalValueNormalized = (abnormalValue - minValue) / range;
					normalValueNormalized = (normalValue - minValue) / range;
				}
				ComputedFeature abnormalFeatureNormalized = abnormalFeature.clone();
				abnormalFeatureNormalized.setFeatureValue(abnormalValueNormalized);
				ComputedFeature normalFeatureNormalized = normalFeature.clone();
				normalFeatureNormalized.setFeatureValue(normalValueNormalized);
				this.abnormalFeaturesNormalized.put(key, abnormalFeatureNormalized);
				this.normalFeaturesNormalized.put(key, normalFeatureNormalized);

			}
			
			
		}
	}
	public void printNormalizedFeatures(String outputFilePath) throws IOException{
		CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath));
		String[] nextLine = new String[11];
		nextLine[0] = "FeatureName";
		nextLine[1] = "AbnormalValue";
		nextLine[2] = "NormalValue";
		nextLine[3] = "NormalizedAbnormalValue";
		nextLine[4] = "NormalizedNormalizedValue";
		nextLine[5] = "ManhattanDistance";
		nextLine[6] = "EuclideanDistance";
		nextLine[7] = "AbnormalMaxValue";
		nextLine[8] = "AbnormalMinValue";
		nextLine[9] = "NormalMaxValue";
		nextLine[10] = "NormalMinValue";

		writer.writeNext(nextLine);
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Name\tAbnormalValue\tNormalValue");
		for(String key: this.abnormalFeaturesNormalized.keySet()){
			ComputedFeature abnormalFeature = this.abnormalFeaturesNormalized.get(key);
			ComputedFeature normalFeature = this.normalFeaturesNormalized.get(key);
			double abnormalValue = abnormalFeature.getFeatureValue();
			double normalValue = normalFeature.getFeatureValue();
			System.out.println(key + "\t " + abnormalValue + "\t " + normalValue);
			
			nextLine[0] = key;
			nextLine[1] = this.abnormalFeatures.get(key).getFeatureValue() + "";
			nextLine[2] = this.normalFeatures.get(key).getFeatureValue() + "";
			nextLine[3] = abnormalValue + "";
			nextLine[4] = normalValue + "";
			double diff = Math.abs(abnormalValue - normalValue) ;
			nextLine[5] = diff + "";
			nextLine[6] = diff * diff + "";
			
			
			nextLine[7] = this.abnormalFeatures.get(key).getMaxValue() + "";
			nextLine[8] = this.abnormalFeatures.get(key).getMinValue() + "";
			nextLine[9] = this.normalFeatures.get(key).getMaxValue() + "";
			nextLine[10] = this.normalFeatures.get(key).getMinValue() + "";
			writer.writeNext(nextLine);
		}
		writer.flush();
		writer.close();
	}
	public static void main(String args[]) throws IOException{
		//test the nromalization
		String abnormalFolderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\abnormal";
		String normalFolderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\normal";
		String normalizedFeatureOutput = "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\normalizedFeatures3-morevalues.csv";

		long abnormalStart = 1373637583030L; 
		long abnormalEnd =   1373639906229L;
		
		
		long normalStart= 1373639906229L;
		long normalEnd =  1373642072543L ;
		
		
		FeatureNormalizationComputer c = new FeatureNormalizationComputer(abnormalFolderPath, normalFolderPath, abnormalStart, abnormalEnd, normalStart, normalEnd);
		c.computeFeatures();
		c.normalizeFeatures();
		
		c.printNormalizedFeatures(normalizedFeatureOutput);
		
	}
}
