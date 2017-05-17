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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.StringTokenizer;

import edu.umass.cs.sase.stream.Event;
import edu.umass.cs.sase.stream.GangliaEvent;
import edu.umass.cs.sase.stream.HadoopEvent;


/**
 * @author haopeng
 * This class merge all events into one file
 *
 */
public class GangliaHadoopEventSorter {
	ArrayList<Event> allEvents;
//	ArrayList<GangliaEvent> allGangliaEvents;
	//ArrayList<HadoopEvent> allHadoopEvents;
	String gangliaRootFolder;
	String hadoopRootFolder;
	String outputFolder;
	HashSet<String> gangliaEventTypes;
	HashSet<String> HadoopEventTypes;
	int count;
	
	long startTimestamp;
	long endTimestamp;
	
	public GangliaHadoopEventSorter(){
		this.count = 0;
		this.allEvents = new ArrayList<Event>();
	}
	public GangliaHadoopEventSorter(String gangliaRootFolder, String hadoopRootFolder, String outputFolder, long startTimestamp, long endTimestamp){
		this.gangliaRootFolder = gangliaRootFolder;
		this.hadoopRootFolder = hadoopRootFolder;
		this.outputFolder = outputFolder;
		this.count = 0;
		this.allEvents = new ArrayList<Event>();
		
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}
	
	public void readAllGangliaTypes(){
		this.gangliaEventTypes = new HashSet<String>();
		File gangliaFolder = new File(this.gangliaRootFolder);
		Stack stack = new Stack();
		stack.push(gangliaFolder);
		while(!stack.empty()){
			File currentFile = (File)stack.pop();
			if(currentFile.isFile()){
				this.gangliaEventTypes.add(currentFile.getName());
			}else{
				File[] fileArray = currentFile.listFiles();
				for(int i = 0; i < fileArray.length; i ++){
					stack.push(fileArray[i]);
				}
			}
		}
		
		System.out.println("There are total " + this.gangliaEventTypes.size() + " event types for ganglia");
		int count = 0;
		for(String type : this.gangliaEventTypes){
			count ++;
			System.out.println(count + ":" + type);
		}
	}
	
	
	
	
	public void readAllGangliaEvents() throws IOException{
		File pFolder = new File(this.gangliaRootFolder);
		File[] subFolder = pFolder.listFiles();
		for(int folderNum = 0; folderNum < subFolder.length; folderNum ++){
			if(subFolder[folderNum].isDirectory()){
				//the original metrics
				
				//parse node num
				
				String folderName = subFolder[folderNum].getName();
				//int nodeN = Integer.parseInt(folderName.substring(6));//2013
				int nodeN = Integer.parseInt(folderName.substring(10, folderName.indexOf('.')));//2015, compute-0-28.yeeha or compute-1-0.yeeha
				readFromGangliaFolder(subFolder[folderNum].getAbsolutePath(), nodeN);
				
			}else{
				//the balance
				readFromGangliaFile(subFolder[folderNum].getAbsolutePath(), 999);//as a fake node

			}
		}
	}
	
	
	public void readAllGangliaEventsForType(String typeToRead) throws IOException{
		this.allEvents = new ArrayList<Event>();
		File pFolder = new File(this.gangliaRootFolder);
		File[] subFolder = pFolder.listFiles();
		for(int folderNum = 0; folderNum < subFolder.length; folderNum ++){
			if(subFolder[folderNum].isDirectory()){
				String folderName = subFolder[folderNum].getName();
				int nodeN = Integer.parseInt(folderName.substring(6));
				readFromGangliaFolderForType(subFolder[folderNum].getAbsolutePath(), nodeN, typeToRead);
				
			}else{
				//the balance
				readFromGangliaFileForType(subFolder[folderNum].getAbsolutePath(), 0, typeToRead);

			}
		}
	}
	
	public void readAllHadoopEventsFromFolder(String folderPath) throws IOException{
		if(folderPath.contains(".DS_Store")) {
			return;
		}
		File pFolder = new File(folderPath);
		File[] subFolder = pFolder.listFiles();
		for(int folderNum = 0; folderNum < subFolder.length; folderNum ++){
			if(subFolder[folderNum].isDirectory()){
				readFromHadoopFolder(subFolder[folderNum].getAbsolutePath());
			}else {
				this.readFromHadoopFile(subFolder[folderNum].getAbsolutePath());
			}
		}
	}
	
