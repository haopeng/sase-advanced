#!/bin/bash


mkdir confidence-true-false-imprecise-baseline-postponing-dpc


echo "confidence, true-false, imprecise, baseline-postponing-dpc, window = 30, confidence = 0.9"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.9 5 1 0 >>confidence-true-false-imprecise-baseline-postponing-dpc/0.9-$(date +%Y%m%d-%T).result


echo "confidence, true-false, imprecise, baseline-postponing-dpc, window = 30, confidence = 0.8"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.8 5 1 0 >>confidence-true-false-imprecise-baseline-postponing-dpc/0.8-$(date +%Y%m%d-%T).result

echo "confidence, true-false, imprecise, baseline-postponing-dpc, window = 30, confidence = 0.7"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.7 5 1 0 >>confidence-true-false-imprecise-baseline-postponing-dpc/0.7-$(date +%Y%m%d-%T).result


echo "confidence, true-false, imprecise, baseline-postponing-dpc, window = 30, confidence = 0.6"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.6 5 1 0 >>confidence-true-false-imprecise-baseline-postponing-dpc/0.6-$(date +%Y%m%d-%T).result


echo "confidence, true-false, imprecise, baseline-postponing-dpc, window = 30, confidence = 0.5"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>confidence-true-false-imprecise-baseline-postponing-dpc/0.5-$(date +%Y%m%d-%T).result

echo "confidence, true-false, imprecise, baseline-postponing-dpc, window = 30, confidence = 0.4"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.4 5 1 0 >>confidence-true-false-imprecise-baseline-postponing-dpc/0.4-$(date +%Y%m%d-%T).result


echo "confidence, true-false, imprecise, baseline-postponing-dpc, window = 30, confidence = 0.3"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.3 5 1 0 >>confidence-true-false-imprecise-baseline-postponing-dpc/0.3-$(date +%Y%m%d-%T).result


echo "confidence, true-false, imprecise, baseline-postponing-dpc, window = 30, confidence = 0.2"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.2 5 1 0 >>confidence-true-false-imprecise-baseline-postponing-dpc/0.2-$(date +%Y%m%d-%T).result



echo "confidence, true-false, imprecise, baseline-postponing-dpc, window = 30, confidence = 0.1"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.1 5 1 0 >>confidence-true-false-imprecise-baseline-postponing-dpc/0.1-$(date +%Y%m%d-%T).result

echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader confidence-true-false-imprecise-baseline-postponing-dpc >>confidence-true-false-imprecise-baseline-postponing-dpc/result.txt
