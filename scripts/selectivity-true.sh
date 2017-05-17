#!/bin/bash

# query 1

echo "selectivity-true-precise, query 1, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/1.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-true-precise/nopostponing-1-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 1, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/1.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-true-precise/onthefly-1-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 1, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/1.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-true-precise/collapsed-1-$(date +%Y%m%d-%T).result
# query 2

echo "selectivity-true-precise, query 2, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/2.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-true-precise/nopostponing-2-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 2, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/2.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-true-precise/onthefly-2-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 2, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/2.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-true-precise/collapsed-2-$(date +%Y%m%d-%T).result
# query 3

echo "selectivity-true-precise, query 3, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/3.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-true-precise/nopostponing-3-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 3, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/3.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-true-precise/onthefly-3-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 3, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/3.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-true-precise/collapsed-3-$(date +%Y%m%d-%T).result
# query 4

echo "selectivity-true-precise, query 4, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-true-precise/nopostponing-4-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 4, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-true-precise/onthefly-4-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 4, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-true-precise/collapsed-4-$(date +%Y%m%d-%T).result
# query 5

echo "selectivity-true-precise, query 5, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/5.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-true-precise/nopostponing-5-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 5, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/5.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-true-precise/onthefly-5-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 5, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/5.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-true-precise/collapsed-5-$(date +%Y%m%d-%T).result
# query 6

echo "selectivity-true-precise, query 6, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/6.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-true-precise/nopostponing-6-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 6, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/6.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-true-precise/onthefly-6-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 6, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/6.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-true-precise/collapsed-6-$(date +%Y%m%d-%T).result
# query 7

echo "selectivity-true-precise, query 7, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/7.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-true-precise/nopostponing-7-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 7, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/7.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-true-precise/onthefly-7-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 7, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/7.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-true-precise/collapsed-7-$(date +%Y%m%d-%T).result
# query 8

echo "selectivity-true-precise, query 8, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/8.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 0 0 0 1 >>selectivity-true-precise/nopostponing-8-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 8, optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/8.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 0 1 >>selectivity-true-precise/onthefly-8-$(date +%Y%m%d-%T).result

echo "selectivity-true-precise, query 8, optimization,  on the fly, collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/8.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 1 1 >>selectivity-true-precise/collapsed-8-$(date +%Y%m%d-%T).result


echo "all is finished except for postponign without on the fly"

echo "selectivity-true-precise, query 1, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/1.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-true-precise/postponing-1-$(date +%Y%m%d-%T).result


echo "selectivity-true-precise, query 2, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/2.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-true-precise/postponing-2-$(date +%Y%m%d-%T).result


echo "selectivity-true-precise, query 3, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/3.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-true-precise/postponing-3-$(date +%Y%m%d-%T).result


echo "selectivity-true-precise, query 4, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-true-precise/postponing-4-$(date +%Y%m%d-%T).result


echo "selectivity-true-precise, query 5, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/5.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-true-precise/postponing-5-$(date +%Y%m%d-%T).result


echo "selectivity-true-precise, query 6, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/6.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-true-precise/postponing-6-$(date +%Y%m%d-%T).result


echo "selectivity-true-precise, query 7, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/7.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-true-precise/postponing-7-$(date +%Y%m%d-%T).result


echo "selectivity-true-precise, query 8, optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/8.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 0 0 1 >>selectivity-true-precise/postponing-8-$(date +%Y%m%d-%T).result
