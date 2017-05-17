#!/bin/bash

# query 1

echo "selectivity-false-uncertain, query 1, no optimization, no on the fly, no collapsed format"
java -Xms100m -Xmx6000m edu.umass.cs.sase.util.ResultReader $1 