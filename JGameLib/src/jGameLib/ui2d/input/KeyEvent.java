package jGameLib.ui2d.input;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public record KeyEvent(
    Key key,
    char keyChar,
    KeyLocation keyLocation,
    KeyEventType keyEventType,
    boolean isShiftDown,
    boolean isAltDown,
    boolean isCtrlDown
) {
    public enum KeyEventType {
        KEY_DOWN,
        KEY_UP
    }

    public enum KeyLocation {
        LEFT, RIGHT, NUMPAD, UNKNOWN, NORMAL
    }

    private static final Map<Integer, Key> keys = new HashMap<>();

    public enum Key {
        K_BACK_SPACE(0x08),
        K_TAB(0x09),
        K_ENTER(0x10),
        K_SHIFT(0x10),
        K_CONTROL(0x11),
        K_ALT(0x12),
        K_PAUSE(0x13),
        K_CAPS_LOCK(0x14),
        K_ESCAPE(0x1B),
        K_SPACE(0x20),
        K_PAGE_UP(0x21),
        K_PAGE_DOWN(0x22),
        K_END(0x23),
        K_HOME(0x24),
        K_LEFT(0x25),
        K_UP(0x26),
        K_RIGHT(0x27),
        K_DOWN(0x28),
        K_COMMA(0x2C),
        K_MINUS(0x2D),
        K_PERIOD(0x2E),
        K_SLASH(0x2F),
        K_0(0x30),
        K_1(0x31),
        K_2(0x32),
        K_3(0x33),
        K_4(0x34),
        K_5(0x35),
        K_6(0x36),
        K_7(0x37),
        K_8(0x38),
        K_9(0x39),
        K_SEMICOLON(0x3B),
        K_EQUALS(0x3D),
        K_A(0x41),
        K_B(0x42),
        K_C(0x43),
        K_D(0x44),
        K_E(0x45),
        K_F(0x46),
        K_G(0x47),
        K_H(0x48),
        K_I(0x49),
        K_J(0x4A),
        K_K(0x4B),
        K_L(0x4C),
        K_M(0x4D),
        K_N(0x4E),
        K_O(0x4F),
        K_P(0x50),
        K_Q(0x51),
        K_R(0x52),
        K_S(0x53),
        K_T(0x54),
        K_U(0x55),
        K_V(0x56),
        K_W(0x57),
        K_X(0x58),
        K_Y(0x59),
        K_Z(0x5A),
        K_OPEN_BRACKET(0x5B),
        K_BACK_SLASH(0x5C),
        K_CLOSE_BRACKET(0x5D),
        K_MULTIPLY(0x6A),
        K_ADD(0x6B),
        K_SUBTRACT(0x6D),
        K_DECIMAL(0x6E),
        K_DIVIDE(0x6F),
        K_DELETE(0x7F),
        K_NUM_LOCK(0x90),
        K_SCROLL_LOCK(0x91),
        K_F1(0x70),
        K_F2(0x71),
        K_F3(0x72),
        K_F4(0x73),
        K_F5(0x74),
        K_F6(0x75),
        K_F7(0x76),
        K_F8(0x77),
        K_F9(0x78),
        K_F10(0x79),
        K_F11(0x7A),
        K_F12(0x7B),
        K_F13(0xF000),
        K_F14(0xF001),
        K_F15(0xF002),
        K_F16(0xF003),
        K_F17(0xF004),
        K_F18(0xF005),
        K_F19(0xF006),
        K_F20(0xF007),
        K_F21(0xF008),
        K_F22(0xF009),
        K_F23(0xF00A),
        K_F24(0xF00B),
        K_PRINTSCREEN(0x9A),
        K_INSERT(0x9B),
        K_HELP(0x9C),
        K_META(0x9D),
        K_BACK_QUOTE(0xC0),
        K_QUOTE(0xDE),
        K_KP_UP(0xE0),
        K_KP_DOWN(0xE1),
        K_KP_LEFT(0xE2),
        K_KP_RIGHT(0xE3),
        K_AMPERSAND(0x96),
        K_ASTERISK(0x97),
        K_QUOTEDBL(0x98),
        K_LESS(0x99),
        K_GREATER(0xa0),
        K_BRACELEFT(0xa1),
        K_BRACERIGHT(0xa2),
        K_AT(0x0200),
        K_COLON(0x0201),
        K_CIRCUMFLEX(0x0202),
        K_DOLLAR(0x0203),
        K_EXCLAMATION_MARK(0x0205),
        K_LEFT_PARENTHESIS(0x0207),
        K_NUMBER_SIGN(0x0208),
        K_PLUS(0x0209),
        K_RIGHT_PARENTHESIS(0x020A),
        K_UNDERSCORE(0x020B),
        K_UNKNOWN(0x0);

        Key(int code) {
            keys.put(code, this);
        }
    }

    public static final char CHAR_UNDEFINED = 0xFFFF;

    KeyEvent(java.awt.event.KeyEvent e, KeyEventType eventType) {
        this(
            keys.getOrDefault(e.getKeyCode(), Key.K_UNKNOWN),
            e.getKeyChar(),
            switch (e.getKeyLocation()) {
                case java.awt.event.KeyEvent.KEY_LOCATION_LEFT -> KeyLocation.LEFT;
                case java.awt.event.KeyEvent.KEY_LOCATION_RIGHT -> KeyLocation.RIGHT;
                case java.awt.event.KeyEvent.KEY_LOCATION_STANDARD -> KeyLocation.NORMAL;
                case java.awt.event.KeyEvent.KEY_LOCATION_NUMPAD -> KeyLocation.NUMPAD;
                case java.awt.event.KeyEvent.KEY_LOCATION_UNKNOWN -> KeyLocation.UNKNOWN;
                default -> throw new IllegalStateException("Unexpected value: " + e.getKeyLocation());
            },
            eventType,
            e.isShiftDown(),
            e.isAltDown(),
            e.isControlDown()
        );
    }
}
