package be.fluid_it.mvn.cd.x.freeze.stamp.revision;

import be.fluid_it.mvn.cd.x.freeze.stamp.Stamp;

public class RevisionStamp implements Stamp<RevisionStamp> {
    private final String revision;

    public RevisionStamp(String revision) {
        this.revision = revision;
    }

    @Override
    public int compareTo(RevisionStamp revisionStamp) {
        return safeCompareTo(this.revision, revisionStamp.revision);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RevisionStamp that = (RevisionStamp) o;

        if (revision != null ? !revision.equals(that.revision) : that.revision != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return revision != null ? revision.hashCode() : 0;
    }

    @Override
    public String value(String key) {
        switch (key) {
            case RevisionStamper.REVISION:
                return revision();
        }
        return null;
    }

    @Override
    public String toString() {
        return "RevisionStamp{" +
                "revision='" + revision + '\'' +
                '}';
    }
}
