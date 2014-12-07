package be.fluid_it.mvn.cd.x.freeze.locator;

import be.fluid_it.mvn.cd.x.freeze.FreezeException;
import be.fluid_it.mvn.cd.x.freeze.FreezeExtension;
import be.fluid_it.mvn.cd.x.freeze.mapping.ArtifactFreezeMapping;
import be.fluid_it.mvn.cd.x.freeze.replace.PomFreezer;
import be.fluid_it.mvn.cd.x.freeze.stamp.Stamper;
import be.fluid_it.mvn.cd.x.freeze.stamp.StamperSwitch;
import org.apache.maven.model.locator.DefaultModelLocator;
import org.apache.maven.model.locator.ModelLocator;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.File;

@Component(role = ModelLocator.class)
public class FreezeModelLocator implements ModelLocator {
    @Requirement
    private Logger logger;

    @Requirement
    private PomFreezer pomFreezer;

    @Requirement
    private ArtifactFreezeMapping artifactFreezeMapping;

    @Requirement
    private StamperSwitch stamper;

    @Override
    public File locatePom(File projectDirectory) {
        try {
            File pomFile = new DefaultModelLocator().locatePom(projectDirectory);
            if (stamper.isEnabled()) {
                logger.info("[FreezeModelLocator]: Freeze pom in folder " + projectDirectory.getAbsolutePath());
                artifactFreezeMapping.put(pomFile);
                logger.debug("[FreezeModelLocator]: Freezing pom " + pomFile.getAbsolutePath());
                return pomFreezer.freeze(pomFile);
            } else {
                return pomFile;
            }
        } catch (RuntimeException e) {
            throw new FreezeException("Unable to locate " +
                    (stamper.isEnabled() ? " frozen " : "") +
                    "pom in directory [" + projectDirectory + "] ...", e);
        }
    }

}
