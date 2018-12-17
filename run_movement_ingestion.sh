#!/bin/bash

sdk use java 11.0.1-open

java -jar ./kafka-vehicle-movement-ingestion/build/libs/kafka-vehicle-movement-ingestion-1.0.0-SNAPSHOT-all.jar
