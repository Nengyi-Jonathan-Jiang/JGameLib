package jGameLib.core;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ToStringHelper {
    private ToStringHelper() {
    }

    public static <T> String toSingleLineList(Stream<T> items) {
        return items.map(Objects::toString).collect(Collectors.joining(", "));
    }

    public static <T> String toMultiLineList(Stream<T> items) {
        String s = "\n" + items.map(Objects::toString).collect(Collectors.joining(", \n"));
        if (s.equals("\n")) {
            return "";
        }
        return indent(s) + "\n";
    }

    public static String toMultiLineList(Object... items) {
        return toMultiLineList(Arrays.stream(items));
    }

    public static String indent(String str) {
        return str.replace("\n", "\n    ");
    }
}
