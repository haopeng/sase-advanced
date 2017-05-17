#!/bin/bash


#datestr = $(date +%Y%m%d-%T)
#mkdir $dirname

# time window 10
echo "zstream, timewindow,  10, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-10-$(date +%Y%m%d-%T).result
# time window 20
echo "zstream, timewindow,  20, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/20.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-20-$(date +%Y%m%d-%T).result
# time window 30
echo "zstream, timewindow,  30, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/30.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-30-$(date +%Y%m%d-%T).result
# time window 40
echo "zstream, timewindow,  40, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/40.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-40-$(date +%Y%m%d-%T).result
# time window 50
echo "zstream, timewindow,  50, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/50.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-50-$(date +%Y%m%d-%T).result
# time window 60
echo "zstream, timewindow,  60, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/60.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-60-$(date +%Y%m%d-%T).result
# time window 70
echo "zstream, timewindow,  70, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/70.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-70-$(date +%Y%m%d-%T).result
# time window 80
echo "zstream, timewindow,  80, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/80.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-80-$(date +%Y%m%d-%T).result
# time window 90
echo "zstream, timewindow,  90, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/90.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-90-$(date +%Y%m%d-%T).result
# time window 100
echo "zstream, timewindow,  100, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/100.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-100-$(date +%Y%m%d-%T).result



# query 1
echo "zstream, selectivity, query 1, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-1-$(date +%Y%m%d-%T).result
# query 2
echo "zstream, selectivity, query 2, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/2.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-2-$(date +%Y%m%d-%T).result
# query 3
echo "zstream, selectivity, query 3, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/3.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-3-$(date +%Y%m%d-%T).result
# query 4
echo "zstream, selectivity, query 4, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/4.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-4-$(date +%Y%m%d-%T).result
# query 5
echo "zstream, selectivity, query 5, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/5.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-5-$(date +%Y%m%d-%T).result
# query 6
echo "zstream, selectivity, query 6, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/6.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-6-$(date +%Y%m%d-%T).result
# query 7
echo "zstream, selectivity, query 7, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/7.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-7-$(date +%Y%m%d-%T).result
# query 8
echo "zstream, selectivity, query 8, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/8.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-8-$(date +%Y%m%d-%T).result
# query 9
echo "zstream, selectivity, query 9, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/9.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-9-$(date +%Y%m%d-%T).result
# query 10
echo "zstream, selectivity, query 10, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/10.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-10-$(date +%Y%m%d-%T).result
# query 11
echo "zstream, selectivity, query 11, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/11.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-11-$(date +%Y%m%d-%T).result


# query 21
echo "zstream, selectivity, query 21, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/21.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-21-$(date +%Y%m%d-%T).result
# query 22
echo "zstream, selectivity, query 22, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/22.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-22-$(date +%Y%m%d-%T).result
# query 23
echo "zstream, selectivity, query 23, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/23.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-23-$(date +%Y%m%d-%T).result
# query 24
echo "zstream, selectivity, query 24, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/24.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-24-$(date +%Y%m%d-%T).result
# query 25
echo "zstream, selectivity, query 25, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/25.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-25-$(date +%Y%m%d-%T).result
# query 26
echo "zstream, selectivity, query 26, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/26.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-26-$(date +%Y%m%d-%T).result
# query 27
echo "zstream, selectivity, query 27, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/27.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-27-$(date +%Y%m%d-%T).result
# query 28
echo "zstream, selectivity, query 28, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/28.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-28-$(date +%Y%m%d-%T).result
# query 29
echo "zstream, selectivity, query 29, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/29.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-29-$(date +%Y%m%d-%T).result
# query 30
echo "zstream, selectivity, query 30, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/30.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-30-$(date +%Y%m%d-%T).result
# query 31
echo "zstream, selectivity, query 31, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/31.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-31-$(date +%Y%m%d-%T).result
# query 32
echo "zstream, selectivity, query 32, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/32.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-32-$(date +%Y%m%d-%T).result
# query 33
echo "zstream, selectivity, query 33, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/33.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-33-$(date +%Y%m%d-%T).result
# query 34
echo "zstream, selectivity, query 34, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/34.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-34-$(date +%Y%m%d-%T).result
# query 35
echo "zstream, selectivity, query 35, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/35.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-35-$(date +%Y%m%d-%T).result
# query 36
echo "zstream, selectivity, query 36, "
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/36.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-36-$(date +%Y%m%d-%T).result

echo "1.truefalse,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/1.truefalse.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 0 0 0 1 >>zstream/querytype-1-truefalse-$(date +%Y%m%d-%T).result


echo "2.false,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/2.false.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 0 0 0 1 >>zstream/querytype-2-false-$(date +%Y%m%d-%T).result



echo "3.true,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/3.true.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 0 0 0 1 >>zstream/querytype-3-true-$(date +%Y%m%d-%T).result



echo "4.inconsistent,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/4.inconsistent.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 0 0 0 1 >>zstream/querytype-4-inconsistent-$(date +%Y%m%d-%T).result

echo "1.truefalse,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype2-moreruns/1.truefalse.nfa $HOME/sase2013/queries/querytype2-moreruns/querytype.stream 0 0 0 0 1 >>zstream/querytype-moreruns-1-truefalse-$(date +%Y%m%d-%T).result


echo "2.false,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype2-moreruns/2.false.nfa $HOME/sase2013/queries/querytype2-moreruns/querytype.stream 0 0 0 0 1 >>zstream/querytype-moreruns-2-false-$(date +%Y%m%d-%T).result



echo "3.true,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype2-moreruns/3.true.nfa $HOME/sase2013/queries/querytype2-moreruns/querytype.stream 0 0 0 0 1 >>zstream/querytype-moreruns-3-true-$(date +%Y%m%d-%T).result



echo "4.inconsistent,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype2-moreruns/4.inconsistent.nfa $HOME/sase2013/queries/querytype2-moreruns/querytype.stream 0 0 0 0 1 >>zstream/querytype-moreruns-4-inconsistent-$(date +%Y%m%d-%T).result


