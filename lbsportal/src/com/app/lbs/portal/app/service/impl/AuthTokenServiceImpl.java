/*     */ package com.app.lbs.portal.app.service.impl;
/*     */ 
/*     */ import com.app.lbs.portal.app.service.AuthTokenService;
/*     */ import com.app.platform.base.task.SimpleTimerTask;
/*     */ import com.app.platform.base.utils.DateUtil;
/*     */ import com.app.platform.base.utils.EncryptUtil;
/*     */ import com.app.platform.base.utils.SystemConfig;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AuthTokenServiceImpl
/*     */   implements AuthTokenService
/*     */ {
/*     */   private static final String K_REFRESH_TIME = "K_REFRESH_TIME";
/*  31 */   private Map<String, Map<String, Object>> authTokenCache = null;
/*     */   
/*     */ 
/*  34 */   private SimpleTimerTask timeoutAuthTokenCleaner = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialize()
/*     */   {
/*  41 */     this.authTokenCache = new ConcurrentHashMap<String, Map<String, Object>>();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */     this.timeoutAuthTokenCleaner = new SimpleTimerTask(3600000L)
/*     */     {
/*     */       public void doTask() throws Throwable {
/*  51 */         Iterator<Entry<String, Map<String, Object>>> allAuthTokens = AuthTokenServiceImpl.this.authTokenCache.entrySet().iterator();
/*  52 */         long now = System.currentTimeMillis();
/*  53 */         long gap = SystemConfig.getLong("appuser.expire.time");
/*  54 */         Map<String, Object> authToken = null;
/*  55 */         Entry<String, Map<String, Object>> entry = null;
/*  56 */         while (allAuthTokens.hasNext()) {
/*  57 */           entry = (Entry)allAuthTokens.next();
/*  58 */           authToken = (Map<String, Object>)entry.getValue();
/*  59 */           if (now - ((Long)authToken.get("K_REFRESH_TIME")).longValue() > gap) {
/*  60 */             AuthTokenServiceImpl.this.authTokenCache.remove(entry.getKey());
/*     */           }
/*     */         }
/*     */       }
/*     */     }.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/*  73 */     if (this.timeoutAuthTokenCleaner != null) {
/*  74 */       this.timeoutAuthTokenCleaner.stop();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<String, Object> checkAndGetUser(String tokenId)
/*     */   {
/*  82 */     Map<String, Object> user = (Map<String, Object>)this.authTokenCache.get(tokenId);
/*     */     
/*  84 */     if (user != null)
/*     */     {
/*  86 */       user.put("K_REFRESH_TIME", Long.valueOf(System.currentTimeMillis()));
/*     */     }
/*     */     
/*  89 */     return user;
/*     */   }
/*     */   
/*     */ 
/*     */   public String createTokenId(Map<String, Object> user)
/*     */   {
/*  95 */     String mobile = (String)user.get("mobile");
/*  96 */     String rawTokenId = mobile + "_" + DateUtil.getDate("yyyyMMddHHmmss") + "_" + EncryptUtil.getRandPwd(10);
/*     */     
/*     */ 
/*  99 */     String tokenId = EncryptUtil.toMd5(rawTokenId);
/*     */     
/*     */ 
/* 102 */     user.put("K_REFRESH_TIME", Long.valueOf(System.currentTimeMillis()));
/*     */     
/* 104 */     this.authTokenCache.put(tokenId, user);
/*     */     
/* 106 */     return tokenId;
/*     */   }
/*     */ }


/* Location:              E:\新建文件夹\tomcat_lbs_pt\lbsapp\ROOT\WEB-INF\lib\lbs.portal.jar!\com\app\lbs\portal\app\service\impl\AuthTokenServiceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */