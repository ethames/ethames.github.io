package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World implements Serializable {

    //private static long serialVersionUID = -1384942743799532936l;
    private int WIDTH;
    private int HEIGHT;
    private long seed;
    private Character hero;
    private Random r;
    private int round;
    private ArrayList<Monster> monsters;
    private Integer[] door;
    private int totalScore;
    private double timeLimit;
    private TETile[][] finalWorldFrame;

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }
    public Character getHero() {
        return hero;
    }

    public Random getR() {
        return r;
    }

    public int getRound() {
        return round;
    }

    public Integer[] getDoor() {
        return door;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public double getTimeLimit() {
        return timeLimit;
    }

    public World(int width, int height, int round) {
        WIDTH = width;
        HEIGHT = height;
        finalWorldFrame = emptyWorld();
        this.round = round;
        this.totalScore = 0;
    }

    public double timeLimit() {
        switch (round) {
            case 1:
                return 60;
            case 2:
                return 50;
            case 3:
                return 40;
            case 4:
                return 30;
            case 5:
                return 20;
            case 6:
                return 10;
            default:
                return 60;
        }
    }

    public void setSeed(long seed1) {
        this.seed = seed1;
        this.makeWorld(seed);
    }

    public void makeWorld(long seed1) {
        Random r1 = new Random(seed1);
        this.r = r1;
        Tileset.chooseColor(round);
        this.timeLimit = timeLimit();
        emptyWorld();
        List<Integer[]> rooms = makeRooms(r, finalWorldFrame);
        List<Integer[]> sortedRooms = roomSort(rooms);
        drawHallways(sortedRooms, finalWorldFrame);
        hero = new Character();
        hero.setStartPoint(r, finalWorldFrame);
        placeDoor(finalWorldFrame, r);
        placeKey();
        placeCoins();
        //fillInNothing(finalWorldFrame);
    }

    public TETile[][] getFinalWorldFrame() {
        return finalWorldFrame;
    }

    public TETile[][] emptyWorld() {
        // Initialize a new world filled with nothing
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.getNOTHING();
            }
        }
        for (int i = 0; i < WIDTH; i++) {
            finalWorldFrame[i][HEIGHT - 2] = Tileset.getTopBar();
            finalWorldFrame[i][HEIGHT - 1] = Tileset.getTopBar();
        }
        return finalWorldFrame;
    }

    public void makeHallway(Integer[] room1, Integer[] room2, TETile[][] fwf) {
        // Centers of mass of the two rooms

        Integer[] rightRoom = room1;
        Integer[] leftRoom = room2;
        if (room2[0] > room1[0]) {
            rightRoom = room2;
            leftRoom = room1;
        }
        Integer[] upperRoom = room1;
        Integer[] bottomRoom = room2;
        if (room2[1] > room1[1]) {
            upperRoom = room2;
            bottomRoom = room1;
        }
        int overlapY = checkVerticalOverlaps(room1, room2);
        if (overlapY == -1) {
            overlapY = checkVerticalOverlaps(room2, room1);
        }
        int overlapX = checkHorizontalOverlap(room1, room2);
        if (overlapX == -1) {
            overlapX = checkVerticalOverlaps(room2, room1);
        }
        if (overlapY > 0) {
            // makes straight horizontal hallway
            for (int x = leftRoom[0] + leftRoom[2] - 1; x < rightRoom[0] + 1; x++) {
                //straight hallway
                if (fwf[x][overlapY] != Tileset.getFLOOR()) {
                    fwf[x][overlapY] = Tileset.getFLOOR();
                    if (fwf[x][overlapY + 1] != Tileset.getFLOOR()) {
                        fwf[x][overlapY + 1] = Tileset.getWALL();
                    }
                    if (fwf[x][overlapY - 1] != Tileset.getFLOOR()) {
                        fwf[x][overlapY - 1] = Tileset.getWALL();
                    }
                }
            }
        } else if (overlapX > 0) {
            // makes straight vertical hallway
            for (int y = bottomRoom[1] + bottomRoom[3] - 1; y < upperRoom[1] + 1; y++) {
                if (fwf[overlapX][y] != Tileset.getFLOOR()) {
                    fwf[overlapX][y] = Tileset.getFLOOR();
                    if (fwf[overlapX - 1][y] != Tileset.getFLOOR()) {
                        fwf[overlapX - 1][y] = Tileset.getWALL();
                    }
                    if (fwf[overlapX + 1][y] != Tileset.getFLOOR()) {
                        fwf[overlapX + 1][y] = Tileset.getWALL();
                    }
                }
            }
        } else {
            hallwayTurn(room1, room2, fwf);
        }
    }

    public Integer[] closestRoom(Integer[] targetRoom, List<Integer[]> rooms) {

        int minimum = (int) Math.pow(WIDTH + HEIGHT, 2);
        Integer[] closest = targetRoom;
        int distance = 0;
        for (int i = 0; i < rooms.size(); i++) {
            int d1 = rooms.get(i)[0] + rooms.get(i)[2] / 2 - targetRoom[0] - targetRoom[2] / 2;
            int d2 = rooms.get(i)[1] + rooms.get(i)[3] / 2 - targetRoom[1] - targetRoom[3] / 2;
            distance = (int) (Math.pow(d1, 2) + Math.pow(d2, 2));
            if (distance < minimum) {
                minimum = distance;
                closest = rooms.get(i);
            }
        }
        return closest;
    }

    public List<Integer[]> roomSort(List<Integer[]> rooms) {
        // Sort rooms in order of distance to (0,0)
        List<Integer[]> sorted = new ArrayList<>();
        Integer[] targetRoom = rooms.get(0);
        sorted.add(targetRoom);
        Integer[] closest;
        rooms.remove(targetRoom);

        while (rooms.size() > 0) {
            closest = closestRoom(targetRoom, rooms);
            rooms.remove(closest);
            sorted.add(closest);
            targetRoom = closest;
        }
        return sorted;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setTimeLimit(double timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public List<Integer[]> makeRooms(Random r1, TETile[][] fwf) {
        int maxHeight = r1.nextInt(15) + 10;
        int maxWidth = r1.nextInt(15) + 10;

        int numRooms = r1.nextInt(30) + 30;
        List<Integer[]> rooms = new ArrayList<>();

        for (int i = 0; i < numRooms; i++) {
            int roomWidth = r1.nextInt(maxWidth);
            int roomHeight = r1.nextInt(maxHeight) - 2;

            while (roomHeight < 3 || roomWidth < 3) {
                roomWidth = r1.nextInt(maxWidth);
                roomHeight = r1.nextInt(maxHeight) - 2;
            }

            int x = r1.nextInt(WIDTH - roomWidth); // x intercept of bottom left corner of room
            int y = r1.nextInt(HEIGHT - 2 - roomHeight);
            // for each row in the room
            if (checkRoom(x, y, roomWidth, roomHeight, fwf)) {
                for (int row = y; row < y + roomHeight; row++) {
                    for (int e = x; e < x + roomWidth; e++) {
                        // For top and bottom perimeters of room
                        if (row == y || row == y + roomHeight - 1) {
                            fwf[e][row] = Tileset.getWALL();
                        } else { // for the middle rows of the room
                            if (e == x || e == (x + roomWidth - 1)) {
                                fwf[e][row] = Tileset.getWALL();
                            } else {
                                fwf[e][row] = Tileset.getFLOOR();
                            }
                        }
                    }
                }
                Integer[] room = new Integer[4];
                room[0] = x;
                room[1] = y;
                room[2] = roomWidth;
                room[3] = roomHeight;
                rooms.add(room);
            }
        }
        return rooms;
    }

    public void hallwayTurn(Integer[] room1, Integer[] room2, TETile[][] fwf) {
        Integer[] rightRoom = room1;
        Integer[] leftRoom = room2;
        if (room2[0] > room1[0]) {
            rightRoom = room2;
            leftRoom = room1;
        }
        Integer[] upperRoom = room1;
        Integer[] bottomRoom = room2;
        if (room2[1] > room1[1]) {
            upperRoom = room2;
            bottomRoom = room1;
        }
        //hallway with turn
        Integer[] turnpoint = new Integer[2];
        turnpoint[0] = rightRoom[0] + rightRoom[2] / 2;
        turnpoint[1] = leftRoom[1] + leftRoom[3] / 2;

        for (int x = leftRoom[0] + leftRoom[2] - 1; x < turnpoint[0] + 1; x++) {
            if (fwf[x][turnpoint[1]] != Tileset.getFLOOR()) {
                fwf[x][turnpoint[1]] = Tileset.getFLOOR();
                if (fwf[x][turnpoint[1] + 1] != Tileset.getFLOOR()) {
                    fwf[x][turnpoint[1] + 1] = Tileset.getWALL();
                }
                if (fwf[x][turnpoint[1] - 1] != Tileset.getFLOOR()) {
                    fwf[x][turnpoint[1] - 1] = Tileset.getWALL();
                }
            }
        }
        if (rightRoom.equals(upperRoom)) {
            // go right and go up
            if (fwf[turnpoint[0] + 1][turnpoint[1]] != Tileset.getFLOOR()) {
                fwf[turnpoint[0] + 1][turnpoint[1]] = Tileset.getWALL();
            }
            if (fwf[turnpoint[0]][turnpoint[1] - 1] != Tileset.getFLOOR()) {
                fwf[turnpoint[0]][turnpoint[1] - 1] = Tileset.getWALL();
            }
            if (fwf[turnpoint[0] + 1][turnpoint[1] - 1] != Tileset.getFLOOR()) {
                fwf[turnpoint[0] + 1][turnpoint[1] - 1] = Tileset.getWALL();
            }
            for (int y = turnpoint[1]; y < upperRoom[1] + 1; y++) {
                if (fwf[turnpoint[0]][y] != Tileset.getFLOOR()) {
                    fwf[turnpoint[0]][y] = Tileset.getFLOOR();
                    if (fwf[turnpoint[0] - 1][y] != Tileset.getFLOOR()) {
                        fwf[turnpoint[0] - 1][y] = Tileset.getWALL();
                    }
                    if (fwf[turnpoint[0] + 1][y] != Tileset.getFLOOR()) {
                        fwf[turnpoint[0] + 1][y] = Tileset.getWALL();
                    }
                }
            }

        } else {
            // go right and go down
            if (fwf[turnpoint[0] + 1][turnpoint[1]] != Tileset.getFLOOR()) {
                fwf[turnpoint[0] + 1][turnpoint[1]] = Tileset.getWALL();
            }
            if (fwf[turnpoint[0]][turnpoint[1] + 1] != Tileset.getFLOOR()) {
                fwf[turnpoint[0]][turnpoint[1] + 1] = Tileset.getWALL();
            }
            if (fwf[turnpoint[0] + 1][turnpoint[1] + 1] != Tileset.getFLOOR()) {
                fwf[turnpoint[0] + 1][turnpoint[1] + 1] = Tileset.getWALL();
            }
            for (int y = turnpoint[1]; y > bottomRoom[1] + bottomRoom[3] - 2; y--) {
                if (fwf[turnpoint[0]][y] != Tileset.getFLOOR()) {
                    fwf[turnpoint[0]][y] = Tileset.getFLOOR();
                    if (fwf[turnpoint[0] - 1][y] != Tileset.getFLOOR()) {
                        fwf[turnpoint[0] - 1][y] = Tileset.getWALL();
                    }
                    if (fwf[turnpoint[0] + 1][y] != Tileset.getFLOOR()) {
                        fwf[turnpoint[0] + 1][y] = Tileset.getWALL();
                    }
                }
            }

        }
    }

    public int checkVerticalOverlaps(Integer[] room1, Integer[] room2) {
        for (int i = room1[1]; i < room1[1] + room1[3] - 2; i++) {
            if (i + 2 < room2[1] + room2[3] && i > room2[1]) {
                return i + 1;
            }
        }
        return -1;
    }

    public int checkHorizontalOverlap(Integer[] room1, Integer[] room2) {
        for (int i = room1[0]; i < room1[0] + room1[2] - 2; i++) {
            if (i + 2 < room2[0] + room2[2] && i > room2[0]) {
                return i + 1;
            }
        }
        return -1;
    }

    public boolean checkRoom(int x, int y, int rW, int rH, TETile[][] fwf) {
        for (int row = y; row < y + rH; row++) {
            for (int e = x; e < x + rW; e++) {
                if (!fwf[e][row].equals(Tileset.getNOTHING())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void drawHallways(List<Integer[]> sortedRooms, TETile[][] fwf) {
        for (int i = 0; i < sortedRooms.size() - 1; i++) {
            makeHallway(sortedRooms.get(i), sortedRooms.get(i + 1), fwf);
        }
    }

    public void placeDoor(TETile[][] fwf, Random r1) {
        int x = r1.nextInt(WIDTH - 2) + 1;
        int y = r1.nextInt(HEIGHT - 2) + 2;
        while (!checkDoor(x, y, fwf)) {
            x = r1.nextInt(WIDTH);
            y = r1.nextInt(HEIGHT);
        }
        door = new Integer[]{x, y};
        fwf[x][y] = Tileset.getLockedDoor();

    }

    public boolean checkDoor(int x, int y, TETile[][] fwf) {
        if (fwf[x][y] != Tileset.getWALL()) {
            return false;
        }
        if (x == 0 || x == WIDTH - 1) {
            if (y == 0) {
                return false;
            }
            TETile up = fwf[x][y + 1];
            TETile down = fwf[x][y - 1];
            if (!up.equals(Tileset.getWALL()) || !down.equals(Tileset.getWALL())) {
                return false;
            }
        } else if (y == 0) {
            TETile right = fwf[x - 1][y];
            TETile left = fwf[x + 1][y];
            if (!right.equals(Tileset.getWALL()) || !left.equals(Tileset.getWALL())) {
                return false;
            }
        } else {
            TETile up = fwf[x][y + 1];
            TETile down = fwf[x][y - 1];
            TETile right = fwf[x - 1][y];
            TETile left = fwf[x + 1][y];
            if (!up.equals(Tileset.getWALL()) || !down.equals(Tileset.getWALL())) {
                if (!right.equals(Tileset.getWALL()) || !left.equals(Tileset.getWALL())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void placeKey() {
        int xStart = r.nextInt(Game.getWIDTH());
        int yStart = r.nextInt(Game.getHEIGHT());
        while (!finalWorldFrame[xStart][yStart].equals(Tileset.getFLOOR())) {
            xStart = r.nextInt(Game.getWIDTH());
            yStart = r.nextInt(Game.getHEIGHT());
        }
        finalWorldFrame[xStart][yStart] = Tileset.getKEY();
    }

    public void placeCoins() {
        int numCoins = r.nextInt(20) + 7;
        for (int i = 0; i < numCoins + 2; i++) {
            int xStart = r.nextInt(Game.getWIDTH());
            int yStart = r.nextInt(Game.getHEIGHT());
            while (!finalWorldFrame[xStart][yStart].equals(Tileset.getFLOOR())) {
                xStart = r.nextInt(Game.getWIDTH());
                yStart = r.nextInt(Game.getHEIGHT());
            }
            finalWorldFrame[xStart][yStart] = Tileset.getCOIN();
            if (i == numCoins + 1 || i == numCoins) {
                finalWorldFrame[xStart][yStart] = Tileset.getBOOST();
            }
        }
    }


    public Monster placeMonster() {
        int xStart = r.nextInt(Game.getWIDTH());
        int yStart = r.nextInt(Game.getHEIGHT());
        while (!finalWorldFrame[xStart][yStart].equals(Tileset.getFLOOR())) {
            xStart = r.nextInt(Game.getWIDTH());
            yStart = r.nextInt(Game.getHEIGHT());
        }
        Monster monster = new Monster(xStart, yStart);
        finalWorldFrame[xStart][yStart] = Tileset.getMONSTER();
        monster.setR(r);
        return monster;
    }

    public void makeMonster() {
        int numOfMonster = r.nextInt(5)+3;
        monsters = new ArrayList<>(numOfMonster);
        for (int i = 0; i < numOfMonster; i ++) {
            monsters.add(placeMonster());
        }

    }


}

