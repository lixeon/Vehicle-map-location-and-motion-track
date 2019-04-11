/*     */ package com.app.lbs.portal.app.service.impl;
/*     */ 
/*     */ import com.app.lbs.common.facade.RemoteInvokeFacade;
/*     */ import com.app.lbs.common.remote.CallRequest;
/*     */ import com.app.lbs.common.remote.CallResponse;
/*     */ import com.app.lbs.portal.app.service.AbstractClientAppService;
/*     */ import com.app.platform.base.utils.StringUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.log4j.Logger;
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
/*     */ public class A0210ServiceImpl
/*     */   extends AbstractClientAppService
/*     */ {
/*  29 */   private static final Logger LOG = Logger.getLogger(A0210ServiceImpl.class);
/*     */   
/*     */ 
/*     */ 
/*     */   protected Map<String, Object> execute(Map<String, Object> params)
/*     */   {
/*  35 */     String terminalIds = (String)params.get("terminal_ids");
/*     */     
/*  37 */     Map<String, Object> output = new HashMap<String, Object>();
/*  38 */     List<Map<String, Object>> list = null;
/*  39 */     if (StringUtil.isTrimEmpty(terminalIds)) {
/*  40 */       LOG.info("empty terminal ids!");
/*  41 */       list = new ArrayList<Map<String, Object>>();
/*     */     } else {
/*  43 */       list = getTerminalsRunningInfo(terminalIds);
/*     */     }
/*     */     
/*  46 */     output.put("terminal_infos", list);
/*  47 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */   private List<Map<String, Object>> getTerminalsRunningInfo(String terminalIds)
/*     */   {
/*  53 */     String[] myTerminalIds = terminalIds.split(",");
/*  54 */     List<String> terminalIdList = new ArrayList<String>(myTerminalIds.length);
/*  55 */     for (String terminalId : myTerminalIds) {
/*  56 */       if (!StringUtil.isTrimEmpty(terminalId)) {
/*  57 */         terminalIdList.add(StringUtil.trim(terminalId));
/*     */       }
/*     */     }
/*     */     
/*  61 */     if (terminalIdList.isEmpty()) {
/*  62 */       LOG.warn("no valid terminal id, " + terminalIds);
/*  63 */       return new ArrayList<Map<String, Object>>();
/*     */     }
/*     */     
/*  66 */     CallRequest request = new CallRequest();
/*  67 */     request.setActType("001");
/*  68 */     request.set("terminalIdList", terminalIdList);
/*  69 */     CallResponse response = RemoteInvokeFacade.call(request);
/*     */     
/*  71 */     List<Map<String, Object>> myTmnRunInfoList = new ArrayList<Map<String, Object>>(terminalIdList.size());
/*  72 */     Map<String, Object> myTmnRunInfo = null;
/*     */     String ggv;
/*  74 */     if ((response != null) && (response.getData() != null)) {
/*  75 */       List<Map<String, Object>> terminalRunInfoList = (List<Map<String, Object>>)response.getData().get("terminalRunInfoList");
/*     */       
/*  77 */       ggv = null;
/*  78 */       for (Map<String, Object> terminalRunInfo : terminalRunInfoList) {
/*  79 */         myTmnRunInfo = new HashMap<String, Object>();
/*     */         
/*  81 */         myTmnRunInfo.put("id", terminalRunInfo.get("terminalSerialNo"));
/*  82 */         myTmnRunInfo.put("status", terminalRunInfo.get("status"));
/*  83 */         myTmnRunInfo.put("speed", terminalRunInfo.get("speed"));
/*  84 */         myTmnRunInfo.put("lng", terminalRunInfo.get("lng"));
/*  85 */         myTmnRunInfo.put("lat", terminalRunInfo.get("lat"));
/*     */         
/*  87 */         ggv = (String)terminalRunInfo.get("ggv");
/*  88 */         if (StringUtil.isTrimEmpty(ggv)) {
/*  89 */           myTmnRunInfo.put("gps", "无数据");
/*  90 */           myTmnRunInfo.put("vtg", "无数据");
/*     */         } else {
/*  92 */           myTmnRunInfo.put("gps", ggv.substring(0, 1));
/*  93 */           myTmnRunInfo.put("vtg", ggv.substring(2));
/*     */         }
/*     */         
/*  96 */         myTmnRunInfo.put("ltime", terminalRunInfo.get("hbTime"));
/*     */         
/*     */ 
/*  99 */         myTmnRunInfoList.add(myTmnRunInfo);
/*     */       }
/*     */     }
/*     */     
/* 103 */     return myTmnRunInfoList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String serviceKey()
/*     */   {
/* 110 */     return "0210";
/*     */   }
/*     */   
/*     */   protected boolean isAuth()
/*     */   {
/* 115 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\鏂板缓鏂囦欢澶筡tomcat_lbs_pt\lbsapp\ROOT\WEB-INF\lib\lbs.portal.jar!\com\app\lbs\portal\app\service\impl\A0210ServiceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */