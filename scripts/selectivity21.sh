#!/bin/bash


# query 30
echo "selectivity, query 30, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/30.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-30-$(date +%Y%m%d-%T).result

echo "selectivity, query 30, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/30.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-30-$(date +%Y%m%d-%T).result
