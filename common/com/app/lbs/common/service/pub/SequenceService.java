/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Apr 7, 2012
 */
package com.app.lbs.common.service.pub;

/**
 * Sequence Service
 *
 * @author colen
 *
 */
public interface SequenceService {

    /**
     * Next Sequence
     *
     * @param idKey id key
     * @return sequence
     */
    String nextSeq(String idKey);

}
