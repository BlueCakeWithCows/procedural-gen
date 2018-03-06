package inputString;

import renderer.Input;

import java.awt.geom.Point2D;
import java.text.StringCharacterIterator;

public class StringInput implements Input {

    private StringCharacterIterator iterator;

    public StringInput(String inputString) {
        if (!inputString.contains(":q")) {
            inputString += ":q";
        }
        iterator = new StringCharacterIterator(inputString);
    }

    @Override
    public char getInput() {
        return iterator.next();
    }

    @Override
    public char getBlockingInput() {
        return iterator.next();
    }

    @Override
    public Point2D pollMouse() {
        return new Point2D.Double(0, 0);
    }
}
