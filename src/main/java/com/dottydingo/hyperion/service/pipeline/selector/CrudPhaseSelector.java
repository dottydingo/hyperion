package com.dottydingo.hyperion.service.pipeline.selector;

import com.dottydingo.hyperion.exception.HyperionException;
import com.dottydingo.hyperion.service.context.HttpMethod;
import com.dottydingo.hyperion.service.context.HyperionContext;
import com.dottydingo.service.pipeline.PhaseExecutor;
import com.dottydingo.service.pipeline.PhaseSelector;

/**
 */
public class CrudPhaseSelector implements PhaseSelector<HyperionContext>
{
    private PhaseExecutor<HyperionContext> queryPhaseExecutor;
    private PhaseExecutor<HyperionContext> getPhaseExecutor;
    private PhaseExecutor<HyperionContext> historyPhaseExecutor;
    private PhaseExecutor<HyperionContext> postPhaseExecutor;
    private PhaseExecutor<HyperionContext> putPhaseExecutor;
    private PhaseExecutor<HyperionContext> deletePhaseExecutor;
    private PhaseExecutor<HyperionContext> optionsPhaseExecutor;

    public void setQueryPhaseExecutor(PhaseExecutor<HyperionContext> queryPhaseExecutor)
    {
        this.queryPhaseExecutor = queryPhaseExecutor;
    }

    public void setGetPhaseExecutor(PhaseExecutor<HyperionContext> getPhaseExecutor)
    {
        this.getPhaseExecutor = getPhaseExecutor;
    }

    public void setPostPhaseExecutor(PhaseExecutor<HyperionContext> postPhaseExecutor)
    {
        this.postPhaseExecutor = postPhaseExecutor;
    }

    public void setPutPhaseExecutor(PhaseExecutor<HyperionContext> putPhaseExecutor)
    {
        this.putPhaseExecutor = putPhaseExecutor;
    }

    public void setDeletePhaseExecutor(PhaseExecutor<HyperionContext> deletePhaseExecutor)
    {
        this.deletePhaseExecutor = deletePhaseExecutor;
    }

    public void setHistoryPhaseExecutor(PhaseExecutor<HyperionContext> historyPhaseExecutor)
    {
        this.historyPhaseExecutor = historyPhaseExecutor;
    }

    public void setOptionsPhaseExecutor(PhaseExecutor<HyperionContext> optionsPhaseExecutor)
    {
        this.optionsPhaseExecutor = optionsPhaseExecutor;
    }

    @Override
    public PhaseExecutor<HyperionContext> getNextPhase(HyperionContext context)
    {
        HttpMethod method = context.getEffectiveMethod();

        PhaseExecutor<HyperionContext> executor = null;
        switch (method)
        {
            case DELETE:
                executor = deletePhaseExecutor;
                break;
            case POST:
                executor = postPhaseExecutor;
                break;
            case PUT:
                executor = putPhaseExecutor;
                break;
            case GET:
            {
                if(context.isHistory())
                    executor = historyPhaseExecutor;
                else if (context.getId() != null)
                    executor = getPhaseExecutor;
                else
                    executor = queryPhaseExecutor;

                break;

            }
            case OPTIONS:
                executor = optionsPhaseExecutor;
                break;
        }

        if(executor == null)
            throw new HyperionException(405,"Method not allowed.");

        return executor;
    }
}
