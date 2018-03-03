package Core;

public class SaveAndQuit implements InputHandler {
    private StringBuilder log = new StringBuilder();
    private Game game;

    public SaveAndQuit(Game game) {
        this.game = game;
    }

    private boolean colonLast = false;

    @Override
    public boolean doInput(char c) {
        if (colonLast && c == 'q') {
            log.setLength(log.length() - 1);
            SaveGame.saveData(log.toString());
            game.setGameOver(true);
            return true;
        }
        if (game.getGameState() instanceof StartMenu && c == 'l') {
            return false;
        }
        colonLast = c == ':';
        log.append(c);
        return false;
    }
}
