package renderer;

public class DrawTextCommand implements DrawCommand {

    public final Fonts type;
    public final String text;
    public final double x, y;


    public DrawTextCommand(Fonts type, String text, double x, double y) {
        this.type = type;
        this.text = text;
        this.x = x;
        this.y = y;
    }
}
