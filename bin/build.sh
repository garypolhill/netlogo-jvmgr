#!/bin/sh
cd bin
JAR=jar
if [ $(uname) = "CYGWIN_NT-10.0" ]
then
    JAR='/cygdrive/c/Java/jdk-1.8.0/bin/jar'
elif [ $(uname) = "Darwin" ]
then
    export JAVA_HOME=$(/usr/libexec/java_home -v1.8)
    JAR=/usr/bin/jar
fi

"$JAR" cvmf ../manifest.txt ../zip/math.jar \
    uk/ac/hutton/netlogo/math/MathLibrary\$MathCommand.class \
    uk/ac/hutton/netlogo/math/MathExtension.class \
    uk/ac/hutton/netlogo/math/MathLibrary.class \
    uk/ac/hutton/netlogo/math/MathLibrary\$1.class
