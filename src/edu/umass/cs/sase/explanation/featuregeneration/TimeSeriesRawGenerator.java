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
package edu.umass.cs.sase.explanation.featuregeneration;

import java.util.ArrayList;
import java.util.HashMap;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;


/**
 * This class converts raw events into TimeSeriesRaw features
 * 
 * Input: schema and eventList
 * Output: time series for each attribute
 * @author haopeng
 *
 */
public class TimeSeriesRawGenerator {
	ArrayList<String[]> schemas;
	ArrayList<ArrayList<String[]>> eventLists;
	ArrayList<TimeSeriesFeature> timeSeriesRawFeatures;
	LabelType label;
	
	long startTime;
	long endTime;
	
	public TimeSeriesRawGenerator(ArrayList<String[]> schemas, ArrayList<ArrayList<String[]>> eventLists , long startTime, long endTime, LabelType label) {
		this.schemas = schemas;
		this.eventLists = eventLists;
		this.startTime = startTime;
		this.endTime = endTime;
		this.label = label;
		this.generateTimeSeriesRawFeatures();
		if (ExplanationSettings.printResult) {
			this.printInformation();	
		}
		
	}
	
	public void printInformation() {
		System.out.println("Input eventTypes:" + this.schemas.size());
		System.out.println("Generated time series:" + this.timeSeriesRawFeatures.size());
		if (ExplanationSettings.debugMode) {
			System.out.println("=========Feature Names========");
			int count = 0;
			for (TimeSeriesFeature f: this.timeSeriesRawFeatures) {
				System.out.println(count ++ + f.getFeatureName());
			}
		}

	}
	
	public void generateTimeSeriesRawFeatures() {
		if (ExplanationSettings.printResult) {
			System.out.println("Generating time series features...");			
		}

		this.timeSeriesRawFeatures = new ArrayList<TimeSeriesFeature>();
		
		for (int i = 0; i < this.schemas.size(); i ++) {
			if (this.schemas.get(i).length == 0) {
			//	System.out.println("hi");//debug
			}
			if (Character.isUpperCase(this.schemas.get(i)[0].charAt(0))) {//Hadoop event types starts with Capital letters
				this.processOneTypeHadoop(this.schemas.get(i), this.eventLists.get(i));
			} else {
				this.processOneTypeGanglia(this.schemas.get(i), this.eventLists.get(i));
			}
			//this.processOneType(this.schemas.get(i), this.eventLists.get(i));
		}
	}
	
	public void processOneTypeHadoop(String[] schema, ArrayList<String[]> list) {
		//skip first two attributes(log source/event type), generate a time series for every other attribute
		//skip 8th (No.7)attribute: timestamp for hadoop events
		//skip 4th (No.3) attribute: job-id for hadoop events, makes no sense
		//skip 5th (No.4) task-id for hadoop events, makes no sense
		
		HashMap<Integer, TimeSeriesFeature> tempIndex = new HashMap<Integer, TimeSeriesFeature>();
		
		for (int i = 2; i < schema.length; i ++) {
			if (i != 7 && i != 3 && i != 4) {

				//TimeSeriesFeature tsFeature = new TimeSeriesFeature(FeatureType.TimeSeriesRaw, list.get(0)[1] + "-" + schema[i], 0);// to change, retrieve event type name
				TimeSeriesFeature tsFeature = new TimeSeriesFeature(FeatureType.TimeSeriesRaw, schema[0] + "-" + schema[i], 0, this.label);// to change, retrieve event type name
				tsFeature.setRawEventType(schema[0]);
				//add feature to list
				this.timeSeriesRawFeatures.add(tsFeature);
				tempIndex.put(i, tsFeature);
				tsFeature.setStartTime(this.startTime);
				tsFeature.setEndTime(this.endTime);
				//System.out.println("New feature:" + tsFeature.getFeatureName());
			}
		}
		
		//add values to time series
		for (int i = 0; i < list.size(); i ++) {
			String[] attrs = list.get(i);
			long ts = Long.parseLong(attrs[7]);//wrong for ganglia
			
			
			//System.out.println(i + ":" + ts);
			
			for (int j = 2; j < attrs.length; j ++) {
				if (j != 7 && j != 3 && j != 4) {
					double value = Double.parseDouble(attrs[j]);
					TimeSeriesFeature f = tempIndex.get(j);
					f.addPoint(ts, value);
					
					//System.out.println("Debug:add value pair to feature " + f.getFeatureName() + ":" + ts + "," + value);
				}
			}
			
			
		}
	}

	public void processOneTypeGanglia(String[] schema, ArrayList<String[]> list) {
		//skip first two attributes(log source/event type), generate a time series for every other attribute
		//skip 5th attribute: timestamp for ganglia
		HashMap<Integer, TimeSeriesFeature> tempIndex = new HashMap<Integer, TimeSeriesFeature>();
		
		for (int i = 2; i < schema.length; i ++) {
			if (i != 4) {
				//TimeSeriesFeature tsFeature = new TimeSeriesFeature(FeatureType.TimeSeriesRaw, list.get(0)[1] + "-" + schema[i], 0);// to change, retrieve event type name
				TimeSeriesFeature tsFeature = new TimeSeriesFeature(FeatureType.TimeSeriesRaw, schema[0] + "-" + schema[i], 0, this.label);// to change, retrieve event type name
				tsFeature.setRawEventType(schema[0]);
				//add feature to list
				this.timeSeriesRawFeatures.add(tsFeature);
				tempIndex.put(i, tsFeature);
				tsFeature.setStartTime(this.startTime);
				tsFeature.setEndTime(this.endTime);
				//System.out.println("New feature:" + tsFeature.getFeatureName());
			}
		}
		
		//add values to time series
		for (int i = 0; i < list.size(); i ++) {
			String[] attrs = list.get(i);
			long ts = Long.parseLong(attrs[4]);//wrong for ganglia
			
			
			//System.out.println(i + ":" + ts);
			
			for (int j = 2; j < attrs.length; j ++) {
				if (j != 4) {
					try{
						double value = Double.parseDouble(attrs[j]);
						TimeSeriesFeature f = tempIndex.get(j);
						f.addPoint(ts, value);

					} catch(NumberFormatException e) {
						System.out.println(j);
						e.printStackTrace();
					}
					
					//System.out.println("Debug:add value pair to feature " + f.getFeatureName() + ":" + ts + "," + value);
				}
			}
			
			
		}
	}

	
	public ArrayList<String[]> getSchemas() {
		return schemas;
	}

	public void setSchemas(ArrayList<String[]> schemas) {
		this.schemas = schemas;
	}

	public ArrayList<ArrayList<String[]>> getEventLists() {
		return eventLists;
	}

	public void setEventLists(ArrayList<ArrayList<String[]>> eventLists) {
		this.eventLists = eventLists;
	}

	public ArrayList<TimeSeriesFeature> getTimeSeriesRawFeatures() {
		return timeSeriesRawFeatures;
	}

	public void setTimeSeriesRawFeatures(
			ArrayList<TimeSeriesFeature> timeSeriesRawFeatures) {
		this.timeSeriesRawFeatures = timeSeriesRawFeatures;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	
}
