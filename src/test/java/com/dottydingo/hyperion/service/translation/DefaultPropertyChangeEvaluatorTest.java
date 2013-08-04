package com.dottydingo.hyperion.service.translation;

import org.junit.Assert;
import org.junit.Test;

/**
 */
public class DefaultPropertyChangeEvaluatorTest
{
    private DefaultPropertyChangeEvaluator propertyChangeEvaluator = new DefaultPropertyChangeEvaluator();

    @Test
    public void testHasChanged() throws Exception
    {
        Assert.assertFalse(propertyChangeEvaluator.hasChanged(null,null));
        Assert.assertFalse(propertyChangeEvaluator.hasChanged("",""));
        Assert.assertFalse(propertyChangeEvaluator.hasChanged(1,1));

        Assert.assertTrue(propertyChangeEvaluator.hasChanged(null,"bar"));
        Assert.assertTrue(propertyChangeEvaluator.hasChanged("foo","bar"));
        Assert.assertTrue(propertyChangeEvaluator.hasChanged(1,2));

    }
}
