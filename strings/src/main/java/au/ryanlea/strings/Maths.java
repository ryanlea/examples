package au.ryanlea.strings;

/**
 * Created by ryan on 26/10/15.
 */
public class Maths {

    public static boolean isPowerOf2(int x) {
        return (x & (x - 1)) == 0;
    }

    public static int nextPowerOf2(int x) {
        x |= (x >> 1);
        x |= (x >> 2);
        x |= (x >> 4);
        x |= (x >> 8);
        x |= (x >> 16);
        return x + 1;
    }

}
