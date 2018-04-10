package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.awt.*;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        // add top half
        bottomHalf(world, p, s, t);
        Position p1 = new Position(p.x - s + 1, p.y + s);
        Position p2 = new Position(p.x + 2 * s - 2, p.y + s);
        topHalf(world, p1, p2, s, t);

    }

    public static void initialRow(TETile[][] world, Position p, int s, TETile t) {
        for (int i = p.x; i < p.x + s; i++) {
            world[i][p.y] = t;
        }
    }

    // build bottom half: initial row, addRow and then add mid row
    public static void bottomHalf(TETile[][] world, Position p, int s, TETile t) {
        // P1 and P2 are the left and rightmost positions of tiles from the most recently added row
        initialRow(world, p, s, t);
        Position p2 = new Position(p.x + s - 1, p.y);
        Position p1 = p;
        // add each row between the bottom and the middle rows
        for (int i = 1; i < s - 1; i++) {
            addRow(p1, p2, world, t, 1);
            p2 = new Position(p2.x + 1, p2.y + 1);
            p1 = new Position(p1.x - 1, p1.y + 1);
        }
        // add middle row
        Position m1 = new Position(p1.x - 1, p1.y + 1);
        Position m2 = new Position(p2.x + 1, p2.y + 1);
        addRow(m1, m2, world, t, 0);
    }

    public static void topHalf(TETile[][] world, Position p1, Position p2, int s, TETile t) {
        // P1 and P2 are the left and rightmost positions of tiles from the most recently added row
        addRow(p1, p2, world, t, 0);
        for (int i = 1; i < s; i++) {
            p1 = new Position(p1.x + 1, p1.y + 1);
            p2 = new Position(p2.x - 1, p2.y + 1);
            addRow(p1, p2, world, t, -1);
        }
    }

    // build upper half; mid row, addRow
    public static void addRow(Position p1, Position p2, TETile[][] world, TETile t, int d) {
        // P1 is the position of the leftmost tile on the last row and P2 is the position of the rightmost tile
        // if D is positive, the rows are increasing. If D is negative the rows are decreasing
        if (d > 0) {
            for (int i = p1.x - 1; i < p2.x + 2; i++) {
                world[i][p1.y + 1] = t;
            }
        } else if (d < 0) {
            for (int i = p1.x; i < p2.x + 1; i++) {
                world[i][p1.y] = t;
            }
        } else {
            for (int i = p1.x; i < p2.x + 1; i++) {
                world[i][p1.y] = t;
            }
        }
    }

    public static void multiHex(TETile[][] world, Position p, int s, int num, TETile t) {
        // P is the position of the bottom leftmost tile in the bottom hexagon
        addHexagon(world, p, s, t);

    }

    public static void tesselationBody() {

    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.getNOTHING();
            }
        }

        // fills in a block 14 tiles wide by 4 tiles tall
        for (int x = 20; x < 35; x += 1) {
            for (int y = 5; y < 10; y += 1) {
                world[x][y] = Tileset.getWALL();
            }
        }


        TETile t = new TETile('#', Color.black, Color.yellow, "test");
        Position p = new Position(20, 10);
        addHexagon(world, p, 5, t);
        // draws the world to the screen
        ter.renderFrame(world);
    }

    public static class Position {
        // X and Y are the x and y coordinates of the bottom left corner of a hexagon
        private int x;
        private int y;

        public Position(int xx, int yy) {
            x = xx;
            y = yy;
        }
    }

}
