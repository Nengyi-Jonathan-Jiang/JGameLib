package app.frontend;

import app.backend.Card;
import app.backend.Gem;
import app.frontend.components.CircleRendererComponent;
import jGameLib.ecs.ui2d.rendering.UIEntity;
import jGameLib.ecs.ui2d.rendering.VerticalLayoutEntity;
import jGameLib.ecs.ui2d.utils.*;
import jGameLib.math.Vec2;
import jGameLib.util.FontLoader;
import jGameLib.util.Pair;

import java.awt.*;
import java.util.Arrays;

public class CardEntity extends UIEntity {
    private final Card card;

    public CardEntity(Card card) {
        this.card = card;
        boundingBox.setSize(200, 350);
        addCardBackgroundEntity();
        addCardCostEntity();
        addCardResourceEntity();
        addCardPrestigeEntity();
    }

    private void addCardPrestigeEntity() {
        new UIEntity()
            .withBoundingBox(b -> boundingBox.addChild(b
                .setSize(60, 60)
                .setTopRight(boundingBox.getTopRightOffset().plus(-10, 10))
            ))
            .addComponent(new CircleRendererComponent(Color.BLACK, null))
            .addComponent(new TextRendererBehavior(card.prestige + "", new TextStyleBuilder()
                .setFont(FontLoader.load("fonts/Nunito.ttf").deriveFont(40f))
                .setAlignment(TextStyle.TextAlign.ALIGN_CENTER)
                .get()
            ));
    }

    private void addCardResourceEntity() {
        new UIEntity()
            .withBoundingBox(b -> boundingBox.addChild(b
                .setSize(60, 60)
                .setTopLeft(boundingBox.getTopLeftOffset().plus(10, 10))
            ))
            .addComponent(new CircleRendererComponent(null, card.resourceType.getColor()));
    }

    private void addCardCostEntity() {
        VerticalLayoutEntity v = new VerticalLayoutEntity();
        boundingBox.addChild(v.boundingBox);

        Arrays.stream(Gem.values())
            .map(i -> new Pair<>(i, card.cost(i)))
            .filter(i -> i.b != 0)
            .forEachOrdered((p) -> {
                UIEntity x = new UIEntity(new Vec2(40, 40))
                    .addComponent(new CircleRendererComponent(
                        null, p.a.getColor()
                    ))
                    .addComponent(
                        new TextRendererBehavior(p.b.toString())
                            .setStyle(
                                new TextStyleBuilder()
                                    .setFont(FontLoader.load("fonts/Nunito.ttf")
                                        .deriveFont(25f)
                                        .deriveFont(Font.BOLD))
                                    .setColor(Color.WHITE)
                                    .setAlignment(TextStyle.TextAlign.ALIGN_CENTER)
                                    .get()
                            )
                    )
                    .cast();

                v.boundingBox
                    .addChild(new SpacingEntity(new Vec2(0, 10)).boundingBox)
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
