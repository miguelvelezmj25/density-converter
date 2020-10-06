#!/usr/bin/env bash

mvn clean compile

rm -rf output
mkdir output

echo ""

SCALE="$*"
java -cp ./target/classes at.favre.tools.dconvert.Convert "$SCALE"