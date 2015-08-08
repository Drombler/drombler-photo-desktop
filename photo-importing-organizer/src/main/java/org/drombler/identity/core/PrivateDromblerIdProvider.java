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
public class PrivateDromblerIdProvider implements DromblerIdentityProvider{

    @Override
    public String getDromblerIdentityProviderId() {
        return "private"; // TODO: ""?
    }

    @Override
    public boolean isPrivate() {
        return true;
    }
    
}
