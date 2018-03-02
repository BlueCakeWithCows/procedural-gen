package core;

import geometry.Point;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MailDistanceSort {
    public static void sortByMailDistance(Point origin, List<Point> points) {
        Collections.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return Integer.compare(origin.mailDistance(o1), origin.mailDistance(o2));
            }
        });
    }
}
