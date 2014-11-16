package be.fluid.mvn.cd.x.steps;

import be.fluid.mvn.cd.x.multi.ExtensionRegistry;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component( role = AbstractMavenLifecycleParticipant.class, hint = StepsExtension.STEPS)
public class StepsExtension extends AbstractMavenLifecycleParticipant {
    public static final String STEPS = "steps";

    @Requirement
    private Logger logger;

    @Requirement
    private ExtensionRegistry extensionRegistry;

    public void afterProjectsRead( MavenSession session ) {
        extensionRegistry.register(STEPS);
    }

}
