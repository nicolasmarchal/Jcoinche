package SlickClient;

/**
 * Created by marcha_0 on 28/11/16.
 */

import jcoinche.protobuf.protos.Packets;
import jcoinche.protobuf.protos.Packets.Packet;

public class Player {
    private int _nbCard;
    private int _id;

    private CardImage _lastCardPlayed;

    private Packets.Types lastBetType;
    private int lastBetValues;
    private boolean isPassing;
    private boolean isCoinching;
    private boolean isSurCoinching;

    public int get_nbCard() {
        return _nbCard;
    }

    public void delCard()
    {
        _nbCard -= 1;
    }

    public Player()
    {
        _lastCardPlayed = null;
        _nbCard = 8;
        _id = 0;
        lastBetValues = -1;
        isCoinching = false;
        isSurCoinching = false;
        isPassing = false;
    }

    public void cleanBet()
    {
        lastBetType = null;
        lastBetValues = -1;
        isCoinching = false;
        isPassing = false;
        isSurCoinching = false;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setLastBet(Packets.Types lastBetType, int lastBetValues, boolean isPassing, boolean isCoinching, boolean isSurCoinching)
    {
        this.lastBetType = lastBetType;
        this.lastBetValues = lastBetValues;
        this.isCoinching = isCoinching;
        this.isSurCoinching = isSurCoinching;
        this.isPassing = isPassing;
    }

    public Packets.Types getLastBetType() {
        return lastBetType;
    }

    public void set_nbCard(int _nbCard) {
        this._nbCard = _nbCard;
    }

    public void setLastBetType(Packets.Types lastBetType) {
        this.lastBetType = lastBetType;
    }

    public int getLastBetValues() {
        return lastBetValues;
    }

    public void setLastBetValues(int lastBetValues) {
        this.lastBetValues = lastBetValues;
    }

    public boolean isPassing() {
        return isPassing;
    }

    public void setPassing(boolean passing) {
        isPassing = passing;
    }

    public boolean isCoinching() {
        return isCoinching;
    }

    public void setCoinching(boolean coinching) {
        isCoinching = coinching;
    }

    public boolean isSurCoinching() {
        return isSurCoinching;
    }

    public void setSurCoinching(boolean surCoinching) {
        isSurCoinching = surCoinching;
    }

    public CardImage get_lastCardPlayed() {
        return _lastCardPlayed;
    }

    public void set_lastCardPlayed(CardImage _lastCardPlayed) {
        this._lastCardPlayed = _lastCardPlayed;
    }
}
