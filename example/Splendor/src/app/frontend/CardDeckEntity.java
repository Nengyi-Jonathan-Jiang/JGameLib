package app.frontend;

import app.backend.Card;
import app.backend.CardDeck;
import app.backend.CardTier;
import app.backend.Noble;
import jGameLib.core.GameState;
import jGameLib.ui2d.rendering.UIEntity;
import jGameLib.ui2d.rendering.UIRendererComponent;
import jGameLib.ui2d.utils.PositionAnimationComponent;
import jGameLib.util.math.Vec2;
import jGameLib.util.math.Vec2i;

import java.util.Arrays;

public class CardDeckEntity extends UIEntity {
    private final CardDeck deck;
    private final CardEntity[][] cardEntities = new CardEntity[CardDeck.NUM_TIERS][CardDeck.FACE_UP_CAPACITY];
    private final NobleEntity[] nobleEntities = new NobleEntity[CardDeck.NUM_NOBLES];

    public CardDeckEntity(GameState state, CardDeck deck) {
        super(state);
        boundingBox.setSize(CardEntity.CARD_SIZE.times(5, 3).plus(40, 20));

        for (int i = 0; i < CardDeck.NUM_TIERS; i++) {
            for (int j = 0; j < CardDeck.FACE_UP_CAPACITY; j++) {
                replaceCardEntityAt(new Vec2i(j, i), new CardEntity(state, null));
            }
        }

        for (int i = 0; i < CardDeck.NUM_NOBLES; i++) {
            replaceNobleEntityAt(i, new NobleEntity(state, null));
        }

        addCardBackForTier(CardTier.I);
        addCardBackForTier(CardTier.II);
        addCardBackForTier(CardTier.III);

        this.deck = deck;
        addComponent(new UIRendererComponent() {
            @Override
            public void update() {
                updateEntities();
                if (!isAnimating()) {
                    deck.replenishFaceUpCards();
                }
            }
        });

        updateEntities();
    }

    private void addCardBackForTier(CardTier tier) {
        new CardBackEntity(state, tier).withBoundingBox(
            i -> boundingBox.addChild(
                i.setRelativePosition(getCardPosFor(new Vec2i(4, tier.ordinal())))
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

    private Vec2 getCardPosFor(Vec2i position) {
        return boundingBox.getTopLeftOffset()
            .plus(
                new Vec2(position).times(CardEntity.CARD_SIZE.plus(10, 10))
            )
            .plus(CardEntity.CARD_SIZE.times(0.5))
            .plus(0, NobleEntity.NOBLE_SIZE.y + 20);
    }

    private Vec2 getNoblePosFor(int position) {
        return boundingBox.getTopLeftOffset()
            .plus(
                new Vec2(position * (CardEntity.CARD_SIZE.x + 10), 0)
            )
            .plus(NobleEntity.NOBLE_SIZE.times(0.5));
    }

    public Vec2i getHoveredCard() {
        for (int i = 0; i < CardDeck.NUM_TIERS; i++) {
            for (int j = 0; j < CardDeck.FACE_UP_CAPACITY; j++) {
                if (cardEntities[i][j] != null && cardEntities[i][j].isHovered()) {
                    return new Vec2i(j, i);
                }
            }
        }
        return null;
    }

    public boolean isAnimating() {
        return Arrays.stream(cardEntities).flatMap(Arrays::stream)
            .filter(e -> e.hasComponent(PositionAnimationComponent.class))
            .map(e -> e.getComponent(PositionAnimationComponent.class))
            .anyMatch(PositionAnimationComponent::isAnimating);
    }

    public int getHoveredNoble() {
        for (int i = 0; i < CardDeck.NUM_NOBLES; i++) {
            if (nobleEntities[i].isHovered()) {
                return i;
            }
        }
        return -1;
    }

    private CardEntity getCardEntityAt(Vec2i position) {
        return this.cardEntities[position.y][position.x];
    }

    private NobleEntity getNobleEntityAt(int position) {
        return this.nobleEntities[position];
    }

    private void replaceCardEntityAt(Vec2i position, CardEntity newEntity) {
        CardEntity oldEntity = getCardEntityAt(position);
        if (oldEntity != null) {
            oldEntity.destroy();
        }
        cardEntities[position.y][position.x] = newEntity;
        boundingBox.addChild(newEntity.boundingBox);
        Vec2 cardPos = getCardPosFor(position);
        newEntity.boundingBox.setRelativePosition(cardPos);

        if (newEntity.hasComponent(PositionAnimationComponent.class)) {
            newEntity.boundingBox.setTopRight(new Vec2(
                boundingBox.getTopRightOffset().x, newEntity.boundingBox.getTopRight().y
            ));
            newEntity.getComponent(PositionAnimationComponent.class).moveTo(cardPos, 20);
        }
    }

    private void replaceNobleEntityAt(int position, NobleEntity newEntity) {
        NobleEntity oldEntity = getNobleEntityAt(position);
        if (oldEntity != null) {
            oldEntity.destroy();
        }
        nobleEntities[position] = newEntity;
        boundingBox.addChild(newEntity.boundingBox);
        newEntity.boundingBox.setRelativePosition(getNoblePosFor(position));
    }

    private void updateEntities() {
        for (int i = 0; i < CardDeck.NUM_TIERS; i++) {
            for (int j = 0; j < CardDeck.FACE_UP_CAPACITY; j++) {
                Vec2i position = new Vec2i(j, i);
                Card newCard = deck.faceUpCards[i][j];
                CardEntity oldCardEntity = getCardEntityAt(position);
                Card oldCard = oldCardEntity.card;

                if (newCard != oldCard) {
                    replaceCardEntityAt(position, new CardEntity(state, newCard));
                }
            }
        }
        for (int i = 0; i < CardDeck.NUM_NOBLES; i++) {
            Noble newNoble = deck.nobles[i];
            NobleEntity oldNobleEntity = getNobleEntityAt(i);
            Noble oldNoble = oldNobleEntity.noble;

            if (newNoble != oldNoble) {
                replaceNobleEntityAt(i, new NobleEntity(state, newNoble));
            }
        }
    }
}
