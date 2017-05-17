#!/bin/bash


#timewindow-true-false-precise-baseline-postponing-collapsed.sh
#####################

mkdir timewindow-true-false-precise-baseline-postponing-collapsed

# time window: 10



echo "time window, true-false, precise, baseline + postponing, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/20.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/30.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/40.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/40-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 50"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/50.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/50-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 60"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/60.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/60-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 70"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/70.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/70-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 80"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/80.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/80-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 90"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/90.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/90-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 100"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/100.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/100-$(date +%Y%m%d-%T).result

echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-false-precise-baseline-postponing-collapsed >>timewindow-true-false-precise-baseline-postponing-collapsed/result.txt
###############
#timewindow-true-false-precise-baseline-postponing.sh
#####################

mkdir timewindow-true-false-precise-baseline-postponing

# time window: 10

echo "time window, true-false, precise, baseline + postponing, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/20.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/30.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/40.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/40-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 50"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/50.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/50-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 60"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/60.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/60-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 70"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/70.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/70-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 80"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/80.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/80-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 90"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/90.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/90-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, window = 100"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/100.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/100-$(date +%Y%m%d-%T).result

echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-false-precise-baseline-postponing >>timewindow-true-false-precise-baseline-postponing/result.txt
###############
#timewindow-true-false-precise-baseline-zstream-postponing.sh
#####################

mkdir timewindow-true-false-precise-baseline-zstream-postponing



echo "time window, true-false, precise, baseline + zstream + postponing, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream-postponing/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream + postponing, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/20.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream-postponing/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream + postponing, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/30.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream-postponing/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream + postponing, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/40.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream-postponing/40-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream + postponing, window = 50"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/50.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream-postponing/50-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream + postponing, window = 60"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/60.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream-postponing/60-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream + postponing, window = 70"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/70.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream-postponing/70-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream + postponing, window = 80"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/80.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream-postponing/80-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream + postponing, window = 90"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/90.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream-postponing/90-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream + postponing, window = 100"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/100.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream-postponing/100-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-false-precise-baseline-zstream-postponing >>timewindow-true-false-precise-baseline-zstream-postponing/result.txt
###############
#timewindow-true-false-precise-baseline-zstream.sh
#####################

mkdir timewindow-true-false-precise-baseline-zstream



echo "time window, true-false, precise, baseline + zstream, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/20.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/30.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/40.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/40-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream, window = 50"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/50.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/50-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream, window = 60"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/60.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/60-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream, window = 70"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/70.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/70-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream, window = 80"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/80.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/80-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream, window = 90"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/90.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/90-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + zstream, window = 100"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/100.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/100-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-false-precise-baseline-zstream >>timewindow-true-false-precise-baseline-zstream/result.txt
###############
#timewindow-true-false-precise-baseline.sh
#####################

mkdir timewindow-true-false-precise-baseline

# time window: 10


echo "time window, true-false, precise, baseline, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/20.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/30.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/40.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/40-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline, window = 50"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/50.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/50-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline, window = 60"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/60.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/60-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline, window = 70"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/70.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/70-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline, window = 80"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/80.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/80-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline, window = 90"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/90.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/90-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline, window = 100"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/100.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/100-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-false-precise-baseline >>timewindow-true-false-precise-baseline/result.txt
###############
