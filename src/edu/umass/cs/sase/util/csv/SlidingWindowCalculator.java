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
package edu.umass.cs.sase.util.csv;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SlidingWindowCalculator {
	long windowSize;// microsceonds
	int count;
	double sum;
	PriorityQueue<AttributeValue> max;
	PriorityQueue<AttributeValue> min;
	
	ArrayList<AttributeValue> valuesInWindow;
	
	String attributeName;
	
	long currentTimestamp;
	
	public SlidingWindowCalculator(String attr, long window){
		this.valuesInWindow = new ArrayList<AttributeValue>();
		this.attributeName = attr;
		this.windowSize = window;
		this.count = 0;
		this.sum = 0.0;
		
		this.max = new PriorityQueue<AttributeValue>(30, new Comparator<AttributeValue>(){
			public int compare(AttributeValue v1, AttributeValue v2){
				Double value1 = v1.getValue();
				Double value2 = v2.getValue();
				return value2.compareTo(value1);
			}
		});
		
		this.min = new PriorityQueue<AttributeValue>(30, new Comparator<AttributeValue>(){
			public int compare(AttributeValue v1, AttributeValue v2){
				Double value1 = v1.getValue();
				Double value2 = v2.getValue();
				return value1.compareTo(value2);
			}
		});
	}
	
	public void addValue(AttributeValue v){
		this.currentTimestamp = v.getTimestamp();
		boolean done = false;
		int count = 0;
		while(!done && count < this.valuesInWindow.size()){
			if(this.currentTimestamp - this.valuesInWindow.get(0).getTimestamp() > this.windowSize){
				AttributeValue valueToRemove = this.valuesInWindow.remove(0);
				this.count --;
				this.sum -= valueToRemove.value;
				this.max.remove(valueToRemove);
				this.min.remove(valueToRemove);
			}else{
				done = true;
			}
		
		}
		
		this.valuesInWindow.add(v);
		this.count ++;
		this.sum += v.getValue();
		this.max.add(v);
		this.min.add(v);
	}
	public double getAverage(){
		return this.sum / this.count;
	}
	public double getMax(){
		return this.max.peek().getValue();
	}
	public double getMin(){
		return this.min.peek().getValue();
	}
	public long getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(long windowSize) {
		this.windowSize = windowSize;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	

	
	
	public ArrayList<AttributeValue> getValuesInWindow() {
		return valuesInWindow;
	}

	public void setValuesInWindow(ArrayList<AttributeValue> valuesInWindow) {
		this.valuesInWindow = valuesInWindow;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public long getCurrentTimestamp() {
		return currentTimestamp;
	}

	public void setCurrentTimestamp(long currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}
	
	
	
	
	
	
}
