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
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

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

		for (String name : beanFactory.getBeanDefinitionNames()) {
			Class<?> clazz = beanFactory.getType(name);

			if (logger.isDebugEnabled()) {
				logger.debug("Configuring properties for bean=" + name + "["
						+ clazz + "]");
			}

			if (clazz != null
					&& clazz.isAnnotationPresent(PluginConfigutation.class)) {
				PluginConfigutation annotationConfigutation = clazz
						.getAnnotation(PluginConfigutation.class);
				MutablePropertyValues mutablePropertyValues = beanFactory
						.getBeanDefinition(name).getPropertyValues();

				for (PropertyDescriptor property : BeanUtils
						.getPropertyDescriptors(clazz)) {
					Method setter = property.getWriteMethod();
					PluginValue valueAnnotation = null;
					if (setter != null
							&& setter.isAnnotationPresent(PluginValue.class)) {
						valueAnnotation = setter
								.getAnnotation(PluginValue.class);
					}
					if (valueAnnotation != null) {
						String key = extractKey(annotationConfigutation,
								valueAnnotation);
						String value = resolvePlaceholder(key, properties,
								SYSTEM_PROPERTIES_MODE_FALLBACK);
						if (StringUtils.isEmpty(value)) {
							throw new BeanCreationException(name,
									"No such property=[" + key
											+ "] found in properties.");
						}
						if (logger.isDebugEnabled()) {
							logger.debug("setting property=[" + clazz.getName()
									+ "." + property.getName() + "] value=["
									+ key + "=" + value + "]");
						}
						mutablePropertyValues.addPropertyValue(
								property.getName(), value);
					}
				}

				for (Field field : clazz.getDeclaredFields()) {
					if (logger.isDebugEnabled()) {
						logger.debug("examining field=[" + clazz.getName()
								+ "." + field.getName() + "]");
					}
					if (field.isAnnotationPresent(PluginValue.class)) {
						PluginValue valueAnnotation = field
								.getAnnotation(PluginValue.class);
						PropertyDescriptor property = BeanUtils
								.getPropertyDescriptor(clazz, field.getName());

						if (property == null
								|| property.getWriteMethod() == null) {
							throw new BeanCreationException(name,
									"setter for property=[" + clazz.getName()
											+ "." + field.getName()
											+ "] not available.");
						}
						String key = extractKey(annotationConfigutation,
								valueAnnotation);
						Object value = resolvePlaceholder(key, properties,
								SYSTEM_PROPERTIES_MODE_FALLBACK);
						if (value == null) {
							throw new BeanCreationException(name,
									"No such property=[" + key
											+ "] found in properties.");
						}
						if (logger.isDebugEnabled()) {
							logger.debug("setting property=[" + clazz.getName()
									+ "." + field.getName() + "] value=[" + key
									+ "=" + value + "]");
						}
						mutablePropertyValues.addPropertyValue(
								property.getName(), value);
						if (logger.isDebugEnabled()) {
							logger.debug("setting property=[" + clazz.getName()
									+ "." + field.getName() + "] value=[" + key
									+ "=" + value + "]");
						}

						mutablePropertyValues.addPropertyValue(field.getName(),
								value);
					}
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
	private String extractKey(PluginConfigutation annotation, PluginValue value) {
		String initialKey = value.key();
		String plugInBeanName = annotation.plugInBeanName();
		String configPrefix = annotation.plugInType().getConfigPrefix();
		StringBuilder key = new StringBuilder();
		key.append(configPrefix).append(".").append(plugInBeanName).append(".")
				.append(initialKey);
		return key.toString();
	}

}
