package be.fluid_it.mvn.cd.x.freeze.stamp.revision_buildnumber;

import be.fluid_it.mvn.cd.x.freeze.model.MavenConventions;
import be.fluid_it.mvn.cd.x.freeze.stamp.revision_buildnumber.RevisionBuildNumberStamp;
import be.fluid_it.mvn.cd.x.freeze.stamp.revision_buildnumber.RevisionBuildNumberStamper;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Test;

import java.util.Properties;

import static be.fluid_it.mvn.cd.x.freeze.stamp.revision_buildnumber.RevisionBuildNumberStamper.*;
import static org.junit.Assert.*;

public class RevisionBuildNumberStamperTest {
    public static final String THE_MAJOR_MINOR_REVISION = "1.0";
    public static final String THE_REVISION = "12";
    public static final String THE_BUILD_NUMBER = "34";
    public final static Properties PROPS = new Properties();

    static {
        PROPS.put(REVISION, THE_REVISION);
        PROPS.put(BUILD_NUMBER, THE_BUILD_NUMBER);
    }

    @Test
    public void createStampFromProperties() {
        assertEquals(new RevisionBuildNumberStamp(THE_REVISION, THE_BUILD_NUMBER), revisionBuildNumberStamper().createStamp(PROPS));
    }

    private RevisionBuildNumberStamper revisionBuildNumberStamper() {
        return new RevisionBuildNumberStamper(new ConsoleLogger());
    }

    @Test
    public void stamp() {
        assertEquals(THE_MAJOR_MINOR_REVISION + "-" + THE_REVISION + "-" + THE_BUILD_NUMBER, revisionBuildNumberStamper().stamp(THE_MAJOR_MINOR_REVISION + "-" + MavenConventions.SNAPSHOT, PROPS));
    }

    @Test
    public void extract() {
        assertEquals(new RevisionBuildNumberStamp(THE_REVISION, THE_BUILD_NUMBER), revisionBuildNumberStamper().extract(THE_MAJOR_MINOR_REVISION + "-" + THE_REVISION + "-" + THE_BUILD_NUMBER));
    }

    @Test
    public void asStamp() {
        assertEquals(THE_REVISION + "-" + THE_BUILD_NUMBER, revisionBuildNumberStamper().asString(new RevisionBuildNumberStamp(THE_REVISION, THE_BUILD_NUMBER)));
    }

    @Test
    public void unFreeze() {
        assertEquals(THE_MAJOR_MINOR_REVISION + "-" + MavenConventions.SNAPSHOT, revisionBuildNumberStamper().unfreeze(THE_MAJOR_MINOR_REVISION + "-" + THE_REVISION + "-" + THE_BUILD_NUMBER));
    }

    @Test
    public void regularExpressionComplexity() {
        assertEquals("(.+?)-(.+?)", REVISION_BUILD_NUMBER_TEMPLATE.replaceAll(PLACEHOLDER_EXP, "(.+?)"));
    }

    @Test
    public void isEnabled() {
        assertTrue(revisionBuildNumberStamper().isEnabled(PROPS));
        assertFalse(revisionBuildNumberStamper().isEnabled(new Properties()));
    }
}