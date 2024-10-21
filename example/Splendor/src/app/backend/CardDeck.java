package app.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDeck {
    public final int faceUpCapacity;
    private final Map<CardTier, List<Card>> deck = new HashMap<>();
    public final Map<CardTier, Card[]> faceUpCards = new HashMap<>();

    public CardDeck(int faceUpCapacity, List<Card> allCards) {
        this.faceUpCapacity = faceUpCapacity;

        for (CardTier tier : CardTier.values()) {
            deck.put(tier, new ArrayList<>());
            faceUpCards.put(tier, new Card[faceUpCapacity]);
        }

        for (Card card : allCards) {
            deck.get(card.tier).add(card);
        }

        replenishFaceUpCards();
    }

    public void replenishFaceUpCards() {
        for (CardTier tier : CardTier.values()) {
            Card[] tierCards = faceUpCards.get(tier);
            List<Card> tierDeck = deck.get(tier);
            for (int i = 0; i < tierCards.length && !tierDeck.isEmpty(); i++) {
                if (tierCards[i] == null) {
                    tierCards[i] = tierDeck.removeLast();
                }
            }
        }
    }

    public void removeCard(Card reservedCard) {
        for (CardTier tier : CardTier.values()) {
            Card[] tierCards = faceUpCards.get(tier);
            for (int i = 0; i < tierCards.length; i++) {
                if (tierCards[i] == reservedCard) {
                    tierCards[i] = null;
                    return;
                }
            }
        }
        throw new RuntimeException("Tried to remove card that was not in face up grid");
    }
}
