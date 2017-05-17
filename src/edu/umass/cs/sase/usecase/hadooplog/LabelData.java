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
import java.io.IOException;

public class LabelData {
	public static void labelSingleType() throws IOException{
		GangliaHadoopEventSorter sorter = new GangliaHadoopEventSorter();
		//String csvFile = "I:\\Copy\\Data\\2013\\singleType\\proc_run_event.txt.csv";
		//String outputFile = "I:\\Copy\\Data\\2013\\labeled\\proc_run_event_labeled.csv";
		
		String csvFile = "I:\\Copy\\Data\\2013\\singleType\\load_fifteen_event.txt.csv";
		String outputFile = "I:\\Copy\\Data\\2013\\labeled\\load_fifteen_event.txt_labeled.csv";

		
		//int startTime[] = {637583030,624867328,630274770,647741800,658550307};
		//int middleTime[] = {639906229,624881352,630292844,649648791,659789027};
		//int endTime[] = {642072543,626384107,631831971,651635718,661672953};
		int startTime = 637583030;
		int endTime = 642072543;
		int abnormalStartTime = 637583030;
		int abnormalEndTime = 639906229;
		
		sorter.readCSVAndLabel(csvFile, outputFile, startTime, endTime, abnormalStartTime, abnormalEndTime);
		
	}
	
	public static void labelSingleTypeMultipleFiles() throws IOException{
		GangliaHadoopEventSorter sorter = new GangliaHadoopEventSorter();
		String csvFolder = "I:\\Copy\\Data\\2013\\singleType\\";
		File folder = new File(csvFolder);
		File[] csvFiles = folder.listFiles();
		for(File f: csvFiles){
			String csvFile = f.getAbsolutePath();
			String outputFile = f.getAbsolutePath().replaceFirst("singleType", "labeled");
			int startTime = 637583030;
			int endTime = 642072543;
			int abnormalStartTime = 637583030;
			int abnormalEndTime = 639906229;
			
			sorter.readCSVAndLabel(csvFile, outputFile, startTime, endTime, abnormalStartTime, abnormalEndTime);
			
		}
	}
	
	public static void main(String args[]) throws IOException{
		//LabelData.labelSingleType();
		LabelData.labelSingleTypeMultipleFiles();
	}

}
