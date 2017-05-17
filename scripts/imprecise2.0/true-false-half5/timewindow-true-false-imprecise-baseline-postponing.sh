#!/bin/bash


mkdir timewindow-true-false-imprecise-baseline-postponing


echo "time window, true-false, imprecise, baseline-postponing, window = 10"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/10.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/10-$(date +%Y%m%d-%T).result

echo "time window, true-false, imprecise, baseline-postponing, window = 5"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/5.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline-postponing/5-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 10"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/10.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline-postponing/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 15"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/15.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline-postponing/15-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 20"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/20.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline-postponing/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 25"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/25.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline-postponing/25-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 30"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline-postponing/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 35"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/35.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline-postponing/35-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 40"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/40.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline-postponing/40-$(date +%Y%m%d-%T).result


echo "time window, true-false, imprecise, baseline-postponing, window = 45"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/45.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/45-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 50"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/50.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/50-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 55"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/55.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/55-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 60"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/60.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/60-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 65"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/65.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/65-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 70"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/70.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/70-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 75"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/75.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/75-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 80"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/80.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/80-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 85"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/85.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/85-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 90"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/90.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/90-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 95"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/95.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/95-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 100"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/100.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/100-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 110"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/110.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/110-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 120"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/120.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/120-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 130"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/130.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/130-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 140"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/140.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/140-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 150"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/150.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/150-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 160"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/160.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/160-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 170"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/170.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/170-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 180"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/180.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/180-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 190"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/190.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/190-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing, window = 200"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/200.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/200-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-false-imprecise-baseline-postponing >>timewindow-true-false-imprecise-baseline-postponing/result.txt
