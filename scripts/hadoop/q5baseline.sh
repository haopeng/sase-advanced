#!/bin/bash



echo "q5, baseline"
java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.HadoopUsecaseImpreciseUI 5.reducebalance-k.nfa balancehadoop5.txt  >>q5/baseline.result


