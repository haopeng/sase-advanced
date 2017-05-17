#!/bin/bash

#run this on yeeha.yeeha

java -Xms100m -Xmx8000m edu.umass.cs.sase.usecase.hadooplog.PeriodEventGenerator /nfs/yeeha/data1/users/haopeng/data/hadoop0426/pairs/pullpairs.txt /nfs/yeeha/data1/users/haopeng/data/hadoop0426/event/period/pullperiod.txt /nfs/yeeha/data1/users/haopeng/data/hadoop0426/pairs/mapreducepairs.txt /nfs/yeeha/data1/users/haopeng/data/hadoop0426/event/period/mapperiod.txt /nfs/yeeha/data1/users/haopeng/data/hadoop0426/event/period/reduceperiod.txt