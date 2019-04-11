/*
 * lbsgw
 */
package com.app.lbs.common.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.app.platform.base.task.SimpleTimerTask;


/**
 * Processor Factory
 *
 * @author colen
 *
 */
public class ProcessorFactory {

    /** processor holder */
    private static final Map<String, Processor> holder
        = new HashMap<String, Processor>();

    /**
     * Get Processor
     *
     * @param name name of processor
     * @return
     */
    public synchronized static Processor getProcessor(String name) {
        Processor processor = holder.get(name);
        if (processor == null) {
            processor = new DefaultProcessor(name);
            holder.put(name, processor);
        }
        return processor;
    }
    
  public static void destroy()
   {
    Iterator<Map.Entry<String, Processor>> itr = holder.entrySet().iterator();
    DefaultProcessor p = null;
    while (itr.hasNext()) {
    p = (DefaultProcessor)itr.next();
      p.stop();
    }
 }   
    /**
     * Default Processor
     * 
     *
     * @author colen
     *
     * @param <ProcessTask>
     */
    private static class DefaultProcessor implements Processor {

        /** Cache Size */
        private static final int CACHE_SIZE = 50000;

 		private SimpleTimerTask dispatcher = null;
        /** Task Queue */
        private BlockingQueue<ProcessTask> taskQueue = null;

        /** executor */
        private ExecutorService executor = null;

        /**
         * Constructor
         */
        public DefaultProcessor(String name) {
            this.taskQueue = new LinkedBlockingQueue<ProcessTask>(CACHE_SIZE);
            this.executor = Executors.newSingleThreadExecutor();

            this.dispatcher = new SimpleTimerTask(-1L) {
        public void doTask() throws Throwable {
          ProcessorFactory.DefaultProcessor.this.executor.execute((Runnable)ProcessorFactory.DefaultProcessor.this.taskQueue.take());
        }
      }.start();
        }
		 public void stop()
     {
       dispatcher.stop();
    }

        @Override
        public boolean addTask(ProcessTask task) {
            return this.taskQueue.offer(task);
        }

    }
}
