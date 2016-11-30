package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jcoinche.protobuf.protos.Packets;
import jcoinche.protobuf.protos.Packets.Packet;

public class ClientHandler extends SimpleChannelInboundHandler<Packet> {

    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {

        if (packet.getCommand() == Packets.Command.RESPONSE) {
            System.out.print(packet.getMessage());
            return;
        }

        if (packet.getCommand() == Packets.Command.DISPLAY_CARD) {
            Packets.Card card = packet.getExtension(Packets.Card.card);

            System.out.println(card.getValue().name() + " de " + card.getType().name());
            return;
        }

        if (packet.getCommand() == Packets.Command.HELP) {
            System.out.print(packet.getMessage());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.err.printf("Closing client ...");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}