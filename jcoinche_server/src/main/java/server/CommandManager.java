package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import jcoinche.protobuf.protos.Packets;
import jcoinche.protobuf.protos.Packets.Bet;
import jcoinche.protobuf.protos.Packets.Packet;
import jcoinche.protobuf.protos.Packets.Command;


public class CommandManager {

    public static final void manageReady(ChannelHandlerContext ctx, Packet packet, Game game)
    {
        if (game.getPlayerByChannel(ctx.channel()) == null)
        {
            manageHelp(ctx, game);
            return;
        }
        if (game.isBetPhaseActive() || game.isPlayPhaseActive())
        {
            Notifications.sendBetOrGamePhaseHaveBegun(ctx.channel(), "The bet/game phase has already begun !\n");
            return;
        }
        if (game.getPlayerByChannel(ctx.channel()).isReady())
        {
            Notifications.sendAlreadyReady(ctx.channel(), "You are already ready. Waiting for " + game.howManyPlayersToBegin() + " more players to begin\n");
            return;
        }
        game.getPlayerByChannel(ctx.channel()).setReady(true);
        if (game.playersAreReady()) {
            game.assignTeams();
            game.distributeCards();
        }
        else
            Notifications.notifWaitingPlayers(Game.getDefaultInstance().getPlayers());

        return;
    }

    public static final void manageBet(ChannelHandlerContext ctx, Packet packet, Game game)
    {
        if (!game.isBetPhaseActive())
        {
            Notifications.sendBetPhaseNotActive(ctx.channel(), "The bet phase is not active\n");
            return;
        }

        Packets.Bet bet = packet.getExtension(Bet.bet);

        if (bet.getIsCoinche()) {
            if (game.getBetPhase().lastBetIsPartner(game.getPlayerByChannel(ctx.channel())))
            {
                Notifications.sendCannotCoinchePartner(ctx.channel(), "You cannot coinche on your partner !\n");
                return;
            }
            if (game.getBetPhase().getType() == null) {
                Notifications.sendNoBetMadeBefore(ctx.channel(), "No bet was made before, you cannot coinche !\n");
                return;
            }
            if (!game.getBetPhase().getCoinche()) {
                game.getBetPhase().setCoinche(true);
                game.getBetPhase().setLastPlayerWhoCoinche(game.getPlayerByChannel(ctx.channel()));
                game.getBetPhase().nextPlayer();
                game.getBetPhase().setHasPassed(0);
                Notifications.notifBetWasMade(game.getBetPhase().returnLastBet(game.getPlayers()), game.getPlayers());
                Notifications.notifIsBetTurn(game.getPlayers().elementAt(game.getBetPhase().getIndexCurrentPlayer()));
                return;
            }
            Notifications.sendCannotCoincheOnCoinche(ctx.channel(), "If you want to surcoinche, please type /surcoinche !\n");
        } else if (bet.getIsSurCoinche()) {
            if (game.getBetPhase().lastCoincheIsPartner(game.getPlayerByChannel(ctx.channel())))
            {
                Notifications.sendCannotSurcoincheOnPartner(ctx.channel(), "You cannot surcoinche on your partner !\n");
                return;
            }
            if (game.getBetPhase().getCoinche()){
                game.getBetPhase().setCoinche(false);
                game.getBetPhase().setSurCoinche(true);
                game.getBetPhase().setLastPlayerWhoSurCoinche(game.getPlayerByChannel(ctx.channel()));
                game.getBetPhase().nextPlayer();
                Notifications.notifBetWasMade(game.getBetPhase().returnLastBet(game.getPlayers()), game.getPlayers());
                Notifications.notifIsBetTurn(game.getPlayers().elementAt(game.getBetPhase().getIndexCurrentPlayer()));
                game.startGamePhase();
                Notifications.notifPlayPhaseHasBegun(game.getPlayers(), game.getBetPhase().returnLastBet(game.getPlayers()));
                return;
            }
            Notifications.sendCannotSurcoincheWhenNoCoinche(ctx.channel(), "You cannot surcoinche when no coinche was made before !\n");
        } else if (bet.getIsPassing()) {
            if (!game.getBetPhase().playerWantsToPass(game.getIndexPlayer(game.getPlayerByChannel(ctx.channel())))) {
                Notifications.sendLastErrorOnBet(ctx.channel(), game.getBetPhase().getLastErrorOnBet());
                return;
            }
            if (game.getBetPhase().getHasPassed() == 3 && game.getBetPhase().getLastPlayerWhoBet() != null) {
                game.startGamePhase();
                Notifications.notifPlayPhaseHasBegun(game.getPlayers(), game.getBetPhase().returnLastBet(game.getPlayers()));
                return;
            }

            if (game.getBetPhase().getHasPassed() == 4) {
                Notifications.notifAllPlayersPassed(game.getPlayers());
                game.pushAllCardsInDeck();
                game.distributeCards();
                return;
            }
            int idexBoundException = game.getBetPhase().getIndexCurrentPlayer() - 1;
            if (idexBoundException == -1)
                idexBoundException = 3;
            Notifications.notifPass(game.getPlayers().get(idexBoundException), game.getPlayers());
            Notifications.notifIsBetTurn(game.getPlayers().elementAt(game.getBetPhase().getIndexCurrentPlayer()));
        }
        else {
            if (!game.getBetPhase().tryNewBet(bet.getType().getNumber(), bet.getPoints(), game.getPlayerByChannel(ctx.channel()), game.getIndexPlayer(game.getPlayerByChannel(ctx.channel())))) {
                Notifications.sendLastErrorOnBet(ctx.channel(), game.getBetPhase().getLastErrorOnBet());
                return;
            }
            Notifications.notifBetWasMade(game.getBetPhase().returnLastBet(game.getPlayers()), game.getPlayers());
            Notifications.notifIsBetTurn(game.getPlayers().elementAt(game.getBetPhase().getIndexCurrentPlayer()));
        }
    }

