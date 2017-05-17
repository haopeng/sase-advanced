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
import edu.umass.cs.sase.query.NFA;
import edu.umass.cs.sase.stream.GangliaHadoopStreamController;
import edu.umass.cs.sase.stream.StockStreamConfig;
import edu.umass.cs.sase.stream.StreamController;

public class HadoopUsecasePreciseUI {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws EvaluationException 
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws IOException, CloneNotSupportedException, EvaluationException {
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
		
		//debug: https://docs.google.com/spreadsheet/ccc?key=0AoXZRaSI7VTzdC11cU9iS1l2MWlsR3NSNk9jZW16MGc#gid=0
		String nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\simple\\mapbalance.nfa";
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\kleene\\reducebalance.nfa";
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\simple\\reduce.nfa";
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\kleene\\reducepull.nfa";
		String streamDataFile = "F:\\Copy\\Data\\2013\\balancehadoop.txt";
		streamDataFile = "F:\\Copy\\Data\\2013\\testmapstart.txt";
		
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\kleene\\reduceperiod";
		streamDataFile = "F:\\Copy\\Data\\2013\\balancehadoop3.txt";
		
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\1.mapper.nfa";
		
		streamDataFile = "F:\\Copy\\Data\\2013\\balancehadoop5.txt";

		
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\5.reducebalance.nfa";
		
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\6.reducebalance.nfa";
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\4.dataactivity-nopredicate.nfa";
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\1.mapper.nfa";	
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\2.reducer.nfa";
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\3.Reducepull.nfa";
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\4.dataactivity.nfa";
		

		//streamDataFile = "F:\\Copy\\Data\\2013\\balancehadoopforq4.txt";
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\4.dataactivity-nopredicate.nfa";

		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\4.dataactivity-nopredicate-jobid.nfa";
		
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\5.reducebalance.nfa";
		nfaFileLocation = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\hadoop\\final\\6.reducebalance.nfa";
		
		
		streamDataFile = "/Users/haopeng/Copy/Data/2013/balancehadoop5.txt";
		nfaFileLocation = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/hadoop/final/6.reducebalance.nfa";
		
		int repeatNumber = 1;
		if(args.length > 0){
			nfaFileLocation = args[0];
		}
		//args[1]: stream config
		if(args.length > 1){
			streamDataFile = args[1];
		}
		//args[2]: whether or not print the results
		if(args.length > 2){
			if(Integer.parseInt(args[2])== 1){
				ConfigFlags.printResults = true;
			}else{
				ConfigFlags.printResults = false;
			}
		}
		//args[3]: whether or not using postpone
		if(args.length > 3){
			if(Integer.parseInt(args[3]) == 1){
				ConfigFlags.usingPostponingOptimization = true;
			}else{
				ConfigFlags.usingPostponingOptimization = false;
			}
		}
		//args[4] on the fly
		if(args.length > 4){
			if(Integer.parseInt(args[4]) == 1){
				ConfigFlags.evaluatePredicatesOnTheFly = true;
			}else{
				ConfigFlags.evaluatePredicatesOnTheFly = false;
			}
		}
		//args[5]:whether collapsed format of the result
		if(args.length > 5){
			if(Integer.parseInt(args[5]) == 1){
				ConfigFlags.usingCollapsedFormatResult = true;
			}else{
				ConfigFlags.usingCollapsedFormatResult = false;
			}
		}
		//args[6]: how many times to repeat the experiments
		if(args.length > 6){
			repeatNumber = Integer.parseInt(args[6]);
		}
		//args[7]: whether to use zstream optimization
		if(args.length > 7){
			if(Integer.parseInt(args[7]) == 1){
				ConfigFlags.usingZstream = true;
			}else{
				ConfigFlags.usingZstream = false;
			}
		}

		GangliaHadoopStreamController myStreamController = null; 
		EngineController myEngineController = new EngineController();
		myEngineController.setNfa(nfaFileLocation);
		int ignoreNumber = 7;
		ConfigFlags.printConfigs();
		NFA nfa = new NFA(nfaFileLocation);
		System.out.println("~~~~~~~~~~~~~~NFA CONFIG:~~~~~~~~~~~~\n" + nfa +"\n~~~~~~~~~~~~~~~~~~~~~~~~~NED~~~~~~~~~~~~~~~~~");
		System.out.println("sase-opensource");
		System.out.println("NFA file:" + nfaFileLocation);
		System.out.println("Stream file:" + streamDataFile);
		
		int counter = 1;
		long currentThroughput = 0;
		long totalThroughput = 0;
		while(counter <= repeatNumber){
			myEngineController.initializeEngine();
			System.gc();
			System.out.println("\nRepeat No." + (counter) +" is started...");
			myStreamController = new GangliaHadoopStreamController(streamDataFile);
			//myStreamController.generateStockEventsAsConfigType();			
			myEngineController.setInput(myStreamController.getMyStream());
			//myStreamController.printStream();
			myEngineController.runEngine();
			System.out.println("\nProfiling results for repeat No." + (counter ) +" are as follows:");
			currentThroughput = Profiling.printProfiling();
			if(currentThroughput < 1000){
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
