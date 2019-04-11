<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function () {

var _PID = "${menuId}";

// page info
var _P = {
    id: _PID,
    items: null
};


// Model -> company info
Ext.define("lbs_company_info_model", {
    extend: "Ext.data.Model",
    fields: [
        {name:"org_id", type:"string"},
        {name:"org_name", type:"string"},
        {name:"corp_account", type:"string"},
        {name:"manager_id", type:"string"},
        {name:"manager_name", type:"string"},
        {name:"manager_mobile", type:"string"},
        {name:"terminal_num", type:"string"}
    ]
});

// store
var _store = Ext.create("Ext.data.Store", {
    model: lbs_company_info_model,
    pageSize: lbs.C.PAGE_SIZE,
    proxy: {
        type: "ajax",
        url: "org/query_company_list.shtm",
        actionMethods: {
         	read: "POST"
        },
        reader: {
         	root: "companys",
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

var _deleteCompany = function(orgId, managerId) {
    Ext.Ajax.request({
        url: "org/delete_company.shtm",
        params: {
           orgId: orgId,
           uid: managerId
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
                    var orgId = rec.get("org_id");
                    var uid = rec.get("manager_id");
                    lbs.U.createTopTab({
                        id: _PID + "_edit",
                        name: ["编辑企业组织（", orgId, "）"].join(""),
                        url: "org/open_company.shtm?m=modify",
                        qparams: {
                            orgId: orgId,
                            uid: uid,
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
                    Ext.MessageBox.confirm("操作提示（高危）", "您确定要删除该企业组织吗？", function(btnId) {
                        if (btnId == "yes") {
                            _deleteCompany(rec.get("org_id"), rec.get("manager_id"));
                        }
                    });
                }
            }]
         },
         { text: "编号",  dataIndex: "org_id", width: 100},
         { text: "企业组织名称",  dataIndex: "org_name", width: 200},
         /* { text: "企业组织ID（外）",  dataIndex: "corp_account", width: 140}, */
         { text: "管理员ID",  dataIndex: "manager_id", width: 140},
         { text: "管理员姓名",  dataIndex: "manager_name" , width: 160},
         { text: "管理员电话",  dataIndex: "manager_mobile", width: 160},
         { text: "终端数",  dataIndex: "terminal_num", width: 160}
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
            text: "新增企业组织",
            listeners: {
                click: function() {
                    lbs.U.createTopTab({
                        id: _PID + "_add",
                        name: "新增企业组织",
                        url: "org/open_company.shtm?m=add",
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