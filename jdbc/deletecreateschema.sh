#!/bin/sh
set -x


mvn exec:java -Dexec.mainClass="com.ganesan.ExasolJdbc" -Dexec.args="createschema"

