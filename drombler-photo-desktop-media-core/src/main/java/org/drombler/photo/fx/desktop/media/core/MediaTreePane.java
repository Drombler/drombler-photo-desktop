package org.drombler.photo.fx.desktop.media.core;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.data.DataHandler;
import org.drombler.commons.data.DataHandlerDescriptorRegistry;
import org.drombler.commons.data.DataHandlerRegistry;
import org.drombler.commons.data.fx.DataHandlerRenderer;
import org.drombler.commons.fx.scene.control.RenderedTreeCellFactory;
import org.drombler.commons.fx.scene.renderer.DataRenderer;
import org.drombler.commons.fx.scene.renderer.time.YearRenderer;
import org.drombler.event.core.Event;
import org.drombler.event.core.AllDayEventDuration;
import org.drombler.media.core.MediaSource;
import org.drombler.photo.fx.desktop.event.EventDataHandler;
import org.drombler.photo.fx.desktop.identity.DromblerIdDataHandler;

/**
 *
 * @author Florian
 */
public class MediaTreePane extends BorderPane {

    private final ObservableList<AbstractMediaSourceHandler<?>> mediaSourceHandlers = FXCollections.observableArrayList();
    private final RenderedTreeCellFactory<Object> renderedTreeCellFactory = new RenderedTreeCellFactory<>();
    private final ObjectProperty<DataHandlerDescriptorRegistry> dataHandlerDescriptorRegistry = new SimpleObjectProperty<>(this, "dataHandlerDescriptorRegistry");
    private final ObjectProperty<DataHandlerRegistry> dataHandlerRegistry = new SimpleObjectProperty<>(this, "dataHandlerRegistry");
    private final ObjectProperty<MediaMasterViewSettings> mediaMasterViewSettings = new SimpleObjectProperty<>(this, "mediaMasterViewSettings", new MediaMasterViewSettings());
    private TreeView<Object> mediaTreeView;

    public MediaTreePane() {
        TreeItem<Object> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);

        this.mediaTreeView = new TreeView<>(rootItem);
        mediaTreeView.setShowRoot(false);
        mediaTreeView.setCellFactory(renderedTreeCellFactory);

        renderedTreeCellFactory.registerDataRenderer(Year.class, new YearRenderer());

        setCenter(mediaTreeView);

        mediaSourceHandlers.addListener((ListChangeListener.Change<? extends AbstractMediaSourceHandler<?>> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    addMediaSourceItems(change.getAddedSubList());
                } else if (change.wasRemoved()) {
                    removeMediaSourceItems(change.getRemoved());
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

    private void addMediaSourceItems(List<? extends AbstractMediaSourceHandler<?>> mediaSources) {
        SortedMap<Year, List<? extends MediaSource<?>>> eventHandlersGroupedByYear = groupMediaSourcesByYear(mediaSources);
        List<TreeItem<Object>> yearTreeItems = eventHandlersGroupedByYear.entrySet().stream()
                .map(this::createYearTreeItem)
                .collect(Collectors.toList());
        mediaTreeView.getRoot().getChildren().addAll(yearTreeItems);
    }

    private TreeItem<Object> createYearTreeItem(Map.Entry<Year, List<? extends AbstractMediaSourceHandler<?>>> entry) {
        return createYearTreeItem(entry.getKey(), entry.getValue());
    }

    private TreeItem<Object> createYearTreeItem(final Year year, final List<? extends AbstractMediaSourceHandler<?>> handlers) {
        TreeItem<Object> yearTreeItem = new TreeItem<>(year);
        List<TreeItem<Object>> eventDataHandlerTreeItems = handlers.stream()
                .map(this::createEventDataHandlerTreeItem)
                .collect(Collectors.toList());
        yearTreeItem.getChildren().addAll(eventDataHandlerTreeItems);
        return yearTreeItem;
    }

    private TreeItem<Object> createEventTreeItem(AbstractMediaSourceHandler<?> mediaSourceHandler) {
        TreeItem<Object> eventTreeItem = new TreeItem<>(getDataHandlerRegistry().registerDataHandler(dataHandlerRegistry));
        return eventTreeItem;
    }
    
    private EventDataHandler getEventDataHandler(Event event){
        return getDataHandlerRegistry().registerDataHandler(new EventDataHandler(event));
    }
    
    private List<EventDataHandler> detectDistinctEvents(List<AbstractMediaSourceHandler<?>> mediaSourceHandlers){
        return mediaSourceHandlers.stream()
                .map(AbstractMediaSourceHandler::getMediaSource)
                .map(MediaSource::getEvent)
                .distinct()
                .map(EventDataHandler::new)
                .map(getDataHandlerRegistry()::registerDataHandler)
                .collect(Collectors.toList());
    }

    private TreeItem<Object> createCopyrightOwnerTreeItem(final DromblerIdDataHandler coyprightOwnerDataHandler) {
        TreeItem<Object> eventTreeItem = new TreeItem<>(coyprightOwnerDataHandler);
        return eventTreeItem;
    }

    private TreeItem<Object> createMediaSourceTreeItem(final AbstractMediaSourceHandler<?> mediaSourceHandler) {
        TreeItem<Object> eventTreeItem = new TreeItem<>(mediaSourceHandler);
        return eventTreeItem;
    }

    private SortedMap<Year, List<? extends MediaSource<?>>> groupMediaSourcesByYear(List<? extends AbstractMediaSourceHandler<?>> mediaSourceHandlers) {
        SortedMap<Year, List<? extends MediaSource<?>>> eventHandlersGroupedByYear = new TreeMap<>();
        mediaSourceHandlers.forEach(eventDataHandler -> {
            Year year = Year.of(((AllDayEventDuration) eventDataHandler.getEvent().getDuration()).getStartDateInclusive().getYear());
            if (!eventHandlersGroupedByYear.containsKey(year)) {
                eventHandlersGroupedByYear.put(year, new ArrayList<>());
            }
            eventHandlersGroupedByYear.get(year).add(eventDataHandler);
        });
        return eventHandlersGroupedByYear;
    }

    private TreeItem<Object> createEventDataHandlerTreeItem(AbstractMediaSourceHandler<?> eventDataHandler) {
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

    private void removeMediaSourceItems(List<? extends AbstractMediaSourceHandler<?>> removed) {
        // TODO
    }

    /**
     * @return the media sources
     */
    public ObservableList<AbstractMediaSourceHandler<?>> getMediaSources() {
        return mediaSourceHandlers;
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

    public final DataHandlerRegistry getDataHandlerRegistry() {
        return dataHandlerRegistryProperty().get();
    }

    public final void setDataHandlerRegistry(DataHandlerRegistry dataHandlerDescriptorRegistry) {
        dataHandlerRegistryProperty().set(dataHandlerDescriptorRegistry);
    }

    public ObjectProperty<DataHandlerRegistry> dataHandlerRegistryProperty() {
        return dataHandlerRegistry;
    }
}
