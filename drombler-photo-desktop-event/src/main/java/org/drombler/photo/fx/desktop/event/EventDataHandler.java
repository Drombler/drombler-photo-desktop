package org.drombler.photo.fx.desktop.event;

import java.util.UUID;
import org.drombler.acp.core.data.AbstractDataHandler;
import org.drombler.acp.core.data.BusinessObjectHandler;
import org.drombler.event.core.Event;

/**
 *
 * @author Florian
 */
@BusinessObjectHandler
public class EventDataHandler extends AbstractDataHandler<UUID> {

    private final Event event;

    public EventDataHandler(Event event) {
        this.event = event;
    }

    @Override
    public String getTitle() {
        return event.getName();
    }

    @Override
    public String getTooltipText() {
        return event.getName() + "(" + event.getDuration() + ")";
    }

    @Override
    public UUID getUniqueKey() {
        return event.getId();
    }

}
