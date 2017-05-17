/*
 * Copyright (c) 2017, Regents of the University of Massachusetts Amherst 
 * All rights reserved.

 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:

 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 *      and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *      and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *   * Neither the name of the University of Massachusetts Amherst nor the names of its contributors 
 *      may be used to endorse or promote products derived from this software without specific prior written 
 *      permission.

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

public class EnumerationTool {
	public static void main(String[] args)
    {

// Test case num = 5, outOf = 8.
		
		for(int i = 0; i < 1; i ++){
			System.out.println("cccc");
		}
		//System.out.println(computeNumberOfCombinations(20,1));
		
        int num = 1;
        int outOf = 23;
        int[][] combinations = getCombinations(num, outOf);
        for (int i = 0; i < combinations.length; i++)
        {
            for (int j = 0; j < combinations[i].length; j++)
            {
                System.out.print(combinations[i][j] + " ");
            }
            
        }
    }

    public static int[][] getCombinations(int num, int outOf)
    {
    	long startTime = System.nanoTime();
    	
        int possibilities = computeNumberOfCombinations(outOf, num);
        //System.out.println(possibilities + "," + outOf + "," + num);
        int[][] combinations = new int[possibilities][num];// p result, in each result num numbers
        int arrayPointer = 0;

        int[] counter = new int[num];

        for (int i = 0; i < num; i++)
        {
            counter[i] = i;// initialize the counter for each number
        }
        breakLoop: while (true)
        {
            // Initializing part
            for (int i = 1; i < num; i++)
            {
                if (counter[i] >= outOf - (num - 1 - i))
                    counter[i] = counter[i - 1] + 1;
            }

            // Testing part
            for (int i = 0; i < num; i++)
            {
                if (counter[i] < outOf)
                {
                    continue;
                } else
                {
                    break breakLoop;
                }
            }

            // Innermost part
            combinations[arrayPointer] = counter.clone();
            arrayPointer++;

            // Incrementing part
            counter[num - 1]++;
            for (int i = num - 1; i >= 1; i--)
            {
                if (counter[i] >= outOf - (num - 1 - i))
                    counter[i - 1]++;
            }
        }
        
        long lastTime = System.nanoTime() - startTime;
        Profiling.enumerationTime += lastTime;
        
        return combinations;
    }
/**
 * Get the number of results
 * @param n
 * @param r
 * @return
 */
    /*
    public static int get_nCr(int n, int r)
    {
        if(r > n)
        {
            throw new ArithmeticException("r is greater then n");
        }
        long numerator = 1;
        long denominator = 1;
        for (int i = n; i >= r + 1; i--)
        {
            numerator *= i;
        }
        for (int i = 2; i <= n - r; i++)
        {
            denominator *= i;
        }

        return (int) (numerator / denominator);
    }*/
    
    public static int computeNumberOfCombinations(int n, int r){
    	if(r > n){
    		throw new ArithmeticException("");
    	}
    	if(n - r < r){
    		r = n - r;
    	}
    	
    	long numerator = 1;
    	long denominator = 1;
    	for(int i = n; i >= n - r + 1; i--){
    		numerator *= i;
    	}
    	for(int i = 1; i <= r; i ++){
    		denominator *= i;
    	}
    	
    	return (int)(numerator / denominator);
    }
    
}
