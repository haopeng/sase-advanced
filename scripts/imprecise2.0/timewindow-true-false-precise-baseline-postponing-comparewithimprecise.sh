#!/bin/bash

mkdir timewindow-true-false-precise-baseline-postponing-compare-with-imprecise



echo "time window, true-false, precise, baseline + postponing, compare with imprecise, window = 5"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-imprecise/5.nfa $HOME/sase2013/queries/timewindow-imprecise/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing-compare-with-imprecise/5-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, compare with imprecise, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-imprecise/10.nfa $HOME/sase2013/queries/timewindow-imprecise/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing-compare-with-imprecise/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, compare with imprecise, window = 15"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-imprecise/15.nfa $HOME/sase2013/queries/timewindow-imprecise/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing-compare-with-imprecise/15-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, compare with imprecise, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-imprecise/20.nfa $HOME/sase2013/queries/timewindow-imprecise/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing-compare-with-imprecise/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, compare with imprecise, window = 25"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-imprecise/25.nfa $HOME/sase2013/queries/timewindow-imprecise/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing-compare-with-imprecise/25-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, compare with imprecise, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing-compare-with-imprecise/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, compare with imprecise, window = 35"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-imprecise/35.nfa $HOME/sase2013/queries/timewindow-imprecise/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing-compare-with-imprecise/35-$(date +%Y%m%d-%T).result
echo "time window, true-false, precise, baseline + postponing, compare with imprecise, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-imprecise/40.nfa $HOME/sase2013/queries/timewindow-imprecise/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing-compare-with-imprecise/40-$(date +%Y%m%d-%T).result



echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-false-precise-baseline-postponing-compare-with-imprecise >>timewindow-true-false-precise-baseline-postponing-compare-with-imprecise/result.txt
