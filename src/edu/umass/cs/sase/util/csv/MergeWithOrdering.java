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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * This class merges all files ordered by timestamps
 * @author haopeng
 *
 */
public class MergeWithOrdering {
	public void mergeFolder(String inputFolderPath, String outputFilePath) throws IOException{
		File inputFolder = new File(inputFolderPath);
		File[] files = inputFolder.listFiles();
		PriorityQueue<LogFileReader> q = new PriorityQueue<LogFileReader>(files.length, new Comparator<LogFileReader>(){
			public int compare(LogFileReader a, LogFileReader b){
				Long ta = a.getCurrentTimestamp();
				Long tb = b.getCurrentTimestamp();
				return ta.compareTo(tb);
			}
		});
		for(File f: files){
			LogFileReader reader = new LogFileReader(f.getAbsolutePath());
			if(reader.isAvailability()){
				q.add(reader);
			}
		}
		CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
		int lineCount = 0;
		while(q.size() > 0){
			LogFileReader reader = q.poll();
			if(lineCount == 0){
				writer.writeNext(reader.getAttributes());
			}
			writer.writeNext(reader.getCurrentValues());
			System.out.println("line:" + lineCount);
			if(reader.readNextLine()){
				q.add(reader);
			}else{
				reader.getReader().close();
			}
			lineCount ++;
			//writer.flush();
			
		}
		writer.flush();
		writer.close();
		
	}
	
	public static void main(String args[]) throws IOException{
		
		//String inputFilePath = "I:\\Copy\\Data\\2013\\test\\";
		//String outputFilePath = "I:\\Copy\\Data\\2013\\testmerge\\ordered.csv";
		
		String inputFilePath = "I:\\Copy\\Data\\2013\\singleTypeOneTable\\";
		String outputFilePath = "I:\\Copy\\Data\\2013\\merged\\ordered.csv";

		
		MergeWithOrdering m = new MergeWithOrdering();
		m.mergeFolder(inputFilePath, outputFilePath);
		
	}
	
}
