package au.ryanlea.strings;

import java.util.Objects;

/**
 * String used to represent characters within the byte range, 0-255.
 *
 * https://en.wikipedia.org/wiki/ISO/IEC_8859-1
 */
public class ISO_8859_1String implements CharSequence {

    private byte[] value = new byte[0];

    private int start;

    private int end;

    private int hash;

    public ISO_8859_1String(CharSequence cs) {
        value(cs);
    }

    @Override
    public int length() {
        return end - start;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }

    public ISO_8859_1String value(CharSequence cs) {
        reset();
        final int length = cs.length();
        expandValue(length);
        for (int i = 0; i < length; i++, end++) {
            value[i] = (byte) cs.charAt(i);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ISO_8859_1String) {
            final ISO_8859_1String s = (ISO_8859_1String)o;
            if (s.length() == length()) {
                byte[] v = s.value;
                for (int i = 0; i < end; i++) {
                    if (value[i] != v[i]) {
                        return false;
                    }
                }
                return true;
            }
        } else if (o instanceof CharSequence) {
            final CharSequence cs = (CharSequence)o;
            if (cs.length() == length()) {
                for (int i = 0; i < end; i++) {
                    if (value[i] != cs.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            byte val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }

    private ISO_8859_1String reset() {
        start = 0;
        end = 0;
        return this;
    }

    private void expandValue(int size) {
        if (size > value.length) {
            final byte[] tmp = new byte[Maths.nextPowerOf2(size)];
            System.arraycopy(value, 0, tmp, 0, value.length);
            value = tmp;
        }
    }

}
