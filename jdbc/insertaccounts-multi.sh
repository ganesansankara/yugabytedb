#!/bin/sh

for i in {1..10}
do
prefix="gan${i}"
maxrecs=100000
echo "Calling ${prefix},${maxrecs}"
nohup sh ./insertaccounts.sh ${prefix} ${maxrecs} > /tmp/${prefix}.out 2>&1 &

done



