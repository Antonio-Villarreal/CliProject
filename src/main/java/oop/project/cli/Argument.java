package oop.project.cli;

import java.lang.reflect.Constructor;

public class Argument<T>
{
    //Required
    String name;
    Class<T> type;

    //Optional
    Boolean required;
    String helpMsg;
    ValidationFunction<Object> validationFunction;

    private Argument(){}
    
    public Argument(String argName, Class<T> argType)
    {
        this.name = argName;
        this.type = argType;
        this.required = Boolean.TRUE;
        this.helpMsg = "";
        this.validationFunction = null;
    }

    public static <T> T convertStringToType(String value, Class<T> type) throws Exception {
        Constructor<T> constructor = type.getConstructor(String.class);
        return constructor.newInstance(value);
    }

    public T validate(String input) throws Exception {
        T parsedInput = convertStringToType(input, type);

        if (validationFunction == null)
        {
            return parsedInput;
        }
        else if (validationFunction.validate(parsedInput))
        {
            return parsedInput;
        }
        else
        {
            throw new Exception("Validation failed");
        }
    }

    public void printHelp() {
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
        System.out.println(msg.toString());
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
}
