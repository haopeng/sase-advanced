#!/bin/bash

# query 1
echo "selectivity - uncertain,  query 1, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/1.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 1 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/postponing-1-$(date +%Y%m%d-%T).result

echo "selectivity - uncertain,  query 1, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/1.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 0 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/nopostponing-1-$(date +%Y%m%d-%T).result
# query 2
echo "selectivity - uncertain,  query 2, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/2.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 1 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/postponing-2-$(date +%Y%m%d-%T).result

echo "selectivity - uncertain,  query 2, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/2.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 0 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/nopostponing-2-$(date +%Y%m%d-%T).result
# query 3
echo "selectivity - uncertain,  query 3, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/3.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 1 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/postponing-3-$(date +%Y%m%d-%T).result

echo "selectivity - uncertain,  query 3, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/3.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 0 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/nopostponing-3-$(date +%Y%m%d-%T).result
# query 4
echo "selectivity - uncertain,  query 4, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/4.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 1 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/postponing-4-$(date +%Y%m%d-%T).result

echo "selectivity - uncertain,  query 4, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/4.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 0 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/nopostponing-4-$(date +%Y%m%d-%T).result
# query 5
echo "selectivity - uncertain,  query 5, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/5.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 1 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/postponing-5-$(date +%Y%m%d-%T).result

echo "selectivity - uncertain,  query 5, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/5.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 0 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/nopostponing-5-$(date +%Y%m%d-%T).result
# query 6
echo "selectivity - uncertain,  query 6, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/6.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 1 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/postponing-6-$(date +%Y%m%d-%T).result

echo "selectivity - uncertain,  query 6, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/6.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 0 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/nopostponing-6-$(date +%Y%m%d-%T).result
# query 7
echo "selectivity - uncertain,  query 7, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/7.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 1 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/postponing-7-$(date +%Y%m%d-%T).result

echo "selectivity - uncertain,  query 7, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/7.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 0 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/nopostponing-7-$(date +%Y%m%d-%T).result
# query 8
echo "selectivity - uncertain,  query 8, optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/8.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 1 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/postponing-8-$(date +%Y%m%d-%T).result

echo "selectivity - uncertain,  query 8, no optimization"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-new/8.nfa $HOME/sase2013/queries/selectivity-new/selectivity10-uncertain.stream 0 0 0 0 15 1 >>selectivity-new10-result-uncertain-isolation/nopostponing-8-$(date +%Y%m%d-%T).result
