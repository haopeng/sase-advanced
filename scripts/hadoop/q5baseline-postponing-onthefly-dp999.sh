#!/bin/bash



echo "q5, baseline"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.HadoopUsecaseImpreciseUI 5.reducebalance-k.nfa balancehadoop5.txt 0 1 1 0 1 0 0 0.999 0 1 >>q5/dp999.result


