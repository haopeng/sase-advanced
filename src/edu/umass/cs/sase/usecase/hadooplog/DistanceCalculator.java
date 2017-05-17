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

/**
 * This class is to used to measure the distance between different events in batch
 * @author haopeng
 *
 */
public class DistanceCalculator {
	public static void computeAVG() throws IOException{
		int jobNum[] = {12, 9, 11, 14, 16};
		int startTime[] = {637583030,624867328,630274770,647741800,658550307};
		int middleTime[] = {639906229,624881352,630292844,649648791,659789027};
		int endTime[] = {642072543,626384107,631831971,651635718,661672953};
		
		String attribute = "value";
		
		String logDirectory = "I:\\Copy\\Data\\2013\\singleType\\";
		File direct = new File(logDirectory);
		File[] logs = direct.listFiles();
		GangliaHadoopEventSorter r = new GangliaHadoopEventSorter();
		for(int i = 0; i < logs.length; i ++){
			File currentLog = logs[i];
			System.out.println(i + ":" +currentLog.getName());
			for(int j = 0; j < jobNum.length; j ++){
				r.readCSVBetweenInterval(currentLog.getAbsolutePath(), startTime[j], middleTime[j]);
				double firstAvg = r.computeAVG(attribute); 
				r.readCSVBetweenInterval(currentLog.getAbsolutePath(), middleTime[j], endTime[j]);
				double secondAvg = r.computeAVG(attribute);
				double distance = Math.abs(firstAvg - secondAvg)/(Math.abs(firstAvg) + Math.abs(secondAvg)); 
				System.out.println(jobNum[j] + "\t" + firstAvg + "\t" + secondAvg + "\t" + distance);
			}
			
		}
	}
	
	
	public static void computeAVGByJob() throws IOException{
		int jobNum[] = {12, 9, 11, 14, 16};
		int startTime[] = {637583030,624867328,630274770,647741800,658550307};
		int middleTime[] = {639906229,624881352,630292844,649648791,659789027};
		int endTime[] = {642072543,626384107,631831971,651635718,661672953};
		
		String attribute = "value";
		
		String logDirectory = "I:\\Copy\\Data\\2013\\singleType\\";
		File direct = new File(logDirectory);
		File[] logs = direct.listFiles();
		GangliaHadoopEventSorter r = new GangliaHadoopEventSorter();
		for(int j = 0; j < jobNum.length; j ++){
			System.out.println("~~~~~~~~~~~~~~~~~" + jobNum[j] + "~~~~~~~~~~~~~");
			for(int i = 0; i < logs.length; i ++){
				File currentLog = logs[i];
				r.readCSVBetweenInterval(currentLog.getAbsolutePath(), startTime[j], middleTime[j]);
				double firstAvg = r.computeAVG(attribute); 
				r.readCSVBetweenInterval(currentLog.getAbsolutePath(), middleTime[j], endTime[j]);
				double secondAvg = r.computeAVG(attribute);
				double distance = Math.abs(firstAvg - secondAvg)/(Math.abs(firstAvg) + Math.abs(secondAvg)); 
				System.out.println(currentLog.getName() + "\t" + firstAvg + "\t" + secondAvg + "\t" + distance);

			}
		}
		
		
	}
	
	public static void computeDeviation() throws IOException{
		int jobNum[] = {12, 9, 11, 14, 16};
		int startTime[] = {637583030,624867328,630274770,647741800,658550307};
		int middleTime[] = {639906229,624881352,630292844,649648791,659789027};
		int endTime[] = {642072543,626384107,631831971,651635718,661672953};
		
		String attribute = "value";
		
		String logDirectory = "I:\\Copy\\Data\\2013\\singleType\\";
		File direct = new File(logDirectory);
		File[] logs = direct.listFiles();
		GangliaHadoopEventSorter r = new GangliaHadoopEventSorter();
		for(int i = 0; i < logs.length; i ++){
			File currentLog = logs[i];
			System.out.println(i + ":" +currentLog.getName());
			for(int j = 0; j < jobNum.length; j ++){
				r.readCSVBetweenInterval(currentLog.getAbsolutePath(), startTime[j], middleTime[j]);
				double firstDev = r.computeDeviation(attribute); 
				r.readCSVBetweenInterval(currentLog.getAbsolutePath(), middleTime[j], endTime[j]);
				double secondDev = r.computeDeviation(attribute);
				double distance = Math.abs(firstDev - secondDev)/(Math.abs(firstDev) + Math.abs(secondDev)); 
				System.out.println(jobNum[j] + "\t" + firstDev + "\t" + secondDev + "\t" + distance);
			}
			
		}
	}
	
	public static void computeDeviationByJob() throws IOException{
		int jobNum[] = {12, 9, 11, 14, 16};
		int startTime[] = {637583030,624867328,630274770,647741800,658550307};
		int middleTime[] = {639906229,624881352,630292844,649648791,659789027};
		int endTime[] = {642072543,626384107,631831971,651635718,661672953};
		
		String attribute = "value";
		
		String logDirectory = "I:\\Copy\\Data\\2013\\singleType\\";
		File direct = new File(logDirectory);
		File[] logs = direct.listFiles();
		GangliaHadoopEventSorter r = new GangliaHadoopEventSorter();
		for(int j = 0; j < jobNum.length; j ++){
			System.out.println("~~~~~~~~~~~~~~" + jobNum[j] +"~~~~~~~~~~~~");
			for(int i = 0; i < logs.length; i ++){
				File currentLog = logs[i];
				r.readCSVBetweenInterval(currentLog.getAbsolutePath(), startTime[j], middleTime[j]);
				double firstDev = r.computeDeviation(attribute); 
				r.readCSVBetweenInterval(currentLog.getAbsolutePath(), middleTime[j], endTime[j]);
				double secondDev = r.computeDeviation(attribute);
				double distance = Math.abs(firstDev - secondDev)/(Math.abs(firstDev) + Math.abs(secondDev)); 
				System.out.println(currentLog.getName() + "\t" + firstDev + "\t" + secondDev + "\t" + distance);

			}
		}
		
		

	}
	
	public static void main(String args[]) throws IOException{
		//DistanceCalculator.computeAVG();
		//DistanceCalculator.computeDeviation();
		//DistanceCalculator.computeAVGByJob();
		DistanceCalculator.computeDeviationByJob();
	}
}
