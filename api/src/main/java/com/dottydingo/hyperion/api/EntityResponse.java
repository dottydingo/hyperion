package com.dottydingo.hyperion.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * A paged entity list response
 */
@JsonPropertyOrder({"page","entries"})
public class EntityResponse<T extends ApiObject> extends EntityList<T>
{
    private Page page;

    /**
     * Return the page information
     * @return the page information
     */
    public Page getPage()
    {
        return page;
    }

    /**
     * Set the page information
     * @param page The page information
     */
    public void setPage(Page page)
    {
        this.page = page;
    }
}
