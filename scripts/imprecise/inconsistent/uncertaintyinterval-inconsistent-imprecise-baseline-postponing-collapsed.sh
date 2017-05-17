#!/bin/bash

mkdir uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed

echo "uncertaintyinterval, inconsistent, imprecise, baseline-postponing-collapsed, half uncertainty interval = 5"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-inconsistent/1.nfa $HOME/sase2013/queries/uncertaintyinterval-inconsistent/imprecise.stream 0 1 1 1 10 0 0 0.5 5 >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/5-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, inconsistent, imprecise, baseline-postponing-collapsed, half uncertainty interval = 10"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-inconsistent/1.nfa $HOME/sase2013/queries/uncertaintyinterval-inconsistent/imprecise.stream 0 1 1 1 10 0 0 0.5 10 >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/10-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, inconsistent, imprecise, baseline-postponing-collapsed, half uncertainty interval = 15"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-inconsistent/1.nfa $HOME/sase2013/queries/uncertaintyinterval-inconsistent/imprecise.stream 0 1 1 1 10 0 0 0.5 15 >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/15-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, inconsistent, imprecise, baseline-postponing-collapsed, half uncertainty interval = 20"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-inconsistent/1.nfa $HOME/sase2013/queries/uncertaintyinterval-inconsistent/imprecise.stream 0 1 1 1 10 0 0 0.5 20 >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/20-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, inconsistent, imprecise, baseline-postponing-collapsed, half uncertainty interval = 25"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-inconsistent/1.nfa $HOME/sase2013/queries/uncertaintyinterval-inconsistent/imprecise.stream 0 1 1 1 10 0 0 0.5 25 >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/25-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, inconsistent, imprecise, baseline-postponing-collapsed, half uncertainty interval = 30"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-inconsistent/1.nfa $HOME/sase2013/queries/uncertaintyinterval-inconsistent/imprecise.stream 0 1 1 1 10 0 0 0.5 30 >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/30-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, inconsistent, imprecise, baseline-postponing-collapsed, half uncertainty interval = 35"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-inconsistent/1.nfa $HOME/sase2013/queries/uncertaintyinterval-inconsistent/imprecise.stream 0 1 1 1 10 0 0 0.5 35 >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/35-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, inconsistent, imprecise, baseline-postponing-collapsed, half uncertainty interval = 40"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-inconsistent/1.nfa $HOME/sase2013/queries/uncertaintyinterval-inconsistent/imprecise.stream 0 1 1 1 10 0 0 0.5 40 >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/40-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, inconsistent, imprecise, baseline-postponing-collapsed, half uncertainty interval = 45"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-inconsistent/1.nfa $HOME/sase2013/queries/uncertaintyinterval-inconsistent/imprecise.stream 0 1 1 1 10 0 0 0.5 45 >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/45-$(date +%Y%m%d-%T).result
echo "uncertaintyinterval, inconsistent, imprecise, baseline-postponing-collapsed, half uncertainty interval = 50"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-inconsistent/1.nfa $HOME/sase2013/queries/uncertaintyinterval-inconsistent/imprecise.stream 0 1 1 1 10 0 0 0.5 50 >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/50-$(date +%Y%m%d-%T).result



echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed >>uncertaintyinterval-inconsistent-imprecise-baseline-postponing-collapsed/result.txt

