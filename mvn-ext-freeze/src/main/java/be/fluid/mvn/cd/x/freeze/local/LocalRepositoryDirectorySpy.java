package be.fluid.mvn.cd.x.freeze.local;

import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.settings.building.SettingsBuildingResult;
import org.codehaus.plexus.component.annotations.Component;

import java.io.File;

@Component( role = EventSpy.class, hint = LocalRepositoryDirectorySpy.HINT)
public class LocalRepositoryDirectorySpy implements EventSpy {
    public static final String HINT = "localRepositoryDirectorySpy";

    private File localRepositoryDirectory;

    @Override
    public void init(Context context) throws Exception {

    }

    @Override
    public void onEvent(Object event) throws Exception {
        if (event instanceof SettingsBuildingResult) {
            String localRepositoryPath = ((SettingsBuildingResult) event).getEffectiveSettings().getLocalRepository();
            if (localRepositoryPath == null) {
                localRepositoryPath = "~/.m2/repository";
            }
            File localRepositoryFile = new File(localRepositoryPath);
            if (localRepositoryFile.exists() && localRepositoryFile.isDirectory()) {
                this.localRepositoryDirectory = localRepositoryFile;
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
