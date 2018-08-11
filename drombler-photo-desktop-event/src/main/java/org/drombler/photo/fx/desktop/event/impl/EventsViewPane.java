package org.drombler.photo.fx.desktop.event.impl;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.drombler.commons.data.fx.DataHandlerRenderer;
import org.drombler.commons.fx.scene.control.RenderedTreeCellFactory;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.apache.commons.lang3.ClassUtils;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.data.spi.DataHandlerDescriptorRegistryProvider;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.osgi.util.tracker.ServiceTracker;

import org.drombler.acp.core.docking.ViewDocking;
import org.drombler.acp.core.docking.WindowMenuEntry;
import org.drombler.commons.data.DataHandler;
import org.drombler.commons.fx.concurrent.FXConsumer;
import org.drombler.commons.fx.scene.renderer.DataRenderer;
import org.drombler.commons.fx.scene.renderer.ObjectRenderer;
import org.drombler.commons.fx.scene.renderer.time.YearRenderer;
import org.drombler.event.core.Event;
import org.drombler.event.core.FullTimeEventDuration;
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
    private TreeView<Object> eventsTreeView;

    public EventsViewPane() {
        TreeItem<Object> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);

        this.eventsTreeView = new TreeView<>(rootItem);
        eventsTreeView.setShowRoot(false);
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
            RenderedTreeCellFactory<Object> renderedTreeCellFactory = new RenderedTreeCellFactory<>();
            DataRenderer<DataHandler<?>> dataHandlerRenderer = new DataHandlerRenderer(this.dataHandlerDescriptorRegistryProvider.getDataHandlerDescriptorRegistry(), 16);
            renderedTreeCellFactory.registerDataRenderer((Class<DataHandler<?>>) (Class<?>) DataHandler.class, dataHandlerRenderer);
            renderedTreeCellFactory.registerDataRenderer(Year.class, new YearRenderer());
            eventsTreeView.setCellFactory(renderedTreeCellFactory);
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
            List<EventDataHandler> eventDataHandlers = eventManagerClientProvider.getEventManagerClient().getAllEvents();
            SortedMap<Year, List<EventDataHandler>> eventHandlersGroupedByYear = groupEventsByYear(eventDataHandlers);
            List<TreeItem<Object>> yearTreeItems = eventHandlersGroupedByYear.entrySet().stream()
                    .map(this::createYearTreeItem)
                    .collect(Collectors.toList());
            eventsTreeView.getRoot().getChildren().addAll(yearTreeItems);
        } else {
            eventsTreeView.getRoot().getChildren().clear();
        }
    }

    private TreeItem<Object> createYearTreeItem(Map.Entry<Year, List<EventDataHandler>> entry) {
        return createYearTreeItem(entry.getKey(), entry.getValue());
    }

    private TreeItem<Object> createYearTreeItem(final Year year, final List<EventDataHandler> handlers) {
        TreeItem<Object> yearTreeItem = new TreeItem<>(year);
        List<TreeItem<Object>> eventDataHandlerTreeItems = handlers.stream()
                .map(this::createEventDataHandlerTreeItem)
                .collect(Collectors.toList());
        yearTreeItem.getChildren().addAll(eventDataHandlerTreeItems);
        return yearTreeItem;
    }

    private SortedMap<Year, List<EventDataHandler>> groupEventsByYear(List<EventDataHandler> eventDataHandlers) {
        SortedMap<Year, List<EventDataHandler>> eventHandlersGroupedByYear = new TreeMap<>();
        eventDataHandlers.forEach(eventDataHandler -> {
            Year year = Year.of(((FullTimeEventDuration) eventDataHandler.getEvent().getDuration()).getStartDateInclusive().getYear());
            if (!eventHandlersGroupedByYear.containsKey(year)) {
                eventHandlersGroupedByYear.put(year, new ArrayList<>());
            }
            eventHandlersGroupedByYear.get(year).add(eventDataHandler);
        });
        return eventHandlersGroupedByYear;
    }

    private TreeItem<Object> createEventDataHandlerTreeItem(EventDataHandler eventDataHandler) {
        TreeItem<Object> eventDataHandlerTreeItem = new TreeItem<>(eventDataHandler);
        eventDataHandlerTreeItem.getChildren().addAll(createEventPhotoSectionTreeItem(), createEventVideoSectionTreeItem());
        return eventDataHandlerTreeItem;
    }

    private TreeItem<Object> createEventPhotoSectionTreeItem() {
        TreeItem<Object> eventPhotoSectionTreeItem = new TreeItem<>("photo");
        return eventPhotoSectionTreeItem;
    }

    private TreeItem<Object> createEventVideoSectionTreeItem() {
        TreeItem<Object> eventVideoSectionTreeItem = new TreeItem<>("video");
        return eventVideoSectionTreeItem;
    }

    @Override
    public void close() {
        dataHandlerDescriptorRegistryProviderServiceTracker.close();
        eventManagerClientProviderServiceTracker.close();
    }

}
