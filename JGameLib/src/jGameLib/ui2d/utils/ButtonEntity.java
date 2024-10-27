package jGameLib.ui2d.utils;

import jGameLib.core.GameState;
import jGameLib.ui2d.input.MouseEvent;
import jGameLib.ui2d.input.UserInputHandlerComponent;
import jGameLib.ui2d.input.UserInputState;
import jGameLib.ui2d.rendering.UIEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ButtonEntity extends UIEntity {
    public final TextRendererComponent textRendererComponent = new TextRendererComponent("");
    public final HoverDetectionComponent hoverDetectionComponent = new HoverDetectionComponent();

    private boolean isHovered;
    private final List<ButtonClickListener> clickListeners = new ArrayList<>();

    public ButtonEntity(GameState state, String text, Color background, Color border, TextStyle style) {
        super(state);
        textRendererComponent.setText(text);
        textRendererComponent.setStyle(style);

        addComponents(
            new UserInputHandlerComponent() {
                @Override
                protected void update(UserInputState state) {
                    isHovered = hoverDetectionComponent.contains(state.getMousePosition());
                    getComponent(TextRendererComponent.class)
                        .setUnderlined(isHovered);
                }

                @Override
                protected void onMouseDown(MouseEvent me) {
                    if (hoverDetectionComponent.contains(me.position())) {
                        clickListeners.forEach(i -> i.execute(ButtonEntity.this, me));
                    }
                }
            },
            new RoundedRectRendererComponent(style.font().getSize() * 0.8, border, background),
            textRendererComponent,
            hoverDetectionComponent
        );

        textRendererComponent.resizeToFit(style.font().getSize() * 0.5);
    }

    public interface ButtonClickListener {
        void execute(ButtonEntity button, MouseEvent event);
    }

    public ButtonEntity addClickListener(ButtonClickListener listener) {
        clickListeners.add(listener);
        return this;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public boolean isHovered(UserInputState state) {
        return hoverDetectionComponent.isHovered(state);
    }
}
