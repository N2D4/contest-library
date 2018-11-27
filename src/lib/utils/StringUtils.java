package lib.utils;

import lib.polyfill.PolyfillIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class StringUtils {

    private StringUtils() {
        // Quite dusty here...
    }

    public static int indexOf(String s, String sub) {
        return indexOf(s, sub, 0);
    }

    public static int indexOf(String s, String sub, int start) {
        Iterator<Integer> it = indicesOf(s, sub, start);
        return it.hasNext() ? it.next() : -1;
    }


    public static Iterator<Integer> indicesOf(String s, String sub) {
        return indicesOf(s, sub);
    }

    public static Iterator<Integer> indicesOf(final String s, final String sub, final int start) {

        final int[] jumpBacks = new int[sub.length()];
        for (int i = 1; i < sub.length(); i++) {
            int h = jumpBacks[i-1];
            jumpBacks[i] = sub.charAt(i) == sub.charAt(h) ? h + 1 : 0;
        }

        return new PolyfillIterator<Integer>() {
            int i = start;
            int j = 0;
            int next = -1;

            @Override
            public boolean hasNext() {
                if (sub.length() >= s.length()) return false;
                if (sub.length() <= 0) next = i;

                while (next < 0) {
                    if (i >= s.length()) return false;

                    if (s.charAt(i) == sub.charAt(j)) {
                        i++;
                        j++;
                    } else if (j == 0) {
                        i++;
                    } else {
                        j = jumpBacks[j - 1];
                    }

                    if (j >= sub.length()) {
                        next = i - j;
                        j = jumpBacks[j - 1];
                    }
                }
                return true;
            }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                int n = next;
                next = -1;
                return n;
            }

            void prepareNext() {

            }
        };
    }

}
