package og;

import java.awt.Color;

/** Light-theme color palette for OG cards. */
public final class Palette {

    // Background & chrome
    public static final String BG        = "#ffffff";
    public static final String BORDER    = "#d8d8e0";
    public static final String TEXT       = "#1a1a2e";
    public static final String TEXT_MUTED = "#6b7280";
    public static final String OLD_BG     = "#fef2f2";
    public static final String MODERN_BG  = "#eff6ff";
    public static final String OLD_ACCENT = "#dc2626";
    public static final String GREEN      = "#059669";
    public static final String ACCENT     = "#6366f1";
    public static final String BADGE_BG   = "#f3f4f6";

    // Syntax highlighting (VS Code light-inspired)
    public static final String SYN_KEYWORD    = "#7c3aed"; // purple
    public static final String SYN_TYPE       = "#0e7490"; // teal
    public static final String SYN_STRING     = "#059669"; // green
    public static final String SYN_COMMENT    = "#6b7280"; // gray
    public static final String SYN_ANNOTATION = "#b45309"; // amber
    public static final String SYN_NUMBER     = "#c2410c"; // orange
    public static final String SYN_DEFAULT    = "#1a1a2e"; // dark

    public static Color color(String hex) {
        return Color.decode(hex);
    }

    private Palette() {}
}
