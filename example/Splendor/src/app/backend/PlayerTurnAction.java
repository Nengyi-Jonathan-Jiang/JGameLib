package app.backend;

import java.util.Collection;

// PlayerTurn
public sealed interface PlayerTurnAction permits
    PlayerTurnAction.TakeGems,
    PlayerTurnAction.ReserveCard,
    PlayerTurnAction.BuyCard,
    PlayerTurnAction.Pass {

    void apply(Game game, Player player);

    record TakeGems(Gem[] gemsTaken, Gem[] gemsReturned) implements PlayerTurnAction {
        @Override
        public void apply(Game game, Player player) {
            for (Gem gem : gemsTaken) {
                game.bank.removeGem(gem);
                player.gems.add(gem);
            }
            for (Gem gem : gemsReturned) {
                game.bank.returnGem(gem);
                player.gems.remove(gem);
            }
        }
    }

    record ReserveCard(Card reservedCard, boolean didTakeGold) implements PlayerTurnAction {
        @Override
        public void apply(Game game, Player player) {
            game.deck.removeCard(reservedCard);
            if (didTakeGold) {
                game.bank.removeGold();
                player.numGold++;
            }
        }
    }

    record BuyCard(Card card, Collection<Gem> spentGems) implements PlayerTurnAction {
        @Override
        public void apply(Game game, Player player) {
            for (Gem gem : spentGems) {
                player.gems.remove(gem);
                game.bank.returnGem(gem);
            }
        }
    }

    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    record Pass() implements PlayerTurnAction {
        @Override
        public void apply(Game game, Player player) {
            // Do nothing.
        }
    }
}
