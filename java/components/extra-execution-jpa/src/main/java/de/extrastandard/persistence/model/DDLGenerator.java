package de.extrastandard.persistence.model;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * siehe:
 * http://jandrewthompson.blogspot.de/2009/10/how-to-generate-ddl-scripts-
 * from.html
 * 
 * Generiert aus den JPA-Annotationen eine DDL-Datei
 * 
 * @author john.thompson
 * 
 */
public class DDLGenerator {
	private final AnnotationConfiguration cfg;

	public DDLGenerator(final String packageName) throws Exception {
		cfg = new AnnotationConfiguration();
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");

		for (final Class<?> clazz : getClasses(packageName)) {
			cfg.addAnnotatedClass(clazz);
		}
	}

	/**
	 * Method that actually creates the file.
	 * 
	 * @param dbDialect
	 *            to use
	 */
	private void generate(final Dialect dialect) {
		cfg.setProperty("hibernate.dialect", dialect.getDialectClass());

		final SchemaExport export = new SchemaExport(cfg);
		export.setDelimiter(";");
		export.setOutputFile("ddl_" + dialect.name().toLowerCase() + ".sql");
		export.execute(true, false, false, false);
	}

	/**
	 * Utility method used to fetch Class list based on a package name.
	 * 
	 * @param packageName
	 *            (should be the package containing your annotated beans.
	 */
	private List<Class<?>> getClasses(final String packageName)
			throws Exception {
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		File directory = null;
		try {
			final ClassLoader cld = Thread.currentThread()
					.getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			final String path = packageName.replace('.', '/');
			final URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
		} catch (final NullPointerException x) {
			throw new ClassNotFoundException(packageName + " (" + directory
					+ ") does not appear to be a valid package");
		}
		if (directory.exists()) {
			final String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					classes.add(Class.forName(packageName + '.'
							+ files[i].substring(0, files[i].length() - 6)));
				}
			}
		} else {
			throw new ClassNotFoundException(packageName
					+ " is not a valid package");
		}

		return classes;
	}

	/**
	 * Holds the classnames of hibernate dialects for easy reference.
	 */
	private static enum Dialect {
		ORACLE("org.hibernate.dialect.Oracle10gDialect"), MYSQL(
				"org.hibernate.dialect.MySQLDialect"), HSQL(
				"org.hibernate.dialect.HSQLDialect");

		private String dialectClass;

		private Dialect(final String dialectClass) {
			this.dialectClass = dialectClass;
		}

		public String getDialectClass() {
			return dialectClass;
		}
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) throws Exception {
		final DDLGenerator gen = new DDLGenerator(
				"de.extrastandard.persistence.model");
		gen.generate(Dialect.MYSQL);
		gen.generate(Dialect.ORACLE);
		gen.generate(Dialect.HSQL);
	}
}
