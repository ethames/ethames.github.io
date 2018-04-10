package byog.Core;
import java.io.Serializable;

public class LeaderBoard implements Serializable {
    private int[] topScores;
    private String[] topScoresString;

    public int[] getTopScores() {
        return topScores;
    }

    public void setTopScores(int i, int score) {
        topScores[i] = score;
    }

    public String[] getTopScoresString() {
        return topScoresString;
    }

    public void setTopScoresString(int i, String score) {
        topScoresString[i] = score;
    }

    public LeaderBoard() {
        this.topScores = new int[3];
        this.topScoresString = new String[3];

        for (int i = 0; i < 3; i++) {
            this.topScores[i] = 0;
            this.topScoresString[i] = "---";
        }
    }
}
