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

import java.io.File;
import java.util.HashMap;

public class JobCategorizor {
	String inputFolder;
	HashMap<String, String> jobTypeIndex;
	public JobCategorizor(String inputFolder){
		this.inputFolder = inputFolder;
		this.jobTypeIndex = new HashMap<String, String>();
		this.parseFolder();
	}
	
	public JobCategorizor(){
		this.inputFolder =  "H:\\Copy\\Data\\2013\\hadoop\\log\\jtnn\\history\\done";
		this.jobTypeIndex = new HashMap<String, String>();
		this.parseFolder();
	}
	
	public void parseFolder(){
		File folder = new File(inputFolder);
		File[] allFiles = folder.listFiles();
		for(int i = 0; i < allFiles.length; i ++){
			//System.out.println(i + ":" + allFiles[i].getName());
			this.parseFileName(allFiles[i].getName());
		}
	}
	public void parseFileName(String fileName){
		//example: obelix8.local_1373569450477_job_201307111504_0023_boduo_worldcup+frequent+users
		//String jobIdString = "" + Integer.parseInt(fileName.substring(38, 49).replaceAll("_", ""));//2013
		if(fileName.endsWith(".xml")) {
			return;
		}
		System.out.println(fileName);
		String jobIdString = "" + Integer.parseInt(fileName.substring(39, 50).replaceAll("_", ""));
		String jobCategoryString = fileName.substring(56);
		System.out.println(jobIdString + ":" + jobCategoryString);
		//System.out.println(fileName + ":" + jobIdString + "---" + jobCategoryString);
		this.jobTypeIndex.put(jobIdString, jobCategoryString);
		if(jobCategoryString.startsWith("worldcup+sessionize")){
			System.out.println(jobIdString);
		}
	}
	public String getInputFolder() {
		return inputFolder;
	}
	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}
	public HashMap<String, String> getJobTypeIndex() {
		return jobTypeIndex;
	}

	public void setJobTypeIndex(HashMap<String, String> jobTypeIndex) {
		this.jobTypeIndex = jobTypeIndex;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String inputFolder = "F:\\Copy\\Data\\2013\\hadoop\\log\\jtnn\\history\\done";
		
		String inputFolder = "/Users/haopeng/Copy/Data/2015/hadoop/head/hadoop-system-logs/compute-0-28.yeeha/history/done";
		JobCategorizor c = new JobCategorizor(inputFolder);
		
	}
}