	public void readAllHadoopEvents() throws IOException{
		File pFolder = new File(this.hadoopRootFolder);
		File[] subFolder = pFolder.listFiles();
		for(int folderNum = 0; folderNum < subFolder.length; folderNum ++){
			if(subFolder[folderNum].isDirectory()){
				readFromHadoopFolder(subFolder[folderNum].getAbsolutePath());
			}
		}
	}
	
	
	
	public void readFromGangliaFolder(String inputFolder, int nodeNumber) throws IOException{
		File folder = new File(inputFolder);
		File[] allFiles = folder.listFiles();
		//System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + inputFolder);
		for(int fileNum = 0; fileNum < allFiles.length; fileNum ++){
			//System.out.println(fileNum + "************************" + allFiles[fileNum].getAbsolutePath());
			readFromGangliaFile(allFiles[fileNum].getAbsolutePath(), nodeNumber);
		}
	}
	
	
	public void readFromGangliaFolderForType(String inputFolder, int nodeNumber, String typeToRead) throws IOException{
		File folder = new File(inputFolder);
		File[] allFiles = folder.listFiles();
		for(int fileNum = 0; fileNum < allFiles.length; fileNum ++){
			//System.out.println(fileNum + "************************" + allFiles[fileNum].getAbsolutePath());
			readFromGangliaFileForType(allFiles[fileNum].getAbsolutePath(), nodeNumber, typeToRead);
		}
	}
	
	public void readFromHadoopFolder(String inputFolder) throws IOException{
		File folder = new File(inputFolder);
		File[] allFiles = folder.listFiles();
		for(int fileNum = 0; fileNum < allFiles.length; fileNum ++){
			if (allFiles[fileNum].isDirectory()) {
				this.readFromHadoopFolder(allFiles[fileNum].getAbsolutePath());
			} else {
				readFromHadoopFile(allFiles[fileNum].getAbsolutePath());
			}
		}
	}
	public void readFromGangliaFile(String inputFile, int nodeNumber) throws IOException{
		if (inputFile.contains(".DS_Store")) {
			return;
		}
		//System.out.println(inputFile);
		//parse the event type
		File f = new File(inputFile);
		String eventType = f.getName().replaceFirst("[.][^.]+$", "");
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line = br.readLine();
		while(line != null){
			//debug
			//System.out.println("reading:" + line);
			
			Event e = parseGangliaLine(line, nodeNumber, eventType);
			
			if (e.getTimestamp() >= this.startTimestamp && e.getTimestamp() <= this.endTimestamp) {
				allEvents.add(e);
				System.out.println("Adding event:" + e.toCSV() + "\t total so far:" + allEvents.size());
			}
			
			line = br.readLine();
		}
		br.close();

	}
	
	public void readFromGangliaFileForType(String inputFile, int nodeNumber, String typeToRead) throws IOException{
		//parse the event type
		File f = new File(inputFile);
		if(!f.getName().equalsIgnoreCase(typeToRead)){
			return;
		}
		String eventType = f.getName().replaceFirst("[.][^.]+$", "");
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line = br.readLine();
		while(line != null){
			allEvents.add(parseGangliaLine(line, nodeNumber, eventType));
			//debug
			System.out.println("reading:" + line);
			line = br.readLine();
		}
		br.close();
		

	}
	
	public Event parseGangliaLine(String line, int nodeNumber, String eventType){
		GangliaEvent e = new GangliaEvent();
		StringTokenizer st = new StringTokenizer(line, ",");
		long timestamp = Long.parseLong(st.nextToken());
		e.setTimestamp(timestamp);
		int value = (int)Double.parseDouble(st.nextToken());
		e.setValue(value);
		e.setEventType(eventType);
		e.setNodeNumber(nodeNumber);
			
		return e;
	}
	
