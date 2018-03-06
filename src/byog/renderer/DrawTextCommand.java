package byog.renderer;

public class DrawTextCommand implements DrawCommand {

    private final Fonts type;
    private final String text;
    private final double x;
    private final double y;


    public DrawTextCommand(Fonts type, String text, double x, double y) {
        this.type = type;
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public Fonts getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
