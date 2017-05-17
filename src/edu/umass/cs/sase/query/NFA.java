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
package edu.umass.cs.sase.query;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import edu.umass.cs.sase.engine.ConfigFlags;

/**
 * This class represents an NFA.
 * @author haopeng
 *
 */
public class NFA {
	/**
	 * The states
	 */
	State[] states;
	
	/**
	 * The number of states
	 */
	int size = 0; 
	
	/**
	 * The selection strategy
	 */
	String selectionStrategy;
	
	/**
	 * The time window
	 */
	int timeWindow;
	
	/**
	 * Flag denoting whether the query needs value vector
	 * It is needed when there are aggregates or parameterized predicates
	 */
	boolean needValueVector;
	
	/**
	 * The value vectors for the computation state
	 */
	ValueVectorTemplate[][] valueVectors;
	
	/**
	 * Denoting whether the query has value vectors
	 */
	boolean hasValueVector[];
	
	/**
	 * Specifies the partiton attribute, only for partiton-contiguity selection strategy.
	 */
	String partitionAttribute;// this is used only when we use partition-contiguity selection strategy
	/**
	 * Flag denoting whether the query has a partition attribute
	 */
	boolean hasPartitionAttribute;
	/**
	 * Store other partition attributes except for the first one, if any
	 */
	ArrayList<String> morePartitionAttribute;
	/**
	 * Flag denoting whether the query has more than one partition attributes
	 */
	boolean hasMorePartitionAttribute;
	/**
	 * Flag denoting whether the query has a negation component
	 */
	boolean hasNegation;
	/**
	 * The negation state
	 */
	State negationState;
	
	boolean hasKleeneClosure;
	
	int kleeneClosureStatePosition;//
	
	boolean isSafe;
	
	// for postponeOptimization
	ValueVectorTemplate[][] postponeOptimizedValueVectorTemplates;
	boolean needExtraValueVector;
	boolean hasExtraValueVector[];
	
	//boolean hasMultipleKleeneClosure;//
	//int numOfKleene;
	
	/**
	 * Index for quick checking of edges for zstream
	 */
	HashMap<String, Edge> edgeIndex;
	String lastEdgeTag;
	
	/**
	 * This type represents the most important type among all predicates.
	 * For example, if one only includes True-false, then it is true-false.
	 * If it include both true-false and false, it is false.
	 * If it include both inconsistent and true-false, it is inconsistent
	 * One nfa at most include one type from true/false/inconsistent
	 */
	KleeneClosurePredicateType kTypeForNFA;
	
	/**
	 * Constructs an NFA from a file
	 * @param nfaFile the nfa file
	 */
	public NFA(String nfaFile){
		parseNfaFile(nfaFile);
		this.testNegation();
		this.testKleeneClosure();
		this.compileValueVectorOptimized();
		
		if(ConfigFlags.usingPostponingOptimization){
			this.categorizePredicate();
			this.compileValueVectorPostponeOptimized();
			
			this.computeHelpValuesForPostponing();
		}
		
		this.buildEdgeIndex();//used by events from EventStackGroup
		
		
	}
	public void buildEdgeIndex(){
		this.edgeIndex = new HashMap<String, Edge>();
		for(int i = 0; i < this.size; i ++){
			State s = this.getStates(i);
			Edge[] edges = s.getEdges();
			for(int j = 0; j < edges.length; j ++){
				Edge e = edges[j];
				String tag = e.getEdgeTag();
				this.edgeIndex.put(tag, e);
				this.lastEdgeTag = tag;
			}
		}
	}
	
	public Edge getEdgeByTad(String edgeTag){
		Edge e = null;
		e = this.edgeIndex.get(edgeTag);
		return e;
	}
	/**
	 * Constructs an NFA from a file, specifies the selection strategy
	 * @param selectionStrategy
	 * @param nfaFile
	 */
	public NFA(String selectionStrategy, String nfaFile){
		this.selectionStrategy = selectionStrategy;
		parseNfaFile(nfaFile);
		this.compileValueVectorOptimized();
		
	}
	
