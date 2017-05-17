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
import java.util.HashMap;

import net.sourceforge.jeval.EvaluationException;

import edu.umass.cs.sase.query.Edge;
import edu.umass.cs.sase.query.NFA;
import edu.umass.cs.sase.query.State;
import edu.umass.cs.sase.stream.Event;

/**
 * Used for ZStream optimization
 * The group of SingleEventStack
 * @author haopeng
 *
 */
public class EventStackGroup {

	ArrayList<SingleEventStack> stacks;
	
	NFA nfa;
	
	int numberOfStacks;
	
	Event lastEvent;
	/**
	 * The stack which gets full 
	 */
	int lastFullStackNumber;
	
	/**
	 * The two variables are used to store the youngest/oldest timestamp of the runs 
	 */
	long youngestTimestamp;
	long oldestTimestamp;
	
	
	public EventStackGroup(NFA nfa){
		this.nfa = nfa;
		this.stacks = new ArrayList<SingleEventStack>();
		this.initializeStacks();
		this.numberOfStacks = this.stacks.size();
	}
	
	public SingleEventStack getStackAtPosition(int i){
		return this.stacks.get(i);
	}
	
	
	
	public boolean isAllFull(){
		for(int i = 0; i < this.numberOfStacks; i ++){
			if( (!this.stacks.get(i).hasMoreEvents()) && this.stacks.get(i).shouldBeCheckedForFull){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Initialize as the nfa
	 */
	public void initializeStacks(){
		int counter = 0;
		for(int i = 0; i < this.nfa.getSize(); i ++){
			State s = this.nfa.getStates(i);
			if(!s.isKleeneClosure()){
				Edge e = s.getEdges(0);
				String tag = e.getEdgeTag();
				SingleEventStack newStack = new SingleEventStack(tag, e, counter);
				newStack.shouldBeCheckedForFull = true;
				counter ++;
				this.stacks.add(newStack);
			}else{
				Edge beginE = s.getEdges(0);
				String beginTag = beginE.getEdgeTag();
				SingleEventStack beginStack = new SingleEventStack(beginTag, beginE, counter);
				beginStack.shouldBeCheckedForFull = true;
				counter ++;
				this.stacks.add(beginStack);

				Edge takeE = s.getEdges(1);
				String takeTag = takeE.getEdgeTag();
				SingleEventStack takeStack = new SingleEventStack(takeTag, takeE, counter);
				takeStack.shouldBeCheckedForFull = false;
				counter ++;
				this.stacks.add(takeStack);
			}
			
		}
		
	}
	
	public void removeExpiredEvent(HashMap<Integer, ArrayList<Run>> activeRunsByPartition, Event lastEvent){
		//reset the timestamps first
		this.youngestTimestamp = -1;
		this.oldestTimestamp = -1;
		this.computeRangeTimestamps(activeRunsByPartition);
		for(int i = 0; i < this.numberOfStacks; i ++){
			SingleEventStack stack = this.stacks.get(i);
			if(youngestTimestamp == -1){
				stack.expireEvents(lastEvent.getTimestamp(), this.nfa.getTimeWindow(), this.lastFullStackNumber);
			}else{
				stack.expireEvents(this.oldestTimestamp, this.youngestTimestamp, lastEvent.getTimestamp(), this.nfa.getTimeWindow(), this.lastFullStackNumber);
			}
		}
	}
	
	public void computeRangeTimestamps(HashMap<Integer, ArrayList<Run>> activeRunsByPartition){
		ArrayList<Run> runsInOnePartition;
		long tempTimestamp = 0;
		Run tempRun;
		for(Integer partitionId : activeRunsByPartition.keySet() ){
			runsInOnePartition = activeRunsByPartition.get(partitionId);
			for(int i = 0; i < runsInOnePartition.size(); i ++){
				tempRun = runsInOnePartition.get(i);
				tempTimestamp = tempRun.getStartTimeStamp();
				if(this.youngestTimestamp == -1){
					this.youngestTimestamp = tempTimestamp;
					this.oldestTimestamp = tempTimestamp;
				}else{
					if(tempTimestamp < this.oldestTimestamp){
						this.oldestTimestamp = tempTimestamp;
					}
					if(tempTimestamp > this.youngestTimestamp){
						this.youngestTimestamp = tempTimestamp;
					}
				}
			
		}
		}
	}
	
	public void checkEvent(Event e) throws EvaluationException{
		
		for(int i = 0; i < this.numberOfStacks; i ++){
			SingleEventStack stack = this.stacks.get(i);
			boolean isFullBefore = stack.hasMoreEvents();
			this.stacks.get(i).checkEvent(e);
			boolean isFullAfter = stack.hasMoreEvents();
			if(isFullBefore != isFullAfter){
				this.lastFullStackNumber = i;
			}
		}
	}

	public ArrayList<SingleEventStack> getStacks() {
		return stacks;
	}

	public void setStacks(ArrayList<SingleEventStack> stacks) {
		this.stacks = stacks;
	}

	public NFA getNfa() {
		return nfa;
	}

	public void setNfa(NFA nfa) {
		this.nfa = nfa;
	}

	public int getNumberOfStacks() {
		return numberOfStacks;
	}

	public void setNumberOfStacks(int numberOfStacks) {
		this.numberOfStacks = numberOfStacks;
	}
	
	
	
}
