<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function () {

var _PID = "${menuId}";
var _DATA_COMPANY = Ext.JSON.decode('${tmn_list_company}');
var _DATA_GROUP = Ext.JSON.decode('${tmn_list_group}');
var _ORG_ID = "${orgId}";


var _TH = 480;
var _TW = 240;

// page info
var _P = {
    id: _PID,
    items: null
};

// click item and copy to the right tree if it dosen't has the child.
var _clickCompanyListItem = function(v, rcd) {
    var node = _groupListStore.getRootNode().findChild("id", rcd.raw.id);
    if (node == null) {
        var newObj = rcd.copy();
        _groupListStore.getRootNode().appendChild(newObj);
    }
};


var _companyListStore = Ext.create("Ext.data.TreeStore", {
    root: {
        expanded: true,
        children: _DATA_COMPANY
    }
});


var _groupListStore = Ext.create("Ext.data.TreeStore", {
    root: {
        expanded: true,
        children: _DATA_GROUP
    }
});

// delete item form group list
var _deleteGroupListItem = function() {
    var models = Ext.getCmp(_PID + "group_list").getSelectionModel().getSelection();
    if (models == null || models.length == 0) {
        return;
    }

    for (var i = 0; i < models.length; i++) {
        _groupListStore.getRootNode().removeChild(models[i]);
    }
};

var _copyAll2GroupList = function() {
	var compNodes = _companyListStore.getRootNode().childNodes;
	for (var i = 0; i < compNodes.length; i++) {
    	_clickCompanyListItem(null, compNodes[i]);
	}
};

// save group items
var _saveGroupItems = function() {
    var newRcds = _groupListStore.getNewRecords();
    var removedRcds = _groupListStore.getRemovedRecords();
    
    if ((newRcds == null || newRcds.length == 0)
            && (removedRcds == null || removedRcds.length == 0)) {
        return;
    }
	
	var allRcds = _groupListStore.getRootNode().childNodes;
	var tmnIds = [];
	for (var i = 0; i < allRcds.length; i++) {
	    tmnIds.push(allRcds[i].raw.id);
	}
	
    Ext.Ajax.request({
        url: "org/bind_terminal.shtm",
        params: {
           orgId: _ORG_ID,
           tmnIds: tmnIds.join(",")
        },
        success: function(response) {
            var json = Ext.JSON.decode(response.responseText);
            if (json.success == "true") {
 	           	Ext.MessageBox.show({
                    title: lbs.G.MsgTitle,
                    msg: "保存成功！",
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
				});
            } else {
               lbs.U.showError(json.errorMsgs);
            }
        }
    });
};

// Main Panel
var _mainPanel = Ext.create("Ext.panel.Panel", {
	region: "center",
    buttonAlign: "left",
    frame: true,
    bodyPadding: 10,
    layout: "hbox",
    items: [{
        xtype: "treepanel",
        width: _TW,
        height: _TH,
        region : "left",
        rootVisible : false,
        border : true,
        store: _companyListStore,
        listeners : {
            itemclick : {
                fn : _clickCompanyListItem
            }
        }
    }, {
        xtype: "panel",
        region: "center",
        bodyPadding: "20, 10",
        items: [{
            xtype: "button",
            text: "全部&nbsp;>>",
            handler: function() {
                _copyAll2GroupList();
            }
        }]
    }, {
        id: _PID + "group_list",
        xtype: "treepanel",
        width: _TW,
        height: _TH,
        region : "right",
        rootVisible : false,
        border : true,
        store: _groupListStore
    }, {
        xtype: "panel",
        region: "center",
        bodyPadding: "20, 10",
        items: [{
            xtype: "button",
            text: "删除",
            handler: function() {
                _deleteGroupListItem();
            }
        }]
    }],
    dockedItems: [{
        xtype: "toolbar",
        dock: "top",
        ui: "header",
        items: [{
            text: "保存",
            width: 80,
            handler: function() {
                _saveGroupItems();
            }
        }]
    }]
});


//show tab content
_P.items = [ _mainPanel ];
lbs.U.showTabContent(_P);


})();
</script>
