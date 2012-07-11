package de.extra.xtt.util.schema;

import com.sun.xml.xsom.parser.AnnotationParser;
import com.sun.xml.xsom.parser.AnnotationParserFactory;


/**
 * Factory zum Erstellen eines Parsers für Annotationen von Schemadateien
 * 
 * @author Beier
 * 
 */
public class AnnotationFactory implements AnnotationParserFactory {

	/**
	 * {@inheritdoc}
	 */
	@Override
	public AnnotationParser create() {
		return new XsdAnnotationParser();
	}
}