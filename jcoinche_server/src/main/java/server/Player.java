package server;

import io.netty.channel.Channel;
import shared.Hand;

public class Player {

    private Hand hand;
    private boolean isReady;
    private Channel channel;
    private Player partner;
    private TeamPoints points;
    private String name;

    public Player(Channel channel){

        this.channel = channel;
        this.hand = new Hand();
        isReady = false;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public Channel getChannel() {
        return channel;
    }

    public Hand getHand() { return this.hand; }

    public Player getPartner() {
        return partner;
    }

    public void setPartner(Player partner) {
        this.partner = partner;
    }

    public void setPoints(TeamPoints points) { this.points = points; }

    public TeamPoints getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
