#!/bin/bash


#datestr = $(date +%Y%m%d-%T)
#mkdir $dirname

#baseline, q1-q10

mkdir selectivity-true-false-precise-baseline


echo "selectivity, true-false, precise, baseline, query 1"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-true-false-precise-baseline/1-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline, query 2"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/2.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-true-false-precise-baseline/2-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline, query 3"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/3.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-true-false-precise-baseline/3-$(date +%Y%m%d-%T).result

echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader selectivity-true-false-precise-baseline >>selectivity-true-false-precise-baseline/result.txt

