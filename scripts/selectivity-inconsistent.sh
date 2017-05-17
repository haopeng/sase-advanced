#!/bin/bash


# query 1
echo "selectivity-inconsistent, query 1, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/1.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-inconsistent/postponing-1-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 1, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/1.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-inconsistent/nopostponing-1-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 1, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/1.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-inconsistent/onthefly-1-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 1, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/1.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-inconsistent/collapsed-1-$(date +%Y%m%d-%T).result
# query 2
echo "selectivity-inconsistent, query 2, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/2.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-inconsistent/postponing-2-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 2, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/2.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-inconsistent/nopostponing-2-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 2, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/2.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-inconsistent/onthefly-2-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 2, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/2.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-inconsistent/collapsed-2-$(date +%Y%m%d-%T).result
# query 3
echo "selectivity-inconsistent, query 3, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/3.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-inconsistent/postponing-3-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 3, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/3.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-inconsistent/nopostponing-3-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 3, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/3.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-inconsistent/onthefly-3-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 3, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/3.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-inconsistent/collapsed-3-$(date +%Y%m%d-%T).result
# query 4
echo "selectivity-inconsistent, query 4, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/4.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-inconsistent/postponing-4-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 4, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/4.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-inconsistent/nopostponing-4-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 4, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/4.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-inconsistent/onthefly-4-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 4, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/4.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-inconsistent/collapsed-4-$(date +%Y%m%d-%T).result
# query 5
echo "selectivity-inconsistent, query 5, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/5.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-inconsistent/postponing-5-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 5, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/5.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-inconsistent/nopostponing-5-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 5, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/5.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-inconsistent/onthefly-5-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 5, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/5.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-inconsistent/collapsed-5-$(date +%Y%m%d-%T).result
# query 6
echo "selectivity-inconsistent, query 6, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/6.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-inconsistent/postponing-6-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 6, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/6.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-inconsistent/nopostponing-6-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 6, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/6.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-inconsistent/onthefly-6-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 6, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/6.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-inconsistent/collapsed-6-$(date +%Y%m%d-%T).result
# query 7
echo "selectivity-inconsistent, query 7, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/7.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-inconsistent/postponing-7-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 7, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/7.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-inconsistent/nopostponing-7-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 7, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/7.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-inconsistent/onthefly-7-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 7, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/7.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-inconsistent/collapsed-7-$(date +%Y%m%d-%T).result
# query 8
echo "selectivity-inconsistent, query 8, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/8.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-inconsistent/postponing-8-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 8, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/8.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-inconsistent/nopostponing-8-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 8, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/8.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-inconsistent/onthefly-8-$(date +%Y%m%d-%T).result

echo "selectivity-inconsistent, query 8, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent-precise/8.nfa $HOME/sase2013/queries/selectivity-inconsistent-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-inconsistent/collapsed-8-$(date +%Y%m%d-%T).result
