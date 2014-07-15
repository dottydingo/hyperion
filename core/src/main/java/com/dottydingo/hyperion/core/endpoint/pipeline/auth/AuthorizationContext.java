package com.dottydingo.hyperion.core.endpoint.pipeline.auth;

/**
 */
public interface AuthorizationContext
{
    boolean isAuthorized();
    boolean isReadable(String propertyName);
    boolean isWritable(String propertyName);
}
