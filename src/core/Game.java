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


    private static final double MAX_FPS = 60;
    private List<InputHandler> handlerList;
    private Deque<Character> inputDeque;
    private GameState gameState;
    private boolean gameOver;
    private int headSpace = 5;

    public static final int WIDTH = 20;
    public static final int HEIGHT = 15;
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
        inputDeque = new ArrayDeque<>();
        handlerList = new ArrayList<>();
        setGameState(new StartMenu(this));
        (new RenderThread(renderer)).start();
        while (!gameOver) {
            inputDeque.add(input.getBlockingInput());
            doNextInput();
            gameState.update(this, 0);
        }
        gameState.close(this);
    }

    private class RenderThread extends Thread {
        Renderer renderer;

        public RenderThread(Renderer renderer) {
            this.renderer = renderer;
        }

        @Override
        public void run() {
            renderer.initialize(WIDTH, HEIGHT + headSpace, 1);
            long lastTime = System.currentTimeMillis();
            long currentTime = System.currentTimeMillis();
            long dt = 0;
            long msPerFrame = (long) (1000.0 * 1.0 / MAX_FPS);
            View view = new View(WIDTH, HEIGHT);

            while (renderer != null) {
                //long start = System.nanoTime();
                currentTime = System.currentTimeMillis();
                dt = currentTime - lastTime;
                if (dt < msPerFrame) {
                    wait((int) (msPerFrame - dt));
                }
                if (gameState.graphicsReady()) { renderer.render(gameState.getDrawBatch(view)); }
                //System.out.println(1d / ((double) (System.nanoTime() - start) / 1000000000.0));

            }
        }

        private void wait(int delay) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    ;


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
