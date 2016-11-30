package server;

import io.netty.channel.Channel;
import shared.Card;
import jcoinche.protobuf.protos.Packets;

import java.util.Vector;

public class Notifications {

    /**
     * Send response to a channel
     * @param channel
     * @param message
     */
    public static final void sendResponse(Channel channel, String message) {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.RESPONSE).setMessage(message).build();
        channel.writeAndFlush(packet);
    }

    /**
     * Notify everyone in the game that the bet has begun
     *
     * @param players
     */
    public static final void notifBetPhaseHasBegun(Vector<Player> players) {
        String message;
        for (Player player : players) {
            message = "Bet phase begins ! You are " + player.getName() + " and you're playing with " + player.getPartner().getName() + "\n";
            GraphSender.sendBetPhaseBegins(player);
            GraphSender.sendHand(player);
            sendResponse(player.getChannel(), message);
        }
    }

    /**
     * Notify everyone in the game that a bet was made
     *
     * @param lastBet
     * @param players
     */
    public static final void notifBetWasMade(String lastBet, Vector<Player> players) {
        for (Player player : players) {
            GraphSender.sendBetWasMade(player);
            sendResponse(player.getChannel(), lastBet);
        }
    }

    /**
     * Notif hand to all players in game
     *
     * @param players
     */
    public static final void notifHand(Vector<Player> players) {
        String message;

        for (Player player : players) {
            Notifications.notifHand(player);
        }
    }

    /**
     * Notify players that the player passed
     *
     * @param players
     */
    public static final void notifPass(Player playerWhoPassed, Vector<Player> players) {
        String message = playerWhoPassed.getName() + " pass\n";
        for (Player player : players) {
            GraphSender.sendPass(player, playerWhoPassed);
            sendResponse(player.getChannel(), message);
        }
    }

    /**
     * Announce to the players in the game that the play phase has begun and telling them the last bet
     *
     * @param players
     * @param lastBet
     */
    public static final void notifPlayPhaseHasBegun(Vector<Player> players, String lastBet) {
        String message = "Play phase has begun !\n";

        message += lastBet;
        for (Player player : players) {
            GraphSender.sendPlayBegins(player);
            sendResponse(player.getChannel(), message);
        }
    }

    /**
     * Notify to the players in the game the cards on the table
     *
     * @param players
     */
    public static final void notifCardOnTable(Vector<Player> players) {
        String message = "";

        for (Player player : players) {
            message = "Cards on the table: \n";
            message += Game.getDefaultInstance().getGamePhase().printCardTable();
            sendResponse(player.getChannel(), message);
        }
    }

    /**
     * Notify to all players in the game who won the last turn
     *
     * @param winner
     */
    public static void notifWhoWonGamePhase(Player winner, Vector<Player> players) {
        String message;

        message = winner.getName() + " and his partner " + winner.getPartner().getName() + " won this round\nIf you want to see the scores use /getpoints\n";

        for (Player player : players) {
            sendResponse(player.getChannel(), message);
        }
    }

    /**
     * Notify to all players the scores of the game
     *
     * @param players
     */
    public static void notifTeamGamePoints(Vector<Player> players) {
        String message = Game.getDefaultInstance().getTeamPoints();

        for (Player player : players) {
            notifTeamGamePoints(player);
        }
    }

    /**
     * Notify a player that it's his turn to bet
     *
     * @param player
     */
    public static final void notifIsBetTurn(Player player) {
        Notifications.notifHand(player);
        String message = "This is your turn to bet !\n";
        GraphSender.sendTurnBet(player);
        sendResponse(player.getChannel(), message);
    }

    /**
     * Notif hand to one player
     *
     * @param player
     */
    public static final void notifHand(Player player) {
        String message = "Your hand :\n" + player.getHand().printHand();

        sendResponse(player.getChannel(), message);
    }

    /**
     * Notify to the players in the game that everybody passed
     *
     * @param players
     */
    public static final void notifAllPlayersPassed(Vector<Player> players) {
        String message = "\n\nEverybody passed, the cards have been redistributed\n\n";

        for (Player player : players) {
            sendResponse(player.getChannel(), message);
        }
    }

    /**
     * Notify  to the player the cards on the table
     *
     * @param player
     */
    public static final void notifCardOnTable(Player player) {
        String message = "";

        message = "Cards on the table: \n";
        message += Game.getDefaultInstance().getGamePhase().printCardTable();
        sendResponse(player.getChannel(), message);
    }

    /**
     * Notify to a player the scores of the game
     *
     * @param player
     */
    public static void notifTeamGamePoints(Player player) {
        String message = Game.getDefaultInstance().getTeamPoints();
        GraphSender.sendTeamPoints(player);
        sendResponse(player.getChannel(), message);
    }

    /**
     * Notify which team won the card turn to all players
     *
     * @param whoWon
     * @param players
     */
    public static void notifWhoWonTurn(Player whoWon, Vector<Player> players) {
        String message = "Team " + whoWon.getName() + " - " + whoWon.getPartner().getName() + " won this turn\n";

        for (Player player : players) {
            GraphSender.sendWinTurn(player, whoWon);
            sendResponse(player.getChannel(), message);
        }
    }

    /**
     * Calls the notify functions for each beginning of bet phase
     */
    public static void onBetPhaseBegins() {
        Game game = Game.getDefaultInstance();
        Notifications.notifHand(game.getPlayers());
        Notifications.notifBetPhaseHasBegun(game.getPlayers());
        Notifications.notifIsBetTurn(game.getPlayers().elementAt(game.getBetPhase().getIndexCurrentPlayer()));
    }

    /**
     * Notify to all players that a player has played a card
     * @param players
     * @param card
     * @param playerWhoPlayed
     */
    public static void notifCardHasBeenPlayed(Vector<Player> players, Card card, Player playerWhoPlayed) {
        String message = playerWhoPlayed.getName() + " played a " + card.getName() + " de " + card.getTypeName() + "\n";

        for (Player player : players) {
            GraphSender.sendPlayCard(player, card, playerWhoPlayed);
            sendResponse(player.getChannel(), message);
        }
    }

    /**
     * Notify a player that it is his turn to play
     * @param player
     */
    public static void notifIsPlayTurn(Player player) {
        Notifications.notifHand(player);
        String message = "This is your turn to play!\n";
        GraphSender.sendTurnPlay(player);
        sendResponse(player.getChannel(), message);
    }

    /**
     * Notify to all players who won and the points of this turn
     * @param players
     */
    public static void notifTeamTurnPoints(Vector<Player> players) {
        String message = Game.getDefaultInstance().getTurnPoints();

        for (Player player : players) {
            sendResponse(player.getChannel(), message);
        }
    }

    /**
     * Notify to all players who won the game
     * @param winner
     * @param players
     */
    public static void notifWhoWonTheGame(Player winner, Vector<Player> players) {

        String message = "Team " + winner.getName() + " - " + winner.getPartner().getName() + " won this game\n";

        for (Player player : players) {
            sendResponse(player.getChannel(), message);
        }
    }

    /**
     * Notify to player that he can't be ready when bet or game phase have begun
     * @param channel
     * @param s
     */
    public static void sendBetOrGamePhaseHaveBegun(Channel channel, String s) {
        sendResponse(channel, s);
    }

    /**
     * Notify to player that he is already ready
     * @param channel
     * @param s
     */
    public static void sendAlreadyReady(Channel channel, String s) {
        sendResponse(channel, s);
    }

    /**
     * Notify to player how many people are recquired to begin the game
     * @param channel
     * @param s
     */
    public static void sendWaitingForPlayers(Channel channel, String s) {
        GraphSender.sendResponseGraph(channel, s);
        sendResponse(channel, s);
    }

    /**
     * Notify to player that the bet phase is not active yet
     * @param channel
     * @param s
     */
    public static void sendBetPhaseNotActive(Channel channel, String s) {
        sendResponse(channel, s);
    }

    /**
     * Notify to player that he can't coinche on his partner
     * @param channel
     * @param s
     */
    public static void sendCannotCoinchePartner(Channel channel, String s) {
        GraphSender.sendBetError(channel, s);
        sendResponse(channel, s);
    }

    /**
     * Notify to player that he can't coinche when no bet was made before
     * @param channel
     * @param s
     */
    public static void sendNoBetMadeBefore(Channel channel, String s) {
        GraphSender.sendBetError(channel, s);
        sendResponse(channel, s);
    }

    /**
     * Notify to player that he can't surcoinche on his partner
     * @param channel
     * @param s
     */
    public static void sendCannotSurcoincheOnPartner(Channel channel, String s) {
        GraphSender.sendBetError(channel, s);
        sendResponse(channel, s);
    }

    /**
     * Notify to player that he cant't coinche on a coinche
     * @param channel
     * @param s
     */
    public static void sendCannotCoincheOnCoinche(Channel channel, String s) {
        GraphSender.sendBetError(channel, s);
        sendResponse(channel, s);
    }

    /**
     * Notify to player that he can't surcoinche when no coinche was made before
     * @param channel
     * @param s
     */
    public static void sendCannotSurcoincheWhenNoCoinche(Channel channel, String s) {
        GraphSender.sendBetError(channel, s);
        sendResponse(channel, s);
    }

    /**
     * Notify to player the last error on his bet
     * @param channel
     * @param lastErrorOnBet
     */
    public static void sendLastErrorOnBet(Channel channel, String lastErrorOnBet) {
        GraphSender.sendBetError(channel, lastErrorOnBet);
        sendResponse(channel, lastErrorOnBet);
    }

    /**
     * Notify to player that the play phase is not active
     * @param channel
     * @param s
     */
    public static void sendPlayPhaseNotActive(Channel channel, String s) {
        //GraphSender.sendResponseGraph(channel, s);
        sendResponse(channel, s);
    }

    /**
     * Notify to player that the card he want to play is not in his hand
     * @param channel
     * @param s
     */
    public static void sendNoCardWithIndex(Channel channel, String s) {
        sendResponse(channel, s);
    }

    /**
     * Notify to player that it's not his turn
     * @param channel
     * @param s
     */
    public static void sendNotYourTurn(Channel channel, String s) {
        //GraphSender.sendResponseGraph(channel, s);
        sendResponse(channel, s);
    }

    /**
     * Notify to player the last error on his playcard
     * @param channel
     * @param lastErrorAtPlaying
     */
    public static void sendLastErrorOnPlay(Channel channel, String lastErrorAtPlaying) {
        GraphSender.sendPlayError(channel, lastErrorAtPlaying);
        sendResponse(channel, lastErrorAtPlaying);
    }

    /**
     * Notify to player that both either bet or play phase are not active
     * @param channel
     * @param s
     */
    public static void sendNoPhaseBegun(Channel channel, String s) {
        sendResponse(channel, s);
    }

    /**
     * Notify to player the last bet made
     * @param channel
     * @param s
     */
    public static void sendLastBet(Channel channel, String s) {
        sendResponse(channel, s);
    }

    /**
     * Notify players how many people are missing to begin the game
     * @param players
     */
    public static void notifWaitingPlayers(Vector<Player> players) {
        String message = "Waiting for " + Game.getDefaultInstance().howManyPlayersToBegin() + " more players to begin\n";

        for (Player player : players) {
            GraphSender.sendWhoAmI(player);
            GraphSender.sendResponseGraph(player.getChannel(), message);
            sendResponse(player.getChannel(), message);
        }
    }

    public static void notifPlayerLeave(Player player) {
        String message = "A player leave the game, we're relaunching the game please use /help to get some help\n";

        sendResponse(player.getChannel(), message);
    }

    public static void notifPlayerJoinGame(Player player) {
        String message = "You join the game ! Use /help to see what you can do\n";

        sendResponse(player.getChannel(), message);
    }
}
