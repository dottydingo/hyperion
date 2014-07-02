package com.dottydingo.hyperion.core.endpoint.status;

import com.dottydingo.service.endpoint.status.EndpointStatus;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 */
public class ServiceStatus extends EndpointStatus
{
    private AtomicBoolean forceDown = new AtomicBoolean(false);
    private AtomicBoolean readOnly = new AtomicBoolean(false);

    public boolean getForceDown()
    {
        return forceDown.get();
    }

    public void setForceDown(boolean forceDown)
    {
        this.forceDown.set(forceDown);
    }

    public boolean getReadOnly()
    {
        return readOnly.get();
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly.set(readOnly);
    }
}
