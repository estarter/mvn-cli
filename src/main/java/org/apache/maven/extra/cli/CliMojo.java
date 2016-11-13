package org.apache.maven.extra.cli;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;
import org.codehaus.mojo.versions.AbstractVersionsDependencyUpdaterMojo;
import org.codehaus.mojo.versions.api.ArtifactVersions;
import org.codehaus.mojo.versions.rewriting.ModifiedPomXMLEventReader;

import javax.xml.stream.XMLStreamException;


/**
 * @author Alexey Merezhin
 * @goal cli
 * @requiresProject true
 * @requiresDirectInvocation true
 */
public class CliMojo  extends AbstractVersionsDependencyUpdaterMojo
{

    public void execute() throws MojoExecutionException {
        try {
            showDeps();
            Cli cli = new Cli(this);
        } catch (Exception e) {
            throw new MojoExecutionException("Can't run cli", e);
        }
    }

    protected void update(ModifiedPomXMLEventReader modifiedPomXMLEventReader) throws MojoExecutionException, MojoFailureException, XMLStreamException, ArtifactMetadataRetrievalException {
        System.out.println("update?");
    }

    void showDeps() throws DependencyTreeBuilderException, ArtifactMetadataRetrievalException, MojoExecutionException {
        System.out.println("get proj " + getProject());

        for (Object dep : getProject().getDependencies()) {
            Artifact artifact = toArtifact((Dependency) dep);
            ArtifactVersions versions = getHelper().lookupArtifactVersions( artifact, false );
            System.out.println(versions.toString());
        }
//        for (Object dep : getProject().getDependencies()) {
//            System.out.println(((Dependency) dep).getArtifactId());
//        }
//        DependencyNode node =
//                dependencyTreeBuilder.buildDependencyTree(project, localRepository, null);
//        printNode(node, "");
    }

    private void printNode(DependencyNode node, String prefix) throws MojoExecutionException, ArtifactMetadataRetrievalException {
        System.out.println(prefix + node.getArtifact().toString());
        if (!"".equals(prefix)) {
            System.out.println("looking for latest version");
            getHelper().lookupArtifactVersions(node.getArtifact(), false );
        }

        for (org.apache.maven.shared.dependency.tree.DependencyNode it : node.getChildren()) {
            printNode(it, prefix + " ");
        }

    }
}
