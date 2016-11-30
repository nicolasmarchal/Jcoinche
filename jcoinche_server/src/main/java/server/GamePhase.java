package server;

import shared.Card;
import shared.Enums.Types;

import java.util.Vector;

public class GamePhase {

    private Types atout;
    private Vector<Card> cards;
    private Player playerWhoPlayedBestCard;
    private String lastErrorAtPlaying;
    private int indexCurrentPlayer;

    public void init(Types atout, int indexCurrentPlayer)
    {
        this.cards = new Vector<Card>();
        this.atout = atout;
        this.indexCurrentPlayer = indexCurrentPlayer;
        Notifications.notifIsPlayTurn(Game.getDefaultInstance().getPlayers().get(this.indexCurrentPlayer));
    }

    public void manageEndTurn()
    {
        Game.getDefaultInstance().endGamePhase();
    }

    public String printCardTable()
    {
        String message = "";

        for (Card card : this.cards) {
            message += this.cards.indexOf(card) + ": " + card.getName() + " de " + card.getTypeName() + "\n";
        }

        if (this.cards.size() == 0)
            message = "No cards on the table yet\n";

        return message;
    }

    public void addCardOnTable(Player player, Card card)
    {
        int priority = -1;

        for (Card cardTable : this.cards) {
            if ((cardTable.getType() == this.cards.get(0).getType() || cardTable.isAtout()) && cardTable.getPriority() > priority)
                priority = cardTable.getPriority();
        }

        if ((this.cards.size() == 0 || card.getType() == this.cards.get(0).getType() || card.isAtout()) && card.getPriority() > priority)
            this.playerWhoPlayedBestCard = player;
        this.cards.add(card);

        Notifications.notifCardHasBeenPlayed(Game.getDefaultInstance().getPlayers(), card, player);
        Notifications.notifCardOnTable(Game.getDefaultInstance().getPlayers());

        if (this.cards.size() == 4) {
            this.playerWhoPlayedBestCard.getPoints().addToPointsTurn(this.getPointsOnTable());
            Game.getDefaultInstance().getDeck().pushCardsToDeck(this.cards);
            this.cards.clear();
            if (player.getHand().getCards().size() == 0) {
                this.playerWhoPlayedBestCard.getPoints().addToPointsTurn(10);
                this.manageEndTurn();
                return;
            }
            Notifications.notifWhoWonTurn(this.playerWhoPlayedBestCard, Game.getDefaultInstance().getPlayers());
            this.indexCurrentPlayer = Game.getDefaultInstance().getPlayers().indexOf(this.playerWhoPlayedBestCard) - 1;
        }
        this.nextPlayer();
        Notifications.notifIsPlayTurn(Game.getDefaultInstance().getPlayers().get(this.indexCurrentPlayer));
    }

    public int getPointsOnTable()
    {
        int points = 0;

        for (Card card : this.cards) {
            points += card.getPoints();
        }
        return (points);
    }

    public int getPriorityBestAtoutOnTable() {

        int priority = 0;

        for (Card card : this.cards) {
            if (card.getPriority() > priority)
                priority = card.getPriority();
        }

        return priority;
    }

    public boolean playerHasAtout(Player player , Card card) {

        if (this.atout != Types.NON_ATOUT && player.getHand().hasAtout(this.atout.getType())) {
            this.lastErrorAtPlaying = "Atout was asked, you have some but you tried to play an other type\n";
            return false;
        } else {
            player.getHand().getCards().remove(card);
            this.addCardOnTable(player, card);
            return true;
        }
    }

    public boolean isCardAtout(Player player, Card card)
    {
        if (!card.isAtout()) {
            return this.playerHasAtout(player, card);
        } else {
            if (card.getPriority() > this.getPriorityBestAtoutOnTable()) {
                player.getHand().getCards().remove(card);
                this.addCardOnTable(player, card);
                return true;
            } else {
                if (this.atout != Types.NON_ATOUT && player.getHand().hasAtoutHigher(this.atout.getType(), this.getPriorityBestAtoutOnTable(), card)) {
                    this.lastErrorAtPlaying = "You have atout higher in your hand but you tried to play something lower\n";
                    return false;
                } else {
                    player.getHand().getCards().remove(card);
                    this.addCardOnTable(player, card);
                    return true;
                }
            }
        }
    }

    public boolean isYourTurn(int idxPlayer)
    {
        return this.indexCurrentPlayer == idxPlayer;
    }

