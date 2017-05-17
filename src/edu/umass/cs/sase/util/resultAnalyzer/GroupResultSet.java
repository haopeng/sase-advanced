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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GroupResultSet {

	
	String rootFolderLoc;
	ArrayList<GroupResult> results;
	
	public GroupResultSet(String root) throws IOException{
		this.rootFolderLoc = root;
		this.results = new ArrayList<GroupResult>();
		this.readResults();
	}
	
	
	public void readResults() throws IOException{
		File rootFolder = new File(this.rootFolderLoc);
		File[] subFolders = rootFolder.listFiles();
		for(int i = 0; i < subFolders.length; i ++){
			if(subFolders[i].isDirectory()){
				String resultPath = subFolders[i].getPath() + "\\result.txt";
				GroupResult result = new GroupResult(resultPath, subFolders[i].getName());
				this.results.add(result);
			}
		}
		
	}
	
	public void printResults(){
		for(int i = 0; i < this.results.size(); i ++){
			GroupResult r = this.results.get(i);
			r.printResult();
			System.out.println();
		}
	}
	
	public void printAttribute(String attributeName){
		StringBuilder sb = new StringBuilder();
		sb.append(attributeName + "\t");
		GroupResult tempResult;
		for(int i = 0; i < this.results.size(); i ++){
			tempResult = this.results.get(i);
			sb.append(tempResult.getTitle() + "\t");
		}
		sb.append("\n");
		tempResult = this.results.get(0);
		ArrayList<Integer> sortedKeys = tempResult.getSortedKeys();
		for(int i = 0; i < sortedKeys.size(); i ++){
			sb.append(sortedKeys.get(i) + "\t");
			for(int j = 0; j < this.results.size(); j ++){
				tempResult = this.results.get(j);
				sb.append(tempResult.getAttributeValue(sortedKeys.get(i), attributeName) + "\t");
			}
			sb.append("\n");
		}
		
		System.out.println(sb.toString());
		
	}
	
	/**
	 * Used to extract multiple sub-groups of experiments
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String folderPath = "F:\\2.true-false-timewindow";
		folderPath = "f:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\1.true-false-selectivity";
		folderPath = "f:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\2.true-false-timewindow";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\3.false-selectivity";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\4.false-timewindow";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\5.true-selectivity";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\6.true-timewindow";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\7.inconsistent-selectivity";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\8.inconsistent-timewindow";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Imprecise\\1.true-false-selectivity";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Imprecise\\3.true-false-uncertaintyinterval";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Imprecise\\2.true-false-timewindow";
		
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Imprecise2.0\\a.1";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\imprecise3.0\\confidence";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\imprecise3.0\\true";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\imprecise3.0\\true-false";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\dpc\\half3";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\dpc\\half1";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\dpc\\half2";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\dpc\\obelix\\half5";		
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\dpc\\obelix\\half5-largerwindow";

		
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\3.false-selectivity";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\4.false-timewindow";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\5.true-selectivity";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\6.true-timewindow";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\7.inconsistent-selectivity";
		folderPath = "F:\\Dropbox\\research\\2nd\\Experiments\\2013\\Precise4.0\\8.inconsistent-timewindow";
		
		
		GroupResultSet rSet = new GroupResultSet(folderPath);
		rSet.printResults();
		//rSet.printAttribute("averageThroughput");
		rSet.printAttribute("Throughput");
		rSet.printAttribute("Number Of Runs Created");
		rSet.printAttribute("Time for compute confidence");
		rSet.printAttribute("Time on Create New Run Baseline");
		rSet.printAttribute("Time for sorting events in imprecise");
		rSet.printAttribute("Pattern matching time");
		rSet.printAttribute("Enumeration time");
		rSet.printAttribute("Time for compute confidence");
		rSet.printAttribute("Total Running Time");
		
	}

}
