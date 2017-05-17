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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import edu.umass.cs.sase.stream.Event;
import edu.umass.cs.sase.stream.HadoopEvent;


/**
 * This class is used to visualize the data acitivty for each job
 * @author haopeng
 *
 */
public class HadoopDataAcitivityVisualizer {
	String folderPath;
	String outputCSVPath;
	String jobEventPath;
	
	HashMap<Long, HadoopEvent> jobStarts;
	HashMap<Long, HadoopEvent> jobEnds;
	
	//HashMap<Integer, Integer> jobIds;
	
	
	//LinkedList<HadoopEvent> events;
	
	HashMap<Long, LinkedList<HadoopEvent>> eventLists;
	
	
	public HadoopDataAcitivityVisualizer(String folderPath, String outputCSVPath, String jobEventPath) {
		this.folderPath = folderPath;
		this.outputCSVPath = outputCSVPath;
		this.jobEventPath = jobEventPath;
		
		
		//this.events = new LinkedList<HadoopEvent>();
		this.eventLists = new HashMap<Long, LinkedList<HadoopEvent>>();
		
	}
	
	/**
	 * Read in all job information
	 * @throws IOException
	 */
	public void indexJobs() throws IOException {
		///Users/haopeng/Copy/Data/2015/hadoop/event/job
		this.jobStarts = new HashMap<Long, HadoopEvent>();
		this.jobEnds = new HashMap<Long, HadoopEvent>();
		
		BufferedReader reader = new BufferedReader(new FileReader(this.jobEventPath));
		String line = null;
		while((line = reader.readLine()) != null) {
			HadoopEvent job = new HadoopEvent(line);
			if (job.getEventType().equalsIgnoreCase("JobStart")) {
				this.jobStarts.put(job.getJobId(), job);
			} else {
				this.jobEnds.put(job.getJobId(), job);
			}
		}
		
		reader.close();
	}
	
	public void visualize() throws IOException {
		System.out.println("Indexing all jobs...");
		this.indexJobs();
		
		System.out.println();
		System.out.println("Start processing file:" + this.folderPath);
		this.readEvents();
		
		
		if (this.eventLists.size() > 0) {
			this.outputEvents();
		}
		
	}
	
	public void readEvents() throws IOException {
		
		File folder = new File(this.folderPath);
		File[] files = folder.listFiles();
		for (File f : files) {
			BufferedReader reader = new BufferedReader(new FileReader(f.getAbsolutePath()));
			String line = null;
			while((line = reader.readLine()) != null) {
				HadoopEvent e = new HadoopEvent(line);
				long jobId = e.getJobId();
				if (!this.eventLists.containsKey(jobId)) {
					this.eventLists.put(jobId, new LinkedList<HadoopEvent>());
				}
				
				LinkedList<HadoopEvent> list = this.eventLists.get(jobId);
				list.add(e);
				
			}
			reader.close();
		}
		
		
	}
	
	public void sortEvents(LinkedList<HadoopEvent> list) {
		Collections.sort(list, new Comparator<Event>(){
			@Override
			public int compare(Event e1, Event e2){
				//System.out.println("comparing " + e1.getTimestamp() + " and " + e2.getTimestamp());
				Long t1 = e1.getTimestamp();
				Long t2 = e2.getTimestamp();
				return t1.compareTo(t2);
			}
		});
	}
	
	public void outputEvents() throws IOException {
		for (Long jobId : this.eventLists.keySet()) {
			if (this.jobStarts.containsKey(jobId)) {
				
				//sort
				LinkedList<HadoopEvent> list = this.eventLists.get(jobId);
				this.sortEvents(list);
				//output
				HadoopEvent jobStart = this.jobStarts.get(jobId);
				HadoopEvent jobEnd = this.jobEnds.get(jobId);
				if (jobStart != null) {
					
					BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputCSVPath + jobId + ".csv"));
					writer.write(list.get(0).attributeNameToCSV() + ",TimeSinceJobStarted ,AccumulatedValue\n");
					double accumulatedValue = 0;
					for (HadoopEvent e: list) {
						accumulatedValue += e.getValue();
						writer.write(e.toCSV() + "," + (e.getTimestamp() - jobStart.getTimestamp()) + "," + accumulatedValue + "\n");
						//debug
						//System.out.println(e.toCSV() + "," + (e.getTimestamp() - jobStart.getTimestamp()) + "," + accumulatedValue + "\n");
					}
					
					if (jobEnd != null) {
						//writer.write(list.get(list.size() - 1).toCSV() + "," + (jobEnd.getTimestamp() - jobStart.getTimestamp()) + "," + 0 + "\n");
					}
					
					writer.flush();
					writer.close();
				}

				//remove jobId from jobStarts/jobEnds
				this.jobStarts.remove(jobId);
				this.jobEnds.remove(jobId);
				//
				System.out.println("JobId=" + jobId + ":" + list.size() + " events are written to " + this.outputCSVPath + jobId + ".csv\n");
			}
		}
		
		
		
	}
	
	
	public static void main(String[] args) throws IOException {
		String rootFolder = "/Users/haopeng/Copy/Data/2015/hadoop/event2/dataactivity/";
		String outputFolder = "/Users/haopeng/Copy/Data/2015/hadoop/visualize3/";
		String jobEventPath = "/Users/haopeng/Copy/Data/2015/hadoop/event2/job/job.txt";
		
		File root = new File(rootFolder);
		File[] inputFolders = root.listFiles();
		
		for (File folder : inputFolders) {
			if (folder.getName().equals(".DS_Store")) {
				continue;
			}
			//String inputFolder = "/Users/haopeng/Copy/Data/2015/hadoop/event/dataactivity/A0-1.150328191651";
			//String outputFile = "/Users/haopeng/Copy/Data/2015/hadoop/visualize/A0-1.150328191651.csv";
			
			String inputFolder = folder.getAbsolutePath();
			
			HadoopDataAcitivityVisualizer jv = new HadoopDataAcitivityVisualizer(inputFolder, outputFolder, jobEventPath);
			jv.visualize();

		}
		
	}
	
}
