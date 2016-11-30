package shared;

import shared.Enums.*;

public class Card {

    private Types type;
    private Points point;
    private boolean isAtout;

    public Card(Types type, Points point)
    {
        this.type = type;
        this.point = point;
        this.isAtout = false;
    }

    public void changeCardNonAtout()
    {
        this.isAtout = false;
        if (this.point.getName().compareToIgnoreCase("Sept") == 0)
            this.point = PointsNonAtout.SEPT;
        if (this.point.getName().compareToIgnoreCase("Huit") == 0)
            this.point = PointsNonAtout.HUIT;
        if (this.point.getName().compareToIgnoreCase("Dix") == 0)
            this.point = PointsNonAtout.DIX;
        if (this.point.getName().compareToIgnoreCase("Neuf") == 0)
            this.point = PointsNonAtout.NEUF;
        if (this.point.getName().compareToIgnoreCase("Valet") == 0)
            this.point = PointsNonAtout.VALET;
        if (this.point.getName().compareToIgnoreCase("Dame") == 0)
            this.point = PointsNonAtout.DAME;
        if (this.point.getName().compareToIgnoreCase("Roi") == 0)
            this.point = PointsNonAtout.ROI;
        if (this.point.getName().compareToIgnoreCase("As") == 0)
            this.point = PointsNonAtout.AS;
    }

    public void changeCardToAtout()
    {
        this.isAtout = true;
        if (this.point.equals(PointsNonAtout.SEPT))
            this.point = PointsAtout.SEPT;
        if (this.point.equals(PointsNonAtout.HUIT))
            this.point = PointsAtout.HUIT;
        if (this.point.equals(PointsNonAtout.DIX))
            this.point = PointsAtout.DIX;
        if (this.point.equals(PointsNonAtout.NEUF))
            this.point = PointsAtout.NEUF;
        if (this.point.equals(PointsNonAtout.VALET))
            this.point = PointsAtout.VALET;
        if (this.point.equals(PointsNonAtout.DAME))
            this.point = PointsAtout.DAME;
        if (this.point.equals(PointsNonAtout.ROI))
            this.point = PointsAtout.ROI;
        if (this.point.equals(PointsNonAtout.AS))
            this.point = PointsAtout.AS;
    }

    public void changeCardSansAtout()
    {
        if (this.point.equals(PointsNonAtout.SEPT))
            this.point = PointsSansAtout.SEPT;
        if (this.point.equals(PointsNonAtout.HUIT))
            this.point = PointsSansAtout.HUIT;
        if (this.point.equals(PointsNonAtout.DIX))
            this.point = PointsSansAtout.DIX;
        if (this.point.equals(PointsNonAtout.NEUF))
            this.point = PointsSansAtout.NEUF;
        if (this.point.equals(PointsNonAtout.VALET))
            this.point = PointsSansAtout.VALET;
        if (this.point.equals(PointsNonAtout.DAME))
            this.point = PointsSansAtout.DAME;
        if (this.point.equals(PointsNonAtout.ROI))
            this.point = PointsSansAtout.ROI;
        if (this.point.equals(PointsNonAtout.AS))
            this.point = PointsSansAtout.AS;
    }

    public void changeCardToutAtout()
    {
        this.isAtout = true;
        if (this.point.equals(PointsNonAtout.SEPT))
            this.point = PointsToutAtout.SEPT;
        if (this.point.equals(PointsNonAtout.HUIT))
            this.point = PointsToutAtout.HUIT;
        if (this.point.equals(PointsNonAtout.DIX))
            this.point = PointsToutAtout.DIX;
        if (this.point.equals(PointsNonAtout.NEUF))
            this.point = PointsToutAtout.NEUF;
        if (this.point.equals(PointsNonAtout.VALET))
            this.point = PointsToutAtout.VALET;
        if (this.point.equals(PointsNonAtout.DAME))
            this.point = PointsToutAtout.DAME;
        if (this.point.equals(PointsNonAtout.ROI))
            this.point = PointsToutAtout.ROI;
        if (this.point.equals(PointsNonAtout.AS))
            this.point = PointsToutAtout.AS;
    }

    public int getType() {
        return type.getType();
    }

    public String getTypeName() { return type.getName(); }

    public boolean isAtout() {
        return isAtout;
    }

    public void setAtout(boolean atout) {
        isAtout = atout;
    }

    public int getPoints() {
        return point.getPoints();
    }

    public String getName () { return point.getName(); }

    public int getPriority() { return point.getPriority(); }

    public int getId() { return point.getId(); }

}

//32 Cartes (8 cartes de chaque types (Trefle, Pique, Coeur, Carreau))