package core;

import renderer.DrawBatchCommand;
import renderer.View;

public interface GameState {
    String getLabel();

    void update(Game game, double dt);

    void close(Game game);

    void show(Game game);

    DrawBatchCommand getDrawBatch(View view);
}
