#!/bin/sh

me=COEV
opp=OLMCTS
games=100
#for p in 0 1 10 11 100 110; do
./EditAndRun.sh ${me} ${opp} 1 5 ${games} 100 ${p} &
./EditAndRun.sh ${me} ${opp} 1 10 ${games} 100 ${p} &
./EditAndRun.sh ${me} ${opp} 1 25 ${games} 100 ${p} &                                     
./EditAndRun.sh ${me} ${opp} 1 50 ${games} 100 ${p} &  
./EditAndRun.sh ${me} ${opp} 2 5 ${games} 100 ${p} & 
./EditAndRun.sh ${me} ${opp} 5 2 ${games} 100 ${p} &
./EditAndRun.sh ${me} ${opp} 10 1 ${games} 100 ${p}
#done
