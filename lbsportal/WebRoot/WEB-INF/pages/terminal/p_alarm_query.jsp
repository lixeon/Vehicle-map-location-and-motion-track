<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<style>
.mycell-impt {
    font-weight: bold;
    color: red;
}

</style>
<script type="text/javascript" >
(function () {

var _PID = "${menuId}";
var _LW = 120;
var _FW = 280;

// page info
var _P = {
    id: _PID,
    focusId: _PID + "userMobile",
    items: null
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
    items: [{
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
                fieldLabel: "告警类型",
                name: "alarmType",
                editable: false,
				value : "",
				displayField: "name",
			    valueField: "value",
			    store: Ext.create("Ext.data.Store", {
			        fields: ["value", "name"],
			        data: [
					    {"value": "", "name": "--全部--"},
						{"value": "ALARM_SHOCK", "name": "异常震动"},
					    {"value": "ALARM_OOB", "name": "越界"},
					    {"value": "ALARM_LOWBTT", "name": "低电量"},
					    {"value": "ALARM_MEOFF", "name": "主电源断开"},
					    {"value": "ALARM_LOGINF", "name": "登录失败"}
			        ]
			    })
            }/*, {
                fieldLabel: "昵称",
                name: "terminalName"
            }*/]
        }, {
            layout: "hbox",
            items: [{
			    xtype: "datefield",
                id: _PID + "alarmTimeSt",
				fieldLabel : "告警时间",
                name : "alarmTimeSt",
                format: "Y-m-d",
                width: _FW - 40,
                editable: true
            }, {
                xtype: "timefield",
                name : "st",
                format: "H:i",
                width: _FW - _LW - 80,
                value: "00:00",
                minValue: "00:00",
                maxValue: "23:59",
                margin: "0 1",
                increment: 5,
                editable: true
            }, {
               xtype: "label",
               margin: "5, 15",
               text: "~"
            }, {
                xtype: "datefield",
                id: _PID + "alarmTimeEt",
                width: _FW - _LW - 40,
                name : "alarmTimeEt",
                format: "Y-m-d",
                editable: true,
                maxValue: new Date()
            }, {
                xtype: "timefield",
                name : "et",
                format: "H:i",
                width: _FW - _LW - 80,
                value: "23:59",
                minValue: "00:00",
                maxValue: "23:59",
                margin: "0 1",
                increment: 5,
                editable: true
            }]
        }, {
            layout: "hbox",
            hidden: (Ext.util.Cookies.get("org") == lbs.C.TOP_GROUP) ? false : true,
            items: [{
    	        id: _PID + "orgName",
	            fieldLabel : "企业组织",
	            readOnly: true,
	            width: _FW * 2 - _LW + 28,
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
            } else {
                lbs.U.showError("查询条件格式不正确！");
            }
        }
    }, {
        text: "清空条件",
        handler: function() {
            _condForm.getForm().reset();
        }
    }]
});
    

// Model -> alarm info
Ext.define("lbs_alarm_info_ListReuslt", {
    extend: "Ext.data.Model",
    fields: [
        {name:"id", type: "string"},
        {name:"terminal_serial_no", type:"string"},
        {name:"terminal_name", type:"string"},
        {name:"alarm_type", type:"string"},
        {name:"loc_status", type:"string"},
        {name:"content", type:"string"},
        {name:"lng", type:"string"},
        {name:"lat", type:"string"},
        {name:"speed", type:"string"},
        {name:"tmn_status", type:"string"},
        {name:"sms_flag", type:"string"},
        {name:"notify_status", type:"string"},
        {name:"owner", type:"string"},
        {name:"terminal_owner", type:"string"},
        {name:"alarm_time", type:"string"},
        {name:"create_time", type:"string"}
    ]
});


