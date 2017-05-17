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

public class MaterialMonitoringSimulator {
	long startTime;
	long endTime;
	long abnormalStart;
	long abnormalEnd;
	
	int step;
	boolean isNormal;
	double minNormal;
	double maxNormal;
	double minAbnormal;
	double maxAbnormal;
	String outputPath;
	
	int materialId;
	
	public MaterialMonitoringSimulator(long startTime, long endTime, long abnormalStart, long abnormalEnd, int step, boolean isNormal, String outputPath, double minNormal, double maxNormal, double minAbnormal, double maxAbnormal, int materialId) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.abnormalStart = abnormalStart;
		this.abnormalEnd = abnormalEnd;
		
		this.step = step;
		this.isNormal = isNormal;
		this.outputPath = outputPath;
		this.minNormal = minNormal;
		this.maxNormal = maxNormal;
		this.minAbnormal = minAbnormal;
		this.maxAbnormal = maxAbnormal;
		
		this.materialId = materialId;
		
	}
	
	public void simulate() throws IOException{
		System.out.println("outputPath is:" + this.outputPath);
		CSVWriter writer = new CSVWriter(new FileWriter(this.outputPath));
		//header
		String[] header = {"LogSource", "eventType", "id",
				"nodeNumber",	"timestamp",	"lowerBound",	"upperBound",
				"originalEventId",	"value"};
		writer.writeNext(header);
		
		//events
		String[] eventContent = new String[header.length];
		eventContent[0] = "g";//log source
		eventContent[1] = "Material-" + this.materialId;//eventType
		eventContent[2] = "0";//id
		eventContent[3] = "0";//nodeNumber
		//eventContent[4] = "PartQuality";//timestamp
		eventContent[5] = "0";//lowerbound
		eventContent[6] = "0";//upperbound
		eventContent[7] = "0";//originalEventId
		//eventContent[8] = "0";//value
		
		//populate events here
		long currentTime = this.startTime;
		
		Random random = new Random(System.currentTimeMillis());
		int count = 0;
		while(currentTime <= this.endTime) {
			//part quality
			double value = random.nextDouble();
			if (this.isNormal || currentTime < this.abnormalStart || currentTime > this.abnormalEnd) {
				value = this.minNormal + (this.maxNormal - this.minNormal) * value;

			} else {
				value = this.minAbnormal + (this.maxAbnormal - this.minAbnormal) * value;
			}
			
			
			eventContent[8] = "" + value;
			
			eventContent[4] = "" + currentTime;
			writer.writeNext(eventContent);
			count ++;
			currentTime += this.step;
			

		}
		System.out.println(count + " events generated");
		writer.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		
		int partId = 1;
		String outputPath = "/Users/haopeng/Copy/Data/supplychain/quality/1.csv";
		long startTime = 0;
		long endTime = 1036800011; //1 day in milliseconds;
		long abnormalStart = 864000009;
		long abnormalEnd = 1036800011;
		
		boolean isNormal = true;
		
		
		int step = 10000;//10 seconds
		
		double minNormal = 9000;
		double maxNormal = 10000;
		
		double minAbnormal = 6000;
		double maxAbnormal = 9000;
		
		for (int i = 1; i <= 3; i ++) {
			isNormal = false;
			partId = i;
			outputPath = "/Users/haopeng/Copy/Data/supplychain/material/material-" + i + ".csv";

			MaterialMonitoringSimulator mms = new MaterialMonitoringSimulator(startTime, endTime, abnormalStart, abnormalEnd, step, isNormal, outputPath, minNormal, maxNormal, minAbnormal, maxAbnormal, partId);
			mms.simulate();
		}
		
		
		for (int i = 4; i <= 100; i ++) {
			isNormal = true;
			partId = i;
			outputPath = "/Users/haopeng/Copy/Data/supplychain/material/material-" + i + ".csv";

			EnviromentMonitoringSimulator mms = new EnviromentMonitoringSimulator(startTime, endTime, abnormalStart, abnormalEnd, step, isNormal, outputPath, minNormal, maxNormal, partId);
			mms.simulate();
		}
		

	}

}
