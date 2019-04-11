//切换百度地图和天地图的逻辑
var Switch = {
    objTemp: null,
    toggle: function (e) {
        var target = e.sender,
            txt = target.text,
            reverseMapType = ($('#satliteMap').text() == '普通地图' ? '卫星地图' : '普通地图');

        if (txt === '切换到天地图') {//tianditu Map
            target.setText('切换到百度地图');
            $('#divBaiduMap').css('z-index', '100');
            $('#divTiandiMap').css('z-index', '200');

            if (window.tiandiMapResReady) {
                //清除上一个自动刷新器
                if (window.curIndex.loadDataHandler) {
                    window.clearInterval(window.curIndex.loadDataHandler);
                    window.clearInterval(window.curIndex.loadDataHandlerLoadTree);                    
                }
                window.curIndex.delMaker();
                window.curIndex = T_Index;
                curIndex.init();
                curIndex.switchMapType(reverseMapType);
            } else {
                this.loadTiandiMapResource(function () {
                    window.tiandiMapResReady = true;

                    //清除上一个自动刷新器
                    if (window.curIndex.loadDataHandler) {
                        window.clearInterval(window.curIndex.loadDataHandler);
                        window.clearInterval(window.curIndex.loadDataHandlerLoadTree);
                    }
                    window.curIndex.delMaker();
                    window.curIndex = T_Index;
                    curIndex.init();
                    curIndex.switchMapType(reverseMapType);
                });
            }
        } else { //baidu Map
            target.setText('切换到天地图');
            $('#divBaiduMap').css('z-index', '200');
            $('#divTiandiMap').css('z-index', '100');

            //清除上一个自动刷新器
            if (window.curIndex.loadDataHandler) {
                window.clearInterval(window.curIndex.loadDataHandler);
                window.clearInterval(window.curIndex.loadDataHandlerLoadTree);
            }
            window.curIndex.delMaker();
            window.curIndex = Index;
            curIndex.init();
            curIndex.switchMapType(reverseMapType);
        }

        //重新加载历史报警数据，避免百度、天地图坐标不一致问题
        var tabMain = mini.get('tabMain'),
            tabHistoryAlarm = tabMain.getTab('tabHistoryAlarm');
        if (tabHistoryAlarm.visible == true) {
            curIndex.historicAlarmQuery();
        }
    },
    //异步加载天地图api
    loadTiandiMapResource: function (callback) {
        var me = this;
        me.loadJs('js/maps_tianditu.js'/*tpa=http://www.beidouguard.com:6010/js/js/maps_tianditu.js*/, function () {
            me.loadJs('js/t_index.js'/*tpa=http://www.beidouguard.com:6010/js/js/t_index.js*/, function () {
                me.loadJs('js/t_LuShu.js'/*tpa=http://www.beidouguard.com:6010/js/js/t_LuShu.js*/, function () {
                    callback();
                });
            });
        });
    },
    //动态加载js文件
    loadJs: function (url, callback) {
        var head = document.getElementsByTagName("head")[0];
        var script = document.createElement('script');
        script.onload = script.onreadystatechange = script.onerror = function () {
            if (script && script.readyState && /^(?!(?:loaded|complete)$)/.test(script.readyState)) return;
            script.onload = script.onreadystatechange = script.onerror = null;
            script.src = '';
            script.parentNode.removeChild(script);
            script = null;
            callback();
        }
        script.charset = "utf-8";
        script.src = url;
        try {
            head.appendChild(script);
        } catch (exp) { }
    }
};

//自执行，异步加载天地图的资源
(function () {
    window.tiandiMapResReady = false; //全局变量，天地图资源是否已经下载到浏览器端
    Switch.loadTiandiMapResource(function () {
        window.tiandiMapResReady = true;
    });
})();