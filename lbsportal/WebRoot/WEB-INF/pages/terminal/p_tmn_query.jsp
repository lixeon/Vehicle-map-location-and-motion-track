<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function () {

//var _PID = "${menuId}";
var _PID = "tbp_M0101";
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
                fieldLabel: "手机号码",
                name: "userMobile"
            }, {
                fieldLabel: "用户姓名",
                name: "owner"
            }]
        }, {
            layout: "hbox",
            items: [{
                id: _PID + "terminalSerialNo",
                fieldLabel: "终端编号",
                name: "terminalSerialNo"
            }, {
                fieldLabel: "终端SIM卡号",
                name: "terminalOpNo"
            }/*, {
                fieldLabel: "IMEI",
                name: "terminalIMEI"
            }*/]
        }, {
            layout: "hbox",
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
	        }/*, {
	            fieldLabel: "昵称",
                name: "terminalName"
	        }*/]
        }/*, {
        	html: "<div style='color:#ed9200; padding:5px 10px'>请至少输入一个查询条件。</div>"
        }*/]
    }],
    buttons: [{
        text: "查询",
        handler: function() {
            _store.loadRecords([]);
            _store.loadPage(1);
        }
    }, {
        text: "清空条件",
        handler: function() {
            _condForm.getForm().reset();
        }
    }, {
        text: "添加终端",
        handler: function() {
            lbs.U.createTopTab({
                id: _PID + "_add",
                name: "添加终端",
                icon: "images/icons/add_tmn.png",
                url: "terminal/open.shtm?m=add",
                qparams: {
                    menuId: _PID + "_add"
                }
            });
        }
    }]
});
    

// Model -> terminal info
Ext.define("lbs_terminal_info_ListReuslt", {
    extend: "Ext.data.Model",
    fields: [
        {name:"terminal_serial_no", type:"string"},
        {name:"terminal_name", type:"string"},
        {name:"terminal_imei", type:"string"},
        {name:"owner", type:"string"},
        {name:"user_mobile", type:"string"},
        {name:"terminal_op_no", type:"string"},
        {name:"city_disp_nm", type:"string"},
        {name:"status_disp_nm", type:"string"},
        {name:"service_end_date", type:"string"},
        {name:"reg_date", type:"string"},
        {name:"org_name", type:"string"}
    ]
});

// store
var _store = Ext.create("Ext.data.Store", {
    model: lbs_terminal_info_ListReuslt,
    pageSize: lbs.C.PAGE_SIZE,
    proxy: {
        type: "ajax",
        url: "terminal/query.shtm",
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
 	
var _deleteTerminal = function(terminalSerialNo) {
    Ext.Ajax.request({
        url: "terminal/delete.shtm",
        params: {
           terminalSerialNo: terminalSerialNo
        },
        success: function(response) {
            var json = Ext.JSON.decode(response.responseText);
            if (json.success == "true") {
 	           	Ext.MessageBox.show({
                    title: lbs.G.MsgTitle,
                    msg: "删除成功！",
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO,
                    fn: function() {
         	            // reload data.
         	           _store.reload();
                    }
				});
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
            width: 100,
            align: "center",
            items: [{
             	icon: "images/icons/edit.png",
                tooltip: "编辑",
                iconCls: "grid-tools-icon",
                handler: function(grid, rowIndex, colIndex) {
                    var rec = grid.getStore().getAt(rowIndex);
                    var tmnId = rec.get("terminal_serial_no");
                    lbs.U.createTopTab({
                        id: _PID + "_edit",
                        icon: "images/icons/edit.png",
                        name: ["编辑终端（", tmnId, "）"].join(""),
                        url: "terminal/open.shtm?m=modify",
                        qparams: {
                            terminalSerialNo: tmnId,
                            menuId: _PID + "_edit"
                        }
                    });
                }
            }, {
                icon: "images/icons/delete.gif",
                tooltip: "删除",
                iconCls: "grid-tools-icon",
                handler: function(grid, rowIndex, colIndex) {
                    var rec = grid.getStore().getAt(rowIndex);
                    Ext.MessageBox.confirm("操作提示（高危）", "您确定要删除该终端吗？", function(btnId) {
                        if (btnId == "yes") {
                            _deleteTerminal(rec.get("terminal_serial_no"));
                        }
                    });
                }
            }]
         },
         { text: "终端编号",  dataIndex: "terminal_serial_no" ,width: 120},
         { text: "昵称",  dataIndex: "terminal_name" ,width: 120},
         { text: "SIM卡号码", dataIndex: "terminal_op_no", width: 120},
         { text: "IMEI", dataIndex: "terminal_imei", width: 140},
         { text: "用户姓名",  dataIndex: "owner" ,width: 80},
         { text: "手机号码",  dataIndex: "user_mobile" ,width: 120},
         { text: "城市", dataIndex: "city_disp_nm"},
         { text: "服务开通状态", dataIndex: "status_disp_nm"},
         { text: "企业组织", dataIndex: "org_name", width: 140},
         { text: "服务截止日", dataIndex: "service_end_date"},
         { text: "注册时间", dataIndex: "reg_date", width: 140}
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

// load first page
window.setTimeout(function() {
    _store.loadPage(1);
}, 200);

})();
</script>