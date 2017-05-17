#!/bin/bash

#run this on yeeha.yeeha
#Timestamp in milliseconds: 1429675200000
#Human time (your time zone): 4/22/2015, 12:00:00 AM#

#Timestamp in milliseconds: 1430107200000
#Human time (your time zone): 4/27/2015, 12:00:00 AM

java -Xms100m -Xmx8000m edu.umass.cs.sase.usecase.hadooplog.GangliaHadoopEventSorter /nfs/yeeha/data1/users/haopeng/data/ganglia-event-0426/ /nfs/yeeha/data1/users/haopeng/data/hadoop0426/event /nfs/yeeha/data1/users/haopeng/data/singleTypes-0426/ 1429675200000 1430107200000