	/**
	 * Parses the nfa file
	 * @param nfaFile the nfa file
	 */
	public void parseNfaFile(String nfaFile) {
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(nfaFile));
			line = br.readLine();
			if(line.startsWith("SelectionStrategy") || line.startsWith("selectionStrategy")){
			// parse the descriptive format
			// next line, parse the configuration parameters
			parseNfaConfig(line);
			// count the size of nfa
			while ((line = br.readLine())!=null){
				if (line.equalsIgnoreCase("end"))
					break;
				else
					size ++;
			}			
			states = new State[size];
			
			//parse each state			
			br = new BufferedReader(new FileReader(nfaFile));
			br.readLine();// pass the configuration line;
			
			int counter = 0;
			while((line = br.readLine())!=null){
				
				if (line.equalsIgnoreCase("end")) // reads the end of nfa file
					break;
				else 
				{
					states[counter] = new State(line.trim(), counter);// starts with 0
					counter ++;
				}	
				
			}
			}
			else if(line.startsWith("PATTERN")){
				// parse the simpler format
				this.morePartitionAttribute = new ArrayList<String>();
				this.hasMorePartitionAttribute = false;
				do {
					this.parseFastQueryLine(line);
				}
				while((line = br.readLine()) != null);
				if(this.hasMorePartitionAttribute){
					this.addMorePartitionAttribute();
				}
			}
			//br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{

		}
		
		if(this.size > 0){
		states[0].setStart(true);
		states[size-1].setEnding(true);
		}
	}
	/**
	 * Parses each line for the fast query format
	 * @param line
	 */
	public void parseFastQueryLine(String line){
		if(line.startsWith("PATTERN")){
			this.parseFastQueryLineStartWithPattern(line);
		}else if(line.startsWith("WHERE")){
			// parse the selection strategy
			StringTokenizer st = new StringTokenizer(line);
			st.nextToken();
			this.selectionStrategy = st.nextToken().trim();
		}else if(line.startsWith("AND")){
			this.parseFastQueryLineStartWithAND(line);
		}else if(line.startsWith("WITHIN")){
			// parse the time window
			StringTokenizer st = new StringTokenizer(line);
			st.nextToken();
			this.timeWindow = Integer.parseInt(st.nextToken().trim());
		}
	}
	/**
	 * Parses the query sequence
	 * @param line
	 */
	public void parseFastQueryLineStartWithPattern(String line){
		String seq = line.substring(line.indexOf('(') + 1, line.indexOf(')'));
		StringTokenizer st = new StringTokenizer(seq, ",");
		this.size = st.countTokens();
		//System.out.println(size);
		this.states = new State[size];
		String state;
		for(int i = 0; i < size; i ++){
			boolean isKleeneClosure = false;
			boolean isNegation = false;
			state = st.nextToken();
			
			StringTokenizer stateSt = new StringTokenizer(state);
			String eventType = stateSt.nextToken().trim();
			String stateTag = stateSt.nextToken().trim();
			if(eventType.contains("+")){
				isKleeneClosure = true;
				eventType = eventType.substring(0, eventType.length() - 1);// the first letter
				stateTag = stateTag.substring(0, 1);
			}else if(eventType.contains("!")){
				isNegation = true;
				eventType = eventType.substring(1, eventType.length());
				
			}
			
			//System.out.println("The tag for state " + i + " is " + stateTag);
			if(isKleeneClosure){
				
				this.states[i] = new State(i + 1, stateTag, eventType, "kleeneClosure");
			}else if(isNegation){
				this.states[i] = new State(i + 1, stateTag, eventType, "negation");
			}else{
				this.states[i] = new State (i + 1, stateTag, eventType, "normal");
			}
		}
	}
	/**
	 * Parses the conditions starting with "AND", it might be the partition attribute, or predicates for states
	 * @param line
	 */
	public void parseFastQueryLineStartWithAND(String line){
		StringTokenizer st = new StringTokenizer(line);
		st.nextToken();
		String token = st.nextToken().trim();  
		if(token.startsWith("[")){
			//the partition attribute
			if(!this.hasPartitionAttribute){
			this.partitionAttribute = token.substring(1, token.length() - 1);
			this.hasPartitionAttribute = true;
			}else{
				this.hasMorePartitionAttribute = true;
				this.morePartitionAttribute.add(token.substring(1, token.length() - 1));
			}
		}else {
			char initial = token.charAt(0);
			int stateNum = initial - 'a';//determine which state this predicate works for according to the initial
			this.states[stateNum].addPredicate(line.substring(3).trim());
		}
	}
	/**
	 * Adds other partition attributes except for the first to each state
	 */
	public void addMorePartitionAttribute(){
		String tempPredicate;
		for(int i = 0; i < this.morePartitionAttribute.size(); i ++){
			tempPredicate = this.morePartitionAttribute.get(i)+"=$1." + this.morePartitionAttribute.get(i);//?
			for(int j = 1; j < this.size; j ++){
				State tempState = this.getStates(j);
				for(int k = 0; k < tempState.getEdges().length; k ++){
					Edge tempEdge = tempState.getEdges(k);
					tempEdge.addPredicate(tempPredicate);
				}
			}
		}
	}
	/**
	 * Parses the configuration line in the nfa file
	 * @param line
	 */
	public void parseNfaConfig(String line){
		StringTokenizer st = new StringTokenizer(line, "|");
		while(st.hasMoreTokens()){
			parseConfig(st.nextToken().trim());
		}
		
	}
	
	/**
	 * Parses a configuration, now we have selection strategy, time window and partiton attribute
	 * @param attribute a configuration
	 */
	public void parseConfig(String attribute){
		StringTokenizer st = new StringTokenizer(attribute, "=");
		String left = st.nextToken().trim();
		String right = st.nextToken().trim();
		if(left.equalsIgnoreCase("selectionStrategy")){
			this.selectionStrategy = right;
		}else if(left.equalsIgnoreCase("timeWindow")){
			this.timeWindow = Integer.parseInt(right);
		}else if(left.equalsIgnoreCase("partitionAttribute")){
			this.partitionAttribute = right;
			ConfigFlags.partitionAttribute = right;
			this.hasPartitionAttribute = true;
		}
	}
	/**
	 * Tests whether the query contains a negation component
	 */
	public void testNegation(){
		this.hasNegation = false;
		for(int i = 0; i < this.size; i ++){
			if(this.getStates(i).stateType.equalsIgnoreCase("negation")){
				this.hasNegation = true;
				this.negationState = this.getStates(i);
			}
		}
		if(this.hasNegation){
		int negationOrder = this.negationState.getOrder()- 1;
		State newState[] = new State[this.size - 1];
		for(int i = 0; i < this.size - 1; i ++){
			if(i < negationOrder){
				newState[i] = this.getStates(i);
				if(i == negationOrder - 1){
					newState[i].setBeforeNegation(true);
				}
			}else {
				newState[i] = this.getStates(i + 1);
				if(i == negationOrder ){
					newState[i].setAfterNegation(true);
				}
			
			}
		}
		this.size = this.size - 1;
		this.setStates(newState);
		}
		
	}
	
	public void testKleeneClosure(){
		this.hasKleeneClosure = false;
		//this.numOfKleene = 0;
		for(int i = 0; i < this.size; i ++){
			if(this.getStates(i).stateType.equalsIgnoreCase("kleeneClosure")){
				this.hasKleeneClosure = true;
				//this.numOfKleene ++;
				//this.negationState = this.getStates(i);
			}
		}
		/*
		if(this.numOfKleene > 1){
			this.hasMultipleKleeneClosure = true;
			ConfigFlags.multipleKleeneClosure = true;
		}
		*/
		
	}
	
	/**
	 * Compiles the value vector based on the nfa
	 */
	
	public void compileValueVectorOptimized(){
		this.valueVectors = new ValueVectorTemplate[this.size][];
		ArrayList<ValueVectorTemplate> valueV = new ArrayList<ValueVectorTemplate>();
		int counter[] = new int[this.size];
		for(int i = 0; i < this.size; i ++){
			counter[i] = 0;
		}
		for(int i = 0; i < this.getSize(); i ++){
			State tempState = this.getStates(i);
			
			for(int j = 0; j < tempState.getEdges().length; j ++){
				Edge tempEdge = tempState.getEdges(j);
				for(int k = 0; k < tempEdge.getPredicates().length; k ++){
					PredicateOptimized tempPredicate = tempEdge.getPredicates()[k];
					if(!tempPredicate.isSingleState()){
						String operationName = tempPredicate.getOperation();
						String attributeName = tempPredicate.getAttributeName();
						int stateNumber;
						if(tempPredicate.getRelatedState().equals("previous")){
							stateNumber = i - 1;
						}else{
							stateNumber = Integer.parseInt(tempPredicate.getRelatedState()) - 1;
						}
						valueV.add(new ValueVectorTemplate(stateNumber,attributeName, operationName,i));
						counter[stateNumber]++;
				
				}
			}
		}
						
		}
		
		//set the needValueVector flag as true
		if(valueV.size() > 0){
			this.needValueVector = true;
		}
		//put the value vector tempate to each state
		for(int i = 0; i < this.size ; i ++){
			this.valueVectors[i] = new ValueVectorTemplate[counter[i]];
			ValueVectorTemplate temp;
			int count = 0;
			for(int j = 0; j < valueV.size(); j ++){
				temp = valueV.get(j);
				if(temp.getState() == i){
					this.valueVectors[i][count] = temp;
					count ++;
				}
			}
		}
		this.hasValueVector = new boolean[this.size];
		for(int i = 0; i < this.size; i ++){
			if(counter[i]>0){
				this.hasValueVector[i] = true;
			}else{
				this.hasValueVector[i] = false;
			}
		}
		
		
		
	}
	
	
	/**
	 * Compiles the value vector based on the nfa
	 */
	
	public void compileValueVectorPostponeOptimized(){
		this.postponeOptimizedValueVectorTemplates	 = new ValueVectorTemplate[this.size][];
		ArrayList<ValueVectorTemplate> valueV = new ArrayList<ValueVectorTemplate>();
		int counter[] = new int[this.size];
		for(int i = 0; i < this.size; i ++){
			counter[i] = 0;
		}
		for(int i = 0; i < this.getSize(); i ++){
			State tempState = this.getStates(i);
			
			for(int j = 0; j < tempState.getEdges().length; j ++){
				Edge tempEdge = tempState.getEdges(j);
				for(int k = 0; k < tempEdge.getPredicates().length; k ++){
					PredicateOptimized tempPredicate = tempEdge.getPredicates()[k].getFurtherCheckingPredicate();
					if(tempPredicate!= null && !tempPredicate.isSingleState()){
						String operationName = tempPredicate.getOperation();
						String attributeName = tempPredicate.getAttributeName();
						int stateNumber;
						if(tempPredicate.getRelatedState().equals("previous")){
							stateNumber = i - 1;
						}else{
							stateNumber = Integer.parseInt(tempPredicate.getRelatedState()) - 1;
						}
						valueV.add(new ValueVectorTemplate(stateNumber,attributeName, operationName,i));
						counter[stateNumber]++;
				
				}
			}
		}
						
		}
		
		//set the needValueVector flag as true
		if(valueV.size() > 0){
			this.needExtraValueVector = true;
		}
		//put the value vector tempate to each state
		for(int i = 0; i < this.size ; i ++){
			this.postponeOptimizedValueVectorTemplates[i] = new ValueVectorTemplate[counter[i]];
			ValueVectorTemplate temp;
			int count = 0;
			for(int j = 0; j < valueV.size(); j ++){
				temp = valueV.get(j);
				if(temp.getState() == i){
					this.postponeOptimizedValueVectorTemplates[i][count] = temp;
					count ++;
				}
			}
		}
		this.hasExtraValueVector = new boolean[this.size];
		for(int i = 0; i < this.size; i ++){
			if(counter[i]>0){
				this.hasExtraValueVector[i] = true;
			}else{
				this.hasExtraValueVector[i] = false;
			}
		}
		
		
		
	}
	
	public void computeHelpValuesForPostponing(){
		// the position of Kleene closure
		for(int i = 0; i < this.size; i ++){
			State s = this.getStates(i);
			if(s.isKleeneClosure()){
				this.kleeneClosureStatePosition = i;
			}
		}
		
		//safe positions
		
		//unsafe positions
		
		//all safe
		for(int i = 0; i < this.size; i ++){
			State s = this.getStates(i);
			for(int j = 0; j < s.getEdges().length; j ++){
				Edge e = s.getEdges(j);
				for(int k = 0; k < e.getPredicates().length; k ++){
					PredicateOptimized p = e.getPredicates()[k];
					p.computeSafe();
					//System.out.println("p["+k+"] "+p.isSafe);
				}
				e.computeSafe();
				//System.out.println("e["+j+"] " + e.isSafe);
			}
			s.computeSafe();
			//System.out.println("s["+i+"] " + s.isSafe);
		}
	
		this.computeSafe();
		//System.out.println(this.isSafe);
		
	}
	public void computeSafe(){
		for(int i = 0; i < this.size; i ++){
			if(!this.getStates(i).isSafe){
				this.isSafe = false;
				return;
			}
		}
		this.isSafe = true;
	}
	/**
	 * Categorization main method
	 */
	public void categorizePredicate(){
		this.setkTypeForNFA(KleeneClosurePredicateType.Unconfigured);
		for(int i = 0; i < this.size; i ++){
			Edge tempEdges[] = this.states[i].getEdges();
			for(int j = 0; j < tempEdges.length; j ++){
				PredicateOptimized tempPredicates[] = tempEdges[j].getPredicates();
				for(int k = 0; k < tempPredicates.length;k ++){
					int rightStateNumber = rightStateNumber(tempPredicates[k],i);
					KleeneClosurePredicateType type = typeForPredicate(tempPredicates[k],i,rightStateNumber);
					tempPredicates[k].setkType(type);
					//remember the most important type for this nfa
					this.setkTypeForNFA(type);
					//todo
					FurtherCheckingRule rule = this.getFurtherCheckingRuleForPredicate(tempPredicates[k], i, rightStateNumber);
					if(rule != null){
						tempPredicates[k].setFurtherCheckingRule(rule);
						tempPredicates[k].compileFurtherCheckingPredicate();
					}
					
				}
			}
		}
	}
	
	public FurtherCheckingRule getFurtherCheckingRuleForPredicate(PredicateOptimized p, int leftStateNumber, int rightStateNumber){
		KleeneClosurePredicateType kType = p.getkType();
		String aggType = null;
		switch(kType){
		case FalseValueConsistent:
			aggType = p.aggregationOperand.aggregation;
			if(aggType.equalsIgnoreCase("max") || aggType.equalsIgnoreCase("sum")){
				return new FurtherCheckingRule(true,"min",
						FurtherCheckingAction.NoReCheck,FurtherCheckingAction.ReCheck);
			}else if(aggType.equalsIgnoreCase("min")){
				return new FurtherCheckingRule(true,"max",
						FurtherCheckingAction.NoReCheck,FurtherCheckingAction.ReCheck);
			}else if(aggType.equalsIgnoreCase("count")){
				return new FurtherCheckingRule(true,"1",
						FurtherCheckingAction.NoReCheck,FurtherCheckingAction.ReCheck);
			}
			break;
		case TrueValueConsistent:
			if(!this.states[leftStateNumber].isKleeneClosure()){
				aggType = p.aggregationOperand.aggregation;
				if(aggType.equalsIgnoreCase("max") || aggType.equalsIgnoreCase("sum")){
					return new FurtherCheckingRule(false,"min",
							FurtherCheckingAction.Pass,FurtherCheckingAction.Discard);
				}else if(aggType.equalsIgnoreCase("min")){
					return new FurtherCheckingRule(false,"max",
							FurtherCheckingAction.Pass,FurtherCheckingAction.Discard);
				}else if(aggType.equalsIgnoreCase("count")){
					return new FurtherCheckingRule(false,"1",
							FurtherCheckingAction.Pass,FurtherCheckingAction.Discard);
				}
			}
			break;
		case Inconsistent:
			if(!this.states[leftStateNumber].isKleeneClosure() 
					&& this.states[rightStateNumber].isKleeneClosure()
					&& p.aggregationOperand.aggregation.equalsIgnoreCase("avg")
					){
				if(p.logicalOperator.equals(">") || p.logicalOperator.equals(">=")){
					return new FurtherCheckingRule(false,"min",
							FurtherCheckingAction.Pass,FurtherCheckingAction.Discard);
				}else if(p.logicalOperator.equals("<") || p.logicalOperator.equals("<=")){
					return new FurtherCheckingRule(false,"max",
							FurtherCheckingAction.Pass,FurtherCheckingAction.Discard);
				}
			}
			break;
		default:
			return null;
			
		}
		
		
		return null;
	}
	
