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
package edu.umass.cs.sase.explanation.simulation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class SimulationUI {
	public static ArrayList<String> parseNFALocations(String multiplePaths) {
		StringTokenizer st = new StringTokenizer(multiplePaths, "-");
		ArrayList<String> paths = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			paths.add(st.nextToken());
		}
		return paths;
	}
	public static void main(String[] args) throws IOException {
		//0: concurrent number of threads
		int maxConcurrentQueryThreads = 200;
	    if (args.length > 0) {
	    	maxConcurrentQueryThreads = Integer.parseInt(args[0]);
	    	System.out.println("concurrent threads=" + maxConcurrentQueryThreads);
	    }
		
	    //1: stream config file
		//String streamConfigFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/selectivity10.stream";
	    String streamConfigFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/6min.stream";
	    //String streamConfigFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/1min.stream";
		
		if (args.length > 1) {
			streamConfigFile = args[1];
			System.out.println("Stream config file=" + streamConfigFile);
		}
		
		//2: stream archive folder
		String streamArchiveFolder = "/Users/haopeng/Documents/DataStreamArchive/";
		
		if (args.length > 2) {
			streamArchiveFolder = args[2];
			System.out.println("Stream archive folder=" + streamArchiveFolder);
		}
		
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HHmmss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    String streamArchiveFolderForCurrentRun = streamArchiveFolder + strDate + "/";
	    File folder = new File(streamArchiveFolderForCurrentRun);
	    if (!folder.exists()) {
	    	folder.mkdirs();
	    }
	    
	    // nfa locations
	    ArrayList<String> nfaLocations = new ArrayList<String>();
	    //nfaLocations.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/7.nfa");
	    
	    /*
	    nfaLocations.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/1.nfa");
	    nfaLocations.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/2.nfa");
	    nfaLocations.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/3.nfa");
	    nfaLocations.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/4.nfa");
	    nfaLocations.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/5.nfa");
	    */
	    
	    //server
	    nfaLocations.add("/nfs/avid/users1/haopeng/sase2015/bin/simulation/1.nfa");
	    nfaLocations.add("/nfs/avid/users1/haopeng/sase2015/bin/simulation/2.nfa");
	    nfaLocations.add("/nfs/avid/users1/haopeng/sase2015/bin/simulation/3.nfa");
	    nfaLocations.add("/nfs/avid/users1/haopeng/sase2015/bin/simulation/4.nfa");
	    nfaLocations.add("/nfs/avid/users1/haopeng/sase2015/bin/simulation/5.nfa");
	    
	    /*
	    if (args.length > 3) {
	    	nfaLocations = parseNFALocations(args[3]);
	    }
	    */
	    //ArrayList<String> explanationPropertiesFiles = new ArrayList<String>();
	    //explanationPropertiesFiles.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f-chunk.properties");//high memory||Usecase NO.1, one partition, chunk
	    
	    
	    /*
	    explanationPropertiesFiles.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case2/c3-1-cross-chunk.properties");//high memory||Usecase NO.2, cross partition, chunk
	    explanationPropertiesFiles.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2-1f-cross-chunk.properties");//writing disk||Usecase NO.4, cross partition
	    explanationPropertiesFiles.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f-cross-chunk.properties");//high cpu||Usecase NO.7, cross partition
	    explanationPropertiesFiles.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case8/c4-1f-cross-chunk.properties");//high cpu||Usecase NO.8, cross partition
	    explanationPropertiesFiles.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case9/a4-1f-cross-chunk.properties");//high cpu||Usecase NO.9, cross partition
	    explanationPropertiesFiles.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case11/c5-1f-cross-chunk.properties");//Network||Usecase NO.11, cross partition
	    explanationPropertiesFiles.add("/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5-1f-cross-chunk.properties");//Network||Usecase NO.12, cross partition
	    */
	    
	    //3: explanation peroperty file
	    //String explanationPropertyFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f-chunk.properties";//no.1
	    //String explanationPropertyFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case2/c3-1-cross-chunk.properties";//no.2
	    //String explanationPropertyFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case4/b2-1f-cross-chunk.properties";//no.3
	    //String explanationPropertyFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case7/b4-1f-cross-chunk.properties";//no.4
	    //String explanationPropertyFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case8/c4-1f-cross-chunk.properties";//no.5
	    //String explanationPropertyFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case9/a4-1f-cross-chunk.properties";//no.6, fastest
	    String explanationPropertyFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case11/c5-1f-cross-chunk.properties";//no.8. as 7 in illustration
	    //String explanationPropertyFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case12/a5-1f-cross-chunk.properties";//no.9. as 8 in illustration
	    
	    if (args.length > 3) {
	    	explanationPropertyFile = args[3];
	    	System.out.println("Explanation=" + explanationPropertyFile);
	    }
	    
	    
	    
	    //5: run or not run explanation thread
	    if (args.length > 4) {
	    	if (args[4].equalsIgnoreCase("True")) {
	    		SimulationSettings.runExplanation = true;
	    		System.out.println("Explanation enabled");
	    	} else {
	    		SimulationSettings.runExplanation = false;
	    		System.out.println("Explanation disabled");
	    	}
	    }
	    
	    
	    //6: stream rate
	    int rate = 3700;
	    		
	    if (args.length > 5) {
	    	rate = Integer.parseInt(args[5]);
	    	System.out.println("StreamRate=" + rate);
	    }
	    SimulationSettings.streamRate  = rate;
	    
		//SimulationEngine engine = new SimulationEngine(streamConfigFile, streamArchiveFolderForCurrentRun, nfaLocations, maxConcurrentQueryThreads, explanationPropertiesFiles);
	    SimulationEngine engine = new SimulationEngine(streamConfigFile, streamArchiveFolderForCurrentRun, nfaLocations, maxConcurrentQueryThreads, explanationPropertyFile);
		engine.runEngine();
	}
}
