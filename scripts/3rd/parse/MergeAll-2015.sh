#!/bin/bash

#run this on yeeha.yeeha
#		long startTimestamp = 1427515200000L;
#		long endTimestamp = 1428897600000L;

java -Xms100m -Xmx8000m edu.umass.cs.sase.usecase.hadooplog.GangliaHadoopEventSorter /nfs/yeeha/data1/users/haopeng/data/ganglia-event/ /nfs/yeeha/data1/users/haopeng/data/hadoop/event2 /nfs/yeeha/data1/users/haopeng/data/singleTypes/ 1427515200000 1428897600000



