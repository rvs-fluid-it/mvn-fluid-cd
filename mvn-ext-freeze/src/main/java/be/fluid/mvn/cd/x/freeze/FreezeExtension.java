package be.fluid.mvn.cd.x.freeze;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component( role = AbstractMavenLifecycleParticipant.class, hint = FreezeExtension.FREEZE)
public class FreezeExtension extends AbstractMavenLifecycleParticipant {
    public static final String FREEZE = "freeze";

    @Requirement
    private Logger logger;


}
