#!/bin/bash

# query 1
echo "selectivity-false, query 1, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/1.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-false/postponing-1-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 1, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/1.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-false/nopostponing-1-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 1, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/1.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-false/onthefly-1-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 1, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/1.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-false/collapsed-1-$(date +%Y%m%d-%T).result
# query 2
echo "selectivity-false, query 2, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/2.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-false/postponing-2-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 2, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/2.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-false/nopostponing-2-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 2, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/2.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-false/onthefly-2-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 2, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/2.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-false/collapsed-2-$(date +%Y%m%d-%T).result
# query 3
echo "selectivity-false, query 3, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/3.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-false/postponing-3-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 3, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/3.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-false/nopostponing-3-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 3, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/3.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-false/onthefly-3-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 3, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/3.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-false/collapsed-3-$(date +%Y%m%d-%T).result
# query 4
echo "selectivity-false, query 4, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/4.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-false/postponing-4-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 4, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/4.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-false/nopostponing-4-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 4, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/4.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-false/onthefly-4-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 4, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/4.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-false/collapsed-4-$(date +%Y%m%d-%T).result
# query 5
echo "selectivity-false, query 5, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/5.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-false/postponing-5-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 5, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/5.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-false/nopostponing-5-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 5, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/5.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-false/onthefly-5-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 5, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/5.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-false/collapsed-5-$(date +%Y%m%d-%T).result
# query 6
echo "selectivity-false, query 6, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/6.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-false/postponing-6-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 6, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/6.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-false/nopostponing-6-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 6, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/6.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-false/onthefly-6-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 6, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/6.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-false/collapsed-6-$(date +%Y%m%d-%T).result
# query 7
echo "selectivity-false, query 7, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/7.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-false/postponing-7-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 7, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/7.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-false/nopostponing-7-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 7, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/7.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-false/onthefly-7-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 7, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/7.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-false/collapsed-7-$(date +%Y%m%d-%T).result
# query 8
echo "selectivity-false, query 8, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/8.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-false/postponing-8-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 8, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/8.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-false/nopostponing-8-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 8, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/8.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-false/onthefly-8-$(date +%Y%m%d-%T).result

echo "selectivity-false, query 8, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/8.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-false/collapsed-8-$(date +%Y%m%d-%T).result
