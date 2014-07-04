package de.extra.client.starter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Thorsten Vogel
 */
public class ExtraClientParametersTest {

    @Test
    public void testExtraClientParametersHelp() {
        final ExtraClientParameters parameters = new ExtraClientParameters();
        assertTrue(parameters.getShowHelp());
    }

    @Test
    public void testExtraClientParameters() throws Exception {
        final List<String> errors = Collections.<String>emptyList();

        final Resource globalConfigDir = new ClassPathResource("/testglobalconfig");
        final File globalConfigPath = globalConfigDir.getFile();

        final Resource configDir = new ClassPathResource("/testconfig");
        final File configPath = configDir.getFile();

        final Resource logDir = new ClassPathResource("/testlog");
        final File logPath = logDir.getFile();

        final ExtraClientParameters parameters = new ExtraClientParameters("mandant",
                globalConfigPath, configPath, logPath,
                "outputConfirm", "outputFailure", false, errors);

        assertFalse(parameters.getShowHelp());

        assertEquals(globalConfigPath, parameters.getGlobalConfigurationDirectory());
        assertEquals(configPath, parameters.getConfigurationDirectory());
        assertEquals(logPath, parameters.getLogDirectory());
        assertEquals(errors, parameters.getErrors());
        assertEquals("mandant", parameters.getMandant());
        assertEquals("outputConfirm", parameters.getOutputConfirm());
        assertEquals("outputFailure", parameters.getOutputFailure());
    }
}
