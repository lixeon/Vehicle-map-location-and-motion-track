//右下角弹出提示信息
var count = 0;
var TipInfomation = {
    popWin: function () {

        var bodyWidth = $(document.body).width(),
            bodyHeight = $(document.body).height(),
            winUserRegionAlarmWidth = $('#winUserRegionAlarm').width(),
            winUserRegionAlarmHeight = $('#winUserRegionAlarm').height(),
            bufferX = bufferInterval = 5,
            bufferY = 20,
            winUserRegionAlarmLeft = bodyWidth - winUserRegionAlarmWidth - bufferX,
            winUserRegionAlarmTop = bodyHeight - winUserRegionAlarmHeight - bufferY;

        //mini.alert(serverInfo.configParam.isFirst);
        if (serverInfo.configParam.isFirst == 0) {

            var winInsureAlarmWidth = $('#winInsureAlarm').width(),
            winInsureAlarmHeight = $('#winInsureAlarm').height(),
            winInsureAlarmLeft = bodyWidth - winInsureAlarmWidth - bufferX,
            winInsureAlarmTop = bodyHeight - winInsureAlarmHeight - winUserRegionAlarmHeight - bufferY - bufferInterval;

            var winInsureAlarm = mini.get('winInsureAlarm'),
            dgInsureAlarm = mini.get('dgInsureAlarm');
            dgInsureAlarm.load({}, function (e) {
                if (e.data.length > 0) {
                    winInsureAlarm.show(winInsureAlarmLeft, winInsureAlarmTop);
                }
            });
            serverInfo.configParam.isFirst = serverInfo.configParam.isFirst + 1;
        }
        //mini.alert(serverInfo.configParam.isFirst);

        //用户自定义区域报警提醒
        var winUserRegionAlarm = mini.get('winUserRegionAlarm'),
            dgUserRegionAlarm = mini.get('dgUserRegionAlarm');
        dgUserRegionAlarm.load({}, function (e) {
            if (e.data.length > 0) {
                winUserRegionAlarm.show(winUserRegionAlarmLeft, winUserRegionAlarmTop);
            }
        });
    }
};

//自执行
(function () {    
/*
    TipInfomation.popWin();

    //间隔半小时更新次
    window.setInterval(function (e) {
        TipInfomation.popWin();
    }, serverInfo.configParam.popupInterval);
    */
})();