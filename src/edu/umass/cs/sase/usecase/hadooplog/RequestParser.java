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

import edu.umass.cs.sase.stream.HadoopEvent;
import edu.umass.cs.sase.stream.Event;


/**
 * @author haopeng
 *
 */
public class RequestParser {
	static BufferedWriter  bw; 
	static int startCount, finishCount;
	static long jobId;
	static int taskId;
	static int attemptId;
	static int start = 0;
	static int finish = 0;
	static long timestamp;
	static int numberOfSegments;
	static String eventType;
	static String temp;
	static int nodeNum;
	
	public static void parseFile(String fileLoc) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(fileLoc));
		String line = br.readLine();
		while(line != null){
			if(line.contains("Adding task 'attempt") ){
				parsePullStartLine(line);
				startCount ++;
			}else if(line.contains("registerTask")){
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
		//2013-07-09 00:00:12,433 INFO org.apache.hadoop.mapred.JobTracker: Adding task 'attempt_201307081910_0007_m_000061_0' to tip task_201307081910_0007_m_000061, for tracker 'tracker_obelix114.local:localhost/127.0.0.1:33104'
		
		System.out.println(line);
		eventType = "RequestStart";
		String time = line.substring(0, 23);
		//System.out.println("time" + time);
		timestamp = DateTimeToTimestamp.dateToTs(time);
		System.out.println(timestamp);
		
		StringTokenizer st = new StringTokenizer(line);
		for(int i = 0; i < 6; i ++ ){
			st.nextToken();
		}
		temp = st.nextToken();
		//'attempt_201002191511_0001_r_000003_1'
		//attempt_201002191511_0001_m_000037_0
		jobId = Long.parseLong(temp.substring(9,26).replaceAll("_", ""));//has to keep it in 10 digits due to the limitation of Integer
		taskId = Integer.parseInt(temp.substring(29, 35));
		System.out.println(taskId);
		attemptId = Integer.parseInt(temp.substring(36,37));
		System.out.println(attemptId);
		
		System.out.println(temp);
		
		for(int i = 0; i < 5; i ++){
			st.nextToken();
		}
		temp = st.nextToken();
		//System.out.println(temp);
		//nodeNum = Integer.parseInt(temp.substring(15,18));//2013
		nodeNum = Integer.parseInt(temp.substring(19,20));
		//System.out.println("node number is:" + nodeNum);
		
		Event e = new HadoopEvent(0,eventType, jobId, taskId,  attemptId, nodeNum, timestamp, 0, 0, 0, 0);
		System.out.println(e);
		bw.append(e.toString());
		bw.flush();
	}
	public static void parsePullFinishLine(String line) throws IOException{
		System.out.println(line);
		eventType = "RequestFinish";
		String time = line.substring(0, 23);
		//System.out.println("time" + time);
		timestamp = DateTimeToTimestamp.dateToTs(time);
		System.out.println(timestamp);
		
		StringTokenizer st = new StringTokenizer(line);
		for(int i = 0; i < 6; i ++ ){
			st.nextToken();
		}
		temp = st.nextToken();
		System.out.println(temp);
		//'attempt_201002191511_0001_r_000003_1'
		//attempt_201002191511_0001_m_000037_0
		jobId = Long.parseLong(temp.substring(8,26).replaceAll("_", ""));//has to keep it in 10 digits due to the limitation of Integer

		taskId = Integer.parseInt(temp.substring(28, 34));
		System.out.println(taskId);
		attemptId = Integer.parseInt(temp.substring(35,36));
		System.out.println(attemptId);
		
		
		
		Event e = new HadoopEvent(0,eventType, jobId, taskId,  attemptId, nodeNum, timestamp, 0, 0, 0, 0);
		System.out.println(e);
		bw.append(e.toString());
		bw.flush();
	}	
	
	public  static void parseDirectory(String directory) throws IOException{
		File mainDirectory = new File(directory);
		File[] subDirectory = mainDirectory.listFiles();

		String temp;
		for(int i = 0; i < subDirectory.length; i ++){
			String sub = subDirectory[i].getName();
			if (sub.contains("jobtracker")){
				System.out.println(sub);
				
				parseFile(subDirectory[i].getAbsolutePath());
			}
		}
		
		
	}
	
	public  static void parseFinishDirectory(String directory) throws IOException{
		File mainDirectory = new File(directory);
		File[] subDirectory = mainDirectory.listFiles();

		String temp;
		for(int i = 0; i < subDirectory.length; i ++){
			String sub = subDirectory[i].getName();
			if (sub.contains("tasktracker")){
				System.out.println(sub);
				
				
				
				parseFile(subDirectory[i].getAbsolutePath());
			}
		}
		
		
	}
	
	
	public static void main(String args[]) throws IOException{
		
		
		//*****************Process requestStart logs*************************
		//String testLine = "2013-07-09 00:00:12,433 INFO org.apache.hadoop.mapred.JobTracker: Adding task 'attempt_201307081910_0007_m_000061_0' to tip task_201307081910_0007_m_000061, for tracker 'tracker_obelix114.local:localhost/127.0.0.1:33104'";
		//parsePullStartLine(testLine);
		
		/*2013
		String startInputDirectory = "f:\\copy\\Data\\2013\\hadoop\\log\\jtnn";
		String startOutput = "f:\\copy\\Data\\2013\\hadoop\\event2\\request\\start.txt";
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(startOutput)));
		parseDirectory(startInputDirectory);
		bw.flush();
		bw.close();
		*/
		//2015
		
		/*
		String startInputDirectory = "/Users/haopeng/Copy/Data/20150426/hadoop/head/hadoop-system-logs/compute-0-28.yeeha";
		String startOutput = "/Users/haopeng/Copy/Data/20150426/hadoop/event/request/start.txt";
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(startOutput)));
		parseDirectory(startInputDirectory);
		bw.flush();
		bw.close();
		*/
		
		
		
		
		
		
		//***********************end of start*********************************
		
		
		
		//*********************Process requestFinish logs***********************
		/*2013
		String finishInputDirectoryPattern = "f:\\copy\\Data\\2013\\hadoop\\log\\boduo-obelix%%.local\\boduolog\\logs";
		String finishOutputFilePattern = "f:\\copy\\Data\\2013\\hadoop\\event2\\request\\obelix%%requestfinish.txt";
		for(int i = 105; i < 115; i ++){
			String finishInputDirectory = finishInputDirectoryPattern.replace("%%", "" + i);
			String finishOutputFile = finishOutputFilePattern.replace("%%", "" + i);
			nodeNum = i;
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(finishOutputFile)));
			parseFinishDirectory(finishInputDirectory);
			bw.flush();
			bw.close();
			
		}
		*/
		
		//2015
		
		String finishInputDirectoryPattern = "/Users/haopeng/Copy/Data/20150426/hadoop/head/hadoop-system-logs/compute-1-%%.yeeha";
		String finishOutputFilePattern = "/Users/haopeng/Copy/Data/20150426/hadoop/event/request/compute-1-%%-requestfinish.txt";
		for(int i = 0; i <= 9; i ++){
			String finishInputDirectory = finishInputDirectoryPattern.replace("%%", "" + i);
			String finishOutputFile = finishOutputFilePattern.replace("%%", "" + i);
			nodeNum = i;
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(finishOutputFile)));
			parseFinishDirectory(finishInputDirectory);
			bw.flush();
			bw.close();
			
		}
		
		//*********************End of requestFinish******************************
		
		
		

		
		/*
		 * start request:
		 2010-02-19 15:17:25,505 INFO org.apache.hadoop.mapred.JobTracker: Adding task 'attempt_201002191511_0001_r_000003_1' to tip task_201002191511_0001_r_000003, for tracker 'tracker_compute-0-16.local:localhost.localdomain/127.0.0.1:45056'
		 2010-02-19 15:16:57,329 INFO org.apache.hadoop.mapred.JobTracker: Adding task 'attempt_201002191511_0001_m_000165_1' to tip task_201002191511_0001_m_000165, for tracker 'tracker_compute-0-17.local:localhost.localdomain/127.0.0.1:49502' 
		 regeister task
		 2010-02-19 15:15:10,302 INFO org.apache.hadoop.mapred.TaskTracker: LaunchTaskAction (registerTask): attempt_201002191511_0001_r_000008_0 task's state:UNASSIGNED
		  *
		  */
		
		/*
		//String fileLoc = "d:\\hadooptest\\jt.log";
		String fileLoc = "d:\\hadooptest\\tt.log";
		String dirLoc= "D:\\haopeng-03\\logs";
		startCount = 0;
		finishCount = 0;
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("d:\\parsed\\hadoop-requestfinish.txt")));
		//parseFile(fileLoc);
		//parseDirectory(dirLoc);
		parseFinishDirectory(dirLoc);
		bw.flush();
		bw.close();
		*/
		
	}


}
