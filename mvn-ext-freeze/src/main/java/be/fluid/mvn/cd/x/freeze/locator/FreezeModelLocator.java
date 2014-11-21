package be.fluid.mvn.cd.x.freeze.locator;

import be.fluid.mvn.cd.x.freeze.FreezeException;
import be.fluid.mvn.cd.x.freeze.FreezeExtension;
import be.fluid.mvn.cd.x.freeze.replace.PomFreezer;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.model.locator.DefaultModelLocator;
import org.apache.maven.model.locator.ModelLocator;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.File;
import java.io.IOException;

@Component(role = ModelLocator.class)
public class FreezeModelLocator implements ModelLocator {
    @Requirement
    private Logger logger;

    @Requirement
    private PomFreezer pomFreezer;

    @Override
    public File locatePom(File projectDirectory) {
        try {
            logger.info("[FreezeModelLocator]: locate pom in " + projectDirectory.getAbsolutePath());
            File pomFile = new DefaultModelLocator().locatePom(projectDirectory);
            if (FreezeExtension.freezingEnabled()) {
                logger.debug("[FreezeModelLocator]: Freezing pom " + pomFile.getAbsolutePath());
                return pomFreezer.freeze(pomFile);
            } else {
                return pomFile;
            }
        } catch (Error e) {
            throw new FreezeException("Unable to locate pom in directory [" + projectDirectory + "] ...", e);
        } catch (RuntimeException e) {
            throw new FreezeException("Unable to locate pom in directory [" + projectDirectory + "] ...", e);
        }
    }

}
