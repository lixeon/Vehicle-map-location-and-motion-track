<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<style>
.t-c-all {
    font-size: 16px;
    font-weight: bold;
    padding-right: 20px;
}

.t-c-on {
    font-size: 16px;
    font-weight: bold;
    color: green;
    padding-right: 20px;
}

.t-c-off {
    font-size: 16px;
    font-weight: bold;
    color: red;
    padding-right: 20px;
}

</style>
<script type="text/javascript" >
(function () {

var _PID = "${menuId}";
var _LW = 120;
var _FW = 280;

var _CNT_HINT_TPL = "终端数：<span class='t-c-all' >#ALL</span>在线：<span class='t-c-on'>#ON</span>离线：<span class='t-c-off'>#OFF</span>";

// page info
var _P = {
    id: _PID,
    focusId: _PID + "userMobile",
    items: null
};

// auto refresher
var _auto_refresher = null;
// refreher gap
var _REFRESH_INTERVAL = 1000 * 15;


// refresh terminal count
var _refrenTerminalCnt = function() {
    Ext.Ajax.request({
        url: "terminal/refresh_terminal_count.shtm",
        params: {},
        success: function(response) {
            var json = Ext.JSON.decode(response.responseText);
            if (json.success == "true") {
 	           	var tpl = _CNT_HINT_TPL.replace(/#ALL/g, json.terminal_all_cnt)
 	           		.replace(/#ON/g, json.terminal_on_cnt)
 	           		.replace(/#OFF/g, json.terminal_off_cnt);
 	           
 	           	var countHint = Ext.getCmp(_PID + "count_hint");
				countHint.update(tpl);
            } else {
               lbs.U.showError(json.errorMsgs);
            }
        }
    });
};


// switch refresher
var _switchRefresher = function(checked) {
    if (checked) {
        _auto_refresher = window.setInterval(function() {
            _store.reload();
            _refrenTerminalCnt();
        }, _REFRESH_INTERVAL);
    } else {
        if (_auto_refresher != null) {
            window.clearInterval(_auto_refresher);
            _auto_refresher = null;
        }
    }
};

//// show mass terminal distribution.
var _showTerminalDistribution = function() {
    lbs.U.createTopTabInIframe({
    	id: _PID + "_map_mass_distribution",
        icon: "images/icons/location.png",
        name: "查看终端分布",
        url: "terminal_distb/open.shtm",
        qparams: {
            m: "distribution"
        }
    });
};


var _condForm = Ext.create("Ext.form.Panel", {
    region: "north",
    buttonAlign: "left",
    frame: true,
    bodyPadding: 5,
    fieldDefaults: {
        labelAlign: "right",
        labelWidth: _LW,
        width: _FW,
        labelStyle : "padding-right: 10px"
    },
    items: [
    	{
        xtype: "fieldset",
        title: "检索条件",
        layout: "anchor",
        defaults: {
            xtype: "container",
            anchor: "100%",
            defaultType: "textfield",
            padding: "2 0"
        },
        margin: "0",
        items: [{
            layout: "hbox",
            items: [{
                id: _PID + "userMobile",
                fieldLabel: "车主号码",
                name: "userMobile"
            }, {
                fieldLabel: "车主姓名",
                name: "owner"
            }]
        }, {
            layout: "hbox",
            items: [{
                id: _PID + "terminalSerialNo",
                fieldLabel: "终端编号",
                name: "terminalSerialNo"
            }, {
                xtype: "combobox",
                fieldLabel: "在线离线",
                editable: false,
                name: "onlineStatus",
				value : "1",
				displayField: "name",
			    valueField: "value",
			    store: Ext.create("Ext.data.Store", {
			        fields: ["value", "name"],
			        data: [
					    {"value": "", "name": "--全部--"},
						{"value": "1", "name": "在线"},
					    {"value": "0", "name": "离线"}
			        ]
			    })
            }]
        }, {
            layout: "hbox",
            hidden: (Ext.util.Cookies.get("org") == lbs.C.TOP_GROUP) ? false : true,
            items: [{
    	        id: _PID + "orgName",
	            fieldLabel : "企业组织",
	            readOnly: true,
	            width: _FW * 2 - _LW,
	            name: "orgName"
	        }, {
	           xtype: "container",
	           defaults: {
	               xtype: "button",
	               widht: 48,
	               style: {
	                   marginLeft: "2px"
	               }
	           },
	           items: [{
	               text: "选择",
	               listeners: {
       	               click: function() {
       	            		lbs.U.showOrgWin({
       	                        at: this,
       	                        selMode: "0",
       	                        callback: function(id, name) {
           	                        Ext.getCmp(_PID + "orgId").setValue(id);
           	                        Ext.getCmp(_PID + "orgName").setValue(name);
       	                        }
       	                    });
       	               }
       	           }
	           }, {
	               text: "清空",
	               listeners: {
       	                click: function() {
   	                        Ext.getCmp(_PID + "orgId").setValue("");
   	                        Ext.getCmp(_PID + "orgName").setValue("");
       	                }
       	           }
	           }]
	        }, {
	            id: _PID + "orgId",
	            xtype: "hidden",
	            name: "orgId",
	            width: 0
	        }]
        }]
    }],
    buttons: [{
        text: "查询",
        handler: function() {
            if (_condForm.getForm().isValid()) {
                _store.loadRecords([]);
                _store.loadPage(1);
                _refrenTerminalCnt();
            } else {
                lbs.U.showError("查询条件格式不正确！");
            }
        }
    }, {
        text: "清空条件",
        handler: function() {
            _condForm.getForm().reset();
        }
    }, {
    	text: "查看终端分布",
    	hidden: lbs.U.isAuthByRole(lbs.C.ROLE.CP),
    	handler: function() {
    		_showTerminalDistribution();
    	}
    }, {
        id: _PID + "autoRefresh",
        xtype: "checkbox",
        width: 120,
        margin: "0 10",
        boxLabel: "自动刷新",
        name: "autoRefresh",
        handler: function(el, value) {
            _switchRefresher(value);
        }
    }, {
        id: _PID + "count_hint",
        xtype: "label",
        width: 400,
        html: "终端数：<span class='t-c-all' >${terminal_all_cnt}</span>在线：<span class='t-c-on'>${terminal_on_cnt}</span>离线：<span class='t-c-off'>${terminal_off_cnt}</span>"
    }]
});


// Model -> terminal status info
Ext.define("lbs_TerminalStatus_info_ListReuslt", {
    extend: "Ext.data.Model",
    fields: [
        {name:"terminal_serial_no", type:"string"},
        {name:"terminal_name", type:"string"},
        {name:"owner", type:"string"},
        {name:"user_mobile", type:"string"},
        {name:"speed", type:"string"},
        {name:"on_off_status", type:"string"},
        {name:"tmn_status", type:"string"},
        {name:"gps", type:"string"},
        {name:"gsm", type:"string"},
        {name:"notify_status", type:"string"},
        {name:"city_id", type:"string"},
        {name:"org_name", type:"string"},
        {name:"last_update_time", type:"string"}
    ]
});


// store
var _store = Ext.create("Ext.data.Store", {
    model: lbs_TerminalStatus_info_ListReuslt,
    pageSize: lbs.C.PAGE_SIZE,
    proxy: {
        type: "ajax",
        url: "terminal/query_statuslist.shtm",
        actionMethods: {
            read: "POST"
        },
        reader: {
            root: "terminalInfos",
            totalProperty: "totalCount"
        },
        simpleSortMode: true,
        listeners: {
            exception: function(proxy, resp, op, opts) {
                var obj = Ext.JSON.decode(resp.responseText);
                lbs.U.showError(obj.errorMsgs);
            }
        }
    },
    listeners: {
        beforeload: function(me, operation, opts) {
            operation.params = _condForm.getValues();
        }
    }
});


var _getDisabledFlag = function(table, rowIdx) {
    var rec = table.getStore().getAt(rowIdx);
    var onOff = rec.get("on_off_status");
    //var tmnStatus = rec.get("tmn_status");
    return ((onOff == "0")) ? true : false;
};


// switch protected status
var _switchProtectedStatus = function(terminalId, newStatus) {
    Ext.Ajax.request({
        url: "portal/send_protected_cmd.shtm",
        params: {
        	terminalId: terminalId,
        	offon: newStatus
        },
        success: function(response) {
            var json = Ext.JSON.decode(response.responseText);
            if (json.success == "true") {
 	           lbs.U.showInfo("设置成功!");
            } else {
               lbs.U.showError(json.errorMsgs);
            }
        }
    });
};


// restart terminal
var _restartTerminal = function(terminalSerialNo) {
    Ext.Ajax.request({
        url: "portal/restart_terminal.shtm",
        params: {
            terminalSerialNo: terminalSerialNo
        },
        success: function(response) {
            var json = Ext.JSON.decode(response.responseText);
            if (json.success == "true") {
 	           lbs.U.showInfo("重启指令下发成功!");
            } else {
               lbs.U.showError(json.errorMsgs);
            }
        }
    });
};


// grid panel
var _gridPanel = Ext.create("Ext.grid.Panel", {
    region: "center",
    frame: true,
    margin:  0,
    autoScroll: true,
    store: _store,
    columns: [{
    	xtype: 'rownumberer',
    	header: "序号",
    	width: 50,
    	renderer: function(value, cellmeta, record, rowIndex, columnIndex, store) {
         	return (_store.currentPage - 1) * store.pageSize + rowIndex + 1;
        }
	},
        {
            text: "操作",
            menuDisabled: true,
            sortable: false,
            xtype: "actioncolumn",
            width: 160,
            align: "center",
            items: [{
                icon: "images/icons/location.png",
                iconCls: "grid-tools-icon",
                tooltip: "地图模式",
                handler: function(table, rowIdx) {
                    var rec = table.getStore().getAt(rowIdx);
                    var tmnId = rec.get("terminal_serial_no");
                    lbs.U.createTopTabInIframe({
                        id: _PID + "_map_" + tmnId,
                        icon: "images/icons/location.png",
                        name: "地图模式（"+ tmnId +"）",
                        url: "portal/terminal_portal.shtm",
                        qparams: {
                            terminalSerialNo: tmnId,
                            cityId: rec.get("city_id"),
                            mode: "manager"
                        }
                    });
                }
            }, {
                icon: "images/icons/reboot.png",
                tooltip: "重启终端",
                isDisabled: _getDisabledFlag,
                iconCls: "grid-tools-icon",
                handler: function(table, rowIdx) {
                    var rec = table.getStore().getAt(rowIdx);
                    _restartTerminal(rec.get("terminal_serial_no"));
                }
            }, {
                icon: "images/icons/unlock.png",
                iconCls: "grid-tools-icon",
                isDisabled: function(table, rowIdx) {
                    /*var disable = _getDisabledFlag(table, rowIdx);
                    if (disable) {
                        return disable;
                    }*/
                    var rec = table.getStore().getAt(rowIdx);
                    var tmnStatus = rec.get("tmn_status");
                    /*if (tmnStatus == lbs.C.TMN_STATUS.EMPTY) {
                        return true;
                    }*/
                    if (tmnStatus == lbs.C.TMN_STATUS.UNPT) {
                        return true;
                    }
                    
                    return false;
                },
                tooltip: "撤防",
                handler: function(table, rowIdx) {
                    var rec = table.getStore().getAt(rowIdx);
                    var terminalSerialNo = rec.get("terminal_serial_no");
                    _switchProtectedStatus(terminalSerialNo, lbs.C.TMN_STATUS.UNPT);
                }
            }, {
                icon: "images/icons/lock.png",
                iconCls: "grid-tools-icon",
                isDisabled: function(table, rowIdx) {
                    /*var disable = _getDisabledFlag(table, rowIdx);
                    if (disable) {
                        return disable;
                    }*/
                    var rec = table.getStore().getAt(rowIdx);
                    var tmnStatus = rec.get("tmn_status");
                    /*if (tmnStatus == lbs.C.TMN_STATUS.EMPTY) {
                        return true;
                    }*/
                    if (tmnStatus == lbs.C.TMN_STATUS.PT) {
                        return true;
                    }
                    
                    return false;
                },
                tooltip: "设防",
                handler: function(table, rowIdx) {
                    var rec = table.getStore().getAt(rowIdx);
                    var terminalSerialNo = rec.get("terminal_serial_no");
                    _switchProtectedStatus(terminalSerialNo, lbs.C.TMN_STATUS.PT);
                }
            }, {
                icon: "images/icons/settings2.png",
                iconCls: "grid-tools-icon",
                tooltip: "设置参数",
                handler: function(table, rowIdx) {
                    var rec = table.getStore().getAt(rowIdx);
                    var tmnId = rec.get("terminal_serial_no");
                    lbs.U.createTopTab({
                        id: _PID + "_setting",
                        icon: "images/icons/settings2.png",
                        name: "参数设置（"+ tmnId +"）",
                        url: "terminal/open.shtm?m=setting",
                        qparams: {
                            terminalSerialNo: tmnId
                        }
                    });
                }
            }, {
                icon: "images/icons/bell.png",
                iconCls: "grid-tools-icon",
                tooltip: "最近告警",
                handler: function(table, rowIdx) {
                    var rec = table.getStore().getAt(rowIdx);
                    var tmnId = rec.get("terminal_serial_no");
                    lbs.U.showAlarmList({
                        terminalSerialNo: tmnId
                    });
                }
            }]
         },
         { text: "在线离线",  dataIndex: "on_off_status", width: 100,
             renderer: function(value) {
                 if (lbs.U.isEmpty(value)) {
                     return "无";
                 } else {
                     return lbs.C.ONOFF_STATUS[value];
                 }
             }
         },
         { text: "终端编号",  dataIndex: "terminal_serial_no" ,width: 120},
         { text: "昵称",  dataIndex: "terminal_name", width: 120},
         { text: "车主姓名", dataIndex: "owner", width: 120},
         { text: "车主号码", dataIndex: "user_mobile", width: 120},
         { text: "企业组织", dataIndex: "org_name", width: 120},
         { text: "速度(km/h)", dataIndex: "speed", width: 100,
             renderer: function(value) {
                 return lbs.U.isEmpty(value) ? "无数据" : (Math.floor(value) == 0 ? "0.00" : value);
             }
         },
         { text: "终端状态", dataIndex: "tmn_status", width: 120,
             renderer: function(value) {
                 if (lbs.U.isEmpty(value)) {
                     return "无";
                 } else {
                     return lbs.C.TMN_STATUS[value];
                 }
             }
         },
         { text: "卫星数", dataIndex: "gps", width: 80},
         { text: "信号强度", dataIndex: "gsm", width: 80},
         { text: "最后一次数据时间", dataIndex: "last_update_time", width: 140}
    ],
    autoScroll: true,
    bbar: Ext.create("Ext.PagingToolbar", {
        store: _store,
        displayInfo: true,
        displayMsg: "显示数据 {0} - {1} / {2}",
        emptyMsg: "无记录"
    })
});


//show tab content
_P.items = [ _condForm, _gridPanel ];
lbs.U.showTabContent(_P);

//load first page
window.setTimeout(function() {
    _store.loadPage(1);
}, 200);


})();
</script>