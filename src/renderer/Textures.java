package renderer;

public enum Textures {
    BASIC_WALL('#'), FLOOR('.'), NOTHING(' '), PLAYER('❀'), BASIC_WALL2('#');

    Textures(char c) {
        this.aChar = c;
    }

    private char aChar;

    public char getChar() {
        return aChar;
    }
}
