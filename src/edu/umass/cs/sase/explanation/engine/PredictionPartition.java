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

package edu.umass.cs.sase.explanation.engine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import edu.umass.cs.sase.explanation.evaluation.DataPoint;


/**
 * Used to read other partition for prediction
 * @author haopeng
 *
 */
public class PredictionPartition extends ExplainEngine{

	public PredictionPartition(String inputPropertiesFile) throws IOException {
		super(inputPropertiesFile);
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<DataPoint> generateDataPoints() throws Exception {
		//read raw events
		this.readRawEvents();
		// generate features
		this.generateFeatures();	
		
		//this.buildFeaturePairs();
		this.computeDistance();
		
		return this.tsFeatureComparator.generateDataPoints();
	}
	
	
	public void getProperties() throws IOException {
		Properties prop = new Properties();
 		InputStream inputStream = new FileInputStream(this.inputPropertiesFile);
 		prop.load(inputStream);
 
		// get the property value and print it out
 		name = prop.getProperty("name");
 		
		abnormalStart = Long.parseLong(prop.getProperty("abnormalStart"));
		abnormalEnd = Long.parseLong(prop.getProperty("abnormalEnd"));
		referenceStart = Long.parseLong(prop.getProperty("referenceStart"));
		referenceEnd = Long.parseLong(prop.getProperty("referenceEnd"));
		currentPartitionId= Long.parseLong(prop.getProperty("currentPartitionId"));
		inputFolder= prop.getProperty("inputFolder");
				
		System.out.println("Experiment name=\t" + name);
		System.out.println("abnormalStart=\t" + abnormalStart);
		System.out.println("abnormalEnd=\t" + abnormalEnd);
		System.out.println("referenceStart=\t" + referenceStart);
		System.out.println("referenceEnd=\t" + referenceEnd);
		
		System.out.println("currentPartitionId=\t" + currentPartitionId);


	}
	
}
