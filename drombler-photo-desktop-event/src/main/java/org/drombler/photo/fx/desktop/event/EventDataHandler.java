package org.drombler.photo.fx.desktop.event;

import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.drombler.acp.core.data.AbstractDataHandler;
import org.drombler.acp.core.data.BusinessObjectHandler;
import org.drombler.event.core.Event;
import org.drombler.event.core.format.EventDisplayNameFormatter;
import org.softsmithy.lib.text.FormatException;

/**
 *
 * @author Florian
 */
@BusinessObjectHandler
public class EventDataHandler extends AbstractDataHandler<UUID> {

    private final Event event;
    private final EventDisplayNameFormatter eventDisplayNameFormatter = new EventDisplayNameFormatter(Locale.getDefault(Locale.Category.DISPLAY), false);

    public EventDataHandler(Event event) {
        this.event = event;
    }

    @Override
    public String getTitle() {
        try {
            return eventDisplayNameFormatter.format(event);
        } catch (FormatException ex) {
            Logger.getLogger(EventDataHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "<error>";
        }
    }

    @Override
    public String getTooltipText() {
        return event.getName() + "(" + event.getDuration() + ")";
    }

    @Override
    public UUID getUniqueKey() {
        return event.getId();
    }

    /**
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    

}
