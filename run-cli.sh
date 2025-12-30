#!/bin/bash
cd cli
mvn exec:java -Dexec.mainClass="com.fuel.tracking.cli.CliApplication" -Dexec.args="$*"