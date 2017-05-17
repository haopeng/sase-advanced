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
package edu.umass.cs.sase.usecase.hadooplog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ResultSampler {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputFile  = "F:\\Dropbox\\research\\2nd\\paper\\vldb13-sase\\writing\\figures\\hadoop\\q4\\data-twitter.txt";
		String outputFile = "F:\\Dropbox\\research\\2nd\\paper\\vldb13-sase\\writing\\figures\\hadoop\\q4\\data-twitter-sample.txt";
		//int sampleRate = 500; // every n tuples take 1
		int sampleRate = 50; // every n tuples take 1
		
		inputFile  = "F:\\Dropbox\\research\\2nd\\paper\\vldb13-sase\\writing\\figures\\hadoop\\q4\\data-twitter.txt";
		outputFile = "F:\\Dropbox\\research\\2nd\\paper\\vldb13-sase\\writing\\figures\\hadoop\\q4\\data-twitter-sample.txt";
		
		inputFile  = "F:\\Dropbox\\research\\2nd\\paper\\vldb13-sase\\writing\\figures\\hadoop\\q4\\data-worldcupuser.txt";
		outputFile = "F:\\Dropbox\\research\\2nd\\paper\\vldb13-sase\\writing\\figures\\hadoop\\q4\\data-worldcupuser-sample.txt";
		
		inputFile  = "g:\\Dropbox\\research\\3rd\\Writing\\figures\\hadoopjobs\\q4\\m9.txt";
		outputFile = "g:\\Dropbox\\research\\3rd\\Writing\\figures\\hadoopjobs\\q4\\m9-sample.txt";
		
		inputFile  = "g:\\Dropbox\\research\\3rd\\Writing\\figures\\hadoopjobs\\q4\\m10.txt";
		outputFile = "g:\\Dropbox\\research\\3rd\\Writing\\figures\\hadoopjobs\\q4\\m10-sample.txt";
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		String line = br.readLine();
		int counter = 0;
		while(line != null){
			if(counter % sampleRate == 0){
				bw.write(line + "\n");
				
			}
			
			line = br.readLine();
			counter ++;
		}
		br.close();
		bw.flush();
		bw.flush();
	}

}
