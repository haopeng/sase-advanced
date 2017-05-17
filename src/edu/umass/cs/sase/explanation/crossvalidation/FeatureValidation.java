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

public class FeatureValidation {
	int timeWindowSize = 30000;//milliseconds
	
	String labeledPartitionPropertiesFile;
	String classifiedPartitionPropertiesFile;
	
	TimeSeriesFeatureComparator labeledComparator;
	TimeSeriesFeatureComparator classifiedComparator;
	public FeatureValidation(String labeled, String classified) throws IOException {
		this.labeledPartitionPropertiesFile = labeled;
		this.classifiedPartitionPropertiesFile = classified;
		
	}
	
	public void generateFeatures() throws IOException {
		this.labeledComparator = this.readFileAndGenerateTSFeatures(this.labeledPartitionPropertiesFile);
		this.classifiedComparator = this.readFileAndGenerateTSFeatures(this.classifiedPartitionPropertiesFile);
	}
	
	public TimeSeriesFeatureComparator readFileAndGenerateTSFeatures(String propertyFile) throws IOException {
		PropertyReader pReader = new PropertyReader(propertyFile);
		RawEventReader reader = new RawEventReader(pReader.getInputFolder(), pReader.getAbnormalStart(), pReader.getAbnormalEnd(), pReader.getReferenceStart(), pReader.getReferenceEnd(), pReader.getCurrentPartitionId());
		
		//raw
		TimeSeriesRawGenerator abnormalTSRawGenerator = new TimeSeriesRawGenerator(reader.getSchemas(), reader.getAbnormalRawEventLists(), reader.getAbnormalStart(), reader.getAbnormalEnd(), LabelType.Abnormal);
		TimeSeriesRawGenerator referenceTSRawGenerator = new TimeSeriesRawGenerator(reader.getSchemas(), reader.getReferenceRawEventLists(), reader.getReferenceStart(), reader.getReferenceEnd(), LabelType.Reference);
		//window ts
		WindowTimeSeriesGenerator abnormalWTSGenerator = new WindowTimeSeriesGenerator(abnormalTSRawGenerator.getTimeSeriesRawFeatures(), timeWindowSize, LabelType.Abnormal); // 60 secons(60000 milliseconds)
		WindowTimeSeriesGenerator referenceWTSGenerator = new WindowTimeSeriesGenerator(referenceTSRawGenerator.getTimeSeriesRawFeatures(), timeWindowSize, LabelType.Reference);
		
		TimeSeriesFeatureComparator tsFeatureComparator = new TimeSeriesFeatureComparator(abnormalTSRawGenerator.getTimeSeriesRawFeatures(), referenceTSRawGenerator.getTimeSeriesRawFeatures(), 
				abnormalWTSGenerator.getWindowTimeSeriesFeatures(), referenceWTSGenerator.getWindowTimeSeriesFeatures());
		return tsFeatureComparator;
	}
	
