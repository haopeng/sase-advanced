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

public class FitIntoOneTable {
	
	String[] eventTypes;
	HashMap<String, Integer> typeIndex;
	String[] attributes;
	String[] values;
	int indexForCurrentFile;
	
	public FitIntoOneTable(String eventTypeFolder){
		this.readAllTypes(eventTypeFolder);
		
	}
	
	public void readAllTypes(String folderPath){
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		this.typeIndex = new HashMap<String, Integer>();
		this.eventTypes = new String[files.length];
		int count = 0;
		for(File file : files){
			String fileName = file.getName();
			String type = this.typeForFileName(fileName);
			System.out.println(count + ".File name:" + fileName + ", event type:" + type);
			eventTypes[count] = type;
			typeIndex.put(type, count);
			count ++;
		}
		
	}
	public String typeForFileName(String fileName){
		int dotPosition = fileName.indexOf(".");
		String type = fileName.substring(0, dotPosition);
		return type;
	}
	public void convertFile(String filePath, String outputFilePath) throws IOException{
		CSVReader reader = new CSVReader(new FileReader(filePath));
		CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String[] nextLine;
		nextLine = reader.readNext();//attributes
		this.attributes = new String[nextLine.length + this.eventTypes.length];
		int count = 0;
		for(String s: nextLine){
			this.attributes[count] = s;
			count ++;
		}
		for(String s: this.eventTypes){
			this.attributes[count] = s;
			count ++;
		}
		String currentType = this.typeForFileName(new File(filePath).getName());
		this.indexForCurrentFile = this.typeIndex.get(currentType);
		writer.writeNext(this.attributes);
		writer.flush();
		this.values = new String[this.attributes.length];
		for(int i = 0; i < this.values.length; i ++){
			this.values[i] = "0";
		}
		int lineCount = 0;
		while((nextLine = reader.readNext())!= null){
			count = 0;
			lineCount ++;
			for(String s: nextLine){
				this.values[count] = s;
				count ++;
			}
			this.values[count + this.indexForCurrentFile] = nextLine[count - 1];
			writer.writeNext(this.values);
			writer.flush();
			System.out.println(filePath + ", Line-"+lineCount);
		}
		reader.close();
		writer.close();
		
		
	}
	
	public void convertFolder(String inputFolderPath, String outputFolderPath) throws IOException{
		File inputFolder = new File(inputFolderPath);
		File[] files = inputFolder.listFiles();
		for(File f: files){
			String name = f.getName();
			this.convertFile(f.getAbsolutePath(), outputFolderPath + name);
		}
	}
	
	public static void main(String args[]) throws IOException{
		String folderPath = "I:\\Copy\\Data\\2013\\singleTypeExtend\\";
		FitIntoOneTable f = new FitIntoOneTable(folderPath);
		
		String inputFolderPath = "I:\\Copy\\Data\\2013\\singleTypeExtend\\";
		String outputFolderPath = "I:\\Copy\\Data\\2013\\singleTypeOneTable\\";
		
		f.convertFolder(inputFolderPath, outputFolderPath);
		
		
		/*
		String inputFile = "I:\\Copy\\Data\\2013\\test\\proc_run_event.txt.csv";
		String outputFile = "I:\\Copy\\Data\\2013\\singleTypeOneTable\\proc_run_event.OneTable.csv";
		f.convertFile(inputFile, outputFile);
		*/
	}
}
