package app.backend;

import java.util.List;

public class Game {
    public final List<Player> players;
    public final CardDeck deck;
    public final Bank bank;
    private int currentPlayerIndex;

    public Game(List<Player> players, CardDeck deck, Bank bank) {
        this.players = players;
        this.deck = deck;
        this.bank = bank;
        this.currentPlayerIndex = 0;
    }

    public Player currentPlayer() {
        return this.players.get(currentPlayerIndex % this.players.size());
    }

    public void applyTurnActionAndAdvancePlayer(PlayerTurnAction action) {
        action.apply(this, currentPlayer());
        this.currentPlayerIndex++;
    }
}
