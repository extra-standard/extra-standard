/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.extra.client.starter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Stellt Kommandozeilenparameter für einen ExtraClient Aufruf bereit.
 *
 * @author Thorsten Vogel
 */
public class ExtraClientParameters {

    /**
     * Wert aus dem ermitteltem Kommandozeilenparameter
     * {@link #OPTION_NAME_MANDANT}.
     */
    private final String mandant;

    /**
     * Wert aus dem ermitteltem Kommandozeilenparameter
     * {@link #OPTION_NAME_GLOBAL_CONFIG_DIRECTORY}.
     */
    private final File globalConfigurationDirectory;

    /**
     * Wert aus dem ermitteltem Kommandozeilenparameter
     * {@link #OPTION_NAME_CONFIG_DIRECTORY}.
     */
    private final File configurationDirectory;

    /**
     * Wert aus dem ermitteltem Kommandozeilenparameter
     * {@link #OPTION_NAME_LOG_DIRECTORY}.
     */
    private final File logDirectory;

    /**
     * Wert aus dem ermitteltem Kommandozeilenparameter
     * {@link #OPTION_NAME_OUTPUT_CONFIRM}.
     */
    private final String outputConfirm;

    /**
     * Wert aus dem ermitteltem Kommandozeilenparameter
     * {@link #OPTION_NAME_OUTPUT_FAILURE}.
     */
    private final String outputFailure;

    /**
     * Wert aus dem ermitteltem Kommandozeilenparameter
     * {@link #OPTION_NAME_BACKUP_DIRECTORY}.
     */
    private final File backupDirectory;

    /**
     * Wert aus dem ermitteltem Kommandozeilenparameter
     * {@link ClientArgumentParser#OPTION_NAME_DELETE_INPUTFILES}.
     */
    private final Boolean deleteInputFiles;

    /**
     * Gibt an ob ein Hilfetext ausgegeben werden soll Parameter
     * #OPTION_NAME_HELP.
     */
    private final Boolean showHelp;

    /**
     * Evtl. Fehlermeldungen.
     */
    private final List<String> errors;

    public ExtraClientParameters(final String mandant, final File globalConfigurationDirectory,
            final File configurationDirectory, final File logDirectory, final String outputConfirm,
            final String outputFailure, final File backupDirectory, final Boolean deleteInputFiles,
            final Boolean showHelp, final List<String> errors) {
        this.mandant = mandant;
        this.globalConfigurationDirectory = globalConfigurationDirectory;
        this.configurationDirectory = configurationDirectory;
        this.logDirectory = logDirectory;
        this.outputConfirm = outputConfirm;
        this.outputFailure = outputFailure;
        this.backupDirectory = backupDirectory;
        this.deleteInputFiles = deleteInputFiles;
        this.showHelp = showHelp;
        this.errors = errors;
    }

    public ExtraClientParameters(final String mandant, final File globalConfigurationDirectory,
            final File configurationDirectory, final File logDirectory, final String outputConfirm,
            final String outputFailure, final File backupDirectory, final Boolean deleteInputFiles) {
        this.mandant = mandant;
        this.globalConfigurationDirectory = globalConfigurationDirectory;
        this.configurationDirectory = configurationDirectory;
        this.logDirectory = logDirectory;
        this.backupDirectory = backupDirectory;
        this.deleteInputFiles = deleteInputFiles;
        this.outputConfirm = outputConfirm;
        this.outputFailure = outputFailure;
        this.showHelp = false;
        this.errors = new LinkedList<String>();
    }

    public ExtraClientParameters() {
        this(null, null, null, null, null, null, null, null, true, null);
    }

    public String getMandant() {
        return mandant;
    }

    public File getGlobalConfigurationDirectory() {
        return globalConfigurationDirectory;
    }

    public File getConfigurationDirectory() {
        return configurationDirectory;
    }

    public File getLogDirectory() {
        return logDirectory;
    }

    public String getOutputConfirm() {
        return outputConfirm;
    }

    public String getOutputFailure() {
        return outputFailure;
    }

    public boolean shouldCreateBackup() {
        return backupDirectory != null;
    }

    public File getBackupDirectory() {
        return backupDirectory;
    }

    public Boolean getDeleteInputFiles() {
        return deleteInputFiles;
    }

    public Boolean getShowHelp() {
        return showHelp;
    }

    public boolean hasErrors() {
        if (showHelp) {
            return false;
        }
        if (errors != null) {
            return !errors.isEmpty();
        }
        // keine optionen angegeben
        return true;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void printErrors() {
        if (errors != null && !errors.isEmpty()) {
            System.err.println("Fehler beim Aufruf:");
            for (final String error : errors) {
                System.err.println(error);
            }
        }
    }

    public boolean isExternalCall() {
        return (outputConfirm != null) || (outputFailure != null);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ExtraClientParameters [mandant=");
        builder.append(mandant);
        builder.append(", globalConfigurationDirectory=");
        builder.append(globalConfigurationDirectory);
        builder.append(", configurationDirectory=");
        builder.append(configurationDirectory);
        builder.append(", logDirectory=");
        builder.append(logDirectory);
        builder.append(", outputConfirm=");
        builder.append(outputConfirm);
        builder.append(", outputFailure=");
        builder.append(outputFailure);
        builder.append(", backupDirectory=");
        builder.append(backupDirectory);
        builder.append(", deleteInputFiles=");
        builder.append(deleteInputFiles);
        builder.append(", showHelp=");
        builder.append(showHelp);
        builder.append(", errors=");
        builder.append(errors);
        builder.append("]");
        return builder.toString();
    }

}
