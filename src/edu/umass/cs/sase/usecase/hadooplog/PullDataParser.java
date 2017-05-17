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
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

import edu.umass.cs.sase.stream.HadoopEvent;
import edu.umass.cs.sase.stream.Event;
import edu.umass.cs.sase.util.FileOperator;


/**
 * @author haopeng
 *
 */
public class PullDataParser {
	static BufferedWriter  bw; 

	static int startCount, finishCount;
	 
	static int nodeNum;
	static long jobId;
	static int taskId;
	static int attemptId;
	static int start = 0;
	static int finish = 0;
	static long timestamp;
	static long numberOfSegments;
	static String eventType;
	static String temp;
	
	
	public static void parseFile(String fileLoc) throws IOException{
		if (!FileOperator.checkFileExist(fileLoc)) {
			return;
		}
		
		BufferedReader br = new BufferedReader(new FileReader(fileLoc));
		String line = br.readLine();
		while(line != null){
			if(line.contains("Sent out") ){
				parsePullStartLine(line);
				startCount ++;
			}else if(line.contains("ReduceTask: header:")){
				parsePullFinishLine(line);
				finishCount ++;
			}
			line = br.readLine();
		}
		System.out.println("startcount = "+ startCount	);
		System.out.println("finishcount = " + finishCount	);
		br.close();
		
	}
	public static void parsePullStartLine(String line) throws IOException{
		
		System.out.println(line);
		eventType = "PullStart";
		String time = line.substring(0, 23);
		//System.out.println("time" + time);
		timestamp = DateTimeToTimestamp.dateToTs(time);
		System.out.println(timestamp);
		StringTokenizer st = new StringTokenizer(line);
		for(int i = 0; i < 13; i ++ ){
			st.nextToken();
		}
		temp = st.nextToken();
		//attempt_201002191511_0001_m_000037_0
		jobId = Long.parseLong(temp.substring(8,25).replaceAll("_", ""));//has to keep it in 10 digits due to the limitation of Integer
		taskId = Integer.parseInt(temp.substring(28, 34));
		System.out.println(taskId);
		attemptId = Integer.parseInt(temp.substring(35));
		System.out.println(attemptId);
		System.out.println(temp);
		
		
		Event e = new HadoopEvent(0,eventType, jobId, taskId,  attemptId, nodeNum, timestamp, 0, 0, 0, 0);
		System.out.println(e);
		bw.append(e.toString());
		bw.flush();
	}
	public static void parsePullFinishLine(String line) throws IOException{

		System.out.println(line);
		eventType = "PullFinish";
		String time = line.substring(0, 23);
		//System.out.println("time" + time);
		timestamp = DateTimeToTimestamp.dateToTs(time);
		System.out.println(timestamp);
		StringTokenizer st = new StringTokenizer(line);
		for(int i = 0; i < 5; i ++ ){
			st.nextToken();
		}
		temp = st.nextToken();
		//attempt_201002191511_0001_m_000037_0
		jobId = Long.parseLong(temp.substring(8,25).replaceAll("_", ""));//has to keep it in 10 digits due to the limitation of Integer
		taskId = Integer.parseInt(temp.substring(28, 34));
		System.out.println(taskId);
		attemptId = Integer.parseInt(temp.substring(35,36));
		System.out.println(attemptId);
		System.out.println(temp);
		
		
		Event e = new HadoopEvent(0,eventType, jobId,taskId,  attemptId, nodeNum, timestamp, 0, 0, 0, 0);
		System.out.println(e);
		bw.append(e.toString());
		bw.flush();

	}
	
	public  static void parseDirectory(String directory) throws IOException{
		File mainDirectory = new File(directory);
		File[] subDirectory = mainDirectory.listFiles();


		for(int i = 0; i < subDirectory.length; i ++){
			String sub = subDirectory[i].getName();
			if (sub.contains("tasktracker")){
				System.out.println(sub);
				
				
				
				parseFile(subDirectory[i].getAbsolutePath());
			}
		}
		
		
	}
	
