#!/bin/bash


#datestr = $(date +%Y%m%d-%T)
#mkdir $dirname

#baseline, q1-q10

mkdir streamsize-true-false-precise-baseline-postponing-collapsed


echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 100000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity10.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/10-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 200000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity20.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/20-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 300000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity30.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/30-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 400000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity40.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/40-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 500000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity50.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/50-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 600000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity60.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/60-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 700000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity70.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/70-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 800000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity80.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/80-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 900000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity90.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/90-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 1000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity100.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/100-$(date +%Y%m%d-%T).result

echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 2000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity200.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/200-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 3000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity300.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/300-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 4000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity400.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/400-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 5000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity500.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/500-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 6000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity600.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/600-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 7000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity700.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/700-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 8000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity800.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/800-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 9000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity900.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/900-$(date +%Y%m%d-%T).result
echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 10000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity1000.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/1000-$(date +%Y%m%d-%T).result

echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 100000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity10000.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/10000-$(date +%Y%m%d-%T).result


echo "stream size, true-false, precise, baseline+postponing+collapsed format, stream size = 1000000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity100000.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/100000-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader streamsize-true-false-precise-baseline-postponing-collapsed >>streamsize-true-false-precise-baseline-postponing-collapsed/result.txt
