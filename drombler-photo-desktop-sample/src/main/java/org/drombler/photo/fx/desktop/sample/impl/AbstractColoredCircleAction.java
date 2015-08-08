
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.photo.fx.desktop.sample.impl;

import org.drombler.photo.fx.desktop.sample.ColoredCircle;
import org.drombler.photo.fx.desktop.sample.ColoredCircleManager;
import org.drombler.commons.action.AbstractToggleActionListener;
import org.drombler.commons.context.ActiveContextSensitive;
import org.drombler.commons.context.Context;
import org.drombler.commons.context.ContextEvent;


public abstract class AbstractColoredCircleAction extends AbstractToggleActionListener<Object> implements
        ActiveContextSensitive {

    private ColoredCircleManager coloredCircleManager;
    private Context activeContext;
    private final ColoredCircle coloredCircle;

    public AbstractColoredCircleAction(ColoredCircle coloredCircle) {
        this.coloredCircle = coloredCircle;
    }

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        if (newValue && coloredCircleManager != null) {
            coloredCircleManager.setColoredCircle(coloredCircle);
        }
    }

    @Override
    public void setActiveContext(Context activeContext) {
        this.activeContext = activeContext;
        this.activeContext.addContextListener(ColoredCircleManager.class,
                (ContextEvent event) -> AbstractColoredCircleAction.this.contextChanged());
        contextChanged();
    }

    private void contextChanged() {
        coloredCircleManager = activeContext.find(ColoredCircleManager.class);
        setEnabled(coloredCircleManager != null);
        setSelected(coloredCircleManager != null
                && coloredCircleManager.getColoredCircle() != null
                && coloredCircleManager.getColoredCircle().equals(coloredCircle));
    }
}
