#!/bin/bash

mkdir timewindow-false-precise-baseline-postponing-onthefly-collapsed

# time window: 10




echo "time window, false, precise, baseline + postponing + onthefly-collapsed, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/10.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/10-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline + postponing + onthefly-collapsed, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/20.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/20-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline + postponing + onthefly-collapsed, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/30.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/30-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline + postponing + onthefly-collapsed, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/40.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/40-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline + postponing + onthefly-collapsed, window = 50"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/50.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/50-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline + postponing + onthefly-collapsed, window = 60"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/60.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/60-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline + postponing + onthefly-collapsed, window = 70"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/70.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/70-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline + postponing + onthefly-collapsed, window = 80"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/80.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/80-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline + postponing + onthefly-collapsed, window = 90"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/90.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/90-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline + postponing + onthefly-collapsed, window = 100"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/100.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/100-$(date +%Y%m%d-%T).result

echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-false-precise-baseline-postponing-onthefly-collapsed >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/result.txt
