#!/bin/bash


mkdir timewindow-inconsistent-imprecise-baseline-postponing


echo "time window, inconsistent, imprecise, baseline-postponing, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-inconsistent-imprecise/10.nfa $HOME/sase2013/queries/timewindow-inconsistent-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-inconsistent-imprecise-baseline-postponing/10-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, imprecise, baseline-postponing, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-inconsistent-imprecise/20.nfa $HOME/sase2013/queries/timewindow-inconsistent-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-inconsistent-imprecise-baseline-postponing/20-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, imprecise, baseline-postponing, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-inconsistent-imprecise/30.nfa $HOME/sase2013/queries/timewindow-inconsistent-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-inconsistent-imprecise-baseline-postponing/30-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, imprecise, baseline-postponing, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-inconsistent-imprecise/40.nfa $HOME/sase2013/queries/timewindow-inconsistent-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-inconsistent-imprecise-baseline-postponing/40-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, imprecise, baseline-postponing, window = 50"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-inconsistent-imprecise/50.nfa $HOME/sase2013/queries/timewindow-inconsistent-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-inconsistent-imprecise-baseline-postponing/50-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, imprecise, baseline-postponing, window = 60"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-inconsistent-imprecise/60.nfa $HOME/sase2013/queries/timewindow-inconsistent-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-inconsistent-imprecise-baseline-postponing/60-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, imprecise, baseline-postponing, window = 70"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-inconsistent-imprecise/70.nfa $HOME/sase2013/queries/timewindow-inconsistent-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-inconsistent-imprecise-baseline-postponing/70-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, imprecise, baseline-postponing, window = 80"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-inconsistent-imprecise/80.nfa $HOME/sase2013/queries/timewindow-inconsistent-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-inconsistent-imprecise-baseline-postponing/80-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, imprecise, baseline-postponing, window = 90"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-inconsistent-imprecise/90.nfa $HOME/sase2013/queries/timewindow-inconsistent-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-inconsistent-imprecise-baseline-postponing/90-$(date +%Y%m%d-%T).result
echo "time window, inconsistent, imprecise, baseline-postponing, window = 100"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-inconsistent-imprecise/100.nfa $HOME/sase2013/queries/timewindow-inconsistent-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-inconsistent-imprecise-baseline-postponing/100-$(date +%Y%m%d-%T).result



echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-inconsistent-imprecise-baseline-postponing >>timewindow-inconsistent-imprecise-baseline-postponing/result.txt
