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
package edu.umass.cs.sase.explanation.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVReader;


/**
 * This class is used to calculate the arrival rate of each event type
 * @author haopeng
 *
 */
public class StreamRateCalculator {
	String inputFolder;
	HashMap<String, Double> hadoopRates;
	HashMap<String, Double> gangliaRates;
	
	double hadoopSum;
	double hadoopAvg;
	
	double gangliaSum;
	double gangliaAvg;
	
	double totalSum;
	double totalAvg;
	public StreamRateCalculator(String inputFolder) {
		this.inputFolder = inputFolder;
		this.hadoopRates = new HashMap<String, Double>();
		this.gangliaRates = new HashMap<String, Double>();
	}
	
	public void calculate() throws IOException {
		File folder = new File(this.inputFolder);
		File[] files = folder.listFiles();
		for (File f: files) {
			this.countOneFile(f);
		}
		
		//summarize
		this.gangliaSum = this.getSum(this.gangliaRates);
		this.gangliaAvg = this.gangliaSum / (double) this.gangliaRates.size();
		
		this.hadoopSum = this.getSum(this.hadoopRates);
		this.hadoopAvg = this.hadoopSum / (double) this.hadoopRates.size();
		
		this.totalSum = this.gangliaSum + this.hadoopSum;
		this.totalAvg = (this.totalSum) / (double) (this.hadoopRates.size() + this.gangliaRates.size());
		
		//print details
		System.out.println("Ganglia Rates============");
		for (String t: this.gangliaRates.keySet()) {
			System.out.println(t + "\t" + this.gangliaRates.get(t));
		}
		
		System.out.println("Hadoop Rates============");
		for (String t: this.hadoopRates.keySet()) {
			System.out.println(t + "\t" + this.hadoopRates.get(t));
		}
		
		//print summary
		System.out.println("Summary Rates============");
		System.out.println("Name\tRate");
		System.out.println("GangliaSum\t" + this.gangliaSum);
		System.out.println("GangliaAVG\t" + this.gangliaAvg);
		System.out.println("HadoopSum\t" + this.hadoopSum);
		System.out.println("HadoopAVG\t" + this.hadoopAvg);
		System.out.println("TotalSum\t" + this.totalSum);
		System.out.println("totalAVG\t" + this.totalAvg);
		
	}
	public double getSum(HashMap<String, Double> rates) {
		double sum = 0.0;
		for (Double d: rates.values()) {
			sum += d;
		}
		return sum;
	}
	public void countOneFile(File f) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(f.getAbsolutePath()));
		reader.readNext();//header
		String[] line = reader.readNext();//first line
		String eventType = line[1];
		
		boolean isHadoop = true;
		if (!line[0].equals("h")) {
			isHadoop = false;
		}
		
		if (isHadoop) {
			long startTimestamp = Long.parseLong(line[7]);
			int count = 1;
			long endTimestamp = 0;
			while ((line = reader.readNext()) != null) {
				count ++;
				endTimestamp = Long.parseLong(line[7]);
			}
			double rate = (double)count * 1000.0 / (double)(endTimestamp - startTimestamp);
			this.hadoopRates.put(eventType, rate);

		} else {
			long startTimestamp = Long.parseLong(line[4]);
			int count = 1;
			long endTimestamp = 0;
			while ((line = reader.readNext()) != null) {
				count ++;
				endTimestamp = Long.parseLong(line[4]);
			}
			double rate = (double)count * 1000.0 / (double)(endTimestamp - startTimestamp);
			this.gangliaRates.put(eventType, rate);
	
		}
		reader.close();
	}
	
	public static void main(String[] args) throws IOException {
		String input = "/Users/haopeng/Copy/Data/2015/singleTypes-all";
		StreamRateCalculator c = new StreamRateCalculator(input);
		c.calculate();
	}
}
