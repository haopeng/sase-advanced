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
package edu.umass.cs.sase.explanation.simulation;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import net.sourceforge.jeval.EvaluationException;
import edu.umass.cs.sase.engine.Engine;
import edu.umass.cs.sase.query.NFA;
import edu.umass.cs.sase.stream.Event;
import edu.umass.cs.sase.stream.Stream;

public class QueryThread implements Runnable{
	String nfaLocation;
	String queryName;
	
	Stream inputStream;
	int streamIndex;
	int localCount;
	
	long duration;
	Engine cepEngine;
	
	int[] delays;
	boolean[] delayed;
	double[] avgs;
	int[] maxs;
	int[] sums;
	int[] counts;
	
	ArrayList<Integer> delayForNoExplanation;
	ArrayList<Integer> delayForExplanation;
	double avgDelayForNoExplanation;
	double avgDelayForExplanation;
	
	StringBuilder outputSB;
	public QueryThread(String nfaLocation, String queryName, Stream inputStream) {
		this.nfaLocation = nfaLocation;
		this.queryName = queryName;
		this.inputStream = inputStream;
		
		this.localCount = 0;
		this.streamIndex = 0;
		
		int duration = inputStream.getSize() / (int)SimulationSettings.streamRate;
		this.delays = new int[duration + 10];
		
		this.delayForNoExplanation = new ArrayList<Integer>();
		this.delayForExplanation = new ArrayList<Integer>();
		
		this.initializeEngine();
	}

