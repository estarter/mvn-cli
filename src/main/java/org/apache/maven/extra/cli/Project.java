package org.apache.maven.extra.cli;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.DefaultArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.project.*;
import org.apache.maven.shared.dependency.tree.DefaultDependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;

import java.io.File;

/**
 * @author Alexey Merezhin
 */
public class Project {
    File location;

    public Project(String path) throws Exception {
        location = new File(path);
        if (location.isFile()) {
            location = location.getParentFile();
        }

        File pomFile = new File(location + File.separator + "pom.xml");
        if (!pomFile.exists()) throw new Exception("File not found: " + pomFile.getPath());


        DependencyTreeBuilder dependencyTreeBuilder = new DefaultDependencyTreeBuilder();

        MavenProjectBuilder builder = new DefaultMavenProjectBuilder();
        ProjectBuilderConfiguration config = new DefaultProjectBuilderConfiguration();
        MavenProject project = builder.build(pomFile, config);

        final ArtifactRepository localRepository = new DefaultArtifactRepository(
                "local",
                new File("/Users/estarter/.m2/repository").toURI().toURL().toString(),
                new DefaultRepositoryLayout());
        // ArtifactRepository repo = new DefaultArtifactRepository("id", "url", new DefaultRepositoryLayout());

        DependencyNode node = dependencyTreeBuilder.buildDependencyTree(project, localRepository, null);
    }
}
