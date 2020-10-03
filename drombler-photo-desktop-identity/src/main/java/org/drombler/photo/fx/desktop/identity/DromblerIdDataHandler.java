package org.drombler.photo.fx.desktop.identity;

import org.drombler.acp.core.data.AbstractDataHandler;
import org.drombler.identity.core.DromblerId;

/**
 *
 * @author Florian
 */


public class DromblerIdDataHandler extends AbstractDataHandler<String> {

    private final DromblerId dromblerId;
    
    public DromblerIdDataHandler(DromblerId dromblerId){
        this.dromblerId = dromblerId;
    }

    @Override
    public String getTitle() {
        return dromblerId.getDromblerIdFormatted();
    }

    @Override
    public String getTooltipText() {
        return dromblerId.getDromblerIdFormatted();
    }

    @Override
    public String getUniqueKey() {
        return dromblerId.getDromblerIdFormatted();
    }
    
}
