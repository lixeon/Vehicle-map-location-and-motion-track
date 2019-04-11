<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function() {

var _PID = "${menuId}";
var _LW = 160;
var _FW = 340;

// page info
var _P = {
    id: _PID,
    focusId: _PID + "clientType",
    items: null
};

var _resetForm = function(form) {
    // reset
    form.reset();

    // set focus
    Ext.getCmp(_P.focusId).focus();
};

var _chkForm = function(form) {
    var ok = true;

    // nothing

    return ok;
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
				labelStyle : "padding-right: 10px"
			}
		}
	},
	items : [ {
		title : "基本信息",
		items : [{
			xtype : "container",
			items : [ {
				id : _PID + "clientType",
				fieldLabel : "客户端ID",
				name : "clientType",
				afterLabelTextTpl: lbs.G.required,
				allowBlank : false
			}]
		}, {
		    xtype: "container",
		    items: [{
				id : _PID + "clientTypeName",
				fieldLabel : "客户端名称",
				name : "clientTypeName",
				afterLabelTextTpl: lbs.G.required,
				allowBlank : false
		    }]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel : "软件版本",
		        width : _FW,
		        name : "ver"
		    }]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel : "下载地址",
		        width : _FW * 2,
		        name : "downloadUrl"
		    }]
		}]
	}, {
		xtype : "container",
		frame : true,
		layout : "anchor",
		margin : "20 0",
		items : [ {
			xtype : "container",
			defaults : {
				xtype : "button",
				style : "margin-right: 5px; padding: 2px 20px"
			},
			items : [ {
				text : "重置",
				handler : function() {
					_resetForm(this.up("form").getForm());
				}
			}, {
				text : "保存",
				handler : function() {
				    if (_chkForm(this.up("form").getForm())) {
    					this.up("form").getForm().submit({
    						url : "app/client_add.shtm",
    						success : function(form, resp) {
    							if (resp.result.success == "true") {
    								Ext.Msg.alert(lbs.G.MsgTitle, "保存成功", function() {
								        lbs.U.deleteTopTab(_PID);
    								});
    							} else {
    			                    lbs.U.showError(resp.result.errorMsgs);
    			                }
    						},
    						faliure : function(form, resp) {
    							lbs.U.showError(resp.result.errorMsgs);
    						},
    						waitMsg: "正在保存..."
    					});
				    }
				}
			}]
		}]
	} ]
});

//show tab content
_P.items = [ _mainPanel ];
lbs.U.showTabContent(_P);
    
})();
</script>
