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
/*
 * Created on Jan 10, 2007
 *
 */
package de.extra.client.plugins.outputplugin.config;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import de.extra.client.plugins.outputplugin.utils.PropertiesHelperException;

/**
 * Helper class to read the specifies property file
 */
public class ExtraPropertiesHelper {

	/**
	 * 
	 * @param fileName
	 * @param propertyName
	 * @return the property corresponding to the given propertyName
	 * @throws PropertiesHelperException
	 */
	public static String getProperty(String fileName, String propertyName)
			throws PropertiesHelperException {

		String property = null;
		try {
			// Initialisiere Properties
			Properties properties = new Properties();

			// Lade Properties
			properties.load(new FileInputStream(fileName));
			property = properties.getProperty(propertyName);

		} catch (Exception e) {
			throw new PropertiesHelperException(
					"Fehler beim Import der Properties.", e);
		}

		return property;
	}

	/**
	 * 
	 * @param storageObject
	 * @param fileName
	 * @return the given storageObject that was modified by this method
	 * @throws PropertiesHelperException
	 */
	public static Object processPropertyFile(Object storageObject,
			String fileName) throws PropertiesHelperException {
		if (storageObject != null) {
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(fileName));
				Field[] fields = storageObject.getClass().getDeclaredFields();
				Field field = null;
				String value = null;
				for (int i = 0; i < fields.length; i++) {
					field = fields[i];
					field.setAccessible(true);
					value = checkValue(properties.getProperty(field.getName()),
							field.getName());
					if (field.getType().isPrimitive()) {
						if (field.getType() == Boolean.TYPE) {
							field.setBoolean(storageObject,
									Boolean.valueOf(value).booleanValue());
						} else if (field.getType() == Byte.TYPE) {
							field.setByte(storageObject, Byte.valueOf(value)
									.byteValue());
						} else if (field.getType() == Double.TYPE) {
							field.setDouble(storageObject, Double
									.valueOf(value).doubleValue());
						} else if (field.getType() == Float.TYPE) {
							field.setFloat(storageObject, Float.valueOf(value)
									.floatValue());
						} else if (field.getType() == Integer.TYPE) {
							field.setInt(storageObject, Integer.valueOf(value)
									.intValue());
						} else if (field.getType() == Long.TYPE) {
							field.setLong(storageObject, Long.valueOf(value)
									.longValue());
						} else if (field.getType() == Short.TYPE) {
							field.setShort(storageObject, Short.valueOf(value)
									.shortValue());
						} else {
							throw new PropertiesHelperException(
									"Fehler beim Typ-Mapping.");
						}
					} else {
						field.set(storageObject, value);
					}
				}
			} catch (Exception e) {
				throw new PropertiesHelperException(
						"Fehler beim Import der Properties.", e);
			}
		}
		return storageObject;
	}

	private static String checkValue(Object input, String fieldName) {
		// if (input == null) {
		// return "";
		// } else {
		// return (String) input;
		// }
		return (String) input;
	}
}