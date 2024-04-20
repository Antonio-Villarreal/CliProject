package oop.project.cli;

/**
 * This class represents the user defined function for validating input.
 * @param <T> The type of the argument.
 */
public interface ValidationFunction<T> {
    boolean validate(T value);
}
