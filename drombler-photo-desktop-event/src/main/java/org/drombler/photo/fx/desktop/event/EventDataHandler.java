package org.drombler.photo.fx.desktop.event;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.UUID;
import org.drombler.acp.core.data.AbstractDataHandler;
import org.drombler.acp.core.data.BusinessObjectHandler;
import org.drombler.event.core.Event;
import org.drombler.event.core.FullTimeEventDuration;

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
        return getDurationString() + " " + event.getName();
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

    private String getDurationString() {
        FullTimeEventDuration duration = (FullTimeEventDuration) event.getDuration();
        StringBuilder sb = new StringBuilder("[");
        appendMonthDayString(sb, duration.getStartDateInclusive());
        if (!duration.isSingleDay()) {
            sb.append(" - ");
            appendMonthDayString(sb, duration.getEndDateInclusive());
        }
        sb.append("]");
        return sb.toString();
    }

    private void appendMonthDayString(StringBuilder sb, final LocalDate date) {
        sb.append(getMonthString(date.getMonth())).append(", ").append(date.getDayOfMonth());
    }

    private String getMonthString(final Month month) {
        return month.getDisplayName(TextStyle.SHORT, Locale.getDefault(Locale.Category.DISPLAY));
    }

}
