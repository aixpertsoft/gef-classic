/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.tests.dot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.Assert;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.dot.DotExport;
import org.junit.BeforeClass;

/**
 * Tests for the {@link DotExport} class.
 * @author Fabian Steeg (fsteeg)
 */
public class TestImageExport extends TestDotTemplate {

    private static String dotDir = null;

    @BeforeClass
    public static void setup() throws IOException {
        dotDir = dotBinDir();
    }

    /**
     * @return The directory containing the local Graphviz DOT executable, as specified in the
     *         test.properties file
     */
    public static String dotBinDir() {
        if (dotDir == null) {
            Properties props = new Properties();
            InputStream stream = TestImageExport.class.getResourceAsStream("test.properties");
            if (stream == null) {
                Assert.fail("Could not load the test.properties file in directory of "
                        + TestImageExport.class.getSimpleName());
            }
            try {
                props.load(stream);
                /* Path to the local Graphviz folder containing the dot executable file: */
                dotDir = props.getProperty("org.eclipse.zest.dot.bin.dir");
                if (dotDir == null || dotDir.trim().length() == 0) {
                    Assert.fail("Graphviz DOT directory not set in test.properties file");
                }
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return dotDir;
    }

    @Override
    protected void testDotGeneration(final Graph graph) {
        /*
         * The DotExport class wraps the simple DotTemplate class, so when we test DotExport, we
         * also run the test in the test superclass:
         */
        super.testDotGeneration(graph);
        File image = new DotExport(graph).toImage(dotDir, "pdf");
        Assert.assertNotNull("Image must not be null", image);
        System.out.println("Created image: " + image);
        Assert.assertTrue("Image must exist", image.exists());
    }

}
