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

package edu.umass.cs.sase.engine;

import java.util.ArrayList;
import net.sourceforge.jeval.EvaluationException;
import edu.umass.cs.sase.query.Edge;
import edu.umass.cs.sase.stream.Event;

/**
 * this class is used to wrap an event stack used by the EventStacks.
 * A single stack is buffering events for an edge.
 * A normal query component has only one edge, and in this case one stack is enough for one component.
 * For a kleene closure component, it has more than one edges, and in this case, it has multiple stacks to buffer events seperately for its edges
 * A special case is a proceed edge, we do not buffer event for it, because it is evaluating events for the take/begin edge, not for new events
 * 
 * 
 * @author haopeng
 *
 */
public class SingleEventStack {
	Edge edge;
	String edgeTag;
	long oldestTimestamp;
	ArrayList<Event> events;
	int stackNumber;
	/**
	 * for take edges, if it has no events, the processing can be done.
	 */
	boolean shouldBeCheckedForFull;
	
	public SingleEventStack(String tag, Edge e, int stackNumber){
		this.edgeTag = tag;
		this.edge = e;
		this.stackNumber = stackNumber;
		this.oldestTimestamp = Integer.MAX_VALUE;
		this.events = new ArrayList<Event>();
	}
	
	public void addEvent(Event newEvent){
		//assumption: timestamps are increasing
		if(this.events.size() == 0){
			this.oldestTimestamp = newEvent.getTimestamp();
		}
		
		this.events.add(newEvent);
		
	}
	
	public Event popEvent(){
		Event e = null;
		if(this.events.size() > 0){
			e = this.events.get(0);
			this.events.remove(0);
			if(this.events.size() > 0){
				this.oldestTimestamp = this.events.get(0).getTimestamp();
			}else {
				this.oldestTimestamp = Integer.MAX_VALUE;
			}
		}
		
	
		return e;
	}
	
	public void expireEvents(long oldestRunTimestamp, long youngestRunTimestamp, long latestEventTimestamp, int timeWindow, int latestStackNumber){
		if(this.stackNumber != latestStackNumber){
			if(this.stackNumber == 0){
				//in this case, the first stack is not the last stack, so the last event is on the right
				//only apply latest event, no run timestamp filter
				long left = latestEventTimestamp - timeWindow;
				long right = latestEventTimestamp + timeWindow;
				int counter = 0;
				while(counter < this.events.size()){
					Event e = this.events.get(counter);
					if(e.getTimestamp() < left || e.getTimestamp() > right){
						this.events.remove(e);
						Profiling.numberOfExpiredEvents ++;
					}else{
						counter ++;
					}
				}
				
			}else{
				//apply two filters
				long left1 = oldestRunTimestamp;
				long right1 = youngestRunTimestamp + timeWindow;
				long left2 = latestEventTimestamp - timeWindow;
				long right2 = latestEventTimestamp + timeWindow;
				
				int counter = 0;
				while(counter < this.events.size()){
					Event e = this.events.get(counter);
					long t = e.getTimestamp();
					if((t >= left1 && t <= right1) || (t >= left2 && t <= right2)){
						counter ++;
					}else{
						this.events.remove(e);
						Profiling.numberOfExpiredEvents ++;
					}
				}
				
			}
			
		}
	}
	
	/**
	 * No runs in runpool yet. So only use the latest event to filter other events
	 * @param latestEventTimestamp
	 * @param timeWindow
	 * @param latestStackNumber
	 */
	public void expireEvents(long latestEventTimestamp, int timeWindow, int latestStackNumber){

		if(this.stackNumber != latestStackNumber){
			long left = latestEventTimestamp - timeWindow;
			long right = latestEventTimestamp + timeWindow;
			int counter = 0;
			while(counter < this.events.size()){
				Event e = this.events.get(counter);
				if(e.getTimestamp() < left || e.getTimestamp() > right){
					
					this.events.remove(e);
					Profiling.numberOfExpiredEvents ++;
				}else{
					counter ++;
				}
			}
			
			
		}
	}
	
	
	public boolean hasMoreEvents(){
		return (this.events.size() > 0);
	}
	
	public void checkEvent(Event e) throws EvaluationException{
		
		if(this.edge.evaluatePredicateZstreamTypeCheck(e)){
			this.events.add(e);
			Profiling.numberOfBufferedEvents ++;
		}
		
		Profiling.numberOfCheckingEvents ++;
		
	}

	public Edge getEdge() {
		return edge;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	public String getEdgeTag() {
		return edgeTag;
	}

	public void setEdgeTag(String edgeTag) {
		this.edgeTag = edgeTag;
	}

	public long getOldestTimestamp() {
		return oldestTimestamp;
	}

	public void setOldestTimestamp(int oldestTimestamp) {
		this.oldestTimestamp = oldestTimestamp;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public int getStackNumber() {
		return stackNumber;
	}

	public void setStackNumber(int stackNumber) {
		this.stackNumber = stackNumber;
	}

	public boolean isShouldBeCheckedForFull() {
		return shouldBeCheckedForFull;
	}

	public void setShouldBeCheckedForFull(boolean shouldBeCheckedForFull) {
		this.shouldBeCheckedForFull = shouldBeCheckedForFull;
	}
	
	
}
