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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.umass.cs.sase.stream.Event;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CSVSorter {
	ArrayList<Tuple> allRecords;
	public void sortCSV(String inputFilePath, String outputFilePath) throws IOException{
		this.allRecords = new ArrayList<Tuple>();
		long start = System.nanoTime();
		System.out.println("Start reading");
		CSVReader reader = new CSVReader(new FileReader(inputFilePath));
		ArrayList<String[]> allLine = (ArrayList<String[]>) reader.readAll();
		System.out.println("Reading complete, total time:" + (System.nanoTime() - start));
		System.out.println("Start converting...");
		start = System.nanoTime();
		for(int i = 1; i < allLine.size(); i ++){
			String[] line = allLine.get(i);
			Tuple tuple = new Tuple(line[7], line);
			this.allRecords.add(tuple);
		}
		System.out.println("Converting complete,total time:" + (System.nanoTime() - start));
		start = System.nanoTime();
		System.out.println("Start sorting");
		Collections.sort(this.allRecords, new Comparator<Tuple>(){
			@Override
			public int compare(Tuple tuple1, Tuple tuple2){
				//System.out.println("comparing " + e1.getTimestamp() + " and " + e2.getTimestamp());
				Long t1 = tuple1.getTimestamp();
				Long t2 = tuple2.getTimestamp();
				return t1.compareTo(t2);
			}
		});
		System.out.println("Sorting complete, total time:" + (System.nanoTime() - start));
		start = System.nanoTime();
		CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
		writer.writeNext(allLine.get(0));
		
		for(Tuple tuple: this.allRecords){
			writer.writeNext(tuple.getAttributes());
		}
		System.out.println("Writing complete, total time:" + (System.nanoTime() - start));
		
		reader.close();
		writer.close();
		
	}
	
	public static void main(String args[]) throws IOException{
		String inputFilePath = "I:\\Copy\\Data\\2013\\merged\\noorder-labeled-3abnormal-periods.csv";
		String outputFilePath = "I:\\Copy\\Data\\2013\\merged\\noorder-labeled-3abnormal-periods-chopped.csv";
		
		CSVSorter sorter = new CSVSorter();
		sorter.sortCSV(inputFilePath, outputFilePath);


	}
}
