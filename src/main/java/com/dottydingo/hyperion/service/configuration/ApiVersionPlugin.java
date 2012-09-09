package com.dottydingo.hyperion.service.configuration;

import com.dottydingo.hyperion.api.BaseApiObject;
import com.dottydingo.hyperion.service.model.BasePersistentObject;
import com.dottydingo.hyperion.service.translation.Translator;
import com.dottydingo.hyperion.service.validation.Validator;

/**
 */
public class ApiVersionPlugin<C extends BaseApiObject,P extends BasePersistentObject>
{
    private Integer version;
    private Translator<C,P> translator;
    private Validator<C,P> validator;

    public ApiVersionPlugin()
    {
    }

    public ApiVersionPlugin(Integer version, Translator<C,P> translator, Validator<C,P> validator)
    {
        this.version = version;
        this.translator = translator;
        this.validator = validator;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public Translator<C,P> getTranslator()
    {
        return translator;
    }

    public void setTranslator(Translator<C,P> translator)
    {
        this.translator = translator;
    }

    public Validator<C,P> getValidator()
    {
        return validator;
    }

    public void setValidator(Validator<C,P> validator)
    {
        this.validator = validator;
    }
}
