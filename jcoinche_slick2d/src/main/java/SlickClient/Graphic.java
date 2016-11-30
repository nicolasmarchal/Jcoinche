package SlickClient;

import client.PacketSender;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;

import jcoinche.protobuf.protos.Packets;
import jcoinche.protobuf.protos.Packets.Packet;

/**
 * Created by marcha_0 on 27/11/16.
 */
public class Graphic extends BasicGame{

    /**
     * Create a new basic game
     *
     * @param title The title for the game
     */

    private MyHand _graphHand;
    private GraphPlayers _graphPlayer;
    private BetButtons _betButtons;
    private GameBet _gameBet;
    private Button _ready;
    private boolean isReady;
    private Message message;

    private int _width;
    private  int _height;

    private InitCallBack _initCallback;


    public GraphPlayers getGraphPlayer()
    {
        return _graphPlayer;
    }

    public MyHand getMyHand()
    {
        return _graphHand;
    }

    public BetButtons getBetButtons()
    {
        return _betButtons;
    }

    public Graphic(String title, int width, int height, InitCallBack initCallBack) {
        super(title);
        _width = width;
        _height = height;
        _initCallback = initCallBack;
    }

    public void addCardToHand(int index, Packets.Types type, Packets.Values num)
    {

        CardImage card = Ressources.getCardInDeck(type, num);
        _graphHand.addCard(index, card);
    }




    public void init(GameContainer container) throws SlickException {

        Ressources.init();

        _graphHand = new MyHand();
        message = new Message();
        _betButtons = new BetButtons();
        _graphPlayer = new GraphPlayers();
        _gameBet = new GameBet();
        _ready = new Button(Ressources.play);
        _ready.setX(_width / 2 - _ready.getImage().getWidth() / 2);
        _ready.setY(_height / 2 - _ready.getImage().getHeight() /2);
        isReady = false;

        _initCallback.InitFinished();
    }

    private boolean mouseLeft;

    public void update(GameContainer container, int delta) throws SlickException {
        int posX = Mouse.getX();
        int posY = _height - Mouse.getY();

        if (!isReady)
        _ready.onMouse(posX, posY);

        _graphHand.setPreSelected(posX, posY);
        _betButtons.checkMouse(posX, posY);

        _graphHand.setX(_width);
        if (container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
            mouseLeft = true;
        else
        {
            if (mouseLeft) {

                if (!isReady)
                {
                    _ready.setOnClick(posX, posY);
                    if (_ready.isClicked())
                    {
                        PacketSender.getDefaultInstance().sendReady();
                        isReady = true;
                        _ready.setClick(false);
                    }
                }
                else {
                    _graphHand.setSelected(posX, posY);
                    _betButtons.onClick(posX, posY);
                }
            }
            mouseLeft = false;
        }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {

        g.setBackground(new Color(0x212a31));
        if (!isReady) {
            _ready.draw();
            return;
        }

        _graphHand.drawHand();
        _graphPlayer.drawPlayerCard(_width, _height);
        if (_graphHand.get_phase() == MyHand.BET_PHASE)
            _betButtons.drawButtons();
        if (_graphHand.get_phase() == MyHand.PLAY_PHASE)
            _gameBet.draw();
        message.draw();

    }

    public void setPlayer(String player) {
        _graphPlayer.setPlayer(player);
        _graphHand.setPlayer(player);
    }

    public GameBet get_gameBet() {
        return _gameBet;
    }

    public Message getMessage() {
        return message;
    }
}
