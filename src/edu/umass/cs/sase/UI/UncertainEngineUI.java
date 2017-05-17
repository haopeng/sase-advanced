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

package edu.umass.cs.sase.UI;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import net.sourceforge.jeval.EvaluationException;
import edu.umass.cs.sase.engine.ConfigFlags;
import edu.umass.cs.sase.engine.EngineController;
import edu.umass.cs.sase.engine.Profiling;
import edu.umass.cs.sase.stream.ParseStockStreamConfig;
import edu.umass.cs.sase.stream.StockStreamConfig;
import edu.umass.cs.sase.stream.StreamController;

public class UncertainEngineUI {
	
	public static void main(String args[]) throws CloneNotSupportedException, EvaluationException, IOException{
		Profiling.startupTime = System.nanoTime();
		Profiling.printDateTime();
		Timer t = new Timer();
		t.scheduleAtFixedRate(
		    new TimerTask()
		    {
		        public void run()
		        {
		        	Profiling.checkPerformance();
		        }
		    },
		    600000,      // run after 600 seconds
		    600000);     //run every 600 seconds
		String nfaFileLocation = "/Users/haopeng/Dropbox/Code/workspace/sase-opensource/queries/uncertain-interval/uncertain.nfa";
		String streamConfigFile = "/Users/haopeng/Dropbox/Code/workspace/sase-opensource/queries/uncertain-interval/uncertain4.stream";
		
		
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity\\1.nfa";
		streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity\\selectivity-uncertain.stream";
		
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-inconsistent-imprecise\\1.nfa";
		streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-inconsistent-imprecise\\selectivity10-uncertain.stream";
		
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-false-imprecise\\1.nfa";
		streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-false-imprecise\\selectivity10-uncertain.stream";
		
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-true-imprecise\\1.nfa";
		streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-true-imprecise\\selectivity10-uncertain.stream";
		
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-imprecise\\1.nfa";
		streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-imprecise\\selectivity-imprecise.stream";
		
		//imprecise, inconsistent
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\timewindow-inconsistent-imprecise\\30.nfa";
		streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\timewindow-inconsistent-imprecise\\window-imprecise.stream";
		//imprecise, false
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\timewindow-false-imprecise\\30.nfa";
		streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\timewindow-false-imprecise\\window-imprecise.stream";
		
		//test
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\test\\truefalse-imprecise.nfa";
		streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\test\\truefalse-uncertain.stream";
		
		//imprecise, true
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\timewindow-true-imprecise\\30.nfa";
		streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\timewindow-true-imprecise\\window-imprecise.stream";
		//imprecise, true-false
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\timewindow-imprecise\\200.nfa";
		streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\timewindow-imprecise\\window-imprecise.stream";
		
		//debug
		int repeatNumber = 10;
		if(args.length > 0){
			nfaFileLocation = args[0];
		}
		if(args.length > 1){
			streamConfigFile = args[1];
		}
		if(args.length > 2){
			if(Integer.parseInt(args[2])== 1){
				ConfigFlags.printResults = true;
			}else{
				ConfigFlags.printResults = false;
			}
		}
		if(args.length > 3){
			if(Integer.parseInt(args[3]) == 1){
				ConfigFlags.usingPostponingOptimization = true;
			}else{
				ConfigFlags.usingPostponingOptimization = false;
			}
		}
		if(args.length > 4){
			if(Integer.parseInt(args[4]) == 1){
				ConfigFlags.evaluatePredicatesOnTheFly = true;
			}else{
				ConfigFlags.evaluatePredicatesOnTheFly = false;
			}
		}
		if(args.length > 5){
			if(Integer.parseInt(args[5]) == 1){
				ConfigFlags.usingCollapsedFormatResult = true;
			}else{
				ConfigFlags.usingCollapsedFormatResult = false;
			}
		}
		if(args.length > 6){
			repeatNumber = Integer.parseInt(args[6]);
		}
		if(args.length > 7){
			if(Integer.parseInt(args[7]) == 1){
				ConfigFlags.confidenceIsolation = true;
			}else{
				ConfigFlags.confidenceIsolation = false;
			}
		}
		if(args.length > 8){
			if(Integer.parseInt(args[8]) == 1){
				ConfigFlags.usingZstream = true;
			}else{
				ConfigFlags.usingZstream = false;
			}
		}
		
		if(args.length > 9){
			ConfigFlags.confidenceThreshold = Double.parseDouble(args[9]);
		}
		
		int halfInterval = -1;
		if(args.length > 10){
			halfInterval = Integer.parseInt(args[10]);
		}
		if(args.length > 11){
			if(Integer.parseInt(args[11]) == 1){
				ConfigFlags.usingDynamicProgrammingForConfidence = true;
			}else{
				ConfigFlags.usingDynamicProgrammingForConfidence = false;
			}
		}
		if(args.length > 12){
			if(Integer.parseInt(args[12]) == 1){
				ConfigFlags.usingDynamicProgrammingForPredicate = true;
			}else{
				ConfigFlags.usingDynamicProgrammingForPredicate = false;
			}
		}
		
		
		
		ParseStockStreamConfig.parseStockEventConfig(streamConfigFile);
		if(halfInterval != -1){
			StockStreamConfig.uncertaintyInterval = halfInterval;
		}
		StockStreamConfig.printConfig();
		StreamController myStreamController = null; 
		EngineController myEngineController = new EngineController("imprecise");//
		System.out.println("EngineType: " + ConfigFlags.engineType + " engine is being used!");
		myEngineController.setNfa(nfaFileLocation);
		int ignoreNumber = 7;
		int counter = 1;
		long currentThroughput = 0;
		long totalThroughput = 0;
		while(counter <= repeatNumber){
			myEngineController.initializeEngine();
			System.gc();
			System.out.println("\nRepeat No." + (counter) +" is started...");
			myStreamController = new StreamController(StockStreamConfig.streamSize,"StockEvent");
			myStreamController.generateUncertainStockEventsAsConfigType();
			//myStreamController.printStream();
			myEngineController.setInput(myStreamController.getMyStream());			
			myEngineController.runEngine();
			System.out.println("\nProfiling results for repeat No." + (counter) +" are as follows:");
			currentThroughput = Profiling.printProfiling();
			//when debug ,change it
			//when run, change to 1000?
			if(currentThroughput < 300){
				repeatNumber = 0;
			}
			if(counter > ignoreNumber){
				totalThroughput += currentThroughput;
			}
			counter ++;
		}
		System.out.println("***********************");
		if(totalThroughput > 0){
			System.out.println("Average throughput is: " + totalThroughput / (repeatNumber - ignoreNumber));
		}else{
			System.out.println("Average throughput is: " + currentThroughput);
		}
		System.exit(0);
	}
}
