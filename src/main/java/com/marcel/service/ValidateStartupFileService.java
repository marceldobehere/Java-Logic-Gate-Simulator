package com.marcel.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.StandardCopyOption;

public final class ValidateStartupFileService {

    private static final Logger log = LoggerFactory.getLogger(ValidateStartupFileService.class);
    private static final String configDirName = ".Logic Gate Simulator";
    private static final File configFolder = new File(
            FileUtils.getUserDirectory(),
            configDirName
    );


    public static void validateLocalResources() {
        if (configFolder.exists())
            return;

        log.debug("Config Folder does not exist.");
        configFolder.mkdir();
        try {
            final File resourceFile = getLocalResources();
            FileUtils.copyDirectory(resourceFile, new File(configFolder, resourceFile.getName()), null, true, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO: Make recursive method to copy files which dont exist yet.
    }

    private static File getLocalResources() throws URISyntaxException {
        return new File(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("internal")
                        .toURI()
        );
    }

}
