package app.backend;

public class Card {
    private final Multiset<Gem> cost;
    public final CardTier tier;
    public final Gem resourceType;
    public final int prestige;

    public Card(Multiset<Gem> cost, CardTier tier, Gem resourceType, int prestige) {
        this.cost = cost;
        this.tier = tier;
        this.resourceType = resourceType;
        this.prestige = prestige;
    }

    public int cost(Gem gem) {
        return cost.count(gem);
    }
}
