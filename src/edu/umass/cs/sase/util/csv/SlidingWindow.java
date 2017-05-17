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

import au.com.bytecode.opencsv.CSVWriter;

/**
 * This one is used to generate the sliding windows
 * @author haopeng
 *
 */
public class SlidingWindow {
	//1373658550307L
	long startingTimestamp;
	long endingTimestamp;
	long windowSize;//minute
	
	long slidingStep;//seconds
	String inputFolder;
	String outputFolder;
	
	public SlidingWindow(long st, long endTime, long ws, long slidingStep, String inputFolder, String outputFolder){
		this.startingTimestamp = st;
		this.endingTimestamp = endTime;
		this.windowSize = ws;
		this.slidingStep = slidingStep;
		//this.endingTimestamp = st + ws * 1000L * 60L;// convert minute to miliseconds
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
		File folder = new File(this.outputFolder);
		if(!(folder.exists())){
			folder.mkdirs();
		}
	}
	public void computeFeatures(String label) throws IOException{
		long windowStart = this.startingTimestamp;
		
		int n = 1;
		while(windowStart < this.endingTimestamp){
			long windowEnd = Math.min(windowStart + this.windowSize * 1000L * 60L, this.endingTimestamp);
			//
			System.out.println("Computing for window:" + n + "\t, starting time:" + windowStart + "\t, endingTime:" + windowEnd);
			
			
			FolderFeatureComputer folderFC = new FolderFeatureComputer(this.inputFolder, windowStart, windowEnd);
			folderFC.computeFeatures();
			folderFC.printFeatureInfo();
			folderFC.writeFeatureToFile(this.outputFolder, label);
			
			windowStart += this.slidingStep * 1000L;//second to milliseconds
			n ++;
		}
		
		
	}
	
