package STD;

import edu.princeton.cs.introcs.StdDraw;
import renderer.Input;

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

    private void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
