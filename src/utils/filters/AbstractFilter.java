package utils.filters;

@FunctionalInterface
public interface AbstractFilter<T> {
    boolean accept(T t);
}
