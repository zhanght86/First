package com.sinosoft.common;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.sinosoft.log.exception.IocInstanceNotFoundException;
import com.sinosoft.log.exception.IocInstanceNotUniqueException;

/**
 * <p>
 */
public class InstanceFactory {

    private static InstanceProvider instanceProvider;

    private InstanceFactory() {
    }

    /**
     *
     * @param provider    */
    public static void setInstanceProvider(InstanceProvider provider) {
        instanceProvider = provider;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> beanType) {
        T result = null;
        if (instanceProvider != null) {
            result = getInstanceFromProvider(beanType);
        }
        if (result != null) {
            return result;
        }
        result = getInstanceFromServiceLoader(beanType);
        if (result != null) {
            return result;
        }
        result = (T) instances.get(beanType);
        if (result != null) {
            return result;
        }
        throw new IocInstanceNotFoundException("There's not bean of type '" + beanType + "' exists in IoC container!");
    }

    private static <T> T getInstanceFromProvider(Class<T> beanType) {
        try {
            return instanceProvider.getInstance(beanType);
        } catch (IocInstanceNotFoundException e) {
            return null;
        }
    }

    private static <T> T getInstanceFromServiceLoader(Class<T> beanType) {
        Set<T> results = new HashSet<T>();
        for (T instance : ServiceLoader.load(beanType)) {
            results.add(instance);
        }
        if (results.size() > 1) {
            throw new IocInstanceNotUniqueException("There're more than one bean of type '" + beanType + "'");
        }
        if (results.size() == 1) {
            return results.iterator().next();
        }
        return null;
    }

    /**
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> beanType, String beanName) {
        T result = null;
        if (instanceProvider != null) {
            result = getInstanceFromProvider(beanType, beanName);
        }
        if (result != null) {
            return result;
        }
        result = getInstanceFromServiceLoader(beanType, beanName);
        if (result != null) {
            return result;
        }
        result = (T) instances.get(toName(beanType, beanName));
        if (result != null) {
            return result;
        }
        throw new IocInstanceNotFoundException("There's not bean of type '" + beanType + "' exists in IoC container!");
    }

    private static <T> T getInstanceFromProvider(Class<T> beanType, String beanName) {
        try {
            return instanceProvider.getInstance(beanType, beanName);
        } catch (IocInstanceNotFoundException e) {
            return null;
        }
    }

    private static <T> T getInstanceFromServiceLoader(Class<T> beanType, String beanName) {
        Set<T> results = new HashSet<T>();
        for (T instance : ServiceLoader.load(beanType)) {
            Component named = instance.getClass().getAnnotation(Component.class);
            if (named != null && beanName.equals(named.value())) {
                results.add(instance);
            }
        }
        if (results.size() > 1) {
            throw new IocInstanceNotUniqueException("There're more than one bean of type '"
                    + beanType + "' and named '" + beanName + "'");
        }
        if (results.size() == 1) {
            return results.iterator().next();
        }
        return null;

    }

    /**
     */
    @SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> beanType, Class<? extends Annotation> annotationType) {
        T result = null;
        if (instanceProvider != null) {
            result = getInstanceFromProvider(beanType, annotationType);
        }
        if (result != null) {
            return result;
        }
        result = getInstanceFromServiceLoader(beanType, annotationType);
        if (result != null) {
            return result;
        }
        result = (T) instances.get(toName(beanType, annotationType));
        if (result != null) {
            return result;
        }
        throw new IocInstanceNotFoundException("There's not bean '" 
                + annotationType + "' of type '" + beanType + "' exists in IoC container!");
    }

    private static <T> T getInstanceFromProvider(Class<T> beanType, Class<? extends Annotation> annotationType) {
        try {
            return instanceProvider.getInstance(beanType, annotationType);
        } catch (IocInstanceNotFoundException e) {
            return null;
        }
    }

    private static <T> T getInstanceFromServiceLoader(Class<T> beanType, Class<? extends Annotation> annotationType) {
        Set<T> results = new HashSet<T>();
        for (T instance : ServiceLoader.load(beanType)) {
            Annotation beanAnnotation = instance.getClass().getAnnotation(annotationType);
            if (beanAnnotation != null && beanAnnotation.annotationType().equals(annotationType)) {
                results.add(instance);
            }
        }
        if (results.size() > 1) {
            throw new IocInstanceNotUniqueException("There're more than one bean of type '"
                    + beanType + "' and annotated with '" + annotationType + "'");
        }
        if (results.size() == 1) {
            return results.iterator().next();
        }
        return null;
    }

    private static final Map<Object, Object> instances = new HashMap<Object, Object>();

    public static <T> void bind(Class<T> serviceInterface, T serviceImplementation) {
        instances.put(serviceInterface, serviceImplementation);
    }

    public static <T> void bind(Class<T> serviceInterface, T serviceImplementation, String beanName) {
        instances.put(toName(serviceInterface, beanName), serviceImplementation);
    }

    /**
     */
    public static void clear() {
        instances.clear();
    }

    public static <T> void bind(Class<T> serviceInterface, T serviceImplementation, Class<? extends Annotation> annotationType) {
        instances.put(toName(serviceInterface, annotationType), serviceImplementation);
    }

    private static String toName(Class<?> beanType, String beanName) {
        return beanType.getName() + ":" + beanName;
    }

    private static String toName(Class<?> beanType, Class<? extends Annotation> annotationType) {
        return beanType.getName() + ":" + annotationType.getName();
    }
}
