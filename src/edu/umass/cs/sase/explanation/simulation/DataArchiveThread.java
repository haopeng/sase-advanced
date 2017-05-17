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

package edu.umass.cs.sase.explanation.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import edu.umass.cs.sase.stream.Event;
import edu.umass.cs.sase.stream.Stream;

public class DataArchiveThread implements Runnable{
	String archiveFolder;
	Stream inputStream;
	
	int streamIndex;
	
	int windowSize = 1000;//seconds
	//int windowSize = 10;//seconds
	int localCount;
	HashMap<String, EventTypeArchiver> writerIndex;
	
	ArrayList<QueryThread> queryThreads;
	
	public DataArchiveThread(String archiveFolder, Stream inputStream, ArrayList<QueryThread> queryThreads) {
		this.archiveFolder = archiveFolder;
		this.inputStream = inputStream;
		
		this.localCount = 0;
		this.streamIndex = 0;
		
		this.writerIndex = new HashMap<String, EventTypeArchiver>();
		
		this.queryThreads = queryThreads;
	}
	
	public void run() {
		while(true) {
			if (this.localCount < SimulationSettings.globalCount) {
				try {
					this.archiveOneBatch();
					//System.out.println(this.streamIndex + "\t" + "total:" + this.inputStream.getSize());
				} catch (IOException e) {
					e.printStackTrace();
				}
				localCount ++;
				
				int delay = SimulationSettings.globalCount - this.localCount;
				if (delay >= SimulationSettings.maxDelayTime) {
					System.out.println("ArchiverThread delayed by" + delay + " seconds.");
				}
			} else {
				//System.out.println("Debug: No delay. Thread will sleep for 0.5 second");
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void archiveOneBatch() throws IOException {
		for (int i = 0; i < SimulationSettings.streamRate; i ++) {
			Event e = this.inputStream.getEventAtIndex(this.streamIndex);
			if (e == null) {
				this.outputThreadAnalytics();
				this.flushAllWriters();
				this.closeAllWriters();
				System.exit(0);
			}
			this.archiveOneEvent(e);
			//System.out.println("Debug: archived one evnt" + e.toString());
			this.streamIndex ++;
		}
	}
	
	public void archiveOneEvent(Event e) throws IOException {
		String eventType = e.getEventType();
		EventTypeArchiver writer = this.writerIndex.get(eventType);
		if (writer == null) {
			writer = new EventTypeArchiver(this.archiveFolder, e.getEventType(), e.attributeNameToStringArray(), this.windowSize);
			this.writerIndex.put(e.getEventType(), writer);
		}
		writer.archiveOneEvent(e);
	}
	
	public void flushAllWriters() throws IOException {
		for (EventTypeArchiver archiver : this.writerIndex.values()) {
			archiver.flushWriter();
		}
	}
	
	public void closeAllWriters() throws IOException {
		for (EventTypeArchiver archiver : this.writerIndex.values()) {
			archiver.closeWriter();
		}
	}
	
	public void outputThreadAnalytics() {
		SimulationSettings.stopOuputCount = true;
		double[] avgDelayForExplanation = new double[this.queryThreads.size()];
		double[] avgDelayForNoExplanation = new double[this.queryThreads.size()];
		
		System.out.println("***********************Threads Stats**************************");
		//this.queryThreads.get(0).printAnalyticsHeader();
		for (int i = 0; i < this.queryThreads.size(); i ++) {
			this.queryThreads.get(i).computeAvgDelay();
			avgDelayForNoExplanation[i] = this.queryThreads.get(i).getAvgDelayForNoExplanation();
			avgDelayForExplanation[i] = this.queryThreads.get(i).getAvgDelayForExplanation();
			
			System.out.println("Thread " + i + "\t" + avgDelayForNoExplanation[i] + "\t" + avgDelayForExplanation[i]);
		}
		System.out.println("***********************End of Threads Stats**************************");
		
		//mean
		Mean mean = new Mean();
		double avgDelayNoExplanationForAllThreads = mean.evaluate(avgDelayForNoExplanation);
		double avgDelayExplanationForAllThreads = mean.evaluate(avgDelayForExplanation);
		//standard deviation
		StandardDeviation sd = new StandardDeviation();
		double sdDelayNoExplanation = sd.evaluate(avgDelayForNoExplanation);
		double sdDelayExplanation = sd.evaluate(avgDelayForExplanation);
		//response time
		double avgResponse = (double)SimulationSettings.explanationDuration / 1000.0 / (double)SimulationSettings.explanationRepeat; // in seconds
		
		
		//print
		System.out.println("AvgDelayNoExplanation\tStandradDeviationNoExplanation\tAvgDelayExplanation\tStandardDeviationExplanation\tReponseTimeExplanation");
		System.out.println(avgDelayNoExplanationForAllThreads + "\t" + sdDelayNoExplanation + "\t" + avgDelayExplanationForAllThreads + "\t" + sdDelayExplanation + "\t" + avgResponse);
	}

	
	public void outputThreadAnalyticsOld() {
		SimulationSettings.stopOuputCount = true;
		System.out.println("***********************Threads Stats**************************");
		this.queryThreads.get(0).printAnalyticsHeader();
		for (int i = 0; i < this.queryThreads.size(); i ++) {
			this.queryThreads.get(i).analyzeDelayInformationAndOutput();
		}
		System.out.println("***********************End of Threads Stats**************************");
		
		//temporal analysis, concurrent delayed threads at each second
		int duration = inputStream.getSize() / (int) SimulationSettings.streamRate;
		int[] concurrent = new int[duration];
		int[] sum = new int[duration];
		int[] max = new int[duration];
		for (QueryThread qThread : this.queryThreads) {
			int[] delayTime = qThread.delays;
			for (int i = 0; i < concurrent.length; i ++) {
				if (delayTime[i] > 0) {
					concurrent[i] ++;
					sum[i] += delayTime[i];
					max[i] = Math.max(max[i], delayTime[i]);
				}
			}
		}
		
		//output
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("#################Temporal Analysis###############");
		System.out.println("Time\tConcurrentDelayedThread\tSum\tMax\tAvg\tDelayOrNot");
		for (int i = 0; i < duration; i ++) {
			System.out.print(i + "\t" + concurrent[i] + "\t" + sum[i] + "\t" + max[i] + "\t" + (double)sum[i] / (double)concurrent[i] + "\t");
			if (concurrent[i] > 0) {
				System.out.println("1" + "\t");
			} else {
				System.out.println("0" + "\t");
			}
		}
		
		System.out.println("#################End of Temporal Analysis###############");
	}


}
