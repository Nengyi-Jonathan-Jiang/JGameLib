package app.backend;

import jGameLib.util.files.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CardDeck {
    public static final int FACE_UP_CAPACITY = 4;
    public static final int NUM_TIERS = CardTier.values().length;

    private static final Card[] allCardsList;
    static {
        List<Card> cards = new ArrayList<>();
        Scanner scan = new Scanner(FileUtil.readFileAsText("cards.txt"));
        while (scan.hasNext()) {
            Multiset<Gem> cost = new Multiset<>();
            Gem resourceType = switch (scan.next()) {
                case "Black" -> Gem.BLACK;
                case "Blue" -> Gem.BLUE;
                case "Green" -> Gem.GREEN;
                case "Red" -> Gem.RED;
                case "White" -> Gem.WHITE;
                default -> throw new RuntimeException("Error reading cards from file");
            };
            int prestige = scan.nextInt();
            for (int N = scan.nextInt(); N-- > 0; ) cost.add(Gem.BLACK);
            for (int N = scan.nextInt(); N-- > 0; ) cost.add(Gem.WHITE);
            for (int N = scan.nextInt(); N-- > 0; ) cost.add(Gem.RED);
            for (int N = scan.nextInt(); N-- > 0; ) cost.add(Gem.BLUE);
            for (int N = scan.nextInt(); N-- > 0; ) cost.add(Gem.GREEN);
            CardTier tier = CardTier.values()[scan.nextInt() - 1];

            cards.add(new Card(cost, tier, resourceType, prestige));
        }

        allCardsList = cards.toArray(Card[]::new);
    }

    @SuppressWarnings("unchecked")
    private final List<Card>[] deck = new List[NUM_TIERS];
    public final Card[][] faceUpCards = new Card[NUM_TIERS][FACE_UP_CAPACITY];

    public CardDeck() {
        this(List.of(allCardsList));
    }

    public CardDeck(List<Card> allCards) {
        for (int i = 0; i < NUM_TIERS; i++) {
            deck[i] = new ArrayList<>();
        }

        List<Card> shuffled = new ArrayList<>(allCards);
        Collections.shuffle(shuffled);
        for (Card card : shuffled) {
            deck[card.tier.ordinal()].add(card);
        }

        replenishFaceUpCards();
    }

    public void replenishFaceUpCards() {
        for (int i = 0; i < NUM_TIERS; i++) {
            Card[] tierCards = faceUpCards[i];
            List<Card> tierDeck = deck[i];
            for (int j = 0; j < tierCards.length && !tierDeck.isEmpty(); j++) {
                if (tierCards[j] == null) {
                    tierCards[j] = tierDeck.removeLast();
                }
            }
        }
    }

    public void removeCard(Card reservedCard) {
        for (int i = 0; i < NUM_TIERS; i++) {
            Card[] tierCards = faceUpCards[i];
            for (int j = 0; j < tierCards.length; j++) {
                if (tierCards[j] == reservedCard) {
                    tierCards[j] = null;
                    return;
                }
            }
        }
        throw new RuntimeException("Tried to remove card that was not in face up grid");
    }

    public boolean hasFaceDownCardsInTier(CardTier tier) {
        return !deck[tier.ordinal()].isEmpty();
    }
}
