<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function() {

var _PID = "${menuId}";
var _LW = 100;
var _FW = 140;

// page info
var _P = {
    id: _PID,
    focusId: _PID + "btn_terminal_org_cache",
    items: null
};


var _mainPanel = Ext.create("Ext.form.Panel", {
    region : "center",
	autoScroll: true,
	buttonAlign : "left",
	frame : true,
	bodyPadding : 5,
	waitMsgTarget: true,
	defaults : {
		xtype : "fieldset",
		layout : "anchor",
		defaults : {
			anchor : "100%",
			layout : "hbox",
			defaultType : "textfield",
			padding : "2 0",
			defaults : {
				labelAlign : "right",
				labelWidth : _LW,
				width : _FW,
				labelStyle : "padding-right: 10px; "
			}
		}
	},
	items : [ {
	    title: "缓存列表",
	    items: [{
	        xtype: "container",
	        items: [{
	            xtype: "label",
	            html: "分组终端信息缓存："
	        }, {
	        	id: _PID + "btn_terminal_org_cache",
	        	xtype: "button",
	        	text: "刷新",
	        	handler : function() {
	        	    Ext.Ajax.request({
	        	        url: "sysset/refresh_terminalgroup.shtm",
	        	        params: {},
	        	        success: function(response) {
	        	            var json = Ext.JSON.decode(response.responseText);
	        	            if (json.success == "true") {
	        	                var countHint = Ext.getCmp(_PID + "terminal_org_cache_info");
	        	                countHint.update("刷新成功!");
	        	            } else {
	        	               lbs.U.showError(json.errorMsgs);
	        	            }
	        	        }
	        	    });
                }
	        }, {
	        	id: _PID + "terminal_org_cache_info",
	        	xtype: "label",
	        	html: ""
	        }]
        }]
	}]
});

//show tab content
_P.items = [ _mainPanel ];
lbs.U.showTabContent(_P);
    
})();
</script>
