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
public interface DromblerId {
    String getDromblerIdString();
    
    String getDromblerIdFormatted();
    
    DromblerIdentityProvider getDromblerIdentityProvider();
    
    DromblerIdType getType();
    
}
