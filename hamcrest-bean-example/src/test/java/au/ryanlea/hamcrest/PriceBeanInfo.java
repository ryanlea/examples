package au.ryanlea.hamcrest;

import au.ryanlea.testing.AbstractBeanInfo;
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

public class PriceBeanInfo extends AbstractBeanInfo {

    public PriceBeanInfo() {
        super(Price.class);
    }

}
