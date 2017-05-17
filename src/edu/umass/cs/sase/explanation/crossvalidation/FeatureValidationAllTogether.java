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

import java.io.IOException;
import java.util.ArrayList;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeatureComparator;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.PropertyReader;
import edu.umass.cs.sase.explanation.featuregeneration.RawEventReader;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesRawGenerator;
import edu.umass.cs.sase.explanation.featuregeneration.WindowTimeSeriesGenerator;

public class FeatureValidationAllTogether extends FeatureValidation{
	ArrayList<String> classifiedPartitionPropertiesFiles;
	
	ArrayList<TimeSeriesFeatureComparator> classifiedComparators;
	ArrayList<LabelType> firstLabels;
	ArrayList<LabelType> secondLabels;
	public FeatureValidationAllTogether(String labeled, String classified) throws IOException{
		super(labeled, classified);
	}
	public FeatureValidationAllTogether(String labeled, ArrayList<String> classified, ArrayList<LabelType> firstLabels, ArrayList<LabelType> secondLabels) throws IOException {
		super(labeled, labeled);//no use of classified
		
		this.labeledPartitionPropertiesFile = labeled;
		this.classifiedPartitionPropertiesFiles = classified;
		
		this.firstLabels = firstLabels;
		this.secondLabels = secondLabels;
		
		
	}
	
	public void generateFeatures() throws IOException {
		this.labeledComparator = this.readFileAndGenerateTSFeatures(this.labeledPartitionPropertiesFile);
		
		this.classifiedComparators = new ArrayList<TimeSeriesFeatureComparator>();
		for (int i = 0; i < this.classifiedPartitionPropertiesFiles.size(); i ++) {
			String str = this.classifiedPartitionPropertiesFiles.get(i);
			TimeSeriesFeatureComparator c = this.readFileAndGenerateTSFeatures(str);
			
			LabelType firstLabel = this.firstLabels.get(i);
			LabelType secondLabel = this.secondLabels.get(i);
			c.assignClassifiedLabel(firstLabel, secondLabel);
			this.classifiedComparators.add(c);
		}
	}

	public void validateFeaturesByEntropyDistanceTogether() throws Exception {
		this.generateFeatures();
		
		this.labeledComparator.computeEntropyDistance(700);
		//this.labeledComparator.buildIntervals();
		for (TimeSeriesFeatureComparator c: this.classifiedComparators) {
			c.computeEntropyDistance(700);
			c.indexFeatures();
		}
		
		ArrayList<TimeSeriesFeaturePair> sortedFeatures = this.labeledComparator.returnAllTimeSeriesFeatureListRanked();
		ArrayList<TimeSeriesFeaturePair> invalidatedFeatures = new ArrayList<TimeSeriesFeaturePair>();
		ArrayList<TimeSeriesFeaturePair> validatedFeatures = new ArrayList<TimeSeriesFeaturePair>();
		
		
		for (int i = 0; i < 200; i ++) {
			TimeSeriesFeaturePair p = sortedFeatures.get(i);
			
			TimeSeriesFeature mergedAbnormal = new TimeSeriesFeature(p.getTsFeature1());
			mergedAbnormal.setFeatureName(p.getFeatureName() + "-merged");
			TimeSeriesFeature mergedReference = new TimeSeriesFeature(p.getTsFeature2());
			mergedReference.setFeatureName(p.getFeatureName() + "-merged");
			
			for (TimeSeriesFeatureComparator c: this.classifiedComparators) {
				TimeSeriesFeature classifiedAbnormal = c.getAbnormalFeature(p.getFeatureName());
				this.meregeToFeature(mergedAbnormal, mergedReference, classifiedAbnormal);
			}
			
			for (TimeSeriesFeatureComparator c: this.classifiedComparators) {
				TimeSeriesFeature classifiedReference = c.getReferenceFeature(p.getFeatureName());
				this.meregeToFeature(mergedAbnormal, mergedReference, classifiedReference);
			}
			
			TimeSeriesFeaturePair mergedPair = new TimeSeriesFeaturePair(mergedAbnormal, mergedReference);
			mergedPair.computeEntropyDistance();
			double diff = Math.abs(mergedPair.getEntropyDistance() - p.getEntropyDistance()) ;
			double largerDis = Math.max(mergedPair.getEntropyDistance(), p.getEntropyDistance());
			if (diff > 0.45 * largerDis || mergedPair.getEntropyDistance() < 0.3 || p.getEntropyDistance() < 0.3) {
				invalidatedFeatures.add(p);
				System.out.println("Invalidated\t" + mergedPair.getFeatureName() + "\tOriginalDistance\t" + p.getEntropyDistance() + "\tMergedDistance\t" + mergedPair.getEntropyDistance() + "\tDifference\t" + diff + "\tDiffPercentage\t" + diff/largerDis);
				p.setMergedDistance(mergedPair.getEntropyDistance());
			} else {
				validatedFeatures.add(p);
				System.out.println("Validated\t" + mergedPair.getFeatureName() + "\tOriginalDistance\t" + p.getEntropyDistance() + "\tMergedDistance\t" + mergedPair.getEntropyDistance() +  "\tDifference\t" + diff + "\tDiffPercentage\t" + diff/largerDis);
				p.setMergedDistance(mergedPair.getEntropyDistance());
			}
		}
		

		System.out.println("Invalidated features: \t" + invalidatedFeatures.size());
		for (int i = 0; i < invalidatedFeatures.size(); i ++) {
			TimeSeriesFeaturePair p = invalidatedFeatures.get(i);
			System.out.println(p.getFeatureName() + "\t" + p.getRecentDistance() + "\t" + p.getMergedDistance());
		}
		
		System.out.println();
		System.out.println("Validated features:\t" + validatedFeatures.size());
		for (int i = 0; i < validatedFeatures.size(); i ++) {
			TimeSeriesFeaturePair p = validatedFeatures.get(i);
			System.out.println(p.getFeatureName() + "\t" + p.getRecentDistance() + "\t" + p.getMergedDistance());
		}
		System.out.println("Validated features:\t" + validatedFeatures.size());
		System.out.println("Invalidated features: \t" + invalidatedFeatures.size());
		double vPercentage = (double)validatedFeatures.size() / (double)(validatedFeatures.size() + invalidatedFeatures.size());
		System.out.println("Validated percentage\t" + vPercentage);
	}
	
	
	
	public static void main(String[] args) throws Exception {
		String labelPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-2f.properties";//high memory
		String[] classifiedPaths = {
				"/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b0-1f.point.properties",
				"/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b0-2f.point.properties",
				"/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-3f.point.properties"
			};
		ArrayList<String> classifiedPartitionPaths = new ArrayList<String>();
		for (String str: classifiedPaths) {
			classifiedPartitionPaths.add(str);
		}
		
		
		ArrayList<LabelType> firstLabels = new ArrayList<LabelType>();
		ArrayList<LabelType> secondLabels = new ArrayList<LabelType>();
		firstLabels.add(LabelType.Reference);
		firstLabels.add(LabelType.Reference);
		firstLabels.add(LabelType.Abnormal);
		
		secondLabels.add(LabelType.Reference);
		secondLabels.add(LabelType.Reference);
		secondLabels.add(LabelType.Mixed);
		
		FeatureValidationAllTogether validation = new FeatureValidationAllTogether(labelPartitionPath, classifiedPartitionPaths, firstLabels, secondLabels);
		
		validation.validateFeaturesByEntropyDistanceTogether();
	}
	
}
