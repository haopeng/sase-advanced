#!/bin/bash



echo "time window, false, precise, baseline+zstream, window = 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/10.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/10-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline+zstream, window = 20"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/20.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/20-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline+zstream, window = 30"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/30.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/30-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline+zstream, window = 40"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/40.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/40-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline+zstream, window = 50"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/50.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/50-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline+zstream, window = 60"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/60.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/60-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline+zstream, window = 70"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/70.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/70-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline+zstream, window = 80"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/80.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/80-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline+zstream, window = 90"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/90.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/90-$(date +%Y%m%d-%T).result
echo "time window, false, precise, baseline+zstream, window = 100"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/100.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/100-$(date +%Y%m%d-%T).result
