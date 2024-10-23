package app.frontend;

import app.backend.CardTier;
import jGameLib.ecs.GameState;
import jGameLib.ecs.ui2d.rendering.UIEntity;
import jGameLib.ecs.ui2d.utils.RectRendererComponent;
import jGameLib.ecs.ui2d.utils.TextRendererComponent;
import jGameLib.ecs.ui2d.utils.TextStyle;
import jGameLib.ecs.ui2d.utils.TextStyleBuilder;

import java.awt.*;

public class CardBackEntity extends UIEntity {
    public CardBackEntity(GameState state, CardTier tier) {
        super(state);
        boundingBox.setSize(CardEntity.CARD_SIZE);
        addComponent(new RectRendererComponent(Color.BLACK, new Color(0xdddddd)));
        addComponent(new TextRendererComponent(
            tier.name(),
            new TextStyleBuilder()
                .setFontSize(80)
                .setFont("fonts/Nunito.ttf")
                .setAlignment(TextStyle.TextAlign.CENTER)
                .get()
        ));
    }
}
