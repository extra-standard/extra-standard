package de.extra.client.core.model.inputdata.impl;

import de.extrastandard.api.util.IImplementor;

/**
 * 
 * Als Superklasse casted diese Klasse in die Subclass gegebene durch
 * <code>this.getClass()</code>
 * 
 * @author dprs
 * 
 */
public class Implementor implements IImplementor {

	/**
	 * casten übergebene Klasse Klasse ist.
	 */
	@Override
	public <X> X cast(final Class<X> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("Parameter 'cls' muss angegeben werden");
		}

		if (clazz.isAssignableFrom(this.getClass())) {
			return clazz.cast(this);
		}
		throw new ClassCastException("Klasse " + this.getClass() + " kann nicht nach " + clazz + " gewandelt werden");
	}

	@Override
	/**
	 * prueft, ob uebergebene Klasse <code>assignable</code> von aktueller
	 * Klasse ist.
	 */
	public <X> boolean isImplementationOf(final Class<X> clazz) {
		return clazz == null ? false : clazz.isAssignableFrom(this.getClass());
	}

}
