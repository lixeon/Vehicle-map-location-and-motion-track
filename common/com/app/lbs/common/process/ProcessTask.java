/*
 * lbsgw
 */
package com.app.lbs.common.process;

/**
 * Process Task
 *
 * @author colen
 *
 */
public abstract class ProcessTask implements Runnable {

    /**
     * run
     */
    public void run() {
        process();
    }

    /**
     * process
     */
    public abstract void process();
    
    /**
     * is Poison Task
     *
     * @return
     */
    public boolean isPoison() {
        return false;
    }
}
