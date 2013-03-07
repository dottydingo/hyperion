package com.dottydingo.hyperion.service.persistence.sort;

import com.dottydingo.hyperion.service.configuration.EntityPlugin;

/**
 * User: mark
 * Date: 2/24/13
 * Time: 10:11 AM
 */
public interface OrderBuilderFactory<P>
{
    OrderBuilder<P> createOrderBuilder(String sortString, EntityPlugin entityPlugin);
}
