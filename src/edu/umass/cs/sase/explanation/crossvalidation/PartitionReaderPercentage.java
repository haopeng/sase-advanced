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

package edu.umass.cs.sase.explanation.crossvalidation;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;

public class PartitionReaderPercentage {
	public static void main(String[] args) throws Exception {
		//b3_2, labeled high memory, wc-frequent users
		String b3_2Path = "/Users/haopeng/Dropbox/research/3rd/experiment/b3-2/b3-2.csv";
		PartitionReader b3_2 = new PartitionReader(b3_2Path, "b3-2");
		long b3_2CutTimestamp= 593711L;
		PartitionReader b3_2First = new PartitionReader(b3_2, b3_2CutTimestamp, true);
		PartitionReader b3_2Second = new PartitionReader(b3_2, b3_2CutTimestamp, false);
		
		long entireLength = b3_2.getEntireIntervalLength();
		long abnormalLength = b3_2First.getEntireIntervalLength();
		double abnormalPercent = (double)abnormalLength / (double)entireLength;
		
		//b3_3, unlabeled high memory
		String b3_3Path = "/Users/haopeng/Dropbox/research/3rd/experiment/b3-3/b3-3.csv";
		PartitionReader b3_3 = new PartitionReader(b3_3Path, "b3-3");
		PartitionReader b3_3First = new PartitionReader(b3_3, true, abnormalPercent);
		PartitionReader b3_3Second = new PartitionReader(b3_3, false, abnormalPercent);
		
		//b0_1, unlabeled reference
		String b0_1Path = "/Users/haopeng/Dropbox/research/3rd/experiment/b0-1/b0-1.csv";
		PartitionReader b0_1 = new PartitionReader(b0_1Path, "b0-1");
		PartitionReader b0_1First = new PartitionReader(b0_1, true, abnormalPercent);
		PartitionReader b0_1Second = new PartitionReader(b0_1, false,abnormalPercent);
		
		//b0_2, unlabeled reference
		String b0_2Path = "/Users/haopeng/Dropbox/research/3rd/experiment/b0-2/b0-2.csv";
		PartitionReader b0_2 = new PartitionReader(b0_2Path, "b0-2");
		PartitionReader b0_2First = new PartitionReader(b0_2, true, abnormalPercent);
		PartitionReader b0_2Second = new PartitionReader(b0_2, false, abnormalPercent);
		
		
		//c0_1, different job--wc-sessions
		/*
		String c0_1Path = "/Users/haopeng/Dropbox/research/3rd/experiment/c0-1/c0-1.csv";
		long c0_1CutTimestamp = 0L;
		PartitionReader c0_1 = new PartitionReader(c0_1Path, "c0_1");
		PartitionReader c0_1First = new PartitionReader(c0_1, c0_1CutTimestamp, true);
		PartitionReader c0_1Second = new PartitionReader(c0_1, c0_1CutTimestamp, false);
		*/
		//pairs
		
		//unlabeled reference vs unlabeled reference, expected distance: small|| quite similar two partitions
		TimeSeriesFeaturePair b0_1_b_02 = new TimeSeriesFeaturePair(b0_1.getPartition(), b0_2.getPartition(), "b0_1_b_02"); //useless
		
		
		//b3_2 vs b0_1
		TimeSeriesFeaturePair b3_2First_b0_1First = new TimeSeriesFeaturePair(b3_2First.getPartition(), b0_1First.getPartition(), "b3_2First_b0_1First");//expect: large distance
		
		TimeSeriesFeaturePair b3_2Second_b0_1Second = new TimeSeriesFeaturePair(b3_2Second.getPartition(), b0_1Second.getPartition(), "b3_2Second_b0_1Second");//expect: small distance
		
		//b3_2 vs b0_2
		TimeSeriesFeaturePair b3_2First_b0_2First = new TimeSeriesFeaturePair(b3_2First.getPartition(), b0_2First.getPartition(), "b3_2First_b0_2First");//expect: large distance
		
		TimeSeriesFeaturePair b3_2Second_b0_2Second = new TimeSeriesFeaturePair(b3_2Second.getPartition(), b0_2Second.getPartition(), "b3_2Second_b0_2Second");//expect: small distance
		
		//b3_2 vs b3_3
		TimeSeriesFeaturePair b3_2First_b3_3First = new TimeSeriesFeaturePair(b3_2First.getPartition(), b3_3First.getPartition(), "b3_2First_b3_3First"); // expect: small distance
		
		TimeSeriesFeaturePair b3_2Second_b3_3Second = new TimeSeriesFeaturePair(b3_2Second.getPartition(), b3_3Second.getPartition(), "b3_2Second_b3_3Second");//expect: small distance
		
		//TimeSeriesFeaturePair b3_2_b3_3 = new TimeSeriesFeaturePair(b3_2.getPartition(), b3_3.getPartition(), "b3_2_b3_3");//compare entire and see
		
		//test distances, existing ones
		System.out.println();
		System.out.println("~~~~~~~~~~~");
		System.out.println("PairName\t NumOfPoint1\t NumOfPoint2\tFrequency1\tFrequency2\tManhattanDistance\t EuclideanDistance\t DTW\t LCSS\t EDR\t Swale\t ERP\tTquest\tSpade\tEntropy");
		
		b0_1_b_02.printDistancesForPartitionClassification();
		
		//b3_2 vs b0_1
		b3_2First_b0_1First.printDistancesForPartitionClassification();
		
		b3_2Second_b0_1Second.printDistancesForPartitionClassification();
		
		//b3_2 vs b0_2
		b3_2First_b0_2First.printDistancesForPartitionClassification();		
		b3_2Second_b0_2Second.printDistancesForPartitionClassification();
		
		//b3_2 vs b3_3
		b3_2First_b3_3First.printDistancesForPartitionClassification();
		b3_2Second_b3_3Second.printDistancesForPartitionClassification();
		//b3_2_b3_3.printDistancesForPartitionClassification();
		
		
		//test distance, design a new one, leveled!
		
		
		
		//validate features
		
		//invalidate features
		
		
		//validate correlations
		
		//invalidate correlations
	}

}