	public  static void parseReducerDirectory(String directory) throws IOException{
		if (!FileOperator.checkFileExist(directory)) {
			return;
		}
		
		
		File mainDirectory = new File(directory);
		File[] subDirectory = mainDirectory.listFiles();
		
		

		for(int i = 0; i < subDirectory.length; i ++){
			String sub = subDirectory[i].getName();
			if (sub.contains(".DS_Store")) {
				continue;
			}
			if (sub.contains("r")){
				
				//parseFile(subDirectory[i].getAbsolutePath()+"\\syslog");//windows
				
				parseFile(subDirectory[i].getAbsolutePath()+"/syslog");//mac
			}
		}
		
		
	}
	
	
public static void main(String args[]) throws IOException{
		
		/*
		 Data Pull:
			You mentioned there are 2 lines per pull in the TT logs:
			2010-02-19 15:15:21,296 INFO org.apache.hadoop.mapred.TaskTracker: Sent out 2518507 bytes for reduce: 13 from map: attempt_201002191511_0001_m_000005_0 given 2518507/2518503
			2010-02-19 15:15:21,296 INFO org.apache.hadoop.mapred.TaskTracker.clienttrace: src: 10.255.255.242:50060, dest: 10.255.255.244:54743, bytes: 2518507, op: MAPRED_SHUFFLE, cliID: attempt_201002191511_0001_m_000005_0
			
			And 4 lines per pull in the reducer task logs:
			2010-02-19 15:15:26,257 INFO org.apache.hadoop.mapred.ReduceTask: header: attempt_201002191511_0001_m_000037_0, compressed len: 2626321, decompressed len: 2626317
			2010-02-19 15:15:26,257 INFO org.apache.hadoop.mapred.ReduceTask: Shuffling 2626317 bytes (2626321 raw bytes) into RAM from attempt_201002191511_0001_m_000037_0
			2010-02-19 15:15:26,330 INFO org.apache.hadoop.mapred.ReduceTask: Read 2626317 bytes from map-output for attempt_201002191511_0001_m_000037_0
			2010-02-19 15:15:26,330 INFO org.apache.hadoop.mapred.ReduceTask: Rec #1 from attempt_201002191511_0001_m_000037_0 -> (12, 98) from compute-0-17.local
		 */
		
		/* 2013
		String inputDirectoryPattern = "f:\\copy\\Data\\2013\\hadoop\\log\\boduo-obelix%%.local\\boduolog\\logs";
		String outputFilePattern = "f:\\copy\\Data\\2013\\hadoop\\event2\\pulldata\\obelix%%pullstart.txt";
		
		String reducerInputDirectoryPattern = "f:\\copy\\Data\\2013\\hadoop\\log\\boduo-obelix%%.local\\boduolog\\logs\\userlogs";
		String reducerOutputFilePattern = "f:\\copy\\Data\\2013\\hadoop\\event2\\pulldata\\obelix%%pullfinish.txt";
		
		for(int nodeNumber = 105; nodeNumber < 115; nodeNumber ++){
			String inputDirectory = inputDirectoryPattern.replace("%%", nodeNumber + "");
			String outputFile = outputFilePattern.replaceAll("%%", nodeNumber + "");
			nodeNum = nodeNumber;
			System.out.println("input directory is " + inputDirectory);
			System.out.println("output file is " + outputFile);
			
			
			startCount = 0;
			finishCount = 0;
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			parseDirectory(inputDirectory);
			bw.flush();
			bw.close();
			
			String reducerInputDirectory = reducerInputDirectoryPattern.replace("%%", nodeNumber + "");
			String reducerOutputFile = reducerOutputFilePattern.replace("%%", nodeNumber + "");
			System.out.println("reducer input directory is " + reducerInputDirectory);
			System.out.println("reducer output file is " + reducerOutputFile);
			
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reducerOutputFile)));
			parseReducerDirectory(reducerInputDirectory);
			bw.flush();
			bw.close();
			
		}
		*/
	
		//2015
		String inputDirectoryPattern = "/Users/haopeng/Copy/Data/20150423/hadoop/head/hadoop-system-logs/compute-1-%%.yeeha";
		String outputFilePattern = "/Users/haopeng/Copy/Data/20150423/hadoop/event/pulldata/compute-1-%%-pullstart.txt";
		
		if (args.length > 3) {
			inputDirectoryPattern = args[0];
			outputFilePattern = args[1];
		}
		
		//Pull start
		for(int nodeNumber = 0; nodeNumber <= 9; nodeNumber ++){
			String inputDirectory = inputDirectoryPattern.replace("%%", nodeNumber + "");
			String outputFile = outputFilePattern.replaceAll("%%", nodeNumber + "");
			nodeNum = nodeNumber;
			System.out.println("input directory is " + inputDirectory);
			System.out.println("output file is " + outputFile);
			
			startCount = 0;
			finishCount = 0;
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			parseDirectory(inputDirectory);
			bw.flush();
			bw.close();
		}
		
		
		//Pull finish
		String inputDirectoryPath = "/Users/haopeng/Copy/Data/20150423/hadoop/slave/hadoop-user-logs";
		String outputDirectoryPath = "/Users/haopeng/Copy/Data/20150423/hadoop/event/pulldata/";
		
		if (args.length > 3) {
			inputDirectoryPath = args[2];
			outputDirectoryPath = args[3];
		}
		
		//for each sub-folder
		//for each node
		
		//modify this part
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
				String outputPath = outputDirectoryPath + subDir.getName() + "/" + HadoopNodeNames.nodePrefix + node + HadoopNodeNames.nodeSuffix +"pullfinish.txt";
				
				FileOperator.makeFolderForFile(outputPath);
				
				System.out.println("reducer input directory is " + inputPath);
				System.out.println("reducer output file is " + outputPath);
				
				bw = new BufferedWriter(new FileWriter(outputPath));

				parseReducerDirectory(inputPath);
				bw.flush();
				bw.close();
				
			}
		}
		
		
		//////////
		/*
		String reducerInputDirectoryPattern = "f:\\copy\\Data\\2013\\hadoop\\log\\boduo-obelix%%.local\\boduolog\\logs\\userlogs";
		String reducerOutputFilePattern = "f:\\copy\\Data\\2013\\hadoop\\event2\\pulldata\\obelix%%pullfinish.txt";
		
		
		//reducer
		for(int nodeNumber = 105; nodeNumber < 115; nodeNumber ++){
			String reducerInputDirectory = reducerInputDirectoryPattern.replace("%%", nodeNumber + "");
			String reducerOutputFile = reducerOutputFilePattern.replace("%%", nodeNumber + "");
			System.out.println("reducer input directory is " + reducerInputDirectory);
			System.out.println("reducer output file is " + reducerOutputFile);
			
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reducerOutputFile)));
			parseReducerDirectory(reducerInputDirectory);
			bw.flush();
			bw.close();
		}
				
		*/
		
		
		/*
		String fileLoc = "D:\\haopeng-03\\logs";
		startCount = 0;
		finishCount = 0;
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("d:\\parsed\\hadoop-datapullstart.txt")));
		//parseFile(fileLoc);
		//String line = "MapAttempt TASK_TYPE=\"MAP\" TASKID=\"task_201002191511_0001_m_000003\" TASK_ATTEMPT_ID=\"attempt_201002191511_0001_m_000003_0\" TASK_STATUS=\"SUCCESS\" FINISH_TIME=\"1266610500304\" HOSTNAME=\"/default-rack/compute-0-15\\.local\" STATE_STRING=\"\" ";
		//String line2 = "MapAttempt TASK_TYPE=\"MAP\" TASKID=\"task_201002191511_0001_m_000003\" TASK_ATTEMPT_ID=\"attempt_201002191511_0001_m_000003_0\" START_TIME=\"1266610479165\" TRACKER_NAME=\"tracker_compute-0-15\\.local:localhost\\.localdomain/127\\.0\\.0\\.1:44048\" HTTP_PORT=\"50060\" .";
		parseDirectory(fileLoc);
		bw.flush();
		bw.close();
		*/
		
	}
	
}
