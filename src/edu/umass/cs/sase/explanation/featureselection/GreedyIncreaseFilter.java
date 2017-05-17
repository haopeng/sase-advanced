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
package edu.umass.cs.sase.explanation.featureselection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import au.com.bytecode.opencsv.CSVWriter;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.visualize.XYLineChart;

public class GreedyIncreaseFilter extends MaxDistanceCorrelationFilter{

	ArrayList<TimeSeriesFeaturePair> preFilteredFeatures;
	ArrayList<TimeSeriesFeaturePair> preFilteredFeaturesCopy;
	
	HashMap<String, Double> correlationIndex;
	HashMap<String, Double> abnormalCorrelationIndex;
	HashMap<String, Double> referenceCorrelationIndex;
	
	//from son
	ArrayList<TimeSeriesFeaturePair> selectedWithoutSingular;//used to compute multiple correlation
	XYSeriesCollection dataset;
	
	String outputFilePath = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/dimishing return/greedy/";
	
	int iteration;
	
	String saveFilePrefix;
	
	ArrayList<String[]> linesToWrite;//gain value and attribute name
	ArrayList<String[]> linesToWriteSimple; // gain value only
	
	
	ArrayList<ArrayList<TimeSeriesFeaturePair>> greedyRecords;
	
	
	public GreedyIncreaseFilter(ArrayList<TimeSeriesFeaturePair> list) {
		super(list);
		DateFormat df = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
		this.saveFilePrefix = df.format(new Date(System.currentTimeMillis()));
		this.dataset = new XYSeriesCollection();
	}
	
	public void filterWithValidatedFeatures(HashSet<String> featureNames, int iteration) throws IOException {
		this.iteration = iteration;
		this.preFilteredFeatures = new ArrayList<TimeSeriesFeaturePair>();
		for (TimeSeriesFeaturePair f : this.sortedFeatureList) {
			if (featureNames.contains(f.getFeatureName())) {
				this.preFilteredFeatures.add(f);
			}
		}
		
		
		
		System.out.println("~~~~~~~PreFilter without correlations result: " + this.preFilteredFeatures.size() + " features are selected~~~~~~~~~");
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			System.out.println(i + "\t" + this.preFilteredFeatures.get(i).getFeatureName() + "\t" + this.preFilteredFeatures.get(i).getRecentDistance());
		}
		this.computeCorrelationIndex();
		
		this.linesToWrite = new ArrayList<String[]>();
		this.linesToWriteSimple = new ArrayList<String[]>();
		for (int i = 0; i < this.iteration; i ++) {
			this.resetMultilpleCorrelations();
			this.fastestSelect(i);
		}
		
		this.visualize("Distance Gain of Greedy Selection");
		this.saveToCSV();
		
