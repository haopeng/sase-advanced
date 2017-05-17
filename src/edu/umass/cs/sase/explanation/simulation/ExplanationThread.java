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

import edu.umass.cs.sase.explanation.engine.ExplainEngine;

public class ExplanationThread implements Runnable{
	public static int count = 0;
	String inputPropertiesFile;
	
	public ExplanationThread(String inputPropertiesFile) {
		this.inputPropertiesFile = inputPropertiesFile;
	}
	
	@Override
	public void run() {
		
		count ++;
		if (count > SimulationSettings.explanationRepeat) {
			return;
		}
		
		SimulationSettings.isExlaining = true;
		long start = System.currentTimeMillis();
		System.out.println("Start to explain:" + this.inputPropertiesFile);
		try {
			ExplainEngine engine = new ExplainEngine(this.inputPropertiesFile);
			engine.runEngineForSimulation();
			System.out.println("Complete to explain:" + this.inputPropertiesFile + "=============================");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long duration = System.currentTimeMillis() - start;
		SimulationSettings.explanationDuration += duration;
		SimulationSettings.isExlaining = false;
		System.out.println("Done with explanation of " + this.inputPropertiesFile);
	}
}
