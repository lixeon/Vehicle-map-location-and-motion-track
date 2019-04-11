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

    
var _chkForm = function(form) {
    var ok = true;

    var uid = form.findField(_PID + "uid").getValue();
    var uidOld = form.findField(_PID + "uid_old").getValue();
    if (uid != uidOld) {
        ok = window.confirm("管理员ID已经变更（" + uidOld + "->" + uid + "）将强制重置密码为123456, 请确认是否继续？");
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
				fieldLabel : "企业组织编号",
				name : "orgId",
				value : "${org_id}",
				readOnly: true,
				fieldStyle: {
				    backgroundColor: "#efefef"  
				},
				allowBlank : false
			},{
				id : _PID + "org_name",
				fieldLabel : "企业组织名称",
				name : "orgName",
				value : "${org_name}",
				afterLabelTextTpl: lbs.G.required,
				allowBlank : false
			}]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel : "公司名称",
		        width : _FW * 2,
		        value : "${company}",
		        name : "company"
		    }]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel : "公司地址",
		        width : _FW * 2,
		        value : "${addr}",
		        name : "addr"
		    }]
		}]
	}, {
	    title : "企业管理员信息",
	    items: [{
	        xtype: "container",
	        items: [{
    	        id: _PID + "uid",
	            fieldLabel : "管理员ID",
	            allowBlank : false,
	            afterLabelTextTpl: lbs.G.required,
	            regex: /^\w+$/,
                regexText: "只能输入英文和数字，中间不能有空格！",
	            value : "${manager_id}",
	            name: "uid"
	        }, {
	            fieldLabel : "管理员姓名",
	            name: "name",
	            value : "${manager_name}",
	            afterLabelTextTpl: lbs.G.required,
	            allowBlank : false
	        }, {
	            id: _PID + "uid_old",
	            xtype: "hidden",
	            name: "uid_old",
	            value: "${manager_id}",
	            width: 0
	        }, {
	            xtype: "hidden",
	            name: "password",
	            value : "123456",
			    width: 0
	        }]
	    }, {
	        xtype: "container",
	        items: [{
    	        id: _PID + "mobile",
	            fieldLabel : "手机号码",
	            name: "mobile",
	            value : "${manager_mobile}",
	            regex: /^1\d{10}$/,
                regexText: "号码格式不正确！",
                maxLength: 11,
	            afterLabelTextTpl: lbs.G.required,
	            allowBlank: false
	        }, {
			    id: _PID + "resetPwd",
			    xtype: "checkboxfield",
			    fieldLabel: "重置密码",
			    checked: false,
			    value: "1",
			    name: "resetPwd",
			    labelAlign: "right"
	        }]
	    }, {
	        xtype: "box",
	        html: "<div style='margin: 10px; padding: 5px; border: 1px dashed #ff8040;color:#ed9200;' ><ul style='list-style: decimal;'>"
	        	+ "<li>管理员ID变更时将强制重置密码为123456。</li>"
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
			items : [{
				text : "保存",
				handler : function() {
				    if (_chkForm(this.up("form").getForm())) {
    					this.up("form").getForm().submit({
    						url : "org/modify_company.shtm",
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
