package shared.Enums;

public enum PointsToutAtout implements Points {
    SEPT(0, 8, 0, "Sept"),
    HUIT(1, 9, 0, "Huit"),
    DAME(5, 10, 1, "Dame"),
    ROI(6, 11, 3, "Roi"),
    DIX(3, 12, 5, "Dix"),
    AS(7, 13, 6, "As"),
    NEUF(2, 14, 9, "Neuf"),
    VALET(4, 15, 14, "Valet");

    private int priority;
    private int points;
    private String name;
    private int id;

    private PointsToutAtout(int id, int priority, int points, String name)
    {
        this.priority = priority;
        this.points = points;
        this.name = name;
        this.id = id;
    }

    public int getPoints() { return this.points; }

    public int getPriority() { return this.priority; }

    public String getName() { return this.name; }

    public int getId() {
        return id;
    }
}
