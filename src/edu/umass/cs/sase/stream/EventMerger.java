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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;


/**
 * @author haopeng
 *
 */
public class EventMerger {
	public static void main(String args[]) throws IOException{
		String filesToMerge[] = {"d:\\hadoop_result.txt", "d:\\ganglia-event\\cpu_idle.0-14-events.txt",
				"d:\\ganglia-event\\cpu_idle.0-15-events.txt","d:\\ganglia-event\\cpu_idle.0-16-events.txt",
				"d:\\ganglia-event\\cpu_idle.0-17-events.txt","d:\\ganglia-event\\cpu_idle.0-19-events.txt",
				"d:\\ganglia-event\\cpu_idle.0-20-events.txt","d:\\ganglia-event\\cpu_idle.0-21-events.txt",
				"d:\\ganglia-event\\cpu_idle.0-22-events.txt"};
		String filesToMerge3[] = {"d:\\hadoop_result.txt", "d:\\ganglia-event\\cpu_user.0-14-events.txt",
				"d:\\ganglia-event\\cpu_user.0-15-events.txt","d:\\ganglia-event\\cpu_user.0-16-events.txt",
				"d:\\ganglia-event\\cpu_user.0-17-events.txt","d:\\ganglia-event\\cpu_user.0-19-events.txt",
				"d:\\ganglia-event\\cpu_user.0-20-events.txt","d:\\ganglia-event\\cpu_user.0-21-events.txt",
				"d:\\ganglia-event\\cpu_user.0-22-events.txt"};
		
		String filesToMerge2[] = {"d:\\hadoop_result.txt","d:\\ganglia-event\\cpu_user.0-14-events.txt"};
		Event[] stream = mergeFile(filesToMerge2, 50, 7500);
		BufferedWriter bw = new BufferedWriter(new FileWriter("d:\\merged.stream"));
		for(int i = 0; i < stream.length; i ++){
			bw.append(stream[i].toString());
		}
		bw.flush();
		
	}
	public static Event[] mergeFile(String files[], int hadoopHalfPeriod, int gangliaHalfPeriod) throws IOException{
		Event[][] eventFromFile = new Event[files.length][];
		for(int i = 0; i < files.length; i ++){
			if(files[i].contains("hadoop")){
				eventFromFile[i] = AttemptEventReader.readFromFile(files[i], hadoopHalfPeriod);
			}else{
				eventFromFile[i] = GangliaEventReader.readFromFile(files[i], gangliaHalfPeriod);
			}
		}
		
		Event [] mergedEvents = mergeEvents(eventFromFile);
		
		/*for(int i = 0; i < mergedEvents.length; i ++){
			System.out.println(i + ": "+mergedEvents[i]);
		}*/
		
		return mergedEvents;
		
	}
	
	public static Event[] mergeEvents(Event[][] eventFromFile){
		int total = 0;
		
		for(int i = 0; i < eventFromFile.length; i ++){
			total += eventFromFile[i].length;
		}
		Event[] mergedEvents = new Event[total];
		int count = 0;
		for(int i = 0; i < eventFromFile.length; i ++){
			for(int j = 0; j < eventFromFile[i].length; j ++){
				mergedEvents[count] = eventFromFile[i][j];
				//mergedEvents[count].setId(count);
				count ++;
			}
		}
		Arrays.sort(mergedEvents, new Comparator<Object>(){
			public int compare(Object arg1, Object arg2	){
				Event e1 = (Event)arg1;
				Event e2 = (Event)arg2;
				long t1 = e1.getTimestamp();
				long t2 = e2.getTimestamp();
				if(t1 < t2){
					return -1;
				}else if(t1 > t2){
					return 1;
				}else
					return 0;
			}
		});
		for(int i = 0; i < mergedEvents.length; i ++){
			mergedEvents[i].setId(i);
			mergedEvents[i].setOriginalEventId(i);
		}
		return mergedEvents;
	}

}
