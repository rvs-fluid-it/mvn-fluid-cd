package be.fluid.mvn.cd.x.freeze.locator;

import be.fluid.mvn.cd.x.freeze.FreezeExtension;
import org.apache.maven.model.locator.DefaultModelLocator;
import org.apache.maven.model.locator.ModelLocator;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.File;
import java.io.IOException;

@Component( role = ModelLocator.class )
public class FreezeModelLocator implements ModelLocator {
    @Requirement
    private Logger logger;

    //@Requirement
    //private PomFreezer pomFreezer;

    @Override
    public File locatePom(File projectDirectory) {
        File pomFile = new DefaultModelLocator().locatePom(projectDirectory);
        if (FreezeExtension.freezingEnabled()) {
            try {
                logger.debug("Freezing pom " + pomFile.getCanonicalPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //return pomFreezer.freeze(pomFile);
            return pomFile;
        } else {
            return pomFile;
        }
    }

}
