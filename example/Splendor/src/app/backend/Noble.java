package app.backend;

public class Noble {
    private final Multiset<Gem> cost;
    public static final int PRESTIGE_BONUS = 3;

    public Noble(Multiset<Gem> cost) {
        this.cost = cost;
    }

    public int cost(Gem gem) {
        return cost.count(gem);
    }
}