	@Override
	public void run() {
		System.out.println(this.queryName + " starts to run!");
		while(true) {
			if (this.localCount < SimulationSettings.globalCount) {
				long start = System.currentTimeMillis();
				try {
					this.processOneBatch();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				} catch (EvaluationException e) {
					e.printStackTrace();
				}
				localCount ++;
				duration = System.currentTimeMillis() - start;
				
				int delay = SimulationSettings.globalCount - this.localCount;
				
				if (SimulationSettings.isExlaining) {
					this.delayForExplanation.add(delay);
				} else {
					this.delayForNoExplanation.add(delay);
				}
				
				/*
				if (delay > SimulationSettings.maxDelayTime) {
					//System.out.println("QueryThread" + this.queryName + " delayed by \t" + delay + " \tseconds.");//skip printing for test
					//this.delays[SimulationSettings.globalCount] = delay;
					this.delayForNoExplanation.add(delay);
				} else if (delay == 0) {
					this.delayForExplanation.add(0);
				}
				*/
			} else {
				int delay = SimulationSettings.globalCount - this.localCount;
				if (SimulationSettings.isExlaining) {
					this.delayForExplanation.add(delay);
				} else {
					this.delayForNoExplanation.add(delay);
				}
				//System.out.println("Debug: No delay. Thread will sleep for 0.5 second");
				try {
					long toSleep = Math.max(1000 - duration, 0);
					Thread.sleep(toSleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void processOneBatch() throws CloneNotSupportedException, EvaluationException {
		for (int i = 0; i < SimulationSettings.streamRate; i ++) {
			Event e = this.inputStream.getEventAtIndex(this.streamIndex);
			if (e == null) {

			} else {
				//process one event
				this.cepEngine.runSimulationEngineForEvent(e);
			}

			//System.out.println("Debug: processed one evnt" + e.toString() + "\t by query " + this.queryName);
			this.streamIndex ++;
		}
	}
	
	
	
	public void initializeEngine() {
		//initialize engine
		this.cepEngine = new Engine();
		this.cepEngine.initialize();
		//query
		NFA nfa = new NFA(nfaLocation);
		this.cepEngine.setNfa(nfa);
	}
	
	public void printAnalyticsHeader() {
		System.out.println("QueryName\tRepeat0-delayded\tCount\tSum\tAvgDelay\tMaxDelay\t" +
									   "Repeat1-delayed\tCount\tSum\tAvgDelay\tMaxDelay\t" +
									   "Repeat2-delayed\tCount\tSum\tAvgDelay\tMaxDelay\t" +
									   "Repeat3-delayed\tCount\tSum\tAvgDelay\tMaxDelay\t" +
									   "Repeat4-delayed\tCount\tSum\tAvgDelay\tMaxDelay\t"
				);
	}
	public void analyzeDelayInformationAndOutput() {
		this.delayed = new boolean[SimulationSettings.explanationRepeat];
		this.maxs = new int[SimulationSettings.explanationRepeat];
		this.avgs = new double[SimulationSettings.explanationRepeat];
		this.sums = new int[SimulationSettings.explanationRepeat ];
		this.counts = new int[SimulationSettings.explanationRepeat];
		this.outputSB = new StringBuilder();
		outputSB.append(this.queryName + "\t");
		
		for (int i = 0; i < SimulationSettings.explanationRepeat; i ++) {
			this.analyzeOneSlide(i);
		}
		

		
		System.out.println(this.outputSB.toString());
	}
	
	public void analyzeOneSlide(int repeatNumber) {
		int startIndex = SimulationSettings.startExplanation + repeatNumber * SimulationSettings.explanationInterval;
		int endIndex = startIndex + SimulationSettings.explanationInterval;
		
		int sum = 0;
		int max = 0;
		int count = 0;
		for (int i = startIndex; i <= endIndex; i ++) {
			sum += this.delays[i];
			max = Math.max(max, this.delays[i]);
			if (delays[i] > 0) {
				count ++;
			}
		}
		
		this.sums[repeatNumber] = sum;
		this.maxs[repeatNumber] = max;
		this.counts[repeatNumber] = count;
		if (count > 0) {
			this.avgs[repeatNumber] = (double)sum / (double)count;
			this.delayed[repeatNumber] = true;
			this.outputSB.append("1\t" + count + "\t" + sum + "\t" + this.avgs[repeatNumber] + "\t" + max + "\t");
		} else {
			this.outputSB.append("0\t0\t0\t0\t0\t");
		}
		
		
		

		
	}
	
	public void computeAvgDelay() {
		this.avgDelayForNoExplanation = this.computeAvgForList(this.delayForNoExplanation);
		this.avgDelayForExplanation = this.computeAvgForList(this.delayForExplanation);
	}
	
	
	
	
	public double computeAvgForList(ArrayList<Integer> list) {
		double[] array = new double[list.size()];
		for (int i = 0; i < list.size(); i ++) {
			array[i] = (double)list.get(i);
		}
		
		Mean mean = new Mean();
		return mean.evaluate(array);
		
	}
	
	
	public String getNfaLocation() {
		return nfaLocation;
	}

	public void setNfaLocation(String nfaLocation) {
		this.nfaLocation = nfaLocation;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public Stream getInputStream() {
		return inputStream;
	}

	public void setInputStream(Stream inputStream) {
		this.inputStream = inputStream;
	}

	public int getStreamIndex() {
		return streamIndex;
	}

	public void setStreamIndex(int streamIndex) {
		this.streamIndex = streamIndex;
	}

	public int getLocalCount() {
		return localCount;
	}

	public void setLocalCount(int localCount) {
		this.localCount = localCount;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Engine getCepEngine() {
		return cepEngine;
	}

	public void setCepEngine(Engine cepEngine) {
		this.cepEngine = cepEngine;
	}

	public int[] getDelays() {
		return delays;
	}

	public void setDelays(int[] delays) {
		this.delays = delays;
	}

	public boolean[] getDelayed() {
		return delayed;
	}

	public void setDelayed(boolean[] delayed) {
		this.delayed = delayed;
	}

	public double[] getAvgs() {
		return avgs;
	}

	public void setAvgs(double[] avgs) {
		this.avgs = avgs;
	}

	public int[] getMaxs() {
		return maxs;
	}

	public void setMaxs(int[] maxs) {
		this.maxs = maxs;
	}

	public int[] getSums() {
		return sums;
	}

	public void setSums(int[] sums) {
		this.sums = sums;
	}

	public int[] getCounts() {
		return counts;
	}

	public void setCounts(int[] counts) {
		this.counts = counts;
	}

	public ArrayList<Integer> getDelayForNoExplanation() {
		return delayForNoExplanation;
	}

	public void setDelayForNoExplanation(ArrayList<Integer> delayForNoExplanation) {
		this.delayForNoExplanation = delayForNoExplanation;
	}

	public ArrayList<Integer> getDelayForExplanation() {
		return delayForExplanation;
	}

	public void setDelayForExplanation(ArrayList<Integer> delayForExplanation) {
		this.delayForExplanation = delayForExplanation;
	}

	public double getAvgDelayForNoExplanation() {
		return avgDelayForNoExplanation;
	}

	public void setAvgDelayForNoExplanation(double avgDelayForNoExplanation) {
		this.avgDelayForNoExplanation = avgDelayForNoExplanation;
	}

	public double getAvgDelayForExplanation() {
		return avgDelayForExplanation;
	}

	public void setAvgDelayForExplanation(double avgDelayForExplanation) {
		this.avgDelayForExplanation = avgDelayForExplanation;
	}

	public StringBuilder getOutputSB() {
		return outputSB;
	}

	public void setOutputSB(StringBuilder outputSB) {
		this.outputSB = outputSB;
	}

	public static void main(String[] args) {
		double[] array = {1.0, 2.0, 3.0};
		Mean mean = new Mean();
		System.out.println(mean.evaluate(array));
	}
	
	
	
	
}
