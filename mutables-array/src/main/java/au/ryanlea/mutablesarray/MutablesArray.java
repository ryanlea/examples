package au.ryanlea.mutablesarray;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.function.Predicate;

import static au.ryanlea.mutablesarray.ArrayUtils.expand;

public class MutablesArray<T extends Mutable<T>> implements Iterable<T> {

    private final MutablesIterator mutablesIterator = new MutablesIterator();

    private T[] mutables;

    private int numMutables;

    public MutablesArray(Class<T> cls, int initialSize) {
        //noinspection unchecked
        mutables = expand((T[]) Array.newInstance(cls, 0), initialSize);
        numMutables = 0;
    }

    public T create() {
        ensureCapacity();
        return mutables[numMutables++].reset();
    }

    public MutablesArray<T> clear() {
        numMutables = 0;
        return this;
    }

    public int size() {
        return numMutables;
    }

    public T get(int i) {
        return mutables[i];
    }

    public Iterator<T> iterator() {
        return mutablesIterator.reset();
    }

    public T find(Predicate<T> predicate) {
        for (int i = 0; i < numMutables; i++) {
            final T mutable = mutables[i];
            if (predicate.test(mutable)) {
                return mutable;
            }
        }
        return null;
    }

    private void ensureCapacity() {
        int required = numMutables + 1;
        if (required > mutables.length) {
            mutables = expand(mutables, required);
        }
    }

    private class MutablesIterator implements Iterator<T> {

        private int idx;

        public MutablesIterator reset() {
            idx = 0;
            return this;
        }

        @Override
        public boolean hasNext() {
            return idx < numMutables;
        }

        @Override
        public T next() {
            return mutables[idx++];
        }
    }
}
