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
import java.util.ArrayList;

import edu.umass.cs.sase.stream.HadoopEvent;

public class PairGenerator {
	String inputFolder;
	String outputFilePath;
	
	ArrayList<HadoopEvent> events;
	
	public PairGenerator(String input, String output) {
		this.inputFolder = input;
		this.outputFilePath = output;
		this.events = new ArrayList<HadoopEvent>();
	}
	
	public void readPullData() throws IOException {
		//read Pull Start
		System.out.println("Reading pull start...");
		File folder = new File(this.inputFolder);
		File[] files = folder.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				System.out.println("Reading file:" + f.getAbsolutePath());
				this.readFile(f);
			}
		}
		
		//read Pull Finish
		for (File f: files) {
			if (f.isDirectory()) {
				File[] subfiles = f.listFiles();
				for (File sub : subfiles) {
					System.out.println("Reading file:" + f.getAbsolutePath());
					this.readFile(sub);
				}
			}
		}
		
		//write
		this.writeEventsToFile();
	}
	
	public void writeEventsToFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputFilePath));
	
		for(int i = 0; i < events.size(); i ++){
			System.out.println(i + ":" + events.get(i).toStringSelectedContentOnly());
			writer.write(events.get(i).toStringSelectedContentOnly() + "\n");
		}
		
		writer.flush();
		writer.close();
		
		System.out.println(this.events.size() + " events have been written to " + this.outputFilePath);
	}
	
	public void readFile(File f) throws IOException {
		if (f.getName().contains(".DS_Store")) {
			return;
		}
		
		BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()));
		String line = br.readLine();
		while(line != null){
			events.add(new HadoopEvent(line));
			line = br.readLine();
		}
		br.close();
	}
	
	public void readMapReduceData() throws IOException {
		System.out.println("Reading map/reduce files...");
		File folder = new File(this.inputFolder);
		File[] files = folder.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				System.out.println("Reading file:" + f.getAbsolutePath());
				this.readFile(f);
			}
		}
		
		
		
		this.writeEventsToFile();
	}
	
	public static void main(String args[]) throws IOException {
		String inputFolder = "/Users/haopeng/Copy/Data/20150426/hadoop/event/pulldata";
		String outputFolder = "/Users/haopeng/Copy/Data/20150426/hadoop/pairs/pullpairs.txt";
		
		if (args.length > 1) {
			inputFolder = args[0];
			outputFolder = args[1];
		}
		
		PairGenerator pg = new PairGenerator(inputFolder, outputFolder);
		pg.readPullData();
	}
	

}
