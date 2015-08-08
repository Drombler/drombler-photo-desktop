/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.identity.core;

/**
 *
 * @author Florian
 */
public class DromblerUserId implements DromblerId {

    private final String dromblerIdString;
    private DromblerIdentityProvider dromblerIdentityProvider;

    public DromblerUserId(String dromblerIdString) {
        this(dromblerIdString, new PrivateDromblerIdProvider());
    }

    public DromblerUserId(String dromblerIdString, DromblerIdentityProvider dromblerIdentityProvider) {
        this.dromblerIdString = dromblerIdString;
        this.dromblerIdentityProvider = dromblerIdentityProvider;
    }

    @Override
    public String getDromblerIdString() {
        return dromblerIdString;
    }

    @Override
    public String getDromblerIdFormatted() {
        return getDromblerIdentityProvider().isPrivate() ? getDromblerIdString()
                : getDromblerIdString() + "@" + getDromblerIdentityProvider().getDromblerIdentityProviderId();
    }

    @Override
    public DromblerIdentityProvider getDromblerIdentityProvider() {
        return dromblerIdentityProvider;
    }

    @Override
    public DromblerIdType getType() {
        return DromblerIdType.USER;
    }

}
