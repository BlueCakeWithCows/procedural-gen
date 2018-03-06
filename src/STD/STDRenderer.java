package STD;

import Core.MyLogger;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static renderer.Fonts.CHAR;

public class STDRenderer implements Renderer {

    private static final int TILE_SIZE = 16;
    private static final Color[] DARKNESS = new Color[]{
        new Color(0, 0, 0, 255), new Color(0, 0, 0, 200), new Color(0, 0, 0, 180),
        new Color(0, 0, 0, 160), new Color(0, 0, 0, 140), new Color(0, 0, 0, 120),
        new Color(0, 0, 0, 100), new Color(0, 0, 0, 80), new Color(0, 0, 0, 60),
        new Color(0, 0, 0, 40), new Color(0, 0, 0, 20), new Color(0, 0, 0, 0)
    };
    private HashMap<Fonts, Font> fontMap;
    private HashMap<Textures, String> texturesMap;
    private int width;
    private int height;
    private TextureSorter sorter;

    public STDRenderer() {

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

    @Override
    public void initialize(int w, int h, double scale) {
        this.width = w;
        this.height = h;
        StdDraw
            .setCanvasSize((int) (width * TILE_SIZE * scale), (int) (height * TILE_SIZE * scale));
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
        loadGraphics();
        this.sorter = new TextureSorter();
    }

    private void loadGraphics() {
        fontMap = new HashMap<>();
        fontMap.put(Fonts.MONACO, new Font("monaco", Font.BOLD, 30));
        fontMap.put(CHAR, new Font("monaco", Font.BOLD, 14));
        texturesMap = new HashMap<>();
        texturesMap.put(Textures.BASIC_WALL, "res/basic_wall.png");
        texturesMap.put(Textures.BASIC_WALL2, "res/basic_wall_front.png");


        for (Textures tex : texturesMap.keySet()) {
            try {
                StdDraw.picture(0, 0, texturesMap.get(tex));
            } catch (IllegalArgumentException exception) {
                MyLogger.log("StdDraw failed to load " + tex + ": " + texturesMap.get(tex));
                texturesMap.remove(tex);
            }
        }
    }

    public void render(DrawBatchCommand commandPack) {
        StdDraw.clear(new Color(0, 0, 0));
        List<DrawCommand> commands = commandPack.unpack();

        Map<Class, List<DrawCommand>> collections =
            commands.stream().collect(groupingBy(x -> x.getClass()));


        List<DrawTextureCommand> textureCommands = (List<DrawTextureCommand>) (List<?>) collections
                                                                                            .getOrDefault(
                                                                                                DrawTextureCommand.class,
                                                                                                new ArrayList<>());
        Collections.sort(textureCommands, sorter);
        StdDraw.setFont(fontMap.get(CHAR));
        for (DrawTextureCommand cmd : textureCommands) {
            if (texturesMap.containsKey(cmd.texture)) {
                StdDraw.picture(cmd.x + cmd.width / 2, cmd.y + cmd.height / 2,
                    texturesMap.get(cmd.texture), 1, 1);
            } else {
                StdDraw.setPenColor(Color.RED);
                StdDraw.text(cmd.x + cmd.width / 2, cmd.y + cmd.height / 2,
                    String.valueOf(cmd.texture.getChar()));
            }
            StdDraw.setPenColor(DARKNESS[cmd.brightness]);
            StdDraw.filledSquare(cmd.x + cmd.width / 2, cmd.y + cmd.height / 2, 0.5);
        }

        for (DrawTextCommand cmd : (List<DrawTextCommand>) (List<?>) collections.getOrDefault(
            DrawTextCommand.class, new ArrayList<>())) {
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setFont(getFont(cmd.type));
            StdDraw.text(cmd.x, cmd.y, cmd.text);

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

    private class TextureSorter implements Comparator<DrawTextureCommand> {

        @Override
        public int compare(DrawTextureCommand o1, DrawTextureCommand o2) {
            if (o1.getZ() == o2.getZ()) {
                return o1.texture.compareTo(o2.texture);
            }
            return Integer.compare(o1.getZ(), o2.getZ());
        }
    }
}
