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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import edu.umass.cs.sase.stream.Event;
import edu.umass.cs.sase.stream.GangliaEvent;

/**
 * This class is used to extract events of one type
 * @author haopeng
 *
 */
public class EventTypeExtractor {
	ArrayList<Event> allEvents;
	String folder;
	String eventType;
	String outputFolder;
	int count;

	public EventTypeExtractor(String folder){
		this.folder = folder;
		this.allEvents = new ArrayList<Event>();
	}
	
	public void extractHadoopEventInType(String eventType, String outputFolder) throws IOException{
		this.eventType = eventType;
		this.outputFolder = outputFolder;
		this.count = 0;
		File pFolder = new File(this.folder);
		File[] subFolder = pFolder.listFiles();
		for(int folderNum = 0; folderNum < subFolder.length; folderNum ++){
			if(subFolder[folderNum].isDirectory() && subFolder[folderNum].getName().startsWith("obelix")){
				//the original metrics
				//parse node num
				String folderName = subFolder[folderNum].getName();
				int nodeN = Integer.parseInt(folderName.substring(6));
				readFromNodeFolder(subFolder[folderNum].getAbsolutePath(), nodeN, eventType);
			}
		}
		this.sortAllEvents();
		
	}
	
	public void outputEventsToFile() throws IOException{
		BufferedWriter allWriter = new BufferedWriter(new FileWriter(this.outputFolder + this.eventType + ".csv"));
		
		for(int i = 0; i < this.allEvents.size() ; i ++){
			Event e = allEvents.get(i);
			System.out.println(i + ":"+ e.toCSV());
			allWriter.write(e.toCSV() + "\n");
		}
		allWriter.flush();
		System.out.println("total:" + this.allEvents.size());
	}
	public void outputEventsToFileWithinInterval(int start, int end) throws IOException{
		BufferedWriter allWriter = new BufferedWriter(new FileWriter(this.outputFolder + this.eventType + "_" + start + "_" + end + ".csv"));
		int countValid = 0;
		for(int i = 0; i < this.allEvents.size() ; i ++){
			Event e = allEvents.get(i);
			if(e.getTimestamp() >= start && e.getTimestamp() <= end){
				countValid ++;
				System.out.println(countValid + ":"+ e.toCSV());
				allWriter.write(e.toCSV() + "\n");
				
			}else if(e.getTimestamp() > end){
				allWriter.flush();
				System.out.println("total:" + countValid);
				allWriter.close();
				return;
			}

		}

	}
	public void readFromNodeFolder(String inputFolder, int nodeNumber, String eventType) throws IOException{
		File folder = new File(inputFolder);
		File[] allFiles = folder.listFiles();
		for(int fileNum = 0; fileNum < allFiles.length; fileNum ++){
			if(allFiles[fileNum].getName().startsWith(eventType)){
				readFromGangliaFile(allFiles[fileNum].getAbsolutePath(), nodeNumber);
			}
			
		}
	}
	public void readFromGangliaFile(String inputFile, int nodeNumber) throws IOException{
		//parse the event type
		File f = new File(inputFile);
		System.out.println("fileName:" + inputFile);
		String eventType = f.getName().replaceFirst("[.][^.]+$", "");
		//int count = 0;
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line = br.readLine();
		while(line != null){
			this.count ++;
			System.out.println(this.count + ":" + line);
			allEvents.add(parseGangliaLine(line, nodeNumber, eventType));
			line = br.readLine();
		}
		br.close();
	}
	
	public Event parseGangliaLine(String line, int nodeNumber, String eventType){
		GangliaEvent e = new GangliaEvent();
		StringTokenizer st = new StringTokenizer(line, ",");
		int timestamp = Integer.parseInt(st.nextToken());
		e.setTimestamp(timestamp);
		int value = Integer.parseInt(st.nextToken());
		e.setValue(value);
		e.setEventType(eventType);
		e.setNodeNumber(nodeNumber);
			
		return e;
	}
	
	public void sortAllEvents(){
		System.out.println("Sorting~~~~~~~~~~~`");
		Collections.sort(this.allEvents, new Comparator<Event>(){
			@Override
			public int compare(Event e1, Event e2){
				//System.out.println("comparing " + e1.getTimestamp() + " and " + e2.getTimestamp());
				Long t1 = e1.getTimestamp();
				Long t2 = e2.getTimestamp();
				return t1.compareTo(t2);
			}
		});
	}
	public static void main(String args[]) throws IOException{
		String gangliaFolder = "I:\\Copy\\Data\\2013\\ganglia\\ganglia-event";
		
		EventTypeExtractor extractor = new EventTypeExtractor(gangliaFolder);
		
		String eventType = "proc_run";
		String outputFolder = "I:\\Copy\\Data\\2013\\ganglia\\eventtypes\\";
		
		extractor.extractHadoopEventInType(eventType, outputFolder);
		
//		int start = 637583030;
		//int end = 639906229;
		int start = 639906229;
		int end = 642072543;
		extractor.outputEventsToFileWithinInterval(start, end);
	}
	
}
