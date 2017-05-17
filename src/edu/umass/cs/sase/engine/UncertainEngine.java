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

import java.text.DecimalFormat;
import java.util.*;

import edu.umass.cs.sase.query.Edge;
import edu.umass.cs.sase.query.KleeneClosurePredicateType;
import edu.umass.cs.sase.query.State;
import edu.umass.cs.sase.stream.*;

import net.sourceforge.jeval.EvaluationException;

public class UncertainEngine extends Engine {

	UncertainRunPool uncertainEngineRunController;
	UncertainRun tempRun;
	
	HashMap<String, ArrayList<Path>> confidenceCache;
	
	
	public UncertainEngine(){
		super();
		this.uncertainEngineRunController = new UncertainRunPool();
		if(ConfigFlags.confidenceSharing){
			this.confidenceCache = new HashMap<String, ArrayList<Path>>();
		}
		Profiling.resetProfiling();
		System.out.println("From uncertain engine: UncertainEngine()");
	}
	
	public void initialize(){
		System.out.println("From uncertain engine: initialize()");
		super.initialize();
		this.uncertainEngineRunController = new UncertainRunPool();
		Profiling.resetProfiling();
		PathPool.getInstance().reset();
	}
	
	
	public void runEngine() throws CloneNotSupportedException, EvaluationException{
		System.out.println("From uncertain engine: runEngine()");
		ConfigFlags.timeWindow = this.nfa.getTimeWindow();
		ConfigFlags.sequenceLength = this.nfa.getSize();
		ConfigFlags.selectionStrategy = this.nfa.getSelectionStrategy();
		ConfigFlags.hasPartitionAttribute = this.nfa.isHasPartitionAttribute();
		ConfigFlags.hasNegation = this.nfa.isHasNegation();
		
		if(ConfigFlags.selectionStrategy.equalsIgnoreCase("skip-till-any-match")){
			if(this.nfa.isHasKleeneClosure() && ConfigFlags.usingPostponingOptimization){
				this.runSkipTillAnyEnginePostponeOptimized();
			}else{
				System.out.println("no postpone");
				this.runSkipTillAnyEngine();
			}
		}
		Profiling.bufferedEvents = buffer.getSize();
	}
	/**
	 * Postpone engine
	 */
	public void runSkipTillAnyEnginePostponeOptimized() throws CloneNotSupportedException, EvaluationException{
		if(!ConfigFlags.hasPartitionAttribute){
		}
		if(ConfigFlags.hasPartitionAttribute){
			ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
			this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
			Event e = null;
			long currentTime = 0;
			this.eventStackGroup = new EventStackGroup(this.nfa);
			currentTime = System.nanoTime();
			while((e = this.input.popEvent())!= null){
				Profiling.numberOfEvents ++;
				this.eventStackGroup.checkEvent(e);
			}
			Profiling.timeForSorting += (System.nanoTime() - currentTime);
			this.processEventsFromStackGroupForPostpone();
			Profiling.totalRunTime += (System.nanoTime() - currentTime);
		}
	}
	
	
	/**
	 * Baseline engine
	 */
	public void runSkipTillAnyEngine() throws CloneNotSupportedException, EvaluationException{
		if(!ConfigFlags.hasPartitionAttribute){
		}

		if(ConfigFlags.hasPartitionAttribute){
			ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
			this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
			Event e = null;
			long currentTime = 0;
			this.eventStackGroup = new EventStackGroup(this.nfa);
			currentTime = System.nanoTime();
			while((e = this.input.popEvent())!= null){
				Profiling.numberOfEvents ++;
				this.eventStackGroup.checkEvent(e);
			}
			Profiling.timeForSorting += (System.nanoTime() - currentTime);
		
			this.processEventsFromStackGroupForBaseline();
			
			Profiling.totalRunTime += (System.nanoTime() - currentTime);
		}
	}
	
	/**
	 * Core processing method for baseline algorithm.
	 * @throws EvaluationException
	 * @throws CloneNotSupportedException
	 */
	public void processEventsFromStackGroupForBaseline() throws EvaluationException, CloneNotSupportedException{
		SingleEventStack singleStack = null;
		Event singleEvent = null;
		String edgeTag = null;
		for(int i = 0; i < this.eventStackGroup.getNumberOfStacks(); i ++){
			singleStack = this.eventStackGroup.getStackAtPosition(i);
			edgeTag = singleStack.getEdgeTag();
			while(singleStack.hasMoreEvents()){
				singleEvent = singleStack.popEvent();
				if(i == 0){
					long time1 = System.nanoTime();
					this.createNewRunByPartitionFromStack(singleEvent, edgeTag);
					Profiling.timeOnCreateNewRunBaseline += (System.nanoTime() - time1);
				}else{
					long time2 = System.nanoTime();
					this.evaluateRunsByPartitionForSkipTillAnyFromStackCleanIncluded(singleEvent, edgeTag);
					Profiling.timeOnEvluateRunsBaseline += (System.nanoTime() - time2);
					if(this.toDeleteRuns.size() > 0){
						long time3 = System.nanoTime();
						this.cleanRunsByPartition();
						Profiling.timeOnCleanRunsBaseline += (System.nanoTime() - time3);
					}
				}
			}
		}
	}
	/**
	 * Core processing method for postpone algorithm.
	 * @throws EvaluationException
	 * @throws CloneNotSupportedException
	 */
	public void processEventsFromStackGroupForPostpone() throws EvaluationException, CloneNotSupportedException{
		SingleEventStack singleStack = null;
		Event singleEvent = null;
		String edgeTag = null;
		for(int i = 0; i < this.eventStackGroup.getNumberOfStacks(); i ++){
			singleStack = this.eventStackGroup.getStackAtPosition(i);
			edgeTag = singleStack.getEdgeTag();
			while(singleStack.hasMoreEvents()){
				singleEvent = singleStack.popEvent();
				if(i == 0){
					long time1 = System.nanoTime();
					this.createNewRunByPartitionFromStackPostponeOptimized(singleEvent, edgeTag);
					Profiling.timeOnCreateNewRunBaseline += (System.nanoTime() - time1);
				}else{
					long time2 = System.nanoTime();
					this.evaluateRunsByPartitionForSkipTillAnyPostponeOptimizedFromStack(singleEvent, edgeTag);
					Profiling.timeOnEvluateRunsBaseline += (System.nanoTime() - time2);
					if(this.toDeleteRuns.size() > 0){
						long time3 = System.nanoTime();
						this.cleanRunsByPartition();
						Profiling.timeOnCleanRunsBaseline += (System.nanoTime() - time3);
					}
				}
			}
		}
	}

	public void createNewRun(Event e) throws EvaluationException{
		if(this.nfa.getStates()[0].canStartWithEvent(e)){
			this.buffer.bufferEvent(e);
			Run newRun = this.uncertainEngineRunController.getRun();
			newRun.initializeRun(this.nfa);
			newRun.addEvent(e);
			//this.numberOfRuns.update(1);
			Profiling.numberOfRuns ++;
			this.activeRuns.add(newRun);
		}
	}	
	public void createNewRunByPartition(Event e) throws EvaluationException, CloneNotSupportedException{
		if(this.nfa.getStates()[0].canStartWithEvent(e)){
			this.buffer.bufferEvent(e);
			Run newRun = this.uncertainEngineRunController.getRun();
			newRun.initializeRun(this.nfa);
			newRun.addEvent(e); 
			Profiling.numberOfRuns ++;
			Profiling.numberOfStartRuns ++;
			this.activeRuns.add(newRun);
			this.addRunByPartition(newRun);
		}
	}
	public void createNewRunByPartitionPostponeOptimized(Event e) throws EvaluationException, CloneNotSupportedException{
		if(this.nfa.getStates()[0].canStartWithEvent(e)){
			this.buffer.bufferEvent(e);
			Run newRun = this.uncertainEngineRunController.getRun();
			newRun.initializeRun(this.nfa);
			newRun.addEventAndStatus(e, true);
			Profiling.numberOfRuns ++;
			Profiling.numberOfStartRuns ++;
			this.activeRuns.add(newRun);
			this.addRunByPartition(newRun);
		}
	}
	
	
	public void createNewRunByPartitionFromStack(Event e, String edgeTag) throws EvaluationException, CloneNotSupportedException{
		Edge edge = this.nfa.getEdgeByTad(edgeTag);
		boolean canStart = false;
		if(edge.getEdgeTag().equalsIgnoreCase(edgeTag) && edge.isSingle()){
			canStart = true;
		}else if(this.nfa.getStates()[0].canStartWithEvent(e)){
			canStart = true;
		}
		if(canStart){
			this.buffer.bufferEvent(e);
			Run newRun = this.uncertainEngineRunController.getRun();
			newRun.initializeRun(this.nfa);
			newRun.addEvent(e); 
			this.activeRuns.add(newRun);
			this.addRunByPartition(newRun);
			Profiling.numberOfStartRuns ++;
			Profiling.numberOfRuns ++;
		}
	}
	public void createNewRunByPartitionFromStackPostponeOptimized(Event e, String edgeTag) throws EvaluationException, CloneNotSupportedException{
		Edge edge = this.nfa.getEdgeByTad(edgeTag);
		boolean canStart = false;
		if(edge.getEdgeTag().equalsIgnoreCase(edgeTag) && edge.isSingle()){
			canStart = true;
		}else if(this.nfa.getStates()[0].canStartWithEvent(e)){
			canStart = true;
		}
		if(canStart){
			this.buffer.bufferEvent(e);
			Run newRun = this.uncertainEngineRunController.getRun();
			newRun.initializeRun(this.nfa);
			newRun.addEventAndStatus(e, true);
			this.activeRuns.add(newRun);
			this.addRunByPartition(newRun);
			Profiling.numberOfStartRuns ++;
			Profiling.numberOfRuns ++;
		}
	}
	
	public void evaluateRunsByPartitionForSkipTillAnyFromStackCleanIncluded(Event e, String edgeTag) throws CloneNotSupportedException{
		int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
		if(this.activeRunsByPartition.containsKey(key)){
			ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
			int count = 0;
			this.runIteratorCounter = 0;
			int size = partitionedRuns.size();
			while(count < size){
				Run r = partitionedRuns.get(this.runIteratorCounter);
				if(!r.isFull()){
					this.evaluateEventOptimizedForSkipTillAnyFromStackCleanIncluded(e, r, edgeTag, partitionedRuns);//
				}
				count ++;
			}
		}
	}
	/**
	 * One event vs. multiple runs
	 * @param e
	 * @throws CloneNotSupportedException
	 * @throws EvaluationException 
	 */
	public void evaluateRunsByPartitionForSkipTillAnyPostponeOptimizedFromStack(Event e,String edgeTag) throws CloneNotSupportedException, EvaluationException{
		int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
		if(this.activeRunsByPartition.containsKey(key)){
			ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
			int size = partitionedRuns.size();
			for(int i = 0; i < size; i ++){
				Run r = partitionedRuns.get(i);
				if(!r.isFull()){
					this.evaluateEventPostponeOptimizedFromStackForSkipTillAny(e, r, edgeTag);
				}
			}
		}
	}

