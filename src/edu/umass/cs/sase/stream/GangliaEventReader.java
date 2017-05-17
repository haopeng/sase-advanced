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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import edu.umass.cs.sase.engine.ConfigFlags;


/**
 * @author haopeng
 *
 */
public class GangliaEventReader {
	
	public static void main(String args[]) throws IOException{
		String inputFile = "d:\\ganglia-event\\boottime.0-6-events.txt";
		Event[] events = readFromFile(inputFile, 7500);
		for(int i = 0; i < events.length; i ++){
			System.out.println("the "+ i + "th event is: " + events[i]);
		}
	}
	
	public static Event[] readFromFile(String inputFile, int halfPeriod) throws IOException{
		ArrayList<Event> eventList = new ArrayList<Event>();
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line = br.readLine();
		while(line != null){
			eventList.add(parseLine(line, halfPeriod));
			line = br.readLine();
		}
		Event[] eventArray = new Event[eventList.size()];
		for(int i = 0; i < eventList.size(); i ++){
			eventArray[i] = eventList.get(i);
		}
		return eventArray;
	}
	
	public static Event parseLine(String line, int halfPeriod){
		GangliaEvent e = new GangliaEvent();
		String attribute;
		StringTokenizer st = new StringTokenizer(line);
		int timestamp;
		String attributeValue;
		while(st.hasMoreElements()){
			attribute = st.nextToken();
			if(attribute.startsWith("EventType")){
				e.setEventType(attribute.substring(attribute.indexOf('=') + 1));
			}else if(attribute.startsWith("nodeNumber")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				e.setNodeNumber(Integer.parseInt(attributeValue));
			}else if(attribute.startsWith("timestamp")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 5);
				timestamp = Integer.parseInt(attributeValue);
				timestamp = timestamp / ConfigFlags.timeUnit;
				e.setTimestamp(timestamp);
				e.setLowerBound(timestamp - ConfigFlags.gangliaHalfPeriod);
				e.setUpperBound(timestamp + ConfigFlags.gangliaHalfPeriod);
			}else if(attribute.startsWith("value")){
				attributeValue = attribute.substring(attribute.indexOf('=') + 1);
				e.setValue((int)Double.parseDouble(attributeValue));
			}
		}
		return e;
	}

}
