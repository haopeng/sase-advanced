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
import java.util.StringTokenizer;

import au.com.bytecode.opencsv.CSVReader;

public class SASEEvaluationEngine extends LogisticRegressionEvaluationEngine{
	public SASEEvaluationEngine(String GTPath, String LRPath, String correlationCSV) throws IOException {
		this.groundTruthFilePath = GTPath;
		this.readGroundTruth();
		
		this.logisticRegressionResultCSV = LRPath;
		this.readLRResult();
		
		this.correlationCSV = correlationCSV;
		this.readCorrelations();
	}
	
	
	public void readLRResult() throws IOException {
		//read 
		BufferedReader reader = new BufferedReader(new FileReader(this.logisticRegressionResultCSV));
		this.LRSelectedFeatures = new ArrayList<String>();
		
		String line;
		while ((line = reader.readLine()) != null) {
			this.totalFeatureSpaceSize ++;
			StringTokenizer st = new StringTokenizer(line);
			st.nextToken();
			String featureName = st.nextToken();
			this.LRSelectedFeatures.add(featureName);
			System.out.println(featureName + "\t");
	
		}
		reader.close();
		
		System.out.println("Total features:" + this.LRSelectedFeatures.size());
	}
	
	public static void main(String[] args) throws IOException {
		//use case 1
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3.truth";
		//String saseResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/sase/result/1.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/1.csv";
		
		//use case 2
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case2/c3.truth";
		//String saseResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/sase/result/2.txt";
 		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/2.csv";
		
		//3 use case 4
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2.truth";
		//String saseResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/sase/result/3.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/3.csv";
		
		//4 use case 7
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4.truth";
		//String saseResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/sase/result/4.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/4.csv";
		
		//5 use case 8
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case8/c4.truth";
		//String saseResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/sase/result/5.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/5.csv";
		
		//6 use case 9
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case9/a4.truth";
		//String saseResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/sase/result/6.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/6.csv";
		
		
		//8 use case 11
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case11/c5.truth";
		//String saseResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/sase/result/8.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/8.csv";
		
		
		//9 use case 12
		//String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5.truth";
		//String saseResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/sase/result/9.txt";
		//String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/9.csv";
		
		//n1 use case 12
		String groundTruth = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case10/b5.truth";
		String saseResult = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/sase/result/n1.txt";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/15.experiments/effectiveness/correlation/n1.csv";
		
		
		
		SASEEvaluationEngine engine = new SASEEvaluationEngine(groundTruth, saseResult, correlationCSV);
		engine.runClusterEngine();
		
	}
	
}
