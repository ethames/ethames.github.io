package byog.Core;

import byog.TileEngine.TERenderer;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

import java.io.Serializable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import java.util.ArrayList;
import java.util.Random;

public class Game implements Serializable {
    /* Feel free to change the width and height. */
    private static int WIDTH = 80;
    private static int HEIGHT = 30;
    private static long savedSeed;
    private World world;
    private Random r;
    private int round;
    private String header;
    private int totalScore;
    private TETile[][] finalWorldFrame;
    private ArrayList<Monster> monsters;
    private LeaderBoard leaderBoard;
    //private static int[] topScores;
    ///sprivate static String[] topScoresString;

    TERenderer ter = new TERenderer();
    public static int getWIDTH() {
        return WIDTH;
    }
    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static void printTitle() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(WIDTH * 16, (HEIGHT) * 16);
        StdDraw.clear(Color.LIGHT_GRAY);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        Font font = new Font("Monaco", Font.BOLD, 90);
        StdDraw.setPenColor(new Color(97, 3, 69));
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "GAME");
        //StdDraw.setPenColor(new Color(226, 91, 165));
        StdDraw.setPenColor(new Color(149, 25, 12));
        StdDraw.text(WIDTH / 3 - 1, HEIGHT / 3, "GAME");
        StdDraw.setPenColor(new Color(16, 126, 125));
        StdDraw.text(WIDTH * 2 / 3 + 1, HEIGHT * 2 / 3, "GAME");
        StdDraw.setPenColor(new Color(227, 181, 5));
        StdDraw.text(10, HEIGHT / 6, "GAME");
        StdDraw.setPenColor(new Color(4, 75, 127));
        StdDraw.text(WIDTH * 5 / 6 + 3, HEIGHT * 5 / 6, "GAME");

        StdDraw.show();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
    }

    public static void printMenu() {
        //StdDraw.enableDoubleBuffering();
        //StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.clear(Color.LIGHT_GRAY);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        Font font = new Font("Monaco", Font.BOLD, 80);
        StdDraw.setPenColor(new Color(97, 3, 69));
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 8, "GAME");
        font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(new Color(149, 25, 12));
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "NEW GAME (N)");
        StdDraw.setPenColor(new Color(4, 75, 127));
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "LOAD GAME (L)");
        StdDraw.setPenColor(new Color(16, 126, 125));
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, "QUIT (Q)");
        StdDraw.show();
    }

    public String selectStart(int n) {
        while (!StdDraw.hasNextKeyTyped()) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
            }
        }
        String input = "";
        while (StdDraw.hasNextKeyTyped() && n > 0) {
            input += StdDraw.nextKeyTyped();
            n--;
        }
        return input;
    }

    public void saveGame() {
        // @Source Java Useful Resources: https://www.tutorialspoint.com/java/java_serialization.htm
        File f = new File("./world.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(world);
            out.close();
            //fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void saveLeaderBoard() {
        // @Source Java Useful Resources: https://www.tutorialspoint.com/java/java_serialization.htm
        File f = new File("./leaderboard.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(leaderBoard);
            out.close();
            //fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void loadLeaderBoard() {
        try {
            FileInputStream fileIn = new FileInputStream("./leaderboard.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.leaderBoard = (LeaderBoard) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("World class not found");
            c.printStackTrace();
            return;
        }
    }
    public World getWorld() {
        return world;
    }

    public void newRound() {
        r = world.getR();
        world.setTotalScore(world.getTotalScore());
        totalScore = world.getTotalScore();
        world.setRound(world.getRound() + 1);
        if (world.getRound() > 6) {
           winGame();
        } else {
            long newSeed = r.nextInt(999);
            world = new World(WIDTH, HEIGHT, world.getRound());
            world.setSeed(newSeed);
            world.setTotalScore(totalScore);
            world.makeMonster();
            monsters = world.getMonsters();
            ter.renderFrame(world.getFinalWorldFrame());
            getMovement();
        }

    }


    public void loadGame() {
        try {
            FileInputStream fileIn = new FileInputStream("./world.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            world = (World) in.readObject();
            this.monsters = world.getMonsters();
            in.close();
            fileIn.close();
            round = world.getRound();
            Tileset.chooseColor(round);
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("World class not found");
            c.printStackTrace();
            return;
        }
    }

    public void playWithKeyboard() {
        printTitle();
        printMenu();
        String input = selectStart(1);

        //String input = "N";
        int level = 1;
        world = new World(WIDTH, HEIGHT, level);

        if (input.equals("N") || input.equals("n")) {
            long seed = getSeed();
            world.setSeed(seed);
            world.makeMonster();
            monsters = world.getMonsters();
        } else if (input.equals("L") || input.equals("l")) {
            //long seed = savedSeed;
            loadGame();
            //world.setSeed(seed);
        } else if (input.equals("Q") || input.equals("q")) {
            // game ends
            saveGame();
            printLeaveScreen();
        }
        loadLeaderBoard();
        //leaderBoard = new LeaderBoard();
        ter.renderFrame(world.getFinalWorldFrame());
        getMovement();
    }

    public void getMovement() {
        boolean door = false;
        world.getHero().setDoor(world.getDoor());
        //time = world.timeLimit;

        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).setHero(world.getHero());
        }

        while (world.getTimeLimit() > 0) {
            StdDraw.clear(Color.BLACK);
            ter.renderFrame(world.getFinalWorldFrame());
            for (int i = 0; i < monsters.size(); i++){
                monsters.get(i).moveMonster(world.getFinalWorldFrame());
            }
            if (world.getHero().getLives() == 0) {
                break;
            }
            headsUpDisplay();
            if (StdDraw.hasNextKeyTyped()) {
                // Quit Q
                world.setTotalScore(world.getTotalScore() + world.getHero().getIncrease());
                world.getHero().setIncrease(0);
                if (world.getHero().getExtraTime() > 0) {
                    world.setTimeLimit(world.getTimeLimit() + world.getHero().getExtraTime());
                    StdDraw.clear(Color.BLACK);
                    ter.renderFrame(world.getFinalWorldFrame());
                    printExtraTime();
                    world.getHero().setExtraTime(0);
                }
                if (StdDraw.isKeyPressed(113) || StdDraw.isKeyPressed(81)) {
                    saveGame();
                    printLeaveScreen();
                    break;
                }
                //up W
                if (StdDraw.isKeyPressed(119) || StdDraw.isKeyPressed(87)) {
                    door = world.getHero().moveUp(world.getFinalWorldFrame());
                }
                // down S
                if (StdDraw.isKeyPressed(115) || StdDraw.isKeyPressed(83)) {
                    door = world.getHero().moveDown(world.getFinalWorldFrame());
                }
                // left A
                if (StdDraw.isKeyPressed(97) || StdDraw.isKeyPressed(65)) {
                    door = world.getHero().moveLeft(world.getFinalWorldFrame());
                }
                // right D
                if (StdDraw.isKeyPressed(68) || StdDraw.isKeyPressed(100)) {
                    door = world.getHero().moveRight(world.getFinalWorldFrame());
                }
                ter.renderFrame(world.getFinalWorldFrame());

                headsUpDisplay();
                if (door) {
                    newRound();
                    break;
                }

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
            }
            world.setTimeLimit(world.getTimeLimit() - .1);
        }
        gameOver();
    }

    public void gameOver() {
        StdDraw.clear(Color.LIGHT_GRAY);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        Font font = new Font("Monaco", Font.BOLD, 80);
        StdDraw.setPenColor(new Color(97, 3, 69));
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 8, "GAME OVER");
        font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(new Color(149, 25, 12));
        String totalScoreS = "Total Score: " + Integer.toString(world.getTotalScore());
        StdDraw.text(WIDTH / 2, HEIGHT / 2, totalScoreS);
        StdDraw.setPenColor(new Color(4, 75, 127));
        String roundS = "Final Round: " + Integer.toString(world.getRound());
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, roundS);
        StdDraw.show();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
        checkScore();
        saveLeaderBoard();
        printLeaderboard();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
        StdDraw.clear(Color.BLACK);
    }

    public void winGame() {
        StdDraw.clear(Color.LIGHT_GRAY);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        Font font = new Font("Monaco", Font.BOLD, 80);
        StdDraw.setPenColor(new Color(97, 3, 69));
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 8, "YOU WON");
        font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(new Color(149, 25, 12));
        String totalScoreS = "Total Score: " + Integer.toString(world.getTotalScore());
        StdDraw.text(WIDTH / 2, HEIGHT / 2, totalScoreS);
        StdDraw.setPenColor(new Color(4, 75, 127));
        String roundS = "Final Round: " + Integer.toString(world.getRound());
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, roundS);
        StdDraw.show();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
        checkScore();
        saveLeaderBoard();
        printLeaderboard();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
        StdDraw.clear(Color.BLACK);
    }
    public void checkScore() {
        this.totalScore = world.getTotalScore();
        for (int i = 0; i < 3; i++) {
            if (totalScore > leaderBoard.getTopScores()[i]) {
                int temp = leaderBoard.getTopScores()[i];
                String tempString = leaderBoard.getTopScoresString()[i];
                leaderBoard.setTopScores(i, totalScore);
                leaderBoard.setTopScoresString(i, getName() + " " + totalScore);
                while (i < 2) {
                    int temp2 = leaderBoard.getTopScores()[i + 1];
                    String temp2String = leaderBoard.getTopScoresString()[i + 1];
                    leaderBoard.setTopScores(i + 1, temp);
                    leaderBoard.setTopScoresString(i + 1, tempString);
                    temp = temp2;
                    tempString = temp2String;
                    i++;
                }
            }
        }

    }


    public String getName() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
        StdDraw.clear(Color.LIGHT_GRAY);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "enter name (type . when finished)");
        StdDraw.show();

        boolean over = false;
        String name =  "";
        //String alpha = "abcdefghijklmnopqrstuv";
        while (!over) {
            if (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                if (letter == '.') {
                    over = true;
                } else {
                    name += letter;
                    StdDraw.clear(Color.LIGHT_GRAY);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, name);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "Enter seed (Press S when finished)");
                    StdDraw.show();
                }
            }
        }
        return name;
    }

    public void printLeaveScreen() {
        StdDraw.clear(Color.LIGHT_GRAY);
        StdDraw.setPenColor(new Color(97, 3, 69));
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Goodbye");
        StdDraw.show();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
    }

    public long getSeed() {
        // Gets seed from user and makes new world instance
        StdDraw.clear(Color.LIGHT_GRAY);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Enter seed (Press S when finished)");
        StdDraw.show();

        String seedTemp = "";
        char nextChar = ' ';
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                if (StdDraw.isKeyPressed(83) || StdDraw.isKeyPressed(115)) {
                    break;
                }
                nextChar = StdDraw.nextKeyTyped();
                seedTemp += nextChar;
                StdDraw.clear(Color.LIGHT_GRAY);
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, seedTemp);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "Enter seed (Press S when finished)");
                StdDraw.show();
            }

        }
        long seed = Long.parseLong(seedTemp);
        // make new world instance
        return seed;
    }

    public void stringMovement(String input) {
        while (!input.equals(":q") && !input.equals("")) {
            char move = input.charAt(0);
            world.getHero().setDoor(world.getDoor());
            switch (move) {
                case 'a':
                    world.getHero().moveLeft(world.getFinalWorldFrame());
                    break;
                case 's':
                    world.getHero().moveDown(world.getFinalWorldFrame());
                    break;
                case 'd':
                    world.getHero().moveRight(world.getFinalWorldFrame());
                    break;
                case 'w':
                    world.getHero().moveUp(world.getFinalWorldFrame());
                    break;
                default:
                    break;
            }
            input = input.substring(1);
            /**try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
            }*/
            ter.renderFrame(world.getFinalWorldFrame());
        }
        if (input.equals(":q") || input.equals(":Q")) {
            saveGame();
        }
    }

    public void printLeaderboard() {
        StdDraw.clear(Color.LIGHT_GRAY);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        Font font = new Font("Monaco", Font.BOLD, 80);
        StdDraw.setPenColor(new Color(97, 3, 69));
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 8, "LEADER BOARD");
        font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(new Color(149, 25, 12));
        StdDraw.text(WIDTH / 2, HEIGHT / 2, leaderBoard.getTopScoresString()[0]);
        StdDraw.setPenColor(new Color(4, 75, 127));
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, leaderBoard.getTopScoresString()[1]);
        StdDraw.setPenColor(new Color(16, 126, 125));
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, leaderBoard.getTopScoresString()[2]);
        StdDraw.show();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
        StdDraw.clear(Color.BLACK);
    }

    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        // @source BoringWorldDemo.java
        //TERenderer ter = new TERenderer();
        //ter.initialize(WIDTH, HEIGHT);
        //initialize tiles

        //if input ends with :q
        // saved = input
        round = 1;
        world = new World(WIDTH, HEIGHT, round);
        if (input.equals("")) {
            return world.getFinalWorldFrame();
        }
        char starter = input.charAt(0);
        if (starter == 'n' || starter == 'N') {
            input = input.substring(1);
            String seedTemp = "";
            while (input.charAt(0) != 's' && input.charAt(0) != 'S') {
                seedTemp += input.charAt(0);
                input = input.substring(1);
            }
            long seed = Long.parseLong(seedTemp);
            world.setSeed(seed);
            String movement = input;
            stringMovement(movement);

            // make rooms
            if (input.length() > 1) {
                String end = input.substring(input.length() - 2);
                if (end.equals(":q") || end.equals(":Q")) {
                    savedSeed = seed;
                }
            }
        } else if (starter == 'l' || starter == 'L') {
            loadGame();
            String movement = input.substring(1);
            stringMovement(movement);
        } else if (starter == 'q' || starter == 'Q') {
            saveGame();
        }
        finalWorldFrame = world.getFinalWorldFrame();
        //ter.renderFrame(finalWorldFrame);
        return world.getFinalWorldFrame();
    }

    public void headsUpDisplay() {
        double mouseX;
        double mouseY;
        mouseX = StdDraw.mouseX();
        mouseY = StdDraw.mouseY();
        int mX = (int) mouseX;
        int mY = (int) mouseY;
        checkTile(mX, mY);
        printHeader();
    }

    public void printHeader() {
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text(5, HEIGHT - 1, header);
        String score = "Score: " + Integer.toString(world.getHero().getScore());
        String totalScoreS = "Total Score: " + Integer.toString(world.getTotalScore());
        String roundS = "Round: " + Integer.toString(world.getRound());
        String timeS = "Remaining Time: " + Integer.toString((int) world.getTimeLimit());
        String hearts = "";
        int heroLives = world.getHero().getLives();
        String lives = "Lives: " + heroLives;
        StdDraw.text(15, HEIGHT - 1, score);
        StdDraw.text(25, HEIGHT - 1, totalScoreS);
        StdDraw.text(35, HEIGHT - 1, roundS);
        StdDraw.text(50, HEIGHT - 1, timeS);
        StdDraw.text(65, HEIGHT - 1, lives);
        StdDraw.show();
    }

    public void printExtraTime() {
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT - 1, "You get 5 extra seconds!");
    }

    public void checkTile(int mouseX, int mouseY) {
        if (world.getFinalWorldFrame()[mouseX][mouseY].equals(Tileset.getFLOOR())) {
            header = "Floor";
        } else if (world.getFinalWorldFrame()[mouseX][mouseY].equals(Tileset.getWALL())) {
            header = "Wall";
        } else if (world.getFinalWorldFrame()[mouseX][mouseY].equals(Tileset.getLockedDoor())) {
            header = "Locked Door";
        } else if (world.getFinalWorldFrame()[mouseX][mouseY].equals(Tileset.getNOTHING())) {
            header = "Outside";
        } else if (world.getFinalWorldFrame()[mouseX][mouseY].equals(Tileset.getFLOWER())) {
            header = "You";
        } else if (world.getFinalWorldFrame()[mouseX][mouseY].equals(Tileset.getCOIN())) {
            header = "Coin";
        } else if (world.getFinalWorldFrame()[mouseX][mouseY].equals(Tileset.getUnlockedDoor())) {
            header = "Unlocked Door";
        } else if (world.getFinalWorldFrame()[mouseX][mouseY].equals(Tileset.getKEY())) {
            header = "Key";
        } else if (world.getFinalWorldFrame()[mouseX][mouseY].equals(Tileset.getMONSTER())) {
            header = "Monster";
        } else if (world.getFinalWorldFrame()[mouseX][mouseY].equals(Tileset.getBOOST())) {
            header = "Extra Time";
        } else {
            header = "Not on Screen";
        }
    }



}
