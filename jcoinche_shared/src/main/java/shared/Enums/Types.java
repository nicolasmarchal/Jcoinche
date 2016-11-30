package shared.Enums;

public enum Types
{
    TREFLE(0, "Trefle"),
    PIQUE(1, "Pique"),
    COEUR(2, "Coeur"),
    CARREAU(3, "Carreau"),
    NON_ATOUT(4, "Sans atout"),
    TOUT_ATOUT(5, "Tout atout");

    private int id;
    private String name;

    private Types(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getType()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
