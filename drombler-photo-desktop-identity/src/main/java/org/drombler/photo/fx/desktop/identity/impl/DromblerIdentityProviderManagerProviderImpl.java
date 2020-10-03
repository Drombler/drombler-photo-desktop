package org.drombler.photo.fx.desktop.identity.impl;

import org.drombler.identity.management.DromblerIdentityProviderManager;
import org.drombler.photo.fx.desktop.identity.DromblerIdentityProviderManagerProvider;
import org.osgi.service.component.annotations.Component;

/**
 *
 * @author Florian
 */
@Component
public class DromblerIdentityProviderManagerProviderImpl implements DromblerIdentityProviderManagerProvider {

    private final DromblerIdentityProviderManager dataHandlerRegistryProvider = new DromblerIdentityProviderManager();

    @Override
    public DromblerIdentityProviderManager getDromblerIdentityProviderManager() {
        return dataHandlerRegistryProvider;
    }

}
