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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;
import edu.umass.cs.sase.explanation.UI.ExplanationSettings;
import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import edu.umass.cs.sase.explanation.featuregeneration.FeatureType;
import edu.umass.cs.sase.explanation.featuregeneration.LabelType;
import edu.umass.cs.sase.explanation.featuregeneration.TimeSeriesFeature;

public class PartitionReader {
	String csvPath;
	String partitionName;
	
	long partitionId;
	
	TimeSeriesFeature partition;
	boolean isEntire;
	boolean fromStart;
	long period;
	double percent;
	
	long cutTimestamp;
	
	int startIndex;
	int endIndex;
	
	int countOfPoints;
	long intervalLength;
	
	
	long startTimestamp;
	long endTimestamp;
	
	
	LabelType alignLabel;// aligned to which period
	public PartitionReader(String path, String partitionName) throws IOException {
		this.csvPath = path;
		this.partitionName = partitionName;
		this.readPartition();
		this.isEntire = true;
	}
	
	public PartitionReader(String path, String partitionName, long partitionId) throws IOException {
		this.csvPath = path;
		this.partitionName = partitionName;
		this.partitionId = partitionId;
		this.readPartition();
		this.isEntire = true;
	}
	
	
	//starting and ending timestamps
	public PartitionReader(PartitionReader partition, long cutTimestamp, boolean fromStart) {
		this.fromStart = fromStart;
		this.cutTimestamp = cutTimestamp;
		this.cutFromPartitionWithTimestamps(partition);
	}

	//absolute length
	public PartitionReader(PartitionReader partition, boolean fromStart, long period) {
		this.fromStart = fromStart;
		this.period = period;
		this.cutFromPartitionWithPeriodLength(partition);
	}
	
	//absolute percentage
	public PartitionReader(PartitionReader partition, boolean fromStart, double percent) {
		this.fromStart = fromStart;
		this.percent = percent;
		this.cutFromPartitionByPercentage(partition);
	}
	
	//position of points
	public PartitionReader(PartitionReader partition, boolean fromStart, int startIndex, int endIndex) {
		this.fromStart = fromStart;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.cutFromPartitionByIndexOfPoints(partition);
	}
	
	
	public PartitionReader(PartitionReader partition, long startTimestamp, long endTimestamp) {
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.partitionId = partition.partitionId;
		this.cutFromPartitionWithStartAndEndTimestamp(partition);
	}
	
	//cut by start timestamp and end timestmaps
	public void cutFromPartitionWithStartAndEndTimestamp(PartitionReader partition) {
		TimeSeriesFeature feature = partition.getPartition();
		this.partition = new TimeSeriesFeature(FeatureType.TimeSeriesRaw, this.partitionName, 0, LabelType.Mixed);
		ArrayList<Long> timestamps = feature.getTimestamps();
		ArrayList<Double> values = feature.getValues();
		for (int i = 0; i < timestamps.size() && timestamps.get(i) <= this.endTimestamp ; i ++) {
			Long ts = timestamps.get(i);
			Double value = values.get(i);
			if (ts >= this.startTimestamp) {
				this.partition.addPoint(ts, value);
			}
		}
		//update count of points
		//update interval length
		this.countOfPoints = this.partition.getTimestamps().size();
		this.intervalLength = this.partition.getTimestamps().get(countOfPoints - 1) - this.partition.getTimestamps().get(0);
		this.startTimestamp = this.partition.getTimestamps().get(0);
		this.endTimestamp = this.partition.getTimestamps().get(countOfPoints - 1);
		
		if (ExplanationSettings.printResult) {
			System.out.println(this.partitionId + "\t" + this.partition.getTimestamps().size() + "\t values");			
		}
	}
	
	
	//cut by position of points
	
