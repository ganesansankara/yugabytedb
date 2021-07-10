#!/bin/sh
set -x

prefix=$1
maxrecs=$2

echo ${prefix}, ${maxrecs}

mvn  exec:java -Dexec.mainClass="com.ganesan.ExasolJdbc" -Dexec.args="${prefix} ${maxrecs}"

