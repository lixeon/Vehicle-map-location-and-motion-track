/*     */ package com.app.lbs.portal.app.service.impl;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;

/*     */ import org.apache.log4j.Logger;

/*     */ import com.app.lbs.common.process.ProcessorFactory;
/*     */ import com.app.lbs.common.process.SMSSendTask;
/*     */ import com.app.lbs.portal.app.service.AbstractClientAppService;
/*     */ import com.app.lbs.portal.app.service.AuthCodeManagerService;
/*     */ import com.app.platform.base.task.SimpleTimerTask;
/*     */ import com.app.platform.base.utils.EncryptUtil;
/*     */ import com.app.platform.base.utils.SystemConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class A0000ServiceImpl
/*     */   extends AbstractClientAppService
/*     */   implements AuthCodeManagerService
/*     */ {
/*  31 */   private static final Logger LOG = Logger.getLogger(A0000ServiceImpl.class);
/*     */   
/*     */ 
/*  34 */   private SimpleTimerTask timeoutAuthCodeCleaner = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public A0000ServiceImpl()
/*     */   {
/*  40 */     this.authCodeCache = new ConcurrentHashMap<String, AuthCode>();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialize()
/*     */   {
/*  53 */     this.timeoutAuthCodeCleaner = new SimpleTimerTask(5000L)
/*     */     {
/*     */       public void doTask() throws Throwable {
/*  56 */         Iterator<Entry<String, A0000ServiceImpl.AuthCode>> allAuthCodes = A0000ServiceImpl.this.authCodeCache.entrySet().iterator();
/*  57 */         long now = System.currentTimeMillis();
/*  58 */         A0000ServiceImpl.AuthCode authCode = null;
/*  59 */         while (allAuthCodes.hasNext()) {
/*  60 */           authCode = (A0000ServiceImpl.AuthCode)((Entry<?, ?>)allAuthCodes.next()).getValue();
/*  61 */           if (authCode.isTimeout(now)) {
/*  62 */             A0000ServiceImpl.this.authCodeCache.remove(authCode.mobile);
/*     */           }
/*     */         }
/*     */       }
/*     */     }.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/*  74 */     if (this.timeoutAuthCodeCleaner != null) {
/*  75 */       this.timeoutAuthCodeCleaner.stop();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Map<String, Object> execute(Map<String, Object> params)
/*     */   {
/*  83 */     String mobile = (String)params.get("mobile");
/*  84 */     String authCode = newAuthCode(mobile);
/*  85 */     String message = SystemConfig.get("app.sms.authcode", new Object[] { authCode });
/*     */     
/*  87 */     if (LOG.isInfoEnabled()) {
/*  88 */       LOG.info("send random pwd to mobile : (" + mobile + "," + message + ")");
/*     */     }
/*     */     
/*  91 */     SMSSendTask smsSendTask = new SMSSendTask(mobile, message);
/*  92 */     ProcessorFactory.getProcessor("send-sms-processor").addTask(smsSendTask);
/*     */     
/*  94 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String serviceKey()
/*     */   {
/* 100 */     return "0000";
/*     */   }
/*     */   
/*     */   protected boolean isAuth()
/*     */   {
/* 105 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 110 */   private Map<String, AuthCode> authCodeCache = null;
/*     */   
/*     */   public boolean isRigth(String mobile, String authCode)
/*     */   {
/* 114 */     AuthCode myAuthCode = (AuthCode)this.authCodeCache.get(mobile);
/* 115 */     if ((myAuthCode != null) && (myAuthCode.isSame(authCode))) {
/* 116 */       this.authCodeCache.remove(mobile);
/* 117 */       return true;
/*     */     }
/*     */     
/* 120 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public String newAuthCode(String mobile)
/*     */   {
/* 126 */     String randPwd = EncryptUtil.getDigitRandPwd(6);
/* 127 */     AuthCode authCode = new AuthCode();
/* 128 */     authCode.mobile = mobile;
/* 129 */     authCode.value = randPwd;
/* 130 */     this.authCodeCache.put(mobile, authCode);
/*     */     
/* 132 */     return randPwd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public class AuthCode
/*     */   {
/*     */     private AuthCode() {}
/*     */     
/*     */ 
/*     */ 
/* 144 */     String mobile = null;
/* 145 */     String value = null;
/* 146 */     long time = System.currentTimeMillis();
/*     */     
/*     */     boolean isSame(String anotherValue) {
/* 149 */       return this.value.equalsIgnoreCase(anotherValue);
/*     */     }
/*     */     
/*     */     boolean isTimeout(long now) {
/* 153 */       if (now - this.time > 300000L) {
/* 154 */         return true;
/*     */       }
/* 156 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\新建文件夹\tomcat_lbs_pt\lbsapp\ROOT\WEB-INF\lib\lbs.portal.jar!\com\app\lbs\portal\app\service\impl\A0000ServiceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */