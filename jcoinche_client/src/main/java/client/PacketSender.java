package client;

import com.google.protobuf.ExtensionRegistry;
import io.netty.channel.Channel;
import io.netty.channel.socket.InternetProtocolFamily;
import jcoinche.protobuf.protos.Packets;

public class PacketSender {

    private Channel channel;
    private static PacketSender DEFAULT_INSTANCE = new PacketSender();

    public static PacketSender getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }


    public void sendReady()
    {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.READY).build();
        this.channel.writeAndFlush(packet);
    }

    private Packets.Types isTypeIsValid(String type) {
        if (type == null)
            return null;
        if (type.equalsIgnoreCase("trefle"))
            return Packets.Types.TREFLE;
        if (type.equalsIgnoreCase("pique"))
            return Packets.Types.PIQUE;
        if (type.equalsIgnoreCase("coeur"))
            return Packets.Types.COEUR;
        if (type.equalsIgnoreCase("carreau"))
            return Packets.Types.CARREAU;
        if (type.equalsIgnoreCase("sans-atout"))
            return Packets.Types.NON_ATOUT;
        if (type.equalsIgnoreCase("tout-atout"))
            return Packets.Types.TOUT_ATOUT;
        return null;
    }

    public void sendGraphBet(Packets.Types type, int points)
    {
        Packets.Bet bet = Packets.Bet.newBuilder().setPoints(points).setType(type).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.BET).setExtension(Packets.Bet.bet, bet).build();

        this.channel.writeAndFlush(packet);
    }

    public void sendBet(String[] splited)
    {
        Packets.Types newType;
        int newPoints;

        if (splited.length < 3)
        {
            System.out.print("Missing an argument ! /bet type points\n");
            return;
        }

        if ((newType = isTypeIsValid(splited[1])) == null)
        {
            System.out.print("/bet: type is invalid\nCandidates are: trefle / pique / coeur / carreau / sans-atout / tout-/atout\n");
            return;
        }

        try {
            newPoints = Integer.parseInt(splited[2]);
        } catch (Exception e) {
            System.out.println("/bet: second argument must be a number");
            return;
        }

        if (newPoints < 80 || newPoints > 160 || newPoints % 10 != 0) {
            System.out.print("/bet: second argument must be (80, 90, 100, 110, 120, 130, 140, 150, 160)\n");
            return;
        }

        Packets.Bet bet = Packets.Bet.newBuilder().setPoints(newPoints).setType(newType).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.BET).setExtension(Packets.Bet.bet, bet).build();

        this.channel.writeAndFlush(packet);
    }

    public void sendHelp()
    {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.HELP).build();
        this.channel.writeAndFlush(packet);
    }

    public void sendBetPass()
    {
        Packets.Bet bet = Packets.Bet.newBuilder().setIsPassing(true).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.BET).setExtension(Packets.Bet.bet, bet).build();

        this.channel.writeAndFlush(packet);
    }

    public void sendBetCoinche()
    {
        Packets.Bet bet = Packets.Bet.newBuilder().setIsCoinche(true).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.BET).setExtension(Packets.Bet.bet, bet).build();

        this.channel.writeAndFlush(packet);

    }

    public void sendBetSurCoinche()
    {
        Packets.Bet bet = Packets.Bet.newBuilder().setIsSurCoinche(true).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.BET).setExtension(Packets.Bet.bet, bet).build();

        this.channel.writeAndFlush(packet);
    }

    public void sendGetCurrentBet()
    {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.GET_CURRENT_BET).build();

        this.channel.writeAndFlush(packet);
    }

    public void sendGetHand()
    {
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.GET_HAND).build();

        this.channel.writeAndFlush(packet);
    }

    public void sendPlayCardGraph(int idx)
    {
        Packets.Card card = Packets.Card.newBuilder().setCardNumber(idx).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.PLAY_CARD).setExtension(Packets.Card.card, card).build();
        this.channel.writeAndFlush(packet);
    }

    public void sendPlayCard(String[] splited) {
        if (splited.length < 2) {
            System.out.print("Missing an argument ! /playcard idxCard\n");
            return;
        }

        int idxCard;

        try {
            idxCard = Integer.parseInt(splited[1]);
        } catch (Exception e) {
            System.out.print("/playcard: first argument must be a number\n");
            return;
        }

        Packets.Card card = Packets.Card.newBuilder().setCardNumber(idxCard).build();
        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.PLAY_CARD).setExtension(Packets.Card.card, card).build();

        this.channel.writeAndFlush(packet);
    }

    public void getCardsTable() {

        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.GET_CARDS_TABLE).build();

        this.channel.writeAndFlush(packet);
    }

    public void getPoints() {

        Packets.Packet packet = Packets.Packet.newBuilder().setCommand(Packets.Command.GET_POINTS).build();

        this.channel.writeAndFlush(packet);
    }
}
