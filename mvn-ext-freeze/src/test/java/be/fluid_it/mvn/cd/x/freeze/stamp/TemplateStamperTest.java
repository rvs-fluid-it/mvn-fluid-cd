package be.fluid_it.mvn.cd.x.freeze.stamp;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class TemplateStamperTest {

    public static final String REVISION = "revision";
    public static final String BUILD_NUMBER = "buildNumber";
    public static final String TEMPLATE = "${" + REVISION + "}-${" + BUILD_NUMBER + "}";

    @Test
    public void extractKeysFromFormatString() {
        Set<String> keys = TemplateStamper.extractKeys(TEMPLATE);
        assertNotNull(keys);
        assertEquals(2, keys.size());
        assertTrue(keys.contains(REVISION));
        assertTrue(keys.contains(BUILD_NUMBER));
    }

}