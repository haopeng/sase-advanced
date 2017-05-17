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

package edu.umass.cs.sase.util.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVWriter;
import edu.umass.cs.sase.util.csv.FileFeatureComputer;

public class FolderFeatureComputer {
	String folderPath;
	long startTime;
	long endTime;
	HashMap<String, ComputedFeature> featureIndex;
	public FolderFeatureComputer(String folderPath, long startTime, long endTime){
		this.folderPath = folderPath;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public void computeFeatures() throws IOException{
		this.featureIndex = new HashMap<String, ComputedFeature>();
		File folder = new File(this.folderPath);
		File[] files = folder.listFiles();
		for(File f : files){
			FileFeatureComputer fFeatureComputer = new FileFeatureComputer(f.getAbsolutePath(), this.startTime, this.endTime);
			fFeatureComputer.compute();
			fFeatureComputer.printFeatures();
			ArrayList<ComputedFeature> features = fFeatureComputer.getFeatures();
			for(ComputedFeature feature: features){
				this.featureIndex.put(feature.getFeatureSignature(), feature);
			}
		}
		
	}
	public void printFeatureInfo(){
		System.out.println("There are total:" + this.featureIndex.size() + " features");
		for(ComputedFeature f: this.featureIndex.values()){
			System.out.println(f.getFeatureSignature() + "\t" + f.getFeatureValue());
		}
		
	}
	
	public void writeFeatureToFile(String outputFolder, String label) throws IOException{
		CSVWriter writer = new CSVWriter(new FileWriter(outputFolder + "\\" + this.startTime + "-" + this.endTime +"-" +label +".csv"), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String[] nextLine = new String[4];
		nextLine[0] = "FeatureName";
		nextLine[1] = "FeatureValue";
		nextLine[2] = "MaxValue";
		nextLine[3] = "MinValue";
		writer.writeNext(nextLine);
		for(ComputedFeature f: this.featureIndex.values()){
			nextLine[0] = f.getFeatureSignature();
			nextLine[1] = f.getFeatureValue() + "";
			nextLine[2] = f.getMaxValue() + "";
			nextLine[3] = f.getMinValue() + "";
			writer.writeNext(nextLine);
		}
		nextLine[0] = "Label";
		nextLine[1] = label;// abnormal or normal
		writer.writeNext(nextLine);
		
		writer.close();
	}
	
	public String getFolderPath() {
		return folderPath;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	public HashMap<String, ComputedFeature> getFeatureIndex() {
		return featureIndex;
	}
	public void setFeatureIndex(HashMap<String, ComputedFeature> featureIndex) {
		this.featureIndex = featureIndex;
	}
	public static void main(String args[]) throws IOException{
		
		
		//String folderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\abnormal";
		/* m12, annotated
		String folderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\normal";
		
		long normalStart= 1373639906229L;
		long normalEnd =1373642072543L ;
		FolderFeatureComputer computer = new FolderFeatureComputer(folderPath, normalStart, normalEnd);
		computer.computeFeatures();
		computer.printFeatureInfo();
		*/
		//m9 TimeInterval normalInterval = new TimeInterval(1373624867328L, 1373626384107L);//normal
		/*
		String folderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM9\\total";
		
		long normalStart= 1373624867328L;
		long normalEnd =1373626384107L;
		FolderFeatureComputer computer = new FolderFeatureComputer(folderPath, normalStart, normalEnd);
		computer.computeFeatures();
		computer.printFeatureInfo();
		*/
		//m11
		
		//m14, total TimeInterval normalInterval = new TimeInterval(1373647741800L, 1373651635718L);//normal
		/*
		String folderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM14\\total";
		
		long normalStart= 1373647741800L;
		long normalEnd =1373651635718L;
		FolderFeatureComputer computer = new FolderFeatureComputer(folderPath, normalStart, normalEnd);
		computer.computeFeatures();
		computer.printFeatureInfo();
		*/
		//m16 TimeInterval normalInterval = new TimeInterval(1373658550307L, 1373661672953L);//normal
		/*
		String folderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM16\\total";
		
		long normalStart= 1373658550307L;
		long normalEnd = 1373661672953L;
		FolderFeatureComputer computer = new FolderFeatureComputer(folderPath, normalStart, normalEnd);
		computer.computeFeatures();
		computer.printFeatureInfo();
		*/
		
		//m14, first TimeInterval normalInterval = new TimeInterval(1373647741800L, 1373649648791L);//normal
		/*		
		String folderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM14\\first";
		
		long normalStart= 1373647741800L;
		long normalEnd =1373649648791L;
		FolderFeatureComputer computer = new FolderFeatureComputer(folderPath, normalStart, normalEnd);
		computer.computeFeatures();
		computer.printFeatureInfo();
		*/

		//m14, second //TimeInterval normalInterval = new TimeInterval(1373649648791L, 1373651635718L);//normal
		/*		
		String folderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM14\\second";
		
		long normalStart= 1373649648791L;
		long normalEnd = 1373651635718L;
		FolderFeatureComputer computer = new FolderFeatureComputer(folderPath, normalStart, normalEnd);
		computer.computeFeatures();
		computer.printFeatureInfo();
		*/
		
		//m16, first TimeInterval normalInterval = new TimeInterval(1373658550307L, 1373659789027L);//normal
		/*
		String folderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM16\\first";
		
		long normalStart= 1373658550307L;
		long normalEnd = 1373659789027L;
		FolderFeatureComputer computer = new FolderFeatureComputer(folderPath, normalStart, normalEnd);
		computer.computeFeatures();
		computer.printFeatureInfo();
		*/
		//m16, second TimeInterval normalInterval = new TimeInterval(1373659789027L, 1373661672953L);//normal
		
		String folderPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM16\\secondtest";
		
		long normalStart= 1373659789027L;
		long normalEnd = 1373661672953L;
		FolderFeatureComputer computer = new FolderFeatureComputer(folderPath, normalStart, normalEnd);
		computer.computeFeatures();
		computer.printFeatureInfo();
		
		computer.writeFeatureToFile("I:\\Copy\\Data\\2013\\slidingwindows\\test", "normal");

	}
}
