package com.dottydingo.hyperion.service.pipeline.auth;

/**
 */
public interface AuthorizationContext
{
    boolean isAuthorized();
    boolean isReadable(String propertyName);
    boolean isWritable(String propertyName);
}
