package compilador.util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static String load(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
