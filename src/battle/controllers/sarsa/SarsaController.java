package battle.controllers.sarsa;

import asteroids.Action;
import battle.BattleController;
import battle.NeuroShip;
import battle.SimpleBattle;
import math.Vector2d;

import java.util.*;

public class SarsaController implements BattleController {
    Random random;

    // General agent code
    NeuroShip ship;
    Action action;
    int playerId;

    // SARSA variables
    List<Hashtable<SA, Double>> qtable;
    double epsilon = 0.9;
    double learningRate = 0.1;
    double discountFactor = 0.9;

    static int _episodeLength = 1000;
    static int _maxEpisodes   = 100;
    static Action[] actions = {
            new Action(1, -1, false),
            new Action(0,  0, false),
            new Action(1,  0, false),
            new Action(1,  1, false),
            new Action(1, -1, true),
            new Action(0,  0, true),
            new Action(1,  0, true),
            new Action(1,  1, true)
    };

    // Constructor
    public SarsaController() {
        random = new Random();
        action = new Action();
        qtable = new ArrayList<>(2);
        qtable.add(new Hashtable<>());
        qtable.add(new Hashtable<>());
    }

    // Required overrides
    @Override
    public Action getAction(SimpleBattle gameStateCopy, int playerId) {
        if (ship == null)
            ship = gameStateCopy.getShip(playerId);
        performLearning(gameStateCopy);
        return policyFunction(new StateDescriptors(gameStateCopy, 0), 0);
    }

    // SARSA functions
    private void performLearningEpisode(SimpleBattle gameStateCopy) {
        int episodeStep = 0;
        while (episodeStep++ < _episodeLength) {
            int points0pre = gameStateCopy.getPoints(0);
            int points1pre = gameStateCopy.getPoints(1);

            StateDescriptors stateDescriptors0 = new StateDescriptors(gameStateCopy, 0);
            StateDescriptors stateDescriptors1 = new StateDescriptors(gameStateCopy, 1);

            Action action0 = policyFunction(stateDescriptors0, 0);
            Action action1 = policyFunction(stateDescriptors1, 1);
            gameStateCopy.update(action0, action1);

            StateDescriptors newStateDescriptor0 = new StateDescriptors(gameStateCopy, 0);
            StateDescriptors newStateDescriptor1 = new StateDescriptors(gameStateCopy, 1);

            int points0post = gameStateCopy.getPoints(0);
            int points1post = gameStateCopy.getPoints(1);

            Double points0next = qtable.get(0).get(new SA(newStateDescriptor0, policyFunction(newStateDescriptor0, 0)));
            Double points1next = qtable.get(1).get(new SA(newStateDescriptor1, policyFunction(newStateDescriptor1, 1)));
            if (points0next == null)
                points0next = 0.0;
            if (points1next == null)
                points1next = 0.0;

            performUpdate(0, new SA(stateDescriptors0, action0), (double)(points0post - points0pre), points0next);
            performUpdate(1, new SA(stateDescriptors1, action1), (double)(points1post - points1pre), points1next);
        }
    }

    private void performLearning(SimpleBattle simpleBattle) {
        for (int i=0 ; i<_maxEpisodes ; i++) {
            performLearningEpisode(simpleBattle.clone());
        }
    }

    private void performUpdate(int player,
                               SA sa,
                               Double reward,
                               Double nextValue) {
        if (!qtable.get(player).containsKey(sa))
            qtable.get(player).put(sa, 0.0);
        Double oldValue = qtable.get(player).get(sa);
        Double newValue = oldValue + learningRate * (reward + discountFactor * nextValue - oldValue);
        qtable.get(player).put(sa, newValue);
    }

    private Action policyFunction(StateDescriptors stateDescriptor,
                                  int player) {
        if (random.nextDouble() > epsilon)
            return actions[random.nextInt(actions.length)];
        double[] values = new double[actions.length];
        double maxValue = 0;
        int maxIndex = 0;
        for (int i=0 ; i<actions.length ; i++) {
            SA sa = new SA(stateDescriptor, actions[i]);
            if (qtable.get(player).containsKey(sa))
                values[i] = qtable.get(player).get(sa);
            else
                values[i] = random.nextDouble() * 1E-7;
            if (values[i] > maxValue) {
                maxValue = values[i];
                maxIndex = i;
            }
        }
        return actions[maxIndex];
    }

    class StateDescriptors {
        double locationDeltaMag;
        double locationDeltaAngle;

        int locationDeltaMagCat;   // 0:10
        int locationDeltaAngleCat; // 0:10
        int shipRotationCat;       // 0:10
        int otherShipRotationCat;  // 0:10

        public StateDescriptors(SimpleBattle gameState, int playerId) {
            ship = gameState.getShip(playerId);
            NeuroShip otherShip = gameState.getShip((playerId + 1) % 2);
            Vector2d locationDelta = ship.s.subtract(otherShip.s);

            locationDeltaMag    = locationDelta.mag();
            locationDeltaMag = Math.max(locationDeltaMag, 1.0);
            locationDeltaMagCat = Math.max(100 / (int)locationDeltaMag, 10);

            locationDeltaAngle    = (locationDelta.x > 0 ? 1 : -1)
                                    * Math.atan(locationDelta.x / locationDelta.y);
            locationDeltaAngleCat = angleToCat(locationDeltaAngle);

            shipRotationCat = angleToCat(ship.r());
            otherShipRotationCat = angleToCat(otherShip.r());
        }

        private int angleToCat(double angle) {
            if (angle < 0)
                angle += Math.PI;
            return Math.max(10, (int) (angle * 5 / Math.PI));
        }

        public int getValue() {
            return locationDeltaMagCat
                   + 10*locationDeltaAngleCat
                   + 100*shipRotationCat
                   + 1000*otherShipRotationCat;
        }
    }

    class SA {
        public StateDescriptors s;
        public Action a;

        public SA(StateDescriptors state, Action action) {
            s = state;
            a = action;
        }
    }
}
