package oop.project.cli;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

/**
 * Represents an argument with configurable options.
 *
 * @param <T> The type of the argument.
 */
public class Argument<T> {
    // Required attributes
    private final String name;
    private final Class<T> type;

    // Optional attributes
    private final Boolean required;
    private final String helpMsg;
    private final ValidationFunction<T> validationFunction;
    private final String customTypeConversionMethod;

    /* CONSTRUCTORS */

    private Argument( Builder<T> builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.required = builder.required;
        this.helpMsg = builder.helpMsg;
        this.validationFunction = builder.validationFunction;
        this.customTypeConversionMethod = builder.customTypeConversionMethod;
    }

    /* GETTERS */

    /**
     * Retrieves the name of the argument.
     *
     * @return The name of the argument.
     */
    public String name() { return name; }

    /**
     * Retrieves the type of the argument.
     *
     * @return The type of the argument.
     */
    public Class<T> type() { return type; }

    /**
     * Retrieves whether the argument is required.
     *
     * @return {@code true} if the argument is required, {@code false} otherwise.
     */
    public Boolean required() { return required; }

    /* ARGUMENT BUILDER */

    /**
     * A builder class to construct instances of the {@link Argument} class with flexible configuration options.
     *
     * @param <T> The type of the argument.
     */
    protected static class Builder<T> {
        // Required attributes
        private final String name;
        private final Class<T> type;

        // Optional attributes
        private Boolean required = Boolean.FALSE;
        private String helpMsg = null;
        private ValidationFunction<T> validationFunction = null;
        private String customTypeConversionMethod = null;

        /**
         * Constructs a new builder with the specified name and type.
         *
         * @param name The name of the argument.
         * @param type The type of the argument.
         */
        public Builder(String name, Class<T> type) {
            this.name = name;
            this.type = type;
        }

        /**
         * Sets whether the argument is required.
         *
         * @param required {@code true} if the argument is required, {@code false} otherwise.
         * @return This builder instance for method chaining.
         */
        public Builder<T> required(Boolean required) {
            this.required = required;
            return this;
        }

        /**
         * Sets the help message associated with the argument.
         *
         * @param helpMsg The help message to set.
         * @return This builder instance for method chaining.
         */
        public Builder<T> helpMsg(String helpMsg) {
            this.helpMsg = helpMsg;
            return this;
        }

        /**
         * Sets the validation function for the argument.
         *
         * @param validationFunction The validation function to set.
         * @return This builder instance for method chaining.
         */
        public Builder<T> validationFunction(ValidationFunction<T> validationFunction) {
            this.validationFunction = validationFunction;
            return this;
        }

        /**
         * Sets the custom type conversion method for the argument.
         *
         * @param customTypeConversionMethod The custom type conversion method to set.
         * @return This builder instance for method chaining.
         */
        public Builder<T> customTypeConversionMethod(String customTypeConversionMethod) {
            this.customTypeConversionMethod = customTypeConversionMethod;
            return this;
        }

        /**
         * Builds and returns an instance of the {@link Argument} class with the configured options.
         *
         * @return An instance of the {@link Argument} class.
         */
        public Argument<T> build() {
            return new Argument<>(this);
        }
    }

    /* TYPE CONVERSION & VALIDATION */

    /**
     * Validates the input string and returns the parsed result.
     *
     * @param input The input string to validate and parse.
     * @return The parsed result of type T.
     * @throws ValidationCustomConversionException if custom type conversion fails.
     * @throws ValidationDefaultConversionException if default type conversion fails.
     * @throws ValidationFunctionException if the validation function returns false.
     * @throws NullPointerException if the parsed input is null.
     * @throws Exception if any other exception occurs during validation and parsing.
     */
    public T validate(String input) throws Exception {
        T parsedInput = null;
        if(customTypeConversionMethod != null) {
            try {
                parsedInput = customConversion(input, type, customTypeConversionMethod);
            } catch (Exception e) {
                throw new ValidationCustomConversionException(name, type.toString());
            }
        } else {
            try {
                parsedInput = defaultConversion(input, type);
            } catch (Exception e) {
                throw new ValidationDefaultConversionException(name, type.toString());
            }

        }

        if(validationFunction != null) {
            if(validationFunction.validate(parsedInput)) {
                return parsedInput;
            } else {
                throw new ValidationFunctionException(name);
            }
        }

        if(parsedInput != null) {
            return parsedInput;
        }
        throw new NullPointerException("Validation Exception: Result was null.");
    }

    /**
     * Performs default type conversion using a constructor that accepts a String parameter.
     *
     * @param value The value to convert.
     * @param type The target type.
     * @return The converted value of type T.
     * @throws Exception if any exception occurs during conversion.
     */
    private static <T> T defaultConversion(String value, Class<T> type) throws Exception {
        Constructor<T> constructor = type.getConstructor(String.class);
        return constructor.newInstance(value);
    }

    /**
     * Performs custom type conversion using a static method with the specified name.
     *
     * @param value The value to convert.
     * @param type The target type.
     * @param methodName The name of the static conversion method.
     * @return The converted value of type T.
     * @throws Exception if any exception occurs during conversion.
     */
    private static <T> T customConversion(String value, Class<T> type, String methodName) throws Exception {
        Method method = type.getMethod(methodName, CharSequence.class);
        return type.cast(method.invoke(null, value));
    }

    /* HELP MESSAGE */

    /**
     * Retrieves a message containing information about the argument.
     * <p>
     * This method constructs a message containing details such as the argument name, type, whether it is required,
     * and the help message associated with the argument (if available). The message is formatted for display.
     *
     * @return A formatted message containing information about the argument, including the help message.
     */
    public String getMessage() {
        StringBuilder msg = new StringBuilder();
        msg.append("\t").append(name);
        msg.append("\tType: ").append(type.getSimpleName());
        if (required) {
            msg.append(", Required: True");
        } else {
            msg.append(", Required: False");
        }

        if (helpMsg != null && !helpMsg.isEmpty()) {
            msg.append(", MSG: ").append(helpMsg);
        }
        return msg.toString();
    }
}
