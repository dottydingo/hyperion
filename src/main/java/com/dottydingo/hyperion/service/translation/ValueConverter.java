package com.dottydingo.hyperion.service.translation;

/**
 */
public interface ValueConverter<C,P>
{
    public C convertToClientValue(P persistentValue, TranslationContext context);

    public P convertToPersistentValue(C clientValue, TranslationContext context);
}
