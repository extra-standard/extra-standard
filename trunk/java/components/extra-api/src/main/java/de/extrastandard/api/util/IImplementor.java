package de.extrastandard.api.util;

/**
 * Instanzen, welche mehere Interface implementieren, welche nicht ale an der
 * Instanz sichtbar sind, bieten mit Hilfe dieses IF die Moeglichkeit, zu einen
 * dieser 'versteckten' Interfaces zu wechselen
 * 
 * @author zf4iks2
 * 
 */
public interface IImplementor {

	/**
	 * Prueft, ob cast in die gewuenschte Zielklasse moeglich ist.
	 * 
	 * Ist <code>cls==null</code>, so wird <code>false</code> geliefert.
	 * 
	 * @param <X>
	 * @param cls
	 *            Zielklasse/Zielinterface
	 * @return true g.d.w. die vorliegende Instance einen gesicherten cast nach
	 *         <code>Class<X></code> zulaesst
	 */
	<X> boolean isImplementationOf(Class<X> cls);

	/**
	 * Fuehrt den cast nach <code>Class<X></code>
	 * 
	 * @param <X>
	 * @param cls
	 *            Zielklasse
	 * @return Objekt, welche das gewuenschte Klasse implementiert. Es wird kein
	 *         Zusammenhang zwischen Ausgangsobjekt und Resultat vorausgesetzt
	 *         (wie .z.B. sub/super, implements, instanceof etc)
	 * @throws ClassCastException
	 */
	<X> X cast(Class<X> cls);
}
