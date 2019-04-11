<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function () {

var _PID = "${menuId}";
var _LW = 120;
var _FW = 280;

// page info
var _P = {
    id: _PID,
    focusId: _PID + "mobile",
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
                id: _PID + "mobile",
                fieldLabel: "手机号码",
                name: "mobile"
            }, {
            	xtype: "combobox",
                fieldLabel: "客户端类型",
                name: "clientType",
                editable: false,
				value : "",
				displayField: "name",
			    valueField: "value",
			    store: Ext.create("Ext.data.Store", {
			        fields: ["value", "name"],
			        data: [
					    {"value": "", "name": "--"},
						{"value": "0", "name": "Android"},
					    {"value": "1", "name": "IOS"}
			        ]
			    })
            }, {
                fieldLabel: "软件版本",
                name: "ver"
            }]
        }, {
            layout: "hbox",
            items: [{
			    xtype: "datefield",
                id: _PID + "createDateSt",
				fieldLabel : "反馈时间",
                name : "createDateSt",
                format: "Y-m-d",
                //width: _FW - 40,
                editable: true
            }, {
               xtype: "label",
               margin: "5, 15",
               text: "~"
            }, {
                xtype: "datefield",
                id: _PID + "createDateEt",
                width: _FW - _LW,
                name : "createDateEt",
                format: "Y-m-d",
                editable: true,
                maxValue: new Date()
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
        text: "导出到文件",
        handler: function() {
            _condForm.getForm().submit({
            	standardSubmit: true,
            	url : "app/feedback_export.shtm"/*,
            	success : function(form, resp) {
					// TODO
				},
				faliure : function(form, resp) {
					lbs.U.showError(resp.result.errorMsgs);
				}, 
				waitMsg: "正在导出..." */
            });
        }
    }, {
        text: "清空条件",
        handler: function() {
            _condForm.getForm().reset();
            Ext.getCmp(_P.focusId).focus();
        }
    }]
});


// Model -> Feedback
Ext.define("lbs_Feedback_info_ListReuslt", {
    extend: "Ext.data.Model",
    fields: [
        {name:"id", type:"number"},
        {name:"mobile", type:"string"},
        {name:"client_type", type:"string"},
        {name:"client_type_name", type:"string"},
        {name:"ver", type:"string"},
        {name:"content", type:"string"},
        {name:"create_date", type:"string"}
    ]
});


// store
var _store = Ext.create("Ext.data.Store", {
    model: lbs_Feedback_info_ListReuslt,
    pageSize: lbs.C.PAGE_SIZE,
    proxy: {
        type: "ajax",
        url: "app/feedback_list.shtm",
        actionMethods: {
            read: "POST"
        },
        reader: {
            root: "feedbackList",
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
    columns: [{
    	xtype: "rownumberer",
    	header: "序号",
    	width: 50,
    	renderer: function(value, cellmeta, record, rowIndex, columnIndex, store) {
         	return (_store.currentPage - 1) * store.pageSize + rowIndex + 1;
        }
	},
         { text: "手机号码",  dataIndex: "mobile" ,width: 120},
         { text: "客户端类型",  dataIndex: "client_type_name", width: 120},
         { text: "软件版本", dataIndex: "ver", width: 120},
         { text: "反馈时间", dataIndex: "create_date", width: 140},
         { text: "反馈内容", dataIndex: "content", width: 500}
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