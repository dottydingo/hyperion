package com.dottydingo.hyperion.core.message;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 */
public class HyperionMessageSource
{
    private String defaultEncoding = "UTF-8";
    private String[] baseErrorResourceBundles;
    private String[] additionalErrorResourceBundles;

    private String[] baseValidationResourceBundles;
    private String[] additionalValidationResourceBundles;

    private ResourceBundleMessageSource errorMessageSource;
    private ResourceBundleMessageSource validationMessageSource;

    public void setDefaultEncoding(String defaultEncoding)
    {
        this.defaultEncoding = defaultEncoding;
    }

    public void setBaseErrorResourceBundles(String[] baseErrorResourceBundles)
    {
        this.baseErrorResourceBundles = baseErrorResourceBundles;
    }

    public void setAdditionalErrorResourceBundles(String[] additionalErrorResourceBundles)
    {
        this.additionalErrorResourceBundles = additionalErrorResourceBundles;
    }

    public void setBaseValidationResourceBundles(String[] baseValidationResourceBundles)
    {
        this.baseValidationResourceBundles = baseValidationResourceBundles;
    }

    public void setAdditionalValidationResourceBundles(String[] additionalValidationResourceBundles)
    {
        this.additionalValidationResourceBundles = additionalValidationResourceBundles;
    }

    public void init()
    {
        errorMessageSource = new ResourceBundleMessageSource();
        errorMessageSource.setBasenames(combine(baseErrorResourceBundles, additionalErrorResourceBundles));
        errorMessageSource.setDefaultEncoding(defaultEncoding);

        validationMessageSource = new ResourceBundleMessageSource();
        validationMessageSource.setBasenames(combine(baseValidationResourceBundles,additionalValidationResourceBundles));
        validationMessageSource.setDefaultEncoding(defaultEncoding);
    }

    public String getErrorMessage(String code,Object[] args, Locale locale)
    {
        return errorMessageSource.getMessage(code, args, locale);
    }

    public String getValidationMessage(String code,Object[] args, Locale locale)
    {
        return validationMessageSource.getMessage(code, args, locale);
    }

    protected String[] combine(String[] base, String[] additional)
    {
        List<String> results = new ArrayList<>();
        if(base != null && base.length > 0)
            results.addAll(Arrays.asList(base));

        if(additional != null && additional.length > 0)
            results.addAll(Arrays.asList(additional));

        return results.toArray(new String[results.size()]);
    }
}
