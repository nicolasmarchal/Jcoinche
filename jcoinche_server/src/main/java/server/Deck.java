package server;


import io.netty.util.internal.ThreadLocalRandom;
import shared.Card;
import shared.Enums.Points;
import shared.Enums.PointsNonAtout;
import shared.Enums.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class Deck {

    ArrayList<Card> cards;

    public Deck()
    {
        this.cards = new ArrayList<Card>();

        cards.add(new Card(Types.TREFLE, PointsNonAtout.SEPT));
        cards.add(new Card(Types.TREFLE, PointsNonAtout.HUIT));
        cards.add(new Card(Types.TREFLE, PointsNonAtout.NEUF));
        cards.add(new Card(Types.TREFLE, PointsNonAtout.VALET));
        cards.add(new Card(Types.TREFLE, PointsNonAtout.DAME));
        cards.add(new Card(Types.TREFLE, PointsNonAtout.ROI));
        cards.add(new Card(Types.TREFLE, PointsNonAtout.DIX));
        cards.add(new Card(Types.TREFLE, PointsNonAtout.AS));

        cards.add(new Card(Types.PIQUE, PointsNonAtout.SEPT));
        cards.add(new Card(Types.PIQUE, PointsNonAtout.HUIT));
        cards.add(new Card(Types.PIQUE, PointsNonAtout.NEUF));
        cards.add(new Card(Types.PIQUE, PointsNonAtout.DIX));
        cards.add(new Card(Types.PIQUE, PointsNonAtout.VALET));
        cards.add(new Card(Types.PIQUE, PointsNonAtout.DAME));
        cards.add(new Card(Types.PIQUE, PointsNonAtout.ROI));
        cards.add(new Card(Types.PIQUE, PointsNonAtout.AS));

        cards.add(new Card(Types.COEUR, PointsNonAtout.SEPT));
        cards.add(new Card(Types.COEUR, PointsNonAtout.HUIT));
        cards.add(new Card(Types.COEUR, PointsNonAtout.NEUF));
        cards.add(new Card(Types.COEUR, PointsNonAtout.DIX));
        cards.add(new Card(Types.COEUR, PointsNonAtout.VALET));
        cards.add(new Card(Types.COEUR, PointsNonAtout.DAME));
        cards.add(new Card(Types.COEUR, PointsNonAtout.ROI));
        cards.add(new Card(Types.COEUR, PointsNonAtout.AS));

        cards.add(new Card(Types.CARREAU, PointsNonAtout.SEPT));
        cards.add(new Card(Types.CARREAU, PointsNonAtout.HUIT));
        cards.add(new Card(Types.CARREAU, PointsNonAtout.NEUF));
        cards.add(new Card(Types.CARREAU, PointsNonAtout.DIX));
        cards.add(new Card(Types.CARREAU, PointsNonAtout.VALET));
        cards.add(new Card(Types.CARREAU, PointsNonAtout.DAME));
        cards.add(new Card(Types.CARREAU, PointsNonAtout.ROI));
        cards.add(new Card(Types.CARREAU, PointsNonAtout.AS));

        Collections.shuffle(this.cards);
    }

    /**
     * Push cards to deck
     * @param cards
     */
    public void pushCardsToDeck(Vector<Card> cards)
    {
        for (Card card : cards) {
            this.cards.add(card);
        }
    }

    /**
     * Cut the deck
     */
    public void cut()
    {
        Collections.rotate(this.cards, ThreadLocalRandom.current().nextInt(5, 27));
    }

    public Card getFirstCard() { return this.cards.remove(0); }
}