/**
 * 
 * @param p
 * @return the type: true-false value consistent | true value consistent | false value consistent | inconsistent | irrelevant
 */
	public KleeneClosurePredicateType typeForPredicate(PredicateOptimized p, int leftStateNumber, int rightStateNumber){
		if(this.states[leftStateNumber].isKleeneClosure){
			if(rightStateNumber == -1){//constant
				return KleeneClosurePredicateType.TrueFalseValueConsistent ;
			}else if(!this.states[rightStateNumber].isKleeneClosure()){
				return KleeneClosurePredicateType.TrueFalseValueConsistent;
			}else if(leftStateNumber == rightStateNumber){
				if(p.rightOperands.get(0).relatedState.equalsIgnoreCase("$previous")){
					if(p.logicalOperator.equals(">") || p.logicalOperator.equals(">=") || 
							p.logicalOperator.equals("<") | p.logicalOperator.equals("<=")){
						return KleeneClosurePredicateType.TrueValueConsistent;
					}
				}else if(p.aggregationOperand != null){
					String aggType = p.aggregationOperand.aggregation;
					if(aggType.equalsIgnoreCase("max") || aggType.equalsIgnoreCase("sum") 
							|| aggType.equalsIgnoreCase("count")){
						if(p.logicalOperator.equalsIgnoreCase(">") || p.logicalOperator.equalsIgnoreCase(">=")){
							return KleeneClosurePredicateType.TrueValueConsistent;
						}
					}else if(aggType.equalsIgnoreCase("min")){
						if(aggType.equalsIgnoreCase("<") || aggType.equalsIgnoreCase("<=")){
							return KleeneClosurePredicateType.TrueValueConsistent;
						}
					}
				}
			}
		}else{
			if (rightStateNumber > -1 && this.states[rightStateNumber].isKleeneClosure() && p.aggregationOperand != null ){
				String aggType = p.aggregationOperand.aggregation;
				if(aggType.equalsIgnoreCase("max") || aggType.equalsIgnoreCase("sum") 
						|| aggType.equalsIgnoreCase("count")){
					if(p.logicalOperator.equals("<") || p.logicalOperator.equals("<=")){
						return KleeneClosurePredicateType.FalseValueConsistent;
					}else if(p.logicalOperator.equals(">") || p.logicalOperator.equals(">=")){
						return KleeneClosurePredicateType.TrueValueConsistent;
					}
				}else if(aggType.equalsIgnoreCase("min")){
					if(p.logicalOperator.equals("<") || p.logicalOperator.equals("<=")){
						return KleeneClosurePredicateType.TrueValueConsistent;
					}else if(p.logicalOperator.equals(">") || p.logicalOperator.equals(">=")){
						return KleeneClosurePredicateType.FalseValueConsistent;
					}
				}
			}else{
				return KleeneClosurePredicateType.Irrelevant;
			}
			
			
		}
		
		return KleeneClosurePredicateType.Inconsistent;
		
		
		//return null;
	}
