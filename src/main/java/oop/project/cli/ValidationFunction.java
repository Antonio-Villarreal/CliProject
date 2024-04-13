package oop.project.cli;

public interface ValidationFunction<T> {
    boolean validate(T value);
}
