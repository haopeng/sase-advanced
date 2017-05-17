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
package edu.umass.cs.sase.explanation.visualization;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


/**
 * Input: csv file of data io files
 * Output: visualized 
 * @author haopeng
 *
 */
public class HadoopJobVisualization {
	String inputFilePath;
	String outputFilePath;
	double sampleRate;// 0 ~ 1; 1 means take all
	public HadoopJobVisualization(String input, String output, double sampleRate) {
		this.inputFilePath = input;
		this.outputFilePath = output;
		this.sampleRate = sampleRate;
	}
	
	public void readData() throws IOException {
		CSVReader reader = new CSVReader(new FileReader(this.inputFilePath));
		CSVWriter writer = new CSVWriter(new FileWriter(this.outputFilePath + "-" + this.sampleRate + ".txt"), '\t',CSVWriter.NO_QUOTE_CHARACTER);
		String[] line = reader.readNext();
		
		int readCount = 0;
		int writeCount = 0;
		
		String[] writeLine = new String[2];
		Random r = new Random(System.currentTimeMillis());
		while((line = reader.readNext()) != null) {
			readCount ++;
			//System.out.println(line[12] + "\t" + line[13]);
			if (r.nextDouble() <= this.sampleRate) {
				writeCount ++;
				writeLine[0] = line[12];
				writeLine[1] = line[13];
				writer.writeNext(writeLine);
			}
		}
		reader.close();
		writer.close();
		
		System.out.println("SampleRate = \t" + this.sampleRate + "\tRead count = \t" + readCount + "\tWrite count = \t" + writeCount);
	}
	
	
	public static void main(String[] args) throws IOException {
		
		//String inputFile = "/Users/haopeng/Dropbox/research/3rd/experiment/b3-2/b3-2.csv";
		//String outputFile = "/Users/haopeng/Dropbox/research/3rd/experiment/b3-2/b3-2-sample";
		String inputFile = "/Users/haopeng/Dropbox/research/3rd/experiment/b0-1/b0-1.csv";
		String outputFile = "/Users/haopeng/Dropbox/research/3rd/svn/vldb16-sase/figures/high-memory-wcfrequent/b0-1/b0-1-sample";
		double sampleRate = 0.1;
		
		HadoopJobVisualization jv = new HadoopJobVisualization(inputFile, outputFile, sampleRate);
		jv.readData();
		
		/*
		for (sampleRate = 0.025; sampleRate <= 1; sampleRate += 0.1) {
			HadoopJobVisualization jv = new HadoopJobVisualization(inputFile, outputFile, sampleRate);
			jv.readData();
		}
		*/

	}

}
