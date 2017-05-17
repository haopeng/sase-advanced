#!/bin/bash



mkdir streamsize-true-precise-baseline-postponing


echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 100000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity10.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/10-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 200000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity20.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/20-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 300000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity30.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/30-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 400000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity40.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/40-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 500000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity50.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/50-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 600000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity60.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/60-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 700000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity70.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/70-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 800000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity80.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/80-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 900000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity90.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/90-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 1000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity100.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/100-$(date +%Y%m%d-%T).result

echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 2000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity200.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/200-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 3000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity300.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/300-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 4000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity400.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/400-$(date +%Y%m%d-%T).result
echo "stream size, true, precise, baseline+postponing+collapsed format, stream size = 5000000"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-true-precise/4.nfa $HOME/sase2013/queries/selectivity-true-precise/selectivity500.stream 0 1 1 0  >>streamsize-true-precise-baseline-postponing/500-$(date +%Y%m%d-%T).result


echo "wrap up experiments results"
java edu.umass.cs.sase.util.ResultReader streamsize-true-precise-baseline-postponing >>streamsize-true-precise-baseline-postponing/result.txt
