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
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

import edu.umass.cs.sase.stream.Event;
import edu.umass.cs.sase.stream.HadoopEvent;

public class HadoopJobParser {
	static BufferedWriter  bw; 

	static int startCount, finishCount;
	 
	static int nodeNum;//none
	static long jobId; //key
	static int taskId;//none
	static int attemptId;//none
	static int start = 0;//
	static int finish = 0;
	static long timestamp;//yes
	static String eventType;//JobStart/JobFinish
	static String temp;
	
	

	public static void parseFile(String fileLoc) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(fileLoc));
		String line = br.readLine();
		while(line != null){
			if(line.contains("JobTracker: Initializing job") ){
				parseJobStartLine(line);
				startCount ++;
			}else if(line.contains("has completed successfully")){
				parseJobFinishLine(line);
				finishCount ++;
			}
			line = br.readLine();
		}
		System.out.println("startcount = "+ startCount	);
		System.out.println("finishcount = " + finishCount	);
		br.close();
		
	}
	public static void parseJobStartLine(String line) throws IOException{
		/*
		2013-07-12 21:01:07,224 INFO org.apache.hadoop.mapred.JobTracker: Initializing job_201307111504_0028
		*/
		System.out.println(line);
		eventType = "JobStart";
		String time = line.substring(0, 23);
		//System.out.println("time=" + time);
		timestamp = DateTimeToTimestamp.dateToTs(time);
		//System.out.println(timestamp);
		StringTokenizer st = new StringTokenizer(line);
		for(int i = 0; i < 5; i ++ ){
			st.nextToken();
		}
		temp = st.nextToken();
		jobId = Long.parseLong(temp.substring(4).replaceAll("_", ""));//has to keep it in 10 digits due to the limitation of Integer
		Event e = new HadoopEvent(0,eventType, jobId, 0,  0, 0, timestamp, 0, 0, 0, 0);
		//HadoopEvent(int Id,String eventType, int jobId, int taskId, int attemptId, int nodeNumber, int timestamp, int upperBound, int lowerBound, int originalEventId){
		System.out.println(e.toStringSelectedContentOnly());
		
		bw.append(e.toString());
		bw.flush();
	}
	public static void parseJobFinishLine(String line) throws IOException{
		//2013-07-08 21:48:49,472 INFO org.apache.hadoop.mapred.JobInProgress: Job job_201307081910_0004 has completed successfully.
		System.out.println(line);
		eventType = "JobFinish";
		String time = line.substring(0, 23);
		//System.out.println("time" + time);
		timestamp = DateTimeToTimestamp.dateToTs(time);
		//System.out.println(timestamp);
		StringTokenizer st = new StringTokenizer(line);
		for(int i = 0; i < 5; i ++ ){
			st.nextToken();
		}
		temp = st.nextToken();
		jobId = Long.parseLong(temp.substring(4).replaceAll("_", ""));//has to keep it in 10 digits due to the limitation of Integer
		//System.out.println(temp);
		
		Event e = new HadoopEvent(0,eventType, jobId,0,  0, 0, timestamp, 0, 0, 0, 0);
		System.out.println(e);
		
		bw.append(e.toString());
		bw.flush();

	}
	
	public  static void parseDirectory(String directory) throws IOException{
		File mainDirectory = new File(directory);
		File[] subDirectory = mainDirectory.listFiles();
		
		for(int i = 0; i < subDirectory.length; i ++){
			String sub = subDirectory[i].getName();
			//if (sub.contains("hadoop-boduo-jobtracker-obelix8.local.log")){//2013
			if (sub.contains("boduo-jobtracker-compute-0-28.yeeha.log")){//2015
				
				System.out.println(sub);
				parseFile(subDirectory[i].getAbsolutePath());
			}
		}
		
		
	}
	
	
public static void main(String args[]) throws IOException{
		
		/*start
		2013-07-12 21:01:07,224 INFO org.apache.hadoop.mapred.JobTracker:
		Initializing job_201307111504_0028
		...
		finish
		2013-07-12 23:04:55,851 INFO org.apache.hadoop.mapred.JobInProgress:
		Job job_201307111504_0028 has completed successfully. 
		*/
			
		/**
		 * It's in JobTracker logs on Obelix8 /home/hadoop/hadoop-scalla/logs,
		named as "hadoop-boduo-jobtracker-obelix8.local.*" (one file per day).
	
		 */
	
		/*
		String testStartLine = "2013-07-08 19:11:19,832 INFO org.apache.hadoop.mapred.JobTracker: Initializing job_201307081910_0001";
		System.out.println(testStartLine);
		HadoopJobParser.parseJobStartLine(testStartLine);
		
		System.out.println("````````````````````````````````");
		String testFinishLine = "2013-07-08 21:48:49,472 INFO org.apache.hadoop.mapred.JobInProgress: Job job_201307081910_0004 has completed successfully.";
		System.out.println(testFinishLine);
		HadoopJobParser.parseJobFinishLine(testFinishLine);
		*/
		
		/*
		String testInputFile = "f:\\copy\\data\\2013\\hadoop\\log\\jtnn\\hadoop-boduo-jobtracker-obelix8.local.log.2013-07-08";
		HadoopJobParser.parseFile(testInputFile);
		*/
	
		/*
		String testDirectory = "f:\\copy\\data\\2013\\hadoop\\log\\jtnn";
		HadoopJobParser.parseDirectory(testDirectory);
		*/
	
	
		/* 2013
		String inputDirectory = "f:\\copy\\Data\\2013\\hadoop\\log\\jtnn";
		String outputFile= "f:\\copy\\Data\\2013\\hadoop\\event2\\job\\job.txt";
		System.out.println("input directory is " + inputDirectory);
		System.out.println("output file is " + outputFile);
		startCount = 0;
		finishCount = 0;
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
		parseDirectory(inputDirectory);
		bw.flush();
		bw.close();
		*/
	
	
		String inputDirectory = "/Users/haopeng/Copy/Data/2015/hadoop/head/hadoop-system-logs/compute-0-28.yeeha";
		String outputFile= "/Users/haopeng/Copy/Data/2015/hadoop/event2/job/job.txt";
		System.out.println("input directory is " + inputDirectory);
		System.out.println("output file is " + outputFile);
		startCount = 0;
		finishCount = 0;
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
		parseDirectory(inputDirectory);
		bw.flush();
		bw.close();
	
	}



}
