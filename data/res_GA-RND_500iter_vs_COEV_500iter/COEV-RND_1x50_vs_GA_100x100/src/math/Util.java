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
}
