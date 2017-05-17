#!/bin/bash


mkdir timewindow-true-false-imprecise-baseline-postponing-dpc-0.5
echo "threshold = 0.5"

echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 5"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/5.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/5-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 10"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/10.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 15"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/15.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/15-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 20"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/20.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 25"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/25.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/25-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 30"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 35"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/35.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/35-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 40"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/40.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/40-$(date +%Y%m%d-%T).result

echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 45"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/45.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/45-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 50"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/50.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/50-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 55"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/55.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/55-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 60"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/60.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/60-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 65"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/65.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/65-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 70"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/70.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/70-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 75"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/75.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/75-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 80"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/80.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/80-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 85"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/85.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/85-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 90"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/90.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/90-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 95"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/95.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/95-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 100"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/100.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/100-$(date +%Y%m%d-%T).result


echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 110"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/110.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/110-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 120"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/120.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/120-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 130"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/130.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/130-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 140"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/140.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/140-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 150"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/150.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/150-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 160"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/160.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/160-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 170"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/170.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/170-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 180"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/180.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/180-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 190"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/190.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/190-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.5, window = 200"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/200.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/200-$(date +%Y%m%d-%T).result




echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-false-imprecise-baseline-postponing-dpc-0.5 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.5/result.txt
