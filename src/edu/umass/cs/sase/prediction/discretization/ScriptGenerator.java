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

public class ScriptGenerator {
	public static void generateAlpha(int index, int start, int end, int step) {
		int a =0, b=0, c = 0;
		for (int i = start; i <= end; i += step) {
			switch (index) {
				case 1:
					a = i;
					break;
				case 2:
					b = i;
					break;
				case 3:
					c = i;
					break;
				default:
					break;
			}
			System.out.println("echo \" alpha1=" + a + ", alpha2 =" + b + ",alpha3 = " + c + "\"");
			System.out.println("java -Xms100m -Xmx8000m edu.umass.cs.sase.prediction.discretization.ScriptInterface " + a + " " + b + " " + c + " $HOME/data2015/featureIndex.csv $HOME/data2015/mantarasdistance.txt $HOME/data2015/m14/all $HOME/data2015/m14/all $HOME/data2015/GroundTruth.csv >>" + index + ".result");
			System.out.println();
		}
	}

	public static void main(String[] args) {
		generateAlpha(1, 0, 200, 5);
	}
}
