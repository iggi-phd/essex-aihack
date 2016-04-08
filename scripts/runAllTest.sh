#!/bin/sh
games=100
T=1000

###
me=GA
opp=ONESTEP
for p in 0 1 10 11 100 110; do                                                  
    ./EditAndRunGA_vs_ONESTEP.sh ${me} ${opp} 1 10 ${games} ${T} ${p} & 
    ./EditAndRunGA_vs_ONESTEP.sh ${me} ${opp} 5 2 ${games} ${T} ${p} &
    ./EditAndRunGA_vs_ONESTEP.sh ${me} ${opp} 2 5 ${games} ${T} ${p} &
    ./EditAndRunGA_vs_ONESTEP.sh ${me} ${opp} 10 1 ${games} ${T} ${p}
done 
#
p=1
for opp in COEV OLMCTS; do 
    ./EditAndRunGA_vs_.sh ${me} ${opp} 1 10 ${games} ${T} ${p} &
    ./EditAndRunGA_vs_.sh ${me} ${opp} 5 2 ${games} ${T} ${p} &
    ./EditAndRunGA_vs_.sh ${me} ${opp} 2 5 ${games} ${T} ${p} &
    ./EditAndRunGA_vs_.sh ${me} ${opp} 10 1 ${games} ${T} ${p} 
done

###
me=OLMCTS
opp=ONESTEP
for p in 0 1 10 11 100 110; do
    ./EditAndRun_vs_ONESTEP.sh ${me} ${opp} 1 1 ${games} ${T} ${p} &
done
#
opp=COEV
./EditAndRun.sh ${me} ${opp} 1 1 ${games} ${T} ${p}


###
me=COEV
opp=ONESTEP                                                               
for p in 0 1 10 11 100 110; do                                                  
    ./EditAndRun_vs_ONESTEP.sh ${me} ${opp} 1 1 ${games} ${T} ${p} &                            
done 
