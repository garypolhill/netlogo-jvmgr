#!/bin/sh
if [ $(uname) = "CYGWIN_NT-10.0" ]
then
    cd /cygdrive/c/Program\ Files/NetLogo\ 6.3.0/
    ./NetLogo "\\users\\ds42723\\git\\netlogo-jvr\\test\\mgr-example.nlogo"
elif [ $(uname) = "Darwin" ]
then
    ~/Applications/netlogo/NetLogo\ 6.3.0/netlogo-gui.sh $(pwd)/netlogo/mgr-example.nlogo
else
    ~/.netlogo/NetLogo\ 6.3.0/NetLogo $(pwd)/netlogo/mgr-example.nlogo
fi

