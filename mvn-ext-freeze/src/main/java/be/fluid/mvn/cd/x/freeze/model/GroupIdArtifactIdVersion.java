package be.fluid.mvn.cd.x.freeze.model;

public class GroupIdArtifactIdVersion {
    private final String groupId;
    private final String artifactId;
    private final String version;


    public GroupIdArtifactIdVersion(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public GroupIdArtifactIdVersion() {
        this(null, null, null);
    }

    public String groupId() {
        return this.groupId;
    }

    public String artifactId() {
        return this.artifactId;
    }

    public String version() {
        return this.version;
    }

    public GroupIdArtifactIdVersion addGroupId(String groupId) {
        return new GroupIdArtifactIdVersion(groupId, this.artifactId, this.version);
    }

    public GroupIdArtifactIdVersion addArtifactId(String artifactId) {
        return new GroupIdArtifactIdVersion(this.groupId, artifactId, this.version);
    }

    public GroupIdArtifactIdVersion addVersion(String version) {
        return new GroupIdArtifactIdVersion(this.groupId, this.artifactId, version);
    }

    public GroupIdArtifactIdVersionPrefix stripSnapshotPostfix() {
        return new GroupIdArtifactIdVersionPrefix(this.groupId, this.artifactId, this.version != null ? this.version.split("-SNAPSHOT" )[0] : null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupIdArtifactIdVersion that = (GroupIdArtifactIdVersion) o;

        if (artifactId != null ? !artifactId.equals(that.artifactId) : that.artifactId != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupId != null ? groupId.hashCode() : 0;
        result = 31 * result + (artifactId != null ? artifactId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + version;
    }
}
