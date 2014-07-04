package de.extra.client.starter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.extra.client.core.ReturnCode;
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
 *
 * @author DPRS
 * @author Thorsten Vogel
 * @version $Id: ClientArguments.java 563 2012-09-06 14:15:35Z
 *          thorstenvogel@gmail.com $
 */
public class ClientArgumentParser {

    public static final String OPTION_NAME_HELP = "help";
    public static final String OPTION_NAME_HELP_SHORTCUT = "h";

    public static final String OPTION_NAME_MANDANT = "mandant";
    public static final String OPTION_NAME_MANDANT_SHORTCUT = "m";

    public static final String OPTION_NAME_GLOBAL_CONFIG_DIRECTORY = "globalConfigDirectory";
    public static final String OPTION_NAME_GLOBAL_CONFIG_DIRECTORY_SHORTCUT = "g";

    public static final String OPTION_NAME_CONFIG_DIRECTORY = "configDirectory";
    public static final String OPTION_NAME_CONFIG_DIRECTORY_SHORTCUT = "c";

    public static final String OPTION_NAME_LOG_DIRECTORY = "logDirectory";
    public static final String OPTION_NAME_LOG_DIRECTORY_SHORTCUT = "l";

    // (12.12.12) Externe Anwendungen muessen Output-Dateien bestaetigen koennen
    public static final String OPTION_NAME_OUTPUT_CONFIRM = "outputConfirm";
    public static final String OPTION_NAME_OUTPUT_CONFIRM_SHORTCUT = "oc";

    public static final String OPTION_NAME_OUTPUT_FAILURE = "outputFailure";
    public static final String OPTION_NAME_OUTPUT_FAILURE_SHORTCUT = "of";

    private static final Option OPT_MANDANT = new Option(OPTION_NAME_MANDANT_SHORTCUT, OPTION_NAME_MANDANT, true,
            "Name des Mandanten");

    private static final Option OPT_HELP = new Option(OPTION_NAME_HELP_SHORTCUT, OPTION_NAME_HELP, false,
            "Hilfe anzeigen");

    private static final Option OPT_GLOBAL_CONFIG_DIRECTORY = new Option(OPTION_NAME_GLOBAL_CONFIG_DIRECTORY_SHORTCUT,
            OPTION_NAME_GLOBAL_CONFIG_DIRECTORY, true, "Globales Konfigurationsverzeichnis");

    private static final Option OPT_CONFIG_DIRECTORY = new Option(OPTION_NAME_CONFIG_DIRECTORY_SHORTCUT,
            OPTION_NAME_CONFIG_DIRECTORY, true, "Konfigurationsverzeichnis");

    private static final Option OPT_LOG_DIRECTORY = new Option(OPTION_NAME_LOG_DIRECTORY_SHORTCUT,
            OPTION_NAME_LOG_DIRECTORY, true, "Logverzeichnis");

    private static final Option OPT_OUTPUT_CONFIRM = new Option(OPTION_NAME_OUTPUT_CONFIRM_SHORTCUT,
            OPTION_NAME_OUTPUT_CONFIRM, true, "Korrekten Output bestätigen");

    private static final Option OPT_OUTPUT_FAILURE = new Option(OPTION_NAME_OUTPUT_FAILURE_SHORTCUT,
            OPTION_NAME_OUTPUT_FAILURE, true, "Fehlerhaften Output melden");

    public static final Options OPTIONS;

    private final SystemExiter exiter;

    static {
        OPTIONS = new Options();
        OPTIONS.addOption(OPT_HELP);
        OPTIONS.addOption(OPT_MANDANT);
        OPTIONS.addOption(OPT_GLOBAL_CONFIG_DIRECTORY);
        OPTIONS.addOption(OPT_CONFIG_DIRECTORY);
        OPTIONS.addOption(OPT_LOG_DIRECTORY);
        OPTIONS.addOption(OPT_OUTPUT_CONFIRM);
        OPTIONS.addOption(OPT_OUTPUT_FAILURE);
    }

    /**
     * Kommandozeile
     */
    private final String[] args;

    public ClientArgumentParser(final String[] args, final SystemExiter exiter) {
        Assert.notNull(exiter);
        this.args = args;
        this.exiter = exiter;
    }

    /**
     * Zerlegt die Kommandozeile, setzt erwartete Optionen, behandelt ggf.
     * Fehler.
     */
    public ExtraClientParameters parseArgs() {
        final CommandLineParser parser = new PosixParser();
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(OPTIONS, this.args);
        } catch (final ParseException e) {
            printHelpText(e);
            exiter.exit(ReturnCode.TECHNICAL);
        }

        // falls hilfe aufgerufen wurde, direkt zurück
        final Boolean showHelp = Boolean.valueOf(commandLine.hasOption(OPTION_NAME_HELP));
        if (showHelp) {
            return new ExtraClientParameters();
        }

