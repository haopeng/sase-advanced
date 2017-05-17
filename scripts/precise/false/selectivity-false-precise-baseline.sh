#!/bin/bash




mkdir selectivity-false-precise-baseline


echo "selectivity, false, precise, baseline, query 1"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/1.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 >>selectivity-false-precise-baseline/1-$(date +%Y%m%d-%T).result
echo "selectivity, false, precise, baseline, query 2"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/2.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 >>selectivity-false-precise-baseline/2-$(date +%Y%m%d-%T).result
echo "selectivity, false, precise, baseline, query 3"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/3.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 >>selectivity-false-precise-baseline/3-$(date +%Y%m%d-%T).result
echo "selectivity, false, precise, baseline, query 4"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/4.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 >>selectivity-false-precise-baseline/4-$(date +%Y%m%d-%T).result
echo "selectivity, false, precise, baseline, query 5"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/5.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 >>selectivity-false-precise-baseline/5-$(date +%Y%m%d-%T).result
echo "selectivity, false, precise, baseline, query 6"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/6.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 >>selectivity-false-precise-baseline/6-$(date +%Y%m%d-%T).result
echo "selectivity, false, precise, baseline, query 7"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/7.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 >>selectivity-false-precise-baseline/7-$(date +%Y%m%d-%T).result
echo "selectivity, false, precise, baseline, query 8"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/8.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 >>selectivity-false-precise-baseline/8-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader selectivity-false-precise-baseline >>selectivity-false-precise-baseline/result.txt