	public void validateFeatures() throws Exception {
		this.generateFeatures();
		
		this.labeledComparator.computeEntropyDistance(700);
		//this.labeledComparator.buildIntervals();
		this.classifiedComparator.computeEntropyDistance(700);
		this.classifiedComparator.indexFeatures();
		
		ArrayList<TimeSeriesFeaturePair> sortedFeatures = this.labeledComparator.returnAllTimeSeriesFeatureListRanked();
		ArrayList<TimeSeriesFeaturePair> invalidatedFeatures = new ArrayList<TimeSeriesFeaturePair>();
		ArrayList<TimeSeriesFeaturePair> validatedFeatures = new ArrayList<TimeSeriesFeaturePair>();
		
		
		for (int i = 0; i < 200; i ++) {
			TimeSeriesFeaturePair p = sortedFeatures.get(i);
			p.buildIntervals();
			boolean invalidatedFirst = false;
			if (this.classifiedComparator.getAbnormalFeature(p.getFeatureName()).getClassifiedLabel() != LabelType.Mixed) {
				p.validateByFeature(this.classifiedComparator.getAbnormalFeature(p.getFeatureName()));
			}
			boolean invalidatedSecond = false;
			if(this.classifiedComparator.getReferenceFeature(p.getFeatureName()).getClassifiedLabel() != LabelType.Mixed){
				p.validateByFeature(this.classifiedComparator.getReferenceFeature(p.getFeatureName()));
			}
			//boolean invalidatedSecond = false;
			System.out.println(p.getFeatureName() + "\t" + invalidatedFirst + "\t" + invalidatedSecond);
			if (invalidatedFirst || invalidatedSecond) {
				invalidatedFeatures.add(p);
			} else {
				validatedFeatures.add(p);
			}
		}
		System.out.println("Invalidated features: \t" + invalidatedFeatures.size());
		for (int i = 0; i < invalidatedFeatures.size(); i ++) {
			TimeSeriesFeaturePair p = invalidatedFeatures.get(i);
			System.out.println(p.getFeatureName() + "\t" + p.getRecentDistance());
		}
		
		System.out.println();
		System.out.println("Validated features:\t" + validatedFeatures.size());
		for (int i = 0; i < validatedFeatures.size(); i ++) {
			TimeSeriesFeaturePair p = validatedFeatures.get(i);
			System.out.println(p.getFeatureName() + "\t" + p.getRecentDistance());
		}

		
		System.out.println("Validated features:\t" + validatedFeatures.size());
		System.out.println("Invalidated features: \t" + invalidatedFeatures.size());
		double vPercentage = (double)validatedFeatures.size() / (double)(validatedFeatures.size() + invalidatedFeatures.size());
		System.out.println("Validated percentage\t" + vPercentage);
	}
	
	public void meregeToFeature(TimeSeriesFeature abnormal, TimeSeriesFeature reference, TimeSeriesFeature toBeMerged) {
		if(toBeMerged.getClassifiedLabel() == LabelType.Abnormal) {
			abnormal.addPoints(toBeMerged.getTimestamps(), toBeMerged.getValues());
		} else if (toBeMerged.getClassifiedLabel() == LabelType.Reference) {
			reference.addPoints(toBeMerged.getTimestamps(), toBeMerged.getValues());
		}
	}
	
	public void validateFeaturesByEntropyDistance() throws Exception {
		this.generateFeatures();
		
		this.labeledComparator.computeEntropyDistance(700);
		//this.labeledComparator.buildIntervals();
		this.classifiedComparator.computeEntropyDistance(700);
		this.classifiedComparator.indexFeatures();
		
		ArrayList<TimeSeriesFeaturePair> sortedFeatures = this.labeledComparator.returnAllTimeSeriesFeatureListRanked();
		ArrayList<TimeSeriesFeaturePair> invalidatedFeatures = new ArrayList<TimeSeriesFeaturePair>();
		ArrayList<TimeSeriesFeaturePair> validatedFeatures = new ArrayList<TimeSeriesFeaturePair>();
		
		
		for (int i = 0; i < 200; i ++) {
			TimeSeriesFeaturePair p = sortedFeatures.get(i);
			
			TimeSeriesFeature mergedAbnormal = new TimeSeriesFeature(p.getTsFeature1());
			mergedAbnormal.setFeatureName(p.getFeatureName() + "-merged");
			TimeSeriesFeature mergedReference = new TimeSeriesFeature(p.getTsFeature2());
			mergedReference.setFeatureName(p.getFeatureName() + "-merged");
			
			TimeSeriesFeature classifiedAbnormal = this.classifiedComparator.getAbnormalFeature(p.getFeatureName());
			this.meregeToFeature(mergedAbnormal, mergedReference, classifiedAbnormal);
			
			TimeSeriesFeature classifiedReference = this.classifiedComparator.getReferenceFeature(p.getFeatureName());
			this.meregeToFeature(mergedAbnormal, mergedReference, classifiedReference);
			
			
			TimeSeriesFeaturePair mergedPair = new TimeSeriesFeaturePair(mergedAbnormal, mergedReference);
			mergedPair.computeEntropyDistance();
			double diff = Math.abs(mergedPair.getEntropyDistance() - p.getEntropyDistance()) ;
			double largerDis = Math.max(mergedPair.getEntropyDistance(), p.getEntropyDistance());
			if (diff > 0.15 * largerDis) {
				invalidatedFeatures.add(p);
				System.out.println("Invalidated\t" + mergedPair.getFeatureName() + "\tOriginalDistance\t" + p.getEntropyDistance() + "\tMergedDistance\t" + mergedPair.getEntropyDistance() + "\tDifference\t" + diff + "\tDiffPercentage\t" + diff/largerDis);
			} else {
				validatedFeatures.add(p);
				System.out.println("Validated\t" + mergedPair.getFeatureName() + "\tOriginalDistance\t" + p.getEntropyDistance() + "\tMergedDistance\t" + mergedPair.getEntropyDistance() +  "\tDifference\t" + diff + "\tDiffPercentage\t" + diff/largerDis);
			}
		}
		

		System.out.println("Invalidated features: \t" + invalidatedFeatures.size());
		for (int i = 0; i < invalidatedFeatures.size(); i ++) {
			TimeSeriesFeaturePair p = invalidatedFeatures.get(i);
			System.out.println(p.getFeatureName() + "\t" + p.getRecentDistance());
		}
		
		System.out.println();
		System.out.println("Validated features:\t" + validatedFeatures.size());
		for (int i = 0; i < validatedFeatures.size(); i ++) {
			TimeSeriesFeaturePair p = validatedFeatures.get(i);
			System.out.println(p.getFeatureName() + "\t" + p.getRecentDistance());
		}
		System.out.println("Validated features:\t" + validatedFeatures.size());
		System.out.println("Invalidated features: \t" + invalidatedFeatures.size());
		double vPercentage = (double)validatedFeatures.size() / (double)(validatedFeatures.size() + invalidatedFeatures.size());
		System.out.println("Validated percentage\t" + vPercentage);
	}
	
