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
import java.util.ArrayList;
import java.util.HashMap;

import edu.umass.cs.sase.stream.HadoopEvent;




/**
 * This class is used to get aggregations from results
 * @author haopeng
 *
 */


public class ResultAggregator {
	JobCategorizor categorizor;
	HashMap<String, String> jobIdType;
	
	String inputFile;
	BufferedReader reader;
	BufferedWriter writer;
	String attributeName;
	
	int count;
	
	long startTimestamp;
	
	boolean countNeeded;
	boolean sumNeeded;
	boolean avgNeeded;
	boolean maxNeeded;
	boolean minNeeded;
	
	long jobId;
	String jobCategory;
	
	int numOfEventsToSkipBeginning;
	int numOfEventsToSkipEnd;
	public ResultAggregator(String inputFile, String attributeName, boolean count, boolean sum, boolean avg, boolean max, boolean min, int begin, int end){
		this.inputFile = inputFile;
		this.attributeName = attributeName;
		this.countNeeded = count;
		this.sumNeeded = sum;
		this.avgNeeded = avg;
		this.maxNeeded = max;
		this.minNeeded = min;
		this.numOfEventsToSkipBeginning = begin;
		this.numOfEventsToSkipEnd = end;
		
		this.count = 0;
		
		this.categorizor = new JobCategorizor();
		this.jobIdType = categorizor.getJobTypeIndex();
	}
	
