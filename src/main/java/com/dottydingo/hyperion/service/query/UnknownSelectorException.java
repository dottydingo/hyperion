package com.dottydingo.hyperion.service.query;

/**
 * User: mark
 * Date: 9/16/12
 * Time: 4:19 PM
 */
public class UnknownSelectorException extends Exception
{
    private final String selector;


    /**
     * Construct an <tt>UnknownSelectorException</tt> with specified selector.
     *
     * @param selector
     */
    public UnknownSelectorException(String selector) {
        super("Cannot find property for selector: " + selector);
        this.selector = selector;
    }


    public String getSelector() {
        return selector;
    }
}
