window.targetidList = '';
window.targetnumberList = '';
window.righte = null;
window.transferE = null;
window.transferNum = null;
window.windowsme;
window.addresstemp;
window.myDrawingManagerObject = null;
window.overlays = [];

//百度地图业务逻辑
mini_debugger = false;
//当前绘制的类型,1为多边形，2为圆形
draw_model = 1;
window.methis;
window.threeguaranteescount = 0;
window.superid;
window.supername;

window.realalarmdata = null;
var Index = {
    traceTargetNumber: "",
    provinceBoundary: {},
    markerMaxCount: 50,
    isMarker: false, //初始加载方式为marker
    labels: new Object(),
    initFinished: false,
    mkrs: [],
    updateMarker: false, //标示百度map的load事件是否监听（centerAndZoom）
    pts: [], //轨迹播放  
    ptsOn: [], //在线点
    ptsOff: [], //离线点
    //供pointCollectionOrMarker使用
    mkrs_array: [],
    points_on: [],
    points_off: [],
    //datagrid实时位置
    dgLastInfoDataResult: {},
    dgLastInfoInitialized: false,
    //datagrid实时状态
    dgLastStatusDataResult: {},
    dgLastStatusInitialized: false,
    //datagrid实时警情
    dgLastAlarmInfoDataResult: {},
    dgLastAlarmInfoInitialized: false,
    //datagrid区域报警
    dgLastalarmrealDataResult: {},
    dgLastalarmrealInitialized: false,
    //datagrid左侧目标列表
    dgTargetListInitialized: false,
    //判断地图区域是否有大量目标
    mapHasHugeTarget: false,
    //不同级别和状态对应的icons
    icons: {
        zoom3_5: {//电动车3-5级地图
            accon: new BMap.Icon('images/L1_4x4_ACCOn_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/L1_4x4_ACCOn_Online.png*/, new BMap.Size(4, 4)),
            accoff: new BMap.Icon('images/L1_4x4_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/L1_4x4_ACCOff_Online.png*/, new BMap.Size(4, 4)),
            move: new BMap.Icon('images/L1_4x4_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/L1_4x4_ACCOff_Online.png*/, new BMap.Size(4, 4)),
            offline: new BMap.Icon('images/L1_4x4_ACCOff_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/L1_4x4_ACCOff_Offline.png*/, new BMap.Size(4, 4)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        Pzoom3_5: {//人3-5级地图
            accon: new BMap.Icon('images/P_4x4_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_4x4_Online.png*/, new BMap.Size(4, 4)),
            accoff: new BMap.Icon('images/P_4x4_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_4x4_Online.png*/, new BMap.Size(4, 4)),
            move: new BMap.Icon('images/P_4x4_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_4x4_Online.png*/, new BMap.Size(4, 4)),
            offline: new BMap.Icon('images/P_4x4_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_4x4_Offline.png*/, new BMap.Size(4, 4)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        Czoom3_5: {//小汽车3-5级地图
            accon: new BMap.Icon('images/C_4x4_ACCOn_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/C_4x4_ACCOn_Online.png*/, new BMap.Size(4, 4)),
            accoff: new BMap.Icon('images/C_4x4_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/C_4x4_ACCOff_Online.png*/, new BMap.Size(4, 4)),
            move: new BMap.Icon('images/C_4x4_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/C_4x4_ACCOff_Online.png*/, new BMap.Size(4, 4)),
            offline: new BMap.Icon('images/C_4x4_ACCOff_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/C_4x4_ACCOff_Offline.png*/, new BMap.Size(4, 4)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        zoom6_11: {//电动车6-11级地图
            accon: new BMap.Icon('images/L2_16x16_ACCOn_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/L2_16x16_ACCOn_Online.png*/, new BMap.Size(16, 16)),
            accoff: new BMap.Icon('images/L2_16x16_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/L2_16x16_ACCOff_Online.png*/, new BMap.Size(16, 16)),
            move: new BMap.Icon('images/L2_16x16_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/L2_16x16_ACCOff_Online.png*/, new BMap.Size(16, 16)),
            offline: new BMap.Icon('images/L2_16x16_ACCOff_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/L2_16x16_ACCOff_Offline.png*/, new BMap.Size(16, 16)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        Pzoom6_11: {//人员6-11级地图
            accon: new BMap.Icon('images/P_16x16_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_16x16_Online.png*/, new BMap.Size(16, 16)),
            accoff: new BMap.Icon('images/P_16x16_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_16x16_Online.png*/, new BMap.Size(16, 16)),
            move: new BMap.Icon('images/P_16x16_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_16x16_Online.png*/, new BMap.Size(16, 16)),
            offline: new BMap.Icon('images/P_16x16_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_16x16_Offline.png*/, new BMap.Size(16, 16)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        Czoom6_11: {//小汽车6-11级地图
            accon: new BMap.Icon('images/C_16x16_ACCOn_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/C_16x16_ACCOn_Online.png*/, new BMap.Size(16, 16)),
            accoff: new BMap.Icon('images/C_16x16_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/C_16x16_ACCOff_Online.png*/, new BMap.Size(16, 16)),
            move: new BMap.Icon('images/C_16x16_ACCOff_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/C_16x16_ACCOff_Online.png*/, new BMap.Size(16, 16)),
            offline: new BMap.Icon('images/C_16x16_ACCOff_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/C_16x16_ACCOff_Offline.png*/, new BMap.Size(16, 16)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        zoom12_18: {//电动车12-18级地图
            accon: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            accoff: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            move: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            offline: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        Motorzoom12_18: {//摩托车12-18级地图
            accon: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            accoff: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            move: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            offline: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        Pzoom12_18: {//人员12-18级地图
            accon: new BMap.Icon('images/P_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_32x32_Online.png*/, new BMap.Size(32, 32)),
            accoff: new BMap.Icon('images/P_32x32_Online.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_32x32_Online.png*/, new BMap.Size(32, 32)),
            move: new BMap.Icon('images/P_32x32_Online2.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_32x32_Online2.png*/, new BMap.Size(32, 32)),
            offline: new BMap.Icon('images/P_32x32_Offline.png'/*tpa=http://www.beidouguard.com:6010/js/images/P_32x32_Offline.png*/, new BMap.Size(32, 32)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        Czoom12_18: {//小汽车12-18级地图
            accon: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            accoff: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            move: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            offline: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        HCzoom12_18: {//货车12-18级地图
            accon: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            accoff: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            move: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            offline: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        JCzoom12_18: {//警车12-18级地图
            accon: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            accoff: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            move: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            offline: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        TLJzoom12_18: {//拖拉机12-18级地图
            accon: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            accoff: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            move: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            offline: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        WJJzoom12_18: {//挖掘机车12-18级地图
            accon: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            accoff: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            move: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            offline: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(50, 50)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        },
        SGJzoom12_18: {//收割机车12-18级地图
            accon: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            accoff: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            move: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            offline: new BMap.Icon('images/szga_2.png'/*tpa=http://www.beidouguard.com:6010/js/images/szga_2.png*/, new BMap.Size(32, 32)),
            alarm: new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24))
        }
    },
    displayLabelZoom: 12, //与zoom12_18对应
    clickZoom: 16,
    infoWin: new BMap.InfoWindow(''),
    infoWinRC: new BMap.InfoWindow(''), //右键的信息窗
    selTreeNode: null,
    selGridRow: null,
    traceMgr: null,
    tracePly: null, //轨迹
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
    markerCluster: null,
    threeguaranteesmarkerCluster: null,
    threeguaranteespointCollection: null,
    threeguaranteespointCollectionOff: null,
    pointCollection: null,
    pointCollectionOff: null,
    //初始化环境、包括初始化地图、控件、加载数据等
    init: function () {

        // 一分钟刷新一次
        var me = this;
        windowsme = me;
//        me.loadDataHandler = window.setInterval(function () {
//            me.loadData();
//        }, serverInfo.configParam.popupInterval);
        //        me.loadDataHandlerLoadTree = window.setInterval(function () {
        //            me.reloadTree(); //重新加载左侧树          
        //        }, serverInfo.configParam.popupInterval);
        me.reloadTree();
        //me.threeGuaranteesCar(4, 0);
        if (this.initFinished) {
            return; //only once
        }

        this.hideLeftPanel();
        this.initMap();
        //this.initSearchTabs();
        this.initMouseControl();
        //this.loadData();

        this.initFinished = true;

        // 选中所有左侧节点
        //var tree = mini.get("treeMain");        
        //tree.collapseAll();
        //var nodes = tree.getAllChildNodes(tree.getRootNode());
        //tree.checkNodes(nodes);


        /* var idStr = tree.getValue(false);
        $.ajax({
        url: 'actions/get_select_target.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_select_target.ashx*/,
        type: 'POST',
        data: { 'id': idStr },
        error: function () {
        me.showLeftPanel();
        },
        success: function (data) {
        me.showLeftPanel();
        me.loadData();
        },
        error: function () { mini.alert('error') }
        });
        */
        // mini.get("treeMain").load('actions/get_json_tree.ashx-key='/*tpa=http://www.beidouguard.com:6010/js/actions/get_json_tree.ashx?key=*/);
    },
    //隐藏左侧面板，防止首屏加载慢导致的页面混乱
    hideLeftPanel: function () {
        mini.get('layoutMain').hideRegion('west');
    },
    toolBarMainExpand: function (e) {
        var btn = e.sender;

        var display = $('#tool-bar-main-div').css('display');

        if (display == "none") {
            btn.setIconCls("panel-collapse");
            btn.setText("收缩");
            $('#tool-bar-main-div').css('display', "block");

        } else {
            btn.setIconCls("panel-expand");
            btn.setText("展开");
            $('#tool-bar-main-div').css('display', "none");
        }
    },
    //显示左侧面板
    showLeftPanel: function () {
        var layout = mini.get('layoutMain');
        if (!layout.isVisibleRegion('west')) {
            layout.showRegion('west');
            $(".westZIndex").css('visibility', 'visible');
        }
    },
    //初始化地图
    initMap: function () {
        methis = this;
        me = this;
        window.baiduMap = new BMap.Map('divBaiduMap', { minZoom: 3, enableMapClick:false });
        var pt = new BMap.Point(120.760404, 31.670767);
        baiduMap.centerAndZoom(pt, 10);

        baiduMap.enableScrollWheelZoom();
        baiduMap.enableInertialDragging();
        baiduMap.enableContinuousZoom();

        baiduMap.addControl(new BMap.NavigationControl());
        baiduMap.addControl(new BMap.ScaleControl());
        baiduMap.addControl(new BMap.OverviewMapControl());

        window.mapLoaded = 0;
        baiduMap.addEventListener("dragend", function () {
            window.Index.pointCollectionOrMarker(window.baiduMap, window.Index.markerMaxCount);
        });
        baiduMap.setMapType(BMAP_NORMAL_MAP);

        baiduMap.addEventListener("tilesloaded", function () {
            if (me.updateMarker) {
                me.pointCollectionOrMarker(window.baiduMap, me.markerMaxCount);
                me.updateMarker = false;
            }
            if (window.mapLoaded == 0) {
                mini.get("treeMain").load('actions/get_org_json.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_org_json.ashx*/);
                mini.get("treeMain").collapseLevel(2);

                mini.get("treeProvince").load('actions/get_province_list.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_province_list.ashx*/);
                mini.get("treeProvince").collapseLevel(2);

                window.mapLoaded = 1;
                Index.showLeftPanel();

                var grid = mini.get("targetgrid");
                grid.loading("加载数据中......");
                $.ajax({
                    url: 'actions/get_target_json.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_target_json.ashx*/,
                    type: 'post',
                    error: function () {
                        baiduMap.removeOverlay(overlay);

                    },
                    success: function (data) {
                        me.loadData();
                        window.targetList = eval("(" + data + ")");
                        mini.get("targetgrid").unmask();
                        var len = window.targetList.length;
                        for (var i = 0; i < len; i++) {
                            // window.targetList[i].pids.sort(function (a, b) { return a - b });
                            window.targetList[i].check = false;
                        }
                    }
                });
            }
        });
    },
    //放大地图
    zoomInHandler: function () {
        Index.rectZoomInTool.open();
    },
    noFunction: function () {
        mini.alert("1");
        return;
    },
    //缩小地图
    zoomOutHandler: function () {
        Index.rectZoomOutTool.open();
    },
    //量算距离
    measureLengthHandler: function () {
        Index.distanceTool.open();
    },
    //面积量算
    areaHandler: function () {
        myDrawingManagerObject = new BMapLib.DrawingManager(window.baiduMap, { isOpen: true,
            drawingType: BMAP_DRAWING_MARKER, enableDrawingTool: true,
            enableCalculate: false,
            drawingToolOptions: {
                anchor: BMAP_ANCHOR_TOP_LEFT,
                offset: new BMap.Size(5, 5),
                drawingTypes: [
            BMAP_DRAWING_MARKER,
            BMAP_DRAWING_CIRCLE,
            BMAP_DRAWING_POLYLINE,
            BMAP_DRAWING_POLYGON,
            BMAP_DRAWING_RECTANGLE
         ]
            }, polylineOptions: {
                strokeColor: "#333"
            }
        });
        myDrawingManagerObject.enableCalculate();
        myDrawingManagerObject.setDrawingMode(BMAP_DRAWING_POLYGON);
        var overlaycomplete = function (e) {
            window.overlays.push(e.overlay);
        };
        myDrawingManagerObject.addEventListener('overlaycomplete', overlaycomplete);
    },
    //清除
    clearHandler: function () {
        if (myDrawingManagerObject != null) {
            myDrawingManagerObject.disableCalculate();
            myDrawingManagerObject.close();

            for (var i = 0; i < overlays.length; i++) {
                window.baiduMap.removeOverlay(window.overlays[i]);
            }


        }
    },
    //拉框搜索电动车
    searchBikeHandler: function () {
        Index.searchBikeTool.setDrawingMode(BMAP_DRAWING_RECTANGLE);
        Index.searchBikeTool.open();
    },
    //矩形框回调函数
    onDrawRect: function (e) {
        Index.drawRangeRectTool.setDrawingMode(BMAP_DRAWING_RECTANGLE);
        Index.drawRangeRectTool.open();
    },
    //设置多边形报警区域
    areaAlarmHandler: function () {
        draw_model = 1;
        Index.setupAlarmTool.setDrawingMode(BMAP_DRAWING_POLYGON);
        Index.setupAlarmTool.open();
    },
    //设置圆形报警区域
    areaAlarmCircleHandler: function () {
        draw_model = 2;
        Index.setupAlarmTool.setDrawingMode(BMAP_DRAWING_CIRCLE);
        Index.setupAlarmTool.open();
    },
    //全屏切换
    fullScreenHandler: function (e) {
        var target = e.sender;
        if (target.text == '全屏') {
            mini.get('layoutMain').hideRegion('north');
            mini.get('layoutMain').updateRegion("west", { expanded: false });
            mini.get('layoutMain').hideRegion('south');
            target.setText('退出全屏');
        } else {
            mini.get('layoutMain').showRegion('north');
            mini.get('layoutMain').updateRegion("west", { expanded: true });
            mini.get('layoutMain').showRegion('south');
            target.setText('全屏');
        }
        this.initMouseControl();
        this.loadData();
    },
    //影像图切换
    //    satliteMapHandler: function (e) {
    //        var target = e.sender;
    //        if (target.text == '卫星地图') {
    //            baiduMap.setMapType(BMAP_HYBRID_MAP);
    //            target.setText('普通地图');
    //        } else {
    //            baiduMap.setMapType(BMAP_NORMAL_MAP);
    //            target.setText('卫星地图');
    //        }
    //    },
    satliteMapHandler: function (text) {
        if (text == '卫星地图') {
            baiduMap.setMapType(BMAP_HYBRID_MAP);
        } else {
            baiduMap.setMapType(BMAP_NORMAL_MAP);
        }
    },
    //同步切换地图类型，用于切换天地图和百度地图
    switchMapType: function (type) {
        if (type == '卫星地图') {
            baiduMap.setMapType(BMAP_HYBRID_MAP);
        } else {
            baiduMap.setMapType(BMAP_NORMAL_MAP);
        }
    },
    //获取地图视野
    getMapView: function () {
        var pt = baiduMap.getCenter(),
            zoom = baiduMap.getZoom(),
            lng = pt.lng,
            lat = pt.lat;
        return {
            'lng': lng,
            'lat': lat,
            'zoom': zoom
        }
    },
    //设置地图视野
    setMapView: function (view) {
        var pt = new BMap.Point(view.lng, view.lat);
        baiduMap.centerAndZoom(pt, view.zoom);
    },
    //初始化鼠标控件类型
    initMouseControl: function () {
        this.distanceTool = new BMapLib.DistanceTool(baiduMap);
        this.rectZoomInTool = new BMapLib.RectangleZoom(baiduMap, {
            'zoomType': 0,
            'followText': "拖拽鼠标放大地图",
            'strokeWeight': 3,
            'autoClose': true
        });

        this.rectZoomOutTool = new BMapLib.RectangleZoom(baiduMap, {
            'zoomType': 1,
            'followText': "拖拽鼠标缩小地图",
            'strokeWeight': 3,
            'autoClose': true
        });

        this.setupAlarmTool = new BMapLib.DrawingManager(baiduMap, {
            'polygonOptions': {
                'strokeWeight': 3
            }
        });
        Index.setupAlarmTool.addEventListener('polygoncomplete', function (e) {
            var me = this,
				overlay = e,
				path = overlay.getPath();

            if (draw_model == 1 && path.length < 3) {
                mini.showTips({
                    content: "设置的报警区域必须大于2个点!",
                    state: 'default',
                    x: 'center',
                    y: 'center',
                    timeout: 2000
                });

                baiduMap.removeOverlay(overlay);
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

                        for (var i = 0; i < path.length; i++) {
                            var pt = path[i];
                            pgn += pt.lng + ',' + pt.lat + ',';
                        }
                        pgn = pgn.substr(0, pgn.length - 1);

                        params = 'alarmName=' + alarmName + '&isUsed=' + isUsed + '&leastCount=' + leastCount + '&alarmType=' + alarmType + '&ids=' + ids + '&pgn=' + pgn + '&mapSource=baidu';
                        //mini.alert(params); //调试状态        						
                        $.ajax({
                            url: 'actions/setup_alarm_area.ashx?' + params,
                            type: 'post',
                            error: function () {
                                baiduMap.removeOverlay(overlay);
                                me.close();
                            },
                            success: function (data) {
                                var obj = null;
                                if (data == "个数太少") {
                                    mini.alert("选择区域内聚集报警,关联终端个数应该大于等于最小的报警个数,请重新设定");
                                    baiduMap.removeOverlay(overlay);
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
                                    baiduMap.removeOverlay(overlay);
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

                                baiduMap.removeOverlay(overlay);
                                me.close();
                                Index.loadData();
                            }
                        });
                    } else {//非ok
                        baiduMap.removeOverlay(overlay);
                        me.close();
                    }
                }
            });

        });
        Index.setupAlarmTool.addEventListener("circlecomplete", function (e, overlay) {
            var me = this,
				overlay = e,
				path = overlay.getPath();

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

                        for (var i = 0; i < path.length; i++) {
                            var pt = path[i];
                            pgn += pt.lng + ',' + pt.lat + ',';
                        }
                        pgn = pgn.substr(0, pgn.length - 1);

                        params = 'alarmName=' + alarmName + '&isUsed=' + isUsed + '&leastCount=' + leastCount + '&alarmType=' + alarmType + '&ids=' + ids + '&pgn=' + pgn + '&mapSource=baidu';
                        //mini.alert(params); //调试状态        						
                        $.ajax({
                            url: 'actions/setup_alarm_area.ashx?' + params,
                            type: 'post',
                            error: function () {
                                baiduMap.removeOverlay(overlay);
                                me.close();
                            },
                            success: function (data) {
                                var obj = null;
                                if (data == "个数太少") {
                                    mini.alert("选择区域内聚集报警,关联终端个数应该大于等于最小的报警个数,请重新设定");
                                    baiduMap.removeOverlay(overlay);
                                    me.close();
                                    return;
                                }

                                if (data == "此区域名称已经被使用,请修改区域名称") {
                                    mini.alert("此区域名称已经被使用,请重新设定,并修改区域名称");
                                    baiduMap.removeOverlay(overlay);
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

                                baiduMap.removeOverlay(overlay);
                                me.close();
                                Index.loadData();
                            }
                        });
                    } else {//非ok
                        baiduMap.removeOverlay(overlay);
                        me.close();
                    }
                }
            });
        });

        this.searchBikeTool = new BMapLib.DrawingManager(baiduMap, {
            'rectangleOptions': {
                'strokeWeight': 3
            }
        });
        this.drawRangeRectTool = new BMapLib.DrawingManager(baiduMap, {
            'rectangleOptions': {
                'strokeWeight': 3
            }
        });
        Index.searchBikeTool.addEventListener('rectanglecomplete', function (e) {
            var me = this,
				overlay = e,
				bounds = overlay.getBounds(),
				sw = bounds.getSouthWest(),
				ne = bounds.getNorthEast(),
				params = 'minx=' + sw.lng
					+ '&maxx=' + ne.lng
					+ '&miny=' + sw.lat
					+ '&maxy=' + ne.lat
                    + '&mapSource=baidu';
            $.ajax({
                url: 'actions/search_bike.ashx?' + params,
                type: 'post',
                error: function () {
                    baiduMap.removeOverlay(overlay);
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

                    baiduMap.removeOverlay(overlay);
                    me.close();
                }
            });
        });

        Index.drawRangeRectTool.addEventListener('rectanglecomplete', function (e) {
            var me = this,
				overlay = e,
				bounds = overlay.getBounds(),
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
            baiduMap.removeOverlay(overlay);
            me.close();
        });
    },
    searchTabsChangedHandler: function (e) {

        if (me.markerCluster) {
            me.markerCluster.clearMarkers(); //清除 聚集点
        }
        if (me.pointCollection || me.pointCollectionOff) {
            me.pointCollection.clear(); //清除海量点
            me.pointCollectionOff.clear(); //清除海量点
            me.pointCollectionOff = null;
            me.pointCollection = null;
        }
        window.baiduMap.clearOverlays();

        //清除merker、海量点数组 
        this.ptsOn.length = 0;
        this.ptsOff.length = 0;
        this.mkrs.length = 0;
        //uncheck
        mini.get('treeMain').uncheckAllNodes();
        mini.get('treeProvince').uncheckAllNodes();

        baiduMap.centerAndZoom(new BMap.Point(116.404, 39.915), 6);
        //window.selectedData.length = 0;
        //if (e.tab.name == "org") {
        //    //mini.get('targetSearch').setValue("");               
        //} else if (e.tab.name == "province") {
        //   // mini.get('targetSearch').setValue("");
        //}

    },
    //点击树状控件处理函数
    treeMainHandler: function (e) {
        var me = this;
        var record = e.record,
			id = record.id;

        //isBikeNode = (record.img == 'icon_bike.png'/*tpa=http://www.beidouguard.com:6010/js/icon_bike.png*/ || record.img == 'icon_person.png'/*tpa=http://www.beidouguard.com:6010/js/icon_person.png*/ || record.img == 'icon_car.png'/*tpa=http://www.beidouguard.com:6010/js/icon_car.png*/) ? true : false; //是否是电动车节点，根据icon_bike.png判定
        //	isBikeNode = (record.img != 'location.png'/*tpa=http://www.beidouguard.com:6010/js/location.png*/ && record.img != 'icon_user.png'/*tpa=http://www.beidouguard.com:6010/js/icon_user.png*/) ? true : false; //是否是叶子节点
        /*  if (isBikeNode) {//叶子节点
        Index.zoomToMarker(id);
        }*/
        var searchTabs = mini.get("searchTabs");
        var tabName = mini.get("searchTabs").getActiveTab().name;
        var grid = mini.get("targetgrid");
        //var keyOfTargetSearch = mini.get('targetSearch').getValue();
        var data = [];
        if (tabName == "org") {
            window.selectedData = me.getSelectedData(record.id, "org", record.name);
        } else if (tabName == "province") {
            window.selectedData = me.getSelectedData(record.name.split('(')[0], "province", record.name.split('(')[0]);
        }

        if (!me.dgTargetListInitialized) {
            me.dgTargetListInitialized = true;
            // 监听分页前事件，阻止后自行设置当前数据和分页信息
            grid.on("beforeload", function (e) {
                e.cancel = true;
                var pageIndex = e.data.pageIndex, pageSize = e.data.pageSize;
                me.fillData(pageIndex, pageSize, window.selectedData, grid);
            });
            //初次加载
            me.fillData(0, grid.getPageSize(), window.selectedData, grid);
        }
        me.fillData(0, grid.getPageSize(), window.selectedData, grid);


    },
    searchHandleCheckOrg: function (e, tree) {
        var ids = new Array();
        var nodes = tree.getCheckedNodes(false);
        var nodesLen = nodes.length;
        for (var i = 0; i < nodesLen; i++) {//找出checked的组织id
            if (nodes[i].id != "user") {
                if (nodes[i].id == "directly") {
                    ids.push(0); //直属组织
                } else {
                    ids.push(parseInt(nodes[i].id));
                }
            } else {
                ids.push(-1);
            }
        }

        var len = window.targetList.length;
        for (var i = 0; i < len; i++) {//遍历targetlist

            if (this.isArrayContainArray(ids, window.targetList[i].pids)) {//判断两个数组是否有交集
                window.targetList[i].check = true;
            } else {
                window.targetList[i].check = false;
            }
        }
        //mini.get("targetgrid").reload();
    },
    searchHandleCheckProvince: function (e, tree) {
        var province = new Array();
        var nodes = tree.getCheckedNodes(false);
        var nodesLen = nodes.length;
        for (var i = 0; i < nodesLen; i++) {//找出checked的province name         
            province.push(nodes[i].name.split('(')[0]);
        }
        var len = window.targetList.length;
        for (var i = 0; i < len; i++) {//遍历targetlist
            if (province.indexOf(targetList[i].pro) != -1) {//判断pro字段
                window.targetList[i].check = true;
            } else {
                window.targetList[i].check = false;
            }
        }
        //mini.get("targetgrid").reload();
    },
    isArrayContainArray: function (arr1, arr2) {
        if (arr1.length == 0) {
            return false;
        }
        arr1.sort(function (a, b) { return a - b });
        arr2.sort(function (a, b) { return a - b });
        if (arr1[0] == -1) {
            return true;
        }
        if (arr1[0] > arr2[arr2.length] || arr1[arr1.length] < arr2[0]) {
            return false;
        }
        var len = arr2.length;
        for (var j = 0; j < len; j++) {
            if (arr1.indexOf(arr2[j]) != -1) {
                return true;
            }
        }
        return false;
    },
    getSelectedData: function (pid, type, name) {
        var inputkey = mini.get('targetSearch').getValue();
        var carTypeStr = mini.get('comboxSearch').getValue();
        var carTypeArrary = carTypeStr.split(',');
        var data = [];
        var rule = new RegExp("(" + inputkey + ")+");
        var addrRule = new RegExp("(" + pid + ")+");
        var l = window.targetList.length;

        for (var a = 0; a < l; a++) {
            if (type == "org") {
                if (pid == "directly") {
                    pid = "0";
                }
                if (window.targetList[a].pids.indexOf(parseInt(pid)) != -1 || pid == "user" || !pid) {
                    var isCarType = (carTypeStr == "") || (carTypeArrary.indexOf(window.targetList[a].cType) != -1); //车辆信息
                    if ((inputkey == "" || rule.test(window.targetList[a].name) || rule.test(window.targetList[a].num) || rule.test(window.targetList[a].sim)) && isCarType) {//模糊查询
                        data.push(window.targetList[a]);
                    } else {
                        continue;
                    }
                }
            } else if (type == "province") {
                if (window.targetList[a].pro == name || name == "全国" || name == "") {
                    var isCarType = (carTypeStr == "") || (carTypeArrary.indexOf(window.targetList[a].cType) != -1); //车辆信息
                    if ((inputkey == "" || rule.test(window.targetList[a].name) || rule.test(window.targetList[a].num) || rule.test(window.targetList[a].sim)) && isCarType) {//模糊查询
                        data.push(window.targetList[a]);
                    } else {
                        continue;
                    }
                }
            }
        }
        return data;
    },
    fillData: function (pageIndex, pageSize, dataResult, grid) {
        var data = dataResult, totalCount = dataResult.length;

        var arr = [];
        var start = pageIndex * pageSize, end = start + pageSize;
        for (var i = start, l = end; i < l; i++) {
            var record = data[i];
            if (!record) continue;
            arr.push(record);
        }

        grid.setTotalCount(totalCount);
        grid.setPageIndex(pageIndex);
        grid.setPageSize(pageSize);

        grid.setData(arr);
    },
    getCheckedTargets: function () {
        var len = window.targetList.length;
        var ids = new Array();
        for (var i = 0; i < len; i++) {
            if (window.targetList[i].check) {
                ids.push(window.targetList[i].id);
            }
        }
        return ids.toString();
    },
    setProvinceBoundary: function (province) {
        var me = this;
        var bdary = new BMap.Boundary();
        bdary.get(province, function (rs) {       //获取行政区域
            // map.clearOverlays();        //清除地图覆盖物       
            var count = rs.boundaries.length; //行政区域的点有多少个
            if (count === 0) {
                // alert('未能获取当前输入行政区域');
                // 将地址解析结果显示在地图上,并调整地图视野
                var myGeo = new BMap.Geocoder();
                myGeo.getPoint(province, function (point) {
                    if (point) {
                        baiduMap.centerAndZoom(point, 7);
                    } else {
                        // alert("您选择地址没有解析到结果!");
                    }
                }, province);
                return;
            }
            var pointArray = [];
            for (var i = 0; i < count; i++) {
                var ply = new BMap.Polygon(rs.boundaries[i], { strokeWeight: 2, strokeColor: "#ff0000", fillColor: "" }); //建立多边形覆盖物
                baiduMap.addOverlay(ply);  //添加覆盖物
                me.provinceBoundary[province].push(ply);
                pointArray = pointArray.concat(ply.getPath());
            }
            baiduMap.setViewport(pointArray);    //调整视野                 
        });
    },
    //点击树状控件check处理函数
    treeCheckedHandler: function (e) {
        var me = this;
        var tabName = mini.get("searchTabs").getActiveTab().name;
        var tree = null;
        if (tabName == "org") {
            var tree = mini.get("treeMain");
            this.searchHandleCheckOrg(e, tree);
        } else if (tabName == "province") {
            var tree = mini.get("treeProvince");
            this.searchHandleCheckProvince(e, tree);
            //var myGeo = new BMap.Geocoder();
            if (e.node.name == "全国") {
                if (!e.node.checked) {
                    for (var item in me.provinceBoundary) {
                        var l = me.provinceBoundary[item.split('(')[0]].length;
                        for (var i = 0; i < l; i++) {
                            window.baiduMap.removeOverlay(me.provinceBoundary[item.split('(')[0]][i]);
                        }
                        me.provinceBoundary[item.split('(')[0]].length = 0;
                    }
                }
                baiduMap.centerAndZoom(new BMap.Point(116.404, 39.915), 6);
            } else {
                if (e.node.checked) { //勾选省份花轮廓                
                    me.provinceBoundary[e.node.name.split('(')[0]] = [];
                    me.setProvinceBoundary(e.node.name.split('(')[0]);
                } else if (me.provinceBoundary[e.node.name.split('(')[0]]) {//去除轮廓

                    var l = me.provinceBoundary[e.node.name.split('(')[0]].length;
                    for (var i = 0; i < l; i++) {
                        window.baiduMap.removeOverlay(me.provinceBoundary[e.node.name.split('(')[0]][i]);
                    }
                    me.provinceBoundary[e.node.name.split('(')[0]].length = 0;
                }
                // 将地址解析结果显示在地图上,并调整地图视野
                /*  myGeo.getPoint(e.node.name, function (point) {
                if (point) {
                baiduMap.centerAndZoom(point, 7);

                } else {
                // alert("您选择地址没有解析到结果!");
                }
                }, e.node.name);*/
            }
        }
        // var idStr = tree.getValue(false);
        //        alert(idStr);

        var ids = this.getCheckedTargets();
        mini.get("searchTabs").loading("加载数据中......");
        $.ajax({
            url: 'actions/get_select_target.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_select_target.ashx*/,
            type: 'POST',
            data: { 'id': ids },
            error: function () { },
            success: function (data) {
                me.loadData();
            },
            error: function () { mini.alert('error') }
        });
    },
    tableCellClickHandler: function (e) {
        var cell = e;
        if (e.column.type == "checkboxcolumn") {
            Index.markered = false;
            var record = e.record;
            var grid = mini.get("targetgrid");
            record.check = !record.check;
            grid.reload();
            var ids = this.getCheckedTargets();
            $.ajax({
                url: 'actions/get_select_target.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_select_target.ashx*/,
                type: 'POST',
                data: { 'id': ids },
                error: function () { },
                success: function (data) {
                    me.loadData();
                },
                error: function () { mini.alert('error') }
            });
        }
    },
    tableRowClickHandler: function (e) {
        window.Index.traceTargetNumber = e.record.num;
    },
    tableRowDoubleClickHandler: function (e) {
        if (!e.record.check) {
            mini.showTips({
                content: "<b>提示：</b> <br/>请勾选目标信息！",
                state: "warning",      //default|success|info|warning|danger
                x: "center",          //left|center|right
                y: "center",          //top|center|bottom
                timeout: 2000     //自动消失间隔时间。默认2000（2秒）。
            });
            return;
        }
        if (e.record.pro == "") {
            mini.showTips({
                content: "<b>提示：</b> <br/>尚未收到定位数据！",
                state: "warning",      //default|success|info|warning|danger
                x: "center",          //left|center|right
                y: "center",          //top|center|bottom
                timeout: 2000     //自动消失间隔时间。默认2000（2秒）。
            });
            return;
        }
        Index.zoomToMarker(e.record.id);
    },
    //报警区域树处理函数
    treeRgnHandler: function (e) {
        var record = e.node,
			id = record.id,
            isLeaf = (typeof record.children == 'undefined') ? true : false;
        if (!isLeaf) {//点击根节点不处理
            return;
        }
        $.ajax({
            url: 'actions/get_region_info.ashx?id=' + id + '&q=shape&mapSource=baidu',
            type: 'post',
            error: function () { mini.alert('获取失败'); },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }
                var arr = data.split(','),
					pts = [],
                    l = arr.length;

                if (l > 6) {
                    for (var i = 0; i < l; i += 2) {
                        pts.push(new BMap.Point(arr[i], arr[i + 1]));
                    }
                    var polygon = new BMap.Polygon(pts, {
                        strokeWeight: 2,
                        strokeColor: 'blue',
                        fillColor: 'red'
                    });

                    if (Index.alarmPly) {
                        baiduMap.removeOverlay(Index.alarmPly);
                    }
                    baiduMap.addOverlay(polygon);
                    baiduMap.setViewport(polygon.getPath());

                    Index.alarmPly = polygon;
                }
                else if (l == 6) {
                    var x1 = parseFloat(arr[0]);
                    var y1 = parseFloat(arr[1]);
                    var x2 = parseFloat(arr[2]);
                    var y2 = parseFloat(arr[3]);

                    var cx = (x1 + x2) / 2;
                    var cy = (y1 + y2) / 2;
                    alert(cx + ',' + cy);

                    var point1 = new BMap.Point(arr[0], arr[1]);
                    var point2 = new BMap.Point(arr[2], arr[3]);
                    var centPnt = new BMap.Point(cx, cy);
                    var distance = baiduMap.getDistance(point1, point2);
                    var circle = new BMap.Circle(centPnt, distance / 2, { fillColor: "red", strokeWeight: 1, fillOpacity: 0.5, strokeOpacity: 1 });

                    if (Index.alarmPly) {
                        baiduMap.removeOverlay(Index.alarmPly);
                    }
                    baiduMap.addOverlay(circle);
                    baiduMap.setViewport(circle.getPath());
                    Index.alarmPly = circle;
                }
            }
        });
    },
    //服务网点树处理函数
    treeStationHandler: function (e) {
        var record = e.node,
			id = record.id,
            isLeaf = (typeof record.children == 'undefined') ? true : false;
        if (!isLeaf) {//点击根节点不处理
            return;
        }

        $.ajax({
            url: 'actions/get_station_info.ashx?id=' + id,
            type: 'post',
            error: function () { mini.alert('获取失败'); },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }

                var arr = data.split(',');
                stationname = arr[0];
                producer = arr[1];
                phone = arr[2];
                address = arr[3];
                x = arr[4];
                y = arr[5];
                contact = arr[6];
                opentime = arr[7];

                var pt = new BMap.Point(x, y),
                icon = new BMap.Icon('images/station.png'/*tpa=http://www.beidouguard.com:6010/js/images/station.png*/, new BMap.Size(30, 30)),
                mkr = new BMap.Marker(pt, { 'icon': icon });
                baiduMap.centerAndZoom(pt, baiduMap.getZoom());

                baiduMap.addOverlay(mkr);

                var content = "<table>";
                content = content + "<tr><td> 名称：" + stationname + "</td></tr>";
                content = content + "<tr><td> 厂家：" + producer + "</td></tr>";
                content = content + "<tr><td> 电话：" + phone + "</td></tr>";
                content = content + "<tr><td> 地点：" + address + "</td></tr>";
                content = content + "<tr><td> 联系人：" + contact + "</td></tr>";
                content = content + "<tr><td> 营业时间：" + opentime + "</td></tr>";
                content += "</table>";
                var infowindow = new BMap.InfoWindow(content);
                mkr.addEventListener("click", function () {
                    this.openInfoWindow(infowindow);
                });
            }
        });
    },
    //重新加载左侧树
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
    //加载页面底部各列表数据
    loadData: function () {
        var me = this,
			dgLastInfo = mini.get('dgLastInfo');
        $.ajax({
            url: 'actions/get_last_position.ashx-mapSource=baidu'/*tpa=http://www.beidouguard.com:6010/js/actions/get_last_position.ashx?mapSource=baidu*/,
            type: 'post',
            dataType: 'text',
            success: function (d) {
                //解析数据
                var data = eval('(' + d + ')');
                //更新地图
                if (window.realalarmdata != null && window.realalarmdata.length != 0 && data.data.length != 0) {
                    for (var i = 0; i < window.realalarmdata.length; i++) {
                        for (var j = 0; j < data.data.length; j++) {
                            if (data.data[j].targetnumber == window.realalarmdata[i].targetNumbers) {
                                //var icon = new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24));
                                data.data[j].isonline = "alarm";
                            }
                        }
                    }
                }
                me.addMarkers(data.data);
                window.Index.pointCollectionOrMarker(window.baiduMap, window.Index.markerMaxCount);

                mini.get("searchTabs").unmask();

                //更新实时位置列表
                me.dgLastInfoDataResult = data;
                if (!me.dgLastInfoInitialized) {
                    me.dgLastInfoInit(dgLastInfo, me.dgLastInfoDataResult);
                    me.dgLastInfoInitialized = true;
                } else {
                    me.dataGridFillData(0, dgLastInfo.getPageSize(), me.dgLastInfoDataResult, dgLastInfo);
                }
                //更新实时状态列表
                me.updateRealTimeStatus();

                me.alarmReal();
            },
            error: function (e) {
                mini.alert('加载数据错误!' + e.toString());
            }
        });
        //dgLastInfo.load({ 'mapSource': 'baidu' }, function (e) {
        //    me.addMarkers(e.data);
        //    window.Index.pointCollectionOrMarker(window.baiduMap, window.Index.markerMaxCount);
        //});

        //var dgLastState = mini.get('dgLastState');
        //dgLastState.load();

        //var dgAlarmInfo = mini.get('dgAlarmInfo');
        //dgAlarmInfo.load({ 'mapSource': 'baidu' });
    },
    alarmReal: function () {
        $.ajax({
            url: 'actions/get_user_region_alarm.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_user_region_alarm.ashx*/,
            type: 'post',
            success: function (d) {
                var data = eval('(' + d + ')');
                var grid = mini.get('alarmRealDiv');
                me.dgLastInfoDataResult.data = data;
                me.dgLastInfoDataResult.total = data.length;
                if (!me.dgLastalarmrealInitialized) {
                    me.dgLastalarmrealInit(grid, me.dgLastalarmrealDataResult);
                    me.dgLastalarmrealInitialized = true;
                } else {
                    me.dataGridFillData(0, grid.getPageSize(), me.dgLastInfoDataResult, grid);
                    mini.get('alarmRealDiv').setData(data);
                }
                window.realalarmdata = data;
            }
        });
    },
    updateRealTimeStatus: function () {
        $.ajax({
            url: 'actions/get_last_status.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_last_status.ashx*/,
            type: 'post',
            dataType: 'text',
            success: function (d) {
                //解析数据
                var data = eval('(' + d + ')');
                var dgLastState = mini.get('dgLastState');
                //更新实时状态列表
                me.dgLastStatusDataResult = data;
                if (!me.dgLastStatusInitialized) {
                    me.dgLastStatusInit(dgLastState, me.dgLastStatusDataResult);
                    me.dgLastStatusInitialized = true;
                } else {
                    me.dataGridFillData(0, dgLastState.getPageSize(), me.dgLastStatusDataResult, dgLastState);
                }
                //更新实时警情列表
                me.updateRealTimeAlarmInfo();
            },
            error: function (e) {
                mini.alert('加载数据错误!');
            }
        });
    },
    updateRealTimeAlarmInfo: function () {
        $.ajax({
            url: 'actions/get_last_alarm.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_last_alarm.ashx*/,
            type: 'post',
            dataType: 'text',
            success: function (d) {
                //解析数据
                var data = eval('(' + d + ')');
                var grid = mini.get('dgAlarmInfo');
                //更新实时警情数据
                me.dgLastAlarmInfoDataResult = data;
                if (!me.dgLastAlarmInfoInitialized) {
                    me.dgLastAlarmInfoInit(dgLastState, me.dgLastAlarmInfoDataResult);
                    me.dgLastAlarmInfoInitialized = true;
                } else {
                    me.dataGridFillData(0, grid.getPageSize(), me.dgLastAlarmInfoDataResult, grid);
                }
            },
            error: function (e) {
                mini.alert('加载数据错误!');
            }
        });
    },
    dgLastalarmrealInit: function () {
        var me = this;
        var grid = mini.get('alarmRealDiv');
        // 监听分页前事件，阻止后自行设置当前数据和分页信息
        grid.on("beforeload", function (e) {
            e.cancel = true;
            var pageIndex = e.data.pageIndex, pageSize = e.data.pageSize;
            me.dataGridFillData(pageIndex, pageSize, me.dgLastInfoDataResult, grid);
        });
        // 第一次设置
        me.dataGridFillData(0, grid.getPageSize(), me.dgLastInfoDataResult, grid);
    },
    dgLastInfoInit: function () {
        var me = this;
        var grid = mini.get('dgLastInfo');
        // 监听分页前事件，阻止后自行设置当前数据和分页信息
        grid.on("beforeload", function (e) {
            e.cancel = true;
            var pageIndex = e.data.pageIndex, pageSize = e.data.pageSize;
            me.dataGridFillData(pageIndex, pageSize, me.dgLastInfoDataResult, grid);
        });
        // 第一次设置
        me.dataGridFillData(0, grid.getPageSize(), me.dgLastInfoDataResult, grid);
    },
    dgLastStatusInit: function () {
        var me = this;
        var grid = mini.get('dgLastState');
        // 监听分页前事件，阻止后自行设置当前数据和分页信息
        grid.on("beforeload", function (e) {
            e.cancel = true;
            var pageIndex = e.data.pageIndex, pageSize = e.data.pageSize;
            me.dataGridFillData(pageIndex, pageSize, me.dgLastStatusDataResult, grid);
        });
        // 第一次设置
        me.dataGridFillData(0, grid.getPageSize(), me.dgLastStatusDataResult, grid);
    },
    dgLastAlarmInfoInit: function () {
        var me = this;
        var grid = mini.get('dgAlarmInfo');
        // 监听分页前事件，阻止后自行设置当前数据和分页信息
        grid.on("beforeload", function (e) {
            e.cancel = true;
            var pageIndex = e.data.pageIndex, pageSize = e.data.pageSize;
            me.dataGridFillData(pageIndex, pageSize, me.dgLastAlarmInfoDataResult, grid);
        });
        // 第一次设置
        me.dataGridFillData(0, grid.getPageSize(), me.dgLastAlarmInfoDataResult, grid);
    },
    dataGridFillData: function (pageIndex, pageSize, dataResult, grid) {
        var data = dataResult.data, totalCount = dataResult.total;
        var arr = [];
        var start = pageIndex * pageSize, end = start + pageSize;
        for (var i = start, l = end; i < l; i++) {
            var record = data[i];
            if (!record) continue;
            arr.push(record);
        }

        grid.setTotalCount(totalCount);
        grid.setPageIndex(pageIndex);
        grid.setPageSize(pageSize);
        grid.setData(arr);
    },
    //点击实时信息处理函数
    dgLastInfoHandler: function (e) {
        var record = e.record,
			id = record.id;
        Index.zoomToMarker(id);
    },
    //点击最新状态信息处理函数
    dgLastStateHandler: function (e) {
        var record = e.record,
			id = record.id;
        Index.zoomToMarker(id);
    },
    //点击实时警情处理函数
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
            baiduMap.setCenter(obj.getPosition());
            if (serverInfo.UserConfigParam.isShowTag == 1) {
                me.openAlarmInfoWin(obj, str);
            }
            return;
        }

        var pt = new BMap.Point(x, y),
            icon = new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24)),
            mkr = new BMap.Marker(pt, { 'icon': icon });

        me.realHistoryObject.pts.push(pt);
        me.realHistoryObject.obj[_uid] = mkr;

        baiduMap.addOverlay(mkr);
        mkr.addEventListener('click', function (e) {
            me.openAlarmInfoWin(this, str);
        });
        var view = baiduMap.getViewport(me.realHistoryObject.pts);
        baiduMap.centerAndZoom(pt, view.zoom - 1);
        me.openAlarmInfoWin(mkr, str);
    },
    //点击历史状态处理函数
    dgHistoryStateHandler: function (e) {
        var record = e.record,
			id = record.id;
        Index.zoomToMarker(id);
    },
    //点击历史警情处理函数
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
            baiduMap.setCenter(obj.getPosition());
            me.openAlarmInfoWin(obj, str);
            return;
        }

        var pt = new BMap.Point(x, y),
            icon = new BMap.Icon('images/alarm.gif'/*tpa=http://www.beidouguard.com:6010/js/images/alarm.gif*/, new BMap.Size(30, 24)),
            mkr = new BMap.Marker(pt, { 'icon': icon });

        me.historyObject.pts.push(pt);
        me.historyObject.obj[_uid] = mkr;

        baiduMap.addOverlay(mkr);
        mkr.addEventListener('click', function (e) {
            me.openAlarmInfoWin(this, str);
        });
        var view = baiduMap.getViewport(me.historyObject.pts);
        baiduMap.centerAndZoom(pt, view.zoom - 1);
        me.openAlarmInfoWin(mkr, str);
    },
    //打开报警窗口
    openAlarmInfoWin: function (mkr, str) {
        var me = this;
        me.infoWin.setContent(str);
        if (serverInfo.UserConfigParam.isShowTag == 1) {
            mkr.openInfoWindow(me.infoWin);
        }
    },
    //点击搜索结果面板处理函数
    dgSearchInfoHandler: function (e) {
        var record = e.record,
			id = record.id;
        Index.zoomToMarker(id);
    },
    //点击左侧树状查询按钮后出现结果面板的处理函数
    dgTargetSearchInfoHandler: function (e) {
        var record = e.record,
			id = record.id;

        Index.zoomToMarker(id);
    },
    //结果面板关闭处理函数
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
                baiduMap.removeOverlay(obj[o]);
                obj[o] = null;
                delete obj[o];
            }
            obj = {};
            pts.length = 0;
        }
        e.cancel = true;
    },
    //结果面板切换tab处理函数
    tabMainActiveChangeHandler: function (e) {
        if (this.preActiveTab) {
            if (this.preActiveTab.title == '实时警情') {
                var obj = this.realHistoryObject.obj,
                pts = this.realHistoryObject.pts;
                for (o in obj) {
                    baiduMap.removeOverlay(obj[o]);
                    obj[o] = null;
                    delete obj[o];
                }
                obj = {};
                pts.length = 0;
            }
        }
        this.preActiveTab = e.tab;
    },
    //删除历史报警图标
    delMaker: function () {
        var obj = this.historyObject.obj,
                pts = this.historyObject.pts;
        for (o in obj) {
            baiduMap.removeOverlay(obj[o]);
            obj[o] = null;
            delete obj[o];
        }
        obj = {};
        pts.length = 0;
    },
    //缩放到marker
    zoomToMarker: function (id) {
        var me = this,
			mkrs = me.mkrs;

        for (var i = 0, l = mkrs.length; i < l; i++) {
            var mkr = mkrs[i],
				pt = mkr.getPosition();
            if (mkr._label._id == id) {
                baiduMap.centerAndZoom(pt, me.clickZoom);
                me.updateMarker = true;
                //  me.pointCollectionOrMarker(window.baiduMap, me.markerMaxCount);
                if (serverInfo.UserConfigParam.isShowTag == 1) {
                    me.openInfoWin(mkr);
                }
                break;
            }
        }
    },
    //根据zoom获取对应的icon
    getCurrentIcons: function (zoom, type) {
        var icons = null;
        //mini.alert(type);
        /*if (zoom >= 3 && zoom <= 5) {
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
    //添加markers
    addMarkers: function (data) {

        var mkrs = this.mkrs;
        var labels = this.labels;
        mkrs.length = 0;
        //        for (var i = 0, l = mkrs.length; i < l; i++) {
        //            var mkr = mkrs[i];
        //            mkr._label = null;
        //            baiduMap.removeOverlay(mkr);
        //        }

        var me = this,

            zoom = baiduMap.getZoom(),
            isShowOffline = serverInfo.configParam.isShowOffline,
            isShowTargetName = serverInfo.configParam.isShowTargetName;
        me.ptsOn.length = 0;
        me.ptsOff.length = 0;
        if (!me.markerCluster) {//创建聚集
            me.markerCluster = new BMapLib.MarkerClusterer(baiduMap, {
                markers: mkrs,
                girdSize: 256,
                maxZoom: 15,
                minClusterSize: 100000
            });
        }
        if (me.threeguaranteespointCollection || me.threeguaranteespointCollectionOff) {
            me.threeguaranteespointCollection.clear(); //清除海量点
            me.threeguaranteespointCollectionOff.clear(); //清除海量点
        }
        if (me.threeguaranteesmarkerCluster) {
            this.threeguaranteesmarkerCluster.clearMarkers(); //清除聚集点
        }
        for (var i = 0, l = data.length; i < l; i++) {
            var item = data[i],
                    x = item.x,
                    y = item.y,
                    longitude = item.longitude,
                    latitude = item.latitude,
                    id = item.id,
                    targetnumber = item.targetnumber,
                    sim = item.sim,
                    state = item.isonline, //包含4种状态，accon,accoff,offline,alarm
                    type = item.carType,
                    speed = item.velocity,
                    carownername = item.carownername,
                    carownerphone = item.carownerphone,
                    carowneraddress = item.carowneraddress,
                    carplatenumber = item.carplatenumber,
                    time1 = item.time,
                    mkr,
                    pt,
                    icon;
            curIcons = this.getCurrentIcons(zoom, type);
            if (!(isShowOffline == 0 && state == 'offline')) {
                icon = curIcons[state];
                pt = new BMap.Point(x, y);
                pt._id = id;
                mkr = new BMap.Marker(pt, { icon: icon });

                mkr._flag = "marker";

                // baiduMap.addOverlay(mkr);
                mkr._label = new BMap.Label(targetnumber + ', ' + speed + 'KM/H', {
                    offset: new BMap.Size(30, 5)
                });
                mkr._label._id = id; //目标信息绑定到label，marker 方式加载和“海量点加载”公用一个label
                mkr._label._targetnumber = targetnumber;
                mkr._label._sim = sim;
                mkr._label._ownType = type;
                mkr._label._speed = speed;
                mkr._label._state = state;
                mkr._label._x = x;
                mkr._label._y = y;
                mkr._label._longitude = longitude;
                mkr._label._latitude = latitude;
                mkr._label._time = time1;
                mkr._label._carownername = carownername;
                mkr._label._carownerphone = carownerphone;
                mkr._label._carowneraddress = carowneraddress;
                mkr._label._carplatenumber = carplatenumber;

                mkr._label.setStyle({
                    font: '12px 微软雅黑',
                    padding: '2px 2px',
                    border: 'solid 1px #000',
                    color: '#000',
                    backgroundColor: '#FFFFCC'
                });
                mkr.setLabel(mkr._label);
                if (zoom < me.displayLabelZoom || (isShowTargetName !== 1)) {
                    mkr._label.hide();
                }

                mkrs.push(mkr);
                if (state == "offline") {
                    me.ptsOff.push(pt);
                } else {
                    me.ptsOn.push(pt);
                }

                labels[id] = mkr._label;

                mkr.addEventListener('click', function () {
                    //if (serverInfo.UserConfigParam.isShowTag == 1) {
                    me.openInfoWin(this);
                    //}
                });
                mkr.addEventListener("rightclick", function () {
                    me.openInfoWinRC(this);
                });
            }
        }
        me.isMarker = false;

        if (data.length <= me.markerMaxCount) {//判断数量大小
            me.isMarker = true;
            if (me.pointCollection || me.pointCollectionOff) {
                me.pointCollection.clear(); //清除海量点
                me.pointCollectionOff.clear(); //清除海量点
            }
            if (!me.markerCluster) {
                me.markerCluster = new BMapLib.MarkerClusterer(baiduMap, {
                    markers: mkrs,
                    girdSize: 256,
                    maxZoom: 15,
                    minClusterSize: 100000
                });
            } else {
                me.markerCluster.clearMarkers();
                me.markerCluster.addMarkers(mkrs);
            }
        } else {//数据量过大
            if (me.markerCluster) {
                me.markerCluster.clearMarkers(); //清除 聚集点
            }
            if (document.createElement('canvas').getContext) {  // 判断当前浏览器是否支持绘制海量点

                if (me.pointCollection || me.pointCollectionOff) {
                    me.pointCollection.clear();
                    me.pointCollectionOff.clear();
                    me.pointCollection.setPoints(me.ptsOn);
                    me.pointCollectionOff.setPoints(me.ptsOff);
                } else {
                    var options = {
                        size: BMAP_POINT_SIZE_SMALL,
                        shape: BMAP_POINT_SHAPE_CIRCLE,
                        color: '#00ff00'
                    }
                    me.pointCollection = new BMap.PointCollection(me.ptsOn, options);  // 初始化PointCollection
                    var optionsOff = {
                        size: BMAP_POINT_SIZE_SMALL,
                        shape: BMAP_POINT_SHAPE_CIRCLE,
                        color: '#ff0000'
                    }
                    me.pointCollectionOff = new BMap.PointCollection(me.ptsOff, optionsOff);  // 初始化PointCollection
                }
                /* me.pointCollection.addEventListener('click', function (e) {
                e._flag = "point";
                me.openInfoWinRC(e);  // 监听点击事件
                });
                me.pointCollection.addEventListener('mouseover', function (e) {
                e._flag = "point";
                me.openInfoWin(e);  // 监mouseover击事件
                });*/

                baiduMap.addOverlay(me.pointCollectionOff);  // 添加Overlay
                baiduMap.addOverlay(me.pointCollection);  // 添加Overlay
            } else {
                alert('请使用chrome、safari、ie8+以上浏览器');
            }
        }
        baiduMap.addEventListener('moveend', function (e) {
            var map = e.target;
            window.Index.pointCollectionOrMarker(map, window.Index.markerMaxCount);
        });

        if (!baiduMap._zoomendListener) {//仅绑定一次zoomend事件
            baiduMap._zoomendListener = true;

            //根据不同级别修改为mkr预设定的3套icon
            baiduMap.addEventListener('zoomend', function (e) {
                var map = e.target;
                window.Index.pointCollectionOrMarker(map, window.Index.markerMaxCount);

            });
            // baiduMap.setViewport(pts); //缩放到电动车视野内
            window.baiduMap.centerAndZoom(new BMap.Point(118.767, 32.05), 5);
        }
    },
    threeGuaranteesCar: function (type, num) {
        var me = this;
        if (num == 0) {
            if (threeguaranteescount == 1) {
                me.threeGuardMethod(type);
            }
        }
        else {
            if (threeguaranteescount == 0) {
                me.threeGuardMethod(type);
                threeguaranteescount = 1;
            }
            else {
                if (me.threeguaranteespointCollection || me.threeguaranteespointCollectionOff) {
                    me.threeguaranteespointCollection.clear(); //清除海量点
                    me.threeguaranteespointCollectionOff.clear(); //清除海量点
                }
                if (me.threeguaranteesmarkerCluster) {
                    this.threeguaranteesmarkerCluster.clearMarkers(); //清除聚集点
                }
                threeguaranteescount = 0;
            }
        }
    },
    threeGuardMethod: function (type) {
        var me = this;
        if (me.pointCollection || me.pointCollectionOff) {
            me.pointCollection.clear(); //清除海量点
            me.pointCollectionOff.clear(); //清除海量点
        }
        if (me.markerCluster) {
            this.markerCluster.clearMarkers(); //清除聚集点
        }
        var tree = mini.get("treeProvince");
        var nodes = tree.getAllChildNodes(tree.getRootNode());
        tree.uncheckNodes(nodes);
        var tree1 = mini.get("treeMain");
        var nodes1 = tree1.getAllChildNodes(tree1.getRootNode());
        tree1.uncheckNodes(nodes1);

        $.ajax({
            url: 'actions/get_threeguarantees.ashx?type=' + type,
            type: 'post',
            dataType: "json",
            error: function () { },
            success: function (data) {
                var mkrs = [];
                var labels = "";
                mkrs.length = 0;
                var me = window.methis,

            zoom = window.baiduMap.getZoom(),
            isShowOffline = serverInfo.configParam.isShowOffline,
            isShowTargetName = serverInfo.configParam.isShowTargetName;
                me.ptsOn.length = 0;
                me.ptsOff.length = 0;
                if (!me.threeguaranteesmarkerCluster) {//创建聚集
                    me.threeguaranteesmarkerCluster = new BMapLib.MarkerClusterer(baiduMap, {
                        markers: mkrs,
                        girdSize: 256,
                        maxZoom: 15,
                        minClusterSize: 100000
                    });
                }

                for (var i = 0; i < data.length; i++) {
                    var item = data[i],
                    x = item.x,
                    y = item.y,
                    longitude = item.longitude,
                    latitude = item.latitude,
                    id = item.id,
                    targetnumber = item.targetnumber,
                    sim = item.sim,
                    state = item.isonline, //包含4种状态，accon,accoff,offline,alarm
                    type = item.carType,
                    speed = item.velocity,
                    carownername = item.carownername,
                    carownerphone = item.carownerphone,
                    carowneraddress = item.carowneraddress,
                        carplatenumber = item.carplatenumber,
                    time1 = item.time,
                    mkr,
                    pt,
                    icon;
                    curIcons = me.getCurrentIcons(zoom, type);
                    if (!(isShowOffline == 0 && state == 'offline')) {
                        icon = curIcons[state];
                        pt = new BMap.Point(x, y);
                        pt._id = id;
                        mkr = new BMap.Marker(pt, { icon: icon });

                        mkr._flag = "marker";

                        // baiduMap.addOverlay(mkr);
                        mkr._label = new BMap.Label(targetnumber + ', ' + speed + 'KM/H', {
                            offset: new BMap.Size(30, 5)
                        });
                        mkr._label._id = id; //目标信息绑定到label，marker 方式加载和“海量点加载”公用一个label
                        mkr._label._targetnumber = targetnumber;
                        mkr._label._sim = sim;
                        mkr._label._ownType = type;
                        mkr._label._speed = speed;
                        mkr._label._state = state;
                        mkr._label._x = x;
                        mkr._label._y = y;
                        mkr._label._longitude = longitude;
                        mkr._label._latitude = latitude;
                        mkr._label._time = time1;
                        mkr._label._carownername = carownername;
                        mkr._label._carownerphone = carownerphone;
                        mkr._label._carowneraddress = carowneraddress;
                        mkr._label._carplatenumber = carplatenumber;

                        mkr._label.setStyle({
                            font: '12px 微软雅黑',
                            padding: '2px 2px',
                            border: 'solid 1px #000',
                            color: '#000',
                            backgroundColor: '#FFFFCC'
                        });
                        mkr.setLabel(mkr._label);
                        if (zoom < me.displayLabelZoom || (isShowTargetName !== 1)) {
                            mkr._label.hide();
                        }

                        mkrs.push(mkr);
                        if (state == "offline") {
                            me.ptsOff.push(pt);
                        } else {
                            me.ptsOn.push(pt);
                        }

                        labels[id] = mkr._label;

                        mkr.addEventListener('click', function () {
                            //if (serverInfo.UserConfigParam.isShowTag == 1) {
                            me.openInfoWin(this);
                            //}
                        });
                        mkr.addEventListener("rightclick", function () {
                            me.openInfoWinRC(this);
                        });
                    }
                }
                me.isMarker = false;

                if (data.length <= me.markerMaxCount) {//判断数量大小
                    me.isMarker = true;

                    if (me.threeguaranteespointCollection || me.threeguaranteespointCollectionOff) {
                        me.threeguaranteespointCollection.clear(); //清除海量点
                        me.threeguaranteespointCollectionOff.clear(); //清除海量点
                    }
                    if (!me.threeguaranteesmarkerCluster) {
                        me.threeguaranteesmarkerCluster = new BMapLib.MarkerClusterer(baiduMap, {
                            markers: mkrs,
                            girdSize: 256,
                            maxZoom: 15,
                            minClusterSize: 100000
                        });
                    } else {
                        me.threeguaranteesmarkerCluster.clearMarkers();
                        me.threeguaranteesmarkerCluster.addMarkers(mkrs);
                    }
                } else {//数据量过大
                    if (me.threeguaranteesmarkerCluster) {
                        me.threeguaranteesmarkerCluster.clearMarkers(); //清除 聚集点
                    }
                    if (document.createElement('canvas').getContext) {  // 判断当前浏览器是否支持绘制海量点
                        if (me.threeguaranteespointCollection || me.threeguaranteespointCollectionOff) {
                            me.threeguaranteespointCollection.clear();
                            me.threeguaranteespointCollectionOff.clear();
                            me.threeguaranteespointCollection.setPoints(me.ptsOn);
                            me.threeguaranteespointCollectionOff.setPoints(me.ptsOff);
                        } else {
                            var options = {
                                size: BMAP_POINT_SIZE_SMALL,
                                shape: BMAP_POINT_SHAPE_CIRCLE,
                                color: '#00ff00'
                            }
                            me.threeguaranteespointCollection = new BMap.PointCollection(me.ptsOn, options);  // 初始化PointCollection
                            var optionsOff = {
                                size: BMAP_POINT_SIZE_SMALL,
                                shape: BMAP_POINT_SHAPE_CIRCLE,
                                color: '#ff0000'
                            }
                            me.threeguaranteespointCollectionOff = new BMap.PointCollection(me.ptsOff, optionsOff);  // 初始化PointCollection
                        }
                        /* me.pointCollection.addEventListener('click', function (e) {
                        e._flag = "point";
                        me.openInfoWinRC(e);  // 监听点击事件
                        });
                        me.pointCollection.addEventListener('mouseover', function (e) {
                        e._flag = "point";
                        me.openInfoWin(e);  // 监mouseover击事件
                        });*/

                        baiduMap.addOverlay(me.threeguaranteespointCollectionOff);  // 添加Overlay
                        baiduMap.addOverlay(me.threeguaranteespointCollection);  // 添加Overlay
                    } else {
                        alert('请使用chrome、safari、ie8+以上浏览器');
                    }
                }
                baiduMap.addEventListener('moveend', function (e) {
                    var map = e.target;
                    window.Index.pointCollectionOrMarker(map, window.Index.markerMaxCount);
                });

                if (!baiduMap._zoomendListener) {//仅绑定一次zoomend事件
                    baiduMap._zoomendListener = true;
                    //根据不同级别修改为mkr预设定的3套icon
                    baiduMap.addEventListener('zoomend', function (e) {
                        var map = e.target;
                        window.Index.pointCollectionOrMarker(map, window.Index.markerMaxCount);

                    });
                    // baiduMap.setViewport(pts); //缩放到电动车视野内
                    window.baiduMap.centerAndZoom(new BMap.Point(118.767, 32.05), 5);
                }
            }
        });
    },
    pointCollectionOrMarker: function (map, markerCount) {
        var isShowTargetName = serverInfo.configParam.isShowTargetName;
        var me = this;
        var zoom = map.getZoom(),
              mkrs = me.mkrs;
        if (!me.markerCluster) {//创建聚集
            me.markerCluster = new BMapLib.MarkerClusterer(baiduMap, {
                markers: mkrs,
                girdSize: 256,
                maxZoom: 15,
                minClusterSize: 100000
            });
        }
        if (!me.pointCollection || !me.pointCollectionOff) {//创建海量点容器
            if (document.createElement('canvas').getContext) {  // 判断当前浏览器是否支持绘制海量点
                var options = {
                    size: BMAP_POINT_SIZE_SMALL,
                    shape: BMAP_POINT_SHAPE_CIRCLE,
                    color: '#00ff00'
                }
                me.pointCollection = new BMap.PointCollection([], options);  // 初始化PointCollection
                var optionsOff = {
                    size: BMAP_POINT_SIZE_SMALL,
                    shape: BMAP_POINT_SHAPE_CIRCLE,
                    color: '#ff0000'
                }
                me.pointCollectionOff = new BMap.PointCollection([], optionsOff);  // 初始化PointCollection
                baiduMap.addOverlay(me.pointCollectionOff);  // 添加Overlay
                baiduMap.addOverlay(me.pointCollection);  // 添加Overlay
            } else {
                alert('请使用chrome、safari、IE8+以上浏览器');
            }
        }
        me.markerCluster.clearMarkers();
        var bs = map.getBounds();   //获取可视区域
        var bssw = bs.getSouthWest();   //可视区域左下角
        var bsne = bs.getNorthEast();   //可视区域右上角
        me.mkrs_array.length = 0;
        //me.points_on.length = 0;
        //me.points_off.length = 0;
        var count = 0;

        for (var i = 0, l = mkrs.length; i < l; i++) {//判断可视区域内的点数
            var lable = mkrs[i]._label;
            var x = lable._x;
            var y = lable._y;
            if ((x >= bssw.lng && x <= bsne.lng) && (y >= bssw.lat && y <= bsne.lat)) {
                me.mkrs_array.push(mkrs[i]);
                //if (mkrs[i]._label._state == "offline") {
                //    me.points_off.push(mkrs[i].getPosition());
                //} else {
                //    me.points_on.push(mkrs[i].getPosition());
                //}
                count++;
                if (count > markerCount) {
                    //me.points_off = me.ptsOff;
                    //me.points_on = me.ptsOn;
                    break;
                }
            }
        }

        if (count <= markerCount) {
            for (var i = 0, l = me.mkrs_array.length; i < l; i++) {
                var lable = me.mkrs_array[i]._label;
                var mkr = me.mkrs_array[i], //设置marker图标
                    state = mkr._label._state,
                    type = mkr._label._ownType,
                    curIcons = me.getCurrentIcons(zoom, type),
                    icon = curIcons[state];
                mkr.setIcon(icon);
                // map.addOverlay(mkr);
                if (isShowTargetName !== 1) {
                    mkr._label.hide();
                } else {
                    if (zoom < me.displayLabelZoom) {
                        mkr._label.hide();
                    } else {
                        mkr._label.show();
                    }
                }
            }
            if (me.pointCollection || me.pointCollectionOff) {
                me.pointCollection.clear(); //清除海量点
                me.pointCollectionOff.clear(); //清除海量点
            }
            if (!me.markerCluster) {
                me.markerCluster = new BMapLib.MarkerClusterer(baiduMap, {
                    markers: mkrs,
                    girdSize: 256,
                    maxZoom: 15,
                    minClusterSize: 100000
                });
            } else {
                me.markerCluster.clearMarkers();
                me.markerCluster.addMarkers(me.mkrs_array);
            }
        } else {
            if (me.pointCollection || me.pointCollectionOff) {
                me.pointCollection.clear();
                me.pointCollectionOff.clear();
                me.pointCollection.setPoints(me.ptsOn);
                me.pointCollectionOff.setPoints(me.ptsOff);
            }
        }
    },
    //打开信息窗口 目标信息在label中
    openInfoWin: function (e) {
        var me = this;
        var label = null;
        if (e._flag == "marker") {
            label = e._label;
        } else {
            label = me.labels[e.point._id];
        }

        var myGeo = new BMap.Geocoder();
        // 根据坐标得到地址描述 
        myGeo.getLocation(new BMap.Point(label._x, label._y), function (result) {
            if (result) {
                var address = result.address;
                //                alert(address);
                var vehiclename = "";
                var cook = getCookie("dependencyid");
                if (cook == "2") {
                    vehiclename = label._carplatenumber;
                }
                else {
                    vehiclename = label._targetnumber;
                }
                var str = '	<table style="width:300px;height:160px;font-family:Microsoft Yahei;font-size:12px"> ' +
		'<tr style="height:10px">' +
		'	<td style="border-bottom:1px solid red" colspan="2">' +
		'		<span style="color:#000;font-weight: bold">' + vehiclename + '</span>' +
		'	</td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td style="width:61%">' +
		'		<span style="display:inline-block;color:#98B421;padding-left:8px;">GPS时间:</span><span style="font-size:12px">' + label._time + '</span>' +
		'	</td>' +
		'	<td >' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">速度:</span><span style="font-size:12px">' + label._speed + 'km/h</span>' +
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
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">经度:</span><span style="font-size:12px">' + label._longitude + '东</span>' +
		'	</td>' +
		'	<td>' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">报警信息:</span><span style="font-size:12px"></span>' +
		'	</td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td  colspan="2">' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">纬度:</span><span style="font-size:12px">' + label._latitude + '北</span>' +
		'	</td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td colspan="2">' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;font-size:12px">地址:</span><span style="font-size:12px">' + address + '</span>' +
		'	</td>' +
		'</tr>' +
	'</table>';
                me.infoWin.setContent(str);
                if (e._flag == "marker") {
                    e.openInfoWindow(me.infoWin);
                    setTimeout(function () {
                        e.openInfoWindow(me.infoWin);
                    }, 1000);
                } else {
                    baiduMap.openInfoWindow(me.infoWin, e.point)
                }
            }
        });
    },
    //右键打开车辆信息窗口
    openInfoWinRC: function (e) {
        var me = this;
        var carType = e._label._ownType;
        if ("1" == carType) {
            carType = "摩托车";
        } else if ("2" == carType) {
            carType = "电动车";
        } else if ("3" == carType) {
            carType = "小汽车";
        } else if ("4" == carType) {
            carType = "货车";
        } else if ("5" == carType) {
            carType = "警车";
        } else if ("6" == carType) {
            carType = "挖掘机";
        } else if ("7" == carType) {
            carType = "拖拉机";
        } else if ("8" == carType) {
            carType = "收割机";
        } else {
            carType = "人员";
        }
        var str = '	<table style="width:300px;height:70px;font-family:Microsoft Yahei;font-size:12px" id="treeMenu" onbeforeopen="curIndex.onTreeCtxMenu(e)"> ' +
		'<tr style="height:10px">' +
		'	<td style="border-bottom:1px solid red" colspan="3">' +
		'		<span style="color:#000;font-weight: bold">车辆信息</span>' +
		'	</td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td valign="top">' +
		'		<span style="display:inline-block;color:#98B421;padding-left:8px;">姓名:</span><span style="font-size:12px"> ' + e._label._carownername + '</span>' +
		'	</td>' +
        '   <td valign="top">' +

        '      <li class="li1" style="margin:0;padding:0;list-style:none"><span>尾迹查询</span>' +
       '          <ul class="nav2" style="margin:0;padding:0;list-style:none">' +
       '               <li class="li2" style="margin:0;padding:0;list-style:none" id="dt">地图显示尾迹</li>' +
       '                <li class="li2" style="margin:0;padding:0;list-style:none" id="lb">列表显示尾迹</li>' +
       '          </ul>' +
        '      </li>' +

        '   </td>' +
        '   <td valign="top">' +

        '             <li style="margin:0;padding:0;list-style:none" id="gj"><span>轨迹回放</span>' +

        '            </li>' +
        '  </td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td valign="top">' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">联系电话:</span><span style="font-size:12px">' + e._label._carownerphone + '</span>' +
		'	</td>' +
        '   <td valign="top">' +
        '       <li style="margin:0;padding:0;list-style:none" id="bx">保险查询</li> ' +
        '   </td>' +
      '   <td valign="top">' +

        '      <li class="li11" style="margin:0;padding:0;list-style:none"><span>终端参数设置</span>' +
       '          <ul class="nav22" style="margin:0;padding:0;list-style:none">' +
       '               <li class="li22" style="margin:0;padding:0;list-style:none" id="dc">单参数设置</li>' +
       '                <li class="li22" style="margin:0;padding:0;list-style:none" id="pl">批量参数设置</li>' +
       '          </ul>' +
        '      </li>' +

        '   </td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td valign="top">' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">车辆类型:</span><span style="font-size:12px">' + carType + '</span>' +
		'	</td>' +
        '   <td valign="top" style="width:80px">' +
          '      <li class="dwhb" style="margin:0;padding:0;list-style:none"><span>调位/划拨</span>' +
               '          <ul class="navdwhb" style="margin:0;padding:0;list-style:none">' +
               '      <li style="margin:0;padding:0;list-style:none" id="tw">调位指令</li>  ' +
        '       <li style="margin:0;padding:0;list-style:none" id="hb">划拨指令</li> ' +
       '          </ul>' +
        '      </li>' +
        '   </td>' +
        '   <td valign="top">' +
        '      <li class="dc" style="margin:0;padding:0;list-style:none"><span>导出信息</span>' +
               '          <ul class="navdc" style="margin:0;padding:0;list-style:none">' +
        '       <li style="margin:0;padding:0;list-style:none" id="dcgj">导出车辆轨迹信息</li> ' +
        '       <li style="margin:0;padding:0;list-style:none" id="dcxx">导出车辆详细信息</li> ' +
       '          </ul>' +
        '      </li>' +
        '   </td>' +
		'</tr>' +
		'<tr style="height:10px">' +
		'	<td valign="top"  colspan="2">' +
		'		<span style="display:inline-block;margin-right:5px;color:#98B421;padding-left:8px;">注册地址:</span><span style="font-size:12px">' + e._label._carowneraddress + '</span>' +
		'	</td>' +
        '   <td valign="top" style="width:100px">' +
          '       <li style="margin:0;padding:0;list-style:none" id="zd">终端区域设置</li> ' +
        '   </td>' +
		'</tr>' +
        '<tr style="height:10px">' +
        '   <td valign="top"> ' +
        '      <li style="margin:0;padding:0;list-style:none" id="zt">历史状态</li> ' +
        '   </td>' +
        '   <td valign="top"> ' +
        '      <li style="margin:0;padding:0;list-style:none" id="bj">历史报警</li> ' +
        '   </td>' +
        '   <td valign="top"> ' +
        '      <li style="margin:0;padding:0;list-style:none" id="xx">发送信息</li> ' +
        '   </td>' +
        '</tr>' +
	'</table>';

        me.infoWinRC.setContent(str);
        if (e._flag == "marker") {
            e.openInfoWindow(me.infoWinRC);
        } else {
            baiduMap.openInfoWindow(me.infoWinRC, e.point)
        }

        $(".nav2").css("display", "none");
        $(".navdc").css("display", "none");
        $(".navdwhb").css("display", "none");
        $(".li1").hover(function () {
            $(".nav2").css("display", "block");
        }, function () {
            $(".nav2").css("display", "none");
        });
        $(".dwhb").hover(function () {
            $(".navdwhb").css("display", "block");
        }, function () {
            $(".navdwhb").css("display", "none");
        });
        $(".dc").hover(function () {
            $(".navdc").css("display", "block");
        }, function () {
            $(".navdc").css("display", "none");
        });
        $(".nav22").css("display", "none");
        $(".li11").hover(function () {
            $(".nav22").css("display", "block");
        }, function () {
            $(".nav22").css("display", "none");
        });
        $("#dt").click(function () {
            Index.onInfoWindowMenu(e._label._id, "地图显示尾迹");
        });
        $("#lb").click(function () {
            Index.onInfoWindowMenu(e._label._id, "列表显示尾迹");
        });
        $("#gj").click(function () {
            Index.onInfoWindowMenu(e._label._targetnumber, "轨迹回放");
        });
        $("#bx").click(function () {
            Index.onInfoWindowMenu(e._label._id, "保险查询");
        });
        $("#dc").click(function () {
            Index.onInfoWindowMenu(e._label._id, "单参数设置");
        });
        $("#pl").click(function () {
            Index.onInfoWindowMenu(e._label._id, "批量参数设置");
        });
        $("#zd").click(function () {
            Index.onInfoWindowMenu(e._label._id, "终端区域设置");
        });
        $("#tw").click(function () {
            Index.onInfoWindowMenu(e._label._id, "调位指令");
        });
        $("#zt").click(function () {
            Index.onInfoWindowMenu(e._label._id, "历史状态");
        });
        $("#bj").click(function () {
            Index.onInfoWindowMenu(e._label._id, "历史报警");
        });
        $("#xx").click(function () {
            Index.onInfoWindowMenu(e._label._id, "发送信息");
        })
        $("#dcgj").click(function () {
            Index.onInfoWindowMenu(e._label._targetnumber, "导出车辆轨迹信息");
        })
        $("#dcxx").click(function () {
            Index.onInfoWindowMenu(e._label._targetnumber, "导出车辆详细信息");
        })
        $("#hb").click(function () {
            Index.onInfoWindowMenu(e._label._targetnumber, "车辆划拨");
        })
    },
    // 右键菜单处理函数
    onRightClickTreeBeforeMenu: function (e) {
        righte = e;
    },
    //树节点右键菜单
    onRightClickTreeMenu: function (e) {
        var treeE = e;
        /* var str = Index.selTreeNode.id,
        arr = str.split(','),
        num = arr[0];*/
        e = window.righte;
        var me = this;
        var record = e.records;
        id = record[0].id;
        name = record[0].name;

        //isBikeNode = (record.img == 'icon_bike.png'/*tpa=http://www.beidouguard.com:6010/js/icon_bike.png*/ || record.img == 'icon_person.png'/*tpa=http://www.beidouguard.com:6010/js/icon_person.png*/ || record.img == 'icon_car.png'/*tpa=http://www.beidouguard.com:6010/js/icon_car.png*/) ? true : false; //是否是电动车节点，根据icon_bike.png判定
        //	isBikeNode = (record.img != 'location.png'/*tpa=http://www.beidouguard.com:6010/js/location.png*/ && record.img != 'icon_user.png'/*tpa=http://www.beidouguard.com:6010/js/icon_user.png*/) ? true : false; //是否是叶子节点
        /*  if (isBikeNode) {//叶子节点
        Index.zoomToMarker(id);
        }*/
        var tabName = mini.get("searchTabs").getActiveTab().name;
        //var keyOfTargetSearch = mini.get('targetSearch').getValue();
        var data = [];
        if (tabName == "org") {
            window.selectedData = me.getSelectedData(record[0].id, "org", record[0].name);
        } else if (tabName == "province") {
            window.selectedData = me.getSelectedData(record[0].name, "province", record[0].name);
        }
        var strnums = "";
        for (var i = 0; i < window.selectedData.length; i++) {
            strnums += window.selectedData[i].num + ",";
        }
        strnums = strnums.substring(0, strnums.length - 1);
        e = treeE;
        var menuItemText = $.trim(e.sender.getText());
        switch (menuItemText) {
            case '批量导出车辆详细信息':
                Index.onTreeMenu_exportbatchvehicledetails(strnums);
                break;
            case '批量划拨':
                Index.onTreeMenu_batchvehicletransfer(id);
                break;
            case '群发短信':
                Index.onTreeMenu_batchsendmessage(strnums);
                break;
        }
    },
    //群发短信窗体数据
    onTreeMenu_batchsendmessage: function (datanum) {
        var trackbackD = mini.get("batchsendmessage");
        trackbackD.show('center', 'middle');
        mini.get('batchsendmessageDataGrid').setData(null);
        $.ajax({
            url: 'actions/batchsendmessage.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/batchsendmessage.ashx*/,
            type: 'POST',
            data: { datanum: datanum },
            error: function () { },
            success: function (data) {
                var arr = null;
                try { eval("arr=" + data) } catch (e) { }
                mini.get('batchsendmessageDataGrid').setData(arr);
            }
        });
    },
    //发送短信
    sendmessage: function () {
        var message = mini.get('message').getValue();
        var data = mini.get("batchsendmessageDataGrid").getSelecteds();
        var targetnumber = "";
        for (var i = 0; i < data.length; i++) {
            targetnumber += data[i].targetnumber + ",";
        }
        var targetnumbers = targetnumber.substring(0, targetnumber.length - 1);
        $.ajax({
            url: 'actions/batch_send_message.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/batch_send_message.ashx*/,
            type: 'POST',
            data: { datanum: targetnumbers, message: message },
            success: function (data) {
                alert(data);
            }
        });
    },
    //批量导出车辆详细信息
    onTreeMenu_exportbatchvehicledetails: function (datanum) {
        var trackbackD = mini.get("vehicleDetailsDiv");
        trackbackD.show('center', 'middle');
        mini.get('vehicleDetailsTableDiv').setData(null);
        $.ajax({
            url: 'actions/get_exportbatchvehicledetails.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_exportbatchvehicledetails.ashx*/,
            type: 'POST',
            data: { datanum: datanum, type: "1" },
            error: function () { },
            success: function (data) {
                var arr = null;
                try { eval("arr=" + data) } catch (e) { }
                mini.get('vehicleDetailsTableDiv').setData(arr);
            }
        });
    },
    importCarInfo: function () {
        var targetnumber = mini.get('txttargetnumber').getValue();
        var simcard = mini.get('txtsimcard').getValue();
        var status = mini.get('txtstatus').getValue();
        var model = mini.get('txtmodel').getValue();
        var carbrand = mini.get('txtcarbrand').getValue();
        var cartype = mini.get('txtcartype').getValue();
        var carmodel = mini.get('txtcarmodel').getValue();
        var caridnumber = mini.get('txtcaridnumber').getValue();
        var carplatenumber = mini.get('txtcarplatenumber').getValue();
        var carproducer = mini.get('txtcarproducer').getValue();
        var carpower = mini.get('txtcarpower').getValue();
        var carownername = mini.get('txtcarownername').getValue();
        var carowneraddree = mini.get('txtcarowneraddree').getValue();
        var carownerphone = mini.get('txtcarownerphone').getValue();

        $.ajax({
            url: 'actions/update_vehicleInfo.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/update_vehicleInfo.ashx*/,
            type: 'POST',
            data: { targetnumber: targetnumber, simcard: simcard, status: status, model: model, carbrand: carbrand, cartype: cartype, carmodel: carmodel, caridnumber: caridnumber, carplatenumber: carplatenumber, carproducer: carproducer, carpower: carpower, carownername: carownername, carowneraddree: carowneraddree, carownerphone: carownerphone },
            success: function (data) {
                if (data == 1) {
                    alert("保存成功");
                    var trackbackD = mini.get("vehicleInfoDiv");
                    trackbackD.hide();
                }
                else {
                    alert("保存失败");
                }
            }
        });
    },
    exportCarDetails: function () {
        var data = mini.get("vehicleDetailsTableDiv").getSelecteds();
        var targetnumber = "";
        for (var i = 0; i < data.length; i++) {
            targetnumber += data[i].targetnumber + ",";
        }
        var targetnumbers = targetnumber.substring(0, targetnumber.length - 1);
        $.ajax({
            url: 'actions/get_exportbatchvehicledetails.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_exportbatchvehicledetails.ashx*/,
            type: 'POST',
            data: { datanum: targetnumbers, type: "0" },
            success: function (data) {
                var downUrl = "actions/exportCarTrack.ashx?fullFileName=" + data;
                window.location = downUrl;
            }
        });
    },
    //批量划拨
    onTreeMenu_batchvehicletransfer: function (id) {
        var trackbackD = mini.get("batchvehicletransferDiv");
        trackbackD.show('center', 'middle');
        mini.get('dgimportvehicle').setData(null);
        var path = $('#excelFile').val("");
        mini.get("batchvehicletransferTree").load('actions/get_org_json.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_org_json.ashx*/);
        mini.get("batchvehicletransferTree").collapseLevel(2);
    },
    downloadimporttemplet: function (url) {
        var downUrl = "actions/exportCarTrack.ashx?fullFileName=" + url;
        window.location = downUrl;
    },
    confirmbatchvehicletransfer: function (e) {
        var data = mini.get("dgimportvehicle").getChanges();
        if (data == null || data.length == 0) {
            alert("没有车辆修改了组织机构");
            return;
        }
        else {
            var targetnumber = "";
            var neworg = "";
            for (var i = 0; i < data.length; i++) {
                targetnumber += data[i].targetnumber + ",";
                neworg += data[i].neworgid + ",";
            }
            var str = targetnumber.substring(0, targetnumber.length - 1) + "#" + neworg.substring(0, neworg.length - 1);
            $.ajax({
                url: 'actions/update_batchvehicletransfer.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/update_batchvehicletransfer.ashx*/,
                type: 'POST',
                data: { griddata: str },
                success: function (data) {
                    if (Number(data) == 0) {
                        alert("划拨失败");
                    }
                    else {
                        var trackbackD = mini.get("batchvehicletransferDiv");
                        trackbackD.hide();
                        //this.init();
                        mini.get("treeMain").load('actions/get_org_json.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_org_json.ashx*/);
                        mini.get("treeMain").collapseLevel(2);

                        Index.showLeftPanel();

                        var grid = mini.get("targetgrid");
                        grid.loading("加载数据中......");
                        $.ajax({
                            url: 'actions/get_target_json.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_target_json.ashx*/,
                            type: 'post',
                            error: function () {
                                baiduMap.removeOverlay(overlay);

                            },
                            success: function (data) {

                                window.targetList = eval("(" + data + ")");
                                mini.get("targetgrid").unmask();
                                var len = window.targetList.length;
                                for (var i = 0; i < len; i++) {
                                    window.targetList[i].check = false;
                                }
                            }
                        });
                    }
                },
                error: function () {
                    alert("划拨失败");
                }
            });
        }
        //            var trackbackD = mini.get("batchvehicletransferDiv");
        //            trackbackD.hide();
    },
    //右键菜单处理函数
    onTreeCtxMenu: function (e) {
        var menu = e.sender;
        var grid = mini.get("targetgrid");
        var row = grid.getSelected();
        this.selGridRow = row;
        //tree = mini.get("treeMain"),
        //node = tree.getSelectedNode(),
        //isBikeNode = (node.img == 'icon_bike.png'/*tpa=http://www.beidouguard.com:6010/js/icon_bike.png*/ || node.img == 'icon_person.png'/*tpa=http://www.beidouguard.com:6010/js/icon_person.png*/ || node.img == 'icon_car.png'/*tpa=http://www.beidouguard.com:6010/js/icon_car.png*/) ? true : false; //是否是电动车节点，根据icon_bike.png判定
        //isBikeNode = (node.img != 'location.png'/*tpa=http://www.beidouguard.com:6010/js/location.png*/ && node.img != 'icon_user.png'/*tpa=http://www.beidouguard.com:6010/js/icon_user.png*/) ? true : false; //是否是叶子节点

        /*   if (!node || !isBikeNode) {
        e.htmlEvent.preventDefault();
        e.cancel = true;
        return;
        }*/

        //  Index.selTreeNode = node;
        //  tree.selectNode(null); //最后不选择
    },
    //报警面板右键菜单处理函数
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

        Index.selTreeNode = node;
        tree.selectNode(null); //最后不选择
    },
    //报警面板菜单项处理函数
    onTreeMenuRgn: function (e) {
        var menuItemText = $.trim(e.sender.text),
            num = Index.selTreeNode.id;

        switch (menuItemText) {
            case "修改报警区域":
                $.ajax({
                    url: 'actions/get_alarm_list.ashx?id=' + num,
                    type: 'post',
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
                                        type: 'post',
                                        error: function () { mini.alert('修改失败'); },
                                        success: function (data) {
                                            if (data == "个数太少") {
                                                mini.alert("选择区域内聚集报警,关联终端个数应该大于等于最小的报警个数,请重新设定");
                                                baiduMap.removeOverlay(overlay);
                                                me.close();
                                                return;
                                            }
                                            if (data) {
                                                var obj = null;
                                                try { eval("obj=" + data) } catch (e) { }
                                                var newName = obj.name,
                                                    treeRgn = mini.get("treeRgn");
                                                treeRgn.updateNode(Index.selTreeNode, { text: newName });
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
                        type: 'post',
                        error: function () { mini.alert('删除失败'); },
                        success: function (data) {
                            if (data == "1") {
                                mini.get('treeRgn').removeNode(Index.selTreeNode); //报警区域管理树中删除对应节点
                                baiduMap.removeOverlay(Index.alarmPly);
                                me.loadData();
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
                if (Index.alarmPly) {
                    baiduMap.removeOverlay(Index.alarmPly);
                } else {
                    mini.alert("请先查看用户区域报警");
                }
                break;
        }
    },
    //尾迹查询
    onTreeMenu_weijichaxun: function (num) {
        $.ajax({
            url: 'actions/get_target_weiji.ashx?num=' + num + '&mapSource=baidu',
            type: 'post',
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

                for (var i = 0, l = arr.length; i < l; i += 2) {
                    pts.push(new BMap.Point(arr[i], arr[i + 1]));
                }

                baiduMap.removeOverlay(Index.startMkr);
                baiduMap.removeOverlay(Index.endMkr);
                baiduMap.removeOverlay(Index.tailPly);

                var startIcon = 'images/dest_markers.png'/*tpa=http://www.beidouguard.com:6010/js/images/dest_markers.png*/;
                Index.startMkr = new BMap.Marker(pts[0],
                            { 'icon': new BMap.Icon(startIcon, new BMap.Size(32, 32), { imageOffset: new BMap.Size(0, 0) }),
                                'title': '起点'
                            });
                baiduMap.addOverlay(Index.startMkr);

                Index.endMkr = new BMap.Marker(pts[pts.length - 1],
                            { 'icon': new BMap.Icon(startIcon, new BMap.Size(32, 32), { imageOffset: new BMap.Size(0, -32) }),
                                'title': '终点'
                            });
                baiduMap.addOverlay(Index.endMkr);

                Index.tailPly = new BMap.Polyline(pts, {
                    strokeColor: '#FF0000'
                });
                Index.tailPly.addEventListener('click', function () {
                    var pts = this.getPath(),
                        arr = [];
                    for (var i = 0, l = pts.length; i < l; i++) {
                        arr.push(pts[i].lng + ',' + pts[i].lat + ';<br>');
                    }
                    mini.alert(arr.join(''));
                });
                baiduMap.addOverlay(Index.tailPly);
                baiduMap.setViewport(Index.tailPly.getPath());
                mini.alert("单击起点,即可在地图上去除尾迹");
                Index.startMkr.addEventListener('click', function () {
                    baiduMap.removeOverlay(Index.startMkr);
                    baiduMap.removeOverlay(Index.endMkr);
                    baiduMap.removeOverlay(Index.tailPly);
                    baiduMap.removeOverlay(Index.tailPly.getPath());
                    Index.tailPly = null;
                });
            }
        });
    },
    //尾迹查询
    onTreeMenu_weijiliebiao: function (num) {
        $.ajax({
            url: 'actions/show_target_weiji.ashx?num=' + num + '&mapSource=baidu',
            type: 'post',
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
    showMiniWindow: function (divName) {
        var divWindow = mini.get(divName);
        divWindow.show();
    },
    //轨迹回放
    onTreeMenu_guijihuifang: function (num) {
        if (num == 0) {
            if (window.Index.traceTargetNumber == "") {
                mini.alert("请单击选择左侧列表中的目标！");
                return;
            }
            num = window.Index.traceTargetNumber;
        }
        mini.get('alltargetlist').setValue(num);
        //alltargetlist.load('actions/combox_targetlist.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/combox_targetlist.ashx*/);
        //if (num == 0)
        //    alltargetlist.select(0);
        //else
        //    alltargetlist.setValue(num);

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
            type: 'post',
            error: function () { },
            success: function (data) {
                if (data.lendth <= 0) {
                    return;
                }
                //$('#spanInsureId').html(num);
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
            type: 'post',
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
            type: 'post',
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
                    type: 'post',
                    success: function (data) {
                        if (data.length <= 0) { return; }
                        if (data == 1) {
                            $('#spanBPSNum').html(num);
                            //只有管理员具有设置2个字段权限                       
                            if (serverInfo.userInfo.userType === 1) {
                                mini.get('cfgValue2').disable();
                                mini.get('cfgValue3').disable();
                                mini.get('cfgValue10').disable();
                                mini.get('cfgValue11').disable();
                                mini.get('cfgValue13').disable();
                                mini.get('cfgValue8').disable();
                                mini.get('cfgValue9').disable();
                                $('#isAllowIPandPort').hide();
                                $('#isAllowSMS').hide();
                                $('#isCfgValue2').hide();
                                $('#isCfgValue3').hide();
                                $('#isCfgValue10').hide();
                                $('#isCfgValue11').hide();
                                $('#isCfgValue13').hide();
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
            type: 'post',
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
                    type: 'post',
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
    //调位指令
    onTreeMenu_diaoweizhiling: function (num) {
        var param = "settingType=DiaoWeiZhiLing"
            + "&num=" + num;
        $.ajax({
            url: 'actions/send_message.ashx?' + param,
            type: 'post',
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
            type: 'post',
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
            type: 'post',
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
    onTreeMenu_fasongxinxi: function (num) {
        mini.get("txtMsgContent").setValue('');

        var winHistoricAlarm = mini.get("winSendMsg");
        winHistoricAlarm.show('left', 'bottom');
    },
    //右键菜单项处理函数
    onTreeMenu: function (e) {
        /* var str = Index.selTreeNode.id,
        arr = str.split(','),
        num = arr[0];*/
        var num = 0;
        try {
            num = Index.selGridRow.id;
        } catch (e) {
            num = 11110;
        }

        var menuItemText = $.trim(e.sender.getText());
        switch (menuItemText) {
            case "地图显示尾迹":
                Index.onTreeMenu_weijichaxun(num);
                break;
            case "列表显示尾迹":
                Index.onTreeMenu_weijiliebiao(num);
                break;
            case "轨迹回放":
                Index.onTreeMenu_guijihuifang(Index.selGridRow.num);
                break;
            case "保险查询":
                Index.onTreeMenu_baoxianchaxun(num);
                break;
            case "单参数设置": //终端单个参数设置，hillpig 
                Index.onTreeMenu_dancanshushezhi(num);
                break;
            case "批量参数设置": //终端批量参数设置，hillpig 
                Index.onTreeMenu_piliangcanshushezhi(num);
                break;
            case "终端区域设置": //hillpig 
                Index.onTreeMenu_zhongduanquyushezhi(num);
                break;
            case "调位指令": //hillpig 
                Index.onTreeMenu_diaoweizhiling(num);
                break;
            case '历史状态':
                Index.onTreeMenu_lishizhuangtai(num);
                break;
            case '历史报警':
                Index.onTreeMenu_lishibaojing(num);
                break;
            case '发送信息':
                Index.onTreeMenu_fasongxinxi(num);
                break;
            case '导出车辆轨迹信息':
                Index.onTreeMenu_exportvehicletrackdetails(Index.selGridRow.num);
                break;
            case '导出车辆详细信息':
                Index.onTreeMenu_exportvehicledetails(Index.selGridRow.num);
                break;
            case '车辆划拨':
                Index.onTreeMenu_vehicletransfer(Index.selGridRow.num);
                break;
            case '修改用户信息':
                Index.onTreeMenu_updateUserInfo(Index.selGridRow.num);
                break;
        }
    },
    //右键infowindow处理的指令函数
    onInfoWindowMenu: function (num, menuItemText) {
        switch (menuItemText) {
            case "地图显示尾迹":
                Index.onTreeMenu_weijichaxun(num);
                break;
            case "列表显示尾迹":
                Index.onTreeMenu_weijiliebiao(num);
                break;
            case "轨迹回放":
                Index.onTreeMenu_guijihuifang(num);
                break;
            case "保险查询":
                Index.onTreeMenu_baoxianchaxun(num);
                break;
            case "单参数设置": //终端单个参数设置，hillpig 
                Index.onTreeMenu_dancanshushezhi(num);
                break;
            case "批量参数设置": //终端批量参数设置，hillpig 
                Index.onTreeMenu_piliangcanshushezhi(num);
                break;
            case "终端区域设置": //hillpig 
                Index.onTreeMenu_zhongduanquyushezhi(num);
                break;
            case "调位指令": //hillpig 
                Index.onTreeMenu_diaoweizhiling(num);
                break;
            case '历史状态':
                Index.onTreeMenu_lishizhuangtai(num);
                break;
            case '历史报警':
                Index.onTreeMenu_lishibaojing(num);
                break;
            case '发送信息':
                Index.onTreeMenu_fasongxinxi(num);
                break;
            case '导出车辆轨迹信息':
                Index.onTreeMenu_exportvehicletrackdetails(num);
                break;
            case '导出车辆详细信息':
                Index.onTreeMenu_exportvehicledetails(num);
                break;
            case '车辆划拨':
                Index.onTreeMenu_vehicletransfer(num);
                break;
            case '修改用户信息':
                Index.onTreeMenu_updateUserInfo(num);
                break;
        }
    },
    //单车辆划拨
    onTreeMenu_vehicletransfer: function (num) {
        transferNum = num;
        var trackbackD = mini.get("treeDiv");
        trackbackD.show('center', 'middle');
        mini.get("treeorg").load('actions/get_org_json.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_org_json.ashx*/);
        mini.get("treeorg").collapseLevel(2);
    },
    onTreeMenu_updateUserInfo: function (num) {
        $.ajax({
            url: "actions/updateUserInfo.ashx?targetnum='" + num + "'",
            type: 'POST',
            success: function (data) {
                mini.get('txttargetnumber').setValue(data.toString().split(',')[0]);
                mini.get('txtsimcard').setValue(data.toString().split(',')[1]);
                mini.get('txtstatus').setValue(data.toString().split(',')[2]);
                mini.get('txtmodel').setValue(data.toString().split(',')[3]);
                mini.get('txtcarbrand').setValue(data.toString().split(',')[4]);
                mini.get('txtcartype').setValue(data.toString().split(',')[5]);
                mini.get('txtcarmodel').setValue(data.toString().split(',')[6]);
                mini.get('txtcaridnumber').setValue(data.toString().split(',')[7]);
                mini.get('txtcarplatenumber').setValue(data.toString().split(',')[8]);
                mini.get('txtcarproducer').setValue(data.toString().split(',')[9]);
                mini.get('txtcarpower').setValue(data.toString().split(',')[10]);
                mini.get('txtcarownername').setValue(data.toString().split(',')[11]);
                mini.get('txtcarowneraddree').setValue(data.toString().split(',')[12]);
                mini.get('txtcarownerphone').setValue(data.toString().split(',')[13]);

                var vehicleInfoDiv = mini.get("vehicleInfoDiv");
                vehicleInfoDiv.show('center', 'middle');
            },
            error: function () {

            }
        });
    },
    beforeNodeClick: function (e) {
        transferE = e;
    },
    canelvehicletransfer: function () {
        var trackbackD = mini.get("treeDiv");
        trackbackD.hide();
    },
    confirmvehicletransfer: function (e) {
        if (transferE == null) {
            alert("请选择组织机构");
            return;
        }
        else {
            e = window.transferE;
            var me = this;
            var record = e.record;
            var id = record.id;
            $.ajax({
                url: 'actions/update_vehicletransfer.ashx?id=' + id + "&targetnum=" + window.transferNum,
                type: 'POST',
                success: function (data) {
                    if (Number(data) == 0) {
                        alert("划拨失败");
                    }
                    else {
                        var trackbackD = mini.get("treeDiv");
                        trackbackD.hide();
                        //this.init();
                        mini.get("treeMain").load('actions/get_org_json.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_org_json.ashx*/);
                        mini.get("treeMain").collapseLevel(2);

                        Index.showLeftPanel();

                        var grid = mini.get("targetgrid");
                        grid.loading("加载数据中......");
                        $.ajax({
                            url: 'actions/get_target_json.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_target_json.ashx*/,
                            type: 'post',
                            error: function () {
                                baiduMap.removeOverlay(overlay);

                            },
                            success: function (data) {

                                window.targetList = eval("(" + data + ")");
                                mini.get("targetgrid").unmask();
                                var len = window.targetList.length;
                                for (var i = 0; i < len; i++) {
                                    window.targetList[i].check = false;
                                }
                            }
                        });

                    }
                },
                error: function () {
                    alert("划拨失败");
                }
            });
        }
    },
    //导出车辆详细信息
    onTreeMenu_exportvehicledetails: function (num) {
        if (num == 0) {
            if (window.Index.traceTargetNumber == "") {
                mini.alert("请单击选择左侧列表中的目标！");
                return;
            }
            num = window.Index.traceTargetNumber;
        };
        $.ajax({
            url: 'actions/get_exportvehicledetails.ashx?txtvessel=' + num,
            type: 'post',
            success: function (data) {
                var downUrl = "actions/exportCarTrack.ashx?fullFileName=" + data;
                window.location = downUrl;
            }
        });
    },
    //导出车辆轨迹信息
    onTreeMenu_exportvehicletrackdetails: function (num) {
        if (num == 0) {
            if (window.Index.traceTargetNumber == "") {
                mini.alert("请单击选择左侧列表中的目标！");
                return;
            }
            num = window.Index.traceTargetNumber;
        };
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

        mini.get('txtvessel').setValue(num);
        mini.get('txtstarttime').setValue(oldyear + '-' + oldmonth + '-' + oldday + ' ' + hour + ':' + minute + ':' + second);
        mini.get('txtendtime').setValue(year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second);
        mini.get('trackbikedatagrid').setData(null);
        var trackbackD = mini.get("trackbackDiv");
        trackbackD.show('center', 'middle');
    },
    trackbikeShow: function () {

        var /*num = $('#spanNum').html()*/num = mini.get('txtvessel').getValue(),
	        trace_starttime = mini.get('txtstarttime').getFormValue(),
	        trace_endtime = mini.get('txtendtime').getFormValue();

        if (trace_starttime.length == 0 || trace_endtime.length == 0) {
            mini.alert('请设置开始、结束时间!');
            return;
        }
        $.ajax({
            url: 'actions/show_trace.ashx?num=' + num + "&trace_starttime=" + trace_starttime + "&trace_endtime=" + trace_endtime + "&mapSource=baidu",
            type: 'post',
            error: function () { },
            success: function (data) {
                if (data.lendth <= 0) {
                    mini.alert('wujieguo ');
                    return;
                }
                if (data == '空' || data == null || data == '[]') {
                    mini.alert("您好,查询轨迹结果为空,请调整时间后查询");
                    return;
                }
                if (data == '异常') {
                    mini.alert("您好,查询轨迹出现异常,请联系相关人员");
                    return;
                }
                if (data == '时间超出所配置的时间间隔') {
                    mini.alert("您好,你查询的轨迹时间间隔超出所配置的时间间隔(默认30天),请修改后查询");
                    return;
                }
                var arr = null;
                try { eval("arr=" + data) } catch (e) { }
                window.addresstemp = arr;
                mini.get('trackbikedatagrid').setData(arr);
            }
        });
    },
    exportCarTrackback: function () {
        var /*num = $('#spanNum').html()*/num = mini.get('txtvessel').getValue(),
	        trace_starttime = mini.get('txtstarttime').getFormValue(),
	        trace_endtime = mini.get('txtendtime').getFormValue();

        var temp = window.addresstemp;
        var address = "";
        var count = 1;
        for (var i = 0; i < temp.length; i++) {
            $.ajax({
                url: 'http://api.map.baidu.com/geocoder/v2/?ak=928e0a4bccfc2ed444984db3851e29ab&callback=renderReverse&location=' + temp[i].y + ',' + temp[i].x + '&output=json&pois=1',
                type: 'post',
                dataType: 'JSONP',
                error: function () { },
                success: function (data) {
                    address = data.result.formatted_address + ",";


                    if (count == temp.length) {
                        $.ajax({
                            url: 'actions/get_exporttrackback.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_exporttrackback.ashx*/,
                            type: 'post',
                            async: false,
                            data: { address: address, txtvessel: num, txtstarttime: trace_starttime, txtendtime: trace_endtime, num: count, count: temp.length },
                            success: function (data) {
                                var downUrl = "actions/exportCarTrack.ashx?fullFileName=" + data;
                                window.location = downUrl;
                            }
                        });
                    }
                    else {
                        $.ajax({
                            url: 'actions/get_exporttrackback.ashx'/*tpa=http://www.beidouguard.com:6010/js/actions/get_exporttrackback.ashx*/,
                            type: 'post',
                            async: false,
                            data: { address: address, count: temp.length, num: count }
                        });
                    }

                    count++;
                }
            });
        }

    },
    //历史报警查询
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
        dgHistoryAlarm.load({ 'mapSource': 'baidu', 'num': num, 'historicAlarm_starttime': historicAlarm_starttime, 'historicAlarm_endtime': historicAlarm_endtime, 'targetAlarmType': targetAlarmType });
    },
    //历史状态查询
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
    //查询电动车
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
            url: 'actions/get_trace.ashx?num=' + num + "&trace_starttime=" + trace_starttime + "&trace_endtime=" + trace_endtime + "&mapSource=baidu",
            type: 'post',
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
                Index.pts = [];
                for (var i = 0, l = arr.length; i < l; i += 3) {
                    var pt = new BMap.Point(arr[i], arr[i + 1]);
                    pt._time = arr[i + 2];
                    Index.pts.push(pt);
                }

                if (Index.traceMgr) {//上次的traceMgr
                    baiduMap.removeOverlay(Index.traceMgr._overlay);
                    baiduMap.removeOverlay(Index.traceMgr._marker);
                    Index.traceMgr = null;
                }
                var result = '查询成功,有' + Index.pts.length + '条记录';
                mini.get('QueryResult').setValue(result);
            }
        });
    },
    //查询电动车
    bikeQuery2: function (trace_starttime, trace_endtime) {
        mini.get('TrackBegin').disable();
        mini.get('TrackRun').disable();
        mini.get('TrackShow').disable();

        //alert(trace_starttime + ',' + trace_endtime);
        var num = mini.get('alltargetlist').getValue();

        $.ajax({
            url: 'actions/get_trace.ashx?num=' + num + "&trace_starttime=" + trace_starttime + "&trace_endtime=" + trace_endtime + "&mapSource=baidu",
            type: 'post',
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
                Index.pts = [];
                for (var i = 0, l = arr.length; i < l; i += 3) {
                    var pt = new BMap.Point(arr[i], arr[i + 1]);
                    pt._time = arr[i + 2];
                    Index.pts.push(pt);
                }

                if (Index.traceMgr) {//上次的traceMgr
                    baiduMap.removeOverlay(Index.traceMgr._overlay);
                    baiduMap.removeOverlay(Index.traceMgr._marker);
                    Index.traceMgr = null;
                }
                var result = '查询成功,有' + Index.pts.length + '条记录';
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
        if (Index.traceMgr) {
            var spd1 = mini.get('velocityList').getValue();
            var pauseTime1 = Index.getTrackPauseTime(spd1);
            var landMkrs = [];
            for (var i = 0, l = Index.pts.length; i < l; i++) {
                var ldMkr = Index.pts[i];
                ldMkr.html = '坐标: ' + ldMkr.lng + ', ' + ldMkr.lat + '<br/>时间: ' + ldMkr._time;
                ldMkr.pauseTime = pauseTime1;
                landMkrs.push(ldMkr);
            }
            Index.traceMgr._opts.landmarkPois = landMkrs;
            Index.traceMgr._opts.speed = spd1;
            Index.traceMgr.start();
        }
    },
    timeChange: function () {
        mini.get("TrackQuery").enable();
        mini.get('TraceToday').enable();
        mini.get('TraceYestoday').enable();
        mini.get('TrackBegin').disable();
        mini.get('TrackRun').disable();
        mini.get('TrackShow').enable();
        $('#TrackBegin').html("回放");
        mini.get("winTrace").setShowCloseButton(true);
        mini.get('QueryResult').setValue("");
        if (Index.tracePly) {
            baiduMap.removeOverlay(Index.tracePly);
            baiduMap.removeOverlay(Index.tracePly.getPath());
        }
        Index.pts = [];
        if (Index.traceMgr) {//上次的traceMgr
            baiduMap.removeOverlay(Index.traceMgr._overlay);
            baiduMap.removeOverlay(Index.traceMgr._marker);
            Index.traceMgr.stop();
            Index.traceMgr = null;
        }
    },
    //运行小车动画
    bikeRun: function () {
        mini.get('TrackShow').disable();
        mini.get("TrackQuery").disable();
        mini.get("TraceToday").disable();
        mini.get("TraceYestoday").disable();

        var a = $('#TrackBegin').html();
        if (a.toString().indexOf("回放") > -1) {
            mini.get('QueryResult').setValue("");
            mini.get("winTrace").setShowCloseButton(false);
            $('#TrackBegin').html("结束");
            if (Index.traceMgr) {//上次的traceMgr
                baiduMap.removeOverlay(Index.traceMgr._overlay);
                baiduMap.removeOverlay(Index.traceMgr._marker);
                Index.traceMgr = null;
            }
            var num = mini.get('alltargetlist').getValue(),
                    tip = num + '',
                    pauseTime = 1,
                	spd = mini.get('velocityList').getValue();
            pauseTime = Index.getTrackPauseTime(spd);
            var landMkrs = [];
            for (var i = 0, l = Index.pts.length; i < l; i++) {
                var ldMkr = Index.pts[i];
                ldMkr.html = '坐标: ' + ldMkr.lng + ', ' + ldMkr.lat + '<br/>时间: ' + ldMkr._time;
                ldMkr.pauseTime = pauseTime;
                landMkrs.push(ldMkr);
            }

            Index.traceMgr = new BMapLib.LuShu(baiduMap, Index.pts, {
                defaultContent: tip,
                landmarkPois: landMkrs,
                speed: spd
            });
            baiduMap.removeOverlay(Index.tracePly);
            Index.tracePly = new BMap.Polyline(Index.pts, {
                strokeColor: '#FF0000'
            });
            baiduMap.addOverlay(Index.tracePly);
            baiduMap.setViewport(Index.tracePly.getPath());

            var spd = mini.get('velocityList').getValue();
            Index.traceMgr._opts.speed = spd;
            Index.traceMgr.start();
        }
        else {
            mini.get("TrackQuery").enable();
            mini.get('TraceToday').enable();
            mini.get('TraceYestoday').enable();

            mini.get('TrackBegin').disable();
            mini.get('TrackRun').disable();
            mini.get('TrackShow').enable();
            $('#TrackBegin').html("回放");
            $('#TrackRun').html("暂停");
            mini.get("winTrace").setShowCloseButton(true);
            baiduMap.removeOverlay(Index.tracePly);
            baiduMap.removeOverlay(Index.tracePly.getPath());
            Index.pts = [];
            baiduMap.removeOverlay(Index.traceMgr._overlay);
            baiduMap.removeOverlay(Index.traceMgr._marker);
            Index.traceMgr.stop();
            Index.traceMgr = null;
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
    //停止小车动画
    bikePause: function () {
        var a = $('#TrackRun').html();
        if (a.toString().indexOf("暂停") > -1) {
            $('#TrackRun').html("继续");
            if (Index.traceMgr) {
                Index.traceMgr.pause();
            }
        }
        else {
            $('#TrackRun').html("暂停");
            if (Index.traceMgr) {
                //var spd1 = mini.get('velocityList').getValue();
                // var pauseTime1 = Index.getTrackPauseTime(spd1);
                //var landMkrs = [];
                //for (var i = 0, l = Index.pts.length; i < l; i++) {
                //     var ldMkr = Index.pts[i];
                //     ldMkr.html = '坐标: ' + ldMkr.lng + ', ' + ldMkr.lat + '<br/>时间: ' + ldMkr._time;
                //     ldMkr.pauseTime = pauseTime1;
                //     landMkrs.push(ldMkr);
                //}
                //Index.traceMgr._opts.landmarkPois = landMkrs;
                //Index.traceMgr._opts.speed = spd1;
                Index.traceMgr.start();
            }
        }
    },
    //显示小车运行轨迹数据
    bikeShow: function () {
        mini.get('TrackQuery').disable();
        mini.get("TraceToday").disable();
        mini.get("TraceYestoday").disable();

        var /*num = $('#spanNum').html()*/num = mini.get('alltargetlist').getValue(),
	        trace_starttime = mini.get('trace_starttime').getFormValue(),
	        trace_endtime = mini.get('trace_endtime').getFormValue();

        if (trace_starttime.length == 0 || trace_endtime.length == 0) {
            mini.alert('请设置开始、结束时间!');
            return;
        }
        $.ajax({
            url: 'actions/show_trace.ashx?num=' + num + "&trace_starttime=" + trace_starttime + "&trace_endtime=" + trace_endtime + "&mapSource=baidu",
            type: 'post',
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
                    mini.alert("您好,你查询的轨迹时间间隔超出所配置的时间间隔(默认30天),请修改后查询");
                    return;
                }

                var tabMain = mini.get('tabMain'),
						tabTrackInfo = tabMain.getTab('tabTrackInfo');
                tabTrackInfo.visible = true;
                tabMain.updateTab(tabTrackInfo);
                tabMain.activeTab(tabTrackInfo);
                mini.get('layoutMain').expandRegion('south');
                mini.get('TrackQuery').enable();
                mini.get('TraceToday').enable();
                mini.get('TraceYestoday').enable();
                var arr = null;
                try { eval("arr=" + data) } catch (e) { }
                mini.get('dgTrackInfo').setData(arr);
            }
        });

    },
    //TTargetConfigSetting单参数设置，hillpig
    onSoleParameterSettingOk: function () {
        var input = mini.get("spsValue");
        mini.get('spsValue').on("validation", curIndex.Allvalidation);
        input.validate();
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
            type: 'post',
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
    //TTargetConfigSetting批量参数设置，hillpig  
    onBatchParameterSettingOk: function () {
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
            type: 'post',
            error: function () { },
            success: function (data) {
                mini.get("winBatchParameterSetting").hide();
                mini.alert('发送成功!');
            }
        });
    },
    onBatchParameterSettingCancel: function () {
        mini.get("winBatchParameterSetting").hide();
    },
    //车辆显示设置
    //    ondisplaySettingOk: function () {
    //        var spsPara = mini.get('displaysettingtxt').getValue();
    //        var cookie_username = getCookie("name");
    //        var cookie_val = getCookie(cookie_username);
    //        var date = new Date();
    //        date.setTime(date.getTime() + Number(365) * 24 * 3600 * 1000);
    //        document.cookie = cookie_username + "=" + spsPara + "; path=/;expires = " + date.toGMTString();
    //    },
    //TTargetConfigSetting单参数设置，hillpig
    onUserParameterSettingOk: function () {
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
            type: 'post',
            error: function () { },
            success: function (data) {
                mini.alert('设置成功,刷新界面或者下次登录显示效果!');
            }
        });
    },
    onSendMsgOk: function () {
        sendType = mini.get("sendTypeList").getValue();
        msg = mini.get("txtMsgContent").getValue();
        msg = $.trim(msg);
        if (msg == '') {
            mini.alert('信息内容不能为空！');
            return;
        }

        $.ajax({
            url: 'actions/send_user_message.ashx?msg=' + encodeURI(msg) + '&type=' + sendType,
            type: 'post',
            error: function () { },
            success: function (data) {
                if (data == 'success') {
                    mini.get("winSendMsg").hide();
                    mini.alert('发送成功！');
                }
                else
                    mini.alert(data);
            }
        });
    },
    onSendMsgCancel: function () {
        mini.get("winSendMsg").hide();
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
            type: 'post',
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
    //发送消息处理函数
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
	                + "&AlarmType=" + AlarmType
                    + "&mapSource=baidu";
        $.ajax({
            url: 'actions/send_message.ashx?' + param,
            type: 'post',
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
    //查询结果处理函数
    onTargetSearchOk: function () {
        mini.get('targetSearchDel').show();
        var num = $('#spanMessageNum').html();
        //keyOfTargetSearch = mini.get('targetSearch').getValue();
        var carType = mini.get('comboxSearch').getValue();
        //var param = "key=" + keyOfTargetSearch;
        var node = mini.get("treeMain").getSelectedNode();
        var id = "";
        if (typeof (node) != "undefined") {
            id = node.id || "";
        }

        var grid = mini.get("targetgrid");
        var tabName = mini.get("searchTabs").getActiveTab().name;
        var node = null;
        var nodeName = "";
        if (tabName == "org") {
            node = mini.get("treeMain").getSelectedNode();
        } else {
            node = mini.get("treeProvince").getSelectedNode();
            if (typeof (node) != "undefined") {
                nodeName = node.name
            }
        }

        window.selectedData = this.getSelectedData(id, tabName, nodeName);
        // 监听分页前事件，阻止后自行设置当前数据和分页信息
        grid.on("beforeload", function (e) {
            e.cancel = true;

            var pageIndex = e.data.pageIndex, pageSize = e.data.pageSize;
            me.fillData(pageIndex, pageSize, window.selectedData, grid);
        });
        //初次加载
        this.fillData(0, grid.getPageSize(), window.selectedData, grid);
        //grid.reload();
        // mini.get('treeMain').setUrl('actions/get_json_tree.ashx?' + param);

        //        $.ajax({
        //            url: 'actions/search_target_by_key.ashx?' + param,
        //            type: 'post',
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

        //$('#treeMain').setUrl('actions/get_json_tree.ashx?' + param);
    },
    //取消叉号后恢复树状结构
    onTargetSearchDel: function () {
        mini.get('targetSearchDel').hide();
        var num = $('#spanMessageNum').html();
        mini.get('targetSearch').setValue("");
        mini.get('comboxSearch').setValue("");
        var keyOfTargetSearch = mini.get('targetSearch').getValue();
        var param = "key=" + keyOfTargetSearch;
        // mini.get('treeMain').setUrl('actions/get_json_tree.ashx?' + param);
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
    //验证低电压阈值的范围
    onddyfzvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^(3[0-5]{1}[0-9]{5})$");
            if (re.test(e.value) == false) {
                e.errorText = "低电压阈值的范围为3000000-3500000";
                e.isValid = false;
            }
        }
    },
    //验证正常电压阈值的范围
    onzcdyfzvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^(40{1}[0-9]{5}|4100000)$");
            if (re.test(e.value) == false) {
                e.errorText = "正常电压阈值的范为4000000-4100000";
                e.isValid = false;
            }
        }
    },
    //验证震动报警阈值的范围
    onzdbjfzvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^([1-9]|[1-5]{1}[0-9]{1}|60)$");
            if (re.test(e.value) == false) {
                e.errorText = "震动报警阈值的范围为0-60";
                e.isValid = false;
            }
        }
    },
    //验证震动等级报警阈值的范围
    onzddjfzvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("(^[0-9])$");
            if (re.test(e.value) == false) {
                e.errorText = "震动等级报警阈值的范围为0-9";
                e.isValid = false;
            }
        }
    },
    //验证位移阈值的范围
    onwybjfzvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^(1[0-9]{2}|200|[1-9][0-9])$");
            if (re.test(e.value) == false) {
                e.errorText = "单位米,位移阈值的范围为10-200";
                e.isValid = false;
            }
        }
    },
    //验证电话号码
    onaddcarvalidation: function (e) {
        if (e.isValid) {
            var str = e.value;
            if (str.indexOf(",") < 0) {
                e.errorText = "位置,电话号码(如1,18910406091);位置范围为 0到9";
                e.isValid = false;
            }
            var re = new RegExp("^([0-9]),1[3|4|5|8]{1}[0-9]{9}$");
            if (re.test(e.value) == false) {
                e.errorText = "位置,电话号码(如1,18910406091);位置范围为 0到9";
                e.isValid = false;
            }
        }
    },
    //验证位置范围
    ondelcarvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^([1-9])$");
            if (re.test(e.value) == false) {
                e.errorText = "位置(如1);位置范围为1到9";
                e.isValid = false;
            }
        }
    },
    //验证ip
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
    //验证sms
    onsmsvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^[0-1]{1}$");
            if (re.test(e.value) == false) {
                e.errorText = "允许,输入1,禁止,输入0;为了保证您车辆的安全,请设置为1";
                e.isValid = false;
            }
        }
    },
    //验证终端断开电源到休眠的时间间隔中上报位置的间隔和次数
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
    //验证此配置表示终端在电源开启的时候上报位置的间隔和次数
    onauploadvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^[0-9A-F]{4}$");
            if (re.test(e.value) == false) {
                e.errorText = "此配置表示终端在电源开启的时候上报位置的间隔和次数;十六进制（间隔）XXXX,默认间隔为32秒";
                e.isValid = false;
            }
        }
    },
    //验证电动车行驶速度大于设置阈值
    onoverspeedvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^[0-9A-F]{2}$");
            if (re.test(e.value) == false) {
                e.errorText = "车辆行驶速度大于设置阈值,终端发生报警,此参数为十六进制,单位节,默认输入值为10,十进制:16节,即:29km/h";
                e.isValid = false;
            }
        }
    },
    //验证电动车关闭电源后进入休眠状态的时间间隔
    ongorestvalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^[0-9A-F]{4}$");
            if (re.test(e.value) == false) {
                e.errorText = "车辆关闭电源后进入休眠状态的时间间隔,此参数为十六进制,单位秒,默认输入值为0100,即256秒";
                e.isValid = false;
            }
        }
    },
    //验证经度判断
    onlongitudevalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^((([1-9]|[1-9][0-9]|1[0-7][0-9])[0-9]{6})|180000000)$");
            if (re.test(e.value) == false) {
                e.errorText = "经度判断,区域经度阈值为1000000-180000000";
                e.isValid = false;
            }
        }
    },
    //验证纬度判断
    onlatitudevalidation: function (e) {
        if (e.isValid) {
            var re = new RegExp("^((([1-9]|[1-8][0-9])[0-9]{6})|90000000)$");
            if (re.test(e.value) == false) {
                e.errorText = "纬度判断,区域经度阈值为1000000-90000000";
                e.isValid = false;
            }
        }
    },
    //改变参数处理函数
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
                objSpsDesc.setValue("单位米,当终端在ACC关移动距离大于阈值时,终端发出报警,输入范围为10到200");
                break;
            case "增加车主":
                objSpsValue.setValue("1,18910406091");
                objSpsDesc.setValue("位置,电话号码(如1,18910406091);位置范围为 0到9,当相同位置有电话号码时,原电话号码被替代");
                break;
            case "删除车主":
                objSpsValue.setValue("1");
                objSpsDesc.setValue("位置(如1);位置范围为1到9");
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
    //执行各种验证
    Allvalidation: function (e) {
        var spsParaValue = mini.get('spsPara').getValue();
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
                        e.errorText = "单位米,位移阈值的范围为10-200";
                        e.isValid = false;
                    }
                }
                break;
            case "增加车主":
                if (e.isValid) {
                    var str = e.value;
                    if (str.indexOf(",") < 0) {
                        e.errorText = "位置,电话号码(如1,18910406091);位置范围为 0到9";
                        e.isValid = false;
                    }
                    var re = new RegExp("^([0-9]),1[3|4|5|8]{1}[0-9]{9}$");
                    if (re.test(e.value) == false) {
                        e.errorText = "位置,电话号码(如1,18910406091);位置范围为 0到9";
                        e.isValid = false;
                    }
                }
                break;
            case "删除车主":
                if (e.isValid) {
                    var re = new RegExp("^([1-9])$");
                    if (re.test(e.value) == false) {
                        e.errorText = "位置(如1);位置范围为1到9";
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
    //改变用户参数处理函数
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
    //用户配置验证函数
    UserConfigvalidation: function (e) {
        var spsParaValue = mini.get('userConfig').getValue();
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
    ,
    distance: function () {
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

        mini.get('txtdistancestarttime').setValue(oldyear + '-' + oldmonth + '-' + oldday);
        mini.get('txtdistanceendtime').setValue(year + '-' + month + '-' + day);
        mini.get('Div5').setData(null);
        var trackbackD = mini.get("divdistance");
        trackbackD.show('center', 'middle');
    },
    distancebutton: function () {

        var /*num = $('#spanNum').html()*/num = mini.get('combo1').getValue(),
	        trace_starttime = mini.get('txtdistancestarttime').getFormValue(),
	        trace_endtime = mini.get('txtdistanceendtime').getFormValue();

        if (trace_starttime.length == 0 || trace_endtime.length == 0) {
            mini.alert('请设置开始、结束时间!');
            return;
        }
        $.ajax({
            url: 'actions/show_target_track.ashx?num=' + num + "&trace_starttime=" + trace_starttime + "&trace_endtime=" + trace_endtime + "&mapSource=baidu",
            type: 'post',
            error: function () { },
            success: function (data) {
                if (data == '空' || data == null || data == '' || data.lendth <= 0) {
                    mini.alert("此终端没有位置点，不进行里程计算");
                    return;
                }
                var sb = "[";
                var strRet = "";
                var arr = [];
                arr = data.split(',');
                var arrdistinct = [];
                var count = 0;
                for (var i = 0; i < arr.length / 3; i++) {
                    if (i > 0 && arr[3 * i + 2] != arr[3 * (i - 1) + 2]) {
                        if (i == arr.length / 3 - 1) {
                            sb += "{'time':'" + trace_starttime + "至" + trace_endtime + "','targetnumber':'" + arr[3 * i + 2] + "','distance':'" + 0 + "'},";
                        }
                        if (count == 0) {
                            sb += "{'time':'" + trace_starttime + "至" + trace_endtime + "','targetnumber':'" + arr[3 * (i - 1) + 2] + "','distance':'" + 0 + "'},";
                        }
                        else {
                            var distance = BMapLib.GeoUtils.getPolylineDistance(arrdistinct);
                            if (distance != 0) {
                                distance = distance / 1000;
                                distance = distance.toString().split('.')[0] + '.' + distance.toString().split('.')[1].substring(0, 3);
                            }
                            sb += "{'time':'" + trace_starttime + "至" + trace_endtime + "','targetnumber':'" + arr[3 * (i - 1) + 2] + "','distance':'" + distance + "'},";
                        }
                        arrdistinct = [];
                        count = -1;
                    }
                    else {
                        if (i == arr.length / 3 - 1) {
                            if (count == 0) {
                                sb += "{'time':'" + trace_starttime + "至" + trace_endtime + "','targetnumber':'" + arr[3 * i + 2] + "','distance':'" + 0 + "'},";
                            }
                            else {
                                var distance = BMapLib.GeoUtils.getPolylineDistance(arrdistinct);
                                if (distance != 0) {
                                    distance = distance / 1000;
                                    distance = distance.toString().split('.')[0] + '.' + distance.toString().split('.')[1].substring(0, 3);
                                }
                                sb += "{'time':'" + trace_starttime + "至" + trace_endtime + "','targetnumber':'" + arr[3 * i + 2] + "','distance':'" + distance + "'},";
                            }
                            arrdistinct = [];
                            count = -1;
                        }
                        else {
                            arrdistinct[count] = new BMap.Point(arr[3 * i], arr[3 * i + 1]);
                        }
                    }
                    count++;
                }

                strRet = sb.substring(0, sb.length - 1);
                strRet += "]";
                alert(strRet);
                var arr = null;
                try { eval("arr=" + strRet) } catch (e) { }
                mini.get('Div5').setData(arr);

            }
        });
    },
    area: function () {
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

        mini.get('Text2').setValue(oldyear + '-' + oldmonth + '-' + oldday);
        mini.get('Text3').setValue(year + '-' + month + '-' + day);
        mini.get('Div6').setData(null);
        var trackbackD = mini.get("div4");
        trackbackD.show('center', 'middle');
    },
    areabutton: function () {

        var /*num = $('#spanNum').html()*/num = mini.get('Text1').getValue(),
	        trace_starttime = mini.get('Text2').getFormValue(),
	        trace_endtime = mini.get('Text3').getFormValue();

        if (trace_starttime.length == 0 || trace_endtime.length == 0) {
            mini.alert('请设置开始、结束时间!');
            return;
        }




        $.ajax({
            url: 'actions/area_position.ashx?num=' + num + "&trace_starttime=" + trace_starttime + "&trace_endtime=" + trace_endtime + "&mapSource=baidu",
            type: 'post',
            error: function () { },
            success: function (data) {
                if (data == '空' || data == null || data == '' || data.lendth <= 0) {
                    mini.alert("此终端没有位置点，不进行面积测量");
                    return;
                }
                var sb = "[";
                var strRet = "";
                var arr = [];
                arr = data.split(',');
                var arrdistinct = [];
                var count = 0;
                var distance = 0;
                var arrareapoint = [];
                var array = new Array();


                for (var i = 0; i < arr.length / 5; i++) {
                    if (i > 0 && arr[5 * i + 2] != arr[5 * (i - 1) + 2]) {
                        if (i == arr.length / 5 - 1 || i == arr.length / 5 - 2) {
                            if (trace_starttime == trace_endtime) {
                                sb += "{'time':'" + trace_starttime + "','targetnumber':'" + arr[5 * i + 2] + "','area':'" + 0 + "'},";
                            }
                            else {
                                sb += "{'time':'" + trace_starttime + "至" + trace_endtime + "','targetnumber':'" + arr[5 * i + 2] + "','area':'" + 0 + "'},";
                            }
                        }
                        else {
                            if (count == 1 || count == 0) {
                                if (trace_starttime == trace_endtime) {
                                    sb += "{'time':'" + trace_starttime + "','targetnumber':'" + arr[5 * (i - 1) + 2] + "','area':'" + 0 + "'},";
                                }
                                else {
                                    sb += "{'time':'" + trace_starttime + "至" + trace_endtime + "','targetnumber':'" + arr[5 * (i - 1) + 2] + "','area':'" + 0 + "'},";
                                }
                            }
                        }
                        arrdistinct = [];
                        count = -1;
                        distance = 0;
                        arrareapoint = [];
                    }
                    else {
                        if (i == arr.length / 5 - 1) {
                            if (i == 0) {
                                if (trace_starttime == trace_endtime) {
                                    sb += "{'time':'" + trace_starttime + "','targetnumber':'" + arr[5 * i + 2] + "','area':'" + 0 + "'},";
                                }
                                else {
                                    sb += "{'time':'" + trace_starttime + "至" + trace_endtime + "','targetnumber':'" + arr[5 * i + 2] + "','area':'" + 0 + "'},";
                                }
                            }
                            //                            if (count == 1 || count == 0) {
                            //                                sb += "{'time':'" + trace_starttime + "至" + trace_endtime + "','targetnumber':'" + arr[5 * i + 2] + "','area':'" + 0 + "'},";
                            //                            }
                            arrdistinct = [];
                            count = -1;
                            distance = 0;
                        }
                        else {
                            if (count == 0) {
                                arrdistinct[count] = new BMap.Point(arr[5 * i], arr[5 * i + 1]);
                            }
                            else {
                                if ((arr[5 * i + 3] - arr[5 * (i - 1) + 3]) < 300 && arr[5 * i + 4] >= 0 && arr[5 * i + 4] < 800) {
                                    arrdistinct[count] = new BMap.Point(arr[5 * i], arr[5 * i + 1]);
                                }
                                else {
                                    if (arrdistinct.length > 31) {
                                        for (var t = 0; t < arrdistinct.length; t++) {
                                            if (t == 0) {
                                                arrareapoint[t] = arrdistinct[t];
                                                arrareapoint[t + 1] = arrdistinct[t];
                                                arrareapoint[t + 2] = arrdistinct[t];
                                                arrareapoint[t + 3] = arrdistinct[t];
                                            }
                                            else {
                                                if (arrdistinct[t].lat > arrareapoint[0].lat) {
                                                    arrareapoint[0] = arrdistinct[t];
                                                }
                                                if (arrdistinct[t].lng > arrareapoint[1].lng) {
                                                    arrareapoint[1] = arrdistinct[t];
                                                }
                                                if (arrdistinct[t].lat < arrareapoint[2].lat) {
                                                    arrareapoint[2] = arrdistinct[t];
                                                }
                                                if (arrdistinct[t].lng < arrareapoint[3].lng) {
                                                    arrareapoint[3] = arrdistinct[t];
                                                }
                                            }
                                        }
                                        var nCount = arrareapoint.length;
                                        for (var k = 0; k < arrdistinct.length; k++) {
                                            var x = arrdistinct[k].lng;
                                            var y = arrdistinct[k].lat;

                                            var r;
                                            var j = nCount - 1;
                                            var oddNodes = false;

                                            for (r = 0; r < nCount; r++) {
                                                if ((arrareapoint[r].lat <= y && arrareapoint[j].lat >= y || arrareapoint[j].lat <= y && arrareapoint[r].lat >= y) && (arrareapoint[r].lng <= x || arrareapoint[j].lng <= x)) {
                                                    oddNodes ^= (arrareapoint[r].lng + (y - arrareapoint[r].lat) / (arrareapoint[j].lat - arrareapoint[r].lat) * (arrareapoint[j].lng - arrareapoint[r].lng) < x);
                                                }
                                                j = r;
                                            }

                                            if (oddNodes == false) {
                                                for (var q = 0; q < arrareapoint.length; q++) {
                                                    if (q == arrareapoint.length - 1) {
                                                        if (arrdistinct[k].lat < arrareapoint[0].lat && arrdistinct[k].lat > arrareapoint[q].lat && arrdistinct[k].lng > arrareapoint[q].lng && arrdistinct[k].lng < arrareapoint[0].lng) {
                                                            arrareapoint[arrareapoint.length] = arrdistinct[k];
                                                        }
                                                    }
                                                    else {
                                                        if (arrdistinct[k].lat < arrareapoint[q].lat && arrdistinct[k].lat > arrareapoint[q + 1].lat && arrdistinct[k].lng > arrareapoint[q].lng && arrdistinct[k].lng < arrareapoint[q + 1].lng || arrdistinct[k].lat < arrareapoint[q].lat && arrdistinct[k].lat > arrareapoint[q + 1].lat && arrdistinct[k].lng > arrareapoint[q + 1].lng && arrdistinct[k].lng < arrareapoint[q].lng || arrdistinct[k].lat < arrareapoint[q + 1].lat && arrdistinct[k].lat > arrareapoint[q].lat && arrdistinct[k].lng > arrareapoint[q + 1].lng && arrdistinct[k].lng < arrareapoint[q].lng || arrdistinct[k].lat < arrareapoint[q + 1].lat && arrdistinct[k].lat > arrareapoint[q].lat && arrdistinct[k].lng > arrareapoint[q].lng && arrdistinct[k].lng < arrareapoint[q + 1].lng) {
                                                            for (var w = arrareapoint.length; w > q + 1; w--) {
                                                                arrareapoint[w] = arrareapoint[w - 1];
                                                            }
                                                            arrareapoint[q + 1] = arrdistinct[k];
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        for (var u = 0; u < arrareapoint.length; u++) {
                                            for (var o = u + 1; o < arrareapoint.length; o++) {
                                                if (arrareapoint[u] == arrareapoint[o]) {
                                                    arrareapoint.remove(arrareapoint[o]);
                                                }
                                            }
                                        }
                                        distance = BMapLib.GeoUtils.getPolygonArea(arrareapoint);

                                        distance = distance.toString().split('.')[0] + '.' + distance.toString().split('.')[1].substring(0, 3);
                                        if (array[arr[5 * (i - 1) + 2]] != null && array[arr[5 * (i - 1) + 2]] != 0 && array[arr[5 * (i - 1) + 2]] != "") {
                                            array[arr[5 * (i - 1) + 2]] = Number(array[arr[5 * (i - 1) + 2]]) + Number(distance);
                                        }
                                        else {
                                            array[arr[5 * (i - 1) + 2]] = Number(distance);
                                        }
                                    }
                                    arrdistinct = [];
                                    count = -1;
                                    distance = 0;
                                }
                            }
                        }
                    }
                    count++;
                }
                for (var item in array) {
                    if (!isNaN(Number(item))) {
                        sb += "{'time':'" + trace_starttime + "至" + trace_endtime + "','targetnumber':'" + item + "','area':'" + array[item] * 0.0015 + "'},";
                    }
                }
                strRet = sb.substring(0, sb.length - 1);
                strRet += "]";
                var arr = null;
                try { eval("arr=" + strRet) } catch (e) { }
                mini.get('Div6').setData(arr);
            }
        });
    }
}
// 昨天最早时间
function getYestodayEarly() {
    var date = new Date();
    date.setDate(date.getDate() - 1);

    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " 00:00:00";
    return currentdate;
}
// 昨天最晚时间
function getYestodayLatest() {
    var date = new Date();
    date.setDate(date.getDate() - 1);

    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " 23:59:59";
    return currentdate;
}
// 今天最早时间
function getNowEarly() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " 00:00:00";
    return currentdate;
}
// 当前时间
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}