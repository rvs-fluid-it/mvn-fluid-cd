package be.fluid_it.mvn.cd.x.freeze.stamp.revision_buildnumber;

import be.fluid_it.mvn.cd.x.freeze.stamp.Stamper;
import be.fluid_it.mvn.cd.x.freeze.stamp.TemplateStamper;
import org.codehaus.plexus.component.annotations.Component;

import java.util.Properties;

@Component( role = Stamper.class, hint = RevisionBuildNumberStamper.HINT)
public class RevisionBuildNumberStamper extends TemplateStamper<RevisionBuildNumberStamp> {
    public static final String REVISION = "revision";
    public static final String BUILD_NUMBER = "buildNumber";
    public final static String HINT = REVISION + "-" + BUILD_NUMBER;
    public static final String REVISION_BUILD_NUMBER_TEMPLATE = "${revision}-${buildNumber}";

    public RevisionBuildNumberStamper() {
        super(REVISION_BUILD_NUMBER_TEMPLATE);
    }

    @Override
    public RevisionBuildNumberStamp createStamp(Properties props) {
        return new RevisionBuildNumberStamp(props.getProperty(REVISION), props.getProperty(BUILD_NUMBER));
    }
}
