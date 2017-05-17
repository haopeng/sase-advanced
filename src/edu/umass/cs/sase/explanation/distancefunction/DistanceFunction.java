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

import edu.umass.cs.sase.explanation.featuregeneration.Feature;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;


/**
 * To add a new distance function
 * Step1: Add a new class, extending this abstract class
 * Step2: Add a method in TimeSeriesFeaturePair
 * Step3: Add a method in TimeSeriesFeatureComparator
 * @author haopeng
 *
 */

public abstract class DistanceFunction {
	public double getValueDistance(Feature f1, Feature f2) {
		if (f1.isNull() || f2.isNull()) {
			return 0;
		}

		return Math.abs(f1.getValue() - f2.getValue());
	}
	
	public abstract double getTimeSeiresDistance(TimeSeriesFeature f1, TimeSeriesFeature f2) throws Exception;
	
	public void alignTimeSeires(TimeSeriesFeature f1, TimeSeriesFeature f2) {
		int min = Math.min(f1.getValues().size(), f2.getValues().size());
		
		f1.alignValues(min);
		f2.alignValues(min);
		
		//to do later: padding the min one
	}
	
}
