package battle.controllers.webpigeon;


import battle.JoeCSimpleController;
import ga.SimpleRandomHillClimberEngine;

import java.util.Arrays;

/**
 * Created by jwalto on 12/06/2015.
 */
public class StaticEvolver {
    private GameEvaluator eval;
    private SimpleRandomHillClimberEngine rch;

    public StaticEvolver(JoeCSimpleController battle) {
        this.eval = new GameEvaluator(battle, true);
        this.rch = new SimpleRandomHillClimberEngine(new double[]{2.7631328506251744, 0.746687716615824, 0.11574670823251669}, eval);
    }

    public double[] getBest() {
        return rch.run(100);
    }

    public static void main(String[] args) {
        JoeCSimpleController start = new JoeCSimpleController(false);
        start.reset();

        StaticEvolver evo = new StaticEvolver(start);
        double[] best = evo.getBest();

        System.out.println(Arrays.toString(best));
    }


}
