#!/bin/bash
set -e -v
echo "Compiling..."
./gradlew build
echo "Running..."
.gradlew run