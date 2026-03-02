package og;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/** Downloads and registers Inter + JetBrains Mono fonts. */
public final class FontManager {

    static final Path FONT_CACHE = Path.of(
        System.getProperty("user.home"), ".cache", "javaevolved-fonts");

    static final Map<String, String> FONT_URLS = Map.of(
        "Inter-Regular.ttf",
            "https://fonts.gstatic.com/s/inter/v20/UcCO3FwrK3iLTeHuS_nVMrMxCp50SjIw2boKoduKmMEVuLyfMZg.ttf",
        "Inter-Medium.ttf",
            "https://fonts.gstatic.com/s/inter/v20/UcCO3FwrK3iLTeHuS_nVMrMxCp50SjIw2boKoduKmMEVuI6fMZg.ttf",
        "Inter-SemiBold.ttf",
            "https://fonts.gstatic.com/s/inter/v20/UcCO3FwrK3iLTeHuS_nVMrMxCp50SjIw2boKoduKmMEVuGKYMZg.ttf",
        "Inter-Bold.ttf",
            "https://fonts.gstatic.com/s/inter/v20/UcCO3FwrK3iLTeHuS_nVMrMxCp50SjIw2boKoduKmMEVuFuYMZg.ttf",
        "JetBrainsMono-Regular.ttf",
            "https://fonts.gstatic.com/s/jetbrainsmono/v24/tDbY2o-flEEny0FZhsfKu5WU4zr3E_BX0PnT8RD8yKxjPQ.ttf",
        "JetBrainsMono-Medium.ttf",
            "https://fonts.gstatic.com/s/jetbrainsmono/v24/tDbY2o-flEEny0FZhsfKu5WU4zr3E_BX0PnT8RD8-qxjPQ.ttf"
    );

    /** Download fonts to cache and register with Java's graphics environment. */
    public static void ensureFonts() throws IOException {
        Files.createDirectories(FONT_CACHE);
        var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (var entry : FONT_URLS.entrySet()) {
            var file = FONT_CACHE.resolve(entry.getKey());
            if (!Files.exists(file)) {
                System.out.println("Downloading %s...".formatted(entry.getKey()));
                try (var in = URI.create(entry.getValue()).toURL().openStream()) {
                    Files.copy(in, file);
                }
            }
            try {
                var font = Font.createFont(Font.TRUETYPE_FONT, file.toFile());
                ge.registerFont(font);
            } catch (FontFormatException e) {
                System.out.println("[WARN] Could not register font %s: %s"
                    .formatted(entry.getKey(), e.getMessage()));
            }
        }
    }

    /** Load a specific font file at the given size. */
    public static Font getFont(String filename, float size) {
        try {
            var path = FONT_CACHE.resolve(filename);
            return Font.createFont(Font.TRUETYPE_FONT, path.toFile()).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException("Cannot load font: " + filename, e);
        }
    }

    private FontManager() {}
}
