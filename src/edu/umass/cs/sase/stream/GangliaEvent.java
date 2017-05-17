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

import java.util.StringTokenizer;


/**
 * @author haopeng
 *
 */
public class GangliaEvent implements Event{
	
	int Id;
	String eventType;// maybe mapattemptStart, or mapattemptFinish  or reduceAttemptStart or ReduceAttemptFinish
	int nodeNumber;
	long timestamp;// the unit is millisecond, and we will omit the first "1266" in timestamps, incase of overflow
	int value;
	long upperBound;
	long lowerBound;
	int originalEventId;
	
	public GangliaEvent(){
		
	}
	public Object clone(){
		GangliaEvent o = null;
		o = new GangliaEvent(this.Id, this.eventType, this.nodeNumber, this.timestamp, this.value, this.upperBound, this.lowerBound,this.originalEventId);
		return o;
	}
	public GangliaEvent(int Id, String eventType, int nodeNumber, long timestamp, int value, long upperBound, long lowerBound, int originalEventId){
		this.Id = Id;
		this.nodeNumber = nodeNumber;
		this.timestamp = timestamp;
		this.value = value;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.originalEventId = originalEventId;
	}
	public String attributeNameToCSV(){
		StringBuilder sb = new StringBuilder();
		sb.append("LogSource,");
		sb.append("eventType,");
		sb.append("id,");
		sb.append("nodeNumber,");
		sb.append("timestamp,");
		sb.append("lowerBound,");
		sb.append("upperBound,");
		sb.append("originalEventId,");
		sb.append("value");
		return sb.toString();
	}
	
	public String toCSV(){
		StringBuilder sb = new StringBuilder();
		sb.append("g,");
		sb.append(this.getEventType() + ",");
		sb.append(this.getId() + ",");
		sb.append(this.getNodeNumber() + ",");
		/*
		sb.append("1373" + this.timestamp + ",");
		sb.append("1373" + this.getLowerBound() + ",");
		sb.append("1373" + this.getUpperBound() + ",");
		*/
		sb.append(this.timestamp + ",");
		sb.append(this.getLowerBound() + ",");
		sb.append(this.getUpperBound() + ",");
		
		
		sb.append(this.originalEventId + ",");
		sb.append(this.getValue());
		return sb.toString();
	}
	
	public void setAttributeByCSV(String csvLine){
		//example
		//balance-standarddeviation,0,0,1373166015000,13730,13730,0,0
		StringTokenizer st = new StringTokenizer(csvLine, ",");
		String token = st.nextToken();
		token = st.nextToken(); //skip the first g
		this.eventType = token;
		token = st.nextToken();
		this.Id = Integer.parseInt(token);
		token = st.nextToken();
		this.nodeNumber = Integer.parseInt(token);
		token = st.nextToken().substring(4);
		this.timestamp = Integer.parseInt(token);
		token = st.nextToken().substring(4);
		this.lowerBound = Integer.parseInt(token);
		token = st.nextToken().substring(4);
		this.upperBound = Integer.parseInt(token);
		token = st.nextToken();
		this.originalEventId = Integer.parseInt(token);
		token = st.nextToken();
		this.value = Integer.parseInt(token);
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("EventType=" + this.getEventType() + "\t");
		sb.append("Id=" + this.getId() + "\t");
		sb.append("nodeNumber=" + this.nodeNumber + "\t");
		sb.append("timestamp=1373" + this.timestamp + "\t");// when print the timestamp, we attach the 1266, such that we can get the correct time
		sb.append("value=" + this.value + "\t");
		sb.append("lowerBound=1373" + this.lowerBound + "\t");
		sb.append("upperBound=1373" + this.upperBound + "\t");
		sb.append("originalEventId=" + this.originalEventId + "\t" +"\n");
		return sb.toString();
		
	}
	public String toStringSelectedContentOnly(){
		StringBuilder sb = new StringBuilder();
		sb.append("g,");
		sb.append(this.getTimestamp() + ",");
		sb.append(this.getEventType() + ",");
		sb.append(this.nodeNumber + ",");
		sb.append(this.value + ",");
		return sb.toString();
	}

	public void setAttributesBySelectedContent(String line){
		//g,999985000,proc_run_event,115,1,
		StringTokenizer st = new StringTokenizer(line, ",");
		//skip the header
		String token = st.nextToken().trim();
		//timestamp
		token = st.nextToken().trim();
		this.timestamp = Integer.parseInt(token);
		//eventtype
		token = st.nextToken().trim();
		this.eventType = token;
		//nodeNumber
		token = st.nextToken().trim();
		this.nodeNumber = Integer.parseInt(token);
		//value
		token = st.nextToken().trim();
		this.value = Integer.parseInt(token);
	}

	public String toStringSimpler(){
		StringBuilder sb = new StringBuilder();
		sb.append("EventType=" + this.getEventType() + "\t");
		sb.append("nodeNumber=" + this.nodeNumber + "\t");
		sb.append("timestamp=1266" + this.timestamp + "\t");// when print the timestamp, we attach the 1266, such that we can get the correct time
		sb.append("value=" + this.value + "\t" +"\n");
		return sb.toString();
		
	}
	
	public String toStringOnlyData(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getEventType() + "\t");
		sb.append(this.nodeNumber + "\t");
		sb.append(this.timestamp + "\t");// when print the timestamp, we attach the 1266, such that we can get the correct time
		sb.append(this.value + "\t" +"\n");
		return sb.toString();
		
	}
	
	public String toStringOnlyTimeAndValue(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.timestamp + "\t");// when print the timestamp, we attach the 1266, such that we can get the correct time
		sb.append(this.value  +"\n");
		return sb.toString();
		
	}

	
	public String toStringOnlyTimeAndValueSeparatedByComma(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.timestamp + ",");// when print the timestamp, we attach the 1266, such that we can get the correct time
		sb.append(this.value +"\n");
		return sb.toString();
		
	}
	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.mvc.model.Event#getAttributeByName(java.lang.String)
	 */
	//@Override
	public int getAttributeByName(String attributeName) {
		// TODO Auto-generated method stub
		
		if(attributeName.equalsIgnoreCase("nodeNumber")){
			return this.nodeNumber;
		}else if(attributeName.equalsIgnoreCase("value")){
			return this.value;
		}else if(attributeName.equalsIgnoreCase("originalEventId")){
			return this.originalEventId;
		}
		
		return 0;
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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.mvc.model.Event#getAttributeValueType(java.lang.String)
	 */
	//@Override
	public int getAttributeValueType(String attributeName) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		//0-integer, 1-double, 2-string;-1:error, no such attribute
		if(attributeName.equalsIgnoreCase("nodeNumber")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("lowerBound")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("upperBound")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("originalEventId")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("value")){
			return 0;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see edu.umass.cs.sase.mvc.model.Event#getEventType()
	 */
	//@Override
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
		// TODO Auto-generated method stub
		return this.timestamp;
	}

	/**
	 * @return the nodeNumber
	 */
	public int getNodeNumber() {
		return nodeNumber;
	}

	/**
	 * @param nodeNumber the nodeNumber to set
	 */
	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
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
	public void setUpperBound(long upperBound) {
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
	public void setLowerBound(long lowerBound) {
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
	 * @param eventType the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
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
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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
