#!/bin/sh
me=COEV                                                                         
opp=ONESTEP                                                                     
games=100
T=200
for p in 0 1 10 11 100 110; do                                                  
        ./EditAndRun_vs_ONESTEP.sh ${me} ${opp} 1 1 ${games} ${T} ${p} &                       
    done 