    public static final void manageHelp(ChannelHandlerContext ctx, Game game)
    {
        Packets.Packet packet;

        if (game.getPlayerByChannel(ctx.channel()) == null)
        {
            String help = "You are currently waiting to join the game, while no player leave the current you cannot join, you'll be informed when you will join the game\n" +
                    "Commands in waiting phase: \n" +
                    "/help          Get some help about the current phase\n" +
                    "/quit          Quit the client\n";
            packet = Packets.Packet.newBuilder().setCommand(Packets.Command.HELP).setMessage(help).build();
        } else {
            packet = Packets.Packet.newBuilder().setCommand(Packets.Command.HELP).setMessage(game.getHelpForCurrentPhase()).build();
        }
        ctx.writeAndFlush(packet);
    }

    public static final void managePlay(ChannelHandlerContext ctx, Packet packet, Game game) {

        if (!game.isPlayPhaseActive()) {
            Notifications.sendPlayPhaseNotActive(ctx.channel(), "The play phase is not active\n");
            return;
        }

        int cardNumber = packet.getExtension(Packets.Card.card).getCardNumber();
        if (cardNumber >= game.getPlayerByChannel(ctx.channel()).getHand().getCards().size()) {
            Notifications.sendNoCardWithIndex(ctx.channel(), "There is no card in your hand with the index " + cardNumber + "\n");
            return;
        }

        if (!game.getGamePhase().isYourTurn(game.getPlayers().indexOf(game.getPlayerByChannel(ctx.channel())))) {
            Notifications.sendNotYourTurn(ctx.channel(), "This is not your turn. Wait the other players\n");
            return;
        }

        if (game.getGamePhase().tryPlayCard(game.getPlayerByChannel(ctx.channel()), game.getPlayerByChannel(ctx.channel()).getHand().getCards().get(cardNumber))) {
            return;
        }
        Notifications.sendLastErrorOnPlay(ctx.channel(), game.getGamePhase().getLastErrorAtPlaying());
    }
}
