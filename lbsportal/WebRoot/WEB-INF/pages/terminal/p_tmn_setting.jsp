<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" >
(function() {

var _PID = "${menuId}";
var _LW = 200;
var _FW = 400;

// data checker hodler
var _CHECKER_HOLDER = {};

// page info
var _P = {
    id: _PID,
    focusId: _PID + "terminalName",
    items: null
};


var _chkForm = function(form) {
    var ok = true;

    // nothing

    return ok;
};

var _showInfo = function(configKey, msg) {
    Ext.getCmp(_PID + configKey + "_info").setText("&nbsp;" + msg, false);
};


// data checker
var _dataChecker = function(opts) {
    var _DELAY = 1000 * 3;
    var _MAX_T = 4;

    var _queryTime = opts.queryTime;
    var _configKey = opts.configKey;
    var _terminalSerialNo = opts.terminalSerialNo;
    var _type = opts.type;
    
    var _count = 0;
    var _w_to = null;
    
    var doClear = function() {
    	if (_w_to != null) {
    	    window.clearTimeout(_w_to);
    	    _w_to = null;
    	    _CHECKER_HOLDER[_configKey] = null;
    	    delete _CHECKER_HOLDER[_configKey];
    	}
    };
    
    var handleResult = function(json) {
        if (json.CONFIG) {
            doClear();

            // 1. set new value
            if (_type == "radio") {
	            Ext.getCmp(_PID + _configKey + json.CONFIG.value).setValue(true);
            } else {
	            Ext.getCmp(_PID + _configKey).setValue(json.CONFIG.value);
            }
            // 2. show message.
            _showInfo(_configKey, "数据同步成功！");

        } else {
            // try again
            if (_count < _MAX_T) {
            	_count++;
                _w_to = window.setTimeout(doCheck, _DELAY);
            } else {
                doClear();
                _showInfo(_configKey, "数据同步失败！");
            }
        }
    };

    var doCheck = function() {
        Ext.Ajax.request({
            url: "terminal/get_config.shtm",
            params: {
               terminalSerialNo: _terminalSerialNo,
               queryTime: _queryTime,
               configKey: _configKey
            },
            success: function(response) {
                var json = Ext.JSON.decode(response.responseText);
                if (json.success == "true") {
                    handleResult(json);
                } else {
                   lbs.U.showError(json.errorMsgs);
                }
            }
        });
    };

    _showInfo(_configKey, "数据同步中...");

    // start check.
    _w_to = window.setTimeout(doCheck, _DELAY);
    
};

// sync from terminal
var _syncFromTerminal = function(configKey, type) {
    
    if (!lbs.U.isEmpty(_CHECKER_HOLDER[configKey])) {
        lbs.U.showInfo("正在同步数据，请稍侯！");
        return;
    }

    var terminalSerialNo = Ext.getCmp(_PID + "terminalSerialNo").getValue();

    Ext.Ajax.request({
        url: "terminal/query_config_cmd.shtm",
        params: {
            terminalSerialNo: terminalSerialNo,
           	configKey: configKey
        },
        success: function(response) {
            var json = Ext.JSON.decode(response.responseText);
            if (json.success == "true") {
                _CHECKER_HOLDER[configKey] = new _dataChecker({
                    terminalSerialNo: terminalSerialNo,
                    queryTime: json.queryTime,
                    configKey: configKey,
                    type: type
                });
            } else {
               lbs.U.showError(json.errorMsgs);
            }
        }
    });
    
};


var _isDisabled = function(status) {
    return (lbs.C.CONFIG_STATUS.DONE == status) ? false : true;
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
	    title: "基本信息",
	    items: [{
	        xtype: "container",
	        items: [{
	            id: _PID + "terminalName",
                fieldLabel: "昵称",
                name: "terminalName",
                value: "${terminal.terminal_name}",
                readOnly: true,
                fieldStyle: {
                    backgroundColor: "#efefef"  
                }
	        }, {
                fieldLabel : "车主姓名",
                value: "${terminal.owner}",
                name: "owner",
                readOnly: true,
                fieldStyle: {
                    backgroundColor: "#efefef"  
                }
	        }, {
	            id: _PID + "terminalSerialNo",
	            xtype: "hidden",
	            name: "terminalSerialNo",
	            value: "${terminal.terminal_serial_no}",
	            width: 0
	        }]
	    }]
	}, {
		title : "配置信息",
		items : [ {
			xtype : "container",
			items : [{
			    id: _PID + "SOFTVERSION",
				fieldLabel : "软件版本",
				name : "SOFTVERSION",
				value: "${terminal.CONF.SOFTVERSION.value}",
				readOnly: true,
				fieldStyle: {
				    backgroundColor: "#efefef"  
				}
			}, {
			    id: _PID + "SOFTVERSION_tag",
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.SOFTVERSION.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.SOFTVERSION.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("SOFTVERSION", "text");
                }
            }, {
                id: _PID + "SOFTVERSION_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
			xtype : "container",
			items : [{
			    id: _PID + "VBAT",
				fieldLabel : "电池电压、充电电压、电流",
				name : "VBAT",
				value: "${terminal.CONF.VBAT.value}",
				readOnly: true,
				fieldStyle: {
				    backgroundColor: "#efefef"  
				}
			}, {
			    id: _PID + "VBAT_tag",
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.VBAT.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.VBAT.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("VBAT", "text");
                }
            }, {
                id: _PID + "VBAT_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
			xtype : "container",
			items : [{
			    id: _PID + "VIN",
				fieldLabel : "外接电源电压",
				name : "VIN",
				value: "${terminal.CONF.VIN.value}",
				readOnly: true,
				fieldStyle: {
				    backgroundColor: "#efefef"  
				}
			}, {
			    id: _PID + "VIN_tag",
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.VIN.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.VIN.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("VIN", "text");
                }
            }, {
                id: _PID + "VIN_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
			xtype : "container",
			items : [{
			    id: _PID + "GSM",
				fieldLabel : "信号强度",
				name : "GSM",
				value: "${terminal.CONF.GSM.value}",
				readOnly: true,
				fieldStyle: {
				    backgroundColor: "#efefef"  
				}
			}, {
			    id: _PID + "GSM_tag",
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.GSM.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.GSM.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("GSM", "text");
                }
            }, {
                id: _PID + "GSM_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
			xtype : "container",
			hidden: lbs.U.isAuthByRole(lbs.C.ROLE.SA),
			items : [{
			    id: _PID + "DOMAIN",
				fieldLabel : "平台地址-端口",
				name : "DOMAIN",
				value: "${terminal.CONF.DOMAIN.value}",
				afterLabelTextTpl: lbs.G.required,
				allowBlank : false
			}, {
			    id: _PID + "DOMAIN_tag",
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.DOMAIN.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.DOMAIN.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("DOMAIN", "text");
                }
            }, {
                id: _PID + "DOMAIN_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype : "container",
		    hidden: lbs.U.isAuthByRole(lbs.C.ROLE.SA),
		    items : [ {
		        id: _PID + "FREQ",
            	fieldLabel : "GPS上报频度（秒）",
            	name : "FREQ",
            	value: "${terminal.CONF.FREQ.value}",
            	afterLabelTextTpl: lbs.G.required,
				allowBlank : false
            }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.FREQ.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.FREQ.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("FREQ", "text");
                }
            }, {
                id: _PID + "FREQ_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
	        xtype : "container",
	        items: [{
	            id: _PID + "PHONE",
	            fieldLabel: "终端SIM卡号码",
	            name: "PHONE",
	            value: "${terminal.CONF.PHONE.value}",
	            regex: /^1\d{10}$/,
                regexText: "号码格式不正确！",
				maxLength: 11,
				afterLabelTextTpl: lbs.G.required,
				allowBlank : false
	        }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.PHONE.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.PHONE.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("PHONE", "text");
                }
            }, {
                id: _PID + "PHONE_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
	        xtype : "container",
	        items: [{
	            id: _PID + "USER",
	            fieldLabel : "车主号码",
	            regex: /^1\d{10}$/,
                regexText: "号码格式不正确！",
                maxLength: 11,
	            name: "USER",
	            value: "${terminal.CONF.USER.value}"
	        }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.USER.status}"]
            }, {
                xtype: "button",
                text: "查询",
                width: 60,
                disabled: _isDisabled("${terminal.CONF.USER.status}"),
                handler: function() {
                    _syncFromTerminal("USER", "text");
                }
            }, {
                id: _PID + "USER_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items : [{
		        id: _PID + "PULSE",
		        fieldLabel : "心跳间隔（秒）",
				name : "PULSE",
				value: "${terminal.CONF.PULSE.value}",
				afterLabelTextTpl: lbs.G.required,
				allowBlank : false
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.PULSE.status}"]
            }, {
                xtype: "button",
                text: "查询",
                width: 60,
                disabled: _isDisabled("${terminal.CONF.PULSE.status}"),
                handler: function() {
                    _syncFromTerminal("PULSE", "text");
                }
            }, {
                id: _PID + "PULSE_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype : "container",
			items : [{
            	fieldLabel : "实时监控开关",
            	xtype: "fieldcontainer",
                defaultType: "radiofield",
                defaults: {
                    flex: 1
                },
                layout: "hbox",
                items: [{
                    id: _PID + "TRACE1",
                    boxLabel: "开",
                    name: "TRACE",
                    checked: ("${terminal.CONF.TRACE.value}" == "1") ? true : false,
                    inputValue: "1"
                }, {
                    id: _PID + "TRACE0",
                    boxLabel: "关",
                    name: "TRACE",
                    checked: ("${terminal.CONF.TRACE.value}" == "0") ? true : false,
                    inputValue: "0"
                }]
            }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.TRACE.status}"]
            }, {
                xtype: "button",
                text: "查询",
                width: 60,
                disabled: _isDisabled("${terminal.CONF.TRACE.status}"),
                handler: function() {
                    _syncFromTerminal("TRACE", "radio");
                }
            }, {
                id: _PID + "TRACE_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        id: _PID + "RADIUS",
		        fieldLabel : "围栏半径（米）",
				name : "RADIUS",
				value: "${terminal.CONF.RADIUS.value}",
				regex: /\d{3,5}$/,
                regexText: "格式不正确！",
                maxLength: 5,
                afterLabelTextTpl: lbs.G.required,
				allowBlank : false
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.RADIUS.status}"]
            }, {
                xtype: "button",
                text: "查询",
                width: 60,
                disabled: _isDisabled("${terminal.CONF.RADIUS.status}"),
                handler: function() {
                    _syncFromTerminal("RADIUS", "text");
                }
            }, {
                id: _PID + "RADIUS_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel : "移动告警开关",
				xtype: "fieldcontainer",
                defaultType: "radiofield",
                defaults: {
                    flex: 1
                },
                layout: "hbox",
                items: [{
                    id: _PID + "VIB1",
                    boxLabel: "开",
                    name: "VIB",
                    checked: ("${terminal.CONF.VIB.value}" == "1") ? true : false,
                    inputValue: "1"
                }, {
                    id: _PID + "VIB0",
                    boxLabel: "关",
                    name: "VIB",
                    checked: ("${terminal.CONF.VIB.value}" == "0") ? true : false,
                    inputValue: "0"
                }]
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.VIB.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.VIB.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("VIB", "radio");
                }
            }, {
                id: _PID + "VIB_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        id: _PID + "VIBL",
				xtype: "combobox",
		        fieldLabel : "移动告警灵敏度",
				name : "VIBL",
				editable: false,
				value: "${terminal.CONF.VIBL.value}",
				displayField: "name",
			    valueField: "value",
			    store: Ext.create("Ext.data.Store", {
			        fields: ["value", "name"],
			        data: [
					    {"value": "1", "name": "1"},
						{"value": "2", "name": "2"},
					    {"value": "3", "name": "3"},
					    {"value": "4", "name": "4"},
					    {"value": "5", "name": "5"},
					    {"value": "6", "name": "6"},
					    {"value": "7", "name": "7"},
					    {"value": "8", "name": "8"},
					    {"value": "9", "name": "9"},
					    {"value": "10", "name": "10"},
					    {"value": "11", "name": "11"},
					    {"value": "12", "name": "12"},
					    {"value": "13", "name": "13"},
					    {"value": "14", "name": "14"},
					    {"value": "15", "name": "15"}
			        ]
			    })
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.VIBL.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.VIBL.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("VIBL", "text");
                }
            }, {
                id: _PID + "VIBL_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel : "断电低电告警开关",
				xtype: "fieldcontainer",
                defaultType: "radiofield",
                defaults: {
                    flex: 1
                },
                layout: "hbox",
                items: [{
                    id: _PID + "POF1",
                    boxLabel: "开",
                    name: "POF",
                    checked: ("${terminal.CONF.POF.value}" == "1") ? true : false,
                    inputValue: "1"
                }, {
                    id: _PID + "POF0",
                    boxLabel: "关",
                    name: "POF",
                    checked: ("${terminal.CONF.POF.value}" == "0") ? true : false,
                    inputValue: "0"
                }]
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.POF.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.POF.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("POF", "radio");
                }
            }, {
                id: _PID + "POF_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        id: _PID + "POFT",
		        fieldLabel : "断电开启告警等待时长（秒）",
				name : "POFT",
				value: "${terminal.CONF.POFT.value}",
				regex: /\d{1,4}$/,
                regexText: "格式不正确！",
                maxLength: 4,
                afterLabelTextTpl: lbs.G.required,
				allowBlank : false
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.POFT.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.POFT.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("POFT", "text");
                }
            }, {
                id: _PID + "POFT_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        id: _PID + "LBV",
		        fieldLabel : "低电压阈值（微伏）",
				name : "LBV",
				value: "${terminal.CONF.LBV.value}",
				regex: /\d{6,}$/,
                regexText: "格式不正确！",
                afterLabelTextTpl: lbs.G.required,
				allowBlank : false
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.LBV.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.LBV.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("LBV", "text");
                }
            }, {
                id: _PID + "LBV_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel : "休眠开关",
				xtype: "fieldcontainer",
                defaultType: "radiofield",
                defaults: {
                    flex: 1
                },
                layout: "hbox",
                items: [{
                    id: _PID + "SLEEP1",
                    boxLabel: "开",
                    name: "SLEEP",
                    checked: ("${terminal.CONF.SLEEP.value}" == "1") ? true : false,
                    inputValue: "1"
                }, {
                    id: _PID + "SLEEP0",
                    boxLabel: "关",
                    name: "SLEEP",
                    checked: ("${terminal.CONF.SLEEP.value}" == "0") ? true : false,
                    inputValue: "0"
                }]
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.SLEEP.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.SLEEP.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("SLEEP", "radio");
                }
            }, {
                id: _PID + "SLEEP_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        id: _PID + "SLEEPT",
		        fieldLabel : "断电休眠等待时间（分钟）",
				name : "SLEEPT",
				value: "${terminal.CONF.SLEEPT.value}",
				regex: /\d{1,3}$/,
                regexText: "格式不正确！",
                maxLength: 3,
                afterLabelTextTpl: lbs.G.required,
				allowBlank : false
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.SLEEPT.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.SLEEPT.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("SLEEPT", "text");
                }
            }, {
                id: _PID + "SLEEPT_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel: "GPS过滤开关",
		        xtype: "fieldcontainer",
                defaultType: "radiofield",
                defaults: {
                    flex: 1
                },
                layout: "hbox",
                items: [{
                    id: _PID + "VIBGPS1",
                    boxLabel: "开",
                    name: "VIBGPS",
                    checked: ("${terminal.CONF.VIBGPS.value}" == "1") ? true : false,
                    inputValue: "1"
                }, {
                    id: _PID + "VIBGPS0",
                    boxLabel: "关",
                    name: "VIBGPS",
                    checked: ("${terminal.CONF.VIBGPS.value}" == "0") ? true : false,
                    inputValue: "0"
                }]
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.VIBGPS.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.VIBGPS.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("VIBGPS", "radio");
                }
            }, {
                id: _PID + "VIBGPS_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        id: _PID + "SPEED",
		        fieldLabel : "超速报警阈值（km/h）",
				name : "SPEED",
				value: "${terminal.CONF.SPEED.value}",
				regex: /\d{1,3}$/,
                regexText: "格式不正确！",
                maxLength: 3,
                afterLabelTextTpl: lbs.G.required,
				allowBlank : false
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.SPEED.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.SPEED.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("SPEED", "text");
                }
            }, {
                id: _PID + "SPEED_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        id: _PID + "VIBCHK",
		        fieldLabel : "震动频率告警因子",
				name : "VIBCHK",
				value: "${terminal.CONF.VIBCHK.value}",
				regex: /^\d+:\d+$/,
                regexText: "格式不正确！",
                afterLabelTextTpl: lbs.G.required,
				allowBlank : false
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.VIBCHK.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.VIBCHK.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("VIBCHK", "text");
                }
            }, {
                id: _PID + "VIBCHK_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel : "唤醒开关",
		        xtype: "fieldcontainer",
                defaultType: "radiofield",
                defaults: {
                    flex: 1
                },
                layout: "hbox",
                items: [{
                    id: _PID + "WAKEUP1",
                    boxLabel: "开",
                    name: "WAKEUP",
                    checked: ("${terminal.CONF.WAKEUP.value}" == "1") ? true : false,
                    inputValue: "1"
                }, {
                    id: _PID + "WAKEUP0",
                    boxLabel: "关",
                    name: "WAKEUP",
                    checked: ("${terminal.CONF.WAKEUP.value}" == "0") ? true : false,
                    inputValue: "0"
                }]
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.WAKEUP.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.WAKEUP.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("WAKEUP", "radio");
                }
            }, {
                id: _PID + "WAKEUP_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        id: _PID + "WAKEUPT",
		        fieldLabel : "休眠唤醒等待时间（分钟）",
				name : "WAKEUPT",
				value: "${terminal.CONF.WAKEUPT.value}",
				regex: /\d{1,3}$/,
                regexText: "格式不正确！",
                maxLength: 3,
                afterLabelTextTpl: lbs.G.required,
				allowBlank : false
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.WAKEUPT.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.WAKEUPT.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("WAKEUPT", "text");
                }
            }, {
                id: _PID + "WAKEUPT_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        fieldLabel : "自动设防开关",
		        xtype: "fieldcontainer",
                defaultType: "radiofield",
                defaults: {
                    flex: 1
                },
                layout: "hbox",
                items: [{
                    id: _PID + "ACCLOCK1",
                    boxLabel: "开",
                    name: "ACCLOCK",
                    checked: ("${terminal.CONF.ACCLOCK.value}" == "1") ? true : false,
                    inputValue: "1"
                }, {
                    id: _PID + "ACCLOCK0",
                    boxLabel: "关",
                    name: "ACCLOCK",
                    checked: ("${terminal.CONF.ACCLOCK.value}" == "0") ? true : false,
                    inputValue: "0"
                }]
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.ACCLOCK.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.ACCLOCK.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("ACCLOCK", "radio");
                }
            }, {
                id: _PID + "ACCLOCK_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
		}, {
		    xtype: "container",
		    items: [{
		        id: _PID + "ACCLT",
		        fieldLabel : "自动设防等待时间（秒）",
				name : "ACCLT",
				value: "${terminal.CONF.ACCLT.value}",
				regex: /\d{1,4}$/,
                regexText: "格式不正确！",
                maxLength: 3,
                afterLabelTextTpl: lbs.G.required,
				allowBlank : false
		    }, {
                xtype: "label",
                width: 80,
                html: lbs.C.CONFIG_STATUS["${terminal.CONF.ACCLT.status}"]
            }, {
                xtype: "button",
                text: "查询",
                disabled: _isDisabled("${terminal.CONF.ACCLT.status}"),
                width: 60,
                handler: function() {
                    _syncFromTerminal("ACCLT", "text");
                }
            }, {
                id: _PID + "ACCLT_info",
                xtype: "label",
                width: 200,
                html: ""
            }]
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
						url : "terminal/update_settings.shtm",
						success : function(form, resp) {
							if (resp.result.success == "true") {
								Ext.Msg.alert(lbs.G.MsgTitle, "保存成功!", function() {
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
	}
});

//show tab content
_P.items = [ _mainPanel ];
lbs.U.showTabContent(_P);
    
})();
</script>
