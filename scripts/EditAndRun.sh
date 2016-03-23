###############################################################################
### Created by Jialin Liu on 17/03/2016                                     ###
### Copy and paste the codes for essex-aihack and play with the parameters. ###
###############################################################################

#!/bin/sh
## parameters
me=${1}
opp=${2}
num_actions=${3}
action_length=${4}
num_games=${5}
max_ticks=${6}
learnt_opp=${7-RND}

SOURCE_PATH=~/aihack
MUTATION_PATH=${me}-${learnt_opp}_${num_actions}x${action_length}_vs_${opp}_${num_games}x${max_ticks}
echo ${MUTATION_PATH}

## copy/paste
mkdir -p ${MUTATION_PATH}
cd ${MUTATION_PATH}
cp -r ${SOURCE_PATH}/src ./
cp ${SOURCE_PATH}/sourceList.txt .
cp ${SOURCE_PATH}/BuildAndRun.sh .

## Modify the parameters
sed -i 's/Search.NUM_ACTIONS_INDIVIDUAL = 10;/Search.NUM_ACTIONS_INDIVIDUAL = '${num_actions}';/g' ./src/battle/BattleTest.java 
sed -i 's/MAX_TICKS_GAME = 10;/MAX_TICKS_GAME = '${max_ticks}';/g' ./src/battle/BattleTest.java 
sed -i 's/Search.MACRO_ACTION_LENGTH = 1;/Search.MACRO_ACTION_LENGTH = '${action_length}';/g' ./src/battle/BattleTest.java
sed -i 's/NUM_GAMES_TO_PLAY = 10;/NUM_GAMES_TO_PLAY = '${num_games}';/g' ./src/battle/BattleTest.java
sed -i 's/playN(BattleTest.GA, .*/playN(BattleTest.'${me}', BattleTest.'${opp}', "..\/data\/'${MUTATION_PATH}'.txt");/' ./src/battle/BattleTest.java

#if [ ]; then 
#new NullOpponentGenerator(Search.NUM_ACTIONS_INDIVIDUAL),
## Build and Run the program
./BuildAndRun.sh
cd ..