	//EventType=MergeFinish	Id=0	taskId=2	attemptId=0	nodeNumber=105	timestamp=1373596006209	lowerBound=13730	upperBound=13730	originalEventId=0	numberOfSegments=1000	
	public Event parseHadoopLine(String line){
		HadoopEvent e = new HadoopEvent();
		e.setAttributesByLine(line);
		return e;
	}
	
	public void readFromHadoopFile(String inputFile) throws IOException{
		if (inputFile.contains(".DS_Store")) {
			return;
		}
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line = br.readLine();
		while(line != null){
			Event e = parseHadoopLine(line);
			if (e.getTimestamp() >= this.startTimestamp && e.getTimestamp() <= this.endTimestamp) {
				allEvents.add(e);
				System.out.println("Adding event: " + e.toCSV() + "\t total so far:" + allEvents.size());
			}
			line = br.readLine();
		}
		br.close();
	}
	
	public ArrayList<Event> getAllEvents() {
		return allEvents;
	}
	public void setAllEvents(ArrayList<Event> allEvents) {
		this.allEvents = allEvents;
	}
	
	public int getCount() {
		return this.allEvents.size();
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public void sortAllEvents(){
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
	public void readAndOutputToCSV() throws Exception{
		System.out.println("Start to reading...");
		this.readAllGangliaEvents();
		ArrayList<Event> events = this.getAllEvents();
		System.out.println("Start to sorting...");
		Collections.sort(events, new Comparator<Event>(){
			@Override
			public int compare(Event e1, Event e2){
				//System.out.println("comparing " + e1.getTimestamp() + " and " + e2.getTimestamp());
				Long t1 = e1.getTimestamp();
				Long t2 = e2.getTimestamp();
				return t1.compareTo(t2);
			}
		});
		BufferedWriter allWriter = null;
		
		
		int count = 0;
		int fileCounter = 0;
		allWriter = new BufferedWriter(new FileWriter("I:\\Copy\\Data\\2013\\mixedType\\ganglia\\ganglia-" + events.size() +"_" + fileCounter + ".csv"));
		for(int i = 0; i < events.size(); i ++){
			if(i % 5000000 == 0 && i > 0){
				fileCounter ++;
				allWriter.flush();
				allWriter.close();
				allWriter = new BufferedWriter(new FileWriter("I:\\Copy\\Data\\2013\\mixedType\\ganglia\\ganglia-" + events.size() +"_" + fileCounter + ".csv"));
			}
			System.out.println(i + ":output:" + events.get(i).toCSV());
			allWriter.write(events.get(i).toCSV() + "\n");

		}
		allWriter.flush();
		//proc_runWriter.flush();
		allWriter.close();
		System.out.println("Number of proc run events: " + count);

		
	}
	
	public void separeteTypes() throws IOException {
		this.readAllGangliaEvents();
		this.splitByEventType();
		this.reset();
		//reset
		
		File hadoopFolder = new File(this.hadoopRootFolder);
		File[] subFolders = hadoopFolder.listFiles();
		for (File f: subFolders) {
			this.readAllHadoopEventsFromFolder(f.getAbsolutePath());
			this.splitByEventType();
			this.reset();
		}
		
		
	}
	
	public void reset() {
		this.allEvents = new ArrayList<Event>();
	}
	public void mergeAll(String outputFolder) throws IOException{
		this.readAllGangliaEvents();

		this.readAllHadoopEvents();
		ArrayList<Event> events = this.getAllEvents();
		
		Collections.sort(events, new Comparator<Event>(){
			@Override
			public int compare(Event e1, Event e2){
				//System.out.println("comparing " + e1.getTimestamp() + " and " + e2.getTimestamp());
				Long t1 = e1.getTimestamp();
				Long t2 = e2.getTimestamp();
				return t1.compareTo(t2);
			}
		});
		
		/**
		 * All Events to CSV
		 */
		//BufferedWriter allWriter = new BufferedWriter(new FileWriter("/Users/haopeng/Copy/Data/2015/input-format/allEvents-" + events.size() +".csv"));
		BufferedWriter allWriter = new BufferedWriter(new FileWriter(outputFolder + "/allEvents-" + events.size() +".csv"));

		//BufferedWriter proc_runWriter = new BufferedWriter(new FileWriter("I:\\Copy\\Data\\2013\\ganglia\\eventtypes\\proc_runEvents.csv"));
		int count = 0;
		System.out.println(0 + ":" + events.get(0).toCSV());
		allWriter.write(events.get(0).toCSV() + "\n");
		for(int i = 1; i < events.size(); i ++){
			if (!events.get(i).toCSV().equalsIgnoreCase(events.get(i - 1).toCSV())) {
				System.out.println(i + ":" + events.get(i).toCSV());
				allWriter.write(events.get(i).toCSV() + "\n");
			}
			
			/*
			if(events.get(i).getEventType().equalsIgnoreCase("proc_run")){
				proc_runWriter.write(events.get(i).toCSV());
				count ++;
			}*/
		}
		allWriter.flush();
		//proc_runWriter.flush();
		allWriter.close();
		System.out.println("Number of proc run events: " + count);
	}

	public void readCSV(String csvFile) throws IOException{
		this.count = 0;
		this.allEvents = new ArrayList<Event>();
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		String line = br.readLine();//skip the first line
		while(line!= null){
			//parse the line
			if(line.startsWith("h")){
				System.out.println(line);
				HadoopEvent e = new HadoopEvent();
				e.setAttributeByCSV(line);
				this.allEvents.add(e);
				count ++;
				System.out.println(e.toCSV());
			}else if(line.startsWith("g")){
				//System.out.println(line);
				GangliaEvent e = new GangliaEvent();
				e.setAttributeByCSV(line);
				this.allEvents.add(e);
				count ++;
				//System.out.println(e.toCSV());
			}
			
			line = br.readLine();

		}
	}
	
	
	public void readCSVBetweenInterval(String csvFile, int startTime, int endTime) throws IOException{
		this.allEvents = new ArrayList<Event>();
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		String line = br.readLine();
		line = br.readLine();
		long currentTimestamp = Long.MIN_VALUE;
		
		while(line != null && currentTimestamp <= endTime){
			Event e = this.parseCSVLineOnly(line);
			currentTimestamp = e.getTimestamp();
			if(e.getTimestamp() >= startTime && e.getTimestamp() <= endTime){
				this.allEvents.add(e);
				//System.out.println(e.toCSV());
			}
			line = br.readLine();
		}
		
		br.close();
		//System.out.println("Total:" + this.allEvents.size());
		
	}
	//used to prepare data for weka
	
	/**
	 * @param csvFile
	 * @param outputFile
	 * @param startTime
	 * @param endTime
	 * @param abnormalStartTime
	 * @param abnormalEndTime
	 * @throws IOException
	 */
	public void readCSVAndLabel(String csvFile, String outputFile, int startTime, int endTime, int abnormalStartTime, int abnormalEndTime) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		String line = br.readLine();
		bw.write(line + ",label\n");
		line = br.readLine();
		long currentTimestamp = Long.MIN_VALUE;
		int count = 0;
		while(line != null && currentTimestamp <= endTime){
			count ++;
			Event e = this.parseCSVLineOnly(line);
			currentTimestamp = e.getTimestamp();
			if(e.getTimestamp() >= startTime && e.getTimestamp() <= endTime){
				if(e.getTimestamp() >= abnormalStartTime && e.getTimestamp() <= abnormalEndTime){
					bw.write(e.toCSV() + ",abnormal" + "\n");
					System.out.println(count + ":" +e.toCSV() + ",abnormal");
				}else{
					bw.write(e.toCSV() + ",normal" + "\n");
					System.out.println(count + ":" + e.toCSV() + ",normal");
				}
			}else{
				System.out.println(count + ":" +"skip:" + e.toCSV());
			}
			
			line = br.readLine();
		}
		
		br.close();
		bw.flush();
		bw.close();
		
	}
	
	
	public double computeAVG(String attribute){
		double total = 0.0;
		for(int i = 0; i < this.allEvents.size(); i ++){
			Event e = this.allEvents.get(i);
			total += (double)e.getAttributeByName(attribute);
		}
		double avg = total / (double)this.allEvents.size();
		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//System.out.println("Total number of events compute is:" + this.allEvents.size());
		//System.out.println("The sum value of the attribute--" + attribute + "-- is:" + total);
		//System.out.println("The average value of the atrribute--" + attribute + "-- is:" + avg);
		return avg;
	}
	
	public double computeDeviation(String attribute){
		double dev= 0.0;
		double avg = this.computeAVG(attribute);
		
		for(int i = 0; i < this.allEvents.size(); i ++){
			Event e = this.allEvents.get(i);
			dev += Math.pow((double)e.getAttributeByName(attribute) - avg, 2);
		}
		
		
		return dev;
	}
	
	
	
	
	public void splitByEventType() throws IOException{
		//sort
		Collections.sort(this.allEvents, new Comparator<Event>(){
			@Override
			public int compare(Event e1, Event e2){
				//System.out.println("comparing " + e1.getTimestamp() + " and " + e2.getTimestamp());
				Long t1 = e1.getTimestamp();
				Long t2 = e2.getTimestamp();
				return t1.compareTo(t2);
			}
		});
		
		//deduplcate
		//String outputPath = "I:\\Copy\\Data\\2013\\singleType\\";
		HashMap<String, BufferedWriter> eventTypes = new HashMap<String, BufferedWriter>();
		HashMap<String, String> lastEvent = new HashMap<String, String>();
		
		int dupCount = 0;
		
		for(int i = 0; i < this.allEvents.size(); i ++){
			Event e = this.allEvents.get(i);
			String type = e.getEventType();
			BufferedWriter writer = null;
			writer = eventTypes.get(type);
			if(writer == null){
				System.out.println("new writer:" + type);
				writer = new BufferedWriter(new FileWriter(this.outputFolder+ type + ".csv"));
				eventTypes.put(type, writer);
				//todo: write the schema 
				writer.write(e.attributeNameToCSV() + "\n");
			} 
			
			if (lastEvent.get(type) == null || !((String)lastEvent.get(type)).equals(e.toCSV())) {
				writer.write(e.toCSV() + "\n");
				lastEvent.put(type, e.toCSV());
			} else {
				dupCount ++;
				System.out.println(dupCount + "\tDuplicate event:" + e.toCSV() + " vs last:\n\t\t\t" + lastEvent.get(type));
			}

		}
		
		
		for(BufferedWriter w: eventTypes.values()){
			w.flush();
			w.close();
		}
	}
	
	public void fromRawToSplitCSV(String csvPath) throws IOException{
		this.readAllGangliaTypes();
		for(String type: this.gangliaEventTypes){
			this.readAllGangliaEventsForType(type);
			this.sortAllEvents();
			this.outputAllEventsToCSV(csvPath + type + ".csv");
		}
	}
	public void outputAllEventsToCSV(String csvPath) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath));
		for(int i = 0; i < this.allEvents.size(); i ++){
			Event e = this.allEvents.get(i);
			if(i == 0){
				writer.write(e.attributeNameToCSV() + "\n");
			}
			writer.write(e.toCSV() + "\n");
			System.out.println(i + ":" + e.toCSV());
		}
		writer.flush();
		writer.close();
	}
	
	public void readMultipleCSVAndSplit(String csvFile) throws IOException{
		String outputPath = "I:\\Copy\\Data\\2013\\singleType\\";
		HashMap<String, BufferedWriter> eventTypes = new HashMap<String, BufferedWriter>();
		int count = 0;
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + "testSpeed.csv"));
		for(int i = 0; i <=3; i ++){
			System.gc();//todo
			String currentCsvFile = csvFile + i + ".csv";
			BufferedReader br = new BufferedReader(new FileReader(currentCsvFile));
			
			String line = br.readLine();
			while(line!= null){
				//parse the line
				count ++;
				Event e = this.parseCSVLine(line);
				String type = e.getEventType();
				/*
				BufferedWriter writer = null;
				writer = eventTypes.get(type);
				if(writer == null){
					System.out.println("new writer:" + type);
					writer = new BufferedWriter(new FileWriter(outputPath + type + ".csv"));
					eventTypes.put(type, writer);
					writer.write(e.attributeNameToCSV() + "\n");
				}
				*/
				System.out.println(count + ".output:" + e.toCSV());
				writer.write(e.toCSV() + "\n");
				writer.flush();
				line = br.readLine();
			}
			br.close();
			
		}
		writer.close();
		/*
		for(BufferedWriter w: eventTypes.values()){
			w.flush();
			w.close();
		}*/
	}
	
	public void readCSVAndSplit(String csvFile) throws IOException{
		
		String outputPath = "I:\\Copy\\Data\\2013\\singleType\\";
		HashMap<String, BufferedWriter> eventTypes = new HashMap<String, BufferedWriter>();
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		String line = br.readLine();//skip the first line
		int count = 0;
		while(line!= null){
			//parse the line
			count ++;
			if(count % 500000 == 0){
				System.gc();
			}
			Event e = this.parseCSVLine(line);
			String type = e.getEventType();
			BufferedWriter writer = null;
			writer = eventTypes.get(type);
			if(writer == null){
				System.out.println("new writer:" + type);
				writer = new BufferedWriter(new FileWriter(outputPath + type + ".csv"));
				eventTypes.put(type, writer);
				//todo: output the attribute names
				writer.write(e.attributeNameToCSV() + "\n");
			}
			System.out.println(count + ".output:" + e.toCSV());
			writer.write(e.toCSV() + "\n");
			writer.flush();

			line = br.readLine();

		}
		br.close();
		
		for(BufferedWriter w: eventTypes.values()){
			w.close();
		}
		
		
	}
	public Event parseCSVLine(String line){
		if(line.startsWith("h")){
			//System.out.println(line);
			HadoopEvent e = new HadoopEvent();
			e.setAttributeByCSV(line);
			this.allEvents.add(e);
			count ++;
			//System.out.println(e.toCSV());
			return e;
		}else if(line.startsWith("g")){
			//System.out.println(line);
			GangliaEvent e = new GangliaEvent();
			e.setAttributeByCSV(line);
			this.allEvents.add(e);
			count ++;
			return e;
			//System.out.println(e.toCSV());
		}
		return null;
	}
	
	public Event parseCSVLineOnly(String line){
		if(line.startsWith("h")){
			//System.out.println(line);
			HadoopEvent e = new HadoopEvent();
			e.setAttributeByCSV(line);
			//System.out.println(e.toCSV());
			return e;
		}else if(line.startsWith("g")){
			//System.out.println(line);
			GangliaEvent e = new GangliaEvent();
			e.setAttributeByCSV(line);
			return e;
			//System.out.println(e.toCSV());
		}
		return null;
	}
	
	public void readCSVAndGenerateNewFeature(String csvFile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile+".newFeature.csv"));
		String attr = "value";
		int windowSize = 30000;		
		FeatureGenerator generator = new FeatureGenerator(attr, windowSize);
		String line = br.readLine();
		line = br.readLine();
		int count = 0;
		while(line != null ){
			count ++;
			Event e = this.parseCSVLine(line);
			generator.addEvent(e);
			if(count == 1){
				System.out.println(e.attributeNameToCSV() + ",label,count,sum, slidingAvg");
				bw.write(e.attributeNameToCSV() + ",label,count,sum, slidingAvg\n");
				bw.flush();
			}
			System.out.println(line+ "," + generator.getSlidingCount()+ "," + generator.getSlidingSum() + "," + generator.getSlidingAvg());
			bw.write(line+ "," + generator.getSlidingCount()+ "," + generator.getSlidingSum() + "," + generator.getSlidingAvg() + "\n");
			line = br.readLine();
			bw.flush();
		}
		br.close();
		bw.flush();
		bw.close();
		
	}
	public static void main(String args[]) throws Exception{
		//test compute sliding avg
		/*
		String csvFile = "I:\\Copy\\Data\\2013\\labeled\\proc_run_event.txt.csv";
		GangliaHadoopEventSorter s = new GangliaHadoopEventSorter();
		s.readCSVAndGenerateNewFeature(csvFile);
		*/
		
		/*
		String csvFile = "I:\\Copy\\Data\\2013\\mixedType\\ganglia\\ganglia-17029598_";
		GangliaHadoopEventSorter r = new GangliaHadoopEventSorter();
		r.readMultipleCSVAndSplit(csvFile);
		*/
		
		/*
		String gangliaParentFolder = "I:\\Copy\\Data\\2013\\ganglia\\ganglia-event";
		String hadoopParentFolder = "I:\\Copy\\Data\\2013\\hadoop\\event2";
		GangliaHadoopEventSorter r = new GangliaHadoopEventSorter(gangliaParentFolder, hadoopParentFolder);
		//r.readAllGangliaTypes();
		r.fromRawToSplitCSV("I:\\Copy\\Data\\2013\\singleType\\new\\");
		*/
		
		//Merge all
		/*
		String gangliaParentFolder = "I:\\Copy\\Data\\2013\\ganglia\\ganglia-event";
		String hadoopParentFolder = "I:\\Copy\\Data\\2013\\hadoop\\event2";
		GangliaHadoopEventSorter r = new GangliaHadoopEventSorter(gangliaParentFolder, hadoopParentFolder);
		r.mergeAll();
		*/
		
		//read csv
		/*
		String csvFile = "I:\\Copy\\Data\\2013\\mixedType\\allEvents-17029598-ganglia.csv";
		//String csvFile = "I:\\Copy\\Data\\2013\\mixedType\\allEvents-8735638-hadoop.csv";
		GangliaHadoopEventSorter r = new GangliaHadoopEventSorter();
		//r.readCSV(csvFile);
		//r.splitByEventType();
		r.readCSVAndSplit(csvFile);
		*/
		

		//Merge all, 2015, and output seperate data types
		
		/**
		 * Timestamp in milliseconds: 1427515200000
Human time (your time zone): 3/28/2015, 12:00:00 AM
Timestamp in milliseconds: 1428897600000
Human time (your time zone): 4/13/2015, 12:00:00 AM
		 */
		
		/*
		
		String gangliaParentFolder = "/Users/haopeng/Copy/Data/2015/ganglia-event-test";
		String hadoopParentFolder = "/Users/haopeng/Copy/Data/2015/hadoop/event-test";

		String outputFolder = "/Users/haopeng/Copy/Data/2015/input-format";
		
		long startTimestamp = 1427515200000L;
		long endTimestamp = 1428897600000L;

		if (args.length >= 5) {
			gangliaParentFolder = args[0];
			hadoopParentFolder = args[1];
			outputFolder = args[2];
			
			startTimestamp = Long.parseLong(args[3]);
			endTimestamp = Long.parseLong(args[4]);
			
		}

		//String gangliaParentFolder = "/Users/haopeng/Copy/Data/2015/ganglia-event";
		//String hadoopParentFolder = "/Users/haopeng/Copy/Data/2015/hadoop/event";
				GangliaHadoopEventSorter r = new GangliaHadoopEventSorter(gangliaParentFolder, hadoopParentFolder, startTimestamp, endTimestamp);

		r.mergeAll(outputFolder);
		*/
		
		//Read in parsed logs, and output spearte data types to single files. (Each type / file)
		
		String gangliaParentFolder = "/Users/haopeng/Copy/Data/2015/ganglia-event-test";
		String hadoopParentFolder = "/Users/haopeng/Copy/Data/2015/hadoop/event-test";

		String outputFolder = "/Users/haopeng/Copy/Data/2015/input-format/";
		
		long startTimestamp = 1427515200000L;
		long endTimestamp = 1428897600000L;

		if (args.length >= 5) {
			gangliaParentFolder = args[0];
			hadoopParentFolder = args[1];
			outputFolder = args[2];
			
			startTimestamp = Long.parseLong(args[3]);
			endTimestamp = Long.parseLong(args[4]);
			
		}

		//String gangliaParentFolder = "/Users/haopeng/Copy/Data/2015/ganglia-event";
		//String hadoopParentFolder = "/Users/haopeng/Copy/Data/2015/hadoop/event";
		GangliaHadoopEventSorter r = new GangliaHadoopEventSorter(gangliaParentFolder, hadoopParentFolder, outputFolder, startTimestamp, endTimestamp);
		r.separeteTypes();

		System.out.println("Version 0.10");
		
		
	}

}
