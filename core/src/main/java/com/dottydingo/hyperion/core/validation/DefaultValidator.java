package com.dottydingo.hyperion.core.validation;

import com.dottydingo.hyperion.api.ErrorDetail;
import com.dottydingo.hyperion.api.exception.ConflictException;
import com.dottydingo.hyperion.api.exception.HyperionException;
import com.dottydingo.hyperion.api.exception.ValidationException;
import com.dottydingo.hyperion.core.persistence.PersistenceContext;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class DefaultValidator<C,P> implements Validator<C, P>
{
    public static final String REQUIRED_FIELD = "validation_required_field";
    public static final String FIELD_LENGTH = "validation_field_length";
    public static final String CHANGE_NOT_ALLOWED = "validation_change_not_allowed";
    public static final String CONFLICT = "validation_conflict";
    public static final String VALIDATION_ERROR = "validation_error";

    private MessageSource messageSource;

    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    @Override
    public void validateCreate(C clientObject, PersistenceContext persistenceContext)
    {
        ValidationErrorContext errorContext = new ValidationErrorContext();

        validateCreateConflict(clientObject,errorContext,persistenceContext);
        if(errorContext.hasErrors())
            throw new ConflictException(buildErrorMessage(persistenceContext,CONFLICT,persistenceContext.getHttpMethod(),persistenceContext.getEntity()),
                    buildErrorDetails(errorContext,persistenceContext));

        validateCreate(clientObject,errorContext,persistenceContext);
        if(errorContext.hasErrors())
            throw new ValidationException(buildValidationErrorMessage(errorContext, persistenceContext),
                    buildErrorDetails(errorContext,persistenceContext));
    }

    @Override
    public void validateUpdate(C clientObject, P persistentObject, PersistenceContext persistenceContext)
    {
        ValidationErrorContext errorContext = new ValidationErrorContext();

        validateUpdateConflict(clientObject,persistentObject,errorContext,persistenceContext);
        if(errorContext.hasErrors())
            throw new ConflictException(buildErrorMessage(persistenceContext,CONFLICT,persistenceContext.getHttpMethod(),persistenceContext.getEntity()),
                    buildErrorDetails(errorContext,persistenceContext));

        validateUpdate(clientObject,persistentObject,errorContext,persistenceContext);
        if(errorContext.hasErrors())
            throw new ValidationException(buildValidationErrorMessage(errorContext, persistenceContext),
                    buildErrorDetails(errorContext,persistenceContext));
    }

    @Override
    public void validateDelete(P persistentObject, PersistenceContext persistenceContext)
    {
        ValidationErrorContext errorContext = new ValidationErrorContext();

        validateDeleteConflict(persistentObject,errorContext,persistenceContext);
        if(errorContext.hasErrors())
            throw new ConflictException(buildErrorMessage(persistenceContext,CONFLICT,persistenceContext.getHttpMethod(),persistenceContext.getEntity()),
                    buildErrorDetails(errorContext,persistenceContext));

        validateDelete(persistentObject, errorContext,persistenceContext);
        if(errorContext.hasErrors())
            throw new ValidationException(buildValidationErrorMessage(errorContext, persistenceContext),
                    buildErrorDetails(errorContext,persistenceContext));
    }

    protected List<ErrorDetail> buildErrorDetails(ValidationErrorContext errorContext,PersistenceContext persistenceContext)
    {
        List<ErrorDetail> errorDetails = new ArrayList<ErrorDetail>();
        for (ValidationErrorHolder holder : errorContext.getValidationErrors())
        {
            ErrorDetail errorDetail = new ErrorDetail();
            errorDetail.setCode(holder.getResourceCode());
            errorDetail.setMessage(messageSource.getMessage(holder.getResourceCode(), holder.getParameters(), persistenceContext.getLocale()));
            errorDetails.add(errorDetail);
        }

        return errorDetails;
    }

    protected String buildErrorMessage(PersistenceContext persistenceContext,String key,Object... parameters)
    {
        return messageSource.getMessage(key,parameters,persistenceContext.getLocale());
    }

    @Deprecated
    protected String buildValidationErrorMessage(ValidationErrorContext errorContext, PersistenceContext persistenceContext)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(messageSource.getMessage(VALIDATION_ERROR,null,persistenceContext.getLocale()));
        for (ValidationErrorHolder holder : errorContext.getValidationErrors())
        {
            sb.append("\n");
            sb.append(messageSource.getMessage(holder.getResourceCode(), holder.getParameters(), persistenceContext.getLocale()));
        }
        return sb.toString();
    }

    protected void validateCreateConflict(C clientObject, ValidationErrorContext errorContext,PersistenceContext persistenceContext){}

    protected void validateUpdateConflict(C clientObject, P persistentObject,ValidationErrorContext errorContext,PersistenceContext persistenceContext){}

    protected void validateDeleteConflict(P persistentObject,ValidationErrorContext errorContext,PersistenceContext persistenceContext){}


    protected void validateRequired(ValidationErrorContext errorContext, String fieldName, Object value)
    {
        if(value == null)
            errorContext.addValidationError(REQUIRED_FIELD, fieldName, fieldName);
    }

    protected void validateNotBlank(ValidationErrorContext errorContext,String fieldName, String value)
    {
        if(value != null && value.length() == 0)
            errorContext.addValidationError(REQUIRED_FIELD, fieldName, fieldName, value);
    }

    protected void validateNotChanged(ValidationErrorContext errorContext,String fieldName, Object clientValue,Object persistentValue)
    {
        if(clientValue != null && !clientValue.equals(persistentValue))
            errorContext.addValidationError(CHANGE_NOT_ALLOWED, fieldName, fieldName);

    }

    protected void validateLength(ValidationErrorContext errorContext,String fieldName, String value, int maxLength)
    {
        if(value != null && value.length() > maxLength)
            errorContext.addValidationError(FIELD_LENGTH, fieldName, fieldName, maxLength);
    }

    protected void validateRevisionMatch(String type, Integer clientRevision,Integer persistentRevision)
    {
        if(clientRevision != null && !clientRevision.equals(persistentRevision))
            throw new HyperionException(409,
                    String.format("The value for %s is stale. The supplied revision %s does not match the existing revision %s.",
                            type,clientRevision,persistentRevision));
    }

    protected boolean valueChanged(Object clientValue,Object persistentValue)
    {
        return clientValue != null && !clientValue.equals(persistentValue);
    }


    protected void validateCreate(C clientObject,ValidationErrorContext errorContext,PersistenceContext persistenceContext)
    {

    }

    protected void validateUpdate(C clientObject, P persistentObject,ValidationErrorContext errorContext,PersistenceContext persistenceContext)
    {

    }

    protected void validateDelete(P persistentObject,ValidationErrorContext errorContext,PersistenceContext persistenceContext)
    {

    }
}