	public void cutFromPartitionByIndexOfPoints(PartitionReader partition) {
		TimeSeriesFeature feature = partition.getPartition();
		
		if (fromStart) {
			this.partitionName = partition.getPartitionName() + "-startingPart";
			System.out.println(this.partitionName + "\t startIndex=\t" + this.startIndex + "\t endIndex=\t" + this.endIndex);
		} else {
			this.partitionName = partition.getPartitionName() + "-endingPart";
			System.out.println(this.partitionName + "\t startIndex=\t" + this.startIndex + "\t endIndex=\t" + this.endIndex);
		}
		
		this.partition = new TimeSeriesFeature(FeatureType.TimeSeriesRaw, this.partitionName, 0, LabelType.Mixed);
		ArrayList<Long> timestamps = feature.getTimestamps();
		ArrayList<Double> values = feature.getValues();
		for (int i = this.startIndex; i <= this.endIndex; i ++) {
			Long ts = timestamps.get(i);
			Double value = values.get(i);
			this.partition.addPoint(ts, value);
		}
		System.out.println(this.partitionName + "\t" + this.partition.getTimestamps().size() + "\t values");
		System.out.println("Time range:" + this.partition.getTimestamps().get(0) + "\t~\t" + this.partition.getTimestamps().get(this.partition.getTimestamps().size() - 1) );
	}

	//percentage
	public void cutFromPartitionByPercentage(PartitionReader partition) {
		TimeSeriesFeature feature = partition.getPartition();
		long start;
		long end;
		long length = (long)(partition.getEntireIntervalLength() * this.percent);
		if (fromStart) {
			start = 0L;
			end = length;
			this.partitionName = partition.getPartitionName() + "-startingPart";
			System.out.println(this.partitionName + "\t start=\t" + start + "\t end=\t" + end);
		} else {
			end = feature.getTimestamps().get(feature.getTimestamps().size() - 1);
			start = length;
			this.partitionName = partition.getPartitionName() + "-endingPart";
			System.out.println(this.partitionName + "\t start=\t" + start + "\t end=\t" + end);
		}
		
		this.partition = new TimeSeriesFeature(FeatureType.TimeSeriesRaw, this.partitionName, 0, LabelType.Mixed);
		ArrayList<Long> timestamps = feature.getTimestamps();
		ArrayList<Double> values = feature.getValues();
		for (int i = 0; i < timestamps.size() && timestamps.get(i) <= end ; i ++) {
			Long ts = timestamps.get(i);
			Double value = values.get(i);
			if (ts >= start) {
				this.partition.addPoint(ts, value);
			}
		}
		System.out.println(this.partitionName + "\t" + this.partition.getTimestamps().size() + "\t values");
		
		
	}
	//length, and left
	
	
	//percentage, and left
	
