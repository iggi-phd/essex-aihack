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
p=${7}
learnt_opp=RND

SOURCE_PATH=~/aihack
#MUTATION_PATH=${me}-${learnt_opp}_${num_actions}x${action_length}_vs_${opp}_${num_games}x${max_ticks}
#MUTATION_PATH=${me}-${learnt_opp}_${num_actions}x${action_length}_vs_${opp}_policy${p}_${num_games}x${max_ticks}
MUTATION_PATH=${me}_vs_${opp}_policy${p}_${num_games}x${max_ticks}
#MUTATION_PATH=${me}_vs_${opp}_${num_games}x${max_ticks} 
echo ${MUTATION_PATH}

## copy/paste
mkdir -p ${MUTATION_PATH}
cd ${MUTATION_PATH}
cp -r ${SOURCE_PATH}/src ./
cp ${SOURCE_PATH}/sourceList.txt .
cp ${SOURCE_PATH}/scripts/BuildAndRun.sh .

## Modify the parameters
sed -i 's/Search.NUM_ACTIONS_INDIVIDUAL = .*/Search.NUM_ACTIONS_INDIVIDUAL = '${num_actions}';/' ./src/battle/BattleTest.java 
sed -i 's/MAX_TICKS_GAME = .*/MAX_TICKS_GAME = '${max_ticks}';/' ./src/battle/BattleTest.java 
sed -i 's/Search.MACRO_ACTION_LENGTH = .*/Search.MACRO_ACTION_LENGTH = '${action_length}';/' ./src/battle/BattleTest.java
sed -i 's/NUM_GAMES_TO_PLAY = .*/NUM_GAMES_TO_PLAY = '${num_games}';/' ./src/battle/BattleTest.java
sed -i 's/playN(BattleTest.GA, .*/playN(BattleTest.'${me}', BattleTest.'${opp}', "..\/data\/'${MUTATION_PATH}'.txt");/' ./src/battle/BattleTest.java

sed -i 's/RECOMMEND_POLICY = 0;/RECOMMEND_POLICY = '${p}';/g' ./src/battle/controllers/diego/search/OneStepLookAhead.java

## Build and Run the program
./BuildAndRun.sh  > ../wins/${MUTATION_PATH}.wins
cd ..