		//this.findCommonFeaturesInTop(3);
	}
	
	
	public void filter(int iteration) throws IOException {
		this.iteration = iteration;
		
		this.preFilter();
		
		//remove jobIds and taskIds from top 30 features
		
		/*
		this.preFilteredFeatures.remove(35);
		this.preFilteredFeatures.remove(34);
		this.preFilteredFeatures.remove(33);
		this.preFilteredFeatures.remove(28);
		this.preFilteredFeatures.remove(27);
		this.preFilteredFeatures.remove(26);
		this.preFilteredFeatures.remove(14);
		this.preFilteredFeatures.remove(10);
		*/
		
		//debug, manual remove
		
		/*
		this.preFilteredFeatures.remove(20);
		this.preFilteredFeatures.remove(19);
		this.preFilteredFeatures.remove(18);
		this.preFilteredFeatures.remove(17);
		this.preFilteredFeatures.remove(16);
		this.preFilteredFeatures.remove(14);
		this.preFilteredFeatures.remove(12);
		this.preFilteredFeatures.remove(11);
		this.preFilteredFeatures.remove(10);
		this.preFilteredFeatures.remove(8);
		this.preFilteredFeatures.remove(7);
		this.preFilteredFeatures.remove(6);
		this.preFilteredFeatures.remove(3);
		*/
		
		System.out.println("~~~~~~~PreFilter without correlations result: " + this.preFilteredFeatures.size() + " features are selected~~~~~~~~~");
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			System.out.println(i + "\t" + this.preFilteredFeatures.get(i).getFeatureName() + "\t" + this.preFilteredFeatures.get(i).getRecentDistance());
		}
		this.computeCorrelationIndex();
		
		this.linesToWrite = new ArrayList<String[]>();
		this.linesToWriteSimple = new ArrayList<String[]>();
		for (int i = 0; i < this.iteration; i ++) {
			this.resetMultilpleCorrelations();
			this.fastestSelect(i);
		}
		
		this.visualize("Distance Gain of Greedy Selection");
		this.saveToCSV();
		
		/*
		this.resetMultilpleCorrelations();
		this.incrementalSelect(3);//debug
		*/
	}
	
	
	public void filterAndFindCommonFeatures(int iteration) throws IOException {
		this.iteration = iteration;
		
		this.preFilter();
		
		
		
		
		//remove jobIds and taskIds from top 30 features
		
		//10, 
		this.preFilteredFeatures.remove(35);
		this.preFilteredFeatures.remove(34);
		this.preFilteredFeatures.remove(33);
		this.preFilteredFeatures.remove(28);
		this.preFilteredFeatures.remove(27);
		this.preFilteredFeatures.remove(26);
		this.preFilteredFeatures.remove(14);
		this.preFilteredFeatures.remove(10);
		
		//test top 30
		/*
		while (this.preFilteredFeatures.size() > 29) {
			this.preFilteredFeatures.remove(this.preFilteredFeatures.size() - 1);
		}
		*/
		//debug, manual remove
		
		/*
		this.preFilteredFeatures.remove(20);
		this.preFilteredFeatures.remove(19);
		this.preFilteredFeatures.remove(18);
		this.preFilteredFeatures.remove(17);
		this.preFilteredFeatures.remove(16);
		this.preFilteredFeatures.remove(14);
		this.preFilteredFeatures.remove(12);
		this.preFilteredFeatures.remove(11);
		this.preFilteredFeatures.remove(10);
		this.preFilteredFeatures.remove(8);
		this.preFilteredFeatures.remove(7);
		this.preFilteredFeatures.remove(6);
		this.preFilteredFeatures.remove(3);
		*/
		
		System.out.println("~~~~~~~PreFilter without correlations result: " + this.preFilteredFeatures.size() + " features are selected~~~~~~~~~");
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			System.out.println(i + "\t" + this.preFilteredFeatures.get(i).getFeatureName() + "\t" + this.preFilteredFeatures.get(i).getRecentDistance());
		}
		this.computeCorrelationIndex();
		
		this.linesToWrite = new ArrayList<String[]>();
		this.linesToWriteSimple = new ArrayList<String[]>();
		
		this.greedyRecords = new ArrayList<ArrayList<TimeSeriesFeaturePair>>();
		for (int i = 0; i < this.iteration; i ++) {
			this.resetMultilpleCorrelations();
			this.fastestSelectEarlyStop(i);
		}
		//this.findCommonFeatures();
		this.findCommonFeaturesInTop(10);
		this.visualize("Distance Gain of Greedy Selection");
		this.saveToCSV();
		
		/*
		this.resetMultilpleCorrelations();
		this.incrementalSelect(3);//debug
		*/
	}
	
	public void resetMultilpleCorrelations() {
		for (TimeSeriesFeaturePair p: this.preFilteredFeatures) {
			p.setMaxMultipleCorrelation(0.0);
		}
	}
	
	
	
	public void computeCorrelationIndex() {
		this.correlationIndex = new HashMap<String, Double>();
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			TimeSeriesFeaturePair p1 = this.preFilteredFeatures.get(i);
			this.correlationIndex.put(p1.getFeatureName() + "-" + p1.getFeatureName(), 1.0);
			for (int j = i + 1; j < this.preFilteredFeatures.size(); j ++) {
				
				TimeSeriesFeaturePair p2 = this.preFilteredFeatures.get(j);
				double correlation = this.computePairCorrelation(p1, p2);
				
				this.correlationIndex.put(p1.getFeatureName() + "-" + p2.getFeatureName(), correlation);
				this.correlationIndex.put(p2.getFeatureName() + "-" + p1.getFeatureName(), correlation);
				
				//debug:
				//System.out.println("New index:" + p1.getFeatureName() + "-" + p2.getFeatureName() + "\t value=" + correlation);
				//System.out.println("New index:" + p2.getFeatureName() + "-" + p1.getFeatureName() + "\t value=" + correlation);
			}
		}
	}
	
	public void computeAbnormalCorrelationIndex() {
		this.abnormalCorrelationIndex = new HashMap<String, Double>();
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			TimeSeriesFeaturePair p1 = this.preFilteredFeatures.get(i);
			this.abnormalCorrelationIndex.put(p1.getFeatureName() + "-" + p1.getFeatureName(), 1.0);
			for (int j = i + 1; j < this.preFilteredFeatures.size(); j ++) {
				
				TimeSeriesFeaturePair p2 = this.preFilteredFeatures.get(j);
				double correlation = this.computeFeatureCorrelation(p1.getTsFeature1(), p2.getTsFeature1());
				
				this.abnormalCorrelationIndex.put(p1.getFeatureName() + "-" + p2.getFeatureName(), correlation);
				this.abnormalCorrelationIndex.put(p2.getFeatureName() + "-" + p1.getFeatureName(), correlation);
				
				//debug:
				//System.out.println("New index:" + p1.getFeatureName() + "-" + p2.getFeatureName() + "\t value=" + correlation);
				//System.out.println("New index:" + p2.getFeatureName() + "-" + p1.getFeatureName() + "\t value=" + correlation);
			}
		}
	}
	
	
	public void computeReferenceCorrelationIndex() {
		this.referenceCorrelationIndex = new HashMap<String, Double>();
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			TimeSeriesFeaturePair p1 = this.preFilteredFeatures.get(i);
			this.referenceCorrelationIndex.put(p1.getFeatureName() + "-" + p1.getFeatureName(), 1.0);
			for (int j = i + 1; j < this.preFilteredFeatures.size(); j ++) {
				
				TimeSeriesFeaturePair p2 = this.preFilteredFeatures.get(j);
				double correlation = this.computeFeatureCorrelation(p1.getTsFeature2(), p2.getTsFeature2());
				
				this.referenceCorrelationIndex.put(p1.getFeatureName() + "-" + p2.getFeatureName(), correlation);
				this.referenceCorrelationIndex.put(p2.getFeatureName() + "-" + p1.getFeatureName(), correlation);
				
				//debug:
				//System.out.println("New index:" + p1.getFeatureName() + "-" + p2.getFeatureName() + "\t value=" + correlation);
				//System.out.println("New index:" + p2.getFeatureName() + "-" + p1.getFeatureName() + "\t value=" + correlation);
			}
		}
	}
	/**
	 * 
	 * @param sequence sepcifies the first feature to select
	 * @throws IOException 
	 */
	public void fastestSelect(int sequence) throws IOException {
		//copy preFilteredFeatures
		this.preFilteredFeaturesCopy = new ArrayList<TimeSeriesFeaturePair>(this.preFilteredFeatures);
		//initialize
		this.selectedFeatures = new ArrayList<TimeSeriesFeaturePair>();
		this.selectedWithoutSingular = new ArrayList<TimeSeriesFeaturePair>();
		double gainOfSelected = 0.0;
		XYSeries series = new XYSeries("Greedy" + sequence);
		this.dataset.addSeries(series);
		
		String fileOutputFolder = this.outputFilePath + this.saveFilePrefix + "/";
		File folder = new File(fileOutputFolder);
		folder.mkdirs();
		CSVWriter writer = new CSVWriter(new FileWriter(fileOutputFolder + sequence + ".csv"), ',',CSVWriter.NO_QUOTE_CHARACTER);
		
		series.add(0,0);
		
		//print header
		System.out.println("Selected\t total gain\t new feature name\tGainByFeature\t Distance\t MultipeCorrelation\t");
		String[] line = {"Selected", "total gain", "new feature name", "GainByFeature", "Distance", "MultipeCorrelation"};
		writer.writeNext(line);
		
		//add the first feature
		TimeSeriesFeaturePair firstFeature = this.preFilteredFeaturesCopy.get(sequence);
		this.preFilteredFeaturesCopy.remove(firstFeature);
		this.selectedFeatures.add(firstFeature);
		this.selectedWithoutSingular.add(firstFeature);
		gainOfSelected += firstFeature.getRecentDistance();
		String[] firstContent = {"" + this.selectedFeatures.size(), "" + gainOfSelected, "" + firstFeature.getFeatureName(),
				"" + firstFeature.getRecentDistance(), "" + firstFeature.getRecentDistance(), "0"};
		writer.writeNext(firstContent);
		String[] firstLineContent = null;
		String[] firstLineContentSimple = null;
		if (sequence == 0) {
			firstLineContent = new String[2 * this.iteration];
			this.linesToWrite.add(firstLineContent);
			
			firstLineContentSimple = new String[this.iteration];
			this.linesToWriteSimple.add(firstLineContentSimple);
		}
		firstLineContent = this.linesToWrite.get(this.selectedFeatures.size() - 1);
		firstLineContent[2 * sequence] = "" + gainOfSelected;
		firstLineContent[2 * sequence + 1] = firstFeature.getFeatureName();
		
		firstLineContentSimple = this.linesToWriteSimple.get(this.selectedFeatures.size() - 1);
		firstLineContentSimple[sequence] = "" + gainOfSelected;
		
		System.out.println(this.selectedFeatures.size() + "\t" + gainOfSelected + "\t" + firstFeature.getFeatureName()
				+ "\t" + firstFeature.getRecentDistance()+ "\t" + firstFeature.getRecentDistance() + "\t" + 0);

		//start loop
		TimeSeriesFeaturePair toAddFeature = null;
		while (this.preFilteredFeaturesCopy.size() > 0) {
			double maxGain = -0.1;
			toAddFeature = null;
			for (TimeSeriesFeaturePair pair: this.preFilteredFeaturesCopy) {
				//double gain = this.computeIncreaseGain(pair, this.selectedWithoutSingular);
				double gain = this.computeIncreaseGainWithTreshold(pair, this.selectedWithoutSingular, 0.75);
				if (gain == -1.0) {// stop
					return;
				}
				if (gain >= maxGain) {
					maxGain = gain;
					toAddFeature = pair;
				}
			}
			
			this.preFilteredFeaturesCopy.remove(toAddFeature);
			this.selectedFeatures.add(toAddFeature);
			this.selectedWithoutSingular.add(toAddFeature);
			gainOfSelected += maxGain;
			
			series.add(this.selectedFeatures.size(), gainOfSelected);
			
			System.out.println(this.selectedFeatures.size() + "\t" + gainOfSelected + "\t" + toAddFeature.getFeatureName()
					+ "\t" + maxGain + "\t" + toAddFeature.getRecentDistance() + "\t" + (1.0 - maxGain / toAddFeature.getRecentDistance())
					);
			String[] content = {"" + this.selectedFeatures.size(), "" + gainOfSelected, "" + toAddFeature.getFeatureName(),
					"" + maxGain, "" + toAddFeature.getRecentDistance(), "" + (1.0 - maxGain / toAddFeature.getRecentDistance())};
			writer.writeNext(content);
			
			String[] lineContent = null;
			String[] lineContentSimple = null;
			if (sequence == 0) {
				lineContent = new String[2 * this.iteration];
				this.linesToWrite.add(lineContent);
				
				lineContentSimple = new String[this.iteration];
				this.linesToWriteSimple.add(lineContentSimple);
			}
			lineContent = this.linesToWrite.get(this.selectedFeatures.size() - 1);
			lineContent[2 * sequence] = "" + gainOfSelected;
			lineContent[2 * sequence + 1] = toAddFeature.getFeatureName();
			
			lineContentSimple = this.linesToWriteSimple.get(this.selectedFeatures.size() - 1);
			lineContent[sequence] = "" + gainOfSelected;
		}
		writer.flush();
		writer.close();
	}
	
	/**
	 * Stop at zero gain
	 * @param sequence
	 * @throws IOException
	 */
	public void fastestSelectEarlyStop(int sequence) throws IOException {
		//copy preFilteredFeatures
		this.preFilteredFeaturesCopy = new ArrayList<TimeSeriesFeaturePair>(this.preFilteredFeatures);
		//initialize
		this.selectedFeatures = new ArrayList<TimeSeriesFeaturePair>();
		this.selectedWithoutSingular = new ArrayList<TimeSeriesFeaturePair>();
		double gainOfSelected = 0.0;
		XYSeries series = new XYSeries("Greedy" + sequence);
		this.dataset.addSeries(series);
		
		String fileOutputFolder = this.outputFilePath + this.saveFilePrefix + "/";
		File folder = new File(fileOutputFolder);
		folder.mkdirs();
		CSVWriter writer = new CSVWriter(new FileWriter(fileOutputFolder + sequence + ".csv"), ',',CSVWriter.NO_QUOTE_CHARACTER);
		
		series.add(0,0);
		
		//print header
		System.out.println("Selected\t total gain\t new feature name\tGainByFeature\t Distance\t MultipeCorrelation\t");
		String[] line = {"Selected", "total gain", "new feature name", "GainByFeature", "Distance", "MultipeCorrelation"};
		writer.writeNext(line);
		
		//add the first feature
		TimeSeriesFeaturePair firstFeature = this.preFilteredFeaturesCopy.get(sequence);
		this.preFilteredFeaturesCopy.remove(firstFeature);
		this.selectedFeatures.add(firstFeature);
		this.selectedWithoutSingular.add(firstFeature);
		gainOfSelected += firstFeature.getRecentDistance();
		String[] firstContent = {"" + this.selectedFeatures.size(), "" + gainOfSelected, "" + firstFeature.getFeatureName(),
				"" + firstFeature.getRecentDistance(), "" + firstFeature.getRecentDistance(), "0"};
		writer.writeNext(firstContent);
		String[] firstLineContent = null;
		String[] firstLineContentSimple = null;
		if (sequence == 0) {
			firstLineContent = new String[2 * this.iteration];
			this.linesToWrite.add(firstLineContent);
			
			firstLineContentSimple = new String[this.iteration];
			this.linesToWriteSimple.add(firstLineContentSimple);
		}
		firstLineContent = this.linesToWrite.get(this.selectedFeatures.size() - 1);
		firstLineContent[2 * sequence] = "" + gainOfSelected;
		firstLineContent[2 * sequence + 1] = firstFeature.getFeatureName();
		
		firstLineContentSimple = this.linesToWriteSimple.get(this.selectedFeatures.size() - 1);
		firstLineContentSimple[sequence] = "" + gainOfSelected;
		
		System.out.println(this.selectedFeatures.size() + "\t" + gainOfSelected + "\t" + firstFeature.getFeatureName()
				+ "\t" + firstFeature.getRecentDistance()+ "\t" + firstFeature.getRecentDistance() + "\t" + 0);

		//start loop
		TimeSeriesFeaturePair toAddFeature = null;
		while (this.preFilteredFeaturesCopy.size() > 0) {
			double maxGain = -0.1;
			toAddFeature = null;
			for (TimeSeriesFeaturePair pair: this.preFilteredFeaturesCopy) {
				//double gain = this.computeIncreaseGain(pair, this.selectedWithoutSingular);
				double gain = this.computeIncreaseGainWithTreshold(pair, this.selectedWithoutSingular, 0.75);
				if (gain == -1.0) {// stop
					return;
				}
				if (gain >= maxGain) {
					maxGain = gain;
					toAddFeature = pair;
				}
			}
			
			if (maxGain > 0.0) {
				this.preFilteredFeaturesCopy.remove(toAddFeature);
				this.selectedFeatures.add(toAddFeature);
				this.selectedWithoutSingular.add(toAddFeature);
				gainOfSelected += maxGain;
				
				series.add(this.selectedFeatures.size(), gainOfSelected);
				
				System.out.println(this.selectedFeatures.size() + "\t" + gainOfSelected + "\t" + toAddFeature.getFeatureName()
						+ "\t" + maxGain + "\t" + toAddFeature.getRecentDistance() + "\t" + (1.0 - maxGain / toAddFeature.getRecentDistance())
						);
				String[] content = {"" + this.selectedFeatures.size(), "" + gainOfSelected, "" + toAddFeature.getFeatureName(),
						"" + maxGain, "" + toAddFeature.getRecentDistance(), "" + (1.0 - maxGain / toAddFeature.getRecentDistance())};
				writer.writeNext(content);
				
				String[] lineContent = null;
				String[] lineContentSimple = null;
				if (sequence == 0 || this.linesToWrite.size() < this.selectedFeatures.size()) {
					lineContent = new String[2 * this.iteration];
					this.linesToWrite.add(lineContent);
					
					lineContentSimple = new String[this.iteration];
					this.linesToWriteSimple.add(lineContentSimple);
				}
				lineContent = this.linesToWrite.get(this.selectedFeatures.size() - 1);
				lineContent[2 * sequence] = "" + gainOfSelected;
				lineContent[2 * sequence + 1] = toAddFeature.getFeatureName();
				
				lineContentSimple = this.linesToWriteSimple.get(this.selectedFeatures.size() - 1);
				lineContent[sequence] = "" + gainOfSelected;
			} else {
				this.preFilteredFeaturesCopy.clear();
			}
			
		}
		writer.flush();
		writer.close();
		
		//record the features
		ArrayList<TimeSeriesFeaturePair> selected = new ArrayList<TimeSeriesFeaturePair>(this.selectedFeatures);
		this.greedyRecords.add(selected);
	}
	
	public void findCommonFeatures() {
		HashMap<TimeSeriesFeaturePair, Integer> frequencies = new HashMap<TimeSeriesFeaturePair, Integer>();
		
		for (ArrayList<TimeSeriesFeaturePair> pairList : this.greedyRecords) {
			for (TimeSeriesFeaturePair pair : pairList) {
				if (frequencies.containsKey(pair)) {
					frequencies.put(pair, frequencies.get(pair) + 1);
				} else {
					frequencies.put(pair, 1);
				}
			}
		}
		
		
		//output
		System.out.println("FeatureName\tFrequency\tPercentage+\tDistance");
		for (TimeSeriesFeaturePair pair: frequencies.keySet()) {
			double f = frequencies.get(pair);
			double percentage = f / (double)this.greedyRecords.size();
			System.out.println(pair.getFeatureName() + "\t" + f + "\t" + percentage + "\t" + pair.getRecentDistance());
		}
		
	}
	
	public void findCommonFeaturesInTop(int k) {
		HashMap<TimeSeriesFeaturePair, Integer> frequencies = new HashMap<TimeSeriesFeaturePair, Integer>();
		
		for (ArrayList<TimeSeriesFeaturePair> pairList : this.greedyRecords) {
			for (int i = 0; i < k && i < pairList.size(); i ++) {
				TimeSeriesFeaturePair pair = pairList.get(i);
				if (frequencies.containsKey(pair)) {
					frequencies.put(pair, frequencies.get(pair) + 1);
				} else {
					frequencies.put(pair, 1);
				}
			}
		}
		
		
		//output
		System.out.println("FeatureName\tFrequency\tPercentage+\tDistance");
		for (TimeSeriesFeaturePair pair: frequencies.keySet()) {
			double f = frequencies.get(pair);
			double percentage = f / (double)this.greedyRecords.size();
			System.out.println(pair.getFeatureName() + "\t" + f + "\t" + percentage + "\t" + pair.getRecentDistance());
		}
		
	}
	public double computeIncreaseGain(TimeSeriesFeaturePair pair, ArrayList<TimeSeriesFeaturePair> selectedPairs) {
		int size = selectedPairs.size();
		if (size == 0) {
			return pair.getRecentDistance();
		}
		if (size == 1) {
			double correlation = this.correlationIndex.get(pair.getFeatureName() + "-" + selectedPairs.get(0).getFeatureName());
			
			// avoid decreasing
			correlation = Math.max(Math.abs(correlation), pair.getMaxMultipleCorrelation());
			pair.setMaxMultipleCorrelation(correlation);
			
			return (1.0 - Math.abs(correlation)) * pair.getRecentDistance();//1 - correlation!!
		}
		
		
		//prepare c
		double[][] cArray = new double[size][1];
		for (int i = 0; i < size; i ++) {
			TimeSeriesFeaturePair feature = selectedPairs.get(i);
			
			//debug
			//System.out.println("Search ke=" + feature.getFeatureName() + "-" + pair.getFeatureName());
			
			cArray[i][0] = this.correlationIndex.get(feature.getFeatureName() + "-" + pair.getFeatureName());
		}
		RealMatrix cMatrix = MatrixUtils.createRealMatrix(cArray);
		//prepare R
		double[][] rArray = new double[size][size];
		for (int i = 0; i < size; i ++) {
			for (int j = 0; j < size; j ++) {
				if (i == j) {
					rArray[i][j] = 1.0;
				} else {
					TimeSeriesFeaturePair p1 = selectedPairs.get(i);
					TimeSeriesFeaturePair p2 = selectedPairs.get(j);
					rArray[i][j] = this.correlationIndex.get(p1.getFeatureName() + "-" + p2.getFeatureName());
				}
			}
		}
		RealMatrix rMatrix = MatrixUtils.createRealMatrix(rArray);
		//compute multipe correlation
		double multipleCorrelation = 0;
		try {
			//this.printMatrix(cMatrix, "cMatrix");
			//this.printMatrix(rMatrix, "rMatrix");
			
			RealMatrix rSquare = cMatrix.transpose().multiply(MatrixUtils.inverse(rMatrix)).multiply(cMatrix);
			if (rSquare.getEntry(0, 0) < 0.0) {
				multipleCorrelation = 0.0;
			} else {
				multipleCorrelation= Math.min(1.0, Math.sqrt(rSquare.getEntry(0, 0)));
			}
			
			//avoid decreasing correlation
			multipleCorrelation = Math.max(Math.abs(multipleCorrelation), pair.getMaxMultipleCorrelation());
			pair.setMaxMultipleCorrelation(multipleCorrelation);

			
			return pair.getRecentDistance() * (1- Math.abs(multipleCorrelation));
		} catch (SingularMatrixException e) {
			this.selectedWithoutSingular.remove(this.selectedWithoutSingular.size() - 1);//remove the feature causing singular
			return this.computeIncreaseGain(pair, this.selectedWithoutSingular);
		} 
	}
	
	
	public double computeIncreaseGainWithTreshold(TimeSeriesFeaturePair pair, ArrayList<TimeSeriesFeaturePair> selectedPairs, double threshold) {
		int size = selectedPairs.size();
		if (size == 0) {
			return pair.getRecentDistance();
		}
		if (size == 1) {
			double correlation = this.correlationIndex.get(pair.getFeatureName() + "-" + selectedPairs.get(0).getFeatureName());
			
			// avoid decreasing
			correlation = Math.max(Math.abs(correlation), pair.getMaxMultipleCorrelation());
			pair.setMaxMultipleCorrelation(correlation);
			
			correlation = correlation >= threshold? correlation : 0;
			return (1.0 - Math.abs(correlation)) * pair.getRecentDistance();//1 - correlation!!
		}
		
		
		//prepare c
		double[][] cArray = new double[size][1];
		for (int i = 0; i < size; i ++) {
			TimeSeriesFeaturePair feature = selectedPairs.get(i);
			
			//debug
			//System.out.println("Search ke=" + feature.getFeatureName() + "-" + pair.getFeatureName());
			
			cArray[i][0] = this.correlationIndex.get(feature.getFeatureName() + "-" + pair.getFeatureName());
		}
		RealMatrix cMatrix = MatrixUtils.createRealMatrix(cArray);
		//prepare R
		double[][] rArray = new double[size][size];
		for (int i = 0; i < size; i ++) {
			for (int j = 0; j < size; j ++) {
				if (i == j) {
					rArray[i][j] = 1.0;
				} else {
					TimeSeriesFeaturePair p1 = selectedPairs.get(i);
					TimeSeriesFeaturePair p2 = selectedPairs.get(j);
					rArray[i][j] = this.correlationIndex.get(p1.getFeatureName() + "-" + p2.getFeatureName());
				}
			}
		}
		RealMatrix rMatrix = MatrixUtils.createRealMatrix(rArray);
		//compute multipe correlation
		double multipleCorrelation = 0;
		try {
			//this.printMatrix(cMatrix, "cMatrix");
			//this.printMatrix(rMatrix, "rMatrix");
			
			RealMatrix rSquare = cMatrix.transpose().multiply(MatrixUtils.inverse(rMatrix)).multiply(cMatrix);
			if (rSquare.getEntry(0, 0) < 0.0) {
				multipleCorrelation = 0.0;
			} else {
				multipleCorrelation= Math.min(1.0, Math.sqrt(rSquare.getEntry(0, 0)));
			}
			
			//avoid decreasing correlation
			multipleCorrelation = Math.max(Math.abs(multipleCorrelation), pair.getMaxMultipleCorrelation());
			pair.setMaxMultipleCorrelation(multipleCorrelation);

			multipleCorrelation = multipleCorrelation >= threshold? multipleCorrelation : 0;
			return pair.getRecentDistance() * (1- Math.abs(multipleCorrelation));
		} catch (SingularMatrixException e) {
			this.selectedWithoutSingular.remove(this.selectedWithoutSingular.size() - 1);//remove the feature causing singular
			return this.computeIncreaseGain(pair, this.selectedWithoutSingular);
		} 
	}
	public double computeIncreaseGainOld(TimeSeriesFeaturePair pair, ArrayList<TimeSeriesFeaturePair> selectedPairs) {
		int size = selectedPairs.size();
		
		if (size == 1) {
			if (this.correlationIndex.get(pair.getFeatureName() + "-" + selectedPairs.get(0).getFeatureName()) == null) {
				System.out.println();
			}
			
			//debug
			//System.out.println("Search key=" + selectedPairs.get(0).getFeatureName() + "-" + pair.getFeatureName());
			
			double correlation = this.correlationIndex.get(pair.getFeatureName() + "-" + selectedPairs.get(0).getFeatureName());
			
			// avoid decreasing
			correlation = Math.max(Math.abs(correlation), pair.getMaxMultipleCorrelation());
			pair.setMaxMultipleCorrelation(correlation);
			
			return (1.0 - Math.abs(correlation)) * pair.getRecentDistance();//1 - correlation!!
		}
		
		
		//prepare c
		double[][] cArray = new double[size][1];
		for (int i = 0; i < size; i ++) {
			TimeSeriesFeaturePair feature = selectedPairs.get(i);
			
			//debug
			//System.out.println("Search ke=" + feature.getFeatureName() + "-" + pair.getFeatureName());
			
			cArray[i][0] = this.correlationIndex.get(feature.getFeatureName() + "-" + pair.getFeatureName());
		}
		RealMatrix cMatrix = MatrixUtils.createRealMatrix(cArray);
		//prepare R
		double[][] rArray = new double[size][size];
		for (int i = 0; i < size; i ++) {
			for (int j = 0; j < size; j ++) {
				if (i == j) {
					rArray[i][j] = 1.0;
				} else {
					TimeSeriesFeaturePair p1 = selectedPairs.get(i);
					TimeSeriesFeaturePair p2 = selectedPairs.get(j);
					rArray[i][j] = this.correlationIndex.get(p1.getFeatureName() + "-" + p2.getFeatureName());
				}
			}
		}
		RealMatrix rMatrix = MatrixUtils.createRealMatrix(rArray);
		//compute multipe correlation
		double multipleCorrelation = 0;
		try {
			RealMatrix rSquare = cMatrix.transpose().multiply(MatrixUtils.inverse(rMatrix)).multiply(cMatrix);
			multipleCorrelation= Math.min(1.0, Math.sqrt(rSquare.getEntry(0, 0)));
			
			//avoid decreasing correlation
			multipleCorrelation = Math.max(Math.abs(multipleCorrelation), pair.getMaxMultipleCorrelation());
			pair.setMaxMultipleCorrelation(multipleCorrelation);

			
			return pair.getRecentDistance() * (1- Math.abs(multipleCorrelation));
		} catch (SingularMatrixException e) {
			return -1.0;//special value to stop
		} 
	}
	
	/**
	 * Remove zero gain features
	 * Remove features with all the same values
	 */
	public void preFilter() {
		this.preFilteredFeatures = new ArrayList<TimeSeriesFeaturePair>();
		for (TimeSeriesFeaturePair pair: this.sortedFeatureList) {
			if (pair.getRecentDistance() > 0 && !pair.allTheSameValue()) {
				this.preFilteredFeatures.add(pair);
			}
		}
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		for (TimeSeriesFeaturePair p: this.selectedFeatures) {
			System.out.println(p.getFeatureName() + "\t" + p.getTsFeature1().getValues().size() + "\t" + p.getTsFeature2().getValues().size() + "\t" + p.getRecentDistance());
		}
		
		//debug, print one attribute
		TimeSeriesFeaturePair p = this.selectedFeatures.get(0);
		System.out.println(p.getFeatureName());
		System.out.println("~~~~~~~~~~Abnormal values~~~~~~~~~~");
		//for ()
	}
	
	
	public void visualize(String chartTitle) {
		String fileName = this.saveFilePrefix + "-" + this.iteration + ".png";
		//String chartTitle = "Distance Gain of Random Selection";
		String xLabel = "Number of Features Selected";
		String yLabel = "Accumulative Distance Gain";
		
		XYLineChart chart = new XYLineChart(this.outputFilePath + fileName, this.dataset, chartTitle, xLabel, yLabel);
		chart.visualize();
	}
	
	public void saveToCSV() throws IOException {
		String fileOutputFolder = this.outputFilePath + this.saveFilePrefix + "/";
		File folder = new File(fileOutputFolder);
		folder.mkdirs();
		
		CSVWriter writer = new CSVWriter(new FileWriter(fileOutputFolder + "all.csv"), ',',CSVWriter.NO_QUOTE_CHARACTER);
		writer.writeAll(this.linesToWrite);
		writer.close();
		
		CSVWriter simpleWriter = new CSVWriter(new FileWriter(fileOutputFolder + "all-value-only.csv"), ',',CSVWriter.NO_QUOTE_CHARACTER);
		simpleWriter.writeAll(this.linesToWriteSimple);
		simpleWriter.close();
		
	}

	public XYSeriesCollection getDataset() {
		return dataset;
	}

	public void setDataset(XYSeriesCollection dataset) {
		this.dataset = dataset;
	}

	public String getSaveFilePrefix() {
		return saveFilePrefix;
	}

	public void setSaveFilePrefix(String saveFilePrefix) {
		this.saveFilePrefix = saveFilePrefix;
	}

	public void visualizeCluster(int numOfFeatures, double correlationThreshold) {
		
		System.out.println("Visualizing cluster:" + numOfFeatures + "\t features " + correlationThreshold);
		
		/*
		Graph graph = new SingleGraph(numOfFeatures + "-" + correlationThreshold);
		
		for (int i = 0; i < numOfFeatures; i ++) {
			TimeSeriesFeaturePair pi = this.preFilteredFeatures.get(i);
			graph.addNode(pi.getFeatureName());
		}
		
		
		for (int i = 0; i < numOfFeatures; i ++) {
			TimeSeriesFeaturePair pi = this.preFilteredFeatures.get(i);
			for (int j = i + 1; j < numOfFeatures; j ++) {
				TimeSeriesFeaturePair pj = this.preFilteredFeatures.get(j);
				String key = pi.getFeatureName() + "-" + pj.getFeatureName();
				if (Math.abs(this.correlationIndex.get(key)) >= correlationThreshold) {
					graph.addEdge(key, pi.getFeatureName(), pj.getFeatureName());
				}
			}
		}
		
		graph.display();
		
		*/
	}
	
	public void writePreSelectedFeaturesToCSV(String filePath) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(filePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
		
		String[] line = new String[3];
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			line[0] = "" + i;
			line[1] = this.preFilteredFeatures.get(i).getFeatureName();
			line[2] = this.preFilteredFeatures.get(i).getRecentDistance() + "";
			writer.writeNext(line);
		}
		
		writer.close();
	}
	
	public void writeCorrelationIndexToCSV(String filePath) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(filePath), ',',CSVWriter.NO_QUOTE_CHARACTER);
		String[] line = new String[2];
		for (int i = 0; i < this.preFilteredFeatures.size(); i ++) {
			TimeSeriesFeaturePair pi = this.preFilteredFeatures.get(i);
			for (int j = i + 1; j < this.preFilteredFeatures.size(); j ++) {
				TimeSeriesFeaturePair pj = this.preFilteredFeatures.get(j);
				String key = pi.getFeatureName() + "-" + pj.getFeatureName();
				line[0] = key;
				line[1] = this.correlationIndex.get(key) + "";
				writer.writeNext(line);
				
				key = pj.getFeatureName() + "-" + pi.getFeatureName();
				line[0] = key;
				line[1] = this.correlationIndex.get(key) + "";
				writer.writeNext(line);
			}
		}
		writer.close();
	}

	public ArrayList<TimeSeriesFeaturePair> getPreFilteredFeatures() {
		return preFilteredFeatures;
	}

	public void setPreFilteredFeatures(
			ArrayList<TimeSeriesFeaturePair> preFilteredFeatures) {
		this.preFilteredFeatures = preFilteredFeatures;
	}

	public ArrayList<TimeSeriesFeaturePair> getPreFilteredFeaturesCopy() {
		return preFilteredFeaturesCopy;
	}

	public void setPreFilteredFeaturesCopy(
			ArrayList<TimeSeriesFeaturePair> preFilteredFeaturesCopy) {
		this.preFilteredFeaturesCopy = preFilteredFeaturesCopy;
	}

	public HashMap<String, Double> getCorrelationIndex() {
		return correlationIndex;
	}

	public void setCorrelationIndex(HashMap<String, Double> correlationIndex) {
		this.correlationIndex = correlationIndex;
	}

	public ArrayList<TimeSeriesFeaturePair> getSelectedWithoutSingular() {
		return selectedWithoutSingular;
	}

	public void setSelectedWithoutSingular(
			ArrayList<TimeSeriesFeaturePair> selectedWithoutSingular) {
		this.selectedWithoutSingular = selectedWithoutSingular;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public ArrayList<String[]> getLinesToWrite() {
		return linesToWrite;
	}

	public void setLinesToWrite(ArrayList<String[]> linesToWrite) {
		this.linesToWrite = linesToWrite;
	}

	public ArrayList<String[]> getLinesToWriteSimple() {
		return linesToWriteSimple;
	}

	public void setLinesToWriteSimple(ArrayList<String[]> linesToWriteSimple) {
		this.linesToWriteSimple = linesToWriteSimple;
	}

	public ArrayList<ArrayList<TimeSeriesFeaturePair>> getGreedyRecords() {
		return greedyRecords;
	}

	public void setGreedyRecords(
			ArrayList<ArrayList<TimeSeriesFeaturePair>> greedyRecords) {
		this.greedyRecords = greedyRecords;
	}

	public HashMap<String, Double> getAbnormalCorrelationIndex() {
		return abnormalCorrelationIndex;
	}

	public void setAbnormalCorrelationIndex(
			HashMap<String, Double> abnormalCorrelationIndex) {
		this.abnormalCorrelationIndex = abnormalCorrelationIndex;
	}

	public HashMap<String, Double> getReferenceCorrelationIndex() {
		return referenceCorrelationIndex;
	}

	public void setReferenceCorrelationIndex(
			HashMap<String, Double> referenceCorrelationIndex) {
		this.referenceCorrelationIndex = referenceCorrelationIndex;
	}
	
	
	
}
