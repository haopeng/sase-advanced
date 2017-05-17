#!/bin/bash

#run this on yeeha.yeeha

java -Xms100m -Xmx8000m edu.umass.cs.sase.usecase.hadooplog.PullDataParser /nfs/yeeha/data1/users/haopeng/hadoop-system-logs-apr26/compute-1-%%.yeeha /nfs/yeeha/data1/users/haopeng/data/hadoop0426/event/pulldata/compute-1-%%-pullstart.txt /nfs/yeeha/data1/users/haopeng/hadoop-user-logs-repo/apr26 /nfs/yeeha/data1/users/haopeng/data/hadoop0426/event/pulldata/