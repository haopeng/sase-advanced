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

import edu.umass.cs.sase.engine.ConfigFlags;

/**
 * @author haopeng
 * 
 *this class is used to wrap the event from hadoop logs
 */
public class HadoopEvent implements Event{

	int Id;
	String eventType;// maybe mapattemptStart, or mapattemptFinish  or reduceAttemptStart or ReduceAttemptFinish
	// or pullstart pullfinish  or requeststart requestfinish or mergestart mergefinish
	//or JobStart, JobFinish
	//MapPeriod, ReducePeriod
	//DataActivity
	long jobId;
	int taskId;
	int attemptID;
	int nodeNumber;
	long timestamp;// the unit is millisecond, and we will omit the first "1266" in timestamps, incase of overflow
	long upperBound;
	long lowerBound;
	int originalEventId;
	int value;//this attribute is used to store some numeric values
	
	int numberOfSegments;
	public HadoopEvent(){
		
	}
	
	public HadoopEvent(int Id,String eventType, long jobId, int taskId, int attemptId, int nodeNumber, long timestamp, long upperBound, long lowerBound, int originalEventId, int value){
		this.Id = Id;
		this.eventType = eventType;
		this.jobId = jobId;
		this.taskId = taskId;
		this.attemptID = attemptId;
		this.nodeNumber = nodeNumber;
		this.timestamp = timestamp;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.originalEventId = originalEventId;
		this.value = value;
	}
	
	
	
	public HadoopEvent(int Id,String eventType, long jobId,int taskId, int attemptId, int nodeNumber, long timestamp, long upperBound, long lowerBound, int originalEventId, int value, int numberOfSegments){
		this.Id = Id;
		this.eventType = eventType;
		this.jobId = jobId;
		this.taskId = taskId;
		this.attemptID = attemptId;
		this.nodeNumber = nodeNumber;
		this.timestamp = timestamp;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.originalEventId = originalEventId;
		this.value = value;
		this.numberOfSegments = numberOfSegments;
	}

	public HadoopEvent(String line){
		this.setAttributesByLine(line);
		
	}
	
	public void setAttributesByLine(String line){
		//example line: EventType=MergeFinish	Id=0	taskId=2	attemptId=0	nodeNumber=105	timestamp=1373596006209	lowerBound=13730	upperBound=13730	originalEventId=0	numberOfSegments=1000	
		String attribute;
		long timestamp;
		StringTokenizer st = new StringTokenizer(line);
		String attributeValue;
		while(st.hasMoreElements()){
			attribute = st.nextToken();
			if(attribute.startsWith("EventType")){
				this.setEventType(attribute.substring(attribute.indexOf('=') + 1));
			}else if(attribute.startsWith("nodeNumber")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				this.setNodeNumber(Integer.parseInt(attributeValue));
			}else if(attribute.startsWith("timestamp")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				timestamp = Long.parseLong(attributeValue);
				//timestamp = timestamp / ConfigFlags.timeUnit;
				this.setTimestamp(timestamp);

			}else if(attribute.startsWith("attemptId")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				this.setAttemptID(Integer.parseInt(attributeValue));
			}else if(attribute.startsWith("taskId")){
				this.setTaskId(Integer.parseInt(attribute.substring(attribute.indexOf('=') + 1 )));
			}else if(attribute.startsWith("numberOfSegments")){
				this.setNumberOfSegments(Integer.parseInt(attribute.substring(attribute.indexOf('=')+1)));
			}else if(attribute.startsWith("jobId")){
				this.setJobId(Long.parseLong(attribute.substring(attribute.indexOf('=')+1)));
			}else if(attribute.startsWith("value")){
				this.setValue(Integer.parseInt(attribute.substring(attribute.indexOf('=') + 1)));
			}
		}
	}
	public String attributeNameToCSV(){
		StringBuilder sb = new StringBuilder();
		sb.append("LogSource,");
		sb.append("eventType,");
		sb.append("id,");
		sb.append("jobId,");
		sb.append("taskId,");
		sb.append("attemptID,");
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
		sb.append("h,");
		sb.append(this.getEventType() + ",");
		sb.append(this.getId() + ",");
		sb.append(this.getJobId() + ",");
		sb.append(this.getTaskId() + ",");
		sb.append(this.getAttemptID() + ",");
		sb.append(this.getNodeNumber() + ",");
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
		token = st.nextToken();//skip the first h
		this.eventType = token;
		token = st.nextToken();
		this.Id = Integer.parseInt(token);
		token = st.nextToken();
		this.jobId = Integer.parseInt(token);
		token = st.nextToken();
		this.taskId = Integer.parseInt(token);
		token = st.nextToken();
		this.attemptID = Integer.parseInt(token);
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
		sb.append("jobId=" + this.jobId + "\t");
		

		sb.append("taskId=" + this.taskId + "\t");
		sb.append("attemptId=" + this.attemptID + "\t");
		sb.append("nodeNumber=" + this.nodeNumber + "\t");
		
		/*2013
		sb.append("timestamp=1373" + this.timestamp + "\t");// when print the timestamp, we attach the 1266, such that we can get the correct time
		sb.append("lowerBound=1373" + this.lowerBound + "\t");
		sb.append("upperBound=1373" + this.upperBound + "\t");
		*/
		
		sb.append("timestamp=" + this.timestamp + "\t");// when print the timestamp, we attach the 1266, such that we can get the correct time
		sb.append("lowerBound=" + this.lowerBound + "\t");
		sb.append("upperBound=" + this.upperBound + "\t");
		
		
		sb.append("originalEventId=" + this.originalEventId + "\t" );
		sb.append("value=" + this.value + "\t");
		if(this.eventType.equalsIgnoreCase("mergestart") || this.eventType.equalsIgnoreCase("mergefinish")){
			sb.append("numberOfSegments=" + this.numberOfSegments + "\t");
		}
		sb.append("\n");
		return sb.toString();
		
	}
		public Object clone(){
		HadoopEvent o = null;
		o = new HadoopEvent(this.Id, this.eventType,this.jobId, this.taskId, this.attemptID, this.nodeNumber, this.timestamp, this.upperBound, this.lowerBound, this.originalEventId, this.value, this.numberOfSegments);
		return o;
	}
	
