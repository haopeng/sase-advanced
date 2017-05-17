
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
 *map/reduce
 */
public class JobHistoryParser {
	static BufferedWriter  bw; 
	

	
	

	public static void parseFile(String fileLoc) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(fileLoc));
		String line = br.readLine();
		while(line != null){
			if(line.startsWith("MapAttempt") || line.startsWith("ReduceAttempt")){
				parseLine(line);
			}
			line = br.readLine();
		}
		
		br.close();
		
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
						//TASKID=\"task_201002191511_0001_m_000003\"
						attributeValue = attribute.substring(13,30);
						e.setJobId(Long.parseLong(attributeValue.replaceAll("_", "")));
						
					}else if(attribute.contains("TASK_ATTEMPT_ID")){
							attributeValue = attribute.substring(attribute.length() - 2, (attribute.length() -1) );
							e.setAttemptID(Integer.parseInt(attributeValue));
						
					}else if(attribute.contains("START_TIME")){
						attributeValue = attribute.substring(attribute.indexOf('=') + 2, (attribute.length() -1) );
						e.setTimestamp(Long.parseLong(attributeValue));
					}else if(attribute.contains("TRACKER_NAME")){
						System.out.println(attribute);
						System.out.println(attribute.indexOf('=')+16);
						System.out.println(attribute.indexOf('=')+19);
						
						
						//attributeValue = attribute.substring(attribute.indexOf('=')+16, attribute.indexOf('=')+19 );//2013
						
						if (attribute.contains("compute-0-28")) {
							System.out.println();
						}
						
						attributeValue = attribute.substring(attribute.indexOf('=')+20, attribute.indexOf('=')+21 );//2015
						
						
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
						//TASKID=\"task_201002191511_0001_m_000003\"
						attributeValue = attribute.substring(13,30);
						e.setJobId(Long.parseLong(attributeValue.replaceAll("_", "")));

					}else if(attribute.contains("TASK_ATTEMPT_ID")){
							attributeValue = attribute.substring(attribute.length() - 2, (attribute.length() -1) );
							e.setAttemptID(Integer.parseInt(attributeValue));
						
					}else if(attribute.contains("FINISH_TIME")){
						attributeValue = attribute.substring(attribute.indexOf('=') + 2, (attribute.length() -1) );
						e.setTimestamp(Long.parseLong(attributeValue));
					}else if(attribute.contains("HOSTNAME")){
						System.out.println(attribute);
						//attributeValue = attribute.substring(attribute.indexOf('x') + 1, attribute.indexOf('x') + 4);//2013
						
						
						if(attribute.contains("0-28")) {
							System.out.println();
						}
						attributeValue = attribute.substring(attribute.indexOf('y') -3, attribute.indexOf('y') -2);//2015
						
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
		
		/* 2013
		String inputFolder = "f:\\Copy\\Data\\2013\\hadoop\\log\\jtnn\\history\\done\\";
		String outputFolder = "f:\\Copy\\Data\\2013\\hadoop\\event2\\mapreduceattempt\\";
		
		File rootFolder = new File(inputFolder);
		File[] allInputFiles = rootFolder.listFiles();
		
		for(int i = 0; i < allInputFiles.length; i ++){
			System.out.println("Procesing....File" +i + ":" + allInputFiles[i].getName());
			String currentFileName = allInputFiles[i].getName();
			String inputFile = inputFolder + currentFileName;
			String outputFile = outputFolder + currentFileName;
			
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			parseFile(inputFile);
			bw.flush();
			bw.close();
			
		}
		*/
		
		String inputFolder = "/Users/haopeng/Copy/Data/20150426/hadoop/head/hadoop-system-logs/compute-0-28.yeeha/history/done/";
		String outputFolder = "/Users/haopeng/Copy/Data/20150426/hadoop/event/mapreduceattempt/";
		
		File rootFolder = new File(inputFolder);
		File[] allInputFiles = rootFolder.listFiles();
		
		for(int i = 0; i < allInputFiles.length; i ++){
			System.out.println("Procesing....File" +i + ":" + allInputFiles[i].getName());
			String currentFileName = allInputFiles[i].getName();
			String inputFile = inputFolder + currentFileName;
			String outputFile = outputFolder + currentFileName+ ".txt";
			
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			parseFile(inputFile);
			bw.flush();
			bw.close();
			
		}
		
		/*
		
		String fileLoc = "d:\\bigjobhistory.txt";
		
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("d:\\bigparseresult.txt")));
		parseFile(fileLoc);
		//String line = "MapAttempt TASK_TYPE=\"MAP\" TASKID=\"task_201002191511_0001_m_000003\" TASK_ATTEMPT_ID=\"attempt_201002191511_0001_m_000003_0\" TASK_STATUS=\"SUCCESS\" FINISH_TIME=\"1266610500304\" HOSTNAME=\"/default-rack/compute-0-15\\.local\" STATE_STRING=\"\" ";
		//String line2 = "MapAttempt TASK_TYPE=\"MAP\" TASKID=\"task_201002191511_0001_m_000003\" TASK_ATTEMPT_ID=\"attempt_201002191511_0001_m_000003_0\" START_TIME=\"1266610479165\" TRACKER_NAME=\"tracker_compute-0-15\\.local:localhost\\.localdomain/127\\.0\\.0\\.1:44048\" HTTP_PORT=\"50060\" .";
	
		bw.flush();
		bw.close();
		*/
		
	}
	
}
