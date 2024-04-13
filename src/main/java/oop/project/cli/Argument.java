package oop.project.cli;

public class Argument<T>
{
    //Required
    String name;
    Class<?> type;

    //Optional
    Boolean required;
    String helpMsg;
    ValidationFunction<T> validationFunction;

    private Argument(){}
    public Argument(String argName, Class<?> argType)
    {
        this.name = argName;
        this.type = argType;
        this.required = Boolean.TRUE;
    }

//    public Boolean validate()
//    {
//
//    }

    public void printHelp()
    {
        System.out.println(helpMsg);
    }
    public void setRequired(Boolean required)
    {
        this.required = required;
    }
    public void setHelpMsg(String helpMsg)
    {
        this.helpMsg = helpMsg;
    }
    public void setValidationFunc(ValidationFunction<T> validationFunction)
    {
        this.validationFunction = validationFunction;
    }
}
