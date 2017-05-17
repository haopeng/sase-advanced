#!/bin/bash



# Query type: true-false
# postponing
echo "1.truefalse,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/1.truefalse.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 0 0 >>querytype-result/postponing-1-truefalse-$(date +%Y%m%d-%T).result


echo "2.false,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/2.false.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 0 0 >>querytype-result/postponing-2-false-$(date +%Y%m%d-%T).result



echo "3.true,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/3.true.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 0 0 >>querytype-result/postponing-3-true-$(date +%Y%m%d-%T).result



echo "4.inconsistent,  optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/4.inconsistent.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 0 0 >>querytype-result/postponing-4-inconsistent-$(date +%Y%m%d-%T).result


# no postponing
echo "1.truefalse, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/1.truefalse.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 0 0 0 >>querytype-result/nopostponing-1-truefalse-$(date +%Y%m%d-%T).result


echo "2.false, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/2.false.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 0 0 0 >>querytype-result/nopostponing-2-false-$(date +%Y%m%d-%T).result



echo "3.true, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/3.true.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 0 0 0 >>querytype-result/nopostponing-3-true-$(date +%Y%m%d-%T).result



echo "4.inconsistent, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/4.inconsistent.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 0 0 0 >>querytype-result/nopostponing-4-inconsistent-$(date +%Y%m%d-%T).result



# on the fly
echo "1.truefalse,  optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/1.truefalse.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 0 >>querytype-result/fly-1-truefalse-$(date +%Y%m%d-%T).result


echo "2.false,  optimization, on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/2.false.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 0 >>querytype-result/fly-2-false-$(date +%Y%m%d-%T).result



echo "3.true,  optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/3.true.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 0 >>querytype-result/fly-3-true-$(date +%Y%m%d-%T).result



echo "4.inconsistent,  optimization,  on the fly, no collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/4.inconsistent.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 0 >>querytype-result/fly-4-inconsistent-$(date +%Y%m%d-%T).result


# collapsed
echo "1.truefalse,  optimization,  on the fly,  collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/1.truefalse.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 1 >>querytype-result/collapsed-1-truefalse-$(date +%Y%m%d-%T).result


echo "2.false,  optimization, on the fly, collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/2.false.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 1 >>querytype-result/collapsed-2-false-$(date +%Y%m%d-%T).result



echo "3.true,  optimization,  on the fly, collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/3.true.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 1 >>querytype-result/collapsed-3-true-$(date +%Y%m%d-%T).result



echo "4.inconsistent,  optimization,  on the fly,  collapsed format"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/querytype/4.inconsistent.nfa $HOME/sase2013/queries/querytype/querytype.stream 0 1 1 1 >>querytype-result/collapsed-4-inconsistent-$(date +%Y%m%d-%T).result





