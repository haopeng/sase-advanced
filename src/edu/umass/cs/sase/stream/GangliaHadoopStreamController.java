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
package edu.umass.cs.sase.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import edu.umass.cs.sase.engine.ConfigFlags;


/**
 * @author haopeng
 *
 */
public class GangliaHadoopStreamController extends StreamController{
	int pointer;// used to tag the position in the stream where the engine has processed
	String inputFile;
	int batchNumber;
	int batchSize = 100000;
	int numberOfEventsToRead = Integer.MAX_VALUE;
	HashMap<String, Integer> typeStatistics;
	//int numberOfEventsToRead = 10000;//for debug
	public GangliaHadoopStreamController(String inputFile) throws IOException{
		this.typeStatistics = new HashMap<String, Integer>();
		this.inputFile = inputFile;
		this.batchNumber = 0;
		ArrayList<Event> events = new ArrayList<Event>();
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line = br.readLine();
		int counter = 0;
		while(line != null && counter < this.numberOfEventsToRead){
			//System.out.println(counter + ": a new line:" + line);
			if(line.startsWith("g,")){
				
				GangliaEvent e = new GangliaEvent();
				e.setAttributesBySelectedContent(line);
				e.setId(counter);
				//System.out.println("g:" + e.toString());
				this.addUncertaintyIntervalForGanglia(e);
				events.add(e);
				this.updateTypeStatistics(e);
			}else{
				HadoopEvent e = new HadoopEvent();
				e.setAttributesBySelectedContent(line);
				e.setId(counter);
				//System.out.println("h:" + e.toString());
				this.addUncertaintyIntervalForHadoop(e);
				events.add(e);
				this.updateTypeStatistics(e);
			}
			
			
			line = br.readLine();
			counter ++;
		}
		br.close();
		System.out.println("**********Total events read from file " + this.inputFile + " is:" + events.size());
		Event[] eventArray = new Event[events.size()];
		for(int i = 0; i < events.size(); i ++){
			eventArray[i] = events.get(i);
		}
		
		this.myStream = new Stream(events.size());
		this.myStream.setEvents(eventArray);
		
		
	}
	public void addUncertaintyIntervalForHadoop(HadoopEvent e){
		if(ConfigFlags.engineType.equalsIgnoreCase("imprecise")){
			e.setLowerBound(e.getTimestamp() - ConfigFlags.hadoopHalfPeriod);
			e.setUpperBound(e.getTimestamp() + ConfigFlags.hadoopHalfPeriod);
			e.setOriginalEventId(1);
		}
	}
	
	public void addUncertaintyIntervalForGanglia(GangliaEvent e){
		if(ConfigFlags.engineType.equalsIgnoreCase("imprecise")){
			e.setLowerBound(e.getTimestamp() - ConfigFlags.gangliaHalfPeriod);
			e.setUpperBound(e.getTimestamp() + ConfigFlags.gangliaHalfPeriod);
			e.setOriginalEventId(1);
		}
	}

	
	public void updateTypeStatistics(Event e){
		if(this.typeStatistics.containsKey(e.getEventType())){
			int old = this.typeStatistics.get(e.getEventType());
			this.typeStatistics.put(e.getEventType(), old + 1);
		}else{
			this.typeStatistics.put(e.getEventType(), 1);
		}
	}
	