	public void cutFromPartitionWithPeriodLength(PartitionReader partition) {
		TimeSeriesFeature feature = partition.getPartition();
		long start;
		long end;
		if (fromStart) {
			start = 0L;
			end = start + this.period;
			this.partitionName = partition.getPartitionName() + "-startingPart";
			System.out.println("start=" + start + "\t end=" + end);
		} else {
			end = feature.getTimestamps().get(feature.getTimestamps().size() - 1);
			start = end - this.period;
			System.out.println("start=" + start + "\t end=" + end);
			this.partitionName = partition.getPartitionName() + "-endingPart";
		}
		
		this.partition = new TimeSeriesFeature(FeatureType.TimeSeriesRaw, this.partitionName, 0, LabelType.Mixed);
		ArrayList<Long> timestamps = feature.getTimestamps();
		ArrayList<Double> values = feature.getValues();
		for (int i = 0; i < timestamps.size() && timestamps.get(i) <= end ; i ++) {
			Long ts = timestamps.get(i);
			Double value = values.get(i);
			if (ts >= start) {
				this.partition.addPoint(ts, value);
			}
		}
		System.out.println(this.partitionName + "\t" + this.partition.getTimestamps().size() + "\t values");
	}
	
	
	public void cutFromPartitionWithTimestamps(PartitionReader partition) {
		TimeSeriesFeature feature = partition.getPartition();
		
		long start;
		long end;
		if (fromStart) {
			start = 0L;
			end = this.cutTimestamp;
			this.partitionName = partition.getPartitionName() + "-startingPart";
			System.out.println("start=" + start + "\t end=" + end);
		} else {
			end = feature.getTimestamps().get(feature.getTimestamps().size() - 1);
			start = this.cutTimestamp;
			System.out.println("start=" + start + "\t end=" + end);
			this.partitionName = partition.getPartitionName() + "-endingPart";
		}
		
		this.partition = new TimeSeriesFeature(FeatureType.TimeSeriesRaw, this.partitionName, 0, LabelType.Mixed);
		ArrayList<Long> timestamps = feature.getTimestamps();
		ArrayList<Double> values = feature.getValues();
		for (int i = 0; i < timestamps.size() && timestamps.get(i) <= end ; i ++) {
			Long ts = timestamps.get(i);
			Double value = values.get(i);
			if (ts >= start) {
				this.partition.addPoint(ts, value);
			}
		}
		System.out.println(this.partitionName + "\t" + this.partition.getTimestamps().size() + "\t values");
	}
	
	
	public void readPartition() throws IOException {
		this.partition = new TimeSeriesFeature(FeatureType.TimeSeriesRaw, this.partitionName, 0, LabelType.Mixed);
		
		CSVReader reader = new CSVReader(new FileReader(this.csvPath));
		String[] strs = reader.readNext();//skip header
		int count = 0;
		while ((strs = reader.readNext()) != null) {
			//Long ts = Long.parseLong(strs[12]);//the relative timestamp
			Long ts = Long.parseLong(strs[7]);// the acutal timestamp 
			//double ts = Double.parseDouble(strs[7]);
			Double value = Double.parseDouble(strs[13]);//hard code: accumulated value.
			this.partition.addPoint(ts, value);
			//System.out.println(count ++ + "\t" + ts + "\t" + value);
			//System.out.printf("ts: %f\n", ts);
		}
		
		this.countOfPoints = this.partition.getTimestamps().size();
		this.intervalLength = this.partition.getTimestamps().get(countOfPoints - 1) - this.partition.getTimestamps().get(0);
		this.startTimestamp = this.partition.getTimestamps().get(0);
		this.endTimestamp = this.partition.getTimestamps().get(countOfPoints - 1);
		
		if (ExplanationSettings.printResult) {
			System.out.println(this.partitionId + "\t" + this.partition.getTimestamps().size() + "\t values");			
		}

		
	}
	
	
	
	
	
	public String getCsvPath() {
		return csvPath;
	}

	public void setCsvPath(String csvPath) {
		this.csvPath = csvPath;
	}

	public String getPartitionName() {
		return partitionName;
	}

	public void setPartitionName(String partitionName) {
		this.partitionName = partitionName;
	}

	public TimeSeriesFeature getPartition() {
		return partition;
	}

	public void setPartition(TimeSeriesFeature partition) {
		this.partition = partition;
	}

	public boolean isEntire() {
		return isEntire;
	}

	public void setEntire(boolean isEntire) {
		this.isEntire = isEntire;
	}

	public boolean isFromStart() {
		return fromStart;
	}

	public void setFromStart(boolean fromStart) {
		this.fromStart = fromStart;
	}

	

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	
	
	public double getPercent() {
		return percent;
	}


	public void setPercent(double percent) {
		this.percent = percent;
	}


	public long getCutTimestamp() {
		return cutTimestamp;
	}


	public void setCutTimestamp(long cutTimestamp) {
		this.cutTimestamp = cutTimestamp;
	}


	public long getEntireIntervalLength() {
		return this.partition.getTimestamps().get(this.partition.getTimestamps().size() - 1);//the relative timestamps of the last point
	}


	public int getNumOfPoints() {
		return this.partition.getValues().size();
	}


	public int getStartIndex() {
		return startIndex;
	}


	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}


	public int getEndIndex() {
		return endIndex;
	}


	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}


	public int getCountOfPoints() {
		return countOfPoints;
	}


	public void setCountOfPoints(int countOfPoints) {
		this.countOfPoints = countOfPoints;
	}


	public long getIntervalLength() {
		return intervalLength;
	}


	public void setIntervalLength(long intervalLength) {
		this.intervalLength = intervalLength;
	}

	public long getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(long partitionId) {
		this.partitionId = partitionId;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public LabelType getAlignLabel() {
		return alignLabel;
	}

	public void setAlignLabel(LabelType alignLabel) {
		this.alignLabel = alignLabel;
	}



	
}
