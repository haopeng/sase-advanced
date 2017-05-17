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

public class EnumerationInstance {
	//save event ids for kleene closure
	ArrayList<Integer> ids;
	ArrayList<Boolean> flags;
	boolean previousEvaluationResult;
	
	//
	int[] eventIdsBeforeKleene;
	int[] eventIdsAfterKleene;
	
	boolean afterKleeneSafeFlags[];
	boolean afterKleeneSafeFlagValidateResult;
	
	
	public EnumerationInstance(){
		this.ids = new ArrayList<Integer>();
		this.flags = new ArrayList<Boolean>();
	}
	public EnumerationInstance(EnumerationInstance oldInstance){
		this.ids = new ArrayList<Integer>(oldInstance.getIds());
		this.setPreviousEvaluationResult(oldInstance.isPreviousEvaluationResult());
		this.flags = new ArrayList<Boolean>(oldInstance.getFlags());
		this.eventIdsBeforeKleene = oldInstance.getEventIdsBeforeKleene();
		this.eventIdsAfterKleene = oldInstance.getEventIdsAfterKleene();
		this.afterKleeneSafeFlags = oldInstance.getAfterKleeneSafeFlags();
		this.afterKleeneSafeFlagValidateResult = oldInstance.isAfterKleeneSafeFlagValidateResult();
	}
	
	public int getNumberOfAllEvents(){
		int total = this.eventIdsBeforeKleene.length + this.ids.size() + this.eventIdsAfterKleene.length;
		return total;
	}
	
	public int getCountForEvents(){
		return this.ids.size();
	}
	public void addEvent(int eventId){
		this.ids.add(eventId);
	}
	public void addFlag(boolean flag){
		
	}
	public ArrayList<Integer> getIds() {
		return ids;
	}

	public void setIds(ArrayList<Integer> ids) {
		this.ids = ids;
	}

	public boolean isPreviousEvaluationResult() {
		return previousEvaluationResult;
	}

	public void setPreviousEvaluationResult(boolean previousEvaluationResult) {
		this.previousEvaluationResult = previousEvaluationResult;
	}
	public ArrayList<Boolean> getFlags() {
		return flags;
	}
	public void setFlags(ArrayList<Boolean> flags) {
		this.flags = flags;
	}

	public int[] getEventIdsBeforeKleene() {
		return eventIdsBeforeKleene;
	}
	public void setEventIdsBeforeKleene(int[] eventIdsBeforeKleene) {
		this.eventIdsBeforeKleene = eventIdsBeforeKleene;
	}
	public int[] getEventIdsAfterKleene() {
		return eventIdsAfterKleene;
	}
	public void setEventIdsAfterKleene(int[] eventIdsAfterKleene) {
		this.eventIdsAfterKleene = eventIdsAfterKleene;
	}
	public boolean[] getAfterKleeneSafeFlags() {
		return afterKleeneSafeFlags;
	}
	public void setAfterKleeneSafeFlags(boolean[] afterKleeneSafeFlags) {
		this.afterKleeneSafeFlags = afterKleeneSafeFlags;
	}
	public boolean isAfterKleeneSafeFlagValidateResult() {
		return afterKleeneSafeFlagValidateResult;
	}
	public void setAfterKleeneSafeFlagValidateResult(
			boolean afterKleeneSafeFlagValidateResult) {
		this.afterKleeneSafeFlagValidateResult = afterKleeneSafeFlagValidateResult;
	}
	
	
	
}
