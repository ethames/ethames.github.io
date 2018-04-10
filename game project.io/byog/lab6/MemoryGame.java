package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class MemoryGame {
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
            "You got this!", "You're a star!", "Go Bears!",
            "Too easy for you!", "Wow, so impressive!"};
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.RED);
        StdDraw.enableDoubleBuffering();
        //TODO: Initialize random number generator
        this.rand = new Random(seed);
    }

    public static void main(String[] args) throws InterruptedException {
        args = new String[1];
        args[0] = "24358789";
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = 123456; //Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String letters = "";
        while (n > 0) {
            letters += CHARACTERS[rand.nextInt(26)];
            n--;
        }
        return letters;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.RED);
        StdDraw.text(this.width / 2, this.height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) throws InterruptedException {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            Thread.sleep(2000);
            drawFrame(letters.substring(i, i + 1));
            Thread.sleep(2000);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String input = "";
        while (StdDraw.hasNextKeyTyped() && n > 0) {
            input += StdDraw.nextKeyTyped();
            n--;
        }
        return input;
    }

    public void startGame() throws InterruptedException {
        //TODO: Set any relevant variables before the game starts
        round = 1;
        String randStr = "a";
        String input = "a";
        while (randStr.equals(input)) {
            drawFrame("Round: " + round);
            randStr = generateRandomString(round);
            flashSequence(randStr);
            input = solicitNCharsInput(round);
            round++;
        }
        drawFrame("Game Over! You got to round " + round);

        //TODO: Establish Game loop
    }

}