	public static void main(String[] args) throws IOException{
		//m16, abnormal
		/*
		long startingTimestamp = 1373658550307L;
		long endTimestamp = 1373659789027L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM16\\alltype-first";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m16\\all\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("0");//abnormal
		*/
		
		//m16, normal
				/*
				long startingTimestamp = 1373659789027L;
				long endTimestamp = 1373661672953L;
				long windowSize = 5L;//5 minutes
				long slidingStep = 30L;//30 seconds
				String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM16\\alltype-second";
				String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m16\\all\\";
				
				SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
				
				sw.computeFeatures("1");//normal
				*/
		//m12, abnormal
				/*
				long startingTimestamp = 1373637583030L;
				long endTimestamp = 1373639906229L;
				long windowSize = 5L;//5 minutes
				long slidingStep = 30L;//30 seconds
				String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM12\\alltype-first";
				String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m12\\all\\";
				
				SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
				
				sw.computeFeatures("0");//abnormal
				*/
				//m12, normal
				/*
				long startingTimestamp = 1373639906229L;
				long endTimestamp = 1373642072543L;
				long windowSize = 5L;//5 minutes
				long slidingStep = 30L;//30 seconds
				String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM12\\alltype-second";
				String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m12\\all-new\\";
				
				SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
				
				sw.computeFeatures("1");//normal
				*/
		
		//m14, abnormal
		/*
		long startingTimestamp = 1373647741800L;
		long endTimestamp = 1373649648791L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM14\\alltype-first";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m14\\all\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("0");//abnormal
		*/
		//m14, normal
		/*
		long startingTimestamp = 1373649648791L;
		long endTimestamp = 1373651635718L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM14\\alltype-second";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m14\\all\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("1");//normal
		*/			
		//m9, normal
		//TimeInterval normalInterval = new TimeInterval(1373624881352L, 1373626384107L);//normal
		
		/*
		long startingTimestamp = 1373624881352L;
		long endTimestamp = 1373626384107L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM9\\alltype-second";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m9\\all-new\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("1");//normal
		*/
		
		//m10, normal
		//TimeInterval normalInterval = new TimeInterval(1373626369667L, 1373629584488L);//normal
		
		long startingTimestamp = 1373626369667L;
		long endTimestamp = 1373629584488L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM10\\alltype-second";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m10\\all-new\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("1");//normal
		
		//m12, normal, test
		/*
		long startingTimestamp = 1373639906229L;
		long endTimestamp = 1373642072543L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM12\\alltype-second";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m12\\all-test\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("1");//normal
		*/
		//m11, normal
				//TimeInterval normalInterval = new TimeInterval(1373630292844L, 1373631831971L);//normal
		/*		
		long startingTimestamp = 1373630292844L;
				long endTimestamp = 1373631831971L;
				long windowSize = 5L;//5 minutes
				long slidingStep = 30L;//30 seconds
				String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM11\\alltype-second";
				String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m11\\all\\";
				
				SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
				
				sw.computeFeatures("1");//normal
		*/
/*		//m13, normal
		//TimeInterval normalInterval = new TimeInterval(1373637083632L, 1373640534084L);//normal
		long startingTimestamp = 1373637083632L;
		long endTimestamp = 1373640534084L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM13\\alltype-second";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m13\\all\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("1");//normal
*/
/*		//m15, normal
		//		TimeInterval normalInterval = new TimeInterval(1373646846199L, 1373650105493L);//normal

		long startingTimestamp = 1373646846199L;
		long endTimestamp = 1373650105493L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM15\\alltype-second";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m15\\all\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("1");//normal
*/
		//m17, normal
		/*
		//TimeInterval normalInterval = new TimeInterval(1373657118483L, 1373660271269L);//normal
		long startingTimestamp = 1373657118483L;
		long endTimestamp = 1373660271269L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM17\\alltype-second";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m17\\all\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("1");//normal
		*/
		
		//m13, normal
		
		/*
		//TimeInterval normalInterval = new TimeInterval(1373637083632L, 1373639906229L);//normal
		long startingTimestamp = 1373637083632L;
		long endTimestamp = 1373639906229L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM13\\alltype-normal";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m13\\all-new\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("1");//normal
		*/
		
		//m13, abnormal
		/*
		//TimeInterval normalInterval = new TimeInterval(1373639906229L, 1373640534084L);//abnormal
		long startingTimestamp = 1373639906229L;
		long endTimestamp = 1373640534084L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM13\\alltype-abnormal";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m13\\all-new\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("0");//abnormal
		*/
		//m15, normal
		/*
		//TimeInterval normalInterval = new TimeInterval(1373646846199L, 1373649648791L);//normal
		long startingTimestamp = 1373646846199L;
		long endTimestamp = 1373649648791L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM15\\alltype-normal";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m15\\all-new\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("1");//normal
		*/
		
		
		//m15, abnormal
		
		/*
		TimeInterval normalInterval = new TimeInterval(1373649648791L, 1373650105493L);//abnormal
		long startingTimestamp = 1373649648791L;
		long endTimestamp = 1373650105493L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM15\\alltype-abnormal";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m15\\all-new\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("0");//abnormal
		*/

		//m17, normal
		
		//TimeInterval normalInterval = new TimeInterval(1373657118483L,1373659789027L);//normal
		/*
		long startingTimestamp = 1373657118483L;
		long endTimestamp = 1373659789027L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM17\\alltype-normal";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m17\\all-new\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("1");//normal
		*/
		
		//m17, abnormal
		/*
		TimeInterval normalInterval = new TimeInterval(1373659789027L, 1373660271269L);//abnormal
		long startingTimestamp = 1373659789027L;
		long endTimestamp = 1373660271269L;
		long windowSize = 5L;//5 minutes
		long slidingStep = 30L;//30 seconds
		String inputFolder = "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM17\\alltype-abnormal";
		String outputFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m17\\all-new\\";
		
		SlidingWindow sw = new SlidingWindow(startingTimestamp, endTimestamp, windowSize, slidingStep, inputFolder, outputFolder);
		
		sw.computeFeatures("0");//abnormal
		*/
	}
}
