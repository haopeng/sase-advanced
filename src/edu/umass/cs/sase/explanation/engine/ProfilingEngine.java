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

package edu.umass.cs.sase.explanation.engine;

import java.io.IOException;

public class ProfilingEngine extends ExplainEngine{
	static long startTime;
	static long endTime;
	
	static long readRawStart;
	static long readRawEnd;
	
	static long generateFeatureStart;
	static long generateFeatureEnd;
	
	static long computeDistanceStart;
	static long computeDistanceEnd;
	
	static long validationStart;
	static long validationEnd;
	
	//read raw
	public static int totalReadEvent;
	public static int uselessEvent;
	
	//validation
	public static long readPartitionStart;
	public static long readPartitionEnd;
	
	public static long decideAlignmentStart;
	public static long decideAlignmentEnd;
	
	public static long alignmentStart;
	public static long alignmentEnd;
	
	public static long labelStart;
	public static long labelEnd;
	
	public static long validateStart;
	public static long validateEnd;
	
	//feature validation
	public static long readAndGenerateStart;
	public static long readAndGenerateEnd;
	
	public static long mergeStart;
	public static long mergeEnd;
	
	public static long recomputeStart;
	public static long recomputeEnd;
	
	public static long cutStart;
	public static long cutEnd;
	
	public static long clusterStart;
	public static long clusterEnd;
	
	//read and generate
	public static long readTime;
	public static long generateTime;
	
	//merge
	public static long addPointTime;
	
	
	public ProfilingEngine(String inputPropertiesFile) throws IOException {
		super(inputPropertiesFile);
	}
	public void initialize() {
		totalReadEvent = 0;
		uselessEvent = 0;
	}
	public void runEngine() throws Exception {
		startTime = System.currentTimeMillis();
		
		//read raw events
		readRawStart = System.currentTimeMillis();
		this.readRawEvents();
		readRawEnd = System.currentTimeMillis();
		
		// generate features
		generateFeatureStart = System.currentTimeMillis();
		this.generateFeatures();
		generateFeatureEnd = System.currentTimeMillis();
		//compute distance and sort
		computeDistanceStart = System.currentTimeMillis();
		this.computeDistance();
		computeDistanceEnd = System.currentTimeMillis();
		//Label related partitions and recompute features over broader dataset
		validationStart = System.currentTimeMillis();
		this.validateFeatures();
		validationEnd = System.currentTimeMillis();
		endTime = System.currentTimeMillis();
	}
	
