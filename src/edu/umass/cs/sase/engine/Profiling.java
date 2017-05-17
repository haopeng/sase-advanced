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
package edu.umass.cs.sase.engine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class profiles the numbers of performance
 * @author haopeng
 *
 */
public class Profiling {
	
	/**
	 * The total running time of the engine (nanoseconds)
	 */
	public static long totalRunTime = 0;
	/**
	 * The number of events processed
	 */
	public static long numberOfEvents = 0;
	/**
	 * The number of runs generated
	 */
	public static long numberOfRuns = 0;

	/**
	 * The number of matches
	 */
	public static long numberOfMatches = 0;
	public static long numberOfCollapsedMatches = 0;
	
	
	public static long numberOfEventsInMatches = 0;
	public static int maxNumberOfEventsInMatches = 0;
	
	/**
	 * The number of runs which ends before reach the final state
	 */
	public static long numberOfRunsCutted = 0;
	/**
	 * The number of runs which are deleted because of they violate the time window constraint
	 */
	public static long numberOfRunsOverTimeWindow = 0;
	/**
	 * The cost on match construction
	 */
	public static long timeOfMatchConstruction = 0;
	
	public static long negatedMatches = 0;
	
	public static long numberOfRunsReachedMatchPostponeOptimized = 0;
	
	public static long numberOfEventsInRunsReachedMatch = 0;
	public static int maxNumberOfEventsInRunsReachedMatch = 0;
	
	public static long bufferedEvents = 0;
	
	public static long enumerationTime = 0;
	
		
	public static long startupTime = 0L;
	
	public static long countOfPredicateCheck = 0;
	public static long timeOfPredicateCheck = 0;
	
	//clone cost
	public static long countOfCloneRun = 0;
	public static long timeOfCloneRun = 0;
	
	//zstream
	public static long numberOfBufferedEvents = 0;
	public static long numberOfExpiredEvents = 0;
	public static long numberOfStartRuns = 0;
	public static long numberOfCheckingEvents = 0;
	public static long timeOnBufferingEvents = 0;
	
	
	//baseline 
	public static long timeOnEvluateRunsBaseline = 0;
	public static long timeOnCleanRunsBaseline = 0;
	public static long timeOnCreateNewRunBaseline = 0;
	
	//clean runs

	public static long timeOnResetRuns = 0;
	public static long timeOnRemoveRuns = 0;
	public static long timeOnRemoveRunsFromPartition = 0;
	
	//postponing
	public static long numberOfEventsDiscarded = 0;
	public static long numberOfSafeEvents = 0;
	public static long numberOfUnsafeEvents = 0;
	
	//enumeration
	public static long timeBeforeEnumeration = 0;
	public static long timeOnEnumeration = 0;
	public static long timeAfterEnumeration = 0;
	public static long timeOnGetCombination = 0;
	public static long timeOnFillEvent = 0;
	public static long timeOnValidate = 0;
	public static long numberOfEnumerations = 0;
	public static long numberOfValidations = 0;
	
	//event buffering
	public static long timeOnBuffering = 0;
	
	//imprecise
	public static long timeForSorting = 0;//done in profiling, 
	
	public static long computeConfidenceTime = 0;// how many times the computation function is called
	public static long computeConfidenceNumbers = 0;
	public static long computeConfidenceCacheHitNumbers = 0L;
	public static long numberOfFilteredMatchesByConfidence = 0L;
	
	//dp on predicate
	public static long numberOfDiscardByDPPredicate = 0L;
	public static long numberofskipByDPPredicate = 0L;
	
	//imprecise profiling
	public static long imprecisePhase1 = 0;
	public static long imprecisePhase2 = 0;
	public static long imprecisePhase3 = 0;
	
	public static long numberOfPaths = 0;
	
	public static long estimateProbabilityFiltering = 0;
	
