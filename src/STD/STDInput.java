package STD;

import edu.princeton.cs.introcs.StdDraw;
import renderer.Input;

public class STDInput implements Input {

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
            wait(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
