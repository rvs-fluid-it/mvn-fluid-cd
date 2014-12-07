package be.fluid_it.mvn.cd.x.freeze.execution;

import org.apache.maven.execution.AbstractExecutionListener;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.logging.Logger;

public class MojoExecutionListener extends AbstractExecutionListener {
    private final Logger logger;

    public MojoExecutionListener(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void mojoSkipped(ExecutionEvent event) {
        logger.info(mojoExecutionFullId(event) + " skipped ...");
    }

    private String mojoExecutionFullId(ExecutionEvent event) {
        StringBuffer result = new StringBuffer("[");
        if (event.getMojoExecution() != null) {
            String phase = event.getMojoExecution().getLifecyclePhase();
            if (phase != null) {
                result.append(phase).append(":");
            }
            Plugin plugin = event.getMojoExecution().getPlugin();
            if (plugin != null) {
                result.append(plugin.getGroupId()).append(":").append(plugin.getArtifactId()).append(":").append(plugin.getVersion());
            }

            String executionId = event.getMojoExecution().getExecutionId();
            if (executionId != null) {
                result.append(":").append(executionId);
            }
            String goal = event.getMojoExecution().getGoal();
            if (executionId != null) {
                result.append(":").append(goal);
            }
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public void mojoStarted(ExecutionEvent event) {
        logger.info(mojoExecutionFullId(event) + " started ...");
    }

    @Override
    public void mojoSucceeded(ExecutionEvent event) {
        logger.info(mojoExecutionFullId(event) + " succeeded ...");
    }

    @Override
    public void mojoFailed(ExecutionEvent event) {
        logger.info(mojoExecutionFullId(event) + " failed ...");
    }
}
