#!/bin/bash

#run this on yeeha.yeeha

#echo " alpha1=0, alpha2 =0,alpha3 = 0"
java -Xms100m -Xmx8000m edu.umass.cs.sase.usecase.hadooplog.GangliaLogPreprocessorYeeha /nfs/yeeha/data1/users/haopeng/data/ganglia-log-0426/ /nfs/yeeha/data1/users/haopeng/data/ganglia-event-0426

