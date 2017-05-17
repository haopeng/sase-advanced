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
 *MergeStart
 * AttemptEvent(int Id,String eventType=mergestart/mergefinish, ok
 *  int taskId, ok, from directory name
 *  int attemptId, ok, from directory name
 *  int nodeNumber, no
 *  int timestamp, ok, can parse
 *  int upperBound, 
 *  int lowerBound, 
 *  int originalEventId, 
 *  int numberOfSegments ok, can parse
 */
public class DataMergeParser {
	static BufferedWriter  bw; 
	static long jobId;
	static int taskId;
	static int attemptId;
	static int start = 0;
	static int finish = 0;
	static long timestamp;
	static int numberOfSegments;
	static String eventType;
	
	static int nodeNumber;
	
	
	
	public  static void parseDirectory(String directory) throws IOException{
		if (!FileOperator.checkFileExist(directory)) {
			System.out.println("Folder:" + directory + " not exists!");
			return;
		}
		
		
		File mainDirectory = new File(directory);
		
		
		
		File[] subDirectory = mainDirectory.listFiles();
		System.out.println(mainDirectory.getAbsolutePath());
		
		String temp;
		for(int i = 0; i < subDirectory.length; i ++){
			
			String sub = subDirectory[i].getName();
			if (sub.contains("r") && !sub.contains(".DS_Store")){// reduce
				System.out.println(sub);
				//attempt_201307111504_0007_m_000863_0
				//attempt_201503142229_0023_m_000042_0
				//attempt_201503142229_0006_m_000025_0
				jobId = Long.parseLong(sub.substring(8,25).replaceAll("_", ""));//has to keep it in 10 digits due to the limitation of Integer
				StringTokenizer st = new StringTokenizer(sub,"_");
				temp = st.nextToken();
				temp = st.nextToken();
				temp = st.nextToken();
				temp = st.nextToken();
				temp = st.nextToken();
				taskId = Integer.parseInt(temp);
				System.out.println(taskId + " is taskId");
				
				temp = st.nextToken();
				attemptId = Integer.parseInt(temp);
				System.out.println("taskId=" + taskId + " " + attemptId);
				//parseFile(subDirectory[i].getAbsolutePath()+"\\syslog"); // windows
				
				parseFile(subDirectory[i].getAbsolutePath()+"/syslog");
			}
		}
		
		
	}
	
	public static void parseFile(String fileLoc) throws IOException{
		System.out.println("FileLoc=" + fileLoc + "!");
		if (!FileOperator.checkFileExist(fileLoc)) {
			return;
		}
		
		BufferedReader br = new BufferedReader(new FileReader(fileLoc));
		
		String line = br.readLine();
		while(line != null){
			if(line.contains("Initiating in-memory merge") ){
				parseMergeStartLine(line);
				start ++;
			}else if(line.contains("in-memory complete")){
				parseMergeFinishLine(line);
				finish ++;
			}
			line = br.readLine();
		}
		System.out.println("start = "+ start);
		System.out.println("finish = " + finish);
		br.close();
		
	}
	public static void parseMergeStartLine(String line) throws IOException{
		System.out.println(line);
		eventType = "MergeStart";
		String time = line.substring(0, 23);
		//System.out.println("time" + time);
		timestamp = DateTimeToTimestamp.dateToTs(time);
		//System.out.println(timestamp);
		StringTokenizer st = new StringTokenizer(line);
		for(int i = 0; i < 8; i ++ ){
			st.nextToken();
		}
		numberOfSegments = Integer.parseInt(st.nextToken());
		System.out.println(numberOfSegments);
		//public AttemptEvent(int Id,String eventType, int taskId, int attemptId, int nodeNumber, int timestamp, int upperBound, int lowerBound, int originalEventId, int numberOfSegments){
		//HadoopEvent(int Id,String eventType, int jobId,int taskId, int attemptId, int nodeNumber, int timestamp, int upperBound, int lowerBound, int originalEventId, int value, int numberOfSegments){
		Event e = new HadoopEvent(0,eventType, jobId,taskId,  attemptId, nodeNumber, timestamp, 0L, 0L, 0,  0,numberOfSegments);
		System.out.println(e);
		bw.append(e.toString());
		bw.flush();
		
		
	}
	public static void parseMergeFinishLine(String line) throws IOException{
		System.out.println(line);
		eventType = "MergeFinish";
		String time = line.substring(0, 23);
		//System.out.println("time" + time);
		timestamp = DateTimeToTimestamp.dateToTs(time);
		StringTokenizer st = new StringTokenizer(line);
		for(int i = 0; i < 8; i ++ ){
			st.nextToken();
		}
		numberOfSegments = Integer.parseInt(st.nextToken());
		System.out.println(numberOfSegments);
		//     public HadoopEvent(int Id,String eventType, int jobId, int taskId, int attemptId, int nodeNumber, long timestamp, long upperBound, long lowerBound, int originalEventId, int value){
		//Event e = new HadoopEvent(0, eventType, jobId, taskId, attemptId, nodeNumber, timestamp, 0, 0, 0, numberOfSegments);
		Event e = new HadoopEvent(0,eventType, jobId, taskId,  attemptId, nodeNumber, timestamp, 0, 0, 0, 0, numberOfSegments);
		System.out.println(e);
		bw.append(e.toString());
		bw.flush();
		
		
		
	}
	public static Event parseLine(String line) throws IOException{
		String attribute;
		String attributeValue;
		HadoopEvent e = new HadoopEvent();;
		if(line.startsWith("MapAttempt")){
			if(line.contains("START_TIME")){
				e.setEventType("MapStart");
			}else if(line.contains("FINISH_TIME")){
				e.setEventType("MapFinish");
			}
		}else if(line.startsWith("ReduceAttempt")){
			if(line.contains("START_TIME")){
				e.setEventType("ReduceStart");
			}else if(line.contains("FINISH_TIME")){
				e.setEventType("ReduceFinish");
			}
		}
		if(line.contains("START_TIME")){
				//AttemptEvent e = new AttemptEvent();
				//e.setEventType("MapAttemptStart");
				System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);
				while(st.hasMoreElements()){
					attribute = st.nextToken();
					if(attribute.contains("TASKID")){
						attributeValue = attribute.substring(attribute.length() - 7, (attribute.length() -1) );
						e.setTaskId(Integer.parseInt(attributeValue));
					}else if(attribute.contains("TASK_ATTEMPT_ID")){
							attributeValue = attribute.substring(attribute.length() - 2, (attribute.length() -1) );
							e.setAttemptID(Integer.parseInt(attributeValue));
						
					}else if(attribute.contains("START_TIME")){
						attributeValue = attribute.substring(attribute.indexOf('=')+6, (attribute.length() -1) );
						e.setTimestamp(Integer.parseInt(attributeValue));
					}else if(attribute.contains("TRACKER_NAME")){
						
						attributeValue = attribute.substring(attribute.indexOf('=')+20, (attribute.indexOf('l'))-2 );
						e.setNodeNumber(Integer.parseInt(attributeValue));
						
					}
					
				}
				System.out.println(e.toString());
				
				bw.append(e.toString());
				bw.flush();
				
			}else if (line.contains("FINISH_TIME")){
				//AttemptEvent e = new AttemptEvent();
				//e.setEventType("MapAttemptFinish");
				System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);
				while(st.hasMoreElements()){
					attribute = st.nextToken();
					if(attribute.contains("TASKID")){
						attributeValue = attribute.substring(attribute.length() - 7, (attribute.length() -1) );
						e.setTaskId(Integer.parseInt(attributeValue));
					}else if(attribute.contains("TASK_ATTEMPT_ID")){
							attributeValue = attribute.substring(attribute.length() - 2, (attribute.length() -1) );
							e.setAttemptID(Integer.parseInt(attributeValue));
						
					}else if(attribute.contains("FINISH_TIME")){
						attributeValue = attribute.substring(attribute.indexOf('=')+6, (attribute.length() -1) );
						e.setTimestamp(Integer.parseInt(attributeValue));
					}else if(attribute.contains("HOSTNAME")){
						
						attributeValue = attribute.substring(attribute.indexOf('0')+2, (attribute.indexOf('.'))-1 );
						e.setNodeNumber(Integer.parseInt(attributeValue));
						//System.out.println(attributeValue);
					}
					
				}
				System.out.println(e);
				bw.append(e.toString());
				bw.flush();
			}
			
		
		return null;
}
	
	
public static void main(String args[]) throws IOException{
		
		/*
		//String fileLoc = "d:\\joblog.txt";
		String fileLoc = "d:\\hadooptest\\reducerlog";
		//2010-02-19 15:17:02,099 INFO org.apache.hadoop.mapred.ReduceTask: Initiating in-memory merge with 167 segments...
		//2010-02-19 15:17:08,897 INFO org.apache.hadoop.mapred.ReduceTask: Interleaved on-disk merge complete: 0 files left.
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("d:\\bigparseresult.txt")));
		parseFile(fileLoc);
		//String line = "MapAttempt TASK_TYPE=\"MAP\" TASKID=\"task_201002191511_0001_m_000003\" TASK_ATTEMPT_ID=\"attempt_201002191511_0001_m_000003_0\" TASK_STATUS=\"SUCCESS\" FINISH_TIME=\"1266610500304\" HOSTNAME=\"/default-rack/compute-0-15\\.local\" STATE_STRING=\"\" ";
		//String line2 = "MapAttempt TASK_TYPE=\"MAP\" TASKID=\"task_201002191511_0001_m_000003\" TASK_ATTEMPT_ID=\"attempt_201002191511_0001_m_000003_0\" START_TIME=\"1266610479165\" TRACKER_NAME=\"tracker_compute-0-15\\.local:localhost\\.localdomain/127\\.0\\.0\\.1:44048\" HTTP_PORT=\"50060\" .";
	
		bw.flush();
		bw.close();
		*/
		
		/*
		String mainDirectory = "D:\\haopeng-03\\logs\\userlogs";
		bw = new BufferedWriter(new FileWriter("d:\\parsed\\DataMerge.txt"));
		*/
		
		
		/*This part is used in 2013
		for(int i = 105; i < 115; i ++){
			nodeNumber = i;
			String mainDirectory = "F:\\Copy\\Data\\2013\\hadoop\\log\\boduo-obelix"+i + ".local\\boduolog\\logs\\userlogs";
			String outputFile = "f:\\Copy\\Data\\2013\\hadoop\\event2\\datamerge\\obelix"+i +".txt";
			bw = new BufferedWriter(new FileWriter(outputFile));

			parseDirectory(mainDirectory);
			bw.flush();
			bw.close();

		}
		*/
		
		
		String inputDirectoryPath = "/Users/haopeng/Copy/Data/2015/hadoop/slave/hadoop-user-logs";

		
		String outputDirectoryPath = "/Users/haopeng/Copy/Data/2015/hadoop/event2/datamerge/";
		
		
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
