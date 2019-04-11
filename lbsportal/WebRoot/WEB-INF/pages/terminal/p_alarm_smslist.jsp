<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function () {

var _PID = "${menuId}";
var _LW = 100;
var _FW = 240;

// page info
var _P = {
    id: _PID,
    focusId: _PID + "terminalSerialNo",
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
                id: _PID + "terminalSerialNo",
                fieldLabel: "终端编号",
                name: "terminalSerialNo"
            }, {
			    xtype: "datefield",
                id: _PID + "alarmTimeSt",
				fieldLabel : "发送时间",
                name : "alarmTimeSt",
                format: "Y-m-d",
                width: _FW - 30,
                editable: true
            }, {
                xtype: "timefield",
                name : "st",
                format: "H:i",
                width: _FW - _LW - 60,
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
                width: _FW - _LW - 30,
                name : "alarmTimeEt",
                format: "Y-m-d",
                editable: true,
                maxValue: new Date()
            }, {
                xtype: "timefield",
                name : "et",
                format: "H:i",
                width: _FW - _LW - 60,
                value: "23:59",
                minValue: "00:00",
                maxValue: "23:59",
                margin: "0 1",
                increment: 5,
                editable: true
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
    

// Model -> SMS info
Ext.define("lbs_SMS_info_ListReuslt", {
    extend: "Ext.data.Model",
    fields: [
        {name:"terminal_serial_no", type:"string"},
        {name:"terminal_name", type:"string"},
        {name:"alarm_type", type:"string"},
        {name:"loc_status", type:"string"},
        {name:"content", type:"string"},
        {name:"lng", type:"string"},
        {name:"lat", type:"string"},
        {name:"speed", type:"string"},
        {name:"tmn_status", type:"string"},
        {name:"notify_status", type:"string"},
        {name:"owner", type:"string"},
        {name:"alarm_time", type:"string"},
        {name:"create_time", type:"string"}
    ]
});


// store
var _store = Ext.create("Ext.data.Store", {
    model: lbs_SMS_info_ListReuslt,
    pageSize: lbs.C.PAGE_SIZE,
    proxy: {
        type: "ajax",
        url: "alarm/query_smslist.shtm",
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
 	
 	

// grid panel
var _gridPanel = Ext.create("Ext.grid.Panel", {
    region: "center",
    frame: true,
    margin:  0,
    autoScroll: true,
    store: _store,
    columns: [
		{
        	xtype: 'rownumberer',
        	header: "序号",
        	width: 50,
        	renderer: function(value, cellmeta, record, rowIndex, columnIndex, store) {
             	return (_store.currentPage - 1) * store.pageSize + rowIndex + 1;
            }
		},
         { text: "终端编号",  dataIndex: "terminal_serial_no" ,width: 80},
         { text: "昵称",  dataIndex: "terminal_name", width: 100},
         { text: "车主姓名",  dataIndex: "owner", width: 100},
         { text: "短信内容",  dataIndex: "content", width: 760 },
         { text: "短信状态",  dataIndex: "notify_status", width: 80,
             renderer: function(value) {
                 return lbs.C.NOTIFY_STATUS[value];
             }
         },
         { text: "发送时间", dataIndex: "create_time", width: 130}
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

})();
</script>