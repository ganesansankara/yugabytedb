#!/bin/sh
set -x

prefix=$1
maxrecs=$2

echo ${prefix}, ${maxrecs}
python3 insertaccounts.py ${prefix} ${maxrecs}
