package shared.Enums;

public enum PointsAtout implements Points {
    SEPT(0, 8, 0, "Sept"),
    HUIT(1, 9, 0, "Huit"),
    DAME(5, 10, 2, "Dame"),
    ROI(6, 11, 3, "Roi"),
    DIX(3, 12, 10, "Dix"),
    AS(7, 13, 11, "As"),
    NEUF(2, 14, 14, "Neuf"),
    VALET(4, 15, 20, "Valet");

    private int priority;
    private int points;
    private String name;
    private int id;

    private PointsAtout(int id, int priority, int points, String name)
    {
        this.priority = priority;
        this.name = name;
        this.points = points;
        this.id = id;
    }

    public int getPoints()
    {
        return this.points;
    }

    public int getPriority() { return this.priority; }

    public String getName() { return this.name; }

    public int getId() {
        return id;
    }
}
