#!/bin/bash

# half uncertainty 1
echo "half uncertainty 1, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2013/queries/uncertain-interval/uncertain1.stream 0 1 0 0 15 >>uncertain-interval-result/postponing-1-$(date +%Y%m%d-%T).result

echo "half uncertainty 1, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2013/queries/uncertain-interval/uncertain1.stream 0 0 0 0 15 >>uncertain-interval-result/nopostponing-1-$(date +%Y%m%d-%T).result
# half uncertainty 2
echo "half uncertainty 2, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2013/queries/uncertain-interval/uncertain2.stream 0 1 0 0 15 >>uncertain-interval-result/postponing-2-$(date +%Y%m%d-%T).result

echo "half uncertainty 2, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2013/queries/uncertain-interval/uncertain2.stream 0 0 0 0 15 >>uncertain-interval-result/nopostponing-2-$(date +%Y%m%d-%T).result
# half uncertainty 3
echo "half uncertainty 3, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2013/queries/uncertain-interval/uncertain3.stream 0 1 0 0 15 >>uncertain-interval-result/postponing-3-$(date +%Y%m%d-%T).result

echo "half uncertainty 3, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2013/queries/uncertain-interval/uncertain3.stream 0 0 0 0 15 >>uncertain-interval-result/nopostponing-3-$(date +%Y%m%d-%T).result
# half uncertainty 4
echo "half uncertainty 4, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2013/queries/uncertain-interval/uncertain4.stream 0 1 0 0 15 >>uncertain-interval-result/postponing-4-$(date +%Y%m%d-%T).result

echo "half uncertainty 4, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2013/queries/uncertain-interval/uncertain4.stream 0 0 0 0 15 >>uncertain-interval-result/nopostponing-4-$(date +%Y%m%d-%T).result
# half uncertainty 5
echo "half uncertainty 5, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2013/queries/uncertain-interval/uncertain5.stream 0 1 0 0 15 >>uncertain-interval-result/postponing-5-$(date +%Y%m%d-%T).result

echo "half uncertainty 5, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2013/queries/uncertain-interval/uncertain5.stream 0 0 0 0 15 >>uncertain-interval-result/nopostponing-5-$(date +%Y%m%d-%T).result
