package server;

public class TeamPoints {

    private int turnPoints;
    private int gamePoints;

    public TeamPoints() {
        this.turnPoints = 0;
        this.gamePoints = 0;
    }

    public void addToPointsTurn(int points) {
        this.turnPoints += points;
    }

    public boolean hasMadeHisBet(int points) {
        return turnPoints >= points;
    }

    public void addTurnToGame() {
        this.gamePoints += turnPoints;
    }

    public int getGamePoints() {
        return gamePoints;
    }

    public void setGamePoints(int gamePoints) {
        this.gamePoints = gamePoints;
    }

    public int getTurnPoints() {
        return turnPoints;
    }

    public void setTurnPoints(int turnPoints) {
        this.turnPoints = turnPoints;
    }
}
