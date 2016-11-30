package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jcoinche.protobuf.protos.Packets;
import jcoinche.protobuf.protos.Packets.Bet;
import jcoinche.protobuf.protos.Packets.Packet;
import jcoinche.protobuf.protos.Packets.Command;



public class ServerHandler extends SimpleChannelInboundHandler<Packet> {

    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception
    {
        Game game = Game.getDefaultInstance();

        if (packet.getCommand() == Command.GET_CURRENT_BET){
            if (!game.isPlayPhaseActive() && !game.isBetPhaseActive()) {
                Notifications.sendNoPhaseBegun(ctx.channel(), "Nor bet phase or play phase has begun!\n");
                return;
            }
            Notifications.sendLastBet(ctx.channel(), game.getBetPhase().returnLastBet(game.getPlayers()));
            return;
        }
        if (packet.getCommand() == Command.GET_HAND) {
            if (!game.isPlayPhaseActive() && !game.isBetPhaseActive()) {
                Notifications.sendNoPhaseBegun(ctx.channel(), "Nor bet phase or play phase has begun!\n");
                return;
            }
            Notifications.notifHand(game.getPlayerByChannel(ctx.channel()));
        }
        if (packet.getCommand() == Command.READY) {
            CommandManager.manageReady(ctx, packet, game);
            return;
        }
        if (packet.getCommand() == Command.BET) {
            CommandManager.manageBet(ctx, packet, game);
            return;
        }
        if (packet.getCommand() == Command.HELP){
            CommandManager.manageHelp(ctx, game);
            return;
        }
        if (packet.getCommand() == Command.PLAY_CARD)
        {
            CommandManager.managePlay(ctx, packet, game);
            return;
        }
        if (packet.getCommand() == Command.GET_CARDS_TABLE)
        {
            Notifications.notifCardOnTable(game.getPlayerByChannel(ctx.channel()));
            return;
        }
        if (packet.getCommand() == Command.GET_POINTS)
        {
            Notifications.notifTeamGamePoints(game.getPlayerByChannel(ctx.channel()));
            return;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Player player = new Player(ctx.channel());
        Game.getDefaultInstance().addNewPlayer(player);
        Notifications.notifPlayerJoinGame(player);
        System.out.println("New channel connected !");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        Game.getDefaultInstance().removePlayerByChannel(ctx.channel());
        System.out.println("Channel disconnected !");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
