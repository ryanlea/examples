package au.ryanlea.mutablesarray;

public abstract class ClassUtils {

    public static Class<?>[] toTypes(Object ... args) {
        Class<?>[] types = null;
        if (args != null) {
            types = new Class[args.length];
            for (int j = 0; j < args.length; j++) {
                types[j] = args[j].getClass();
            }
        }
        return types;
    }
}
