package app.backend;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public final String name;
    public final List<Card> cards = new ArrayList<>();
    public final Multiset<Gem> gems = new Multiset<>();
    public int numGold = 0;
    public final List<Card> reservedCards = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }
}
