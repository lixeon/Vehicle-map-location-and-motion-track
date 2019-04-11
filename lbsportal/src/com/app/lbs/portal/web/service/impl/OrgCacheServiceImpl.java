 package com.app.lbs.portal.web.service.impl;
 
 import com.app.lbs.common.web.service.impl.WebCommonServiceImpl;
 import com.app.lbs.portal.web.service.OrgCacheService;
 import com.app.platform.base.task.SimpleTimerTask;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 
 public class OrgCacheServiceImpl
   extends WebCommonServiceImpl
   implements OrgCacheService
 {
   private volatile Map<String, Map<String, Object>> groupCache = null;
   
 
   private volatile Map<String, List<String>> terminalOrgCache = null;
   
   private SimpleTimerTask refresher = null;
   
 
 
 
   public void initialize()
   {
     this.refresher = new SimpleTimerTask(86400000L)
     {
       public void doTask() throws Throwable {
         OrgCacheServiceImpl.this.doRefresh();
       }
     }.start();
   }
   
 
 
 
 
   public void doRefresh()
   {
     loadGroup();
     loadTerminalOrgRL();
   }
   
   public void destroy()
   {
     if (this.refresher != null) {
       this.refresher.stop();
     }
   }
   
   private void loadGroup()
   {
     Map<String, Map<String, Object>> tmpCache = new HashMap<String, Map<String, Object>>();
     String sql = "select ORG_ID orgId, ORG_COLOR orgColor from T_BSF_ORG where length(ORG_ID) = 12";
     
     List<Map<String, Object>> groupList = getCommonDao().getJdbcTemplate().queryForList(sql);
     for (Map<String, Object> group : groupList) {
       tmpCache.put(group.get("orgId").toString(), group);
     }
     
 
     this.groupCache = tmpCache;
   }
   
 
   private void loadTerminalOrgRL()
   {
     Map<String, List<String>> tmpCache = new HashMap<String, List<String>>();
     
 
     String sql = "select ORG_ID orgId, TERMINAL_SERIAL_NO terminalSerialNo from T_ORG_TMN_RL where length(ORG_ID) = 12";
     
     List<Map<String, Object>> terminalOrgList = getCommonDao().getJdbcTemplate().queryForList(sql);
     List<String> orgList = null;
     String terminalSerialNo = null;
     for (Map<String, Object> terminalOrg : terminalOrgList) {
       terminalSerialNo = terminalOrg.get("terminalSerialNo").toString();
       if (tmpCache.containsKey(terminalSerialNo)) {
         orgList = (List<String>)tmpCache.get(terminalSerialNo);
         orgList.add(terminalOrg.get("orgId").toString());
       } else {
         orgList = new ArrayList<String>();
         orgList.add(terminalOrg.get("orgId").toString());
         tmpCache.put(terminalSerialNo, orgList);
       }
     }
     
 
     this.terminalOrgCache = tmpCache;
   }
   
 
 
   public Map<String, Object> getCacheGroupInfo(String groupId)
   {
     return (Map<String, Object>)this.groupCache.get(groupId);
   }
   
   public List<String> getTerminalGroup(String terminalSerialNo)
   {
     return (List<String>)this.terminalOrgCache.get(terminalSerialNo);
   }
 }

