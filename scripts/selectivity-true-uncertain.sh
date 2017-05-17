#!/bin/bash

# query 1

echo "selectivity-true-uncertain, query 1, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/1.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-true-uncertain/nopostponing-1-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 1, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/1.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-true-uncertain/onthefly-1-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 1, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/1.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-true-uncertain/collapsed-1-$(date +%Y%m%d-%T).result
# query 2

echo "selectivity-true-uncertain, query 2, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/2.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-true-uncertain/nopostponing-2-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 2, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/2.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-true-uncertain/onthefly-2-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 2, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/2.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-true-uncertain/collapsed-2-$(date +%Y%m%d-%T).result
# query 3

echo "selectivity-true-uncertain, query 3, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/3.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-true-uncertain/nopostponing-3-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 3, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/3.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-true-uncertain/onthefly-3-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 3, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/3.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-true-uncertain/collapsed-3-$(date +%Y%m%d-%T).result
# query 4

echo "selectivity-true-uncertain, query 4, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/4.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-true-uncertain/nopostponing-4-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 4, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/4.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-true-uncertain/onthefly-4-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 4, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/4.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-true-uncertain/collapsed-4-$(date +%Y%m%d-%T).result
# query 5

echo "selectivity-true-uncertain, query 5, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/5.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-true-uncertain/nopostponing-5-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 5, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/5.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-true-uncertain/onthefly-5-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 5, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/5.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-true-uncertain/collapsed-5-$(date +%Y%m%d-%T).result
# query 6

echo "selectivity-true-uncertain, query 6, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/6.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-true-uncertain/nopostponing-6-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 6, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/6.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-true-uncertain/onthefly-6-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 6, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/6.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-true-uncertain/collapsed-6-$(date +%Y%m%d-%T).result
# query 7

echo "selectivity-true-uncertain, query 7, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/7.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-true-uncertain/nopostponing-7-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 7, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/7.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-true-uncertain/onthefly-7-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 7, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/7.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-true-uncertain/collapsed-7-$(date +%Y%m%d-%T).result
# query 8

echo "selectivity-true-uncertain, query 8, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/8.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-true-uncertain/nopostponing-8-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 8, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/8.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-true-uncertain/onthefly-8-$(date +%Y%m%d-%T).result

echo "selectivity-true-uncertain, query 8, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/8.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-true-uncertain/collapsed-8-$(date +%Y%m%d-%T).result

echo "all is finished except for postponing without on the fly"

echo "selectivity-true-uncertain, query 1, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/1.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-true-uncertain/postponing-1-$(date +%Y%m%d-%T).result


echo "selectivity-true-uncertain, query 2, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/2.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-true-uncertain/postponing-2-$(date +%Y%m%d-%T).result


echo "selectivity-true-uncertain, query 3, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/3.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-true-uncertain/postponing-3-$(date +%Y%m%d-%T).result


echo "selectivity-true-uncertain, query 4, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/4.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-true-uncertain/postponing-4-$(date +%Y%m%d-%T).result


echo "selectivity-true-uncertain, query 5, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/5.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-true-uncertain/postponing-5-$(date +%Y%m%d-%T).result


echo "selectivity-true-uncertain, query 6, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/6.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-true-uncertain/postponing-6-$(date +%Y%m%d-%T).result


echo "selectivity-true-uncertain, query 7, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/7.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-true-uncertain/postponing-7-$(date +%Y%m%d-%T).result


echo "selectivity-true-uncertain, query 8, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-true/8.nfa $HOME/sase2013/queries/selectivity-true/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-true-uncertain/postponing-8-$(date +%Y%m%d-%T).result



