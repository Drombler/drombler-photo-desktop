package org.drombler.photo.fx.desktop.event;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.data.DataHandler;
import org.drombler.commons.data.DataHandlerDescriptorRegistry;
import org.drombler.commons.data.fx.DataHandlerRenderer;
import org.drombler.commons.fx.scene.control.RenderedTreeCellFactory;
import org.drombler.commons.fx.scene.renderer.DataRenderer;
import org.drombler.commons.fx.scene.renderer.time.YearRenderer;
import org.drombler.event.core.FullTimeEventDuration;

/**
 *
 * @author Florian
 */
public class EventsTreePane extends BorderPane {

    private final ObservableList<EventDataHandler> events = FXCollections.observableArrayList();
    private final RenderedTreeCellFactory<Object> renderedTreeCellFactory = new RenderedTreeCellFactory<>();
    private final ObjectProperty<DataHandlerDescriptorRegistry> dataHandlerDescriptorRegistry = new SimpleObjectProperty<>(this, "dataHandlerDescriptorRegistry");
    private TreeView<Object> eventsTreeView;

    public EventsTreePane() {
        TreeItem<Object> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);

        this.eventsTreeView = new TreeView<>(rootItem);
        eventsTreeView.setShowRoot(false);

        renderedTreeCellFactory.registerDataRenderer(Year.class, new YearRenderer());
        eventsTreeView.setCellFactory(renderedTreeCellFactory);

        setCenter(eventsTreeView);

        events.addListener((ListChangeListener.Change<? extends EventDataHandler> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    addEventTreeItems(change.getAddedSubList());
                } else if (change.wasRemoved()) {
                    removeEventTreeItems(change.getRemoved());
                }
            }
        });

        dataHandlerDescriptorRegistry.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                renderedTreeCellFactory.unregisterDataRenderer(getDataHandlerType());
            }
            if (newValue != null) {
                DataRenderer<DataHandler<?>> dataHandlerRenderer = new DataHandlerRenderer(newValue, 16);
                renderedTreeCellFactory.registerDataRenderer(getDataHandlerType(), dataHandlerRenderer);
            }
        });
    }

    private static Class<DataHandler<?>> getDataHandlerType() {
        return (Class<DataHandler<?>>) (Class<?>) DataHandler.class;
    }

    private void addEventTreeItems(List<? extends EventDataHandler> events) {
        SortedMap<Year, List<EventDataHandler>> eventHandlersGroupedByYear = groupEventsByYear(events);
        List<TreeItem<Object>> yearTreeItems = eventHandlersGroupedByYear.entrySet().stream()
                .map(this::createYearTreeItem)
                .collect(Collectors.toList());
        eventsTreeView.getRoot().getChildren().addAll(yearTreeItems);
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

    private SortedMap<Year, List<EventDataHandler>> groupEventsByYear(List<? extends EventDataHandler> eventDataHandlers) {
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

    private void removeEventTreeItems(List<? extends EventDataHandler> removed) {
        // TODO
    }

    /**
     * @return the events
     */
    public ObservableList<EventDataHandler> getEvents() {
        return events;
    }

    public final DataHandlerDescriptorRegistry getDataHandlerDescriptorRegistry() {
        return dataHandlerDescriptorRegistryProperty().get();
    }

    public final void setDataHandlerDescriptorRegistry(DataHandlerDescriptorRegistry dataHandlerDescriptorRegistry) {
        dataHandlerDescriptorRegistryProperty().set(dataHandlerDescriptorRegistry);
    }

    public ObjectProperty<DataHandlerDescriptorRegistry> dataHandlerDescriptorRegistryProperty() {
        return dataHandlerDescriptorRegistry;
    }
}
