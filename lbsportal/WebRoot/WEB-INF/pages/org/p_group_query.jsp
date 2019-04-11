<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function () {

var _PID = "${menuId}";
var _COMPANY_LIST = Ext.JSON.decode('${company_list}');

if (_COMPANY_LIST == null || _COMPANY_LIST.length == 0) {
    lbs.U.deleteTopTab(_PID);
    alert("请先添加企业组织！");
    return;
}

// page info
var _P = {
    id: _PID,
    items: null
};



//Model -> group info
Ext.define("lbs_group_info_model", {
	extend: "Ext.data.Model",
 	fields: [
		{name:"org_id", type:"string"},
     	{name:"org_name", type:"string"},
     	{name:"org_color", type:"string"},
     	{name:"manager_id", type:"string"},
     	{name:"manager_name", type:"string"},
     	{name:"manager_mobile", type:"string"}
 	]
});



//store
var _store = Ext.create("Ext.data.Store", {
	model: lbs_group_info_model,
	pageSize: lbs.C.PAGE_SIZE,
	proxy: {
     	type: "ajax",
     	url: "org/query_group_list.shtm",
     	actionMethods: {
      		read: "POST"
     	},
     	reader: {
      		root: "grouplist",
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
            operation.params = {
				companyId: Ext.getCmp(_PID + "company_id").getValue()
            };
        }
    }
});

var _reloadFirstPage = function() {
    _store.loadRecords([]);
    _store.loadPage(1);
};

var _deleteGroup = function(orgId, managerId) {
    Ext.Ajax.request({
    	url: "org/delete_group.shtm",
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

//grid panel
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
	}, {
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
                     name: ["编辑分组（", orgId, "）"].join(""),
                     url: "org/open_group.shtm?m=modify",
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
                 Ext.MessageBox.confirm("操作提示", "您确定要删除该分组吗？", function(btnId) {
                     if (btnId == "yes") {
                         _deleteGroup(rec.get("org_id"), rec.get("manager_id"));
                     }
                 });
             }
         }, {
             icon: "images/icons/group_add.png",
             tooltip: "关联终端",
             iconCls: "grid-tools-icon",
             handler: function(grid, rowIdx, colIdx) {
                 var rec = grid.getStore().getAt(rowIdx);
                 var orgId = rec.get("org_id");
                 lbs.U.createTopTab({
                     id: _PID + "_bindtmn",
                     name: ["关联终端（", rec.get("org_name"), "）"].join(""),
                     url: "org/open_group.shtm?m=bindtmn",
                     qparams: {
                         orgId: orgId,
                         parentOrgId: Ext.getCmp(_PID + "company_id").getValue(),
                         menuId: _PID + "_bindtmn"
                     }
                 });
             }
         }]
	},
	{ text: "编号",  dataIndex: "org_id", width: 120},
	{ text: "分组名称",  dataIndex: "org_name", width: 200},
	{ text: "标记颜色",  dataIndex: "org_color", width: 120,
		renderer: function(value) {
			if (lbs.U.isEmpty(value)) {
				return "";
			}
            var bgColor = value.split(",")[0];
            return "<span style='background-color:#bgColor'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>".replace(/#bgColor/g, "#" + bgColor);
        }
    },
	{ text: "管理员ID",  dataIndex: "manager_id", width: 140},
	{ text: "管理员姓名",  dataIndex: "manager_name" , width: 160},
	{ text: "管理员电话",  dataIndex: "manager_mobile", width: 160}
 	],
 	dockedItems: [{
    	xtype: "toolbar",
        dock: "top",
        ui: "header",
        items: [{
            xtype: "combobox",
            id: _PID + "company_id",
            fieldLabel : "企业组织",
            labelWidth : 60,
            name : "companyId",
			value : _COMPANY_LIST[0].org_id,
			editable: false,
		    valueField: "org_id",
			displayField: "org_name",
		    store: Ext.create("Ext.data.Store", {
		        fields: ["org_id", "org_name"],
		        data: _COMPANY_LIST
		    }),
		    listeners: {
		        change: function(el, newval, oldval, opts) {
		            _reloadFirstPage();
		        }
		    }
        }, {
             text: "刷新列表",
             listeners: {
                 click: function() {
                     _reloadFirstPage();
                 }
             }
        }, {
    		text: "新增分组",
            listeners: {
                 click: function() {
                     lbs.U.createTopTab({
                         id: _PID + "_add",
                         name: "新增分组",
                         url: "org/open_group.shtm?m=add",
                         qparams: {
                             menuId: _PID + "_add",
                             companyId: Ext.getCmp(_PID + "company_id").getValue(),
                             companyName: Ext.getCmp(_PID + "company_id").getRawValue()
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

//load first page.
_store.loadPage(1, {
    params: {
        companyId: _COMPANY_LIST[0].org_id
    }
});


})();
</script>
