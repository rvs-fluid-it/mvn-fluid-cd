package be.fluid.mvn.cd.x.freeze.locator;

import be.fluid.mvn.cd.x.freeze.FreezeException;
import be.fluid.mvn.cd.x.freeze.FreezeExtension;
import be.fluid.mvn.cd.x.freeze.mapping.ArtifactFreezeMapping;
import be.fluid.mvn.cd.x.freeze.replace.PomFreezer;
import org.apache.maven.model.locator.DefaultModelLocator;
import org.apache.maven.model.locator.ModelLocator;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.File;

import static be.fluid.mvn.cd.x.freeze.FreezeExtension.freezingEnabled;

@Component(role = ModelLocator.class)
public class FreezeModelLocator implements ModelLocator {
    static {
        if (freezingEnabled()) {
            System.out.println("[INFO] [FreezeExtension]: Freezing poms ...");
        }
    }


    @Requirement
    private Logger logger;

    @Requirement
    private PomFreezer pomFreezer;

    @Requirement
    private ArtifactFreezeMapping artifactFreezeMapping;

    @Override
    public File locatePom(File projectDirectory) {
        try {
            File pomFile = new DefaultModelLocator().locatePom(projectDirectory);
            if (freezingEnabled()) {
                logger.info("[FreezeModelLocator]: Freeze pom in folder " + projectDirectory.getAbsolutePath());
                artifactFreezeMapping.put(FreezeExtension.getRevision(), pomFile);
                logger.debug("[FreezeModelLocator]: Freezing pom " + pomFile.getAbsolutePath());
                return pomFreezer.freeze(pomFile);
            } else {
                return pomFile;
            }
        } catch (RuntimeException e) {
            throw new FreezeException("Unable to locate " +
                    (freezingEnabled()? " frozen " : "") +
                    "pom in directory [" + projectDirectory + "] ...", e);
        }
    }

}
