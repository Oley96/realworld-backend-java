package app.demo.realworld.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtil {
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    private SlugUtil() {}

    public static String slugify(String input) {
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");

        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static String slugifyWithUniquePart(String input) {
        String uniquePart = RandomStringUtils.randomAlphabetic(12);

        return String.join("-", slugify(input), uniquePart).toLowerCase(Locale.ENGLISH);
    }

    public static String slugifyWithUniquePart(String input, String uniquePart) {
        return String.join("-", slugify(input), uniquePart).toLowerCase(Locale.ENGLISH);
    }

    public static String getUniquePart(String slug) {
        String[] strings = slug.split("-");

        return strings[strings.length - 1];
    }
}