	public int getAttributeByName(String attributeName) {
		if(attributeName.equalsIgnoreCase("nodeNumber")){
			return this.nodeNumber;
		}else if(attributeName.equalsIgnoreCase("originalEventId")){
			return this.originalEventId;
		}else if(attributeName.equalsIgnoreCase("taskId")){
			return this.taskId;
		}else if(attributeName.equalsIgnoreCase("attemptId")){
			return this.attemptID;
		}else if(attributeName.equalsIgnoreCase("numberOfSegments")){
			return this.numberOfSegments;
		}else if(attributeName.equalsIgnoreCase("value")){
			return this.value;
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
		if(attributeName.equalsIgnoreCase("nodeNumber")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("taskId")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("attemptId")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("lowerBound")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("upperBound")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("originalEventId")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("numberOfSegments")){
			return 0;
		}else if(attributeName.equalsIgnoreCase("jobId")){
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
	 * @return the taskId
	 */
	public int getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the attemptID
	 */
	public int getAttemptID() {
		return attemptID;
	}

	/**
	 * @param attemptID the attemptID to set
	 */
	public void setAttemptID(int attemptID) {
		this.attemptID = attemptID;
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
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the numberOfSegments
	 */
	public int getNumberOfSegments() {
		return numberOfSegments;
	}

	/**
	 * @param numberOfSegments the numberOfSegments to set
	 */
	public void setNumberOfSegments(int numberOfSegments) {
		this.numberOfSegments = numberOfSegments;
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

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toStringSelectedContentOnly() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("h,");
		sb.append(this.timestamp + ",");
		sb.append(this.getEventType() + ",");
		sb.append(this.jobId + ",");
		sb.append(this.taskId + ",");
		sb.append(this.attemptID + ",");
		sb.append(this.nodeNumber + ",");
		sb.append(this.value + ",");
		sb.append(this.numberOfSegments);
		return sb.toString();
	}

	public void setAttributesBySelectedContent(String line){
		//h,596006209,MergeFinish,2,0,105,1000
		StringTokenizer st = new StringTokenizer(line, ",");
		//skip the header
		String token = st.nextToken().trim();
		//timestamp
		token = st.nextToken().trim();
		this.timestamp = Long.parseLong(token);
		//eventtype
		token = st.nextToken().trim();
		this.eventType = token;
		//jobId
		token = st.nextToken().trim();
		this.jobId = Long.parseLong(token);
		//taskid
		token = st.nextToken().trim();
		this.taskId = Integer.parseInt(token);
		//attepmtId
		token = st.nextToken().trim();
		this.attemptID = Integer.parseInt(token);
		//nodeNumber
		token = st.nextToken().trim();
		this.nodeNumber = Integer.parseInt(token);
		//value
		token = st.nextToken().trim();
		this.value = Integer.parseInt(token);
		//numberOfSegments
		token = st.nextToken().trim();
		this.numberOfSegments = Integer.parseInt(token);
	}
	
	
	
public static void main(String args[]){
	String l = "EventType=MergeFinish	Id=0 jobId=2013009	taskId=2	attemptId=0	nodeNumber=105	timestamp=1373596006209	lowerBound=13730	upperBound=13730	originalEventId=0	numberOfSegments=1000	";
	l = "EventType=JobFinish	Id=0	jobId=819100009	taskId=0	attemptId=0	nodeNumber=0	timestamp=1373359554853	lowerBound=13730	upperBound=13730	originalEventId=0	value=0	";
	HadoopEvent e = new HadoopEvent();
	e.setAttributesByLine(l);
	System.out.println(l);
	System.out.println(e.toStringSelectedContentOnly());
	HadoopEvent e2 = new HadoopEvent();
	String t = "h,686326793,PullStart,1115040027,3695,0,110,0,0";
	e2.setAttributesBySelectedContent(t);
	System.out.println(e2);
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
