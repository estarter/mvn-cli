package org.apache.maven.extra.cli;

import java.io.File;

/**
 * @author Alexey Merezhin
 */
public class MavenProject {
    File location;

    public MavenProject(String path) throws Exception {
        location = new File(path);
        if (location.isFile()) {
            location = location.getParentFile();
        }

        File pomFile = new File(location + File.separator + "pom.xml");
        if (!pomFile.exists()) throw new Exception("File not found: " + pomFile.getPath());
    }
}
