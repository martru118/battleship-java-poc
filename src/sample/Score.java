package sample;

public class Score {
    private String player;
    private int gameScore;

    public Score(String player, int gameScore) {
        this.player = player;
        this.gameScore = gameScore;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }

    @Override
    public String toString() {
        return player + ',' + gameScore + '\n';
    }
}
