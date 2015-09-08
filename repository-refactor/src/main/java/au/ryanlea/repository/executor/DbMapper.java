package au.ryanlea.repository.executor;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbMapper<T> implements Mapper<T> {

    @Override
    public void populate(final Response<T> response, final List rows){
        final Type[] types = ((ParameterizedType) response.getClass().getGenericInterfaces()[0]).getActualTypeArguments();
        if (types != null && types.length > 0) {
            final Type type = types[0];
            try {
                @SuppressWarnings("unchecked")
                final Class<T> objClass = (Class<T>)Class.forName(type.getTypeName());
                final List<T> results = buildResults(rows, objClass);
                response.setResults(results);
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private List<T> buildResults(final List rows, final Class<T> objClass){
        final List<T> results = new ArrayList<>();
        for(Object row : rows){
            try {
                if(!(row instanceof Map))
                    throw new UnexpectedException("This is unexpected. Each row in a resultset needs to be a list");

                final Map map = (Map) row;

                final T obj = objClass.newInstance();
                for (PropertyDescriptor propertyDescriptor :
                        Introspector.getBeanInfo(objClass).getPropertyDescriptors()) {

                    final String prop = propertyDescriptor.getName();
                    if (prop.equalsIgnoreCase("class")) continue;

                    final Method setter = propertyDescriptor.getWriteMethod();
                    if(setter.isAnnotationPresent(Ignore.class)) continue;

                    final Object value = map.get(prop);
                    setter.invoke(obj, value);
                }
                results.add(obj);
            } catch (final IntrospectionException |
                    UnexpectedException |
                    IllegalAccessException |
                    InstantiationException |
                    InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}