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

import edu.umass.cs.sase.explanation.engine.ProfilingEngine;

public class ProfilingEngineUI {
	public static void main(String[] args) throws Exception {
		//1. Use case 1
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f-chunk.properties";//high memory||Usecase NO.1, one partition, chunk
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f.properties";//high memory||Usecase NO.1, one partition
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f.properties";//high memory||Usecase NO.1, cross
		
		
		//2. Use case 2
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case2/c3-1-cross-chunk.properties";//high memory||Usecase NO.2, cross partition, chunk
		
		//3. Use case 4
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2-1f.properties";//writing disk||Usecase NO.4, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2-1f-cross.properties";//writing disk||Usecase NO.4, cross partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2-1f-cross-chunk.properties";//writing disk||Usecase NO.4, cross partition
		
		//4. Use case 7
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//high cpu||Usecase NO.7, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f-cross.properties";//high cpu||Usecase NO.7, cross partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f-cross-chunk.properties";//high cpu||Usecase NO.7, cross partition
		
		//5. Use case 8
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//high cpu||Usecase NO.7, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case8/c4-1f-cross.properties";//high cpu||Usecase NO.8, cross partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case8/c4-1f-cross-chunk.properties";//high cpu||Usecase NO.8, cross partition
		
		//6. Use case 9
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//high cpu||Usecase NO.9, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case9/a4-1f-cross.properties";//high cpu||Usecase NO.9, cross partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case9/a4-1f-cross-chunk.properties";//high cpu||Usecase NO.9, cross partition
		
		//8. Use case 11
		///String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f.properties";//high cpu||Usecase NO.9, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case11/c5-1f-cross.properties";//Network||Usecase NO.11, cross partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case11/c5-1f-cross-chunk.properties";//Network||Usecase NO.11, cross partition

		//9. Use case 12
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5-1f.properties";//high cpu||Usecase NO.12, one partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5-1f-cross.properties";//Network||Usecase NO.11, cross partition
		//String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5-1f-cross-chunk.properties";//Network||Usecase NO.11, cross partition

		//Supply chain e7
		String inputPropertiesFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/supplychain/environment-monitoring-missing/e7/e7-cross-chunk.properties";//missing monitoring data
		
		for (int i = 0; i < 5; i ++) {
			ProfilingEngine engine = new ProfilingEngine(inputPropertiesFile);
			engine.runEngine();
			engine.printProfiling();
			engine.resetProfiling();
			System.gc();
		}

	}
}
