package de.extrastandard.persistence.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * siehe:
 * http://jandrewthompson.blogspot.de/2009/10/how-to-generate-ddl-scripts-
 * from.html
 * 
 * Generiert aus den JPA-Annotationen eine DDL-Datei Klasse wurde angepasst.
 * 
 * @author john.thompson
 * 
 */
public class DDLGenerator {
	private final Configuration cfg;

	private static final Logger logger = LoggerFactory
			.getLogger(DDLGenerator.class);

	private static final String OUTPUT_DIRECTORY = "target/generated-test-sources/ddl/";

	public DDLGenerator(final String packageName) throws Exception {
		cfg = new Configuration();
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
		logger.info("generating DDL for :" + dialect.getDialectClass());
		cfg.setProperty("hibernate.dialect", dialect.getDialectClass());

		final SchemaExport export = new SchemaExport(cfg);
		export.setDelimiter(";");
		final String outputFileName = "ddl_" + dialect.name().toLowerCase()
				+ ".sql";
		final File outputDirectoryFile = new File(OUTPUT_DIRECTORY);
		final File file = new File(outputDirectoryFile, outputFileName);
		if (!outputDirectoryFile.exists()) {
			outputDirectoryFile.mkdirs();
		}
		export.setOutputFile(file.getAbsolutePath());
		export.execute(true, false, false, false);
		logger.info("DDL for: " + dialect.getDialectClass()
				+ " generated to File " + file.getAbsolutePath());
	}

	/**
	 * Utility method used to fetch Class list based on a package name.
	 * 
	 * @param packageName
	 *            (should be the package containing your annotated beans.
	 */
	private List<Class<?>> getClasses(final String packageName)
			throws Exception {

		return findMyTypes(packageName);
	}

	private List<Class<?>> findMyTypes(final String basePackage)
			throws IOException, ClassNotFoundException {
		final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
				resourcePatternResolver);

		final List<Class<?>> candidates = new ArrayList<Class<?>>();
		final String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ resolveBasePackage(basePackage) + "/" + "**/*.class";
		final Resource[] resources = resourcePatternResolver
				.getResources(packageSearchPath);
		for (final Resource resource : resources) {
			if (resource.isReadable()) {
				final MetadataReader metadataReader = metadataReaderFactory
						.getMetadataReader(resource);
				if (isCandidate(metadataReader)) {
					candidates.add(Class.forName(metadataReader
							.getClassMetadata().getClassName()));
				}
			}
		}
		return candidates;
	}

	private String resolveBasePackage(final String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils
				.resolvePlaceholders(basePackage));
	}

	private boolean isCandidate(final MetadataReader metadataReader)
			throws ClassNotFoundException {
		try {
			final Class<?> c = Class.forName(metadataReader.getClassMetadata()
					.getClassName());
			if (c.getAnnotation(Entity.class) != null) {
				return true;
			}
		} catch (final Throwable e) {
		}
		return false;
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
