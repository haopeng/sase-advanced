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

import java.util.PriorityQueue;

// borrowed from Ed, didn't use it.

public class MRTaskReporter {
	
	private PriorityQueue<MRTask> tasks;
	
	public MRTaskReporter(PriorityQueue<MRTask> tasks) {
		this.tasks = tasks;
	}
	
	public String report() {
		StringBuilder s = new StringBuilder();
		s.append("id" + "\t");
		s.append("start" + "\t");
		s.append("duration" + "\n");
		MRTask task;
		double start = 0;
		boolean isFirst = true;
		while((task = tasks.poll()) != null) {
			if(isFirst) {
				isFirst = false;
				start = task.getStart();
			}
			s.append(task.getType() + task.getId() + "\t");
			//add start time
			s.append(task.getStart()+"\t");
			s.append(((int)(task.getStart()-start))/1000 + "\t");
			s.append(((int)(task.getFinish()-task.getStart()))/1000 + "\n");
			//s.append(task.getStatus() + "\n");
		}		
		return s.toString();
	}
	
	// Shows time for map and time for reduce
	public String report2() {
		MRTask task;
		double start = Double.MAX_VALUE;
		double lastMap = 0;
		double lastReduce = 0;
		while((task = tasks.poll()) != null) {
			if(task.getStart() < start) {
				start = task.getStart();
			}
			
			if(task.getType().equals("map")) {
				if(task.getFinish() > lastMap) {
					lastMap = task.getFinish();
				}
			}
			else if(task.getType().equals("reduce")) {
				if(task.getFinish() > lastReduce) {
					lastReduce = task.getFinish();
				}
			}
		}
		return ((int)(lastMap-start))/1000 + "\t" + ((int)(lastReduce-lastMap))/1000;
	}
	
}