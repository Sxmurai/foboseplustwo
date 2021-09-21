package me.fobose.client.util;

import java.util.regex.Pattern;

public class TextUtil {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + "\u00a7" + "[0-9A-FK-OR]");
    public static final String BLACK = "\u00a70";
    public static final String DARK_BLUE = "\u00a71";
    public static final String DARK_GREEN = "\u00a72";
    public static final String DARK_AQUA = "\u00a73";
    public static final String DARK_RED = "\u00a74";
    public static final String DARK_PURPLE = "\u00a75";
    public static final String GOLD = "\u00a76";
    public static final String GRAY = "\u00a77";
    public static final String DARK_GRAY = "\u00a78";
    public static final String BLUE = "\u00a79";
    public static final String GREEN = "\u00a7a";
    public static final String AQUA = "\u00a7b";
    public static final String RED = "\u00a7c";
    public static final String LIGHT_PURPLE = "\u00a7d";
    public static final String YELLOW = "\u00a7e";
    public static final String WHITE = "\u00a7f";
    public static final String OBFUSCATED = "\u00a7k";
    public static final String BOLD = "\u00a7l";
    public static final String STRIKE = "\u00a7m";
    public static final String UNDERLINE = "\u00a7n";
    public static final String ITALIC = "\u00a7o";
    public static final String RESET = "\u00a7r";
    public static final String RAINBOW = "\u00a7+";
    public static final String blank = " \u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592";
    public static String shrug = "\u00af\\_(\u30c4)_/\u00af";

    public static String stripColor(String input) {
        if (input == null) {
            return null;
        }
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String coloredString(String string, Color color) {
        return color.getCode() + string + RESET; // @todo make these static vars into the enum
    }

    public enum Color {
        NONE(""),
        WHITE("\u00a7f"),
        BLACK("\u00a70"),
        DARK_BLUE("\u00a71"),
        DARK_GREEN("\u00a72"),
        DARK_AQUA("\u00a73"),
        DARK_RED("\u00a74"),
        DARK_PURPLE("\u00a75"),
        GOLD("\u00a76"),
        GRAY("\u00a77"),
        DARK_GRAY("\u00a78"),
        BLUE("\u00a79"),
        GREEN("u00a7a"),
        AQUA("\u00a7b"),
        RED("\u00a7c"),
        LIGHT_PURPLE("\u00a7d"),
        YELLOW("\u00a7e");

        private String code;
        Color(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}

