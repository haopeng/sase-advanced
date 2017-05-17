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

package edu.umass.cs.sase.util.csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MeasureTool {
	int fp;
	int fn;
	int tp;
	int tn;
	int total;
	public void measure(String labelFile, String predictionFile) throws IOException{
		BufferedReader labelReader = new BufferedReader(new FileReader(labelFile));
		BufferedReader predictionReader = new BufferedReader(new FileReader(predictionFile));
		this.fn = 0;
		this.fn = 0;
		this.tp = 0;
		this.tn = 0;
		this.total = 0;
		String labelLine, predictionLine;
		System.out.println("Start measuring...");
		while((labelLine = labelReader.readLine()) != null && (predictionLine = predictionReader.readLine()) != null){
			double actualValue = Double.parseDouble(labelLine.substring(0, labelLine.indexOf('|')));
			double predictValue = Double.parseDouble(predictionLine);
			if(actualValue < 0 && predictValue >= 0){

				this.fp ++;
				System.out.println("acutal=" + actualValue + ", predicted value=" + predictValue +", false positive:" + this.fp);
			}else if(actualValue >= 0 && predictValue < 0){
				this.fn ++;
				System.out.println("acutal=" + actualValue + ", predicted value=" + predictValue +", false negative:" + this.fn);
			}else if(actualValue >= 0 && predictValue >= 0){
				this.tp ++;
				System.out.println("acutal=" + actualValue + ", predicted value=" + predictValue +", true positive:" + this.tp);
			}else {
				this.tn ++;
				System.out.println("acutal=" + actualValue + ", predicted value=" + predictValue +", true negative:" + this.tn);
			}
			this.total ++;
			System.out.println("Total lines:" + this.total);
		}
		labelReader.close();
		predictionReader.close();
		this.outputMeasure();
	}
	
	public void outputMeasure(){
		double recall = (this.tp)/(this.tp + this.fn);
		double precision = (this.tn)/(this.tp + this.fp);
		double accuracy = (this.tp + this.tn) / this.total;
		System.out.println("Tp:");
		
		System.out.println("Recall:  \t" + recall);
		System.out.println("Precision:  \t" + precision);
		System.out.println("Accuracy:  \t" + accuracy);
		
	}
	
	
	public static void main(String args[]) throws IOException{
		String labelFile = "g:\\Data\\ordered-all-numeric-sliding-chopped-labeled-30percent.vw";
		String predictionFile = "g:\\Data\\presult.txt";
		
		if(args.length > 0){
			labelFile = args[0];
		}
		if(args.length > 1){
			labelFile = args[1];
		}
		
		MeasureTool m = new MeasureTool();
		m.measure(labelFile, predictionFile);
		
		
	}

}
