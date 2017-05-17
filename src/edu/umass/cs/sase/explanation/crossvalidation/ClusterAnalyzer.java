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

import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;

public class ClusterAnalyzer {
	//for clustering
	double threshold = 0.3; //distance <= 0.3: the same label; distance >= 1-0.3, the oppositive label; otherwise, mixed label
	
	public ClusterAnalyzer(double threshold) {
		this.threshold = threshold;
	}
	
	public LabelType assignLabel(PartitionReader labeled, PartitionReader toLabel) throws Exception {
		double frequencyDistance = this.computeFrequencyDistance(labeled, toLabel);
		double entropyDistance = this.computeEntropyDistance(labeled, toLabel);
		if (ExplanationSettings.printResult) {
			System.out.println("Frequency distance=\t" + frequencyDistance + "\t entropyDistance=\t" + entropyDistance);			
		}

		
		if (frequencyDistance >= 1- this.threshold || entropyDistance >= 1 - this.threshold) {
			return LabelType.getOpposite(labeled.getPartition().getLabel());//opposite label			
		} else if (frequencyDistance <= this.threshold && entropyDistance <= this.threshold) {
			return labeled.getPartition().getLabel();//same label
		}
		
		return LabelType.Mixed; //mixed means unuseful
	}
	
	
	public double computeFrequencyDistance(PartitionReader p1, PartitionReader p2) {
		double f1 = (double)p1.getCountOfPoints() / (double)p1.getIntervalLength();
		double f2 = (double)p2.getCountOfPoints() / (double)p2.getIntervalLength();
		double d = Math.abs((f1 - f2) / Math.max(f1, f2));
		return d;
	}
	
	public double computeEntropyDistance(PartitionReader p1, PartitionReader p2) throws Exception {
		TimeSeriesFeature f1 = p1.getPartition();
		TimeSeriesFeature f2 = p2.getPartition();
		TimeSeriesFeaturePair pair = new TimeSeriesFeaturePair(f1, f2);
		pair.computeEntropyDistance();
		return pair.getEntropyDistance();
	}
}
