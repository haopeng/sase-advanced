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

package edu.umass.cs.sase.usecase.serverlog;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
public class FileReader {
	String filePath;
	int numberOfLine;
	
	int countOfLines;
	
	
	HashMap<String, Integer> serverName;
	HashMap<String, Integer> command;
	HashMap<String, Integer> timestamp;
	HashMap<String, Integer> path;
	HashMap<String, Integer> method;
	
	public FileReader(String filePath, int numberOfLine){
		this.filePath = filePath;
		this.numberOfLine = numberOfLine;
		
		this.countOfLines = 0;
		serverName = new HashMap<String, Integer>();
		command = new HashMap<String, Integer>();
		this.timestamp = new HashMap<String, Integer>();
		this.path = new HashMap<String, Integer>();
		this.method = new HashMap<String, Integer>();
	}
	public void parseFile() throws IOException{
		FileInputStream fstream = new FileInputStream(this.filePath);
		  // Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
	
		int count = 0;
		// skip the first line;
		//strLine = br.readLine();
		strLine = br.readLine();
		while(count <this.numberOfLine && strLine != null){
			
			this.parseLine(strLine);
			
			strLine = br.readLine();
			count ++;
			
		}
		
		
	
		in.close();
		  
	}
	
	
	public void parseLine(String line){
		this.countOfLines ++;
		//System.out.println("Line" + this.countOfLines + ":\t" + line);
		
		StringTokenizer st = new StringTokenizer(line, "_|_");
		String token = null;
		//1st
		token = st.nextToken();
		//this.addToken(token, this.serverName);
		
		//2
		token = st.nextToken();
		//this.addToken(token, this.command);
		
		//3rd
		token = st.nextToken();
		//this.addToken(token, this.timestamp);
		
		//4th
		if(st.hasMoreTokens()){
			token = st.nextToken();
			try{
				Integer.parseInt(token);
			}catch(NumberFormatException e){
				//this.addToken(token, this.path);
			}
			
			
		}else{
			System.out.println(line);
		}
		
		
		
		//5th
		if(st.hasMoreTokens()){
			token = st.nextToken();
			this.addToken(token, this.method);
		}
		
		
	}
	
	public void addToken(String token, HashMap<String, Integer> attribute){
		if(attribute.containsKey(token)){
			int oldValue = attribute.get(token);
			attribute.put(token, ++oldValue );
		}else{
			attribute.put(token, 1);
		}
	}
	
	public void printValues(HashMap<String, Integer> attribute, String attributeName){
		
		int counter = 0;
		System.out.println("**************************");
		System.out.println("There are " + attribute.size() + " values for " + attributeName + " from " + this.countOfLines + " lines");
		System.out.println("No.\tValue\tTimes\t");
		for(Map.Entry<String, Integer> entry: attribute.entrySet()){
			counter ++;
			System.out.println("No."+counter + "\t" + entry.getKey() + "\t" + entry.getValue());
		
		}
		
	}
	
	

	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getNumberOfLine() {
		return numberOfLine;
	}
	public void setNumberOfLine(int numberOfLine) {
		this.numberOfLine = numberOfLine;
	}
	public HashMap<String, Integer> getServerName() {
		return serverName;
	}
	public void setServerName(HashMap<String, Integer> serverName) {
		this.serverName = serverName;
	}
	public HashMap<String, Integer> getCommand() {
		return command;
	}
	public void setCommand(HashMap<String, Integer> command) {
		this.command = command;
	}
	public HashMap<String, Integer> getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(HashMap<String, Integer> timestamp) {
		this.timestamp = timestamp;
	}
	public HashMap<String, Integer> getPath() {
		return path;
	}
	public void setPath(HashMap<String, Integer> path) {
		this.path = path;
	}
	public HashMap<String, Integer> getMethod() {
		return method;
	}
	public void setMethod(HashMap<String, Integer> method) {
		this.method = method;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String folder = "/Users/haopeng/Dropbox/research/2nd/Usecase/Yanlei/";
		String fileName = "P0_2324.txt";
		
		FileReader reader = new FileReader(folder + fileName, 20000000);
		reader.parseFile();
		
		//reader.printValues(reader.getServerName(), "ServerName");
		//reader.printValues(reader.getCommand(), "Command");
		//reader.printValues(reader.getTimestamp(), "Timestamp");  
		//reader.printValues(reader.getPath(), "Path");
		reader.printValues(reader.getMethod(), "Method");
	}
	
	

}
