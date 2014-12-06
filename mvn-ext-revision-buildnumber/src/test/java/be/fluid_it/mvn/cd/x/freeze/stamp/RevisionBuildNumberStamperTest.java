package be.fluid_it.mvn.cd.x.freeze.stamp;

import be.fluid_it.mvn.cd.x.freeze.model.MavenConventions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static be.fluid_it.mvn.cd.x.freeze.stamp.RevisionBuildNumberStamper.*;
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
        assertEquals(new RevisionBuildNumberStamp(THE_REVISION, THE_BUILD_NUMBER), new RevisionBuildNumberStamper().createStamp(PROPS));
    }

    @Test
    public void stamp() {
        assertEquals(THE_MAJOR_MINOR_REVISION + "-" + THE_REVISION + "-" + THE_BUILD_NUMBER, new RevisionBuildNumberStamper().stamp(THE_MAJOR_MINOR_REVISION + "-" + MavenConventions.SNAPSHOT, PROPS));
    }

    @Test
    public void extract() {
        assertEquals(new RevisionBuildNumberStamp(THE_REVISION, THE_BUILD_NUMBER), new RevisionBuildNumberStamper().extract(THE_MAJOR_MINOR_REVISION + "-" + THE_REVISION + "-" + THE_BUILD_NUMBER));
    }

    @Test
    public void regularExpressionComplexity() {
        assertEquals("(.+?)-(.+?)", REVISION_BUILD_NUMBER_TEMPLATE.replaceAll(PLACEHOLDER_EXP, "(.+?)"));
    }

    @Test
    public void isEnabled() {
        assertTrue(new RevisionBuildNumberStamper().isEnabled(PROPS));
        assertFalse(new RevisionBuildNumberStamper().isEnabled(new Properties()));
    }
}