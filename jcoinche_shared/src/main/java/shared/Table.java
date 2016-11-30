package shared;

import java.util.Vector;


public class Table {

    private Vector<Card> cards;

    public Vector<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card getFirstCard() {
        if (cards.isEmpty())
        {
            System.out.println("There's no card on the table yet");
            return null;
        }
        return cards.get(0);
    }

    public Card getLastCard() {
        if (cards.isEmpty())
        {
            System.out.println("There's no card on the table yet");
            return null;
        }
        return cards.get(cards.size() - 1);
    }

    public void printTable()
    {
        for (Card card : this.cards) {
            System.out.println(card.getName() + " de " + card.getTypeName());
        }
    }
}
