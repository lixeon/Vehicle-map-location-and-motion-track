/*
 * LBSAPP
 */
package com.app.lbs.common.service.pub;

import com.app.lbs.common.minigps.LocRadiusPoiResponse;

/**
 * Cell Info Service
 *
 * @author colen
 *
 */
public interface CellInfoService {

    /**
     * Get LngLat From CellID
     *
     * @param cellId
     * @return
     */
	LocRadiusPoiResponse getLngLatFromCellId(String cellId,String wifi);

}
