 package com.app.platform.base.dao;
 
 import com.app.platform.base.exception.BaseSystemException;
 import com.app.platform.base.utils.CommonUtil;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 public class BaseDaoEx extends DaoSupportEx implements NamingDaoSupport
 {
   public List<Map<String, Object>> namingQueryForList(String sqlId, Map<String, Object> args)
   {
     Map<String, Object> params = extractValidParams(args, true);
     String bindedSql = getSql(sqlId, params);
     return queryForList(bindedSql, params);
   }
   
   private Map<String, Object> extractValidParams(Map<String, Object> args, boolean filterBlank) {
     Map<String, Object> params = new HashMap<String, Object>();
     if ((args == null) || (args.isEmpty())) {
       return params;
     }
     
     Object value = null;
     for (Map.Entry<String, Object> e : args.entrySet()) {
       value = e.getValue();
       if ((value != null) && (
       
 
         (!filterBlank) || 
         (!(value instanceof String)) || (!"".equals(value.toString().trim()))))
       {
 
 
         params.put(e.getKey(), value); }
     }
     return params;
   }
   
   public int namingUpdate(String sqlId, Map<String, Object> args)
   {
     Map<String, Object> params = extractValidParams(args, false);
     return update(getSql(sqlId, params), params);
   }
   
 
   public int namingUpdate(String sqlId, Map<String, Object> args, boolean affectNull)
   {
     Map<String, Object> params = extractValidParams(args, !affectNull);
     return update(getSql(sqlId, params), params);
   }
  
 
   private String getSql(String sqlId, Map<String, Object> params)
   {
     NamingSql nmSql = NamingSqlFactory.getNamingSql(sqlId);
     if (nmSql == null) {
       throw new BaseSystemException("Can not find SQL by ID (" + sqlId + ")");
     }
     return nmSql.getBindedSql(params);
   }
 
   public Map<String, Object> namingQueryForMap(String sqlId, Map<String, Object> args)
   {
     List<Map<String, Object>> list = namingQueryForList(sqlId, args);
     if (CommonUtil.isEmpty(list)) {
       return null;
     }
     return (Map<String, Object>)list.get(0);
   }
 
   public Page namingQueryForPage(String sqlId, PageCond cond)
   {
     Map<String, Object> params = extractValidParams(cond.getQps(), true);
     String bindedSql = getSql(sqlId, params);
 
     params.put("t_t_p_g_n_max_count", Integer.valueOf(cond.getLimit() * 200));
     StringBuilder bud = new StringBuilder("select count(1) as totalCount from (");
     bud.append(bindedSql);
     bud.append(" limit :t_t_p_g_n_max_count) t_t_p_g_n");
     
     Integer totalCount = Integer.valueOf(queryForInt(bud.toString(), params));
     
 
     bud.delete(0, bud.length());
     bud.append(bindedSql);
     bud.append(" limit :t_t_p_g_n_row_start, :t_t_p_g_n_page_limit");
     params.put("t_t_p_g_n_row_start", Integer.valueOf(cond.getStart()));
     params.put("t_t_p_g_n_page_limit", Integer.valueOf(cond.getLimit()));
     
 
     List<Map<String, Object>> list = queryForList(bud.toString(), params);
 
     Page page = new Page();
     page.setTotalCount(totalCount.intValue());
     page.setResultList(list);
     
     return page;
   }
 }
