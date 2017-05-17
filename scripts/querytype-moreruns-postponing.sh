#!/bin/bash



# Query type: true-false
# postponing
echo "1.truefalse,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/querytype2-moreruns/1.truefalse.nfa $HOME/sase2012/queries/querytype2-moreruns/querytype.stream 0 1 0 0 >>querytype2-moreruns-result/postponing-1-truefalse-$(date +%Y%m%d-%T).result


echo "2.false,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/querytype2-moreruns/2.false.nfa $HOME/sase2012/queries/querytype2-moreruns/querytype.stream 0 1 0 0 >>querytype2-moreruns-result/postponing-2-false-$(date +%Y%m%d-%T).result



echo "3.true,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/querytype2-moreruns/3.true.nfa $HOME/sase2012/queries/querytype2-moreruns/querytype.stream 0 1 0 0 >>querytype2-moreruns-result/postponing-3-true-$(date +%Y%m%d-%T).result



echo "4.inconsistent,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/querytype2-moreruns/4.inconsistent.nfa $HOME/sase2012/queries/querytype2-moreruns/querytype.stream 0 1 0 0 >>querytype2-moreruns-result/postponing-4-inconsistent-$(date +%Y%m%d-%T).result

