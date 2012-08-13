package de.extra.client.starter;

import java.io.PrintWriter;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
//import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Level;

/**
 * Extra Client Argumentauswertung.
 * 
 * <p>Argumente werden beim Start des Extra-Clients übergeben (zum Beispiel über die Kommandozeile) und steuern und konfigurieren 
 * die Verarbeitung des Clients. Gleichnamige Argumente haben in der Regel Vorrang vor Konfigurationsparametern. 
 * Es gibt pro Client-Lauf nur eine Instanz, die einmalig zum Start des Clients gefüllt wird.</p>
 * 
 * <p>Kapselt die technische Realisierung der Kommandozeilenauswertung.</p>
 */
public class ClientArguments {

	private static Option OPT_HELP = new Option("h", "help", false, "print help");
	private static Option OPT_PROCESS = new Option("p", "process", false, "process files");
	
	private static final Option OPT_QUIET = new Option("q", "quiet", false, "be quiet");
	private static final Option OPT_VERBOSE = new Option("v", "verbose", false, "be verbose");
	private static final Option OPT_DEBUG = new Option("d", "debug", false, "print additional debug information during processing (very verbose)");
	private static final Option OPT_TRACE = new Option("t", "trace", false, "print additional trace information (extremely verbose)");
//	private static final Option IN_DIR = OptionBuilder.withArgName( "inputDirectoy" )
//          .hasArg()
//          .withDescription(  "use given directory to read request files" )
//          .create( "logfile" ));

	
	private static OptionGroup OPTGRP_VERBOSITY = new OptionGroup();
	private static OptionGroup OPTGRP_ACTION = new OptionGroup();
	
	private static Options RECOGNIZED_OPTIONS = new Options();
	
	public static enum ClientActions {
		PRINT_HELP,
		PROCESS
	}
	
	static {
		OPTGRP_VERBOSITY.addOption(OPT_QUIET);
		OPTGRP_VERBOSITY.addOption(OPT_VERBOSE);
		OPTGRP_VERBOSITY.addOption(OPT_DEBUG);
		OPTGRP_VERBOSITY.addOption(OPT_TRACE);
		
		OPTGRP_ACTION.addOption(OPT_HELP);
		OPTGRP_ACTION.addOption(OPT_PROCESS);
		
		RECOGNIZED_OPTIONS.addOptionGroup(OPTGRP_VERBOSITY);
		RECOGNIZED_OPTIONS.addOptionGroup(OPTGRP_ACTION);
	}

	/** (ungeparste) Kommandozeile */
	private String[] args;
	/** Log-Level, wenn ein bestimmter über die Kommandozeile festgelegt wird, sonst null */
	private Level selectedLogLevel = null;
	/** Action, die sich aus der Kommandozeile ergibt. Es gibt immer eine (Default: Process). */
	private ClientActions action = ClientActions.PROCESS;
	
	/** Postfix für den Hilfstext */
	private String helpPostfix = "";
	
	/**
	 * Zerlegt die Kommandozeile, setzt erwartete Optionen, behandelt ggf. Fehler.
	 */
	private void parseArgs() {
	    CommandLineParser parser = new GnuParser();
	    
	    try {
	    	CommandLine commandLine = parser.parse(ClientArguments.RECOGNIZED_OPTIONS, this.args );
	    	if (commandLine.getArgs() != null && commandLine.getArgs().length > 0) {
	    		throw new ParseException("unbekannte Kommandozeilenargumente gefunden " + Arrays.toString(commandLine.getArgs()));
	    	}

			String selectedVerbosity = OPTGRP_VERBOSITY.getSelected(); 
			if (OPT_QUIET.getOpt().equals(selectedVerbosity)) {
				this.selectedLogLevel = Level.ERROR;
			} else if (OPT_VERBOSE.getOpt().equals(selectedVerbosity)) {
				this.selectedLogLevel  = Level.INFO;
			} else if (OPT_DEBUG.getOpt().equals(selectedVerbosity)) {
				this.selectedLogLevel  = Level.DEBUG;
			} else if (OPT_TRACE.getOpt().equals(selectedVerbosity)) {
				this.selectedLogLevel  = Level.TRACE;
			}
			
			if (commandLine.hasOption(OPT_HELP.getArgName())) {
				this.action = ClientActions.PRINT_HELP;
			} else if (commandLine.hasOption(OPT_PROCESS.getArgName())) {
				this.action = ClientActions.PROCESS;
			}
	    }
	    catch( ParseException exp ) {
	        // im Fehlerfall ergibt sich aus der Kommandozeile die Aktion "Hilfstext ausgeben", ergänzt um Fehlermessage
	        this.action = ClientActions.PRINT_HELP;
	        this.helpPostfix = "Fehler beim Aufruf: " + exp.getMessage();
	    }		
	}
	
	/**
	 * Create an arguments object for a command line.
	 * 
	 * @param args  unparsed command line
	 */
	public ClientArguments(String[] args) {
		this.args = args;
		parseArgs();
	}
	
	/**
	 * Get unparsed command line.
	 * 
	 * @param args
	 * @return
	 */
	public String[] getArgs(String[] args) {
		return this.args;
	}
	
	/**
	 * Log-Level wie er sich aus den Argumenten ergibt.
	 * 
	 * Wenn sich aus den Argumenten kein bestimmter Log-Level ergibt, <code>null</code>.
	 * In diesem Fall sind die Einstellungen der Log4j Konfiguration unverändert.
	 * 
	 * @return Log-Level oder <code>null</code>
	 */
	public Level getSelectedLogLevel() {
		return this.selectedLogLevel;
	}
	
	/**
	 * Per Kommandozeile gewählte Aktion des Clients.
	 * 
	 * <p>Ist immer gegeben.</p>
	 * 
	 * @return Aktion
	 */
	public ClientActions getSelectedAction() {
		return this.action;
	}
	
	/**
	 * Gibt Hilfstext zur Kommandozeile aus.
	 * 
	 * Kann benutzt werden, um die entsprechende Aktion zu realisieren.
	 * 
	 * @param destination wohin der Hilfstext ausgegeben werden soll
	 */
	public void printHelpText(PrintWriter destination) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("extraClient", "", RECOGNIZED_OPTIONS, this.helpPostfix);
	}
}