/**
 * Whether the related state is K+
 * @param p
 * @param stateNumber
 * @return
 */
	public boolean isRightKleene(PredicateOptimized p, int stateNumber){
		if(p.rightOperands.get(0).relatedState.equalsIgnoreCase( "$previous" )){
			if(this.states[stateNumber].isKleeneClosure){
				return true;
			}else{
				return this.states[stateNumber - 1].isKleeneClosure();
			}
		}else{
			int rightStateNumber = Integer.parseInt(p.rightOperands.get(0).relatedState) - 1;
			return this.states[rightStateNumber].isKleeneClosure();
		}
		
		
	}
	
	public int rightStateNumber(PredicateOptimized p, int leftStateNumber){
		if(p.rightOperands.get(0).relatedState == null){
			return -1;//constant
		}else if(p.rightOperands.get(0).relatedState.equalsIgnoreCase("$previous")
				||p.rightOperands.get(0).relatedState.equalsIgnoreCase("previous")){
			if(this.states[leftStateNumber].isKleeneClosure()){
				return leftStateNumber;
			}else{
				return leftStateNumber - 1;
			}
		}else{
			int rightStateNumber = Integer.parseInt(p.rightOperands.get(0).relatedState) - 1;
			return rightStateNumber;
		}
	}
	/**
	 * Self description
	 */
	public String toString(){
		String temp = "";
		//temp += "I am an NFA";
		temp += "The selection strategy is: " + this.selectionStrategy;
		temp += "\nThe time window is : " + this.timeWindow;
		if(this.size > 0){
		temp += "\nThere are " + this.getStates().length + " states\n";
		for(int i = 0; i < this.getStates().length; i ++){
			temp += ("NO." + i + " state:" + this.getStates(i));
		}
		}
		if(this.hasPartitionAttribute == true){
			System.out.println("The partition attribute is: " + this.partitionAttribute);
		}
		return temp;
	}

	
	/**
	 * @return the states
	 */
	public State[] getStates() {
		return states;
	}
	public State getStates(int order) {
		return states[order];
	}
	/**
	 * @param states the states to set
	 */
	public void setStates(State[] states) {
		this.states = states;
	}
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
	/**
	 * @return the selectionStrategy
	 */
	public String getSelectionStrategy() {
		return selectionStrategy;
	}
	/**
	 * @param selectionStrategy the selectionStrategy to set
	 */
	public void setSelectionStrategy(String selectionStrategy) {
		this.selectionStrategy = selectionStrategy;
	}

	/**
	 * @return the timeWindow
	 */
	public int getTimeWindow() {
		return timeWindow;
	}

	/**
	 * @param timeWindow the timeWindow to set
	 */
	public void setTimeWindow(int timeWindow) {
		this.timeWindow = timeWindow;
	}

	/**
	 * @return the needValueVector
	 */
	public boolean isNeedValueVector() {
		return needValueVector;
	}

	/**
	 * @param needValueVector the needValueVector to set
	 */
	public void setNeedValueVector(boolean needValueVector) {
		this.needValueVector = needValueVector;
	}



	/**
	 * @return the partitionAttribute
	 */
	public String getPartitionAttribute() {
		return partitionAttribute;
	}

	/**
	 * @param partitionAttribute the partitionAttribute to set
	 */
	public void setPartitionAttribute(String partitionAttribute) {
		this.partitionAttribute = partitionAttribute;
	}

	/**
	 * @return the valueVectors
	 */
	public ValueVectorTemplate[][] getValueVectors() {
		return valueVectors;
	}

	/**
	 * @param valueVectors the valueVectors to set
	 */
	public void setValueVectors(ValueVectorTemplate[][] valueVectors) {
		this.valueVectors = valueVectors;
	}

	/**
	 * @return the hasValueVector
	 */
	public boolean[] getHasValueVector() {
		return hasValueVector;
	}

	/**
	 * @param hasValueVector the hasValueVector to set
	 */
	public void setHasValueVector(boolean[] hasValueVector) {
		this.hasValueVector = hasValueVector;
	}

	/**
	 * @return the hasPartitionAttribute
	 */
	public boolean isHasPartitionAttribute() {
		return hasPartitionAttribute;
	}

	/**
	 * @param hasPartitionAttribute the hasPartitionAttribute to set
	 */
	public void setHasPartitionAttribute(boolean hasPartitionAttribute) {
		this.hasPartitionAttribute = hasPartitionAttribute;
	}

	/**
	 * @return the hasNegation
	 */
	public boolean isHasNegation() {
		return hasNegation;
	}

	/**
	 * @param hasNegation the hasNegation to set
	 */
	public void setHasNegation(boolean hasNegation) {
		this.hasNegation = hasNegation;
	}

	/**
	 * @return the negationState
	 */
	public State getNegationState() {
		return negationState;
	}

	/**
	 * @param negationState the negationState to set
	 */
	public void setNegationState(State negationState) {
		this.negationState = negationState;
	}
	
	
	public ArrayList<String> getMorePartitionAttribute() {
		return morePartitionAttribute;
	}

	public void setMorePartitionAttribute(ArrayList<String> morePartitionAttribute) {
		this.morePartitionAttribute = morePartitionAttribute;
	}

	public boolean isHasMorePartitionAttribute() {
		return hasMorePartitionAttribute;
	}

	public void setHasMorePartitionAttribute(boolean hasMorePartitionAttribute) {
		this.hasMorePartitionAttribute = hasMorePartitionAttribute;
	}

	public boolean isHasKleeneClosure() {
		return hasKleeneClosure;
	}

	public void setHasKleeneClosure(boolean hasKleeneClosure) {
		this.hasKleeneClosure = hasKleeneClosure;
	}

	
	public ValueVectorTemplate[][] getPostponeOptimizedValueVectorTemplates() {
		return postponeOptimizedValueVectorTemplates;
	}

	public void setPostponeOptimizedValueVectorTemplates(
			ValueVectorTemplate[][] postponeOptimizedValueVectorTemplates) {
		this.postponeOptimizedValueVectorTemplates = postponeOptimizedValueVectorTemplates;
	}

	public boolean isNeedExtraValueVector() {
		return needExtraValueVector;
	}

	public void setNeedExtraValueVector(boolean needExtraValueVector) {
		this.needExtraValueVector = needExtraValueVector;
	}

	public boolean[] getHasExtraValueVector() {
		return hasExtraValueVector;
	}

	public void setHasExtraValueVector(boolean[] hasExtraValueVector) {
		this.hasExtraValueVector = hasExtraValueVector;
	}

	public int getKleeneClosureStatePosition() {
		return kleeneClosureStatePosition;
	}

	public void setKleeneClosureStatePosition(int kleeneClosureStatePosition) {
		this.kleeneClosureStatePosition = kleeneClosureStatePosition;
	}

	public boolean isSafe() {
		return isSafe;
	}

	public void setSafe(boolean isSafe) {
		this.isSafe = isSafe;
	}
	


	public HashMap<String, Edge> getEdgeIndex() {
		return edgeIndex;
	}
	public void setEdgeIndex(HashMap<String, Edge> edgeIndex) {
		this.edgeIndex = edgeIndex;
	}
	
	
	public String getLastEdgeTag() {
		return lastEdgeTag;
	}
	public void setLastEdgeTag(String lastEdgeTag) {
		this.lastEdgeTag = lastEdgeTag;
	}
	
	
	
	public KleeneClosurePredicateType getkTypeForNFA() {
		return kTypeForNFA;
	}
	public void setkTypeForNFA(KleeneClosurePredicateType kTypeForNFA) {
		if(kTypeForNFA == KleeneClosurePredicateType.Unconfigured){
			this.kTypeForNFA = kTypeForNFA;
		}else{
			if(kTypeForNFA.ordinal() > this.kTypeForNFA.ordinal()){
				this.kTypeForNFA = kTypeForNFA;
			}
		}
		
	}
	public static void main(String args[]){
		/*
		String queryFile = "/Users/haopeng/Dropbox/Code/workspace/sase-opensource/queries/querylength/2.nfa";
		//String queryFile = "F:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\false.nfa";
		//String queryFile = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/build/test.nfa";
		queryFile = "C:\\Dropbox\\code\\workspace\\sase2.0\\query\\vldb10\\e31\\5.query";
		NFA newNFA = new NFA(queryFile);
				//System.out.println(newNFA);
		
		
		for(int i = 0; i < newNFA.size; i ++){
			Edge tempEdges[] = newNFA.states[i].getEdges();
			for(int j = 0; j < tempEdges.length; j ++){
				PredicateOptimized tempPredicates[] = tempEdges[j].getPredicates();
				for(int k = 0; k < tempPredicates.length;k ++){
					//System.out.println("Labeling Predicate" + k + " for Edge" + j + " for State" + i);
					System.out.println("Type for predicate--" + tempPredicates[k] + "-- is:" + tempPredicates[k].getkType());
					
					//System.out.println("Left state number is:" + i + " \n Right state number is:" + rightStateNumber(tempPredicates[k],i));
					System.out.println(tempPredicates[k].aggregationOperand);
					
					if(tempPredicates[k].aggregationOperand != null){
						System.out.println(tempPredicates[k].aggregationOperand.aggregation);
						//tempPredicates[k].aggregationOperand.setAggregation("mlb");
						System.out.println(tempPredicates[k].aggregationOperand.aggregation);
					}
					
					System.out.println("Type for predicate--" + tempPredicates[k] + "-- is:" + tempPredicates[k].getkType());
					System.out.println(tempPredicates[k].relatedState);
					System.out.println(tempPredicates[k].rightOperands.get(0).relatedState);
					
					
					System.out.println("FurtherChecking rule is:" + tempPredicates[k].getFurtherCheckingRule());
					
					System.out.println("---------------------");
				}
			}
		}*/
		
		// test k+
		String nfaFile = "F:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity\\1.nfa";
		nfaFile = "F:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-inconsistent-precise\\1.nfa";
		nfaFile = "F:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-true-precise\\1.nfa";
		nfaFile = "F:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity\\1.nfa";
		//nfaFile = "F:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity-false\\1.nfa";
		NFA n = new NFA(nfaFile);
		System.out.println(n.getkTypeForNFA());
		
		/*
		for(int i = 0; i < n.getSize(); i ++){
			System.out.println("State No." + i + " is Kleene?" + n.getStates(i).isKleeneClosure());
			State tempState = n.getStates(i);
			System.out.println("Order:" + tempState.getOrder());
			System.out.println("Number of edges:" + tempState.getEdges().length);
			Edge[] edges = tempState.getEdges();
			for(int j = 0; j < edges.length; j ++){
				Edge e = edges[j];
				System.out.println("EdgeTag is:" + e.getEdgeTag());
				System.out.println("Edge" + j + " has " + e.getPredicates().length + " predicates");
				System.out.println("Is edge single?" + e.isSingle());
			}
			System.out.println();
			System.out.println();
			
			HashMap<String, Edge> index = n.getEdgeIndex();
			for(String key: index.keySet()){
				System.out.println("Edge key:" + key + ", edge" + index.get(key).toString());
			}
		
		}
		*/
	}
	
}
