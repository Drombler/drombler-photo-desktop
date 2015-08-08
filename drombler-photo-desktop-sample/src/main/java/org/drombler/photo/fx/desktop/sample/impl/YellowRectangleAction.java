
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.photo.fx.desktop.sample.impl;

import org.drombler.photo.fx.desktop.sample.ColoredRectangle;
import org.drombler.acp.core.action.ToggleAction;
import org.drombler.acp.core.action.ToggleMenuEntry;
import org.drombler.acp.core.action.ToolBarToggleEntry;


@ToggleAction(id = "yellowRectangle", category = "test", displayName = "%yellowRectangle.displayName", accelerator = "Shortcut+Y",
icon = "yellow-rectangle.png")
@ToggleMenuEntry(path = "Custom", position = 1220)
@ToolBarToggleEntry(toolBarId = "rectangle", position = 160)
public class YellowRectangleAction extends AbstractColoredRectangleAction {

    public YellowRectangleAction() {
        super(ColoredRectangle.YELLOW);
    }
}
