#!/bin/bash

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
