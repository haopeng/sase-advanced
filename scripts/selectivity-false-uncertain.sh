#!/bin/bash

# query 1

echo "selectivity-false-uncertain, query 1, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/1.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-false-uncertain/nopostponing-1-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 1, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/1.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-false-uncertain/onthefly-1-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 1, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/1.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-false-uncertain/collapsed-1-$(date +%Y%m%d-%T).result
# query 2

echo "selectivity-false-uncertain, query 2, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/2.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-false-uncertain/nopostponing-2-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 2, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/2.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-false-uncertain/onthefly-2-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 2, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/2.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-false-uncertain/collapsed-2-$(date +%Y%m%d-%T).result
# query 3

echo "selectivity-false-uncertain, query 3, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/3.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-false-uncertain/nopostponing-3-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 3, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/3.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-false-uncertain/onthefly-3-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 3, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/3.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-false-uncertain/collapsed-3-$(date +%Y%m%d-%T).result
# query 4

echo "selectivity-false-uncertain, query 4, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/4.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-false-uncertain/nopostponing-4-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 4, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/4.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-false-uncertain/onthefly-4-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 4, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/4.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-false-uncertain/collapsed-4-$(date +%Y%m%d-%T).result
# query 5

echo "selectivity-false-uncertain, query 5, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/5.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-false-uncertain/nopostponing-5-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 5, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/5.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-false-uncertain/onthefly-5-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 5, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/5.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-false-uncertain/collapsed-5-$(date +%Y%m%d-%T).result
# query 6

echo "selectivity-false-uncertain, query 6, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/6.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-false-uncertain/nopostponing-6-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 6, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/6.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-false-uncertain/onthefly-6-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 6, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/6.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-false-uncertain/collapsed-6-$(date +%Y%m%d-%T).result
# query 7

echo "selectivity-false-uncertain, query 7, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/7.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-false-uncertain/nopostponing-7-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 7, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/7.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-false-uncertain/onthefly-7-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 7, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/7.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-false-uncertain/collapsed-7-$(date +%Y%m%d-%T).result
# query 8

echo "selectivity-false-uncertain, query 8, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/8.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-false-uncertain/nopostponing-8-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 8, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/8.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-false-uncertain/onthefly-8-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 8, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/8.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-false-uncertain/collapsed-8-$(date +%Y%m%d-%T).result


echo " all is finished, except the postponing without on the fly"

echo "selectivity-false-uncertain, query 1, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/1.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-false-uncertain/postponing-1-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 2, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/2.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-false-uncertain/postponing-2-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 3, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/3.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-false-uncertain/postponing-3-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 4, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/4.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-false-uncertain/postponing-4-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 5, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/5.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-false-uncertain/postponing-5-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 6, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/6.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-false-uncertain/postponing-6-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 7, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/7.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-false-uncertain/postponing-7-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 8, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-false/8.nfa $HOME/sase2013/queries/selectivity-false/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-false-uncertain/postponing-8-$(date +%Y%m%d-%T).result




