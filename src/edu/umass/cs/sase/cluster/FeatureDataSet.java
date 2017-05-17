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
package edu.umass.cs.sase.cluster;

import java.util.List;

public class FeatureDataSet {
	int id;
	String featureName;
	List<Double> values;
	double mean;
	double meanSquareSumRoot;
	
	
	public FeatureDataSet(int i, String f, List<Double> v) {
		this.id = i;
		this.featureName = f;
		this.values = v;
	}
	
	public void computeMean() {
		if (id == 6) {
			System.out.println();
		}
		
		double sum = 0;
		for (Double d : values) {
			sum += d;
		}
		this.mean = sum / this.values.size();
		
		//mean square root sum
		//http://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient
		double total = 0;
		for (Double d: values) {
			double xi = d - this.mean;
			total += xi * xi;
		}
		this.meanSquareSumRoot = Math.sqrt(total);
		
		/*
		if (this.id == 0) {
			System.out.println("mean = " + this.mean);
			System.out.println("mean square sum root = " + this.meanSquareSumRoot);
		}
		*/
	}

	
	public void printInfo() {
		System.out.println("Id = " + this.id);
		System.out.println("Mean = " + this.mean);
		System.out.println("mean square root sum=" + this.meanSquareSumRoot);
		System.out.println("total values:" + this.values.size());
		System.out.println();
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public List<Double> getValues() {
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getMeanSquareSumRoot() {
		return meanSquareSumRoot;
	}

	public void setMeanSquareSumRoot(double meanSquareSumRoot) {
		this.meanSquareSumRoot = meanSquareSumRoot;
	}
	
	
	
}
