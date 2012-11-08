package com.dottydingo.hyperion.service.validation;

/**
 */
public class DefaultValidator<C,P> implements Validator<C, P>
{
    @Override
    public void validateCreate(C clientObject)
    {

    }

    @Override
    public void validateUpdate(C clientObject, P persistentObject)
    {

    }

    @Override
    public void validateDelete(P persistentObject)
    {

    }
}
