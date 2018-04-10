package byog.TileEngine;

import java.awt.Color;
import java.io.Serializable;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset implements Serializable {
    private static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    private static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    // public static final TETile LOCKED_DOOR = new TETile('█', Color.WHITE, Color.black,
    //        "locked door");
    private static final TETile LOCKED_DOOR = new TETile('X', Color.WHITE, Color.black,
            "locked door");
    //public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
    //        "unlocked door");
    private static final TETile UNLOCKED_DOOR = new TETile('▢', Color.WHITE, Color.WHITE,
            "unlocked door");
    private static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    private static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    private static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    private static final TETile KEY = new TETile('▲', Color.WHITE, Color.black, "key");
    //public static TETile PLAYER = new TETile('*', Color.white, Color.black, "player");
    private static TETile topBar = new TETile('-', Color.black, Color.darkGray, "top bar");
    private static TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    private static TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    private static TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    //public static final TETile FLOWER = new TETile('❀', Color.WHITE, Color.BLACK, "flower");
    private static TETile FLOWER = new TETile('♚', new Color(247, 202, 0), Color.BLACK, "flower");//'❖'
    private static TETile COIN = new TETile('⌾', Color.black, Color.WHITE, "coin");
    private static TETile MONSTER = new TETile('♞', Color.BLACK, Color.WHITE, "monster");
    private static TETile BOOST = new TETile('✭', Color.DARK_GRAY, Color.black, "boost");


    public static void chooseColor(int round) {
        //nt color = r.nextInt(6);
        //int color = 6;
        switch (round ) {
            case 1:
                red();
                break;
            case 2:
                blue();
                break;
            case 3:
                green();
                break;
            case 4:
                orange();
                break;
            case 5:
                purple();
                break;
            case 6:
                pink();
                break;
            default:
                red();
                break;
        }
    }

    public static void red() {
        NOTHING = new TETile(' ', Color.black, new Color(61, 9, 17), "nothing");
        FLOOR = new TETile('·', Color.black, new Color(175, 0, 0),
                "floor");
        WALL = new TETile('#', Color.darkGray, new Color(117, 0, 0), "wall");
        COIN = new TETile('◉', Color.white, new Color(175, 0, 0), "coin");
        BOOST = new TETile('✪', Color.WHITE, new Color(175, 0, 0), "boost");
        MONSTER = new TETile('♞', Color.BLACK, new Color(175, 0, 0), "monster");
        //FLOWER = new TETile('♚', new Color(247, 202, 0), Color.BLACK, "flower");//'❖'


    }

    public static void green() {
        NOTHING = new TETile(' ', Color.black, new Color(8, 40, 31), "nothing");
        FLOOR = new TETile('·', Color.black, new Color(87, 158, 69),
                "floor");
        WALL = new TETile('#', Color.darkGray, new Color(23, 71, 45), "wall");
        COIN = new TETile('◉', Color.white, new Color(87, 158, 69), "coin");
        BOOST = new TETile('✪', Color.WHITE, new Color(87, 158, 69), "boost");
        MONSTER = new TETile('♞', Color.BLACK, new Color(87, 158, 69), "monster");

    }

    public static void blue() {
        NOTHING = new TETile(' ', Color.black, new Color(0, 36, 60), "nothing");
        FLOOR = new TETile('·', Color.black, new Color(0, 117, 196),
                "floor");
        WALL = new TETile('~', Color.darkGray, new Color(0, 76, 128), "wall");
        COIN = new TETile('◉', Color.white, new Color(0, 117, 196), "coin");
        BOOST = new TETile('✪', Color.WHITE, new Color(0, 117, 196), "boost");
        MONSTER = new TETile('♞', Color.BLACK, new Color(0, 117, 196), "monster");
    }

    public static void orange() {
        NOTHING = new TETile(' ', Color.black, new Color(255, 110, 0), "nothing");
        FLOOR = new TETile('·', Color.WHITE, new Color(247, 202, 0),
                "floor");
        WALL = new TETile('~', Color.lightGray, new Color(255, 178, 0), "wall");
        COIN = new TETile('◉', Color.WHITE, new Color(247, 202, 0), "coin");
        BOOST = new TETile('✪', Color.WHITE, new Color(247, 202, 0), "boost");
        MONSTER = new TETile('♞', Color.BLACK, new Color(247, 202, 0), "monster");
    }

    public static void purple() {
        NOTHING = new TETile(' ', Color.black, new Color(58, 29, 96), "nothing");
        FLOOR = new TETile('·', Color.black, new Color(139, 97, 198),
                "floor");
        WALL = new TETile('#', Color.darkGray, new Color(95, 55, 150), "wall");
        COIN = new TETile('◉', Color.white, new Color(139, 97, 198), "coin");
        BOOST = new TETile('✪', Color.WHITE, new Color(139, 97, 198), "boost");
        MONSTER = new TETile('♞', Color.BLACK, new Color(139, 97, 198), "monster");

    }

    public static void pink() {
        NOTHING = new TETile(' ', Color.black, new Color(141, 12, 78), "nothing");
        FLOOR = new TETile('·', Color.WHITE, new Color(226, 91, 165),
                "floor");
        WALL = new TETile('~', Color.darkGray, new Color(210, 17, 115), "wall");
        COIN = new TETile('◉', Color.white, new Color(226, 91, 165), "coin");
        BOOST = new TETile('✪', Color.WHITE, new Color(226, 91, 165), "boost");
        MONSTER = new TETile('♞', Color.BLACK, new Color(226, 91, 165), "monster");

    }

    public static TETile getGRASS() {
        return GRASS;
    }

    public static TETile getWATER() {
        return WATER;
    }

    public static TETile getLockedDoor() {
        return LOCKED_DOOR;
    }

    public static TETile getUnlockedDoor() {
        return UNLOCKED_DOOR;
    }

    public static TETile getSAND() {
        return SAND;
    }

    public static TETile getMOUNTAIN() {
        return MOUNTAIN;
    }

    public static TETile getTREE() {
        return TREE;
    }

    public static TETile getKEY() {
        return KEY;
    }

    public static TETile getTopBar() {
        return topBar;
    }

    public static TETile getWALL() {
        return WALL;
    }

    public static TETile getFLOOR() {
        return FLOOR;
    }

    public static TETile getNOTHING() {
        return NOTHING;
    }

    public static TETile getFLOWER() {
        return FLOWER;
    }

    public static TETile getCOIN() {
        return COIN;
    }

    public static TETile getMONSTER() {
        return MONSTER;
    }

    public static TETile getBOOST() {
        return BOOST;
    }
}




