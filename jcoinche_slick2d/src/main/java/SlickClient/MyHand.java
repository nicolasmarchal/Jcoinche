package SlickClient;

import client.PacketSender;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcha_0 on 28/11/16.
 */
public class MyHand {

    static public final int BET_PHASE = 1;
    static public final int PLAY_PHASE = 2;
    static public final int NONE_PHASE = 3;

    private List<CardImage> _hand;
    private Image _cardSelected;
    private int _preSelected;
    private int _selected;
    private int _x;
    private int _y;
    private TrueTypeFont _ttf;
    private Button _play;

    boolean _turn;
    int _phase;

    private final int _spaceBetweenCard = 50;
    private String player;

    Player _player;

    public MyHand() throws SlickException {
        _hand = new ArrayList<CardImage>();
        _cardSelected = Ressources.cardSelected;
        _preSelected = -1;
        _selected = -1;
        _x = 0;
        _y = 650;
        _ttf = new TrueTypeFont(Fonts.font, true);
        player = "";
        _turn = false;
        _phase = NONE_PHASE;
        _play = new Button(Ressources.play);
        _play.setX(1050);
        _play.setY(710);

        _player = new Player();
    }

    public void addCard(int index, CardImage card) {
        _hand.add(index, card);
    }

    public List<CardImage> getHand()
    {
        return _hand;
    }

    public Image getCardSelected() {
        return _cardSelected;
    }

    public void setCardSelected(Image _cardSelected) {
        this._cardSelected = _cardSelected;
    }

    public int getPreSelected() {
        return _preSelected;
    }

    public void setPreSelected(int posX, int posY) {
        if (!_turn || _phase != PLAY_PHASE)
            return;
        if ((posX >= _x && posX <= (this._spaceBetweenCard * this._hand.size() + (_cardSelected.getWidth() - this._spaceBetweenCard)) + _x)
                && posY >= _y && posY <= _y + _cardSelected.getHeight()) {
            _preSelected = (posX - _x) / this._spaceBetweenCard;
            if (posX - _x >= this._spaceBetweenCard * this._hand.size())
                _preSelected = this._hand.size() - 1;
        }
        else
            _preSelected = -1;

        _play.onMouse(posX, posY);
    }

    public void setX(int screenWidth)
    {
        _x = screenWidth / 2 - (this._spaceBetweenCard * this._hand.size() + (_cardSelected.getWidth() - this._spaceBetweenCard)) / 2;
    }

    public void setY(int screenHeight)
    {

    }

    public void drawHand() {

        int i = 0;
        int x, y;
        x = _x;
        y = _y;
        for (CardImage card : this._hand)
        {
            if (_preSelected == i || _selected == i) {
                card.getImage().draw(x, y -20, 1f);
                _cardSelected.draw(x, y - 20, 1f);
            }
            else
                card.getImage().draw(x, y, 1f);

            x += this._spaceBetweenCard;
            ++i;
        }
        _ttf.drawString(_x , _y + _cardSelected.getHeight() + 10, player , Color.white);


        if (_phase == PLAY_PHASE) {
            if (_turn)
                _play.draw();

            if (_player.get_lastCardPlayed() != null)
            {
                _player.get_lastCardPlayed().getImage().draw(700,450,0.8f);
                //PRINT CARD
            }
            return;
        }
        if (!_player.isSurCoinching() && !_player.isCoinching() && _player.getLastBetValues() != -1 && !_player.isPassing())
        {
            Ressources.getTypeImage(_player.getLastBetType()).getImage().draw(690, 400, 0.7f);
            Ressources.getValuesImage(_player.getLastBetValues()).getImage().draw(750, 402, 0.9f);
        }
        else if (_player.isPassing())
            Ressources.pass.draw(690, 400, 0.7f);
        else if (_player.isSurCoinching())
            Ressources.surCoinche.draw(690, 400, 0.7f);
        else if (_player.isCoinching())
            Ressources.coinche.draw(690, 400, 0.7f);
    }

    public void setSelected(int posX, int posY) {

        if (_phase != PLAY_PHASE || !_turn)
            return;
        _play.setOnClick(posX, posY);
        if (_play.isClicked() && _selected != -1)
        {
            PacketSender.getDefaultInstance().sendPlayCardGraph(_selected);
            _play.setClick(false);
            _turn = false;
        }
        if ((posX >= _x && posX <= (this._spaceBetweenCard * this._hand.size() + (_cardSelected.getWidth() - this._spaceBetweenCard)) + _x)
                && posY >= _y && posY <= _y + _cardSelected.getHeight()) {

            int tmp = (posX - _x) / this._spaceBetweenCard;
            if (posX - _x >= this._spaceBetweenCard * this._hand.size())
                tmp = this._hand.size() - 1;
            if (tmp == _selected)
                _selected = -1;
            else
                _selected = tmp;
        }

    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Player get_player() {
        return _player;
    }

    public void set_phase(int _phase) {
        this._phase = _phase;
    }

    public int get_phase() {
        return _phase;
    }

    public void delCard() {
        _hand.remove(_selected);
        _selected = -1;
    }

    public void set_turn(boolean _turn) {
        this._turn = _turn;
    }

    public void setSelected(int i) {
        _selected = i;
    }
}
