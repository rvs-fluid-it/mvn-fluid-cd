package be.fluid.mvn.cd.x.freeze.execution;

import be.fluid.mvn.cd.x.freeze.model.MavenConventions;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.execution.AbstractExecutionListener;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.ProjectArtifactMetadata;
import org.codehaus.plexus.logging.Logger;

import java.io.File;

public class SnapshotExecutionListener extends AbstractExecutionListener {
    private final Logger logger;

    private Artifact artifact;
    private File pomFile;

    private File artifactFile;
    private String finalName;

    private String version;
    private ProjectArtifactMetadata projectArtifactMetadata;

    public SnapshotExecutionListener(final Logger logger) {
        this.logger = logger;
    }

    private boolean isPomArtifact(MavenProject project) {
        return MavenConventions.POM_PACKAGING.equals(project.getPackaging());
    }

    @Override
    public void mojoStarted( ExecutionEvent event )
    {

        if (event.getMojoExecution().getExecutionId() != null && event.getMojoExecution().getExecutionId().endsWith("-snapshot")) {

            pomFile = event.getProject().getFile();
            event.getProject().setFile(new File( pomFile.getParent(), MavenConventions.POM_FILE));

            artifact = event.getProject().getArtifact();
            version = event.getProject().getArtifact().getVersion();
            Artifact snapshotArtifact = ArtifactUtils.copyArtifact(artifact);
            snapshotArtifact.setVersion(transformToSnapshot(artifact.getVersion()));
            event.getProject().setArtifact(snapshotArtifact);

            if (!isPomArtifact(event.getProject())) {
                finalName = event.getProject().getBuild().getFinalName();
                event.getProject().getBuild().setFinalName(transformToSnapshot(finalName));

                // TODO
                event.getProject().getArtifact().setFile(new File(transformToSnapshot(artifact.getFile().getAbsolutePath()) + ".jar"));
                logger.info("Switched artifact file to " + transformToSnapshot(artifact.getFile().getAbsolutePath()) + ".jar");
                /*
                for (ArtifactMetadata artifactMetadata : event.getProject().getArtifact().getMetadataList()) {
                    if (artifactMetadata instanceof ProjectArtifactMetadata) {
                        logger.info(">>>> ProjectArtifactMetadata file : " + ((ProjectArtifactMetadata)artifactMetadata).getFile());
                        projectArtifactMetadata = (ProjectArtifactMetadata)artifactMetadata;
                    }
                }

                if (projectArtifactMetadata != null) {
                    event.getProject().getArtifact().getMetadataList().remove(projectArtifactMetadata);
                }
                */
            }
        }
    }

    private String transformToSnapshot(String data) {
        String[] parts = data.split("-");
        StringBuffer result = new StringBuffer();
        String[] lastParts = parts[parts.length -1].split(".");
        for (int i = 0; i < parts.length -1; i++) {
            result.append(parts[i]).append("-");
        }
        return result.append("SNAPSHOT").toString();
    }

    @Override
    public void mojoSucceeded( ExecutionEvent event ) {
        restoreProjectState(event);
    }

    @Override
    public void mojoFailed( ExecutionEvent event ) {
        restoreProjectState(event);
    }

    private void restoreProjectState(ExecutionEvent event) {
        if (event.getMojoExecution().getExecutionId() != null && event.getMojoExecution().getExecutionId().endsWith("-snapshot")) {
            if (!event.getProject().getFile().equals(pomFile)) {
                event.getProject().setFile(pomFile);
                //event.getProject().getArtifact().setVersion(version);
                event.getProject().setArtifact(artifact);

                if (!isPomArtifact(event.getProject())) {
                    event.getProject().getBuild().setFinalName(finalName);
                    /*
                    event.getProject().getArtifact().setFile(artifactFile);

                    if (projectArtifactMetadata != null) {
                        event.getProject().getArtifact().getMetadataList().add(projectArtifactMetadata);
                    }
                    */
                }
            }
        }
    }


}
