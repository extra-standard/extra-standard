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
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
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
public class PropertyPlaceholderPluginConfigurer extends
		PropertyPlaceholderConfigurer {

	private static final Logger logger = Logger
			.getLogger(PropertyPlaceholderPluginConfigurer.class);

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactory, Properties properties)
			throws BeansException {
		super.processProperties(beanFactory, properties);

		for (String beanName : beanFactory.getBeanDefinitionNames()) {
			Class<?> clazz = beanFactory.getType(beanName);

			if (logger.isDebugEnabled()) {
				logger.debug("Configuring properties for bean=" + beanName
						+ "[" + clazz + "]");
			}

			if (clazz != null
					&& clazz.isAnnotationPresent(PluginConfiguration.class)) {
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
	private void setPluginsProperties(
			ConfigurableListableBeanFactory beanFactory, Properties properties,
			String beanName, Class<?> clazz) {
		PluginConfiguration annotationConfigutation = clazz
				.getAnnotation(PluginConfiguration.class);
		MutablePropertyValues mutablePropertyValues = beanFactory
				.getBeanDefinition(beanName).getPropertyValues();

		for (PropertyDescriptor property : BeanUtils
				.getPropertyDescriptors(clazz)) {
			Method setter = property.getWriteMethod();
			PluginValue valueAnnotation = null;
			if (setter != null && setter.isAnnotationPresent(PluginValue.class)) {
				valueAnnotation = setter.getAnnotation(PluginValue.class);
			}
			if (valueAnnotation != null) {
				String key = extractKey(annotationConfigutation,
						valueAnnotation, beanName, beanFactory);
				String value = resolvePlaceholder(key, properties,
						SYSTEM_PROPERTIES_MODE_FALLBACK);
				if (StringUtils.isEmpty(value)) {
					throw new BeanCreationException(beanName,
							"No such property=[" + key
									+ "] found in properties.");
				}
				if (logger.isDebugEnabled()) {
					logger.debug("setting property=[" + clazz.getName() + "."
							+ property.getName() + "] value=[" + key + "="
							+ value + "]");
				}
				mutablePropertyValues.addPropertyValue(property.getName(),
						value);
			}
		}

		for (Field field : clazz.getDeclaredFields()) {
			if (logger.isDebugEnabled()) {
				logger.debug("examining field=[" + clazz.getName() + "."
						+ field.getName() + "]");
			}
			if (field.isAnnotationPresent(PluginValue.class)) {
				PluginValue valueAnnotation = field
						.getAnnotation(PluginValue.class);
				PropertyDescriptor property = BeanUtils.getPropertyDescriptor(
						clazz, field.getName());

				if (property == null || property.getWriteMethod() == null) {
					throw new BeanCreationException(beanName,
							"setter for property=[" + clazz.getName() + "."
									+ field.getName() + "] not available.");
				}
				String key = extractKey(annotationConfigutation,
						valueAnnotation, beanName, beanFactory);
				Object value = resolvePlaceholder(key, properties,
						SYSTEM_PROPERTIES_MODE_FALLBACK);
				if (value == null) {
					throw new BeanCreationException(beanName,
							"No such property=[" + key
									+ "] found in properties.");
				}
				if (logger.isDebugEnabled()) {
					logger.debug("setting property=[" + clazz.getName() + "."
							+ field.getName() + "] value=[" + key + "=" + value
							+ "]");
				}
				mutablePropertyValues.addPropertyValue(field.getName(), value);
			}
		}
	}

	/**
	 * Findet Key Anhang Annotation.
	 * 
	 * @param annotation
	 * @return
	 */
	private String extractKey(PluginConfiguration annotation,
			PluginValue value, String beanName, BeanFactory beanFactory) {

		String initialKey = value.key();
		String plugInBeanName = annotation.pluginBeanName();
		PluginConfigType pluginConfigType = annotation.pluginType();
		StringBuilder key = new StringBuilder();
		String configPrefix = annotation.pluginType().getConfigPrefix();
		if (PluginConfigType.Builder == pluginConfigType) {
			key.append(configPrefix);
			Object bean = beanFactory.getBean(beanName);
			if (bean instanceof IXmlComplexTypeBuilder) {
				IXmlComplexTypeBuilder iXmlComplexTypeBuilder = (IXmlComplexTypeBuilder) bean;
				String xmlType = iXmlComplexTypeBuilder.getXmlType();
				String xmlTypeKey = extractKeyFromXmlType(xmlType);
				key.append(".").append(xmlTypeKey);
				key.append(".").append(plugInBeanName);
				key.append(".").append(initialKey);
			} else {
				throw new BeanCreationException(
						beanName,
						"Unexpected AnnotationType. Use PluginConfigType.Builder for IXmlComplexTypeBuilder");
			}

		} else {
			key.append(configPrefix).append(".").append(plugInBeanName)
					.append(".").append(initialKey);
		}

		return key.toString();
	}

	/**
	 * Berechnet Key aus dem xmlType indem ':' durch '.' replased wird
	 * 
	 * @param xmlType
	 * @return
	 */
	private String extractKeyFromXmlType(String xmlType) {
		String xmlTypeKey = StringUtils.replace(xmlType, ":", ".");
		return xmlTypeKey;
	}
}
