/*
 * lbspt
 */
package com.app.lbs.portal.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.app.lbs.common.CodeConst;
import com.app.lbs.common.exception.CommonBusinessException;
import com.app.lbs.common.web.service.impl.WebCommonServiceImpl;
import com.app.lbs.portal.web.service.LoginoutService;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.EncryptUtil;

/**
 * User Login and Logout Service Implementation.
 *
 * @author colen
 *
 */
public class LoginoutServiceImpl extends WebCommonServiceImpl implements LoginoutService {
    
    /** logger */
    private static final Logger LOG = Logger.getLogger(LoginoutServiceImpl.class);
    

    @Override
    public void doLoginCheck(Map<String, Object> params) {
        
        String loginType = (String) params.get("login_type");
      
        if (CodeConst.LOGIN_TYPE_TMN.equals(loginType)) {
            doTerminalLogin(params);
        } else if (CodeConst.LOGIN_TYPE_MGR.equals(loginType)) {
            doManagerLogin(params);
        } else {
            LOG.error("unknow login_type " + loginType);
        }
    }


    private void doManagerLogin(Map<String, Object> params) {
        String uid = (String) params.get("uid");
        // get user info
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("uid", uid);
        Map<String, Object> user = getCommonDao().namingQueryForMap("sql_portal_getUserById", args);
   if (user == null) {
     throw new CommonBusinessException("对不起，找不到您输入的用户名！");
   }
        String pwd = (String) params.get("password");
        String ePwd = EncryptUtil.toSHA256(pwd);
        if (user != null) {
            if (!ePwd.equals(user.get("password"))) {
                throw new CommonBusinessException("对不起，您输入的用户名或密码不正确！");
            }
        }
        user.put("login_type", CodeConst.LOGIN_TYPE_MGR);
        params.put("user", user);
    }


    private void doTerminalLogin(Map<String, Object> params) {
        String uid = (String) params.get("uid");
        // get user info
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("terminalName", uid);
        args.put("terminalSerialNo", uid);
        Map<String, Object> user = getCommonDao().namingQueryForMap("sql_portal_loadTerminalInfoByTmnId", args);
        if (user == null) {
            throw new CommonBusinessException("对不起，您输入的用户名或密码不正确！"); 
        }

        String pwd = (String) params.get("password");
        String ePwd = EncryptUtil.toSHA256(pwd);
        if (!ePwd.equals(user.get("password"))) {
            throw new CommonBusinessException("对不起，您输入的用户名或密码不正确！");
        }
        
        if (!CodeConst.TERMINAL_SERVICE_ENABLE.equals(user.get("status"))) {
            throw new CommonBusinessException("对不起，该用户已经被禁用，请与管理员联系！");
        }
        
        String today = DateUtil.getDate(DateUtil.DATE_FORMAT_F);
        if (today.compareTo(user.get("service_end_date").toString()) > 0) {
            throw new CommonBusinessException("对不起，该用户服务期限已到，请与管理员联系！");
        }

        user.put("login_type", CodeConst.LOGIN_TYPE_TMN);
        params.put("user", user);
    }


    @Override
    @Transactional
    public void doModifyPwd(Map<String, Object> params) {
        String uid = (String) params.get("uid");
        // get user info
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("uid", uid);
        List<Map<String, Object>> users = getCommonDao().namingQueryForList("sql_portal_getUserById", args);
        if (users == null || users.isEmpty()) {
            LOG.error("can not find user by userId = " + uid);
            throw new CommonBusinessException("对不起，需要修改的用户不存在！");
        }

        Map<String, Object> user = users.get(0);
        String oldPwd = (String) params.get("oldPwd");
        String newPwd = (String) params.get("newPwd");
        
        String eOldPwd = EncryptUtil.toSHA256(oldPwd);
        if (!eOldPwd.equals(user.get("password"))) {
            throw new CommonBusinessException("对不起，您输入旧密码不正确！");
        }
        
        args.put("password", EncryptUtil.toSHA256(newPwd));
        // update password
        getCommonDao().namingUpdate("sql_portal_updateUserById", args);
        
    }


    @Override
    @Transactional
    public void doModifyTerminalPwd(Map<String, Object> params) {
        String terminalSerialNo = (String) params.get("terminalSerialNo");
        // get user info
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("terminalSerialNo", terminalSerialNo);
        Map<String, Object> user = getCommonDao().namingQueryForMap("sql_portal_loadTerminalInfoByTmnId", args);
        if (user == null) {
            LOG.error("can not find user by userId = " + terminalSerialNo);
            throw new CommonBusinessException("对不起，需要修改的用户不存在！");
        }

        String oldPwd = (String) params.get("oldPwd");
        String newPwd = (String) params.get("newPwd");
        
        String eOldPwd = EncryptUtil.toSHA256(oldPwd);
        if (!eOldPwd.equals(user.get("password"))) {
            throw new CommonBusinessException("对不起，您输入旧密码不正确！");
        }
        
        args.put("password", EncryptUtil.toSHA256(newPwd));
        // update password
        getCommonDao().namingUpdate("sql_portal_updateTerminalById", args);
    }

}
