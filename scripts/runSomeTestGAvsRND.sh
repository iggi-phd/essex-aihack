#!/bin/sh

opp=GA
me=ONESTEP
games=100
./EditAndRun.sh ${me} ${opp} 1 5 ${games} 100 &
./EditAndRun.sh ${me} ${opp} 1 10 ${games} 100 &
./EditAndRun.sh ${me} ${opp} 1 25 ${games} 100 &                                     
./EditAndRun.sh ${me} ${opp} 1 50 ${games} 100 &  
./EditAndRun.sh ${me} ${opp} 2 5 ${games} 100 & 
./EditAndRun.sh ${me} ${opp} 5 2 ${games} 100 &
./EditAndRun.sh ${me} ${opp} 10 1 ${games} 100 &

./EditAndRun.sh ${opp} ${me} 1 5 ${games} 100 &                                 
./EditAndRun.sh ${opp} ${me} 1 10 ${games} 100 &                                
./EditAndRun.sh ${opp} ${me} 1 25 ${games} 100 &                                
./EditAndRun.sh ${opp} ${me} 1 50 ${games} 100 &                                
./EditAndRun.sh ${opp} ${me} 2 5 ${games} 100 &                                 
./EditAndRun.sh ${opp} ${me} 5 2 ${games} 100 &                                 
./EditAndRun.sh ${opp} ${me} 10 1 ${games} 100 & 
