package be.fluid_it.mvn.cd.x.freeze.stamp;

import static be.fluid_it.mvn.cd.x.freeze.stamp.RevisionBuildNumberStamper.*;

public class RevisionBuildNumberStamp implements Stamp<RevisionBuildNumberStamp> {
    private final String revision;
    private final String buildNumber;

    public RevisionBuildNumberStamp(String revision, String buildNumber) {
        this.revision = revision;
        this.buildNumber = buildNumber;
    }

    @Override
    public int compareTo(RevisionBuildNumberStamp revisionBuildNumberStamp) {
        int result = safeCompareTo(this.revision, revisionBuildNumberStamp.revision);
        if (result == 0) {
            result = safeCompareTo(this.buildNumber, revisionBuildNumberStamp.buildNumber);
        }
        return result;
    }

    private int safeCompareTo(String value, String compareToValue) {
        if (value == null ) {
            if (compareToValue == null) return 0;
            return -1;
        }
        if (compareToValue == null) {
            return 1;
        }
        try {
            Integer iValue = Integer.parseInt(value);
            Integer iCompareToValue = Integer.parseInt(compareToValue);
            return iValue.compareTo(iCompareToValue);
        } catch (NumberFormatException nfe) {
            // String compare
            return value.compareTo(compareToValue);
        }
    }

    public String revision() {
        return revision;
    }

    public String buildNumber() {
        return buildNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RevisionBuildNumberStamp that = (RevisionBuildNumberStamp) o;

        if (buildNumber != null ? !buildNumber.equals(that.buildNumber) : that.buildNumber != null) return false;
        if (revision != null ? !revision.equals(that.revision) : that.revision != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = revision != null ? revision.hashCode() : 0;
        result = 31 * result + (buildNumber != null ? buildNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String value(String key) {
        switch (key) {
            case REVISION:
                return revision();
            case BUILD_NUMBER:
                return buildNumber();
        }
        return null;
    }

    @Override
    public String toString() {
        return "RevisionBuildNumberStamp{" +
                "revision='" + revision + '\'' +
                ", buildNumber='" + buildNumber + '\'' +
                '}';
    }
}
