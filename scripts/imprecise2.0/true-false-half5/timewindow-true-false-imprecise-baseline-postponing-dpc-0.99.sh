#!/bin/bash


mkdir timewindow-true-false-imprecise-baseline-postponing-dpc-0.99
echo "threshold = 0.99"

echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 5"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/5.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/5-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 10"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/10.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/10-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 15"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/15.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/15-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 20"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/20.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/20-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 25"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/25.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/25-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 30"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/30-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 35"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/35.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/35-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc, window = 40"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/40.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/40-$(date +%Y%m%d-%T).result

echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 45"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/45.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/45-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 50"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/50.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/50-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 55"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/55.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/55-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 60"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/60.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/60-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 65"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/65.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/65-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 70"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/70.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/70-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 75"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/75.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/75-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 80"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/80.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/80-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 85"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/85.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/85-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 90"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/90.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/90-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 95"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/95.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/95-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 100"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/100.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/100-$(date +%Y%m%d-%T).result


echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 110"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/110.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/110-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 120"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/120.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/120-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 130"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/130.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/130-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 140"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/140.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/140-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 150"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/150.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/150-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 160"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/160.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/160-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 170"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/170.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/170-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 180"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/180.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/180-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 190"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/190.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/190-$(date +%Y%m%d-%T).result
echo "time window, true-false, imprecise, baseline-postponing-dpc-0.99, window = 200"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/200.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.99 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/200-$(date +%Y%m%d-%T).result
echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader timewindow-true-false-imprecise-baseline-postponing-dpc-0.99 >>timewindow-true-false-imprecise-baseline-postponing-dpc-0.99/result.txt
