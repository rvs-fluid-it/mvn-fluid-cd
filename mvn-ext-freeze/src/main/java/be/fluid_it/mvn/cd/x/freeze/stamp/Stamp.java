package be.fluid_it.mvn.cd.x.freeze.stamp;

public interface Stamp<S> extends Comparable<S> {
    String value(String key);
}
