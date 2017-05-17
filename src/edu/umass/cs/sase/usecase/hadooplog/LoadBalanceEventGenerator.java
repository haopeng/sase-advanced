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

package edu.umass.cs.sase.usecase.hadooplog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Define of load balance
 * Intuition: most machines have a similar load
 * Definition:  20% 80% rule
 * Explanation: if 80% of all nodes are busy/idle then the cluster is load balanced. Otherwise it is unbalanced
 * value = 1 means balance
 * value = 0 means unbalance
 * @author haopeng
 *
 */
public class LoadBalanceEventGenerator {
	//boolean numericMode;// if true, then the values are the actual cpu usage number; if false, then the values are 1:balance, 0: imbalance.
	//output file name is the eventtype
	
	String rootFolder;
	String nodePrefix = "obelix";
	String nodeSuffix;
	int startNode;
	int endNode;
	String metricFileName;
	
	String outputFilePath;
	
	double percentageThreshold;
	int valueThreshold;
	
	String[] lines;
	BufferedReader readers[];
	double[] values;
	long currentTimestamp;
	
	int countOfUnbalance;
	
	BufferedWriter bufferedWriter;

	
	public LoadBalanceEventGenerator(String rootFolder, String nodePrefix, String suffix, String metricFileName, int startNode, int endNode, String outputFilePath, double percentageThreshold, int valueThreshold){
		this.rootFolder = rootFolder;
		this.nodePrefix = nodePrefix;
		this.nodeSuffix = suffix;
		this.metricFileName = metricFileName;
		this.startNode = startNode;
		this.endNode = endNode;
		
		this.outputFilePath = outputFilePath;
		this.percentageThreshold = percentageThreshold;
		this.valueThreshold = valueThreshold;
		
		int size = this.endNode - this.startNode + 1;
		this.lines = new String[size];
		this.readers = new BufferedReader[size];
		this.values = new double[size];
		
		this.countOfUnbalance = 0;
		
		
	}
	
	public void generateLogs() throws IOException{
		this.bufferedWriter = new BufferedWriter(new FileWriter(this.outputFilePath));
		for(int i = this.startNode; i <= this.endNode; i ++){
			readers[i - this.startNode] = new BufferedReader(new FileReader(this.rootFolder + nodePrefix + i + this.nodeSuffix +"/" + this.metricFileName));
			lines[i - this.startNode] = readers[i - startNode].readLine();
		}
		int count = 0;
		while(lines[0] != null){
			System.out.println(++ count);
			
			this.guaranteeTimeConsistency();
			this.processLines();
			for(int i = this.startNode; i <= this.endNode; i ++){
				lines[i - this.startNode] = readers[i - startNode].readLine();
			}
		}
		
		System.out.println("Total unbalance: " + this.countOfUnbalance);
		
		this.bufferedWriter.close();

		for(int i = 0; i < readers.length; i ++){
			readers[i].close();
		}

	}
	
	public void guaranteeTimeConsistency() throws IOException {
		long latestTime = this.getLatestTime();
	
		while(!this.isConsistent(latestTime)) {
			this.adjustTimestampConsistency(latestTime);
			latestTime = this.getLatestTime();
		}
	}
	
	public long getLatestTime() {
		//find minimum
		long latestTime = Long.MIN_VALUE;
		for (String str: this.lines) {
			long currentTime = this.getTimestampFromLine(str);
			latestTime = Math.max(latestTime, currentTime);
		}
		return latestTime;		
	}
	public boolean isConsistent(long time) {
		int count = 0;
		for (int i = 0; i < this.lines.length; i ++) {
			if (this.getTimestampFromLine(lines[i]) == time) {
				count ++;
			}
		}
		
		return count == this.lines.length;
	}
	public void adjustTimestampConsistency(long latestTime) throws IOException {
		//check every reader
		for (int i = 0; i < this.readers.length; i ++) {
			while (this.getTimestampFromLine(lines[i]) < latestTime) {
				this.lines[i] = this.readers[i].readLine();
			}
		}
	}
	
	public void processLines() throws IOException{
		System.out.println("=========");
		for(int i = 0; i < this.lines.length; i ++){
			System.out.println(lines[i]);
			System.out.println("Timestamp = " + this.getTimestampFromLine(lines[i]) + " value =" + this.getValueFromLine(lines[i]));
			if(i == 0){
				this.currentTimestamp = this.getTimestampFromLine(lines[i]);
			}
			this.values[i] = this.getValueFromLine(lines[i]);
		}
		this.processValues();
	}
	
