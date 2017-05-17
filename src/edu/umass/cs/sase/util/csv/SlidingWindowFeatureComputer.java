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
import java.io.IOException;

/**
 * Never used..
 * @author haopeng
 *
 */
public class SlidingWindowFeatureComputer {
	String inputFolder;
	long windowSize;
	long slidingSize;
	long startTimestamp;
	public SlidingWindowFeatureComputer(String inputFolder, long windowSize, long slidingSize){
		this.inputFolder = inputFolder;
		this.windowSize = windowSize;
		this.slidingSize = slidingSize;
	}
	public void compute() throws IOException{
		long start= 1373658550307L;
		long end = 1373660271269L;
		String inputFolder= "I:\\Copy\\Data\\2013\\singleTypeSelected\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM17\\secondtest\\";
		File folder = new File(normalOutputFolder);
		if(!(folder.exists())){
			folder.mkdirs();
		}
		int partitionIndex = 3;
		String partitionValue = "1115040022";

		
		
		
		TimeInterval normalInterval = new TimeInterval(start, end);//normal
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		
		FolderFeatureComputer computer = new FolderFeatureComputer(normalOutputFolder, start, end);
		computer.computeFeatures();
		computer.printFeatureInfo();
	}
	
	public static void main(String args[]){

	}

}
