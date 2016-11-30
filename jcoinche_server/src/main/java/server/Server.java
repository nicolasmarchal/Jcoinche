package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap b;
    private ServerInit init;
    private int port;

    public Server(int port)
    {
        this.port = port;
        this.init = new ServerInit();
    }

    public void run()
    {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            b = new ServerBootstrap();
            b.group(this.bossGroup, this.workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(init);
            b.bind(this.port).sync().channel().closeFuture().sync();
        } catch (Exception e){
            System.out.print(e.getMessage());
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args)
    {
        System.out.println("Welcome to the jcoinche server !");
        Server server = new Server(4242);
        server.run();
    }
}
