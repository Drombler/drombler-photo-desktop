/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.importing;

import java.util.Comparator;
import org.drombler.event.core.EventDuration;
import org.drombler.event.core.FullTimeEventDuration;

/**
 *
 * @author Florian
 */
public class ImportEventDurationComparator implements Comparator<EventDuration> {

    private static final int LESS = -1;
    private static final int GREATER = 1;
    private static final int EQUAL = 0;

    private final Comparator<FullTimeEventDuration> fullTimeEventDurationComparator
            = Comparator.comparing(FullTimeEventDuration::getStartDateInclusive).thenComparing(FullTimeEventDuration::getEndDateInclusive);

    @Override
    public int compare(EventDuration ed1, EventDuration ed2) {
        if (!isFullTimeEventDuration(ed1) && isFullTimeEventDuration(ed2)) {
            return LESS;
        } else if (isFullTimeEventDuration(ed1) && !isFullTimeEventDuration(ed2)) {
            return GREATER;
        } else if (isFullTimeEventDuration(ed1) && isFullTimeEventDuration(ed2)) {
            return fullTimeEventDurationComparator.compare((FullTimeEventDuration) ed1,
                    (FullTimeEventDuration) ed2);
        } else {
            return EQUAL;
        }
    }

    private static boolean isFullTimeEventDuration(EventDuration eventDuration) {
        return eventDuration instanceof FullTimeEventDuration;
    }

}