	/**
	 * This array is used to store the distribution of confidences
	 * for confidence in range [0,0.1),stored in confidenceDistribution[0]
	 * for confidence in range [0.1,0.2), stored in confidenceDistribution[1]
	 * ...
	 * for confidence in range [0.9,1],stored in confidenceDistribution[9]
	 */
	public static int[] confidenceDistribution = {0,0,0,0,0,0,0,0,0,0};
	
	/**
	 * resets the profiling numbers
	 */
	public static void resetProfiling(){
		
		 totalRunTime = 0;
		 numberOfEvents = 0;
		 numberOfRuns = 0;
		 numberOfMatches = 0;
		 numberOfCollapsedMatches = 0;
		 numberOfEventsInMatches = 0;
		 maxNumberOfEventsInMatches = 0;
		 numberOfRunsCutted = 0;
		 numberOfRunsOverTimeWindow = 0;
		 timeOfMatchConstruction = 0;
		 numberOfMergedRuns = 0;
		 negatedMatches = 0;
		 numberOfRunsReachedMatchPostponeOptimized = 0;
		 numberOfEventsInRunsReachedMatch = 0;
		 maxNumberOfEventsInRunsReachedMatch = 0;
		 bufferedEvents = 0;
		 
		 enumerationTime = 0;
		 
		 
		 startupTime = System.nanoTime();
		 
		 countOfPredicateCheck = 0;
		 timeOfPredicateCheck = 0;
		 
		 //zstream
		 numberOfBufferedEvents = 0;
		 numberOfExpiredEvents = 0;
		 numberOfStartRuns = 0;
		 numberOfCheckingEvents = 0;
		 timeOnBufferingEvents = 0;
		 //cloning cost
		 countOfCloneRun = 0;
		 timeOfCloneRun = 0;
		 
		 //baseline cost anaylysis
		//baseline 
		timeOnEvluateRunsBaseline = 0;
		timeOnCleanRunsBaseline = 0;
		timeOnCreateNewRunBaseline = 0;
		
		//clean runs

		timeOnResetRuns = 0;
		timeOnRemoveRuns = 0;
		timeOnRemoveRunsFromPartition = 0;
			
		//postponing
		numberOfEventsDiscarded = 0;
		numberOfSafeEvents = 0;
		numberOfUnsafeEvents = 0;

		//enumeration
		timeBeforeEnumeration = 0;
		timeOnEnumeration = 0;
		timeAfterEnumeration = 0;
		timeOnGetCombination = 0;
		timeOnFillEvent = 0;
		timeOnValidate = 0;
		numberOfEnumerations = 0;
		numberOfValidations = 0;
		
		//event buffering
		timeOnBuffering = 0;
		
		//imprecise
		timeForSorting = 0;
		computeConfidenceTime = 0;
		computeConfidenceNumbers = 0;
		computeConfidenceCacheHitNumbers = 0;
		numberOfFilteredMatchesByConfidence = 0;
		for(int i = 0; i < confidenceDistribution.length; i ++){
			confidenceDistribution[i] = 0;
		}
		
		// dp on predicate
		numberOfDiscardByDPPredicate = 0L;
		numberofskipByDPPredicate = 0L;
		
		//imprecise profiling
		imprecisePhase1 = 0;
		imprecisePhase2 = 0;
		imprecisePhase3 = 0;
		Profiling.numberOfPaths = 0;
		
		estimateProbabilityFiltering = 0;

	}
	