        final List<String> errors = new LinkedList<String>();
        String mandant = null;
        if (commandLine.hasOption(OPTION_NAME_MANDANT)) {
            final String mandantInput = commandLine.getOptionValue(OPTION_NAME_MANDANT);
            if (!StringUtils.hasText(mandantInput)) {
                errors.add("Name des Mandanten muss angeben werden.");
            } else {
                mandant = mandantInput.trim();
            }
        } else {
            errors.add("Parameter Mandant muss angeben werden.");
        }

        File globalConfigDirectory = null;
        if (commandLine.hasOption(OPTION_NAME_GLOBAL_CONFIG_DIRECTORY)) {
            final String optionValue = commandLine.getOptionValue(OPTION_NAME_GLOBAL_CONFIG_DIRECTORY) != null ? commandLine
                    .getOptionValue(OPTION_NAME_GLOBAL_CONFIG_DIRECTORY).trim() : null;
            if (!StringUtils.hasText(optionValue)) {
                errors.add("Globales Konfigurationsverzeichnis muss angegeben werden.");
            } else {
                globalConfigDirectory = new File(optionValue);
                checkDirectory(optionValue, globalConfigDirectory, errors);
            }
        } else {
            errors.add("Globales Konfigurationsverzeichnis muss angegeben werden.");
        }

        File configDirectory = null;
        if (commandLine.hasOption(OPTION_NAME_CONFIG_DIRECTORY)) {
            final String optionValue = commandLine.getOptionValue(OPTION_NAME_CONFIG_DIRECTORY) != null ? commandLine
                    .getOptionValue(OPTION_NAME_CONFIG_DIRECTORY).trim() : null;
            if (!StringUtils.hasText(optionValue)) {
                errors.add("Konfigurationsverzeichnis muss angegeben werden.");
            } else {
                configDirectory = new File(optionValue);
                checkDirectory(optionValue, configDirectory, errors);
            }
        } else {
            errors.add("Konfigurationsverzeichnis muss angegeben werden.");
        }

        File logDirectory = null;
        if (commandLine.hasOption(OPTION_NAME_LOG_DIRECTORY)) {
            final String optionValue = commandLine.getOptionValue(OPTION_NAME_LOG_DIRECTORY) != null ? commandLine
                    .getOptionValue(OPTION_NAME_LOG_DIRECTORY).trim() : null;
            if (!StringUtils.hasText(optionValue)) {
                errors.add("Logverzeichnis muss angegeben werden.");
            } else {
                logDirectory = new File(optionValue);
                checkDirectory(optionValue, logDirectory, errors);
            }
        } else {
            errors.add("Logverzeichnis muss angegeben werden.");
        }

        // (14.12.12) Externe Anwendungen: Optionale Parameter outputConfirm,
        // outputFailure
        String outputConfirm = null;
        if (commandLine.hasOption(OPTION_NAME_OUTPUT_CONFIRM)) {
            final String optionValue = commandLine.getOptionValue(OPTION_NAME_OUTPUT_CONFIRM) != null ? commandLine
                    .getOptionValue(OPTION_NAME_OUTPUT_CONFIRM).trim() : null;
            if (!StringUtils.hasText(optionValue)) {
                errors.add("Dateiname (OutputIdentifier) muss angegeben werden.");
            } else {
                outputConfirm = optionValue;
            }
        }

        String outputFailure = null;
        if (commandLine.hasOption(OPTION_NAME_OUTPUT_FAILURE)) {
            final String optionValue = commandLine.getOptionValue(OPTION_NAME_OUTPUT_FAILURE) != null ? commandLine
                    .getOptionValue(OPTION_NAME_OUTPUT_FAILURE).trim() : null;
            if (!StringUtils.hasText(optionValue)) {
                errors.add("Dateiname (OutputIdentifier) muss angegeben werden.");
            } else {
                outputFailure = optionValue;
            }
        }

        return new ExtraClientParameters(mandant, globalConfigDirectory, configDirectory, logDirectory, outputConfirm,
                outputFailure, showHelp, errors);
    }

    /**
     * @param optionValue
     */
    private void checkDirectory(final String optionValue, final File directory, final List<String> errors) {
        if (!directory.exists()) {
            errors.add(String.format("Verzeichnis existiert nicht: %s", directory));
        }
        if (!directory.isDirectory()) {
            errors.add(String.format("Verzeichnis ist kein Verzeichnis: %s", directory));
        }
        if (!directory.canRead()) {
            errors.add(String.format("Verzeichnis nicht zugreifbar: %s", directory));
        }
    }

    public void printHelpText(final Exception e) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("extraClient", OPTIONS);
        if (e != null) {
            System.out.println("\nFehler: " + e.getMessage());
        }
    }

}
