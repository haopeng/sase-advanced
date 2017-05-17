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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeatureComparator;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;

public class EvaluationManager {
	//ground truth hashset
	String inputGroundTruthFilePath;
	HashSet<String> groundTruth;
	int topK;
	ArrayList<EvaluationMethod> evaluationMethods;
	

	public EvaluationManager(String inputGroundTruthFilePath, int topK) throws IOException {
		this.inputGroundTruthFilePath = inputGroundTruthFilePath;
		this.topK = topK;
		this.parseGroundTruthFile();
		this.initializeEvaluationMethods();
	}
	
	public void evaluate(ArrayList<TimeSeriesFeaturePair> originalSorted, int topK, String distanceName) {
		System.out.println("==========Evaluate the results by " + distanceName + "============");
		//label and sort
		
		
		for (int i = 0; i < originalSorted.size(); i ++) {
			TimeSeriesFeaturePair pair = originalSorted.get(i);
			if (this.groundTruth.contains(pair.getFeatureName())) {
				pair.setGroundTruth(true);
			}
		}
		
		//sort top k by recent distance, and ground truth label
		//sorting by ground truth is to avoid the random order between features with the same distance
		Collections.sort(originalSorted, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getRecentDistance();
				double v2 = p2.getRecentDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				
				if (p1.isGroundTruth()) {
					return -1;
				}
				
				if (p2.isGroundTruth()) {
					return 1;
				}
				
				return 0;
			}
		});
		
		ArrayList<TimeSeriesFeaturePair> topKPair = new ArrayList<TimeSeriesFeaturePair>();
		for (int i = 0; i < topK && i < originalSorted.size(); i ++) {
			topKPair.add(originalSorted.get(i));
		}
		//evaluate
		for (EvaluationMethod evaluation : this.evaluationMethods) {
			evaluation.evaluate(topKPair, groundTruth, topK, distanceName);
		}
	}
	
	
	public void evaluate(TimeSeriesFeatureComparator tsComparator, int topK, String distanceName) {
		System.out.println("==========Evaluate the results by " + distanceName + "============");
		//label and sort
		ArrayList<TimeSeriesFeaturePair> fullList = new ArrayList<TimeSeriesFeaturePair>(); 
		
		if (!ExplanationSettings.aggFeatureOnly) {
			for (int i = 0; i < tsComparator.getRawFeaturePairs().size(); i ++) {
				TimeSeriesFeaturePair pair = tsComparator.getRawFeaturePairs().get(i);
				if (this.groundTruth.contains(pair.getFeatureName())) {
					pair.setGroundTruth(true);
				}
				fullList.add(pair);
			}

		}
		
		for (int i = 0; i < tsComparator.getAggFeaturePairs().size(); i ++) {
			TimeSeriesFeaturePair pair = tsComparator.getAggFeaturePairs().get(i);
			if (this.groundTruth.contains(pair.getFeatureName())) {
				pair.setGroundTruth(true);
			}
			fullList.add(pair);
		}
		
		
		//sort top k by recent distance, and ground truth label
		//sorting by ground truth is to avoid the random order between features with the same distance
		Collections.sort(fullList, new Comparator<TimeSeriesFeaturePair>(){
			public int compare(TimeSeriesFeaturePair p1, TimeSeriesFeaturePair p2) {
				double v1 = p1.getRecentDistance();
				double v2 = p2.getRecentDistance();
				double diff = v1 - v2;
				
				//reverse order
				if (diff > 0) {
					return -1;
				}
				
				if (diff < 0) {
					return 1;
				}
				
				if (p1.isGroundTruth()) {
					return -1;
				}
				
				if (p2.isGroundTruth()) {
					return 1;
				}
				
				return 0;
			}
		});
		
		
		//evaluate
		/*
		for (EvaluationMethod evaluation : this.evaluationMethods) {
			evaluation.evaluate(topKPair, groundTruth, topK, distanceName);
		}
		*/
		for (EvaluationMethod evaluation : this.evaluationMethods) {
			evaluation.evaluate(fullList, groundTruth, 1000, distanceName);
			
			/*
			for (int i = 1; i < fullList.size(); i ++) {
				ArrayList<TimeSeriesFeaturePair> topKPair = new ArrayList<TimeSeriesFeaturePair>();
				for (int j = 0; j < i && j < fullList.size(); j ++) {
					topKPair.add(fullList.get(j));
				}
				evaluation.evaluate(topKPair, groundTruth, i, distanceName);
			}
			*/
		}
	}
	
	public void printResult() {
		for (EvaluationMethod evaluation : this.evaluationMethods) {
			evaluation.printResults();
		}
	}
	
	public void initializeEvaluationMethods() {
		this.evaluationMethods = new ArrayList<EvaluationMethod>();
		
		/*
		PrecisionRecallFMeasureEvaluation prEvaluation = new PrecisionRecallFMeasureEvaluation();
		this.evaluationMethods.add(prEvaluation);
		
		AveragePrecisionEvaluation apEvaluation = new AveragePrecisionEvaluation();
		this.evaluationMethods.add(apEvaluation);
		*/
		//add other evalutions
		
		FullRecallEvaluationMethod fullRecallEvaluation = new FullRecallEvaluationMethod();
		this.evaluationMethods.add(fullRecallEvaluation);
		
		APFullRecallEvaluation apEvalution = new APFullRecallEvaluation();
		this.evaluationMethods.add(apEvalution);
	}
	
	
	public void parseGroundTruthFile() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(this.inputGroundTruthFilePath));
		this.groundTruth = new HashSet<String>();
		
		String line;
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("#")) {
				this.groundTruth.add(line);
			}

		}
		reader.close();
		
		System.out.println(this.groundTruth.size() + " features are read as ground truth");
		for (String str : groundTruth) {
			System.out.println(str);
		}
		System.out.println("~~~~~~~End of ground truth~~~~~~~~~~");
	}
	
	//sorted order to be evaluated, take care of equal scores!
	
	//first sort by distance, then sort by whether it is in the final results.
	//call this after each distance is computed, avoid repeating.
	
	//different evaluations
	
	//output
	
	public static void main(String[] args) throws IOException {
		String inputFile = "/Users/haopeng/Copy/Data/2015/groundTruth/b0-1-test.truth";
		EvaluationManager m = new EvaluationManager(inputFile, 5);
		
	}
}
