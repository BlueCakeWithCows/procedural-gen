import Core.Game;
import tileEngine.TETile;

/**
 * This is the main entry point for the program. This class simply parses
 * the command line inputs, and lets the Core.Game class take over
 * in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else {
            if (args.length == 1) {
                Game game = new Game();
                TETile[][] grid = game.playWithInputString(args[0]);
                //System.out.print(TETile.toString(grid));
            } else {
                Game game = new Game();
                game.playWithKeyboard();
            }
        }
    }
}
