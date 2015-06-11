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

    public MCTSTreeNode(SimpleBattle state)
    {
        nodeState = state;
    }

    public void selectAction() {
        List<MCTSTreeNode> visited = new LinkedList<MCTSTreeNode>();
        MCTSTreeNode cur = this;
        visited.add(this);
        while (!cur.isLeaf()) {
            cur = cur.select();
            // System.out.println("Adding: " + cur);
            visited.add(cur);
        }
        cur.expand();
        MCTSTreeNode newNode = cur.select();
        visited.add(newNode);
        double value = rollOut(newNode);
        for (MCTSTreeNode node : visited) {
            // would need extra logic for n-player game
            // System.out.println(node);
            node.updateStats(value);
        }
    }

    public void expand()
    {
        children = new MCTSTreeNode[macroActions.length * macroActions.length];
        SimpleBattle cloneState = null;

        for (int i=0; i<macroActions.length; i++)
        {
            for (int j=0; j<macroActions.length; j++)
            {
                cloneState = nodeState.clone();
                cloneState.update(macroActions[i], macroActions[j]);
                children[i] = new MCTSTreeNode(cloneState);
            }
        }
    }

    private MCTSTreeNode select() {
        MCTSTreeNode selected = null;
        double bestValue = Double.MIN_VALUE;
        for (MCTSTreeNode c : children) {
            double uctValue =
                    c.totValue / (c.nVisits + epsilon) +
                            Math.sqrt(Math.log(nVisits+1) / (c.nVisits + epsilon)) +
                            r.nextDouble() * epsilon;
            // small random number to break ties randomly in unexpanded nodes
            // System.out.println("UCT value = " + uctValue);
            if (uctValue > bestValue) {
                selected = c;
                bestValue = uctValue;
            }
        }
        // System.out.println("Returning: " + selected);
        return selected;
    }

    public boolean isLeaf() {
        return children == null;
    }

    public double rollOut(MCTSTreeNode tn)
    {
        nodeState.
    }

    public void updateStats(double value) {
        nVisits++;
        totValue += value;
    }

    public int arity() {
        return children == null ? 0 : children.length;
    }
}