package renderer;

public enum Textures {
    BASIC_WALL('#'), FLOOR('.'), NOTHING(' '), PLAYER('❀'), BASIC_WALL2('#'), TORCH('t'), PORTAL('O');

    private char aChar;

    Textures(char c) {
        this.aChar = c;
    }

    public char getChar() {
        return aChar;
    }
}
