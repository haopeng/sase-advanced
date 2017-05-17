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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import edu.umass.cs.sase.util.FileOperator;


/**
 * This class is used to convert original ganglia logs into ganglia events.
 * there are two objectives:
 * 1. compress space
 * 2. more readable numbers
 * 
 * The process is likes this:
 * Log files structure: rootFolder/compute-1-0.yeeha/cpu_idle.log
 * output files structure:outputFolder/compute-1-0.yeeha/cpu_idle.event
 * @author haopeng
 *
 */
public class GangliaLogPreprocessorYeeha {

	String logFileRootFolder;
	String outputFileFolder;
	
	String nodePrefix = "compute-";
	String[] nodeNames = {"1-0", "1-1", "1-2", "1-3", "1-4", "1-5",
						  "1-6", "1-7", "1-8", "1-9", "0-28"};
	String nodeSuffix = ".yeeha";
	String[] metrics = {"boottime","bytes_in","bytes_out","cpu_aidle",
			"cpu_idle","cpu_nice","cpu_num","cpu_speed",
			"cpu_system","cpu_user","cpu_wio","disk_free",
			"disk_total","load_fifteen","load_five","load_one",
			"mem_buffers","mem_cached","mem_free","mem_shared",
			"mem_total","part_max_used","pkts_in","pkts_out",
			"proc_run","proc_total","swap_free","swap_total"};
	
	String logFileSuffix = "_AVERAGE.log";
	String outputFileSuffix = "_event.txt";
	
	BufferedWriter bw;
	
	public GangliaLogPreprocessorYeeha(String logFileRootFolder, String outputFileFolder){
		this.logFileRootFolder = logFileRootFolder;
		this.outputFileFolder = outputFileFolder;
	}
	
	public void iterateRootFolder() throws IOException{
		int counter = 0;

		for(String node : this.nodeNames){
			String inputFilePath, outputFilePath;
			inputFilePath = "";
			outputFilePath = "";
			String inputFilePathPrefix = this.logFileRootFolder + "/" + nodePrefix + node + this.nodeSuffix + "/";
			String outputFilePathPrefix = this.outputFileFolder + "/" + nodePrefix + node + this.nodeSuffix + "/";
			

			
			for(int j = 0; j < this.metrics.length; j ++){
				inputFilePath = inputFilePathPrefix + metrics[j] + this.logFileSuffix;
				outputFilePath = outputFilePathPrefix + metrics[j] + this.outputFileSuffix;
				counter ++;
				System.out.println("=========" + counter + "============");
				System.out.println("input file is:" + inputFilePath);
				System.out.println("ouput file is:" + outputFilePath);
				this.transferFile(inputFilePath, outputFilePath);
				
			}
		}
	}
	
	
	public void transferFile(String logFile, String eventFile) throws IOException{
		FileOperator.makeFolderForFile(eventFile);
		
		this.bw = new BufferedWriter(new FileWriter(eventFile));
		
		BufferedReader gReader = new BufferedReader(new FileReader(logFile));
		
		String line;
		
		line = gReader.readLine();
		int count = 0;
		
		while(line!=null){
			if(line.startsWith("1")){ // to avoid empty lines
				//System.out.println(line);
				parseLine(line, ++count);
			}
			line = gReader.readLine();
		}
		
		this.bw.flush();
		this.bw.close();
		gReader.close();
		
	}
	public void parseLine(String line, int count) throws IOException{
		if(!line.contains("nan")){// avoid invalid lines
			StringTokenizer st = new StringTokenizer(line, ":");
			String time = st.nextToken();
			
			long timestamp = Long.parseLong(time);
			timestamp = timestamp*1000;//convert to milliseconds
			
			double value = Double.parseDouble(st.nextToken());
			//int intValue = (int)value;
			
			System.out.println(count + ": timestamp = "+ timestamp + "\t" + "value="+ value);
			
			this.bw.append(timestamp + "," + value + "\n");
			//bw.flush();
		}
	
	}


	public static void main(String[] args) throws IOException {
		
		
		String inputFolder = "/Users/haopeng/Copy/Data/2015/ganglia-log";
		String outputFolder = "/Users/haopeng/Copy/Data/2015/ganglia-event";
		
		
		if (args.length > 1) {
			inputFolder = args[0];
			outputFolder = args[1];
		}
		
		
		
		GangliaLogPreprocessorYeeha glp = new GangliaLogPreprocessorYeeha(inputFolder, outputFolder);
		glp.iterateRootFolder();
	}
	
}
