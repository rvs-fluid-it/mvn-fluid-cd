package be.fluid_it.mvn.cd.x.steps;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component( role = AbstractMavenLifecycleParticipant.class, hint = StepsExtension.STEPS)
public class StepsExtension extends AbstractMavenLifecycleParticipant {
    public static final String STEPS = "steps";

    @Requirement
    private Logger logger;

}