	public void printProfiling() {
		double total = (double)(endTime - startTime) / 1000.0;
		double readRawTotal = (double)(readRawEnd - readRawStart) / 1000.0;
		double generateFeatureTotal = (double)(generateFeatureEnd - generateFeatureStart) / 1000.0;
		double computeDistanceTotal = (double)(computeDistanceEnd - computeDistanceStart) / 1000.0;
		double validationTotal = (double)(validationEnd - validationStart) / 1000.0;
		
		double totalPercentage = (double)total / (double)total * 100.0;
		double readRawPercentage = (double)readRawTotal / (double)total * 100.0;
		double generateFeaturePercentage = (double)generateFeatureTotal / (double)total * 100.0;
		double computeDistancePercentage = (double)computeDistanceTotal / (double)total * 100.0;
		double validationPercentage = (double) validationTotal / (double)total * 100.0;
		
		System.out.println();
		System.out.println("******************************Profililng Results******************************");
		System.out.println("Item\tReadRaw\tGenerateFeatures\tComputeDistance\tValidation\tTotal");
		System.out.println("Time(Seconds)\t" + readRawTotal + "\t" + generateFeatureTotal + "\t" + computeDistanceTotal + "\t" + validationTotal + "\t" + total);
		System.out.println("Percentage(%)\t" + readRawPercentage + "\t" + generateFeaturePercentage + "\t" + computeDistancePercentage + "\t" + validationPercentage + "\t" + totalPercentage);
		System.out.println("******************************End of Profiling******************************");
		
		int usefulEvent = totalReadEvent - uselessEvent;
		double uselessPercentage = (double)uselessEvent / (double)totalReadEvent * 100.0; 
		double usefulPercentage = 100.0 - uselessPercentage;
		System.out.println("**********Proifling Reading Raw**********");
		System.out.println("Item\tUsefulEvents\tUselessEvents\tTotalEvents");
		System.out.println("Number\t" + usefulEvent + "\t" + uselessEvent + "\t" + totalReadEvent);
		System.out.println("Percentage\t" + usefulPercentage + "\t" + uselessPercentage + "\t100");
		System.out.println("**********End of profiling reading raw*******");
		
		double readPartitionTotal = (double)(readPartitionEnd - readPartitionStart) / 1000.0;
		double decideAlignmentTotal = (double)(decideAlignmentEnd - decideAlignmentStart) / 1000.0;
		double alignmentTotal = (double)(alignmentEnd - alignmentStart) / 1000.0;
		double labelTotal = (double)(labelEnd - labelStart) / 1000.0;
		double validateTotal = (double)(validateEnd - validateStart) / 1000.0;
		
		double readPartitionPercentage = readPartitionTotal / validationTotal * 100.0;
		double decideAlignmentPercentage = decideAlignmentTotal / validationTotal * 100.0;
		double alignmentPercentage = alignmentTotal / validationTotal * 100.0;
		double labelPercentage = labelTotal / validationTotal * 100.0;
		double validatePercentage = validateTotal / validationTotal * 100.0;
		
		System.out.println("***********Profiling Validation*********");
		System.out.println("Item\tReadPartition\tDecideAlignment\tAlignment\tLabel\tValidate");
		System.out.println("Time(seconds)\t" + readPartitionTotal + "\t" + decideAlignmentTotal + "\t" + alignmentTotal + "\t" + labelTotal + "\t" + validateTotal);
		System.out.println("Percentage(%)\t" + readPartitionPercentage + "\t" + decideAlignmentPercentage + "\t" + alignmentPercentage + "\t" + labelPercentage + "\t" + validatePercentage);
		
		
		System.out.println("***********End of Profiling Validation*********");
		
		
		double readAndGenerateTotal = (double)(readAndGenerateEnd - readAndGenerateStart) / 1000.0;
		double readAndGeneratePercentage = readAndGenerateTotal / validateTotal * 100.0;
		
		double mergeTotal = (double)(mergeEnd - mergeStart) / 1000.0;
		double mergePercentage = mergeTotal / validateTotal * 100.0;
		
		double recomputeTotal = (double)(recomputeEnd - recomputeStart) / 1000.0;
		double recomputePercentage = recomputeTotal / validateTotal * 100.0;
		
		double cutTotal = (double)(cutEnd - cutStart) / 1000.0;
		double cutPercentage = cutTotal / validateTotal * 100.0;
		
		double clusterTotal = (double)(clusterEnd - clusterStart) / 1000.0;
		double clusterPercentage = clusterTotal / validateTotal * 100.0;
		
		System.out.println("*****Profiling validate*****");
		System.out.println("Item\tReadAndGenerate\tMerge\tRecompute\tCut\tCluster");
		System.out.println("Time(seconds)\t" + readAndGenerateTotal + "\t" + mergeTotal + "\t" + recomputeTotal + "\t" + cutTotal + "\t" + clusterTotal);
		System.out.println("Percentage(%)\t" + readAndGeneratePercentage + "\t" + mergePercentage + "\t" + recomputePercentage + "\t" + cutPercentage + "\t" + clusterPercentage);
		System.out.println("*****End of Profiling validate*****");
		
		double readTimeSeconds = (double) readTime / 1000.0;
		double readPercentage = readTimeSeconds / readAndGenerateTotal * 100.0;
		
		double generateTimeSeconds = (double) generateTime / 1000.0;
		double generatePercentage = generateTimeSeconds / readAndGenerateTotal * 100.0;
		
		double addPointTimeSeconds = (double)addPointTime / 1000.0;
		double addPointPercentage = addPointTimeSeconds/ mergeTotal * 100.0;
		
		System.out.println("***Level 4: ReadAndGenerate  || Merge Profiling ***");
		System.out.println("Item\tRead\tGenerate\tAddPoints");
		System.out.println("Time(Seconds)\t" + readTimeSeconds + "\t" + generateTimeSeconds + "\t" + addPointTimeSeconds );
		System.out.println("Percentage(%)\t" + readPercentage + "\t" + generatePercentage + "\t" + addPointPercentage);
		System.out.println("***Level 4: ReadAndGenerate  || Merge Profiling ***");
		
		
	}
	
	public void resetProfiling() {
		totalReadEvent = 0;
		uselessEvent = 0;
		readTime = 0;
		generateTime = 0;
		addPointTime = 0;
	}

}
