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
package edu.umass.cs.sase.explanation.UI;

import java.io.File;

import edu.umass.cs.sase.explanation.engine.LogisticRegressionEngine;

public class LogisticRegressionCrossValidationUI {
	public static void main(String[] args) throws Exception {
		
		//Use case 1
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f.properties";//high memory||Usecase NO.1
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f.properties";//high memory||Usecase NO.1, cross
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/1/";
		
		
		//Use case 2
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case2/c3-1f.properties";//high memory||Usecase NO.2, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case2/c3-1-cross.properties";//high memory||Usecase NO.2, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/2/";
		
		//3. Use case 4
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2-1f.properties";//high memory||Usecase NO.2, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2-1f-cross.properties";//high memory||Usecase NO.2, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/3/";
		
		//4. Use case 7
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//high cpu||Usecase NO.7, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f-cross.properties";//high cpu||Usecase NO.7, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/4/";
		
		
		//5. Use case 8
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//high cpu||Usecase NO.7, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case8/c4-1f-cross.properties";//high cpu||Usecase NO.8, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/5/";
		
		
		
		//6. Use case 9
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//high cpu||Usecase NO.9, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case9/a4-1f-cross.properties";//high cpu||Usecase NO.9, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/6/";
		
		//7. Use case 10
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//high cpu||Usecase NO.9, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case10/b5-1f-cross.properties";//Network||Usecase NO.10, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/7.csv";
		
		
		//8. Use case 11
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//high cpu||Usecase NO.9, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case11/c5-1f-cross.properties";//Network||Usecase NO.11, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/8/";
		
		//9. Use case 12
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//high cpu||Usecase NO.9, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5-1f-cross.properties";//Network||Usecase NO.11, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/9/";
		
		//Supplychain. Use case e7
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/environment-monitoring-missing/e7/e7-cross-chunk.properties";//Supply chain||Usecase e7, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/supplychain/e7/";
				
		//Supplychain. Use case e8
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/environment-monitoring-missing/e8/e8-cross-chunk.properties";//Supply chain||Usecase 8, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/supplychain/e8/";
		
		//Supplychain. Use case e9
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/environment-monitoring-missing/e9/e9-cross-chunk.properties";//Supply chain||Usecase 9, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/supplychain/e9/";
		
		//Supplychain. Use case m10
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/material-below-standard/m10/m10-cross-chunk.properties";//Supply chain||Usecase m10, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/supplychain/m10/";
		
		//Supplychain. Use case m11
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/material-below-standard/m11/m11-cross-chunk.properties";//Supply chain||Usecase m11, cross partition
		//String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/supplychain/m11/";
		
		//Supplychain. Use case m12
		String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/material-below-standard/m12/m12-cross-chunk.properties";//Supply chain||Usecase m12, cross partition
		String outputFileCSVPath = "/Users/haopeng/Dropbox/research/3rd/ComparedTechniques/logistic regression/data/supplychain/m12/";
		
		
		
		
		File outputFolder = new File(outputFileCSVPath);
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}
		LogisticRegressionEngine engine = new LogisticRegressionEngine(inputPropertiesFile, outputFileCSVPath);
		int fold = 5;
		engine.runEngine(fold);
	}
}
