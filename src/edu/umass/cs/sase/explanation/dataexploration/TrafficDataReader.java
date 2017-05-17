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

package edu.umass.cs.sase.explanation.dataexploration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Read in large file, and cut by certain attribute value
 * @author haopeng
 *
 */
public class TrafficDataReader {
	String inputFilePath;
	String outputFolder;
	int attrCount;
	int cutAattrIndex;
	
	String[] header;
	HashMap<String, CSVWriter> writerIndex;
	public TrafficDataReader(String input, int attrCount, int cutIndex, String outputFolder) {
		this.inputFilePath = input;
		this.attrCount = attrCount;
		this.cutAattrIndex = cutIndex;
		this.outputFolder = outputFolder;
		
		this.writerIndex = new HashMap<String, CSVWriter>();
		
		//CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
	}
	
	public void readFile() throws IOException {
		CSVReader reader = new CSVReader(new FileReader(this.inputFilePath));
		this.header = reader.readNext();
		String[] line = null;
		
		int count = 0;
		while ((line = reader.readNext()) != null) {
			System.out.print(count ++ + "\t");
			for (int i = 0; i < this.attrCount; i ++) {
				System.out.print(line[i] + "\t");
			}
			System.out.println();
			this.writeLine(line);
		}
		this.closeWriters();
	}
	
	public void closeWriters() throws IOException {
		for (CSVWriter writer: this.writerIndex.values()) {
			writer.flush();
			writer.close();
		}
	}
	public void writeLine(String[] line) throws IOException {
		String key = line[this.cutAattrIndex];
		if (!this.writerIndex.containsKey(key)) {
			CSVWriter writer = new CSVWriter(new FileWriter(this.outputFolder + key + ".csv"), ',',CSVWriter.NO_QUOTE_CHARACTER);
			this.writerIndex.put(key, writer);
			writer.writeNext(this.header);
		}
		CSVWriter writer = this.writerIndex.get(key);
		writer.writeNext(line);
	}
	
	public long convertDateString(String dateStr) throws ParseException {
		//"MM/dd/yyyy hh:mm:ss"
		/*
		String test = "9/15/2011  12:00:00 PM";
		TrafficDataReader reader = new TrafficDataReader();
		System.out.println(reader.convertDateString(test));
		*/
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
		Date date = sdf.parse(dateStr);
		return date.getTime();
	}
	
	
	//detectorid	starttime	volume	speed	occupancy	status	dqflags
	public static void main(String[] args) throws ParseException, IOException {
		String freeway_loopdata = "/Users/haopeng/Copy/TrafficData/Freeway/FreewayData/freeway_loopdata.csv";
		String outputFolder = "/Users/haopeng/Copy/TrafficData/Freeway/FreewayData/freeway_loopdata_by_detectors/";
		int attrCount = 7;
		int cutIndex = 0;
		
		TrafficDataReader reader = new TrafficDataReader(freeway_loopdata, attrCount, 0, outputFolder);
		reader.readFile();
		
	}
	
}
