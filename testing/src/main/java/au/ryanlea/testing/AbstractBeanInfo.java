package au.ryanlea.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;

public class AbstractBeanInfo extends SimpleBeanInfo {

    private final Logger log;

    private final Class beanClass;

    private PropertyDescriptor[] propertyDescriptors;

    public AbstractBeanInfo(Class beanClass) {
        this.beanClass = beanClass;
        this.log = LoggerFactory.getLogger(AbstractBeanInfo.class);
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        if (propertyDescriptors == null) {
            collectPropertyDescriptors();
        }
        return propertyDescriptors;
    }

    private void collectPropertyDescriptors() {
        java.util.List<Field> fields = new ArrayList<>();
        fields.addAll(asList(beanClass.getDeclaredFields()));
        Class<?> parent = beanClass.getSuperclass();
        while (parent != null) {
            fields.addAll(asList(parent.getDeclaredFields()));
            parent = parent.getSuperclass();
        }

        final java.util.List<PropertyDescriptor> propertyDescriptors =

                fields.stream().filter(field -> !Modifier.isStatic(field.getModifiers()))
                        .map(p -> {
                            final String propertyName = p.getName();
                            final Method readMethod = findReadMethod(p);
                            final Method writeMethod = findWriteMethod(p);

                            try {
                                return new PropertyDescriptor(
                                        propertyName,
                                        readMethod,
                                        writeMethod
                                );

                            } catch (IntrospectionException e) {
                                log.warn("Failed to create property descriptor for: " + propertyName, e);
                                return null;
                            }
                        }).collect(Collectors.toList());
        this.propertyDescriptors = propertyDescriptors.toArray(new PropertyDescriptor[propertyDescriptors.size()]);
    }

    private Method findReadMethod(final Field field) {
        for (Method method : beanClass.getMethods()) {
            final String fieldName = field.getName();
            final Class<?> fieldType = field.getType();
            final String methodName = method.getName();
            final boolean methodReturnsValidType = fieldType.isAssignableFrom(method.getReturnType());
            final boolean methodTakesNoArgs = method.getParameterCount() == 0;
            final boolean validMethodSignature = methodTakesNoArgs && methodReturnsValidType;
            if (fieldName.equals(methodName) && validMethodSignature) {
                return method;
            } else if (("is" + capitalize(fieldName)).equals(methodName) && validMethodSignature) {
                return method;
            } else if (("get" + capitalize(fieldName)).equals(methodName) && validMethodSignature) {
                return method;
            }
        }
        return null;
    }

    private Method findWriteMethod(final Field field) {
        for (Method method : beanClass.getMethods()) {
            final String fieldName = field.getName();
            final Class<?> fieldType = field.getType();
            final String methodName = method.getName();
            final boolean methodTakesSingleArg = method.getParameterCount() == 1;
            final boolean methodAcceptsValidType = methodTakesSingleArg && fieldType.isAssignableFrom(method.getParameterTypes()[0]);
            final boolean validMethodSignature = methodTakesSingleArg && methodAcceptsValidType;
            if (fieldName.equals(methodName) && validMethodSignature) {
                return method;
            } else if (("set" + capitalize(fieldName)).equals(methodName)&& validMethodSignature) {
                return method;
            }
        }
        return null;
    }

    /**
     * Returns a String which capitalizes the first letter of the string.
     */
    private static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }

}
