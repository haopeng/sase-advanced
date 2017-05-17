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
package edu.umass.cs.sase.explanation.UI;

import java.io.PrintWriter;


/**
 * Singleton class to profile the running numbers
 * @author haopeng
 *
 */
public class ExplanationProfiling {
	//public PrintWriter out;
	
	private static ExplanationProfiling instance = null; 
	
	protected ExplanationProfiling() {
		//this.out = ExplanationSettings.out;
	}
	
	public static ExplanationProfiling getInstance() {
		if (instance == null) {
			instance = new ExplanationProfiling();
		}
		return instance;
	}
	
	
	public long overallStart;
	public long overallEnd;
	
	public long rawEventReadingStart;
	public long rawEventReadingEnd;
	
	public long timeSeriesRawGenerationStart;
	public long timeSeriesRawGenerationEnd;
	
	public long intervalFeatureGenerationStart;
	public long intervalFeatureGenerationEnd;
	
	public long windowTimeSeriesGenerationStart;
	public long windowTimeSeriesGenerationEnd;
	
	public long valueFeatureCompareStart;
	public long valueFeatureCompareEnd;
	
	public long timeseriesFeatureCompareStart;
	public long timeseriesFeatureCompareEnd;
	
	
	
	// new profiling numbers
	
	public static long featureGenerationTimeMilSec;
	public static long computeDisanceTimeMilSec;
	public static long mergeTimeMilSec;
	public static long clusteringTimeMilSec;
	public static long addPointTimeMilSec;
	
	public static void pringNewProfiling() {
		System.out.println("@@@@@@@@@@@@@@@@@New Profiling Numbers==============\n");
		System.out.println("Name/Cost\tFeatureGenaration\tComputeDistance\tMerge\tClustering\tAddPoint");
	}
	
	public void printProfiling() {
		System.out.println("\n===========Profiling Numbers==============\n");
		//out.println("\n===========Profiling Numbers==============\n");
		long overallRuntime = this.getOverallEnd() - this.getOverallStart();
		System.out.println("OverallRuntime:\t" + overallRuntime + "\t milliseconds");
		//out.println("OverallRuntime:\t" + overallRuntime + "\t milliseconds");
		
		long rawEventReadingRuntime = this.getRawEventReadingEnd() - this.getRawEventReadingStart();
		System.out.println("RawEventReadingRuntime:\t" + rawEventReadingRuntime + "\t milliseconds");
		//out.println("RawEventReadingRuntime:\t" + rawEventReadingRuntime + "\t milliseconds");
		
		long timeSeriesRawGenerationRuntime = this.getTimeSeriesRawGenerationEnd() - this.getTimeSeriesRawGenerationStart();
		System.out.println("TimeSeriesRawGenerationRuntime:\t" + timeSeriesRawGenerationRuntime + "\t milliseconds");
		//out.println("TimeSeriesRawGenerationRuntime:\t" + timeSeriesRawGenerationRuntime + "\t milliseconds");
		
		long intervalFeatureGenerationRuntime = this.intervalFeatureGenerationEnd - this.intervalFeatureGenerationStart;
		System.out.println("IntervalFeatureGenerationRuntime:\t" + intervalFeatureGenerationRuntime + "\t milliseconds");
		//out.println("IntervalFeatureGenerationRuntime:\t" + intervalFeatureGenerationRuntime + "\t milliseconds");
		
		long windowTimeSeriesGenerationRuntime = this.windowTimeSeriesGenerationEnd - this.windowTimeSeriesGenerationStart;
		System.out.println("WindowTimeSeriesFeatureGenerationRuntime:\t" + windowTimeSeriesGenerationRuntime + "\t milliseconds");
		//out.println("WindowTimeSeriesFeatureGenerationRuntime:\t" + windowTimeSeriesGenerationRuntime + "\t milliseconds");
		
		long valueFeatureCompareRuntime = this.valueFeatureCompareEnd - this.valueFeatureCompareStart;
		System.out.println("ValueFeatureCompareRuntime:\t" + valueFeatureCompareRuntime + "\t milliseconds");
		//out.println("ValueFeatureCompareRuntime:\t" + valueFeatureCompareRuntime + "\t milliseconds");
		
		long timeseriesFeatureCompareRuntime = this.timeseriesFeatureCompareEnd - this.timeseriesFeatureCompareStart;
		System.out.println("TimeSeriesFeatureCompareRuntime:\t" + timeseriesFeatureCompareRuntime + "\t milliseconds");
		//out.println("TimeSeriesFeatureCompareRuntime:\t" + timeseriesFeatureCompareRuntime + "\t milliseconds");
		
		System.out.println("\n===========Profiling Numbers END==============\n");
		//out.println("\n===========Profiling Numbers END==============\n");
	}

