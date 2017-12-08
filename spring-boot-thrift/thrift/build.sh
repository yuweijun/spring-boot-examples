#!/usr/bin/env bash

dir=$(dirname $0)
echo ${dir}
cd ${dir}

thrift --gen java -out ../src/main/java hello.thrift
thrift --gen java -out ../src/main/java shared.thrift
thrift --gen java -out ../src/main/java tutorial.thrift
thrift --gen java -out ../src/main/java ServiceConfiguration.thrift
