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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.jeval.EvaluationException;


import edu.umass.cs.sase.query.Edge;
import edu.umass.cs.sase.query.KleeneClosurePredicateType;
import edu.umass.cs.sase.query.NFA;
import edu.umass.cs.sase.query.State;
import edu.umass.cs.sase.stream.Event;
import edu.umass.cs.sase.stream.Stream;


/**
 * This is the processing engine.
 * @author haopeng
 */
public class Engine {
	public enum EdgeEvaluationResult{
		Safe, Unsafe, Discard;
	}
	/**
	 * The input stream
	 */
	Stream input;
	/**
	 * The event buffer
	 */
	EventBuffer buffer;
	/**
	 * The nfa representing the query
	 */
	NFA nfa;
	/**
	 * The run pool, which is used to reuse the run data structure.
	 */
	RunPool engineRunController;
	/**
	 * The active runs in memory
	 */
	ArrayList<Run> activeRuns;
	
	HashMap<Integer, ArrayList<Run>> activeRunsByPartition;
	/**
	 * The runs which can be removed from the active runs.
	 */
	ArrayList<Run> toDeleteRuns;	
	/**
	 * The matches
	 */
	MatchController matches;
	
	/**
	 * The buffered events for the negation components
	 */
	ArrayList<Event> negationEvents;
	
	HashMap<Integer, ArrayList<Event>> negationEventsByPartition;
	
	/**
	 * For the zstream
	 */
	EventStacks eventStacks;
	
	/**
	 * For the new ZStream engine implementation
	 */
	EventStackGroup eventStackGroup;
	/**
	 * The default constructor.
	 */
	
	/**
	 * used for postpoing alogrithm
	 */
	Run runForEnumeration;
	
	Run tempRun;

	/**
	 * Used for run iteration
	 */
	int runIteratorCounter;
	
	
	
	public Engine(){
		buffer = new EventBuffer();
		engineRunController = new RunPool();
		this.activeRuns = new ArrayList<Run>();		
		this.toDeleteRuns = new ArrayList<Run>();
		this.matches = new MatchController();
		Profiling.resetProfiling();	
	
	}
	/**
	 * This method initializes the engine.
	 */
	
	public void initialize(){
		input = null;
		buffer = new EventBuffer();
		engineRunController = new RunPool();
		this.activeRuns = new ArrayList<Run>();
		this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
		this.toDeleteRuns = new ArrayList<Run>();
		this.matches = new MatchController();
		Profiling.resetProfiling();	
	
	}
	/**
	 * This method is used to warm up the engine.
	 * @throws CloneNotSupportedException
	 * @throws EvaluationException
	 */
	public void warmUp() throws CloneNotSupportedException, EvaluationException{
		this.runEngine();
		buffer = new EventBuffer();
		engineRunController = new RunPool();
		this.activeRuns = new ArrayList<Run>();
		this.toDeleteRuns = new ArrayList<Run>();
		this.matches = new MatchController();
		Profiling.resetProfiling();
		
		
	}
	
	/**
	 * This is the main run logic method
	 * 
	 */
	
	public void runEngine() throws CloneNotSupportedException, EvaluationException{
		ConfigFlags.timeWindow = this.nfa.getTimeWindow();
		ConfigFlags.sequenceLength = this.nfa.getSize();
		ConfigFlags.selectionStrategy = this.nfa.getSelectionStrategy();
		ConfigFlags.hasPartitionAttribute = this.nfa.isHasPartitionAttribute();
		ConfigFlags.hasNegation = this.nfa.isHasNegation();
		if(ConfigFlags.usingZstream){
			this.eventStacks = new EventStacks(this.nfa);
			this.eventStackGroup = new EventStackGroup(this.nfa);
			if(ConfigFlags.usingPostponingOptimization){
				this.runZstreamEnginePostponeOptimized();
			}else{
				//this.runZstreamEngine();
				this.runNewZstreamEngine();
			}
			
			
		}else if(ConfigFlags.hasNegation){
			this.runNegationEngine();
		}else if(ConfigFlags.selectionStrategy.equalsIgnoreCase("skip-till-any-match")){
			if(this.nfa.isHasKleeneClosure() && ConfigFlags.usingPostponingOptimization){
				this.runSkipTillAnyEnginePostponeOptimized();
			}else{
				System.out.println("no postpone");
				//System.exit(0);
				
				this.runSkipTillAnyEngine();
			}
			
			
		}else if(ConfigFlags.selectionStrategy.equalsIgnoreCase("skip-till-next-match")){
			this.runSkipTillNextEngine();
		}else if(ConfigFlags.selectionStrategy.equalsIgnoreCase("partition-contiguity")){
			this.runPartitionContiguityEngine();
		}
		
		//System.out.println("Buffered events:" + buffer.getSize());
		Profiling.bufferedEvents = buffer.getSize();
	}

