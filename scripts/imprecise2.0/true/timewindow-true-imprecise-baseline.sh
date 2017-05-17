#!/bin/bash

mkdir timewindow-true-imprecise-baseline



echo "time window, true-false, imprecise, baseline, window = 5"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/5.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline/5-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/10.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline, window = 15"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/15.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline/15-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/20.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline, window = 25"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/25.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline/25-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/30.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline, window = 35"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/35.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline/35-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-true-imprecise/40.nfa $HOME/sase2013/queries/timewindow-true-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 0 0 >>timewindow-true-imprecise-baseline/40-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-imprecise-baseline >>timewindow-true-imprecise-baseline/result.txt
