#!/bin/bash

mkdir timewindow-inconsistent-precise-baseline-postponing-onthefly

# time window: 10


echo "time window, inconsistent, precise, baseline + postponing + onthefly, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-inconsistent-precise/10.nfa $HOME/sase2013/queries/timewindow-inconsistent-precise/window.stream 0 1 1 0 >>timewindow-inconsistent-precise-baseline-postponing-onthefly/10-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, precise, baseline + postponing + onthefly, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-inconsistent-precise/20.nfa $HOME/sase2013/queries/timewindow-inconsistent-precise/window.stream 0 1 1 0 >>timewindow-inconsistent-precise-baseline-postponing-onthefly/20-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, precise, baseline + postponing + onthefly, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-inconsistent-precise/30.nfa $HOME/sase2013/queries/timewindow-inconsistent-precise/window.stream 0 1 1 0 >>timewindow-inconsistent-precise-baseline-postponing-onthefly/30-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, precise, baseline + postponing + onthefly, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-inconsistent-precise/40.nfa $HOME/sase2013/queries/timewindow-inconsistent-precise/window.stream 0 1 1 0 >>timewindow-inconsistent-precise-baseline-postponing-onthefly/40-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, precise, baseline + postponing + onthefly, window = 50"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-inconsistent-precise/50.nfa $HOME/sase2013/queries/timewindow-inconsistent-precise/window.stream 0 1 1 0 >>timewindow-inconsistent-precise-baseline-postponing-onthefly/50-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, precise, baseline + postponing + onthefly, window = 60"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-inconsistent-precise/60.nfa $HOME/sase2013/queries/timewindow-inconsistent-precise/window.stream 0 1 1 0 >>timewindow-inconsistent-precise-baseline-postponing-onthefly/60-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, precise, baseline + postponing + onthefly, window = 70"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-inconsistent-precise/70.nfa $HOME/sase2013/queries/timewindow-inconsistent-precise/window.stream 0 1 1 0 >>timewindow-inconsistent-precise-baseline-postponing-onthefly/70-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, precise, baseline + postponing + onthefly, window = 80"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-inconsistent-precise/80.nfa $HOME/sase2013/queries/timewindow-inconsistent-precise/window.stream 0 1 1 0 >>timewindow-inconsistent-precise-baseline-postponing-onthefly/80-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, precise, baseline + postponing + onthefly, window = 90"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-inconsistent-precise/90.nfa $HOME/sase2013/queries/timewindow-inconsistent-precise/window.stream 0 1 1 0 >>timewindow-inconsistent-precise-baseline-postponing-onthefly/90-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, precise, baseline + postponing + onthefly, window = 100"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-inconsistent-precise/100.nfa $HOME/sase2013/queries/timewindow-inconsistent-precise/window.stream 0 1 1 0 >>timewindow-inconsistent-precise-baseline-postponing-onthefly/100-$(date +%Y%m%d-%T).result

echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-inconsistent-precise-baseline-postponing-onthefly >>timewindow-inconsistent-precise-baseline-postponing-onthefly/result.txt
