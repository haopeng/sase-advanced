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
 * Assume all files in one folder have the same structure
 * This class merges all these files into one file, without ordering.
 * @author haopeng
 *
 */
public class MergeWithoutOrder {
	int fileCount;
	CSVWriter writer;
	
	public void mergeFolder(String inputFolder, String outputFile) throws IOException{
		this.writer = new CSVWriter(new FileWriter(outputFile),',',CSVWriter.NO_QUOTE_CHARACTER);
		File folder = new File(inputFolder);
		this.fileCount = 0;
		File[] files = folder.listFiles();
		for(File file: files){
			this.mergeFile(file);
		}
		this.writer.flush();
		this.writer.close();
	}
	public void mergeFile(File file) throws IOException{
		System.out.println("Merging file No." + this.fileCount);
		CSVReader reader = new CSVReader(new FileReader(file));
		String nextLine[] = reader.readNext();
		if(fileCount == 0){//write the attribute names
			this.writer.writeNext(nextLine);
		}
		int lineCount = 0;
		while((nextLine = reader.readNext())!= null){
			this.writer.writeNext(nextLine);
			System.out.println("File:" + this.fileCount + ",Line:" + lineCount);
			lineCount ++;
		}
		reader.close();
		this.writer.flush();
		fileCount ++;
	}
	
	public static void main(String args[]) throws IOException{
		
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeOneTable\\";
		String outputFilePath = "I:\\Copy\\Data\\2013\\merged\\noorder.csv";
		
		/*
		String inputFolder = "I:\\Copy\\Data\\2013\\test\\";
		String outputFilePath = "I:\\Copy\\Data\\2013\\testmerge\\noorder.csv";
		*/
		//Todo: label
		MergeWithoutOrder m = new MergeWithoutOrder();
		m.mergeFolder(inputFolder, outputFilePath);
		
	}
}