	/**
	 * prints the profiling numbers in console
	 */
	public static long printProfiling(){
		System.out.println();
		System.out.println("**************Profiling Numbers*****************");
		Profiling.printDateTime();
		System.out.println("Total Running Time: " + totalRunTime + " nanoseconds");
		System.out.println("Number Of Events Processed: " + numberOfEvents);
		System.out.println("Number Of Relevant Events:" + bufferedEvents);
		System.out.println("Number Of Runs Created: " + numberOfRuns);
		if(ConfigFlags.usingPostponingOptimization){
			System.out.println("Number Of Runs Reached Match: " + numberOfRunsReachedMatchPostponeOptimized);
			double events = (double)numberOfEventsInRunsReachedMatch;
			double runs = (double)numberOfRunsReachedMatchPostponeOptimized;
			System.out.println("Average Number of Events for Runs Reached Match:" + events/runs);
			System.out.println("Max Number Of Events for Runs Reached Match:" + maxNumberOfEventsInRunsReachedMatch);
		}
		if(ConfigFlags.usingCollapsedFormatResult){
			System.out.println("Number of Collapsed Matches: " + numberOfCollapsedMatches);
		}
		double e = numberOfEventsInMatches;
		double m = numberOfMatches;
		System.out.println("Average number of Events for Matches: " + e/m);
		System.out.println("Max number of Events for Matches: " + maxNumberOfEventsInMatches);
		if(numberOfEvents > 0){
			double sel = ((double)numberOfMatches)/((double)numberOfEvents);
			System.out.println("Selectivity :" + sel + " matches/event");
		}
		if(ConfigFlags.hasNegation){
			System.out.println("Number of Negated Matches: " + negatedMatches );
		}
		if(ConfigFlags.confidenceSharing){
			System.out.println("Numbers of hitting the cache for confidence computation: " + computeConfidenceCacheHitNumbers);
		}
		//cost for predicate check
		System.out.println("Count for predicate checking:" + countOfPredicateCheck);
		System.out.println("Time cost for predicate checking:" + timeOfPredicateCheck);
		double predicatePercentage = ((double)timeOfPredicateCheck / (double)totalRunTime) * 100;
		System.out.println("Time percentage for predicate checking:" + predicatePercentage+"%");
		//Zstream
		System.out.println("Number of events buffered events in ZStream:" + numberOfBufferedEvents);
		System.out.println("Number of expired events in ZStream:" + numberOfExpiredEvents);
		System.out.println("Number of start runs:" + numberOfStartRuns);
		System.out.println("Nubmer of checking events in ZStream stacks:" + numberOfCheckingEvents);
		//baseline
		System.out.println("~~~~~~~~~Break Analysis~~~~~~~~~~~");
		System.out.println("Time for sorting events in imprecise:" + timeForSorting);
		double timeOnSortingPercentage = ((double)timeForSorting/ (double)totalRunTime) * 100;
		System.out.println("Time for sorting events percentage: " + timeOnSortingPercentage);
		
		System.out.println("Time on buffering in stacks:" + timeOnBufferingEvents);
		double timeOnBufferingEventsPercentage = ((double)timeOnBufferingEvents/ (double)totalRunTime) * 100;
		System.out.println("Time on buffering in stacks percentage: " + timeOnBufferingEventsPercentage);
		
		System.out.println("Time on EvluateRuns Baseline: " + timeOnEvluateRunsBaseline);
		double evaluateRunPercentage = ((double)timeOnEvluateRunsBaseline / (double)totalRunTime) * 100;
		System.out.println("Time on EvluateRuns Baseline percentage: " + evaluateRunPercentage);
		
		System.out.println("Time on Clean Runs Baseline:" + timeOnCleanRunsBaseline);
		double timeOnCleanRunsBaselinePercentage = ((double)timeOnCleanRunsBaseline/ (double)totalRunTime) * 100;
		System.out.println("Time on Clean Runs Baseline percentage:" + timeOnCleanRunsBaselinePercentage);
		
		System.out.println("Time on Create New Run Baseline:" + timeOnCreateNewRunBaseline);
		double timeOnCreateNewRunBaselinePercentage = ((double)timeOnCreateNewRunBaseline/ (double)totalRunTime) * 100;
		System.out.println("Time on Create New Run Baseline Percentage:" + timeOnCreateNewRunBaselinePercentage );
		
		System.out.println("Time on event buffering:" + timeOnBuffering);
		double timeOnBufferingPercentage = ((double)timeOnBuffering/(double)totalRunTime) * 100;
		System.out.println("Time on Event buffering percentage:" + timeOnBufferingPercentage);
		//cost for clone run
		System.out.println("Count for clone run:" + countOfCloneRun);
		System.out.println("Clone cost for clone run:" + timeOfCloneRun);
		double cloneRunPercentage = ((double)timeOfCloneRun / (double)totalRunTime) * 100;
		System.out.println("Time percentage for clone run:" + cloneRunPercentage + "%");
		
		
		//pattern matching
		if(ConfigFlags.engineType.equalsIgnoreCase("precise")){
			System.out.println("Enumeration time: " + enumerationTime);
			double enumerationPercentage = ((double)enumerationTime)/((double)totalRunTime) * 100.0;
			System.out.println("Enumeration percentage: " + enumerationPercentage + "%" );
			long patternMatchingTime = (Profiling.timeOnEvluateRunsBaseline- enumerationTime);
			System.out.println("Pattern matching time: " + patternMatchingTime);
			double patternMatchingPercentage = ((double)patternMatchingTime) /((double)totalRunTime) * 100;
			System.out.println("Pattern matching percentage: " + patternMatchingPercentage + "%" );
		}else{
			//imprecise case
			if(ConfigFlags.usingPostponingOptimization){
				//postponing
				long patternMatchingTime = Profiling.timeOnEvluateRunsBaseline - Profiling.enumerationTime;
				long realEnumerationTime = Profiling.enumerationTime - Profiling.computeConfidenceTime;
				
				double enumerationPercentage = ((double)realEnumerationTime)/((double)totalRunTime) * 100.0;
				double patternMatchingPercentage = ((double)patternMatchingTime) /((double)totalRunTime) * 100;
				
				System.out.println("Enumeration time: " + realEnumerationTime);
				System.out.println("Enumeration percentage: " + enumerationPercentage + "%" );
				
				System.out.println("Pattern matching time: " + patternMatchingTime);
				System.out.println("Pattern matching percentage: " + patternMatchingPercentage + "%" );
			}else{
				//baseline
				long patternMatchingTime = Profiling.timeOnEvluateRunsBaseline - Profiling.computeConfidenceTime;
				double patternMatchingPercentage = ((double)patternMatchingTime) /((double)totalRunTime) * 100;
				System.out.println("Pattern matching time: " + patternMatchingTime);
				System.out.println("Pattern matching percentage: " + patternMatchingPercentage + "%" );
			}
		}
		
		System.out.println("~~~~~~~~~Break Analysis END!~~~~~~~~~~~");
		
		System.out.println("~~~~~~~~~CLEANRUNS Analysis~~~~~~~~~~~");
		System.out.println("Number of runs cleaned:" + numberOfRunsCutted);
		System.out.println("Time on Reset runs:" + timeOnResetRuns);
		double timeOnResetRunsPercentage = ((double)timeOnResetRuns / (double)totalRunTime) * 100;
		System.out.println("Time on Reset runs percentage:" + timeOnResetRunsPercentage );
		System.out.println("Time on remove runs:" + timeOnRemoveRuns);
		double timeOnRemoveRunsPercentage = ((double)timeOnRemoveRuns / (double)totalRunTime) * 100;
		System.out.println("Time on remove runs percentage:" + timeOnRemoveRunsPercentage );
		
		System.out.println("Time on remove runs from partition:" + timeOnRemoveRunsFromPartition);
		double timeOnRemoveRunsFromPartitionPercentage = ((double)timeOnRemoveRunsFromPartition/ (double)totalRunTime) * 100;
		System.out.println("Time on remove runs from partition percentage:" + timeOnRemoveRunsFromPartitionPercentage );
		System.out.println("~~~~~~~~~CLEANRUNS Analysis END!~~~~~~~~~~~");
		//cost for postpoing
		System.out.println("~~~~~~~~~Postponing Analysis~~~~~~~~~~~~~~");
		System.out.println("Postpoing Discarded Events: " + numberOfEventsDiscarded);
		System.out.println("Postpoing Safe Events: " + numberOfSafeEvents);
		System.out.println("Postpoing Unsafe Events: " + numberOfUnsafeEvents);
		System.out.println("~~~~~~~~~~Postponing Analysis END!~~~~~~~~~~~~");
		//cost for enumeration
		System.out.println("~~~~~~~~~~~~Enumeration Analysis~~~~~~~~~~~~~~");
		System.out.println("Before Enumeration time: " + timeBeforeEnumeration);
		double timeBeforeEnumerationPercentate = ((double)timeBeforeEnumeration / (double)totalRunTime) * 100;
		System.out.println("Before Enumeration percentage: " + timeBeforeEnumerationPercentate);
		System.out.println("On Enumeration time: " + timeOnEnumeration);
		double timeOnEnumerationPercentate = ((double)timeOnEnumeration / (double)totalRunTime) * 100;
		System.out.println("On Enumeration percentage: " + timeOnEnumerationPercentate);
		System.out.println("After Enumeration time: " + timeAfterEnumeration);
		double timeAfterEnumerationPercentate = ((double)timeAfterEnumeration/ (double)totalRunTime) * 100;
		System.out.println("After Enumeration percentage: " + timeAfterEnumerationPercentate);
		
		System.out.println("Get combination time: " + timeOnGetCombination);
		double timeOnGetCombinationPercentate = ((double)timeOnGetCombination/ (double)totalRunTime) * 100;
		System.out.println("Get combination percentage: " + timeOnGetCombinationPercentate);
		
		System.out.println("Fill event time: " + timeOnFillEvent );
		double timeOnFillEventPercentate = ((double)timeOnFillEvent / (double)totalRunTime) * 100;
		System.out.println("Fill event percentage: " + timeOnFillEventPercentate);
		
		System.out.println("Validate time: " + timeOnValidate );
		double timeOnValidatePercentate = ((double)timeOnValidate / (double)totalRunTime) * 100;
		System.out.println("Vlidate percentage: " + timeOnValidatePercentate);
		
		System.out.println("Count for Enumerations:" + numberOfEnumerations);
		System.out.println("Count for Validations:" + numberOfValidations);
		System.out.println("~~~~~~~~~~~~~~~Enumeration END!~~~~~~~~~~~~~~~~~");
		
		System.out.println("~~~~~~~~~~~~~~~Imprecise part~~~~~~~~~~~~~~~~");
		
		for(int i = 0; i < confidenceDistribution.length; i ++){
			double confidencePercentage = ((double)confidenceDistribution[i] / (double)computeConfidenceNumbers) * 100;
			System.out.println("Confidence distribution count in range" + i + ":" + confidenceDistribution[i]);
			System.out.println("Confidence distribution percentage in range" + i + ":" + confidencePercentage);
		}
		System.out.println("Numbers of computing confidence: " + computeConfidenceNumbers);
		System.out.println("Time for compute confidence: " + computeConfidenceTime);
		double timeOfComputeConfidencePercentage = ((double)computeConfidenceTime / (double)totalRunTime) * 100;
		System.out.println("Percentage of compute confidence:" + timeOfComputeConfidencePercentage);
		System.out.println("Number of matches filtered by confidence threshold:" + numberOfFilteredMatchesByConfidence);
		double filteredMatchesByConfidencePercentage = ((double)numberOfFilteredMatchesByConfidence / (double)computeConfidenceNumbers )* 100;
		System.out.println("Confidence threshold: " + ConfigFlags.confidenceThreshold);
		System.out.println("Percentage of matches filtered by confidence threshold:" + filteredMatchesByConfidencePercentage);
		
		//dp on predicate
		System.out.println("Number of discards by DP on predicate:" + Profiling.numberOfDiscardByDPPredicate);
		System.out.println("Number of skips by DP on predicate:" + Profiling.numberofskipByDPPredicate);
		
		System.out.println("~~~~~~~~~~~~~~~Imprecise END!~~~~~~~~~~~~~~~~~");
		System.out.println("~~~~~~~~~~~Imprecise profiling!~~~~~~~~~~~~~~");
		System.out.println("Confidence phase1 time:" + imprecisePhase1);
		double phase1Percentage = ((double)imprecisePhase1/(double)totalRunTime) * 100;
		System.out.println("Confidence phase1 time percentage:" + phase1Percentage);
		
		System.out.println("Confidence phase2 time:" + imprecisePhase2);
		double phase2Percentage = ((double)imprecisePhase2/(double)totalRunTime) * 100;
		System.out.println("Confidence phase1 time percentage:" + phase2Percentage);

		System.out.println("Confidence phase3 time:" + imprecisePhase3);
		double phase3Percentage = ((double)imprecisePhase3/(double)totalRunTime) * 100;
		System.out.println("Confidence phase3 time percentage:" + phase3Percentage);
		
		System.out.println("Number of paths:" + Profiling.numberOfPaths);
		System.out.println("Number of paths in pool:" + PathPool.getInstance().getCount());

		System.out.println("Number of filtering by estimate probability:" + estimateProbabilityFiltering);
		System.out.println("~~~~~~~~~~~Imprecise profiling end!~~~~~~~~~~~~~~~");
		
		
		System.out.println("Number Of Matches Found: " + numberOfMatches);
		
		
		
		long throughput1 = 0;
		if(totalRunTime > 0){
			throughput1 = numberOfEvents* 1000000000/totalRunTime ;
			System.out.println("Throughput: " + throughput1 + " events/second");
		}
		return throughput1;
	}

