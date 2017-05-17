#!/bin/bash



# time window: 10
echo "time window-uncertain, 10, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 1 >>window-result-uncertain/postponing-10-$(date +%Y%m%d-%T).result



echo "time window-uncertain, 10, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 0 >>window-result-uncertain/nopostponing-10-$(date +%Y%m%d-%T).result



# time window: 20
echo "time window-uncertain, 20, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/20.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 1 >>window-result-uncertain/postponing-20-$(date +%Y%m%d-%T).result



echo "time window-uncertain, 20, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/20.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 0 >>window-result-uncertain/nopostponing-20-$(date +%Y%m%d-%T).result

# time window: 30
echo "time window-uncertain, 30, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/30.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 1 >>window-result-uncertain/postponing-30-$(date +%Y%m%d-%T).result



echo "time window-uncertain, 30, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/30.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 0 >>window-result-uncertain/nopostponing-30-$(date +%Y%m%d-%T).result

# time window: 40
echo "time window-uncertain, 40, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/40.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 1 >>window-result-uncertain/postponing-40-$(date +%Y%m%d-%T).result



echo "time window-uncertain, 40, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/40.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 0 >>window-result-uncertain/nopostponing-40-$(date +%Y%m%d-%T).result

# time window: 50
echo "time window-uncertain, 50, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/50.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 1 >>window-result-uncertain/postponing-50-$(date +%Y%m%d-%T).result



echo "time window-uncertain, 50, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/50.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 0 >>window-result-uncertain/nopostponing-50-$(date +%Y%m%d-%T).result

# time window: 60
echo "time window-uncertain, 60, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/60.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 1 >>window-result-uncertain/postponing-60-$(date +%Y%m%d-%T).result



echo "time window-uncertain, 60, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/60.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 0 >>window-result-uncertain/nopostponing-60-$(date +%Y%m%d-%T).result

# time window: 70
echo "time window-uncertain, 70, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/70.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 1 >>window-result-uncertain/postponing-70-$(date +%Y%m%d-%T).result



echo "time window-uncertain, 70, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/70.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 0 >>window-result-uncertain/nopostponing-70-$(date +%Y%m%d-%T).result

# time window: 80
echo "time window-uncertain, 80, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/80.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 1 >>window-result-uncertain/postponing-80-$(date +%Y%m%d-%T).result



echo "time window-uncertain, 80, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/80.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 0 >>window-result-uncertain/nopostponing-80-$(date +%Y%m%d-%T).result

# time window: 90
echo "time window-uncertain, 90, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/90.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 1 >>window-result-uncertain/postponing-90-$(date +%Y%m%d-%T).result



echo "time window-uncertain, 90, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/90.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 0 >>window-result-uncertain/nopostponing-90-$(date +%Y%m%d-%T).result

# time window: 100
echo "time window-uncertain, 100, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/100.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 1 >>window-result-uncertain/postponing-100-$(date +%Y%m%d-%T).result



echo "time window-uncertain, 100, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow/100.nfa $HOME/sase2013/queries/timewindow/window-uncertain.stream 0 0 >>window-result-uncertain/nopostponing-100-$(date +%Y%m%d-%T).result