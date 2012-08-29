package de.extra.client.starter;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.springframework.util.StringUtils;

import de.extra.client.exit.JvmSystemExiter;
import de.extra.client.exit.SystemExiter;

/**
 * Extra Client Argumentauswertung.
 *
 * <p>
 * Argumente werden beim Start des Extra-Clients übergeben (zum Beispiel über
 * die Kommandozeile) und steuern und konfigurieren die Verarbeitung des
 * Clients. Gleichnamige Argumente haben in der Regel Vorrang vor
 * Konfigurationsparametern. Es gibt pro Client-Lauf nur eine Instanz, die
 * einmalig zum Start des Clients gefüllt wird.
 * </p>
 *
 * <p>
 * Kapselt die technische Realisierung der Kommandozeilenauswertung.
 * </p>
 * @author DPRS
 * @version $Id$
 */
public class ClientArguments {

	public static final String OPTION_NAME_HELP = "help";
	public static final String OPTION_NAME_HELP_SHORTCUT = "h";

	public static final String OPTION_NAME_CONFIGDIR = "configDirectory";
	public static final String OPTION_NAME_CONFIGDIR_SHORTCUT = "c";

	private static final Option OPT_HELP = new Option(OPTION_NAME_HELP_SHORTCUT, OPTION_NAME_HELP, false, "Hilfe anzeigen");

	private static final Option OPT_CONFIGDIRECTORY = new Option(OPTION_NAME_CONFIGDIR_SHORTCUT, OPTION_NAME_CONFIGDIR, true, "Konfigurationsverzeichnis");

	public static final Options OPTIONS;

	private final SystemExiter exiter = new JvmSystemExiter();

	static {
		OPTIONS = new Options();
		OPTIONS.addOption(OPT_HELP);
		OPTIONS.addOption(OPT_CONFIGDIRECTORY);
	}

	private final String[] args;

	/**
	 * Wert aus dem ermitteltem Kommandozeilenparameter {@link #OPTION_NAME_CONFIGDIR}.
	 */
	private File configDirectory = null;

	/**
	 * Gibt an ob ein Hilfetext ausgegeben werden soll Parameter #OPTION_NAME_HELP.
	 */
	private Boolean showHelp = null;

	public ClientArguments(final String[] args) {
		this.args = args;
	}

	/**
	 * Zerlegt die Kommandozeile, setzt erwartete Optionen, behandelt ggf.
	 * Fehler.
	 */
	public void parseArgs() {
		CommandLineParser parser = new PosixParser();

		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(OPTIONS, this.args);
		} catch (ParseException e) {
			printHelpText(e);
			exiter.exit(ReturnCode.TECHNICAL);
		}

		if (commandLine.hasOption(OPTION_NAME_HELP)) {
			this.showHelp = Boolean.TRUE;
		}

		if (commandLine.hasOption(OPTION_NAME_CONFIGDIR)) {
			String optionValue = commandLine.getOptionValue(OPTION_NAME_CONFIGDIR);
			if (!StringUtils.hasText(optionValue)) {
				throw new IllegalArgumentException("Konfigurationsverzeichnis muss angegeben werden.");
			}
			this.configDirectory = new File(optionValue);
			if (!configDirectory.exists()) {
				throw new IllegalArgumentException("Konfigurationsverzeichnis existiert nicht.");
			}
			if (!configDirectory.isDirectory()) {
				throw new IllegalArgumentException("Konfigurationsverzeichnis ist kein Verzeichnis.");
			}
			if (!configDirectory.canRead()) {
				throw new IllegalArgumentException("Konfigurationsverzeichnis nicht zugreifbar.");
			}
		}

		if (showHelp == null && configDirectory == null) {
			throw new IllegalArgumentException("Bitte Parameter angeben.");
		}
	}

	public void printHelpText(final Exception e) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("extraClient", OPTIONS);
		if (e != null) {
			System.out.println("\nFehler: " + e.getMessage());
		}
	}

	public boolean isShowHelp() {
		return showHelp;
	}

	public File getConfigDirectory() {
		return configDirectory;
	}
}
