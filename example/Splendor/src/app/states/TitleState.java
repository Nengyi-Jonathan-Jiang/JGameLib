package app.states;

import app.backend.CardDeck;
import app.backend.Gem;
import app.frontend.CardDeckEntity;
import app.frontend.GemEntity;
import jGameLib.ui2d.input.KeyEvent;
import jGameLib.ui2d.input.MouseEvent;
import jGameLib.ui2d.input.UserInputHandlerEntity;
import jGameLib.ui2d.rendering.UIEntity;
import jGameLib.ui2d.rendering.UIRendererRootComponent;
import jGameLib.ui2d.utils.RectRendererComponent;
import jGameLib.util.math.Vec2;
import jGameLib.util.math.Vec2i;

import java.awt.*;

public class TitleState extends BasicState {

    public TitleState() {
        new UIEntity(this)
            .withBoundingBox(b -> b.setSize(1920, 1080))
            .addComponent(new RectRendererComponent(null, Color.WHITE))
            .addComponent(new UIRendererRootComponent());

        new GemEntity(this, Gem.BLACK)
            .addComponent(new UIRendererRootComponent());

        CardDeck deck = new CardDeck();
        CardDeckEntity cardDeckEntity = new CardDeckEntity(this, deck)
            .withBoundingBox(b -> b.setTopRight(new Vec2(940, -520)))
            .addComponent(new UIRendererRootComponent())
            .cast();

        new UserInputHandlerEntity(this){
            @Override
            protected void onMouseDown(MouseEvent me) {
                if(me.button() == MouseEvent.MouseButton.LEFT) {
                    Vec2i hoveredCard = cardDeckEntity.getHoveredCard();
                    if (hoveredCard != null) {
                        deck.faceUpCards[hoveredCard.y()][hoveredCard.x()] = null;
                        deck.replenishFaceUpCards();
                    }
                }
            }

            @Override
            protected void onKeyDown(KeyEvent ke) {
                if(ke.key() == KeyEvent.Key.K_R) {
                    setNextState(new TitleState());
                }
            }
        };
    }
}