#!/bin/sh

javac MathServer.java
javac MathClient.java

java MathServer &
java MathClient $1
