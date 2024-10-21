package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.ui2d.input.MouseEvent;
import jGameLib.ecs.ui2d.input.UserInputHandlerComponent;
import jGameLib.ecs.ui2d.input.UserInputState;
import jGameLib.ecs.ui2d.rendering.UIEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ButtonEntity extends UIEntity {
    public final TextRendererBehavior textRendererComponent = new TextRendererBehavior("");
    public final HoverDetectionComponent hoverDetectionComponent = new HoverDetectionComponent();

    private final List<ButtonClickListener> clickListeners = new ArrayList<>();

    public ButtonEntity(String text, Font font, Color background, Color border, Color textColor) {
        textRendererComponent.setText(text);
        textRendererComponent.setStyle(
            new TextStyleBuilder()
                .setFont(font)
                .setFgColor(textColor)
                .setAlignment(TextStyle.TextAlign.ALIGN_CENTER)
                .get()
        );

        var thiz = this;

        addComponents(
            new UserInputHandlerComponent() {
                @Override
                protected void update(UserInputState state) {
                    getComponent(TextRendererBehavior.class)
                        .setUnderlined(isHovered(state));
                }

                @Override
                protected void onMouseDown(MouseEvent me) {
                    clickListeners.forEach(i -> i.execute(thiz, me));
                }
            },
            new RoundedRectRendererBehavior(20, border, background),
            textRendererComponent,
            hoverDetectionComponent
        );

        textRendererComponent.resizeToFit(10);
    }

    public interface ButtonClickListener {
        void execute(ButtonEntity button, MouseEvent event);
    }

    public ButtonEntity addClickListener(ButtonClickListener listener) {
        clickListeners.add(listener);
        return this;
    }

    public boolean isHovered(UserInputState state) {
        return hoverDetectionComponent.isHovered(state);
    }
}
