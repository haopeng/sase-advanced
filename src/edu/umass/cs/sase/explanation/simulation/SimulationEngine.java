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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.umass.cs.sase.stream.ParseStockStreamConfig;
import edu.umass.cs.sase.stream.StockStreamConfig;
import edu.umass.cs.sase.stream.StreamController;

public class SimulationEngine {
	String streamConfigFile;
	StreamController streamController;
	String archiveFolder;
		
	DataArchiveThread dataArchiveThread;
	
	ArrayList<String> nfaLocations;
	int maxQueryThread;
	ArrayList<QueryThread> queryThreads;
	
	ArrayList<String> explanationPropertiesFiles;
	String explanationPropertyFile;
	public SimulationEngine(String streamConfigFile, String archiveFolder, ArrayList<String> nfaLocations, int maxQueryThreads, String explanationFile) {
		this.streamConfigFile = streamConfigFile;
		this.archiveFolder = archiveFolder;
		this.nfaLocations = nfaLocations;
		this.maxQueryThread = maxQueryThreads;
		this.explanationPropertyFile = explanationFile;
		
		this.queryThreads = new ArrayList<QueryThread>();
		
	}
	
	
	public SimulationEngine(String streamConfigFile, String archiveFolder, ArrayList<String> nfaLocations, int maxQueryThreads, ArrayList<String> explanationFiles) {
		this.streamConfigFile = streamConfigFile;
		this.archiveFolder = archiveFolder;
		this.nfaLocations = nfaLocations;
		this.maxQueryThread = maxQueryThreads;
		this.explanationPropertiesFiles = explanationFiles;
		
		this.queryThreads = new ArrayList<QueryThread>();
	}
	
	public void runEngine() throws IOException {
		//generate  data streams
		this.prepareDataStream();
		//initialize data archive thread
		this.startDataArchiverThread();
		//initialize query threads, parse nfa
		this.startQueryEngineThreads();
		//initialize global clock thread
		this.startGlobalTimer();
		//initialize explain thread
		if (SimulationSettings.runExplanation) {
			this.startExplanationTimer();			
		}

		
	}
	
	public void startExplanationTimer() {
		Timer t = new Timer();
		t.scheduleAtFixedRate(
		    new TimerTask()
		    {
		        public void run()
		        {
		        	
		        	System.out.println("Explanation triggerred!!");
		        	
		        	ExplanationThread eThread = new ExplanationThread(explanationPropertyFile);
		        	Thread thread = new Thread(eThread);
		        	thread.start();
		        	
		        	
		        	/*
		        	Random rand = new Random(System.currentTimeMillis());
		        	int num = rand.nextInt(explanationPropertiesFiles.size());
		        	System.out.println("Pick explanation for use case:" + num);
		        	ExplanationThread eThread = new ExplanationThread(explanationPropertiesFiles.get(num));
		        	Thread thread = new Thread(eThread);
		        	thread.start();
		        	*/
		        }
		    },
		    32000,      // first time run after 30 seconds 
		    60000);     //run every 120 seconds (2 minutes)
	}
	
	public void startDataArchiverThread() {
		this.dataArchiveThread = new DataArchiveThread(archiveFolder, this.streamController.getMyStream(), this.queryThreads);
		Thread thread = new Thread(this.dataArchiveThread);
		thread.start();//start(), not run()!!!
		System.out.println("Debug:data archiver thread starts");
	}
	
	public void startQueryEngineThreads() {

		for (int i = 0; i < this.maxQueryThread; i ++ ) {
			QueryThread qThread = new QueryThread(this.nfaLocations.get(i % this.nfaLocations.size()), "Q" + i, this.streamController.getMyStream());
			this.queryThreads.add(qThread);
			Thread thread = new Thread(qThread);
			thread.start();
		}
	}
	
	public void startGlobalTimer() {
		Timer t = new Timer();
		t.scheduleAtFixedRate(
		    new TimerTask()
		    {
		        public void run()
		        {
		        	SimulationSettings.globalCount ++;
		        	if (!SimulationSettings.stopOuputCount) {
		        		System.out.println("Global clock:" + SimulationSettings.globalCount);	
		        	}
		        	
		        }
		    },
		    2000,      // first time run after 2 seconds
		    1000);     //run every 1 seconds
	}
	
	public void initialize() {
		SimulationSettings.globalCount = 0;
	}
	public void prepareDataStream() throws IOException {
		System.out.println("Debug: preparing data stream");
		ParseStockStreamConfig.parseStockEventConfig(streamConfigFile);
		StockStreamConfig.printConfig();
		
		this.streamController = new StreamController(StockStreamConfig.streamSize, "StockEvent");
		streamController.generateStockEventsAsConfigType();
		//streamController.generateSimulationStockEventsAsConfigType();
		System.out.println("Debug: data stream is generated!");
		//streamController.printStream();
		//System.out.println("Debug:" + streamController.getMyStream().getEventAtIndex(5).toString());
	}

}
