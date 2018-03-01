package renderer;

public class DrawTextureCommand implements DrawCommand {
    public final Textures texture;
    public final int x, y;
    public final int width, height;
    public final int opacity;
    public final int brightness;

    public DrawTextureCommand(Textures texture, int x, int y, int width, int height, int opacity,
        int brightness) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.opacity = opacity;
        this.brightness = brightness;
    }
}
