package server;

import io.netty.channel.Channel;
import shared.Card;
import jcoinche.protobuf.protos.Packets;
import shared.Enums.Types;

import java.util.Vector;

public class GraphSender {

    public static void sendResponseGraph(Channel channel, String s) {

        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.RESPONSE_GRAPH).setMessage(s).build();

        channel.writeAndFlush(packet);
    }

    public static void sendWhoAmI(Player player) {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.WHO_AM_I).setMessage(player.getName()).build();

        player.getChannel().writeAndFlush(packet);
    }

    public static void sendHand(Player player)
    {
        Vector<Card> cards = player.getHand().getCards();
        Packets.Card cardPacket;
        Packets.Packet packet;
        int idxCard;
        int type;
        int value;
        Packets.Values[] values = {Packets.Values.SEPT, Packets.Values.HUIT, Packets.Values.NEUF, Packets.Values.DIX, Packets.Values.VALET, Packets.Values.DAME, Packets.Values.ROI, Packets.Values.AS };
        Packets.Types[] types = {Packets.Types.TREFLE, Packets.Types.PIQUE, Packets.Types.COEUR, Packets.Types.CARREAU };

        for (Card card : cards) {
            idxCard = cards.indexOf(card);
            type = card.getType();
            value = card.getId();
            cardPacket = Packets.Card.newBuilder().setCardNumber(idxCard).setType(types[type]).setValue(values[value]).build();
            packet = Packets.Packet.newBuilder().setCommand(Packets.Command.HAND).setExtension(Packets.Card.card, cardPacket).build();
            player.getChannel().writeAndFlush(packet);
        }
    }

    public static void sendTurnBet(Player player)
    {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.BET_TURN).build();
        player.getChannel().writeAndFlush(packet);
    }

    public static void sendTurnPlay(Player player)
    {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.PLAY_TURN).build();
        player.getChannel().writeAndFlush(packet);
    }

    public static void sendBetPhaseBegins(Player player)
    {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.BET_BEGIN).build();
        player.getChannel().writeAndFlush(packet);
    }

    public static void sendBetWasMade(Player player) {
        BetPhase betphase = Game.getDefaultInstance().getBetPhase();

        int type = betphase.getTypeObject().getType();
        int points = betphase.getPoints();
        boolean coinche = betphase.getCoinche();
        boolean surcoinche = betphase.getSurCoinche();
        String playerName;
        Packets.Types[] types = {Packets.Types.TREFLE, Packets.Types.PIQUE, Packets.Types.COEUR, Packets.Types.CARREAU, Packets.Types.NON_ATOUT, Packets.Types.TOUT_ATOUT };

        if (betphase.getSurCoinche())
            playerName = betphase.getLastPlayerWhoSurCoinche().getName();
        else if (betphase.getCoinche())
            playerName = betphase.getLastPlayerWhoCoinche().getName();
        else
            playerName = betphase.getLastPlayerWhoBet().getName();

        Packets.Bet bet = Packets.Bet.newBuilder().setType(types[type]).setPoints(points).setIsCoinche(coinche).setIsSurCoinche(surcoinche).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.BET_SUCCESS).setExtension(Packets.Bet.bet, bet).setMessage(playerName).build();
        player.getChannel().writeAndFlush(packet);
    }

    public static void sendBetError(Channel channel, String error) {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.BET_ERROR).setMessage(error).build();
        channel.writeAndFlush(packet);
    }

    public static void sendTeamPoints(Player player) {

        Player player1 = Game.getDefaultInstance().getPlayers().get(0);
        Player player2 = Game.getDefaultInstance().getPlayers().get(1);
        String teamName1 = player1.getName() + "-" + player1.getPartner().getName();
        String teamName2 = player2.getName() + "-" + player2.getPartner().getName();
        int pointsTeam1 = player1.getPoints().getGamePoints();
        int pointsTeam2 = player2.getPoints().getGamePoints();

        Packets.Points points = Packets.Points.newBuilder().setTeamName1(teamName1).setTeamName2(teamName2).setPointsTeam1(pointsTeam1).setPointsTeam2(pointsTeam2).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.GAME_POINTS).setExtension(Packets.Points.points, points).build();
        player.getChannel().writeAndFlush(packet);
    }

    public static void sendPass(Player player, Player playerWhoPassed) {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.BET_PASS).setMessage(playerWhoPassed.getName()).build();

        player.getChannel().writeAndFlush(packet);
    }

    public static void sendPlayBegins(Player player) {
        BetPhase betphase = Game.getDefaultInstance().getBetPhase();

        int type = betphase.getTypeObject().getType();
        int points = betphase.getPoints();
        boolean coinche = betphase.getCoinche();
        boolean surcoinche = betphase.getSurCoinche();
        String playerName;
        Packets.Types[] types = { Packets.Types.TREFLE, Packets.Types.PIQUE, Packets.Types.COEUR, Packets.Types.CARREAU, Packets.Types.NON_ATOUT, Packets.Types.TOUT_ATOUT };

        if (betphase.getSurCoinche())
            playerName = betphase.getLastPlayerWhoSurCoinche().getName();
        else if (betphase.getCoinche())
            playerName = betphase.getLastPlayerWhoCoinche().getName();
        else
            playerName = betphase.getLastPlayerWhoBet().getName();

        Packets.Bet bet = Packets.Bet.newBuilder().setType(types[type]).setPoints(points).setIsCoinche(coinche).setIsSurCoinche(surcoinche).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.PLAY_BEGIN).setExtension(Packets.Bet.bet, bet).setMessage(playerName).build();
        player.getChannel().writeAndFlush(packet);
    }

    public static void sendPlayCard(Player player, Card card, Player playerName)
    {
        Packets.Values[] values = {Packets.Values.SEPT, Packets.Values.HUIT, Packets.Values.NEUF, Packets.Values.DIX, Packets.Values.VALET, Packets.Values.DAME, Packets.Values.ROI, Packets.Values.AS };
        Packets.Types[] types = {Packets.Types.TREFLE, Packets.Types.PIQUE, Packets.Types.COEUR, Packets.Types.CARREAU };

        Packets.Types type = types[card.getType()];
        Packets.Values value = values[card.getId()];

        Packets.Card cardObject = Packets.Card.newBuilder().setType(type).setValue(value).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.PLAY_CARD).setExtension(Packets.Card.card, cardObject).setMessage(playerName.getName()).build();

        player.getChannel().writeAndFlush(packet);
    }

    public static void sendPlayError(Channel channel, String error) {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.PLAY_ERROR).setMessage(error).build();

        channel.writeAndFlush(packet);
    }

    public static void sendWinTurn(Player player, Player whoWon) {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.TURN_WIN).setMessage(whoWon.getName()).build();

        player.getChannel().writeAndFlush(packet);
    }
}
