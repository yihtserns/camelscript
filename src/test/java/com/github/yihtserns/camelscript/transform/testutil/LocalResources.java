/*
 * Copyright 2013 yihtserns.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.yihtserns.camelscript.transform.testutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Format of resources: ${filenamePrefix}${filenameSuffixAndExt}.
 *
 * @author yihtserns
 * @see #forClass(Class)
 * @see #getFile(String)
 */
public class LocalResources {

    private String filenamePrefix = "";
    private File baseDirectory;

    /**
     * Use factory method {@link #forClass(Class)}.
     */
    private LocalResources(final File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public File getFile(final String filenameSuffixAndExt) throws FileNotFoundException, IOException {
        File file = new File(baseDirectory, filenamePrefix + filenameSuffixAndExt);
        if (file.exists()) {
            return file;
        }
        throw new FileNotFoundException(file.getCanonicalPath());
    }

    private void setFilenamePrefix(final String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
    }

    /**
     * {@code filenamePrefix}=${class' simple name}_
     *
     * @param clazz to load resources from the same package/folder level
     * @see LocalResources
     */
    public static LocalResources forClass(final Class clazz) {
        try {
            LocalResources resources = new LocalResources(new File(clazz.getResource(".").toURI()));
            resources.setFilenamePrefix(clazz.getSimpleName() + "_");

            return resources;
        } catch (URISyntaxException ex) {
            // Should not happen
            throw new RuntimeException(ex);
        }
    }
}