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

import java.util.ArrayList;

import edu.umass.cs.sase.stream.Event;

public class FeatureGenerator {
	//sliding window aggregates
	//window size 1 minute
	//aggregation max, min, sum, count, avg
	
	ArrayList<Event> buffer;
	String attr;
	int sum;
	int windowSize;
	
	public FeatureGenerator(String attr, int windowSize){
		this.buffer = new ArrayList<Event>();
		this.attr = attr;
		this.windowSize = windowSize;
	}
	public int getSlidingAvg(){

		return this.sum / this.buffer.size();
	}
	public int getSlidingCount(){
		return this.buffer.size();
	}
	public int getSlidingSum(){
		return this.sum;
	}
	public void clearEventByTimewindow(){
		Event e = this.buffer.get(this.buffer.size() - 1);
		long currentTimestamp = e.getTimestamp();
		long earliestTimestamp = currentTimestamp - windowSize;
		
		boolean done = false;
		while(!done && buffer.get(0).getTimestamp() < earliestTimestamp){
			Event eToRemove = buffer.get(0);
			buffer.remove(0);
			this.removeEvent(eToRemove);
		}
	}
	
	public void addEvent(Event e){
		this.buffer.add(e);
		this.sum += (e.getAttributeByName(attr));
		this.clearEventByTimewindow();
		
	}
	public void removeEvent(Event e){
		this.buffer.remove(e);
		this.sum -= e.getAttributeByName(attr);
	}
	
	
}
