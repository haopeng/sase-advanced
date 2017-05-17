#!/bin/bash


mkdir timewindow-true-imprecise-baseline-postponing


echo "time window, true-false, imprecise, baseline-postponing, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/10.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-imprecise-baseline-postponing/10-$(date +%Y%m%d-%T).result

echo "time window, true-false, imprecise, baseline-postponing, window = 5"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/5.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline-postponing/5-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/10.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline-postponing/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 15"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/15.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline-postponing/15-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/20.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline-postponing/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 25"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/25.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline-postponing/25-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/30.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline-postponing/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 35"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/35.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline-postponing/35-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/40.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline-postponing/40-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-imprecise-baseline-postponing >>timewindow-true-imprecise-baseline-postponing/result.txt
