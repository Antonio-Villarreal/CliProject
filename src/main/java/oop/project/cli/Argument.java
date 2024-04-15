package oop.project.cli;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.Optional;

public class Argument<T>
{
    //Required
    String name;
    Class<T> type;

    //Optional
    Boolean required;
    String helpMsg;
    ValidationFunction<Object> validationFunction;
    String customTypeConversionMethod;

    private Argument(){}
    
    public Argument(String argName, Class<T> argType)
    {
        this.name = argName;
        this.type = argType;
        this.required = Boolean.TRUE;
        this.helpMsg = "";
        this.validationFunction = null;
        this.customTypeConversionMethod = null;
    }

    private static <T> T defaultConversion(String value, Class<T> type) throws Exception {
        try {
            Constructor<T> constructor = type.getConstructor(String.class);
            return constructor.newInstance(value);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static <T> T customConversion(String value, Class<T> type, String methodName) throws Exception {
        try {
            Method method = type.getMethod(methodName, CharSequence.class);
            return type.cast(method.invoke(null, value));
        } catch (NoSuchMethodException e) {
            throw new Exception(e);
        }
    }

    public T validate(String input) throws Exception {
        T parsedInput = null;
        if(customTypeConversionMethod != null) {
            parsedInput = customConversion(input, type, customTypeConversionMethod);
        } else {
            parsedInput = defaultConversion(input, type);
        }

        if(validationFunction != null) {
            if(validationFunction.validate(parsedInput)) {
                return parsedInput;
            } else {
                throw new Exception();
            }
        }
        return parsedInput;
    }

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
    
    public void setRequired(Boolean required){
        this.required = required;
    }
    
    public void setHelpMsg(String helpMsg){
        this.helpMsg = helpMsg;
    }
    
    public void setValidationFunc(ValidationFunction<Object> validationFunction){
        this.validationFunction = validationFunction;
    }

    public void setCustomTypeConversionMethod(String customTypeConversionMethod){
        this.customTypeConversionMethod = customTypeConversionMethod;
    }
}
