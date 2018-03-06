package byog.renderer;

public interface Renderer {

    void initialize(int w, int h, double scale);

    void render(DrawBatchCommand commandPack);

}
