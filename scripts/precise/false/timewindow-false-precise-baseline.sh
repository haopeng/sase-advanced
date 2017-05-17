#!/bin/bash

mkdir timewindow-false-precise-baseline

# time window: 10

echo "time window, false, precise, baseline, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/10.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/10-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/20.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/20-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/30.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/30-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/40.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/40-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline, window = 50"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/50.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/50-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline, window = 60"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/60.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/60-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline, window = 70"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/70.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/70-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline, window = 80"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/80.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/80-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline, window = 90"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/90.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/90-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline, window = 100"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/100.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/100-$(date +%Y%m%d-%T).result
t

echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-false-precise-baseline >>timewindow-false-precise-baseline/result.txt
