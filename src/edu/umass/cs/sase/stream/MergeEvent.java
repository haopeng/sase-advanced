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


/**
 * @author haopeng
 *
 */
public class MergeEvent implements Event{
	int Id;
	String eventType;// maybe mergeStart, or mergeFinish  
	int numberOfSegement; //Initiating in-memory merge with 167 segments...
	int timestamp;// the unit is millisecond, and we will omit the first "1266" in timestamps, incase of overflow
	int upperBound;
	int lowerBound;
	int originalEventId;
	
	public MergeEvent(){
		
	}
	public String attributeNameToCSV(){
		return null;
	}

	public String toCSV(){
		return null;
	}
	public MergeEvent(int Id,String eventType,  int numberOfSegement,int timestamp, int upperBound, int lowerBound, int originalEventId){
		this.Id = Id;
		this.eventType = eventType;
		this.numberOfSegement = numberOfSegement;
		this.timestamp = timestamp;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.originalEventId = originalEventId;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("EventType=" + this.getEventType() + "\t");
		sb.append("Id=" + this.getId() + "\t");
		sb.append("numberOfSegement=" + this.numberOfSegement + "\t");
		
		sb.append("timestamp=1266" + this.timestamp + "\t");// when print the timestamp, we attach the 1266, such that we can get the correct time
		sb.append("lowerBound=1266" + this.lowerBound + "\t");
		sb.append("upperBound=1266" + this.upperBound + "\t");
		sb.append("originalEventId=" + this.originalEventId + "\t" +"\n");
		return sb.toString();
		
	}
	
	public Object clone(){
		MergeEvent o = null;
		o = new MergeEvent(this.Id, this.eventType, this.numberOfSegement,  this.timestamp, this.upperBound, this.lowerBound, this.originalEventId);
		return o;
	}
	
	public int getAttributeByName(String attributeName) {
		if(attributeName.equalsIgnoreCase("numberOfSegement")){
			return this.numberOfSegement;
		}else if(attributeName.equalsIgnoreCase("lowerBound")){
			return this.lowerBound;
		}else if(attributeName.equalsIgnoreCase("upperBound")){
			return this.upperBound;
		}else if(attributeName.equalsIgnoreCase("originalEventId")){
			return this.originalEventId;
		}
		return -1;//error
	}

	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.mvc.model.Event#getAttributeByNameDouble(java.lang.String)
	 */
	//@Override
	public double getAttributeByNameDouble(String attributeName) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.mvc.model.Event#getAttributeByNameString(java.lang.String)
	 */
	//@Override
	public String getAttributeByNameString(String attributeName) {
		 
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.mvc.model.Event#getAttributeValueType(java.lang.String)
	 */
	//@Override
	public int getAttributeValueType(String attributeName) {
		// TODO Auto-generated method stub
		//0-integer, 1-double, 2-string;-1:error, no such attribute
		if(attributeName.equalsIgnoreCase("numberOfSegement")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("lowerBound")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("upperBound")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("originalEventId")){
			return 0;
		}
		return -1;
	}

	
	public String getEventType() {
		// TODO Auto-generated method stub
		return this.eventType;
	}

	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.mvc.model.Event#getId()
	 */
	//@Override
	public int getId() {
		// TODO Auto-generated method stub
		return this.Id;
	}

	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.mvc.model.Event#getTimestamp()
	 */
	//@Override
	public long getTimestamp() {
		
		return this.timestamp;
	}

	


	/**
	 * @return the upperBound
	 */
	public long getUpperBound() {
		return upperBound;
	}

	/**
	 * @param upperBound the upperBound to set
	 */
	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	/**
	 * @return the lowerBound
	 */
	public long getLowerBound() {
		return lowerBound;
	}

	/**
	 * @param lowerBound the lowerBound to set
	 */
	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	/**
	 * @return the originalEventId
	 */
	public int getOriginalEventId() {
		return originalEventId;
	}

	/**
	 * @param originalEventId the originalEventId to set
	 */
	public void setOriginalEventId(int originalEventId) {
		this.originalEventId = originalEventId;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		Id = id;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the numberOfSegement
	 */
	public int getNumberOfSegement() {
		return numberOfSegement;
	}

	/**
	 * @param numberOfSegement the numberOfSegement to set
	 */
	public void setNumberOfSegement(int numberOfSegement) {
		this.numberOfSegement = numberOfSegement;
	}

	@Override
	public boolean isSafe() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSafe(boolean isSafe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getProbabilityForPoint(long timestamp) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getProbabilityForRange(long lower, long upper) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toStringSelectedContentOnly() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] toStringArray() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] attributeNameToStringArray() {
		// TODO Auto-generated method stub
		return null;
	}

	




	


}
