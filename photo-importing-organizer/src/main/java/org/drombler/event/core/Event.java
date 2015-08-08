/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.event.core;

import java.util.Optional;

/**
 *
 * @author Florian
 */
public class Event {

    private final String name;
    private final EventDuration duration;

    public Event(String name, EventDuration duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getDirName() {
        return getDuration().getDirName() + "-" + getName();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the duration
     */
    public EventDuration getDuration() {
        return duration;
    }

    public static Optional<Event> singleFullTimeDayEvent(String dirName) {
        String[] parts = dirName.split("-", 2);
        if (parts.length == 2){
            Optional<FullTimeEventDuration> duration = FullTimeEventDuration.singleDay(parts[0]);
            if (duration.isPresent()){
                return Optional.of(new Event(parts[1], duration.get()));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
