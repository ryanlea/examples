package au.ryanlea.mutablesarray;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import static au.ryanlea.mutablesarray.ClassUtils.toTypes;

public abstract class ArrayUtils {

    public static class ArrayExpansionException extends RuntimeException {
        public ArrayExpansionException(Throwable cause) {
            super(cause);
        }
    }

    public static <T> T[] expand(T[] arr, int required, Object... args) {
        if (arr.length < required) {
            Class<?> componentType = arr.getClass().getComponentType();
            //noinspection unchecked
            T[] tmp = (T[]) Array.newInstance(componentType, nextPowerOf2(required, 2));
            System.arraycopy(arr, 0, tmp, 0, arr.length);
            for (int i = arr.length; i < tmp.length; i++) {
                try {
                    Class<?>[] paramTypes = toTypes(args);
                    //noinspection unchecked
                    tmp[i] = (T) componentType.getConstructor(paramTypes).newInstance(args);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new ArrayExpansionException(e);
                }
            }
            return tmp;
        }
        return arr;
    }

    public static <T> T remove(T[] arr, int idx) {
        int srcPos = idx + 1;
        T removed = arr[idx];
        System.arraycopy(arr, srcPos, arr, idx, arr.length - srcPos);
        arr[arr.length - 1] = removed;
        return removed;
    }

    static int nextPowerOf2(int n, int min)
    {
        int p = 1;
        if (n < min) {
            return min;
        }

        while (p < n) {
            p <<= 1;
        }
        return p;
    }
}
