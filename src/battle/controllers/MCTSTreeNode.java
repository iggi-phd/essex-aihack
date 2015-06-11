package battle.controllers;

import asteroids.Action;
import battle.SimpleBattle;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MCTSTreeNode {
    static Random r = new Random();
    static double epsilon = 1e-6;
    SimpleBattle nodeState = null;
    int actingPlayerId = -1;

    static Action[] macroActions = new Action[]
    {
        new Action(0, 0, true),
            new Action(1,1, true),
            new Action(1,-1, true),
            new Action(0, 0, false),
            new Action(1,1, false),
            new Action(1,-1, false)
    };

    MCTSTreeNode[] children;
    double nVisits, totValue;

    public MCTSTreeNode(SimpleBattle state, int actingPlayerId)
    {
        nodeState = state;
        this.actingPlayerId = actingPlayerId;
    }

    public void selectAction()
    {
        List<MCTSTreeNode> visited = new LinkedList<MCTSTreeNode>();
        MCTSTreeNode cur = this;
        visited.add(this);
        while (!cur.isLeaf()) {
            cur = cur.select();
            visited.add(cur);
        }
        cur.expand();
        MCTSTreeNode newNode = cur.select();
        visited.add(newNode);
        double value = rollOut(newNode);

        for (MCTSTreeNode node : visited)
        {
            node.updateStats(value);
        }
    }

    public void expand()
    {
        if(nodeState.isGameOver())
        {
            //It's terminal, so no children
            children = new MCTSTreeNode[0];
        }
        else
        {
            children = new MCTSTreeNode[macroActions.length * macroActions.length];
            SimpleBattle cloneState = null;

            for (int i = 0; i < macroActions.length; i++)
            {
                for (int j = 0; j < macroActions.length; j++)
                {
                    cloneState = nodeState.clone();
                    cloneState.update(macroActions[i], macroActions[j]);
                    children[i] = new MCTSTreeNode(cloneState, actingPlayerId);
                }
            }
        }
    }

    public Action GetBestAction()
    {
        int highestVisits = 0;
        int highestVisitsIndex = 0;
        int index = 0;

        for (MCTSTreeNode c : children)
        {
            if(c.nVisits > highestVisits)
            {
                highestVisitsIndex = index;
            }

            index++;
        }

        return macroActions[index];
    }

    private MCTSTreeNode select() {
        MCTSTreeNode selected = null;
        double bestValue = Double.MIN_VALUE;

        for (MCTSTreeNode c : children)
        {
            double uctValue =
                    c.totValue / (c.nVisits + epsilon) +
                            Math.sqrt(Math.log(nVisits+1) / (c.nVisits + epsilon)) +
                            r.nextDouble() * epsilon;
            // small random number to break ties randomly in unexpanded nodes
            // System.out.println("UCT value = " + uctValue);
            if (uctValue > bestValue)
            {
                selected = c;
                bestValue = uctValue;
            }
        }

        return selected;
    }

    public boolean isLeaf() {
        return children == null;
    }

    public double rollOut(MCTSTreeNode tn)
    {
        SimpleBattle currentState = this.nodeState.clone();

        while(!currentState.isGameOver())
        {
            currentState.update(macroActions[r.nextInt(macroActions.length)], macroActions[r.nextInt(macroActions.length)]);
        }

        //Max score is 10*number of missles (1000)
        return (currentState.getPoints(actingPlayerId) - currentState.getPoints(1-actingPlayerId))/1000; //Perspective
    }

    public void updateStats(double value) {
        nVisits++;
        totValue += value;
    }

    public int arity() {
        return children == null ? 0 : children.length;
    }
}