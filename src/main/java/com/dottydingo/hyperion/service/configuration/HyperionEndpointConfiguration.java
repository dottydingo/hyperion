package com.dottydingo.hyperion.service.configuration;

import com.dottydingo.service.endpoint.configuration.EndpointConfiguration;

/**
 */
public class HyperionEndpointConfiguration extends EndpointConfiguration
{
    protected String versionParameterName = "version";
    protected String versionHeaderName = "DottyDingo-Hyperion-Version";
    protected boolean requireVersion = false;
    protected String allowedOrigins = "*";
    protected int accessControlMaxAge = 0;

    public String getVersionParameterName()
    {
        return versionParameterName;
    }

    public void setVersionParameterName(String versionParameterName)
    {
        this.versionParameterName = versionParameterName;
    }

    public String getVersionHeaderName()
    {
        return versionHeaderName;
    }

    public void setVersionHeaderName(String versionHeaderName)
    {
        this.versionHeaderName = versionHeaderName;
    }

    public boolean isRequireVersion()
    {
        return requireVersion;
    }

    public void setRequireVersion(boolean requireVersion)
    {
        this.requireVersion = requireVersion;
    }

    public String getAllowedOrigins()
    {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String allowedOrigins)
    {
        this.allowedOrigins = allowedOrigins;
    }

    public int getAccessControlMaxAge()
    {
        return accessControlMaxAge;
    }

    public void setAccessControlMaxAge(int accessControlMaxAge)
    {
        this.accessControlMaxAge = accessControlMaxAge;
    }
}
