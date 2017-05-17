#!/bin/bash


#datestr = $(date +%Y%m%d-%T)
#mkdir $dirname

#baseline, q1-q10

mkdir selectivity-true-false-precise-baseline-postponing-collapsed




echo "selectivity, true-false, precise, baseline+postponing+collapsed format, query 1"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1 >>selectivity-true-false-precise-baseline-postponing-collapsed/1-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline+postponing+collapsed format, query 2"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/2.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1 >>selectivity-true-false-precise-baseline-postponing-collapsed/2-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline+postponing+collapsed format, query 3"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/3.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1 >>selectivity-true-false-precise-baseline-postponing-collapsed/3-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline+postponing+collapsed format, query 4"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/4.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1 >>selectivity-true-false-precise-baseline-postponing-collapsed/4-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline+postponing+collapsed format, query 5"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/5.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1 >>selectivity-true-false-precise-baseline-postponing-collapsed/5-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline+postponing+collapsed format, query 6"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/6.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1 >>selectivity-true-false-precise-baseline-postponing-collapsed/6-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline+postponing+collapsed format, query 7"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/7.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1 >>selectivity-true-false-precise-baseline-postponing-collapsed/7-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline+postponing+collapsed format, query 8"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/8.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1 >>selectivity-true-false-precise-baseline-postponing-collapsed/8-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline+postponing+collapsed format, query 9"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/9.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1 >>selectivity-true-false-precise-baseline-postponing-collapsed/9-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline+postponing+collapsed format, query 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/10.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1 >>selectivity-true-false-precise-baseline-postponing-collapsed/10-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader selectivity-true-false-precise-baseline-postponing-collapsed >>selectivity-true-false-precise-baseline-postponing-collapsed/result.txt
