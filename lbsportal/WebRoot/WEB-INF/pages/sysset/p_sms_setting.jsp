<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function() {

var _PID = "${menuId}";
var _LW = 120;
var _FW = 280;

// page info
var _P = {
    id: _PID,
    focusId: _PID + "lowlevel",
    items: null
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
				labelStyle : "padding-right: 10px; "
			}
		}
	},
	items : [ {
	    title: "配置信息",
	    items: [{
	        xtype: "container",
	        items: [{
                fieldLabel : "当前余额",
                value: "${fee}",
                name: "fee",
                readOnly: true,
                fieldStyle: {
				    backgroundColor: "#efefef"  
				}
	        }, {
	            xtype: "label",
	            html: "&nbsp;更新时间：${fee_update_time}"
	        }]
	    }, {
	        xtype: "container",
	        items: [{
	            id: _PID + "lowlevel",
                fieldLabel : "预警阈值",
                value: "${lowlevel}",
                name: "lowlevel"
	        }]
	    }, {
	        xtype: "container",
	        items: [{
	            fieldLabel : "提醒号码列表",
                value: "${reminder}",
                width: _FW * 2,
                name: "reminder"
	        }]
	    }, {
	        xtype: "box",
	        html: "<div style='margin: 10px; padding: 5px; border: 1px dashed #ff8040;color:#ed9200;' ><ul style='list-style: decimal;'>"
	        	+ "<li>系统默认每天09:30和15:30对短信平台的余额进行查询，余额小于预警值时发送短信通知。</li>"
	        	+ "</ul></div>"
	    }]
	}],
	dockedItems: {
	    xtype: "toolbar",
	    dock: "top",
	    items: [{
	        text: "保存",
	        style: {width: "80px"},
	        handler: function() {
			    if (_chkForm(this.up("form").getForm())) {
					this.up("form").getForm().submit({
						url : "sysset/update_sms_settings.shtm",
						success : function(form, resp) {
							if (resp.result.success == "true") {
								lbs.U.showInfo("保存成功!");
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
	}
});

//show tab content
_P.items = [ _mainPanel ];
lbs.U.showTabContent(_P);
    
})();
</script>
