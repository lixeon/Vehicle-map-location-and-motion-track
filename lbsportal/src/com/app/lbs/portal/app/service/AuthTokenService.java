/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service;

import java.util.Map;

/**
 * AuthToken Manager Service
 *
 * @author luxinxian
 *
 */
public interface AuthTokenService {

    /**
     * Get User
     *
     * @param tokenId
     * @return
     */
    Map<String, Object> checkAndGetUser(String tokenId);

    
    /**
     * Create Token ID for the User
     *
     * @param user
     * @return
     */
    String createTokenId(Map<String, Object> user);

    
}
