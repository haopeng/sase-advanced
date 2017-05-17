#!/bin/bash

# query 1
echo "selectivity-uncertain, query 1, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/1.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 1 >>selectivity-new10-result/postponing-1-$(date +%Y%m%d-%T).result

echo "selectivity-uncertain, query 1, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/1.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 0 >>selectivity-new10-result/nopostponing-1-$(date +%Y%m%d-%T).result
# query 2
echo "selectivity-uncertain, query 2, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/2.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 1 >>selectivity-new10-result/postponing-2-$(date +%Y%m%d-%T).result

echo "selectivity-uncertain, query 2, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/2.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 0 >>selectivity-new10-result/nopostponing-2-$(date +%Y%m%d-%T).result
# query 3
echo "selectivity-uncertain, query 3, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/3.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 1 >>selectivity-new10-result/postponing-3-$(date +%Y%m%d-%T).result

echo "selectivity-uncertain, query 3, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/3.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 0 >>selectivity-new10-result/nopostponing-3-$(date +%Y%m%d-%T).result
# query 4
echo "selectivity-uncertain, query 4, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/4.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 1 >>selectivity-new10-result/postponing-4-$(date +%Y%m%d-%T).result

echo "selectivity-uncertain, query 4, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/4.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 0 >>selectivity-new10-result/nopostponing-4-$(date +%Y%m%d-%T).result
# query 5
echo "selectivity-uncertain, query 5, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/5.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 1 >>selectivity-new10-result/postponing-5-$(date +%Y%m%d-%T).result

echo "selectivity-uncertain, query 5, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/5.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 0 >>selectivity-new10-result/nopostponing-5-$(date +%Y%m%d-%T).result
# query 6
echo "selectivity-uncertain, query 6, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/6.nfa $HOME/sase2012/queries/selectivity-new/selectivity23.stream 0 1 >>selectivity-new10-result/postponing-6-$(date +%Y%m%d-%T).result

echo "selectivity-uncertain, query 6, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/6.nfa $HOME/sase2012/queries/selectivity-new/selectivity23.stream 0 0 >>selectivity-new10-result/nopostponing-6-$(date +%Y%m%d-%T).result
# query 7
echo "selectivity-uncertain, query 7, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/7.nfa $HOME/sase2012/queries/selectivity-new/selectivity23.stream 0 1 >>selectivity-new10-result/postponing-7-$(date +%Y%m%d-%T).result

echo "selectivity-uncertain, query 7, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/7.nfa $HOME/sase2012/queries/selectivity-new/selectivity23.stream 0 0 >>selectivity-new10-result/nopostponing-7-$(date +%Y%m%d-%T).result
# query 8
echo "selectivity-uncertain, query 8, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/8.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 1 >>selectivity-new10-result/postponing-8-$(date +%Y%m%d-%T).result

echo "selectivity-uncertain, query 8, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/8.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 0 >>selectivity-new10-result/nopostponing-8-$(date +%Y%m%d-%T).result
# query 9
echo "selectivity-uncertain, query 9, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/9.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 1 >>selectivity-new10-result/postponing-9-$(date +%Y%m%d-%T).result

echo "selectivity-uncertain, query 9, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/9.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 0 >>selectivity-new10-result/nopostponing-9-$(date +%Y%m%d-%T).result
# query 10
echo "selectivity-uncertain, query 10, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/10.nfa $HOME/sase2012/queries/selectivity-new/selectivity22.stream 0 1 >>selectivity-new10-result/postponing-10-$(date +%Y%m%d-%T).result

echo "selectivity-uncertain, query 10, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/10.nfa $HOME/sase2012/queries/selectivity-new/selectivity22.stream 0 0 >>selectivity-new10-result/nopostponing-10-$(date +%Y%m%d-%T).result
