package org.drombler.photo.fx.desktop.media.photo;

import javafx.scene.layout.BorderPane;
import org.drombler.acp.core.docking.EditorDocking;
import org.drombler.commons.docking.DockableDataSensitive;
import org.drombler.commons.docking.fx.FXDockableData;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.fx.core.docking.FXDockableDataUtils;

@EditorDocking(contentType = PhotoStorageHandler.class)
public class EventPhotoImportPane extends BorderPane implements DockableDataSensitive<FXDockableData> {

    private FXDockableData dockableData;
    private final PhotoStorageHandler photoStorageHandler;

    public EventPhotoImportPane(PhotoStorageHandler photoStorageHandler) {
        this.photoStorageHandler = photoStorageHandler;
//        FXMLLoaders.loadRoot(this);
    }

    @Override
    public void setDockableData(FXDockableData dockableData) {
        this.dockableData = dockableData;
// TODO: could this be called by the framework?
        FXDockableDataUtils.configureDockableData(dockableData, photoStorageHandler, "Event Photo Import");

    }
}
