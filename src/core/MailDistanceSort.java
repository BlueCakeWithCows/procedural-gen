package Core;

import geometry.Point;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MailDistanceSort {
    public static void sortByMailDistance(Point origin, List<Point> points) {
        Collections.sort(points, Comparator.comparingInt(origin::mailDistance));
    }
}
