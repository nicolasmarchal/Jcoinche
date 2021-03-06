package client;

import com.google.protobuf.ExtensionRegistry;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import jcoinche.protobuf.protos.Packets;


public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    ClientHandler clientHandler;

    ClientInitializer()
    {
        this.clientHandler = new ClientHandler();
    }


    ClientInitializer(ClientHandler clientHandler)
    {
        this.clientHandler = clientHandler;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception{

        ChannelPipeline pipeline = ch.pipeline();
        ExtensionRegistry registry = ExtensionRegistry.newInstance();

        registry.add(Packets.Bet.bet);
        registry.add(Packets.Card.card);
        registry.add(Packets.Points.points);

        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(Packets.Packet.getDefaultInstance(), registry));

        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast("handler", clientHandler);
    }

}
