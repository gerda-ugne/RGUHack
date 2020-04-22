package gerda.arcfej.dreamrealm.utils;

public class StringUtils {

    private StringUtils() {

    }

    /**
     * Repeats a String n times.
     * From: https://stackoverflow.com/a/32303117
     *
     * @param str The String to repeat
     * @param count The number of times to repeat str
     * @return The str repeated count of times.
     */
    public static String repeat(String str, int count) {
        StringBuilder buf = new StringBuilder(str.length() * count);
        while (count-- > 0) {
            buf.append(str);
        }
        return buf.toString();
    }
}
