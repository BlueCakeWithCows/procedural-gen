package levelBuilder;

import Core.Util;
import geometry.Point;
import geometry.Rectangle;
import tileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

public class TileRegion {
    private final TETile[][] grid;
    private int x, y, width, height;


    public TileRegion(TETile[][] grid) {
        this(grid, 0, 0, grid.length, grid[0].length);
    }

    public TileRegion(TETile[][] grid, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.grid = grid;
    }

    public TileRegion(TileRegion region, int[] offset, int[] size) {
        this(region.getGrid(), offset[0], offset[1], size[0], size[1]);
    }

    public TileRegion(TileRegion region) {
        this(region.getGrid(), region.getX(), region.getY(), region.getWidth(), region.getHeight());
    }

    public static TileRegion fillHex(TileRegion region, int[] p1, int size, TETile tile) {
        int w = size + 2;
        int h = size * 2;
        int colSize = 1;
        for (int row = 0; row < h / 2; row++) {
            for (int col = colSize; col + colSize < w; col++) {
                region.setTile(col + p1[0] + size - 2, row + p1[1], tile);
                region.setTile(col + p1[0] + size - 2, h - 1 - row + p1[1], tile);
            }
            colSize -= 1;
        }
        return region;
    }

    public void setTile(int x, int y, TETile tile) {
        if (isValid(x, y)) {
            getGrid()[x + getX()][y + getY()] = tile;
        }
    }

    public TETile getTile(int x, int y) {
        if (isValid(x, y)) {
            return getGrid()[x + getX()][y + getY()];
        }
        return null;
    }

    public boolean isValid(int x, int y) {
        if ((x < 0 || x >= getWidth() || y < 0 || y >= getHeight())) {
            return false;
        }
        x += getX();
        y += getY();
        if ((x < 0 || x >= grid.length || y < 0 || y >= grid[0].length)) {
            return false;
        }
        return true;
    }

    public TileRegion getSubRegion(int[] pos1, int[] pos2) {
        return getSubRegion(pos1[0], pos1[1], pos2[0], pos2[1]);
    }

