package app.frontend;

import app.backend.Card;
import app.backend.CardDeck;
import app.backend.CardTier;
import jGameLib.core.GameState;
import jGameLib.ui2d.rendering.UIEntity;
import jGameLib.ui2d.rendering.UIRendererComponent;
import jGameLib.util.math.Vec2;
import jGameLib.util.math.Vec2i;

public class CardDeckEntity extends UIEntity {
    private final CardDeck deck;
    private final CardEntity[][] card = new CardEntity[CardDeck.NUM_TIERS][CardDeck.FACE_UP_CAPACITY];

    public CardDeckEntity(GameState state, CardDeck deck) {
        super(state);
        boundingBox.setSize(CardEntity.CARD_SIZE.times(5, 3).plus(40, 20));

        for (int i = 0; i < CardDeck.NUM_TIERS; i++) {
            for (int j = 0; j < CardDeck.FACE_UP_CAPACITY; j++) {
                replaceEntityAt(new Vec2i(j, i), new CardEntity(state,null));
            }
        }

        addCardBackForTier(CardTier.I);
        addCardBackForTier(CardTier.II);
        addCardBackForTier(CardTier.III);

        this.deck = deck;
        addComponent(new UIRendererComponent() {
            @Override
            public void update() {
                updateCardEntities();
            }
        });
    }

    private void addCardBackForTier(CardTier tier) {
        new CardBackEntity(state, tier).withBoundingBox(
            i -> boundingBox.addChild(
                i.setRelativePosition(getPosFor(new Vec2i(4, tier.ordinal())))
            )
        ).addComponent(new UIRendererComponent() {
            @Override
            public void update() {
                if (!deck.hasFaceDownCardsInTier(tier)) {
                    entity.destroy();
                }
            }
        });
    }

    private Vec2 getPosFor(Vec2i position) {
        return boundingBox.getTopLeftOffset().plus(
            new Vec2(position).times(CardEntity.CARD_SIZE.plus(10, 10))
        ).plus(CardEntity.CARD_SIZE.times(0.5));
    }

    public Vec2i getHoveredCard() {
        for (int i = 0; i < CardDeck.NUM_TIERS; i++) {
            for (int j = 0; j < CardDeck.FACE_UP_CAPACITY; j++) {
                if (card[i][j] != null && card[i][j].isHovered()) {
                    return new Vec2i(j, i);
                }
            }
        }
        return null;
    }

    private CardEntity getEntityAt(Vec2i position) {
        return this.card[position.y][position.x];
    }

    private void replaceEntityAt(Vec2i position, CardEntity newEntity) {
        CardEntity oldEntity = getEntityAt(position);
        if (oldEntity != null) {
            oldEntity.destroy();
        }
        card[position.y][position.x] = newEntity;
        boundingBox.addChild(newEntity.boundingBox);
        newEntity.boundingBox.setRelativePosition(getPosFor(position));
    }

    private void updateCardEntities() {
        for (int i = 0; i < CardDeck.NUM_TIERS; i++) {
            for (int j = 0; j < CardDeck.FACE_UP_CAPACITY; j++) {
                Vec2i position = new Vec2i(j, i);
                Card newCard = deck.faceUpCards[i][j];
                CardEntity oldCardEntity = getEntityAt(position);
                Card oldCard = oldCardEntity.card;

                if (newCard != oldCard) {
                    replaceEntityAt(position, new CardEntity(state, newCard));
                }
            }
        }
    }
}
