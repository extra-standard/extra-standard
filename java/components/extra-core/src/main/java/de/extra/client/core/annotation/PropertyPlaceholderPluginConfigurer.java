/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.extra.client.core.annotation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import de.extra.client.core.builder.IXmlComplexTypeBuilder;

/**
 * <pre>
 * This class extends the PropertyPlaceholderConfigurer so that we will able to
 * configure our plugins.
 * This code is taken and extended from:
 * http://stackoverflow.com/questions/317687/inject-property-value-into-spring-bean
 * </pre>
 * 
 * @author Leonid Potap
 * @version 1.0.0
 * @since 1.0.0
 */
public class PropertyPlaceholderPluginConfigurer extends PropertyPlaceholderConfigurer {

	private static final Logger LOG = LoggerFactory.getLogger(PropertyPlaceholderPluginConfigurer.class);

	private boolean ignoreNullValues = false;

	@Override
	protected void processProperties(final ConfigurableListableBeanFactory beanFactory, final Properties properties)
			throws BeansException {
		super.processProperties(beanFactory, properties);

		for (final String beanName : beanFactory.getBeanDefinitionNames()) {
			final Class<?> clazz = beanFactory.getType(beanName);

			if (LOG.isDebugEnabled()) {
				LOG.debug("Configuring properties for bean=" + beanName + "[" + clazz + "]");
			}

			if (clazz != null && clazz.isAnnotationPresent(PluginConfiguration.class)) {
				setPluginsProperties(beanFactory, properties, beanName, clazz);
			}

		}
	}

	/**
	 * 
	 * 
	 * @param beanFactory
	 * @param properties
	 * @param beanName
	 * @param clazz
	 */
	private void setPluginsProperties(final ConfigurableListableBeanFactory beanFactory, final Properties properties,
			final String beanName, final Class<?> clazz) {
		final PluginConfiguration annotationConfigutation = clazz.getAnnotation(PluginConfiguration.class);
		final MutablePropertyValues mutablePropertyValues = beanFactory.getBeanDefinition(beanName).getPropertyValues();

		for (final PropertyDescriptor property : BeanUtils.getPropertyDescriptors(clazz)) {
			final Method setter = property.getWriteMethod();
			PluginValue valueAnnotation = null;
			if (setter != null && setter.isAnnotationPresent(PluginValue.class)) {
				valueAnnotation = setter.getAnnotation(PluginValue.class);
			}
			if (valueAnnotation != null) {
				final String key = extractKey(annotationConfigutation, valueAnnotation, clazz);
				final String value = resolvePlaceholder(key, properties, SYSTEM_PROPERTIES_MODE_FALLBACK);
				if (StringUtils.isEmpty(value)) {
					throw new BeanCreationException(beanName, "No such property=[" + key + "] found in properties.");
				}
				if (LOG.isDebugEnabled()) {
					LOG.debug("setting property=[" + clazz.getName() + "." + property.getName() + "] value=[" + key
							+ "=" + value + "]");
				}
				mutablePropertyValues.addPropertyValue(property.getName(), value);
			}
		}

		for (final Field field : clazz.getDeclaredFields()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("examining field=[" + clazz.getName() + "." + field.getName() + "]");
			}
			if (field.isAnnotationPresent(PluginValue.class)) {
				final PluginValue valueAnnotation = field.getAnnotation(PluginValue.class);
				final PropertyDescriptor property = BeanUtils.getPropertyDescriptor(clazz, field.getName());

				if (property == null || property.getWriteMethod() == null) {
					throw new BeanCreationException(beanName, "setter for property=[" + clazz.getName() + "."
							+ field.getName() + "] not available.");
				}
				final String key = extractKey(annotationConfigutation, valueAnnotation, clazz);
				final Object value = resolvePlaceholder(key, properties, SYSTEM_PROPERTIES_MODE_FALLBACK);
				if (value != null) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("setting property=[" + clazz.getName() + "." + field.getName() + "] value=[" + key
								+ "=" + value + "]");
					}
					mutablePropertyValues.addPropertyValue(field.getName(), value);
				} else if (!ignoreNullValues) {
					throw new BeanCreationException(beanName, "No such property=[" + key + "] found in properties.");
				}
			}
		}
	}

	/**
	 * Findet Key Anhang Annotation.
	 * 
	 * @param annotation
	 * @return
	 */
	private String extractKey(final PluginConfiguration annotation, final PluginValue value, final Class<?> clazz) {
		try {
			final String initialKey = value.key();
			final String plugInBeanName = annotation.pluginBeanName();
			final PluginConfigType pluginConfigType = annotation.pluginType();
			final StringBuilder key = new StringBuilder();
			final String configPrefix = annotation.pluginType().getConfigPrefix();
			if (PluginConfigType.Builder == pluginConfigType) {
				key.append(configPrefix);
				final Object bean = clazz.newInstance();
				if (bean instanceof IXmlComplexTypeBuilder) {
					final IXmlComplexTypeBuilder iXmlComplexTypeBuilder = (IXmlComplexTypeBuilder) bean;
					final String xmlType = iXmlComplexTypeBuilder.getXmlType();
					final String xmlTypeKey = extractKeyFromXmlType(xmlType);
					key.append(".").append(xmlTypeKey);
					key.append(".").append(plugInBeanName);
					key.append(".").append(initialKey);
				} else {
					throw new BeanCreationException(clazz.getName(),
							" unexpected AnnotationType. Use PluginConfigType.Builder for IXmlComplexTypeBuilder");
				}

			} else {
				key.append(configPrefix).append(".").append(plugInBeanName).append(".").append(initialKey);
			}
			return key.toString();
		} catch (final IllegalAccessException illegalAccessException) {
			throw new BeanCreationException("IllegalAccessException", illegalAccessException);
		} catch (final InstantiationException instantiationException) {
			throw new BeanCreationException("InstantiationException", instantiationException);
		}

	}

	/**
	 * Berechnet Key aus dem xmlType indem ':' durch '.' replased wird
	 * 
	 * @param xmlType
	 * @return
	 */
	private String extractKeyFromXmlType(final String xmlType) {
		final String xmlTypeKey = StringUtils.replace(xmlType, ":", ".");
		return xmlTypeKey;
	}

	/**
	 * @param ignoreNullValues
	 *            the ignoreNullValues to set
	 */
	public void setIgnoreNullValues(final boolean ignoreNullValues) {
		this.ignoreNullValues = ignoreNullValues;
	}
}
