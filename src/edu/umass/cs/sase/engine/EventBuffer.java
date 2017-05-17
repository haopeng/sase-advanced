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

import java.util.HashMap;

import edu.umass.cs.sase.stream.Event;

/**
 * This class is used to buffer the selected events.
 * @author haopeng
 *
 */
public class EventBuffer {
	/**
	 * The buffered events
	 */
	HashMap<Integer, Event> buffer;
	public EventBuffer(){
		buffer = new HashMap<Integer, Event>();
		
	}
	/**
	 * This method returns the event with the provided event id.
	 * <p> this method is awesome.
	 * @param Id The id of the event you want
	 * @return The event with the Id.
	 */
	public Event getEvent(int Id){
		long time = System.nanoTime();
		Event e = buffer.get(Id);
		Profiling.timeOnBuffering +=(System.nanoTime() - time);
		return e;
	}
	/**
	 * Buffers an event
	 * @param e The event to be buffered
	 */
	public void bufferEvent(Event e){
		long time = System.nanoTime();
		if(buffer.get(e.getId()) == null){
			buffer.put(e.getId(), e);
			}
		Profiling.timeOnBuffering +=(System.nanoTime() - time);
	}
	
	public int getSize(){
		return buffer.size();
	}

}
