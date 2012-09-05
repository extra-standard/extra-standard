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
package de.extra.client.starter;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Dient dazu, einen Spring-Context mit vordefinierten Beans zu starten.
 *
 * @author Thorsten Vogel
 * @version $Id$
 */
public abstract class ApplicationContextStarter<C extends ConfigurableApplicationContext> {

	/**
	 * Callback um einen nicht-initialisierten Spring-Context bereitzustellen.
	 *
	 * @return nicht-initialisierter Spring-Context
	 */
	protected abstract C createUninitializedContext();

	/**
	 * Startet einen Spring-Context mit extern definierten Beans. Diese Beans
	 * werden im Spring-Context als Singleton registriert.
	 *
	 * @param preregisteredObjects
	 *            Map mit den vordefinierten Beans.
	 * @return iniitalisierter {@link ApplicationContext}
	 */
	public C createApplicationContext(
			final Map<String, Object> preregisteredObjects) {
		C context = createUninitializedContext();
		BeanFactoryPostProcessor beanFactoryPostProcessor = new BeanFactoryPostProcessor() {
			@Override
			public void postProcessBeanFactory(
					final ConfigurableListableBeanFactory beanFactory)
					throws BeansException {
				for (Map.Entry<String, Object> entry : preregisteredObjects
						.entrySet()) {
					beanFactory.registerSingleton(entry.getKey(),
							entry.getValue());
				}
			}
		};
		context.addBeanFactoryPostProcessor(beanFactoryPostProcessor);
		context.refresh();
		return context;
	}

}