	public void processFile() throws IOException{
		this.reader = new BufferedReader(new FileReader(this.inputFile));
		this.writer = new BufferedWriter(new FileWriter(this.inputFile + ".eachevent"));
		System.out.println("File to process:" + this.inputFile);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		StringBuffer sb = new StringBuffer();
		sb.append("No.\tCount\tSum\tAvg\tMax\tMin\tJobId\tJobType\n");
		//System.out.println("No.\tCount\tSum\tAvg\tMax\tMin");
		
		String outputString = sb.toString();
		System.out.print(outputString);
		this.writer.write(outputString);
		this.writer.flush();
		System.out.println("************************************************");

		
		String line = null;
		while((line = this.reader.readLine()) != null ){
			if(line.startsWith("This match has selected the following events:")){
				this.count ++;
				this.processMatch();
			}
		}
		
		this.reader.close();
		this.writer.close();
		
		
	}
	
	
	public void processMatchEachEvent() throws IOException{
		ArrayList<HadoopEvent> events = new ArrayList<HadoopEvent>();
		BufferedWriter w = new BufferedWriter(new FileWriter(this.inputFile + this.count +".eachevent.txt"));
		
		w.flush();
		int c = 0;
		long sum = 0L;
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		int value;
		String line = this.reader.readLine();
		//line = this.reader.readLine();
		while(!line.startsWith("----------Here is")  && !line.startsWith("Profiling results for repeat No.1 are as follows")){
			if(line.startsWith("EventType=")){
				HadoopEvent e = new HadoopEvent();
				e.setAttributesByLine(line);
				
				events.add(e);
							}
			line = this.reader.readLine();
		}
		for(int i = this.numOfEventsToSkipBeginning; i < events.size() - this.numOfEventsToSkipEnd; i ++){
			HadoopEvent e = events.get(i);
			value = e.getAttributeByName(this.attributeName);
			c ++;
			sum += value;
			if(value > max){
				max = value;
			}
			if(value < min){
				min = value;
				if(value == 0){
					System.out.print("");
				}
			}
			
			
			
			
			if(i == this.numOfEventsToSkipBeginning){
				this.jobId = e.getJobId();
				this.jobCategory = this.jobIdType.get("" + this.jobId);
				this.startTimestamp = e.getTimestamp();
				
				w.write("timeSinceStart\tCurrentValue\tSumTillNow\t\t");
				w.write("jobId=" + this.jobId + "\tJobCategory=" + this.jobCategory +"\n");
			}
			if(i > this.numOfEventsToSkipBeginning){
				//output the time since start, current value, sum till now
				StringBuffer eventStringBuffer = new StringBuffer();
				eventStringBuffer.append(e.getTimestamp() - this.startTimestamp + "\t");
				eventStringBuffer.append(value + "\t");
				eventStringBuffer.append(sum + "\n");
				String recordString = eventStringBuffer.toString();
				
				
				
				w.write(recordString);
				w.flush();
				
				System.out.print(recordString);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~summmary~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		sb.append(this.count + "\t");
		sb.append(c + "\t");
		sb.append(sum + "\t");
		sb.append(sum / c + "\t");
		sb.append(max + "\t");
		sb.append(min + "\t");
		sb.append(this.jobId + "\t");
		sb.append(this.jobCategory + "\t");
		sb.append("\n");
		
		String outputString = sb.toString();
		System.out.print(outputString);
		this.writer.write(outputString);
		this.writer.flush();
		
		w.close();
	}
	
	
	public void processFileEachEvent() throws IOException{
		this.reader = new BufferedReader(new FileReader(this.inputFile));
		this.writer = new BufferedWriter(new FileWriter(this.inputFile + ".aggregation2"));
		System.out.println("File to process:" + this.inputFile);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		StringBuffer sb = new StringBuffer();
		sb.append("No.\tCount\tSum\tAvg\tMax\tMin\tJobId\tJobType\n");
		//System.out.println("No.\tCount\tSum\tAvg\tMax\tMin");
		
		String outputString = sb.toString();
		System.out.print(outputString);
		this.writer.write(outputString);
		this.writer.flush();
		System.out.println("************************************************");

		
		String line = null;
		while((line = this.reader.readLine()) != null ){
			if(line.startsWith("This match has selected the following events:")){
				this.count ++;
				this.processMatchEachEvent();
			}
		}
		
		this.reader.close();
		this.writer.close();
		
		
	}

	public void processMatch() throws IOException{
		ArrayList<HadoopEvent> events = new ArrayList<HadoopEvent>();
		int c = 0;
		long sum = 0L;
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		int value;
		String line = this.reader.readLine();
		//line = this.reader.readLine();
		while(!line.startsWith("----------Here is")  && !line.startsWith("Profiling results for repeat No.1 are as follows")){
			if(line.startsWith("EventType=")){
				HadoopEvent e = new HadoopEvent();
				e.setAttributesByLine(line);
				
				events.add(e);
							}
			
			line = this.reader.readLine();
		}
		
		for(int i = this.numOfEventsToSkipBeginning; i < events.size() - this.numOfEventsToSkipEnd; i ++){
			HadoopEvent e = events.get(i);
			value = e.getAttributeByName(this.attributeName);
			c ++;
			sum += value;
			if(value > max){
				max = value;
			}
			if(value < min){
				min = value;
				if(value == 0){
					System.out.print("");
				}
			}
			if(i == this.numOfEventsToSkipBeginning){
				this.jobId = e.getJobId();
				this.jobCategory = this.jobIdType.get("" + this.jobId);
				
			}

		}
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(this.count + "\t");
		sb.append(c + "\t");
		sb.append(sum + "\t");
		sb.append(sum / c + "\t");
		sb.append(max + "\t");
		sb.append(min + "\t");
		sb.append(this.jobId + "\t");
		sb.append(this.jobCategory + "\t");
		sb.append("\n");
		
		String outputString = sb.toString();
		System.out.print(outputString);
		this.writer.write(outputString);
		this.writer.flush();
	}
	
	/**
	 * This method is used to extract events of matches to CSV files
	 * @throws IOException
	 */
	public void extractEventsToCSV() throws IOException{
		this.reader = new BufferedReader(new FileReader(this.inputFile));
		//this.writer = new BufferedWriter(new FileWriter(this.inputFile + ".aggregation2"));
		System.out.println("File to process:" + this.inputFile);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		String line = null;
		while((line = this.reader.readLine()) != null ){
			if(line.startsWith("This match has selected the following events:")){
				this.count ++;
				this.extractMatch();
			}
		}
		this.reader.close();
	}
	public void extractMatch() throws IOException{
		ArrayList<HadoopEvent> events = new ArrayList<HadoopEvent>();
		BufferedWriter w = new BufferedWriter(new FileWriter(this.inputFile +"-Match"+ this.count +".csv"));
		w.flush();
		int c = 0;
		String line = this.reader.readLine();
		//line = this.reader.readLine();
		while(!line.startsWith("----------Here is")  && !line.startsWith("Profiling results for repeat No.1 are as follows")){
			if(line.startsWith("EventType=")){
				HadoopEvent e = new HadoopEvent();
				e.setAttributesByLine(line);
				events.add(e);
			}
			line = this.reader.readLine();
		}
		w.write("No.,EventType,Id,jobId,taskId,attemptId,nodeNumber,timestamp,lowerBound,upperBound,originalEventId,value\n");
		for(int i = 0; i < events.size(); i ++){
			HadoopEvent e = events.get(i);
			c ++;
			w.write(c + "," + e.toCSV() + "\n");
			w.flush();
		}
		w.close();
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String resultFile = "F:\\Copy\\Data\\2013\\result\\2.reduceperiod.txt";
		
		
		resultFile = "F:\\Copy\\Data\\2013\\result\\3.datapull.txt";
		resultFile = "F:\\Copy\\Data\\2013\\result\\4.dataactivity.txt";
		
		
		resultFile = "F:\\Copy\\Data\\2013\\result\\1.mapperiod.txt";
		
		resultFile = "F:\\Copy\\Data\\2013\\result\\2.reduceperiod.txt";
		
		resultFile = "F:\\Copy\\Data\\2013\\result\\4.dataactivity.txt";
		
		resultFile = "F:\\Copy\\Data\\2013\\result\\1.mapperiod.txt";
		
		
		
		resultFile = "F:\\Copy\\Data\\2013\\result\\4.part_of_all_matches.txt";
		resultFile = "F:\\Copy\\Data\\2013\\result\\4.nopredicatehadoopui.txt";
		
		
		
		resultFile = "F:\\Copy\\Data\\2013\\result\\Q4\\wcs-1.txt";
		resultFile = "H:\\Copy\\Data\\2013\\result\\4.morematches.hadoopui.txt";
		String attributeName = "value";
		boolean sum = true;
		boolean count = true;
		boolean avg = true;
		boolean max = true;
		boolean min = true;
		
		int beginSkip = 0;
		int endSkip = 1;
		
		ResultAggregator agg = new ResultAggregator(resultFile, attributeName, count, sum, avg, max, min, beginSkip, endSkip);
		//agg.processFile();
		
		//compute aggregations for each event
		//agg.processFileEachEvent();
		
		//extract events for matches to csv files
		agg.extractEventsToCSV();
		
		
		
		
		
		
		

	}

}
