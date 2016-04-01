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
    
    public static double[] combineArray(double[] arr1, double[] arr2) {
        double[] res = new double[arr1.length+arr2.length];
        for(int i=0; i<arr1.length; ++i)
            res[i] = arr1[i];
        for(int i=0; i<arr2.length; ++i)
            res[i+arr1.length] = arr2[i];
        return res;
    }
}
