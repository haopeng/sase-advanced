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
package edu.umass.cs.sase.explanation.usecase.supplychain;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * This class is used to generate simulation for monitored metrics: quality of parts. 
 * The schema is the same as files in /Users/haopeng/Copy/Data/2015/hadoop/visualize2
 * 
 * LogSource	eventType	id	jobId	taskId	attemptID	nodeNumber	timestamp	lowerBound	upperBound	originalEventId	value	TimeSinceJobStarted 	AccumulatedValue
 * @author haopeng
 *
 */
public class MonitoredMetricsSimulator {
	long startTime;
	long endTime;
	int step;
	boolean isNormal;
	double minNormal;
	double maxNormal;
	double minAbnormal;
	double maxAbnormal;
	String outputPath;
	
	int partId;
	
	public MonitoredMetricsSimulator(long startTime, long endTime, int step, boolean isNormal, String outputPath, double minNormal, double maxNormal, double minAbnormal, double maxAbnormal, int partId) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.step = step;
		this.isNormal = isNormal;
		this.outputPath = outputPath;
		this.minNormal = minNormal;
		this.maxNormal = maxNormal;
		this.minAbnormal = minAbnormal;
		this.maxAbnormal = maxAbnormal;
		this.partId = partId;
	}
	
	public void simulate() throws IOException{
		System.out.println("outputPath is:" + this.outputPath);
		CSVWriter writer = new CSVWriter(new FileWriter(this.outputPath));
		//header
		String[] header = {"LogSource", "eventType", "id",	"jobId",
				"taskId",	"attemptID",	"nodeNumber",	"timestamp",	"lowerBound",	"upperBound",
				"originalEventId",	"value",	"TimeSinceJobStarted", 	"AccumulatedValue"};
		writer.writeNext(header);
		
		//events
		String[] eventContent = new String[header.length];
		eventContent[0] = "h";//log source
		eventContent[1] = "PartQuality";//eventType
		eventContent[2] = "0";//id
		eventContent[3] = "" + this.partId;//jobId
		eventContent[4] = "1";//eventtaskId
		eventContent[5] = "0";//attemptId
		eventContent[6] = "0";//nodeNumber
		//eventContent[7] = "PartQuality";//timestamp
		eventContent[8] = "0";//lowerbound
		eventContent[9] = "0";//upperbound
		eventContent[10] = "0";//originalEventId
		eventContent[11] = "0";//value
		//eventContent[12] = "";//TimeScineJobStarted
		//eventContent[13] = "PartQuality";//accumulatedValue
		
		//populate events here
		long currentTime = this.startTime;
		
		Random random = new Random(System.currentTimeMillis());
		int count = 0;
		while(currentTime <= this.endTime) {
			//part quality
			double value = random.nextDouble();
			if (this.isNormal) {
				value = this.minNormal + (this.maxNormal - this.minNormal) * value;
			} else {
				value = this.minAbnormal + (this.maxAbnormal - this.minAbnormal) * value;
			}
			eventContent[13] = "" + value;
			
			eventContent[7] = "" + currentTime;
			eventContent[12] = "" + (currentTime - this.startTime);
			
			writer.writeNext(eventContent);
			
			currentTime += this.step;
			count ++;
			System.out.println(count + ":" + value);
		}
		
		writer.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		
		//1
		int partId = 1;
		String outputPath = "/Users/haopeng/Copy/Data/supplychain/quality/1.csv";
		long startTime = 0;
		long endTime = 86400000; //1 day in milliseconds;
		boolean isNormal = true;
		//2
		partId = 2;
		outputPath = "/Users/haopeng/Copy/Data/supplychain/quality/2.csv";
		startTime = 86400001;
		endTime = 172800001; //1 day in milliseconds;
		isNormal = true;
		//3
		partId = 3;
		outputPath = "/Users/haopeng/Copy/Data/supplychain/quality/3.csv";
		startTime = 172800002;
		endTime = 259200002; //1 day in milliseconds;
		isNormal = true;
		//4
		partId = 4;
		outputPath = "/Users/haopeng/Copy/Data/supplychain/quality/4.csv";
		startTime = 259200003;
		endTime = 345600003; //1 day in milliseconds;
		isNormal = true;
		//5
		partId = 5;
		outputPath = "/Users/haopeng/Copy/Data/supplychain/quality/5.csv";
		startTime = 345600004;
		endTime = 432000004; //1 day in milliseconds;
		isNormal = true;
		//6
		partId = 6;
		outputPath = "/Users/haopeng/Copy/Data/supplychain/quality/6.csv";
		startTime = 432000005;
		endTime = 518400005; //1 day in milliseconds;
		isNormal = true;
		
		
		int step = 10000;//10 seconds
		
		double minNormal = 8.0;
		double maxNormal = 10.0;
		double minAbnormal = 0.0;
		double maxAbnormal = 8.0;
		
		
		for (int i = 1; i <= 6; i ++) {
			isNormal = true;
			partId = i;
			outputPath = "/Users/haopeng/Copy/Data/supplychain/quality/" + i + ".csv";
			startTime = 86400000 * (i - 1) + i - 1;
			endTime = startTime + 86400000;
			MonitoredMetricsSimulator mms = new MonitoredMetricsSimulator(startTime, endTime, step, isNormal, outputPath, minNormal, maxNormal, minAbnormal, maxAbnormal, partId);
			mms.simulate();
		}
		
		for (int i = 7; i <= 12; i ++) {
			isNormal = false;
			partId = i;
			outputPath = "/Users/haopeng/Copy/Data/supplychain/quality/" + i + ".csv";
			startTime = 86400000 * (i - 1) + i - 1;
			endTime = startTime + 86400000;
			MonitoredMetricsSimulator mms = new MonitoredMetricsSimulator(startTime, endTime, step, isNormal, outputPath, minNormal, maxNormal, minAbnormal, maxAbnormal, partId);
			mms.simulate();
		}
		
		

	}

}
