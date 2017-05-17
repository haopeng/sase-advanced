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

package edu.umass.cs.sase.explanation.distancefunction;

import java.util.ArrayList;

import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;


/**
 * reference: http://trac.research.cc.gatech.edu/GART/browser/GART/weka/edu/gatech/gart/ml/weka/DTW.java?rev=9
 * @author haopeng
 *
 */
public class DTWDistance extends DistanceFunction {
	protected double[] seq1;
	protected double[] seq2;
	protected int[][] warpingPath;
	
	protected int n;
	protected int m;
	protected int K;
	
	protected double warpingDistance;
	
	/**
	 * Constructor
	 * 
	 * @param query		
	 * @param templete	
	 */
	public DTWDistance(double[] sample, double[] templete) {
		seq1 = sample;
		seq2 = templete;
		
		n = seq1.length;	
		m = seq2.length;
		K = 1;
		
		warpingPath = new int[n + m][2];	// max(n, m) <= K < n + m
		warpingDistance = 0.0;
		
		this.compute();
	}
	
	public DTWDistance() {
		
	}
	
	public void compute() {
		double accumulatedDistance = 0.0;
		
		double[][] d = new double[n][m];	// local distances
		double[][] D = new double[n][m];	// global distances
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				d[i][j] = distanceBetween(seq1[i], seq2[j]);
			}
		}
		
		D[0][0] = d[0][0];
		
		for (int i = 1; i < n; i++) {
			D[i][0] = d[i][0] + D[i - 1][0];
		}

		for (int j = 1; j < m; j++) {
			D[0][j] = d[0][j] + D[0][j - 1];
		}
		
		for (int i = 1; i < n; i++) {
			for (int j = 1; j < m; j++) {
				accumulatedDistance = Math.min(Math.min(D[i-1][j], D[i-1][j-1]), D[i][j-1]);
				accumulatedDistance += d[i][j];
				D[i][j] = accumulatedDistance;
			}
		}
		accumulatedDistance = D[n - 1][m - 1];

		int i = n - 1;
		int j = m - 1;
		int minIndex = 1;
	
		warpingPath[K - 1][0] = i;
		warpingPath[K - 1][1] = j;
		
		while ((i + j) != 0) {
			if (i == 0) {
				j -= 1;
			} else if (j == 0) {
				i -= 1;
			} else {	// i != 0 && j != 0
				double[] array = { D[i - 1][j], D[i][j - 1], D[i - 1][j - 1] };
				minIndex = this.getIndexOfMinimum(array);

				if (minIndex == 0) {
					i -= 1;
				} else if (minIndex == 1) {
					j -= 1;
				} else if (minIndex == 2) {
					i -= 1;
					j -= 1;
				}
			} // end else
			K++;
			warpingPath[K - 1][0] = i;
			warpingPath[K - 1][1] = j;
		} // end while
		warpingDistance = accumulatedDistance / K;
		
		this.reversePath(warpingPath);
	}
	
	/**
	 * Changes the order of the warping path (increasing order)
	 * 
	 * @param path	the warping path in reverse order
	 */
	protected void reversePath(int[][] path) {
		int[][] newPath = new int[K][2];
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < 2; j++) {
				newPath[i][j] = path[K - i - 1][j];
			}
		}
		warpingPath = newPath;
	}
	/**
	 * Returns the warping distance
	 * 
	 * @return
	 */
	public double getDistance() {
		return warpingDistance;
	}
	
	/**
	 * Computes a distance between two points
	 * 
	 * @param p1	the point 1
	 * @param p2	the point 2
	 * @return		the distance between two points
	 */
	protected double distanceBetween(double p1, double p2) {
		return (p1 - p2) * (p1 - p2);
	}

	/**
	 * Finds the index of the minimum element from the given array
	 * 
	 * @param array		the array containing numeric values
	 * @return				the min value among elements
	 */
	protected int getIndexOfMinimum(double[] array) {
		int index = 0;
		double val = array[0];

		for (int i = 1; i < array.length; i++) {
			if (array[i] < val) {
				val = array[i];
				index = i;
			}
		}
		return index;
	}

	/**
	 *	Returns a string that displays the warping distance and path
	 */
	public String toString() {
		String retVal = "Warping Distance: " + warpingDistance + "\n";
		retVal += "Warping Path: {";
		for (int i = 0; i < K; i++) {
			retVal += "(" + warpingPath[i][0] + ", " +warpingPath[i][1] + ")";
			retVal += (i == K - 1) ? "}" : ", ";
			
		}
		return retVal;
	}
	


	@Override
	public double getTimeSeiresDistance(TimeSeriesFeature f1,
			TimeSeriesFeature f2) {
		if (f1.isNull() || f2.isNull() || f1.getValues().size() == 0 || f2.getValues().size() == 0) {
			return 0;
		}

		//System.out.println("Called");
		double[] values1 = new double[f1.getNormalizedValues().size()];
		for (int i = 0; i < f1.getNormalizedValues().size(); i ++) {
			values1[i] = f1.getNormalizedValues().get(i);
		}
		
		double[] values2 = new double[f2.getNormalizedValues().size()];
		for (int i = 0; i < f2.getNormalizedValues().size(); i ++) {
			values2[i] = f2.getNormalizedValues().get(i);
		}
		
		seq1 = values1;
		seq2 = values2;
		
		n = seq1.length;	
		m = seq2.length;
		
		//System.out.println("feature name=" + f1.getFeatureName() + "n=" + n + "\t,m=" + m);
		
		K = 1;
		
		warpingPath = new int[n + m][2];	// max(n, m) <= K < n + m
		warpingDistance = 0.0;
		
		//System.out.println("Debug: DTWDistance. Feature Name=" + f1.getFeatureName());
		
		if (m == 0 && n == 0) {
			return 0;
		}
		this.compute();
		
		return this.warpingDistance;
	}
	
	
	/**
	 * Tests this class
	 * 
	 * @param args	ignored
	 */
	public static void main(String[] args) {
		double[] n2 = {1.5f, 3.9f, 4.1f, 3.3f};
		double[] n1 = {2.1f, 2.45f, 3.673f, 4.32f, 2.05f, 1.93f, 5.67f, 6.01f};
		
		double[] n3 = {1.0, 1.0, 1, 1};
		double[] n4 = {1, 1, 1};
		
		DTWDistance dtw = new DTWDistance(n1, n2);
		System.out.println(dtw);
		
		DTWDistance dtw2 = new DTWDistance(n3, n4);
		System.out.println(dtw2);
	}
}
