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

/**
 * This class is used to label CSV files
 * It will add an attribute called label to the tail of each line.
 * The label is based on timestamp.
 * Input a few intervals for abnormal, others will be labeled as normal.
 * @author haopeng
 *
 */
public class LabelData {
	ArrayList<TimeInterval> abnormalPeriod;
	
	public LabelData(){
		this.abnormalPeriod = new ArrayList<TimeInterval>();
		this.abnormalPeriod.add(new TimeInterval(1373637583030L,1373639906229L));//job no.12
		this.abnormalPeriod.add(new TimeInterval(1373647741800L,1373649648791L));//job no.14
		this.abnormalPeriod.add(new TimeInterval(1373658550307L,1373659789027L));//job no.16
		
		
	}
	//abnormal: -1
	//normal: 1
	public String label(String timestamp){
		long t = Long.parseLong(timestamp);
		for(TimeInterval interval: this.abnormalPeriod){
			if(interval.containsTime(t)){
				return "-1";//abnormal
			}
		}
		return "1";//normal
	}
	
	public void labelFile(String inputFilePath, String outputFilePath) throws IOException{
		CSVReader reader = new CSVReader(new FileReader(inputFilePath));
		CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String nextLine[] = reader.readNext();
		String attributes[] = new String[nextLine.length + 1];
		for(int i = 0; i < nextLine.length; i ++){
			attributes[i] = nextLine[i];
		}
		attributes[nextLine.length] = "label";
		writer.writeNext(attributes);
		int lineCount = 0;
		int abnormalCount = 0;
		while((nextLine = reader.readNext()) != null){
			String values[] = new String[nextLine.length + 1];
			for(int i = 0; i < nextLine.length; i ++){
				values[i] = nextLine[i];
			}
			String labelForLine = this.label(values[7]);
			if(labelForLine.equalsIgnoreCase("-1")){
				abnormalCount ++;
			}
			values[nextLine.length] = labelForLine;
			writer.writeNext(values);
			System.out.println(lineCount + ":" + labelForLine);
			lineCount ++;
		}
		writer.flush();
		writer.close();
		reader.close();
		System.out.println("Total abnormal records: " + abnormalCount);
	}
	
	public static void main(String args[]) throws IOException{
		LabelData ld = new LabelData();
		
		/* test
		String inputFilePath = "I:\\Copy\\Data\\2013\\test\\proc_total_event.txt.oldLabel.csv";
		String outputFilePath = "I:\\Copy\\Data\\2013\\test\\proc_total_event.txt.newLabel.csv";
		*/
		/*no order
		String inputFilePath = "I:\\Copy\\Data\\2013\\merged\\noorder.csv";
		String outputFilePath = "I:\\Copy\\Data\\2013\\merged\\noorder-labeled-3abnormal-periods.csv";
		*/
		String inputFilePath = "I:\\Copy\\Data\\2013\\merged\\ordered-all-numeric-chopped.csv";
		String outputFilePath = "I:\\Copy\\Data\\2013\\merged\\ordered-all-numeric-chopped-labeled.csv";

		ld.labelFile(inputFilePath, outputFilePath);
	}
	
}
