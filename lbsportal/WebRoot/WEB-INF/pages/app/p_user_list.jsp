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
                fieldLabel: "姓名",
                name: "name"
            }, {
                xtype: "combobox",
                fieldLabel: "性别",
                editable: false,
                name: "gender",
				value : "",
				displayField: "name",
			    valueField: "value",
			    store: Ext.create("Ext.data.Store", {
			        fields: ["value", "name"],
			        data: [
					    {"value": "", "name": "--"},
						{"value": "1", "name": "男"},
					    {"value": "0", "name": "女"}
			        ]
			    })
            }]
        }, {
            layout: "hbox",
            items: [{
				xtype : "uXcombo",
			    fieldLabel : "省份",
				name : "provinceId",
				comboOpts : {
					comboId : lbs.G.CDK.CDPROVINCE,
					autoLoad: false
				},
				labelWidth : _LW,
				width : _FW,
				listeners : {
				    change : function(el, newValue, oldValue, opts) {
				        var cityStore = Ext.getCmp(_PID + "cityId").getStore();
				        cityStore.proxy.extraParams = {
				            comboId : newValue + "." + lbs.G.CDK.CDCITY
				        };
				        cityStore.on("load", function() {
				            Ext.getCmp(_PID + "cityId").select(cityStore.getAt(0));
				    	});
				        cityStore.load();
				    }
				}
			}, {
				xtype : "uXcombo",
				id : _PID + "cityId",
			    fieldLabel : "城市",
				name : "cityId",
				comboOpts : {
					comboId : ""
				},
				labelWidth : _LW,
				width : _FW
			}]
        }, {
            layout: "hbox",
            items: [{
			    xtype: "datefield",
                id: _PID + "createDateSt",
				fieldLabel : "注册时间",
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
        text: "清空条件",
        handler: function() {
            _condForm.getForm().reset();
        }
    }]
});


// Model -> app user info
Ext.define("lbs_Appuser_info_ListReuslt", {
    extend: "Ext.data.Model",
    fields: [
        {name:"appuid", type:"string"},
        {name:"mobile", type:"string"},
        {name:"name", type:"string"},
        {name:"gender", type:"string"},
        {name:"province_id", type:"string"},
        {name:"province_name", type:"string"},
        {name:"city_id", type:"string"},
        {name:"city_name", type:"string"},
        {name:"login_date", type:"string"},
        {name:"create_date", type:"string"},
        {name:"update_date", type:"string"},
        {name:"remark", type:"string"}
    ]
});


// store
var _store = Ext.create("Ext.data.Store", {
    model: lbs_Appuser_info_ListReuslt,
    pageSize: lbs.C.PAGE_SIZE,
    proxy: {
        type: "ajax",
        url: "app/user_query.shtm",
        actionMethods: {
            read: "POST"
        },
        reader: {
            root: "appUserList",
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
         { text: "姓名",  dataIndex: "name", width: 120},
         { text: "性别", dataIndex: "gender", width: 80,
             renderer: function(value) {
                 return lbs.U.isEmpty(value) ? "" : (value == "0" ? "女" : "男");
             }
         },
         { text: "省份", dataIndex: "province_name", width: 120},
         { text: "城市", dataIndex: "city_name", width: 100},
         { text: "最近登录时间", dataIndex: "login_date", width: 140},
         { text: "最近更新时间", dataIndex: "update_date", width: 140},
         { text: "注册时间", dataIndex: "create_date", width: 140},
         { text: "备注", dataIndex: "remark", width: 100}
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