package SlickClient;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import java.util.ArrayList;
import java.util.List;
import jcoinche.protobuf.protos.Packets;


/**
 * Created by marcha_0 on 28/11/16.
 */
public class GraphPlayers {


    List<Player> players;

    private TrueTypeFont _ttf;
    private Image _card;
    private int _phase;
    private final int _spaceBetweenCard = 30;


    public void set_phase(int _phase) {
        this._phase = _phase;
    }

    public GraphPlayers() throws SlickException {
        _card = Ressources.back;
        players = new ArrayList<Player>();
        players.add(new Player());
        players.add(new Player());
        players.add(new Player());
        _phase = MyHand.NONE_PHASE;
        _ttf = new TrueTypeFont(Fonts.font, true);

    }

    private void printPlayer1(int width, int height)
    {
        int x, y;

        Player player = this.players.get(0);

        y = height / 2 - (this._spaceBetweenCard * player.get_nbCard() + (_card.getWidth() - this._spaceBetweenCard)) / 2;
        x = 150;
        _ttf.drawString(x - 50, y, "player " + player.get_id(), Color.white);
        _card.rotate(90);
        for (int i = 0 ; i < player.get_nbCard() ; ++i)
        {
            _card.draw(x, y, 0.7f);
            y += _spaceBetweenCard;
        }
        _card.rotate(-90);

        if (_phase == MyHand.PLAY_PHASE) {
            if (player.get_lastCardPlayed() != null)
            {
                player.get_lastCardPlayed().getImage().rotate(90);
                player.get_lastCardPlayed().getImage().draw(350,350,0.8f);
                player.get_lastCardPlayed().getImage().rotate(-90);

                //PRINT CARD
            }
            return;
        }
        if (player.getLastBetValues() != -1 && !player.isCoinching() && !player.isSurCoinching()) {
            Ressources.getTypeImage(player.getLastBetType()).getImage().draw(340, 400, 0.7f);
            Ressources.getValuesImage(player.getLastBetValues()).getImage().draw(342, 460, 0.9f);
            return;
        }
        if (player.isPassing())
            Ressources.pass.draw(340, 400, 0.7f);
        if (player.isSurCoinching())
            Ressources.surCoinche.draw(340, 400, 0.7f);
        if (player.isCoinching())
            Ressources.coinche.draw(340, 400, 0.7f);
    }

    private void printPlayer2(int width, int height)
    {
        int x, y;
        Player player = this.players.get(1);

        y = 250 - _card.getHeight();
        x = width / 2 - (this._spaceBetweenCard * player.get_nbCard() + (_card.getWidth() - this._spaceBetweenCard)) / 2;
        _ttf.drawString(x, y - 20, "player " + player.get_id(), Color.white);

        for (int i = 0 ; i < player.get_nbCard() ; ++i)
        {
            _card.draw(x, y, 0.7f);
            x += _spaceBetweenCard;
        }

        if (_phase == MyHand.PLAY_PHASE) {

            if (player.get_lastCardPlayed() != null)
            {
                player.get_lastCardPlayed().getImage().draw(700,200,0.8f);
                //PRINT CARD
            }
            return;
        }
        if (player.getLastBetValues() != -1 && !player.isCoinching() && !player.isSurCoinching()) {
            Ressources.getTypeImage(player.getLastBetType()).getImage().draw(690, 200, 0.7f);
            Ressources.getValuesImage(player.getLastBetValues()).getImage().draw(750, 202, 0.9f);
            return;
        }
        if (player.isPassing())
            Ressources.pass.draw(690, 200, 0.7f);
        if (player.isSurCoinching())
            Ressources.surCoinche.draw(690, 200, 0.7f);
        if (player.isCoinching())
            Ressources.coinche.draw(690, 200, 0.7f);
    }

    private void printPlayer3(int width, int height)
    {
        int x, y;

        Player player = this.players.get(2);
        y = height / 2 - (this._spaceBetweenCard * player.get_nbCard() + (_card.getHeight() - this._spaceBetweenCard)) / 2;
        x = 1350 - _card.getWidth();
        _ttf.drawString(x + _card.getWidth(), y, "player " + player.get_id(), Color.white);

        _card.rotate(90);
        _card.rotate(180);
        for (int i = 0 ; i < player.get_nbCard() ; ++i)
        {
            _card.draw(x, y, 0.7f);
            y += _spaceBetweenCard;
        }
        _card.rotate(-180);
        _card.rotate(-90);
        if (_phase == MyHand.PLAY_PHASE) {

            if (player.get_lastCardPlayed() != null)
            {
                player.get_lastCardPlayed().getImage().rotate(-90);
                player.get_lastCardPlayed().getImage().draw(1030,350,0.8f);
                player.get_lastCardPlayed().getImage().rotate(90);
                //PRINT CARD
            }
            return;
        }
        if (player.getLastBetValues() != -1 && !player.isCoinching() && !player.isSurCoinching()) {
            Ressources.getTypeImage(player.getLastBetType()).getImage().draw(1100, 400, 0.7f);
            Ressources.getValuesImage(player.getLastBetValues()).getImage().draw(1102, 460, 0.9f);
            return;
        }
        if (player.isPassing())
            Ressources.pass.draw(1050, 400, 0.7f);
        if (player.isSurCoinching())
            Ressources.surCoinche.draw(1050, 400, 0.7f);
        if (player.isCoinching())
            Ressources.coinche.draw(1050, 400, 0.7f);
    }

    public void drawPlayerCard(int width, int height)
    {
        printPlayer1(width, height);

        printPlayer2(width, height);
        printPlayer3(width, height);

    }

    public void setPlayer(String player) {

        int idx = player.charAt(player.length() - 1) - 48;
        System.out.println(idx);
        int i = 0;
        while (i < 3)
        {
            if (idx + 1 > 4)
                idx = 0;
            players.get(i).set_id(++idx);
            ++i;
        }
    }

    public Player getPlayer(int idx)
    {
        for (Player player : players)
        {
            if (player.get_id() == idx)
                return player;
        }
        return null;
    }

    public void cleanBet()
    {
        for (Player player : players)
        {
            player.cleanBet();
        }

    }

    public void cleanCard() {
        for (Player player : players)
        {
            player.set_lastCardPlayed(null);
        }
    }

    public void setNbCard(int nbCard) {
        for (Player player : players)
        {
            player.set_nbCard(nbCard);
        }
    }
}
