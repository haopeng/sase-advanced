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

import edu.umass.cs.sase.explanation.evaluation.EvaluationManager;

public class DistanceFunctionComparisonEngine extends ExplainEngine{
	EvaluationManager evaluationManager;
	int topK = 30;
	public DistanceFunctionComparisonEngine(String inputPropertiesFile)
			throws IOException {
		super(inputPropertiesFile);
	}
	
	public void compare() throws Exception {
		//read raw events
		this.readRawEvents();
		//generate features
		this.generateFeatures();
		this.computeDistance();
		//initialize evaluation manager
		this.initializeEvaluationManager();
		//compute distances and evaluate
		this.computeDistances();
		//print
		this.evaluationManager.printResult();
	}
	
	
	public void compareWithValidation() {
		
	}
	
	public void initializeEvaluationManager() throws IOException {
		this.evaluationManager = new EvaluationManager(this.groundTruthFilePath, this.topK);//todo;
	}
	public void computeDistances() throws Exception {
		
		//1
		System.out.println("Processing Entropy Distance");
		this.tsFeatureComparator.computeEntropyDistance(this.topK);
		this.evaluationManager.evaluate(this.tsFeatureComparator, topK, "Entropy");
		//2
		System.out.println("Processing Euclidean Distance");
		this.tsFeatureComparator.computeEuclideanDistance(this.topK);
		this.evaluationManager.evaluate(this.tsFeatureComparator, topK, "Euclidean");
		
		//3
		System.out.println("Processing Manhanttan Distance");
		this.tsFeatureComparator.computeManhattanDistance(this.topK);
		this.evaluationManager.evaluate(this.tsFeatureComparator, topK, "Manhanttan");
		//4
		//* memory
		System.out.println("Processing DTW Distance");
		this.tsFeatureComparator.computeDTWDistance(topK);
		//this.tsFeatureComparator.computedtw
		this.evaluationManager.evaluate(this.tsFeatureComparator, topK, "DTW");
		
		//5
		System.out.println("Processing LCSS Distance");
		this.tsFeatureComparator.computeLCSSDistance(topK);
		this.evaluationManager.evaluate(this.tsFeatureComparator, topK, "LCSS");
		//6
		System.out.println("Processing EDR Distance");
		this.tsFeatureComparator.computeEDRDistance(topK);
		this.evaluationManager.evaluate(this.tsFeatureComparator, topK, "EDR");
		//7
		System.out.println("Processing ERP Distance");
		this.tsFeatureComparator.computeERPDistance(topK);
		this.evaluationManager.evaluate(this.tsFeatureComparator, topK, "ERP");
		
		
		//8
		/* null pointer
		System.out.println("Processing TQuEST Distance");
		this.tsFeatureComparator.computeTQuESTDistance(topK);
		this.evaluationManager.evaluate(this.tsFeatureComparator, topK, "TQuEST");
		*/
		//9
		/* Needs alignment
		System.out.println("Processing SpADe Distance");
		this.tsFeatureComparator.computeSpADeDistance(topK);
		this.evaluationManager.evaluate(this.tsFeatureComparator, topK, "SpADe");
		*/
		//10
		/*
		System.out.println("Processing Swale Distance");
		this.tsFeatureComparator.computeSwaleDistance(topK);
		this.evaluationManager.evaluate(this.tsFeatureComparator, topK, "Swale");
		*/
		
	}

}
