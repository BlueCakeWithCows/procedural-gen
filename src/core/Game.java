package core;

import STD.STDInput;
import STD.STDRenderer;
import inputString.BlankRenderer;
import inputString.StringInput;
import renderer.Input;
import renderer.Renderer;
import renderer.View;
import tileEngine.TETile;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Game {


    private List<InputHandler> handlerList;
    private Deque<Character> inputDeque;
    private GameState gameState;
    private boolean gameOver;

    private static final int WIDTH = 50, HEIGHT = 50;
    //This is the main game instance // state managers thingy
    //core.Game currently has 3 windows // modes


    public void playWithKeyboard() {
        STDRenderer screen = new STDRenderer();
        STDInput input = new STDInput();
        play(input, screen);
    }

    public TETile[][] playWithInputString(String arg) {
        StringInput input = new StringInput(arg);
        BlankRenderer screen = new BlankRenderer();
        play(input, screen);
        return null;
    }

    public void play(Input input, Renderer renderer) {
        View view = new View(WIDTH, HEIGHT);
        inputDeque = new ArrayDeque<>();
        handlerList = new ArrayList<>();
        renderer.initialize(WIDTH, HEIGHT);
        setGameState(new StartMenu(this));
        renderer.render(gameState.getDrawBatch(view));
        while (!gameOver) {
            inputDeque.add(input.getBlockingInput());
            doNextInput();
            renderer.render(gameState.getDrawBatch(view));
        }
        gameState.close(this);
    }


    private void doNextInput() {
        while (!inputDeque.isEmpty()) {
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

    public void setGameOver(boolean bool) {
        this.gameOver = bool;
    }
}