	public static long getInstantThroughput(){
		long throughput1 = 0;
		long totalStartupTime = System.nanoTime() - Profiling.startupTime;
		if(totalStartupTime > 0){
			throughput1 = numberOfEvents* 1000000000/totalStartupTime ;
			System.out.println("Throughput: " + throughput1 + " events/second");
		}
		return throughput1;
	}

	// sharing numbers
	/**
	 * number of runs merged in the sharing engine
	 */
	public static long numberOfMergedRuns = 0;
	public static void resetSharingProfiling(){
		numberOfMergedRuns = 0;
	}
	/**
	 * outputs the extra profiling numbers for the sharing engine
	 */
	public static void printSharingProfiling(){
		System.out.println("#Merged Runs = " + numberOfMergedRuns);
	}
	
	/**
	 * Check the performance periodically, if it is too slow(<100 events/second) and run for over 30 minutes, stop it
	 */
	public static void checkPerformance(){
		System.out.println("~~~~It is still running~~~~~~");
		Profiling.printProfiling();
		System.out.println("InstantThroughput is: " + Profiling.getInstantThroughput());
		long totalStartupTime = System.nanoTime() - Profiling.startupTime;
		if(Profiling.getInstantThroughput() < 100  && totalStartupTime > 30000000000L){
			
			
				System.out.println("~~~~~~~~~~~~~~It is too slow, so it is terminated~~~~~~~~~~~~~~~~~~");
				Profiling.printProfiling();
				System.exit(0);
			
		}
	}
	
	/**
	 * Print current date time
	 */
	public static void printDateTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
	}
	
	public static void addConfidence(double c){
		if(c >= 1){
			confidenceDistribution[9] ++;
		}else{
			int range =(int) (c / 0.1);
			confidenceDistribution[range] ++;
		}
	}
	
	public static void addConfidenceBruteForce(double c){
		if(c < 0.1){
			confidenceDistribution[0] ++;
		}else if(c < 0.2){
			confidenceDistribution[1] ++;
		}else if(c < 0.3){
			confidenceDistribution[2] ++;
		}else if(c < 0.4){
			confidenceDistribution[3] ++;
		}else if(c < 0.5){
			confidenceDistribution[4] ++;
		}else if(c < 0.6){
			confidenceDistribution[5] ++;
		}else if(c < 0.7){
			confidenceDistribution[6] ++;
		}else if(c < 0.8){
			confidenceDistribution[7] ++;
		}else if(c < 0.9){
			confidenceDistribution[8] ++;
		}else{
			confidenceDistribution[9] ++;
		}
	}
	
	
}
