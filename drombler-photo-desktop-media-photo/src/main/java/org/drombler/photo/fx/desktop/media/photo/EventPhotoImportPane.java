package org.drombler.photo.fx.desktop.media.photo;

import javafx.scene.layout.BorderPane;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.data.spi.DataHandlerDescriptorRegistryProvider;
import org.drombler.acp.core.docking.EditorDocking;
import org.drombler.commons.docking.DockableDataSensitive;
import org.drombler.commons.docking.fx.FXDockableData;
import org.drombler.commons.fx.concurrent.FXConsumer;
import org.drombler.fx.core.docking.FXDockableDataUtils;
import org.drombler.photo.fx.desktop.media.core.MediaTreePane;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

@EditorDocking(contentType = PhotoStorageHandler.class)
public class EventPhotoImportPane extends BorderPane implements DockableDataSensitive<FXDockableData> {

    private final ServiceTracker<DataHandlerDescriptorRegistryProvider, DataHandlerDescriptorRegistryProvider> dataHandlerDescriptorRegistryProviderServiceTracker;
    private DataHandlerDescriptorRegistryProvider dataHandlerDescriptorRegistryProvider;

    private FXDockableData dockableData;
    private final PhotoStorageHandler photoStorageHandler;
    private final MediaTreePane eventsTreePane = new MediaTreePane();

    public EventPhotoImportPane(PhotoStorageHandler photoStorageHandler) {
        this.photoStorageHandler = photoStorageHandler;
//        FXMLLoaders.loadRoot(this);
        setCenter(eventsTreePane);
        this.dataHandlerDescriptorRegistryProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DataHandlerDescriptorRegistryProvider.class, new FXConsumer<>(this::setDataHandlerDescriptorRegistryProvider));
        this.dataHandlerDescriptorRegistryProviderServiceTracker.open(true);
    }

    @Override
    public void setDockableData(FXDockableData dockableData) {
        this.dockableData = dockableData;
// TODO: could this be called by the framework?
        FXDockableDataUtils.configureDockableData(dockableData, photoStorageHandler, "Event Photo Import");

    }

    public void setDataHandlerDescriptorRegistryProvider(DataHandlerDescriptorRegistryProvider dataHandlerDescriptorRegistryProvider) {
        this.dataHandlerDescriptorRegistryProvider = dataHandlerDescriptorRegistryProvider;
        if (dataHandlerDescriptorRegistryProvider != null) {
            eventsTreePane.setDataHandlerDescriptorRegistry(this.dataHandlerDescriptorRegistryProvider.getDataHandlerDescriptorRegistry());
        } else {
            eventsTreePane.setDataHandlerDescriptorRegistry(null);
        }
    }
}
