#!/bin/bash

sdk use java 11.0.1-open

java -jar ./kafka-vehicle-position-generator/build/libs/kafka-vehicle-position-generator-1.0.0-SNAPSHOT-all.jar
