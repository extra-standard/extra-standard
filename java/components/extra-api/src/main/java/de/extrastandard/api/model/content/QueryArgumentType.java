package de.extrastandard.api.model.content;

/**
 * Enum für die Queryes Entspricht dem Element xmsg:Query Argument
 * 
 * @author Leonid Potap
 * @version $Id$
 */
public enum QueryArgumentType {
	EG("EG"), GT("GT"), GE("GE");

	String type;

	private QueryArgumentType(final String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