	/*
	 * Baseline + ZStream (newer implementation)
	 * 
	 */
	public void runNewZstreamEngine() throws CloneNotSupportedException, EvaluationException{
		if(!ConfigFlags.hasPartitionAttribute){
			
		}

		
		if(ConfigFlags.hasPartitionAttribute){
			ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
			this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
			Event e = null;
			long currentTime = 0;
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				
				long time1 = System.nanoTime();
				this.eventStackGroup.checkEvent(e);
				
				if(this.eventStackGroup.isAllFull()){
					this.eventStackGroup.removeExpiredEvent(this.activeRunsByPartition, e);//change here
				}
				Profiling.timeOnBufferingEvents += (System.nanoTime() - time1);
				if(this.eventStackGroup.isAllFull()){
					this.processEventsFromStackGroup();
				}
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
				
			}
			//flush events in stackGroup
			currentTime = System.nanoTime();
			this.processEventsFromStackGroup();
			Profiling.totalRunTime += (System.nanoTime() - currentTime);
			
		}
	}
	
	public void processEventsFromStackGroup() throws EvaluationException, CloneNotSupportedException{

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
					this.createNewRunByPartitionForZstream(singleEvent, edgeTag);
					Profiling.timeOnCreateNewRunBaseline += (System.nanoTime() - time1);
				}else{
				
					long time2 = System.nanoTime();
					//old
					//this.evaluateRunsByPartitionForSkipTillAnyForZstream(singleEvent, edgeTag);
					
					//new
					this.evaluateRunsByPartitionForSkipTillAnyForZstreamCleanIncluded(singleEvent, edgeTag);
					
					
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
	/*
	 * Baseline + ZStream
	 * 
	 */
	
	public void runOldZstreamEngine() throws CloneNotSupportedException, EvaluationException{
		if(!ConfigFlags.hasPartitionAttribute){
			
		}
		
		if(ConfigFlags.hasPartitionAttribute){
			ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
			this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
			Event e = null;
			long currentTime = 0;
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				long time1 = System.nanoTime();
				this.eventStacks.checkEvent(e); // list what it does
				
				/*
				boolean typeCheck;
				for(int i = 0; i < this.nfa.getSize(); i ++){
					typeCheck = this.nfa.getStates(i).stackTypeCheck(e);
					if(typeCheck){
						this.eventStacks.putEvent(e, i);
					}
				}
				*/
				
				this.eventStacks.removeExpiredEvent();
				Profiling.timeOnBufferingEvents +=(System.nanoTime() - time1);
				// check if it is full
				
					
					if(this.eventStacks.isAllFull()){
						Event event;
						for(int i = 0; i < this.nfa.getSize(); i ++){
							while(this.eventStacks.hasMoreEvent(i)){
								event = this.eventStacks.popEvent(i);
								if(event != null){
									//change
									long time2 = System.nanoTime();
									this.evaluateRunsByPartitionForSkipTillAny(event);// evaluate existing runs
									Profiling.timeOnEvluateRunsBaseline +=(System.nanoTime() - time2);
									if(this.toDeleteRuns.size() > 0){
										long time3 = System.nanoTime();
										this.cleanRunsByPartition();
										Profiling.timeOnCleanRunsBaseline +=(System.nanoTime() - time3);
									}
									// change
									long time4 = System.nanoTime();
									this.createNewRunByPartition(event);// create new run starting with this event if possible
									Profiling.timeOnCreateNewRunBaseline +=(System.nanoTime() - time4);
								}
								
							}
						}
					}
					
					
					
				
				
				
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
				
			}
		}
	}

	/*
	 * Baseline + ZStream + Postpone
	 * Todo: chagne it!
	 */
	public void runZstreamEnginePostponeOptimized() throws CloneNotSupportedException, EvaluationException{
		if(!ConfigFlags.hasPartitionAttribute){
		}
		if(ConfigFlags.hasPartitionAttribute){
			ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
			this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
			Event e = null;
			long currentTime = 0;
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				this.eventStacks.checkEvent(e);
				// check if it is full
				if(this.eventStacks.isAllFull()){
					//full, evaluate
					this.eventStacks.removeExpiredEvent();
					if(this.eventStacks.isAllFull()){
						Event event;
						for(int i = 0; i < this.nfa.getSize(); i ++){
							while(this.eventStacks.hasMoreEvent(i)){
								event = this.eventStacks.popEvent(i);
								if(event != null){
									this.evaluateRunsByPartitionForSkipTillAnyPostponeOptimized(event);
									if(this.toDeleteRuns.size() > 0){
										this.cleanRunsByPartition();
									}
									this.createNewRunByPartitionPostponeOptimized(event);
								}
							}
						}
					}
				}
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
			}
		}
	}
	
	/**
	 * Baseline + postponing
	 * The main method for skip-till-any-match
	 * @throws CloneNotSupportedException
	 * @throws EvaluationException
	 */
	public void runSkipTillAnyEnginePostponeOptimized() throws CloneNotSupportedException, EvaluationException{
		if(!ConfigFlags.hasPartitionAttribute){
			//ignore here!
			Event e = null;
			long currentTime = 0;
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				this.evaluateRunsForSkipTillAnyPostponeOptimized(e);// evaluate existing runs
				if(this.toDeleteRuns.size() > 0){
					this.cleanRuns();
				}
				this.createNewRun(e);// create new run starting with this event if possible
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
			}
		}
		if(ConfigFlags.hasPartitionAttribute){
			ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
			this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
			Event e = null;
			long currentTime = 0;
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				long time1 = System.nanoTime();
				
				this.evaluateRunsByPartitionForSkipTillAnyPostponeOptimized(e);// evaluate existing runs
				//to do?
				
				Profiling.timeOnEvluateRunsBaseline +=(System.nanoTime() - time1);
				long time2 = System.nanoTime();
				if(this.toDeleteRuns.size() > 0){
					this.cleanRunsByPartition();
				}
				Profiling.timeOnCleanRunsBaseline +=(System.nanoTime() - time2);
				long time3 = System.nanoTime();
				this.createNewRunByPartitionPostponeOptimized(e);// create new run starting with this event if possible
				Profiling.timeOnCreateNewRunBaseline += (System.nanoTime() - time3);
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
			}
		}
	}
	
	/**
	 * Baseline
	 * The main method for skip-till-any-match
	 * @throws CloneNotSupportedException
	 * @throws EvaluationException
	 */
	public void runSkipTillAnyEngine() throws CloneNotSupportedException, EvaluationException{
		if(!ConfigFlags.hasPartitionAttribute){
			Event e = null;
			long currentTime = 0;
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				this.evaluateRunsForSkipTillAny(e);// evaluate existing runs
				if(this.toDeleteRuns.size() > 0){
					this.cleanRuns();
				}
				this.createNewRun(e);// create new run starting with this event if possible
				
				
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
				
			}
		}
		
		if(ConfigFlags.hasPartitionAttribute){
			ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
			this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
			Event e = null;
			long currentTime = 0;
			
			
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				
				long time1 = System.nanoTime();
				//old
				//this.evaluateRunsByPartitionForSkipTillAny(e);// evaluate existing runs
				//new
				this.evaluateRunsByPartitionForSkipTillAnyCleanIncluded(e);
				
				Profiling.timeOnEvluateRunsBaseline += (System.nanoTime() - time1);
				
				long time2 = System.nanoTime();
				if(this.toDeleteRuns.size() > 0){
					this.cleanRunsByPartition();
				}
				Profiling.timeOnCleanRunsBaseline += (System.nanoTime() - time2);
				
				long time3 = System.nanoTime();
				this.createNewRunByPartition(e);// create new run starting with this event if possible
				Profiling.timeOnCreateNewRunBaseline += (System.nanoTime() - time3);
				
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
				
			}
		}
	}
	/**
	 * The main method for skip-till-next-match
	 * @throws CloneNotSupportedException
	 * @throws EvaluationException
	 */
	public void runSkipTillNextEngine() throws CloneNotSupportedException, EvaluationException{
		if(!ConfigFlags.hasPartitionAttribute){
			Event e = null;
			long currentTime = 0;
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				this.evaluateRunsForSkipTillNext(e);// evaluate existing runs
				if(this.toDeleteRuns.size() > 0){
					this.cleanRuns();
				}
				this.createNewRun(e);// create new run starting with this event if possible
				
				
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
				
			}
		}
		
		if(ConfigFlags.hasPartitionAttribute){
			ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
			this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
			Event e = null;
			long currentTime = 0;
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				this.evaluateRunsByPartitionForSkipTillNext(e);// evaluate existing runs
				if(this.toDeleteRuns.size() > 0){
					this.cleanRunsByPartition();
				}
				this.createNewRunByPartition(e);// create new run starting with this event if possible
				
				
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
				
			}
		}
	}
	
	/**
	 * This method is used for simulation
	 * @param e
	 * @throws CloneNotSupportedException 
	 * @throws EvaluationException 
	 */
	public void runSimulationEngineForEvent(Event e) throws CloneNotSupportedException, EvaluationException {
		if(!ConfigFlags.hasPartitionAttribute){
			this.evaluateRunsForSkipTillNext(e);// evaluate existing runs
			if(this.toDeleteRuns.size() > 0){
				this.cleanRuns();
			}
			this.createNewRun(e);// create new run starting with this event if possible
		} else{
			ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
			this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
			this.evaluateRunsByPartitionForSkipTillNext(e);// evaluate existing runs
			if(this.toDeleteRuns.size() > 0){
				this.cleanRunsByPartition();
			}
			this.createNewRunByPartition(e);// create new run starting with this event if possible
		}
	}
	
	/**
	 * This method is called when the query uses the partition-contiguity selection strategy
	 * @throws CloneNotSupportedException 
	 *  
	 */
	
	public void runPartitionContiguityEngine() throws EvaluationException, CloneNotSupportedException{
		
		ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
		ConfigFlags.hasPartitionAttribute = true;
		this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
		
		ConfigFlags.timeWindow = this.nfa.getTimeWindow();
		ConfigFlags.sequenceLength = this.nfa.getSize();
		ConfigFlags.selectionStrategy = this.nfa.getSelectionStrategy();
		
		Event e = null;
		long currentTime = 0;
		while((e = this.input.popEvent())!= null){
			
			currentTime = System.nanoTime();
			this.evaluateRunsForPartitionContiguity(e);
			if(this.toDeleteRuns.size() > 0){
				this.cleanRunsByPartition();
			}
			this.createNewRunByPartition(e);
			Profiling.totalRunTime += (System.nanoTime() - currentTime);
			Profiling.numberOfEvents += 1;
		}
	}
	/**
	 * The main method when there is a negation component in the query
	 * @throws CloneNotSupportedException
	 * @throws EvaluationException
	 */
	public void runNegationEngine() throws CloneNotSupportedException, EvaluationException{
		if(!ConfigFlags.hasPartitionAttribute){
			Event e = null;
			long currentTime = 0;
			this.negationEvents = new ArrayList<Event>();
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				if(this.checkNegation(e)){
					this.negationEvents.add(e);
				}else{
					this.evaluateRunsForNegation(e);// evaluate existing runs
					if(this.toDeleteRuns.size() > 0){
						this.cleanRuns();
					}
					this.createNewRun(e);// create new run starting with this event if possible
				}
				
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
				
			}
		}
		
		if(ConfigFlags.hasPartitionAttribute){
			ConfigFlags.partitionAttribute = this.nfa.getPartitionAttribute();
			this.activeRunsByPartition = new HashMap<Integer, ArrayList<Run>>();
			this.negationEventsByPartition = new HashMap<Integer, ArrayList<Event>>();
			
			Event e = null;
			long currentTime = 0;
			while ((e = this.input.popEvent())!= null){// evaluate event one by one
				currentTime = System.nanoTime();
				if(this.checkNegation(e)){
					this.indexNegationByPartition(e);
				}else{
					this.evaluateRunsByPartitionForNegation(e);// evaluate existing runs
					if(this.toDeleteRuns.size() > 0){
						this.cleanRunsByPartition();
					}
					this.createNewRunByPartition(e);// create new run starting with this event if possible
				}
				
				Profiling.totalRunTime += (System.nanoTime() - currentTime);
				Profiling.numberOfEvents += 1;
				
			}
		}
	}
	/**
	 * This method will iterate all existing runs for the current event, for skip-till-any-match.
	 * @param e The current event which is being evaluated.
	 * @throws CloneNotSupportedException
	 * @throws EvaluationException
	 */
		
		public void evaluateRunsForSkipTillAny(Event e) throws CloneNotSupportedException, EvaluationException{
			int size = this.activeRuns.size();
			for(int i = 0; i < size; i ++){
				Run r = this.activeRuns.get(i);
					if(r.isFull()){
						continue;
					}
					this.evaluateEventForSkipTillAny(e, r);
				}
		}
		
		public void evaluateRunsForSkipTillAnyPostponeOptimized(Event e) throws CloneNotSupportedException, EvaluationException{
			int size = this.activeRuns.size();
			for(int i = 0; i < size; i ++){
				Run r = this.activeRuns.get(i);
					if(r.isFull()){
						continue;
					}
					this.evaluateEventPostponeOptimizedForSkipTillAny(e, r);
				}
		}
		
		/**
		 * This method will iterate all existing runs for the current event, for skip-till-next-match.
		 * @param e The current event which is being evaluated.
		 * @throws CloneNotSupportedException
		 * @throws EvaluationException
		 */
			
			public void evaluateRunsForSkipTillNext(Event e) throws CloneNotSupportedException, EvaluationException{
				int size = this.activeRuns.size();
				for(int i = 0; i < size; i ++){
					Run r = this.activeRuns.get(i);
						if(r.isFull()){
							continue;
						}
						this.evaluateEventForSkipTillNext(e, r);
					}
			}
			/**
			 * This method will iterate all existing runs for the current event, for queries with a negation component.
			 * @param e The current event which is being evaluated.
			 * @throws CloneNotSupportedException
			 * @throws EvaluationException
			 */
			public void evaluateRunsForNegation(Event e) throws CloneNotSupportedException, EvaluationException{
				int size = this.activeRuns.size();
				for(int i = 0; i < size; i ++){
					Run r = this.activeRuns.get(i);
						if(r.isFull()){
							continue;
						}
						this.evaluateEventForNegation(e, r);
					}
			}

	
		
	/**
	 * This method will iterate runs in the same partition for the current event, for skip-till-any-match
	 * @param e The current event which is being evaluated.
	 * @throws CloneNotSupportedException
	 */
		public void evaluateRunsByPartitionForSkipTillAny(Event e) throws CloneNotSupportedException{
			int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
			if(this.activeRunsByPartition.containsKey(key)){
				ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
				int size = partitionedRuns.size();
				for(int i = 0; i < size; i ++){
					Run r = partitionedRuns.get(i);
					if(r.isFull()){
						continue;
					}
					this.evaluateEventOptimizedForSkipTillAny(e, r);//
				}
			}
		}
	

		
		/**
		 * Optimization of cleaning.
		 * This method will iterate runs in the same partition for the current event, for skip-till-any-match
		 * @param e The current event which is being evaluated.
		 * @throws CloneNotSupportedException
		 */
			public void evaluateRunsByPartitionForSkipTillAnyCleanIncluded(Event e) throws CloneNotSupportedException{
				int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
				if(this.activeRunsByPartition.containsKey(key)){
					ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
					int count = 0;
					this.runIteratorCounter = 0;
					int size = partitionedRuns.size();
					while(count < size){
						Run r = partitionedRuns.get(this.runIteratorCounter);
						if(!r.isFull()){
							this.evaluateEventOptimizedForSkipTillAnyCleanIncluded(e, r, partitionedRuns);//
						}
						count ++;
					}
				}
			}
		
		
		/**
		 * Special version for ZStream
		 * @param e
		 * @throws CloneNotSupportedException
		 */
		public void evaluateRunsByPartitionForSkipTillAnyForZstream(Event e, String edgeTag) throws CloneNotSupportedException{
			int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
			if(this.activeRunsByPartition.containsKey(key)){
				ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
				int size = partitionedRuns.size();
				for(int i = 0; i < size; i ++){
					Run r = partitionedRuns.get(i);
					if(r.isFull()){
						continue;
					}
					this.evaluateEventOptimizedForSkipTillAnyZstream(e, r, edgeTag);//
				}
			}
		}
		
		/**
		 * Special version for ZStream, optimzed run cleaning
		 * @param e
		 * @throws CloneNotSupportedException
		 */
		public void evaluateRunsByPartitionForSkipTillAnyForZstreamCleanIncluded(Event e, String edgeTag) throws CloneNotSupportedException{
			
			int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
			if(this.activeRunsByPartition.containsKey(key)){
				ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
				int count = 0;
				this.runIteratorCounter = 0;
				int size = partitionedRuns.size();
				while(count < size){
					Run r = partitionedRuns.get(this.runIteratorCounter);
					if(!r.isFull()){
						this.evaluateEventOptimizedForSkipTillAnyZstreamCleanIncluded(e, r, edgeTag, partitionedRuns);//
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
		public void evaluateRunsByPartitionForSkipTillAnyPostponeOptimized(Event e) throws CloneNotSupportedException, EvaluationException{
			//modify here
			//
			int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
			if(this.activeRunsByPartition.containsKey(key)){
				//
				ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
				int size = partitionedRuns.size();
				for(int i = 0; i < size; i ++){
					Run r = partitionedRuns.get(i);
					if(r.isFull()){
						continue;
					}
					this.evaluateEventPostponeOptimizedForSkipTillAny(e, r);//
				}
			}
		}

		/**
		 * special version, including run cleaning
		 * One event vs. multiple runs
		 * @param e
		 * @throws CloneNotSupportedException
		 * @throws EvaluationException 
		 */
		public void evaluateRunsByPartitionForSkipTillAnyPostponeOptimizedCleanIncluded(Event e) throws CloneNotSupportedException, EvaluationException{
			int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
			if(this.activeRunsByPartition.containsKey(key)){
				ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
				int size = partitionedRuns.size();
				int count = 0;
				this.runIteratorCounter = 0;
				while(count < size){
					Run r = partitionedRuns.get(this.runIteratorCounter);
					if(!r.isFull()){
						this.evaluateEventPostponeOptimizedForSkipTillAnyCleanIncluded(e, r, partitionedRuns);//
					}
					count ++;
				}
			}
		}

		/**
		 * This method will iterate runs in the same partition for the current event, for skip-till-next-match
		 * @param e The current event which is being evaluated.
		 * @throws CloneNotSupportedException
		 */
			public void evaluateRunsByPartitionForSkipTillNext(Event e) throws CloneNotSupportedException{
				int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
				if(this.activeRunsByPartition.containsKey(key)){
					ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
					int size = partitionedRuns.size();
					for(int i = 0; i < size; i ++){
						Run r = partitionedRuns.get(i);
						if(r.isFull()){
							continue;
						}
						this.evaluateEventOptimizedForSkipTillNext(e, r);//
					}
				}
			}
			/**
			 * This method will iterate runs in the same partition for the current event, for queries with a negation component.
			 * @param e The current event which is being evaluated.
			 * @throws CloneNotSupportedException
			 */
			public void evaluateRunsByPartitionForNegation(Event e) throws CloneNotSupportedException{
				int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
				if(this.activeRunsByPartition.containsKey(key)){
					ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
					int size = partitionedRuns.size();
					for(int i = 0; i < size; i ++){
						Run r = partitionedRuns.get(i);
						if(r.isFull()){
							continue;
						}
						this.evaluateEventOptimizedForNegation(e, r);//
					}
				}
			}

	/**
	 * If the selection strategy is partition-contiguity, this method is called and it will iterate runs in the same partition for the current event
	 * @param e The current event which is being evaluated.
	 * @throws CloneNotSupportedException
	 */
	public void evaluateRunsForPartitionContiguity(Event e) throws CloneNotSupportedException{
		int key = e.getAttributeByName(ConfigFlags.partitionAttribute);
		if(this.activeRunsByPartition.containsKey(key)){
			ArrayList<Run> partitionedRuns = this.activeRunsByPartition.get(key);
			int size = partitionedRuns.size();
			for(int i = 0; i < size; i ++){
				Run r = partitionedRuns.get(i);
				if(r.isFull()){
					continue;
				}
				this.evaluateEventForPartitonContiguityOptimized(e, r);//
			}
		}
	}
	
	/**
	 * ZStream, avoid repeated predicate checking!
	 * 
	 * This method evaluates the event for a given run, for skip-till-any-match
	 * @param e The current event which is being evaluated.
	 * @param r The run against which the evaluation goes
	 * @throws CloneNotSupportedException 
	 */
	public void evaluateEventOptimizedForSkipTillAnyZstream(Event e, Run r) throws CloneNotSupportedException{
		// 
		//add timestamp comparison here, timestamp must be larger than previous one
		boolean timeValidity = this.checkTimeValidity(e, r);
		if(timeValidity){
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
				this.toDeleteRuns.add(r);
			}
		}
		

	}

	/**
	 * This method evaluates the event for a given run, for skip-till-any-match
	 * @param e The current event which is being evaluated.
	 * @param r The run against which the evaluation goes
	 * @throws CloneNotSupportedException 
	 */
	public void evaluateEventOptimizedForSkipTillAny(Event e, Run r) throws CloneNotSupportedException{
		
		//add timestamp comparison here, timestamp must be larger than previous one
		boolean timeValidity = this.checkTimeValidity(e, r);
		if(timeValidity){
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
				
					this.toDeleteRuns.add(r);//trick
				
				
			}
		}
		

	}


	/**
	 * Special version of cleaning optmization
	 * This method evaluates the event for a given run, for skip-till-any-match
	 * @param e The current event which is being evaluated.
	 * @param r The run against which the evaluation goes
	 * @throws CloneNotSupportedException 
	 */
	public void evaluateEventOptimizedForSkipTillAnyCleanIncluded(Event e, Run r, ArrayList<Run> partitionedRuns) throws CloneNotSupportedException{
			boolean timewindowResult = this.checkTimeWindow(e, r);
			
			if(timewindowResult){
				this.runIteratorCounter ++;
				int checkResult = this.checkPredicateOptimized(e, r);
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
				partitionedRuns.remove(this.runIteratorCounter);
			}
		}

	
	/**
	 * Optimized for ZStream
	 * @param e
	 * @param r
	 * @throws CloneNotSupportedException
	 */
	public void evaluateEventOptimizedForSkipTillAnyZstream(Event e, Run r, String edgeTag) throws CloneNotSupportedException{
		// ToDo

		boolean timeValidity = this.checkTimeValidity(e, r);
		if(timeValidity){
			boolean timewindowResult = this.checkTimeWindow(e, r);
			
			if(timewindowResult){
				//change it for ZStream
				int checkResult = this.checkPredicateOptimizedForZstream(e, r, edgeTag);
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
				
				
				if(this.nfa.getLastEdgeTag().equalsIgnoreCase(edgeTag)){
					//only last state to filter runs
					this.toDeleteRuns.add(r);
				}
				
			}
		}
	}

	public void evaluateEventOptimizedForSkipTillAnyZstreamCleanIncluded(Event e, Run r, String edgeTag, ArrayList<Run> partitionedRuns) throws CloneNotSupportedException{
		//todo
		boolean timeValidity = this.checkTimeValidity(e, r);
		if(timeValidity){
			boolean timewindowResult = this.checkTimeWindow(e, r);
			if(timewindowResult){
				this.runIteratorCounter ++;
				int checkResult = this.checkPredicateOptimizedForZstream(e, r, edgeTag);
				Run newRun = null;
				switch(checkResult){
					case 1:
						newRun = this.cloneRun(r);
						//this.addRunByPartition(newRun);
						this.addEventToRunCleanIncluded(newRun, e);
						break;
					case 2:
						newRun = this.cloneRun(r);
						//this.addRunByPartition(newRun);
						newRun.proceed();
						this.addEventToRunCleanIncluded(newRun, e);
						break;
				}
			}else{
				if(this.nfa.getLastEdgeTag().equalsIgnoreCase(edgeTag)){
					//only last state to filter runs
					//partitionedRuns.remove(r);
					partitionedRuns.remove(this.runIteratorCounter);
				}else{
					this.runIteratorCounter ++;
				}
			}
		}else{
			this.runIteratorCounter ++;
		}
	}


	
	/**
	 * One event vs. one run
	 * @param e
	 * @param r
	 * @throws CloneNotSupportedException
	 * @throws EvaluationException 
	 */
	
	public void evaluateEventPostponeOptimizedForSkipTillAny(Event e, Run r) throws CloneNotSupportedException, EvaluationException{
		//modify here
			boolean timeWindowResult = this.checkTimeWindow(e, r);
			if(timeWindowResult){
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
							//TODo
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
					this.toDeleteRuns.add(r);//expired
					//System.out.println("DELETE run: " + r + " by event:" + e);
				}
			}
		}
	

	/**
	 * One event vs. one run
	 * @param e
	 * @param r
	 * @throws CloneNotSupportedException
	 * @throws EvaluationException 
	 */
	
	public void evaluateEventPostponeOptimizedForSkipTillAnyCleanIncluded(Event e, Run r, ArrayList<Run> partitionedRuns) throws CloneNotSupportedException, EvaluationException{
			boolean timeWindowResult = this.checkTimeWindow(e, r);
			if(timeWindowResult){
				this.runIteratorCounter ++;
				int currentState = r.getCurrentState();
				State s = this.nfa.getStates(currentState);
				if(s.isKleeneClosure()){
					if(r.isKleeneClosureInitialized()){
						int currentStateNumber = r.getCurrentState();
						r.setCurrentState(currentStateNumber + 1);
						EdgeEvaluationResult normalResult = this.checkEdgePostponeOptimized(e, r, 0);//k+'s next state,check proceed
						if(normalResult != EdgeEvaluationResult.Discard){
							Run newRun = this.cloneRun(r);
							this.addRunByPartition(newRun);
							//add event to the newRun
							//proceed to next state, shift is 1
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
						Run newRun = this.cloneRun(r);
						this.addEventToRunPostPoneOptimizedCleanIncluded(newRun, e, normalResult, 0);
					}
				}
			}else{
				//check output of the run, or discard the run
				if(r.checkMatch()){
					this.enumerateMatchesForRun(r);
				}
				partitionedRuns.remove(this.runIteratorCounter);
			}
		}

	/**
	 * This method evaluates the event for a given run, for skip-till-next-match.
	 * @param e The current event which is being evaluated.
	 * @param r The run against which the evaluation goes
	 */
	public void evaluateEventOptimizedForSkipTillNext(Event e, Run r){
		int checkResult = this.checkPredicateOptimized(e, r);
		switch(checkResult){
			case 1:
				boolean timeWindow = this.checkTimeWindow(e, r);
				if(timeWindow){
					this.addEventToRun(r, e);
				}else{
					this.toDeleteRuns.add(r);
				}
				break;
			case 2:
				r.proceed();
				this.addEventToRun(r, e);
		}
	}
	/**
	 * This method evaluates the event for a given run, for queries with a negation component.
	 * @param e The current event which is being evaluated.
	 * @param r The run against which the evaluation goes
	 */
	public void evaluateEventOptimizedForNegation(Event e, Run r) throws CloneNotSupportedException{
		int checkResult = this.checkPredicateOptimized(e, r);
		switch(checkResult){
			case 1:
				boolean timeWindow = this.checkTimeWindow(e, r);
				if(timeWindow){
					Run newRun = this.cloneRun(r);
					this.addRunByPartition(newRun);
					
					this.addEventToRunForNegation(r, e);
				}else{
					this.toDeleteRuns.add(r);
				}
				break;
			case 2:
				Run newRun = this.cloneRun(r);
				this.addRunByPartition(newRun);
				  
				r.proceed();
				this.addEventToRunForNegation(r, e);
		}
	}
	/**
	 * If the selection strategy is partition-contiguity, this method is called, and it evaluates the event for a given run
	 * @param e The current event which is being evaluated
	 * @param r The run against which the evaluation goes
	 * @throws CloneNotSupportedException
	 */
	public void evaluateEventForPartitonContiguityOptimized(Event e, Run r) throws CloneNotSupportedException{
		int checkResult = this.checkPredicateOptimized(e, r);
		switch(checkResult){
			case 0: 
				this.toDeleteRuns.add(r); 
				break;
			case 1:
				boolean timeWindow = this.checkTimeWindow(e, r);
				if(timeWindow){
					this.addEventToRun(r, e);
				}else{
					this.toDeleteRuns.add(r);
				}
				break;
			case 2:
				r.proceed();
				this.addEventToRun(r, e);
		}
		
		
}

	/**
	 * This method evaluates an event against a run, for skip-till-any-match
	 * @param e The event which is being evaluated.
	 * @param r The run which the event is being evaluated against.
	 * @throws CloneNotSupportedException
	 */
	public void evaluateEventForSkipTillAny(Event e, Run r) throws CloneNotSupportedException{
		boolean checkResult = true;
		
		
		checkResult = this.checkPredicate(e, r);// check predicate
			if(checkResult){ // the predicate if ok.
				checkResult = this.checkTimeWindow(e, r); // the time window is ok
				if(checkResult){// predicate and time window are ok
					this.buffer.bufferEvent(e);// add the event to buffer
					int oldState = 0;
					int newState = 0;
					
					
						Run newRun = this.cloneRun(r); // clone this run
						
						
						oldState = newRun.getCurrentState();
						newRun.addEvent(e);					// add the event to this run
						newState = newRun.getCurrentState();
						if(oldState != newState){
							this.activeRuns.add(newRun);
						}else{//kleene closure
							if(newRun.isFull){
								//check match and output match
								if(newRun.checkMatch()){
									this.outputMatch(new Match(newRun, this.nfa, this.buffer));
																
								}
							}else{
								//check proceed
								if(this.checkProceed(newRun)){
									Run newerRun = this.cloneRun(newRun);
									this.activeRuns.add(newRun);
									newerRun.proceed();
									if(newerRun.isComplete()){
										this.outputMatch(new Match(r, this.nfa, this.buffer));
										
									}else {
										this.activeRuns.add(newerRun);
									}
								}
							}
						}
						
					
						
					}else{
						this.toDeleteRuns.add(r);
					}

				}
	}
	/**
	 * This method evaluates an event against a run, for skip-till-next-match
	 * @param e The event which is being evaluated.
	 * @param r The run which the event is being evaluated against.
	 * @throws CloneNotSupportedException
	 */
	public void evaluateEventForSkipTillNext(Event e, Run r) throws CloneNotSupportedException{
		boolean checkResult = true;
		
		
		checkResult = this.checkPredicate(e, r);// check predicate
			if(checkResult){ // the predicate if ok.
				checkResult = this.checkTimeWindow(e, r); // the time window is ok
				if(checkResult){// predicate and time window are ok
					this.buffer.bufferEvent(e);// add the event to buffer
					int oldState = 0;
					int newState = 0;
					
					
						oldState = r.getCurrentState();
						r.addEvent(e);
						newState = r.getCurrentState();
						if(oldState == newState)//kleene closure
							if(r.isFull){
								//check match and output match
								if(r.checkMatch()){
									this.outputMatch(new Match(r, this.nfa, this.buffer));
									this.toDeleteRuns.add(r);
								}
							}else{
								//check proceed
								if(this.checkProceed(r)){
									Run newRun = this.cloneRun(r);
									
									this.activeRuns.add(newRun);
									this.addRunByPartition(newRun);
									r.proceed();
									if(r.isComplete()){
										this.outputMatch(new Match(r, this.nfa, this.buffer));
										this.toDeleteRuns.add(r);
										
									}
								}
							}
						
						
					}else{
						this.toDeleteRuns.add(r);
					}

				}
	}
	/**
	 * This method evaluates an event against a run, for queries with a negation component.
	 * @param e The event which is being evaluated.
	 * @param r The run which the event is being evaluated against.
	 * @throws CloneNotSupportedException
	 */
	public void evaluateEventForNegation(Event e, Run r) throws CloneNotSupportedException{
		boolean checkResult = true;
		
		
		checkResult = this.checkPredicate(e, r);// check predicate
			if(checkResult){ // the predicate if ok.
				checkResult = this.checkTimeWindow(e, r); // the time window is ok
				if(checkResult){// predicate and time window are ok
					this.buffer.bufferEvent(e);// add the event to buffer
					int oldState = 0;
					int newState = 0;
					
					
						Run newRun = this.cloneRun(r); // clone this run
						
						
						oldState = newRun.getCurrentState();
						newRun.addEvent(e);					// add the event to this run
						newState = newRun.getCurrentState();
						if(oldState != newState){
							this.activeRuns.add(newRun);
							State tempState = this.nfa.getStates(newState);
							if(tempState.isBeforeNegation()){
								r.setBeforeNegationTimestamp(e.getTimestamp());
							}else if(tempState.isAfterNegation()){
								r.setAfterNegationTimestamp(e.getTimestamp());
							}
						}else{//kleene closure
							if(newRun.isFull){
								//check match and output match
								if(newRun.checkMatch()){
									this.outputMatchForNegation(new Match(newRun, this.nfa, this.buffer), newRun);
																
								}
							}else{
								//check proceed
								if(this.checkProceed(newRun)){
									Run newerRun = this.cloneRun(newRun);
									this.activeRuns.add(newRun);
									newerRun.proceed();
									if(newerRun.isComplete()){
										this.outputMatchForNegation(new Match(r, this.nfa, this.buffer), r);
											
									}else {
										this.activeRuns.add(newerRun);
									}
								}
							}
						}
						
					
						
					}else{
						this.toDeleteRuns.add(r);
					}

				}
	}
	/**
	 * This methods add a new run to a partition.
	 * @param newRun The run to be added
	 */
		
	public void addRunByPartition(Run newRun){
		if(this.activeRunsByPartition.containsKey(newRun.getPartitonId())){
			this.activeRunsByPartition.get(newRun.getPartitonId()).add(newRun);
		}else{
			ArrayList<Run> newPartition = new ArrayList<Run>();
			newPartition.add(newRun);
			this.activeRunsByPartition.put(newRun.getPartitonId(), newPartition);
		}
	}
	
	/**
	 * This method evaluates an event against a run.
	 * @param e The event which is being evaluated.
	 * @param r The run which the event is being evaluated against.
	 * @throws CloneNotSupportedException
	 */
		
		
		public void evaluateEventForPartitonContiguity(Event e, Run r) throws CloneNotSupportedException{
			boolean checkResult = true;
			
			
			checkResult = this.checkPredicate(e, r);// check predicate
				if(checkResult){ // the predicate if ok.
					checkResult = this.checkTimeWindow(e, r); // the time window is ok
					if(checkResult){// predicate and time window are ok
						this.buffer.bufferEvent(e);// add the event to buffer
						int oldState = 0;
						int newState = 0;
						oldState = r.getCurrentState();
						r.addEvent(e);
						newState = r.getCurrentState();
						if(oldState == newState)//kleene closure
							if(r.isFull){
								//check match and output match
								if(r.checkMatch()){
									this.outputMatch(new Match(r, this.nfa, this.buffer));
									this.toDeleteRuns.add(r);
								}
							}else{
								//check proceed
								if(this.checkProceed(r)){
									Run newRun = this.cloneRun(r);
									this.activeRuns.add(newRun);
									this.addRunByPartition(newRun);
									
									r.proceed();
									if(r.isComplete()){
										this.outputMatch(new Match(r, this.nfa, this.buffer));
										this.toDeleteRuns.add(r);
										
									}
								}
							}
						
							
						}else{
							this.toDeleteRuns.add(r);
						}

					}else{
						this.toDeleteRuns.add(r);
					}
	}
	

	/**
	 * This method adds an event to a run
	 * @param r The event to be added
	 * @param e The run to which the event is added
	 */
	public void addEventToRun(Run r, Event e){
		this.buffer.bufferEvent(e);// add the event to buffer
		int oldState = 0;
		int newState = 0;
		oldState = r.getCurrentState();
		r.addEvent(e);
		newState = r.getCurrentState();
		if(oldState == newState)//kleene closure
			if(r.isFull){
				//check match and output match
				if(r.checkMatch()){
					this.outputMatch(new Match(r, this.nfa, this.buffer));
					this.toDeleteRuns.add(r);
				}
			}
		
	}
	
	/**
	 * This method adds an event to a run
	 * @param r The event to be added
	 * @param e The run to which the event is added
	 */
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
	
	/**
	 * Special version, including clean cost
	 * Add event to a run.
	 * @param r
	 * @param e
	 * @param edgeCheckResult
	 * @param shift
	 */
	public void addEventToRunPostPoneOptimizedCleanIncluded(Run newRun, Event e, EdgeEvaluationResult edgeCheckResult, int shift){
		//check output
		this.buffer.bufferEvent(e);// add the event to buffer
		int oldState = 0;
		int newState = 0;
		//shift
		oldState = newRun.getCurrentState() + shift;
		newRun.setCurrentState(oldState);
		//label event, labels should be saved in the run!!!
		if(edgeCheckResult == EdgeEvaluationResult.Safe){
			newRun.addEventAndStatus(e, true);
		}else{
			newRun.addEventAndStatus(e, false);
		}
		newState = newRun.getCurrentState();
		boolean toAddNewRun = true;
		if(oldState == newState){//kleene closure
			if(newRun.isFull){
				//check match and output match
				if(newRun.checkMatchPostponeOptimized()){
					this.enumerateMatchesForRun(newRun);
					toAddNewRun = false;
				}
			}
		}
		if(toAddNewRun){
			this.addRunByPartition(newRun);
		}
	}

	/**
	 * This method adds an event to a run, for queries with a negation component.
	 * @param r The event to be added
	 * @param e The run to which the event is added
	 */
	public void addEventToRunForNegation(Run r, Event e){
		this.buffer.bufferEvent(e);// add the event to buffer
		int oldState = 0;
		int newState = 0;
		oldState = r.getCurrentState();
		r.addEvent(e);
		newState = r.getCurrentState();
		State tempState = this.nfa.getStates(newState);
		
		if(tempState.isBeforeNegation()){
			r.setBeforeNegationTimestamp(e.getTimestamp());
		}else if(tempState.isAfterNegation()){
			r.setAfterNegationTimestamp(e.getTimestamp());
		}
		if(oldState == newState)//kleene closure
			if(r.isFull){
				//check match and output match
				if(r.checkMatch()){
					this.outputMatchByPartitionForNegation(new Match(r, this.nfa, this.buffer), r);
					
					this.toDeleteRuns.add(r);
				}
			}
		
	}
	/**
	 * Creates a new run containing the input event.
	 * @param e The current event.
	 * @throws EvaluationException
	 */
	
	public void createNewRun(Event e) throws EvaluationException{
		if(this.nfa.getStates()[0].canStartWithEvent(e)){
			this.buffer.bufferEvent(e);
			Run newRun = this.engineRunController.getRun();
			newRun.initializeRun(this.nfa);
			newRun.addEvent(e);
			//this.numberOfRuns.update(1);
			Profiling.numberOfRuns ++;
			this.activeRuns.add(newRun);
			
		}
	}
	/**
	 * Creates a new run containing the input event and adds the new run to the corresponding partition
	 * @param e The current event
	 * @throws EvaluationException
	 * @throws CloneNotSupportedException
	 */
	public void createNewRunByPartition(Event e) throws EvaluationException, CloneNotSupportedException{
		if(this.nfa.getStates()[0].canStartWithEvent(e)){
			this.buffer.bufferEvent(e);
			Run newRun = this.engineRunController.getRun();
			newRun.initializeRun(this.nfa);
			newRun.addEvent(e); 
			Profiling.numberOfRuns ++;
			this.activeRuns.add(newRun);
			this.addRunByPartition(newRun);
			Profiling.numberOfStartRuns ++;

		}
		
	}
	
	public void createNewRunByPartitionPostponeOptimized(Event e) throws EvaluationException, CloneNotSupportedException{
		if(this.nfa.getStates()[0].canStartWithEvent(e)){
			this.buffer.bufferEvent(e);
			Run newRun = this.engineRunController.getRun();
			newRun.initializeRun(this.nfa);
			newRun.addEventAndStatus(e, true);
			Profiling.numberOfRuns ++;
			this.activeRuns.add(newRun);
			this.addRunByPartition(newRun);
			Profiling.numberOfStartRuns ++;
		}
	}

	
	
	/**
	 * This method is used by ZStream, and other methods which buffers events in the StackEventGroup. Like imprecise engine
	 * The diffirence between this and normal one is that: this one will not need full check of the predicate
	 * 
	 * @param e
	 * @throws EvaluationException
	 * @throws CloneNotSupportedException
	 */
	
	public void createNewRunByPartitionForZstream(Event e, String edgeTag) throws EvaluationException, CloneNotSupportedException{
		Edge edge = this.nfa.getEdgeByTad(edgeTag);
		boolean canStart = false;
		if(edge.getEdgeTag().equalsIgnoreCase(edgeTag) && edge.isSingle()){
			canStart = true;
		}else if(this.nfa.getStates()[0].canStartWithEvent(e)){
			canStart = true;
		}
		if(canStart){
			this.buffer.bufferEvent(e);
			Run newRun = this.engineRunController.getRun();
			newRun.initializeRun(this.nfa);
			newRun.addEvent(e); 
			Profiling.numberOfRuns ++;
			this.activeRuns.add(newRun);
			this.addRunByPartition(newRun);
			
			Profiling.numberOfStartRuns ++;
		}
		
	}
	
	/**
	 * Checks the predicate for e against r
	 * @param e The current event
	 * @param r The run against which e is evaluated 
	 * @return The check result, 0 for false, 1 for take or begin, 2 for proceed
	 */
	public int checkPredicateOptimized(Event e, Run r){//0 for false, 1 for take or begin, 2 for proceed
		int currentState = r.getCurrentState();
		State s = this.nfa.getStates(currentState);
		if(!s.getEventType().equalsIgnoreCase(e.getEventType())){// event type check;
			return 0;
		}

		if(!s.isKleeneClosure()){
			Edge beginEdge = s.getEdges(0);
			boolean result;
			//result = firstEdge.evaluatePredicate(e, r, buffer);
			result = beginEdge.evaluatePredicate(e, r, buffer);//
			if(result){
				return 1;
			}
		}else{
			if(r.isKleeneClosureInitialized()){
				boolean result;
				result = this.checkProceedOptimized(e, r);//proceedEdge.evaluatePredicate(e, r, buffer);
				if(result){
					return 2;
				}else{
				
				
				
				
				Edge takeEdge = s.getEdges(1);
				result = takeEdge.evaluatePredicate(e, r, buffer);
				if(result){
					return 1;
				}
				}
			}else{
				Edge beginEdge = s.getEdges(0);
				boolean result;
				
				result = beginEdge.evaluatePredicate(e, r, buffer);//
				if(result){
					return 1;
				}
			}
		}
		

		
		return 0;	
		
		
	}
	/**
	 * Optimized for ZStream
	 * Avoid repeating checking
	 * @param e
	 * @param r
	 * @return
	 */
	public int checkPredicateOptimizedForZstream(Event e, Run r, String edgeTag){//0 for false, 1 for take or begin, 2 for proceed
		int currentState = r.getCurrentState();
		State s = this.nfa.getStates(currentState);
		if(!s.getEventType().equalsIgnoreCase(e.getEventType())){// event type check;
			return 0;
		}

		if(!s.isKleeneClosure()){
			Edge beginEdge = s.getEdges(0);
			boolean result;
			if(beginEdge.getEdgeTag().equalsIgnoreCase(edgeTag) && beginEdge.isSingle()){
				result = true;
			}else{
				result = beginEdge.evaluatePredicate(e, r, buffer);//
				//result = beginEdge.evaluatePredicateSecondCheck(e, r, buffer);
			}
			if(result){
				return 1;
			}
			
		}else{
			if(r.isKleeneClosureInitialized()){
				boolean result;
				//change for zstream
				result = this.checkProceedOptimizedForZstream(e, r, edgeTag);//proceedEdge.evaluatePredicate(e, r, buffer);
				if(result){
					return 2;
				}else{
				Edge takeEdge = s.getEdges(1);
				if(takeEdge.getEdgeTag().equalsIgnoreCase(edgeTag) && takeEdge.isSingle()){
					result = true;
				}else{
					result = takeEdge.evaluatePredicate(e, r, buffer);
					//result = takeEdge.evaluatePredicateSecondCheck(e, r, buffer);
				}
				if(result){
					return 1;
				}
				}
			}else{
				Edge beginEdge = s.getEdges(0);
				boolean result;
				if(beginEdge.getEdgeTag().equalsIgnoreCase(edgeTag) && beginEdge.isSingle()){
					result = true;
				}else{
					result = beginEdge.evaluatePredicate(e, r, buffer);
					//result = beginEdge.evaluatePredicateSecondCheck(e, r, buffer);
				}
				if(result){
					return 1;
				}
			}
		}
		return 0;
	}
	
	
	/**
	 * This methods perform the postponed evluation of unsafe events
	 * Optimized to skip repeated checkings!
	 * @param e
	 * @param r
	 * @return
	 */
	public boolean postponeCheckPredicateOptmized(Event e, Run r){//0 for false, 1 for take or begin, 2 for proceed
		int currentState = r.getCurrentState();
		State s = this.nfa.getStates(currentState);
		if(!s.isKleeneClosure()){
			Edge beginEdge = s.getEdges(0);
			boolean result;
			result = beginEdge.evaluatePredicate(e, r, buffer);//
			if(result){
				return true;
			}
		}else{
			if(r.isKleeneClosureInitialized()){
				boolean result;
				Edge takeEdge = s.getEdges(1);
				result = takeEdge.evaluatePredicate(e, r, buffer);
				if(result){
					return true;
				}
			}else{
				Edge beginEdge = s.getEdges(0);
				boolean result;
				result = beginEdge.evaluatePredicate(e, r, buffer);//
				if(result){
					return true;
				}
			}
		}
		return false;	
	}
	/**
	 * This methods perform the postponed evluation of unsafe events
	 * @param e
	 * @param r
	 * @return
	 */
	public boolean postponeCheckPredicate(Event e, Run r){//0 for false, 1 for take or begin, 2 for proceed
		int currentState = r.getCurrentState();
		State s = this.nfa.getStates(currentState);
		if(!s.isKleeneClosure()){
			Edge beginEdge = s.getEdges(0);
			boolean result;
			result = beginEdge.evaluatePredicate(e, r, buffer);//
			//result = beginEdge.evaluatePredicateSecondCheck(e, r, buffer);
			if(result){
				return true;
			}
		}else{
			if(r.isKleeneClosureInitialized()){
				boolean result;
				Edge takeEdge = s.getEdges(1);
				result = takeEdge.evaluatePredicate(e, r, buffer);
				//result = takeEdge.evaluatePredicateSecondCheck(e, r, buffer);
				if(result){
					return true;
				}
			}else{
				Edge beginEdge = s.getEdges(0);
				boolean result;
				result = beginEdge.evaluatePredicate(e, r, buffer);//
				//result = beginEdge.evaluatePredicateSecondCheck(e, r, buffer);
				if(result){
					return true;
				}
			}
		}
		return false;	
	}
	
	/**
	 * One event vs. One edge(Multiple predicates)
	 * @param e
	 * @param r
	 * @param shift 0 means evaluate the current state, 1 means evaluate the next state
	 * @return EdgeEvaluationResult.Discard/Safe/Unsafe
	 * @throws EvaluationException 
	 */
	public EdgeEvaluationResult checkEdgePostponeOptimized(Event e, Run r, int shift) throws EvaluationException{
		
		int currentState = r.getCurrentState();
		State s = this.nfa.getStates(currentState + shift);
		
		if(!s.getEventType().equalsIgnoreCase(e.getEventType())){
			return EdgeEvaluationResult.Discard;
		}
		
		if(!s.isKleeneClosure()){
			Edge beginEdge = s.getEdges(0);
			EdgeEvaluationResult result = beginEdge.evaluatePredicatePostponeOptimzed(e, r, buffer);
			return result;
		}else{
			if(!r.isKleeneClosureInitialized()){
				Edge beginEdge = s.getEdges(0);
				EdgeEvaluationResult result = beginEdge.evaluatePredicatePostponeOptimzed(e, r, buffer);
				return result;
			}else{
				Edge takeEdge = s.getEdges(1);//
				EdgeEvaluationResult result = takeEdge.evaluatePredicatePostponeOptimzed(e, r, buffer);
				return result;
			}
		}
		
			}
	/**
	 * Checks whether the run needs to proceed if we add e to r
	 * @param e The current event
	 * @param r The run against which e is evaluated
	 * @return The checking result, TRUE for OK to proceed
	 */
	public boolean checkProceedOptimized(Event e, Run r){
		int currentState = r.getCurrentState();
		State s = this.nfa.getStates(currentState + 1);//+1
		r.setCurrentState(currentState + 1);
		
		if(!s.getEventType().equalsIgnoreCase(e.getEventType())){// event type check;
			return false;
		}
		Edge beginEdge = s.getEdges(0);
		boolean result;
		result = beginEdge.evaluatePredicate(e, r, buffer);
		
		r.setCurrentState(currentState);
		
		return result;
	}
	/**
	 * Optimized for zstream
	 * No repeated evaluation
	 * @param e
	 * @param r
	 * @return
	 */
	public boolean checkProceedOptimizedForZstream(Event e, Run r, String edgeTag){
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
			//result = false;
		}
		r.setCurrentState(currentState);
		return result;
	}
/**
 * Checks whether the event satisfies the predicates of a run
 * @param e the current event
 * @param r the current run
 * @return the check result
 */
	
	public boolean checkPredicate(Event e, Run r){
		int currentState = r.getCurrentState();
		State s = this.nfa.getStates(currentState);
		if(!s.getEventType().equalsIgnoreCase(e.getEventType())){// event type check;
			return false;
		}

		if(!s.isKleeneClosure()){
			Edge beginEdge = s.getEdges(0);
			boolean result;
			//result = firstEdge.evaluatePredicate(e, r, buffer);
			result = beginEdge.evaluatePredicate(e, r, buffer);//
			if(result){
				return true;
			}
		}else{
			if(r.isKleeneClosureInitialized()){
				Edge takeEdge = s.getEdges(1);
				boolean result;
				result = takeEdge.evaluatePredicate(e, r, buffer);
				if(result){
					return true;
				}
			}else{
				Edge beginEdge = s.getEdges(0);
				boolean result;
				
				result = beginEdge.evaluatePredicate(e, r, buffer);//
				if(result){
					return true;
				}
			}
		}
		

		
		return false;	
		
		
	}
	/**
	 * Checks whether the event satisfies the partition of a run, only used under partition-contiguity selection strategy
	 * @param e the current event
	 * @param r the current run
	 * @return the check result, boolean format
	 */
	public boolean checkPartition(Event e, Run r){
		
		if(r.getPartitonId() == e.getAttributeByName(this.nfa.getPartitionAttribute())){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether a kleene closure state can proceed to the next state
	 * @param r the current run
	 * @return the check result, boolean format
	 */
	
	
	public boolean checkProceed(Run r){// cannot use previous, only use position?
		int currentState = r.getCurrentState();
			
		Event previousEvent = this.buffer.getEvent(r.getPreviousEventId());
		State s = this.nfa.getStates(currentState);


		
		Edge proceedEdge = s.getEdges(2);
		boolean result;
		result = proceedEdge.evaluatePredicate(previousEvent, r, buffer);//
		if(result){
			return true;
		}
		
		return false;	
		
	}
	
	public boolean checkNegation(Event e) throws EvaluationException{
		if(this.nfa.getNegationState().canStartWithEvent(e)){
			return true;
		}
		return false;
	}

	public void indexNegationByPartition(Event e){
		int id = e.getAttributeByName(this.nfa.getPartitionAttribute());
		if(this.negationEventsByPartition.containsKey(id)){
			this.negationEventsByPartition.get(id).add(e);
		}else{
			ArrayList<Event> newPartition = new ArrayList<Event>();
			newPartition.add(e);
			this.negationEventsByPartition.put(id, newPartition);
		}
		
	}
	public boolean searchNegation(long beforeTimestamp, long afterTimestamp, ArrayList<Event> list){
		// basic idea is to use binary search on the timestamp
		int size = list.size();
		int lower = 0;
		int upper = size - 1;
		Event tempE;
		while(lower <= upper){
			tempE = list.get((lower + upper)/2);
			if(tempE.getTimestamp() >= beforeTimestamp && tempE.getTimestamp() <= afterTimestamp){
				return true;
			}
			if(tempE.getTimestamp() < beforeTimestamp){
				lower = (lower + upper)/2;
			}else {
				upper = (lower + upper)/2;
			}
			
		}
		return false;
	}
	public boolean searchNegationByPartition(long beforeTimestamp, long afterTimestamp, int partitionId){
		if(this.negationEventsByPartition.containsKey(partitionId)){
			ArrayList<Event> tempList = this.negationEventsByPartition.get(partitionId);
			return this.searchNegation(beforeTimestamp, afterTimestamp, tempList);
			
		}
		return false;
	}
	
/**
 * Clones a run
 * @param r the run to be cloned
 * @return the new run cloned from the input run.
 * @throws CloneNotSupportedException
 */
	
	public Run cloneRun(Run r) throws CloneNotSupportedException{
		Profiling.countOfCloneRun ++;
		long startTime = System.nanoTime();
		
		Run newRun = this.engineRunController.getRun();
		newRun = (Run)r.clone();
		Profiling.numberOfRuns ++;
		Profiling.timeOfCloneRun += (System.nanoTime() - startTime);
		return newRun;
	}
	
/**
 * Checks whether the event satisfies the time window constraint of a run
 * @param e the current event
 * @param r the current run
 * @return the check result
 */
	public boolean checkTimeWindow(Event e, Run r){
		if((e.getTimestamp() - r.getStartTimeStamp()) <= this.nfa.getTimeWindow()){
			return true;
		}
		return false;
	}
	
/**
 * Checks whether the event satisfies the time increasing monotonicity
 * @param e
 * @param r
 * @return
 */
	public boolean checkTimeValidity(Event e, Run r){
		if(e.getTimestamp() <= r.getLastTimeStamp()){
			return false;
		}
		return true;
	}

/**
 * Outputs a match, and profiles.	
 * @param m the match to be output
 */

	public void outputMatch(Match m){
		Profiling.numberOfMatches ++;
		Profiling.numberOfEventsInMatches += m.getEvents().length;
		if(m.getEvents().length > Profiling.maxNumberOfEventsInMatches){
			Profiling.maxNumberOfEventsInMatches = m.getEvents().length;
		}
		//this.matches.addMatch(m);
		if(ConfigFlags.printResults){
			System.out.println("----------Here is the No." + Profiling.numberOfMatches +" match----------");
			System.out.println(m);
		}
	}
	
	public void outputMatchForEnumInstance(EnumerationInstance enumInstance){
		//convert instance to run
		int[]eventIdsBeforeKleene = enumInstance.getEventIdsBeforeKleene(); 
		int[]eventIdsForKleene = this.convertIntegerArrayListToArray(enumInstance.getIds());
		int[]eventIdsAfterKleene = enumInstance.getEventIdsAfterKleene();
		ArrayList<Integer> idsList = new ArrayList<Integer>();
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
		if(this.tempRun == null){
			this.tempRun = this.engineRunController.getRun();// to optimized, re-use runs
		}
		this.tempRun.setEventIds(idsList);
		this.tempRun.setCount(idsList.size());
		//convert run to match
		Match tempMatch = new Match(tempRun, this.nfa, this.buffer);
		//output match
		this.outputMatch(tempMatch);
	}
	
	public void enumerateMatchesForRun(Run r){
		if(ConfigFlags.usingNewEnumerationInPreciseEngine){
			this.enumerateMatchesForRunWithDynamicProgrammingUsingEnumInstance(r);
		}else{
			this.enumerateMatchesForRunUsingEnumerationTool(r);
		}
			
	}
	
/**
 * Enumerate matches for postponing algorithm
 * @param r
 */
	public void enumerateMatchesForRunUsingEnumerationTool(Run r){
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
					if(this.nfa.isSafe()){
						
						this.outputMatchPostponeOptimized(eventIdsBeforeKleene, kleeneEventIds, eventIdsAfterKleene);
					}else{
						//todo, timestamp
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
					Profiling.timeOnValidate +=(System.nanoTime() - time3);
				}
			}
			Profiling.timeOnEnumeration += (System.nanoTime() - onTime);
			
			//output the run without enumerated k+ events
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
	
	
	/*
	 * Enumeration algorithm using enumeration instance
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
				if(this.nfa.getkTypeForNFA() == KleeneClosurePredicateType.TrueFalseValueConsistent){
					this.outputMatchForEnumInstance(enumInstance);
				}else{
					boolean predicateResult = this.validatePredicateForDP(enumInstance);
					if(predicateResult){
						this.outputMatchForEnumInstance(enumInstance);
					}
				}
				return true;
		}else{
			if(this.nfa.getkTypeForNFA() == KleeneClosurePredicateType.TrueValueConsistent){// true type
				boolean predicateResult = this.validatePredicateForDP(enumInstance);
				if(predicateResult){
					this.outputMatchForEnumInstance(enumInstance);
					return true;
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
				if(enumInstance.isPreviousEvaluationResult()){
					this.outputMatchForEnumInstance(enumInstance);
				}
				return true;
			}
		}
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
			this.runForEnumeration = this.engineRunController.getRun();
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
	public int[] convertIntegerArrayListToArray(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
	    return ret;
	}
	public boolean[] convertBooleanArrayListToArray(List<Boolean> list){
		boolean[] result = new boolean[list.size()];
		for(int i = 0; i < result.length; i ++){
			result[i] = list.get(i).booleanValue();
		}
		return result;
	}

	
	public void cleanConfidenceSharingCache(){
		// empty because the uncertain engine will call the overriden method
	}

	public void checkMatch(int[] eventIdsBeforeKleene, int[]kleeneEventIds, int[]eventIdsAfterKleene,
			ArrayList<Boolean>allEventSafeFlags, int firstKleeneEventPosition, int[]combination, boolean[]afterKleeneSafeFlags,int i){
		if(this.nfa.isSafe()){
			this.outputMatchPostponeOptimized(eventIdsBeforeKleene, kleeneEventIds, eventIdsAfterKleene);
		}else{
			boolean[] kleeneEventSafeFlags = new boolean[i];
			
			for(int k = 0; k < i; k++){
				kleeneEventSafeFlags[k] = allEventSafeFlags.get(firstKleeneEventPosition + combination[k]);
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
	
	public boolean validateSafeFlags(boolean[]safeFlags){
		if(safeFlags == null){
			return true;
		}
		for(int i =0; i < safeFlags.length; i++){
			if(!safeFlags[i]){
				return false;
			}
		}
		return true;
	}
	
	public boolean validateSafeFlags(ArrayList<Boolean>safeFlags){
		if(safeFlags == null){
			return true;
		}
		for(int i =0; i < safeFlags.size(); i++){
			if(!safeFlags.get(i)){
				return false;
			}
		}
		return true;
	}

	public boolean validateRunTimestamp(Run r){
		ArrayList<Integer> ids = r.getEventIds();
		long currentTimestamp = Long.MIN_VALUE;
		Event e;
		for(int i = 0; i < ids.size(); i ++){
			e = this.buffer.getEvent(ids.get(i));
			if(e.getTimestamp() < currentTimestamp){
				return false;
			}else{
				currentTimestamp = e.getTimestamp();
			}
		}
		return true;
	}
	public boolean validateMatchWithoutEnumeration(Run r){
		//first validate timestamp
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
			Run newRun = this.engineRunController.getRun();
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
	public boolean validateMatch(int[]eventIdsBeforeKleene, int[]eventIdsForKleene, int[]eventIdsAfterKleene,
			boolean[]kleeneEventSafeFlags,boolean kleeneSafeFlagValidateResult, 
			boolean[]afterKleeneSafeFlags, boolean afterKleeneSafeFlagValidateResult){
		Profiling.numberOfValidations ++;
		long time1 = System.nanoTime();
		if(this.runForEnumeration == null){
			this.runForEnumeration= this.engineRunController.getRun();
		}
		
		Profiling.timeBeforeEnumeration += (System.nanoTime() - time1);
		long time2 = System.nanoTime();
		runForEnumeration.initializeRun(nfa);
		Profiling.timeAfterEnumeration += (System.nanoTime() - time2);
		
		if(eventIdsBeforeKleene != null){
			for(int i = 0; i < eventIdsBeforeKleene.length; i++){
				int id = eventIdsBeforeKleene[i];
				Event e = this.buffer.getEvent(id);
				
				runForEnumeration.addEvent(e);
			}
		}
		
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
	public boolean validateMatchTimestamp(int[]eventIdsBeforeKleene, int[]eventIdsForKleene, int[]eventIdsAfterKleene){
		long currentTimestamp = Long.MIN_VALUE;
		Event e;
		if(eventIdsBeforeKleene != null){
			for(int i = 0; i < eventIdsBeforeKleene.length; i ++){
				e = this.buffer.getEvent(eventIdsBeforeKleene[i]);
				
				if(e.getTimestamp() < currentTimestamp){
					return false;
				}else{
					currentTimestamp = e.getTimestamp();
				}
			}
		}
		
		if(eventIdsForKleene != null){
			for(int i = 0; i < eventIdsForKleene.length; i ++){
				e = this.buffer.getEvent(eventIdsForKleene[i]);
				if(e.getTimestamp() < currentTimestamp){
					return false;
				}else{
					currentTimestamp = e.getTimestamp();
				}
			}
		}
		
		if(eventIdsAfterKleene != null){
			for(int i = 0; i < eventIdsAfterKleene.length; i ++){
				e = this.buffer.getEvent(eventIdsAfterKleene[i]);
				
				if(e.getTimestamp() < currentTimestamp){
					return false;
				}else{
					currentTimestamp = e.getTimestamp();
				}
			}
		}
		
		return true;
	}
	public void outputMatchPostponeOptimized(int[]eventIdsBeforeKleene, int[]eventIdsForKleene,int[]eventIdsAfterKleene){
		boolean outputMatch = false;
		if(ConfigFlags.usingZstream){
			//check timestamp
			if(this.validateMatchTimestamp(eventIdsBeforeKleene, eventIdsForKleene, eventIdsAfterKleene)){
				outputMatch = true;
			}
		}else{
			outputMatch = true;
		}
		
		if(outputMatch){
			Profiling.numberOfMatches ++;
			int numberOfEvents = 0;
			if(eventIdsBeforeKleene != null){
				numberOfEvents += eventIdsBeforeKleene.length;
			}
			if(eventIdsForKleene != null){
				numberOfEvents += eventIdsForKleene.length;
			}
			if(eventIdsAfterKleene != null){
				numberOfEvents += eventIdsAfterKleene.length;
			}
			
			Profiling.numberOfEventsInMatches += numberOfEvents;
			
			if(numberOfEvents > Profiling.maxNumberOfEventsInMatches){
				Profiling.maxNumberOfEventsInMatches = numberOfEvents;
			}
			
			if(ConfigFlags.printResults){
				System.out.println("----------Here is the No." + Profiling.numberOfMatches +" match----------");
		
		
				if(eventIdsBeforeKleene != null){
					for(int i = 0; i < eventIdsBeforeKleene.length; i++){
						this.printEvent(eventIdsBeforeKleene[i]);
					}
				}
				
				if(eventIdsForKleene != null){
					for(int i = 0; i < eventIdsForKleene.length; i++){
						this.printEvent(eventIdsForKleene[i]);
					}
				}
				
				if(eventIdsAfterKleene != null){
					for(int i = 0; i < eventIdsAfterKleene.length; i ++){
						this.printEvent(eventIdsAfterKleene[i]);
					}
				}
				/*
				if(ConfigFlags.isDebugging){
					//print the ids
					StringBuilder sb = new StringBuilder();
					if(eventIdsBeforeKleene != null){
						
						for(int i = 0; i < eventIdsBeforeKleene.length; i ++){
							sb.append(eventIdsBeforeKleene[i] + "-");
						}
					}
					if(eventIdsForKleene != null){
						for(int i = 0; i < eventIdsForKleene.length; i ++){
							sb.append(eventIdsForKleene[i] + "-");
						}
					}
					if(eventIdsAfterKleene != null){
						for(int i = 0; i < eventIdsAfterKleene.length; i ++){
							sb.append(eventIdsAfterKleene[i] + "-");
						}
					}
					System.out.print("Debug:" + sb.toString() + "\n");
				}
				*/
			}
		
		}
		
		
		
	}
	public void printEvent(int eventId){
		Event e = this.buffer.getEvent(eventId);
		System.out.println(e);
	}
	/**
	 * Outputs a match, and profiles, for queries with a negation componengt, without a partition attribute.	
	 * @param m the match to be output
	 */
	public void outputMatchForNegation(Match m, Run r){
		if(this.searchNegation(r.getBeforeNegationTimestamp(), r.getAfterNegationTimestamp(), this.negationEvents)){
			Profiling.negatedMatches ++;
			System.out.println("~~~~~~~~~~~~~~~~Here is a negated match~~~~~~~~~~~~~~~");
			System.out.println(m);
		}else{
		
		Profiling.numberOfMatches ++;
		//this.matches.addMatch(m);
		if(ConfigFlags.printResults){
		System.out.println("----------Here is the No." + Profiling.numberOfMatches +" match----------");
		
		System.out.println(m);
		}
		
		}
		
	}
	/**
	 * Outputs a match, and profiles, for queries with a negation componengt, with a partition attribute.	
	 * @param m the match to be output
	 */
	public void outputMatchByPartitionForNegation(Match m, Run r){
		if(this.searchNegationByPartition(r.getBeforeNegationTimestamp(), r.getAfterNegationTimestamp(), r.getPartitonId())){
			Profiling.negatedMatches ++;
			System.out.println("~~~~~~~~~~~~~~~~Here is a negated match~~~~~~~~~~~~~~~");
			System.out.println(m);
		}else{
		
		Profiling.numberOfMatches ++;
		//this.matches.addMatch(m);
		if(ConfigFlags.printResults){
		System.out.println("----------Here is the No." + Profiling.numberOfMatches +" match----------");
		
		System.out.println(m);
		}
		
		}
		
	}
