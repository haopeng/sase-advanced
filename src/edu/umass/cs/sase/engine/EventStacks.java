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
import edu.umass.cs.sase.query.*;
import edu.umass.cs.sase.stream.*;

/*
 * Used for ZStream optimization
 */

import java.util.*;

import net.sourceforge.jeval.EvaluationException;
public class EventStacks {
	NFA nfa;
	int size;
	ArrayList<Event> stacks[];
	long lastTimestamp;
	
	public EventStacks(NFA n){
		this.nfa = n;
		this.size = this.nfa.getSize();
		stacks = new ArrayList[size];
		for(int i = 0; i < size; i ++){
			stacks[i] = new ArrayList<Event>();
		}
	}
	public void putEvent(Event e, int position){
		this.stacks[position].add(e);
		this.lastTimestamp = e.getTimestamp();
	}
	public boolean hasMoreEvent(int position){
		if(this.stacks[position].size() > 0){
			return true;
		}else{
			return false;
		}
	}
	public Event popEvent(int position){
		if(this.stacks[position].size() > 0){
			Event e = this.stacks[position].get(0);
			this.stacks[position].remove(0);
			return e;
		}else{
			return null;
		}
		
	}
	
	public boolean isAllFull(){
		for(int i = 0; i < this.size; i ++){
			if(this.stacks[i].size() == 0){
				return false;
			}
		}
		return true;
	}
	
	public void initialize(){
		stacks = new ArrayList[size];
		for(int i = 0; i < size; i ++){
			stacks[i] = new ArrayList<Event>();
		}
	}
	
	public void removeExpiredEvent(){
		//todo , optimize, avoid repeated checking
		for(int i = 0; i < this.size; i ++){
			boolean goon = true;
			Event e;
			while(goon && this.hasMoreEvent(i)){
				e = this.stacks[i].get(0);
				if(this.lastTimestamp - e.getTimestamp() > this.nfa.getTimeWindow()){
					this.stacks[i].remove(0);
					
				}else{
					goon = false;
				}
			}
		}
	}
	
	
	public void checkEvent(Event e) throws EvaluationException{
		
		boolean typeCheck = false;
		
		int counter = 0;
		while(counter < this.nfa.getSize()){
			Profiling.numberOfCheckingEvents ++;
			typeCheck = this.nfa.getStates(counter).stackTypeCheck(e);
			if(typeCheck){
				this.putEvent(e, counter);
				Profiling.numberOfBufferedEvents ++;
			}
			counter ++;
		}
	}
	
}
