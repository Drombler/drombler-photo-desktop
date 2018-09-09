package org.drombler.photo.fx.desktop.event.impl;

import org.drombler.photo.fx.desktop.event.EventManagerClient;
import org.drombler.photo.fx.desktop.event.EventManagerClientProvider;
import java.io.IOException;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 *
 * @author Florian
 */
@Component
public class EventManagerClientProviderImpl implements EventManagerClientProvider {

    @Reference
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;
    private EventManagerClient eventManagerClient;

    @Activate
    public void activate(ComponentContext context) throws IOException {
        this.eventManagerClient = new EventManagerClient(dataHandlerRegistryProvider.getDataHandlerRegistry());
    }

    @Deactivate
    public void deactivate(ComponentContext context) {
        this.eventManagerClient = null;
    }

    @Override
    public EventManagerClient getEventManagerClient() {
        return eventManagerClient;
    }

}
