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
import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYSeries;

import au.com.bytecode.opencsv.CSVWriter;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;

public class AntiGreedyIncreaseFilter extends RandomIncreaseFilter{

	public AntiGreedyIncreaseFilter(ArrayList<TimeSeriesFeaturePair> list) {
		super(list);
		
		this.outputFilePath = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/dimishing return/anti-greedy/";
	}
	
	public void filter(int iteration) throws IOException {
		this.preFilter(); //filter zero gain and all the same value features
		this.iteration = iteration;
		
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
		
		for (int i = 0; i < this.iteration; i ++) {//random repeat
			this.resetMultilpleCorrelations();
			this.slowestSelect(i);
		}
		
		this.visualize("Distance Gain of Anti-Greedy Selection");
		this.saveToCSV();
	}
	
	/**
	 * 
	 * @param sequence: specifies the first feature to add to the selected. (it's the sequence-th from the last)
	 * @throws IOException
	 */
	public void slowestSelect(int sequence) throws IOException {
		this.preFilteredFeaturesCopy = new ArrayList<TimeSeriesFeaturePair>(this.preFilteredFeatures);
		//initialize
		this.selectedFeatures = new ArrayList<TimeSeriesFeaturePair>();
		this.selectedWithoutSingular = new ArrayList<TimeSeriesFeaturePair>();
		double gainOfSelected = 0.0;
		XYSeries series = new XYSeries("Anti-greedy" + sequence);
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
		TimeSeriesFeaturePair firstFeature = this.preFilteredFeaturesCopy.get(this.preFilteredFeaturesCopy.size() - 1 - sequence);
		this.preFilteredFeaturesCopy.remove(firstFeature);
		this.selectedFeatures.add(firstFeature);
		this.selectedWithoutSingular.add(firstFeature);
		gainOfSelected = 0.0;
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
			double minGain = 1.1;
			toAddFeature = null;
			//compute multiple correlation at each round in case of bug?
			for (TimeSeriesFeaturePair pair: this.preFilteredFeaturesCopy) {
				//double gain = this.computeIncreaseGain(pair, this.selectedWithoutSingular);
				double gain = this.computeIncreaseGainWithTreshold(pair, this.selectedWithoutSingular, 0.75);
				if (gain <= minGain) {
					minGain = gain;
					toAddFeature = pair;
				}
			}
			
			this.preFilteredFeaturesCopy.remove(toAddFeature);
			this.selectedFeatures.add(toAddFeature);
			this.selectedWithoutSingular.add(toAddFeature);
			gainOfSelected += minGain;
			
			series.add(this.selectedFeatures.size(), gainOfSelected);
			
			System.out.println(this.selectedFeatures.size() + "\t" + gainOfSelected + "\t" + toAddFeature.getFeatureName()
					+ "\t" + minGain + "\t" + toAddFeature.getRecentDistance() + "\t" + (1.0 - minGain / toAddFeature.getRecentDistance())
					);
			String[] content = {"" + this.selectedFeatures.size(), "" + gainOfSelected, "" + toAddFeature.getFeatureName(),
					"" + minGain, "" + toAddFeature.getRecentDistance(), "" + (1.0 - minGain / toAddFeature.getRecentDistance())};
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

}
