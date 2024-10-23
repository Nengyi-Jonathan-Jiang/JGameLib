package jGameLib.util.files;

import jGameLib.util.caches.Cache;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    private FileUtil() {
    }

    public static @NotNull InputStream readFileAsStream(String path) {
        InputStream stream = FileUtil.class.getResourceAsStream("/" + path);
        if (stream == null) throw new RuntimeException("Could not read file: " + path);
        return stream;
    }

    private static final Cache<String, byte[]> fileCache = new Cache<>();

    public static byte[] readFileAsBytes(String path) {
        return fileCache.getOrCompute(path, () -> {
            try (InputStream stream = readFileAsStream(path)) {
                return stream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException("Error reading file contents: " + path, e);
            }
        });
    }

    public static String readFileAsText(String path) {
        return new String(readFileAsBytes(path));
    }
}
