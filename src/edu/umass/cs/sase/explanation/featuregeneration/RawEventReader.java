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

import au.com.bytecode.opencsv.CSVReader;
import edu.umass.cs.sase.explanation.UI.ExplanationProfiling;
import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.engine.ProfilingEngine;
import edu.umass.cs.sase.stream.Event;


/**
 * Read raw events from input folder
 * Store raw events in memory for feature generation
 * @author haopeng
 *
 */
public class RawEventReader {
	public String inputFolder;
	long abnormalStart;
	long abnormalEnd;
	
	long referenceStart;
	long referenceEnd;
	
	public ArrayList<String[]> schemas;
	public ArrayList<ArrayList<String[]>> abnormalRawEventLists;
	public ArrayList<ArrayList<String[]>> referenceRawEventLists;

	long parititionIdToExclude;
	
	public RawEventReader(String inputFolder, long abnormalStart, long abnormalEnd, long referenceStart, long referenceEnd, long partitionId) throws IOException {
		this.inputFolder = inputFolder;
		this.abnormalStart = abnormalStart;
		this.abnormalEnd = abnormalEnd;
		this.referenceStart = referenceStart;
		this.referenceEnd = referenceEnd;
		
		this.parititionIdToExclude = partitionId;
		
		this.read();
	}
	
	/**
	 * Start reading from here
	 * Scan the folder
	 * Then parse each file
	 * @throws IOException 
	 */
	public void read() throws IOException {
	
		//processs
		this.schemas= new ArrayList<String[]>();
		this.abnormalRawEventLists = new ArrayList<ArrayList<String[]>>();
		this.referenceRawEventLists = new ArrayList<ArrayList<String[]>>();

		
		File folder = new File(this.inputFolder);
		File[] files = folder.listFiles();
		for (File f : files) {
			this.parseFile(f);
		}
	}
	
	public void parseFile(File f) throws IOException {
		if (f.isDirectory() || f.getName().contains(".DS_Store")) {
			return;
		}
		if (ExplanationSettings.debugMode) {
			System.out.println("Parsing:" + f.getAbsolutePath());
		}


		CSVReader csvReader = new CSVReader(new FileReader(f.getAbsolutePath()));
		
		String[] schema= csvReader.readNext();
		this.schemas.add(schema);
		
		ArrayList<String[]> abnormalList = new ArrayList<String[]>();
		ArrayList<String[]> referenceList = new ArrayList<String[]>();
		this.abnormalRawEventLists.add(abnormalList);
		this.referenceRawEventLists.add(referenceList);
		
		String[] strs = null;
		boolean eventTypeSet = false;
		
		while((strs = csvReader.readNext()) != null) {
			ProfilingEngine.totalReadEvent ++;
			if (!eventTypeSet) {
				schema[0] = strs[1];// remember the event type name
				eventTypeSet = true;
			}
			long ts = this.getTimeStamp(schema, strs);
			if (ts >= this.abnormalStart && ts <= this.abnormalEnd && this.checkPartition(strs, schema)) {
				abnormalList.add(strs);
				
			} else if (ts >= this.referenceStart && ts <= this.referenceEnd && this.checkPartition(strs, schema)) {
				referenceList.add(strs);
			} else if (ts > this.abnormalEnd && ts > this.referenceEnd) {
				break; //stop 
			} else {
				ProfilingEngine.uselessEvent ++;
			}
		}
		
		csvReader.close();
	}
	
	public boolean checkPartition(String[] strs, String[] schema) {
		if (!ExplanationSettings.excludePartition) {
			return true;
		}
		
		if (strs[0].equals("g")) {
			return true;
		}
		
		if (Double.parseDouble(strs[3]) == this.parititionIdToExclude) {
			return false;
		}
		
		return true;
	}
	
	public long getTimeStamp(String[] schema, String[] strs) {
		//currently hard coded
		if (strs[0].equals("g")){
			return Long.parseLong(strs[4]);
		} else {
			return Long.parseLong(strs[7]);
		}
	}
	
	public void printRawEventsInformation() {
		System.out.println("Total event types:" + this.schemas.size());
		
		for (int i = 0; i < this.schemas.size(); i ++) {
			System.out.println(i + ":" + "\tAbnormal count:\t" + this.abnormalRawEventLists.get(i).size() + "\tReference count:\t" + this.referenceRawEventLists.get(i).size());
		}
	}
	
	
	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public long getAbnormalStart() {
		return abnormalStart;
	}

	public void setAbnormalStart(long abnormalStart) {
		this.abnormalStart = abnormalStart;
	}

	public long getAbnormalEnd() {
		return abnormalEnd;
	}

	public void setAbnormalEnd(long abnormalEnd) {
		this.abnormalEnd = abnormalEnd;
	}

	public long getReferenceStart() {
		return referenceStart;
	}

	public void setReferenceStart(long referenceStart) {
		this.referenceStart = referenceStart;
	}

	public long getReferenceEnd() {
		return referenceEnd;
	}

	public void setReferenceEnd(long referenceEnd) {
		this.referenceEnd = referenceEnd;
	}

	public ArrayList<String[]> getSchemas() {
		return schemas;
	}

	public void setSchemas(ArrayList<String[]> schemas) {
		this.schemas = schemas;
	}

	public ArrayList<ArrayList<String[]>> getAbnormalRawEventLists() {
		return abnormalRawEventLists;
	}

	public void setAbnormalRawEventLists(
			ArrayList<ArrayList<String[]>> abnormalRawEventLists) {
		this.abnormalRawEventLists = abnormalRawEventLists;
	}

	public ArrayList<ArrayList<String[]>> getReferenceRawEventLists() {
		return referenceRawEventLists;
	}

	public void setReferenceRawEventLists(
			ArrayList<ArrayList<String[]>> referenceRawEventLists) {
		this.referenceRawEventLists = referenceRawEventLists;
	}

	public static void main(String[] args) throws IOException {
		//test
		String inputFolder = "/Users/haopeng/Copy/Data/test/input";
		
		//test is in edu.umass.cs.sase.explanation.UI.ExplanationUI
	}
	
	
}
