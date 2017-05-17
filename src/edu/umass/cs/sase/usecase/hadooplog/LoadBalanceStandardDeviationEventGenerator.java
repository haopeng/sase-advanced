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

import java.io.IOException;

public class LoadBalanceStandardDeviationEventGenerator extends LoadBalanceEventGenerator{

	public LoadBalanceStandardDeviationEventGenerator(String rootFolder,
			String nodePrefix, String suffix, String metricFileName,
			int startNode, int endNode, String outputFilePath,
			double percentageThreshold, int valueThreshold) {
		super(rootFolder, nodePrefix, suffix, metricFileName, startNode, endNode,
				outputFilePath, percentageThreshold, valueThreshold);

	}
	
	
	public void processValues() throws IOException{
		// process the values, to output a line of balance value!
		System.out.println("~~~~~~~~~~~~processing~~~~~~~~~~~~~");
		double valueSum = 0;
		for(int i = 0; i < this.values.length; i ++){
			valueSum += this.values[i];
		}
		double valueAverage = valueSum / this.values.length;
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@valueSum is:" + valueSum);
		System.out.println("averageValue is:" + valueAverage);
		
		int subtractionSquareSum = 0;
		for(int i = 0; i < this.values.length; i ++){
			double subtraction = this.values[i] - valueAverage;
			double square = subtraction * subtraction;
			subtractionSquareSum += square;
		}
		
		double standardDeviation = Math.sqrt(subtractionSquareSum);
		
		
		this.bufferedWriter.append(this.currentTimestamp + "," + standardDeviation + "\n");
		this.bufferedWriter.flush();
		
	}
	//standard deviation
	public double getValueFromLine(String line){
		if(line == null){
			return 0;
		}
		int delimPosition = line.indexOf(",");
		String value = line.substring(delimPosition + 1);
		double originalValue = Double.parseDouble(value);

		return originalValue;
		
		
	}

	
	public static void main(String[] args) throws IOException {
		//2015, slave nodes, yeeha.compute-1-0  -- yeeha.compute-1-9
		String rootFolder = "/Users/haopeng/Copy/Data/2015/ganglia-event/";
		String outputFilePath = "/Users/haopeng/Copy/Data/2015/ganglia-event/balance-standarddeviation.txt";
		
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

		LoadBalanceEventGenerator generator = new LoadBalanceStandardDeviationEventGenerator(rootFolder, nodePrefix, nodeSuffix, metricFileName, startNode, endNode, outputFilePath, percentageThreshold, valueThreshold);
		
		generator.generateLogs();

	}
}
