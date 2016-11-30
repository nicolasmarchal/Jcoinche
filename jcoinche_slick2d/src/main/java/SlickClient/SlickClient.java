package SlickClient;

import client.Client;
import client.ClientHandler;

import client.PacketSender;
import io.netty.channel.ChannelHandlerContext;


import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

import java.util.logging.Logger;


import jcoinche.protobuf.protos.Packets;
import jcoinche.protobuf.protos.Packets.Packet;

/**
 * Created by marcha_0 on 26/11/16.
 */

public class SlickClient extends Client {
    final private int width = 1500;
    final private int  height = 900;

    private Graphic graphic;
    private SlickHandler handler;

    private void gameLoop()
    {
        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(graphic);
            appgc.setDisplayMode(width, height, false);

           // PacketSender.getDefaultInstance().sendReady();
            appgc.start();
        }
        catch (SlickException ex)
        {
            ex.printStackTrace();
        }
    }

    void initHandler(final String ip, final int port)
    {
        handler = new SlickHandler();

        graphic = new Graphic("jcoinche", width, height, new InitCallBack() {
            public void InitFinished() {
                //PacketSender.getDefaultInstance().sendReady();
                //sender.sendReady();
            }
        });
        handler.setGraphic(graphic);
        run(ip, port, handler);
    }

    @Override
    public void readInput() throws Exception {
        gameLoop();
        //super.readInput();
    }

    public static void main(String[] args) {

        System.out.println("Hello I'm the Jcoinche client");

        if (args.length < 2)
        {
            System.out.println("Usage: ./jcoinche_client.jar [ip] [port]");
            return;
        }
        SlickClient slickClient = new SlickClient();
        slickClient.initHandler(args[0], Integer.parseInt(args[1]));
    }
}
