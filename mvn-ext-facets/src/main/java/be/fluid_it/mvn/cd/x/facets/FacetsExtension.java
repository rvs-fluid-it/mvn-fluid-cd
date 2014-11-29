package be.fluid_it.mvn.cd.x.facets;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component( role = AbstractMavenLifecycleParticipant.class, hint = FacetsExtension.FACETS)
public class FacetsExtension extends AbstractMavenLifecycleParticipant {
    public static final String FACETS = "facets";

    @Requirement
    private Logger logger;

}
