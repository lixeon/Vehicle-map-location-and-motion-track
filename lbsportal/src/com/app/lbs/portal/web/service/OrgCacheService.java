package com.app.lbs.portal.web.service;

import java.util.List;
import java.util.Map;

public abstract interface OrgCacheService
{
  public abstract Map<String, Object> getCacheGroupInfo(String paramString);
  
  public abstract List<String> getTerminalGroup(String paramString);
  
  public abstract void doRefresh();
}

