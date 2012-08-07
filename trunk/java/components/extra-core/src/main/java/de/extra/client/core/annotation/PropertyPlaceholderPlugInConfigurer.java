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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


public class PropertyPlaceholderPlugInConfigurer extends PropertyPlaceholderConfigurer{
	

	    private static final Logger LOG = Logger.getLogger(PropertyPlaceholderPlugInConfigurer.class);

	    @Override
	    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties properties) throws BeansException {
	        super.processProperties(beanFactory, properties);

	        for (String name : beanFactory.getBeanDefinitionNames()) {
	            Class<?> clazz = beanFactory.getType(name);

	            if(LOG.isDebugEnabled()){
	            	LOG.debug("Configuring properties for bean="+name+"["+clazz+"]");
	            }
	            
	            if(clazz != null && clazz.isAnnotationPresent(PlugInConfigutation.class)) {
	            	PlugInConfigutation annotationConfigutation = clazz.getAnnotation(PlugInConfigutation.class);
		            MutablePropertyValues mutablePropertyValues = beanFactory.getBeanDefinition(name).getPropertyValues();	
	                for (PropertyDescriptor property : BeanUtils.getPropertyDescriptors(clazz)) {
	                    Method setter = property.getWriteMethod();
	                    Value valueAnnotation = null;
	                    if(setter != null && setter.isAnnotationPresent(Value.class)) {
	                    	valueAnnotation = setter.getAnnotation(Value.class);
	                    }
	                    if(valueAnnotation != null) {
	                    	String key = extractKey (annotationConfigutation, valueAnnotation);
	                        String value = resolvePlaceholder(key, properties, SYSTEM_PROPERTIES_MODE_FALLBACK);
	                        if(StringUtils.isEmpty(value)) {
	                            throw new BeanCreationException (name,"No such property=["+key+"] found in properties.");
	                        }
	                        if(LOG.isDebugEnabled()) {
	                        	LOG.debug("setting property=["+clazz.getName()+"."+property.getName()+"] value=["+key+"="+value+"]");
	                        }
	                        mutablePropertyValues.addPropertyValue(property.getName(), value);
	                    }
	                }

	                for(Field field : clazz.getDeclaredFields()) {
	                    if(LOG.isDebugEnabled()) {
	                    	LOG.debug("examining field=["+clazz.getName()+"."+field.getName()+"]");
	                    }
	                    if(field.isAnnotationPresent(Value.class)) {
	                    	Value valueAnnotation = field.getAnnotation(Value.class);
	                        PropertyDescriptor property = BeanUtils.getPropertyDescriptor(clazz, field.getName());

	                        if(property.getWriteMethod() == null) {
	                            throw new BeanCreationException(name,"setter for property=["+clazz.getName()+"."+field.getName()+"] not available.");
	                        }
	                        String key = extractKey (annotationConfigutation, valueAnnotation);
	                        Object value = resolvePlaceholder(key, properties, SYSTEM_PROPERTIES_MODE_FALLBACK);
	                        if(value == null) {
	                            throw new BeanCreationException(name,"No such property=["+key+"] found in properties.");
	                        }
	                        if(LOG.isDebugEnabled()){
	                        	LOG.debug("setting property=["+clazz.getName()+"."+field.getName()+"] value=["+key+"="+value+"]");
	                        }
	                        mutablePropertyValues.addPropertyValue(property.getName(), value);
	                    }
	                }
	            }
	        }
	    }

	    
		/**
		 * Findet Key Anhang Annotation
		 * @param annotation
		 * @return
		 */
		private String extractKey(PlugInConfigutation annotation, Value value) {
			String initialKey = value.value();
			String plugInBeanName = annotation.plugInBeanName();
			String configPrefix = annotation.plugInType().getConfigPrefix();
			StringBuilder key = new StringBuilder();
			key.append(configPrefix).append(".").append(plugInBeanName).append(".").append(initialKey);
			return key.toString();
		}



}
