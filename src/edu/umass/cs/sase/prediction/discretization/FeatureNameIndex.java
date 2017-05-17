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

package edu.umass.cs.sase.prediction.discretization;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import au.com.bytecode.opencsv.CSVReader;

/**
 * This class is used to map feature names to some number, and verse versa: from number to feature name
 * @author haopeng
 *
 */
public class FeatureNameIndex {
	String sourceFile;
	
	HashMap<String, Integer> featureNameToIndex;
	String[] featureList;
	
	public FeatureNameIndex(String file) throws IOException {
		this.sourceFile = file;
		this.readFeatureNames();
	}
	
	public void readFeatureNames() throws IOException {
		CSVReader reader = new CSVReader(new FileReader(this.sourceFile));
		featureList = reader.readNext();
		this.featureNameToIndex = new HashMap<String, Integer>();
		
		for (int i = 0; i < featureList.length; i ++) {
			this.featureNameToIndex.put(featureList[i], i);
		}
		
		reader.close();
	}
	
	public String getFeatureNameByIndex(int index) {
		if (index < 0 || index >= this.featureList.length) {
			return null;
		}
		
		return this.featureList[index];
	}
	
	public int getFeatureIndexByName(String featureName) {
		if (this.featureNameToIndex.containsKey(featureName)) {
			return this.featureNameToIndex.get(featureName);
		}
		
		return -1;
	}
	
	public int getMaxIndex() {
		return this.featureList.length - 1;
	}
	
	public static void main(String[] args) throws IOException {
		//test
		String file = "G:\\Dropbox\\research\\3rd\\code\\keel\\my data\\m14\\featureIndex.csv";
		FeatureNameIndex fni = new FeatureNameIndex(file);
		
		for (int i = 0; i <= fni.getMaxIndex(); i ++) {
			String name = fni.getFeatureNameByIndex(i);
			System.out.println(i + ":" + name);
			System.out.println(name + ":" + fni.getFeatureIndexByName(name));
			System.out.println("~~~~~~~~~~");
		}
	}

}
