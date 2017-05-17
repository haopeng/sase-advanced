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

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;

public class PropertyReader {
	//RawEventReader reader = new RawEventReader(inputFolder, abnormalStart, abnormalEnd, referenceStart, referenceEnd, currentPartitionId);
	String propertyFilePath;
	
	String name;
	String inputFolder;
	long abnormalStart;
	long abnormalEnd;
	long referenceStart;
	long referenceEnd;
	long currentPartitionId;
	
	public PropertyReader(String path) throws IOException {
		this.propertyFilePath = path;
		this.readProperties();
	}
	
	public void readProperties() throws IOException {
		Properties prop = new Properties();
 		InputStream inputStream = new FileInputStream(this.propertyFilePath);
 		prop.load(inputStream);
 
		// get the property value and print it out
 		name = prop.getProperty("name");
 		
		abnormalStart = Long.parseLong(prop.getProperty("abnormalStart"));
		abnormalEnd = Long.parseLong(prop.getProperty("abnormalEnd"));
		referenceStart = Long.parseLong(prop.getProperty("referenceStart"));
		referenceEnd = Long.parseLong(prop.getProperty("referenceEnd"));
		currentPartitionId= Long.parseLong(prop.getProperty("currentPartitionId"));
		inputFolder= prop.getProperty("inputFolder");
		
		if (prop.getProperty("excludePartition").equalsIgnoreCase("true")) {
			ExplanationSettings.excludePartition = true;
		} else {
			ExplanationSettings.excludePartition = false;
		}
		
		System.out.println("Experiment name=\t" + name);
		System.out.println("abnormalStart=\t" + abnormalStart);
		System.out.println("abnormalEnd=\t" + abnormalEnd);
		System.out.println("referenceStart=\t" + referenceStart);
		System.out.println("referenceEnd=\t" + referenceEnd);
		
		System.out.println("currentPartitionId=\t" + currentPartitionId);
		System.out.println("inputFolder=\t" + inputFolder);
		
		if (ExplanationSettings.excludePartition) {
			System.out.println("Exclude partition");
		} else {
			System.out.println("Include partition");
		}
		
	}

	public String getPropertyFilePath() {
		return propertyFilePath;
	}

	public void setPropertyFilePath(String propertyFilePath) {
		this.propertyFilePath = propertyFilePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public long getCurrentPartitionId() {
		return currentPartitionId;
	}

	public void setCurrentPartitionId(long currentPartitionId) {
		this.currentPartitionId = currentPartitionId;
	}
	
	
	
}
