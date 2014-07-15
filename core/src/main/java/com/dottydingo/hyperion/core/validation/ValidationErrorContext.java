package com.dottydingo.hyperion.core.validation;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ValidationErrorContext
{
    private List<ValidationErrorHolder> validationErrors = new ArrayList<ValidationErrorHolder>();

    public void addValidationError(String resourceCode, String fieldName, Object... parameters)
    {
        validationErrors.add(new ValidationErrorHolder(resourceCode, fieldName, parameters));
    }

    public List<ValidationErrorHolder> getValidationErrors()
    {
        return this.validationErrors;
    }


    public boolean hasErrors()
    {
        return validationErrors.size() > 0;
    }

    public boolean containsError(String resourceCode,String fieldName)
    {
        for (ValidationErrorHolder error : validationErrors)
        {
            if(error.getResourceCode().equals(resourceCode) && error.getFieldName().equals(fieldName))
                return true;
        }

        return false;
    }
}

