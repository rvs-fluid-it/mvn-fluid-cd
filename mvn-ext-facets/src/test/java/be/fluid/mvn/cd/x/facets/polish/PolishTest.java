package be.fluid.mvn.cd.x.facets.polish;

import be.fluid.mvn.cd.x.facets.pom.SamplePom;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class PolishTest {

    @Test
    public void testPolishPom() {
        ByteArrayOutputStream pomOutputStream = new ByteArrayOutputStream();
        ConsoleLogger logger = new ConsoleLogger();
        logger.setThreshold(Logger.LEVEL_DEBUG);
        new DefaultPomPolisher(null, logger).polish(SamplePom.asStream(), pomOutputStream);
        String pomContent = pomOutputStream.toString();
        System.out.print(pomContent);
    }

}