	public void evaluateEventOptimizedForSkipTillAnyFromStackCleanIncluded(Event e, Run r, String edgeTag, ArrayList<Run> partitionedRuns) throws CloneNotSupportedException{
		boolean timewindowResult = this.checkTimeWindow(e, r);
		if(timewindowResult){
			this.runIteratorCounter ++;
			int checkResult = this.checkPredicateFromStack(e, r, edgeTag);
			Run newRun = null;
			switch(checkResult){
				case 1:
					newRun = this.cloneRun(r);
					this.addEventToRunCleanIncluded(newRun, e);
					break;
				case 2:
					newRun = this.cloneRun(r);
					newRun.proceed();
					this.addEventToRunCleanIncluded(newRun, e);
					break;
			}
		}else{
			if(this.nfa.getLastEdgeTag().equalsIgnoreCase(edgeTag) && r.checkTimeWindowLarge(e)){
				// only the last component to stop a run
				partitionedRuns.remove(this.runIteratorCounter);
			}else{
				this.runIteratorCounter ++;
			}
		}
		
	}
	
	public int checkPredicateFromStack(Event e, Run r, String edgeTag){//0 for false, 1 for take or begin, 2 for proceed
		int currentState = r.getCurrentState();
		State s = this.nfa.getStates(currentState);
		if(!s.getEventType().equalsIgnoreCase(e.getEventType())){// event type check;
			return 0;
		}
		Edge edge;
		if(!s.isKleeneClosure()){
			edge = s.getEdges(0);
		}else{
			if(r.isKleeneClosureInitialized()){
				boolean result;
				result = this.checkProceedFromStack(e, r, edgeTag);//proceedEdge.evaluatePredicate(e, r, buffer);
				if(result){
					return 2;
				}else{
					edge = s.getEdges(1);
				}
			}else{
				edge = s.getEdges(0);
			}
		}
		boolean result;
		if(edge.getEdgeTag().equalsIgnoreCase(edgeTag) && edge.isSingle()){
			result = true;
		}else{
			result = edge.evaluatePredicate(e, r, buffer);
		}
		if(result){
			return 1;
		}
		
		return 0;
	}

	public boolean checkProceedFromStack(Event e, Run r, String edgeTag){
		int currentState = r.getCurrentState();
		State s = this.nfa.getStates(currentState + 1);//+1
		r.setCurrentState(currentState + 1);
		
		if(!s.getEventType().equalsIgnoreCase(e.getEventType())){// event type check;
			return false;
		}
		Edge beginEdge = s.getEdges(0);
		boolean result;
		if(beginEdge.getEdgeTag().equalsIgnoreCase(edgeTag) && beginEdge.isSingle()){
			result = true;
		}else{
			result = beginEdge.evaluatePredicate(e, r, buffer);
		}
		r.setCurrentState(currentState);
		return result;
	}

	
	public void addEventToRunCleanIncluded(Run newRun, Event e){
		this.buffer.bufferEvent(e);// add the event to buffer
		int oldState = 0;
		int newState = 0;
		oldState = newRun.getCurrentState();
		newRun.addEvent(e);
		newState = newRun.getCurrentState();
		boolean toAddNewRun = true;
		if(oldState == newState){//kleene closure
			if(newRun.isFull){
				//check match and output match
				if(newRun.checkMatch()){
					toAddNewRun = false;
					this.outputMatch(new Match(newRun, this.nfa, this.buffer));
				}
			}
		}
		if(toAddNewRun){
			this.addRunByPartition(newRun);
		}
	}
	
	public Run cloneRun(Run r) throws CloneNotSupportedException{
		Profiling.countOfCloneRun ++;
		long startTime = System.nanoTime();
		
		Run newRun = this.uncertainEngineRunController.getRun();
		newRun = (Run)r.clone();
		Profiling.numberOfRuns ++;
		Profiling.timeOfCloneRun += (System.nanoTime() - startTime);
		return newRun;
	}

	public boolean checkTimeWindow(Event e, Run r){
		return r.checkTimeWindow(e);
	}
	
