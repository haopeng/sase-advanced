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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Split file into random 30 percent and 70 percent
 * @author haopeng
 *
 */
public class SplitFile {
	public void splitFile(String inputFile, String outputFile1, String outputFile2) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer1 = new BufferedWriter(new FileWriter(outputFile1));
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(outputFile2));
		String line = reader.readLine();//header
		writer1.write(line + "\n" );
		writer2.write(line + "\n" );
		int lineCount = 0;
		int count1 = 0;
		int count2 = 0;
		while((line = reader.readLine()) != null){
			Random rand = new Random(System.currentTimeMillis());
			int i;
			i = rand.nextInt(10);
			if(i >= 7){//70 %
				writer1.write(line + "\n");
				System.out.println("Part1, " + lineCount);
				count1 ++;
			}else{
				writer2.write(line + "\n");
				System.out.println("Part2, " + lineCount);
				count2 ++;
			}
			lineCount ++;
		}
		System.out.println("Total line:" + lineCount + ", Part1:" + count1 + ", Part2:" + count2);
		reader.close();
		writer1.close();
		writer2.close();
	}
	
	public static void main(String args[]) throws IOException{
		/*
		String inputFile = "g:\\Data\\ordered-all-numeric-sliding-chopped-labeled.vw";
		String outputFile1 = "g:\\Data\\ordered-all-numeric-sliding-chopped-labeled-70percent.vw";
		String outputFile2 = "g:\\Data\\ordered-all-numeric-sliding-chopped-labeled-30percent.vw";
		*/
		
		String inputFile = "I:\\Copy\\Data\\2013\\merged\\ordered-all-numeric-chopped-labeled.vw";
		String outputFile1 = "I:\\Copy\\Data\\2013\\merged\\ordered-all-numeric-chopped-labeled-30percent.vw";
		String outputFile2 = "I:\\Copy\\Data\\2013\\merged\\ordered-all-numeric-chopped-labeled-70percent.vw";
		
		SplitFile s = new SplitFile();
		s.splitFile(inputFile, outputFile1, outputFile2);
		
		
	}
}
