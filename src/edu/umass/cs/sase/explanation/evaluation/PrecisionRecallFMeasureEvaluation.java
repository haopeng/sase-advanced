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
package edu.umass.cs.sase.explanation.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;

public class PrecisionRecallFMeasureEvaluation extends EvaluationMethod{
	
	HashMap<String, Double> precisions;
	HashMap<String, Double> recalls;
	HashMap<String, Double> fmeasures;
	//referece: http://en.wikipedia.org/wiki/Information_retrieval#Precision
	
	public PrecisionRecallFMeasureEvaluation() {
		this.distanceNames = new ArrayList<String>();
		this.topKs = new HashMap<String, Integer>();
		this.precisions = new HashMap<String, Double>();
		this.recalls = new HashMap<String, Double>();
		this.fmeasures = new HashMap<String, Double>();
	}

	@Override
	public void evaluate(ArrayList<TimeSeriesFeaturePair> featurePairs, HashSet<String> groundTruth, int topK, String distanceName) {
		int retrieved = Math.min(topK, featurePairs.size());
		int relevant = groundTruth.size();
		int retrievedRelevant = 0;
		for (TimeSeriesFeaturePair pair : featurePairs) {
			if (pair.isGroundTruth()) {
				retrievedRelevant ++;
			}
		}
		
		double precision = (double)retrievedRelevant / (double)retrieved;
		double recall = (double)retrievedRelevant/ (double)relevant;
		double fmeasure = 2 * precision * recall / (precision + recall);
		
		String key = distanceName + topK;
		this.distanceNames.add(key);
		this.topKs.put(key, topK);
		this.precisions.put(key, precision);
		this.recalls.put(key, recall);
		this.fmeasures.put(key, fmeasure);
	}

	@Override
	public void printResults() {
		System.out.println("Result of precision/recall/fmeasure===");
		System.out.println("FeatureName\tTopK\tPrecision\tRecall\tF-Measure");
		for (String name : this.distanceNames) {
			System.out.println(name + "\t" + this.topKs.get(name) + "\t" + this.precisions.get(name) +  "\t" + this.recalls.get(name) + "\t" + this.fmeasures.get(name));
		}
		
	}

}
