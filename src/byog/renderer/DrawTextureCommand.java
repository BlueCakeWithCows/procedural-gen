package byog.renderer;

public class DrawTextureCommand implements DrawCommand {
    private final Textures texture;
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final int opacity;
    private final int brightness;
    private final int z;

    public DrawTextureCommand(Textures texture, double x, double y, double width, double height,
        int opacity, int brightness, int z) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.opacity = opacity;
        this.brightness = brightness;
        this.z = z;
    }

    public int getZ() {
        return z;
    }

    public Textures getTexture() {
        return texture;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getOpacity() {
        return opacity;
    }

    public int getBrightness() {
        return brightness;
    }
}
