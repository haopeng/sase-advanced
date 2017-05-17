#!/bin/bash

mkdir selectivity-inconsistent-imprecise-baseline


echo "selectivity, inconsistent, imprecise, baseline, query 1"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent-imprecise/1.nfa $HOME/sase2013/queries/selectivity-inconsistent-imprecise/selectivity-imprecise.stream 0 0 0 0 10 0 0 0.5 2 >>selectivity-inconsistent-imprecise-baseline/1-$(date +%Y%m%d-%T).result
echo "selectivity, inconsistent, imprecise, baseline, query 2"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent-imprecise/2.nfa $HOME/sase2013/queries/selectivity-inconsistent-imprecise/selectivity-imprecise.stream 0 0 0 0 10 0 0 0.5 2 >>selectivity-inconsistent-imprecise-baseline/2-$(date +%Y%m%d-%T).result
echo "selectivity, inconsistent, imprecise, baseline, query 3"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent-imprecise/3.nfa $HOME/sase2013/queries/selectivity-inconsistent-imprecise/selectivity-imprecise.stream 0 0 0 0 10 0 0 0.5 2 >>selectivity-inconsistent-imprecise-baseline/3-$(date +%Y%m%d-%T).result
echo "selectivity, inconsistent, imprecise, baseline, query 4"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent-imprecise/4.nfa $HOME/sase2013/queries/selectivity-inconsistent-imprecise/selectivity-imprecise.stream 0 0 0 0 10 0 0 0.5 2 >>selectivity-inconsistent-imprecise-baseline/4-$(date +%Y%m%d-%T).result
echo "selectivity, inconsistent, imprecise, baseline, query 5"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent-imprecise/5.nfa $HOME/sase2013/queries/selectivity-inconsistent-imprecise/selectivity-imprecise.stream 0 0 0 0 10 0 0 0.5 2 >>selectivity-inconsistent-imprecise-baseline/5-$(date +%Y%m%d-%T).result
echo "selectivity, inconsistent, imprecise, baseline, query 6"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent-imprecise/6.nfa $HOME/sase2013/queries/selectivity-inconsistent-imprecise/selectivity-imprecise.stream 0 0 0 0 10 0 0 0.5 2 >>selectivity-inconsistent-imprecise-baseline/6-$(date +%Y%m%d-%T).result
echo "selectivity, inconsistent, imprecise, baseline, query 7"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent-imprecise/7.nfa $HOME/sase2013/queries/selectivity-inconsistent-imprecise/selectivity-imprecise.stream 0 0 0 0 10 0 0 0.5 2 >>selectivity-inconsistent-imprecise-baseline/7-$(date +%Y%m%d-%T).result
echo "selectivity, inconsistent, imprecise, baseline, query 8"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent-imprecise/8.nfa $HOME/sase2013/queries/selectivity-inconsistent-imprecise/selectivity-imprecise.stream 0 0 0 0 10 0 0 0.5 2 >>selectivity-inconsistent-imprecise-baseline/8-$(date +%Y%m%d-%T).result



echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader selectivity-inconsistent-imprecise-baseline >>selectivity-inconsistent-imprecise-baseline/result.txt

