package byog.Core;

/**
 * This is the main entry point for the program. This class simply parses
 * the command line inputs, and lets the byog.Core.Game class take over
 * in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // problem seed s623424s
        //Game g = new Game();
        //g.playWithInputString("lwwww");
        //"n9225807swwwwssssssaaaaddd:q"); "lwwww"
        Game g2 = new Game();
        g2.playWithKeyboard();

        long time1 = System.currentTimeMillis();

        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Game game = new Game();
            game.playWithInputString(args[0]);
            System.out.println(game.toString());
        } else {
            Game game = new Game();
            game.playWithKeyboard();
        }
        long time2 = System.currentTimeMillis();
        System.out.println(time2 - time1);
    }
}
