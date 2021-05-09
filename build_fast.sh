#!/usr/bin/env bash

# Runs a clean and build excluding tests tagged as "Slow"
./gradlew -DexcludeTests=Slow