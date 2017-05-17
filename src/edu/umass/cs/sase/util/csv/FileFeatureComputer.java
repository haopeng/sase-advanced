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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import au.com.bytecode.opencsv.CSVReader;

public class FileFeatureComputer {
	long startTime;
	long endTime;
	int count;
	int[] gangliaFeatures = {3, 8};
	int[] hadoopFeatures = {3, 4, 5, 6, 11};
	String[] featureNames;
	boolean isGanglia;
	double sum[];
	double max[];
	double min[];
	String file;
	String fileName;
	ArrayList<ComputedFeature> features;
	public FileFeatureComputer(String file, long startTime, long endTime){
		this.file = file;
		File f = new File(file);
		this.fileName = f.getName();
		this.features = new ArrayList<ComputedFeature>();
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public void compute() throws IOException{
		CSVReader reader = new CSVReader(new FileReader(this.file));
		String nextLine[] = reader.readNext();
		this.sum = new double[nextLine.length];
		this.max = new double[nextLine.length];
		this.min = new double[nextLine.length];
		for(int i = 0; i < nextLine.length; i ++){
			max[i] = Double.MIN_VALUE;
			min[i] = Double.MAX_VALUE;
		}
		this.featureNames = nextLine;
		this.count = 0;
		boolean exceedingWindow = false;
		while((nextLine = reader.readNext()) != null && !exceedingWindow){
			count ++;
			double currentTime = 0L;
			if(nextLine[0].startsWith("h")){
				currentTime = Long.parseLong(nextLine[7]);//the timestamp
			}else{
				currentTime = Long.parseLong(nextLine[4]);
			}
			boolean skip = currentTime < this.startTime;
			exceedingWindow = currentTime > this.endTime;
			if(!skip && !exceedingWindow){
				if(nextLine[0].startsWith("h")){
					//hadoop logs
					this.isGanglia = false;
					
					for(int i: this.hadoopFeatures){
						double value = Double.parseDouble(nextLine[i]);
						sum[i] += value;
						max[i] = Math.max(max[i], value);
						min[i] = Math.min(min[i], value);
					}
				}else{
					//ganglia logs
					this.isGanglia = true;
					
					for(int i: this.gangliaFeatures){
						double value = Double.parseDouble(nextLine[i]);
						sum[i] += value;
						max[i] = Math.max(max[i], value);
						min[i] = Math.min(min[i], value);
					}

				}

			}
		}
		reader.close();
		
		//print
		System.out.println("File:" + file);
		System.out.println("Count:" + this.count);
		//	public ComputedFeature(String filePath, String eventType, String attributeName, String computation, String featureSignature, double featureValue){
		File representingFile = new File(this.file);
		double frequency = (double)this.count * 1000 / (this.endTime - this.startTime);//per second
		ComputedFeature frequencyFeature = new ComputedFeature(file, representingFile.getName(), "", "frequency", frequency);
		this.features.add(frequencyFeature);
		
		//temporaily disable other features
		
		if(this.count == 0) {
			Arrays.fill(this.max, 0);
			Arrays.fill(this.sum, 0);
			Arrays.fill(this.min, 0);

		}
		if(this.featureNames.length == 12) {
			this.isGanglia = false;
		} else {
			this.isGanglia = true;
		}
		//if(this.count > 0){
		if(true){
			if(isGanglia){
				for(int i: this.gangliaFeatures){
					
					try{
						System.out.println(this.featureNames[i] + ":" + this.sum[i] / this.count);
					} catch(ArrayIndexOutOfBoundsException e){
						System.out.print(0);
					}
					
					ComputedFeature f = new ComputedFeature(this.file, representingFile.getName(), this.featureNames[i], "avg", this.sum[i]/(double) this.count);
					f.maxValue = this.max[i];
					f.minValue = this.min[i];
					this.features.add(f);
				}
			}else{
				for(int i: this.hadoopFeatures){
					System.out.println(this.featureNames[i] + ":" + this.sum[i] / this.count);
					ComputedFeature f = new ComputedFeature(this.file, representingFile.getName(), this.featureNames[i], "avg", this.sum[i]/(double) this.count);
					f.maxValue = this.max[i];
					f.minValue = this.min[i];
					this.features.add(f);
				}
			}

		}
		
		
	}
	public void printFeatures(){
		for(int i = 0; i < this.features.size(); i ++){
			ComputedFeature f = this.features.get(i);
			if(i == 0){
				System.out.println("~~~~~~~~~~~~~~~");
				System.out.println(f.getSchema());
			}
			System.out.println(f.toString());
		}
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int[] getGangliaFeatures() {
		return gangliaFeatures;
	}
	public void setGangliaFeatures(int[] gangliaFeatures) {
		this.gangliaFeatures = gangliaFeatures;
	}
	public int[] getHadoopFeatures() {
		return hadoopFeatures;
	}
	public void setHadoopFeatures(int[] hadoopFeatures) {
		this.hadoopFeatures = hadoopFeatures;
	}
	public String[] getFeatureNames() {
		return featureNames;
	}
	public void setFeatureNames(String[] featureNames) {
		this.featureNames = featureNames;
	}
	public boolean isGanglia() {
		return isGanglia;
	}
	public void setGanglia(boolean isGanglia) {
		this.isGanglia = isGanglia;
	}
	public double[] getSum() {
		return sum;
	}
	public void setSum(double[] sum) {
		this.sum = sum;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public ArrayList<ComputedFeature> getFeatures() {
		return features;
	}
	public void setFeatures(ArrayList<ComputedFeature> features) {
		this.features = features;
	}
	public static void main(String[] args) throws IOException{
		String inputFilePath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\normal\\ReduceStart.csv";
		long start = 1373639906229L;
		long end = 1373642072543L;
		FileFeatureComputer c = new FileFeatureComputer(inputFilePath, start, end);
		c.compute();
		c.printFeatures();
	}
}
