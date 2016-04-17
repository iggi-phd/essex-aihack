package battle;

import asteroids.*;
import java.awt.geom.Rectangle2D;
import math.Vector2d;
import math.Util;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random; 
import java.util.Vector;
import java.awt.*;

import static asteroids.Constants.*;

/**
 * Created by simon lucas on 10/06/15.
 * <p>
 * Aim here is to have a simple battle class
 * that enables ships to fish with each other
 * <p>
 * Might start off with just two ships, each with their own types of missile.
 */

public class SimpleBattle {

    // play a time limited game with a strict missile budget for
    // each player

    int nTicks = 500;
    boolean visible = true;

    int nbObstacles = 5;
    int nMissiles = 2000;
    int missileSpeed = 10;
    int life = 10;
    private static final double MAX_SCORE = 1000000;

    ArrayList<GameObject> objects;
    ArrayList<PlayerStats> stats;
    ArrayList<Wall> obstacles;

    NeuroShip s1, s2;
    BattleController p1, p2;
    BattleView view;
    int currentTick;

    double score1, score2;
    StatSummary ss1 = new StatSummary();
    StatSummary ss2 = new StatSummary();
    double scoreRecord[];
    double score1Record[];
    double score2Record[];

    int winner = -1;

    public SimpleBattle() {
        this(true);
        scoreRecord = new double[nTicks+1];
        score1Record = new double[nTicks+1];
        score2Record = new double[nTicks+1];
    }

    public SimpleBattle(boolean visible, int nTicks) {
        this(visible);
        this.nTicks = nTicks;
        scoreRecord = new double[nTicks+1];
        score1Record = new double[nTicks+1];
        score2Record = new double[nTicks+1];
    }

    public SimpleBattle(boolean visible) {
        this.objects = new ArrayList<>();
        this.stats = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.visible = visible;
        this.score1 = 0.0;
        this.score2 = 0.0;

        if (visible) {
            view = new BattleView(this);
            new JEasyFrame(view, "battle");
            view.repaint();
        }
    }

    public int getTicks() {
        return currentTick;
    }

    public double[] playGame(BattleController p1, BattleController p2) {
        this.p1 = p1;
        this.p2 = p2;
        reset(true);

        stats.add(new PlayerStats(nMissiles, 0, life));
        stats.add(new PlayerStats(nMissiles, 0, life));

        if (p1 instanceof KeyListener) {
            view.addKeyListener((KeyListener)p1);
            view.setFocusable(true);
            view.requestFocus();
        }

        if (p2 instanceof KeyListener) {
            view.addKeyListener((KeyListener)p2);
            view.setFocusable(true);
            view.requestFocus();
        }

        waitTillReady();
        while (!isGameOver()) {
            update();
            //System.out.println("player 1: " + this.stats.get(0).life);
            //System.out.println("player 2: " + this.stats.get(1).life); 
        }

        if(this.winner!=-1) {
            System.out.println("Player " + (this.winner+1) + " wins at " + currentTick + " with life " + stats.get(this.winner).life);
            System.out.println("Player " + (2-this.winner) + " loss at " + currentTick + " with life " + stats.get(1-this.winner).life);
        } else {
            System.out.println("Player 1 draws at " + currentTick + " with life " + stats.get(0).life);
            System.out.println("Player 2 draws at " + currentTick + " with life " + stats.get(1).life);
        }
        if (p1 instanceof KeyListener) {
            view.removeKeyListener((KeyListener)p1);
        }
        if (p2 instanceof KeyListener) {
            view.removeKeyListener((KeyListener)p2);
        }

        double[] tmp = Util.combineArray(scoreRecord,score1Record);
        double[] allRecord = Util.combineArray(tmp,score2Record);
        return allRecord;
    }


    protected void waitTillReady()
    {
        if(visible)
        {
            while(!view.ready) {
                view.repaint();
                waitStep(1000);
            }
        }

        waitStep(1000);
    }

