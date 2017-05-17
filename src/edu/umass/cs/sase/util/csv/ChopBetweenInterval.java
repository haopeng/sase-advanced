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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class ChopBetweenInterval {
	String partitionValue;
	int paritionIndex;
	
	public ChopBetweenInterval(int partitionIndex, String partitionValue){
		this.paritionIndex = partitionIndex;
		this.partitionValue = partitionValue;
	}
	
	public void chopFolder(String inputFolder, String outputFolder, TimeInterval interval) throws IOException{
		File inFolder = new File(inputFolder);
		File[] inputFiles = inFolder.listFiles();
		for(File file : inputFiles){
			if(file.getName().endsWith(".csv")){
				this.chopBetween(file.getAbsolutePath(), outputFolder + file.getName(), interval);
			}
		}
		
		
		
	}
	public void chopBetween(String inputFilePath, String outputFilePath, TimeInterval interval) throws IOException{
		CSVReader reader = new CSVReader(new FileReader(inputFilePath));
		CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String nextLine[] = reader.readNext();
		writer.writeNext(nextLine);
		int lineCount = 0;
		int chopCount = 0;
		while((nextLine = reader.readNext()) != null){
			if(nextLine[0].startsWith("h")){
				//filter
				if(this.filter(nextLine[this.paritionIndex])){
					if(interval.containsTime(Long.parseLong(nextLine[7]))){
						writer.writeNext(nextLine);
						chopCount ++;
						System.out.println(lineCount + ": kept");
					}else{
						System.out.println(lineCount + ": removed");
					}
				}
			}else{
				if(interval.containsTime(Long.parseLong(nextLine[4]))){
					writer.writeNext(nextLine);
					chopCount ++;
					System.out.println(lineCount + ": kept");
				}else{
					System.out.println(lineCount + ": removed");
				}

			}
			
			lineCount ++;
		}
		writer.flush();
		writer.close();
		reader.close();
		System.out.println("Total chopped records: " + chopCount);

	}
	
	public boolean filter(String partitionValue){
		//filter events in the same partition!
		//currently, hard coded
		//partition attributed: jobId
		//partition value: 1115040019
		/*
		if(partitionValue.equalsIgnoreCase("1115040019")){
			return false;
		}
		*/
		if(partitionValue.equalsIgnoreCase(this.partitionValue)){
			return false;
		}

		return true;
	}
	
	
	public static void main(String args[]) throws IOException{
		//TimeInterval interval = new TimeInterval(1373166015000L, 1373166135000L);
		//TimeInterval interval = new TimeInterval(1373637583030L, 1373659789027L);
		//String inputFilePath = "I:\\Copy\\Data\\2013\\merged\\noorder-labeled-3abnormal-periods.csv";
		//String outputFilePath = "I:\\Copy\\Data\\2013\\merged\\noorder-labeled-3abnormal-periods-chopped.csv";
		
		//String inputFilePath = "H:\\Data\\ordered-all-numeric-sliding.csv";
		//String outputFilePath = "G:\\Data\\ordered-all-numeric-sliding-chopped.csv";
		//test
//		String inputFilePath = "I:\\Copy\\Data\\2013\\singleTypeOneTable\\balance.txt.csv";
	//	String outputFilePath = "I:\\Copy\\Data\\2013\\test\\chopped.csv";
		
		/* for the annoated one
		TimeInterval abnormalInterval = new TimeInterval(1373637583030L, 1373639906229L);//abnormal
		TimeInterval normalInterval = new TimeInterval(1373639906229L, 1373642072543L);//normal
	
		String inputFolder= "I:\\Copy\\Data\\2013\\singleType";
		String abnormalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\abnormal\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\normal\\";
		
		ChopBetweenInterval abnormalChopper = new ChopBetweenInterval();
		abnormalChopper.chopFolder(inputFolder, abnormalOutputFolder, abnormalInterval);
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval();
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m9
		/*
		TimeInterval normalInterval = new TimeInterval(1373624867328L, 1373626384107L);//normal
	
		String inputFolder= "I:\\Copy\\Data\\2013\\singleTypeSelectedTop5\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM9\\total\\";
		int partitionIndex = 3;
		String partitionValue = "1115040015";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m11
		/*
		TimeInterval normalInterval = new TimeInterval(1373630274770L, 1373631831971L);//normal
		
		String inputFolder= "I:\\Copy\\Data\\2013\\singleTypeSelectedTop5\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM11\\total\\";
		
		int partitionIndex = 3;
		String partitionValue = "1115040017";
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		
		//m14, total
		/*
		TimeInterval normalInterval = new TimeInterval(1373647741800L, 1373651635718L);//normal
	
		String inputFolder= "I:\\Copy\\Data\\2013\\singleTypeSelectedTop5\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM14\\total\\";
		int partitionIndex = 3;
		String partitionValue = "1115040021";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		
		
		//m14, first half
/*				
						TimeInterval normalInterval = new TimeInterval(1373647741800L, 1373649648791L);//normal
					
						String inputFolder= "I:\\Copy\\Data\\2013\\singleTypeSelectedTop5\\";
						String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM14\\first\\";
						int partitionIndex = 3;
						String partitionValue = "1115040021";
						
						ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
						
						normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
	*/					
		//m14, second half
		/*
		TimeInterval normalInterval = new TimeInterval(1373649648791L, 1373651635718L);//normal
	
		String inputFolder= "I:\\Copy\\Data\\2013\\singleTypeSelectedTop5\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM14\\second\\";
		int partitionIndex = 3;
		String partitionValue = "1115040021";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/			
		//m16, total
		/*
		TimeInterval normalInterval = new TimeInterval(1373658550307L, 1373661672953L);//normal
		
		String inputFolder= "I:\\Copy\\Data\\2013\\singleTypeSelectedTop5\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM16\\total\\";
		int partitionIndex = 3;
		String partitionValue = "1115040023";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m16, first
		/*
		TimeInterval normalInterval = new TimeInterval(1373658550307L, 1373659789027L);//normal
		
		String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM16\\alltype-first\\";
		int partitionIndex = 3;
		String partitionValue = "1115040023";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m16, second
		/*		
		TimeInterval normalInterval = new TimeInterval(1373659789027L, 1373661672953L);//normal
		
		String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM16\\alltype-second\\";
		int partitionIndex = 3;
		String partitionValue = "1115040023";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m12, first
				/*
				TimeInterval normalInterval = new TimeInterval(1373637583030L, 1373639906229L);//normal
				
				String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
				String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM12\\alltype-first\\";
				int partitionIndex = 3;
				String partitionValue = "1115040019";
				
				ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
				
				normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
				*/
				//m12, second
				/*		
				TimeInterval normalInterval = new TimeInterval(1373639906229L, 1373642072543L);//normal
				
				String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
				String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM12\\alltype-second\\";
				int partitionIndex = 3;
				String partitionValue = "1115040019";
				
				ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
				
				normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
				*/
				//m14, first half
						/*
										TimeInterval normalInterval = new TimeInterval(1373647741800L, 1373649648791L);//normal
									
										String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
										String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM14\\alltype-first\\";
										int partitionIndex = 3;
										String partitionValue = "1115040021";
										
										ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
										
										normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
							*/			
						//m14, second half
						/*
						TimeInterval normalInterval = new TimeInterval(1373649648791L, 1373651635718L);//normal
					
						String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
						String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM14\\alltype-second\\";
						int partitionIndex = 3;
						String partitionValue = "1115040021";
						
						ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
						
						normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
						*/
		
		//m9, second half, no first half
		/*
		TimeInterval normalInterval = new TimeInterval(1373624881352L, 1373626384107L);//normal
		
		String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM9\\alltype-second\\";
		File folder = new File(normalOutputFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		int partitionIndex = 3;
		String partitionValue = "1115040015";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m10, second half, no first half
		/*
				TimeInterval normalInterval = new TimeInterval(1373626369667L, 1373629584488L);//normal
				
				String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
				String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM10\\alltype-second\\";
				File folder = new File(normalOutputFolder);
				if(!folder.exists()){
					folder.mkdirs();
				}
				int partitionIndex = 3;
				String partitionValue = "1115040016";
				
				ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
				
				normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m11, second half, no first
		/*
		TimeInterval normalInterval = new TimeInterval(1373630292844L, 1373631831971L);//normal
		
		String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM11\\alltype-second\\";
		File folder = new File(normalOutputFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		int partitionIndex = 3;
		String partitionValue = "1115040017";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m13, second half, no first
		/*
				TimeInterval normalInterval = new TimeInterval(1373637083632L, 1373640534084L);//normal
				
				String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
				String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM13\\alltype-second\\";
				File folder = new File(normalOutputFolder);
				if(!folder.exists()){
					folder.mkdirs();
				}
				int partitionIndex = 3;
				String partitionValue = "1115040018";
				
				ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
				
				normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m15, second half, no first
		/*
		TimeInterval normalInterval = new TimeInterval(1373646846199L, 1373650105493L);//normal
		
		String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM15\\alltype-second\\";
		File folder = new File(normalOutputFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		int partitionIndex = 3;
		String partitionValue = "1115040020";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m17, second half, no first
		/*
				TimeInterval normalInterval = new TimeInterval(1373657118483L, 1373660271269L);//normal
				
				String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
				String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM17\\alltype-second\\";
				File folder = new File(normalOutputFolder);
				if(!folder.exists()){
					folder.mkdirs();
				}
				int partitionIndex = 3;
				String partitionValue = "1115040022";
				
				ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
				
				normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
			*/
		
		//m13, normal (first)
				/*
						//TimeInterval normalInterval = new TimeInterval(1373637083632L, 1373640534084L);//all
						TimeInterval normalInterval = new TimeInterval(1373637083632L, 1373639906229L);//normal
						
						String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
						String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM13\\alltype-normal\\";
						File folder = new File(normalOutputFolder);
						if(!folder.exists()){
							folder.mkdirs();
						}
						int partitionIndex = 3;
						String partitionValue = "1115040018";
						
						ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
						
						normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
				*/

		//m13, abnormal (second)
		/*
		//TimeInterval normalInterval = new TimeInterval(1373637083632L, 1373640534084L);//all
		TimeInterval normalInterval = new TimeInterval(1373639906229L, 1373640534084L);//abnormal
		
		String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM13\\alltype-abnormal\\";
		File folder = new File(normalOutputFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		int partitionIndex = 3;
		String partitionValue = "1115040018";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		
		//m15, normal
		/*
		//TimeInterval normalInterval = new TimeInterval(1373646846199L, 1373650105493L);//all
		TimeInterval normalInterval = new TimeInterval(1373646846199L, 1373649648791L);//normal
		
		String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM15\\alltype-normal\\";
		File folder = new File(normalOutputFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		int partitionIndex = 3;
		String partitionValue = "1115040020";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
		*/
		//m15, abnormal
				/*
				//TimeInterval normalInterval = new TimeInterval(1373646846199L, 1373650105493L);//all
				TimeInterval normalInterval = new TimeInterval(1373649648791L, 1373650105493L);//abnormal
				
				String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
				String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM15\\alltype-abnormal\\";
				File folder = new File(normalOutputFolder);
				if(!folder.exists()){
					folder.mkdirs();
				}
				int partitionIndex = 3;
				String partitionValue = "1115040020";
				
				ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
				
				normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
				*/	
		//m17, normal
				/*
				//TimeInterval normalInterval = new TimeInterval(1373657118483L, 1373660271269L);//all
				TimeInterval normalInterval = new TimeInterval(1373657118483L,1373659789027L);//normal
				
				String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
				String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM17\\alltype-normal\\";
				File folder = new File(normalOutputFolder);
				if(!folder.exists()){
					folder.mkdirs();
				}
				int partitionIndex = 3;
				String partitionValue = "1115040022";
				
				ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
				
				normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
			*/
		//m17, abnormal
		
		//TimeInterval normalInterval = new TimeInterval(1373657118483L, 1373660271269L);//all
		TimeInterval normalInterval = new TimeInterval(1373659789027L, 1373660271269L);//abnormal
		
		String inputFolder= "I:\\Copy\\Data\\2013\\singleType\\";
		String normalOutputFolder= "I:\\Copy\\Data\\2013\\singleTypeChopped\\PartitionM17\\alltype-abnormal\\";
		File folder = new File(normalOutputFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		int partitionIndex = 3;
		String partitionValue = "1115040022";
		
		ChopBetweenInterval normalChopper = new ChopBetweenInterval(partitionIndex, partitionValue);
		
		normalChopper.chopFolder(inputFolder, normalOutputFolder, normalInterval);
	
	}
}
