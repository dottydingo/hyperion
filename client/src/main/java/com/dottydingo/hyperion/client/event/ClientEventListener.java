package com.dottydingo.hyperion.client.event;

/**
 * A listener for handling client events. This can be used for logging, metrics, etc.
 */
public interface ClientEventListener
{
    /**
     * Handle the client event
     * @param clientEvent The event
     */
    void handleEvent(ClientEvent clientEvent);
}
