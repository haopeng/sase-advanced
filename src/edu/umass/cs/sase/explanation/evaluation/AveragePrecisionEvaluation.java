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

public class AveragePrecisionEvaluation extends EvaluationMethod{
	HashMap<String, Double> averagePrecisions;

	public AveragePrecisionEvaluation() {
		this.distanceNames = new ArrayList<String>();
		this.topKs = new HashMap<String, Integer>();
		this.averagePrecisions = new HashMap<String, Double>();
	}
	
	@Override
	public void evaluate(ArrayList<TimeSeriesFeaturePair> featurePairs,
			HashSet<String> groundTruth, int topK, String distanceName) {
		int relevantRetrieved = 0;
		double precisionSum = 0.0;
		for (int i = 0; i < featurePairs.size(); i ++) {
			if (featurePairs.get(i).isGroundTruth()) {
				relevantRetrieved ++;
				double precision = (double)relevantRetrieved / (double)(i + 1);
				precisionSum += precision;
			} 
		}
		
		double averagePrecision = relevantRetrieved == 0 ? 0 : precisionSum / (double) relevantRetrieved;
		
		String key = distanceName + topK;
		this.distanceNames.add(key);
		this.topKs.put(key, topK);
		this.averagePrecisions.put(key, averagePrecision);
		
	}

	@Override
	public void printResults() {
		System.out.println("Result of average precision ===");
		System.out.println("FeatureName\tTopK\tAveragerPrecision");
		for (String name : this.distanceNames) {
			System.out.println(name + "\t" + this.topKs.get(name) + "\t" + this.averagePrecisions.get(name));
		}
		
	}

}
