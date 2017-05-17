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

import edu.umass.cs.sase.query.NFA;
import edu.umass.cs.sase.stream.Event;

public class UncertainRun extends Run {
	ArrayList<Long> lowerBounds;
	ArrayList<Long> upperBounds;
	
	ArrayList<Long> tempLowerBounds;
	ArrayList<Long> tempUpperBounds;
	
	public UncertainRun(){
		super();
		
	}
	
	public UncertainRun(NFA nfa){
		this.initializeRun(nfa);
	}
	
	
	void initializeRun(NFA nfa){
		super.initializeRun(nfa);
		this.lowerBounds = new ArrayList<Long>();
		this.upperBounds = new ArrayList<Long>();
	}
	
	void resetRun(){
		super.resetRun();
		
		this.lowerBounds = null;
		this.upperBounds = null;
	}
	
	public Object clone() throws CloneNotSupportedException{
		UncertainRun o = null;
		o = (UncertainRun)super.clone();
		
		o.setLowerBounds((ArrayList<Long>)this.getLowerBounds().clone());
		o.setUpperBounds((ArrayList<Long>)this.getUpperBounds().clone());
		return o;
		
	}
	
	public void addEvent(Event e){
		super.addEvent(e);
		if(this.getCount() == 1){
			this.getLowerBounds().add(e.getLowerBound());
			this.getUpperBounds().add(e.getUpperBound());
		}else{
			if(!ConfigFlags.usingPostponingOptimization){
				this.updateUncertaintyInterval(e);
			}
		}
		
	}

	public void updateUncertaintyInterval(Event e){
			//lower bound
			long previousLower = this.getLowerBounds().get(this.getCount() - 2);
			long currentLower = e.getLowerBound();
			if(previousLower + 1 > currentLower){
				this.getLowerBounds().add(previousLower + 1);
			}else{
				this.getLowerBounds().add(e.getLowerBound());
			}
			
			//update previous upper bounds
			this.getUpperBounds().add(e.getUpperBound());
			boolean needUpdate = true;
			int counter = this.count - 1;
			while(needUpdate && counter > 0){
				long thisUpper = this.getUpperBounds().get(counter);
				long previousUpper = this.getUpperBounds().get(counter - 1);
				if(thisUpper - 1 < previousUpper){
					this.getUpperBounds().set(counter - 1, thisUpper - 1);
					counter --;
				}else{
					needUpdate = false;
				}
			}
			
			//adjust upper bound
			long firstUpper = this.getUpperBounds().get(0);
			long currentUpper = e.getUpperBound();
			if(firstUpper + this.nfa.getTimeWindow() < currentUpper){
				this.getUpperBounds().set(this.getCount() - 1, firstUpper + this.nfa.getTimeWindow());
				//this.getUpperBounds().add(firstUpper + this.nfa.getTimeWindow());
				//update previous upper bounds
				needUpdate = true;
				counter = this.count - 1;
				while(needUpdate && counter > 0){
					long thisUpper = this.getUpperBounds().get(counter);
					long previousUpper = this.getUpperBounds().get(counter - 1);
					if(thisUpper - 1 < previousUpper){
						this.getUpperBounds().set(counter - 1, thisUpper - 1);
						counter --;
					}else{
						needUpdate = false;
					}
				}

			}
		}
	

