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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class JobLog {
	
	private enum LogType {TASKID, TASK_TYPE, TASK_STATUS, START_TIME,
		FINISH_TIME, COUNTERS, UNKNOWN};

	private PriorityQueue<MRTask> tasks;
	private BufferedReader jobLogReader;
	
	// Initialize BufferedReader and fire off parser
	public JobLog(String fileName) {
		tasks = new PriorityQueue<MRTask>();
		try {
			jobLogReader = new BufferedReader(new FileReader(new File(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		parseFile();
	}
	
	// Feed each line of the log to the line parser
	private void parseFile() {
		String line;
		try {
			while((line = jobLogReader.readLine()) != null) {
				parseLine(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Parse a single line and update 
	private void parseLine(String line) {
		// Splits on spaces as long as they don't appear within quotes
		// TODO: Possible to do this with a regular expression? 
		ArrayList<String> parts = new ArrayList<String>();
		boolean insideQuotes = false;
		int lastStartIndex = 0;
		for(int i=0; i<line.length(); i++) {
			char c = line.charAt(i);
			if(c == '"' && !insideQuotes) {
				insideQuotes = true;
			}
			else if(c == '"' && insideQuotes) {
				insideQuotes = false;
			}
			else if(c == ' ' && !insideQuotes) {
				parts.add(line.substring(lastStartIndex, i));
				lastStartIndex = i+1;
			}
		}

		//private enum LogType {TASKID, TASK_TYPE, TASK_STATUS, START_TIME,
			//FINISH_TIME, COUNTERS, UNKNOWN};
		
		if(parts.get(0).equals("Task")) {
			String taskIdString = null;
			String taskTypeString = null;
			String taskStatusString = null;
			String startTimeString = null;
			String finishTimeString = null;
			
			// Extract data from each key="value" pair
			for(int i=1; i<parts.size(); i++) {
				String part = parts.get(i);
				String value = extractValue(part);
				switch(getLogType(part)) {
				case TASKID:
					taskIdString = value;
					break;
				case TASK_TYPE:
					// Special handler for 0.20 logs
					if(value.equals("SETUP") || value.equals("CLEANUP")) {
						return;
					}
					taskTypeString = value;
					break;
				case TASK_STATUS:
					taskStatusString = value;
					break;
				case START_TIME:
					startTimeString = value;
					break;
				case FINISH_TIME:
					finishTimeString = value;
					break;
				case COUNTERS:
					// TODO: (bytes read, bytes written, etc.)
					break;
				}
			}
			
			int taskId;
			MRTask.Type taskType = null;
			
			// TASK_ID is guaranteed to be present
			int start = taskIdString.lastIndexOf('_')+1;
			taskId = Integer.parseInt(taskIdString.substring(start));
			
			// TASK_TYPE is guaranteed to be present
			if(taskTypeString.equals("MAP")) {
				taskType = MRTask.Type.MAP;
			}
			else if(taskTypeString.equals("REDUCE")) {
				taskType = MRTask.Type.REDUCE;
			}
			
			MRTask task = getTask(taskType, taskId);
			
			// TASK_STATUS may or may not be present
			if(taskStatusString != null) {
				if(taskStatusString.equals("SUCCESS")) {
					task.setStatus(MRTask.Status.SUCCESS);
				}
				else if(taskStatusString.equals("KILLED")) {
					task.setStatus(MRTask.Status.KILLED);
				}
			}
			
			// START_TIME may or may not be present
			if(startTimeString != null) {
				task.setStart(Double.parseDouble(startTimeString));
				
				// Reinsert object now that the start time has been updated. I
				// don't quite understand why I have to do this to maintain the
				// ordering, but it works.
				tasks.remove(task);
				tasks.offer(task);
			}
			
			// FINISH_TIME may or may not be present
			if(finishTimeString != null) {
				task.setFinish(Double.parseDouble(finishTimeString));
			}
			
		}
	}
	
	// Expects input of the form: KEY="VALUE"
	private String extractValue(String keyValuePair) {
		int start = keyValuePair.indexOf('\"')+1;
		int end = keyValuePair.lastIndexOf('\"');
		return keyValuePair.substring(start, end);
	}
	
	// E.g.: TASK_TYPE="MAP" would return TASK_TYPE
	private LogType getLogType(String s) {
		String typeString = s.substring(0, s.indexOf("="));
		if(typeString.equals("TASKID")) {
			return LogType.TASKID;
		}
		else if(typeString.equals("TASK_TYPE")) {
			return LogType.TASK_TYPE;
		}
		else if(typeString.equals("TASK_STATUS")) {
			return LogType.TASK_STATUS;
		}
		else if(typeString.equals("START_TIME")) {
			return LogType.START_TIME;
		}
		else if(typeString.equals("FINISH_TIME")) {
			return LogType.FINISH_TIME;
		}
		else if(typeString.equals("COUNTERS")) {
			return LogType.COUNTERS;
		}
		else {
			// Don't care about other log types
			return LogType.UNKNOWN;
		}
	}
	
	// Get the given task if it exists
	// Create and return a new task if it doesn't
	private MRTask getTask(MRTask.Type type, int id) {
		MRTask newTask = new MRTask(type, id);
		boolean taskExists = tasks.contains(newTask);
		if(taskExists) {
			// Find the matching task and return it
			for(MRTask task : tasks) {
				if(task.equals(newTask)) {
					return task;
				}
			}
		}
		else {
			// Insert the new task and return it
			tasks.offer(newTask);
			return newTask;
		}
		System.out.println("Something went wrong with getTask()");
		return null; // Should never reach here
	}
	
	public PriorityQueue<MRTask> getTasks() {
		return tasks;
	}
	
}