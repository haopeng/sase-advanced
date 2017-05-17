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
import java.util.ArrayList;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.evaluation.DataPoint;
import edu.umass.cs.sase.explanation.evaluation.FusionPredictionEngine;
import edu.umass.cs.sase.explanation.evaluation.MajorityVotingPredictionEngine;

public class FusionEngine extends ExplainEngineCrossValidation{

	public FusionEngine(String inputPropertiesFile) throws IOException {
		super(inputPropertiesFile);
	}
	
	public void runEngine(int fold) throws Exception {
		this.fold = fold;
		this.initialize();
		
		//read raw events
		this.readRawEvents();
		// generate features
		this.generateFeatures();	
		
		//compute distance
		this.computeDistance();
		//get count
		this.getPointCount();
		//prepare shuffle array
		this.prepareShuffle(abnormalCount, referenceCount);
		
		for (int i = 0; i < fold; i ++) {
			System.out.println("Cross validation " + i + " of " + fold + " iterations");
			//generate features
			this.generateFeatures();
			//compute distance
			this.computeDistance();
			//sample test point
			this.samplePoints(i);
			//evaluate by prediction
			this.evaluateByPrediction();
		}
		this.printResults();
	}
	
	public void evaluateByPrediction() throws Exception {
		ArrayList<DataPoint> dataPoints = this.tsFeatureComparator.generateDataPoints();//the same partition
		//ArrayList<DataPoint> dataPoints = this.generateDataPointsForPrediction();//the other partition
		ArrayList<TimeSeriesFeaturePair> featureList = this.tsFeatureComparator.getAggFeaturePairs();
		FusionPredictionEngine predictEngine = new FusionPredictionEngine(dataPoints, featureList, dataPoints);
		predictEngine.runEngine();
		
		this.precisionList.add(predictEngine.getPrecision());
		this.recallList.add(predictEngine.getRecall());
		this.fmeasureList.add(predictEngine.getfMeasure());
		
	}
	
}