	public long getOverallStart() {
		return overallStart;
	}

	public void setOverallStart(long overallStart) {
		this.overallStart = overallStart;
	}

	public long getOverallEnd() {
		return overallEnd;
	}

	public void setOverallEnd(long overallEnd) {
		this.overallEnd = overallEnd;
	}

	public long getRawEventReadingStart() {
		return rawEventReadingStart;
	}

	public void setRawEventReadingStart(long rawEventReadingStart) {
		this.rawEventReadingStart = rawEventReadingStart;
	}

	public long getRawEventReadingEnd() {
		return rawEventReadingEnd;
	}

	public void setRawEventReadingEnd(long rawEventReadingEnd) {
		this.rawEventReadingEnd = rawEventReadingEnd;
	}

	
	public long getTimeSeriesRawGenerationStart() {
		return timeSeriesRawGenerationStart;
	}

	public void setTimeSeriesRawGenerationStart(long timeSeriesRawGenerationStart) {
		this.timeSeriesRawGenerationStart = timeSeriesRawGenerationStart;
	}

	public long getTimeSeriesRawGenerationEnd() {
		return timeSeriesRawGenerationEnd;
	}

	public void setTimeSeriesRawGenerationEnd(long timeSeriesRawGenerationEnd) {
		this.timeSeriesRawGenerationEnd = timeSeriesRawGenerationEnd;
	}

	public long getIntervalFeatureGenerationStart() {
		return intervalFeatureGenerationStart;
	}

	public void setIntervalFeatureGenerationStart(
			long intervalFeatureGenerationStart) {
		this.intervalFeatureGenerationStart = intervalFeatureGenerationStart;
	}

	public long getIntervalFeatureGenerationEnd() {
		return intervalFeatureGenerationEnd;
	}

	public void setIntervalFeatureGenerationEnd(long intervalFeatureGenerationEnd) {
		this.intervalFeatureGenerationEnd = intervalFeatureGenerationEnd;
	}

	public long getWindowTimeSeriesGenerationStart() {
		return windowTimeSeriesGenerationStart;
	}

	public void setWindowTimeSeriesGenerationStart(
			long windowTimeSeriesGenerationStart) {
		this.windowTimeSeriesGenerationStart = windowTimeSeriesGenerationStart;
	}

	public long getWindowTimeSeriesGenerationEnd() {
		return windowTimeSeriesGenerationEnd;
	}

	public void setWindowTimeSeriesGenerationEnd(long windowTimeSeriesGenerationEnd) {
		this.windowTimeSeriesGenerationEnd = windowTimeSeriesGenerationEnd;
	}

	public long getValueFeatureCompareStart() {
		return valueFeatureCompareStart;
	}

	public void setValueFeatureCompareStart(long valueFeatureCompareStart) {
		this.valueFeatureCompareStart = valueFeatureCompareStart;
	}

	public long getValueFeatureCompareEnd() {
		return valueFeatureCompareEnd;
	}

	public void setValueFeatureCompareEnd(long valueFeatureCompareEnd) {
		this.valueFeatureCompareEnd = valueFeatureCompareEnd;
	}

	public static void setInstance(ExplanationProfiling instance) {
		ExplanationProfiling.instance = instance;
	}

	public long getTimeseriesFeatureCompareStart() {
		return timeseriesFeatureCompareStart;
	}

	public void setTimeseriesFeatureCompareStart(long timeseriesFeatureCompareStart) {
		this.timeseriesFeatureCompareStart = timeseriesFeatureCompareStart;
	}

	public long getTimeseriesFeatureCompareEnd() {
		return timeseriesFeatureCompareEnd;
	}

	public void setTimeseriesFeatureCompareEnd(long timeseriesFeatureCompareEnd) {
		this.timeseriesFeatureCompareEnd = timeseriesFeatureCompareEnd;
	}

	

}
