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

package edu.umass.cs.sase.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * This class is used to replace the class: OldResultReader.java
 * Why? (1) Scalable so we can add new attributes in the result outputs without changing this class any more
 * 
 * @author haopeng
 *
 */
public class ResultReader {

	String fileLoc;
	File file;
	HashMap<String, String> profilings;
	public ResultReader(String fileLoc) throws IOException{
		this.fileLoc = fileLoc;
		this.profilings = new HashMap<String, String>();
		this.readProfilings();
	}

	public void readProfilings() throws IOException{
		this.file = new File(this.fileLoc);
		this.profilings.put("fileName", file.getName());
		FileInputStream fstream = new FileInputStream(file.getPath());
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		boolean toProcess = true;
		while((strLine = br.readLine()) != null){
			if(toProcess){
				this.parseLine(strLine);
			}
			if(strLine.contains("Profiling Numbers")){
				toProcess = true;
			}
				
		}
		br.close();

	}
	public void parseLine(String line){
		//System.out.println(line);
		StringTokenizer st = new StringTokenizer(line, ":");
		if(st.countTokens() == 2){
			String key = st.nextToken().trim();
			String value = st.nextToken();
			value = this.getFirstToken(value).trim();
			if(this.profilings.containsKey(key)){
				this.profilings.remove(key);
			}
			this.profilings.put(key, value);
		}
		
	}
	public String getFirstToken(String str){
		StringTokenizer st = new StringTokenizer(str);
		return st.nextToken();
	}
	public String getSchemaString(){
		StringBuilder sb = new StringBuilder();
		sb.append("FileName\t");
		for(String key: this.profilings.keySet()){
			sb.append(key + "\t");
		}
		return sb.toString();
	}
	public String getValueString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.file.getName() + "\t");
		for(String key: this.profilings.keySet()){
			sb.append(this.profilings.get(key) + "\t");
		}
		return sb.toString();
	}
	
	public String getRequiredValues(String[] keys){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < keys.length; i ++){
			if(this.profilings.containsKey(keys[i])){
				sb.append(this.profilings.get(keys[i]) + "\t");
			}else{
				sb.append("Unavailable\t");				
			}
		}
		return sb.toString();
	}
	
	public int containsSubStringCount(String originalStr, String subStr){
		String replacedStr = originalStr.replaceAll(subStr, "");
		int difference = originalStr.length() - replacedStr.length();
		int count = difference / subStr.length();
		return count;
	}
	
	public static String connectStringArray(String[] strs){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < strs.length; i ++){
			sb.append(strs[i] + "\t");
		}
		return sb.toString();
	}
	
	/**
	 * used to extract one sub-group of results
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/*
		String fileLoc = "f:\\test.txt";
		ScalableResultReader reader = new ScalableResultReader(fileLoc);
		System.out.println(reader.getSchemaString());
		System.out.println(reader.getValueString());
		*/
		String folderPath = "";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise\\1.true-false-selectivity\\selectivity-true-false-precise-baseline";
		folderPath = "F:\\TestResultScript\\selectivity-true-false-precise-baseline";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise2.0\\8.inconsistent-timewindow\\timewindow-inconsistent-precise-baseline-postponing-onthefly";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Imprecise\\2.true-false-timewindow\\timewindow-true-false-imprecise-baseline-postponing-collapsed";
		
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\imprecise3.0\\true\\timewindow-true-false-imprecise-baseline-postponing-dpc";
		
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\dpc\\obelix\\half5-largerwindow\\timewindow-true-false-imprecise-baseline-postponing-dpc-0.9";
		
		
		if(args.length > 0){
			folderPath = args[0];
		}
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		
		String[] attributes = {"fileName","Throughput","Average throughput is","Selectivity","The time window is" , 
				"Number Of Runs Created", "Number Of Events Processed", "Number Of Relevant Events","Number Of Matches Found","Total Running Time",
				"Time on buffering in stacks","Time on buffering in stacks percentage","Time on EvluateRuns Baseline","Time on EvluateRuns Baseline percentage", "Time on Clean Runs Baseline","Time on Clean Runs Baseline percentage","Time on Create New Run Baseline","Time on Create New Run Baseline Percentage",
				"Enumeration time","Enumeration percentage","Pattern matching time","Pattern matching percentage",
				"Time for sorting events in imprecise","Time for sorting events percentage","Time for compute confidence","Percentage of compute confidence","Numbers of computing confidence","Number of matches filtered by confidence threshold","Percentage of matches filtered by confidence threshold",
				"Number of paths","Number of discards by DP on predicate","Number of skips by DP on predicate",
				"Confidence distribution count in range0","Confidence distribution percentage in range0",
				"Confidence distribution count in range1","Confidence distribution percentage in range1",
				"Confidence distribution count in range2","Confidence distribution percentage in range2",
				"Confidence distribution count in range3","Confidence distribution percentage in range3",
				"Confidence distribution count in range4","Confidence distribution percentage in range4",
				"Confidence distribution count in range5","Confidence distribution percentage in range5",
				"Confidence distribution count in range6","Confidence distribution percentage in range6",
				"Confidence distribution count in range7","Confidence distribution percentage in range7",
				"Confidence distribution count in range8","Confidence distribution percentage in range8",
				"Confidence distribution count in range9","Confidence distribution percentage in range9",
				"Number of events buffered events in ZStream","Number of expired events in ZStream","Number of start runs","Nubmer of checking events in ZStream stacks",
				"Count for Enumerations","Count for Validations","Before Enumeration time","Before Enumeration percentage","On Enumeration time","On Enumeration percentage","After Enumeration time","After Enumeration percentage",
				"Get combination time","Get combination percentage","Fill event time","Fill event percentage","Validate time","Vlidate percentage",
				"Postpoing Discarded Events","Postpoing Safe Events","Postpoing Unsafe Events","Time on event buffering","Time on Event buffering percentage",
				"Count for predicate checking","Time cost for predicate checking","Time percentage for predicate checking","Count for clone run","Clone cost for clone run","Time percentage for clone run",
				"Number of runs cleaned", "Time on Reset runs percentage", "Time on remove runs percentage", "Time on remove runs from partition percentage",
				"Number Of Runs Reached Match","Average Number of Events for Runs Reached Match", "Max Number Of Events for Runs Reached Match","Average number of Events for Matches","Max number of Events for Matches",
				"Hostname of local machine"};
		System.out.println(ResultReader.connectStringArray(attributes));
		
		
		for(int i = 0; i < files.length; i ++){
			if(!files[i].getName().equalsIgnoreCase(".DS_Store") && !files[i].getName().equalsIgnoreCase("result.txt") && !files[i].isDirectory()){
				ResultReader reader = new ResultReader(files[i].getPath());
				System.out.println(reader.getRequiredValues(attributes));
			}
		}
	}

}
