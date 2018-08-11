package org.drombler.photo.fx.desktop.event.impl;

import java.util.List;
import java.util.stream.Collectors;
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
import org.drombler.photo.fx.desktop.event.EventDataHandler;
import org.drombler.photo.fx.desktop.event.EventManagerClientProvider;

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
    private final ServiceTracker<EventManagerClientProvider, EventManagerClientProvider> eventManagerClientProviderServiceTracker;
    private DataHandlerDescriptorRegistryProvider dataHandlerDescriptorRegistryProvider;
    private EventManagerClientProvider eventManagerClientProvider;
    private TreeView<DataHandler<?>> eventsTreeView;

    public EventsViewPane() {
        TreeItem<DataHandler<?>> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);

        this.eventsTreeView = new TreeView<>(rootItem);

        setCenter(eventsTreeView);
        this.dataHandlerDescriptorRegistryProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DataHandlerDescriptorRegistryProvider.class, new FXConsumer<>(this::setDataHandlerDescriptorRegistryProvider));
        this.dataHandlerDescriptorRegistryProviderServiceTracker.open(true);
        this.eventManagerClientProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(EventManagerClientProvider.class, new FXConsumer<>(this::setEventManagerClientProvider));
        this.eventManagerClientProviderServiceTracker.open(true);
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

    /**
     * @return the eventManagerClientProvider
     */
    public EventManagerClientProvider getEventManagerClientProvider() {
        return eventManagerClientProvider;
    }

    /**
     * @param eventManagerClientProvider the eventManagerClientProvider to set
     */
    public void setEventManagerClientProvider(EventManagerClientProvider eventManagerClientProvider) {
        this.eventManagerClientProvider = eventManagerClientProvider;
        if (eventManagerClientProvider != null) {
            List<TreeItem<DataHandler<?>>> eventDataHandlerTreeItems = eventManagerClientProvider.getEventManagerClient().getAllEvents().stream()
                    .map(eventDataHandler -> new TreeItem<DataHandler<?>>(eventDataHandler))
                    .collect(Collectors.toList());
            eventsTreeView.getRoot().getChildren().addAll(eventDataHandlerTreeItems);
        } else {
            eventsTreeView.getRoot().getChildren().clear();
        }
    }

    @Override
    public void close() {
        dataHandlerDescriptorRegistryProviderServiceTracker.close();
        eventManagerClientProviderServiceTracker.close();
    }

}
