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
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ResultComparator {
	
	HashMap<String, String> firstResult;
	HashMap<String, String> secondResult;
	String firstResultFile;
	String secondResultFile;
	
	public ResultComparator(String first, String second) throws IOException{
		this.firstResultFile = first;
		this.secondResultFile = second;
		this.firstResult = new HashMap<String, String>();
		this.secondResult = new HashMap<String, String>();
		this.readResult(this.firstResultFile, this.firstResult);
		this.readResult(this.secondResultFile, this.secondResult);
	}
	
	
	public void readResult(String file, HashMap<String, String> result) throws IOException{
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while((line = br.readLine()) != null){
			if(line.startsWith("Debug:")){
				result.put(line, line);
			}
		}
		
	}
	
	public void compare(){
		int counter = 0;
		String firstKey;
		for (Entry<String, String> entry : this.firstResult.entrySet()) {
			counter ++;
			firstKey = entry.getKey();
		    //System.out.println(counter +":Key = " + entry.getKey() + ", Value = " + entry.getValue());
			if(!this.secondResult.containsKey(firstKey)){
				System.out.println(firstKey);
			}
		}
		
		
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String firstFile = "F:\\Dropbox\\research\\2nd\\debug\\post.txt";
		String secondFile = "F:\\Dropbox\\research\\2nd\\debug\\nopost.txt";
		ResultComparator comparator = new ResultComparator(firstFile, secondFile);
		comparator.compare();

	}

}
