package be.fluid.mvn.cd.x.multi;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component( role = AbstractMavenLifecycleParticipant.class, hint = "multi" )
public class MultiExtensions
        extends AbstractMavenLifecycleParticipant
{
    @Requirement
    private Logger logger;

    @Requirement
    private RuntimeInformation runtime;

    public void afterProjectsRead( MavenSession session ) {
        logger.info( " __  __" );
        logger.info( "|  \\/  |__ _Apache__ ___" );
        logger.info( "| |\\/| / _` \\ V / -_) ' \\  ~ intelligent projects ~" );
        logger.info( "|_|  |_\\__,_|\\_/\\___|_||_|  v. " + runtime.getMavenVersion() );
        logger.info( "Fluid Extensions:" );
    }
}
