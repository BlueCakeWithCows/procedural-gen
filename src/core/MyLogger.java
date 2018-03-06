package Core;

import java.util.Arrays;

public class MyLogger {


    public static final boolean DEBUG = true;

    public static void log(String string) {
        if (DEBUG) {
            System.out.println(string);
        }
    }

    public static void log(long seed) {
        log(Long.toString(seed));
    }

    public static void log(Object object) {
        if (object.getClass().isArray()) {
            log(Arrays.toString((Object[]) object));
        } else {
            log(object.toString());
        }
    }

    public static void log(int[][] map) {
        for (int[] m : map) {
            System.out.println(Arrays.toString(m));
        }
    }

    public static void log(int[] ray) {
        log(Arrays.toString(ray));
    }

    public static void log(boolean[] ray) {
        log(Arrays.toString(ray));
    }

    public static void log(double[] ray) {
        log(Arrays.toString(ray));
    }

    public static void log(String[] ray) {
        log(Arrays.toString(ray));
    }

    public static void log(byte[] ray) {
        log(Arrays.toString(ray));
    }
}
