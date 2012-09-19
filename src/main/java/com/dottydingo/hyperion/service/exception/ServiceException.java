package com.dottydingo.hyperion.service.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 */
public class ServiceException extends WebApplicationException
{

    public ServiceException(String message)
    {
        this(500,message);
    }

    public ServiceException(int status, String message)
    {
        super(Response.status(status).entity(message).build());
    }

    public ServiceException(int status, String message, Throwable throwable)
    {
        super(throwable,Response.status(status).entity(message).build());
    }
}
