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
package de.extra.client.plugins.dataplugin;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import de.extra.client.core.model.inputdata.impl.FileInputData;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * @author Leonid Potap
 * @version $Id$
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class FileDataPluginTest {

    // private final static Logger logger =
    // LoggerFactory.getLogger(FileDataPluginTest.class);

    @InjectMocks
    FileDataPlugin fileDataPlugin;

    private final Integer inputDataLimit = 2;

    private static final String TEST_PATH = "testInputData";

    private File inputDirectory;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	fileDataPlugin.setInputDataLimit(inputDataLimit);
	inputDirectory = new ClassPathResource(TEST_PATH).getFile();
	ReflectionTestUtils.setField(fileDataPlugin, "inputDirectory",
		inputDirectory);
    }

    /**
     * Test method for
     * {@link de.extra.client.plugins.dataplugin.FileDataPlugin#initInputData()}
     * .
     */
    @Test
    public final void testInitData() {
	Assert.assertFalse("InputData is empty", fileDataPlugin.isEmpty());
    }

    /**
     * Test method for
     * {@link de.extra.client.plugins.dataplugin.FileDataPlugin#hasMoreData()}.
     */
    @Test
    public final void testHasMoreData() {
	Assert.assertTrue("InputData hasMoreData false",
		fileDataPlugin.hasMoreData());
    }

    /**
     * Test method for
     * {@link de.extra.client.plugins.dataplugin.FileDataPlugin#getData()}.
     */
    @Test
    public final void testGetData() {
	int counter = 0;
	while (fileDataPlugin.hasMoreData()) {
	    final IInputDataContainer iInputDataContainer = fileDataPlugin
		    .getData();
	    Assert.assertFalse(iInputDataContainer.isContentEmpty());
	    Assert.assertTrue(iInputDataContainer
		    .isImplementationOf(FileInputData.class));
	    Assert.assertTrue("iInputDataContainer size größer als Limit",
		    iInputDataContainer.getContentSize() <= inputDataLimit);
	    counter = counter + iInputDataContainer.getContentSize();
	}
	final Collection<File> listFiles = FileUtils.listFiles(inputDirectory,
		TrueFileFilter.INSTANCE, null);

	Assert.assertEquals("Number of messages does not match",
		listFiles.size(), counter);
    }
}
