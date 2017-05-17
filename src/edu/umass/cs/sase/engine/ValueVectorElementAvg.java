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


import edu.umass.cs.sase.stream.Event;

/**
 * This class represents the AVG aggregation.
 * @author haopeng
 *
 */
public class ValueVectorElementAvg implements ValueVectorElement, Cloneable{
	int stateNumber;
	String attribute;
	int count;
	int sum;
	
	int neededByState;
	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.engine.ValueVectorElement#getValue()
	 */
	public int getValue() {
		return (this.sum /this.count );
	}
	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.engine.ValueVectorElement#updateValue(edu.umass.cs.sase.stream.Event)
	 */
	public void updateValue(Event e) {
		this.count ++;
		this.sum += e.getAttributeByName(attribute);
		
	}
	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.engine.ValueVectorElement#getStateNumber()
	 */
	public int getStateNumber() {
		return this.stateNumber;
	}
	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.engine.ValueVectorElement#getType()
	 */
	public String getType() {
		return "avg";
	}
	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.engine.ValueVectorElement#getAttribute()
	 */
	public String getAttribute() {
		return this.attribute;
	}
	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.engine.ValueVectorElement#initializeValue(edu.umass.cs.sase.stream.Event)
	 */
	public void initializeValue(Event e) {
		this.count = 1;
		this.sum = e.getAttributeByName(attribute);
		
	}
	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.engine.ValueVectorElement#setAttribute(java.lang.String)
	 */
	public void setAttribute(String a) {
		this.attribute = a;
		
	}
	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.engine.ValueVectorElement#getNeededByState()
	 */
	public int getNeededByState() {
		return this.neededByState;
	}
	/**
	 * @param neededByState the neededByState to set
	 */
	public void setNeededByState(int neededByState) {
		this.neededByState = neededByState;
	}
	/**
	 * Clones the run itself
	 */
	public Object clone() throws CloneNotSupportedException{
		ValueVectorElementAvg o = null;
		o = (ValueVectorElementAvg)super.clone();
		return o;
	}
	
}