	public void assignLabelForClassified(LabelType firstLabel, LabelType secondLabel) {
		this.classifiedComparator.assignClassifiedLabel(firstLabel, secondLabel);
	}
	

	public static void main(String[] args) throws Exception {
		//compare b3_2 and b0_1
		
		/*
		String labelPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-2f.properties";//high memory
		//String classifiedPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b0-1f.properties";//no anomalies
		String classifiedPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b0-1fpercentage.properties";//no anomalies
		FeatureValidation validation = new FeatureValidation(labelPartitionPath, classifiedPartitionPath);
		validation.assignLabelForClassified(LabelType.Reference, LabelType.Reference);
		//validation.validateFeatures();//point based
		validation.validateFeaturesByEntropyDistance();//entropy distance
		*/
		
		//compare b3_2 and b0_2
		/*
		String labelPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-2f.properties";//high memory
		String classifiedPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b0-2f.properties";//no anomalies
		//String classifiedPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b0-2fpercentage.properties";//no anomalies
		FeatureValidation validation = new FeatureValidation(labelPartitionPath, classifiedPartitionPath);
		validation.assignLabelForClassified(LabelType.Reference, LabelType.Reference);
		validation.validateFeatures();
		//validation.validateFeaturesByEntropyDistance();
		*/
		
		//compare b3_2 and b3_3
		
		/*
		String labelPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-2f.properties";//high memory
		//String classifiedPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-3f.properties";//no anomalies
		String classifiedPartitionPath = "/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/b3-3fpercentage.properties";//no anomalies
		FeatureValidation validation = new FeatureValidation(labelPartitionPath, classifiedPartitionPath);
		//validation.assignLabelForClassified(LabelType.Abnormal, LabelType.Abnormal);
		//validation.validateFeatures();
		//validation.assignLabelForClassified(LabelType.Abnormal, LabelType.Abnormal);
		validation.assignLabelForClassified(LabelType.Abnormal, LabelType.Reference);
		//validation.assignLabelForClassified(LabelType.Abnormal, LabelType.Mixed);
		validation.validateFeaturesByEntropyDistance();
		//validation.validateFeatures();//points
		*/
	}
}
