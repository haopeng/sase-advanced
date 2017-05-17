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
import java.util.StringTokenizer;

public class OldResultReader {
	
	
	String throughput = null;
	String averageThroughput;
	String selectivity = null;
	String numOfRuns = null;
	String patternMatchingPercentage = null;
	String enumerationPercentage = null;
	String confidencePercentage = null;
	
	String numberOfEvents = null;
	String numOfMatches = null;
	String numOfRelevantEvents = null;
	String numOfRunsReachedMatch = null;
	String averageNumberOfEventsForRunsReachedMatch = null;
	String maxNumberForRunsReachedMatch = null;
	String maxNumberForMatch = null;
	String numOfCollapsedMatches = null;
	
	String machineName = null;
	
	
	public static void main(String args[]) throws IOException{
		String folderPath = "/Users/haopeng/Dropbox/research/2nd/Experiments/may14/window-result-uncertain";
		folderPath = "F:\\TestResultScript\\selectivity-true-false-precise-baseline";
		folderPath = "F:\\TestResultScript\\new";
		
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise\\5.true-selectivity\\selectivity-true-precise-baseline-postponing";
		
		if(args.length > 0){
			folderPath = args[0];
		}
		
		// get all files in this folder
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		
		OldResultReader reader = new OldResultReader();
		System.out.println("fileName\t" + 
				"throughput\t" + 
				"averageThroughput\t" +
				"selectivity\t" + 
				"numOfRuns\t" +
				"patternMatchingPercentage\t" +
				"enumerationPercentage\t" +
				"confidencePercentage\t" +
				"numberOfEvents\t" +
				"numOfMatches\t" +
				"numOfRelevantEvents\t"+
				 "numOfRunsReachedMatch\t" + 
				 "averageNumberOfEventsForRunsReachedMatch\t"+
				"maxNumberForRunsReachedMatch\t" + 
				 "maxNumberForMatch\t" + 
				"numOfCollaspedMatches\t" +
				 "machineName\t");
		
		for(int i = 0; i < files.length; i ++){
			if(!files[i].getName().equalsIgnoreCase(".DS_Store") && !files[i].getName().equalsIgnoreCase("result.txt")){
				System.out.println(reader.readOneFile(files[i]));
			}
		}
	}
	
	public  String readOneFile(File file) throws IOException{
		FileInputStream fstream = new FileInputStream(file.getPath());
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int count = 0;
		/*
		 * 	

	String  = null;
	String= null;
		 * Total Running Time: 231722294000 nanoseconds
Number Of Events Processed: 20000
Number Of Relevant Events:458
Number Of Runs Created: 404
Total run life time:92393472956000
Enumeration time:172849000



Number Of Matches Found: 131735
Average number of Events for Matches: 7.6869017345428325


Time for compute confidence: 229813067000
Numbers of computing confidence: 131735

Throughput: 86 events/second

**************Profiling Numbers*****************
Total Running Time: 215612178 nanoseconds
Number Of Events Processed: 50000
Number Of Relevant Events:435
Number Of Runs Created: 3341
Total run life time:3558306839
Enumeration time:0
Enumeration percentage:0.0%
Pattern matching percentage:100.0%
Number Of Matches Found: 695
Average number of Events for Matches: 4.168345323741007
Max number of Events for Matches: 8
Selectivity :0.0139 matches/event
Time for compute confidence: 0
Numbers of computing confidence: 0
Percentage of compute confidence:0%
Throughput: 231897 events/second
***********************
***********************
Avergae throughput is:231632
Postponing optimiaztion:false
Evaluation on the fly:false
Collapsed result format:false

		 */
		
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
			if(strLine.contains("Throughput")){
				//Throughput: 86 events/second
				throughput = getTokenOfString(2, strLine);
			}else if(strLine.contains("Number Of Matches Found")){
				//Number Of Matches Found: 131735
				numOfMatches = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Number Of Relevant Events")){
				//Number Of Relevant Events:458
				numOfRelevantEvents = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Number Of Runs Created")){
				//Number Of Runs Created: 404
				numOfRuns = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Number Of Runs Reached Match")){
				//Number Of Runs Reached Match: 358
				numOfRunsReachedMatch = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Selectivity")){
				//Selectivity :6.58675 matches/event
				selectivity = this.getTokenOfString(2, strLine);
				selectivity = this.getTokenAfterColon(selectivity);
			}else if(strLine.contains("Percentage of compute confidence")){
				//Percentage of compute confidence:99%
				confidencePercentage = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Average Number of Events for Runs Reached Match")){
				//Average Number of Events for Runs Reached Match:10.988826815642458
				averageNumberOfEventsForRunsReachedMatch = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Max Number Of Events for Runs Reached Match")){
				//Max Number Of Events for Runs Reached Match:18
				maxNumberForRunsReachedMatch = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Max number of Events for Matches")){
				//Max number of Events for Matches: 16
				 maxNumberForMatch = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Number of Collapsed Matches")){
				//Number of Collapsed Matches: 1451
				this.numOfCollapsedMatches = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Pattern matching percentage")){
				//Pattern matching percentage:100.0%
				this.patternMatchingPercentage = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Enumeration percentage")){
				//Enumeration percentage:0.0%
				this.enumerationPercentage = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Average throughput")){
				//Average throughput is: 10000
				this.averageThroughput = this.getTokenAfterColon(strLine);
			}else if(strLine.contains("Hostname of local machine")){
				//Hostname of local machine: compute-1-0.dbcluster
				this.machineName = this.getTokenAfterColon(strLine);
			}
		 }

		/*
		 * 		System.out.println("fileName\t" + 
				"throughput\t" + 
				"averageThroughput\t" + 
				"selectivity\t" + 
				"numOfRuns\t" +
				
				"patternMatchingPercentage\t" +
				"enumerationPercentage\t" +
				
				"confidencePercentage\t" +
				"numberOfEvents\t" +
				"numOfMatches\t" +
				"numOfRelevantEvents\t"+
				 "numOfRunsReachedMatch\t" + 
				 "averageNumberOfEventsForRunsReachedMatch\t"+
				"maxNumberForRunsReachedMatch\t" + 
				 "maxNumberForMatch\t" + 
				"numOfCollaspedMatches\t" +
				 "machineName\t");
		 */
		
		
		StringBuilder sb = new StringBuilder();
		sb.append(file.getName() + "\t");
		sb.append(throughput + "\t");
		sb.append(this.averageThroughput + "\t");
		sb.append(this.selectivity + "\t");
		sb.append(this.numOfRuns + "\t");
		sb.append(this.patternMatchingPercentage + "\t");
		sb.append(this.enumerationPercentage + "\t");
		sb.append(this.confidencePercentage + "\t");
		sb.append(this.numberOfEvents + "\t");
		sb.append(numOfMatches + "\t");
		sb.append(this.numOfRelevantEvents + "\t");
		sb.append(this.numOfRunsReachedMatch + "\t");
		sb.append(this.averageNumberOfEventsForRunsReachedMatch + "\t");
		sb.append(this.maxNumberForRunsReachedMatch + "\t");
		sb.append(this.maxNumberForMatch + "\t");
		sb.append(this.numOfCollapsedMatches + "\t");
		sb.append(this.machineName + "\t");
		
		
		return sb.toString();
	}
	
	 String getTokenOfString(int n, String s){
		StringTokenizer st = new StringTokenizer(s);
		String token = null;
		for(int i = 0; i < n; i ++){
			token = st.nextToken();
		}
		
		return token;
	}
	
	 String getTokenAfterColon(String s){
		String token = s.substring(s.indexOf(':') + 1);
		return token;
	 }
	 
}