	public void processValues() throws IOException{
		// process the values, to output a line of balance value!
		System.out.println("~~~~~~~~~~~~processing~~~~~~~~~~~~~");
		double valueSum = 0;
		for(int i = 0; i < this.values.length; i ++){
			valueSum += this.values[i];
		}
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@valueSum is:" + valueSum);
		if(valueSum > this.percentageThreshold * this.values.length  && valueSum < this.values.length * (1- this.percentageThreshold)){
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Unbalance is found:" + valueSum);
			this.countOfUnbalance ++;
			this.bufferedWriter.append(this.currentTimestamp + ",0\n");
			this.bufferedWriter.flush();
		}else{
			this.bufferedWriter.append(this.currentTimestamp + ",1\n");
			this.bufferedWriter.flush();		}
		
	}
	
	public long getTimestampFromLine(String line){
		//sample line: 166015000,0
		//sample line full: 1415563215000,0.0
		
		if(line == null){
			return 0;
		}
		
		int delimPosition = line.indexOf(",");
		String timestamp = line.substring(0, delimPosition);
		return Long.parseLong(timestamp);
		
	}
	public double getValueFromLine(String line){
		if(line == null){
			return 0;
		}
		int returnValue = 0;
		int delimPosition = line.indexOf(",");
		String value = line.substring(delimPosition + 1);
		double originalValue = Double.parseDouble(value);
		if(originalValue >= this.valueThreshold){
			returnValue = 1;
		}
		return returnValue;
		
		
	}
	
	
	public String getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}

	public String getNodePrefix() {
		return nodePrefix;
	}

	public void setNodePrefix(String nodePrefix) {
		this.nodePrefix = nodePrefix;
	}

	public int getStartNode() {
		return startNode;
	}

	public void setStartNode(int startNode) {
		this.startNode = startNode;
	}

	public int getEndNode() {
		return endNode;
	}

	public void setEndNode(int endNode) {
		this.endNode = endNode;
	}

	public String getMetricFileName() {
		return metricFileName;
	}

	public void setMetricFileName(String metricFileName) {
		this.metricFileName = metricFileName;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	public double getPercentageThreshold() {
		return percentageThreshold;
	}

	public void setPercentageThreshold(double percentageThreshold) {
		this.percentageThreshold = percentageThreshold;
	}

	public int getValueThreshold() {
		return valueThreshold;
	}

	public void setValueThreshold(int valueThreshold) {
		this.valueThreshold = valueThreshold;
	}

	public String[] getLines() {
		return lines;
	}

	public void setLines(String[] lines) {
		this.lines = lines;
	}

	public BufferedReader[] getReaders() {
		return readers;
	}

	public void setReaders(BufferedReader[] readers) {
		this.readers = readers;
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public long getCurrentTimestamp() {
		return currentTimestamp;
	}

	public void setCurrentTimestamp(long currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}

	public int getCountOfUnbalance() {
		return countOfUnbalance;
	}

	public void setCountOfUnbalance(int countOfUnbalance) {
		this.countOfUnbalance = countOfUnbalance;
	}

	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	public void setBufferedWriter(BufferedWriter bufferedWriter) {
		this.bufferedWriter = bufferedWriter;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		/*2013
		String rootFolder = "f:\\Copy\\Data\\2013\\ganglia\\ganglia-event\\";
		String nodePrefix = "obelix";
		String metricFileName = "cpu_user_event.txt";
		int startNode = 105;
		int endNode = 115;
		
		String outputFilePath = "f:\\Copy\\Data\\2013\\ganglia\\ganglia-event\\balance.txt";
		double percentageThreshold = 0.2;
		int valueThreshold = 20;

		LoadBalanceEventGenerator generator = new LoadBalanceEventGenerator(rootFolder, nodePrefix, metricFileName, startNode, endNode, outputFilePath, percentageThreshold, valueThreshold);
		
		generator.generateLogs();
		*/

		//2015, slave nodes, yeeha.compute-1-0  -- yeeha.compute-1-9
		String rootFolder = "/Users/haopeng/Copy/Data/2015/ganglia-event/";
		String outputFilePath = "/Users/haopeng/Copy/Data/2015/ganglia-event/balance.txt";
		
		if (args.length > 1) {
			rootFolder = args[0];
			outputFilePath = args[1];
		}
		
		
		String nodePrefix = "compute-1-";
		String nodeSuffix = ".yeeha";
		String metricFileName = "cpu_user_event.txt";
		int startNode = 0;
		int endNode = 9;
		
		
		double percentageThreshold = 0.2;
		int valueThreshold = 20;

		LoadBalanceEventGenerator generator = new LoadBalanceEventGenerator(rootFolder, nodePrefix, nodeSuffix, metricFileName, startNode, endNode, outputFilePath, percentageThreshold, valueThreshold);
		
		generator.generateLogs();
	}

}
