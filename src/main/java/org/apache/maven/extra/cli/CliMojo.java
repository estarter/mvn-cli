package org.apache.maven.extra.cli;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;

import java.util.List;

/**
 * @author Alexey Merezhin
 */
@Mojo(name = "cli")
public class CliMojo extends AbstractMojo {
    /**
     * The Maven project.
     */
    @Parameter( defaultValue = "${project}", readonly = true, required = true )
    MavenProject project;

    /**
     * The dependency tree builder to use.
     */
    @Component( hint = "default" )
    DependencyGraphBuilder dependencyGraphBuilder;

    @Parameter( defaultValue = "${localRepository}", readonly = true )
    ArtifactRepository localRepository;

    /**
     * The dependency tree builder to use for verbose output.
     */
    @Component
    DependencyTreeBuilder dependencyTreeBuilder;

    @Component
    ArtifactMetadataSource artifactMetadataSource;


    public void execute() throws MojoExecutionException {
        try {
            showDeps();
            Cli cli = new Cli(this);
        } catch (Exception e) {
            throw new MojoExecutionException("Can't run cli", e);
        }
    }

    void showDeps() throws DependencyTreeBuilderException, ArtifactMetadataRetrievalException {
        DependencyNode node = dependencyTreeBuilder.buildDependencyTree(project, localRepository, null);
        printNode(node, "");
    }

    private void printNode(DependencyNode node, String prefix) throws ArtifactMetadataRetrievalException {
        System.out.println(prefix + node.getArtifact().toString());
        if (!"".equals(prefix)) {
            Artifact artifact = node.getArtifact();
            System.out.println(artifact);
            // List remoteRepositories = usePluginRepositories ? remotePluginRepositories : remoteArtifactRepositories;
            List remoteRepositories = project.getRemoteArtifactRepositories();
            List list = artifactMetadataSource.retrieveAvailableVersions(artifact, localRepository, remoteRepositories);
            System.out.println(list);
        }
        for (DependencyNode it : node.getChildren()) {
            printNode(it, prefix + " ");
        }
    }
}
