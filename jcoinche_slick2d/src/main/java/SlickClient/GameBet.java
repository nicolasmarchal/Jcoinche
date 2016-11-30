package SlickClient;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import jcoinche.protobuf.protos.Packets;
import org.newdawn.slick.TrueTypeFont;


/**
 * Created by marcha_0 on 29/11/16.
 */

public class GameBet {
    private Image typeImg;
    private Image valueImg;
    private Image coincheImg;
    private Image surCoincheImg;
    private TrueTypeFont _ttf;


    private Packets.Types type;
    private int value;
    private boolean coinche;
    private boolean surCoinche;
    private String gambeler;

    public GameBet() {
        typeImg = null;
        valueImg = null;
        coinche = false;
        surCoinche = false;
        coincheImg = Ressources.coinche;
        surCoincheImg = Ressources.surCoinche;
        _ttf = new TrueTypeFont(Fonts.font, true);

    }

    public void draw() {
        if (typeImg != null && valueImg != null) {
            typeImg.draw(10, 100);
            valueImg.draw(100, 103);
            _ttf.drawString(10, 50, gambeler, Color.white);
        }
        if (surCoinche) {
            surCoincheImg.draw(200, 100);
        } else if (coinche) {
            coincheImg.draw(200, 100);
        }
    }

    public Packets.Types getType() {
        return type;
    }

    public void setType(Packets.Types type) {
        typeImg = Ressources.getTypeImage(type).getImage();
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        valueImg = Ressources.getValuesImage(value).getImage();
        this.value = value;
    }

    public void setCoinche(boolean coinche) {
        this.coinche = coinche;
    }

    public void setSurCoinche(boolean surCoinche) {
        this.surCoinche = surCoinche;
    }

    public String getGambeler() {
        return gambeler;
    }

    public void setGambeler(String gambeler) {
        this.gambeler = gambeler;
    }
}