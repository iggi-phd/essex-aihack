package math;
import java.util.Random;

public class Util {
    public static double min(double[] x) {
        double min = x[0];
        for (int i=1; i<x.length; i++) {
            min = Math.min(min, x[i]);
        }
        return min;
    }

    /**
     * Randomly generate an integer given a range
     */
    public static int randomIntInRange(int imin, int imax) {
        Random randomGenerator = new Random();                                  
        int r = randomGenerator.nextInt(imax-imin+1);
        r += imin;
        return r;
    }

    /**
     * Randomly generate a 2-d coordinate given a range
     */
    public static void random2dCoordinate(int xmin, int xmax, int ymin, int ymax) {
        int x = randomIntInRange(xmin, xmax);
        int y = randomIntInRange(ymin, ymax);
    }
    
    //Normalizes a value between its MIN and MAX.
    public static double normalise(double a_value, double a_min, double a_max)
    {
        if(a_min < a_max)
            return (a_value - a_min)/(a_max - a_min);
        else    // if bounds are invalid, then return same value
            return a_value;
    }
    
    /**
     * Adds a small noise to the input value.
     * @param input value to be altered
     * @param epsilon relative amount the input will be altered
     * @param random random variable in range [0,1]
     * @return epsilon-random-altered input value
     */
    public static double noise(double input, double epsilon, double random)
    {
        if(input != -epsilon) {
            return (input + epsilon) * (1.0 + epsilon * (random - 0.5));
        }else {
            //System.out.format("Utils.tiebreaker(): WARNING: value equal to epsilon: %f\n",input);
            return (input + epsilon) * (1.0 + epsilon * (random - 0.5));
        }
    }
}
