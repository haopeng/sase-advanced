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
package edu.umass.cs.sase.util.csv;

public class CombinationForCorrelation {
	public String numberToLetter(int num, int startC){
		num = num + (int)(startC - 'A');
		if(num == 0){
			return "A";
		}
		StringBuilder sb = new StringBuilder();
		int r = num % 26;
		num = num / 26;
		char c = (char)('A' + r);
		sb.append(c);
		while(num > 0){
			r = num % 26;
			num = num / 26;
			c = (char)('A' + r - 1);
			sb.insert(0, c);
		}
		
		return sb.toString();
	}
	
	
	public void simpleCombination(int startNum, int largestNum, char startC, String sheetName, int startRow, int endRow){
		for(int i = startNum; i <= largestNum; i ++){
			String a = this.numberToLetter(i, startC);
			for(int j = i + 1; j<= largestNum; j ++){
				String b = this.numberToLetter(j, startC);
				System.out.println(i + "\t" + j + "\t" + "=CORREL(" +sheetName + "!" + a + startRow + ":" + sheetName + "!" + a + endRow +"," + sheetName + "!" + b + startRow +":" + sheetName + "!" + b + endRow +")" );
				
			}
		}
	}
	
	public void combinationForArray(int[] numbers, char startC, String sheetName, int startRow, int endRow){
		for(int i = 0; i < numbers.length; i ++){
			int numA = numbers[i];
			String a = this.numberToLetter(numA, startC);
			for(int j = i + 1; j < numbers.length; j ++){
				int numB = numbers[j];
				String b = this.numberToLetter(numB, startC);
				System.out.println(numA + "\t" + numB + "\t" + "=CORREL(" +sheetName + "!" + a + startRow + ":" + sheetName + "!" + a + endRow +"," + sheetName + "!" + b + startRow +":" + sheetName + "!" + b + endRow +")" );
			}
		}
	}
	
	public static void main(String args[]){
		CombinationForCorrelation c = new CombinationForCorrelation();
		//c.simpleCombination(0, 153 , 'B', "allPoints", 3, 107);
		
		
		//int numbers[] = {0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 23, 28, 20, 25, 34, 14, 15, 21, 22, 132, 145};
		//int numbers[] = {0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 27, 138, 141, 153}; // ground truth for other jobs using up resources
		int numbers[] = {7,	8,	9,	10,	11,	12,	13, 27};
		c.combinationForArray(numbers, 'B', "allPoints", 3, 107);
		
		/*
		for(int i = 0; i < 150; i ++){
			System.out.println(i + "\t" + c.numberToLetter(i, 'A'));
		}
		*/
		
		/*
		int num = 10;
		for(int i = 0; i < num - 1; i ++){
			for(int j = i + 1; j< num; j ++){
				char a = (char)('A' + i);
				char b = (char)('A' + j);
				System.out.println(i + "\t" + j + "\t" + "=CORREL(" + a + "3:" + a + "107," + b + "3:" + b + "107)" );
				
			}
		}
		*/
	}

}
