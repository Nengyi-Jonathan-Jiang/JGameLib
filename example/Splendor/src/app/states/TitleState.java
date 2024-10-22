package app.states;

import app.backend.Card;
import app.backend.CardTier;
import app.backend.Gem;
import app.backend.Multiset;
import app.frontend.CardEntity;
import app.frontend.GemEntity;
import jGameLib.ecs.ui2d.rendering.UIEntity;
import jGameLib.ecs.ui2d.rendering.UIRendererRootComponent;
import jGameLib.ecs.ui2d.utils.ButtonEntity;
import jGameLib.ecs.ui2d.utils.RectRendererComponent;
import jGameLib.ecs.ui2d.utils.TextStyle;
import jGameLib.ecs.ui2d.utils.TextStyleBuilder;

import java.awt.*;

public class TitleState extends BasicState {
    public TitleState() {
        new UIEntity()
            .withBoundingBox(b -> b.setSize(1920, 1080))
            .addComponent(new RectRendererComponent(null, Color.WHITE))
            .addComponent(new UIRendererRootComponent());

        new GemEntity(Gem.BLACK)
            .addComponent(new UIRendererRootComponent());

        new CardEntity(new Card(
            new Multiset<>() {{
                add(Gem.RED);
                add(Gem.BLACK);
                add(Gem.BLACK);
                add(Gem.BLACK);
            }}, CardTier.ONE, Gem.GREEN, 2
        ))
            .withBoundingBox(b -> b.setRelativePosition(-400, 20))
            .addComponent(new UIRendererRootComponent());
    }
}