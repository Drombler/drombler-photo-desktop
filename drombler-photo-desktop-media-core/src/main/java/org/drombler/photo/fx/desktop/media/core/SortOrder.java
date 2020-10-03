package org.drombler.photo.fx.desktop.media.core;

import java.util.Comparator;

/**
 *
 * @author Florian
 */


public enum SortOrder {
    ASCENDING(Comparator.naturalOrder()),
    DESCENDING(Comparator.reverseOrder()),
    UNSORTED(null);
    
    private final Comparator comparator;
    
    SortOrder(Comparator comparator){
        this.comparator = comparator;
    }
}
