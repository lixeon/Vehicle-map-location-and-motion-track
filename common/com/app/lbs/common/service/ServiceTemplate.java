package com.app.lbs.common.service;

public abstract interface ServiceTemplate<T, R>
{
  public abstract R execute(T paramT);
}


/* Location:              E:\新建文件夹\tomcat_lbs_pt\lbsapp\ROOT\WEB-INF\lib\lbs.common.jar!\com\app\lbs\common\service\ServiceTemplate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */