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
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;



/*
 * This class is used to replace some parameters in script
 */
public class StringReplacement {

	String fileLocation;
	String folderName;
	String placeHolder;
	
	ArrayList<String> lines;
	
	int counter;

	public StringReplacement(String fileLoc, String placeHolder){
		this.counter = 0;
		this.fileLocation = fileLoc;
		this.placeHolder = placeHolder;
	}
	
	public void processFile() throws IOException{
		System.out.println("Processing file: " + this.fileLocation);
		this.readFileContent();
		this.writeFileContent();
	}
	
	public void processFolder() throws IOException{
		File folder = new File(this.fileLocation);
		ArrayList<File> fileQueue = new ArrayList<File>();
		File[] initialFiles = folder.listFiles();
		for(int i = 0; i < initialFiles.length; i ++){
			fileQueue.add(initialFiles[i]);
		}
		
		File file;
		while(fileQueue.size() > 0){
			file = fileQueue.get(0);
			fileQueue.remove(0);
			this.fileLocation = file.getAbsolutePath();
			if(file.isDirectory()){
				this.processFolder();
			}else{
				
				this.processFile();
			}
		}
		
		
	}
	
	
	
	
	/**
	 * Read all content of the files into memory, and locate the folderName
	 * @throws IOException 
	 */
	public void readFileContent() throws IOException{
		this.lines = new ArrayList<String>();
		FileInputStream fstream = new FileInputStream(this.fileLocation);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while((strLine = br.readLine()) != null){
			lines.add(strLine);
			this.findFolderName(strLine);
			
		}
		br.close();
	}
	
	public void findFolderName(String line){
		/*
		 * mkdir selectivity-false-precise-baseline
		 */
		if(line.contains("mkdir")){
			StringTokenizer st = new StringTokenizer(line);
			st.nextToken();
			this.folderName = st.nextToken();
			System.out.println("Foldername= " + this.folderName);
		}
	}
	
	/**
	 * Output the content, before write replace place holder with folder name
	 * @throws IOException 
	 */
	public void writeFileContent() throws IOException{
		File file = new File(this.fileLocation);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		String line;
		for(int i = 0; i < this.lines.size(); i ++){
			line = lines.get(i);
			
			//java edu.umass.cs.sase.util.ResultReader
			if(line.startsWith("java edu.umass.cs.sase.util.ResultReader")){
				line = "java edu.umass.cs.sase.util.ResultReader " + this.folderName + " >>" + this.folderName + "/result.txt"; 
			}
			
			
			
			bw.write(line);
			bw.newLine();
		}
		bw.close();
		this.counter ++;
		System.out.println(this.counter + " file processed");
		System.out.println("~~~~~~~~~~~~Done~~~~~~~~~");
	}
	
	/*
	 *
	 * 		String content = "This is the content to write into filaaaaae";
		 
		File file = new File("f:\\haha.txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.newLine();
		bw.write("cao");
		bw.write("\n gan");
		bw.close();

		System.out.println("Done");
	 */
	
	public static void main(String[] args) throws IOException {
		/*
		String file = "f:\\test.sh";
		StringReplacement sr = new StringReplacement(file, "$$");
		sr.processFile();
		*/
		
		String folder = "f:\\precise";
		StringReplacement sr = new StringReplacement(folder, "$$");
		sr.processFolder();
		
		/*
		String s = "haha$$";
		System.out.println(s);
		String b = s.replaceAll("haha", "lallalallalal");
		System.out.println(b);
		*/
	}

}
