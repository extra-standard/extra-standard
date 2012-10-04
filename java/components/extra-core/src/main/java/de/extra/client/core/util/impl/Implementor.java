package de.extra.client.core.util.impl;

import de.extrastandard.api.util.IImplementor;

/**
 * 
 * Als Superklasse casted diese Klasse in die Subclass gegebene durch
 * <code>this.getClass()</code>
 * 
 * @author zf4iks2
 * 
 */
public class Implementor implements IImplementor {

	/**
	 * prueft, ob uebergebene Klasse <code>assignable</code> von aktueller
	 * Klasse ist.
	 */
	@Override
	public <X> X cast(final Class<X> iface) {
		if (iface == null) {
			throw new IllegalArgumentException("Parameter 'cls' muss angegeben werden");
		}
		if (iface.isAssignableFrom(this.getClass())) {
			return iface.cast(this);
		}
		throw new ClassCastException("Klasse " + this.getClass() + " kann nicht nach " + iface + " gewandelt werden");
	}

	@Override
	public <X> boolean isImplementationOf(final Class<X> cls) {
		return cls == null ? false : cls.isAssignableFrom(this.getClass());
	}

}
