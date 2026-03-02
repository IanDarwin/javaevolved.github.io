package og;

import java.util.ArrayList;
import java.util.List;

/** Card dimensions and code-fitting helpers. */
public final class Layout {

    public static final int W = 1200, H = 630;
    public static final int PAD = 40;
    public static final int HEADER_H = 100;
    public static final int FOOTER_H = 56;
    public static final int CODE_TOP = HEADER_H;
    public static final int CODE_H = H - HEADER_H - FOOTER_H;
    public static final int COL_W = (W - PAD * 2 - 20) / 2;   // 20px gap between panels
    public static final int CODE_PAD = 14;
    public static final int LABEL_H = 32;
    public static final int USABLE_W = COL_W - CODE_PAD * 2;
    public static final int USABLE_H = CODE_H - LABEL_H - CODE_PAD;
    public static final double CHAR_WIDTH_RATIO = 0.6;
    public static final double LINE_HEIGHT_RATIO = 1.55;
    public static final int MIN_CODE_FONT = 9;
    public static final int MAX_CODE_FONT = 16;

    /** Compute the best font size (MIN–MAX) that fits both code blocks. */
    public static int bestFontSize(List<String> oldLines, List<String> modernLines) {
        int maxChars = Math.max(
            oldLines.stream().mapToInt(String::length).max().orElse(1),
            modernLines.stream().mapToInt(String::length).max().orElse(1)
        );
        int maxLines = Math.max(oldLines.size(), modernLines.size());
        int byWidth  = (int) (USABLE_W / (maxChars * CHAR_WIDTH_RATIO));
        int byHeight = (int) (USABLE_H / (maxLines * LINE_HEIGHT_RATIO));
        return Math.max(MIN_CODE_FONT, Math.min(MAX_CODE_FONT, Math.min(byWidth, byHeight)));
    }

    /** Truncate lines to fit the panel height at the given font size. */
    public static List<String> fitLines(List<String> lines, int fontSize) {
        int lineH = (int) (fontSize * LINE_HEIGHT_RATIO);
        int maxLines = USABLE_H / lineH;
        if (lines.size() <= maxLines) return lines;
        var truncated = new ArrayList<>(lines.subList(0, maxLines - 1));
        truncated.add("...");
        return truncated;
    }

    private Layout() {}
}
