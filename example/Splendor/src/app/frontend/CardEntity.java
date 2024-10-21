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
    public CardEntity(Card card) {
        boundingBox.setSize(200, 350);
        addComponent(new RectRendererBehavior(
            Color.BLACK,
            new Color(0xeeddcc)
        ));

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
                                    .setFgColor(Color.WHITE)
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
}
