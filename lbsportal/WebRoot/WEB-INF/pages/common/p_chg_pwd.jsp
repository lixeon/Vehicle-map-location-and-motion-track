<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function() {

var _PID = "${menuId}";
var _LW = 100;
var _FW = 240;

// page info
var _P = {
    id: _PID,
    focusId: _PID + "oldPwd",
    items: null
};

var _resetForm = function(form) {
    form.reset();
    // set focus
    Ext.getCmp(_P.focusId).focus();
};

var _chkForm = function(form) {
    var ok = true;
    
    var newPwd = form.findField(_PID + "newPwd").getValue();
    var newPwd2 = form.findField(_PID + "newPwd2").getValue();
    if (newPwd2 != newPwd) {
        lbs.U.showError("两次输入的密码不一致！");
        ok = false;
    }

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
			padding : "5 0",
			defaults : {
				labelAlign : "right",
				labelWidth : _LW,
				width : _FW,
				labelStyle : "padding-right: 10px"
			}
		}
	},
	items : [ {
		title : "修改密码",
		items : [{
			xtype : "container",
			items : [ {
				id : _PID + "oldPwd",
				inputType: "password",
				fieldLabel : "旧密码",
				name : "oldPwd",
				afterLabelTextTpl: lbs.G.required,
				maxLength: 20,
				allowBlank : false
			}]
		}, {
			xtype : "container",
			items : [ {
				id : _PID + "newPwd",
				inputType: "password",
				fieldLabel : "新密码",
				name : "newPwd",
				afterLabelTextTpl: lbs.G.required,
				maxLength: 20,
				allowBlank : false
			}]
		}, {
			xtype : "container",
			items : [ {
				id : _PID + "newPwd2",
				inputType: "password",
				fieldLabel : "再一遍",
				name : "oldPwd2",
				afterLabelTextTpl: lbs.G.required,
				maxLength: 20,
				allowBlank : false
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
			items : [{
				text : "保存",
				handler : function() {
				    if (_chkForm(this.up("form").getForm())) {
    					this.up("form").getForm().submit({
    						url : "portal/modify_pwd.shtm",
    						success : function(form, resp) {
    							if (resp.result.success == "true") {
    								Ext.Msg.alert(lbs.G.MsgTitle, "密码修改成功, 下次请用新密码进行登录！", function() {
    								    _resetForm(form);
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