	public void printTypeStatistics(){
		for(Map.Entry<String, Integer>entry : this.typeStatistics.entrySet()){
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}
	
	public GangliaHadoopStreamController(int hadoopHalfPeriod, int gangliaHalfPeriod) throws IOException{
		pointer = 0;
		String filesToMerge[] = {"d:\\hadoop_result.txt", "d:\\ganglia-event\\cpu_idle.0-14-events.txt",
				"d:\\ganglia-event\\cpu_idle.0-15-events.txt","d:\\ganglia-event\\cpu_idle.0-16-events.txt",
				"d:\\ganglia-event\\cpu_idle.0-17-events.txt","d:\\ganglia-event\\cpu_idle.0-19-events.txt",
				"d:\\ganglia-event\\cpu_idle.0-20-events.txt","d:\\ganglia-event\\cpu_idle.0-21-events.txt",
				"d:\\ganglia-event\\cpu_idle.0-22-events.txt"};
		String filesToMerge3[] = {"d:\\hadoop_result.txt", "d:\\ganglia-event\\cpu_user.0-14-events.txt",
				"d:\\ganglia-event\\cpu_user.0-15-events.txt","d:\\ganglia-event\\cpu_user.0-16-events.txt",
				"d:\\ganglia-event\\cpu_user.0-17-events.txt","d:\\ganglia-event\\cpu_user.0-19-events.txt",
				"d:\\ganglia-event\\cpu_user.0-20-events.txt","d:\\ganglia-event\\cpu_user.0-21-events.txt",
				"d:\\ganglia-event\\cpu_user.0-22-events.txt"};
		
		String filesToMerge2[] = {"d:\\hadoop_result.txt","d:\\ganglia-event\\cpu_user.0-14-events.txt"};

		Event[] events = EventMerger.mergeFile(filesToMerge3, hadoopHalfPeriod, gangliaHalfPeriod);
		Stream newStream = new Stream();
		newStream.setEvents(events);
		this.setMyStream(newStream);
		
		

	}
	public GangliaHadoopStreamController(int hadoopHalfPeriod, int gangliaHalfPeriod, String directoryPath) throws IOException{
		pointer = 0;
		File directory = new File(directoryPath);
		File files[] = directory.listFiles();
		String filesToMerge[] = new String[files.length];
		for(int i = 0; i < files.length; i ++){
			filesToMerge[i] = files[i].getAbsolutePath();
		}

		Event[] events = EventMerger.mergeFile(filesToMerge, hadoopHalfPeriod, gangliaHalfPeriod);
		Stream newStream = new Stream();
		newStream.setEvents(events);
		this.setMyStream(newStream);
		
		

	}
	/*
	 * @param int batchSize: specify the size of the required batch
	 */
	public Stream nextBatch(int batchSize){
		Event[] batchEvents = new Event[batchSize];
		int counter = 0;
		while(counter < batchSize && pointer < this.myStream.getSize()){
			batchEvents[counter] = this.myStream.getEvents()[pointer];
			counter ++;
			pointer ++;
		}
		Stream batchStream = new Stream();
		batchStream.setEvents(batchEvents);
		return batchStream;
		
	}
	public Stream nextExpandedBatch(int batchSize){
		Stream batch = this.nextBatch(batchSize);
		Stream expandedBatch = expandBatchStream(batch);
		return expandedBatch;
		
	}
	
	public boolean hasMoreEvent(){
		return (pointer < this.myStream.getSize());
	}
	
	
	
	
	public GangliaHadoopStreamController(String streamFile, int hadoopHalfPeriod, int gangliaHalfPeriod) throws IOException{
		Event[] events = readStreamFromFile(streamFile, hadoopHalfPeriod, gangliaHalfPeriod);
		Stream newStream = new Stream();
		newStream.setEvents(events);
		this.setMyStream(newStream);
		
	}
	
	public static Event[] readStreamFromFile(String fileLoc, int hadoopHalfPeriod, int gangliaHalfPeriod) throws IOException{
		ArrayList<Event> eventList = new ArrayList<Event>();
		
		BufferedReader br = new BufferedReader(new FileReader(fileLoc));
		String line = br.readLine();
		while(line != null){
			if(line.startsWith("EventType=MapStart") || line.startsWith("EventType=MapStart")
					||line.startsWith("EventType=MapStart") || line.startsWith("EventType=MapStart")){
				eventList.add(readHadoopLine(hadoopHalfPeriod, line));
			}else {
				eventList.add(readGangliaLine(gangliaHalfPeriod, line));
			}
			line = br.readLine();
			
		}
		Event[] eventArray = new Event[eventList.size()];
		for(int i = 0; i < eventList.size(); i ++){
			eventArray[i] = eventList.get(i);
		}
		br.close();
		return eventArray;
	}
	
	public static Event readHadoopLine(int hadoopHalfPeriod, String line){
		HadoopEvent e = new HadoopEvent();
		String attribute;
		int timestamp;
		StringTokenizer st = new StringTokenizer(line);
		String attributeValue;
		while(st.hasMoreElements()){
			attribute = st.nextToken();
			if(attribute.startsWith("EventType")){
				e.setEventType(attribute.substring(attribute.indexOf('=') + 1));
			}else if(attribute.startsWith("Id")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				e.setId(Integer.parseInt(attributeValue));
				e.setOriginalEventId(e.getId());
			}	else if(attribute.startsWith("nodeNumber")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				e.setNodeNumber(Integer.parseInt(attributeValue));
			}else if(attribute.startsWith("timestamp")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 5);
				timestamp = Integer.parseInt(attributeValue);
				e.setTimestamp(timestamp);
				e.setLowerBound(timestamp - hadoopHalfPeriod);
				e.setUpperBound(timestamp + hadoopHalfPeriod);
			}else if(attribute.startsWith("attemptId")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				e.setAttemptID(Integer.parseInt(attributeValue));
			}else if(attribute.startsWith("taskId")){
				e.setTaskId(Integer.parseInt(attribute.substring(attribute.indexOf('=') + 1 )));
			}
			
		}
		
		return e;

	}
	
	public static Event readGangliaLine(int gangliaHalfPeriod, String line){
		GangliaEvent e = new GangliaEvent();
		String attribute;
		StringTokenizer st = new StringTokenizer(line);
		int timestamp;
		String attributeValue;
		while(st.hasMoreElements()){
			attribute = st.nextToken();
			if(attribute.startsWith("EventType")){
				e.setEventType(attribute.substring(attribute.indexOf('=') + 1));
			}else if(attribute.startsWith("Id")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				e.setId(Integer.parseInt(attributeValue));
				e.setOriginalEventId(e.getId());
			}else if(attribute.startsWith("nodeNumber")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				e.setNodeNumber(Integer.parseInt(attributeValue));
			}else if(attribute.startsWith("timestamp")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 5);
				timestamp = Integer.parseInt(attributeValue);
				e.setTimestamp(timestamp);
				e.setLowerBound(timestamp - gangliaHalfPeriod * 2);
				e.setUpperBound(timestamp);
			}else if(attribute.startsWith("value")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				e.setValue((int)Double.parseDouble(attributeValue));
			}
			
		}
		
