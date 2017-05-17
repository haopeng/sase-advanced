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
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class ReplaceWithNumeric {
	HashMap<String, String> logSourceIndex;
	HashMap<String, String> eventTypeIndex;
	public ReplaceWithNumeric(){
		//initialize index
		this.logSourceIndex = new HashMap<String, String>();
		this.logSourceIndex.put("h", "0");
		this.logSourceIndex.put("g", "1");
		
		this.eventTypeIndex = new HashMap<String, String>();
		String folderPath = "I:\\Copy\\Data\\2013\\singleTypeExtend\\";
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		for(int i= 0; i < files.length; i ++){
			String type = this.typeForFileName(files[i].getName());
			this.eventTypeIndex.put(type, "" + i );
			System.out.println(i + ":" + type);
		}
		
		
	}
	
	public String typeForFileName(String fileName){
		int dotPosition = fileName.indexOf(".");
		String type = fileName.substring(0, dotPosition);
		return type;
	}
	
	public void replaceFile(String inputFilePath, String outputFilePath) throws IOException{
		CSVReader reader = new CSVReader(new FileReader(inputFilePath));
		CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String[] nextLine = reader.readNext();
		writer.writeNext(nextLine);//attribute names
		int lineCount = 0;
		while((nextLine = reader.readNext()) != null){
			nextLine[0] = this.logSourceIndex.get(nextLine[0]);
			nextLine[1] = this.eventTypeIndex.get(nextLine[1]);
			writer.writeNext(nextLine);
			System.out.println(lineCount);
			lineCount ++;
		}
		reader.close();
		writer.flush();
		writer.close();

	}
	
	
	public static void main(String[] args) throws IOException{
		ReplaceWithNumeric r = new ReplaceWithNumeric();
		//String inputFilePath = "I:\\Copy\\Data\\2013\\merged\\ordered-all.csv";
		//String outputFilePath = "I:\\Copy\\Data\\2013\\merged\\ordered-all-numeric.csv";
		
		String inputFilePath = "I:\\Copy\\Data\\2013\\merged\\JobStart.csv";
		String outputFilePath = "I:\\Copy\\Data\\2013\\merged\\JobStart-numeric.csv";
		r.replaceFile(inputFilePath, outputFilePath);

	}
	
}
