package core;

import renderer.Renderer;
import renderer.View;
import tileEngine.TETile;

import java.util.Deque;
import java.util.List;

public class Game {


    private List<InputHandler> handlerList;
    private Deque<Character> inputDeque;
    private GameState gameState;
    private static final int WIDTH = 32, HEIGHT = 32;
    //This is the main game instance // state managers thingy
    //core.Game currently has 3 windows // modes


    public TETile[][] playWithInputString(String arg) {
        return null;
    }

    public void playWithKeyboard() {
        View view = new View(WIDTH, HEIGHT);
        Renderer screen = new Renderer();
        screen.initialize(WIDTH, HEIGHT);
        setGameState(new StartMenu(this));
        while (gameState != null) {
            gameState.update(this, 1);
            screen.render(gameState.getDrawBatch(view));
        }
    }


    private void doNextInput() {
        if (!inputDeque.isEmpty()) {
            doInput(inputDeque.poll());
        }
    }

    private void doInput(char c) {
        for (InputHandler input : handlerList) {
            input.doInput(c);
        }
    }


    public void registerInputHandler(InputHandler handler) {
        handlerList.add(handler);
    }

    public void removeInputHandler(InputHandler handler) {
        assert handlerList.contains(handler);
        handlerList.remove(handler);
    }

    public void clearInputHandlers(InputHandler handler) {
        handlerList.clear();
    }

    public void setGameState(GameState state) {
        if (gameState != null) { this.gameState.close(this); }
        this.gameState = state;
        this.gameState.show(this);
    }

    public void close() {
        this.gameState.close(this);
    }
}
