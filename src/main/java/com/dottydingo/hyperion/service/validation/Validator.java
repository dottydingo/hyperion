package com.dottydingo.hyperion.service.validation;

/**
 */
public interface Validator<C,P>
{
    void validateCreate(C clientObject);

    void validateUpdate(C clientObject,P persistentObject);
}
