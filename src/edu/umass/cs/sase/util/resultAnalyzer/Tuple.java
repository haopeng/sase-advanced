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
package edu.umass.cs.sase.util.resultAnalyzer;

import java.util.HashMap;
import java.util.StringTokenizer;

public class Tuple {
	Integer id;
	String tupleStr;
	String schemaStr;
	HashMap<String, String> attributes;
	
	public Tuple(String tupleString, String schemaString){
		this.tupleStr = tupleString;
		this.schemaStr = schemaString;
		this.attributes = new HashMap<String, String>();
		this.parseTuple();
	}
	public void parseTuple(){
		//parse id first
		String idStr = tupleStr.substring(0, tupleStr.indexOf('-'));
		this.id = Integer.parseInt(idStr);
		
		StringTokenizer tupleST = new StringTokenizer(this.tupleStr, "\t");
		StringTokenizer schemaST = new StringTokenizer(this.schemaStr, "\t");
		
		while(tupleST.hasMoreTokens() && schemaST.hasMoreTokens()){
			String schema = schemaST.nextToken();
			String tuple = tupleST.nextToken();
			this.attributes.put(schema, tuple);
			//System.out.println("schema is:" + schema + ", tuple is:"  + tuple);
		}
		
		
		
	}
	
	public String getAttributeValue(String attributeName){
		if(this.attributes.containsKey(attributeName)){
			return this.attributes.get(attributeName);
		}else{
			return null;
		}
	}
	
	public void printAllAttributes(){
		for(String key: this.attributes.keySet()){
			System.out.println(key + ":" + this.attributes.get(key));
		}
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTupleStr() {
		return tupleStr;
	}
	public void setTupleStr(String tupleStr) {
		this.tupleStr = tupleStr;
	}
	public String getSchemaStr() {
		return schemaStr;
	}
	public void setSchemaStr(String schemaStr) {
		this.schemaStr = schemaStr;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		

		String schema ="fileName	throughput	averageThroughput	selectivity	numOfRuns	patternMatchingPercentage	enumerationPercentage	confidencePercentage	numberOfEvents	numOfMatches	numOfRelevantEvents	numOfRunsReachedMatch	averageNumberOfEventsForRunsReachedMatch	maxNumberForRunsReachedMatch	maxNumberForMatch	numOfCollaspedMatches	machineName	";
		String tuple = "10-20130428-01:11:37.result	233130	 232958	0.00214	 662	 100.0%	 0.0%	0%	null	 214	406	null	null	null	 4	null	 compute-1-1.dbcluster	";
		Tuple t = new Tuple(tuple, schema);
		System.out.println(t.getId());
		System.out.println(t.getAttributeValue("numOfRuns"));
		t.printAllAttributes();
	}

}
