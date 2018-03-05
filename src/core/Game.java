package Core;
import STD.STDInput;
import STD.STDRenderer;
import inputString.BlankRenderer;
import inputString.StringInput;
import renderer.Input;
import renderer.Renderer;
import renderer.View;
import tileEngine.TETile;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Game {

    public static final boolean RENDER_MAP = false;
    public static double SCALE = 1d;

    public static final int TOTAL_WIDTH = 25;
    public static final int TOTAL_HEIGHT = 30;
    public static final int WIDTH = 25;
    public static final int HEIGHT = 25;
    private static final double MAX_FPS = 60;
    private List<InputHandler> handlerList;
    private Deque<Character> inputDeque;
    private GameState gameState;
    private volatile boolean gameOver;
    private Input input;
    //This is the main game instance // state managers thingy
    //Core.Game currently has 3 windows // modes

    public void playWithKeyboard() {
        STDRenderer screen = new STDRenderer();
        this.input = new STDInput();
        play(input, screen);
        System.exit(0);
    }

    public TETile[][] playWithInputString(String arg) {
        this.input = new StringInput(arg);
        BlankRenderer screen = new BlankRenderer();
        play(input, screen);
        return null;
    }

    public void play(Input inputInstance, Renderer renderer) {
        inputDeque = new ArrayDeque<>();
        handlerList = new LinkedList<>();
        this.registerInputHandler(new SaveAndQuit(this));
        setGameState(new StartMenu(this));
        (new RenderThread(renderer)).start();
        while (!gameOver) {
            inputDeque.add(inputInstance.getBlockingInput());
            doNextInput();
        }
        gameState.close(this);
    }

    public Input getInput() {
        return input;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) {
        if (gameState != null) { this.gameState.close(this); }
        this.gameState = state;
        this.gameState.show(this);
    }

    public void addToQueue(String save) {
        for (char c : save.toCharArray()) {
            inputDeque.add(c);
        }
    }

    ;

    private void doNextInput() {
        while (!inputDeque.isEmpty()) {
            doInput(inputDeque.poll());
            gameState.update(this, 1);
        }
    }

    private void doInput(char c) {
        for (InputHandler input : handlerList) {
            if (input.doInput(c)) { break; }
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

    public void setGameOver(boolean bool) {
        this.gameOver = bool;
    }

    private class RenderThread extends Thread {
        Renderer renderer;

        public RenderThread(Renderer renderer) {
            this.renderer = renderer;
        }

        @Override
        public void run() {
            renderer.initialize(TOTAL_WIDTH, TOTAL_HEIGHT, SCALE);
            long lastTime = System.currentTimeMillis();
            long currentTime = System.currentTimeMillis();
            long dt = 0;
            long msPerFrame = (long) (1000.0 * 1.0 / MAX_FPS);
            View view = new View(WIDTH, HEIGHT);

            while (true && !gameOver) {
                //long start = System.nanoTime();
                currentTime = System.currentTimeMillis();
                dt = currentTime - lastTime;
//                if (dt < msPerFrame) {
//                    wait((int) (msPerFrame - dt));
//                }
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
}
