#!/bin/bash



# time window: 10
echo "time window, 10, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-10-$(date +%Y%m%d-%T).result



echo "time window, 10, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>window-result/nopostponing-10-$(date +%Y%m%d-%T).result



# time window: 20
echo "time window, 20, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/20.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-20-$(date +%Y%m%d-%T).result



echo "time window, 20, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/20.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>window-result/nopostponing-20-$(date +%Y%m%d-%T).result

# time window: 30
echo "time window, 30, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/30.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-30-$(date +%Y%m%d-%T).result



echo "time window, 30, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/30.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>window-result/nopostponing-30-$(date +%Y%m%d-%T).result

# time window: 40
echo "time window, 40, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/40.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-40-$(date +%Y%m%d-%T).result



echo "time window, 40, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/40.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>window-result/nopostponing-40-$(date +%Y%m%d-%T).result

# time window: 50
echo "time window, 50, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/50.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-50-$(date +%Y%m%d-%T).result



echo "time window, 50, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/50.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>window-result/nopostponing-50-$(date +%Y%m%d-%T).result

# time window: 60
echo "time window, 60, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/60.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-60-$(date +%Y%m%d-%T).result



echo "time window, 60, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/60.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>window-result/nopostponing-60-$(date +%Y%m%d-%T).result

# time window: 70
echo "time window, 70, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/70.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-70-$(date +%Y%m%d-%T).result



echo "time window, 70, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/70.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>window-result/nopostponing-70-$(date +%Y%m%d-%T).result

# time window: 80
echo "time window, 80, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/80.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-80-$(date +%Y%m%d-%T).result



echo "time window, 80, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/80.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>window-result/nopostponing-80-$(date +%Y%m%d-%T).result

# time window: 90
echo "time window, 90, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/90.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-90-$(date +%Y%m%d-%T).result



echo "time window, 90, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/90.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>window-result/nopostponing-90-$(date +%Y%m%d-%T).result

# time window: 100
echo "time window, 100, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/100.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-100-$(date +%Y%m%d-%T).result



echo "time window, 100, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/100.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>window-result/nopostponing-100-$(date +%Y%m%d-%T).result