#!/bin/bash

docker exec broker kafka-topics --zookeeper zookeeper:2181 --create --replication-factor 1 --partitions 1 --topic vehicle-new
docker exec broker kafka-topics --zookeeper zookeeper:2181 --create --replication-factor 1 --partitions 1 --topic vehicle-position-update
docker exec broker kafka-topics --zookeeper zookeeper:2181 --create --replication-factor 1 --partitions 1 --topic vehicle-snapshot
docker exec broker kafka-topics --zookeeper zookeeper:2181 --create --replication-factor 1 --partitions 1 --topic vehicle-distance