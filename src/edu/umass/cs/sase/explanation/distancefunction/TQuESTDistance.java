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

import core.Trajectory;
import core.distance.TQuESTOperator;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;

public class TQuESTDistance extends DistanceFunction{

	@Override
	public double getTimeSeiresDistance(TimeSeriesFeature f1,
			TimeSeriesFeature f2) throws Exception {
		// TODO Auto-generated method stub
		if (f1.isNull() || f2.isNull() || (f1.getValues().size() == 0 && f2.getValues().size() == 0)) {
			return 0;
		}

		
		f1.convertToPointRepresentation();
		f2.convertToPointRepresentation();
		
		if (f1.getPointRepresentation().size() == 0 && f2.getPointRepresentation().size() == 0) {
			return 0;
		}

		// the lengths do not need to be the same
		
		TQuESTOperator operator = new TQuESTOperator();
		
		Trajectory t1 = new Trajectory(1, f1.getPointRepresentation(), operator);
		Trajectory t2 = new Trajectory(1, f2.getPointRepresentation(), operator);
		
		System.out.println("Debug: TQuESTDistance Feature name:" + f1.getFeatureName());
		if (f1.getFeatureName().equalsIgnoreCase("PullPeriod-value")) {
			System.out.println();
		}
		
		
		return t1.getDistance(t2);
	}

}
