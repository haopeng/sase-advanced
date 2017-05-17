#!/bin/bash

mkdir uncertaintyinterval-true-imprecise-baseline-postponing

echo "uncertaintyinterval, true, imprecise, baseline-postponing, half uncertainty interval = 5"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true/imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>uncertaintyinterval-true-imprecise-baseline-postponing/5-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, true, imprecise, baseline-postponing, half uncertainty interval = 10"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true/imprecise.stream 0 1 1 0 10 0 0 0.5 10 >>uncertaintyinterval-true-imprecise-baseline-postponing/10-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, true, imprecise, baseline-postponing, half uncertainty interval = 15"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true/imprecise.stream 0 1 1 0 10 0 0 0.5 15 >>uncertaintyinterval-true-imprecise-baseline-postponing/15-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, true, imprecise, baseline-postponing, half uncertainty interval = 20"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true/imprecise.stream 0 1 1 0 10 0 0 0.5 20 >>uncertaintyinterval-true-imprecise-baseline-postponing/20-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, true, imprecise, baseline-postponing, half uncertainty interval = 25"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true/imprecise.stream 0 1 1 0 10 0 0 0.5 25 >>uncertaintyinterval-true-imprecise-baseline-postponing/25-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, true, imprecise, baseline-postponing, half uncertainty interval = 30"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true/imprecise.stream 0 1 1 0 10 0 0 0.5 30 >>uncertaintyinterval-true-imprecise-baseline-postponing/30-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, true, imprecise, baseline-postponing, half uncertainty interval = 35"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true/imprecise.stream 0 1 1 0 10 0 0 0.5 35 >>uncertaintyinterval-true-imprecise-baseline-postponing/35-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, true, imprecise, baseline-postponing, half uncertainty interval = 40"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true/imprecise.stream 0 1 1 0 10 0 0 0.5 40 >>uncertaintyinterval-true-imprecise-baseline-postponing/40-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, true, imprecise, baseline-postponing, half uncertainty interval = 45"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true/imprecise.stream 0 1 1 0 10 0 0 0.5 45 >>uncertaintyinterval-true-imprecise-baseline-postponing/45-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, true, imprecise, baseline-postponing, half uncertainty interval = 50"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true/imprecise.stream 0 1 1 0 10 0 0 0.5 50 >>uncertaintyinterval-true-imprecise-baseline-postponing/50-$(date +%Y%m%d-%T).result



echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader uncertaintyinterval-true-imprecise-baseline-postponing >>uncertaintyinterval-true-imprecise-baseline-postponing/result.txt

