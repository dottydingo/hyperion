package com.dottydingo.hyperion.service.query;

/**
 * User: mark
 * Date: 9/16/12
 * Time: 4:18 PM
 */
public class ArgumentFormatException extends Exception
{
    private final String selector;
    private final String argument;
    private final Class<?> propertyType;


    /**
     * Construct an <tt>ArgumentFormatException</tt> with specified argument
     * and property type.
     *
     * @param argument
     * @param propertyType
     */
    public ArgumentFormatException(String argument, Class<?> propertyType) {
        super("Cannot cast '" + argument + "' to type " + propertyType);
        this.selector = null;
        this.argument = argument;
        this.propertyType = propertyType;
    }
    /**
     * Construct an <tt>ArgumentFormatException</tt> with specified selector,
     * argument and property type.
     *
     * @param selector
     * @param argument
     * @param propertyType
     */
    public ArgumentFormatException(String selector, String argument, Class<?> propertyType) {
        super("Argument '" + argument + "' of " + selector + " must be of type " + propertyType.getSimpleName());
        this.selector = selector;
        this.argument = argument;
        this.propertyType = propertyType;
    }


    public String getArgument() {
        return argument;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public String getSelector() {
        return selector;
    }

}
