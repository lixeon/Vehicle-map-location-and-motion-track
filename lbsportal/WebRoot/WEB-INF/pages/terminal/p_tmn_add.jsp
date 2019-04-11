<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">
	(function() {

		var _PID = "${menuId}";
		var _LW = 140;
		var _FW = 320;

		// page info
		var _P = {
			id : _PID,
			focusId : _PID + "terminalSerialNo",
			items : null
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
			Ext.getCmp(_PID + "serviceEndDate").setValue(
					Ext.Date.format(dt, "Y-m-d"));
		};

		var _mainPanel = Ext
				.create(
						"Ext.form.Panel",
						{
							region : "center",
							autoScroll : true,
							buttonAlign : "left",
							frame : true,
							bodyPadding : 5,
							waitMsgTarget : true,
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
							items : [
									{
										title : "基本信息",
										items : [
												{
													xtype : "container",
													items : [
															{
																id : _PID
																		+ "terminalSerialNo",
																fieldLabel : "终端编号",
																name : "terminalSerialNo",
																afterLabelTextTpl : lbs.G.required,
																maxLength : 20,
																allowBlank : false
															},
															{
																id : _PID
																		+ "terminalOpNo",
																fieldLabel : "终端SIM卡号码",
																name : "terminalOpNo",
																regex : /^1\d{10}$/,
																regexText : "号码格式不正确！",
																afterLabelTextTpl : lbs.G.required,
																maxLength : 11,
																allowBlank : false
															} ]
												},
												{
													xtype : "container",
													items : [
															{
																fieldLabel : "IMEI",
																name : "terminalIMEI",
																afterLabelTextTpl : lbs.G.required,
																maxLength : 30,
																allowBlank : false
															},
															{
																id : _PID
																		+ "serviceEndDate",
																xtype : "datefield",
																fieldLabel : "服务截止日",
																name : "serviceEndDate",
																afterLabelTextTpl : lbs.G.required,
																format : "Y-m-d",
																editable : true,
																allowBlank : false
															},
															{
																xtype : "label",
																style : {
																	marginTop : "4px"
																},
																width : 50,
																html : "<span style='padding: 0px 5px'><a href='javascript:void(0);' >三个月</a></span>",
																listeners : {
																	render : function() {
																		var monthOffset = 3;
																		Ext
																				.fly(
																						this.el)
																				.on(
																						"click",
																						function() {
																							_setServiceEndDateQuickly(monthOffset);
																						});
																	}
																}
															},
															{
																xtype : "label",
																style : {
																	marginTop : "4px"
																},
																width : 50,
																html : "<span style='padding: 0px 5px'><a href='javascript:void(0);' >六个月</a></span>",
																listeners : {
																	render : function() {
																		var monthOffset = 6;
																		Ext
																				.fly(
																						this.el)
																				.on(
																						"click",
																						function() {
																							_setServiceEndDateQuickly(monthOffset);
																						});
																	}
																}
															},
															{
																xtype : "label",
																style : {
																	marginTop : "4px"
																},
																width : 40,
																html : "<span style='padding: 0px 5px'><a href='javascript:void(0);' >一年</a></span>",
																listeners : {
																	render : function() {
																		var monthOffset = 12;
																		Ext
																				.fly(
																						this.el)
																				.on(
																						"click",
																						function() {
																							_setServiceEndDateQuickly(monthOffset);
																						});
																	}
																}
															} ]
												},
												{
													xtype : "container",
													items : [
															{
																fieldLabel : "IMSI",
																maxLength : 30,
																name : "terminalIMSI"
															},
															{
																fieldLabel : "昵称",
																maxLength : 20,
																afterLabelTextTpl : lbs.G.required,
																name : "terminalName",
																regex : /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/,
																regexText : "昵称只能由中文、字母、数字组成！",
																allowBlank : false
															} ]
												},
												{
													xtype : "container",
													items : [
															{
																xtype : "uXcombo",
																id : _PID
																		+ "provider",
																fieldLabel : "终端厂商",
																name : "provider",
																comboOpts : {
																	comboId : "CDPROVIDER",
																	autoLoad : true
																},
																labelWidth : _LW,
																width : _FW
															},
															{
																xtype : "uXcombo",
																id : _PID
																		+ "terminalTypeId",
																fieldLabel : "终端类型",
																name : "terminalType",
																comboOpts : {
																	comboId : "CDTTYPE",
																	autoLoad : true
																},
																labelWidth : _LW,
																width : _FW
															} ]
												},
												{
													xtype : "container",
													items : [
															{
																xtype : "uXcombo",
																fieldLabel : "省份",
																name : "provinceId",
																comboOpts : {
																	comboId : lbs.G.CDK.CDPROVINCE,
																	autoLoad : false
																},
																labelWidth : _LW,
																width : _FW,
																listeners : {
																	change : function(
																			el,
																			newValue,
																			oldValue,
																			opts) {
																		var cityStore = Ext
																				.getCmp(
																						_PID
																								+ "cityId")
																				.getStore();
																		cityStore.proxy.extraParams = {
																			comboId : newValue
																					+ "."
																					+ lbs.G.CDK.CDCITY
																		};
																		cityStore
																				.on(
																						"load",
																						function() {
																							Ext
																									.getCmp(
																											_PID
																													+ "cityId")
																									.select(
																											cityStore
																													.getAt(0));
																						});
																		cityStore
																				.load();
																	}
																}
															},
															{
																xtype : "uXcombo",
																id : _PID
																		+ "cityId",
																fieldLabel : "城市",
																name : "cityId",
																comboOpts : {
																	comboId : ""
																},
																labelWidth : _LW,
																width : _FW
															} ]
												},
												{
													xtype : "container",
													items : [
															{
																fieldLabel : "用户类型",
																xtype : "fieldcontainer",
																defaultType : "radiofield",
																defaults : {
																	flex : 1
																},
																layout : "hbox",
																items : [
																		{
																			boxLabel : "个人用户",
																			id : _PID
																					+ "userType_P",
																			name : "userType",
																			checked : true,
																			inputValue : "1"
																		},
																		{
																			boxLabel : "集团用户",
																			name : "userType",
																			inputValue : "2"
																		} ]
															},
															{
																fieldLabel : "终端服务",
																xtype : "fieldcontainer",
																defaultType : "radiofield",
																defaults : {
																	flex : 1
																},
																layout : "hbox",
																items : [
																		{
																			boxLabel : "开启",
																			name : "tmnServiceStatus",
																			checked : true,
																			inputValue : "1"
																		},
																		{
																			boxLabel : "关闭",
																			name : "tmnServiceStatus",
																			inputValue : "0"
																		} ]
															} ]
												},
												{
													xtype : "container",
													items : [
															{
																id : _PID
																		+ "orgName",
																fieldLabel : "企业组织",
																readOnly : true,
																width : _FW * 2,
																name : "orgName",
																afterLabelTextTpl : lbs.G.required,
																allowBlank : false
															},
															{
																xtype : "button",
																width : 48,
																style : {
																	marginLeft : "2px"
																},
																text : "选择",
																listeners : {
																	click : function() {
																		lbs.U
																				.showOrgWin({
																					at : this,
																					selMode : "0",
																					callback : function(
																							id,
																							name) {
																						Ext
																								.getCmp(
																										_PID
																												+ "orgId")
																								.setValue(
																										id);
																						Ext
																								.getCmp(
																										_PID
																												+ "orgName")
																								.setValue(
																										name);
																					}
																				});
																	}
																}
															},
															{
																id : _PID
																		+ "orgId",
																xtype : "hidden",
																name : "orgId",
																width : 0
															} ]
												} ]
									},
									{
										title : "用户信息及其它",
										items : [
												{
													xtype : "container",
													items : [ {
														id : _PID + "mobile",
														fieldLabel : "车主号码",
														regex : /^1\d{10}$/,
														regexText : "号码格式不正确！",
														maxLength : 11,
														name : "mobile"
													}, {
														id : _PID + "userName",
														fieldLabel : "车主姓名",
														maxLength : 20,
														name : "owner"
													} ]
												},
												{
													xtype : "container",
													items : [ {
														fieldLabel : "家庭电话",
														maxLength : 20,
														name : "ownerTel"
													} ]
												},
												{
													xtype : "container",
													items : [ {
														fieldLabel : "地址",
														width : _FW * 2,
														maxLength : 50,
														name : "ownerAddr"
													} ]
												},
												{
													xtype : "container",
													items : [ {
														fieldLabel : "车辆设备编号",
														maxLength : 30,
														name : "ownerVehiceNo"
													}, {
														fieldLabel : "车架号",
														maxLength : 30,
														name : "ownerVcbodyNo"
													} ]
												},
												{
													xtype : "container",
													items : [ {
														fieldLabel : "发动机号",
														maxLength : 30,
														name : "ownerVcmotorNo"
													}, {
														fieldLabel : "保险单号",
														maxLength : 30,
														name : "ownerInsurance"
													} ]
												},
												{
													xtype : "container",
													items : [ {
														fieldLabel : "备注",
														maxLength : 100,
														width : _FW * 2,
														name : "ownerRemark"
													} ]
												},
												{
													xtype : "box",
													html : "<div style='margin: 10px; padding: 5px; border: 1px dashed #ff8040;color:#ed9200;' ><ul style='list-style: decimal;'>"
															+ "<li>默认登录密码：车主手机号码提供时为<font color='red'>该手机号码后六位。</font>否则为<font color='red'>终端SIM卡号码后六位。</font></li>"
															+ "</ul></div>"
												} ]
									},
									{
										title : "配置信息",
										collapsed : true,
										collapsible : true,
										hidden : true,
										checkboxToggle : true,
										items : [ {
											xtype : "container",
											items : [ {
												fieldLabel : "平台地址-端口",
												name : "DOMAIN",
												value : "${conf.DOMAIN}"
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "实时监控开关",
												xtype : "fieldcontainer",
												defaultType : "radiofield",
												defaults : {
													flex : 1
												},
												layout : "hbox",
												items : [ {
													boxLabel : "开",
													name : "TRACE",
													checked : true,
													inputValue : "1"
												}, {
													boxLabel : "关",
													name : "TRACE",
													inputValue : "0"
												} ]
											}, {
												fieldLabel : "GPS上报频度",
												name : "FREQ",
												value : "${conf.FREQ}"
											}, {
												xtype : "label",
												html : "&nbsp;秒"
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "心跳间隔",
												name : "PULSE",
												value : "${conf.PULSE}"
											}, {
												xtype : "label",
												html : "&nbsp;秒"
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "围栏半径",
												name : "RADIUS",
												value : "${conf.RADIUS}"
											}, {
												xtype : "label",
												html : "&nbsp;米"
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "移动告警开关",
												xtype : "fieldcontainer",
												defaultType : "radiofield",
												defaults : {
													flex : 1
												},
												layout : "hbox",
												items : [ {
													boxLabel : "开",
													name : "VIB",
													checked : true,
													inputValue : "1"
												}, {
													boxLabel : "关",
													name : "VIB",
													inputValue : "0"
												} ]
											}, {
												fieldLabel : "移动告警灵敏度",
												name : "VIBL",
												value : "${conf.VIBL}"
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "断电低电告警开关",
												xtype : "fieldcontainer",
												defaultType : "radiofield",
												defaults : {
													flex : 1
												},
												layout : "hbox",
												items : [ {
													boxLabel : "开",
													name : "POF",
													checked : true,
													inputValue : "1"
												}, {
													boxLabel : "关",
													name : "POF",
													inputValue : "0"
												} ]
											}, {
												fieldLabel : "断电告警等待时间",
												name : "POFT",
												value : "${conf.POFT}"
											}, {
												xtype : "label",
												html : "&nbsp;秒"
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "低电压阈值",
												name : "LBV",
												value : "${conf.LBV}"
											}, {
												xtype : "label",
												html : "&nbsp;毫伏"
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "休眠开关",
												xtype : "fieldcontainer",
												defaultType : "radiofield",
												defaults : {
													flex : 1
												},
												layout : "hbox",
												items : [ {
													boxLabel : "开",
													name : "SLEEP",
													checked : true,
													inputValue : "1"
												}, {
													boxLabel : "关",
													name : "SLEEP",
													inputValue : "0"
												} ]
											}, {
												fieldLabel : "断电休眠等待时间",
												name : "SLEEPT",
												value : "${conf.SLEEPT}"
											}, {
												xtype : "label",
												html : "&nbsp;分钟"
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "GPS过滤开关",
												xtype : "fieldcontainer",
												defaultType : "radiofield",
												defaults : {
													flex : 1
												},
												layout : "hbox",
												items : [ {
													boxLabel : "开",
													name : "VIBGPS",
													checked : true,
													inputValue : "1"
												}, {
													boxLabel : "关",
													name : "VIBGPS",
													inputValue : "0"
												} ]
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "超速报警阈值",
												name : "SPEED",
												value : "${conf.SPEED}"
											}, {
												xtype : "label",
												html : "&nbsp;千米/小时",
												width : 80
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "震动频率告警因子",
												name : "VIBCHK",
												value : "${conf.VIBCHK}"
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "唤醒开关",
												xtype : "fieldcontainer",
												defaultType : "radiofield",
												defaults : {
													flex : 1
												},
												layout : "hbox",
												items : [ {
													boxLabel : "开",
													name : "WAKEUP",
													checked : true,
													inputValue : "1"
												}, {
													boxLabel : "关",
													name : "WAKEUP",
													inputValue : "0"
												} ]
											}, {
												fieldLabel : "休眠唤醒等待时间",
												name : "WAKEUPT",
												value : "${conf.WAKEUPT}"
											}, {
												xtype : "label",
												html : "&nbsp;分钟"
											} ]
										}, {
											xtype : "container",
											items : [ {
												fieldLabel : "自动设防开关",
												xtype : "fieldcontainer",
												defaultType : "radiofield",
												defaults : {
													flex : 1
												},
												layout : "hbox",
												items : [ {
													boxLabel : "开",
													name : "ACCLOCK",
													checked : true,
													inputValue : "1"
												}, {
													boxLabel : "关",
													name : "ACCLOCK",
													inputValue : "0"
												} ]
											}, {
												fieldLabel : "自动设防等待时间",
												name : "ACCLT",
												value : "${conf.ACCLT}"
											}, {
												xtype : "label",
												html : "&nbsp;秒"
											} ]
										} ]
									},
									{
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
											items : [
													{
														text : "重置",
														handler : function() {
															_resetForm(this.up(
																	"form")
																	.getForm());
														}
													},
													{
														text : "保存",
														handler : function() {
															if (_chkForm(this
																	.up("form")
																	.getForm())) {
																this
																		.up(
																				"form")
																		.getForm()
																		.submit(
																				{
																					url : "terminal/add.shtm",
																					success : function(
																							form,
																							resp) {
																						if (resp.result.success == "true") {
																							Ext.Msg
																									.alert(
																											lbs.G.MsgTitle,
																											"保存成功",
																											function() {
																												if (!Ext
																														.getCmp(
																																_PID
																																		+ "clearFlag")
																														.getValue()) {
																													_resetForm(form);
																												}
																											});
																						} else {
																							lbs.U
																									.showError(resp.result.errorMsgs);
																						}
																					},
																					faliure : function(
																							form,
																							resp) {
																						lbs.U
																								.showError(resp.result.errorMsgs);
																					},
																					waitMsg : "正在保存..."
																				});
															}
														}
													},
													{
														id : _PID + "clearFlag",
														xtype : "checkboxfield",
														fieldLabel : "保存后不清空",
														checked : true,
														labelAlign : "right"
													} ]
										} ]
									} ]
						});

		//show tab content
		_P.items = [ _mainPanel ];
		lbs.U.showTabContent(_P);

	})();
</script>
