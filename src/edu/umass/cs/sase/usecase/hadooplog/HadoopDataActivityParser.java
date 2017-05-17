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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import edu.umass.cs.sase.stream.Event;
import edu.umass.cs.sase.stream.HadoopEvent;
import edu.umass.cs.sase.util.FileOperator;

public class HadoopDataActivityParser {
	static BufferedWriter  bw; 

	static int startCount, finishCount;
	 
	static int nodeNum;//none
	static long jobId; //key
	static int taskId;//none
	static int attemptId;//none
	static int start = 0;//
	static int finish = 0;
	static long timestamp;//yes
	static int value;
	static String eventType;//JobStart/JobFinish
	static String temp;
	/*
	 map
	2013-07-12 10:38:56,222 INFO org.apache.hadoop.mapred.MapTask: [SPILL START] Map: start spill, file /home/hadoop/hadoop-scalla/local/mapred/local/taskTracker/jobcache/job_201307111504_0019/attempt_201307111504_0019_m_000016_0/output/spill0.out, for partition 0
	2013-07-12 10:38:56,778 INFO org.apache.hadoop.mapred.MapTask: [SPILL COMPLETE] Map: finish spill, file /home/hadoop/hadoop-scalla/local/mapred/local/taskTracker/jobcache/job_201307111504_0019/attempt_201307111504_0019_m_000016_0/output/spill0.out, for partition 0, Bytes: 116552
	2013-07-12 10:38:56,779 INFO org.apache.hadoop.mapred.MapTask: [SPILL START] Map: start spill, file /home/hadoop/hadoop-scalla/local/mapred/local/taskTracker/jobcache/job_201307111504_0019/attempt_201307111504_0019_m_000016_0/output/spill0.out, for partition 1
	2013-07-12 10:38:56,848 INFO org.apache.hadoop.mapred.MapTask: [SPILL COMPLETE] Map: finish spill, file /home/hadoop/hadoop-scalla/local/mapred/local/taskTracker/jobcache/job_201307111504_0019/attempt_201307111504_0019_m_000016_0/output/spill0.out, for partition 1, Bytes: 117000
	2013-07-12 10:38:56,849 INFO org.apache.hadoop.mapred.MapTask: [SPILL START] Map: start spill, file /home/hadoop/hadoop-scalla/local/mapred/local/taskTracker/jobcache/job_201307111504_0019/attempt_201307111504_0019_m_000016_0/output/spill0.out, for partition 2
	
	reduce
	2013-07-12 22:17:50,335 INFO org.apache.hadoop.mapred.ReduceTask: Read 74720 bytes from map-output for attempt_201307111504_0029_m_003756_0
	 */
	
	
	

	public static void parseMapLine(String line) throws IOException{
		//nodeNum = 15;
		/*
		2013-07-12 10:38:56,778 INFO org.apache.hadoop.mapred.MapTask: [SPILL COMPLETE] Map: finish spill, file /home/hadoop/hadoop-scalla/local/mapred/local/taskTracker/jobcache/job_201307111504_0019/attempt_201307111504_0019_m_000016_0/output/spill0.out, for partition 0, Bytes: 116552
		*/
		System.out.println(line);
		eventType = "HadoopDataActivity";
		String time = line.substring(0, 23);
		timestamp = DateTimeToTimestamp.dateToTs(time);
		StringTokenizer st = new StringTokenizer(line);
		for(int i = 0; i < 10; i ++ ){
			st.nextToken();
		}
		temp = st.nextToken();
		System.out.println(temp);
		///state/partition1/hadoop/scalla-dev/local/mapred/local/taskTracker/jobcache/job_201503142229_0006/attempt_201503142229_0006_m_000025_0/output/spill0.out
		//jobId = Integer.parseInt(temp.substring(77,88).replaceAll("_", ""));//has to keep it in 10 digits due to the limitation of Integer // old version, on obelix
		
		jobId = Long.parseLong(temp.substring(80,97).replaceAll("_", ""));//has to keep it in 10 digits due to the limitation of Integer // new version, on yeeha

		//taskId = Integer.parseInt(temp.substring(118,123));//2013
		taskId = Integer.parseInt(temp.substring(127,132));
		attemptId = Integer.parseInt(temp.substring(133,134));
		
		for(int i = 0; i < 4; i ++ ){
			st.nextToken();
		}
		temp = st.nextToken();
		System.out.println(temp);
		value = Integer.parseInt(temp);
		
		Event e = new HadoopEvent(0,eventType, jobId, taskId,  attemptId, nodeNum, timestamp, 0, 0, 0, value);
		//HadoopEvent(int Id,String eventType, int jobId, int taskId, int attemptId, int nodeNumber, int timestamp, int upperBound, int lowerBound, int originalEventId){
		System.out.println(e.toString());
		
		bw.append(e.toString());
		bw.flush();
	}
	public static void parseReduceLine(String line) throws IOException{
		//2013-07-12 21:55:37,519 INFO org.apache.hadoop.mapred.ReduceTask: Read 107102 bytes from map-output for attempt_201307111504_0029_m_000013_0
		System.out.println(line);
		eventType = "HadoopDataActivity";
		String time = line.substring(0, 23);
		timestamp = DateTimeToTimestamp.dateToTs(time);
		StringTokenizer st = new StringTokenizer(line);

		for(int i = 0; i < 5; i ++ ){
			st.nextToken();
		}
		temp = st.nextToken();
		System.out.println(temp);
		value = Integer.parseInt(temp) * -1;

		
		for(int i = 0; i < 4; i ++ ){
			st.nextToken();
		}
		temp = st.nextToken();
		System.out.println(temp);
		jobId = Long.parseLong(temp.substring(8,25).replaceAll("_", ""));//has to keep it in 10 digits due to the limitation of Integer
		taskId = Integer.parseInt(temp.substring(28,34));
		attemptId = Integer.parseInt(temp.substring(35));
		
		
		Event e = new HadoopEvent(0,eventType, jobId, taskId,  attemptId, nodeNum, timestamp, 0, 0, 0, value);
		
		System.out.println(e.toString());
		bw.append(e.toString());
		bw.flush();

	}
	
