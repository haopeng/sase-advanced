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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScriptGrouper {
	public static void generateGeneral(){
		//String folderLoc = "F:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\precise\\false";
		String folderLoc = "F:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\precise\\inconsistent";
		folderLoc = "F:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\precise\\true";
		folderLoc = "f:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\imprecise\\inconsistent";
		folderLoc = "f:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\imprecise2.0\\true-false";
		folderLoc = "f:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\imprecise2.0\\true-false-half5";
		
		
		String keywordPrefix = "selectivity";
		keywordPrefix = "timewindow";
		
		String filterPrefix = "group";
		
		File folder = new File(folderLoc);
		File[] files = folder.listFiles();
		String tempFileName;
		for(int i = 0; i < files.length; i ++){
			tempFileName = files[i].getName();
			
			/*
			if(tempFileName.startsWith(keywordPrefix)){
				System.out.println("./" + tempFileName);
			}
			*/
			if(!tempFileName.startsWith(filterPrefix)){
				System.out.println("./" + tempFileName);
			}
		}
	}
	
	public static void generateDetails() throws IOException{
		//String folderLoc = "F:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\precise\\false";
		String folderLoc = "F:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\precise\\inconsistent";
		folderLoc = "F:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\precise\\true";
		folderLoc = "c:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\precise\\true-false";
		
		folderLoc = "f:\\Dropbox\\code\\workspace\\sase-opensource\\scripts\\imprecise\\true-false";
		
		
		String keywordPrefix = "selectivity";
		keywordPrefix = "timewindow";
		
		File folder = new File(folderLoc);
		File[] files = folder.listFiles();
		String tempFileName;
		for(int i = 0; i < files.length; i ++){
			tempFileName = files[i].getName();
			if(tempFileName.startsWith(keywordPrefix)){
				System.out.println("#" + tempFileName);
				ScriptGrouper.printFile(files[i].getPath());
			}
			
		}
	}
	
	public static void printFile(String filePath) throws IOException{
		System.out.println("#####################");
		FileInputStream fstream = new FileInputStream(filePath);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int count = 0;
		strLine = br.readLine();
		while((strLine = br.readLine()) != null){
			System.out.println(strLine);
		}
		
		System.out.println("###############");
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
			//ScriptGrouper.generateDetails();
		ScriptGrouper.generateGeneral();

	}

}
