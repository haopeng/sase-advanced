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
package edu.umass.cs.sase.interval;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.umass.cs.sase.util.csv.PredictionDriver;
import edu.umass.cs.sase.util.csv.SamplePoint;

public class IntervalPredictionDriver extends PredictionDriver{
	IntervalBasedPredictor intervalPredictor;
	public ArrayList<SamplePoint> testPoints;
	String testFolder;
	
	public IntervalPredictionDriver(String modelPath, String pFolder) throws IOException {
		super(modelPath, pFolder);
	}
	
	public IntervalPredictionDriver(String modelPath, String pFolder, String tFolder) throws IOException {
		super(modelPath, pFolder);
		this.testFolder = tFolder;
		this.testPoints = new ArrayList<SamplePoint>();
		this.readTestData();
		
	}

	
	@Override
	public void readData() throws IOException{
		super.readData();
		this.intervalPredictor = new IntervalBasedPredictor(this.modelPath);
	}
	
	public void readTestData() throws IOException {
		File folder = new File(this.testFolder);
		File[] files = folder.listFiles();
		for (File f: files) {
			SamplePoint point = new SamplePoint(f.getAbsolutePath());
			this.testPoints.add(point);
		}
		//System.out.println("Total test poitns:" + this.testPoints.size());
	}
	
	public void predictBySingleFeature(int featureIndex) {
		//build intervals
		this.intervalPredictor.buildIntervalsByAttribute(featureIndex, this.points);
		//predict using intervals
	}
	public void printSeparationScore() {
		//build intervals
		for(int i = 0; i < this.predictor.getFeatures().size(); i ++) {
			this.intervalPredictor.buildIntervalsByAttribute(i, this.points);
			System.out.println(i + "\t" + this.predictor.getFeatures().get(i).getFeatureSignature() + "\t" + this.intervalPredictor.separationScore + "\t" + this.intervalPredictor.weightedScore);
			
		}

		//predict using intervals
	}
	
	
	public void predictTestPoints(int featureIndex){
		this.intervalPredictor.buildIntervalsByAttribute(featureIndex, this.points);
		this.intervalPredictor.predictPoints(this.testPoints);
		this.intervalPredictor.removeSinglePoints();
		this.intervalPredictor.predictPointsByAdjustedIntervals(this.testPoints);
		this.intervalPredictor.printInformation();
		
	}
	
	public void predictTestPoints() {
		this.intervalPredictor.printHeader();
		for (int i = 0; i < this.predictor.getFeatures().size(); i ++) {

			this.intervalPredictor.buildIntervalsByAttribute(i, this.points);
			this.intervalPredictor.predictPoints(this.testPoints);
			this.intervalPredictor.removeSinglePoints();
			this.intervalPredictor.predictPointsByAdjustedIntervals(this.testPoints);
			this.intervalPredictor.printInformation();
		}
		
	}
	
	public void printInterval(int featureIndex){
		this.intervalPredictor.buildIntervalsByAttribute(featureIndex, this.points);
		this.intervalPredictor.removeSinglePoints();
		this.intervalPredictor.printOriginalIntervals();
		System.out.println("~~~~~~~~~~~~~~~~~");
		this.intervalPredictor.printAdjustedIntervals();
		
	}
	
	public void test(){
		this.intervalPredictor.printFeaturesByDistanceOrder();
	}
	public static void main(String[] args) throws IOException {
		String modelPath = "I:\\Copy\\Data\\2013\\singleTypeChopped\\firstPartition\\normalizedFeatures3-morevalues.csv";
		String pFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m12\\all";//training data
		//String testFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m17\\all";//test data
		String testFolder = "I:\\Copy\\Data\\2013\\slidingwindows\\m17\\all-new";//test data
		//IntervalPredictionDriver driver = new IntervalPredictionDriver(modelPath, pFolder);
		//driver.test();
		//driver.predictBySingleFeature(5);
		//output all separation scores
		//driver.printSeparationScore();
		
		IntervalPredictionDriver testDriver = new IntervalPredictionDriver(modelPath, pFolder, testFolder);
		//testDriver.printSeparationScore();
		//testDriver.predictTestPoints();//prediction
		//testDriver.printInterval(8);
		testDriver.printInterval(0);
		
		//debug purpose only
		//testDriver.predictTestPoints(17);
		
		
		
		
	}

}
