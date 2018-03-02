package renderer;

public interface Renderer {

    void initialize(int w, int h);

    void render(DrawBatchCommand commandPack);

}
