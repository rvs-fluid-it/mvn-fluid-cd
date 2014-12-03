package be.fluid_it.mvn.cd.x.freeze.execution;

import be.fluid_it.mvn.cd.x.freeze.model.MavenConventions;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.execution.AbstractExecutionListener;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.Logger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SnapshotExecutionListener extends AbstractExecutionListener {
    private final Logger logger;

    private Artifact frozenArtifact;
    private File frozenPomFile;
    private String frozenFinalName;
    private List<Artifact> frozenAttachedArtifacts;

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

            frozenPomFile = event.getProject().getFile();
            event.getProject().setFile(new File( frozenPomFile.getParent(), MavenConventions.POM_FILE));

            frozenArtifact = event.getProject().getArtifact();
            Artifact snapshotArtifact = ArtifactUtils.copyArtifact(frozenArtifact);
            snapshotArtifact.setVersion(transformToSnapshot(frozenArtifact.getVersion()));
            event.getProject().setArtifact(snapshotArtifact);

            if (!isPomArtifact(event.getProject())) {
                frozenFinalName = event.getProject().getBuild().getFinalName();
                event.getProject().getBuild().setFinalName(transformToSnapshot(frozenFinalName));

                event.getProject().getArtifact().setFile(new File(transformToSnapshot(frozenArtifact.getFile().getAbsolutePath()) + ".jar"));
                logger.info("Switched frozenArtifact file to " + transformToSnapshot(frozenArtifact.getFile().getAbsolutePath()) + ".jar");
            }
            frozenAttachedArtifacts = new LinkedList<Artifact>();

            for (Artifact attachedArtifact : event.getProject().getAttachedArtifacts()) {
                String attachedFileAbsolutePath = attachedArtifact.getFile().getAbsolutePath();
                logger.info(">>>> Attached frozenArtifact file : " + attachedFileAbsolutePath);
                frozenAttachedArtifacts.add(attachedArtifact);
            }
            event.getProject().getAttachedArtifacts().clear();
            for (Artifact frozenAttachedArtifact : frozenAttachedArtifacts) {
                Artifact unfrozenAttachedArtifact = ArtifactUtils.copyArtifact(frozenAttachedArtifact);
                unfrozenAttachedArtifact.setVersion(transformToSnapshot(unfrozenAttachedArtifact.getVersion()));
                event.getProject().addAttachedArtifact(unfrozenAttachedArtifact);
            }
        }
    }

    private String transformToSnapshot(String data) {
        String[] parts = data.split("-");
        StringBuffer result = new StringBuffer();
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
            if (!event.getProject().getFile().equals(frozenPomFile)) {
                event.getProject().setFile(frozenPomFile);
                event.getProject().setArtifact(frozenArtifact);

                event.getProject().getAttachedArtifacts().clear();
                for (Artifact frozenAttachedArtifact : frozenAttachedArtifacts) {
                    event.getProject().addAttachedArtifact(frozenAttachedArtifact);
                }

                if (!isPomArtifact(event.getProject())) {
                    event.getProject().getBuild().setFinalName(frozenFinalName);
                }
            }
        }
    }
}
