package server;

import io.netty.channel.Channel;
import shared.Card;
import shared.Enums.Types;
import shared.Hand;
import java.util.Vector;
import jcoinche.protobuf.protos.Packets;

public class Game {

    private static Game DEFAULT_INSTANCE = new Game();
    private Vector<Player> players;
    private Vector<Player> waiters;
    private Deck deck;
    private BetPhase betPhase;
    private GamePhase gamePhase;
    private boolean betPhaseActive;
    private boolean playPhaseActive;

    private Game() {
        this.players = new Vector<Player>();
        this.waiters = new Vector<Player>();
        this.betPhase = new BetPhase();
        this.gamePhase = new GamePhase();
        this.deck = new Deck();
        this.betPhaseActive = false;
        this.playPhaseActive = false;
    }

    public void init()
    {
        this.betPhase = new BetPhase();
        this.gamePhase = new GamePhase();
        this.deck = new Deck();
        this.betPhaseActive = false;
        this.playPhaseActive = false;
    }

    /**
     * Adding new player to the game
     * @param player
     */
    public void addNewPlayer(Player player) {
        if (this.players.size() >= 4) {
            this.waiters.add(player);
            return;
        }
        player.setName("Player " + (this.players.size() + 1));
        this.players.add(player);
    }

    /**
     * Updating all the cards to the atout decided in bet phase
     * @param atout
     */
    private void assignAtout(Types atout) {
        for (Player player : players) {
            for (Card card : player.getHand().getCards()) {
                if (card.getType() == atout.getType())
                    card.changeCardToAtout();
            }
        }
    }

    /**
     * Updating the cards to play without atout
     */
    private void assignSansAtout(){
        for (Player player : players) {
            for (Card card : player.getHand().getCards()) {
                card.changeCardSansAtout();
            }
        }
    }

    /**
     * Updating the cards to play in all atout
     */
    private void assignToutAtout() {
        for (Player player : players) {
            for (Card card : player.getHand().getCards()) {
                card.changeCardToutAtout();
            }
        }
    }

    /**
     * Resetting the card to their initial values
     */
    private void assignNonAtout() {
        for (Player player : players) {
            for (Card card : player.getHand().getCards()) {
                card.changeCardNonAtout();
            }
        }
    }

    /**
     * Launching game phase
     */
    public void startGamePhase() {
        this.gamePhase.init(this.betPhase.getTypeObject(), this.betPhase.getNextPlayerToStart());
        this.betPhaseActive = false;
        this.playPhaseActive = true;
        if (this.betPhase.getTypeObject() == Types.NON_ATOUT)
            this.assignSansAtout();
        else if (this.betPhase.getTypeObject() == Types.TOUT_ATOUT)
            this.assignToutAtout();
        else
            this.assignAtout(this.betPhase.getTypeObject());

    }

    /**
     * Give three cards to a player
     * @param hand
     */
    private void pushThreeCards(Hand hand) {
        for (int i = 0; i < 3; ++i) {
            hand.addCard(this.deck.getFirstCard());
        }
    }

    /**
     * Give two cards to a player
     * @param hand
     */
    private void pushTwoCards(Hand hand) {
        for (int i = 0; i < 2; ++i) {
            hand.addCard(this.deck.getFirstCard());
        }
    }

    /**
     * Distribute the cards between the players
     */
    public void distributeCards() {
        this.betPhase.initPhase();
        this.playPhaseActive = false;
        this.betPhaseActive = true;
        int idxFirstPlayer = this.betPhase.getNextPlayerToStart();

        for (int i = 0; i < 3; ++i) {

            for (int j = 0; j < 4; ++j) {
                Hand hand = this.players.get(idxFirstPlayer).getHand();
                if (i % 2 == 0)
                    pushThreeCards(hand);
                else
                    pushTwoCards(hand);
                ++idxFirstPlayer;
                if (idxFirstPlayer == 4)
                    idxFirstPlayer = 0;
            }
        }
        this.deck.cut();
        this.assignNonAtout();
        Notifications.onBetPhaseBegins();
    }

