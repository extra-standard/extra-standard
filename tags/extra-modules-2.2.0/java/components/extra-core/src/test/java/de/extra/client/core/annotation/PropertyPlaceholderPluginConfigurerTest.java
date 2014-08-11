package de.extra.client.core.annotation;

import java.util.Map;

import junit.framework.Assert;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Thorsten Vogel
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class PropertyPlaceholderPluginConfigurerTest {

    private final static char[] ENCRYPTION_PASSWORD = new char[] {'t','e','s','t','v','o','g','e','l',};

    private final static String ENCRYPTED_TEXT = "This text is highly confidential!";

    private ClassPathXmlApplicationContext context;

    @Before
    public void setUp() throws Exception {
        context = new ClassPathXmlApplicationContext("/configurer/configurer-context-test.xml");
    }

    @Test
    public void testPropertyPlaceholderPluginConfigurer() {
        @SuppressWarnings("unchecked")
        final Map<String, String> texts = context.getBean("textmap", Map.class);
        final String notencrypted = texts.get("notencrypted");
        Assert.assertEquals("This text is not encrypted.", notencrypted);
        final String decrypted = texts.get("decrypted");
        Assert.assertEquals(ENCRYPTED_TEXT, decrypted);
    }

    void printEncryptedText() {
        final StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        final SimpleStringPBEConfig pbeConfig = new SimpleStringPBEConfig();
        pbeConfig.setAlgorithm("PBEWithMD5AndDES");
        pbeConfig.setPasswordCharArray(ENCRYPTION_PASSWORD);
        stringEncryptor.setConfig(pbeConfig);
        System.out.println(stringEncryptor.encrypt(ENCRYPTED_TEXT));
    }

}
