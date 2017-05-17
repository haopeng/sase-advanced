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
package edu.umass.cs.sase.usecase.hadooplog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import edu.umass.cs.sase.stream.HadoopEvent;

/**
 * This class is used to generate period events, like PullPeriod, which describes the runtime period of a datapull.
 * There are also MapPeriod/ReducePeriod
 * @author haopeng
 *
 */
public class PeriodEventGenerator {
	HashMap<String, HadoopEvent> startIndex;
	String inputFile;
	String outputFile;
	BufferedReader reader;
	BufferedWriter writer;
	int startCount;
	int endCount;
	int pairCount;
	int missCount;
	public PeriodEventGenerator(String inputFile, String outputFile) throws IOException{
		this.startIndex = new HashMap<String, HadoopEvent>();
		this.inputFile = inputFile;
		this.outputFile = outputFile;

		
	}
	
	public void processFileForReduceEvents() throws IOException{
		this.reader = new BufferedReader(new FileReader(this.inputFile));
		this.writer = new BufferedWriter(new FileWriter(this.outputFile));
		this.startCount = 0;
		this.endCount = 0;
		this.pairCount = 0;
		this.missCount = 0;
		String line = null;
		while((line = this.reader.readLine()) != null){
			if(line.contains("ReduceFinish")){//to change
				this.processFinishLine(line);
				this.endCount ++;
			}else if (line.contains("ReduceStart")){
				this.processStartLine(line);
				this.startCount ++;
			}
		}
		
		this.writer.flush();
		this.reader.close();
		this.writer.close();
		
	}
	
	
	public void processFileForMapEvents() throws IOException{
		this.reader = new BufferedReader(new FileReader(this.inputFile));
		this.writer = new BufferedWriter(new FileWriter(this.outputFile));
		this.startCount = 0;
		this.endCount = 0;
		this.pairCount = 0;
		this.missCount = 0;
		String line = null;
		while((line = this.reader.readLine()) != null){
			if(line.contains("MapFinish")){//to change
				this.processFinishLine(line);
				this.endCount ++;
			}else if (line.contains("MapStart")){
				this.processStartLine(line);
				this.startCount ++;
			}
		}
		
		this.writer.flush();
		this.reader.close();
		this.writer.close();
		
	}
	
	
	public void processFileForPullEvents() throws IOException{
		this.reader = new BufferedReader(new FileReader(this.inputFile));
		this.writer = new BufferedWriter(new FileWriter(this.outputFile));
		this.startCount = 0;
		this.endCount = 0;
		this.pairCount = 0;
		this.missCount = 0;
		String line = null;
		while((line = this.reader.readLine()) != null){
			if(line.contains("PullFinish")){//to change
				this.processFinishLine(line);
				this.endCount ++;
			}else if (line.contains("PullStart")){
				this.processStartLine(line);
				this.startCount ++;
			}
		}
		
		this.writer.flush();
		this.reader.close();
		this.writer.close();
		
	}


	
	public void processStartLine(String line){
		HadoopEvent e = new HadoopEvent();
		e.setAttributesBySelectedContent(line);
		String key = "" + e.getJobId() + "," + e.getTaskId() + "," + e.getAttemptID(); 
		this.startIndex.put(key, e);
	}
	public void processFinishLine(String line) throws IOException{
		HadoopEvent e = new HadoopEvent();
		e.setAttributesBySelectedContent(line);
		String key = "" + e.getJobId() + "," + e.getTaskId() + "," + e.getAttemptID(); 
		HadoopEvent startEvent = this.startIndex.get(key);
		if(startEvent != null){
			this.pairCount ++;
			HadoopEvent period = new HadoopEvent();
			period.setAttributesBySelectedContent(line);
			period.setEventType(e.getEventType().replaceAll("Finish", "Period"));
			period.setValue((int)(e.getTimestamp() - startEvent.getTimestamp()));
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			//System.out.println("Start:" + startEvent.toString());
			//System.out.println("end:" + e.toString());
			System.out.println(period.toString());
			this.writer.append(period.toString());
			this.writer.flush();

		}else{
			//System.out.println("no pair found");
			this.missCount ++;
		}

	}
	
	public void printStats(){
		System.out.println("Start event:" + this.startCount);
		System.out.println("End event:" + this.endCount);
		System.out.println("Pair:" + this.pairCount);
		System.out.println("Miss:" + this.missCount);
	}
	
	
	public static void main(String args[]) throws IOException{
		
		/* 2013
		String inputFile = "F:\\Copy\\Data\\2013\\hadoop\\other\\allpullpairs.txt";
		String outputFile = "F:\\Copy\\Data\\2013\\hadoop\\other\\pullperiod.txt";
		
		inputFile = "F:\\Copy\\Data\\2013\\hadoop\\other\\allmapreducepairs.txt";
		outputFile = "F:\\Copy\\Data\\2013\\hadoop\\other\\mapperiod.txt";

		
		inputFile = "F:\\Copy\\Data\\2013\\hadoop\\other\\allmapreducepairs.txt";
		outputFile = "F:\\Copy\\Data\\2013\\hadoop\\other\\reduceperiod.txt";
		*/
		
		
		//2015
		
		//pull
		String pullInputFile = "/Users/haopeng/Copy/Data/2015/hadoop/pairs2/pullpairs.txt";
		String pullOutputFile = "/Users/haopeng/Copy/Data/2015/hadoop/event2/period/pullperiod.txt";
		
		if (args.length >= 5) {
			pullInputFile = args[0];
			pullOutputFile = args[1];
		}
		
		PeriodEventGenerator pullGenerator = new PeriodEventGenerator(pullInputFile, pullOutputFile);
		pullGenerator.processFileForPullEvents();
		pullGenerator.printStats();
		
		//map
		String mapInputFile = "/Users/haopeng/Copy/Data/2015/hadoop/pairs2/mapreducepairs.txt";
		String mapOutputFile = "/Users/haopeng/Copy/Data/2015/hadoop/event2/period/mapperiod.txt";
		
		if (args.length >= 5) {
			mapInputFile = args[2];
			mapOutputFile = args[3];
		}
		
		PeriodEventGenerator mapGenerator = new PeriodEventGenerator(mapInputFile, mapOutputFile);
		mapGenerator.processFileForMapEvents();
		mapGenerator.printStats();
		
		//reduce
		String reduceInputFile = "/Users/haopeng/Copy/Data/2015/hadoop/pairs2/mapreducepairs.txt";
		String reduceOutputFile = "/Users/haopeng/Copy/Data/2015/hadoop/event2/period/reduceperiod.txt";
		
		if (args.length >= 5) {
			reduceInputFile = args[2];
			reduceOutputFile = args[4];
		}
		
		PeriodEventGenerator reduceGenerator = new PeriodEventGenerator(reduceInputFile, reduceOutputFile);
		reduceGenerator.processFileForReduceEvents();
		reduceGenerator.printStats();

	}
	
	

}
