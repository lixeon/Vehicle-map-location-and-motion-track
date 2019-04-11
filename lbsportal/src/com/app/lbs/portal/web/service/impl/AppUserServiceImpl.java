/*
 * LBSAPP
 */
package com.app.lbs.portal.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.app.lbs.common.CodeConst;
import com.app.lbs.common.web.service.impl.WebCommonServiceImpl;
import com.app.lbs.portal.web.service.AppUserService;
import com.app.platform.base.dao.Page;
import com.app.platform.base.dao.PageCond;
import com.app.platform.base.utils.CommonUtil;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * App User Service
 *
 * @author luxinxian
 *
 */
public class AppUserServiceImpl extends WebCommonServiceImpl implements AppUserService {


    @Override
    public void queryAppUserList(Map<String, Object> params) {

        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);

        String stdate = (String) qps.get("createDateSt");
        if (!StringUtil.isTrimEmpty(stdate)) {
            qps.put("createDateSt", DateUtil.toDate(stdate + " 00:00:00", DateUtil.DATE_FORMAT_1));
        }

        String etdate = (String) qps.get("createDateEt");
        if (!StringUtil.isTrimEmpty(etdate)) {
            qps.put("createDateEt", DateUtil.toDate(etdate + " 23:59:59", DateUtil.DATE_FORMAT_1));
        }

        String name = (String) params.get("name");
        if (!StringUtil.isTrimEmpty(name)) {
            qps.put("name", "%" + name + "%");
        }

        // query page condition
        PageCond cond = new PageCond(qps, Integer.valueOf((String)
                params.get("start")),
                Integer.valueOf((String) params.get("limit")));

        // query for page
        Page page = getCommonDao().namingQueryForPage("sql_app_queryAppUserList", cond);
        
        if (!CommonUtil.isEmpty(page.getResultList())) {
            for (Map<String, Object> m : page.getResultList()) {
                m.put("province_name", getCdNmByValue(CodeConst.CDK_PROVINCE, m.get("province_id")));
                m.put("city_name", getCdNmByValue(CodeConst.CDK_CITY, m.get("city_id")));
            }
        }

        // hold
        params.put("totalCount", page.getTotalCount());
        params.put("appUserList", page.getResultList());

    }

}