    private int getPriorityToutAtout()
    {
        int priority = -1;

        for (Card card : this.cards) {
            if (this.cards.get(0).getType() == card.getType() && card.getPriority() > priority)
                priority = card.getPriority();
        }

        return priority;
    }

    public void addCardOnTableToutAtout(Player player, Card card)
    {
        int priority = -1;

        for (Card cardTable : this.cards) {
            if (cardTable.getType() == this.cards.get(0).getType() && cardTable.getPriority() > priority)
                priority = cardTable.getPriority();
        }

        if ((this.cards.size() == 0 || card.getType() == this.cards.get(0).getType()) && card.getPriority() > priority)
            this.playerWhoPlayedBestCard = player;
        this.cards.add(card);

        System.out.println("Type :" + card.getTypeName() + " Value: " + card.getName());
        Notifications.notifCardHasBeenPlayed(Game.getDefaultInstance().getPlayers(), card, player);
        Notifications.notifCardOnTable(Game.getDefaultInstance().getPlayers());

        if (this.cards.size() == 4) {
            this.playerWhoPlayedBestCard.getPoints().addToPointsTurn(this.getPointsOnTable());
            Game.getDefaultInstance().getDeck().pushCardsToDeck(this.cards);
            this.cards.clear();
            if (player.getHand().getCards().size() == 0) {
                this.playerWhoPlayedBestCard.getPoints().addToPointsTurn(10);
                this.manageEndTurn();
                return;
            }
            Notifications.notifWhoWonTurn(this.playerWhoPlayedBestCard, Game.getDefaultInstance().getPlayers());
            this.indexCurrentPlayer = Game.getDefaultInstance().getPlayers().indexOf(this.playerWhoPlayedBestCard) - 1;
        }
        this.nextPlayer();
        Notifications.notifIsPlayTurn(Game.getDefaultInstance().getPlayers().get(this.indexCurrentPlayer));
    }

    private boolean manageToutAtout(Player player, Card card) {
        System.out.println("Card type: " + card.getTypeName());
        if (this.cards.get(0).getType() == card.getType()) {
            if (this.getPriorityToutAtout() < card.getPriority())
            {
                System.out.print(this.getPriorityToutAtout() + " " + card.getPriority() + " " + card.getTypeName());
                player.getHand().getCards().remove(card);
                this.addCardOnTableToutAtout(player, card);
                return true;
            } else {
                if (player.getHand().hasAtoutHigher(this.cards.get(0).getType(), this.getPriorityToutAtout(), card)) {
                    this.lastErrorAtPlaying = "You have atout higher in your hand but you tried to play something lower\n";
                    return false;
                } else {
                    player.getHand().getCards().remove(card);
                    this.addCardOnTableToutAtout(player, card);
                    return true;
                }
            }
        } else {
            if (player.getHand().hasAtout(this.cards.get(0).getType())) {
                this.lastErrorAtPlaying = this.cards.get(0).getTypeName() + " was asked, you have some but you tried to play an other type\n";
                return false;
            } else
            {
                player.getHand().getCards().remove(card);
                this.addCardOnTableToutAtout(player, card);
                return true;
            }
        }
    }

    public boolean tryPlayCard(Player player, Card card)
    {
        if (this.cards.size() == 0) {
            player.getHand().getCards().remove(card);
            if (this.atout == Types.TOUT_ATOUT)
                this.addCardOnTableToutAtout(player, card);
            else
                this.addCardOnTable(player, card);
            return true;
        }
        else if (this.cards.get(0).isAtout()) {
            if (this.atout == Types.TOUT_ATOUT)
                return this.manageToutAtout(player, card);
            else
                return this.isCardAtout(player, card);
        } else {
            if (this.cards.get(0).getType() == card.getType()) {
                player.getHand().getCards().remove(card);
                this.addCardOnTable(player, card);
                return true;
            } else {
                if (player.getHand().hasType(this.cards.get(0).getType())) {
                    this.lastErrorAtPlaying = this.cards.get(0).getTypeName() + " was asked, you have some but you tried to play an other type\n";
                    return false;
                } else {
                    return this.isCardAtout(player, card);
                }
            }
        }
    }

    public void nextPlayer() {
        this.indexCurrentPlayer += 1;
        if (this.indexCurrentPlayer == 4)
            this.indexCurrentPlayer = 0;
    }

    public String getLastErrorAtPlaying() { return this.lastErrorAtPlaying; }

    public Vector<Card> getCardsOnTable()
    {
        return this.cards;
    }

}
