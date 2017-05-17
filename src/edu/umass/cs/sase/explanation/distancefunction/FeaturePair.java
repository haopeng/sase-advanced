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


//value feature pair
public class FeaturePair implements Comparable{
	Feature feature1;
	Feature feature2;
	
	double distance;
	double normalizedDistance;
	
	public FeaturePair(Feature f1, Feature f2) {
		this.feature1 = f1;
		this.feature2 = f2;
		
		this.computeDistance();
	}
	
	public void computeDistance() {
		if (this.feature1.isNull() || this.feature2.isNull()) {// special case
			this.distance = 0;
			this.normalizedDistance = 0;
			return;
		}

		
		
		double v1 = this.feature1.getValue();
		double v2 = this.feature2.getValue();
		
		if (v1 == v2) {
			distance = 0;
			normalizedDistance = 0;
		} else {
			this.distance = Math.abs(v1 - v2);
			
			//nomalization function: |f1 - f1|/2Max(|f1|,|f2|). dealing with negative values
			
			double denominator = 2 * Math.max(Math.abs(v1), Math.abs(v2));
			this.normalizedDistance = this.distance / denominator;
		}
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		FeaturePair p = (FeaturePair)o;
		
		double diff = this.normalizedDistance - p.getNormalizedDistance();
		
		if (diff > 0) {
			return 1;
		}
		
		if (diff < 0) {
			return -1;
		}
		
		if (diff == 0) {
			return 0;
		}
		
		return 0;
		
	}

	public Feature getFeature1() {
		return feature1;
	}

	public void setFeature1(Feature feature1) {
		this.feature1 = feature1;
	}

	public Feature getFeature2() {
		return feature2;
	}

	public void setFeature2(Feature feature2) {
		this.feature2 = feature2;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getNormalizedDistance() {
		return normalizedDistance;
	}

	public void setNormalizedDistance(double normalizedDistance) {
		this.normalizedDistance = normalizedDistance;
	}
	
	

}
