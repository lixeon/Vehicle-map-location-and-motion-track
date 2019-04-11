<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function () {

var _PID = "${menuId}";

// page info
var _P = {
    id: _PID,
    items: null
};


// Model -> App Client Info
Ext.define("lbs_AppClient_info_model", {
    extend: "Ext.data.Model",
    fields: [
        {name:"client_type", type:"string"},
        {name:"client_type_name", type:"string"},
        {name:"ver", type:"string"},
        {name:"download_url", type:"string"},
        {name:"update_date", type:"string"}
    ]
});

// store
var _store = Ext.create("Ext.data.Store", {
    model: lbs_AppClient_info_model,
    pageSize: lbs.C.PAGE_SIZE,
    proxy: {
        type: "ajax",
        url: "app/client_list.shtm",
        actionMethods: {
         	read: "POST"
        },
        reader: {
         	root: "clientList",
         	totalProperty: "totalCount"
        },
        simpleSortMode: true,
        listeners: {
            exception: function(proxy, resp, op, opts) {
             	var obj = Ext.JSON.decode(resp.responseText);
             	lbs.U.showError(obj.errorMsgs);
            }
        }
    }
});

var _deleteClient = function(clientType) {
    Ext.Ajax.request({
        url: "app/client_delete.shtm",
        params: {
			clientType: clientType
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
                    var clientType = rec.get("client_type");
                    var clientTypeName = rec.get("client_type_name");
                    lbs.U.createTopTab({
                        id: _PID + "_edit",
                        name: ["修改客户端（", clientTypeName, "）"].join(""),
                        url: "app/client.shtm?m=modify",
                        qparams: {
                            clientType: clientType,
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
                    Ext.MessageBox.confirm("操作提示（高危）", "您确定要删除该APP客户端配置吗？", function(btnId) {
                        if (btnId == "yes") {
                            _deleteClient(rec.get("client_type"));
                        }
                    });
                }
            }]
         },
         { text: "客户端ID",  dataIndex: "client_type", width: 120},
         { text: "客户端名称",  dataIndex: "client_type_name", width: 140},
         { text: "软件版本",  dataIndex: "ver", width: 120},
         { text: "更新时间",  dataIndex: "update_date" , width: 160},
         { text: "下载地址",  dataIndex: "download_url", width: 300}
    ],
    dockedItems: [{
        xtype: "toolbar",
        dock: "top",
        ui: "header",
        items: [{
            text: "刷新列表",
            listeners: {
                click: function() {
                    _store.reload();
                }
            }
        }, {
            text: "添加APP客户端",
            listeners: {
                click: function() {
                    lbs.U.createTopTab({
                        id: _PID + "_add",
                        name: "添加APP客户端",
                        url: "app/client.shtm?m=add",
                        qparams: {
                            menuId: _PID + "_add"
                        }
                    });
                }
            }
        }]
    }],
    autoScroll: true,
    bbar: Ext.create("Ext.PagingToolbar", {
		store: _store,
        displayInfo: true,
        displayMsg: "显示数据 {0} - {1} / {2}",
        emptyMsg: "无记录"
    })
});


//show tab content
_P.items = [ _gridPanel ];
lbs.U.showTabContent(_P);

// load first page.
window.setTimeout(function() {
    _store.loadPage(1);
}, 200);
    
})();
</script>