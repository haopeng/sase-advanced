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
import java.util.HashMap;
import java.util.LinkedList;

import edu.umass.cs.sase.stream.HadoopEvent;

public class MergeVisualizer extends HadoopDataAcitivityVisualizer {
	
	HashMap<Long, LinkedList<HadoopEvent>> mergeStartLists;
	HashMap<Long, LinkedList<HadoopEvent>> mergeFinishLists;
	

	public MergeVisualizer(String folderPath, String outputCSVPath,
			String jobEventPath) {
		super(folderPath, outputCSVPath, jobEventPath);
		
		this.mergeFinishLists = new HashMap<Long, LinkedList<HadoopEvent>>();
		this.mergeStartLists = new HashMap<Long, LinkedList<HadoopEvent>>();
	}
	
	
	public void visualize() throws IOException {
		System.out.println("Indexing all jobs...");
		this.indexJobs();
		
		System.out.println();
		System.out.println("Start processing file:" + this.folderPath);
		this.readEvents();
		
		
		if (this.mergeStartLists.size() > 0) {
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
				
				if (e.getEventType().equalsIgnoreCase("MergeStart")) {
					long jobId = e.getJobId();
					if (!this.mergeStartLists.containsKey(jobId)) {
						this.mergeStartLists.put(jobId, new LinkedList<HadoopEvent>());
					}
					
					LinkedList<HadoopEvent> list = this.mergeStartLists.get(jobId);
					list.add(e);
				
				} else {
					long jobId = e.getJobId();
					if (!this.mergeFinishLists.containsKey(jobId)) {
						this.mergeFinishLists.put(jobId, new LinkedList<HadoopEvent>());
					}
					
					LinkedList<HadoopEvent> list = this.mergeFinishLists.get(jobId);
					list.add(e);
				
				}
				
				
			}
			reader.close();
		}
		
		
	}
	
	
	public void outputEvents() throws IOException {
		
		//mergeStart
		for (Long jobId : this.mergeStartLists.keySet()) {
			if (this.jobStarts.containsKey(jobId)) {
				//sort
				LinkedList<HadoopEvent> list = this.mergeStartLists.get(jobId);
				this.sortEvents(list);
				//output
				HadoopEvent jobStart = this.jobStarts.get(jobId);
				HadoopEvent jobEnd = this.jobEnds.get(jobId);
				if (jobStart != null) {
					
					BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputCSVPath + jobId + "-MergeStart.csv"));
					writer.write(list.get(0).attributeNameToCSV() + ",TimeSinceJobStarted ,numberOfSegments\n");
					for (HadoopEvent e: list) {

						writer.write(e.toCSV() + "," + (e.getTimestamp() - jobStart.getTimestamp()) + "," + e.getNumberOfSegments() + "\n");
					}
					
					
					writer.flush();
					writer.close();
				}

				
				System.out.println("JobId=" + jobId + ":" + list.size() + " events are written to " + this.outputCSVPath + jobId + "-MergeStart.csv\n");
			}
		}
		
		//mergeFinish
		for (Long jobId : this.mergeFinishLists.keySet()) {
			if (this.jobStarts.containsKey(jobId)) {
				//sort
				LinkedList<HadoopEvent> list = this.mergeFinishLists.get(jobId);
				this.sortEvents(list);
				//output
				HadoopEvent jobStart = this.jobStarts.get(jobId);
				if (jobStart != null) {
					
					BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputCSVPath + jobId + "-MergeFinish.csv"));
					writer.write(list.get(0).attributeNameToCSV() + ",TimeSinceJobStarted ,numberOfSegments\n");
					for (HadoopEvent e: list) {

						writer.write(e.toCSV() + "," + (e.getTimestamp() - jobStart.getTimestamp()) + "," + e.getNumberOfSegments() + "\n");
					}
					
					
					writer.flush();
					writer.close();
				}

				
				System.out.println("JobId=" + jobId + ":" + list.size() + " events are written to " + this.outputCSVPath + jobId + "-MergeFinish.csv\n");
			}
		}

	}
	
	
	public static void main(String[] args) throws IOException {
		String rootFolder = "/Users/haopeng/Copy/Data/20150423/hadoop/event/datamerge/";
		String outputFolder = "/Users/haopeng/Copy/Data/20150423/hadoop/visualize/datamerge/";
		String jobEventPath = "/Users/haopeng/Copy/Data/20150423/hadoop/event/job/job.txt";
		
		File root = new File(rootFolder);
		File[] inputFolders = root.listFiles();
		
		for (File folder : inputFolders) {
			if (folder.getName().equals(".DS_Store")) {
				continue;
			}
			//String inputFolder = "/Users/haopeng/Copy/Data/2015/hadoop/event/dataactivity/A0-1.150328191651";
			//String outputFile = "/Users/haopeng/Copy/Data/2015/hadoop/visualize/A0-1.150328191651.csv";
			
			String inputFolder = folder.getAbsolutePath();
			
			MergeVisualizer jv = new MergeVisualizer(inputFolder, outputFolder, jobEventPath);
			jv.visualize();

		}
		
	}
	
	
	
}
