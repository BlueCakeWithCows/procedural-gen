import geometry.ImmutablePoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test2 {

    public static void main(String[] args) {
        ImmutablePoint point = ImmutablePoint.valueOf(32, 55);
        int width = 256, height = 256;
        for (int i = 0; i < 10; i++) {
            pointTest(width, height);
            arrayTest(width, height);
        }
        double[] pointCounter = new double[2];
        double[] arrayCounter = new double[2];
        double trials = 1000;
        for (int i = 0; i < trials; i++) {
            add(pointCounter, pointTest(width, height));
            add(arrayCounter, arrayTest(width, height));
        }
        System.out.println("ImmutablePoint Allocation 1: " + pointCounter[0] / trials);
        System.out.println("ImmutablePoint Allocation 2: " + pointCounter[1] / trials);
        System.out.println("Array Allocation 1: " + arrayCounter[0] / trials);
        System.out.println("Array Allocation 2: " + arrayCounter[1] / trials);
    }

    private static void add(double[] a, double[] b) {
        a[0] += b[0];
        a[1] += b[1];
    }

    public static double[] pointTest(int w, int h) {
        long start1 = System.nanoTime();
        HashMap<ImmutablePoint, String> map = new HashMap<>(w * h);
        List<Object> obs = new ArrayList<>();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Object o = ImmutablePoint.valueOf(col, row);
                obs.add(o);
            }
        }
        long end1 = System.nanoTime();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Object o = ImmutablePoint.valueOf(col, row);
                obs.add(o);
            }
        }
        long end2 = System.nanoTime();

        return new double[]{(end1 - start1) / 1000000000.0, (end2 - end1) / 1000000000.0};
    }

    public static double[] arrayTest(int w, int h) {
        String[][] string = new String[w][h];
        long start1 = System.nanoTime();
        List<Object> obs = new ArrayList<>();

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Object o = new int[]{col, row};
                obs.add(o);
            }
        }
        long end1 = System.nanoTime();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Object o = new int[]{col, row};
                obs.add(o);
            }
        }
        long end2 = System.nanoTime();
        return new double[]{(end1 - start1) / 1000000000.0, (end2 - end1) / 1000000000.0};
    }
}