		return e;
	}
	public Stream expandBatchStream(Stream s){
		Event[] originalEvents = s.getEvents();
		int size = originalEvents.length;
		ArrayList<Event> newEventsList = new ArrayList<Event>();
		int count = 0;
		Event tempE;
		HadoopEvent aE;
		GangliaEvent gE;
		int timestamp;
		int upper;
		int lower;
		int nodeNumber;
		String eventType;
		int Id;
		int taskId;
		int attemptId;
		int value;
		
		for(int i = 0; i < size; i ++){
			tempE = originalEvents[i];
			upper = tempE.getAttributeByName("upperBound");
			lower = tempE.getAttributeByName("lowerBound");
			eventType = tempE.getEventType();
			for(int j = lower; j <= upper; j ++){
				if(eventType.equalsIgnoreCase("MapStart") || eventType.equalsIgnoreCase("MapFinish") || eventType.equalsIgnoreCase("ReduceStart") || eventType.equalsIgnoreCase("ReduceFinish")){
					//hadoop event
					aE = (HadoopEvent)tempE.clone();
					aE.setEventType(eventType);
					aE.setOriginalEventId(aE.getId());
					aE.setId(count ++);
					aE.setTimestamp(j);
					newEventsList.add(aE);
					//System.out.println(count  + "events added");
					
				}else{
					// ganglia event
					gE = (GangliaEvent)tempE.clone();
					gE.setEventType(eventType);
					gE.setOriginalEventId(gE.getId());
					gE.setId(count ++);
					gE.setTimestamp(j);
					newEventsList.add(gE);
					
					//System.out.println(count  + "events added");
				}
			}
			
		}
		
		Event[] expandedEvents = new Event[count];
		newEventsList.toArray(expandedEvents);
		
		Stream newStream = new Stream();
		newStream.setEvents(expandedEvents);
		return newStream;
		
		
	}
	
	
	
	public void expandStream(){
		Event[] originalEvents = this.myStream.getEvents();
		int size = originalEvents.length;
		ArrayList<Event> newEventsList = new ArrayList<Event>();
		int count = 0;
		Event tempE;
		HadoopEvent aE;
		GangliaEvent gE;
		int timestamp;
		int upper;
		int lower;
		int nodeNumber;
		String eventType;
		int Id;
		int taskId;
		int attemptId;
		int value;
		
		for(int i = 0; i < size; i ++){
			tempE = originalEvents[i];
			upper = tempE.getAttributeByName("upperBound");
			lower = tempE.getAttributeByName("lowerBound");
			eventType = tempE.getEventType();
			for(int j = lower; j <= upper; j ++){
				if(eventType.equalsIgnoreCase("MapStart") || eventType.equalsIgnoreCase("MapFinish") || eventType.equalsIgnoreCase("ReduceStart") || eventType.equalsIgnoreCase("ReduceFinish")){
					//hadoop event
					aE = (HadoopEvent)tempE.clone();
					aE.setOriginalEventId(aE.getId());
					aE.setId(count ++);
					aE.setTimestamp(j);
					newEventsList.add(aE);
					System.out.println(count  + "events added");
					
				}else{
					// ganglia event
					gE = (GangliaEvent)tempE.clone();
					gE.setOriginalEventId(gE.getId());
					gE.setId(count ++);
					gE.setTimestamp(j);
					newEventsList.add(gE);
					
					System.out.println(count  + "events added");
				}
			}
			
		}
		
		Event[] expandedEvents = new Event[count];
		newEventsList.toArray(expandedEvents);
		
		Stream newStream = new Stream();
		newStream.setEvents(expandedEvents);
		this.setMyStream(newStream);
		
		
	}

	public void printStream(){
		Event e;
		int counter = 0;
		while((e = this.myStream.popEvent()) != null){
			
			System.out.println(counter + ":" + e.toString());
			counter ++;
		}
	}
	//todo, test this classs
	public static void main(String args[]) throws IOException{
		String inputFile = "F:\\Copy\\Data\\2013\\gheventssorted.txt";
		inputFile = "F:\\Copy\\Data\\2013\\balancehadoop.txt";
		//inputFile = "F:\\Copy\\Data\\2013\\testmapstart.txt";
		if(args.length > 0){
			inputFile = args[0];
		}
		
		GangliaHadoopStreamController controller = new GangliaHadoopStreamController(inputFile);
		Stream s = controller.getMyStream();
		Event e;
		int counter = 0;
		controller.printTypeStatistics();
		/*
		while((e = s.popEvent()) != null){
			
			System.out.println(counter + ":" + e.toString());
			counter ++;
		}*/
	}
	
}
