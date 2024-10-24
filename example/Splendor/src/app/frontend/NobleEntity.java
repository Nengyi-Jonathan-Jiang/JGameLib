package app.frontend;

import app.backend.Gem;
import app.backend.Noble;
import app.frontend.components.CircleRendererComponent;
import jGameLib.core.GameState;
import jGameLib.ui2d.input.UserInputHandlerComponent;
import jGameLib.ui2d.input.UserInputState;
import jGameLib.ui2d.rendering.UIEntity;
import jGameLib.ui2d.rendering.VerticalLayoutEntity;
import jGameLib.ui2d.utils.*;
import jGameLib.util.Pair;
import jGameLib.util.math.Vec2;

import java.awt.*;
import java.util.Arrays;

public class NobleEntity extends UIEntity {
    public static final Vec2 NOBLE_SIZE = new Vec2(150, 150);
    public final Noble noble;
    private boolean isHovered;

    public NobleEntity(GameState state, Noble noble) {
        super(state);
        this.noble = noble;
        boundingBox.setSize(NOBLE_SIZE);

        if (noble == null) {
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

        addNobleBackgroundEntity();
        addCardCostEntity();
        addCardPrestigeEntity();
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
            .addComponent(new TextRendererComponent("3", new TextStyleBuilder()
                .setFont("fonts/Nunito.ttf")
                .setFontSize(40)
                .setAlignment(TextStyle.TextAlign.CENTER)
                .get()
            ));
    }

    private void addCardCostEntity() {
        VerticalLayoutEntity v = new VerticalLayoutEntity(state);
        boundingBox.addChild(v.boundingBox);

        Arrays.stream(Gem.values())
            .map(i -> new Pair<>(i, noble.cost(i)))
            .filter(i -> i.b() != 0)
            .forEachOrdered((p) -> {
                UIEntity x = new UIEntity(state, new Vec2(30, 30))
                    .addComponent(new CircleRendererComponent(
                        p.a().getColor(), null
                    ))
                    .addComponent(
                        new TextRendererComponent(p.b().toString())
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

    private void addNobleBackgroundEntity() {
        addComponent(new RectRendererComponent(
            Color.BLACK,
            new Color(0xeeddcc)
        ));
    }
}
