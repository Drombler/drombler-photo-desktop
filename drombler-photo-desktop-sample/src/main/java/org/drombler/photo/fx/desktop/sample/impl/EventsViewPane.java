package org.drombler.photo.fx.desktop.sample.impl;

import org.drombler.commons.data.fx.DataHandlerRenderer;
import org.drombler.commons.fx.scene.control.RenderedTreeCellFactory;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.data.spi.DataHandlerDescriptorRegistryProvider;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.osgi.util.tracker.ServiceTracker;

import org.drombler.acp.core.docking.ViewDocking;
import org.drombler.acp.core.docking.WindowMenuEntry;
import org.drombler.commons.data.DataHandler;
import org.drombler.commons.fx.concurrent.FXConsumer;
import org.drombler.event.core.Event;
import org.drombler.photo.fx.desktop.sample.EventDataHandler;

/**
 *
 * @author Florian
 */
@ViewDocking(areaId = "left", position = 10, displayName = "%displayName", // icon = "top-test-pane.png",
        accelerator = "Shortcut+e",
        menuEntry
        = @WindowMenuEntry(path = "", position = 20))
public class EventsViewPane extends BorderPane implements AutoCloseable {

    private final ServiceTracker<DataHandlerDescriptorRegistryProvider, DataHandlerDescriptorRegistryProvider> dataHandlerDescriptorRegistryProviderServiceTracker;
    private DataHandlerDescriptorRegistryProvider dataHandlerDescriptorRegistryProvider;
    private TreeView<DataHandler<?>> eventsTreeView;

    public EventsViewPane() {
        TreeItem<DataHandler<?>> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);

        for (int i = 1; i < 6; i++) {
            TreeItem<DataHandler<?>> item = new TreeItem<>(new EventDataHandler(Event.fullTimeDayEvent("20180203-Test " + i).get()));
            rootItem.getChildren().add(item);
        }

        this.eventsTreeView = new TreeView<>(rootItem);

        setCenter(eventsTreeView);
        this.dataHandlerDescriptorRegistryProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DataHandlerDescriptorRegistryProvider.class, new FXConsumer<>(this::setDataHandlerDescriptorRegistryProvider));
        this.dataHandlerDescriptorRegistryProviderServiceTracker.open(true);
    }

    /**
     * @return the dataHandlerDescriptorRegistryProvider
     */
    public DataHandlerDescriptorRegistryProvider getDataHandlerDescriptorRegistryProvider() {
        return dataHandlerDescriptorRegistryProvider;
    }

    /**
     * @param dataHandlerDescriptorRegistryProvider the
     * dataHandlerDescriptorRegistryProvider to set
     */
    public void setDataHandlerDescriptorRegistryProvider(DataHandlerDescriptorRegistryProvider dataHandlerDescriptorRegistryProvider) {
        this.dataHandlerDescriptorRegistryProvider = dataHandlerDescriptorRegistryProvider;
        if (dataHandlerDescriptorRegistryProvider != null) {
            eventsTreeView.setCellFactory(new RenderedTreeCellFactory<>(new DataHandlerRenderer(this.dataHandlerDescriptorRegistryProvider.getDataHandlerDescriptorRegistry(), 16)));
        } else {
            eventsTreeView.setCellFactory(null);
        }
    }

    @Override
    public void close() {
        dataHandlerDescriptorRegistryProviderServiceTracker.close();
    }
}
