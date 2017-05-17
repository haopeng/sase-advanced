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

package edu.umass.cs.sase.util.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Extend ganglia events with more attributes: jobId, taskId, attemptId
 * @author haopeng
 *
 */
public class GangliaExtender {
	public  void extendGangliaFile(String input, String output) throws IOException{
		CSVReader reader = new CSVReader(new FileReader(input));
		CSVWriter writer = new CSVWriter(new FileWriter(output), ',',CSVWriter.NO_QUOTE_CHARACTER);
		
		String[] nextLine;
		int count = 0;
		while((nextLine = reader.readNext()) != null ){
			System.out.print(count + ":");
			for(String s : nextLine){
				System.out.print(s + ",");
			}
			System.out.print("\n");
			//output
			String[] outputLine = new String[nextLine.length + 3];
			for(int i = 0; i < nextLine.length; i ++){
				if(i < 3){
					outputLine[i] = nextLine[i];
				}else{
					outputLine[i + 3] = nextLine[i];
				}
			}
			if(count == 0){
				outputLine[3] = "jobId";
				outputLine[4] = "taskId";
				outputLine[5] = "attepmtId";
				
			}else{
				outputLine[3] = "0";
				outputLine[4] = "0";
				outputLine[5] = "0";
			}
			writer.writeNext(outputLine);
			//nextLine = reader.readNext();
			count ++;
		}
		writer.flush();
		writer.close();

	}
	
	public void extendFolder(String folderPath, String  outputFolder) throws IOException{
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		for(File file: files){
			String input = file.getAbsolutePath();
			String output = outputFolder + file.getName();
			this.extendGangliaFile(input, output);
		}
		
	}
	public static void main(String args[]) throws IOException{
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeGanglia";
		String outputFolder = "I:\\Copy\\Data\\2013\\singleTypeExtend\\";
		GangliaExtender extender = new GangliaExtender();
		extender.extendFolder(inputFolder, outputFolder);
		/*
		String input = "I:\\Copy\\Data\\2013\\singleType\\boottime_event.txt.csv";
		String output = "I:\\Copy\\Data\\2013\\singleTypeExtend\\boottime_event.txt.csv";
		CSVReader reader = new CSVReader(new FileReader(input));
		CSVWriter writer = new CSVWriter(new FileWriter(output), ',',CSVWriter.NO_QUOTE_CHARACTER);
		
		String[] nextLine;
		int count = 0;
		while((nextLine = reader.readNext()) != null && count < 100){
			for(String s : nextLine){
				System.out.print(s + ",");
				
			}
			System.out.print("\n");
			//output
			String[] outputLine = new String[nextLine.length + 3];
			for(int i = 0; i < nextLine.length; i ++){
				if(i < 3){
					outputLine[i] = nextLine[i];
				}else{
					outputLine[i + 3] = nextLine[i];
				}
			}
			if(count == 0){
				outputLine[3] = "jobId";
				outputLine[4] = "taskId";
				outputLine[5] = "attepmtId";
				
			}else{
				outputLine[3] = "0";
				outputLine[4] = "0";
				outputLine[5] = "0";

			}
			
			writer.writeNext(outputLine);
			
			
			
			nextLine = reader.readNext();
			count ++;
		}
		writer.flush();
		writer.close();
		*/
	}
}
