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

package edu.umass.cs.sase.stream;

public class UncertainStockEvent extends StockEvent implements
		Event {
	int lowerBound;
	int upperBound;
	public UncertainStockEvent(int id, int ts, int symbol, int price, int volume, int uncertaintyInterval){
		super(id, ts, symbol, price, volume);
		this.lowerBound = ts - uncertaintyInterval;
		this.upperBound = ts + uncertaintyInterval;
	}
	
	public UncertainStockEvent(int id, int ts, int symbol, int price, int volume, String type, int uncertaintyInterval){
		super(id, ts, symbol, price, volume, type);
		this.lowerBound = ts - uncertaintyInterval;
		this.upperBound = ts + uncertaintyInterval;
		
			
	}
	@Override
	public long getUpperBound() {
		// TODO Auto-generated method stub
		return this.upperBound;
	}

	@Override
	public long getLowerBound() {
		// TODO Auto-generated method stub
		return this.lowerBound;
	}
	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}
	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	public String toString(){
		return super.toString() + "\tLowerBound= " + this.lowerBound + "\tUpperBound= " + this.upperBound;
	}
	
	public double getProbabilityForPoint(int timestamp) {
		//todo , add boundry check
		double intervalLength = this.upperBound - this.lowerBound + 1;
		return 1.0 / intervalLength;
		// if we have other distriubtion, then the proability is different for different timestamp
		//current it is simply the same
	}
	
	public double getProbabilityForRange(int lower, int upper){
		//todo, add boundary check
		double fullLength = this.upperBound - this.lowerBound + 1;
		double rangeLength = upper - lower + 1;
		return rangeLength / fullLength;
	}
	
	public static void main(String args[]){
		Event e = new UncertainStockEvent(1, 2, 3, 4, 5, 2);
		System.out.println(e);
		System.out.println(e.getProbabilityForPoint(3));
		
		System.out.println(e.getProbabilityForRange(0, 4));
		
		
		
	}
	
}
