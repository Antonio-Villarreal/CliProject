package oop.project.cli;

public class Argument
{
    //Required
    String name;
    Class<T> argType;

    //Optional
    Boolean required;
    String helpMsg;
    ValidatorFunction<T, R> validatorFunction;

    private Argument(){}
    public Argument(String argName, Class<T> argType)
    {
        name = argName;
        this.argType = argType;

        required = true;
    }

    public Boolean validate()
    {

    }

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
    public void setValidator(ValidatorFunction<T> validatorFunction)
    {
        this.validatorFunction = validatorFunction;
    }
}
