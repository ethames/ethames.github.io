package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Monster extends Character {
    private int xPos;
    private int yPos;
    private char direction;
    private Random r;
    Character hero;


    public Monster(int xStart, int yStart) {
        this.xPos = xStart;
        this.yPos = yStart;
        this.direction = 's';
    }

    public void setHero(Character hero) {
        this.hero = hero;
    }

    public void attackHero() {
        hero.setLives(hero.getLives()-1);
    }

    public boolean moveRight(TETile[][] finalWorldFrame) {
        if (checkPlayer(xPos - 1, yPos , finalWorldFrame) || checkPlayer(xPos + 1, yPos , finalWorldFrame))  {
            attackHero();
            changeDirection();
            return true;
        }
        moveCharacter(xPos + 1, yPos, finalWorldFrame);
        return false;

    }

    public boolean moveUp(TETile[][] finalWorldFrame) {
        if (checkPlayer(xPos, yPos -1 , finalWorldFrame) || checkPlayer(xPos, yPos + 1 , finalWorldFrame))  {
            attackHero();
            changeDirection();
            return true;
        }
        moveCharacter(xPos, yPos + 1, finalWorldFrame);
        return false;

    }

    public boolean moveDown(TETile[][] finalWorldFrame) {
        if (checkPlayer(xPos, yPos -1 , finalWorldFrame) || checkPlayer(xPos, yPos + 1 , finalWorldFrame)){
            attackHero();
            changeDirection();
            return true;
        }
        moveCharacter(xPos, yPos - 1, finalWorldFrame);
        return false;
    }

    public boolean moveLeft(TETile[][] finalWorldFrame) {
        if (checkPlayer(xPos - 1, yPos , finalWorldFrame) || checkPlayer(xPos + 1, yPos , finalWorldFrame)) {
            attackHero();
            changeDirection();
            return true;
        }
        moveCharacter(xPos - 1, yPos, finalWorldFrame);
        return false;
    }
    public void moveMonster(TETile[][] fwf) {
        checkWall(fwf);
        switch (this.direction) {
            case 's': moveDown(fwf); break;
            case 'a': moveLeft(fwf); break;
            case 'w': moveUp(fwf); break;
            case 'd': moveRight(fwf); break;
            default: break;
        }
    }

    public void moveCharacter(int x, int y, TETile[][] fwf) {
        if (fwf[x][y].equals(Tileset.getFLOOR())) {
            fwf[xPos][yPos] = Tileset.getFLOOR();
            fwf[x][y] = Tileset.getMONSTER();
            xPos = x;
            yPos = y;
        }
    }
    public void checkWall(TETile[][] fwf) {
       switch (direction) {
           case 'w':
               if (!fwf[xPos][yPos + 1].equals(Tileset.getFLOOR())) {
                   changeDirection();
               }
               break;
           case 'a':
               if (!fwf[xPos - 1][yPos].equals(Tileset.getFLOOR())) {
                   changeDirection();
               }
               break;
           case 's':
               if (!fwf[xPos][yPos - 1].equals(Tileset.getFLOOR())) {
                   changeDirection();
               }
               break;
           case 'd':
               if (!fwf[xPos + 1][yPos].equals(Tileset.getFLOOR())) {
                   changeDirection();
               }
               break;
           default:
               changeDirection();
               break;

       }
    }

    public boolean checkPlayer(int x, int y, TETile[][] fwf) {
        if (fwf[x][y].equals(Tileset.getFLOWER())) {
            return true;
        } else {
            return false;
        }
    }

    public void setR(Random r) {
        this.r = r;
    }

    public void changeDirection() {
        char temp = direction;
        char[] dir = new char[] {'a','s','d','w'};
        while (direction == temp) {
            int i = r.nextInt(4);
            direction = dir[i];
        }
    }
}