/**
 * Deletes runs violating the time window
 * @param currentTime current time
 * @param timeWindow time window of the query
 * @param delayTime specified delay period, any run which has been past the time window by this value would be deleted.
 */

	public void deleteRunsOverTimeWindow(int currentTime, int timeWindow, int delayTime){
		int size = this.activeRuns.size();
		Run tempRun = null;
		for(int i = 0; i < size; i ++){
			tempRun = this.activeRuns.get(i);
			if(!tempRun.isFull&&tempRun.getStartTimeStamp() + timeWindow + delayTime < currentTime){
				this.toDeleteRuns.add(tempRun);
				Profiling.numberOfRunsOverTimeWindow ++;
				
			}
		}
	}
	
	/**
	 * Cleans useless runs
	 */
	public void cleanRuns(){

		int size = this.toDeleteRuns.size();
		Profiling.numberOfRunsCutted += size;
		Run tempRun = null;
		for(int i = 0; i < size; i ++){
			tempRun = toDeleteRuns.get(0);
			
			tempRun.resetRun();
			this.activeRuns.remove(tempRun);
			this.toDeleteRuns.remove(0);
			
		}
		

	}
	
	/**
	 * Cleans useless runs by partition.
	 */
	public void cleanRunsByPartition(){

		int size = this.toDeleteRuns.size();
		Profiling.numberOfRunsCutted += size;
		Run tempRun = null;
		ArrayList<Run> partitionedRuns = null;
		//System.out.println("ToDeleteRuns size:" + this.toDeleteRuns.size());
		
		/*
		System.out.println("ActiveRuns size:" + this.activeRuns.size());
		System.out.println("ToDeleteRuns size:" + this.toDeleteRuns.size());
		System.out.println("Partition size:" + this.activeRunsByPartition.get(tempRun.getPartitonId()).size());
		*/
		
		/*backup 3, remove all
		for(int i = 0; i < size; i ++){
			tempRun = toDeleteRuns.get(i);
			Profiling.totalRunLifeTime += (System.nanoTime() - tempRun.getLifeTimeBegin());
			
			long time1 = System.nanoTime();
			tempRun.resetRun();
			Profiling.timeOnResetRuns += (System.nanoTime() - time1);
			
		}
		long time2 = System.nanoTime();
		
		System.out.println("Active runs size:" + this.activeRuns.size());
		this.activeRuns.removeAll(this.toDeleteRuns);
		
		Profiling.timeOnRemoveRuns += (System.nanoTime() - time2);
		
		
		long time3 = System.nanoTime();
		for(Integer key : this.activeRunsByPartition.keySet()){
			partitionedRuns = this.activeRunsByPartition.get(key);
			partitionedRuns.removeAll(this.toDeleteRuns);
			if(partitionedRuns.size() == 0){
				this.activeRunsByPartition.remove(partitionedRuns);
			}
		}
		Profiling.timeOnRemoveRunsFromPartition += (System.nanoTime() - time3);
		
		long time4 = System.nanoTime();
		this.toDeleteRuns = new ArrayList<Run>();
		Profiling.timeOnRemoveRuns += (System.nanoTime() - time4);
		*/ 
		
		//, back up 2, delete runs remove in batch
		
		for(int i = 0; i < size; i ++){
			tempRun = toDeleteRuns.get(i);
			
			long time1 = System.nanoTime();
			tempRun.resetRun();
			Profiling.timeOnResetRuns += (System.nanoTime() - time1);
			
			long time2 = System.nanoTime();
			
			this.activeRuns.remove(tempRun);
			Profiling.timeOnRemoveRuns += (System.nanoTime() - time2);
			
			
			
			long time3 = System.nanoTime();
			partitionedRuns = this.activeRunsByPartition.get(tempRun.getPartitonId());
			partitionedRuns.remove(tempRun);
			
			
			if(partitionedRuns.size() == 0){
				this.activeRunsByPartition.remove(partitionedRuns);
			}
			Profiling.timeOnRemoveRunsFromPartition += (System.nanoTime() - time3);
			
		}
		this.toDeleteRuns = new ArrayList<Run>();
		//*/
		
		/* back up, old one
		for(int i = 0; i < size; i ++){
			
			
			tempRun = toDeleteRuns.get(0);
			Profiling.totalRunLifeTime += (System.nanoTime() - tempRun.getLifeTimeBegin());
			
			long time1 = System.nanoTime();
			tempRun.resetRun();
			Profiling.timeOnResetRuns += (System.nanoTime() - time1);
			
			long time2 = System.nanoTime();
			
			this.activeRuns.remove(tempRun);
			this.toDeleteRuns.remove(0);
			
			Profiling.timeOnRemoveRuns += (System.nanoTime() - time2);
			
			long time3 = System.nanoTime();
			partitionedRuns = this.activeRunsByPartition.get(tempRun.getPartitonId());
			partitionedRuns.remove(tempRun);
			if(partitionedRuns.size() == 0){
				this.activeRunsByPartition.remove(partitionedRuns);
			}
			Profiling.timeOnRemoveRunsFromPartition += (System.nanoTime() - time3);
			
		}
		*/

	}


	/**
	 * @return the input
	 */
	public Stream getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(Stream input) {
		this.input = input;
	}

	/**
	 * @return the buffer
	 */
	public EventBuffer getBuffer() {
		return buffer;
	}

	/**
	 * @param buffer the buffer to set
	 */
	public void setBuffer(EventBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * @return the nfa
	 */
	public NFA getNfa() {
		return nfa;
	}

	/**
	 * @param nfa the nfa to set
	 */
	public void setNfa(NFA nfa) {
		this.nfa = nfa;
	}

	/**
	 * @return the engineRunController
	 */
	public RunPool getEngineRunController() {
		return engineRunController;
	}

	/**
	 * @param engineRunController the engineRunController to set
	 */
	public void setEngineRunController(RunPool engineRunController) {
		this.engineRunController = engineRunController;
	}


	/**
	 * @return the activeRuns
	 */
	public ArrayList<Run> getActiveRuns() {
		return activeRuns;
	}

	/**
	 * @param activeRuns the activeRuns to set
	 */
	public void setActiveRuns(ArrayList<Run> activeRuns) {
		this.activeRuns = activeRuns;
	}

	/**
	 * @return the matches
	 */
	public MatchController getMatches() {
		return matches;
	}

	/**
	 * @param matches the matches to set
	 */
	public void setMatches(MatchController matches) {
		this.matches = matches;
	}


	/**
	 * @return the toDeleteRuns
	 */
	public ArrayList<Run> getToDeleteRuns() {
		return toDeleteRuns;
	}

	/**
	 * @param toDeleteRuns the toDeleteRuns to set
	 */
	public void setToDeleteRuns(ArrayList<Run> toDeleteRuns) {
		this.toDeleteRuns = toDeleteRuns;
	}
	/**
	 * @return the activeRunsByPartiton
	 */
	public HashMap<Integer, ArrayList<Run>> getActiveRunsByPartiton() {
		return activeRunsByPartition;
	}
	/**
	 * @param activeRunsByPartiton the activeRunsByPartiton to set
	 */
	public void setActiveRunsByPartiton(
			HashMap<Integer, ArrayList<Run>> activeRunsByPartiton) {
		this.activeRunsByPartition = activeRunsByPartiton;
	}
	/**
	 * @return the activeRunsByPartition
	 */
	public HashMap<Integer, ArrayList<Run>> getActiveRunsByPartition() {
		return activeRunsByPartition;
	}
	/**
	 * @param activeRunsByPartition the activeRunsByPartition to set
	 */
	public void setActiveRunsByPartition(
			HashMap<Integer, ArrayList<Run>> activeRunsByPartition) {
		this.activeRunsByPartition = activeRunsByPartition;
	}
	/**
	 * @return the negationEvents
	 */
	public ArrayList<Event> getNegationEvents() {
		return negationEvents;
	}
	/**
	 * @param negationEvents the negationEvents to set
	 */
	public void setNegationEvents(ArrayList<Event> negationEvents) {
		this.negationEvents = negationEvents;
	}
	/**
	 * @return the negationEventsByPartition
	 */
	public HashMap<Integer, ArrayList<Event>> getNegationEventsByPartition() {
		return negationEventsByPartition;
	}
	/**
	 * @param negationEventsByPartition the negationEventsByPartition to set
	 */
	public void setNegationEventsByPartition(
			HashMap<Integer, ArrayList<Event>> negationEventsByPartition) {
		this.negationEventsByPartition = negationEventsByPartition;
	}
	
	


}
