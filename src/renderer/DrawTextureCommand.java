package renderer;

public class DrawTextureCommand implements DrawCommand {
    public final Textures texture;
    public final double x, y;
    public final double width, height;
    public final int opacity;
    public final int brightness;

    public DrawTextureCommand(Textures texture, double x, double y, double width, double height,
        int opacity, int brightness) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.opacity = opacity;
        this.brightness = brightness;
    }
}
