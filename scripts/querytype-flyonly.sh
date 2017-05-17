#!/bin/bash





# on the fly
echo "1.truefalse,  optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/1.truefalse.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 0 >>querytype-result/fly-1-truefalse-$(date +%Y%m%d-%T).result


echo "2.false,  optimization, on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/2.false.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 0 >>querytype-result/fly-2-false-$(date +%Y%m%d-%T).result



echo "3.true,  optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/3.true.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 0 >>querytype-result/fly-3-true-$(date +%Y%m%d-%T).result



echo "4.inconsistent,  optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/4.inconsistent.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 0 >>querytype-result/fly-4-inconsistent-$(date +%Y%m%d-%T).result





