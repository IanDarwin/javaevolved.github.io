package og;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/** Tokenizes Java source lines into colored segments for SVG and PNG rendering. */
public final class SyntaxHighlighter {

    public record Token(String text, String color) {}

    static final Set<String> JAVA_KEYWORDS = Set.of(
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
        "class", "const", "continue", "default", "do", "double", "else", "enum",
        "extends", "final", "finally", "float", "for", "goto", "if", "implements",
        "import", "instanceof", "int", "interface", "long", "native", "new", "null",
        "package", "private", "protected", "public", "record", "return", "sealed",
        "short", "static", "strictfp", "super", "switch", "synchronized", "this",
        "throw", "throws", "transient", "try", "var", "void", "volatile", "when",
        "while", "with", "yield", "permits", "non-sealed", "module", "open", "opens",
        "requires", "exports", "provides", "to", "uses", "transitive",
        "true", "false"
    );

    static final Pattern SYN_PATTERN = Pattern.compile(
        "(?<comment>//.*)|" +
        "(?<blockcomment>/\\*.*?\\*/)|" +
        "(?<annotation>@\\w+)|" +
        "(?<string>\"\"\"[\\s\\S]*?\"\"\"|\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*')|" +
        "(?<number>\\b\\d[\\d_.]*[dDfFlL]?\\b)|" +
        "(?<word>\\b[A-Za-z_]\\w*\\b)|" +
        "(?<other>[^\\s])"
    );

    /** Tokenize a line of Java code into colored segments. */
    public static List<Token> tokenize(String line) {
        if (line.equals("...")) return List.of(new Token("...", null));
        var tokens = new ArrayList<Token>();
        var m = SYN_PATTERN.matcher(line);
        int last = 0;
        while (m.find()) {
            if (m.start() > last)
                tokens.add(new Token(line.substring(last, m.start()), null));
            last = m.end();
            var text = m.group();
            String color = null;
            if (m.group("comment") != null || m.group("blockcomment") != null) {
                color = Palette.SYN_COMMENT;
            } else if (m.group("annotation") != null) {
                color = Palette.SYN_ANNOTATION;
            } else if (m.group("string") != null) {
                color = Palette.SYN_STRING;
            } else if (m.group("number") != null) {
                color = Palette.SYN_NUMBER;
            } else if (m.group("word") != null) {
                if (JAVA_KEYWORDS.contains(text)) {
                    color = Palette.SYN_KEYWORD;
                } else if (Character.isUpperCase(text.charAt(0))) {
                    color = Palette.SYN_TYPE;
                }
            }
            tokens.add(new Token(text, color));
        }
        if (last < line.length())
            tokens.add(new Token(line.substring(last), null));
        return tokens;
    }

    /** Convert tokens to SVG tspan fragments. */
    public static String tokensToSvg(List<Token> tokens) {
        var sb = new StringBuilder();
        for (var t : tokens) {
            if (t.color() != null) {
                sb.append("<tspan fill=\"").append(t.color()).append("\">")
                  .append(ContentLoader.xmlEscape(t.text())).append("</tspan>");
            } else {
                sb.append(ContentLoader.xmlEscape(t.text()));
            }
        }
        return sb.toString();
    }

    private SyntaxHighlighter() {}
}
