package org.drombler.photo.fx.desktop.event;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.drombler.commons.data.DataHandlerRegistry;
import org.drombler.event.management.EventManager;

/**
 *
 * @author Florian
 */
public class EventManagerClient {

    private final EventManager eventManager;
    private final DataHandlerRegistry dataHandlerRegistry;

    public EventManagerClient(DataHandlerRegistry dataHandlerRegistry) throws IOException {
        this.eventManager = new EventManager();
        this.dataHandlerRegistry = dataHandlerRegistry;
    }

    public List<EventDataHandler> getAllEvents() {
        return eventManager.getAllEvents().stream()
                .map(EventDataHandler::new)
                .map(dataHandlerRegistry::registerDataHandler)
                .collect(Collectors.toList());
    }
}
