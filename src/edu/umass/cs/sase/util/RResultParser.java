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

package edu.umass.cs.sase.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class RResultParser {
	
	public void parseFile(String input, String output) throws IOException{
		System.out.println("Input: " + input);
		System.out.println("Output: " + output);
		
		BufferedReader reader = new BufferedReader(new FileReader(input));
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		
		String line;
		
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("[1] \"on")) {
				writer.write(line + "\n");
				continue;
			}
			
			String firstLine = line;
			String secondLine = reader.readLine();
			String thirdLine = reader.readLine();
			String fourLine = reader.readLine();
			String fifthLine = reader.readLine();
			
			//process the two lines
			this.computeMeasures(firstLine, secondLine, thirdLine, fourLine, fifthLine, writer);
			
		}
		
		reader.close();
		writer.close();
	}
	
	public void computeMeasures(String firstLine, String secondLine, String thirdLine, String fourthLine, String fifthLine, BufferedWriter writer) throws IOException{
		//System.out.println(firstLine);
		/* example
		[1] "on m12"
		          y12
		           Abnormal Normal
		  Abnormal       78      0
		  Normal          0     73
		*/
		writer.write(firstLine + "\n");
		writer.write(secondLine + "\n");
		
		StringTokenizer st3 = new StringTokenizer(thirdLine);
		
	
		String firstAttribute = st3.nextToken();
		writer.write("\t" + firstAttribute + "\t");
		writer.write(st3.nextToken() + "\n");

		
		double tp = 0.0;
		double fp = 0.0;
		
		
		StringTokenizer st4 = new StringTokenizer(fourthLine);
		writer.write(st4.nextToken()+ "\t");

		tp = Double.parseDouble(st4.nextToken());
		writer.write(tp + "\t");
			
		fp = Double.parseDouble(st4.nextToken());
		writer.write(fp + "\n");

		double fn = 0.0;
		double tn = 0.0;
		
		StringTokenizer st5 = new StringTokenizer(fifthLine);
		writer.write(st5.nextToken()+ "\t");

		fn = Double.parseDouble(st5.nextToken());
		writer.write(fn + "\t");
			
		tn = Double.parseDouble(st5.nextToken());
		writer.write(tn + "\n");
		
		writer.write("Precision\tRecall\tAccuracy\n");
		double precision = tp + fp == 0 ? 1.0 : (tp / (tp + fp));
		double recall = tp + fn == 0 ? 1.0 : (tp / (tp + fn));
		double accuracy = ((tp + tn) / (tp + tn + fp + fn));
		writer.write(precision + "\t" + recall + "\t" + accuracy + "\n");
		
	}
	
	public static void main(String[] args) throws IOException {
		String input = "G:\\Dropbox\\research\\3rd\\logistic regression\\models\\prediction\\90.results";
		String output = "G:\\Dropbox\\research\\3rd\\logistic regression\\models\\prediction\\90.parsed";
		
		RResultParser parser = new RResultParser();
		parser.parseFile(input, output);
		
	}
}
