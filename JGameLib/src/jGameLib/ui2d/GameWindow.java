package jGameLib.ui2d;

import jGameLib.util.math.Vec2;
import jGameLib.util.math.Vec2i;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A basic class representing a game window.
 */
public class GameWindow {
    public final GameCanvas canvas;

    /**
     * Constructs a GameWindow with a minimum size of 1280 x 720 and a reference resolution of 1920 x 1080. See
     * {@link GameWindow#GameWindow(String, Vec2, Vec2)} for details on reference resolution.
     */
    public GameWindow(String title) {
        this(title, new Vec2(1280, 720));
    }

    /**
     * Constructs a GameWindow with a minimum size and a reference resolution of 1920 x 1080. See
     * {@link GameWindow#GameWindow(String, Vec2, Vec2)} for details on reference resolution.
     *
     * @param title         Title of the window
     * @param minWindowSize Minimum size of the window, in pixels
     */
    public GameWindow(String title, Vec2 minWindowSize) {
        this(title, minWindowSize, new Vec2(1920, 1080));
    }

    /**
     * Constructs a GameWindow with a minimum size and a custom reference resolution
     *
     * @param title                     Title of the window
     * @param minWindowSize             Minimum size of the window, in pixels
     * @param referenceWindowResolution The default of the drawing window, in pixels. When the window size, excluding
     *                                  the border, is equal to {@code referenceWindowResolution}, length on the
     *                                  {@link GameWindow#canvas} is measured in pixels. Otherwise, length is
     *                                  automatically scaled down to fit the window size. This allows you to avoid
     *                                  coding logic to respond to different window sizes. Pressing F11 while the
     *                                  window is open will toggle between normal and "borderless" (fullscreen) mode,
     *                                  but this may not work on some devices.
     */
    public GameWindow(String title, Vec2 minWindowSize, Vec2 referenceWindowResolution) {
        canvas = new GameCanvas(new Vec2i(referenceWindowResolution));

        new JFrame() {
            {
                // Init window
                setTitle(title);
                setDefaultCloseOperation(EXIT_ON_CLOSE);

                // Get window border
                pack();
                Vec2 border_size = new Vec2(getSize()).minus(new Vec2(getContentPane().getSize()));

                // Init canvas
                add(canvas.canvas);

                // Set size
                setMinimumSize(new Vec2i(minWindowSize.plus(border_size)).toDimension());
                setExtendedState(JFrame.MAXIMIZED_BOTH);

                // Set visible
                setVisible(true);

                // Add key listener
                addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_F11) {
                            dispose();
                            setUndecorated(!isUndecorated());
                            setVisible(true);
                            repaint();
                        }
                    }
                });
            }
        };
    }

    /**
     * Enables hardware acceleration for rendering and images
     * Usually, this can improve performance by up to 5x, at
     * least in my experience
     * <p>
     * Make sure to call this method before instantiating a
     * {@link GameWindow}, in order to ensure it affects the
     * rest of your code. THIS IS VERY IMPORTANT.
     */
    public static void enableHardwareAcceleration() {
        // Enable hardware acceleration using opengl (faster than default settings)
        System.setProperty("sun.java2d.opengl", "True");
        // Enable caching images as soon as they are loaded
        System.setProperty("sun.java2d.accthreshold", "0");
    }
}