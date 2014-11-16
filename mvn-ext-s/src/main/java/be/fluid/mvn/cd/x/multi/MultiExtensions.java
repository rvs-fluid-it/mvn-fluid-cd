package be.fluid.mvn.cd.x.multi;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component( role = AbstractMavenLifecycleParticipant.class, hint = MultiExtensions.MULTI)
public class MultiExtensions extends AbstractMavenLifecycleParticipant
{
    public static final String MULTI = "multi";

    @Requirement
    private Logger logger;

    @Requirement
    private RuntimeInformation runtime;

    @Requirement
    private ExtensionRegistry extensionRegistry;

    public void afterProjectsRead( MavenSession session ) {
        logger.info( " __  __" );
        logger.info( "|  \\/  |__ _Apache__ ___" );
        logger.info( "| |\\/| / _` \\ V / -_) ' \\  ~ intelligent projects ~" );
        logger.info( "|_|  |_\\__,_|\\_/\\___|_||_|  v. " + runtime.getMavenVersion() );
        logger.info( "+ Fluid extensions: " + extensionRegistry.asText());
    }
}
