#!/bin/sh

# Doug - March 2023

# A script to compile the netlogo.math plugin. I probably should use ant to do
# this, I am unaware of how the $JAVAC treats dependencies and cannot be
# bothered to find out. I have also taken this opportunity to clear out the lib
# directory, because generally people cannot be bothered working out which
# libraries they need and just bung in the last set that work - hence the
# massive list of libraries in lib/old

# Note that I am using a named removed in the clean, because there might be
# other stuff that this shouldn't clean - I am talking to you eclipse IDE.

#JAVAC=javac
JAVAC=/usr/lib/jvm/java-8-openjdk/bin/javac
CLASS_PATH=lib/netlogo-6.3.0.jar:bin:lib/scala-library-2.12.4.jar:src/uk/ac/hutton/netlogo/math
if [ $(uname) = "CYGWIN_NT-10.0" ]
then
    JAVAC='/cygdrive/c/Java/jdk-17.0.1/bin/javac'
    CLASS_PATH="bin;lib\netlogo-6.3.0.jar;lib\scala-library.jar"
elif [ $(uname) = "Darwin" ]
then
    export JAVA_HOME=$(/usr/libexec/java_home -v1.8)
    JAVAC=/usr/bin/javac
fi

if [ -n "$1" ]
then
    "$JAVAC" -d bin -cp $CLASS_PATH "$1"
    exit
fi

bin/clean.sh 2>/dev/null

"$JAVAC" -d bin -cp $CLASS_PATH \
    src/uk/ac/hutton/netlogo/math/MathExtension.java \
    src/uk/ac/hutton/netlogo/math/MathLibrary.java
