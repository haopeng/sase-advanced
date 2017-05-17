#!/bin/bash

# kvpairs.sh Find number of kv-pairs per reduce

# Find the input file
if [ $# != 1 ]; then
	echo "no input file specified"
	exit
fi
INPUT=$1

# Temporary file with relevant lines
grep 'ReduceAttempt' $INPUT | grep 'COUNTERS' > $INPUT.tmp

# Process each line of the tmp file
while read LINE; do
	# I'm sure I'm grossly abusing awk...

	# Get the reduce id
	REDUCE_ID=`echo $LINE | awk '{print $3}' | awk -F '_' '{print $NF}' | awk -F '\"' '{print $1}' | tr -d '0'`
	if [ "$REDUCE_ID" = "" ]; then
		REDUCE_ID=0
	fi

	# Get the number of kv-pairs
	KV_PAIRS=`echo $LINE | awk -F '(' '{print $NF}' | awk -F ')' '{print $1}'`

	echo -e "r"$REDUCE_ID"\t"$KV_PAIRS >> unsorted.tmp
done < $INPUT.tmp

# Sort by number of kv-pairs
cat unsorted.tmp | sort -nrk 2

# Cleanup tmp files
rm $INPUT.tmp
rm unsorted.tmp
