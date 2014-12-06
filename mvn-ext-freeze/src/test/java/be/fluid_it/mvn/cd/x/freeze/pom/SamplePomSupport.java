package be.fluid_it.mvn.cd.x.freeze.pom;

import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid_it.mvn.cd.x.freeze.stamp.Stamp;
import be.fluid_it.mvn.cd.x.freeze.stamp.Stamper;

import java.util.Properties;

public abstract class SamplePomSupport {
    public static final String GROUP_ID = "be.fluid-it.tools.mvn.cd";
    public static final String ARTIFACT_ID = "sample-artifact-id";
    public static final String PARENT_ARTIFACT_ID = "parent-sample-artifact-id";
    public static final String VERSION_PREFIX = "0.1";
    public static final String VERSION = VERSION_PREFIX + "-SNAPSHOT";
    public static final String STAMP = "123-1";
    public static final String FROZEN_VERSION = VERSION_PREFIX + "-" + STAMP;
    public static final String LIBRARY_GROUP_ID = "be.fluid-it.tools.mvn.cd.library";
    public static final String LIBRARY_ARTIFACT_ID = "sample-library-artifact-id";
    public static final String LIBRARY_VERSION_PREFIX = "1.2";
    public static final String LIBRARY_VERSION = LIBRARY_VERSION_PREFIX + "-SNAPSHOT";
    public static final String LIBRARY_STAMP = "234-1";
    public static final String LIBRARY_FROZEN_VERSION = LIBRARY_VERSION_PREFIX + "-" + LIBRARY_STAMP;

    public static GroupIdArtifactIdVersion parentGroupIdArtifactIdVersion() {
        return new GroupIdArtifactIdVersion(GROUP_ID, PARENT_ARTIFACT_ID, VERSION);
    }

    public static GroupIdArtifactIdVersion groupIdArtifactIdVersion() {
        return new GroupIdArtifactIdVersion(GROUP_ID, ARTIFACT_ID, VERSION);
    }

    public static GroupIdArtifactIdVersion libraryGroupIdArtifactIdVersion() {
        return new GroupIdArtifactIdVersion(LIBRARY_GROUP_ID, LIBRARY_ARTIFACT_ID, LIBRARY_VERSION);
    }

    public static GroupIdArtifactIdVersion frozenParentGroupIdArtifactIdVersion() {
        return parentGroupIdArtifactIdVersion().addVersion(FROZEN_VERSION);
    }

    public static GroupIdArtifactIdVersion frozenGroupIdArtifactIdVersion() {
        return groupIdArtifactIdVersion().addVersion(FROZEN_VERSION);
    }

    public static GroupIdArtifactIdVersion frozenLibraryGroupIdArtifactIdVersion() {
        return libraryGroupIdArtifactIdVersion().addVersion(LIBRARY_FROZEN_VERSION);
    }

    public static Stamper fakedStamper() {
        return new Stamper() {
            @Override
            public String stamp(String snapshotVersion) {
                switch (snapshotVersion) {
                    case VERSION:
                        return FROZEN_VERSION;
                    case LIBRARY_VERSION:
                        return  LIBRARY_FROZEN_VERSION;
                }
                throw new IllegalStateException("Unexpected snapshotVersion " + snapshotVersion + " given to faked fakedStamper.");
            }

            @Override
            public String stamp(String snapshotVersion, Properties props) {
                return null;
            }

            @Override
            public Stamp createStamp() {
                return null;
            }

            @Override
            public Stamp createStamp(Properties properties) {
                return null;
            }

            @Override
            public String asString(Stamp stamp) {
                return null;
            }

            @Override
            public String stamp(String snapshotVersion, Stamp stamp) {
                return null;
            }

            @Override
            public Stamp extract(String frozenVersion) {
                return null;
            }

            @Override
            public boolean isEnabled(Properties properties) {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        };
    }
}