	public boolean validateUncertaintyInterval(EventBuffer buffer){
		//first fill all 
		
		Event e;
		for(int i = 1; i < this.count; i ++){
			e = buffer.getEvent(this.getEventIds().get(i));
				//set lower
				if(this.getLowerBounds().get(i - 1) + 1 > e.getLowerBound()){
					this.getLowerBounds().add( this.getLowerBounds().get(i - 1) + 1);
				}else{
					this.getLowerBounds().add(e.getLowerBound());
				}
				//initialize upper
				this.getUpperBounds().add(e.getUpperBound());
				//validate first time
				if(this.getLowerBounds().get(i) > this.getUpperBounds().get(i)){
					return false;
				}
			}
		//update upperbounds for the first time
		for(int i = this.getCount() - 2; i >= 0; i --){
			if(this.getUpperBounds().get(i + 1) - 1 < this.getUpperBounds().get(i)){
				this.getUpperBounds().set(i, this.getUpperBounds().get(i + 1) - 1);
				//validate second time
				if(this.getLowerBounds().get(i) > this.getUpperBounds().get(i)){
					return false;
				}
			}
		}
		//operate on upper 
		if(this.getUpperBounds().get(0) + this.nfa.getTimeWindow() < this.getUpperBounds().get(this.count - 1)){
			this.upperBounds.set(this.count - 1, this.upperBounds.get(0) + this.nfa.getTimeWindow());
			//validate third time
			if(this.lowerBounds.get(size - 1) > this.getUpperBounds().get(size - 1)){
				return false;
			}
			for(int i = this.count - 2; i >= 0; i --){
				if(this.getUpperBounds().get(i + 1) - 1 < this.getUpperBounds().get(i)){
					this.getUpperBounds().set(i, this.getUpperBounds().get(i + 1) - 1);
					//validate fourth time
					if(this.getLowerBounds().get(i) > this.getUpperBounds().get(i)){
						return false;
					}
				}
			}
		}
		
		
		return true;
	}
	public int checkTimeWindowUncertain(Event e){
		//-1, too eary, Action: skip
		//1, ok, Action: take the event
		//0, false, too late, remove the run
		//2, not late, but no valid interval, skip.
		if(e.getUpperBound() <= this.getLowerBounds().get(0)){
			return - 1;
		}
		
		
		if(ConfigFlags.usingPostponingOptimization){
			if((e.getLowerBound() - this.getUpperBounds().get(0) <= this.nfa.getTimeWindow())){
				return 1;
			}else{
				return 0;
			}
		}else{
			if(e.getLowerBound() - this.getUpperBounds().get(0) > this.nfa.getTimeWindow() ){
				return 0;
			}
			
			long lastLower;
			long lastUpper;
			
			long previousLower = this.getLowerBounds().get(this.getCount() - 1);
			long currentLower = e.getLowerBound();
			if(previousLower + 1 > currentLower){
				lastLower = previousLower + 1;
			}else{
				lastLower = e.getLowerBound();
			}
			
			//upper bound
			long firstUpper = this.getUpperBounds().get(0);
			long currentUpper = e.getUpperBound();
			if(firstUpper + this.nfa.getTimeWindow() < currentUpper){
				lastUpper = firstUpper + this.nfa.getTimeWindow();
			}else{
				lastUpper = e.getUpperBound();
			}
			
			
			if(lastUpper < lastLower){
				return 2;
			}
			//update previous upper bounds
			int counter = this.count - 1;
			long testUpper;
			do{
				testUpper = this.getUpperBounds().get(counter);
				if(lastUpper - 1 < testUpper){
					testUpper = lastUpper - 1;
				}
				if(testUpper < this.getLowerBounds().get(counter)){
					return 2;
				}else{
					lastUpper = testUpper;
					counter --;
				}
				
			}
			while(counter > 0);

			return 1;
		}
		
	}
	
	/**
	 * Check if the timewindow is exceeded.
	 * @param e
	 * @return
	 */
	public boolean checkTimeWindowLarge(Event e){
		if(e.getLowerBound() - this.getUpperBounds().get(0) > this.nfa.getTimeWindow() ){
			return true;
		}
		return false;
	}
	
	public boolean checkTimeWindow(Event e){
		if(ConfigFlags.usingPostponingOptimization){
			if((e.getLowerBound() - this.getUpperBounds().get(0) <= this.nfa.getTimeWindow()) &&
					e.getUpperBound() > this.getLowerBounds().get(0)){
				return true;
			}else{
				return false;
			}
		}else{
			if(e.getLowerBound() - this.getUpperBounds().get(0) > this.nfa.getTimeWindow() ){
				return false;
			}
			long lastLower;
			long lastUpper;
			
			long previousLower = this.getLowerBounds().get(this.getCount() - 1);
			long currentLower = e.getLowerBound();
			if(previousLower + 1 > currentLower){
				lastLower = previousLower + 1;
			}else{
				lastLower = e.getLowerBound();
			}
			//upper bound
			long firstUpper = this.getUpperBounds().get(0);
			long currentUpper = e.getUpperBound();
			if(firstUpper + this.nfa.getTimeWindow() < currentUpper){
				lastUpper = firstUpper + this.nfa.getTimeWindow();
			}else{
				lastUpper = e.getUpperBound();
			}
			if(lastUpper < lastLower){
				return false;
			}
			//update previous upper bounds
			int counter = this.count - 1;
			long testUpper;
			do{
				testUpper = this.getUpperBounds().get(counter);
				if(lastUpper - 1 < testUpper){
					testUpper = lastUpper - 1;
				}
				if(testUpper < this.getLowerBounds().get(counter)){
					return false;
				}else{
					lastUpper = testUpper;
					counter --;
				}
			}
			while(counter > 0);

			return true;
		}
	}
	
	
	public ArrayList<Long> getLowerBounds() {
		return lowerBounds;
	}

	public void setLowerBounds(ArrayList<Long> lowerBounds) {
		this.lowerBounds = lowerBounds;
	}

	public ArrayList<Long> getUpperBounds() {
		return upperBounds;
	}

	public void setUpperBounds(ArrayList<Long> upperBounds) {
		this.upperBounds = upperBounds;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < this.count; i ++){
			sb.append("NO." + i + ":" + this.getEventIds() + " , lowerBound=" + this.getLowerBounds().get(i));
			sb.append(", upperBound=" + this.getUpperBounds().get(i));
		}
		return sb.toString();
	}
	
}
