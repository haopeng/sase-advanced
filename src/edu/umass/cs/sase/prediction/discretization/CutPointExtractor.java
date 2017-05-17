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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class is used to extract number of cut points from the results of Keel.
 * input file sample snippet:
 * Number of cut points of attribute 0 : 0
Number of cut points of attribute 1 : 0
Number of cut points of attribute 2 : 0
Number of cut points of attribute 3 : 0
Cut point 0 of attribute 4 : 0.55
Number of cut points of attribute 4 : 1
Cut point 0 of attribute 5 : 0.55
Number of cut points of attribute 5 : 1
 * @author haopeng
 *
 */

public class CutPointExtractor {
	public List<Integer> getNumOfCutPoints(String filePath) throws IOException {
		List<Integer> result = new ArrayList<Integer>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		
		while((line = br.readLine()) != null) {
			if (!line.startsWith("Number of")) {
				continue;
			}
			
			StringTokenizer st = new StringTokenizer(line, ":");
			st.nextToken();
			result.add(Integer.parseInt(st.nextToken().trim()));
		}
		
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		//String filePath = "C:\\users\\haopeng\\Desktop\\test-class\\1.m14-1R-D\\datasets\\1R-D.m14-keel\\result10e0.txt";
		
		String filePath = "C:\\users\\haopeng\\Desktop\\extract\\result4e0.txt";
		
		CutPointExtractor ex = new CutPointExtractor();
		List<Integer> result = ex.getNumOfCutPoints(filePath);
		
		for (Integer num: result) {
			System.out.print(num + "\t");
		}
	}

}
