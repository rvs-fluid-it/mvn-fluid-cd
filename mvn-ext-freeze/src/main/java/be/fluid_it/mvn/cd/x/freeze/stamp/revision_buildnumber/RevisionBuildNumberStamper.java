package be.fluid_it.mvn.cd.x.freeze.stamp.revision_buildnumber;

import be.fluid_it.mvn.cd.x.freeze.stamp.Stamper;
import be.fluid_it.mvn.cd.x.freeze.stamp.TemplateStamper;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.util.Properties;

@Component( role = Stamper.class, hint = RevisionBuildNumberStamper.HINT)
public class RevisionBuildNumberStamper extends TemplateStamper<RevisionBuildNumberStamp> {
    public static final String REVISION = "revision";
    public static final String BUILD_NUMBER = "buildNumber";
    public final static String HINT = REVISION + "-" + BUILD_NUMBER;
    public static final String REVISION_BUILD_NUMBER_TEMPLATE = "${revision}-${buildNumber}";

    @Requirement
    private Logger logger;

    // Plexus
    public RevisionBuildNumberStamper() {
        super(REVISION_BUILD_NUMBER_TEMPLATE);
    }
    // Testing
    public RevisionBuildNumberStamper(Logger logger) {
        super(REVISION_BUILD_NUMBER_TEMPLATE);
        this.logger = logger;
    }

    @Override
    public RevisionBuildNumberStamp createStamp(Properties props) {
        RevisionBuildNumberStamp revisionBuildNumberStamp = new RevisionBuildNumberStamp(props.getProperty(REVISION), props.getProperty(BUILD_NUMBER));
        logger().info("[RevisionBuildNumberStamper]: Stamp " + revisionBuildNumberStamp + " created");
        return revisionBuildNumberStamp;
    }

    @Override
    public Logger logger() {
        return logger;
    }
}
