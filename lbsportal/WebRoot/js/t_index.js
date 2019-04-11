mini_debugger = false;
//天地图业务逻辑，因为此文件盒index.js文件一样，所以相关函数注释请看index.js
var T_Index = {
    initFinished: false,
    mkrs: [],
    pts: [],
    //不同级别和状态对应的icons
    icons: {
        zoom3_5: {//电动车3-5级地图
            accon: new TIcon('images/terminal/L1_4x4_ACCOn_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L1_4x4_ACCOn_Online.png*/, new TSize(4, 4)),
            accoff: new TIcon('images/terminal/L1_4x4_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L1_4x4_ACCOff_Online.png*/, new TSize(4, 4)),
            move: new TIcon('images/terminal/L1_4x4_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L1_4x4_ACCOff_Online.png*/, new TSize(4, 4)),
            offline: new TIcon('images/terminal/L1_4x4_ACCOff_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L1_4x4_ACCOff_Offline.png*/, new TSize(4, 4)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        Pzoom3_5: {//人3-5级地图
            accon: new TIcon('images/terminal/P_4x4_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_4x4_Online.png*/, new TSize(4, 4)),
            accoff: new TIcon('images/terminal/P_4x4_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_4x4_Online.png*/, new TSize(4, 4)),
            move: new TIcon('images/terminal/P_4x4_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_4x4_Online.png*/, new TSize(4, 4)),
            offline: new TIcon('images/terminal/P_4x4_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_4x4_Offline.png*/, new TSize(4, 4)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        Czoom3_5: {//小汽车3-5级地图
            accon: new TIcon('images/terminal/C_4x4_ACCOn_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_4x4_ACCOn_Online.png*/, new TSize(4, 4)),
            accoff: new TIcon('images/terminal/C_4x4_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_4x4_ACCOff_Online.png*/, new TSize(4, 4)),
            move: new TIcon('images/terminal/C_4x4_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_4x4_ACCOff_Online.png*/, new TSize(4, 4)),
            offline: new TIcon('images/terminal/C_4x4_ACCOff_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_4x4_ACCOff_Offline.png*/, new TSize(4, 4)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        zoom6_11: {//电动车6-11级地图
            accon: new TIcon('images/terminal/L2_16x16_ACCOn_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L2_16x16_ACCOn_Online.png*/, new TSize(16, 16)),
            accoff: new TIcon('images/terminal/L2_16x16_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L2_16x16_ACCOff_Online.png*/, new TSize(16, 16)),
            move: new TIcon('images/terminal/L2_16x16_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L2_16x16_ACCOff_Online.png*/, new TSize(16, 16)),
            offline: new TIcon('images/terminal/L2_16x16_ACCOff_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L2_16x16_ACCOff_Offline.png*/, new TSize(16, 16)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        Pzoom6_11: {//人员6-11级地图
            accon: new TIcon('images/terminal/P_16x16_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_16x16_Online.png*/, new TSize(16, 16)),
            accoff: new TIcon('images/terminal/P_16x16_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_16x16_Online.png*/, new TSize(16, 16)),
            move: new TIcon('images/terminal/P_16x16_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_16x16_Online.png*/, new TSize(16, 16)),
            offline: new TIcon('images/terminal/P_16x16_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_16x16_Offline.png*/, new TSize(16, 16)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        Czoom6_11: {//小汽车6-11级地图
            accon: new TIcon('images/terminal/C_16x16_ACCOn_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_16x16_ACCOn_Online.png*/, new TSize(16, 16)),
            accoff: new TIcon('images/terminal/C_16x16_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_16x16_ACCOff_Online.png*/, new TSize(16, 16)),
            move: new TIcon('images/terminal/C_16x16_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_16x16_ACCOff_Online.png*/, new TSize(16, 16)),
            offline: new TIcon('images/terminal/C_16x16_ACCOff_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_16x16_ACCOff_Offline.png*/, new TSize(16, 16)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        zoom12_18: {//电动车12-18级地图
            accon: new TIcon('images/terminal/L3_32x32_ACCOn_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L3_32x32_ACCOn_Online.png*/, new TSize(32, 32)),
            accoff: new TIcon('images/terminal/L3_32x32_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L3_32x32_ACCOff_Online.png*/, new TSize(32, 32)),
            move: new TIcon('images/terminal/L3_32x32_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L3_32x32_ACCOff_Online.png*/, new TSize(32, 32)),
            offline: new TIcon('images/terminal/L3_32x32_ACCOff_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/L3_32x32_ACCOff_Offline.png*/, new TSize(32, 32)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        Motorzoom12_18: {//摩托车12-18级地图
            accon: new TIcon('images/terminal/Motor_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/Motor_32x32_Online.png*/, new TSize(32, 32)),
            accoff: new TIcon('images/terminal/Motor_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/Motor_32x32_Online.png*/, new TSize(32, 32)),
            move: new TIcon('images/terminal/Motor_32x32_Online2.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/Motor_32x32_Online2.png*/, new TSize(32, 32)),
            offline: new TIcon('images/terminal/Motor_32x32_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/Motor_32x32_Offline.png*/, new TSize(32, 32)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        Pzoom12_18: {//人员12-18级地图
            accon: new TIcon('images/terminal/P_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_32x32_Online.png*/, new TSize(32, 32)),
            accoff: new TIcon('images/terminal/P_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_32x32_Online.png*/, new TSize(32, 32)),
            move: new TIcon('images/terminal/P_32x32_Online2.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_32x32_Online2.png*/, new TSize(32, 32)),
            offline: new TIcon('images/terminal/P_32x32_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/P_32x32_Offline.png*/, new TSize(32, 32)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        Czoom12_18: {//小汽车12-18级地图
            accon: new TIcon('images/terminal/C_32x32_ACCOn_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_32x32_ACCOn_Online.png*/, new TSize(32, 32)),
            accoff: new TIcon('images/terminal/C_32x32_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_32x32_ACCOff_Online.png*/, new TSize(32, 32)),
            move: new TIcon('images/terminal/C_32x32_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_32x32_ACCOff_Online.png*/, new TSize(32, 32)),
            offline: new TIcon('images/terminal/C_32x32_ACCOff_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/C_32x32_ACCOff_Offline.png*/, new TSize(32, 32)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        HCzoom12_18: {//货车12-18级地图
            accon: new TIcon('images/terminal/G1.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/G1.png*/, new TSize(32, 32)),
            accoff: new TIcon('images/terminal/G1.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/G1.png*/, new TSize(32, 32)),
            move: new TIcon('images/terminal/Y1.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/Y1.png*/, new TSize(32, 32)),
            offline: new TIcon('images/terminal/R1.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/R1.png*/, new TSize(32, 32)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        JCzoom12_18: {//警车12-18级地图
            accon: new TIcon('images/terminal/JC_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/JC_32x32_Online.png*/, new TSize(32, 32)),
            accoff: new TIcon('images/terminal/JC_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/JC_32x32_Online.png*/, new TSize(32, 32)),
            move: new TIcon('images/terminal/JC_32x32_Online2.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/JC_32x32_Online2.png*/, new TSize(32, 32)),
            offline: new TIcon('images/terminal/JC_32x32_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/JC_32x32_Offline.png*/, new TSize(32, 32)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        TLJzoom12_18: {//拖拉机12-18级地图
            accon: new TIcon('images/terminal/TLJ_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/TLJ_32x32_Online.png*/, new TSize(32, 32)),
            accoff: new TIcon('images/terminal/TLJ_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/TLJ_32x32_Online.png*/, new TSize(32, 32)),
            move: new TIcon('images/terminal/TLJ_32x32_Online2.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/TLJ_32x32_Online2.png*/, new TSize(32, 32)),
            offline: new TIcon('images/terminal/TLJ_32x32_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/TLJ_32x32_Offline.png*/, new TSize(32, 32)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        WJJzoom12_18: {//挖掘机车12-18级地图
            accon: new TIcon('images/terminal/WJJ_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/WJJ_32x32_Online.png*/, new TSize(32, 32)),
            accoff: new TIcon('images/terminal/WJJ_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/WJJ_32x32_Online.png*/, new TSize(32, 32)),
            move: new TIcon('images/terminal/WJJ_32x32_Online2.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/WJJ_32x32_Online2.png*/, new TSize(32, 32)),
            offline: new TIcon('images/terminal/WJJ_32x32_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/WJJ_32x32_Offline.png*/, new TSize(32, 32)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        },
        SGJzoom12_18: {//收割机车12-18级地图
            accon: new TIcon('images/terminal/SGJ_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/SGJ_32x32_Online.png*/, new TSize(32, 32)),
            accoff: new TIcon('images/terminal/SGJ_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/SGJ_32x32_Online.png*/, new TSize(32, 32)),
            move: new TIcon('images/terminal/SGJ_32x32_Online2.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/SGJ_32x32_Online2.png*/, new TSize(32, 32)),
            offline: new TIcon('images/terminal/SGJ_32x32_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/SGJ_32x32_Offline.png*/, new TSize(32, 32)),
            alarm: new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24))
        }
    },
    displayLabelZoom: 1, //与zoom12_18对应
    clickZoom: 16,
    selTreeNode: null,
    traceMgr: null,
    tracePly: null,
    tailPly: null, //尾迹
    startMkr: null,
    endMkr: null,
    alarmPly: null, //报警区域
    loadDataHandler: -1,
    loadDataHandlerLoadTree: -1,
    historyObject: {
        pts: [],
        obj: {}
    },
    realHistoryObject: {
        pts: [],
        obj: {}
    },
    preActiveTab: null,
    init: function () {
        //一分钟刷新一次
        var me = this;
        me.loadDataHandler = window.setInterval(function () {
            me.reloadTree(); //重新加载左侧树
            me.loadData();
            //  }, 60000);
        }, serverInfo.configParam.popupInterval);
        me.loadDataHandlerLoadTree = window.setInterval(function () {
            me.reloadTree(); //重新加载左侧树          
        }, 3 * serverInfo.configParam.popupInterval);

        if (this.initFinished) {
            return; //only once
        }

        this.initMap();
        this.initMouseControl();
        this.loadData();

        this.initFinished = true;
    },
    initMap: function () {
        window.tiandiMap = new TMap('divTiandiMap');
        var pt = new TLngLat(116.404, 39.915);
        tiandiMap.centerAndZoom(pt, 12);

        tiandiMap.enableDoubleClickZoom();
        tiandiMap.enableHandleMouseScroll();
        tiandiMap.enableInertia();

        tiandiMap.addControl(new TNavigationControl());
        tiandiMap.addControl(new TScaleControl());
        tiandiMap.addControl(new TOverviewMapControl());

        //resize bug
        setInterval(function () {
            var mapSize = tiandiMap.getViewSize(),
				mapWidth = mapSize.width,
				mapHeight = mapSize.height,
				divWidth = $('#divTiandiMap')[0].clientWidth,
				divHeight = $('#divTiandiMap')[0].clientHeight;

            if (mapWidth != divWidth
				|| mapHeight != divHeight) {
                tiandiMap.checkResize();
            }
        }, 1000);

    },
    zoomInHandler: function () {
        T_Index.rectZoomInTool.open();
    },
    zoomOutHandler: function () {
        T_Index.rectZoomOutTool.open();
    },
    measureLengthHandler: function () {
        T_Index.distanceTool.open();
        tiandiMap.setMapCursor('crosshair');
    },
    searchBikeHandler: function () {
        T_Index.searchBikeTool.open();
    },
    onDrawRect: function (e) {
        T_Index.drawRangeRectTool.open();
    },
    areaAlarmHandler: function () {
        T_Index.setupAlarmTool.open();
    },
    fullScreenHandler: function (e) {
        var target = e.sender;
        if (target.text == '全屏') {
            mini.get('layoutMain').hideRegion('north');
            mini.get('layoutMain').hideRegion('west');
            mini.get('layoutMain').hideRegion('south');
            target.setText('退出全屏');
        } else {
            mini.get('layoutMain').showRegion('north');
            mini.get('layoutMain').showRegion('west');
            mini.get('layoutMain').showRegion('south');
            target.setText('全屏');
        }
    },
    satliteMapHandler: function (e) {
        var target = e.sender;
        if (target.text == '卫星地图') {
            if (tiandiMap.getMapType() != TMAP_HYBRID_MAP) {
                tiandiMap.setMapType(TMAP_HYBRID_MAP);
                //tiandiMap bug特殊处理下，否则出现空白图
                tiandiMap.zoomIn();
                tiandiMap.zoomOut();
                target.setText('普通地图');
            }
        } else {
            if (tiandiMap.getMapType() != TMAP_NORMAL_MAP) {
                tiandiMap.setMapType(TMAP_NORMAL_MAP);

                //tiandiMap bug特殊处理下，否则出现空白图
                tiandiMap.zoomIn();
                tiandiMap.zoomOut();
                target.setText('卫星地图');
            }
        }
    },
    switchMapType: function (type) {
        if (type == '卫星地图') {
            if (tiandiMap.getMapType() != TMAP_HYBRID_MAP) {
                tiandiMap.setMapType(TMAP_HYBRID_MAP);

                //tiandiMap bug特殊处理下，否则出现空白图
                tiandiMap.zoomIn();
                tiandiMap.zoomOut();
            }
        } else {
            if (tiandiMap.getMapType() != TMAP_NORMAL_MAP) {
                tiandiMap.setMapType(TMAP_NORMAL_MAP);

                //tiandiMap bug特殊处理下，否则出现空白图
                tiandiMap.zoomIn();
                tiandiMap.zoomOut();
            }
        }
    },
    getMapView: function () {
        var pt = tiandiMap.getCenter(),
            zoom = tiandiMap.getZoom(),
            lng = pt.getLng(),
            lat = pt.getLat();
        return {
            'lng': lng,
            'lat': lat,
            'zoom': zoom
        }
    },
    setMapView: function (view) {
        var pt = new TLngLat(view.lng, view.lat);
        tiandiMap.centerAndZoom(pt, view.zoom);
    },
    initMouseControl: function () {
        var me = this,
			config = {
			    strokeColor: "#000000",  //折线颜色
			    fillColor: "#FFFFFF",    //填充颜色。当参数为空时，折线覆盖物将没有填充效果
			    strokeWeight: "2px", //折线的宽度，以像素为单位
			    opacity: 0.5,    //折线的透明度，取值范围0 - 1
			    strokeStyle: "solid" //折线的样式，solid或dashed
			};

        T_Index.rectZoomInTool = new TRectTool(tiandiMap, config);
        TEvent.addListener(T_Index.rectZoomInTool, "draw", function (bds, rect) {
            tiandiMap.setCenterAtLngLat(bds.getCenter());
            tiandiMap.zoomIn();

            tiandiMap.removeOverLay(rect);
            T_Index.rectZoomInTool.close();
        });


        T_Index.rectZoomOutTool = new TRectTool(tiandiMap, config);
        TEvent.addListener(T_Index.rectZoomOutTool, "draw", function (bds, rect) {
            tiandiMap.setCenterAtLngLat(bds.getCenter());
            tiandiMap.zoomOut();

            tiandiMap.removeOverLay(rect);
            T_Index.rectZoomOutTool.close();
        });


        T_Index.distanceTool = new TPolylineTool(tiandiMap, config);
        TEvent.addListener(T_Index.distanceTool, "draw", function (pts, len, ply) {
            T_Index.distanceTool.close();
            tiandiMap.setMapCursor('default');
        });


        T_Index.searchBikeTool = new TRectTool(tiandiMap, config);
        TEvent.addListener(T_Index.searchBikeTool, "draw", function (bds, rect) {
            var me = this,
				overlay = rect,
				bounds = bds,
				sw = bounds.getSouthWest(),
				ne = bounds.getNorthEast(),
				params = 'minx=' + sw.lng
					+ '&maxx=' + ne.lng
					+ '&miny=' + sw.lat
					+ '&maxy=' + ne.lat;

            $.ajax({
                url: 'actions/search_bike.ashx?' + params,
                type: 'GET',
                error: function () {
                    tiandiMap.removeOverLay(overlay);
                    me.close();
                },
                success: function (data) {
                    var tabMain = mini.get('tabMain'),
						tabSearchInfo = tabMain.getTab('tabSearchInfo');
                    tabSearchInfo.visible = true;
                    tabMain.updateTab(tabSearchInfo);
                    tabMain.activeTab(tabSearchInfo);
                    mini.get('layoutMain').expandRegion('south');

                    var arr = null;
                    try { eval("arr=" + data) } catch (e) { }
                    mini.get('dgSearchInfo').setData(arr);

                    tiandiMap.removeOverLay(overlay);
                    me.close();
                }
            });
        });

        T_Index.drawRangeRectTool = new TRectTool(tiandiMap, config);
        TEvent.addListener(T_Index.drawRangeRectTool, "draw", function (bds, rect) {
            var me = this,
				overlay = rect,
				bounds = bds,
				sw = bounds.getSouthWest(),
				ne = bounds.getNorthEast();
            var a = sw.lat,
                    b = sw.lng,
                    c = ne.lat,
                    d = ne.lng;
            mini.get('lLatitude').setValue(a * 1000000);
            mini.get('lLongitude').setValue(b * 1000000);
            mini.get('rLatitude').setValue(c * 1000000);
            mini.get('rLongitude').setValue(d * 1000000);
            tiandiMap.removeOverLay(overlay);
            me.close();

        });

        config.showLabel = false;
        T_Index.setupAlarmTool = new TPolygonTool(tiandiMap, config);
        TEvent.addListener(T_Index.setupAlarmTool, "draw", function (points, area, pgn) {
            var me = this,
				overlay = pgn;

            if (points.length < 3) {
                mini.showTips({
                    content: "设置的报警区域必须大于3个点!",
                    state: 'default',
                    x: 'center',
                    y: 'center',
                    timeout: 2000
                });

                tiandiMap.removeOverLay(overlay);
                me.close();

                return;
            }

            mini.open({
                url: "alarmWindow.html"/*tpa=http://www.beidouguard.com:6010/js/alarmWindow.html*/,
                title: "添加报警区域",
                width: 360,
                height: 350,
                showModal: true,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    //进行初始化页面数据
                    iframe.contentWindow.initAlarmList();
                },
                ondestroy: function (action) {
                    if (action == "ok") {
                        var iframe = this.getIFrameEl(),
                            data = iframe.contentWindow.getData();
                        data = mini.clone(data);

                        var alarmName = data.alarmName,
                            alarmType = data.alarmType,
                            ids = data.ids,
                            pgn = '',
                            params = '',
                            leastCount = data.leastCount,
                            isUsed = data.isUsed;

                        for (var i = 0; i < points.length; i++) {
                            var pt = points[i];
                            pgn += pt.getLng() + ',' + pt.getLat() + ',';
                        }
                        pgn = pgn.substr(0, pgn.length - 1);

                        params = 'alarmName=' + alarmName + '&isUsed=' + isUsed + '&leastCount=' + leastCount + '&alarmType=' + alarmType + '&ids=' + ids + '&pgn=' + pgn;
                        //mini.alert(params); //调试状态        						     						
                        $.ajax({
                            url: 'actions/setup_alarm_area.ashx?' + params,
                            type: 'GET',
                            error: function () {
                                tiandiMap.removeOverLay(overlay);
                                me.close();
                            },
                            success: function (data) {
                                var obj = null;
                                if (data == "个数太少") {
                                    mini.alert("选择区域内聚集报警,关联终端个数应该大于等于最小的报警个数,请重新设定");
                                    tiandiMap.removeOverLay(overlay);
                                    me.close();
                                    return;
                                }

                                if (data == "此区域名称已经被使用,请修改区域名称") {
                                    //                                    mini.showTips({
                                    //                                        content: "此区域名称已经被使用,请修改区域名称",
                                    //                                        state: 'default',
                                    //                                        x: 'center',
                                    //                                        y: 'center',
                                    //                                        timeout: 2000
                                    //                                    });
                                    mini.alert("此区域名称已经被使用,请重新设定,并修改区域名称");
                                    tiandiMap.removeOverLay(overlay);
                                    me.close();
                                    return;
                                }
                                try { eval("obj=" + data) } catch (e) { }
                                var id = obj.ret,
                                    name = obj.name;
                                if (id != 0) {
                                    //报警区域管理树中增加对应节点
                                    var treeRgn = mini.get('treeRgn');
                                    node = { 'id': id, 'text': name, 'img': 'icon_alarm.png'/*tpa=http://www.beidouguard.com:6010/js/icon_alarm.png*/ },
                                        rootNode = treeRgn.getRootNode(),
                                        midNode = rootNode.children[0],
                                        parentNode = midNode ? midNode.children[0] : null;
                                    if (parentNode) {
                                        treeRgn.addNode(node, 'before', parentNode);
                                    } else {
                                        treeRgn.addNode(node, 'add', midNode);
                                    }

                                    info = "<b>设置报警区域成功!</b><br/>进入该区域的车辆将报警。";
                                } else {
                                    info = "<b>设置报警区域失败!</b><br/>请不要输入非法字符。";
                                }
                                mini.showTips({
                                    content: info,
                                    state: 'default',
                                    x: 'center',
                                    y: 'center',
                                    timeout: 2000
                                });

                                tiandiMap.removeOverLay(overlay);
                                me.close();
                            }
                        });
                    } else {//非ok
                        tiandiMap.removeOverLay(overlay);
                        me.close();
                    }
                }
            });

        });
    },
    treeMainHandler: function (e) {
        var record = e.record,
			id = record.id,
			isBikeNode = (record.img != 'location.png'/*tpa=http://www.beidouguard.com:6010/js/location.png*/ && record.img != 'icon_user.png'/*tpa=http://www.beidouguard.com:6010/js/icon_user.png*/) ? true : false; //是否是叶子节点
        //isBikeNode = (record.img == 'icon_bike.png'/*tpa=http://www.beidouguard.com:6010/js/icon_bike.png*/ || record.img == 'icon_person.png'/*tpa=http://www.beidouguard.com:6010/js/icon_person.png*/ || record.img == 'icon_car.png'/*tpa=http://www.beidouguard.com:6010/js/icon_car.png*/) ? true : false; //是否是电动车节点，根据icon_bike.png判定

        if (isBikeNode) {//叶子节点
            T_Index.zoomToMarker(id);
        }
    },
    /**
    * 点击树状控件check处理函数
    */
    treeCheckedHandler: function (e) {
        var tree = mini.get("treeMain");
        var idStr = tree.getValue(false);
        //        alert(idStr);

        $.ajax({
            url: 'actions/get_select_target.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_select_target.ashx*/,
            type: 'POST',
            data: { 'id': idStr },
            error: function () { },
            success: function (data) {
            },
            error: function () { mini.alert('error') }
        });
    },
    treeRgnHandler: function (e) {
        var record = e.node,
			id = record.id,
            isLeaf = (typeof record.children == 'undefined') ? true : false;

        if (!isLeaf) {//点击根节点不处理
            return;
        }

        $.ajax({
            url: 'actions/get_region_info.ashx?id=' + id + '&q=shape',
            type: 'GET',
            error: function () { mini.alert('获取失败'); },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }

                var arr = data.split(','),
					pts = [],
                    l = arr.length;
                for (var i = 0; i < l; i += 2) {
                    pts.push(new TLngLat(arr[i], arr[i + 1]));
                }
                var polygon = new TPolygon(pts, {
                    strokeWeight: 2,
                    strokeColor: 'blue',
                    fillColor: 'red'
                });

                if (T_Index.alarmPly) {
                    tiandiMap.removeOverLay(T_Index.alarmPly);
                }
                tiandiMap.addOverLay(polygon);
                tiandiMap.setViewport(polygon.getLngLats());

                T_Index.alarmPly = polygon;
            }
        });
    },
    /**
    * 重新加载左侧树
    */
    reloadTree: function () {
        var tree = mini.get("treeMain");
        var idStr = tree.getValue(false); //获取已经checked的节点值
        tree.load();
        tree.collapseAll();
        if (idStr.length > 0) {
            var checkedNodes = tree.findNodes(function (node) {
                if (idStr.indexOf(node.id) != -1) {
                    return true;
                }
            });
            tree.checkNodes(checkedNodes);
            //用于控制到expand第一个选择的节点
            var firstCheckedNode;
            var firstCheckedNodePosi = idStr.indexOf(",");
            var firstCheckedNodenodeId;
            if (firstCheckedNodePosi > 0) {
                firstCheckedNodenodeId = idStr.substr(0, firstCheckedNodePosi);
            } else {
                firstCheckedNodenodeId = idStr;
            }

            firstCheckedNode = tree.getNode(firstCheckedNodenodeId);
            tree.expandPath(firstCheckedNode);
        }
    },
    loadData: function () {
        var me = this,
			dgLastInfo = mini.get('dgLastInfo');

        dgLastInfo.load({}, function (e) {
            me.addMarkers(e.data);
        });

        var dgLastState = mini.get('dgLastState');
        dgLastState.load();

        var dgAlarmInfo = mini.get('dgAlarmInfo');
        dgAlarmInfo.load();
    },
    dgLastInfoHandler: function (e) {
        var record = e.record,
			id = record.id;

        T_Index.zoomToMarker(id);
    },
    dgLastStateHandler: function (e) {
        var record = e.record,
			id = record.id;

        T_Index.zoomToMarker(id);
    },
    dgAlarmInfoHandler: function (e) {
        var me = this,
            record = e.record,
			id = record.id,
            _uid = '_' + record._uid,
            x = record.x,
            y = record.y,
            targetnumber = record.targetnumber,
            lng = record.longitude,
            lat = record.latitude,
            time = record.time,
            alarmType = record.alarmType,
            obj = me.realHistoryObject.obj[_uid],
            str = '终端号: ' + targetnumber + '<br/>'
                + '经纬度: ' + lng + ', ' + lat + '<br/>'
                + '时间: ' + time + '<br/>'
                + '类型: ' + alarmType + '<br/>';
        if (obj) {
            tiandiMap.setCenterAtLngLat(obj.getLngLat());
            me.openAlarmInfoWin(obj, str);
            return;
        }

        var pt = new TLngLat(x, y),
            icon = new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24)),
            mkr = new TMarker(pt, { 'icon': icon });

        me.realHistoryObject.pts.push(pt);
        me.realHistoryObject.obj[_uid] = mkr;

        tiandiMap.addOverLay(mkr);
        TEvent.addListener(mkr, "click", function () {
            me.openAlarmInfoWin(this, str);
        });

        var view = tiandiMap.getViewport(me.realHistoryObject.pts);
        tiandiMap.centerAndZoom(pt, view.zoom - 1);
        me.openAlarmInfoWin(mkr, str);
    },
    dgHistoryStateHandler: function (e) {
        var record = e.record,
			id = record.id;

        T_Index.zoomToMarker(id);
    },
    dgHistoryAlarmHandler: function (e) {
        var me = this,
            record = e.record,
			id = record.id,
            _uid = '_' + record._uid,
            x = record.x,
            y = record.y,
            targetnumber = record.targetnumber,
            lng = record.longitude,
            lat = record.latitude,
            time = record.time,
            alarmType = record.alarmType,
            obj = me.historyObject.obj[_uid],
            str = '终端号: ' + targetnumber + '<br/>'
                + '经纬度: ' + lng + ', ' + lat + '<br/>'
                + '时间: ' + time + '<br/>'
                + '类型: ' + alarmType + '<br/>';
        if (obj) {
            tiandiMap.setCenterAtLngLat(obj.getLngLat());
            me.openAlarmInfoWin(obj, str);
            return;
        }

        var pt = new TLngLat(x, y),
            icon = new TIcon('images/terminal/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/alarm.gif*/, new TSize(30, 24)),
            mkr = new TMarker(pt, { 'icon': icon });

        me.historyObject.pts.push(pt);
        me.historyObject.obj[_uid] = mkr;

        tiandiMap.addOverLay(mkr);
        TEvent.addListener(mkr, "click", function () {
            me.openAlarmInfoWin(this, str);
        });

        var view = tiandiMap.getViewport(me.historyObject.pts);
        tiandiMap.centerAndZoom(pt, view.zoom - 1);
        me.openAlarmInfoWin(mkr, str);
    },
    openAlarmInfoWin: function (mkr, str) {
        if (serverInfo.UserConfigParam.isShowTag == 1) {
            mkr.openInfoWinHtml(str);
        }
    },
    dgSearchInfoHandler: function (e) {
        var record = e.record,
			id = record.id;

        T_Index.zoomToMarker(id);
    },
    dgTargetSearchInfoHandler: function (e) {
        var record = e.record,
			id = record.id;

        T_Index.zoomToMarker(id);
    },
    tabMainCloseHandler: function (e) {
        var tabMain = mini.get('tabMain'),
            tab = e.tab;
        tab.visible = false;
        tabMain.updateTab(tab);
        tabMain.activeTab(tabMain.getTab(0));

        //恢复点击导致的icon的变化
        if (tab.name == 'tabHistoryAlarm') {
            var obj = this.historyObject.obj,
                pts = this.historyObject.pts;
            for (o in obj) {
                var mkr = obj[o];
                mkr.closeInfoWindow();
                tiandiMap.removeOverLay(mkr);
                obj[o] = null;
                delete obj[o];
            }
            //obj = {};
            pts.length = 0;
        }

        e.cancel = true;
    },
    tabMainActiveChangeHandler: function (e) {
        if (this.preActiveTab) {
            if (this.preActiveTab.title == '实时警情') {
                var obj = this.realHistoryObject.obj,
                pts = this.realHistoryObject.pts;
                for (o in obj) {
                    var mkr = obj[o];
                    mkr.closeInfoWindow();
                    tiandiMap.removeOverLay(obj[o]);
                    obj[o] = null;
                    delete obj[o];
                }
                obj = {};
                pts.length = 0;
            }
        }
        this.preActiveTab = e.tab;
    },
    delMaker: function () {
        var obj = this.historyObject.obj,
                pts = this.historyObject.pts;
        for (o in obj) {
            var mkr = obj[o];
            mkr.closeInfoWindow();
            tiandiMap.removeOverLay(mkr);
            obj[o] = null;
            delete obj[o];
        }
        pts.length = 0;
    },
    zoomToMarker: function (id) {
        var me = this,
			mkrs = me.mkrs;
        for (var o in mkrs) {
            var mkr = mkrs[o],
				pt = mkr.getLngLat();
            if (mkr._id == id) {
                tiandiMap.centerAndZoom(pt, me.clickZoom);
                if (serverInfo.UserConfigParam.isShowTag == 1) {
                    me.openInfoWin(mkr);
                }
                break;
            }
        }
    },
    getCurrentIcons: function (zoom, type) {
        var icons = null;
        //if (zoom >= 3 && zoom <= 5) {//天地图和百度地图最小级别不同，天地图最小取1
        /*if (zoom >= 1 && zoom <= 5) {
        if ('1' == type || '2' == type) {
        icons = this.icons.zoom3_5;
        } else if ('3' == type || '4' == type || '5' == type || '7' == type || '8' == type || '9' == type) {
        icons = this.icons.Czoom3_5;
        } else {
        icons = this.icons.Pzoom3_5;
        }
        } else if (zoom >= 6 && zoom <= 11) {
        if ('1' == type || '2' == type) {
        icons = this.icons.zoom6_11;
        } else if ('3' == type || '4' == type || '5' == type || '7' == type || '8' == type || '9' == type) {
        icons = this.icons.Czoom6_11;
        } else {
        icons = this.icons.Pzoom6_11;
        }
        } else*/
        {
            if ('1' == type) {
                icons = this.icons.Motorzoom12_18;
            } else if ('2' == type) {
                icons = this.icons.zoom12_18;
            } else if ('3' == type) {
                icons = this.icons.Czoom12_18;
            } else if ('4' == type) {
                icons = this.icons.HCzoom12_18;
            } else if ('5' == type) {
                icons = this.icons.JCzoom12_18;
            } else if ('6' == type) {
                icons = this.icons.WJJzoom12_18;
            } else if ('7' == type) {
                icons = this.icons.TLJzoom12_18;
            } else if ('8' == type) {
                icons = this.icons.SGJzoom12_18;
            } else {
                icons = this.icons.Pzoom12_18;
            }
        }
        return icons;
    },
    addMarkers: function (data) {
        var mkrs = this.mkrs;
        for (var i = 0, l = mkrs.length; i < l; i++) {
            var mkr = mkrs[i];
            tiandiMap.removeOverLay(mkr);
            tiandiMap.removeOverLay(mkr._label);
            mkr._label = null;
        }
        mkrs.length = 0;

        var me = this,
            pts = [],
            zoom = tiandiMap.getZoom(),
            isShowOffline = serverInfo.configParam.isShowOffline,
            isShowTargetName = serverInfo.configParam.isShowTargetName;
        for (var i = 0, l = data.length; i < l; i++) {
            var item = data[i],
				x = item.x,
				y = item.y,
				id = item.id,
				targetnumber = item.targetnumber,
                sim = item.sim,
				state = item.isonline, //包含4种状态，accon,accoff,offline,alarm
				type = item.carType,
                speed = item.velocity,
                time1 = item.time,
                carplatenumber = item.carplatenumber,
                mkr,
                pt,
                icon;
            curIcons = this.getCurrentIcons(zoom, type);
            if (!(isShowOffline == 0 && state == 'offline')) {
                icon = curIcons[state];
                pt = new TLngLat(x, y);
                mkr = new TMarker(new TLngLat(x, y), { 'icon': icon });
                mkr._id = id;
                mkr._targetnumber = targetnumber;
                mkr._sim = sim;
                mkr._ownType = type;
                mkr._state = state;
                mkr._speed = speed;
                mkr._x = x;
                mkr._y = y;
                mkr._time = time1;
                mkr._carplatenumber = carplatenumber;
                targetnumber = targetnumber + ', ' + speed + 'KM/H';
                mkr._label = new TLabel({ text: targetnumber, // 指定文本标注所在的地理位置
                    offset: new TPixel(20, -15),    //设置文本偏移量
                    position: pt
                });

                mkr._label.setBackgroundColor('#ffc');
                mkr._label.setBorderLine(1);
                mkr._label.setBorderColor('#000');
                mkr._label.setFontColor('#000');
                mkr._label.setFontSize(12);

                tiandiMap.addOverLay(mkr);
                tiandiMap.addOverLay(mkr._label);

                if (zoom < me.displayLabelZoom || (isShowTargetName !== 1)) {
                    mkr._label.hide();
                }

                mkrs.push(mkr);
                pts.push(pt);

                var me = this;
                TEvent.addListener(mkr, "click", function () {
                    me.openInfoWin(this);
                });
            }
        }

        if (!tiandiMap._zoomendListener) {
            tiandiMap._zoomendListener = true;

            //根据不同级别修改为mkr预设定的3套icon
            TEvent.addListener(tiandiMap, "zoomend", function (e) {//仅绑定一次zoomend事件
                var zoom = tiandiMap.getZoom(),

                    mkrs = me.mkrs;

                for (var i = 0, l = mkrs.length; i < l; i++) {
                    var mkr = mkrs[i],
                        state = mkr._state,
                        type = mkr._ownType,
                        curIcons = me.getCurrentIcons(zoom, type),
                        icon = curIcons[state];
                    mkr.setIcon(icon);

                    if (isShowTargetName !== 1) {
                        mkr._label.hide();
                    } else {
                        if (zoom < me.displayLabelZoom) {
                            mkr._label.hide();
                        } else {
                            mkr._label.show();
                            mkr._label.setOffset(new TPixel(-20, -40)); //强刷新下才行
                        }
                    }
                };
            });

            tiandiMap.setViewport(pts); //缩放到电动车视野内
        }

    },
    openInfoWin: function (mkr) {
        var me = this;
        var myGeo = new TGeocoder();
        // 根据坐标得到地址描述 
        myGeo.getLocation(new TLngLat(mkr._x, mkr._y), function (result) {
            if (result.getStatus() == 0) {
                var address = result.getAddress();
                //                alert(address);
                var vehiclename = "";
                var cook = getCookie("dependencyid");
                if (cook == "2") {
                    vehiclename = mkr._carplatenumber;
                }
                else {
                    vehiclename = mkr._targetnumber;
                }

                var str = '	<table style="width:320px;height:160px;font-family:Microsoft Yahei;font-size:12px"> ' +
		'<tr style="height:10px">' +
		'	<td style="border-bottom:1px solid red" colspan="2">' +
		'		<span style="color:#000;font-weight: bold">' + vehiclename + '</span>' +
		'	</td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td style="width:61%">' +
		'		<span style="display:inline-block;color:#98B421;padding-left:8px;">GPS时间:</span><span style="font-size:12px">' + mkr._time + '</span>' +
		'	</td>' +
		'	<td >' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">速度:</span><span style="font-size:12px">' + mkr._speed + 'km/h</span>' +
		'	</td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td>' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">定位方式:</span><span style="font-size:12px">卫星定位</span>' +
		'	</td>' +
		'	<td>' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">车辆状态:</span><span style="font-size:12px">未设防</span>' +
		'	</td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td>' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">经度:</span><span style="font-size:12px">' + mkr._x + '东</span>' +
		'	</td>' +
		'	<td>' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">报警信息:</span><span style="font-size:12px"></span>' +
		'	</td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td  colspan="2">' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">纬度:</span><span style="font-size:12px">' + mkr._y + '北</span>' +
		'	</td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td colspan="2">' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;font-size:12px">地址:</span><span style="font-size:12px">' + address + '</span>' +
		'	</td>' +
		'</tr>' +
	'</table>';

                mkr.openInfoWinHtml(str);
            }
        });
        /*        var me = this,
        arr = [];
        arr.push('<div id="tiandiMapInfoWin">');
        arr.push('<div>目标号: ' + mkr._targetnumber + '</div>');
        arr.push('<div>SIM卡号: ' + mkr._sim + '</div>');

        arr.push('<div class="content">');
        arr.push('<a plain="true" href="javascript:T_Index.onTreeMenu_weijichaxun(' + mkr._id + ')" >尾迹查询</a>');
        arr.push('<a plain="true" href="javascript:T_Index.onTreeMenu_guijihuifang(' + mkr._id + ')" >轨迹回放</a>');
        arr.push('<a plain="true" href="javascript:T_Index.onTreeMenu_baoxianchaxun(' + mkr._id + ')" >保险查询</a>');
        arr.push('<a plain="true" href="javascript:T_Index.onTreeMenu_diaoweizhiling(' + mkr._id + ')" >调位指令</a>');
        arr.push('<a plain="true" href="javascript:T_Index.onTreeMenu_lishizhuangtai(' + mkr._id + ')" >历史状态</a>');
        arr.push('<a plain="true" href="javascript:T_Index.onTreeMenu_lishibaojing(' + mkr._id + ')" >历史报警</a>');
        arr.push('<a plain="true" href="javascript:T_Index.onTreeMenu_dancanshushezhi(' + mkr._id + ')" >单参数设置</a>');
        arr.push('<a plain="true" href="javascript:T_Index.onTreeMenu_piliangcanshushezhi(' + mkr._id + ')" >批量参数设置</a>');
        arr.push('<a plain="true" href="javascript:T_Index.onTreeMenu_zhongduanquyushezhi(' + mkr._id + ')" >终端区域设置</a>');
        arr.push('</div>');

        arr.push('</div>');

        mkr.setInfoWinHeight(120);
        mkr.setInfoWinWidth(200);
        mkr.openInfoWinHtml(arr.join(''));*/
    },
    onTreeCtxMenuRgn: function (e) {
        var menu = e.sender,
			tree = mini.get("treeRgn"),
			node = tree.getSelectedNode(),
            isLeaf = (typeof node.children == 'undefined') ? true : false;

        if (!node || !isLeaf) {
            e.htmlEvent.preventDefault();
            e.cancel = true;
            return;
        }

        T_Index.selTreeNode = node;
        tree.selectNode(null); //最后不选择
    },
    onTreeCtxMenu: function (e) {
        var menu = e.sender,
			tree = mini.get("treeMain"),
			node = tree.getSelectedNode(),
        //isBikeNode = (node.img == 'icon_bike.png'/*tpa=http://www.beidouguard.com:6010/js/icon_bike.png*/ || node.img == 'icon_car.png'/*tpa=http://www.beidouguard.com:6010/js/icon_car.png*/ || node.img == 'icon_person.png'/*tpa=http://www.beidouguard.com:6010/js/icon_person.png*/) ? true : false; //是否是电动车节点，根据icon_bike.png判定
            isBikeNode = (node.img != 'location.png'/*tpa=http://www.beidouguard.com:6010/js/location.png*/ && node.img != 'icon_user.png'/*tpa=http://www.beidouguard.com:6010/js/icon_user.png*/) ? true : false; //是否是叶子节点

        if (!node || !isBikeNode) {
            e.htmlEvent.preventDefault();
            e.cancel = true;
            return;
        }

        T_Index.selTreeNode = node;
        tree.selectNode(null); //最后不选择
    },
    onTreeMenuRgn: function (e) {
        var menuItemText = $.trim(e.sender.getText()),
            num = T_Index.selTreeNode.id;
        switch (menuItemText) {
            case "修改报警区域":
                $.ajax({
                    url: 'actions/get_alarm_list.ashx?id=' + num,
                    type: 'GET',
                    error: function () { mini.alert('修改失败'); },
                    success: function (data) {
                        var objData = null;
                        try { eval("objData=" + data) } catch (e) { }
                        mini.open({
                            url: "alarmWindow.html"/*tpa=http://www.beidouguard.com:6010/js/alarmWindow.html*/,
                            title: "修改报警区域",
                            width: 360,
                            height: 350,
                            showModal: true,
                            onload: function () {
                                var iframe = this.getIFrameEl();
                                //进行初始化页面数据
                                iframe.contentWindow.setData(objData);
                            },
                            ondestroy: function (action) {
                                if (action == "ok") {
                                    var iframe = this.getIFrameEl(),
                                        data = iframe.contentWindow.getData();
                                    data = mini.clone(data);

                                    var alarmName = data.alarmName,
                                        alarmType = data.alarmType,
                                        ids = data.ids,
                                        params = '',
                                        leastCount = data.leastCount,
                                        isUsed = data.isUsed;

                                    params = 'num=' + num + '&alarmName=' + alarmName + '&isUsed=' + isUsed + '&leastCount=' + leastCount + '&alarmType=' + alarmType + '&ids=' + ids;
                                    //mini.alert(params); //调试状态

                                    //修改数据保存到数据库
                                    $.ajax({
                                        url: 'actions/setup_alarm_area.ashx?' + params,
                                        type: 'GET',
                                        error: function () { mini.alert('修改失败'); },
                                        success: function (data) {
                                            if (data == "个数太少") {
                                                mini.alert("选择区域内聚集报警,关联终端个数应该大于等于最小的报警个数,请重新设定");
                                                tiandiMap.removeOverLay(overlay);
                                                me.close();
                                                return;
                                            }
                                            if (data) {
                                                var obj = null;
                                                try { eval("obj=" + data) } catch (e) { }
                                                var newName = obj.name,
                                                    treeRgn = mini.get("treeRgn");
                                                treeRgn.updateNode(T_Index.selTreeNode, { text: newName });
                                                mini.alert('修改成功');
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
                break;
            case "删除报警区域":
                if (confirm("确定删除选中记录？")) {
                    $.ajax({
                        url: 'actions/delete_rgn_by_id.ashx?num=' + num,
                        type: 'GET',
                        error: function () { mini.alert('删除失败'); },
                        success: function (data) {
                            if (data == "1") {
                                mini.get('treeRgn').removeNode(T_Index.selTreeNode); //报警区域管理树中删除对应节点
                                tiandiMap.removeOverLay(T_Index.alarmPly);
                                mini.alert("成功删除用户区域信息");
                            }
                            else {
                                mini.alert("删除用户区域出现问题");
                            }
                        }
                    });
                }
                break;
            case "取消查看报警区域":
                if (T_Index.alarmPly) {
                    tiandiMap.removeOverLay(T_Index.alarmPly);
                } else {
                    mini.alert("请先查看用户区域报警");
                }
                break;
        }
    },
    //尾迹查询
    onTreeMenu_weijichaxun: function (num) {
        $.ajax({
            url: 'actions/get_target_weiji.ashx?num=' + num,
            type: 'GET',
            error: function () { },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }

                if (data == "1") {
                    mini.alert("您所查车辆已两个月没有位置信息上报了");
                    return;
                }

                var arr = data.split(','),
				    pts = [];

                if (arr.length < 4) {
                    mini.alert('没有尾迹信息！');
                    return;
                }

                for (var i = 0, l = arr.length; i < l; i += 2) {
                    pts.push(new TLngLat(arr[i], arr[i + 1]));
                }

                tiandiMap.removeOverLay(T_Index.startMkr);
                tiandiMap.removeOverLay(T_Index.endMkr);
                tiandiMap.removeOverLay(T_Index.tailPly);

                T_Index.startMkr = new TMarker(pts[0],
                        { 'icon': new TIcon('images/terminal/t_start_markers.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/t_start_markers.png*/, new TSize(32, 32)) });
                tiandiMap.addOverLay(T_Index.startMkr);

                T_Index.endMkr = new TMarker(pts[pts.length - 1],
                        { 'icon': new TIcon('images/terminal/t_dest_markers.png'/*tpa=http://www.beidouguard.com:6010/js/images/terminal/t_dest_markers.png*/, new TSize(32, 32)) });
                tiandiMap.addOverLay(T_Index.endMkr);

                T_Index.tailPly = new TPolyline(pts, {
                    strokeColor: '#FF0000'
                });

                TEvent.addListener(T_Index.tailPly, "click", function () {
                    var pts = this.getLngLats(),
                        arr = [];
                    for (var i = 0, l = pts.length; i < l; i++) {
                        arr.push(pts[i].getLng() + ',' + pts[i].getLat() + ';<br>');
                    }
                    mini.alert(arr.join(''));
                });

                tiandiMap.addOverLay(T_Index.tailPly);
                tiandiMap.setViewport(T_Index.tailPly.getLngLats());

                mini.alert("单击起点,即可在地图上去除尾迹");

                TEvent.addListener(T_Index.startMkr, "click", function () {
                    tiandiMap.removeOverLay(T_Index.startMkr);
                    tiandiMap.removeOverLay(T_Index.endMkr);
                    tiandiMap.removeOverLay(T_Index.tailPly);
                    tiandiMap.removeOverLay(T_Index.tailPly.getLngLats());
                    T_Index.tailPly = null;
                });
            }
        });
    },
    //尾迹查询
    onTreeMenu_weijiliebiao: function (num) {
        $.ajax({
            url: 'actions/show_target_weiji.ashx?num=' + num,
            type: 'GET',
            error: function () { },
            success: function (data) {
                if (data.length <= 0) {
                    mini.alert("您所查车辆已两个月没有位置信息上报了");
                    return;
                }
                if (data == "1") {
                    mini.alert("您所查车辆已两个月没有位置信息上报了");
                    return;
                }
                var arr = data.split(','),
				    pts = [];

                if (arr.length < 4) {
                    mini.alert('没有尾迹信息！');
                    return;
                }
                var tabMain = mini.get('tabMain'),
						tabTrackWJInfo = tabMain.getTab('tabTrackWJInfo');
                tabTrackWJInfo.visible = true;
                tabMain.updateTab(tabTrackWJInfo);
                tabMain.activeTab(tabTrackWJInfo);
                mini.get('layoutMain').expandRegion('south');

                var arr = null;
                try { eval("arr=" + data) } catch (e) { }
                mini.get('dgTrackWJInfo').setData(arr);
            }
        });
    },
    //轨迹回放
    onTreeMenu_guijihuifang: function (num) {
        /*
        $.ajax({
        url: 'actions/getTargetNumberByTargetId.ashx?num=' + num,
        type: 'GET',
        error: function () { },
        success: function (data) {
        if (data.lendth <= 0) {
        return;
        }
        $('#spanNum').html(num);
        $('#SpanTargetNumber').html(data);
        var winTrace = mini.get("winTrace"),
        preMonthDay = new Date(new Date() - 7 * 24 * 60 * 60 * 1000), //最多三天
        today = new Date();
        mini.get('TrackQuery').enable();
        mini.get('trace_starttime').setValue(preMonthDay);
        mini.get('trace_endtime').setValue(today);
        mini.get('TrackBegin').disable();
        mini.get('TrackRun').disable();
        mini.get('TrackShow').enable();
        T_Index.pts = [];
        mini.get('QueryResult').setValue("");
        winTrace.show('left', 'bottom');
        },
        error: function () { mini.alert('error') }
        });
        */
        var alltargetlist = mini.get('alltargetlist');
        alltargetlist.load('actions/combox_targetlist.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/combox_targetlist.ashx*/);
        if (num == 0)
            alltargetlist.select(0);
        else
            alltargetlist.setValue(num);

        var winTrace = mini.get("winTrace"),
        preMonthDay = new Date(new Date() - 7 * 24 * 60 * 60 * 1000), //最多三天
        today = new Date();
        mini.get('TrackQuery').enable();
        mini.get('TraceToday').enable();
        mini.get('TraceYestoday').enable();

        mini.get('trace_starttime').setValue(preMonthDay);
        mini.get('trace_endtime').setValue(today);
        mini.get('TrackBegin').disable();
        mini.get('TrackRun').disable();
        mini.get('TrackShow').enable();
        Index.pts = [];
        mini.get('QueryResult').setValue("");
        winTrace.show('left', 'bottom');

    },
    //保险查询
    onTreeMenu_baoxianchaxun: function (num) {
        $.ajax({
            url: 'actions/get_insure_info.ashx?num=' + num,
            type: 'GET',
            error: function () { },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }
                // $('#spanInsureId').html(num);
                $('#spanInfo').html(data);
                var winInsure = mini.get("winInsure");
                winInsure.show('left', 'bottom');
            },
            error: function () { mini.alert('error') }
        });
    },
    //单参数设置
    onTreeMenu_dancanshushezhi: function (num) {
        $.ajax({
            url: 'actions/getTargetNumberByTargetId.ashx?num=' + num,
            type: 'GET',
            error: function () { },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }
                var winSoleParameterSetting = mini.get("winSoleParameterSetting");
                $('#spanSPSNum').html(num);
                $('#spanSPSTarget').html(data);
                if (serverInfo.userInfo.userType) {//1-普通用户           
                    mini.get("spsPara").load("data/sole_para_setting_list_putong.txt"/*tpa=http://www.beidouguard.com:6010/js/data/sole_para_setting_list_putong.txt*/);
                }
                mini.get("spsPara").setValue(1);
                mini.get("spsValue").setValue(3400000);
                mini.get("spsDesc").setValue("当终端中电池容量小于阈值时,终端发出报警,输入范围为0到3500000");
                winSoleParameterSetting.show('left', 'bottom');
            },
            error: function () { mini.alert('error') }
        });
    },
    //批量参数设置
    onTreeMenu_piliangcanshushezhi: function (num) {
        var winBatchParameterSetting = mini.get("winBatchParameterSetting");

        $.ajax({
            url: 'actions/getTargetNumberByTargetId.ashx?num=' + num,
            type: 'GET',
            error: function () { },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }
                var winSoleParameterSetting = mini.get("winSoleParameterSetting");
                $('#spanBPSNum').html(num);
                $('#spanBPSTarget').html(data);
                $.ajax({//此处为了填充各字段已有的值
                    url: 'actions/get_TargetConfigSetting.ashx?num=' + num,
                    type: 'GET',
                    success: function (data) {
                        if (data.length <= 0) { return; }
                        if (data == 1) {
                            $('#spanBPSNum').html(num);
                            //只有管理员具有设置2个字段权限                       
                            if (serverInfo.userInfo.userType === 1) {
                                mini.get('cfgValue8').disable();
                                mini.get('cfgValue9').disable();
                                $('#isAllowIPandPort').hide();
                                $('#isAllowSMS').hide();
                            }
                            winBatchParameterSetting.show('left', 'bottom');
                            return;
                        }
                        $('#spanBPSNum').html(num);
                        var obj = null;
                        try { eval("obj=" + data) } catch (e) { }
                        mini.get('cfgValue1').setValue(obj.cfgValue1 ? obj.cfgValue1 : mini.get('cfgValue1').getValue());
                        mini.get('cfgValue2').setValue(obj.cfgValue2 ? obj.cfgValue2 : mini.get('cfgValue2').getValue());
                        mini.get('cfgValue3').setValue(obj.cfgValue3 ? obj.cfgValue3 : mini.get('cfgValue3').getValue());
                        mini.get('cfgValue4').setValue(obj.cfgValue4 ? obj.cfgValue4 : mini.get('cfgValue4').getValue());
                        mini.get('cfgValue5').setValue(obj.cfgValue5 ? obj.cfgValue5 : mini.get('cfgValue5').getValue());
                        //mini.get('cfgValue6').setValue(obj.cfgValue6 ? obj.cfgValue6 : mini.get('cfgValue3').getValue());
                        //mini.get('cfgValue7').setValue(obj.cfgValue7 ? obj.cfgValue7 : mini.get('cfgValue3').getValue());
                        mini.get('cfgValue8').setValue(obj.cfgValue7 ? obj.cfgValue7 : mini.get('cfgValue8').getValue());
                        if (obj.cfgValue8 == 0) {
                            mini.get('cfgValue9').setValue(0);
                        }
                        else {
                            mini.get('cfgValue9').setValue(1);
                        }
                        //must have 'not selected' state

                        //只有管理员具有设置2个字段权限                       
                        if (serverInfo.userInfo.userType === 1) {
                            mini.get('cfgValue8').disable();
                            mini.get('cfgValue9').disable();
                            $('#isAllowIPandPort').hide();
                            $('#isAllowSMS').hide();
                        }

                        mini.get('cfgValue10').setValue(obj.cfgValue9 ? obj.cfgValue9 : mini.get('cfgValue10').getValue());
                        mini.get('cfgValue11').setValue(obj.cfgValue10 ? obj.cfgValue10 : mini.get('cfgValue11').getValue());
                        mini.get('cfgValue12').setValue(obj.cfgValue11 ? obj.cfgValue11 : mini.get('cfgValue12').getValue());
                        mini.get('cfgValue13').setValue(obj.cfgValue12 ? obj.cfgValue12 : mini.get('cfgValue13').getValue());

                        winBatchParameterSetting.show('left', 'bottom');
                    },
                    error: function () { mini.alert('请重新登录，若还有问题请咨询管理员') }
                });
            },
            error: function () { mini.alert('error') }
        });
    },
    //终端区域设置
    onTreeMenu_zhongduanquyushezhi: function (num) {
        var winMessage = mini.get("winMessage");

        $.ajax({
            url: 'actions/getTargetNumberByTargetId.ashx?num=' + num,
            type: 'GET',
            error: function () { },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }
                var winSoleParameterSetting = mini.get("winSoleParameterSetting");
                $('#spanMessageNum').html(num);
                $('#spanMessageTarget').html(data);
                $.ajax({//此处为了填充各字段已有的值
                    url: 'actions/get_TargetRangeSetting.ashx?num=' + num,
                    type: 'GET',
                    success: function (data) {
                        $('#spanMessageNum').html(num);

                        var obj = {};
                        try { eval("obj=" + data) } catch (e) { }
                        mini.get('lLatitude').setValue(obj.lLatitude ? obj.lLatitude : '');
                        mini.get('lLongitude').setValue(obj.lLongitude ? obj.lLongitude : '');
                        mini.get('rLatitude').setValue(obj.rLatitude ? obj.rLatitude : '');
                        mini.get('rLongitude').setValue(obj.rLongitude ? obj.rLongitude : '');
                        mini.get('AlarmType').setValue(obj.AlarmType ? obj.AlarmType : 68);
                        mini.get("AlarmDesc").setValue("所输入经纬度的单位为百万分之一度,经度:116123456;纬度:40123456。可通过最下方的按钮在屏幕上画矩形框填充经纬度。");
                        winMessage.show('left', 'bottom');
                    },
                    error: function () { mini.alert('请重新登录，若还有问题请咨询管理员') }
                });
            },
            error: function () { mini.alert('error') }
        });
    },
    onTreeMenu_diaoweizhiling: function (num) { //调位指令
        var param = "settingType=DiaoWeiZhiLing"
            + "&num=" + num;
        $.ajax({
            url: 'actions/send_message.ashx?' + param,
            type: 'GET',
            error: function () { },
            success: function (data) {
                mini.alert('发送成功!');
            }
        });
    },
    //历史状态
    onTreeMenu_lishizhuangtai: function (num) {
        $.ajax({
            url: 'actions/getTargetNumberByTargetId.ashx?num=' + num,
            type: 'GET',
            error: function () { },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }
                $('#historicStatusNum').html(num);
                $('#historicStatusTarget').html(data);

                var preMonthDay = new Date(new Date() - 30 * 24 * 60 * 60 * 1000), //上一月的一天
                    today = new Date();
                mini.get('historicStatus_starttime').setValue(preMonthDay);
                mini.get('historicStatus_endtime').setValue(today);

                var winHistoricStatus = mini.get("winHistoricStatus");
                winHistoricStatus.show('left', 'bottom');
            },
            error: function () { mini.alert('error') }
        });
    },
    //历史报警
    onTreeMenu_lishibaojing: function (num) {
        $.ajax({
            url: 'actions/getTargetNumberByTargetId.ashx?num=' + num,
            type: 'GET',
            error: function () { },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }
                $('#historicAlarmNum').html(num);
                $('#historicAlarmTarget').html(data);

                var preMonthDay = new Date(new Date() - 30 * 24 * 60 * 60 * 1000), //上一月的一天
                    today = new Date();
                mini.get('historicAlarm_starttime').setValue(preMonthDay);
                mini.get('historicAlarm_endtime').setValue(today);

                var winHistoricAlarm = mini.get("winHistoricAlarm");
                winHistoricAlarm.show('left', 'bottom');
            },
            error: function () { mini.alert('error') }
        });
    },
    //导出车辆详细信息
    onTreeMenu_exportvehicledetails: function (num) {
        $.ajax({
            url: 'actions/get_exportvehicledetails.ashx?txtvessel=' + num,
            type: 'GET',
            success: function (data) {
                var downUrl = "actions/exportCarTrack.ashx?fullFileName=" + data;
                window.location = downUrl;
            }
        });
    },
    //导出车辆轨迹信息
    onTreeMenu_exportvehicletrackdetails: function (num) {
        var alltargetlist = mini.get('txtvessel');
        alltargetlist.load('actions/combox_targetlist.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/combox_targetlist.ashx*/);
        if (num == 0)
            alltargetlist.select(0);
        else
            alltargetlist.setValue(num);

        var subtracter = 7;
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var hour = date.getHours();
        var minute = date.getMinutes();
        var second = date.getSeconds();
        var oldday = 0;
        var oldmonth = 0;
        var oldyear = 0;
        if (day <= subtracter) {
            if (month == 04 || month == 06 || month == 08 || month == 09 || month == 11 || month == 01) {
                oldday = 31 - (subtracter - day);
                oldmonth = month - 1;
                oldyear = year;
            }
            if (month == 05 || month == 07 || month == 10 || month == 12) {
                oldday = 30 - (subtracter - day);
                oldmonth = month - 1;
                oldyear = year;
            }
            if (month == 03) {
                if (((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)) {
                    oldday = 29 - (subtracter - day);
                }
                else {
                    oldday = 28 - (subtracter - day);
                }
                oldmonth = month - 1;
                oldyear = year;
            }
            if (month == 02) {
                oldday = 31 - (subtracter - day);
                oldmonth = month - 1;
                oldyear = year - 1;
            }
        } else {
            oldday = day - subtracter;
            oldmonth = month;
            oldyear = year;
        }

        mini.get('txtstarttime').setValue(oldyear + '-' + oldmonth + '-' + oldday + ' ' + hour + ':' + minute + ':' + second);
        mini.get('txtendtime').setValue(year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second);
        mini.get('trackbikedatagrid').setData(null);
        var trackbackD = mini.get("trackbackDiv");
        trackbackD.show('center', 'middle');
    },
    exportCarTrackback: function () {
        var /*num = $('#spanNum').html()*/num = mini.get('txtvessel').getValue(),
	        trace_starttime = mini.get('txtstarttime').getFormValue(),
	        trace_endtime = mini.get('txtendtime').getFormValue();


        $.ajax({
            url: 'actions/get_exporttrackback.ashx?txtvessel=' + num + "&txtstarttime=" + trace_starttime + "&txtendtime=" + trace_endtime,
            type: 'GET',
            success: function (data) {
                var downUrl = "actions/exportCarTrack.ashx?fullFileName=" + data;
                window.location = downUrl;
            }
        });
    },
    onTreeMenu: function (e) {
        var str = T_Index.selTreeNode.id,
			arr = str.split(','),
			num = arr[0],
            menuItemText = $.trim(e.sender.getText());
        switch (menuItemText) {
            case "地图显示尾迹":
                T_Index.onTreeMenu_weijichaxun(num);
                break;
            case "列表显示尾迹":
                T_Index.onTreeMenu_weijiliebiao(num);
                break;
            case "轨迹回放":
                T_Index.onTreeMenu_guijihuifang(num);
                break;
            case "保险查询":
                T_Index.onTreeMenu_baoxianchaxun(num);
                break;
            case "单参数设置": //终端单个参数设置，hillpig 
                T_Index.onTreeMenu_dancanshushezhi(num);
                break;
            case "批量参数设置": //终端批量参数设置，hillpig 
                T_Index.onTreeMenu_piliangcanshushezhi(num);
                break;
            case "终端区域设置": //hillpig 
                T_Index.onTreeMenu_zhongduanquyushezhi(num);
                break;
            case "调位指令": //hillpig 
                T_Index.onTreeMenu_diaoweizhiling(num);
                break;
            case '历史状态':
                T_Index.onTreeMenu_lishizhuangtai(num);
                break;
            case '历史报警':
                T_Index.onTreeMenu_lishibaojing(num);
                break;
            case '导出车辆轨迹信息':
                T_Index.onTreeMenu_exportvehicletrackdetails(num);
                break;
            case '导出车辆详细信息':
                T_Index.onTreeMenu_exportvehicledetails(num);
                break;
        }
    },
    historicAlarmQuery: function () {
        var num = $('#historicAlarmNum').html(),
	        historicAlarm_starttime = mini.get('historicAlarm_starttime').getFormValue(),
	        historicAlarm_endtime = mini.get('historicAlarm_endtime').getFormValue(),
            targetAlarmType = mini.get('targetAlarmType').getValue();

        var tabMain = mini.get('tabMain'),
			tabHistoryAlarm = tabMain.getTab('tabHistoryAlarm');
        tabHistoryAlarm.visible = true;
        tabMain.updateTab(tabHistoryAlarm);
        tabMain.activeTab(tabHistoryAlarm);
        mini.get('layoutMain').expandRegion('south');

        var dgHistoryAlarm = mini.get('dgHistoryAlarm');
        dgHistoryAlarm.load({ 'num': num, 'historicAlarm_starttime': historicAlarm_starttime, 'historicAlarm_endtime': historicAlarm_endtime, 'targetAlarmType': targetAlarmType });
    },
    historicStatusQuery: function () {
        var num = $('#historicStatusNum').html(),
	        historicStatus_starttime = mini.get('historicStatus_starttime').getFormValue(),
	        historicStatus_endtime = mini.get('historicStatus_endtime').getFormValue();

        var tabMain = mini.get('tabMain'),
			tabHistoryState = tabMain.getTab('tabHistoryState');
        tabHistoryState.visible = true;
        tabMain.updateTab(tabHistoryState);
        tabMain.activeTab(tabHistoryState);
        mini.get('layoutMain').expandRegion('south');

        var dgHistoryState = mini.get('dgHistoryState');
        dgHistoryState.load({ 'num': num, 'historicStatus_starttime': historicStatus_starttime, 'historicStatus_endtime': historicStatus_endtime });

    },
    bikeQuery: function () {

        mini.get('TrackBegin').disable();
        mini.get('TrackRun').disable();
        mini.get('TrackShow').disable();

        var /*num = $('#spanNum').html()*/num = mini.get('alltargetlist').getValue(),
	        trace_starttime = mini.get('trace_starttime').getFormValue(),
	        trace_endtime = mini.get('trace_endtime').getFormValue();

        if (trace_starttime.length == 0 || trace_endtime.length == 0) {
            mini.alert('请设置开始、结束时间!');
            return;
        }
        $.ajax({
            url: 'actions/get_trace.ashx?num=' + num + "&trace_starttime=" + trace_starttime + "&trace_endtime=" + trace_endtime,
            type: 'GET',
            error: function () { },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }
                if (data == '空') {
                    mini.alert("您好,查询轨迹结果为空,请调整时间后查询");
                    return;
                }
                if (data == '开始时间大于结束时间') {
                    mini.alert("您好,您设置的开始时间大于结束时间,请调整时间后查询");
                    return;
                }
                if (data == '异常') {
                    mini.alert("您好,查询轨迹出现异常,请联系相关人员");
                    return;
                }
                if (data == '时间超出所配置的时间间隔') {
                    mini.alert("您好，你查询的轨迹时间间隔超出所配置的时间间隔,请修改后查询");
                    return;
                }
                var arr = data.split(',');
                if (arr.length < 4) {
                    mini.alert('没有轨迹信息！');
                    return;
                }
                T_Index.pts = [];
                mini.get('TrackBegin').enable();
                mini.get('TrackRun').enable();
                mini.get('TrackShow').enable();
                mini.get('TrackQuery').disable();
                for (var i = 0, l = arr.length; i < l; i += 3) {
                    var pt = new TLngLat(arr[i], arr[i + 1]);
                    pt._time = arr[i + 2];
                    T_Index.pts.push(pt);
                }

                var result = '查询成功,有' + T_Index.pts.length + '条记录';
                mini.get('QueryResult').setValue(result);

                if (T_Index.traceMgr) {//上次的traceMgr
                    tiandiMap.removeOverLay(T_Index.traceMgr._overlay);
                    tiandiMap.removeOverLay(T_Index.traceMgr._marker);
                    T_Index.traceMgr = null;
                }
            }
        });

    },
    /**
    * 查询电动车
    */
    bikeQuery2: function (trace_starttime, trace_endtime) {
        mini.get('TrackBegin').disable();
        mini.get('TrackRun').disable();
        mini.get('TrackShow').disable();

        //alert(trace_starttime + ',' + trace_endtime);
        var num = mini.get('alltargetlist').getValue();

        $.ajax({
            url: 'actions/get_trace.ashx?num=' + num + "&trace_starttime=" + trace_starttime + "&trace_endtime=" + trace_endtime + "&mapSource=baidu",
            type: 'GET',
            error: function () { },
            success: function (data) {

                if (data.lendth <= 0) {
                    return;
                }
                if (data == '空') {
                    mini.alert("您好,查询轨迹结果为空,请调整时间后查询");
                    return;
                }
                if (data == '开始时间大于结束时间') {
                    mini.alert("您好,您设置的开始时间大于结束时间,请调整时间后查询");
                    return;
                }
                if (data == '异常') {
                    mini.alert("您好,查询轨迹出现异常,请联系相关人员");
                    return;
                }
                if (data == '时间超出所配置的时间间隔') {
                    mini.alert("您好，你查询的轨迹时间间隔超出所配置的时间间隔,请修改后查询");
                    return;
                }
                var arr = data.split(',');
                //pts = [];
                if (arr.length < 4) {
                    mini.alert('没有轨迹信息！');
                    return;
                }
                mini.get('TrackBegin').enable();
                mini.get('TrackRun').enable();
                mini.get('TrackShow').enable();
                mini.get("TrackQuery").disable();
                mini.get("TraceToday").disable();
                mini.get("TraceYestoday").disable();
                T_Index.pts = [];
                for (var i = 0, l = arr.length; i < l; i += 3) {
                    var pt = new TLngLat(arr[i], arr[i + 1]);
                    pt._time = arr[i + 2];
                    T_Index.pts.push(pt);
                }

                if (Index.traceMgr) {//上次的traceMgr
                    tiandiMap.removeOverlay(T_Index.traceMgr._overlay);
                    tiandiMap.removeOverlay(T_Index.traceMgr._marker);
                    T_Index.traceMgr = null;
                }
                var result = '查询成功,有' + T_Index.pts.length + '条记录';
                mini.get('QueryResult').setValue(result);
            }
        });
    },

    bikeQueryToday: function () {
        trace_starttime = getNowEarly();
        trace_endtime = getNowFormatDate();
        mini.get('trace_starttime').setValue(trace_starttime);
        mini.get('trace_endtime').setValue(trace_endtime);

        var me = this;
        me.bikeQuery2(trace_starttime, trace_endtime);
    },
    bikeQueryYestoday: function () {
        trace_starttime = getYestodayEarly();
        trace_endtime = getYestodayLatest();
        mini.get('trace_starttime').setValue(trace_starttime);
        mini.get('trace_endtime').setValue(trace_endtime);


        var me = this;
        me.bikeQuery2(trace_starttime, trace_endtime);
    },

    trackSpeedChange: function () {
        if (T_Index.traceMgr) {
            var spd1 = mini.get('velocityList').getValue();
            var pauseTime1 = T_Index.getTrackPauseTime(spd1);
            var landMkrs = [];
            for (var i = 0, l = T_Index.pts.length; i < l; i++) {
                var ldMkr = T_Index.pts[i];
                ldMkr.html = '坐标: ' + ldMkr.lng + ', ' + ldMkr.lat + '<br/>时间: ' + ldMkr._time;
                ldMkr.pauseTime = pauseTime1;
                landMkrs.push(ldMkr);
            }
            T_Index.traceMgr._opts.landmarkPois = landMkrs;
            T_Index.traceMgr._opts.speed = spd1;
            T_Index.traceMgr.start();
        }
    },
    timeChange: function () {
        mini.get("TrackQuery").enable();
        mini.get('TrackBegin').disable();
        mini.get('TrackRun').disable();
        mini.get('TrackShow').enable();
        $('#TrackBegin').html("回放");
        mini.get("winTrace").setShowCloseButton(true);
        mini.get('QueryResult').setValue("");
        if (T_Index.tracePly) {
            tiandiMap.removeOverlay(T_Index.tracePly);
            tiandiMap.removeOverlay(T_Index.tracePly.getPath());
        }
        T_Index.pts = [];
        if (T_Index.traceMgr) {//上次的traceMgr
            tiandiMap.removeOverlay(T_Index.traceMgr._overlay);
            tiandiMap.removeOverlay(T_Index.traceMgr._marker);
            T_Index.traceMgr.stop();
            T_Index.traceMgr = null;
        }
    },
    bikeRun: function () {
        mini.get('TrackShow').disable();
        mini.get("TrackQuery").disable();
        var a = $('#TrackBegin').html();
        if (a.toString().indexOf("回放") > -1) {
            mini.get('QueryResult').setValue("");
            $('#TrackBegin').html("结束");
            mini.get("winTrace").setShowCloseButton(false);
            if (T_Index.traceMgr) {//上次的traceMgr
                tiandiMap.removeOverLay(T_Index.traceMgr._overlay);
                tiandiMap.removeOverLay(T_Index.traceMgr._marker);
                T_Index.traceMgr = null;
            }
            var /*num = $('#spanNum').html()*/num = mini.get('alltargetlist').getValue(),
                tip = num + '',
                pauseTime = 1,
			    spd = mini.get('velocityList').getValue();
            pauseTime = T_Index.getTrackPauseTime(spd);
            var landMkrs = [];
            for (var i = 0, l = T_Index.pts.length; i < l; i++) {
                var ldMkr = T_Index.pts[i];
                ldMkr.html = '坐标: ' + ldMkr.lng + ', ' + ldMkr.lat + '<br/>时间: ' + ldMkr._time;
                ldMkr.pauseTime = pauseTime;
                landMkrs.push(ldMkr);
            }

            T_Index.traceMgr = new BMapLib.T_LuShu(tiandiMap, T_Index.pts, {
                defaultContent: tip,
                landmarkPois: landMkrs,
                speed: spd
            });

            tiandiMap.removeOverLay(T_Index.tracePly);
            T_Index.tracePly = new TPolyline(T_Index.pts, {
                strokeColor: '#FF0000'
            });

            tiandiMap.addOverLay(T_Index.tracePly);
            tiandiMap.setViewport(T_Index.tracePly.getLngLats());

            var spd = mini.get('velocityList').getValue();
            T_Index.traceMgr._opts.speed = spd;

            T_Index.traceMgr.start();
        }
        else {
            mini.get('TrackBegin').disable();
            mini.get('TrackRun').disable();
            mini.get('TrackShow').enable();
            mini.get('TrackQuery').enable();
            $('#TrackBegin').html("回放");
            $('#TrackRun').html("暂停");
            mini.get("winTrace").setShowCloseButton(true);

            tiandiMap.removeOverLay(T_Index.traceMgr._overlay);
            tiandiMap.removeOverLay(T_Index.traceMgr._marker);
            tiandiMap.removeOverLay(T_Index.tracePly);
            T_Index.pts = [];
            T_Index.traceMgr.stop();
            T_Index.traceMgr = null;
            mini.get("winTrace").hide();
        }
    },
    getTrackPauseTime: function (speed) {
        var pauseTime = 1;
        if (speed == 1000) {
            pauseTime = 2;
        } else if (speed == 2000) {
            pauseTime = 1;
        } else if (speed == 4000) {
            pauseTime = 0.5;
        } else if (speed == 8000) {
            pauseTime = 0.1;
        } else if (speed == 32000) {
            pauseTime = 0.02;
        }
        return pauseTime;
    },
    bikePause: function () {
        var a = $('#TrackRun').html();
        if (a.toString().indexOf("暂停") > -1) {
            $('#TrackRun').html("继续");
            if (T_Index.traceMgr) {
                T_Index.traceMgr.pause();
            }
        }
        else {
            $('#TrackRun').html("暂停");
            if (T_Index.traceMgr) {
                //                var spd1 = mini.get('velocityList').getValue();
                //                var pauseTime1 = T_Index.getTrackPauseTime(spd1);
                //                var landMkrs = [];
                //                for (var i = 0, l = T_Index.pts.length; i < l; i++) {
                //                    var ldMkr = T_Index.pts[i];
                //                    ldMkr.html = '坐标: ' + ldMkr.lng + ', ' + ldMkr.lat + '<br/>时间: ' + ldMkr._time;
                //                    ldMkr.pauseTime = pauseTime1;
                //                    landMkrs.push(ldMkr);
                //                }
                //                T_Index.traceMgr._opts.landmarkPois = landMkrs;
                //                T_Index.traceMgr._opts.speed = spd1;
                T_Index.traceMgr.start();
            }
        }
    },
    bikeShow: function () {
        mini.get('TrackQuery').disable();
        var /*num = $('#spanNum').html()*/num = mini.get('alltargetlist').getValue(),
	        trace_starttime = mini.get('trace_starttime').getFormValue(),
	        trace_endtime = mini.get('trace_endtime').getFormValue();

        if (trace_starttime.length == 0 || trace_endtime.length == 0) {
            mini.alert('请设置开始、结束时间!');
            return;
        }
        $.ajax({
            //url: 'actions/show_trace.ashx?num=' + num + "&trace_starttime=" + trace_starttime + "&trace_endtime=" + trace_endtime + "&mapSource=baidu",
            url: 'actions/show_trace.ashx?num=' + num + "&trace_starttime=" + trace_starttime + "&trace_endtime=" + trace_endtime,
            type: 'GET',
            error: function () { },
            success: function (data) {

                if (data.lendth <= 0) {
                    mini.alert('wujieguo ');
                    return;
                }
                if (data == '空') {
                    mini.alert("您好,查询轨迹结果为空,请调整时间后查询");
                    return;
                }
                if (data == '异常') {
                    mini.alert("您好,查询轨迹出现异常,请联系相关人员");
                    return;
                }
                if (data == '时间超出所配置的时间间隔') {
                    mini.alert("您好,你查询的轨迹时间间隔超出所配置的时间间隔,请修改后查询");
                    return;
                }

                var tabMain = mini.get('tabMain'),
						tabTrackInfo = tabMain.getTab('tabTrackInfo');
                tabTrackInfo.visible = true;
                tabMain.updateTab(tabTrackInfo);
                tabMain.activeTab(tabTrackInfo);
                mini.get('layoutMain').expandRegion('south');
                mini.get('TrackQuery').enable();
                var arr = null;
                try { eval("arr=" + data) } catch (e) { }
                mini.get('dgTrackInfo').setData(arr);
            }
        });
    },
    onSoleParameterSettingOk: function () {//TTargetConfigSetting单参数设置，hillpig   

        var input = mini.get("#spsValue");
        mini.get('spsValue').on("validation", curIndex.Allvalidation);
        if (input.isValid() == false) {
            mini.alert('输入值不合法,请重新输入');
            return;
        }

        var num = $('#spanSPSNum').html(),
	        spsPara = mini.get('spsPara').getValue(),
	        spsValue = mini.get('spsValue').getValue();

        var param = "settingType=SoleParameterSetting"
            + "&num=" + num
	        + "&spsPara=" + spsPara
	        + "&spsValue=" + spsValue;

        $.ajax({
            url: 'actions/send_message.ashx?' + param,
            type: 'GET',
            error: function () { },
            success: function (data) {
                mini.get("winSoleParameterSetting").hide();
                mini.alert('发送成功!');
            }
        });
    },
    onSoleParameterSettingCancel: function () {
        mini.get("winSoleParameterSetting").hide();
    },
    onBatchParameterSettingOk: function () {//TTargetConfigSetting批量参数设置，hillpig   
        var input1 = mini.get("#cfgValue1");
        var input2 = mini.get("#cfgValue2");
        var input3 = mini.get("#cfgValue3");
        var input4 = mini.get("#cfgValue4");
        var input5 = mini.get("#cfgValue5");
        var input6 = mini.get("#cfgValue6");
        var input7 = mini.get("#cfgValue7");
        var input8 = mini.get("#cfgValue8");
        var input10 = mini.get("#cfgValue10");
        var input11 = mini.get("#cfgValue11");
        var input12 = mini.get("#cfgValue12");
        var input13 = mini.get("#cfgValue13");
        input1.validate();
        input2.validate();
        input3.validate();
        input4.validate();
        input5.validate();
        input6.validate();
        input7.validate();
        input8.validate();
        input10.validate();
        input11.validate();
        input12.validate();
        input13.validate();
        if ((input1.isValid() == false) || (input2.isValid() == false) || (input3.isValid() == false) || (input4.isValid() == false) || (input5.isValid() == false) || (input6.isValid() == false) || (input7.isValid() == false) || (input8.isValid() == false) || (input10.isValid() == false) || (input11.isValid() == false) || (input12.isValid() == false) || (input13.isValid() == false)) {
            mini.alert('输入值不合法,请重新输入');
            return;
        }

        var num = $('#spanBPSNum').html(),
	        cfgValue1 = mini.get('cfgValue1').getValue(),
	        cfgValue2 = mini.get('cfgValue2').getValue(),
            cfgValue3 = mini.get('cfgValue3').getValue(),
            cfgValue4 = mini.get('cfgValue4').getValue(),
            cfgValue5 = mini.get('cfgValue5').getValue(),
            cfgValue6 = mini.get('cfgValue6').getValue(),
            cfgValue7 = mini.get('cfgValue7').getValue(),
            cfgValue8 = mini.get('cfgValue8').getValue(),
            cfgValue9 = mini.get('cfgValue9').getValue(),
            cfgValue10 = mini.get('cfgValue10').getValue(),
            cfgValue11 = mini.get('cfgValue11').getValue(),
            cfgValue12 = mini.get('cfgValue12').getValue(),
            cfgValue13 = mini.get('cfgValue13').getValue();

        var param = "settingType=BatchParameterSetting"
                    + "&num=" + num
	                + "&cfgValue1=" + cfgValue1
                    + "&cfgValue2=" + cfgValue2
                    + "&cfgValue3=" + cfgValue3
                    + "&cfgValue4=" + cfgValue4
                    + "&cfgValue5=" + cfgValue5
                    + "&cfgValue6=" + cfgValue6
                    + "&cfgValue7=" + cfgValue7
                    + "&cfgValue8=" + cfgValue8
                    + "&cfgValue9=" + cfgValue9
                    + "&cfgValue10=" + cfgValue10
                    + "&cfgValue11=" + cfgValue11
                    + "&cfgValue12=" + cfgValue12
	                + "&cfgValue13=" + cfgValue13;

        $.ajax({
            url: 'actions/send_message.ashx?' + param,
            type: 'GET',
            error: function () { },
            success: function (data) {
                mini.get("winBatchParameterSetting").hide();
                mini.alert('发送成功!');
            }
        });
    },
    onUserParameterSettingOk: function () {//TTargetConfigSetting单参数设置，hillpig
        var input = mini.get("userConfigValue");
        mini.get('userConfigValue').on("validation", curIndex.UserConfigvalidation);
        input.validate();
        if (input.isValid() == false) {
            mini.alert('输入值不合法,请重新输入');
            return;
        }

        var spsPara = mini.get('userConfig').getValue(),
	        spsValue = mini.get('userConfigValue').getValue();

        var param = "userConfig=" + spsPara + "&userConfigValue=" + spsValue;

        $.ajax({
            url: 'actions/setUserConfig.ashx?' + param,
            type: 'GET',
            error: function () { },
            success: function (data) {
                mini.alert('设置成功,刷新界面或者下次登录显示效果!');
            }
        });
    },
    onBatchParameterSettingCancel: function () {
        mini.get("winBatchParameterSetting").hide();
    },
    //修改密码
    changePwd: function (e) {
        var oldPwd = mini.get('oldPwd').getValue(),
            newPwd = mini.get('newPwd').getValue(),
	        newPwd2 = mini.get('newPwd2').getValue();
        if (newPwd != newPwd2) {
            mini.alert('两次输入的密码不一致!');
            return;
        }
        var param = "oldPwd=" + oldPwd + "&newPwd=" + newPwd;
        $.ajax({
            url: 'actions/changePwd.ashx?' + param,
            type: 'GET',
            error: function () { },
            success: function (data) {
                if (data == '0') {
                    mini.alert('密码修改成功！');
                }
                else if (data == '-2') {
                    mini.alert('旧密码输入错误！');
                }
                else {
                    mini.alert('密码修改失败！');
                }
            }
        });
    },
    onMessageOk: function () {
        var rLongitude = mini.get("#rLongitude");
        var rLatitude = mini.get("#rLatitude");
        var lLatitude = mini.get("#lLatitude");
        var lLongitude = mini.get("#lLongitude");
        rLongitude.validate();
        rLatitude.validate();
        lLatitude.validate();
        lLongitude.validate();
        if ((rLongitude.isValid() == false) || (rLatitude.isValid() == false) || (lLatitude.isValid() == false) || (lLongitude.isValid() == false)) {
            mini.alert('输入的经纬度不合法,请重新输入');
            return;
        }
        var num = $('#spanMessageNum').html(),
            lLatitude = mini.get('lLatitude').getValue(),
	        lLongitude = mini.get('lLongitude').getValue(),
	        rLatitude = mini.get('rLatitude').getValue(),
	        rLongitude = mini.get('rLongitude').getValue(),
	        AlarmType = mini.get('AlarmType').getValue();
        if (AlarmType == "") {
            mini.alert("请选择报警类型")
            return;
        }
        var param = "settingType=RangeSetting"
                    + "&num=" + num
	                + "&lLatitude=" + lLatitude
	                + "&lLongitude=" + lLongitude
	                + "&rLatitude=" + rLatitude
	                + "&rLongitude=" + rLongitude
	                + "&AlarmType=" + AlarmType;

        $.ajax({
            url: 'actions/send_message.ashx?' + param,
            type: 'GET',
            error: function () { },
            success: function (data) {
                mini.get("winMessage").hide();
                mini.alert('发送成功!');
            }
        });
    },
    onMessageCancel: function () {
        mini.get("winMessage").hide();
    },
    onTargetSearchOk: function () {
        mini.get('targetSearchDel').show();
        var num = $('#spanMessageNum').html(),
        keyOfTargetSearch = mini.get('targetSearch').getValue();
        var param = "key=" + keyOfTargetSearch;
        mini.get('treeMain').setUrl('actions/get_json_tree.ashx?' + param);

        //        var num = $('#spanMessageNum').html(),
        //            keyOfTargetSearch = mini.get('targetSearch').getValue();

        //        var param = "key=" + keyOfTargetSearch;

        //        $.ajax({
        //            url: 'actions/search_target_by_key.ashx?' + param,
        //            type: 'GET',
        //            error: function () { },
        //            success: function (data) {
        //                var tabMain = mini.get('tabMain'),
        //				    tabTargetSearchInfo = tabMain.getTab('tabTargetSearchInfo');
        //                tabTargetSearchInfo.visible = true;
        //                tabMain.updateTab(tabTargetSearchInfo);
        //                tabMain.activeTab(tabTargetSearchInfo);

        //                var arr = null;
        //                try { eval("arr=" + data) } catch (e) { }
        //                mini.get('dgTargetSearchInfo').setData(arr);
        //            }
        //        });
    },
    onTargetSearchDel: function () {
        mini.get('targetSearchDel').hide();
        var num = $('#spanMessageNum').html();
        mini.get('targetSearch').setValue("");
        var keyOfTargetSearch = mini.get('targetSearch').getValue();
        var param = "key=" + keyOfTargetSearch;
        mini.get('treeMain').setUrl('actions/get_json_tree.ashx?' + param);
    },
    //查询网点结果处理函数
    onStationSearchOk: function () {
        mini.get('stationSearchDel').show();
        var keyOfStationSearch = mini.get('stationSearch').getValue();
        var param = "key=" + keyOfStationSearch;
        mini.get('treeStation').setUrl('actions/get_service_station.ashx?' + param);
    },
    //查询网点取消叉号后恢复结构
    onStateSearchDel: function () {
        mini.get('stationSearchDel').hide();
        mini.get('stationSearch').setValue("");
        var keyOfStationSearch = mini.get('stationSearch').getValue();
        var param = "key=" + keyOfStationSearch;
        mini.get('treeStation').setUrl('actions/get_service_station.ashx?' + param);
    },
    onddyfzvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^(3[0-5]{1}[0-9]{5})$");
            if (re.test(e.value) == false) {
                e.errorText = "低电压阈值的范围为3000000-3500000";
                e.isValid = false;
            }
        }
    },
    onzcdyfzvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^(40{1}[0-9]{5}|4100000)$");
            if (re.test(e.value) == false) {
                e.errorText = "正常电压阈值的范为4000000-4100000";
                e.isValid = false;
            }
        }
    },
    onzdbjfzvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^([1-9]|[1-5]{1}[0-9]{1}|60)$");
            if (re.test(e.value) == false) {
                e.errorText = "震动报警阈值的范围为0-60";
                e.isValid = false;
            }
        }
    },
    onzddjfzvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("(^[0-9])$");
            if (re.test(e.value) == false) {
                e.errorText = "震动等级报警阈值的范围为0-9";
                e.isValid = false;
            }
        }
    },
    onwybjfzvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^(1[0-9]{2}|200|[1-9][0-9])$");
            if (re.test(e.value) == false) {
                e.errorText = "位移阈值的范围为10-200";
                e.isValid = false;
            }
        }
    },
    onaddcarvalidation: function (e) {
        if (e.isValid) {
            var str = e.value;
            if (str.indexOf(",") < 0) {
                e.errorText = "位置,电话号码(如1,18910406091);位置范围为 0到30";
                e.isValid = false;
            }
            var re = new RegExp("^([1-2]?[0-9]|30),1[3|4|5|8]{1}[0-9]{9}$");
            if (re.test(e.value) == false) {
                e.errorText = "位置,电话号码(如1,18910406091);位置范围为 0到30";
                e.isValid = false;
            }
        }
    },
    ondelcarvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^([1-2]?[1-9]|20|30)$");
            if (re.test(e.value) == false) {
                e.errorText = "位置(如1);位置范围为1到30";
                e.isValid = false;
            }
        }
    },
    onipportvalidation: function (e) {
        if (e.isValid) {
            var str = e.value,
                    count = 0;
            while (str.indexOf(".") > -1) {
                str = str.substr(str.indexOf(".") + 1);
                count++;
            }
            if (count != 3) {
                e.errorText = "ip,port;xxx.xxx.xxx.xxx,yyyyy";
                e.isValid = false;
            }
            var re = new RegExp("^((1[0-9]{2}|2[0-4][0-9]|25[0-5]|[1-9]?[0-9])\.){3}(1[0-9]{2}|2[0-4][0-9]|25[0-5]|[1-9]?[0-9]),[1-9]?[0-9]{1,4}$");
            if (re.test(e.value) == false) {
                e.errorText = "ip,port;xxx.xxx.xxx.xxx,yyyyy";
                e.isValid = false;
            }
        }
    },
    onsmsvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^[0-1]{1}$");
            if (re.test(e.value) == false) {
                e.errorText = "允许,输入1,禁止,输入0;为了保证您车辆的安全,请设置为1";
                e.isValid = false;
            }
        }
    },
    onbuploadvalidation: function (e) {
        if (e.isValid) {
            var str = e.value;
            if (str.indexOf(",") < 0) {
                e.errorText = "此配置表示终端断开电源到休眠的时间间隔中上报位置的间隔和次数;十六进制（间隔,次数）XXXX，XXXX,默认间隔为32秒,次数不限";
                e.isValid = false;
            }
            var re = new RegExp("^[0-9A-F]{4},[0-9A-F]{4}$");
            if (re.test(e.value) == false) {
                e.errorText = "此配置表示终端断开电源到休眠的时间间隔中上报位置的间隔和次数;十六进制（间隔,次数）XXXX，XXXX,默认间隔为32秒,次数不限";
                e.isValid = false;
            }
        }
    },
    onauploadvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^[0-9A-F]{4}$");
            if (re.test(e.value) == false) {
                e.errorText = "此配置表示终端在电源开启的时候上报位置的间隔和次数;十六进制（间隔）XXXX,默认间隔为32秒";
                e.isValid = false;
            }
        }
    },
    onoverspeedvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^[0-9A-F]{2}$");
            if (re.test(e.value) == false) {
                e.errorText = "车辆行驶速度大于设置阈值,终端发生报警,此参数为十六进制,单位节,默认输入值为10,十进制:16节,即:29km/h";
                e.isValid = false;
            }
        }
    },
    ongorestvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^[0-9A-F]{4}$");
            if (re.test(e.value) == false) {
                e.errorText = "车辆关闭电源后进入休眠状态的时间间隔,此参数为十六进制,单位秒,默认输入值为0100,即256秒";
                e.isValid = false;
            }
        }
    },
    onlongitudevalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^((([1-9]|[1-9][0-9]|1[0-7][0-9])[0-9]{6})|180000000)$");
            if (re.test(e.value) == false) {
                e.errorText = "经度判断,区域经度阈值为1000000-180000000";
                e.isValid = false;
            }
        }
    },
    onlatitudevalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^((([1-9]|[1-8][0-9])[0-9]{6})|90000000)$");
            if (re.test(e.value) == false) {
                e.errorText = "纬度判断,区域经度阈值为1000000-90000000";
                e.isValid = false;
            }
        }
    },
    changeSoleParaSel: function (e) {
        var spsParaValue = e.sender.text,
            objSpsValue = mini.get('spsValue');
        objSpsDesc = mini.get('spsDesc');
        switch (spsParaValue) {
            case "低电压报警阈值":
                objSpsValue.setValue("3400000");
                objSpsDesc.setValue("当终端中电池容量小于阈值时,终端发出报警,输入范围为3000000到3500000");
                break;
            case "正常电压阈值":
                objSpsValue.setValue("4000000");
                objSpsDesc.setValue("当终端中电池容量大于阈值时,终端发出报警,输入范围为4000000到4100000");
                break;
            case "震动报警阈值":
                objSpsValue.setValue("30");
                objSpsDesc.setValue("当终端震动幅度大于阈值时,终端发出报警,输入范围为0到60");
                break;
            case "震动等级报警阈值":
                objSpsValue.setValue("3");
                objSpsDesc.setValue("当终端震动等级大于阈值时,终端发出报警,输入范围为0到9");
                break;
            case "位移报警阈值":
                objSpsValue.setValue("50");
                objSpsDesc.setValue("当终端在ACC关移动距离大于阈值时,终端发出报警,输入范围为10到200");
                break;
            case "增加车主":
                objSpsValue.setValue("1,18910406091");
                objSpsDesc.setValue("位置,电话号码(如1,18910406091);位置范围为 0到30,当相同位置有电话号码时,原电话号码被替代");
                break;
            case "删除车主":
                objSpsValue.setValue("1");
                objSpsDesc.setValue("位置(如1);位置范围为1到30");
                break;
            case "设置IP地址与端口号":
                objSpsValue.setValue("114.242.203.139,6007");
                objSpsDesc.setValue("ip,port;xxx.xxx.xxx.xxx,yyyyy");
                break;
            case "是否允许短信上传信息":
                objSpsValue.setValue("1");
                objSpsDesc.setValue("允许,输入1,禁止,输入0;为了保证您车辆的安全,请设置为1");
                break;
            case "定时回传时间间隔和定时回传次数":
                objSpsValue.setValue("0020,FFFF");
                objSpsDesc.setValue("此配置表示终端断开电源到休眠的时间间隔中上报位置的间隔和次数;十六进制（间隔,次数）XXXX，XXXX,默认间隔为32秒,次数不限");
                break;
            case "在线待命回传间隔":
                objSpsValue.setValue("0020");
                objSpsDesc.setValue("此配置表示终端在电源开启的时候上报位置的间隔和次数;十六进制（间隔）XXXX,默认间隔为32秒");
                break;
            case "车速报警阈值":
                objSpsValue.setValue("10");
                objSpsDesc.setValue("车辆行驶速度大于设置阈值,终端发生报警,此参数为十六进制,单位节,默认输入值为10,十进制:16节,即:29km/h");
                break;
            case "ACC熄火进入休眠时间":
                objSpsValue.setValue("0100");
                objSpsDesc.setValue("车辆关闭电源后进入休眠状态的时间间隔,此参数为十六进制,单位秒,默认输入值为0100,即256秒");
                break;
        }
    },
    Allvalidation: function (e) {
        var spsParaValue = mini.get('spsPara').getText();
        switch (spsParaValue) {
            case "低电压报警阈值":
                if (e.isValid) {
                    var re = new RegExp("^(3[0-5]{1}[0-9]{5})$");
                    if (re.test(e.value) == false) {
                        e.errorText = "低电压阈值的范围为3000000-3500000";
                        e.isValid = false;
                    }
                }
                break;
            case "正常电压阈值":
                if (e.isValid) {
                    var re = new RegExp("^(40{1}[0-9]{5}|4100000)$");
                    if (re.test(e.value) == false) {
                        e.errorText = "正常电压阈值的范为4000000-4100000";
                        e.isValid = false;
                    }
                }
                break;
            case "震动报警阈值":
                if (e.isValid) {
                    var re = new RegExp("^([1-9]|[1-5]{1}[0-9]{1}|60)$");
                    if (re.test(e.value) == false) {
                        e.errorText = "震动报警阈值的范围为0-60";
                        e.isValid = false;
                    }
                }
                break;
            case "震动等级报警阈值":
                if (e.isValid) {
                    var re = new RegExp("^([0-9])$");
                    if (re.test(e.value) == false) {
                        e.errorText = "震动等级报警阈值的范围为0-9";
                        e.isValid = false;
                    }
                }
                break;
            case "位移报警阈值":
                if (e.isValid) {
                    var re = new RegExp("^(1[0-9]{2}|200|[1-9][0-9])$");
                    if (re.test(e.value) == false) {
                        e.errorText = "位移阈值的范围为10-200";
                        e.isValid = false;
                    }
                }
                break;
            case "增加车主":
                if (e.isValid) {
                    var str = e.value;
                    if (str.indexOf(",") < 0) {
                        e.errorText = "位置,电话号码(如1,18910406091);位置范围为 0到30";
                        e.isValid = false;
                    }
                    var re = new RegExp("^([1-2]?[0-9]|30),1[3|4|5|8]{1}[0-9]{9}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "位置,电话号码(如1,18910406091);位置范围为 0到30";
                        e.isValid = false;
                    }
                }
                break;
            case "删除车主":
                if (e.isValid) {
                    var re = new RegExp("^([1-2]?[1-9]|20|30)$");
                    if (re.test(e.value) == false) {
                        e.errorText = "位置(如1);位置范围为1到30";
                        e.isValid = false;
                    }
                }
                break;
            case "设置IP地址与端口号":
                if (e.isValid) {
                    var str = e.value,
                    count = 0;
                    while (str.indexOf(".") > -1) {
                        str = str.substr(str.indexOf(".") + 1);
                        count++;
                    }
                    if (count != 3) {
                        e.errorText = "ip,port;xxx.xxx.xxx.xxx,yyyyy";
                        e.isValid = false;
                    }
                    var re = new RegExp("^((1[0-9]{2}|2[0-4][0-9]|25[0-5]|[1-9]?[0-9])\.){3}(1[0-9]{2}|2[0-4][0-9]|25[0-5]|[1-9]?[0-9]),[1-9]?[0-9]{1,4}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "ip,port;xxx.xxx.xxx.xxx,yyyyy";
                        e.isValid = false;
                    }
                }
                break;
            case "是否允许短信上传信息":
                if (e.isValid) {
                    var re = new RegExp("^[0-1]{1}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "允许,输入1,禁止,输入0;为了保证您车辆的安全,请设置为1";
                        e.isValid = false;
                    }
                }
                break;
            case "定时回传时间间隔和定时回传次数":
                if (e.isValid) {
                    var str = e.value;
                    if (str.indexOf(",") < 0) {
                        e.errorText = "此配置表示终端断开电源到休眠的时间间隔中上报位置的间隔和次数;十六进制（间隔,次数）XXXX，XXXX,默认间隔为32秒,次数不限";
                        e.isValid = false;
                    }
                    var re = new RegExp("^[0-9A-F]{4},[0-9A-F]{4}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "此配置表示终端断开电源到休眠的时间间隔中上报位置的间隔和次数;十六进制（间隔,次数）XXXX，XXXX,默认间隔为32秒,次数不限";
                        e.isValid = false;
                    }
                }
                break;
            case "在线待命回传间隔":
                if (e.isValid) {
                    var re = new RegExp("^[0-9A-F]{4}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "此配置表示终端在电源开启的时候上报位置的间隔和次数;十六进制（间隔）XXXX,默认间隔为32秒";
                        e.isValid = false;
                    }
                }
                break;
            case "车速报警阈值":
                if (e.isValid) {
                    var re = new RegExp("^[0-9A-F]{2}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "车辆行驶速度大于设置阈值,终端发生报警,此参数为十六进制,单位节,默认输入值为10,十进制:16节,即:29km/h";
                        e.isValid = false;
                    }
                }
                break;
            case "ACC熄火进入休眠时间":
                if (e.isValid) {
                    var re = new RegExp("^[0-9A-F]{4}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "车辆关闭电源后进入休眠状态的时间间隔,此参数为十六进制,单位秒,默认输入值为0100,即256秒";
                        e.isValid = false;
                    }
                }
                break;
        }
    },
    changeUserParaSel: function (e) {
        var spsParaValue = e.sender.text,
            objSpsValue = mini.get('userConfigValue');
        objSpsDesc = mini.get('userConfigDesc');
        switch (spsParaValue) {
            case "终端在线阈值":
                objSpsValue.setValue(serverInfo.UserConfigParam.isOnlineSpan);
                objSpsDesc.setValue("单位:小时;终端最后报位时间加上在线阈值大于当前时间,终端的状态为在线状态,范围为1小时到8640小时");
                break;
            case "终端报警阈值":
                objSpsValue.setValue(serverInfo.UserConfigParam.baojingduqufazhi);
                objSpsDesc.setValue("单位:小时;终端最后报警时间加上在线阈值大于当前时间,终端的状态为在线状态,范围为1小时到24小时");
                break;
            case "是否显示离线终端":
                objSpsValue.setValue(serverInfo.UserConfigParam.xianshi_lixianzhongduan);
                objSpsDesc.setValue("当终端不在线的时候,是否将终端显示在地图上.1:显示;0:不显示");
                break;
            case "是否显示终端的名称":
                objSpsValue.setValue(serverInfo.UserConfigParam.xianshi_zhongduanmingcheng);
                objSpsDesc.setValue("当终端在地图上显示为车辆图标的时候,是否同时显示终端的终端名称.1:显示;0:不显示");
                break;
            case "尾迹查询中轨迹点个数":
                objSpsValue.setValue(serverInfo.UserConfigParam.weijigeshu);
                objSpsDesc.setValue("终端尾迹查询,查询的轨迹点个数,以50个为最优,范围为1个到999个");
                break;
            case "轨迹查询开始结束时间间隔":
                objSpsValue.setValue(serverInfo.UserConfigParam.zuidashijianjiange);
                objSpsDesc.setValue("终端轨迹回放,查询开始时间和结束时间的时间间隔,默认为7天,范围为1天到7天");
                break;
            case "轨迹查询是否屏蔽异常定位":
                objSpsValue.setValue(serverInfo.UserConfigParam.guijihuifang_pingbiyichangwei);
                objSpsDesc.setValue("异常定位是指终端在接受不到北斗或者GPS信号时候,产生的非真实数据.1:屏蔽;0:不屏蔽");
                break;
            case "地图刷新时间间隔":
                objSpsValue.setValue(serverInfo.UserConfigParam.ditushuaxinshijianjiange);
                objSpsDesc.setValue("单位:秒;地图刷新时间间隔,范围为10秒到999秒");
                break;
            case "是否显示标签":
                objSpsValue.setValue(serverInfo.UserConfigParam.isShowTag);
                objSpsDesc.setValue("是否显示标签 1:显示 0:不显示");
                break;
        }
    },
    UserConfigvalidation: function (e) {
        var spsParaValue = mini.get('userConfig').getText();
        switch (spsParaValue) {
            case "终端在线阈值":
                if (e.isValid) {
                    var re = new RegExp("^([1-9]|[1-9][0-9]|[1-9][0-9]{2}|[1-7][0-9]{3}|8[1-5][0-9]{2}|86[1-3][0-9]|8640)$");
                    if (re.test(e.value) == false) {
                        e.errorText = "终端在线阈值为1到8640小时,默认为240小时";
                        e.isValid = false;
                    }
                }
                break;
            case "终端报警阈值":
                if (e.isValid) {
                    var re = new RegExp("^([1]?[1-9]|[2]?[1-4]|10|20)$");
                    if (re.test(e.value) == false) {
                        e.errorText = "终端报警阈值为1到24小时,默认为1小时";
                        e.isValid = false;
                    }
                }
                break;
            case "是否显示离线终端":
                if (e.isValid) {
                    var re = new RegExp("^[0-1]{1}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "1:显示,0:不显示";
                        e.isValid = false;
                    }
                }
                break;
            case "是否显示终端的名称":
                if (e.isValid) {
                    var re = new RegExp("^[0-1]{1}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "1:显示,0:不显示";
                        e.isValid = false;
                    }
                }
                break;
            case "尾迹查询中轨迹点个数":
                if (e.isValid) {
                    var re = new RegExp("^([1-9]{1}[0-9]?[0-9]?)$");
                    if (re.test(e.value) == false) {
                        e.errorText = "尾迹查询中轨迹点个数为1到999个";
                        e.isValid = false;
                    }
                }
                break;
            case "轨迹查询开始结束时间间隔":
                if (e.isValid) {
                    //var re = new RegExp("^([1-9]|[1-2][0-9]|30|31)$");
                    var re = new RegExp("^([1-3])$");
                    if (re.test(e.value) == false) {
                        e.errorText = "轨迹查询开始结束时间间隔为1到3天";
                        e.isValid = false;
                    }
                }
                break;
            case "轨迹查询是否屏蔽异常定位":
                if (e.isValid) {
                    var re = new RegExp("^[0-1]{1}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "1:屏蔽,0:不屏蔽";
                        e.isValid = false;
                    }
                }
                break;
            case "地图刷新时间间隔":
                if (e.isValid) {
                    var re = new RegExp("^([1-9]{1}[0-9]{1}[0-9]?)$");
                    if (re.test(e.value) == false) {
                        e.errorText = "地图刷新时间间隔10到999秒";
                        e.isValid = false;
                    }
                }
                break;
            case "是否显示标签":
                if (e.isValid) {
                    var re = new RegExp("^[0-1]{1}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "1:显示 0:不显示";
                        e.isValid = false;
                    }
                }
                break;
        }
    }
}