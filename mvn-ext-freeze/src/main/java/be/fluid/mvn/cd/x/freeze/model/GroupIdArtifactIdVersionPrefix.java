package be.fluid.mvn.cd.x.freeze.model;

public class GroupIdArtifactIdVersionPrefix {
    public static final String REVISION_SEPARATOR = "-";

    private final String groupId;
    private final String artifactId;
    private final String versionPrefix;

    public GroupIdArtifactIdVersionPrefix(String groupId, String artifactId, String versionPrefix) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.versionPrefix = versionPrefix;
    }

    public GroupIdArtifactIdVersionPrefix() {
        this(null, null, null);
    }

    public String groupId() {
        return this.groupId;
    }

    public String artifactId() {
        return this.artifactId;
    }

    public String versionPrefix() {
        return this.versionPrefix;
    }

    public GroupIdArtifactIdVersionPrefix addGroupId(String groupId) {
        return new GroupIdArtifactIdVersionPrefix(groupId, this.artifactId, this.versionPrefix);
    }

    public GroupIdArtifactIdVersionPrefix addArtifactId(String artifactId) {
        return new GroupIdArtifactIdVersionPrefix(this.groupId, artifactId, this.versionPrefix);
    }

    public GroupIdArtifactIdVersionPrefix addVersionPrefix(String versionPrefix) {
        return new GroupIdArtifactIdVersionPrefix(this.groupId, this.artifactId, versionPrefix);
    }

    public GroupIdArtifactIdVersion addRevision(String revision) {
        return new GroupIdArtifactIdVersion(this.groupId, this.artifactId, this.versionPrefix + REVISION_SEPARATOR + revision);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupIdArtifactIdVersionPrefix that = (GroupIdArtifactIdVersionPrefix) o;

        if (artifactId != null ? !artifactId.equals(that.artifactId) : that.artifactId != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (versionPrefix != null ? !versionPrefix.equals(that.versionPrefix) : that.versionPrefix != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupId != null ? groupId.hashCode() : 0;
        result = 31 * result + (artifactId != null ? artifactId.hashCode() : 0);
        result = 31 * result + (versionPrefix != null ? versionPrefix.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + versionPrefix;
    }

}