    public TileRegion getSubRegion(int x1, int y1, int x2, int y2) {
        return getSubRegionByDimension(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
    }

    public TileRegion getSubRegionByDimension(int x1, int y1, int tWidth, int tHeight) {
        if (x1 >= getWidth()) {
            x1 = getWidth() - 1;
        }
        if (y1 >= getHeight()) {
            y1 = getHeight() - 1;
        }
        if (x1 < 0) {
            x1 = 0;
        }
        if (y1 < 0) {
            y1 = 0;
        }
        if (tWidth < 0) {
            tWidth = 0;
        }
        if (tHeight < 0) {
            tHeight = 0;
        }
        if (x1 + tWidth > getWidth()) {
            tWidth = getWidth() - x1;
        }
        if (y1 + tHeight > getHeight()) {
            tHeight = getHeight() - y1;
        }
        return new TileRegion(getGrid(), this.getX() + x1, this.getY() + y1, tWidth, tHeight);
    }

    public List<Point> getByFloodFill(Point initial, TETile tile) {
        List<Point> positions = new ArrayList<>();
        List<Point> newPositions = new ArrayList<>();
        positions.add(initial);
        newPositions.add(initial);
        while (newPositions.size() > 0) {
            List<Point> newSet = new ArrayList<>();
            for (Point pos : newPositions) {
                for (Point adj : pos.getAdjacent()) {
                    if (getTile(adj) == tile && !positions.contains(adj)) {
                        positions.add(adj);
                        newSet.add(adj);
                    }
                }
            }
            newPositions = newSet;
        }
        return positions;
    }

    public TETile getTile(Point adj) {
        return getTile(adj.getX(), adj.getY());
    }

    public List<TETile> getAdjacentNodes(int x, int y) {
        List<TETile> lst = new ArrayList<>();
        if (getTile(x + 1, y) != null) {
            lst.add(getTile(x + 1, y));
        }
        if (getTile(x - 1, y) != null) {
            lst.add(getTile(x - 1, y));
        }
        if (getTile(x, y + 1) != null) {
            lst.add(getTile(x, y + 1));
        }
        if (getTile(x, y - 1) != null) {
            lst.add(getTile(x, y - 1));
        }
        return lst;
    }

    public List<TETile> getEightAdjacent(int x, int y) {
        List<TETile> lst = new ArrayList<>();
        lst.addAll(getAdjacentNodes(x, y));
        if (getTile(x + 1, y + 1) != null) {
            lst.add(getTile(x + 1, y + 1));
        }
        if (getTile(x - 1, y - 1) != null) {
            lst.add(getTile(x - 1, y - 1));
        }
        if (getTile(x - 1, y + 1) != null) {
            lst.add(getTile(x - 1, y + 1));
        }
        if (getTile(x + 1, y - 1) != null) {
            lst.add(getTile(x + 1, y - 1));
        }
        return lst;
    }

    public TileRegion getShrunkRegion(int i) {
        return this.getSubRegion(i, i, getWidth() - i - 1, getHeight() - i - 1);
    }

    public TileRegion getGridForm() {
        TETile[][] tiles = new TETile[getGrid().length][getGrid()[0].length];
        for (int col = 0; col < getWidth(); col++) {
            for (int row = 0; row < getHeight(); row++) {
                tiles[col][row] = this.getTile(col, row);
            }
        }
        return new TileRegion(tiles);
    }

    public TileRegion fillBox(int[] p1, int[] p2, TETile tile) {
        int[] pos1 = new int[]{Math.min(p1[0], p2[0]), Math.min(p1[1], p2[1])};
        int[] pos2 = new int[]{Math.max(p1[0], p2[0]), Math.max(p1[1], p2[1])};
        for (int col = pos1[0]; col <= pos2[0]; col++) {
            for (int row = pos1[1]; row <= pos2[1]; row++) {
                this.setTile(col, row, tile);
            }
        }
        return this;
    }

    public TileRegion fill(TETile tile) {
        int[] pos1 = new int[]{0, 0};
        int[] pos2 = new int[]{getWidth(), getHeight()};
        this.fillBox(pos1, pos2, tile);
        return this;
    }

    public List<TETile> getAllTiles() {
        List<TETile> list = new ArrayList<>();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                list.add(getTile(i, j));
            }
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                builder.append(getTile(col, row));
            }
            builder.append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public TileRegion getSubRegionByDimension(int[] pos, int[] dim) {
        return this.getSubRegionByDimension(pos[0], pos[1], dim[0], dim[1]);
    }
    //TODO CREATE FILL FROM GRID

    public int[][] getLightMap() {
        int[][] ray = new int[getWidth()][getHeight()];

        List<int[]> nodes = new ArrayList<>();
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                ray[col][row] = getTile(col, row).getNaturalLightLevel();
                if (ray[col][row] > 0) {
                    nodes.add(new int[]{col, row});
                }
            }
        }
        while (!nodes.isEmpty()) {
            List<int[]> newNodes = new ArrayList<>();
            for (int[] node : nodes) {
                for (int[] pos : Util.getAdjacentPositions(node)) {
                    if (pos[0] >= 0 && pos[0] < ray.length && pos[1] >= 0
                            && pos[1] < ray[0].length) {
                        if (ray[pos[0]][pos[1]] + 1 < ray[node[0]][node[1]]
                                && 1 < ray[node[0]][node[1]]) {
                            ray[pos[0]][pos[1]] = ray[node[0]][node[1]] - 1;
                            if (!getTile(pos[0], pos[1]).getType().isOpaque()) {
                                newNodes.add(pos);
                            }
                        }
                    }
                }
            }
            nodes = newNodes;
        }
        return ray;
    }

    public TETile[][] getGrid() {
        return grid;
    }


    public void fillRect(Rectangle r, TETile tile) {
        int[] pos1 = new int[]{Math.min(r.getX(), r.getX2()), Math.min(r.getY(), r.getY2())};
        int[] pos2 = new int[]{Math.max(r.getX(), r.getX2()), Math.max(r.getY(), r.getY2())};
        for (int col = pos1[0]; col <= pos2[0]; col++) {
            for (int row = pos1[1]; row <= pos2[1]; row++) {
                this.setTile(col, row, tile);
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
