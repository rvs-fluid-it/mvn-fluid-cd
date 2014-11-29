package be.fluid_it.mvn.cd.x.freeze.local;

import be.fluid_it.mvn.cd.x.freeze.FreezeException;
import be.fluid_it.mvn.cd.x.freeze.FreezeExtension;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.repository.RepositorySystem;
import org.apache.maven.settings.building.SettingsBuildingResult;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.File;

@Component( role = EventSpy.class, hint = LocalRepositoryDirectorySpy.HINT)
public class LocalRepositoryDirectorySpy implements EventSpy {
    public static final String HINT = "localRepositoryDirectorySpy";

    private File localRepositoryDirectory;

    @Override
    public void init(Context context) throws Exception {
    }

    @Requirement
    private Logger logger;

    @Override
    public void onEvent(Object event) throws Exception {
        if (event instanceof SettingsBuildingResult) {
            logger.debug("[LocalRepositoryDirectorySpy]: Received a " + SettingsBuildingResult.class.getSimpleName());
            String localRepositoryPath = ((SettingsBuildingResult) event).getEffectiveSettings().getLocalRepository();
            if (localRepositoryPath == null) {
                logger.debug("[LocalRepositoryDirectorySpy]: No local repository folder configured in effective settings");
                localRepositoryPath = RepositorySystem.defaultUserLocalRepository.getAbsolutePath();
            }
            File localRepositoryFile = new File(localRepositoryPath);
            if (localRepositoryFile.exists() && localRepositoryFile.isDirectory()) {
                this.localRepositoryDirectory = localRepositoryFile;
            } else {
                if (FreezeExtension.freezingEnabled()) {
                    throw new FreezeException("[LocalRepositoryDirectorySpy]: Invalid local repository folder " +
                            localRepositoryPath +
                            "in effective settings");
                }
            }
        }
    }

    @Override
    public void close() throws Exception {

    }

    public File localRepositoryDirectory() {
        return localRepositoryDirectory;
    }
}