    /**
     * Waits until the next step.
     * @param duration Amount of time to wait for.
     */
    protected static void waitStep(int duration) {

        try
        {
            Thread.sleep(duration);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Randomly reset the game if randomInit==true
     */
    public void reset(boolean randomInit) {
        stats.clear();
        objects.clear();
        if(randomInit) {
            Vector<Integer> pos = randomPositionBy4Area();
            if(pos.get(0)>=pos.get(2)) {
                s1 = buildShip(pos.get(0), pos.get(1), 1, 0, 0);
                s2 = buildShip(pos.get(2), pos.get(3), -1, 0, 1);
            } else {
                s1 = buildShip(pos.get(0), pos.get(1), -1, 0, 0);
                s2 = buildShip(pos.get(2), pos.get(3), 1, 0, 1);
            }
            //for(int i=0; i<nbObstacles; i++) {
            //    addRandomObstacle();
            //}
        } else {
            s1 = buildShip(200, 250, 1, 0, 0);
            s2 = buildShip(300, 250, -1, 0, 1);
        }

        this.currentTick = 0;
        this.winner = -1;

        stats.add(new PlayerStats(nMissiles, 0, life));
        stats.add(new PlayerStats(nMissiles, 0, life));
        objects.add(s1);
        objects.add(s2);
    }

    public void addRandomObstacle() {
        obstacles.add(new Wall(Util.randomIntInRange(0,500),Util.randomIntInRange(0,500),Util.randomIntInRange(2,50),Util.randomIntInRange(2,50)));
    }

    public Vector<Integer> randomPositionBy4Area() {
        Vector<Integer> pos = new Vector<Integer>();
        Random randomGenerator = new Random();
        int area1 = randomGenerator.nextInt(4)+1;
        int area2 = randomGenerator.nextInt(4)+1;
        while(area2==area1) {
            area2 = randomGenerator.nextInt(4)+1;
        }
        pos.add(Util.randomIntInRange(0,100)+((int)(area1%2)*250)+100);
        pos.add(Util.randomIntInRange(0,100)+((int)(area1/2)*250)+100);
        pos.add(Util.randomIntInRange(0,100)+((int)(area2%2)*250)+100);
        pos.add(Util.randomIntInRange(0,100)+((int)(area2/2)*250)+100);
        return pos;
    }

    protected NeuroShip buildShip(int x, int y, int dx, int dy, int playerID) {
        Vector2d position = new Vector2d(x, y, true);
        Vector2d speed = new Vector2d(true);
        Vector2d direction = new Vector2d(dx, dy, true);

        return new NeuroShip(position, speed, direction, playerID );
    }

    public void update() {
        // update missiles
        for (GameObject ob : objects)
            if(ob instanceof BattleMissile)
                ob.update();
        checkMissiles();
        // get the actions from each player

        // apply them to each player's ship, taking actions as necessary
        Action a1 = p1.getAction(this.clone(), 0);
        Action a2 = p2.getAction(this.clone(), 1);
        update(a1, a2);

        if(a1.shoot)
            s1.addRandomForce();

        if(a2.shoot)
            s2.addRandomForce();

        ss1.add(score(0));
        ss2.add(score(1));
    }

    public void update(Action a1, Action a2) {
        // now apply them to the ships
        s1.update(a1);
        s2.update(a2);

        checkCollision(s1);
        checkCollision(s2);

        // and fire any missiles as necessary
        if (a1.shoot) fireMissile(s1.s, s1.d, 0);
        if (a2.shoot) fireMissile(s2.s, s2.d, 1);

        wrap(s1);
        wrap(s2);

        // here need to add the game objects ...
        /**
        java.util.List<GameObject> killList = new ArrayList<GameObject>();
        for (GameObject object : objects) {
            object.update();
            wrap(object);
            if (object.dead()) {
                killList.add(object);
            }
        }

        objects.removeAll(killList);
        **/
        currentTick++;
        updateScores();

        if (visible) {
            view.repaint();
            sleep();
        }

        if(scoreRecord != null)
            scoreRecord[currentTick] = score(0);
        if(score1Record != null)
            score1Record[currentTick] = score1;
        if(score2Record != null)
            score2Record[currentTick] = score2;
        //System.out.println(currentTick + " " + score(0));
        //int a = 0;
    }

    public void updateScores()
    {
        score1 = calcScore(0);
        score2 = calcScore(1);
        while(score1==0 && score2==0)
        {
            s1.addRandomForce();
            s2.addRandomForce();
            score1 = calcScore(0);
            score2 = calcScore(1);
        }
    }


    private double calcScore(int playerId)
    {
        NeuroShip ss1 = s1;
        NeuroShip ss2 = s2;

        if(playerId == 1)
        {
            ss1 = s2;
            ss2 = s1;
        }

        double dist = ss1.distTo(ss2);
        double distPoints = 1.0/(1.0+dist/100.0);
        double dot = ss1.dotTo(ss2);
        double dotDirs = ss1.dotDirections(ss2);

        //if(playerId == 0)
        //    System.out.println("player 1 currentTick: " +currentTick+"; d: " + dist + "; dp: " + distPoints + "; dot: " + dot + "; TOTAL: " + (dot*distPoints));
        //if(playerId == 1)
        //    System.out.println("player 2 currentTick: " +currentTick+"; d: " + dist + "; dp: " + distPoints + "; dot: " + dot + "; TOTAL: " + (dot*distPoints));

        /**                                                                     
        * Check if the two ships are too closed to each other (less than 5) 
        * If yes, neither of them can shoot, score=0                           
        */
        double minShootRange = 10;
        double maxShootRange = 100;
        if(dist<=minShootRange)
        {
            s1.addRandomForce();
            s2.addRandomForce();
            //return 0;
        }
        /**
         * Check if the opponent in the shooting range
         */
        
        double dotAngle= Math.sqrt(3.0)/2.0;
        if(distPoints>=1.0/(1.0+maxShootRange/100.0) && dot>=dotAngle && dotDirs>=dotAngle)
        //if(dot>=dotAngle && dotDirs>=dotAngle)
        {
            return MAX_SCORE;
        }
        
        return (stats.get(playerId).life + dot*distPoints);
        //return dot*distPoints;//*dotDirs;
    }

    public double score(int playerId)
    {
        if(playerId == 0)
            return score1 - score2;
        return score2 - score1;
    }

    public SimpleBattle clone() {
        SimpleBattle state = new SimpleBattle(false);
        state.objects = copyObjects();
        state.stats = copyStats();
        state.currentTick = currentTick;
        state.visible = false; //stop MCTS people having all the games :p

        state.s1 = s1.copy();
        state.s2 = s2.copy();
        return state;
    }

    protected ArrayList<GameObject> copyObjects() {
        ArrayList<GameObject> objectClone = new ArrayList<GameObject>();
        for (GameObject object : objects) {
            objectClone.add(object.copy());
        }

        return objectClone;
    }

    protected ArrayList<PlayerStats> copyStats() {
        ArrayList<PlayerStats> statsClone = new ArrayList<PlayerStats>();
        for (PlayerStats object : stats) {
            statsClone.add(new PlayerStats(object.nMissiles, object.nPoints, object.life));
        }

        return statsClone;
    }

    protected void checkCollision(GameObject actor) {
        // check with all other game objects
        // but use a hack to only consider interesting interactions
        // e.g. asteroids do not collide with themselves
        if (!actor.dead() &&
                (actor instanceof BattleMissile
                        || actor instanceof NeuroShip)) {
            if (actor instanceof BattleMissile) {
                System.out.println("Missile: " + actor);
            }
            /**
            for(Wall wall:obstacles) {
                Rectangle2D thisWall = wall.getBound();                               
                boolean crash = thisWall.intersects(actor.getBound());
                if(crash) {
                    actor.hit();
                    int playerID = (actor == s1 ? 0 : 1);
                    PlayerStats stats = this.stats.get(playerID);
                    stats.nPoints++; 
                    return;
                }
            }
            **/
            // the ob is a ship
            for (GameObject ob : objects) {
                if (overlap(actor, ob)) {
                    // the object is hit, and the actor is also

                    int playerID = (actor == s1 ? 0 : 1);
                    if(ob instanceof NeuroShip) {
                        this.stats.get(playerID).life=0;
                        this.stats.get(1-playerID).life=0;
                        //System.out.println(this.stats.get(1-playerID).life);
                    } else {
                        ob.hit();
                        objects.remove(ob);
                        this.stats.get(playerID).life--;
                    }
                    return;
                }
            }
        }
    }

    protected void checkMissiles() {
        for(int i=objects.size()-1; i>1; i--) {
            GameObject ob1 = objects.get(i);
            for(int j=i-1; j>1; j--) {
                GameObject ob2 = objects.get(j);
                if(overlap(ob1,ob2)) {
                    ob1.hit();
                    ob2.hit();
                }
            }
        }
        for(int i=objects.size()-1; i>1; i--) {
            GameObject ob = objects.get(i);
            if(ob.dead())
                objects.remove(i);
        }
    }

    private boolean overlap(GameObject actor, GameObject ob) {
        if (actor.equals(ob)) {
            return false;
        }

        // otherwise do the default check
        double dist = actor.s.dist(ob.s);
        boolean ret = dist < (actor.r() + ob.r());
        return ret;
    }

    public void sleep() {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void fireMissile(Vector2d s, Vector2d d, int playerId) {
        // need all the usual missile firing code here
        NeuroShip currentShip = playerId == 0 ? s1 : s2;
        PlayerStats thisStats = this.stats.get(playerId);
        if (thisStats.nMissiles > 0) {
            BattleMissile m = new BattleMissile(s, new Vector2d(0, 0, true), playerId);
            // the velocity is noisy
            double noiseStrength = 0.1;
            // TODO The following line is strange !!!
            // Vector2d releaseVelocity = new Vector2d(1+Math.random()*noiseStrength,1+Math.random()*noiseStrength);
            double releaseVelocity = missileSpeed * (1+Math.random()*noiseStrength);
            m.v.add(d, releaseVelocity);
            // make it clear the ship
            m.s.add(m.v, (currentShip.r() + missileRadius) * 1.5 / m.v.mag());
            // add missile to the object list
            objects.add(m);
            //System.out.println("Fired: " + m);
            //sounds.fire();
            this.stats.get(playerId).nMissiles--;
            //currentShip.addRandomForce(1,3);
        }
    }

    public void draw(Graphics2D g) {
        // for (Object ob : objects)
        if (s1 == null || s2 == null) {
            return;
        }

        // System.out.println("In draw(): " + n);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(bg);
        g.fillRect(0, 0, size.width, size.height);

        for (GameObject go : objects) {
            go.draw(g);
        }

        s1.draw(g);
        if (p1 instanceof RenderableBattleController) {
            RenderableBattleController rbc = (RenderableBattleController)p1;
            rbc.render(g, s1.copy());
        }

        s2.draw(g);
        if (p2 instanceof RenderableBattleController) {
            RenderableBattleController rbc = (RenderableBattleController)p2;
            rbc.render(g, s2.copy());
        }
    }

    public NeuroShip getShip(int playerID) {
        assert playerID < 2;
        assert playerID >= 0;

        if (playerID == 0) {
            return s1.copy();
        } else {
            return s2.copy();
        }
    }

    public ArrayList<GameObject> getObjects()
    {
        return new ArrayList<>(objects);
    }

    public int getPoints(int playerID) {
        assert playerID < 2;
        assert playerID >= 0;

        return stats.get(playerID).nPoints;
    }

    public int getMissilesLeft(int playerID) {
        //return 0;
        assert playerID < 2;
        assert playerID >= 0;

        return stats.get(playerID).nMissiles;
    }

    private void wrap(GameObject ob) {
        // only wrap objects which are wrappable
        if (ob.wrappable()) {
            ob.s.x = (ob.s.x + width) % width;
            ob.s.y = (ob.s.y + height) % height;
        }
    }

    public boolean isGameOver() {
        if(stats.get(1).life==0 && stats.get(0).life==0)
        {
            return true;
        }
        if(score1==MAX_SCORE || score2==-MAX_SCORE || stats.get(1).life==0)
        {
            this.winner = 0;
            //System.out.println("green win at " + currentTick+" " +score1 + " " +score2);
            return true;
        }
        if(score2==MAX_SCORE || score1==-MAX_SCORE || stats.get(0).life==0)
        {
            //System.out.println("blue win at " + currentTick+" " +score1 + " " +score2);
            this.winner = 1;
            return true;
        }
        /*if (getMissilesLeft(0) >= 0 && getMissilesLeft(1) >= 0) {
            //ensure that there are no bullets left in play
            if (objects.isEmpty()) {
                return true;
            }
        }*/
       
        /**
        PlayerStats stat = this.stats.get(0);
        assert(stat.nPoints<=this.life);
        if(stat.nPoints==this.life) {
            this.winner = 1;
            return true;
        }
        stat = this.stats.get(1);
        assert(stat.nPoints<=this.life);
        if(stat.nPoints==this.life) {                                          
            this.winner = 0;                                                    
            return true;                                                        
        } 
        **/
        return currentTick >= nTicks;
    }

    public int getGameWinner() {
        boolean end = isGameOver();
        assert((!end) && (this.winner !=-1));
        return this.winner;
    }

    public double getScore(int playerId)
    {
        if(playerId == 0)
            return score1;
        return score2;
    }

    static class PlayerStats {
        int nMissiles;
        int nPoints;
        int life;

        public PlayerStats(int _nMissiles, int _nPoints, int _life) {
            this.nMissiles = _nMissiles;
            this.nPoints = _nPoints;
            this.life = _life;
        }

        public PlayerStats() {
            this.nMissiles = 10;
            this.nPoints = 0;
            this.life = 3;
        }

        public int getMissilesFired() {
            return nMissiles;
        }

        public int getPoints() {
            return nPoints;
        }

        public String toString() {
            return nMissiles + " : " + nPoints;
        }
    }
}
