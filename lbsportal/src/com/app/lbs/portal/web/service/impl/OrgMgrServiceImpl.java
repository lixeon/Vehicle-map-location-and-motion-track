/*
 * LBSAPP
 */
package com.app.lbs.portal.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.app.lbs.common.CodeConst;
import com.app.lbs.common.CommonConsts;
import com.app.lbs.common.exception.CommonBusinessException;
import com.app.lbs.common.web.service.impl.WebCommonServiceImpl;
import com.app.lbs.portal.web.service.OrgMgrService;
import com.app.platform.base.dao.Page;
import com.app.platform.base.dao.PageCond;
import com.app.platform.base.utils.CommonUtil;
import com.app.platform.base.utils.ConvertUtil;
import com.app.platform.base.utils.EncryptUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * Organization Manager Service
 *
 * @author colen
 *
 */
public class OrgMgrServiceImpl extends WebCommonServiceImpl implements OrgMgrService {

    /** logger */
    public static final Logger LOG = Logger.getLogger(OrgMgrServiceImpl.class);


    @Override
    public Map<String, Object> queryCompanyList(Map<String, Object> params) {
        
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        qps.put("parentOrgId", CodeConst.TOP_GRPID);
        
        PageCond cond = new PageCond(qps,
                Integer.valueOf((String) params.get("start")),
                Integer.valueOf((String) params.get("limit")));
        
        Page page = getCommonDao().namingQueryForPage("sql_manager_queryCompanyList", cond);
        
        List<Map<String, Object>> compList = page.getResultList();
        if (!CommonUtil.isEmpty(compList)) {
            qps.clear();
            for (Map<String, Object> comp : compList) {
                Long cnt = getCommonDao().queryForLong("select count(1) from T_ORG_TMN_RL where ORG_ID = ?", comp.get("org_id"));
                comp.put("terminal_num", cnt.toString());
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalCount", page.getTotalCount());
        resultMap.put("companys", page.getResultList());

        return resultMap;
    }


    @Override
    public String getNextOrgId(String parentOrgId) {

        Map<String, Object> qps = new HashMap<>();
        qps.put("parentOrgId", parentOrgId);
        List<Map<String, Object>> resultList
            = getCommonDao().queryForList("select MAX(ORG_ID + 0) + 1 as nextval from T_BSF_ORG where PARENT_ORG_ID =:parentOrgId", qps);

        String nextOrgId = null;
        Map<String, Object> resultMap = resultList.get(0);
        if (resultMap.get("nextval") == null) {
            nextOrgId = parentOrgId + "0001";
        } else {
            long nextG = ((Double) resultMap.get("nextval")).longValue();
            nextOrgId = ConvertUtil.padd(nextG, parentOrgId.length() + 4);
        }
        return nextOrgId;
    }



    @Override
    @Transactional
    public void addCompany(Map<String, Object> params) {

        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        
        Map<String, Object> org = getCommonDao().namingQueryForMap("sql_manager_queryOrg", qps);
        if (org != null) {
            throw new CommonBusinessException("该企业组织ID已经存在！");
        }

        Map<String, Object> user = getCommonDao().namingQueryForMap("sql_portal_getUserById", qps);
        if (user != null) {
            throw new CommonBusinessException("管理员ID已经存在！");
        }

        // parent orgid is 'top group'
        qps.put("parentOrgId", CodeConst.TOP_GRPID);

        // add group company
        getCommonDao().namingUpdate("sql_manager_addOrg", qps);


        // prepare user info
        // uid, name
        // password
        qps.put("password", EncryptUtil.toSHA256(params.get("password").toString()));
        // role as 'company manager'
        qps.put("roleId", CodeConst.ROLE_COMAPANY);
        // orgid, mobile, addr, company
        // creator
        qps.put("creator", params.get("login_user_id"));
        // create date, update date
        Date now = new Date();
        qps.put("createDate", now);
        qps.put("updateDate", now);

        // add user
        getCommonDao().namingUpdate("sql_manager_addUser", qps);

    }


    @Override
    public Map<String, Object> getCompanyById(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        
        Map<String, Object> company = getCommonDao().namingQueryForMap("sql_manager_queryCompanyList", qps);

        return company;
    }


    @Override
    @Transactional
    public void modifyCompany(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        
        // check if manager changed
        String uid = (String) params.get("uid");
        String uidOld = (String) params.get("uid_old");

        if (!uid.equals(uidOld)) {
            Map<String, Object> user = getCommonDao().namingQueryForMap("sql_portal_getUserById", qps);
            if (user != null) {
                throw new CommonBusinessException("管理员ID已经存在！");
            }
        }

        // modify company
        getCommonDao().namingUpdate("sql_manager_updateOrg", qps);
        

        if (uid.equals(uidOld)) {
            // just modify manager message.
            
            // reset password
            String resetPwd = (String) params.get("resetPwd");
            if (CommonConsts.CHECK_ON.equals(resetPwd)) {
                qps.put("password", EncryptUtil.toSHA256(params.get("password").toString()));
            } else {
                // remove password key, don't modify password.
                qps.remove("password");
            }
            
            getCommonDao().namingUpdate("sql_portal_updateUserById", qps, true);

        } else {
            // delete old manager
            Map<String, Object> oldInfo = new HashMap<>();
            oldInfo.put("uid", uidOld);
            getCommonDao().namingUpdate("sql_portal_deleteUserById", oldInfo);

            // add a new one
            // prepare user info
            // uid, name
            // password
            qps.put("password", EncryptUtil.toSHA256(params.get("password").toString()));
            // role as 'company manager'
            qps.put("roleId", CodeConst.ROLE_COMAPANY);
            // orgid, mobile, addr, company
            // creator
            qps.put("creator", params.get("login_user_id"));
            // create date, update date
            Date now = new Date();
            qps.put("createDate", now);
            qps.put("updateDate", now);

            // add user
            getCommonDao().namingUpdate("sql_manager_addUser", qps);
        }
        
    }


    @Override
    @Transactional
    public void deleteCompany(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        
        int tmnCnt = getCommonDao().queryForInt("select count(1) from T_ORG_TMN_RL where ORG_ID like ?", qps.get("orgId") + "%");
        if (tmnCnt != 0) {
            throw new CommonBusinessException("该企业组织已经关联了终端或群组，不能删除！如需确定要删除企业组织，请先删除其名下的终端。");
        }

        // delete child T_BSF_ORG
        getCommonDao().update("delete from T_BSF_ORG where PARENT_ORG_ID=:orgId", qps);

        // delete T_BSF_ORG
        getCommonDao().update("delete from T_BSF_ORG where ORG_ID=:orgId", qps);
        
        // delete T_USER
        getCommonDao().update("delete from T_USER where UID=:uid", qps);
 		getCommonDao().update("delete from T_USER where ORG_ID like ?", new Object[] { qps.get("orgId") + "%" });
    }


    @Override
    public Map<String, Object> queryGroupList(Map<String, Object> params) {
        
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        qps.put("parentOrgId", params.get("companyId"));
        
        PageCond cond = new PageCond(qps,
                Integer.valueOf((String) params.get("start")),
                Integer.valueOf((String) params.get("limit")));
        
        Page page = getCommonDao().namingQueryForPage("sql_manager_queryCompanyList", cond);
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalCount", page.getTotalCount());
        resultMap.put("grouplist", page.getResultList());

        return resultMap;
    }


    @Override
    @Transactional
    public void addGroup(Map<String, Object> params) {
        
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        
        Map<String, Object> org = getCommonDao().namingQueryForMap("sql_manager_queryOrg", qps);
        if (org != null) {
            throw new CommonBusinessException("该分组ID已经存在！");
        }

        Map<String, Object> user = getCommonDao().namingQueryForMap("sql_portal_getUserById", qps);
        if (user != null) {
            throw new CommonBusinessException("分组管理员ID已经存在！");
        }

        // parent orgid is 'top group'
        qps.put("parentOrgId", params.get("companyId"));

        // add group company
        getCommonDao().namingUpdate("sql_manager_addOrg", qps);


        // prepare user info
        // uid, name
        // password
        qps.put("password", EncryptUtil.toSHA256(params.get("password").toString()));
        // role as 'company manager'
        qps.put("roleId", CodeConst.ROLE_GROUP_OWNER);
        // orgid, mobile, addr, company
        // creator
        qps.put("creator", params.get("login_user_id"));
        // create date, update date
        Date now = new Date();
        qps.put("createDate", now);
        qps.put("updateDate", now);

        // add user
        getCommonDao().namingUpdate("sql_manager_addUser", qps);
    }


    @Override
    @Transactional
    public void modifyGroup(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        
        // check if manager changed
        String uid = (String) params.get("uid");
        String uidOld = (String) params.get("uid_old");

        if (!uid.equals(uidOld)) {
            Map<String, Object> user = getCommonDao().namingQueryForMap("sql_portal_getUserById", qps);
            if (user != null) {
                throw new CommonBusinessException("管理员ID已经存在！");
            }
        }

        // modify company
        getCommonDao().namingUpdate("sql_manager_updateOrg", qps);
        

        if (uid.equals(uidOld)) {
            // just modify manager info.
            
            // reset password
            String resetPwd = (String) params.get("resetPwd");
            if (CommonConsts.CHECK_ON.equals(resetPwd)) {
                qps.put("password", EncryptUtil.toSHA256(params.get("password").toString()));
            } else {
                // remove password key, don't modify password.
                qps.remove("password");
            }
            
            getCommonDao().namingUpdate("sql_portal_updateUserById", qps, true);

        } else {
            // delete old manager
            Map<String, Object> oldInfo = new HashMap<>();
            oldInfo.put("uid", uidOld);
            getCommonDao().namingUpdate("sql_portal_deleteUserById", oldInfo);

            // add a new one
            // prepare user info
            // uid, name
            // password
            qps.put("password", EncryptUtil.toSHA256(params.get("password").toString()));
            // role as 'company manager'
            qps.put("roleId", CodeConst.ROLE_GROUP_OWNER);
            // orgid, mobile, addr, company
            // creator
            qps.put("creator", params.get("login_user_id"));
            // create date, update date
            Date now = new Date();
            qps.put("createDate", now);
            qps.put("updateDate", now);

            // add user
            getCommonDao().namingUpdate("sql_manager_addUser", qps);
        }
    }


    @Override
    public void queryBindedTerminalList(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();

        qps.put("user_mobile_key", CommonConsts.TMN_CFG_USER);
        
        qps.put("orgId", params.get("parentOrgId"));
        // query company terminals
        List<Map<String, Object>> tmnListCompany = makeTreeList(getCommonDao().namingQueryForList("sql_manager_queryTerminalList", qps));

        qps.put("orgId", params.get("orgId"));
        // query group terminals
        List<Map<String, Object>> tmnListGroup = makeTreeList(getCommonDao().namingQueryForList("sql_manager_queryTerminalList", qps));

        // store
        params.put("tmn_list_company", tmnListCompany);
        params.put("tmn_list_group", tmnListGroup);
    }

    
    private List<Map<String, Object>> makeTreeList(List<Map<String, Object>> list) {
        List<Map<String, Object>> newlist = new ArrayList<>();
        Map<String, Object> newObj = null;
        for (Map<String, Object> rawObj : list) {
            newObj = new HashMap<>();
            newObj.put("id", rawObj.get("terminal_serial_no"));
            newObj.put("text", rawObj.get("terminal_name") + "(" + rawObj.get("terminal_serial_no") + ")");
            newObj.put("owner", rawObj.get("owner"));
            newObj.put("leaf", true);

            newlist.add(newObj);
        }
        return newlist;
    }


    @Override
    @Transactional
    public void bindTerminal(Map<String, Object> params) {
        String orgId = (String) params.get("orgId");
        String tmnIds = (String) params.get("tmnIds");
        String[] tmnIdArr = null;
        if (!StringUtil.isTrimEmpty(tmnIds)) {
            tmnIdArr = tmnIds.split(",");
        }

        // delete old relation.
        getCommonDao().update("delete from T_ORG_TMN_RL where ORG_ID=?", orgId);

        Map<String, Object> qps = null;
        if (tmnIdArr != null) {
            for (String tmnId : tmnIdArr) {
                qps = new HashMap<>();
                qps.put("orgId", orgId);
                qps.put("terminalSerialNo", tmnId);
                
                getCommonDao().namingUpdate("sql_manager_addOrgTerminalRL", qps);
            }
        }
    }


    @Override
    @Transactional
    public void deleteGroup(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);

        // delete child T_ORG_TMN_RL
        getCommonDao().update("delete from T_ORG_TMN_RL where ORG_ID=:orgId", qps);

        // delete T_BSF_ORG
        getCommonDao().update("delete from T_BSF_ORG where ORG_ID=:orgId", qps);

        // delete T_USER
        getCommonDao().update("delete from T_USER where UID=:uid", qps);
    }

}
