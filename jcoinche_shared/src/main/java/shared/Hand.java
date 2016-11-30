package shared;

import java.util.Vector;

public class Hand {

    public Vector<Card> cards;

    public Vector<Card> getCards() {
        return cards;
    }

    public Hand()
    {
        this.cards = new Vector<Card>();
    }

    public void addCard(Card newCard)
    {
        cards.add(newCard);
    }

    public boolean hasType(int type)
    {
        for (Card card : cards) {
            if (card.getType() == type)
                return true;
        }
        return false;
    }

    public boolean hasAtout(int type)
    {
        for (Card card : cards) {
            if (card.getType() == type)
                return true;
        }
        return false;
    }

    public boolean hasAtoutHigher(int type, int priority, Card isTryingToPlay)
    {
        for (Card card : cards) {
            if (card.getType() == type && card.getPriority() > priority && !isTryingToPlay.equals(card))
                return true;
        }
        return false;
    }

    public String printHand()
    {
        String message = "";
        for (Card card : this.cards) {
            message += cards.indexOf(card) + ": " + card.getName() + " de " + card.getTypeName() + "\n";
        }
        return message;
    }
}
