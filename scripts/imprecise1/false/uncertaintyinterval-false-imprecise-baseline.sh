#!/bin/bash

mkdir uncertaintyinterval-false-imprecise-baseline


echo "uncertaintyinterval, false, imprecise, baseline, half uncertainty interval = 5"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-false/imprecise.stream 0 0 0 0 10 0 0 0.5 5 >>uncertaintyinterval-false-imprecise-baseline/5-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, false, imprecise, baseline, half uncertainty interval = 10"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-false/imprecise.stream 0 0 0 0 10 0 0 0.5 10 >>uncertaintyinterval-false-imprecise-baseline/10-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, false, imprecise, baseline, half uncertainty interval = 15"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-false/imprecise.stream 0 0 0 0 10 0 0 0.5 15 >>uncertaintyinterval-false-imprecise-baseline/15-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, false, imprecise, baseline, half uncertainty interval = 20"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-false/imprecise.stream 0 0 0 0 10 0 0 0.5 20 >>uncertaintyinterval-false-imprecise-baseline/20-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, false, imprecise, baseline, half uncertainty interval = 25"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-false/imprecise.stream 0 0 0 0 10 0 0 0.5 25 >>uncertaintyinterval-false-imprecise-baseline/25-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, false, imprecise, baseline, half uncertainty interval = 30"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-false/imprecise.stream 0 0 0 0 10 0 0 0.5 30 >>uncertaintyinterval-false-imprecise-baseline/30-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, false, imprecise, baseline, half uncertainty interval = 35"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-false/imprecise.stream 0 0 0 0 10 0 0 0.5 35 >>uncertaintyinterval-false-imprecise-baseline/35-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, false, imprecise, baseline, half uncertainty interval = 40"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-false/imprecise.stream 0 0 0 0 10 0 0 0.5 40 >>uncertaintyinterval-false-imprecise-baseline/40-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, false, imprecise, baseline, half uncertainty interval = 45"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-false/imprecise.stream 0 0 0 0 10 0 0 0.5 45 >>uncertaintyinterval-false-imprecise-baseline/45-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, false, imprecise, baseline, half uncertainty interval = 50"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-false/imprecise.stream 0 0 0 0 10 0 0 0.5 50 >>uncertaintyinterval-false-imprecise-baseline/50-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader uncertaintyinterval-false-imprecise-baseline >>uncertaintyinterval-false-imprecise-baseline/result.txt

