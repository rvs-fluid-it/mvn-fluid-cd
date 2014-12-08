package be.fluid_it.mvn.cd.x.freeze;

import be.fluid_it.mvn.cd.x.freeze.execution.SnapshotExecutionListener;
import be.fluid_it.mvn.cd.x.freeze.execution.TeeExecutionListener;
import be.fluid_it.mvn.cd.x.freeze.model.MavenConventions;
import be.fluid_it.mvn.cd.x.freeze.stamp.Stamper;
import be.fluid_it.mvn.cd.x.freeze.stamp.StamperSwitch;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.util.LinkedList;
import java.util.List;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = FreezeExtension.FREEZE)
public class FreezeExtension extends AbstractMavenLifecycleParticipant {
    public static final String FREEZE = "freeze";

    @Requirement
    private Logger logger;

    @Requirement
    private StamperSwitch stamper;

    @Override
    public void afterSessionStart(MavenSession session)
            throws MavenExecutionException {
        // The poms are already frozen before the lifecycle participant gets notified
    }

    @Override
    public void afterSessionEnd(MavenSession session)
            throws MavenExecutionException {
    }

    @Override
    public void afterProjectsRead(MavenSession session)
            throws MavenExecutionException {
        // TODO installSnapshotEnabled
        if (stamper.isEnabled()) {
            for (MavenProject project : session.getAllProjects()) {
                for (Plugin plugin : project.getBuild().getPlugins()) {
                    List<PluginExecution> pluginExecutionClones = new LinkedList<PluginExecution>();
                    for (PluginExecution execution : plugin.getExecutions()) {
                        if (executionToBeCloned(plugin, execution.getId())) {
                            PluginExecution pluginExecutionClone = clone(execution);
                            pluginExecutionClones.add(pluginExecutionClone);
                        }

                    }
                    for (PluginExecution pluginExecutionClone : pluginExecutionClones) {
                        plugin.addExecution(pluginExecutionClone);
                        logger.info("Added cloned pluginExecution :" +
                                pluginExecutionClone.getId() +
                                ":" +
                                pluginExecutionClone.getPhase() +
                                ":" +
                                pluginExecutionClone.getGoals());
                    }
                }
            }
        }
        ExecutionListener originalExecutionListener = session.getRequest().getExecutionListener();
        session.getRequest().setExecutionListener(new TeeExecutionListener(originalExecutionListener, new SnapshotExecutionListener(logger, stamper)));
        logger.info("[FreezeExtension]: " + SnapshotExecutionListener.class.getSimpleName() + " added");
    }

    private PluginExecution clone(PluginExecution execution) {
        PluginExecution clonedExecution = new PluginExecution();
        clonedExecution.setId(execution.getId() + "-snapshot");
        clonedExecution.setGoals(execution.getGoals());
        clonedExecution.setPhase(execution.getPhase());
        clonedExecution.setPriority(execution.getPriority());
        return clonedExecution;
    }



    private boolean executionToBeCloned(Plugin plugin, String executionId) {
        List<String> clonableArtifactIds = new LinkedList<String>();
        clonableArtifactIds.add("maven-jar-plugin");
        clonableArtifactIds.add("maven-install-plugin");
        clonableArtifactIds.add("maven-deploy-plugin");
        if (executionId != null && executionId.startsWith(MavenConventions.DEFAULT_EXECUTION_ID_PREFIX)) {
            if (plugin.getGroupId().equals("org.apache.maven.plugins")) {
                for (String clonableArtifactId : clonableArtifactIds) {
                    if (plugin.getArtifactId().equals(clonableArtifactId)) return true;
                }
            }
        }
        return false;
    }

}
