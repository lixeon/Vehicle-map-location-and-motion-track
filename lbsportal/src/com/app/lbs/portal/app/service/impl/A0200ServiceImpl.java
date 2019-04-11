/*    */ package com.app.lbs.portal.app.service.impl;
/*    */ import com.app.lbs.portal.app.service.AbstractClientAppService;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class A0200ServiceImpl
/*    */   extends AbstractClientAppService
/*    */ {
/* 25 */   private static final Logger LOG = Logger.getLogger(A0200ServiceImpl.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Map<String, Object> execute(Map<String, Object> params)
/*    */   {
/* 32 */     Map<String, Object> user = (Map<String, Object>)params.get("K_P_AUTHUSER");
/* 33 */     String mobile = (String)user.get("mobile");
/*    */     
/* 35 */     Map<String, Object> args = new HashMap<String, Object>();
/* 36 */     args.put("configKey", "USER");
/* 37 */     args.put("ownerTel", mobile);
/* 38 */     args.put("configKey_login", "LOGIN");
/* 39 */     List<Map<String, Object>> terminals = getCommonDao().namingQueryForList("sql_app_getTerminalByOwner", args);
/* 40 */     if (terminals == null) {
/* 41 */       LOG.info("no terminal found by " + mobile);
/* 42 */       terminals = new ArrayList<Map<String, Object>>();
/*    */     }
/*    */     
/* 45 */     Map<String, Object> output = new HashMap<String, Object>();
/*    */     
/* 47 */     output.put("terminals", terminals);
/*    */     
/* 49 */     return output;
/*    */   }
/*    */   
/*    */   public String serviceKey()
/*    */   {
/* 54 */     return "0200";
/*    */   }
/*    */   
/*    */   protected boolean isAuth()
/*    */   {
/* 59 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\新建文件夹\tomcat_lbs_pt\lbsapp\ROOT\WEB-INF\lib\lbs.portal.jar!\com\app\lbs\portal\app\service\impl\A0200ServiceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */