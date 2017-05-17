#!/bin/bash


# query 1

echo "selectivity-false-uncertain, query 1, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/1.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-inconsistent-uncertain/nopostponing-1-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 1, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/1.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-inconsistent-uncertain/onthefly-1-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 1, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/1.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-inconsistent-uncertain/collapsed-1-$(date +%Y%m%d-%T).result
# query 2

echo "selectivity-false-uncertain, query 2, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/2.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-inconsistent-uncertain/nopostponing-2-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 2, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/2.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-inconsistent-uncertain/onthefly-2-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 2, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/2.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-inconsistent-uncertain/collapsed-2-$(date +%Y%m%d-%T).result
# query 3

echo "selectivity-false-uncertain, query 3, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/3.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-inconsistent-uncertain/nopostponing-3-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 3, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/3.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-inconsistent-uncertain/onthefly-3-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 3, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/3.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-inconsistent-uncertain/collapsed-3-$(date +%Y%m%d-%T).result
# query 4

echo "selectivity-false-uncertain, query 4, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/4.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-inconsistent-uncertain/nopostponing-4-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 4, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/4.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-inconsistent-uncertain/onthefly-4-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 4, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/4.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-inconsistent-uncertain/collapsed-4-$(date +%Y%m%d-%T).result
# query 5

echo "selectivity-false-uncertain, query 5, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/5.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-inconsistent-uncertain/nopostponing-5-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 5, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/5.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-inconsistent-uncertain/onthefly-5-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 5, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/5.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-inconsistent-uncertain/collapsed-5-$(date +%Y%m%d-%T).result
# query 6

echo "selectivity-false-uncertain, query 6, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/6.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-inconsistent-uncertain/nopostponing-6-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 6, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/6.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-inconsistent-uncertain/onthefly-6-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 6, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/6.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-inconsistent-uncertain/collapsed-6-$(date +%Y%m%d-%T).result
# query 7

echo "selectivity-false-uncertain, query 7, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/7.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-inconsistent-uncertain/nopostponing-7-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 7, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/7.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-inconsistent-uncertain/onthefly-7-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 7, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/7.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-inconsistent-uncertain/collapsed-7-$(date +%Y%m%d-%T).result
# query 8

echo "selectivity-false-uncertain, query 8, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/8.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 0 0 0 1 >>selectivity-inconsistent-uncertain/nopostponing-8-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 8, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/8.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 0 1 >>selectivity-inconsistent-uncertain/onthefly-8-$(date +%Y%m%d-%T).result

echo "selectivity-false-uncertain, query 8, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/8.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 1 1 1 >>selectivity-inconsistent-uncertain/collapsed-8-$(date +%Y%m%d-%T).result

echo "all is finished, except for postponing without on the fly"

echo "selectivity-false-uncertain, query 1, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/1.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-inconsistent-uncertain/postponing-1-$(date +%Y%m%d-%T).result


echo "selectivity-false-uncertain, query 2, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/2.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-inconsistent-uncertain/postponing-2-$(date +%Y%m%d-%T).result


echo "selectivity-false-uncertain, query 3, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/3.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-inconsistent-uncertain/postponing-3-$(date +%Y%m%d-%T).result


echo "selectivity-false-uncertain, query 4, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/4.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-inconsistent-uncertain/postponing-4-$(date +%Y%m%d-%T).result


echo "selectivity-false-uncertain, query 5, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/5.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-inconsistent-uncertain/postponing-5-$(date +%Y%m%d-%T).result


echo "selectivity-false-uncertain, query 6, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/6.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-inconsistent-uncertain/postponing-6-$(date +%Y%m%d-%T).result


echo "selectivity-false-uncertain, query 7, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/7.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-inconsistent-uncertain/postponing-7-$(date +%Y%m%d-%T).result


echo "selectivity-false-uncertain, query 8, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-inconsistent/8.nfa $HOME/sase2013/queries/selectivity-inconsistent/selectivity10-uncertain.stream 0 1 0 0 1 >>selectivity-inconsistent-uncertain/postponing-8-$(date +%Y%m%d-%T).result