// store
var _store = Ext.create("Ext.data.Store", {
    model: lbs_alarm_info_ListReuslt,
    pageSize: lbs.C.PAGE_SIZE,
    proxy: {
        type: "ajax",
        url: "alarm/query.shtm",
        actionMethods: {
         	read: "POST"
        },
        reader: {
         	root: "alarmList",
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
 	

// update notify status
var _updateNotifyStatus = function(id, newStatus) {
    Ext.Ajax.request({
        url: "alarm/update_notify_status.shtm",
        params: {
           id: id,
           newStatus: newStatus
        },
        success: function(response) {
            // ignore
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
    	width: 40,
    	renderer: function(value, cellmeta, record, rowIndex, columnIndex, store) {
         	return (_store.currentPage - 1) * store.pageSize + rowIndex + 1;
        }
	},
        {
			text: "操作",
     		menuDisabled: true,
            sortable: false,
            xtype: "actioncolumn",
            width: 60,
            align: "center",
            items: [{
             	icon: "images/icons/location.png",
                tooltip: "显示位置",
                handler: function(grid, rowIndex, colIndex) {
                    var rec = grid.getStore().getAt(rowIndex);
                    var alarmType = rec.get("alarm_type");
                    if (alarmType == "ALARM_LOWBTT" || alarmType == "ALARM_LOGINF") {
                        lbs.U.showInfo("无定位信息！");
                        return;
                    }

                    var url = lbs.G.basePath + "mbd.jsp?p=" + rec.get("lng") + "," + rec.get("lat") + "&dt=" + new Date();
                    window.open(url, '', 'height=520,width=520,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
                }
            }]
         },
         { text: "终端编号",  dataIndex: "terminal_serial_no" ,width: 100},
         { text: "昵称",  dataIndex: "terminal_name", width: 100},
         { text: "车主姓名", dataIndex: "owner", width: 80},
         { text: "车主号码", dataIndex: "terminal_owner", width: 100},
         { text: "告警类型", dataIndex: "alarm_type", width: 100,
             renderer: function(value, metaData, record) {
                 if (value == "ALARM_MEOFF" && record.get("notify_status") != lbs.C.NOTIFY_STATUS.MANUAL) {
                     metaData.tdCls = "mycell-impt";
                 }
                 return lbs.C.ALARM_TYPE[value];
             }
         },
         { text: "定位结果",  dataIndex: "loc_status", width: 80,
             renderer: function(value) {
                 if (lbs.U.isEmpty(value)) {
                     return "无";
                 } else {
                     return lbs.C.LOC_RESULT[value];
                 }
             }
         },
         { text: "速度(km/h)", dataIndex: "speed", width: 80,
             renderer: function(value) {
                 return lbs.U.isEmpty(value) ? "无" : value;
             }
         },
         { text: "终端状态", dataIndex: "tmn_status", width: 80,
             renderer: function(value) {
                 if (lbs.U.isEmpty(value)) {
                     return "无";
                 } else {
                     return lbs.C.TMN_STATUS[value];
                 }
             }
         },
         { text: "通知状态",  dataIndex: "notify_status", width: 80,
             renderer: function(value, metaData, record) {
                 if (record.get("sms_flag") == "0") {
                     return lbs.C.NOTIFY_STATUS[lbs.C.NOTIFY_STATUS.UNNOTIFY];
                 } else {
                     return lbs.C.NOTIFY_STATUS[value];
                 }
             }
         },
         { text: "短信内容",  dataIndex: "content", width: 400,
			renderer: function(value, metaData) {
			    metaData.tdAttr = 'data-qtip="' + value + '"';
			    return value;
			}
         },
         { text: "告警时间", dataIndex: "alarm_time", width: 140},
         { text: "短信发送时间", dataIndex: "create_time", width: 140}
    ],
    listeners: {
        itemclick: function(grid, record, item, index) {
            if (record.get("alarm_type") == "ALARM_MEOFF"
                    && record.get("notify_status") != lbs.C.NOTIFY_STATUS.MANUAL) {
                // Update status as 'Manual Operation'
                _updateNotifyStatus(record.get("id"), lbs.C.NOTIFY_STATUS.MANUAL);
                // update style.
                var cell6 = item.cells[6];
                cell6.className = cell6.className.replace(/mycell-impt/g, "");
            }
        }
    },
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