    /**
     * Making the teams fot the game
     */
    public void assignTeams() {
        TeamPoints pointsTeam1 = new TeamPoints();
        TeamPoints pointsTeam2 = new TeamPoints();

        this.players.get(0).setPartner(this.players.get(2));
        this.players.get(2).setPartner(this.players.get(0));
        this.players.get(0).setPoints(pointsTeam1);
        this.players.get(2).setPoints(pointsTeam1);
        this.players.get(1).setPartner(this.players.get(3));
        this.players.get(3).setPartner(this.players.get(1));
        this.players.get(1).setPoints(pointsTeam2);
        this.players.get(3).setPoints(pointsTeam2);

    }

    /**
     * Checking all the players are ready
     * @return
     */
    public boolean playersAreReady() {
        if (this.players.size() != 4)
            return false;
        for (Player player : players) {
            if (!player.isReady()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a player by his channel
     * @param channel
     * @return
     */
    public Player getPlayerByChannel(Channel channel) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getChannel().equals(channel))
                return players.get(i);
        }
        System.err.println("No player with this channel !");
        return null;
    }

    /**
     * Pushing all the cards in the players's hand to the deck
     */
    public void pushAllCardsInDeck() {
        for (Player player : this.players) {
            Vector<Card> cards = player.getHand().getCards();
            this.deck.pushCardsToDeck(cards);
            cards.clear();
        }
    }


    /**
     * Calculating the bonus points and adding them plus the turn points to the game points
     */
    public void addTurnAndBonusPointsToGame() {

        int bonusPoints = this.betPhase.getPoints();

        TeamPoints team1 = this.players.get(0).getPoints();
        TeamPoints team2 = this.players.get(1).getPoints();

        if (this.betPhase.getLastPlayerWhoBet().getPoints().hasMadeHisBet(this.betPhase.getPoints())) {
            if (this.betPhase.getSurCoinche())
                bonusPoints *= 3;
            else if (this.betPhase.getCoinche())
                bonusPoints *= 2;
            if (this.betPhase.getLastPlayerWhoBet().getPoints().equals(team1))
                team1.addToPointsTurn(bonusPoints);
            else
                team2.addToPointsTurn(bonusPoints);
        } else {
            this.betPhase.getLastPlayerWhoBet().getPoints().setTurnPoints(0);
            if (this.betPhase.getSurCoinche())
                bonusPoints *= 3;
            else if (this.betPhase.getCoinche())
                bonusPoints *= 2;
            if (!this.betPhase.getLastPlayerWhoBet().getPoints().equals(team1)) {
                team1.setTurnPoints(160);
                team1.addToPointsTurn(bonusPoints);
            }
            else {
                team2.setTurnPoints(160);
                team2.addToPointsTurn(bonusPoints);
            }
        }
        team1.addTurnToGame();
        team2.addTurnToGame();
        team1.setTurnPoints(0);
        team2.setTurnPoints(0);
    }

    /**
     * Ending game phase;
     */
    public void endGamePhase() {
        Player player;

        if (this.betPhase.getLastPlayerWhoBet().getPoints().hasMadeHisBet(this.betPhase.getPoints())) {
            player = this.betPhase.getLastPlayerWhoBet();
        } else {
            int idx  = this.players.indexOf(this.betPhase.getLastPlayerWhoBet()) + 1;
            if (idx == 4)
                idx = 0;
            player = this.players.get(idx);
        }
        Notifications.notifWhoWonGamePhase(player, this.players);
        Notifications.notifTeamTurnPoints(this.players);
        this.addTurnAndBonusPointsToGame();
        Notifications.notifTeamGamePoints(this.players);

        TeamPoints team1 = this.players.get(0).getPoints();
        TeamPoints team2 = this.players.get(1).getPoints();
        if (team1.getGamePoints() >= 3000 && team1.getGamePoints() > team2.getGamePoints()) {
            Notifications.notifWhoWonTheGame(this.players.get(0), this.players);
        } else if (team2.getGamePoints() >= 3000 && team2.getGamePoints() > team1.getGamePoints()) {
            Notifications.notifWhoWonTheGame(this.players.get(1), this.players);
        }

        this.distributeCards();
    }

