package byog.STD;

import byog.renderer.Input;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.geom.Point2D;

public class STDInput implements Input {

    private final Object lock = new Object();


    @Override
    public char getInput() {
        return StdDraw.nextKeyTyped();
    }

    @Override
    public char getBlockingInput() {
        while (!StdDraw.hasNextKeyTyped()) {
            delay(2);
        }
        return StdDraw.nextKeyTyped();
    }

    @Override
    public Point2D pollMouse() {
        Point2D point = new Point2D.Double(StdDraw.mouseX(), StdDraw.mouseY());
        return point;
    }

    private void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
