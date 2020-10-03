package org.drombler.photo.fx.desktop.media.core.impl;

import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.data.spi.DataHandlerDescriptorRegistryProvider;
import org.osgi.util.tracker.ServiceTracker;

import org.drombler.acp.core.docking.ViewDocking;
import org.drombler.acp.core.docking.WindowMenuEntry;
import org.drombler.commons.fx.concurrent.FXConsumer;
import org.drombler.event.core.Event;
import org.drombler.media.core.MediaSource;
import org.drombler.media.core.MediaStorage;
import org.drombler.photo.fx.desktop.event.EventDataHandler;
import org.drombler.photo.fx.desktop.event.EventManagerClientProvider;
import org.drombler.photo.fx.desktop.media.core.MediaTreePane;
import org.drombler.photo.fx.desktop.media.core.AbstractMediaSourceHandler;
import org.drombler.photo.fx.desktop.media.core.MediaStorageClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Florian
 */
@ViewDocking(areaId = "left", position = 10, displayName = "%displayName", // icon = "top-test-pane.png",
        accelerator = "Shortcut+e",
        menuEntry
        = @WindowMenuEntry(path = "", position = 20))
public class MediaMasterViewPane extends BorderPane implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaMasterViewPane.class);

    private final ServiceTracker<DataHandlerDescriptorRegistryProvider, DataHandlerDescriptorRegistryProvider> dataHandlerDescriptorRegistryProviderServiceTracker;
    private final ServiceTracker<EventManagerClientProvider, EventManagerClientProvider> eventManagerClientProviderServiceTracker;
    private final ServiceTracker<MediaStorageClientProvider, MediaStorageClientProvider> mediaStorageClientProviderServiceTracker;
    private DataHandlerDescriptorRegistryProvider dataHandlerDescriptorRegistryProvider;
    private EventManagerClientProvider eventManagerClientProvider;
    private final ObservableList<MediaStorageClientProvider<?, ?, ?>> mediaStorageClientProviders = FXCollections.observableArrayList();
    private final MediaTreePane eventsTreePane = new MediaTreePane();

    public MediaMasterViewPane() {
        setCenter(eventsTreePane);

        this.dataHandlerDescriptorRegistryProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DataHandlerDescriptorRegistryProvider.class, new FXConsumer<>(this::setDataHandlerDescriptorRegistryProvider));
        this.dataHandlerDescriptorRegistryProviderServiceTracker.open(true);
        this.eventManagerClientProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(EventManagerClientProvider.class, new FXConsumer<>(this::setEventManagerClientProvider));
        this.eventManagerClientProviderServiceTracker.open(true);
        this.mediaStorageClientProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(MediaStorageClientProvider.class, new FXConsumer<>(this::addMediaStorageClientProvider), new FXConsumer<>(this::removeMediaStorageClientProvider));
        this.mediaStorageClientProviderServiceTracker.open(true);
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
            eventsTreePane.setDataHandlerDescriptorRegistry(this.dataHandlerDescriptorRegistryProvider.getDataHandlerDescriptorRegistry());
        } else {
            eventsTreePane.setDataHandlerDescriptorRegistry(null);
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
            eventsTreePane.getMediaSources().addAll(eventDataHandlers);
        } else {
            eventsTreePane.getMediaSources().clear();
        }
    }

    public <M extends MediaSource<M>, S extends MediaStorage<M>, H extends AbstractMediaSourceHandler<M>> void addMediaStorageClientProvider(MediaStorageClientProvider<M, S, H> mediaStorageClientProvider) {
        mediaStorageClientProviders.add(mediaStorageClientProvider);
//        List<H> mediaSources = getMediaSources(mediaStorageClientProvider); // TODO: get sources outside Application Event Thread
//        mediaSources.forEach(mediaSourceHandler -> {
//            EventDataHandler event = getEvent(mediaSourceHandler);
////            event.addMediaSource(mediaSourceHandler);
//        });

    }

    private <M extends MediaSource<M>, S extends MediaStorage<M>, H extends AbstractMediaSourceHandler<M>> EventDataHandler getEvent(H mediaSourceHandler) {
        final M mediaSource = mediaSourceHandler.getMediaSource();
        Event event = mediaSource.getEvent();
        return eventsTreePane.getMediaSources().stream()
                .filter(eventDataHandler -> eventDataHandler.getEvent().equals(event))
                .findAny()
                .orElseGet(() -> {
                    LOGGER.warn("New event {} found for mediaSource {}", mediaSource.getEvent(), mediaSource);
                    EventDataHandler eventDataHandler = new EventDataHandler(event);
                    eventsTreePane.getMediaSources().add(eventDataHandler);
                    return eventDataHandler;
                });
    }

    private <M extends MediaSource<M>, S extends MediaStorage<M>, H extends AbstractMediaSourceHandler<M>> List<H> getMediaSources(MediaStorageClientProvider<M, S, H> mediaStorageClientProvider) {
        try {
            return mediaStorageClientProvider.getMediaStorageClient().getMediaSources();
        } catch (IOException ex) {
            return null;
        }
    }

//    private <M extends MediaSource<M>, S extends MediaStorage<M>, H extends AbstractMediaSourceHandler<M>> void addMediaStorageClientProvider(MediaStorageClientProvider<M, S, H> mediaStorageClientProvider){
//        
//    }
    public void removeMediaStorageClientProvider(MediaStorageClientProvider<?, ?, ?> mediaStorageClientProvider) {
        mediaStorageClientProviders.remove(mediaStorageClientProvider);
        // TODO: remove treeItems etc.
    }

    @Override
    public void close() {
        dataHandlerDescriptorRegistryProviderServiceTracker.close();
        eventManagerClientProviderServiceTracker.close();
        mediaStorageClientProviderServiceTracker.close();
    }

}