    /**
     * Getting the points at the end of a turn
     * @return
     */
    public String getTurnPoints() {

        String message = "";
        TeamPoints team1 = this.players.get(0).getPoints();
        TeamPoints team2 = this.players.get(1).getPoints();

        message += "Points for this turn are : \n";
        message += "Team " + this.players.get(0).getName() + " - " + this.players.get(0).getPartner().getName() + ": " + team1.getTurnPoints() + "\n";
        message += "Team " + this.players.get(1).getName() + " - " + this.players.get(1).getPartner().getName() + ": " + team2.getTurnPoints() + "\n";

        return message;
    }

    /**
     * Getting the points for the whole game
     * @return
     */
    public String getTeamPoints() {

        String message = "";
        TeamPoints team1 = this.players.get(0).getPoints();
        TeamPoints team2 = this.players.get(1).getPoints();

        message += "Game points: \n";
        message += "Team " + this.players.get(0).getName() + " - " + this.players.get(0).getPartner().getName() + ": " + team1.getGamePoints() + "\n";
        message += "Team " + this.players.get(1).getName() + " - " + this.players.get(1).getPartner().getName() + ": " + team2.getGamePoints() + "\n";

        return message;
    }

    /**
     * Getting the help of the current phase
     * @return
     */
    public String getHelpForCurrentPhase() {
        String message;
        if (!isBetPhaseActive() && !isPlayPhaseActive()) {
            message = "Commands in startup phase:\n\n" +
                    "/ready                 Announce that you are ready\n";
        } else if (isBetPhaseActive()) {
            message = "Commands in bet phase:\n\n" +
                    "/bet type points       Bet by announcing the type and the points\n" +
                    "/coinche               Announce that you want to coinche the current bet\n" +
                    "/surcoinche            Announce that you want to surcoinche the previous coinche\n" +
                    "/gethand               See the cards in your hand\n" +
                    "/getcurrentbet         Get the current bet\n" +
                    "/getpoints             See your team points for the game\n";
        } else {
            message = "Commands in play phase:\n\n" +
                    "/playcard idxCard      Play a card\n" +
                    "/getcardstable         Get every cards on the table\n" +
                    "/getcurrentbet         Get the current bet for the turn\n" +
                    "/gethand               See the cards in your hand\n" +
                    "/getpoints             See your team points for the game\n";
        }
        message +=  "/help                  Get some help on the current phase\n" +
                    "/quit                  Quit the game\n";
        return message;
    }

    /**
     * Getting the number of players missing to begin the game
     * @return
     */
    public int howManyPlayersToBegin() {
        int playersReady = 0;

        for (Player player : this.players) {
            if (player.isReady())
                ++playersReady;
        }
        return 4 - playersReady;
    }

    /**
     * Removing a player by his channel
     * @param channel
     */
    public void removePlayerByChannel(Channel channel) {
        System.out.println("Prepare to remove player");
        Player playerToRemove = null;
        for (Player player : this.players) {
            if (player.getChannel().equals(channel)) {
                playerToRemove = player;
            }
        }

        if (playerToRemove != null) {
            this.players.remove(playerToRemove);
        }

        if (this.players.size() != 4)
        {
            System.out.println("Size players != 4");
            for (Player player : this.players)
            {
                Notifications.notifPlayerLeave(player);
                player.setReady(false);
            }
            this.init();
            if (this.waiters.size() != 0) {
                Player playerToAdd = this.waiters.get(0);
                Notifications.notifPlayerJoinGame(playerToAdd);
                this.addNewPlayer(playerToAdd);
                this.waiters.remove(playerToAdd);
            }
        }

        for (Player player : this.waiters) {
            if (player.getChannel().equals(channel)) {
                this.waiters.remove(player);
                return;
            }
        }

    }

    public Deck getDeck() {
        return deck;
    }

    public Vector<Player> getPlayers() {
        return this.players;
    }

    public int getIndexPlayer(Player player) {
        return this.players.indexOf(player);
    }

    public boolean isBetPhaseActive() {
        return betPhaseActive;
    }

    public void setBetPhaseActive(boolean betPhaseActive) {
        this.betPhaseActive = betPhaseActive;
    }

    public boolean isPlayPhaseActive() {
        return playPhaseActive;
    }

    public void setPlayPhaseActive(boolean playPhaseActive) {
        this.playPhaseActive = playPhaseActive;
    }

    public static Game getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public BetPhase getBetPhase() {
        return betPhase;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }
}
