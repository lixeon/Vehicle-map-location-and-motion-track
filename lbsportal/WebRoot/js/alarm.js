/**
 * 初始化弹出窗口数据
 */
function initAlarmList() {
    mini.parse();
    var gridNum = mini.get("gridNum");
    gridNum.setUrl("actions/get_alarm_list.ashx"/*tpa=http://www.beidouguard.com:6010/js/actions/get_alarm_list.ashx*/);
    gridNum.load();
}

/**
 * 获取用户设置的参数
 */
function getData(){
    var data = {}, 
        alarmName = mini.get("alarmName").getValue(),
        alarmType = mini.get("radAlarmType").getValue(),
        selectedRows = mini.get('gridNum').getSelecteds(),
        leastCount = mini.get("leastCount").getValue(),
        isUsed = mini.get("ckIsUsed").getChecked(); 
    
    var arr = [];
    for (var i = 0, l = selectedRows.length; i < l; i++) {
        arr.push(selectedRows[i].id);
    }    
    data.alarmName = alarmName;
    data.alarmType = alarmType;
    data.ids = arr.join(',');
    data.leastCount = leastCount;
    if (isUsed == true) {
        data.isUsed = 1; //启用
    } else {
        data.isUsed = 2;//不启用
    }
   
    return data;
}

/*
 * 显示用户上次设置的参数，编辑使用
 */
function setData(objData) {    
    var ids = objData.ids,
        gridNum = mini.get('gridNum');
    for (var i = 0, l = ids.length; i < l; i++) {
        var row = ids[i];
        gridNum.addRow(row);

        if (row.isselected == 1) {
            gridNum.select(row);
        }
    }
    mini.get("alarmName").setValue(objData.rangename);
    mini.get("ckIsUsed").setValue(objData.isused);
    mini.get("radAlarmType").setValue(objData.alarmtype);
    mini.get("leastCount").setValue(objData.leastcount); 
}

/**
 * 关闭窗口
 */        
function closeWindow(action) {
    if (window.CloseOwnerWindow){
        return window.CloseOwnerWindow(action);
    } else {
        window.close();
    }
}

/**
 * 确定处理函数
 */
function onOk() {
    var alarmName = mini.get("alarmName").getValue();
    var re = new RegExp("^([0-9]|[a-z]|[A-Z])+$");
    if (re.test(alarmName) == false) {
        mini.alert("名称请输入数字或者字符");
        return; 
    }

    var leastCount = mini.get("leastCount").getValue();
    var re = new RegExp("^([0-9])+$");
    if (mini.get("radAlarmType").getValue() == 3) {
        if (re.test(leastCount) == false) {
            mini.alert("最小数量请输入数字");
            return;
        }
    }

    var  selectedRows = mini.get('gridNum').getSelecteds();
       
    if(alarmName.length == 0){
        mini.alert('报警名称不能为空', '提示');
        return;
    }
    
    if(alarmName.indexOf(';') > -1){
        mini.alert('报警名称不能含有分号', '提示');
        return;
    }
      // 允许用户不选择车辆，表示对此用户监控的所有的车辆有效。  
    if(selectedRows.length <= 0){
        if (confirm("您没有进行终端关联,则此报警区域对您所监控的所有终端有效,是否继续")) {

        }
        else {
            return;
         }
    }
    

        
    closeWindow("ok");
}

/**
 * 取消
 */
function onCancel() {
    closeWindow("cancel");
}