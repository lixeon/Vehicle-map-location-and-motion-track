/*
 * lbsgw
 */
package com.app.lbs.common.process;

/**
 * Processor
 *
 * @author colen
 *
 */
public interface Processor {

    /**
     * add task to processor
     *
     * @param task T
     * @return
     */
    boolean addTask(ProcessTask task);
}
