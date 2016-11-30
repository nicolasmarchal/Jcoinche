package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import jcoinche.protobuf.protos.Packets.Packet;
import jcoinche.protobuf.protos.Packets.Command;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client
{
    private String ip;
    private int port;
    protected Channel channel;
    private BufferedReader in;
    private boolean isConnected;

    public Client()
    {
        this.in = new BufferedReader(new InputStreamReader(System.in));
        this.isConnected = false;
    }

    public void run(String ip, int port, ClientHandler handler)
    {
        this.ip = ip;
        this.port = port;

        EventLoopGroup group = new NioEventLoopGroup();
        ClientInitializer clientInitializer;

        try {
            Bootstrap b = new Bootstrap();

            if (handler != null)
                clientInitializer = new ClientInitializer(handler);
            else
                clientInitializer = new ClientInitializer();

            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(clientInitializer);
            try {
                this.channel = b.connect(ip, port).sync().channel();
                PacketSender.getDefaultInstance().setChannel(this.channel);
                this.isConnected = true;
                System.out.println("Connected to server!");
                this.readInput();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public void run(String ip, int port)
    {
        run(ip, port, null);
    }


    public void handleCommand(String command)
    {
        String[] splited = command.split(" ");
        if (splited[0].equalsIgnoreCase("/ready"))
        {
            Packet packet = Packet.newBuilder().setCommand(Command.READY).build();
            channel.writeAndFlush(packet);

        } else if (splited[0].equalsIgnoreCase("/help")){
            PacketSender.getDefaultInstance().sendHelp();
        } else if (splited[0].equalsIgnoreCase("/bet")) {
            PacketSender.getDefaultInstance().sendBet(splited);
        } else if (splited[0].equalsIgnoreCase("/quit")) {
            this.channel.close();
            this.isConnected = false;
        } else if (splited[0].equalsIgnoreCase("/coinche")) {
            PacketSender.getDefaultInstance().sendBetCoinche();
        } else if (splited[0].equalsIgnoreCase("/surcoinche")) {
            PacketSender.getDefaultInstance().sendBetSurCoinche();
        } else if (splited[0].equalsIgnoreCase("/pass")) {
            PacketSender.getDefaultInstance().sendBetPass();
        } else if (splited[0].equalsIgnoreCase("/getcurrentbet")) {
            PacketSender.getDefaultInstance().sendGetCurrentBet();
        } else if (splited[0].equalsIgnoreCase("/gethand")) {
            PacketSender.getDefaultInstance().sendGetHand();
        } else if (splited[0].equalsIgnoreCase("/playcard")){
            PacketSender.getDefaultInstance().sendPlayCard(splited);
        } else if (splited[0].equalsIgnoreCase("/getcardstable")) {
            PacketSender.getDefaultInstance().getCardsTable();
        } else if (splited[0].equalsIgnoreCase("/getpoints")) {
            PacketSender.getDefaultInstance().getPoints();
        }
        else {
            System.err.println("jcoinche: command not found: " + command + " use /help to view commands");
        }
    }

    public void readInput() throws Exception
    {
        while (isConnected)
        {
            String line = this.in.readLine();
            handleCommand(line);
        }
    }

    public static void main(String[] args) {

        System.out.println("Hello I'm the Jcoinche client 2");

        if (args.length < 2)
        {
            System.out.println("Usage: ./jcoinche_client.jar [ip] [port]");
            return;
        }

        Client client = new Client();

        client.run(args[0], Integer.parseInt(args[1]));
    }
}
