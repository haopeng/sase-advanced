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

package edu.umass.cs.sase.explanation.featureselection;

import java.util.ArrayList;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;

public abstract class FeatureFilter {
	ArrayList<TimeSeriesFeaturePair> sortedFeatureList;
	ArrayList<TimeSeriesFeaturePair> selectedFeatures;
	String filterName;
	public FeatureFilter(ArrayList<TimeSeriesFeaturePair> list) {
		this.sortedFeatureList = list;
		this.selectedFeatures = new ArrayList<TimeSeriesFeaturePair>();
	}
	
	public ArrayList<TimeSeriesFeaturePair> returnSelectedFeatures() {
		return this.selectedFeatures;
	}
	public abstract void print();

	/*
	public void print() {
		System.out.println("Selected features by " + this.filterName);
		int count = 0;
		for (TimeSeriesFeaturePair pair: this.filteredFeatures) {
			count ++;
			System.out.println(count + "\t" + pair.getFeatureName() + "\t" +pair.getEntropyDistance());
		}
	}
	*/
}
