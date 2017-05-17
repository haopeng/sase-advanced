#!/bin/bash


#datestr = $(date +%Y%m%d-%T)
#mkdir $dirname


#baseline
#baseline + zstream
#baseline + postponing
#baseline + zstream + postponing
#baseline + zstream + collapsed format


# query 1
echo "selectivity, query 1, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-1-$(date +%Y%m%d-%T).result



echo "selectivity, query 1, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-1-$(date +%Y%m%d-%T).result



# query 2
echo "selectivity, query 2, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/2.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-2-$(date +%Y%m%d-%T).result



echo "selectivity, query 2, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/2.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-2-$(date +%Y%m%d-%T).result

# query 3
echo "selectivity, query 3, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/3.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-3-$(date +%Y%m%d-%T).result



echo "selectivity, query 3, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/3.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-3-$(date +%Y%m%d-%T).result


# query 4
echo "selectivity, query 4, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/4.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-4-$(date +%Y%m%d-%T).result



echo "selectivity, query 4, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/4.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-4-$(date +%Y%m%d-%T).result


# query 5
echo "selectivity, query 5, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/5.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-5-$(date +%Y%m%d-%T).result



echo "selectivity, query 5, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/5.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-5-$(date +%Y%m%d-%T).result


# query 6
echo "selectivity, query 6, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/6.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-6-$(date +%Y%m%d-%T).result



echo "selectivity, query 6, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/6.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-6-$(date +%Y%m%d-%T).result

# query 7
echo "selectivity, query 7, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/7.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-7-$(date +%Y%m%d-%T).result



echo "selectivity, query 7, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/7.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-7-$(date +%Y%m%d-%T).result

# query 8
echo "selectivity, query 8, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/8.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-8-$(date +%Y%m%d-%T).result



echo "selectivity, query 8, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/8.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-8-$(date +%Y%m%d-%T).result


# query 9
echo "selectivity, query 9, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/9.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-9-$(date +%Y%m%d-%T).result



echo "selectivity, query 9, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/9.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-9-$(date +%Y%m%d-%T).result

# query 10
echo "selectivity, query 10, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/10.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-10-$(date +%Y%m%d-%T).result



echo "selectivity, query 10, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/10.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-10-$(date +%Y%m%d-%T).result


# query 11
echo "selectivity, query 11, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/11.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-11-$(date +%Y%m%d-%T).result



echo "selectivity, query 11, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/11.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-11-$(date +%Y%m%d-%T).result

