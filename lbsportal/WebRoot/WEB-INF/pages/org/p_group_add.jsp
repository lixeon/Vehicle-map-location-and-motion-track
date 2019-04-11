<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function() {

var _PID = "${menuId}";
var _LW = 160;
var _FW = 340;

// page info
var _P = {
    id: _PID,
    focusId: _PID + "org_name",
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
				id : _PID + "org_id",
				fieldLabel : "分组编号",
				name : "orgId",
				value : "${nextGroupId}",
				readOnly: true,
				fieldStyle: {
				    backgroundColor: "#efefef"  
				},
				allowBlank : false
			},{
				id : _PID + "org_name",
				fieldLabel : "分组名称",
				name : "orgName",
				afterLabelTextTpl: lbs.G.required,
				allowBlank : false
			}]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel : "所属企业组织",
		        name: "companyName",
		        readOnly: true,
		        fieldStyle: {
				    backgroundColor: "#efefef"  
				},
		        value: "${companyName}"
		    }, {
		        xtype: "hidden",
		        name: "companyId",
		        value: "${companyId}",
		        width: 0
		    }, {
	            fieldLabel: "分组标记色",
	            xtype: "fieldcontainer",
	            defaultType: "radiofield",
	            width : _FW * 1.7,
				defaults: {
				    flex: 1
				},
				layout: "hbox",
				items: [{
				    boxLabel: "默认",
				    name: "orgColor",
				    checked: true,
				    inputValue: "FFFFFF,000000"
				}, {
				    boxLabel: "<span style='background-color:#5B5B5B'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>",
				    name: "orgColor",
				    inputValue: "5B5B5B,FFFFFF"
				}, {
                    boxLabel: "<span style='background-color:#930000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>",
                    name: "orgColor",
                    inputValue: "930000,FFFFFF"
                }, {
				    boxLabel: "<span style='background-color:#0000E3'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>",
				    name: "orgColor",
				    inputValue: "0000E3,FFFFFF"
				}, {
				    boxLabel: "<span style='background-color:#D94600'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>",
				    name: "orgColor",
				    inputValue: "D94600,FFFFFF"
				}, {
                    boxLabel: "<span style='background-color:#6F00D2'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>",
                    name: "orgColor",
                    inputValue: "6F00D2,FFFFFF"
                }, {
                    boxLabel: "<span style='background-color:#00BB00'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>",
                    name: "orgColor",
                    inputValue: "00BB00,FFFFFF"
                }]
		    }]
		}]
	}, {
	    title : "分组管理员信息",
	    items: [{
	        xtype: "container",
	        items: [{
    	        id: _PID + "uid",
	            fieldLabel : "管理员ID",
	            allowBlank : false,
	            afterLabelTextTpl: lbs.G.required,
	            regex: /^\w+$/,
                regexText: "只能输入英文和数字，中间不能有空格！",
	            name: "uid"
	        }, {
	            fieldLabel : "管理员姓名",
	            name: "name",
	            afterLabelTextTpl: lbs.G.required,
	            allowBlank : false
	        }]
	    }, {
	        xtype: "container",
	        items: [{
    	        id: _PID + "mobile",
	            fieldLabel : "手机号码",
	            name: "mobile",
	            regex: /^1\d{10}$/,
                regexText: "号码格式不正确！",
                maxLength: 11,
	            afterLabelTextTpl: lbs.G.required,
	            allowBlank: false
	        }, {
	            fieldLabel : "密码",
	            name: "password",
	            afterLabelTextTpl: lbs.G.required,
	            value : "123456",
	            allowBlank: false
	        }]
	    }, {
	        xtype: "box",
	        html: "<div style='margin: 10px; padding: 5px; border: 1px dashed #ff8040;color:#ed9200;' ><ul style='list-style: decimal;'>"
	        	+ "<li>管理员默认登录密码为<font color='red'>123456</font>，请管理员登录后务必修改密码。</li>"
	        	+ "</ul></div>"
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
    						url : "org/add_group.shtm",
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
