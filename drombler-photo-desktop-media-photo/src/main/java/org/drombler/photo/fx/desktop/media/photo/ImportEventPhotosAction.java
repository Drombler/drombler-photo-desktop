package org.drombler.photo.fx.desktop.media.photo;

import java.nio.file.Path;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadConsumer;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.commons.action.AbstractActionListener;
import org.drombler.commons.client.dialog.DirectoryChooserProvider;
import org.drombler.commons.data.Openable;
import org.drombler.media.core.photo.PhotoStorage;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * @author Florian
 */
/**
 *
 * @author puce
 */
// TODO: close AutoCloseable Actions
@Action(id = "importEventPhotos", category = "eventMedia", displayName = "%importEventPhotos.displayName", accelerator = "Shortcut+I")
@MenuEntry(path = "File", position = 2010)
public class ImportEventPhotosAction extends AbstractActionListener<Object> implements AutoCloseable {

    private final ServiceTracker<ApplicationThreadExecutorProvider, ApplicationThreadExecutorProvider> applicationThreadExecutorProviderServiceTracker;
    private ServiceTracker<DirectoryChooserProvider, DirectoryChooserProvider> directoryChooserProviderServiceTracker;
    private ServiceTracker<DataHandlerRegistryProvider, DataHandlerRegistryProvider> dataHandlerRegistryServiceTracker;
    private DirectoryChooserProvider directoryChooserProvider;
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;
    private ApplicationThreadExecutorProvider applicationThreadExecutorProvider;

    public ImportEventPhotosAction() {
        this.applicationThreadExecutorProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FrameworkUtil.getBundle(ImportEventPhotosAction.class).getBundleContext(),
                ApplicationThreadExecutorProvider.class, this::setApplicationThreadExecutorProvider);
        this.applicationThreadExecutorProviderServiceTracker.open(true);
        setEnabled(isInitialized());
    }

    @Override
    public void onAction(Object event) {
//        Path testFxmlPath = Paths.get("/fastdata/Programming/Java/drombler/drombler-scene-designer/tmp/test.fxml");
//        ContentPane contentPane = new ContentPane(testFxmlPath);
//        Dockables.inject(contentPane);
//        Dockables.open(contentPane);
        Path directoryPath = directoryChooserProvider.showDialog();
        if (directoryPath != null) {
            openDirectory(directoryPath);
        }
    }

    private boolean isInitialized() {
        return directoryChooserProvider != null;
    }

    private void openDirectory(Path directoryToOpen) {
        PhotoStorage photoStorage = new PhotoStorage(directoryToOpen.getFileName().toString(), directoryToOpen); // TODO: manage photo storages
        PhotoStorageHandler photoStorageHandler = new PhotoStorageHandler(photoStorage);
        dataHandlerRegistryProvider.getDataHandlerRegistry().registerDataHandler(photoStorageHandler);
        Openable openable = photoStorageHandler.getLocalContext().find(Openable.class);
        if (openable != null) {
            openable.open();
        }
    }

    /**
     * @return the applicationExecutorProvider
     */
    public ApplicationThreadExecutorProvider getApplicationThreadExecutorProvider() {
        return applicationThreadExecutorProvider;
    }

    /**
     * @param applicationThreadExecutorProvider the applicationExecutorProvider
     * to set
     */
    public void setApplicationThreadExecutorProvider(ApplicationThreadExecutorProvider applicationThreadExecutorProvider) {
        this.applicationThreadExecutorProvider = applicationThreadExecutorProvider;
        if (this.applicationThreadExecutorProvider != null) {
            this.directoryChooserProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DirectoryChooserProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationThreadExecutorProvider, this::setDirectoryChooserProvider));
            this.dataHandlerRegistryServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DataHandlerRegistryProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationThreadExecutorProvider, this::setDataHandlerRegistryProvider));
            this.directoryChooserProviderServiceTracker.open(true);
            this.dataHandlerRegistryServiceTracker.open(true);
        } else {
            this.directoryChooserProviderServiceTracker.close();
            this.directoryChooserProviderServiceTracker = null;
            this.dataHandlerRegistryServiceTracker.close();
            this.dataHandlerRegistryServiceTracker = null;
        }
    }

    /**
     * @return the directoryChooserProvider
     */
    public DirectoryChooserProvider getDirectoryChooserProvider() {
        return directoryChooserProvider;
    }

    /**
     * @param directoryChooserProvider the directoryChooserProvider to set
     */
    public void setDirectoryChooserProvider(DirectoryChooserProvider directoryChooserProvider) {
        this.directoryChooserProvider = directoryChooserProvider;
        setEnabled(isInitialized());
    }

    /**
     * @return the dataHandlerRegistryProvider
     */
    public DataHandlerRegistryProvider getDataHandlerRegistryProvider() {
        return dataHandlerRegistryProvider;
    }

    /**
     * @param dataHandlerRegistryProvider the dataHandlerRegistryProvider to set
     */
    public void setDataHandlerRegistryProvider(DataHandlerRegistryProvider dataHandlerRegistryProvider) {
        this.dataHandlerRegistryProvider = dataHandlerRegistryProvider;
        setEnabled(isInitialized());
    }

    @Override
    public void close() {
        this.applicationThreadExecutorProviderServiceTracker.close();
    }

}
