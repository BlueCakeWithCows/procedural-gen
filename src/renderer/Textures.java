package renderer;

public enum Textures {
    BASIC_WALL('#'), FLOOR('.'), NOTHING(' '), PLAYER('❀');

    Textures(char c) {
        this.aChar = c;
    }

    private char aChar;

    public char getChar() {
        return aChar;
    }
}
