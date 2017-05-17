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
package edu.umass.cs.sase.util.resultAnalyzer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class GroupResult {
	
	String fileLoc;
	String title;
	String schema;
	HashMap<Integer, Tuple> tuples;
	ArrayList<Integer> sortedKeys;
	
	
	public GroupResult(String fileLoc, String title) throws IOException{
		this.fileLoc = fileLoc;
		this.title = title;
		this.tuples = new HashMap<Integer, Tuple>();
		this.readResult();
		this.sortKeys();
		
	}
	
	public void readResult() throws IOException{
		File f = new File(this.fileLoc);
		//this.title = f.getName();
		
		FileInputStream fstream = new FileInputStream(f.getPath());
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine = br.readLine();
		this.schema = strLine;
		
		while((strLine = br.readLine()) != null){
			this.parseLine(strLine);
		}
		
		
	}
	
	public void parseLine(String line){
		Tuple newTuple = new Tuple(line, this.schema);
		this.tuples.put(new Integer(newTuple.getId()), newTuple);
		
		
		//10-20130428-01:01:45.result	86011	 85979	0.00232	 682	 100.0%	 0.0%	0%	null	 232	356	null	null	null	 4	null	 compute-1-1.dbcluster
		/*
		String lineNumberStr = line.substring(0, line.indexOf('-'));
		//System.out.println(lineNumberStr);
		Integer lineNumber = new Integer(Integer.parseInt(lineNumberStr));
		this.tuples.put(lineNumber, line);
		*/
	}
	
	public void sortKeys(){
		this.sortedKeys = new ArrayList<Integer>(this.tuples.keySet()) ;
		Collections.sort(this.sortedKeys);
		//return null;
	}
	
	public String getAttributeValue(Integer id, String attributeName){
		Tuple t = this.tuples.get(id);
		return t.getAttributeValue(attributeName);
		
	}
	public void printResult(){
		System.out.println(this.title);
		System.out.println(this.schema);
		for(int i = 0; i < this.sortedKeys.size(); i ++){
			//System.out.println("Key:" + this.sortedKeys.get(i) + "  " + this.tuples.get(this.sortedKeys.get(i)));
			System.out.println(this.tuples.get(this.sortedKeys.get(i)).getTupleStr());
			
		}
	}
	

	public String getFileLoc() {
		return fileLoc;
	}

	public void setFileLoc(String fileLoc) {
		this.fileLoc = fileLoc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public HashMap<Integer, Tuple> getTuples() {
		return tuples;
	}

	public void setTuples(HashMap<Integer, Tuple> tuples) {
		this.tuples = tuples;
	}

	public ArrayList<Integer> getSortedKeys() {
		return sortedKeys;
	}

	public void setSortedKeys(ArrayList<Integer> sortedKeys) {
		this.sortedKeys = sortedKeys;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String f = "f:\\result.txt";
		f = "f:\\testreader.txt";
		String title = "Timewindow";
		GroupResult gResult = new GroupResult(f, title);
		gResult.printResult();

	}

}
