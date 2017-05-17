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
package edu.umass.cs.sase.explanation.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


/**
 * This class is used to chop log files into small chunks by timestamps.
 * 
 * A chunk is created for a 1000-second window [0,)
 * chunk no. = timestamp / 1000
 * @author haopeng
 *
 *
 *Input: a file
 *Output: a folder with chunks with sliced content of the original file. (Header is copied into every file)
 */
public class FileChopper {
	String inputFilePath;
	String outputFolder;
	
	String[] header;
	CSVWriter writer;
	long windowSize = 1000000L; // A window is 1000 seconds 
	
	long currentFileNumber;
	String originalFileName;
	int count;
	public FileChopper(String inputFilePath, String outputFolderPath) {
		this.inputFilePath = inputFilePath;
		File file = new File(this.inputFilePath);
		String fileFullName = file.getName();
		
		this.originalFileName = this.removeExtension(fileFullName);

		this.currentFileNumber = 0L;
		
		String mainName = this.removeExtension(this.originalFileName);
		this.outputFolder = outputFolderPath + this.originalFileName + "/";
				
		File folder = new File(this.outputFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}
	
	public String removeExtension(String fileName) {
		StringTokenizer st = new StringTokenizer(fileName, ".");
		return st.nextToken();
	}
	
	public void chop() throws IOException {
		if (this.inputFilePath.contains(".DS_Store")) {
			return;
		}
		CSVReader reader = new CSVReader(new FileReader(this.inputFilePath));
		this.header = reader.readNext();
		
		String[] line;
		while((line = reader.readNext()) != null) {
			long fileNumber = this.getFileNumber(line);
			if (this.writer == null) {
				this.currentFileNumber = fileNumber;
				this.resetFileWriter();
			} else if (fileNumber > this.currentFileNumber) {
				this.writer.close();
				this.currentFileNumber = fileNumber;
				this.resetFileWriter();
			} 
			this.writer.writeNext(line);
		}
		
		this.writer.close();
		reader.close();
	}
	public void resetFileWriter() throws IOException {
		String fileName = this.outputFolder + this.originalFileName + "-" + this.currentFileNumber + ".csv";
		this.writer = new CSVWriter(new FileWriter(fileName));
		this.writer.writeNext(header);
		count ++;
		System.out.println(count + ": a new file is created at " + fileName);
	}
	public long getFileNumber(String[] line) {
		long timestamp = 0L;
		if (line[0].equals("g")) {
			timestamp = Long.parseLong(line[4]);
		} else {
			timestamp = Long.parseLong(line[7]);
		}
		
		long fileNumber = timestamp / this.windowSize;
		
		return fileNumber;
	}
	
	
	public static void main(String[] args) throws IOException {
		//String inputFile = "/Users/haopeng/Copy/Data/2015/singleTypes-all-chunk-original/boottime_event.csv";
		String inputFile = "/Users/haopeng/Copy/Data/2015/singleTypes-all-chunk-original/JobFinish.csv";
		String outputFolder = "/Users/haopeng/Copy/Data/2015/singleTypes-all-chunk/";
		FileChopper fc = new FileChopper(inputFile, outputFolder);
		fc.chop();
	}

}
