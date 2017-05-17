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
package edu.umass.cs.sase.cluster;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CorrelationClusterFromScratch {
	String inputSampleData;
	List<String[]> allData;
	List<FeatureDataSet> allDataSets;
	HashMap<String, Double> correlation;
	
	public CorrelationClusterFromScratch(String inputFile) throws IOException {
		this.inputSampleData = inputFile;
		
		this.allDataSets = new LinkedList<FeatureDataSet>();
		this.correlation = new HashMap<String, Double>();
		
		this.readData();
		this.precompute();
		
		/*
		for (int i = 0; i < 6; i ++) {
			this.allDataSets.get(i).printInfo();
		}
		*/
	}
	
	public void readData() throws IOException {
		CSVReader reader = new CSVReader(new FileReader(this.inputSampleData));
		this.allData = reader.readAll();
		reader.close();
		
		/*
		int count = 0;
		for (int i = 0; i < 10; i ++) {
			String[] strs = allData.get(i);
			for (String str : strs) {
				System.out.print(str + "\t");
			}
			System.out.println();
		}
		*/
	}
	
	public void precompute() {
		int count = this.allData.get(0).length - 1;
		for (int i = 0; i < count; i ++) {
			//System.out.println("feature number:" + i);
			String featureName = this.allData.get(1)[i + 1]; 
			List<Double> values = new LinkedList<Double>();
			for (int j = 2; j < this.allData.size(); j ++) {
				//read each value
				//System.out.println("Value:" + j);
				try{
					values.add(Double.parseDouble(this.allData.get(j)[i + 1]));
				} catch(NumberFormatException e) {
					values.add(0.0);
				}
			}
			FeatureDataSet f = new FeatureDataSet(i, featureName, values);
			this.allDataSets.add(f);
			f.computeMean();
			
		}
		
	}
	
	
	
	public double getCorrelation(int id1, int id2) {
		String hashKey = id1 > id2? id2 + "-" + id1 : id1 + "-" + id2;
		
		if(!correlation.containsKey(hashKey)) {
			double value = this.computeCorrelation(id1, id2);
			this.correlation.put(hashKey, value);
		}
		
		return correlation.get(hashKey);
		
	}
	
	public double computeCorrelation(int id1, int id2) {
		//compute the correlations
		FeatureDataSet f1 = this.allDataSets.get(id1);
		FeatureDataSet f2 = this.allDataSets.get(id2);
		
		double numerator = 0;
		for (int i = 0; i < f1.getValues().size(); i ++) {
			double xi = f1.getValues().get(i);
			double yi = f2.getValues().get(i);
			numerator += (xi - f1.getMean()) * (yi - f2.getMean());
		}
		
		double denominator = f1.meanSquareSumRoot * f2.meanSquareSumRoot;
		
		return numerator / denominator;
	}
	
	
	public String getInputSampleData() {
		return inputSampleData;
	}

	public void setInputSampleData(String inputSampleData) {
		this.inputSampleData = inputSampleData;
	}

	public List<String[]> getAllData() {
		return allData;
	}

	public void setAllData(List<String[]> allData) {
		this.allData = allData;
	}

	public List<FeatureDataSet> getAllDataSets() {
		return allDataSets;
	}

	public void setAllDataSets(List<FeatureDataSet> allDataSets) {
		this.allDataSets = allDataSets;
	}

	public HashMap<String, Double> getCorrelation() {
		return correlation;
	}

	public void setCorrelation(HashMap<String, Double> correlation) {
		this.correlation = correlation;
	}

	public static void main(String[] args) throws IOException {
		String dataFile = "/Users/haopeng/Dropbox/research/3rd/data/allPoints.csv";
		CorrelationClusterFromScratch correlation = new CorrelationClusterFromScratch(dataFile);
		
		//System.out.println(correlation.getCorrelation(0, 2));
		//System.out.println(correlation.getCorrelation(1, 3));
		//System.out.println(correlation.getCorrelation(4, 5));
		
		System.out.println(correlation.getCorrelation(1, 4));
		
		
	}

}
