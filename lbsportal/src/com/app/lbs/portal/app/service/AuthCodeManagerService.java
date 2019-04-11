/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service;

/**
 * AuthCode Manager Service
 *
 * @author luxinxian
 *
 */
public interface AuthCodeManagerService {

    /**
     * Is the right AuthCode
     *
     * @param mobile
     * @param authCode
     * @return
     */
    boolean isRigth(String mobile, String authCode);

    /**
     * new AuthCode
     *
     * @param mobile
     * @return
     */
    String newAuthCode(String mobile);

}
