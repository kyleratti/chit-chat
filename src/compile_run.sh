#!/bin/bash
clear
CLASSPATH=".:/home/kyle/Documents/Java/bin/*"

function cleanup
{
        rm com/chitchat/*.class com/chitchat/components/*.class com/chitchat/server/components/*.class
}

cleanup
javac -classpath ${CLASSPATH} com/chitchat/*.java com/chitchat/components/*.java com/chitchat/server/components/*.java
java -classpath ${CLASSPATH} ${1}
cleanup
