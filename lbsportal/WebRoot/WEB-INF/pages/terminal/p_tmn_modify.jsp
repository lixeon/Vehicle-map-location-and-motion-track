<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function() {
	var _PID = "${menuId}";
	var _LW = 140;
	var _FW = 320;

    // page info
    var _P = {
        id: _PID,
        focusId: _PID + "terminalOpNo",
        items: null
    };

    var _resetForm = function(form) {
        // backup value
        var clearFlg = Ext.getCmp(_PID + "clearFlag");
        var checked = clearFlg.getValue();

        // reset
        form.reset();

        // recover value
        clearFlg.setValue(checked);

        // set focus
        Ext.getCmp(_P.focusId).focus();
    };
    
    var _chkForm = function(form) {
        var ok = true;

        // nothing

        return ok;
    };
    
    var _setServiceEndDateQuickly = function(monthOffset) {
        var dt = Ext.Date.add(new Date(), Ext.Date.MONTH, monthOffset);
        Ext.getCmp(_PID + "serviceEndDate").setValue(Ext.Date.format(dt, "Y-m-d"));
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
    				id : _PID + "terminalSerialNo",
    				fieldLabel : "终端编号",
    				name : "terminalSerialNo",
    				afterLabelTextTpl: lbs.G.required,
    				maxLength: 20,
    				readOnly: true,
    				value: "${terminal.terminal_serial_no}",
    				fieldStyle: {
    				    backgroundColor: "#efefef"  
    				},
    				allowBlank : false
    			},{
    				id : _PID + "terminalOpNo",
    				fieldLabel : "终端SIM卡号码",
    				name : "terminalOpNo",
    				regex: /^1\d{10}$/,
                    regexText: "号码格式不正确！",
    				afterLabelTextTpl: lbs.G.required,
    				maxLength: 11,
    				value: "${terminal.terminal_op_no}",
    				allowBlank : false
    			}]
    		}, {
    			xtype : "container",
    			items : [ {
    				fieldLabel : "IMEI",
    				name : "terminalIMEI",
    				afterLabelTextTpl: lbs.G.required,
    				maxLength: 30,
    				value: "${terminal.terminal_imei}",
    				allowBlank : false
    			}, {
    			    id: _PID + "serviceEndDate",
				    xtype: "datefield",
    				fieldLabel : "服务截止日",
                    name : "serviceEndDate",
                    afterLabelTextTpl: lbs.G.required,
                    format: "Y-m-d",
                    value: "${terminal.service_end_date}",
                    editable: true,
                    allowBlank: false
            	}, {
            	    xtype: "label",
            	    style: {
            	    	marginTop: "4px"
            	    },
            	    width: 50,
            	    html: "<span style='padding: 0px 5px'><a href='javascript:void(0);' >三个月</a></span>",
            	    listeners: {
            	        render: function() {
            	            var monthOffset = 3;
            	            Ext.fly(this.el).on("click", function() {
            	                _setServiceEndDateQuickly(monthOffset);
            	            });
            	        }
            	    }
            	}, {
            	    xtype: "label",
            	    style: {
            	    	marginTop: "4px"
            	    },
            	    width: 50,
            	    html: "<span style='padding: 0px 5px'><a href='javascript:void(0);' >六个月</a></span>",
            	    listeners: {
            	        render: function() {
            	            var monthOffset = 6;
            	            Ext.fly(this.el).on("click", function() {
            	                _setServiceEndDateQuickly(monthOffset);
            	            });
            	        }
            	    }
            	}, {
            	    xtype: "label",
            	    style: {
            	    	marginTop: "4px"
            	    },
            	    width: 40,
            	    html: "<span style='padding: 0px 5px'><a href='javascript:void(0);' >一年</a></span>",
            	    listeners: {
            	        render: function() {
            	            var monthOffset = 12;
            	            Ext.fly(this.el).on("click", function() {
            	                _setServiceEndDateQuickly(monthOffset);
            	            });
            	        }
            	    }
            	}]
    		}, {
    		    xtype : "container",
    		    items : [{
    			    fieldLabel : "IMSI",
    			    maxLength: 30,
    			    value: "${terminal.terminal_imsi}",
    				name : "terminalIMSI"
    		    }, {
    			    fieldLabel : "昵称",
    			    maxLength: 20,
    			    value: "${terminal.terminal_name}",
    				afterLabelTextTpl: lbs.G.required,
                    name : "terminalName",
                    regex: /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/,
                    regexText: "昵称只能由中文、字母、数字组成！",
                    allowBlank : false
    		    }]
    		}, {
    			xtype : "container",
    			items : [ {
    				xtype: "uXcombo",
        	        id: _PID + "providerId",
    			    fieldLabel : "终端厂商",
    				name : "providerId",
    				comboOpts : {
    					comboId:"CDPROVIDER",
    					autoLoad:true
    				},
    				labelWidth : _LW,
    				width : _FW
    			}, {
    				  	xtype: "uXcombo",
    	    	       	id: _PID + "terminalTypeId",
    				   	fieldLabel : "终端类型",
    					name : "terminalTypeId",
    					comboOpts : {
    						comboId:"CDTTYPE",
    						autoLoad:true
    					},
    					labelWidth : _LW,
    					width : _FW
    				}]
    		}, {
    		    xtype : "container",
    			items : [{
    				xtype : "uXcombo",
    				id: _PID + "provinceId",
    			    fieldLabel : "省份",
    				name : "provinceId",
    				comboOpts : {
    					comboId : "CDPROVINCE",
    					autoLoad: true
    				},
    				labelWidth : _LW,
    				width : _FW,
    				listeners : {
    				    change : function(el, newValue, oldValue, opts) {
    				        //alert(oldValue);
    				        var cityStore = Ext.getCmp(_PID + "cityId").getStore();
    				        cityStore.proxy.extraParams = {
    				            comboId : newValue + "." + lbs.G.CDK.CDCITY
    				        };
    				        cityStore.on("load", function() {
    				            if (lbs.U.isEmpty(oldValue)) {
    				                Ext.getCmp(_PID + "cityId").setValue("${terminal.city_id}");
    				            } else {
        				            Ext.getCmp(_PID + "cityId").select(cityStore.getAt(0));
    				            }
    				    	});
    				        cityStore.load();
    				    }
    				}
    			}, {
    				xtype : "uXcombo",
    				id : _PID + "cityId",
    			    fieldLabel : "城市",
    				name : "cityId",
    				comboOpts : {
    					comboId : ""
    				},
    				labelWidth : _LW,
    				width : _FW
    			}]
    		}, {
    	        xtype: "container",
    	        items: [{
    	            xtype: "radiogroup",
    	            id: _PID + "userType",
    	            fieldLabel: "用户类型",
    	            columns: 2,
    	            name: "userType",
    	            items: [{
    	                boxLabel: "个人用户",
    	                name: "userType",
    	                inputValue: "1"
    	            }, {
    	                boxLabel: "集团用户",
    	                name: "userType",
    	                inputValue: "2"
    	            }]
    	        }, {
    	            xtype: "radiogroup",
    	            id: _PID + "tmnServiceStatus",
    	            fieldLabel: "终端服务",
    	            columns: 2,
    	            name: "tmnServiceStatus",
    	            items: [{
    	                boxLabel: "开启",
                        name: "tmnServiceStatus",
                        inputValue: "1"
    	            }, {
    	                boxLabel: "关闭",
                        name: "tmnServiceStatus",
                        inputValue: "0"
    	            }]
    	        }]
    	    }, {
    	        xtype: "container",
    	        items: [{
        	        id: _PID + "orgName",
    	            fieldLabel : "企业组织",
    	            readOnly: true,
    	            width: _FW * 2,
    	            name: "orgName",
    	            value: "${terminal.org_name}",
    	            afterLabelTextTpl: lbs.G.required,
    	            allowBlank: false
    	        }, {
    	            xtype: "button",
    	            width: 48,
    	            style: {
    	                marginLeft: "2px"
    	            },
    	            text: "选择",
    	            listeners: {
    	                click: function() {
    	                    lbs.U.showOrgWin({
       	                        at: this,
       	                        selMode: "0",
       	                        callback: function(id, name) {
           	                        Ext.getCmp(_PID + "orgId").setValue(id);
           	                        Ext.getCmp(_PID + "orgName").setValue(name);
       	                        }
       	                    });
    	                }
    	            }
    	        }, {
    	            id: _PID + "orgId",
    	            xtype: "hidden",
    	            name: "orgId",
    	            value: "${terminal.org_id}",
    	            width: 0
    	        }, {
    	            xtype: "hidden",
    	            name: "orgId_old",
    	            value: "${terminal.org_id}",
    	            width: 0
    	        }]
    	    }]
    	}, {
    	    title : "用户信息及其它",
    	    items: [{
    	        xtype: "container",
    	        items: [{
        	        id: _PID + "mobile",
    	            fieldLabel : "车主号码",
    	            regex: /^1\d{10}$/,
                    regexText: "号码格式不正确！",
                    maxLength: 11,
                    value: "${terminal.CONF.USER.value}",
    	            name: "mobile"
    	        }, {
    	            id: _PID + "userName",
    	            fieldLabel : "车主姓名",
    	            value: "${terminal.owner}",
    	            maxLength: 20,
    	            name: "owner"
    	        }]
    	    }, {
    	        xtype: "container",
    	        items: [{
    	            fieldLabel : "家庭电话",
    	            value: "${terminal.owner_tel}",
    	            maxLength: 20,
    	            name: "ownerTel"
    	        }]
    	    }, {
    	        xtype: "container",
    	        items: [{
    	            fieldLabel : "地址",
    	            maxLength: 50,
    	            width: _FW * 2,
    	            value: "${terminal.owner_addr}",
    	            name: "ownerAddr"
    	        }]
    	    }, {
    	        xtype: "container",
    	        items: [{
    	            fieldLabel : "车辆设备编号",
    	            maxLength: 30,
    	            value: "${terminal.owner_vehice_no}",
    	            name: "ownerVehiceNo"
    	        }, {
    	            fieldLabel : "车架号",
    	            maxLength: 30,
    	            value: "${terminal.owner_vcbody_no}",
    	            name: "ownerVcbodyNo"
    	        }]
    	    }, {
    	        xtype: "container",
    	        items: [{
    	            fieldLabel : "发动机号",
    	            maxLength: 30,
    	            value: "${terminal.owner_vcmotor_no}",
    	            name: "ownerVcmotorNo"
    	        }, {
    	            fieldLabel : "保险单号",
    	            maxLength: 30,
    	            value: "${terminal.owner_insurance}",
    	            name: "ownerInsurance"
    	        }]
    	    }, {
    	        xtype: "container",
    	        items: [{
    	            fieldLabel : "备注",
    	            maxLength: 100,
    	            value: "${terminal.owner_remark}",
    	            width: _FW * 2,
    	            name: "ownerRemark"
    	        }]
    	    }, {
    	        xtype: "container",
    	        items: [{
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
    	            + "<li>重置默认登录密码：车主手机号码提供时为<font color='red'>该手机号码后六位。</font>否则为<font color='red'>终端SIM卡号码后六位。</font></li>"
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
    			items : [ /*{
    				text : "重置",
    				handler : function() {
    					_resetForm(this.up("form").getForm());
    				}
    			}, */{
    				text : "保存",
    				handler : function() {
    				    if (_chkForm(this.up("form").getForm())) {
        					this.up("form").getForm().submit({
        						url : "terminal/modify.shtm",
        						success : function(form, resp) {
        							if (resp.result.success == "true") {
        								Ext.Msg.alert(lbs.G.MsgTitle, "保存成功", function() {
        								    if (!Ext.getCmp(_PID + "clearFlag").getValue()) {
            								    _resetForm(form);
        								    }
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
    			}, {
    			    id: _PID + "clearFlag",
    			    xtype: "checkboxfield",
    			    fieldLabel: "保存后不清空",
    			    checked: true,
    			    labelAlign: "right"
    			} ]
    		}]
    	} ]
    });
    
	//show tab content
    _P.items = [ _mainPanel ];
    lbs.U.showTabContent(_P);

    // init combox
 
    lbs.U.initRemoteComboBox(_PID + "provinceId", "${terminal.province_id}");
    lbs.U.initRemoteComboBox(_PID + "terminalTypeId", "${terminal.terminal_type}");
    lbs.U.initRemoteComboBox(_PID + "providerId", "${terminal.provider}");

    Ext.getCmp(_PID + "userType").setValue({userType: "${terminal.user_type}"});
    Ext.getCmp(_PID + "tmnServiceStatus").setValue({tmnServiceStatus: "${terminal.status}"});
    
})();
</script>
