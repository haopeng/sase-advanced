#!/bin/bash


mkdir selectivity-true-false-precise-baseline-zstream




echo "selectivity, true-false, precise, baseline + zstream, query 1"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/1-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline + zstream, query 2"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/2.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/2-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline + zstream, query 3"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/3.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/3-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline + zstream, query 4"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/4.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/4-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline + zstream, query 5"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/5.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/5-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline + zstream, query 6"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/6.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/6-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline + zstream, query 7"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/7.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/7-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline + zstream, query 8"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/8.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/8-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline + zstream, query 9"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/9.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/9-$(date +%Y%m%d-%T).result
echo "selectivity, true-false, precise, baseline + zstream, query 10"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/10.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/10-$(date +%Y%m%d-%T).result

echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader selectivity-true-false-precise-baseline-zstream >>selectivity-true-false-precise-baseline-zstream/result.txt
