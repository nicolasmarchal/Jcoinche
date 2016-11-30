package SlickClient;

import client.ClientHandler;
import client.PacketSender;
import io.netty.channel.ChannelHandlerContext;


import jcoinche.protobuf.protos.Packets;
import jcoinche.protobuf.protos.Packets.Packet;
import org.lwjgl.Sys;

/**
 * Created by marcha_0 on 28/11/16.
 */
public class SlickHandler extends ClientHandler {

    private Graphic graphic;


    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
       // super.channelRead0(ctx, packet);
//        if (packet.getCommand() == Packets.Command.RESPONSE) {
//            System.out.print(packet.getMessage());
//            return;
//        }
//
        if (packet.getCommand() == Packets.Command.HAND)
        {
           Packets.Card card = packet.getExtension(Packets.Card.card);
            if (card.getCardNumber() == 0) {
                graphic.getGraphPlayer().setNbCard(8);
                this.graphic.getMyHand().getHand().clear();
            }
            this.graphic.addCardToHand(card.getCardNumber(), card.getType(), card.getValue());
            return;
        }

        if (packet.getCommand() == Packets.Command.HELP) {
            System.out.print(packet.getMessage());
            return;
        }

        if (packet.getCommand() == Packets.Command.WHO_AM_I)
        {
            String namePlayer = packet.getMessage();
            this.graphic.setPlayer(namePlayer);
        }

        if (packet.getCommand() == Packets.Command.BET_BEGIN)
        {
            this.graphic.getMyHand().set_phase(MyHand.BET_PHASE);
            this.graphic.getGraphPlayer().set_phase(MyHand.BET_PHASE);
        }

        if (packet.getCommand() == Packets.Command.BET_TURN)
        {
            this.graphic.getBetButtons().setTurn(true);
        }

        if (packet.getCommand() == Packets.Command.BET_ERROR)
        {
            this.graphic.getMessage().set_errorMsg(packet.getMessage());
            this.graphic.getBetButtons().setTurn(true);
        }

        if (packet.getCommand() == Packets.Command.BET_SUCCESS)
        {
            this.graphic.getMessage().set_errorMsg(null);
            String playerName = packet.getMessage();
            Packets.Bet bet = packet.getExtension(Packets.Bet.bet);

            Player player = this.graphic.getGraphPlayer().getPlayer(playerName.charAt(playerName.length() - 1) - 48);
            if (player != null)
                player.setLastBet(bet.getType(), bet.getPoints(), false, bet.getIsCoinche(), bet.getIsSurCoinche());
            else
                this.graphic.getMyHand().get_player().setLastBet(bet.getType(), bet.getPoints(), false, bet.getIsCoinche(), bet.getIsSurCoinche());

        }
        if (packet.getCommand() == Packets.Command.BET_PASS)
        {
            String playerName = packet.getMessage();
            Player player = this.graphic.getGraphPlayer().getPlayer(playerName.charAt(playerName.length() - 1) - 48);
            if (player != null)
                player.setLastBet(Packets.Types.NON_ATOUT, -1, true, false, false);
            else
                this.graphic.getMyHand().get_player().setLastBet(Packets.Types.NON_ATOUT, -1, true, false, false);
        }

        if (packet.getCommand() == Packets.Command.PLAY_TURN)
        {
            this.graphic.getMyHand().set_turn(true);
        }

        if (packet.getCommand() == Packets.Command.PLAY_CARD)
        {

            String playerName = packet.getMessage();
            Packets.Card card = packet.getExtension(Packets.Card.card);

            this.graphic.getMessage().set_errorMsg(null);
            this.graphic.getMessage().set_turnWin(null);
            Player player = this.graphic.getGraphPlayer().getPlayer(playerName.charAt(playerName.length() - 1) - 48);
            if (player != null)
            {
                player.set_lastCardPlayed(Ressources.getCardInDeck(card.getType(), card.getValue()));
                player.delCard();
            }
            else {
                this.graphic.getMyHand().get_player().set_lastCardPlayed(Ressources.getCardInDeck(card.getType(), card.getValue()));
                this.graphic.getMyHand().delCard();
            }
        }

        if (packet.getCommand() == Packets.Command.PLAY_ERROR)
        {
            this.graphic.getMessage().set_errorMsg(packet.getMessage());
            this.graphic.getMyHand().set_turn(true);
            this.graphic.getMyHand().setSelected(-1);
        }

        if (packet.getCommand() == Packets.Command.TURN_WIN)
        {
            String playerName = packet.getMessage();
            this.graphic.getMessage().set_turnWin(playerName + " Win the turn !");
            this.graphic.getGraphPlayer().cleanCard();
            this.graphic.getMyHand().get_player().set_lastCardPlayed(null);
        }


        if (packet.getCommand() == Packets.Command.PLAY_BEGIN)
        {
            graphic.getGraphPlayer().cleanBet();
            graphic.getMyHand().get_player().cleanBet();
            String playerName = packet.getMessage();
            Packets.Bet bet = packet.getExtension(Packets.Bet.bet);
            GameBet gameBet = this.graphic.get_gameBet();
            gameBet.setGambeler(playerName);
            gameBet.setType(bet.getType());
            gameBet.setValue(bet.getPoints());
            gameBet.setCoinche(bet.getIsCoinche());
            gameBet.setSurCoinche(bet.getIsSurCoinche());
            this.graphic.getGraphPlayer().set_phase(MyHand.PLAY_PHASE);
            this.graphic.getMyHand().set_phase(MyHand.PLAY_PHASE);
        }

        if (packet.getCommand() == Packets.Command.GAME_POINTS) {

            Packets.Points points = packet.getExtension(Packets.Points.points);

            String teamName1 = points.getTeamName1();
            String teamName2 = points.getTeamName2();
            int pointsTeam1 = points.getPointsTeam1();
            int pointsTeam2 = points.getPointsTeam2();
            System.out.println(teamName1 + teamName2 + pointsTeam1 + pointsTeam2);
            graphic.getMessage().setGamePoint(teamName1, teamName2, pointsTeam1, pointsTeam2);

            this.graphic.getGraphPlayer().cleanCard();
            this.graphic.getMyHand().get_player().set_lastCardPlayed(null);
        }

    }

    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
    }
}
