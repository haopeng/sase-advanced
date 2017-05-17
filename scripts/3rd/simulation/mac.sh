#!/bin/bash

#1
echo "1..."

java -Xms100m -Xmx10000m edu.umass.cs.sase.explanation.simulation.SimulationUI 2000 /Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/6min.stream /Users/haopeng/Documents/DataStreamArchive/ /Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/1.nfa;/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/2.nfa;/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/3.nfa;/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/4.nfa;/Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/simulation/5.nfa; /Users/haopeng/Dropbox/code/workspace/sase-opensource/queries/3rd/case1/b3-2f-chunk.properties True >>/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/21.simulation/logs/1.txt




