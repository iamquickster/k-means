#!/bin/bash

# First argument must be the absolute path to the data file containing the points to analyse

cd bin
java -classpath .:../lib/jfreechart-1.0.19/lib/* kmoyennes.app.KMoyenneController $1
