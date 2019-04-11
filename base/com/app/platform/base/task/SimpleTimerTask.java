 package com.app.platform.base.task;
 
 import org.apache.log4j.Logger;

 
 public abstract class SimpleTimerTask
 {
   private static final Logger LOG = Logger.getLogger(SimpleTimerTask.class);
   
   private Thread thread = null;
   private boolean running = true;
  
   public SimpleTimerTask(final long delay, long cycle)
   {
     this.thread = new Thread(new Runnable()
     {
       public void run() {
         try {
           Thread.sleep(delay);
         }
         catch (InterruptedException e) {}
         
         SimpleTimerTask.this.doRun(cycle);
       }
       
 
     });
     this.thread.setDaemon(true);
   }
 
   public SimpleTimerTask(final long cycle)
   {
     this.thread = new Thread(new Runnable()
     {
       public void run() {
         SimpleTimerTask.this.doRun(cycle);
       }
       
 
     });
     this.thread.setDaemon(true);
   }
   
   private void doRun(long cycle) {
     for (;;) {
       if (isRunning()) {
         try {
           doTask();
 
           try
           {
             if (cycle > 0L) {
               Thread.sleep(cycle);
             }
           }
           catch (InterruptedException e) {}
         }
         catch (Throwable t)
         {
           LOG.error("Running simple task error!", t);
         } finally {
           try {
             if (cycle > 0L) {
               Thread.sleep(cycle);
             }
           }
           catch (InterruptedException e) {}
         }
       }
     }
   }
  
   public SimpleTimerTask start()
   {
     this.thread.start();
     return this;
   }
   
 
 
 
   public void stop()
   {
     this.running = false;
   }
   
 
 
 
 
   public abstract void doTask()
     throws Throwable;
   
 
 
 
   public boolean isRunning()
   {
     return this.running;
   }
 }

