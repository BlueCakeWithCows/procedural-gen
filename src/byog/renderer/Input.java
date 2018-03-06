package byog.renderer;

import java.awt.geom.Point2D;

public interface Input {

    char getInput();

    char getBlockingInput();

    Point2D pollMouse();
}