	public static void parseFile(String fileLoc) throws IOException{
		if (!FileOperator.checkFileExist(fileLoc)) {
			System.out.println("File Not exists:" + fileLoc);
			return;
		}
		
		BufferedReader br = new BufferedReader(new FileReader(fileLoc));
		String line = br.readLine();
		while(line != null){
			if(line.contains("[SPILL COMPLETE] Map: finish spill") ){
				parseMapLine(line);
				startCount ++;
			}else if(line.contains("from map-output for")){
				parseReduceLine(line);
				finishCount ++;
			}
			line = br.readLine();
		}
		System.out.println("startcount = "+ startCount	);
		System.out.println("finishcount = " + finishCount	);
		br.close();
		
	}
	
	//todo
	//parsedirectoy
	
	public  static void parseDirectory(String directory) throws IOException{
		if (!FileOperator.checkFileExist(directory)) {
			System.out.println("Directory Not exists:" + directory);
			return;
		}
		
		File mainDirectory = new File(directory);
		File[] subDirectory = mainDirectory.listFiles();

		String temp;
		for(int i = 0; i < subDirectory.length; i ++){
			String sub = subDirectory[i].getName();
			//parseFile(subDirectory[i].getAbsolutePath()+"\\syslog");// windows
			parseFile(subDirectory[i].getAbsolutePath()+"/syslog");//mac/linux
		}
		
		
	}
	
	
	public static void main(String args[]) throws IOException{
		/*
		String testMap = "2013-07-12 10:38:56,778 INFO org.apache.hadoop.mapred.MapTask: [SPILL COMPLETE] Map: finish spill, file /home/hadoop/hadoop-scalla/local/mapred/local/taskTracker/jobcache/job_201307111504_0019/attempt_201307111504_0019_m_000016_0/output/spill0.out, for partition 0, Bytes: 116552";
		HadoopDataActivityParser.parseMapLine(testMap);
		
		String testReduce = "2013-07-12 21:55:37,519 INFO org.apache.hadoop.mapred.ReduceTask: Read 107102 bytes from map-output for attempt_201307111504_0029_m_000013_0";
		HadoopDataActivityParser.parseReduceLine(testReduce);
		
		String testFile = "F:\\Copy\\Data\\2013\\hadoop\\log\\boduo-obelix105.local\\boduolog\\logs\\userlogs\\attempt_201307111504_0007_m_001227_0\\syslog";
		testFile = "F:\\Copy\\Data\\2013\\hadoop\\log\\boduo-obelix105.local\\boduolog\\logs\\userlogs\\attempt_201307111504_0029_r_000007_0\\syslog";
		HadoopDataActivityParser.parseFile(testFile);
		*/
		
		/* for 2013 use
		for(int i = 105; i < 115; i ++){
			nodeNum = i;
			String mainDirectory = "F:\\Copy\\Data\\2013\\hadoop\\log\\boduo-obelix"+i + ".local\\boduolog\\logs\\userlogs";
			String outputFile = "f:\\Copy\\Data\\2013\\hadoop\\event2\\dataactivity\\obelix"+i +".txt";
			bw = new BufferedWriter(new FileWriter(outputFile));

			parseDirectory(mainDirectory);
			bw.flush();
			bw.close();

		}
		*/
		
		String inputDirectoryPath = "/Users/haopeng/Copy/Data/20150426/hadoop/slave/hadoop-user-logs";

		
		String outputDirectoryPath = "/Users/haopeng/Copy/Data/20150426/hadoop/event/dataactivity/";
		
		
		if (args.length > 1) {
			inputDirectoryPath = args[0];
			outputDirectoryPath = args[1];
		}
		
		File inputDirectory = new File(inputDirectoryPath);
		File[] subDirectories = inputDirectory.listFiles();
		
		System.out.println("Input:" + inputDirectoryPath);
		System.out.println("Output:" + outputDirectoryPath);
		
		for (File subDir : subDirectories) {
			if (subDir.getAbsolutePath().contains(".DS_Store")) {
				continue;
			}
			for (String node : HadoopNodeNames.slaveNodeNames) {
				String inputPath = subDir.getAbsolutePath() + "/" + HadoopNodeNames.nodePrefix + node + HadoopNodeNames.nodeSuffix;
				String outputPath = outputDirectoryPath + subDir.getName() + "/" + HadoopNodeNames.nodePrefix + node + HadoopNodeNames.nodeSuffix +".txt";
				
				FileOperator.makeFolderForFile(outputPath);
				
				bw = new BufferedWriter(new FileWriter(outputPath));

				parseDirectory(inputPath);
				bw.flush();
				bw.close();
				
			}
		}
		
		
	}


}
