#!/bin/bash


mkdir selectivity-false-imprecise-baseline-postponing-collapsed

echo "selectivity, false, imprecise, baseline-postponing, query 1"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false-imprecise/1.nfa $HOME/sase2013/queries/selectivity-false-imprecise/selectivity-imprecise.stream 0 1 1 1 10 0 0 0.5 2 >>selectivity-false-imprecise-baseline-postponing-collapsed/1-$(date +%Y%m%d-%T).result
echo "selectivity, false, imprecise, baseline-postponing, query 2"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false-imprecise/2.nfa $HOME/sase2013/queries/selectivity-false-imprecise/selectivity-imprecise.stream 0 1 1 1 10 0 0 0.5 2 >>selectivity-false-imprecise-baseline-postponing-collapsed/2-$(date +%Y%m%d-%T).result
echo "selectivity, false, imprecise, baseline-postponing, query 3"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false-imprecise/3.nfa $HOME/sase2013/queries/selectivity-false-imprecise/selectivity-imprecise.stream 0 1 1 1 10 0 0 0.5 2 >>selectivity-false-imprecise-baseline-postponing-collapsed/3-$(date +%Y%m%d-%T).result
echo "selectivity, false, imprecise, baseline-postponing, query 4"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false-imprecise/4.nfa $HOME/sase2013/queries/selectivity-false-imprecise/selectivity-imprecise.stream 0 1 1 1 10 0 0 0.5 2 >>selectivity-false-imprecise-baseline-postponing-collapsed/4-$(date +%Y%m%d-%T).result
echo "selectivity, false, imprecise, baseline-postponing, query 5"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false-imprecise/5.nfa $HOME/sase2013/queries/selectivity-false-imprecise/selectivity-imprecise.stream 0 1 1 1 10 0 0 0.5 2 >>selectivity-false-imprecise-baseline-postponing-collapsed/5-$(date +%Y%m%d-%T).result
echo "selectivity, false, imprecise, baseline-postponing, query 6"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false-imprecise/6.nfa $HOME/sase2013/queries/selectivity-false-imprecise/selectivity-imprecise.stream 0 1 1 1 10 0 0 0.5 2 >>selectivity-false-imprecise-baseline-postponing-collapsed/6-$(date +%Y%m%d-%T).result
echo "selectivity, false, imprecise, baseline-postponing, query 7"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false-imprecise/7.nfa $HOME/sase2013/queries/selectivity-false-imprecise/selectivity-imprecise.stream 0 1 1 1 10 0 0 0.5 2 >>selectivity-false-imprecise-baseline-postponing-collapsed/7-$(date +%Y%m%d-%T).result
echo "selectivity, false, imprecise, baseline-postponing, query 8"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false-imprecise/8.nfa $HOME/sase2013/queries/selectivity-false-imprecise/selectivity-imprecise.stream 0 1 1 1 10 0 0 0.5 2 >>selectivity-false-imprecise-baseline-postponing-collapsed/8-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader selectivity-false-imprecise-baseline-postponing-collapsed >>selectivity-false-imprecise-baseline-postponing-collapsed/result.txt
