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
package edu.umass.cs.sase.explanation.featuregeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.engine.ProfilingEngine;
import au.com.bytecode.opencsv.CSVReader;

public class OptimizedRawEventReader extends RawEventReader{
	static long chunkSize = 1000000L; // miliseconds
	ArrayList<Long> abnormalFileNumbers;
	ArrayList<Long> referenceFileNumbers;
	
	int skipped;
	public OptimizedRawEventReader(String inputFolder, long abnormalStart,
			long abnormalEnd, long referenceStart, long referenceEnd,
			long partitionId) throws IOException {
		super(inputFolder, abnormalStart, abnormalEnd, referenceStart, referenceEnd,
				partitionId);
	}
	
	public void read() throws IOException {
		//initialize
		this.schemas= new ArrayList<String[]>();
		this.abnormalRawEventLists = new ArrayList<ArrayList<String[]>>();
		this.referenceRawEventLists = new ArrayList<ArrayList<String[]>>();
		//generate fileNumbers
		//System.out.println("Debug: generate file numbers for abnormal");
		this.abnormalFileNumbers = new ArrayList<Long>();
		this.generateFileNumbers(this.abnormalStart, this.abnormalEnd, this.abnormalFileNumbers);
		this.referenceFileNumbers = new ArrayList<Long>();
		//System.out.println("Debug: generate file numbers for reference");
		this.generateFileNumbers(this.referenceStart, this.referenceEnd, this.referenceFileNumbers);
		//read
		File folder = new File(this.inputFolder);
		File[] subFolders = folder.listFiles();
		if (subFolders == null) {
			System.out.println();
		}
		for (File subfolder : subFolders) {
			this.parseSubFolder(subfolder);
		}
	}
	
	public void parseSubFolder(File subFolder) throws IOException {
		if (subFolder.getName().contains(".DS_Store")) {
			return; //exception
		}
		//schema
		String[] schema = this.getSchema(subFolder);
		this.schemas.add(schema);
		//System.out.println("Debug: file type:" + schema[0]);
		//read abnormalList
		ArrayList<String[]> abnormalList = this.readEventList(subFolder, this.abnormalStart, this.abnormalEnd, this.abnormalFileNumbers, schema);
		ArrayList<String[]> referenceList = this.readEventList(subFolder, this.referenceStart, this.referenceEnd, this.referenceFileNumbers, schema);
		
		this.abnormalRawEventLists.add(abnormalList);
		this.referenceRawEventLists.add(referenceList);
	}
	
	public ArrayList<String[]> readEventList(File subFolder, long start, long end, ArrayList<Long> fileNumbers, String[] schema) throws IOException {
		ArrayList<String[]> eventList = new ArrayList<String[]>();
		String[] line;
		for (Long num : fileNumbers) {
			String fileName = subFolder.getAbsolutePath() + "/" + schema[0] + "-" + num + ".csv";
			//System.out.println(fileName);
			//check file exists
			File f = new File(fileName);
			if (f.exists()) {
				//read 
				CSVReader reader = new CSVReader(new FileReader(fileName));
				reader.readNext();//skip header
				while((line = reader.readNext()) != null) {
					ProfilingEngine.totalReadEvent ++;
					long ts = this.getTimeStamp(schema, line);
					if (ts >= start && ts<= end && this.checkPartition(line, schema)) {
						eventList.add(line);
					} else if (ts > end) {
						reader.close();
						break;
					} else {
						ProfilingEngine.uselessEvent ++;// events before start timestamp
					}
				}
				reader.close();
			}
		}
		return eventList;
	}
	
	/**
	 * Read schema and event type
	 * @return
	 * @throws IOException 
	 */
	public String[] getSchema(File folder) throws IOException {
		String eventType = folder.getName();
		System.out.println(folder.getName());
		CSVReader csvReader = new CSVReader(new FileReader(folder.listFiles()[0].getAbsolutePath()));
		String[] schema = csvReader.readNext();
		csvReader.close();
		
		schema[0] = eventType;//remember event type
		return schema;
	}
	
	
	public void generateFileNumbers(long start, long end, ArrayList<Long> list) {
		long first = start / this.chunkSize;
		long last = end / this.chunkSize;
		for (long i = first; i <= last; i ++) {
			list.add(i);
			//System.out.println("Debug: " + i + "\t total:\t" + list.size()  );
		}
		if (ExplanationSettings.printResult) {
			System.out.println("Total file numbers:" + list.size());
		}

	}
	
	
	public static void main(String[] args) throws IOException {
		String inputFolder = "/Users/haopeng/Copy/Data/2015/singleTypes-all-chunks/";
		long start = System.currentTimeMillis();
		OptimizedRawEventReader reader = new OptimizedRawEventReader(inputFolder, 1427515215000L, 1427545765000L, 1428795495000L, 1428800585000L, 100);
		System.out.println("Total:" + ProfilingEngine.totalReadEvent + "\tUseless = " + ProfilingEngine.uselessEvent + "\tUseful=" + (ProfilingEngine.totalReadEvent - ProfilingEngine.uselessEvent));
		System.out.println(System.currentTimeMillis() - start);
		
		ProfilingEngine.totalReadEvent = 0;
		ProfilingEngine.uselessEvent = 0;
		String input = "/Users/haopeng/Copy/Data/2015/singleTypes-all/";
		start = System.currentTimeMillis();
		RawEventReader reader2 = new RawEventReader(input, 1427515215000L, 1427545765000L, 1428795495000L, 1428800585000L, 100);
		System.out.println("Total:" + ProfilingEngine.totalReadEvent + "\tUseless = " + ProfilingEngine.uselessEvent + "\tUseful=" + (ProfilingEngine.totalReadEvent - ProfilingEngine.uselessEvent));
		System.out.println(System.currentTimeMillis() - start);
		
		
	}


}
