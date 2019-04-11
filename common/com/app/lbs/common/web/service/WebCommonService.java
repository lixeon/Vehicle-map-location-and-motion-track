/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.lbs.common.web.service;

import java.util.Map;

public abstract interface WebCommonService
{
  public abstract Map<String, Object> getCityInfoById(String paramString);
}
