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

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class SlidingWindowFeatureGenerator {
	int numberOfAttributesToIgnore;
	long windowSize;// milliseconds, 1/1000 second
	SlidingWindowCalculator[] calculators;
	CSVReader reader;
	CSVWriter writer;
	String[] attributes;
	int lineCount;
	
	public SlidingWindowFeatureGenerator(int attributesToIgnore, String inputFilePath, String outputFilePath, long windowSize) throws IOException{
		this.lineCount = 0;
		this.numberOfAttributesToIgnore = attributesToIgnore;
		this.windowSize = windowSize;
		this.reader = new CSVReader(new FileReader(inputFilePath));
		this.writer = new CSVWriter(new FileWriter(outputFilePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String[] nextLine = this.reader.readNext();// attributes
		int numberOfAttributes = attributesToIgnore + (nextLine.length - attributesToIgnore) * 6;
		this.attributes = new String[numberOfAttributes];
		for(int i = 0; i < nextLine.length; i ++){
			this.attributes[i] = nextLine[i];
		}
		for(int i = this.numberOfAttributesToIgnore; i < nextLine.length; i ++){
			int index = nextLine.length + (i - this.numberOfAttributesToIgnore) * 5;
			String valueNamePrefix = nextLine[i];
			this.attributes[index] = valueNamePrefix + "_Count";
			this.attributes[index+1] = valueNamePrefix + "_Sum";
			this.attributes[index+2] = valueNamePrefix + "_Avg";
			this.attributes[index+3] = valueNamePrefix + "_Max";
			this.attributes[index+4] = valueNamePrefix + "_Min";
		}
		this.writer.writeNext(this.attributes);
		this.writer.flush();
		
		//initialize the caculators
		this.calculators = new SlidingWindowCalculator[nextLine.length - this.numberOfAttributesToIgnore];
		for(int i = 0; i < this.calculators.length;i ++){
			this.calculators[i] = new SlidingWindowCalculator(nextLine[this.numberOfAttributesToIgnore + i], this.windowSize);
		}
		
	}
	public void readAndCompute() throws IOException{
		ArrayList<String[]> tuplesForCurrentTimestamp = new ArrayList<String[]>();
		String currentTimestamp;
		String nextLine[] = this.reader.readNext();
		boolean finish = false;
		while(! finish){
			tuplesForCurrentTimestamp = new ArrayList<String[]>();
			currentTimestamp = nextLine[7];
			tuplesForCurrentTimestamp.add(nextLine);
			while(((nextLine = this.reader.readNext()) != null) && nextLine[7].equalsIgnoreCase(currentTimestamp)){
				tuplesForCurrentTimestamp.add(nextLine);
			}
			this.processValues(tuplesForCurrentTimestamp);
			if(nextLine == null){
				finish = true;
			}
		}
		this.reader.close();
		this.writer.flush();
		this.writer.close();
		
	}
	public void processValues(ArrayList<String[]> tuples) throws IOException{
		//feed into calculators
		for(String[] values: tuples){
			for(int i = this.numberOfAttributesToIgnore; i < values.length; i ++){
				AttributeValue v = new AttributeValue(values[7], values[i]);
				this.calculators[i - this.numberOfAttributesToIgnore].addValue(v);
			}
		}
		//output
		for(String[] values: tuples){
			String[] valuesToOutput = new String[this.attributes.length];
			for(int i = 0; i < values.length; i ++){
				valuesToOutput[i] = values[i];
			}
			for(int i = this.numberOfAttributesToIgnore; i < values.length; i ++){
				int index = values.length + (i - this.numberOfAttributesToIgnore) * 5;
				valuesToOutput[index] = this.calculators[i - this.numberOfAttributesToIgnore].getCount() + "";
				valuesToOutput[index+1] = this.calculators[i - this.numberOfAttributesToIgnore].getSum() + "";
				valuesToOutput[index+2] = this.calculators[i - this.numberOfAttributesToIgnore].getAverage() + "";
				valuesToOutput[index+3] = this.calculators[i - this.numberOfAttributesToIgnore].getMax() + "";
				valuesToOutput[index+4] = this.calculators[i - this.numberOfAttributesToIgnore].getMin() + "";
			}
			this.writer.writeNext(valuesToOutput);
			System.out.println("Output Line:" + this.lineCount);
			lineCount ++;
			this.writer.flush();
		}
	}
	
	public static void main(String args[]) throws IOException{
		int attributesToIgnore = 12;// first 12 attributes will be ingored for sliding window values;
		long timewindow = 60000L;//1min
		String inputFile = "I:\\Copy\\Data\\2013\\merged\\JobStart-numeric.csv";
		String outputFile = "I:\\Copy\\Data\\2013\\merged\\JobStart-numeric-sliding.csv";
		
		SlidingWindowFeatureGenerator generator = new SlidingWindowFeatureGenerator(attributesToIgnore, inputFile, outputFile, timewindow);
		generator.readAndCompute();
		
		
	}
}
