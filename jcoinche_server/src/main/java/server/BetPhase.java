package server;

import shared.Enums.Types;
import java.util.Vector;


public class BetPhase {

    private Types type;
    private int points;
    private boolean coinche;
    private boolean surCoinche;
    private Player lastPlayerWhoBet;
    private Player lastPlayerWhoCoinche;
    private Player lastPlayerWhoSurCoinche;
    private int nextPlayerToStart;
    private int indexCurrentPlayer;
    private int hasPassed;
    private String lastErrorOnBet;

    public BetPhase() {
        this.nextPlayerToStart = 3;
    }

    /**
     * Initialize the phase
     */
    public void initPhase() {
        this.type = null;
        this.points = 0;
        this.hasPassed = 0;
        this.nextPlayerToStart += 1;
        this.lastPlayerWhoBet = null;
        if (this.nextPlayerToStart == 4)
            this.nextPlayerToStart = 0;
        this.indexCurrentPlayer = this.nextPlayerToStart;
    }

    /**
     * Test a bet to know if it can be done
     * @param type
     * @param points
     * @param player
     * @param idxPlayer
     * @return
     */
    public boolean tryNewBet(int type, int points, Player player, int idxPlayer) {
        Types[] types = {Types.TREFLE, Types.PIQUE, Types.COEUR, Types.CARREAU, Types.NON_ATOUT, Types.TOUT_ATOUT};

        if (idxPlayer != indexCurrentPlayer) {
            this.lastErrorOnBet = "This is not your turn. Wait the other players\n";
            return false;
        }
        if (this.coinche)
        {
            this.lastErrorOnBet = "An player previously coinche you cannot bet\n. If you want to surcoinche use /surcoinche\n";
            return false;
        }
        if (points > this.points) {
            this.hasPassed = 0;
            this.type = types[type];
            this.points = points;
            this.lastPlayerWhoBet = player;
            this.nextPlayer();
            return true;
        }
        this.lastErrorOnBet = "You tried to bet something lower than the current bet. Use /getcurrentbet to see the last bet\n";
        return false;
    }

    /**
     * Testing if the player can pass
     * @param idxPlayer
     * @return
     */
    public boolean playerWantsToPass(int idxPlayer) {
        if (idxPlayer != indexCurrentPlayer) {
            this.lastErrorOnBet = "This is not your turn. Wait the other players\n";
            return false;
        }
        this.hasPassed += 1;
        this.nextPlayer();
        return true;
    }

    /**
     * Testing if the partner did the last bet
     * @param player
     * @return
     */
    public boolean lastBetIsPartner(Player player)
    {
        if (player.getPartner().equals(this.lastPlayerWhoBet)) {
            return true;
        }
        return false;
    }

    /**
     * Passing to next player
     */
    public void nextPlayer()
    {
        this.indexCurrentPlayer += 1;
        if (this.indexCurrentPlayer == 4)
            this.indexCurrentPlayer = 0;
    }

    public String getType() {
        if (this.type != null)
            return this.type.getName();
        return null;
    }

    /**
     * Get the last bet made
     * @param players
     * @return
     */
    public String returnLastBet(Vector<Player> players)
    {
        String message;

        if (this.getLastPlayerWhoBet() == null) {
            message = "No current bet ! Bets are starting at 80 points on any type\n";
        } else {
            //message = this.lastPlayerWhoBet.getName();
            if (this.getCoinche()) {
                message = this.lastPlayerWhoCoinche.getName() + " coinche on " + this.getPoints() + " " + this.getType();
            } else if (this.getSurCoinche()) {
                message = this.lastPlayerWhoSurCoinche.getName() + " surcoinche on " + this.getPoints() + " " + this.getType();
            } else {
                message = this.lastPlayerWhoBet.getName() + " bet " + this.getPoints() + " " + this.getType();
            }
            message += "\n";
        }
        return message;
    }

    public boolean lastCoincheIsPartner(Player player)
    {
        return player.getPartner().equals(this.lastPlayerWhoCoinche);
    }

    public Types getTypeObject() { return this.type; }

    public int getHasPassed() {
        return hasPassed;
    }

    public void setHasPassed(int i) { this.hasPassed = i; }

    public int getIndexCurrentPlayer() { return this.indexCurrentPlayer; }

    public int getPoints() { return this.points; }

    public void setLastPlayerWhoBet(Player player){ this.lastPlayerWhoBet = player; }

    public Player getLastPlayerWhoBet() { return this.lastPlayerWhoBet; }

    public boolean getCoinche() {
        return this.coinche;
    }

    public boolean getSurCoinche() { return this.surCoinche; }

    public void setCoinche(boolean coinche) {
        this.coinche = coinche;
    }

    public void setSurCoinche(boolean surCoinche) { this.surCoinche = surCoinche; }

    public int getNextPlayerToStart() {
        return this.nextPlayerToStart;
    }

    public String getLastErrorOnBet() { return this.lastErrorOnBet; }

    public void setLastPlayerWhoCoinche(Player lastPlayerWhoCoinche) {
        this.lastPlayerWhoCoinche = lastPlayerWhoCoinche;
    }

    public void setLastPlayerWhoSurCoinche(Player lastPlayerWhoSurCoinche) {
        this.lastPlayerWhoSurCoinche = lastPlayerWhoSurCoinche;
    }

    public Player getLastPlayerWhoCoinche() {
        return lastPlayerWhoCoinche;
    }

    public Player getLastPlayerWhoSurCoinche() {
        return lastPlayerWhoSurCoinche;
    }
}
