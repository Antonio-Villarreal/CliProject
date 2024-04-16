package oop.project.cli;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

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

    private Argument(ArgumentBuilder<T> builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.required = builder.required;
        this.helpMsg = builder.helpMsg;
        this.validationFunction = builder.validationFunction;
        this.customTypeConversionMethod = builder.customTypeConversionMethod;
    }

    /* GETTERS */

    public String name() { return name; }
    public Class<T> type() { return type; }
    public Boolean required() { return required; }
    public String helpMsg() { return helpMsg; }

    /* ARGUMENT BUILDER */

    public static class ArgumentBuilder<T> {
        // Required attributes
        private final String name;
        private final Class<T> type;

        // Optional attributes
        private Boolean required = Boolean.FALSE;
        private String helpMsg = null;
        private ValidationFunction<T> validationFunction = null;
        private String customTypeConversionMethod = null;

        public ArgumentBuilder(String name, Class<T> type) {
            this.name = name;
            this.type = type;
        }

        public ArgumentBuilder<T> required(Boolean required) {
            this.required = required;
            return this;
        }

        public ArgumentBuilder<T> helpMsg(String helpMsg) {
            this.helpMsg = helpMsg;
            return this;
        }

        public ArgumentBuilder<T> validationFunction(ValidationFunction<T> validationFunction) {
            this.validationFunction = validationFunction;
            return this;
        }

        public ArgumentBuilder<T> customTypeConversionMethod(String customTypeConversionMethod) {
            this.customTypeConversionMethod = customTypeConversionMethod;
            return this;
        }

        public Argument<T> build() {
            return new Argument<>(this);
        }
    }

    /* TYPE CONVERSION & VALIDATION */

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

    private static <T> T defaultConversion(String value, Class<T> type) throws Exception {
        Constructor<T> constructor = type.getConstructor(String.class);
        return constructor.newInstance(value);
    }

    private static <T> T customConversion(String value, Class<T> type, String methodName) throws Exception {
        Method method = type.getMethod(methodName, CharSequence.class);
        return type.cast(method.invoke(null, value));
    }

    /* HELP MESSAGE */

    public String printHelp() {
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