	/**
	 * compute confidence and output match
	 */
	public void outputMatch(Match m){
		//compute the confidence
		double c = 1;
		if(!ConfigFlags.usingCollapsedFormatResult){
			
			if(ConfigFlags.confidenceIsolation){
				c = this.computeConfidenceWithIsolationOptimization(m);
			}else{
				if(ConfigFlags.confidenceSharing){
					c = this.computeConfidenceWithSharingCache(m);
				}else{
					c = this.computeConfidence(m);
				}
			}
		}
		//threshold filter
		if(c > ConfigFlags.confidenceThreshold){
			Profiling.numberOfMatches ++;
			Profiling.numberOfEventsInMatches += m.getEvents().length;
			if(m.getEvents().length > Profiling.maxNumberOfEventsInMatches){
				Profiling.maxNumberOfEventsInMatches = m.getEvents().length;
			}
			if(ConfigFlags.printResults){
				System.out.println("----------Here is the No." + Profiling.numberOfMatches +" match----------");
				System.out.println(m);
				DecimalFormat df = new DecimalFormat("#.###################");
				System.out.println("\n The confidence is: " + df.format(c));
				//print the lower bounds
				ArrayList<Long> lowerBounds = m.getRun().getLowerBounds();
				for(int i = 0; i < lowerBounds.size(); i ++){
					System.out.print(lowerBounds.get(i) + "\t");
				}
				System.out.println();
				//print the upper bounds
				//debug
				ArrayList<Long> upperBounds = m.getRun().getUpperBounds();
				for(int i = 0; i < upperBounds.size(); i ++){
					System.out.print(upperBounds.get(i) + "\t");
				}
				System.out.println();
			}
		}
	}
	
	
	public void outputMatch(Match m, double confidence){
		Profiling.numberOfMatches ++;
		Profiling.numberOfEventsInMatches += m.getEvents().length;
		if(m.getEvents().length > Profiling.maxNumberOfEventsInMatches){
			Profiling.maxNumberOfEventsInMatches = m.getEvents().length;
		}
		if(ConfigFlags.printResults){
			System.out.println("----------Here is the No." + Profiling.numberOfMatches +" match----------");
			System.out.println(m);
			DecimalFormat df = new DecimalFormat("#.###################");
			System.out.println("\n The confidence is: " + df.format(confidence));
			//print the lower bounds
			ArrayList<Long> lowerBounds = m.getRun().getLowerBounds();
			for(int i = 0; i < lowerBounds.size(); i ++){
				System.out.print(lowerBounds.get(i) + "\t");
			}
			System.out.println();
			//print the upper bounds
			//debug
			ArrayList<Long> upperBounds = m.getRun().getUpperBounds();
			for(int i = 0; i < upperBounds.size(); i ++){
				System.out.print(upperBounds.get(i) + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * idea:
	 * @param m
	 * @return
	 */
	
	public double computeConfidenceWithIsolationOptimization(Match m){
		double factor = 1.0;
		
		Run r = m.getRun();
		
		ArrayList<Integer> eventIds = r.getEventIds();
		ArrayList<Long> validLowerBounds = r.getLowerBounds();
		ArrayList<Long> validUpperBounds = r.getUpperBounds();
		
		long currentLower, currentUpper, previousUpper, nextLower;
		int counter = 0;
		boolean previousResult, nextResult;
		while(counter < eventIds.size()){
			// compare with previous event
			if(counter != 0){
				previousUpper = validUpperBounds.get(counter - 1);
				currentLower = validLowerBounds.get(counter);
				if(previousUpper < currentLower){
					previousResult = true;
				}else{
					previousResult = false;
				}
			}else{
				previousResult = true;
			}
			if(previousResult){
				// compare with next event
				if(counter != eventIds.size() - 1){
					currentUpper = validUpperBounds.get(counter);
					nextLower = validLowerBounds.get(counter + 1);
					if(currentUpper < nextLower){
						nextResult = true;
					}else{
						nextResult = false;
					}
				}else{
					nextResult = true;
				}
			}else{
				nextResult = false;
			}
			if(nextResult){
				//compute the factor
				int id = eventIds.get(counter);
				Event e = this.buffer.getEvent(id);
				long validLower = validLowerBounds.get(counter);
				long validUpper = validUpperBounds.get(counter);
				if((e.getLowerBound() != validLower)  || (e.getUpperBound() != validUpper)){
					double c = 0;
					for(long i = validLower; i <= validUpper; i ++){
						c += e.getProbabilityForPoint(i);
					}
					factor *= c;
				}
				// remove the event
				eventIds.remove(counter);
				validLowerBounds.remove(counter);
				validUpperBounds.remove(counter);
			}else{
				counter ++;
			}
		}
		if(eventIds.size() > 0){
			r.setEventIds(eventIds);
			r.setCount(eventIds.size());
			r.setLowerBounds(validLowerBounds);
			r.setUpperBounds(validUpperBounds);
			double cForRemainingEvents;
			if(ConfigFlags.confidenceSharing){
				cForRemainingEvents = this.computeConfidenceWithSharingCache(m);
			}else{
				cForRemainingEvents = this.computeConfidenceBruteForce(m);
			}
			return factor * cForRemainingEvents;
		}else{
			return factor;
		}
	}
	
	public double removeIsolatedEventsOptimization(Match m){
		double factor = 1.0;
		Run r = m.getRun();
		ArrayList<Integer> eventIds = r.getEventIds();
		ArrayList<Long> validLowerBounds = r.getLowerBounds();
		ArrayList<Long> validUpperBounds = r.getUpperBounds();
		long currentLower, currentUpper, previousUpper, nextLower;
		int counter = 0;
		boolean previousResult, nextResult;
		while(counter < eventIds.size()){
			// compare with previous event
			if(counter != 0){
				previousUpper = validUpperBounds.get(counter - 1);
				currentLower = validLowerBounds.get(counter);
				if(previousUpper < currentLower){
					previousResult = true;
				}else{
					previousResult = false;
				}
			}else{
				previousResult = true;
			}
			if(previousResult){
				// compare with next event
				if(counter != eventIds.size() - 1){
					currentUpper = validUpperBounds.get(counter);
					nextLower = validLowerBounds.get(counter + 1);
					if(currentUpper < nextLower){
						nextResult = true;
					}else{
						nextResult = false;
					}
				}else{
					nextResult = true;
				}
			}else{
				nextResult = false;
			}
			if(nextResult){
				//compute the factor
				int id = eventIds.get(counter);
				Event e = this.buffer.getEvent(id);
				long validLower = validLowerBounds.get(counter);
				long validUpper = validUpperBounds.get(counter);
				double cForEvent = e.getProbabilityForRange(validLower, validUpper);
				factor *= cForEvent;
				// remove the event
				eventIds.remove(counter);
				validLowerBounds.remove(counter);
				validUpperBounds.remove(counter);
			}else{
				counter ++;
			}
		}
		if(eventIds.size() > 0){
			r.setEventIds(eventIds);
			r.setCount(eventIds.size());
			r.setLowerBounds(validLowerBounds);
			r.setUpperBounds(validUpperBounds);
			double cForRemainingEvents;
			if(ConfigFlags.confidenceSharing){
				cForRemainingEvents = this.computeConfidenceWithSharingCache(m);
			}else{
				cForRemainingEvents = this.computeConfidenceBruteForce(m);
			}
			return factor * cForRemainingEvents;
		}else{
			return factor;
		}
	}
	
	
	
	/*
	 * idea:
	 */
	public double computeConfidenceUsingPathPool(Match m){
		PathPool pool = PathPool.getInstance();
		Run r = m.getRun();
		ArrayList<Path> initialPaths = new ArrayList<Path>();
		long lower = r.getLowerBounds().get(0);
		long upper = r.getUpperBounds().get(0);
		int eId = r.getEventIds().get(0);
		Event e = this.buffer.getEvent(eId);
		for(long i = lower; i <= upper; i ++){
			double prob = e.getProbabilityForPoint(i);
			Path p = pool.getPath();
			Profiling.numberOfPaths ++;
			p.setLastEventTimestamp(i);
			p.setProbability(prob);
			initialPaths.add(p);
		}
		ArrayList<Path> newPaths = null; //= initialPaths;//??
		int counter = 1;
		while(counter < r.getCount()){
			//pool.returnPath(newPaths);
			newPaths = new ArrayList<Path>();
			eId = r.getEventIds().get(counter);
			e = this.buffer.getEvent(eId);
			lower = r.getLowerBounds().get(counter);
			upper = r.getUpperBounds().get(counter);
			for(int i = 0; i < initialPaths.size(); i ++){
				Path p = initialPaths.get(i);
				long start;
				if(p.getLastEventTimestamp() + 1 > lower){
					start = p.getLastEventTimestamp() + 1;
				}else{
					start = lower;
				}
				for(long j = start; j <= upper; j ++){
					long time1 = System.nanoTime();
					Path newPath = pool.getPath();
					Profiling.numberOfPaths ++;
					newPath.setLastEventTimestamp(j);
					newPath.setProbability(p.getProbability() * e.getProbabilityForPoint(j));
					Profiling.imprecisePhase1 += (System.nanoTime() - time1);
					long time2 = System.nanoTime();	
					newPaths.add(newPath);
					Profiling.imprecisePhase2 += (System.nanoTime() - time2);	
				}
			}
			pool.returnPath(initialPaths);
			initialPaths = newPaths;
			counter ++;
		}
		double resultC = this.computeSumOfPaths(newPaths);
		pool.returnPath(newPaths);
		//System.out.println("Confidence is:" + resultC + "=========================");
		return resultC;
	}
	
	
	public double computeConfidence(Match m){
		long startTime = System.nanoTime();
		double confidence = 0;
		if(ConfigFlags.removeIsolatedEvent){
			confidence = this.removeIsolatedEventsOptimization(m);
		}else{
			confidence = this.computeConfidenceBruteForce(m);
		}
		
		Profiling.addConfidence(confidence);
		Profiling.computeConfidenceNumbers ++;
		Profiling.computeConfidenceTime += (System.nanoTime() - startTime);
		if(confidence < ConfigFlags.confidenceThreshold){
			Profiling.numberOfFilteredMatchesByConfidence ++;
		}
		return confidence;
	}
	/*
	 * idea:
	 */
	public double computeConfidenceBruteForce(Match m){
		//long startTime = System.nanoTime();
		Run r = m.getRun();
		ArrayList<Path> initialPaths = new ArrayList<Path>();
		long lower = r.getLowerBounds().get(0);
		long upper = r.getUpperBounds().get(0);
		int eId = r.getEventIds().get(0);
		Event e = this.buffer.getEvent(eId);
		for(long i = lower; i <= upper; i ++){
			double prob = e.getProbabilityForPoint(i);
			Path p = new Path(i, prob);
			Profiling.numberOfPaths ++;
			initialPaths.add(p);
		}
		ArrayList<Path> newPaths = null; //= initialPaths;//??
		int counter = 1;
		while(counter < r.getCount()){
			newPaths = new ArrayList<Path>();
			eId = r.getEventIds().get(counter);
			e = this.buffer.getEvent(eId);
			lower = r.getLowerBounds().get(counter);
			upper = r.getUpperBounds().get(counter);
			for(int i = 0; i < initialPaths.size(); i ++){
				Path p = initialPaths.get(i);
				long start;
				if(p.getLastEventTimestamp() + 1 > lower){
					start = p.getLastEventTimestamp() + 1;
				}else{
					start = lower;
				}
				for(long j = start; j <= upper; j ++){
					long time1 = System.nanoTime();
					Path newP = new Path(j, p.getProbability() * e.getProbabilityForPoint(j));
					Profiling.numberOfPaths ++;
					Profiling.imprecisePhase1 += (System.nanoTime() - time1);
					long time2 = System.nanoTime();	
					newPaths.add(newP);
					Profiling.imprecisePhase2 += (System.nanoTime() - time2);	
					//cc ++;
					//System.out.println(cc + ": " + p.getLastEventTimestamp() + "," + j);
				}
			}
			initialPaths = newPaths;
			counter ++;
		}
		double resultC = this.computeSumOfPaths(newPaths);
		//System.out.println("Confidence is:" + resultC + "=========================");
		/*
		Profiling.addConfidence(resultC);
		Profiling.computeConfidenceNumbers ++;
		Profiling.computeConfidenceTime += (System.nanoTime() - startTime);
		if(resultC < ConfigFlags.confidenceThreshold){
			Profiling.numberOfFilteredMatchesByConfidence ++;
		}
		*/
		
		return resultC;
	}

	public double computeConfidenceForRun(Run r){
		ArrayList<Path> initialPaths = new ArrayList<Path>();
		long lower = r.getLowerBounds().get(0);
		long upper = r.getUpperBounds().get(0);
		int eId = r.getEventIds().get(0);
		Event e = this.buffer.getEvent(eId);
		for(long i = lower; i <= upper; i ++){
			double prob = e.getProbabilityForPoint(i);
			Path p = new Path(i, prob);
			Profiling.numberOfPaths ++;
			initialPaths.add(p);
		}
		ArrayList<Path> newPaths = null; //= initialPaths;//??
		int counter = 1;
		while(counter < r.getCount()){
			newPaths = new ArrayList<Path>();
			eId = r.getEventIds().get(counter);
			e = this.buffer.getEvent(eId);
			lower = r.getLowerBounds().get(counter);
			upper = r.getUpperBounds().get(counter);
			for(int i = 0; i < initialPaths.size(); i ++){
				Path p = initialPaths.get(i);
				long start;
				if(p.getLastEventTimestamp() + 1 > lower){
					start = p.getLastEventTimestamp() + 1;
				}else{
					start = lower;
				}
				for(long j = start; j <= upper; j ++){
					long time1 = System.nanoTime();
					Path newP = new Path(j, p.getProbability() * e.getProbabilityForPoint(j));
					Profiling.numberOfPaths ++;
					Profiling.imprecisePhase1 += (System.nanoTime() - time1);
					long time2 = System.nanoTime();	
					newPaths.add(newP);
					Profiling.imprecisePhase2 += (System.nanoTime() - time2);	
					//cc ++;
					//System.out.println(cc + ": " + p.getLastEventTimestamp() + "," + j);
				}
			}
			initialPaths = newPaths;
			counter ++;
		}
		double resultC = this.computeSumOfPaths(newPaths);
		//System.out.println("Confidence is:" + resultC + "=========================");
		return resultC;
	}
	
	/*
	 * 
	 * 
	 */
	public int cachedFromEvent(Match m){
		StringBuilder keyString = new StringBuilder("");
		Run r = m.getRun();
		int eId;
		for(int i = 0; i < r.getCount(); i ++){
			eId = r.getEventIds().get(i);
			keyString.append(eId + "-");
			if(!this.confidenceCache.containsKey(keyString.toString())){
				if(i > 0){
					Profiling.computeConfidenceCacheHitNumbers += i;
				}
				
				return i;
			}
		}
		return 0;
	}
	
	/*
	 * idea:
	 */
	public double computeConfidenceWithSharingCache(Match m){
		StringBuilder keyString = new StringBuilder("");
		Run r = m.getRun();
		ArrayList<Path> initialPaths = new ArrayList<Path>();
		long lower = r.getLowerBounds().get(0);
		long upper = r.getUpperBounds().get(0);
		int eId;
		//keyString.append(eId);
		Event e = null;
		int startPosition = this.cachedFromEvent(m);
		if(startPosition == 0){
			eId = r.getEventIds().get(0);
			keyString.append(eId + "-");
			e = this.buffer.getEvent(eId);
			for(long i = lower; i <= upper; i ++){
				
				initialPaths.add(new Path(i, e.getProbabilityForPoint(i)));
			}
			this.confidenceCache.put(keyString.toString(), initialPaths);
		}else{
			for(int i = 0; i < startPosition; i ++){
				eId = r.getEventIds().get(i);
				keyString.append(eId + "-");
			}
			initialPaths = this.confidenceCache.get(keyString.toString());
		}
		ArrayList<Path> newPaths = initialPaths;
		int counter = startPosition;
		while(counter < r.getCount()){
			newPaths = new ArrayList<Path>();
			eId = r.getEventIds().get(counter);
			keyString.append( eId + "-");
			{
				e = this.buffer.getEvent(eId);
				lower = r.getLowerBounds().get(counter);
				upper = r.getUpperBounds().get(counter);
				for(int i = 0; i < initialPaths.size(); i ++){
					Path p = initialPaths.get(i);
					long start;
					if(p.getLastEventTimestamp() + 1 > lower){
						start = p.getLastEventTimestamp();
					}else{
						start = lower;
					}
					for(long j = start; j <= upper; j ++){
						Path newP = new Path(j, p.getProbability() * e.getProbabilityForPoint(j));
						newPaths.add(newP);
						//System.out.println(newP);
					}
				}
				this.confidenceCache.put(keyString.toString(), newPaths);
			}
			initialPaths = newPaths;
			counter ++;
		}
		return this.computeSumOfPaths(newPaths);
	}
	
	/*
	 * Optimization with sharing cache
	 */
	/*
	public double computeConfidenceWithSharingCache(Match m){
		//debug
		//System.out.println("Debug: CacheShared");
		
		StringBuilder keyString = new StringBuilder("");
		Run r = m.getRun();
		ArrayList<Path> initialPaths = new ArrayList<Path>();
		int lower = r.getLowerBounds().get(0);
		int upper = r.getUpperBounds().get(0);
		int eId = r.getEventIds().get(0);
		keyString.append(eId);
		Event e = null;
		if(this.confidenceCache.containsKey(keyString.toString())){
			initialPaths = this.confidenceCache.get(keyString.toString());
			Profiling.computeConfidenceCacheHitNumbers ++;
		}else{
			e = this.buffer.getEvent(eId);
			for(int i = lower; i <= upper; i ++){
				
				initialPaths.add(new Path(i, e.getProbabilityForPoint(i)));
			}
			this.confidenceCache.put(keyString.toString(), initialPaths);
		}
		ArrayList<Path> newPaths = initialPaths;
		int counter = 1;
		while(counter < r.getCount()){
			
			newPaths = new ArrayList<Path>();
			
			eId = r.getEventIds().get(counter);
			keyString.append("-" + eId);
			if(this.confidenceCache.containsKey(keyString.toString())){
				newPaths = this.confidenceCache.get(keyString.toString());
				Profiling.computeConfidenceCacheHitNumbers ++;
			}else{
				e = this.buffer.getEvent(eId);
				lower = r.getLowerBounds().get(counter);
				upper = r.getUpperBounds().get(counter);
				
				for(int i = 0; i < initialPaths.size(); i ++){
					Path p = initialPaths.get(i);
					int start;
					if(p.getLastEventTimestamp() + 1 > lower){
						start = p.getLastEventTimestamp();
					}else{
						start = lower;
					}
					
					for(int j = start; j <= upper; j ++){
						Path newP = new Path(j, p.getProbability() * e.getProbabilityForPoint(j));
						newPaths.add(newP);
						//System.out.println(newP);
					}
				}
				
				this.confidenceCache.put(keyString.toString(), newPaths);
			}
			
			initialPaths = newPaths;
			counter ++;
		}
		
		return this.computeSumOfPaths(newPaths);
	}*/
	
	public double computeSumOfPaths(ArrayList<Path> paths){
		if(paths == null){
			return 0.0;
		}
		double c = 0;
		for(int i = 0; i < paths.size(); i ++){
			c += paths.get(i).getProbability();
		}
		
		return c;
	}
	
public void cleanConfidenceSharingCache(){
	//System.out.println("haha");
	if(ConfigFlags.confidenceSharing){
		this.confidenceCache.clear();
		//this.confidenceCache = new HashMap<String, ArrayList<Path>>();
		//System.gc();
	}
	}
	
	public void outputMatchPostponeOptimized(int[]eventIdsBeforeKleene, int[]eventIdsForKleene,int[]eventIdsAfterKleene){
		ArrayList<Long> lowerBounds = new ArrayList<Long>();
		ArrayList<Long> upperBounds = new ArrayList<Long>();
		ArrayList<Integer> eventIds = new ArrayList<Integer>();
		//events before kleene
		if(eventIdsBeforeKleene != null){
			for(int i = 0; i < eventIdsBeforeKleene.length; i ++){
				int eId = eventIdsBeforeKleene[i];
				eventIds.add(eId);
			}
		}
		// events for kleene
		if(eventIdsForKleene != null){
			for(int i = 0; i < eventIdsForKleene.length; i ++){
				int eId = eventIdsForKleene[i];
				eventIds.add(eId);
			}
		}
		// events after kleene
		if(eventIdsAfterKleene != null){
			for(int i = 0; i < eventIdsAfterKleene.length; i ++){
				int eId = eventIdsAfterKleene[i];
				eventIds.add(eId);
			}
		}
		//set lower bounds and initialize upper bounds
		Event e;
		for(int i = 0; i < eventIds.size(); i ++){
			int eId = eventIds.get(i);
			e = this.buffer.getEvent(eId);
			//initialize upper
			upperBounds.add(e.getUpperBound());
			//set lower
			if(i == 0){
				lowerBounds.add(e.getLowerBound());
			}else{
				if(lowerBounds.get(i - 1) + 1 > e.getLowerBound()){
					lowerBounds.add(lowerBounds.get(i - 1) + 1);
				}else{
					lowerBounds.add(e.getLowerBound());
				}
			}
		}
		//update upper bounds for the first time
		for(int i = eventIds.size() - 2; i >= 0; i --){
			if(upperBounds.get(i + 1) - 1 < upperBounds.get(i)){
				upperBounds.set(i, upperBounds.get(i + 1) - 1);
			}
		}
		//adjust upper bounds and update upper bounds again
		if(upperBounds.get(0) + this.nfa.getTimeWindow() < upperBounds.get(eventIds.size() - 1)){
			upperBounds.set(eventIds.size() - 1, upperBounds.get(0) + this.nfa.getTimeWindow());
			boolean needUpdate = true;
			int counter = eventIds.size() - 2;
			while(needUpdate && counter >= 0){
				if(upperBounds.get(counter + 1) - 1 < upperBounds.get(counter)){
					upperBounds.set(counter, upperBounds.get(counter + 1) - 1);
				}else{
					needUpdate = false;
				}
			}
		}
		
		this.tempRun = this.uncertainEngineRunController.getRun();// to optimized, re-use runs
		tempRun.setEventIds(eventIds);
		tempRun.setCount(eventIds.size());
		tempRun.setLowerBounds(lowerBounds);
		tempRun.setUpperBounds(upperBounds);
		Match m = new Match(tempRun, this.nfa, this.buffer);
		this.outputMatch(m);
	}
	/**
	 * Validates the uncertainty interval.
	 * @param eventIdsBeforeKleene
	 * @param eventIdsForKleene
	 * @param eventIdsAfterKleene
	 * @param kleeneEventSafeFlags
	 * @param kleeneSafeFlagValidateResult
	 * @param afterKleeneSafeFlags
	 * @param afterKleeneSafeFlagValidateResult
	 * @return
	 */
	
	public boolean validateMatchUncertaintyInterval(int[]eventIdsBeforeKleene, int[]eventIdsForKleene, int[]eventIdsAfterKleene){
		
		int size = 0;
		if(eventIdsBeforeKleene != null){
			size += eventIdsBeforeKleene.length;
		}
		if(eventIdsForKleene != null){
			size += eventIdsForKleene.length;
		}
		if(eventIdsAfterKleene != null){
			size += eventIdsAfterKleene.length;
		}
		int[] ids = new int[size];
		long[] lowerBounds = new long[size];
		long[] upperBounds = new long[size];
		Event e;
		int count = 0;
		if(eventIdsBeforeKleene != null){
			for(int i = 0; i < eventIdsBeforeKleene.length; i ++){
				ids[count] = eventIdsBeforeKleene[i];
				count ++;
			}

		}
		if(eventIdsForKleene != null){
			for(int i = 0; i < eventIdsForKleene.length; i ++){
				ids[count] = eventIdsForKleene[i];
				count ++;
			}
		}
		if(eventIdsAfterKleene != null){
			for(int i = 0; i < eventIdsAfterKleene.length; i ++){
				ids[count] = eventIdsAfterKleene[i];
				count ++;
			}
		}
		
		for(int i = 0; i < ids.length; i ++){
			e = this.buffer.getEvent(ids[i]);
			if(i == 0){
				lowerBounds[0] = e.getLowerBound();
				upperBounds[0] = e.getUpperBound();
			}else{
				//set lower
				if(lowerBounds[i - 1] + 1 > e.getLowerBound()){
					lowerBounds[i] = lowerBounds[i - 1] + 1;
				}else{
					lowerBounds[i] = e.getLowerBound();
				}
				//initialize upper
				upperBounds[i] = e.getUpperBound();
				//validate first time
				if(lowerBounds[i] > upperBounds[i]){
					return false;
				}
			}
		}
		//update upperbounds for the first time
		for(int i = size - 2; i >= 0; i --){
			if(upperBounds[i + 1] - 1 < upperBounds[i]){
				upperBounds[i] = upperBounds[i + 1] - 1;
				//validate second time
				if(lowerBounds[i] > upperBounds[i]){
					return false;
				}
			}
		}
		//operate on upper 
		if(upperBounds[0] + this.nfa.getTimeWindow() < upperBounds[size - 1]){
			upperBounds[size - 1] = upperBounds[0] + this.nfa.getTimeWindow();
			//validate third time
			if(lowerBounds[size - 1] > upperBounds[size - 1]){
				return false;
			}
			for(int i = size -2; i >= 0; i --){
				if(upperBounds[i + 1] - 1 < upperBounds[i]){
					upperBounds[i] = upperBounds[i + 1] - 1;
					//validate fourth time
					if(lowerBounds[i] > upperBounds[i]){
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean validateMatchUncertaintyIntervalForDP(int[]eventIdsBeforeKleene, int[]eventIdsForKleene, int[]eventIdsAfterKleene){
		int size = 0;
		if(eventIdsBeforeKleene != null){
			size += eventIdsBeforeKleene.length;
		}
		if(eventIdsForKleene != null){
			size += eventIdsForKleene.length;
		}
		if(eventIdsAfterKleene != null){
			size += eventIdsAfterKleene.length;
		}
		ArrayList<Integer> idsList = new ArrayList<Integer>();
		ArrayList<Long> lowerList = new ArrayList<Long>();
		ArrayList<Long> upperList = new ArrayList<Long>();
		
		double[] prob = new double[size];
		Event[] events = new Event[size];
		
		Event e;
		if(eventIdsBeforeKleene != null){
			for(int i = 0; i < eventIdsBeforeKleene.length; i ++){
				idsList.add(eventIdsBeforeKleene[i]);
			}
		}
		if(eventIdsForKleene != null){
			for(int i = 0; i < eventIdsForKleene.length; i ++){
				idsList.add(eventIdsForKleene[i]);
			}
		}
		if(eventIdsAfterKleene != null){
			for(int i = 0; i < eventIdsAfterKleene.length; i ++){
				idsList.add(eventIdsAfterKleene[i]);
			}
		}
		for(int i = 0; i < idsList.size(); i ++){
			e = this.buffer.getEvent(idsList.get(i));
			//heuristic
			events[i] = e;
			//end
			if(i == 0){
				lowerList.add(e.getLowerBound());
				upperList.add(e.getUpperBound());
			}else{
				//set lower
				
				if(lowerList.get(i - 1) + 1 > e.getLowerBound()){
					lowerList.add(lowerList.get(i - 1) + 1);
					//heuristic
					if(ConfigFlags.estimateProbability){
						prob[i] = e.getProbabilityForRange(lowerList.get(i), e.getUpperBound());
						if(prob[i] < ConfigFlags.confidenceThreshold){
							Profiling.estimateProbabilityFiltering ++;
							return false;
						}
					}
					//heuristic end
				}else{
					lowerList.add(e.getLowerBound());
				}
				//initialize upper
				upperList.add(e.getUpperBound());
				//validate first time
				if(lowerList.get(i) > upperList.get(i)){
					return false;
				}
			}
		}
		//update upperbounds for the first time
		for(int i = size - 2; i >= 0; i --){
			if(upperList.get(i + 1) - 1 < upperList.get(i)){
				upperList.set(i, upperList.get(i + 1) - 1);
				if(ConfigFlags.estimateProbability){
					prob[i] = events[i].getProbabilityForRange(lowerList.get(i), upperList.get(i));
					if(prob[i] < ConfigFlags.confidenceThreshold){
						Profiling.estimateProbabilityFiltering ++;
						return false;
					}
				}
				if(lowerList.get(i) > upperList.get(i)){
					return false;
				}
			}
		}
		//operate on upper 
		if(upperList.get(0) + this.nfa.getTimeWindow() < upperList.get(size - 1)){
			upperList.set(size - 1, upperList.get(0) + this.nfa.getTimeWindow());
			if(lowerList.get(size - 1) > upperList.get(size - 1)){
				return false;
			}
			for(int i = size - 2; i >= 0; i --){
				if(upperList.get(i + 1) - 1 < upperList.get(i)){
					upperList.set(i, upperList.get(i + 1) - 1);
					if(ConfigFlags.estimateProbability){
						
						prob[i] = events[i].getProbabilityForRange(lowerList.get(i), upperList.get(i));
						if(prob[i] < ConfigFlags.confidenceThreshold){
							Profiling.estimateProbabilityFiltering ++;
							return false;
						}
					}
					if(lowerList.get(i) > upperList.get(i)){
						return false;
					}
				}
			}
		}
		for(int i = 0; i < size; i ++){
			if(prob[i] == 0){
				prob[i] = events[i].getProbabilityForRange(lowerList.get(i), upperList.get(i));
			}
		}
		
		if(ConfigFlags.estimateProbability){
			double estimateProb = 1.0;
			for(int i = 0; i < prob.length; i ++){
				estimateProb = estimateProb * prob[i];
				if(estimateProb < ConfigFlags.confidenceThreshold){
					Profiling.estimateProbabilityFiltering ++;
					return false;
				}
			}
		}
		
		
		//set to a shared run
		if(this.tempRun == null){
			this.tempRun = this.uncertainEngineRunController.getRun();// to optimized, re-use runs
		}
		
		this.tempRun.setEventIds(idsList);
		this.tempRun.setCount(idsList.size());
		this.tempRun.setLowerBounds(lowerList);
		this.tempRun.setUpperBounds(upperList);
		
		

		
		//
		return true;
	}
	public boolean verifyProbabilityForEvent(Event e, int lower, int upper){
		double p = e.getProbabilityForRange(lower, upper);
		if(p >= ConfigFlags.confidenceThreshold){
			return true;
		}
		return false;
	}
	
	/**
	 * Validates the predicates 
	 */
	public boolean validateMatch(int[]eventIdsBeforeKleene, int[]eventIdsForKleene, int[]eventIdsAfterKleene,
			boolean[]kleeneEventSafeFlags,boolean kleeneSafeFlagValidateResult, 
			boolean[]afterKleeneSafeFlags, boolean afterKleeneSafeFlagValidateResult){
		Profiling.numberOfValidations ++;
		long time1 = System.nanoTime();
		if(this.runForEnumeration == null){
			this.runForEnumeration= this.uncertainEngineRunController.getRun();
		}
		Profiling.timeBeforeEnumeration += (System.nanoTime() - time1);
		long time2 = System.nanoTime();
		runForEnumeration.initializeRun(nfa);
		Profiling.timeAfterEnumeration += (System.nanoTime() - time2);
		//fill events before kleene
		if(eventIdsBeforeKleene != null){
			for(int i = 0; i < eventIdsBeforeKleene.length; i++){
				int id = eventIdsBeforeKleene[i];
				Event e = this.buffer.getEvent(id);
				runForEnumeration.addEvent(e);
			}
		}
		//events for kleene
		if(eventIdsForKleene != null){
			if(kleeneSafeFlagValidateResult){
				for(int i = 0; i < eventIdsForKleene.length; i++){
					int id = eventIdsForKleene[i];
					Event e = this.buffer.getEvent(id);
					runForEnumeration.addEvent(e);
				}
			}else{
				for(int i = 0; i < eventIdsForKleene.length; i++){
					int id = eventIdsForKleene[i];
					Event e = this.buffer.getEvent(id);
					if(kleeneEventSafeFlags[i]){
						runForEnumeration.addEvent(e);
					}else{
						boolean evaluateResult = this.postponeCheckPredicate(e, runForEnumeration);
						if(evaluateResult){
							runForEnumeration.addEvent(e);
						}else{
							return false;
						}
					}
				}
			}
		}
		runForEnumeration.proceed();
		if(afterKleeneSafeFlagValidateResult){
			return true;
		}else{
			if(eventIdsAfterKleene != null){
				for(int i = 0; i < eventIdsAfterKleene.length; i++){
					int id = eventIdsAfterKleene[i];
					Event e = this.buffer.getEvent(id);
					if(afterKleeneSafeFlags[i]){
						runForEnumeration.addEvent(e);
					}else{
						boolean evaluateResult = this.postponeCheckPredicate(e, runForEnumeration);
						if(evaluateResult){
							runForEnumeration.addEvent(e);
						}else{
							return false;
						}
					}
				}
			}
		}
		//all events are ok
		return true;
	}
	
	public boolean validatePredicateForDP(EnumerationInstance enumInstance){
		//first check flags to see it is necessary to validate predicates
		boolean afterKleeneSafeFlagValidateResult = enumInstance.isAfterKleeneSafeFlagValidateResult();
		boolean kleeneSafeFlagValidateResult  = this.validateSafeFlags(enumInstance.getFlags());
		if(kleeneSafeFlagValidateResult  && afterKleeneSafeFlagValidateResult){
			return true;
		}
		// validate predicates
		Profiling.numberOfValidations ++;
		int[]eventIdsBeforeKleene = enumInstance.getEventIdsBeforeKleene();
		int[]eventIdsForKleene = this.convertIntegerArrayListToArray(enumInstance.getIds());
		
		int[] eventIdsAfterKleene = enumInstance.getEventIdsAfterKleene();
		
		
		long time1 = System.nanoTime();
		if(this.runForEnumeration == null){
			this.runForEnumeration= this.uncertainEngineRunController.getRun();
		}
		Profiling.timeBeforeEnumeration += (System.nanoTime() - time1);
		long time2 = System.nanoTime();
		runForEnumeration.initializeRun(nfa);
		Profiling.timeAfterEnumeration += (System.nanoTime() - time2);
		//fill events before kleene
		if(eventIdsBeforeKleene != null){
			for(int i = 0; i < eventIdsBeforeKleene.length; i++){
				int id = eventIdsBeforeKleene[i];
				Event e = this.buffer.getEvent(id);
				runForEnumeration.addEvent(e);
			}
		}
		//events for kleene
		if(eventIdsForKleene != null){
			if(kleeneSafeFlagValidateResult){
				for(int i = 0; i < eventIdsForKleene.length; i++){
					int id = eventIdsForKleene[i];
					Event e = this.buffer.getEvent(id);
					runForEnumeration.addEvent(e);
				}
			}else{
				boolean[] kleeneEventSafeFlags =this.convertBooleanArrayListToArray(enumInstance.getFlags());
				for(int i = 0; i < eventIdsForKleene.length; i++){
					int id = eventIdsForKleene[i];
					Event e = this.buffer.getEvent(id);
					if(kleeneEventSafeFlags[i]){
						runForEnumeration.addEvent(e);
					}else{
						boolean evaluateResult = this.postponeCheckPredicate(e, runForEnumeration);
						if(evaluateResult){
							runForEnumeration.addEvent(e);
						}else{
							return false;
						}
					}
				}
			}
		}
		runForEnumeration.proceed();
		if(afterKleeneSafeFlagValidateResult){
			return true;
		}else{
			if(eventIdsAfterKleene != null){
				boolean[]afterKleeneSafeFlags = enumInstance.getAfterKleeneSafeFlags();
				for(int i = 0; i < eventIdsAfterKleene.length; i++){
					int id = eventIdsAfterKleene[i];
					Event e = this.buffer.getEvent(id);
					if(afterKleeneSafeFlags[i]){
						runForEnumeration.addEvent(e);
					}else{
						boolean evaluateResult = this.postponeCheckPredicate(e, runForEnumeration);
						if(evaluateResult){
							runForEnumeration.addEvent(e);
						}else{
							return false;
						}
					}
				}
			}
		}
		//all events are ok
		return true;
	}

	
	public void evaluateEventOptimizedForSkipTillAny(Event e, Run r) throws CloneNotSupportedException{
		boolean timewindowResult = this.checkTimeWindow(e, r);
		if(timewindowResult){
			int checkResult = this.checkPredicateOptimized(e, r);
			Run newRun = null;
			switch(checkResult){
				case 1:
					
					
					newRun = this.cloneRun(r);
					this.addRunByPartition(newRun);
					
					this.addEventToRun(r, e);
					
					break;
				case 2:
					newRun = this.cloneRun(r);
					this.addRunByPartition(newRun);
					  
					r.proceed();
					this.addEventToRun(r, e);
			}
		}else{
			
			if(e.getLowerBound() - r.getUpperBounds().get(0) > this.nfa.getTimeWindow()){
				this.toDeleteRuns.add(r);
			}
			
			
			
		}

	}
	
	
	public void evaluateEventPostponeOptimizedForSkipTillAny(Event e, Run r) throws CloneNotSupportedException, EvaluationException{
		//modify here
		boolean timeWindowResult = this.checkTimeWindow(e, r);
		if(timeWindowResult){
			//
			//if K+, check two states
			//if normal check one state
			// when checking, should be check each edge!
			/*
			 * if K+
			 * result1 = evaluateAs K+
			 * result2 = evaluateAs next
			 * 
			 * if result 2 = true, clone
			 * then process result 1
			 * 
			 * if non k+
			 * result1 = evaluate as normal
			 * 
			 */
			//////////
			int currentState = r.getCurrentState();
			State s = this.nfa.getStates(currentState);
			//
			if(s.isKleeneClosure()){
				if(r.isKleeneClosureInitialized()){
					int currentStateNumber = r.getCurrentState();
					r.setCurrentState(currentStateNumber + 1);
					EdgeEvaluationResult normalResult = this.checkEdgePostponeOptimized(e, r, 0);//k+'s next state
					
					if(normalResult != EdgeEvaluationResult.Discard){
						//clone
						Run newRun = this.cloneRun(r);
						// add new run to run pool
						this.addRunByPartition(newRun);
						//add event to the newRun
						//proceed to next state, shift is 1
						//TODO
						this.addEventToRunPostPoneOptimized(newRun, e, normalResult, 0);
						
					}
					r.setCurrentState(currentStateNumber);
				}
				
				EdgeEvaluationResult kleeneResult = this.checkEdgePostponeOptimized(e, r, 0);//k+
				if(kleeneResult != EdgeEvaluationResult.Discard){
					// add event to the current run
					this.addEventToRunPostPoneOptimized(r, e, kleeneResult, 0);
				}
				
			}else{
				EdgeEvaluationResult normalResult = this.checkEdgePostponeOptimized(e, r, 0);
				if(normalResult != EdgeEvaluationResult.Discard){
					//clone 
					Run newRun = this.cloneRun(r);
					// add to existing runs pool
					this.addRunByPartition(newRun);
					//add event to the currentRun
					this.addEventToRunPostPoneOptimized(r, e, normalResult, 0);
				}
			}
		}else{
			//check output of the run, or discard the run
			if(r.checkMatch()){
				this.enumerateMatchesForRun(r);
				this.toDeleteRuns.add(r);
			}else{
				//this.toDeleteRuns.add(r);//expired
				//System.out.println("DELETE run: " + r + " by event:" + e);
			}
		}
	
	}
	

	public void evaluateEventPostponeOptimizedFromStackForSkipTillAny(Event e, Run r, String edgeTag) throws CloneNotSupportedException, EvaluationException{
		boolean timeWindowResult = this.checkTimeWindow(e, r);

		if(timeWindowResult){
			int currentState = r.getCurrentState();
			State s = this.nfa.getStates(currentState);
			if(s.isKleeneClosure()){
				if(r.isKleeneClosureInitialized()){
					int currentStateNumber = r.getCurrentState();
					r.setCurrentState(currentStateNumber + 1);
					EdgeEvaluationResult normalResult = this.checkEdgePostponeOptimizedFromStack(e, r, 0, edgeTag);//k+'s next state
					if(normalResult != EdgeEvaluationResult.Discard){
						Run newRun = this.cloneRun(r);
						this.addRunByPartition(newRun);
						this.addEventToRunPostPoneOptimized(newRun, e, normalResult, 0);
					}
					r.setCurrentState(currentStateNumber);
				}
				EdgeEvaluationResult kleeneResult = this.checkEdgePostponeOptimizedFromStack(e, r, 0, edgeTag);//k+, no cloning here
				if(kleeneResult != EdgeEvaluationResult.Discard){
					this.addEventToRunPostPoneOptimized(r, e, kleeneResult, 0);
				}
			}else{
				EdgeEvaluationResult normalResult = this.checkEdgePostponeOptimizedFromStack(e, r, 0, edgeTag);
				if(normalResult != EdgeEvaluationResult.Discard){
					Run newRun = this.cloneRun(r);
					this.addRunByPartition(newRun);
					this.addEventToRunPostPoneOptimized(r, e, normalResult, 0);
				}
			}
		}else{
			//check output of the run, or discard the run
			if(r.checkMatch()){
				this.enumerateMatchesForRun(r);
				this.toDeleteRuns.add(r);
			}else{
				//todo
				if(this.nfa.getLastEdgeTag().equalsIgnoreCase(edgeTag) && r.checkTimeWindowLarge(e)){
					this.toDeleteRuns.add(r);
				}
			}
		}
	}


	/**
	 * One event vs. One edge(Multiple predicates)
	 * @param e
	 * @param r
	 * @param shift 0 means evaluate the current state, 1 means evaluate the next state
	 * @return EdgeEvaluationResult.Discard/Safe/Unsafe
	 * @throws EvaluationException 
	 */
	public EdgeEvaluationResult checkEdgePostponeOptimizedFromStack(Event e, Run r, int shift, String edgeTag) throws EvaluationException{
		// safe, unsafe, discard
		int currentState = r.getCurrentState();
		State s = this.nfa.getStates(currentState + shift);
		if(!s.getEventType().equalsIgnoreCase(e.getEventType())){
			return EdgeEvaluationResult.Discard;
		}
		Edge edge;
		if(!s.isKleeneClosure()){
			edge = s.getEdges(0);
		}else{
			if(!r.isKleeneClosureInitialized()){
				edge = s.getEdges(0);
			}else{
				edge = s.getEdges(1);
			}
		}
		EdgeEvaluationResult result;
		if(edge.getEdgeTag().equalsIgnoreCase(edgeTag) && edge.isSingle()){
			result = EdgeEvaluationResult.Safe;
		}else{
			result = edge.evaluatePredicatePostponeOptimzed(e, r, buffer);
		}
		return result;
		}
	/**
	 * Add event to a run.
	 * @param r
	 * @param e
	 * @param edgeCheckResult
	 * @param shift
	 */
	public void addEventToRunPostPoneOptimized(Run r, Event e, EdgeEvaluationResult edgeCheckResult, int shift){
		
		//check output
		this.buffer.bufferEvent(e);// add the event to buffer
		int oldState = 0;
		int newState = 0;
		//shift
		oldState = r.getCurrentState() + shift;
		r.setCurrentState(oldState);
		//label event, labels should be saved in the run!!!
		if(edgeCheckResult == EdgeEvaluationResult.Safe){
			r.addEventAndStatus(e, true);
		}else{
			r.addEventAndStatus(e, false);
		}
		newState = r.getCurrentState();
		if(oldState == newState)//kleene closure
			if(r.isFull){
				//check match and output match
				if(r.checkMatchPostponeOptimized()){
					this.enumerateMatchesForRun(r);
					this.toDeleteRuns.add(r);
				}
			}
	}
	public void enumerateMatchesForRun(Run r){
		if(ConfigFlags.usingDynamicProgrammingForConfidence){
			//debugging
			//this.enumerateMatchesForRunWithDynamicProgramming(r);
			this.enumerateMatchesForRunWithDynamicProgrammingUsingEnumInstance(r);
		}else{
			this.enumerateMatchesForRunWithoutDP(r);
		}
	}
	/*
	public void enumerateMatchesForRunWithDynamicProgramming(Run r){
		//get all events for kleene closure
		long startTime = System.nanoTime();
		Profiling.numberOfRunsReachedMatchPostponeOptimized ++;
		Profiling.numberOfEventsInRunsReachedMatch += r.getCount();
		if(r.getCount() > Profiling.maxNumberOfEventsInRunsReachedMatch){
			Profiling.maxNumberOfEventsInRunsReachedMatch = r.getCount();
		}
		if(ConfigFlags.usingCollapsedFormatResult){
			this.outputMatch(new Match(r, this.nfa, this.buffer));
			Profiling.numberOfCollapsedMatches ++;
			return;
		}
		//main part
		int totalEvents = r.getCount();
		int otherEvents = this.nfa.getSize();// the first event of Kleene closure is included in other events
		int totalKleeneEvents = totalEvents - otherEvents;
		if(totalKleeneEvents > 0){
			int firstKleeneEventPosition = this.nfa.getKleeneClosureStatePosition() + 1;
			int numberOfEventsBeforeKleene = firstKleeneEventPosition;
			int numberOfEventsAfterKleene = this.nfa.getSize() - this.nfa.getKleeneClosureStatePosition() - 1;
			int lastKleeneEventPosition = totalEvents - numberOfEventsAfterKleene - 1;
			ArrayList<Integer> allEventIds = r.getEventIds();
			int[]eventIdsBeforeKleene = null;
			int[]eventIdsForKleene = null;
			int[]eventIdsAfterKleene = null;
			ArrayList<Boolean> allEventSafeFlags = null;
			boolean[]afterKleeneSafeFlags = null;
			if(!this.nfa.isSafe()){
				allEventSafeFlags = r.getEventSafeFlags();
			}
			//fill events before Kleene
			if(firstKleeneEventPosition > 0){
				eventIdsBeforeKleene = new int[numberOfEventsBeforeKleene];
				for(int i = 0; i < numberOfEventsBeforeKleene; i ++){
					eventIdsBeforeKleene[i] = allEventIds.get(i);
				}
			}
			//fill events after Kleene
			if(lastKleeneEventPosition < totalEvents - 1){
				eventIdsAfterKleene = new int[numberOfEventsAfterKleene];
				afterKleeneSafeFlags = new boolean[numberOfEventsAfterKleene];
				for(int i =0 ; i < numberOfEventsAfterKleene; i ++){
					eventIdsAfterKleene[i] = allEventIds.get(lastKleeneEventPosition + 1 + i);
				}
				if(!this.nfa.isSafe()){
					for(int i =0 ; i < numberOfEventsAfterKleene; i ++){
						afterKleeneSafeFlags[i] = allEventSafeFlags.get(lastKleeneEventPosition + 1 + i);
					}
				}
			}
			//fill events for kleene
			eventIdsForKleene = new int[totalKleeneEvents];
			for(int i = 0; i < totalKleeneEvents; i ++){
				eventIdsForKleene[i] = allEventIds.get(i + firstKleeneEventPosition);
			}
			//dynamic programming part starts
			int totalInstanceNumbers = 0;
			ArrayList<ArrayList<Integer>> enumSpace;
			for(int i = 0; i < totalKleeneEvents; i ++){
				enumSpace = new ArrayList<ArrayList<Integer>>();
				ArrayList<Integer> enumInstance = new ArrayList<Integer>();
				enumInstance.add(eventIdsForKleene[i]);
				int kIds[] = this.convertIntegerArrayListToArray(enumInstance);
				if(this.validateEnumInstance(eventIdsBeforeKleene, kIds, eventIdsAfterKleene)){
					enumSpace.add(enumInstance);
					for(int j = i + 1; j < totalKleeneEvents; j ++){
						int size = enumSpace.size();
						for(int k = 0; k < size; k ++){
							ArrayList<Integer> existingEnumInstance = enumSpace.get(k);
							ArrayList<Integer> newEnumInstance = new ArrayList<Integer>(existingEnumInstance);
							newEnumInstance.add(eventIdsForKleene[j]);
							int[] newIdsForK = this.convertIntegerArrayListToArray(newEnumInstance);
							if(this.validateEnumInstance(eventIdsBeforeKleene, newIdsForK, eventIdsAfterKleene)){
								enumSpace.add(newEnumInstance);
							}
						}
					}
				}
				System.out.println("There are total enumerations:" + enumSpace.size());
				totalInstanceNumbers += enumSpace.size();
			}
			System.out.println("Total number of enumerations:" + totalInstanceNumbers);
			
			//dynamic programming part ends~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			//output the run without enumerated k+ events
			if(this.validateMatchUncertaintyInterval(eventIdsBeforeKleene, null, eventIdsAfterKleene)){
				if(this.nfa.isSafe()){
					this.outputMatchPostponeOptimized(eventIdsBeforeKleene, null, eventIdsAfterKleene);
				}else{
					boolean afterKleeneSafeFlagValidateResult = this.validateSafeFlags(afterKleeneSafeFlags);
					if(afterKleeneSafeFlagValidateResult){
						this.outputMatchPostponeOptimized(eventIdsBeforeKleene, null, eventIdsAfterKleene);
					}else{
						boolean matchValidateResult;
						matchValidateResult = this.validateMatch(eventIdsBeforeKleene, null, eventIdsAfterKleene, 
								null,true, afterKleeneSafeFlags, afterKleeneSafeFlagValidateResult);
						if(matchValidateResult){
							this.outputMatchPostponeOptimized(eventIdsBeforeKleene, null, eventIdsAfterKleene);
						}
					}
				}
			}
			
		}else{
			boolean matchValidate = this.validateMatchWithoutEnumeration(r);
			if(matchValidate){
				this.outputMatch(new Match(r, this.nfa, this.buffer));
			}
		}
		//clean the cache for imprecise case
		this.cleanConfidenceSharingCache();
		//profiling
		Profiling.enumerationTime += System.nanoTime() - startTime;

		
	}
*/	
	/*
	 * This one only makes use of one dimentional information of dp
	 */
	public void enumerateMatchesForRunWithDynamicProgrammingUsingEnumInstance(Run r){
		//get all events for kleene closure
		long startTime = System.nanoTime();
		Profiling.numberOfRunsReachedMatchPostponeOptimized ++;
		Profiling.numberOfEventsInRunsReachedMatch += r.getCount();
		if(r.getCount() > Profiling.maxNumberOfEventsInRunsReachedMatch){
			Profiling.maxNumberOfEventsInRunsReachedMatch = r.getCount();
		}
		if(ConfigFlags.usingCollapsedFormatResult){
			this.outputMatch(new Match(r, this.nfa, this.buffer));
			Profiling.numberOfCollapsedMatches ++;
			return;
		}
		//main part
		int totalEvents = r.getCount();
		int otherEvents = this.nfa.getSize();// the first event of Kleene closure is included in other events
		int totalKleeneEvents = totalEvents - otherEvents;
		if(totalKleeneEvents > 0){
			int firstKleeneEventPosition = this.nfa.getKleeneClosureStatePosition() + 1;
			int numberOfEventsBeforeKleene = firstKleeneEventPosition;
			int numberOfEventsAfterKleene = this.nfa.getSize() - this.nfa.getKleeneClosureStatePosition() - 1;
			int lastKleeneEventPosition = totalEvents - numberOfEventsAfterKleene - 1;
			ArrayList<Integer> allEventIds = r.getEventIds();
			int[]eventIdsBeforeKleene = null;
			int[]eventIdsForKleene = null;
			int[]eventIdsAfterKleene = null;
			ArrayList<Boolean> allEventSafeFlags = null;
			boolean[]afterKleeneSafeFlags = null;
			if(!this.nfa.isSafe()){
				allEventSafeFlags = r.getEventSafeFlags();
			}
			//fill events before Kleene
			if(firstKleeneEventPosition > 0){
				eventIdsBeforeKleene = new int[numberOfEventsBeforeKleene];
				for(int i = 0; i < numberOfEventsBeforeKleene; i ++){
					eventIdsBeforeKleene[i] = allEventIds.get(i);
				}
			}
			//fill events after Kleene
			boolean afterKleeneSafeFlagValidateResult = true;
			if(lastKleeneEventPosition < totalEvents - 1){
				eventIdsAfterKleene = new int[numberOfEventsAfterKleene];
				afterKleeneSafeFlags = new boolean[numberOfEventsAfterKleene];
				for(int i =0 ; i < numberOfEventsAfterKleene; i ++){
					eventIdsAfterKleene[i] = allEventIds.get(lastKleeneEventPosition + 1 + i);
				}
				if(!this.nfa.isSafe()){
					for(int i =0 ; i < numberOfEventsAfterKleene; i ++){
						afterKleeneSafeFlags[i] = allEventSafeFlags.get(lastKleeneEventPosition + 1 + i);
					}
					afterKleeneSafeFlagValidateResult = this.validateSafeFlags(afterKleeneSafeFlags);
				}
			}
			//fill events for kleene
			eventIdsForKleene = new int[totalKleeneEvents];
			for(int i = 0; i < totalKleeneEvents; i ++){
				eventIdsForKleene[i] = allEventIds.get(i + firstKleeneEventPosition);
			}
			//dynamic programming part starts
			int totalInstanceNumbers = 0;
			ArrayList<EnumerationInstance> enumSpace;
			for(int i = 0; i < totalKleeneEvents; i ++){
				enumSpace = new ArrayList<EnumerationInstance>();
				EnumerationInstance enumInstance = new EnumerationInstance();
				enumInstance.addEvent(eventIdsForKleene[i]);
				
				enumInstance.setEventIdsBeforeKleene(eventIdsBeforeKleene);
				enumInstance.setEventIdsAfterKleene(eventIdsAfterKleene);
				if(!this.nfa.isSafe()){
					enumInstance.addFlag(allEventSafeFlags.get(firstKleeneEventPosition + i));
					enumInstance.setAfterKleeneSafeFlags(afterKleeneSafeFlags);
					enumInstance.setAfterKleeneSafeFlagValidateResult(afterKleeneSafeFlagValidateResult);
				}
				if(this.validateEnumInstance(enumInstance)){
					enumSpace.add(enumInstance);
					for(int j = i + 1; j < totalKleeneEvents; j ++){
						int size = enumSpace.size();
						for(int k = 0; k < size; k ++){
							EnumerationInstance existingEnumInstance = enumSpace.get(k);
							EnumerationInstance newEnumInstance = new EnumerationInstance(existingEnumInstance);
							newEnumInstance.addEvent(eventIdsForKleene[j]);
							if(!this.nfa.isSafe()){
								newEnumInstance.addFlag(allEventSafeFlags.get(firstKleeneEventPosition + j));
							}
							if(this.validateEnumInstance(newEnumInstance)){
								enumSpace.add(newEnumInstance);
							}
						}
					}
				}
				totalInstanceNumbers += enumSpace.size();
			}
			//dynamic programming part ends~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			//output the run without enumerated k+ events
			if(this.validateMatchUncertaintyInterval(eventIdsBeforeKleene, null, eventIdsAfterKleene)){
				if(this.nfa.isSafe()){
					this.outputMatchPostponeOptimized(eventIdsBeforeKleene, null, eventIdsAfterKleene);
				}else{
					
					if(afterKleeneSafeFlagValidateResult){
						this.outputMatchPostponeOptimized(eventIdsBeforeKleene, null, eventIdsAfterKleene);
					}else{
						boolean matchValidateResult;
						matchValidateResult = this.validateMatch(eventIdsBeforeKleene, null, eventIdsAfterKleene, 
								null,true, afterKleeneSafeFlags, afterKleeneSafeFlagValidateResult);
						if(matchValidateResult){
							this.outputMatchPostponeOptimized(eventIdsBeforeKleene, null, eventIdsAfterKleene);
						}
					}
				}
			}
		}else{
			boolean matchValidate = this.validateMatchWithoutEnumeration(r);
			if(matchValidate){
				this.outputMatch(new Match(r, this.nfa, this.buffer));
			}
		}
		//clean the cache for imprecise case
		this.cleanConfidenceSharingCache();
		//profiling
		Profiling.enumerationTime += System.nanoTime() - startTime;

		
	}
	
	/**
	 * this one follows the diagram in /dropbox/Research/2nd/codedesign/validateenumerationInstance.graffle
	 * @param eventIdsBeforeKleene
	 * @param enumInstance
	 * @param eventIdsAfterKleene
	 * @return
	 */
	public boolean validateEnumInstance(EnumerationInstance enumInstance){
		if(this.nfa.getkTypeForNFA() == KleeneClosurePredicateType.TrueFalseValueConsistent || 
				this.nfa.getkTypeForNFA() == KleeneClosurePredicateType.Inconsistent ||
				!ConfigFlags.usingDynamicProgrammingForPredicate){
			boolean uncertaintyIntervalResult = this.validateMatchUncertaintyIntervalForDP(enumInstance.getEventIdsBeforeKleene(), this.convertIntegerArrayListToArray(enumInstance.getIds()), enumInstance.getEventIdsAfterKleene());
			if(uncertaintyIntervalResult){
				Match tempMatch = new Match(this.tempRun, this.nfa, this.buffer);
				double confidence = this.computeConfidence(tempMatch);
				if(confidence >= ConfigFlags.confidenceThreshold){
					if(this.nfa.getkTypeForNFA() == KleeneClosurePredicateType.TrueFalseValueConsistent){
						this.outputMatch(tempMatch, confidence);
					}else{
						boolean predicateResult = this.validatePredicateForDP(enumInstance);
						if(predicateResult){
							this.outputMatch(tempMatch,confidence);
						}
					}
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			if(this.nfa.getkTypeForNFA() == KleeneClosurePredicateType.TrueValueConsistent){// true type
				boolean predicateResult = this.validatePredicateForDP(enumInstance);
				if(predicateResult){
					boolean uncertaintyIntervalResult = this.validateMatchUncertaintyIntervalForDP(enumInstance.getEventIdsBeforeKleene(), this.convertIntegerArrayListToArray(enumInstance.getIds()), enumInstance.getEventIdsAfterKleene());
					if(uncertaintyIntervalResult){
						Match tempMatch = new Match(this.tempRun, this.nfa, this.buffer);
						double confidence = this.computeConfidence(tempMatch);
						if(confidence > ConfigFlags.confidenceThreshold){
							this.outputMatch(tempMatch, confidence);
							return true;
						}else{
							return false;
						}
					}else{
						return false;
					}
				}else{
					Profiling.numberOfDiscardByDPPredicate ++;
					return false;
				}
			}else if(this.nfa.getkTypeForNFA() == KleeneClosurePredicateType.FalseValueConsistent){// false type
				if((enumInstance.getCountForEvents() == 1) || (enumInstance.getCountForEvents() > 1) && !enumInstance.isPreviousEvaluationResult()){
					boolean predicateResult = this.validatePredicateForDP(enumInstance);
					if(predicateResult){
						enumInstance.setPreviousEvaluationResult(true);
					}else{
						enumInstance.setPreviousEvaluationResult(false);
					}
				}else{
					Profiling.numberofskipByDPPredicate ++;
				}
				boolean uncertaintyIntervalResult = this.validateMatchUncertaintyIntervalForDP(enumInstance.getEventIdsBeforeKleene(), this.convertIntegerArrayListToArray(enumInstance.getIds()), enumInstance.getEventIdsAfterKleene());
				if(uncertaintyIntervalResult){
					Match tempMatch = new Match(this.tempRun, this.nfa, this.buffer);
					double confidence = this.computeConfidence(tempMatch);
					if(confidence > ConfigFlags.confidenceThreshold){
						if(enumInstance.isPreviousEvaluationResult()){
							this.outputMatch(tempMatch, confidence);
						}
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean validateMatchWithoutEnumeration(Run r){
	if(!r.validateUncertaintyInterval(this.buffer)){//??
		return false;
	}
	boolean output = false;
	if(ConfigFlags.usingZstream){
		if(this.validateRunTimestamp(r)){
			output = true;
		}else{
			output = false;
		}
	}else{
		output = true;
	}
	
	if(output){
		boolean safe;
		Run newRun = this.uncertainEngineRunController.getRun();
		newRun.initializeRun(nfa);
		for(int i = 0; i < r.getCount(); i ++){
			int id = r.getEventIds().get(i);
			Event e = this.buffer.getEvent(id);
			safe = r.getEventSafeFlags().get(i);
			if(safe){
				int oldState = newRun.getCurrentState();
				
				newRun.addEvent(e);
				int newState = newRun.getCurrentState();
				
				if(oldState == newState && !newRun.isFull()){
					newRun.proceed();
				}
				
				
			}else{
				boolean evaluateResult = this.postponeCheckPredicate(e, newRun);
				if(evaluateResult){
					int oldState = newRun.getCurrentState();
					
					
					newRun.addEvent(e);
					
					int newState = newRun.getCurrentState();
					
					if(oldState == newState && !newRun.isFull()){
						newRun.proceed();
					}
					
				}else{
					return false;
				}
			}
		}
		
		
		return true;
	
	}else{
		return false;
	}
	
	}

	/**
	 * Enumerate matches for postponing algorithm
	 * Here the baseline is comparing with dp
	 * @param r
	 */
		public void enumerateMatchesForRunWithoutDP(Run r){
			long startTime = System.nanoTime();
			Profiling.numberOfRunsReachedMatchPostponeOptimized ++;
			Profiling.numberOfEventsInRunsReachedMatch += r.getCount();
			if(r.getCount() > Profiling.maxNumberOfEventsInRunsReachedMatch){
				Profiling.maxNumberOfEventsInRunsReachedMatch = r.getCount();
			}
			if(ConfigFlags.usingCollapsedFormatResult){
				this.outputMatch(new Match(r, this.nfa, this.buffer));
				Profiling.numberOfCollapsedMatches ++;
				return;
			}
			//main part
			int totalEvents = r.getCount();
			int otherEvents = this.nfa.getSize();// the first event of Kleene closure is included in other events
			int totalKleeneEvents = totalEvents - otherEvents;
			if(totalKleeneEvents > 0){
				int firstKleeneEventPosition = this.nfa.getKleeneClosureStatePosition() + 1;
				int numberOfEventsBeforeKleene = firstKleeneEventPosition;
				int numberOfEventsAfterKleene = this.nfa.getSize() - this.nfa.getKleeneClosureStatePosition() - 1;
				int lastKleeneEventPosition = totalEvents - numberOfEventsAfterKleene - 1;
				ArrayList<Integer> allEventIds = r.getEventIds();
				int[]eventIdsBeforeKleene = null;
				int[]eventIdsAfterKleene = null;
				ArrayList<Boolean> allEventSafeFlags = null;
				boolean[]afterKleeneSafeFlags = null;
				if(!this.nfa.isSafe()){
					allEventSafeFlags = r.getEventSafeFlags();
				}
				//fill events before Kleene
				if(firstKleeneEventPosition > 0){
					eventIdsBeforeKleene = new int[numberOfEventsBeforeKleene];
					for(int i = 0; i < numberOfEventsBeforeKleene; i ++){
						eventIdsBeforeKleene[i] = allEventIds.get(i);
					}
				}
				//fill events after Kleene
				if(lastKleeneEventPosition < totalEvents - 1){
					eventIdsAfterKleene = new int[numberOfEventsAfterKleene];
					afterKleeneSafeFlags = new boolean[numberOfEventsAfterKleene];
					for(int i =0 ; i < numberOfEventsAfterKleene; i ++){
						eventIdsAfterKleene[i] = allEventIds.get(lastKleeneEventPosition + 1 + i);
					}
					if(!this.nfa.isSafe()){
						for(int i =0 ; i < numberOfEventsAfterKleene; i ++){
							afterKleeneSafeFlags[i] = allEventSafeFlags.get(lastKleeneEventPosition + 1 + i);
						}
					}
				}
				//enumeration
				long onTime = System.nanoTime();
				for(int i = 1; i <= totalKleeneEvents; i ++){
					long time1 = System.nanoTime();
					int[][] combinations = EnumerationTool.getCombinations(i, totalKleeneEvents);//no enumeration for the first
					Profiling.numberOfEnumerations += combinations.length;
					Profiling.timeOnGetCombination +=(System.nanoTime() - time1);
					for(int j = 0; j < combinations.length; j++){
						long time2 = System.nanoTime();
						int[] kleeneEventIds  = new int[i];
						//fill eventIds for Kleene
						for(int k = 0; k < i; k++){
							kleeneEventIds[k] = allEventIds.get(firstKleeneEventPosition + combinations[j][k]);
						}
						Profiling.timeOnFillEvent +=(System.nanoTime() - time2);
						long time3 = System.nanoTime();
						if(this.validateMatchUncertaintyInterval(eventIdsBeforeKleene, kleeneEventIds, eventIdsAfterKleene)){
							if(this.nfa.isSafe()){
								this.outputMatchPostponeOptimized(eventIdsBeforeKleene, kleeneEventIds, eventIdsAfterKleene);
							}else{
								boolean[] kleeneEventSafeFlags = new boolean[i];
								for(int k = 0; k < i; k++){
									kleeneEventSafeFlags[k] = allEventSafeFlags.get(firstKleeneEventPosition + combinations[j][k]);
								}
								boolean kleeneSafeFlagValidateResult = this.validateSafeFlags(kleeneEventSafeFlags);
								boolean afterKleeneSafeFlagValidateResult = this.validateSafeFlags(afterKleeneSafeFlags);
								
								if(kleeneSafeFlagValidateResult && afterKleeneSafeFlagValidateResult){
									this.outputMatchPostponeOptimized(eventIdsBeforeKleene, kleeneEventIds, eventIdsAfterKleene);
								}else{
									boolean matchValidateResult;
									
									matchValidateResult = this.validateMatch(eventIdsBeforeKleene, kleeneEventIds, eventIdsAfterKleene, 
											kleeneEventSafeFlags,kleeneSafeFlagValidateResult, afterKleeneSafeFlags, afterKleeneSafeFlagValidateResult);
									
									if(matchValidateResult){
										this.outputMatchPostponeOptimized(eventIdsBeforeKleene, kleeneEventIds, eventIdsAfterKleene);
									}
								}
							}
						}
						
						Profiling.timeOnValidate +=(System.nanoTime() - time3);
					}
				}
				Profiling.timeOnEnumeration += (System.nanoTime() - onTime);
				//output the run without enumerated k+ events
				if(this.validateMatchUncertaintyInterval(eventIdsBeforeKleene, null, eventIdsAfterKleene)){
					if(this.nfa.isSafe()){
						this.outputMatchPostponeOptimized(eventIdsBeforeKleene, null, eventIdsAfterKleene);
					}else{
						boolean afterKleeneSafeFlagValidateResult = this.validateSafeFlags(afterKleeneSafeFlags);
						if(afterKleeneSafeFlagValidateResult){
							this.outputMatchPostponeOptimized(eventIdsBeforeKleene, null, eventIdsAfterKleene);
						}else{
							boolean matchValidateResult;
							matchValidateResult = this.validateMatch(eventIdsBeforeKleene, null, eventIdsAfterKleene, 
									null,true, afterKleeneSafeFlags, afterKleeneSafeFlagValidateResult);
							if(matchValidateResult){
								this.outputMatchPostponeOptimized(eventIdsBeforeKleene, null, eventIdsAfterKleene);
							}
						}
					}
				}
				
			}else{
				boolean matchValidate = this.validateMatchWithoutEnumeration(r);
				if(matchValidate){
					this.outputMatch(new Match(r, this.nfa, this.buffer));
				}
			}
			//clean the cache for imprecise case
			this.cleanConfidenceSharingCache();
			//profiling
			Profiling.enumerationTime += System.nanoTime() - startTime;
		}

		
}
