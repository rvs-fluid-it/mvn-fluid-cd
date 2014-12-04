package be.fluid_it.mvn.cd.x.freeze.replace;

import be.fluid_it.mvn.cd.x.freeze.FreezeExtension;
import be.fluid_it.mvn.cd.x.freeze.mapping.ArtifactFreezeMapping;
import be.fluid_it.mvn.cd.x.freeze.model.MavenConventions;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.model.locator.ModelLocator;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.File;

import static be.fluid_it.mvn.cd.x.freeze.FreezeExtension.freezingEnabled;

@Component( role = EventSpy.class, hint = FreezePomSpy.HINT)
public class FreezePomSpy implements EventSpy {
    public static final String HINT = "freezePomSpy";

    @Requirement
    private Logger logger;

    @Requirement
    private ModelLocator modelLocator;

    @Requirement
    private PomFreezer pomFreezer;

    @Requirement
    private ArtifactFreezeMapping artifactFreezeMapping;

    @Override
    public void init(Context context) throws Exception {
        logger.info("[FreezePomSpy]: Initialized");
    }

    @Override
    public void onEvent(Object event) throws Exception {
        if (event instanceof MavenExecutionRequest) {
            if (freezingEnabled()) {
                logger.info("[FreezePomSpy]: Received a " + MavenExecutionRequest.class.getSimpleName());
                MavenExecutionRequest mavenExecutionRequest = (MavenExecutionRequest)event;
                File pomFile = mavenExecutionRequest.getPom();
                if (!MavenConventions.FROZEN_POM_FILE.equals(pomFile.getName())) {
                    logger.info("[FreezeModelLocator]: Freeze pom " + pomFile.getAbsolutePath());
                    artifactFreezeMapping.put(FreezeExtension.getRevision(), pomFile);
                    logger.debug("[FreezeModelLocator]: Freezing pom " + pomFile.getAbsolutePath());
                    mavenExecutionRequest.setPom(pomFreezer.freeze(pomFile));
                }
            }
        }
    }

    @Override
    public void close() throws Exception {

    }
}
