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

package edu.umass.cs.sase.prediction.discretization;

/**
 * This class is used to build intervals for each attribute by extracting the cut-points from output file of discretization
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class IntervalExtractor {
	public List<List<CutInterval>> extractIntervals(String filePath) throws IOException {
		List<List<CutInterval>> result = new ArrayList<List<CutInterval>>();
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		do {
			//for each attribute
			List<CutInterval> intervals = new ArrayList<CutInterval>();
			
			CutInterval current = new CutInterval(Double.MAX_VALUE * (-1), Double.MAX_VALUE);
			while((line = br.readLine()) != null && !line.startsWith("Number")) {
				//parse the value
				StringTokenizer st = new StringTokenizer(line, ":");
				st.nextToken();
				double cutValue = Double.parseDouble(st.nextToken().trim());
				
				//add an new interval
				CutInterval newInt = new CutInterval(current.lower, cutValue);
				current.lower = cutValue;
				intervals.add(newInt);
			}
			
			if (line != null) { // summary of an attribute
				intervals.add(current);
				result.add(intervals);
			}
			
		} while (line != null);
		
		br.close();
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		String filePath = "C:\\users\\haopeng\\Desktop\\extract\\fayyad.txt";
		
		IntervalExtractor ie = new IntervalExtractor();
		List<List<CutInterval>> result = ie.extractIntervals(filePath);
		
		int i = 0;
		for (List<CutInterval> list : result) {
			System.out.print("Interval for attribute " + i);
			for (CutInterval interval : list) {
				System.out.print("[" + interval.lower + "," + interval.upper + ")" + "\t");
			}
			System.out.println();
			i ++;
		}
		
	}

}
