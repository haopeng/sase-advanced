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
import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import au.com.bytecode.opencsv.CSVWriter;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.visualize.XYLineChart;

public class RandomIncreaseFilter extends GreedyIncreaseFilter{
	public RandomIncreaseFilter(ArrayList<TimeSeriesFeaturePair> list) {
		super(list);
		//this.dataset = new XYSeriesCollection();
		this.outputFilePath = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/dimishing return/random/";
	}
	
	public void filter(int iteration) throws IOException {
		this.iteration = iteration;
		this.preFilter(); //filter zero gain and all the same value features
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
			this.randomSelect(i);
		}
		
		this.visualize("Distance Gain of Random Selection");
		this.saveToCSV();
	}
	
	public void randomSelect(int sequence) throws IOException {
		this.preFilteredFeaturesCopy = new ArrayList<TimeSeriesFeaturePair>(this.preFilteredFeatures);
		//initialize
		this.selectedFeatures = new ArrayList<TimeSeriesFeaturePair>();
		this.selectedWithoutSingular = new ArrayList<TimeSeriesFeaturePair>();
		double gainOfSelected = 0.0;
		XYSeries series = new XYSeries("Random" + sequence);
		this.dataset.addSeries(series);
		
		
		String fileOutputFolder = this.outputFilePath + this.saveFilePrefix + "/";
		File folder = new File(fileOutputFolder);
		folder.mkdirs();
		
		CSVWriter writer = new CSVWriter(new FileWriter(fileOutputFolder + sequence + ".csv"), ',',CSVWriter.NO_QUOTE_CHARACTER);
		
		series.add(0,0);
		
		Random r = new Random(System.nanoTime());
		//Random r = new Random(2);//debug
		//print header
		System.out.println("Selected\t total gain\t new feature name\tGainByFeature\t Distance\t MultipeCorrelation\t");
		
		String[] line = {"Selected", "total gain", "new feature name", "GainByFeature", "Distance", "MultipeCorrelation"};
		writer.writeNext(line);
		
		while (this.preFilteredFeaturesCopy.size() > 0) {
			
			//compute multiple correlation at each round in case of bug? shit!
			for (TimeSeriesFeaturePair pair: this.preFilteredFeaturesCopy) {
				this.computeIncreaseGain(pair, this.selectedWithoutSingular);
			}
			
			
			int index = r.nextInt(this.preFilteredFeaturesCopy.size());
			TimeSeriesFeaturePair feature = this.preFilteredFeaturesCopy.get(index);
			//double gain =  this.computeIncreaseGain(feature, this.selectedWithoutSingular);
			double gain = this.computeIncreaseGainWithTreshold(feature, this.selectedWithoutSingular, 0.75);
			
			this.preFilteredFeaturesCopy.remove(feature);
			this.selectedFeatures.add(feature);
			this.selectedWithoutSingular.add(feature);
			gainOfSelected += gain;
			
			series.add(this.selectedFeatures.size(), gainOfSelected);
			
			System.out.println(this.selectedFeatures.size() + "\t" + gainOfSelected + "\t" + feature.getFeatureName()
					+ "\t" + gain + "\t" + feature.getRecentDistance() + "\t" + (1.0 - gain / feature.getRecentDistance())
					);
			String[] content = {"" + this.selectedFeatures.size(), "" + gainOfSelected, "" + feature.getFeatureName(),
					"" + gain, "" + feature.getRecentDistance(), "" + (1.0 - gain / feature.getRecentDistance())};
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
			lineContent[2 * sequence + 1] = feature.getFeatureName();
			
			lineContentSimple = this.linesToWriteSimple.get(this.selectedFeatures.size() - 1);
			lineContent[sequence] = "" + gainOfSelected;
			
		}

		writer.flush();
		writer.close();
	}
	
}
