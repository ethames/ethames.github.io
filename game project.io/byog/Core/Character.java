package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class Character implements Serializable {
    private TETile image = Tileset.getFLOWER();
    private int xPos;

    public void setDoor(Integer[] door) {
        this.door = door;
    }

    private int yPos;
    private Integer[] door;
    private int score;
    private int increase;
    private int extraTime;
    private int lives;


    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public void setIncrease(int increase) {
        this.increase = increase;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setExtraTime(int extraTime) {
        this.extraTime = extraTime;
    }

    public Integer[] getDoor() {
        return door;
    }

    public int getScore() {
        return score;
    }

    public int getIncrease() {
        return increase;
    }

    public int getExtraTime() {
        return extraTime;
    }

    public Character() {
        score = 0;
        increase = 0;
        extraTime = 0;
        lives = 4;
    }

    public void setStartPoint(Random r, TETile[][] fwf) {
        int xStart = r.nextInt(Game.getWIDTH());
        int yStart = r.nextInt(Game.getHEIGHT());
        while (fwf[xStart][yStart] != Tileset.getFLOOR()) {
            xStart = r.nextInt(Game.getWIDTH());
            yStart = r.nextInt(Game.getHEIGHT());
        }
        fwf[xStart][yStart] = image;
        xPos = xStart;
        yPos = yStart;
    }

    public boolean moveRight(TETile[][] finalWorldFrame) {
        if (checkDoor(xPos + 1, yPos, finalWorldFrame)) {
            return true;
        }
        checkCoin(xPos + 1, yPos, finalWorldFrame);
        checkKey(xPos + 1, yPos, finalWorldFrame);
        moveCharacter(xPos + 1, yPos, finalWorldFrame);
        return false;

    }

    public boolean moveUp(TETile[][] finalWorldFrame) {
        if (checkDoor(xPos, yPos + 1, finalWorldFrame)) {
            return true;
        }
        checkCoin(xPos, yPos + 1, finalWorldFrame);
        checkKey(xPos, yPos + 1, finalWorldFrame);
        moveCharacter(xPos, yPos + 1, finalWorldFrame);
        return false;

    }

    public boolean moveDown(TETile[][] finalWorldFrame) {
        if (checkDoor(xPos, yPos - 1, finalWorldFrame)) {
            return true;
        }
        checkCoin(xPos, yPos - 1, finalWorldFrame);
        checkKey(xPos, yPos - 1, finalWorldFrame);
        moveCharacter(xPos, yPos - 1, finalWorldFrame);
        return false;
    }

    public boolean moveLeft(TETile[][] finalWorldFrame) {
        if (checkDoor(xPos - 1, yPos, finalWorldFrame)) {
            return true;
        }
        checkCoin(xPos - 1, yPos, finalWorldFrame);
        checkKey(xPos - 1, yPos, finalWorldFrame);
        moveCharacter(xPos - 1, yPos, finalWorldFrame);
        return false;
    }

    public boolean checkDoor(int x, int y, TETile[][] fwf) {
        if (fwf[x][y].equals(Tileset.getUnlockedDoor())) {
            return true;
        }
        return false;
    }

    public void checkKey(int x, int y, TETile[][] fwf) {
        if (fwf[x][y].equals(Tileset.getKEY())) {
            fwf[door[0]][door[1]] = Tileset.getUnlockedDoor();
            fwf[x][y] = Tileset.getFLOOR();
        }
    }

    public void moveCharacter(int x, int y, TETile[][] fwf) {
        if (fwf[x][y].equals(Tileset.getFLOOR())) {
            fwf[xPos][yPos] = Tileset.getFLOOR();
            fwf[x][y] = image;
            xPos = x;
            yPos = y;
        }
    }

    public void checkCoin(int x, int y, TETile[][] fwf) {
        if (fwf[x][y].equals(Tileset.getCOIN())) {
            fwf[x][y] = Tileset.getFLOOR();
            score += 1;
            increase += 1;
        } else if (fwf[x][y].equals(Tileset.getBOOST())) {
            fwf[x][y] = Tileset.getFLOOR();
            extraTime += 5;
        } else {
            increase = 0;
        }
    }

    public void checkMonster(int x, int y, TETile[][] fwf) {
        if (fwf[x][y].equals(Tileset.getCOIN())) {
            fwf[x][y] = Tileset.getFLOOR();
            score += 1;
            increase += 1;
        } else if (fwf[x][y].equals(Tileset.getBOOST())) {
            fwf[x][y] = Tileset.getFLOOR();
            extraTime += 5;
        } else {
            increase = 0;
        }
    }



}
