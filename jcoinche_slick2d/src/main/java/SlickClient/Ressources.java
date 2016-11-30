package SlickClient;

import java.util.ArrayList;
import java.util.List;

import jcoinche.protobuf.protos.Packets;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by marcha_0 on 29/11/16.
 */
public class Ressources {

    static public List<TypesImage> typesImages = new ArrayList<TypesImage>();
    static public List<CardImage> cardImages = new ArrayList<CardImage>();
    static public List<ValuesImage> valuesImages = new ArrayList<ValuesImage>();
    static public Image surCoinche;
    static public Image coinche;
    static public Image pass;
    static public Image play;
    static public Image back;
    static public Image cardSelected;


    public static void init() throws SlickException {
        typesImages.add(new TypesImage(new Image("button_trefle.png"),Packets.Types.TREFLE));
        typesImages.add(new TypesImage(new Image("button_pique.png"), Packets.Types.PIQUE));
        typesImages.add(new TypesImage(new Image("button_carreau.png"), Packets.Types.CARREAU));
        typesImages.add(new TypesImage(new Image("button_coeur.png"), Packets.Types.COEUR));
        typesImages.add(new TypesImage(new Image("button.png"), Packets.Types.TOUT_ATOUT));
        typesImages.add(new TypesImage(new Image("button_sans.png"), Packets.Types.NON_ATOUT));
        back = new Image("deck_4.png");

        valuesImages.add(new ValuesImage(new Image("button_80.png"), 80));
        valuesImages.add(new ValuesImage(new Image("button_90.png"), 90));
        valuesImages.add(new ValuesImage(new Image("button_100.png"), 100));
        valuesImages.add(new ValuesImage(new Image("button_110.png"), 110));
        valuesImages.add(new ValuesImage(new Image("button_120.png"), 120));
        valuesImages.add(new ValuesImage(new Image("button_130.png"), 130));
        valuesImages.add(new ValuesImage(new Image("button_140.png"), 140));
        valuesImages.add(new ValuesImage(new Image("button_150.png"), 150));
        valuesImages.add(new ValuesImage(new Image("button_160.png"), 160));

        cardImages.add(new CardImage(new Image("c8.png"), Packets.Types.TREFLE, Packets.Values.HUIT));
        cardImages.add(new CardImage(new Image("c7.png"), Packets.Types.TREFLE, Packets.Values.SEPT));

        cardImages.add(new CardImage(new Image("c9.png"), Packets.Types.TREFLE, Packets.Values.NEUF));
        cardImages.add(new CardImage(new Image("c10.png"), Packets.Types.TREFLE, Packets.Values.DIX));
        cardImages.add(new CardImage(new Image("cj.png"), Packets.Types.TREFLE, Packets.Values.VALET));
        cardImages.add(new CardImage(new Image("cq.png"), Packets.Types.TREFLE, Packets.Values.DAME));

        cardImages.add(new CardImage(new Image("ck.png"), Packets.Types.TREFLE, Packets.Values.ROI));
        cardImages.add(new CardImage(new Image("ca.png"), Packets.Types.TREFLE, Packets.Values.AS));

        cardImages.add(new CardImage(new Image("d7.png"), Packets.Types.CARREAU, Packets.Values.SEPT));
        cardImages.add(new CardImage(new Image("d8.png"), Packets.Types.CARREAU, Packets.Values.HUIT));
        cardImages.add(new CardImage(new Image("d9.png"), Packets.Types.CARREAU, Packets.Values.NEUF));
        cardImages.add(new CardImage(new Image("d10.png"), Packets.Types.CARREAU, Packets.Values.DIX));
        cardImages.add(new CardImage(new Image("dj.png"), Packets.Types.CARREAU, Packets.Values.VALET));
        cardImages.add(new CardImage(new Image("dq.png"), Packets.Types.CARREAU, Packets.Values.DAME));
        cardImages.add(new CardImage(new Image("dk.png"), Packets.Types.CARREAU, Packets.Values.ROI));
        cardImages.add(new CardImage(new Image("da.png"), Packets.Types.CARREAU, Packets.Values.AS));

        cardImages.add(new CardImage(new Image("h7.png"), Packets.Types.COEUR, Packets.Values.SEPT));
        cardImages.add(new CardImage(new Image("h8.png"), Packets.Types.COEUR, Packets.Values.HUIT));
        cardImages.add(new CardImage(new Image("h9.png"), Packets.Types.COEUR, Packets.Values.NEUF));
        cardImages.add(new CardImage(new Image("h10.png"), Packets.Types.COEUR, Packets.Values.DIX));
        cardImages.add(new CardImage(new Image("hj.png"), Packets.Types.COEUR, Packets.Values.VALET));
        cardImages.add(new CardImage(new Image("hq.png"), Packets.Types.COEUR, Packets.Values.DAME));
        cardImages.add(new CardImage(new Image("hk.png"), Packets.Types.COEUR, Packets.Values.ROI));
        cardImages.add(new CardImage(new Image("ha.png"), Packets.Types.COEUR, Packets.Values.AS));

        cardImages.add(new CardImage(new Image("s7.png"), Packets.Types.PIQUE, Packets.Values.SEPT));
        cardImages.add(new CardImage(new Image("s8.png"), Packets.Types.PIQUE, Packets.Values.HUIT));
        cardImages.add(new CardImage(new Image("s9.png"), Packets.Types.PIQUE, Packets.Values.NEUF));
        cardImages.add(new CardImage(new Image("s10.png"), Packets.Types.PIQUE, Packets.Values.DIX));
        cardImages.add(new CardImage(new Image("sj.png"), Packets.Types.PIQUE, Packets.Values.VALET));
        cardImages.add(new CardImage(new Image("sq.png"), Packets.Types.PIQUE, Packets.Values.DAME));
        cardImages.add(new CardImage(new Image("sk.png"), Packets.Types.PIQUE, Packets.Values.ROI));
        cardImages.add(new CardImage(new Image("sa.png"), Packets.Types.PIQUE, Packets.Values.AS));
        surCoinche = new Image("button_surcoinche.png");
        coinche = new Image("button_coinche.png");
        pass = new Image("button_pass.png");
        cardSelected = new Image("card_selected.png");
        play = new Image("button_play.png");
    }

    static public TypesImage getTypeImage(Packets.Types type)
    {
        for (TypesImage typesImage : typesImages)
        {
            if (typesImage.getType() == type)
                return typesImage;
        }
        return null;
    }

    public static ValuesImage getValuesImage(int value)
    {
        for (ValuesImage valuesImage : valuesImages)
        {
            if (valuesImage.values == value)
            {
                return valuesImage;
            }
        }
        return null;
    }

    public static CardImage getCardInDeck(Packets.Types type, Packets.Values num)
    {
        for (CardImage card : cardImages)
        {
            if (card.getType() == type && card.getValue() == num)
                return card;
        }
        return null;
    }


}
