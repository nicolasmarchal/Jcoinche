package shared.Enums;

public enum PointsNonAtout implements Points {
    SEPT(0, 0, 0, "Sept"),
    HUIT(1, 1, 0, "Huit"),
    NEUF(2, 2, 0, "Neuf"),
    VALET(4, 3, 2, "Valet"),
    DAME(5, 4, 3, "Dame"),
    ROI(6, 5, 4, "Roi"),
    DIX(3, 6, 10, "Dix"),
    AS(7, 7, 11, "As");

    private int priority;
    private int points;
    private String name;
    private int id;

    private PointsNonAtout(int id, int priority, int points, String name)
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