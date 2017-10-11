package com.sinosoft.common;


import java.lang.annotation.Annotation;

/**
 *
 */
public interface InstanceProvider {

	<T> T getInstance(Class<T> beanType);

	<T> T getInstance(Class<T> beanType, String beanName);

    <T> T getInstance(Class<T> beanType, Class<? extends Annotation> annotationType);
}
