 package com.app.lbs.portal.web.controller;
 import com.app.lbs.common.facade.RemoteInvokeFacade;
 import com.app.lbs.common.remote.CallRequest;
 import com.app.lbs.common.remote.CallResponse;
 import com.app.lbs.common.web.controller.WebCommonController;
 import com.app.lbs.portal.web.service.OrgCacheService;
 import com.app.platform.base.dao.Page;
 import com.app.platform.base.dao.PageCond;
 import com.app.platform.base.utils.CommonUtil;
 import com.app.platform.base.utils.DateUtil;
 import com.app.platform.base.utils.StringUtil;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import javax.annotation.Resource;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.log4j.Logger;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.servlet.ModelAndView;
 
 @Controller
 public class TerminalDistributionController
   extends WebCommonController
 {
   private static final Logger LOG = Logger.getLogger(TerminalDistributionController.class);

   @RequestMapping({"terminal_distb/open.shtm"})
   public ModelAndView open(HttpServletRequest request, HttpServletResponse resp)
   {
     LOG.info("open terminal distribute page start...");
     
     ModelAndView mv = createOpenPageMV(request);
     
 
 
     String cityId = "287";
     
 
     Map<String, Object> city = getCityInfoById(cityId);
     
     mv.addObject("city", city);
     
     mv.addObject("server_time", DateUtil.getDate("yyyyMMddHHmmss"));
     
     String mapType = request.getParameter("mapType");
     mv.addObject("mapCnf", getMapCnf(city.get("city_id").toString(), mapType));
     
 
     List<Map<String, Object>> companyList = getCompanyListByRole(request);
     mv.addObject("company_list", companyList);
     
 
     mv.setViewName("terminal/p_tmn_distribution.jsp");
     
     LOG.info("open terminal distribute page end.");
     return mv;
   }
 
 
   @RequestMapping({"terminal_distb/loadgrplist.shtm"})
   public void loadGroupList(HttpServletRequest request, HttpServletResponse response)
   {
     String companyId = request.getParameter("companyId");
     
     if (LOG.isDebugEnabled())
     {
       LOG.debug("load group list, companyId:" + companyId);
     }
     
     List<Map<String, Object>> groupList = null;
     if ("0000".equals(companyId)) {
       groupList = new ArrayList<Map<String, Object>>();
     } else {
       groupList = getOrgListByParentOrg(companyId);
     }
     
 
     Map<String, Object> output = getDefaultResult();
     output.put("groupList", groupList);
     
     outputJSON(response, output);
   }
  
   private List<Map<String, Object>> getCompanyListByRole(HttpServletRequest request)
   {
     String orgId = getCookieValue(request, "org", false);
     String roleId = getCookieValue(request, "role", false);
     List<Map<String, Object>> companyList = null;
     if ("1".equals(roleId)) {
       companyList = getOrgListByParentOrg("0000");
       Map<String, Object> all = new HashMap<String, Object>();
       all.put("org_id", "0000");
       all.put("org_name", "--全部--");
       companyList.add(0, all);
     } else if ("50".equals(roleId)) {
       companyList = getCompanyListByOrgId(orgId);
     } else {
       LOG.error("Error Role Open this Page!");
     }
     
     return companyList;
   }
   
   private List<Map<String, Object>> getCompanyListByOrgId(String orgId)
   {
     Map<String, Object> qps = new HashMap<String, Object>();
     qps.put("orgId", orgId);
     Map<String, Object> company = getCommonDao().namingQueryForMap("sql_manager_queryCompanyList", qps);
     List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
     companyList.add(company);
     return companyList;
   }
   
   private List<Map<String, Object>> getOrgListByParentOrg(String parentOrgId) {
     Map<String, Object> qps = new HashMap<String, Object>();
     qps.put("page", "1");
     qps.put("parentOrgId", parentOrgId);
     PageCond cond = new PageCond(qps, 0, 9999);
     Page page = getCommonDao().namingQueryForPage("sql_manager_queryCompanyList", cond);
     
     List<Map<String, Object>> orgList = page.getResultList();
     return orgList;
   }
 
 
   @RequestMapping({"terminal/map_distribution.shtm"})
   public void showDistribution(HttpServletRequest req, HttpServletResponse resp)
   {
     LOG.info("show distribution begin...");
     
     Map<String, Object> reqObj = getReqParams(req);
     
     Map<String, Object> qps = new HashMap<String, Object>();
     String orgId = reqObj.get("orgId").toString();
     qps.put("orgId", orgId + "%");
     qps.put("login_status_key", "LOGIN");
     qps.put("onlineStatus", "1");
     
 
     double leftBottonLat = Double.valueOf(reqObj.get("leftBottomLat").toString()).doubleValue();
     double leftBottomLng = Double.valueOf(reqObj.get("leftBottomLng").toString()).doubleValue();
     double rightTopLat = Double.valueOf(reqObj.get("rightTopLat").toString()).doubleValue();
     double rightTopLng = Double.valueOf(reqObj.get("rightTopLng").toString()).doubleValue();
     
     qps.put("leftBottomLat", Double.valueOf(leftBottonLat));
     qps.put("leftBottomLng", Double.valueOf(leftBottomLng));
     qps.put("rightTopLat", Double.valueOf(rightTopLat));
     qps.put("rightTopLng", Double.valueOf(rightTopLng));
     
 
     qps.put("cLat", Double.valueOf((leftBottonLat + rightTopLat) / 2.0D));
     qps.put("cLng", Double.valueOf((leftBottomLng + rightTopLng) / 2.0D));
     
 
     List<Map<String, Object>> terminals = getCommonDao().namingQueryForList("sql_portal_queryInBoundTerminals", qps);
     
     if (LOG.isInfoEnabled()) {
       if (CommonUtil.isEmpty(terminals)) {
         LOG.info("terminal size is, 0");
       } else {
         LOG.info("terminal size is, " + terminals.size());
       }
     }
     
 
     String color = null;
     if (12 == orgId.length()) {
       color = getDispColor(orgId);
     }
     
     fillDispInfo(terminals, color);
     
     Map<String, Object> output = getDefaultResult();
     output.put("terminals", terminals);
     
 
     outputJSON(resp, output);
     
     LOG.info("show distribution end.");
   }
   
   private String getDispColor(String orgId)
   {
     String color = null;
     Map<String, Object> group = this.orgCacheService.getCacheGroupInfo(orgId);
     if (group != null) {
       color = group.get("orgColor").toString();
     } else {
       color = "FFFFFF,000000";
     }
     return color;
   }
 
   @SuppressWarnings("unchecked")
void fillDispInfo(List<Map<String, Object>> terminals, String color)
   {
     CallRequest request = new CallRequest();
     request.setActType("001");
     List<String> terminalIdList = new ArrayList<String>(terminals.size());
     for (Map<String, Object> terminal : terminals) {
       terminalIdList.add(terminal.get("terminal_serial_no").toString());
     }
     request.set("terminalIdList", terminalIdList);
     CallResponse response = RemoteInvokeFacade.call(request);
     
     List<Map<String, Object>> terminalRunInfoList = null;
     if ((response != null) && (response.getData() != null)) {
       terminalRunInfoList = (List<Map<String, Object>>)response.getData().get("terminalRunInfoList");
     }
     
     Map<String, Object> terminal = null;
     Map<String, Object> terminalRunInfo = null;
     String myColor = null;
     for (int i = 0; i < terminals.size(); i++) {
       terminal = (Map<String, Object>)terminals.get(i);
       if (terminalRunInfoList != null) {
         terminalRunInfo = (Map<String, Object>)terminalRunInfoList.get(i);
       }
       myColor = color;
       if (StringUtil.isTrimEmpty(myColor)) {
         List<String> orgList = this.orgCacheService.getTerminalGroup(terminal.get("terminal_serial_no").toString());
         if ((!CommonUtil.isEmpty(orgList)) && (orgList.size() == 1)) {
           myColor = getDispColor((String)orgList.get(0));
         } else {
           myColor = "FFFFFF,000000";
         }
       }
       terminal.put("color", myColor);
       
       if ((terminalRunInfo != null) && (terminalRunInfo.get("hbTime") != null)) {
         terminal.put("tmn_status", terminalRunInfo.get("status"));
         terminal.put("speed", terminalRunInfo.get("speed"));
         terminal.put("gps", terminalRunInfo.get("gps"));
         terminal.put("gsm",terminalRunInfo.get("rssi"));
         terminal.put("last_update_time", terminalRunInfo.get("hbTime"));
       } else {
         terminal.put("tmn_status", "NODATA");
         terminal.put("speed", "");
         terminal.put("gps", "无数据");
         terminal.put("gsm", "无数据");
         terminal.put("last_update_time", "无数据");
       }
     }
   }
 
   @Resource(name="pub_OrgCacheService")
   private OrgCacheService orgCacheService = null;
 }

