package renderer;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Renderer {

    private HashMap<Fonts, Font> fontMap;
    private int width;
    private int height;
    private static final int TILE_SIZE = 16;


    public Renderer() {

    }

    public void initialize(int w, int h) {
        this.width = w;
        this.height = h;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
        loadGraphics();
    }

    private void loadGraphics() {
        fontMap = new HashMap<>();
        fontMap.put(Fonts.ARIAL, new Font("monaco", Font.BOLD, 30));
    }

    public void render(DrawBatchCommand commandPack) {
        StdDraw.clear(new Color(0, 0, 0));
        List<DrawCommand> commands = unpack(commandPack);
        for (DrawCommand cmd : commands) {
            if (cmd instanceof DrawTextCommand) {
                DrawTextCommand command = (DrawTextCommand) cmd;
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(getFont(command.type));
                StdDraw.text(command.x, command.y, command.text);
            }
        }
        StdDraw.show();
    }

    private Font getFont(Fonts type) {
        Font font = fontMap.getOrDefault(type, null);
        if (font == null) {
            throw new RuntimeException("Font " + type + " not defined for Renderer");
        }
        return font;
    }

    private List<DrawCommand> unpack(DrawBatchCommand commandPack) {
        List<DrawCommand> commands = new ArrayList<>();
        for (DrawCommand cmd : commandPack.commands) {
            if (cmd instanceof DrawBatchCommand) {
                commands.addAll(unpack((DrawBatchCommand) cmd));
            } else {
                commands.add(cmd);
            }
        }
        return commands;
    }
}
