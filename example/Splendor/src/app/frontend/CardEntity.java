package app.frontend;

import app.backend.Card;
import app.backend.Gem;
import app.frontend.components.CircleRendererComponent;
import jGameLib.core.GameState;
import jGameLib.ui2d.input.UserInputHandlerComponent;
import jGameLib.ui2d.input.UserInputState;
import jGameLib.ui2d.rendering.UIEntity;
import jGameLib.ui2d.rendering.VerticalLayoutEntity;
import jGameLib.util.math.Vec2;
import jGameLib.ui2d.utils.*;
import jGameLib.util.Pair;

import java.awt.*;
import java.util.Arrays;

public class CardEntity extends UIEntity {
    public static final Vec2 CARD_SIZE = new Vec2(150, 210);
    public final Card card;
    private boolean isHovered;

    public CardEntity(GameState state, Card card) {
        super(state);
        this.card = card;
        boundingBox.setSize(CARD_SIZE);

        if (card == null) {
            addComponent(new RectRendererComponent());
            return;
        }

        HoverDetectionComponent hoverDetectionComponent = new HoverDetectionComponent();
        addComponent(hoverDetectionComponent);
        addComponent(new UserInputHandlerComponent() {
            @Override
            protected void update(UserInputState state) {
                isHovered = hoverDetectionComponent.contains(state.getMousePosition());
                getComponent(RectRendererComponent.class).setBorderColor(
                    isHovered ? Color.YELLOW : Color.BLACK
                );
            }
        });

        addCardBackgroundEntity();
        addCardCostEntity();
        addCardResourceEntity();
        addCardPrestigeEntity();
        addCardTierEntity();
    }

    private void addCardTierEntity() {
        new UIEntity(state)
            .withBoundingBox(b -> boundingBox.addChild(b
                .setSize(60, 60)
                .setBottomRight(boundingBox.getBottomRight().minus(10, 10))
            ))
            .addComponent(new CircleRendererComponent(Color.BLACK, null))
            .addComponent(new TextRendererComponent(card.tier.name(), new TextStyleBuilder()
                .setFont("fonts/Nunito.ttf")
                .setFontSize(40)
                .setAlignment(TextStyle.TextAlign.CENTER)
                .get()
            ));
    }

    public boolean isHovered() {
        return isHovered;
    }

    private void addCardPrestigeEntity() {
        new UIEntity(state)
            .withBoundingBox(b -> boundingBox.addChild(b
                .setSize(60, 60)
                .setTopRight(boundingBox.getTopRightOffset().plus(-10, 10))
            ))
            .addComponent(new CircleRendererComponent(Color.BLACK, null))
            .addComponent(new TextRendererComponent(card.prestige + "", new TextStyleBuilder()
                .setFont("fonts/Nunito.ttf")
                .setFontSize(40)
                .setAlignment(TextStyle.TextAlign.CENTER)
                .get()
            ));
    }

    private void addCardResourceEntity() {
        new UIEntity(state)
            .withBoundingBox(b -> boundingBox.addChild(b
                .setSize(30, 30)
                .setTopLeft(boundingBox.getTopLeftOffset().plus(10, 10))
            ))
            .addComponent(new CircleRendererComponent(null, card.resourceType.getColor()));
    }

    private void addCardCostEntity() {
        VerticalLayoutEntity v = new VerticalLayoutEntity(state);
        boundingBox.addChild(v.boundingBox);

        Arrays.stream(Gem.values())
            .map(i -> new Pair<>(i, card.cost(i)))
            .filter(i -> i.b != 0)
            .forEachOrdered((p) -> {
                UIEntity x = new UIEntity(state, new Vec2(30, 30))
                    .addComponent(new CircleRendererComponent(
                        p.a.getColor(), null
                    ))
                    .addComponent(
                        new TextRendererComponent(p.b.toString())
                            .setStyle(
                                new TextStyleBuilder()
                                    .setFont("fonts/Nunito.ttf")
                                    .setFontSize(20)
                                    .setFontStyle(TextStyleBuilder.FontStyle.BOLD)
                                    .setColor(Color.BLACK)
                                    .setAlignment(TextStyle.TextAlign.CENTER)
                                    .get()
                            )
                    )
                    .cast();

                v.boundingBox
                    .addChild(new SpacingEntity(state, new Vec2(0, 10)).boundingBox)
                    .addChild(x.boundingBox);
            });

        v.setPadding(new Vec2(10))
            .applyLayout()
            .boundingBox
            .setBottomLeft(boundingBox.getBottomLeftOffset());
    }

    private void addCardBackgroundEntity() {
        addComponent(new RectRendererComponent(
            Color.BLACK,
            new Color(0xeeddcc)
        ));
    }
}
