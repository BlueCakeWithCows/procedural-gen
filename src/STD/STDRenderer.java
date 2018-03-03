package STD;

import core.MyLogger;
import edu.princeton.cs.introcs.StdDraw;
import renderer.DrawBatchCommand;
import renderer.DrawCommand;
import renderer.DrawTextCommand;
import renderer.DrawTextureCommand;
import renderer.Fonts;
import renderer.Renderer;
import renderer.Textures;
import tileEngine.TETile;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;

import static renderer.Fonts.CHAR;

public class STDRenderer implements Renderer {

    private HashMap<Fonts, Font> fontMap;
    private HashMap<Textures, String> texturesMap;
    private int width;
    private int height;
    private static final int TILE_SIZE = 16;


    public STDRenderer() {

    }

    @Override
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
        fontMap.put(CHAR, new Font("monaco", Font.BOLD, 14));
        texturesMap = new HashMap<>();
        for (Textures tex : texturesMap.keySet()) {
            try {
                StdDraw.picture(0, 0, texturesMap.get(tex));
            } catch (Exception e) {
                MyLogger.log("StdDraw failed to load " + tex + ": " + texturesMap.get(tex));
                texturesMap.remove(tex);
            }
        }
    }

    public void render(DrawBatchCommand commandPack) {
        StdDraw.clear(new Color(0, 0, 0));
        List<DrawCommand> commands = commandPack.unpack();

        for (DrawCommand drawCommand : commands) {
            if (drawCommand instanceof DrawTextCommand) {
                DrawTextCommand cmd = (DrawTextCommand) drawCommand;
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(getFont(cmd.type));
                StdDraw.text(cmd.x, cmd.y, cmd.text);
            }
            StdDraw.setFont(fontMap.get(CHAR));
            if (drawCommand instanceof DrawTextureCommand) {
                DrawTextureCommand cmd = (DrawTextureCommand) drawCommand;
                if (texturesMap.containsKey(cmd.texture)) {
                    StdDraw.picture(cmd.x + cmd.width / 2, cmd.y + cmd.height / 2,
                        texturesMap.get(cmd.texture), 1, 1
                    );
                } else {
                    StdDraw.setPenColor(Color.RED);
                    StdDraw.text(cmd.x + cmd.width / 2, cmd.y + cmd.height / 2,
                        String.valueOf(cmd.texture.getChar())
                    );
                }
            }
        }
        StdDraw.show();
    }

    private Font getFont(Fonts type) {
        Font font = fontMap.getOrDefault(type, null);
        if (font == null) {
            throw new RuntimeException("Font " + type + " not defined for STDRenderer");
        }
        return font;
    }

    public static String toString(TETile[][] world) {
        int width = world.length;
        int height = world[0].length;
        StringBuilder sb = new StringBuilder();

        for (int y = height - 1; y >= 0; y -= 1) {
            for (int x = 0; x < width; x += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException(
                        "Tile at position x=" + x + ", y=" + y + "" + " is null.");
                }
                //sb.append(world[x][y].getCharacter());
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
