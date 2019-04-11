lbs.U.toStr = function(c) {
	var b = "";
	for ( var a in c) {
		b += a + ":" + c[a] + "\n"
	}
	return b
};
lbs.U.encodeEach = function(b) {
	for ( var a in b) {
		b[a] = Ext.encode(b[a])
	}
};
lbs.U.isEmpty = function(a) {
	if (a === undefined) {
		return true
	}
	if (a == null) {
		return true
	}
	if (a == "") {
		return true
	}
	return false
};
lbs.U.createTopTab = function(b) {
	var d = "tbp_" + b.id;
	var a = Ext.getCmp(d);
	if (a == null) {
		var c = null;
		if (!lbs.U.isEmpty(b.qparams)) {
			c = b.qparams
		} else {
			c = {}
		}
		c.menuId = d;
		a = Ext.create("Ext.window.Window", {
			id : d,
			title : b.name,
			icon : b.icon || "",
			closable : true,
			layout : "fit",
			width : 1004,
			height : 500,
			loader : {
				baseParams : c,
				autoLoad : true,
				scripts : true,
				url : b.url
			}
		});
		a.show()
	} else {
//		lbs.G.tabs.setActiveTab(a)
	}
};
lbs.U.createTopTabInIframe = function(d) {
	var g = "tbp_" + d.id;
	var c = Ext.getCmp(g);
	if (c == null) {
		var f = Ext.merge({
			menuId : g
		}, d.qparams);
		var e = "";
		for ( var b in f) {
			e += "" + b + "=" + f[b] + "&"
		}
		e = e.substring(0, e.length - 1);
		var a = d.url + "?" + e;
		c = Ext.create("Ext.panel.Panel", {
			id : g,
			title : d.name,
			icon : d.icon || "",
			closable : true,
			layout : "fit",
			html : "<iframe frameborder='0' name='" + g
					+ "' height='100%' width='100%' src='" + a + "' ></iframe>"
		});
		lbs.G.tabs.add(c).show()
	} else {
		lbs.G.tabs.setActiveTab(c)
	}
};
lbs.U.deleteTopTab = function(a) {
	lbs.G.tabs.remove(a)
};
lbs.U.showTabContent = function(a) {
	Ext.getCmp(a.id).add({
		xtype : "panel",
		bodyPadding : 0,
		border : 0,
		layout : "border",
		margin : 0,
		items : a.items
	});
	if (!lbs.U.isEmpty(a.focusId)) {
		Ext.getCmp(a.focusId).focus()
	}
};
lbs.U.initRemoteComboBox = function(a, c) {
	var b = Ext.getCmp(a).getStore();
	b.on("load", function() {
		Ext.getCmp(a).setValue(c)
	});
	b.load()
};
lbs.U.showError = function(a) {
	Ext.MessageBox.show({
		title : lbs.G.MsgTitle,
		msg : a,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.ERROR
	})
};
lbs.U.showInfo = function(a) {
	Ext.MessageBox.show({
		title : lbs.G.MsgTitle,
		msg : a,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.INFO
	})
};
lbs.G.OrgWin = null;
lbs.U.showOrgWin = function(d) {
	var a = d.at || "";
	var h = d.callback;
	var g = d.selMode || "0";
	var e = "lbs.U.CommonSelect.OrgTree";
	if (lbs.G.OrgWin == null) {
		var f = Ext.create("Ext.data.TreeStore", {
			fields : [ {
				name : "id",
				type : "string"
			}, {
				name : "org_name",
				type : "string"
			}, {
				name : "text",
				type : "string"
			}, {
				name : "leaf",
				type : "boolean"
			} ],
			nodeParam : "parentOrgId",
			proxy : {
				type : "ajax",
				url : "common/load_orglist.shtm",
				reader : {
					root : "orgList"
				}
			},
			root : {
				expanded : true,
				id : lbs.C.TOP_GROUP
			}
		});
		lbs.G.OrgWin = Ext.create("Ext.window.Window", {
			title : "企业组织选择",
			height : 360,
			width : 240,
			layout : "fit",
			closable : true,
			closeAction : "hide",
			animateTarget : a,
			modal : true,
			renderTo : Ext.getBody(),
			items : {
				id : e,
				xtype : "treepanel",
				useArrows : true,
				rootVisible : false,
				store : f,
				dockedItems : [ {
					xtype : "toolbar",
					dock : "bottom",
					ui : "footer",
					items : [ {
						id : e + "_yes",
						text : "确定",
						width : 50
					} ]
				}, {
					xtype : "toolbar",
					dock : "top",
					ui : "header",
					items : [ {
						text : "展开",
						listeners : {
							click : function() {
								Ext.getCmp(e).expandAll()
							}
						}
					}, {
						text : "收起",
						listeners : {
							click : function() {
								Ext.getCmp(e).collapseAll()
							}
						}
					}, {
						text : "刷新",
						listeners : {
							click : function() {
								f.load()
							}
						}
					} ]
				} ]
			}
		})
	} else {
		Ext.getCmp(e).getStore().load()
	}
	var c = lbs.U.showOrgWin;
	var b = c.okbtnAction;
	c.okbtnAction = function() {
		var i = Ext.getCmp(e).getChecked();
		if (g == "0") {
			if (i.length != 1) {
				lbs.U.showError("只能选中一个企业组织！");
				return
			}
			var l = i[0];
			var k = l.parentNode;
			var j = k.raw.text;
			if (j == "Root") {
				j = ""
			} else {
				j = j + "->"
			}
			h(l.raw.id, j + l.raw.text)
		} else {
		}
		lbs.G.OrgWin.hide(a)
	};
	lbs.G.OrgWin.show(a, function() {
		var i = Ext.getCmp(e + "_yes");
		if (b != null && b != undefined) {
			i.removeListener("click", b)
		}
		i.on("click", c.okbtnAction)
	})
};
lbs.U.isAuthByRole = function(b) {
	var a = Ext.util.Cookies.get("role");
	return (Math.floor(a) <= Math.floor(b)) ? false : true
};
lbs.G.AlarmListWin = null;
lbs.U.showAlarmList = function(d) {
	var a = "stid_lalarm_list";
	if (lbs.G.AlarmListWin == null) {
		var e = Ext.create("Ext.data.Store", {
			storeId : a,
			fields : [ {
				name : "terminal_serial_no",
				type : "string"
			}, {
				name : "terminal_name",
				type : "string"
			}, {
				name : "alarm_type",
				type : "string"
			}, {
				name : "loc_status",
				type : "string"
			}, {
				name : "content",
				type : "string"
			}, {
				name : "lng",
				type : "string"
			}, {
				name : "lat",
				type : "string"
			}, {
				name : "speed",
				type : "string"
			}, {
				name : "tmn_status",
				type : "string"
			}, {
				name : "notify_status",
				type : "string"
			}, {
				name : "owner",
				type : "string"
			}, {
				name : "alarm_time",
				type : "string"
			}, {
				name : "create_time",
				type : "string"
			} ],
			pageSize : 10,
			proxy : {
				type : "ajax",
				url : "alarm/query.shtm",
				actionMethods : {
					read : "POST"
				},
				reader : {
					root : "alarmList",
					totalProperty : "totalCount"
				},
				simpleSortMode : true,
				listeners : {
					exception : function(f, i, j, g) {
						var h = Ext.JSON.decode(i.responseText);
						lbs.U.showError(h.errorMsgs)
					}
				}
			}
		});
		var c = Ext.create("Ext.grid.Panel", {
			margin : 0,
			autoScroll : true,
			store : e,
			border : false,
			columns : [ {
				text : "终端编号",
				dataIndex : "terminal_serial_no",
				width : 100
			}, {
				text : "昵称",
				dataIndex : "terminal_name",
				width : 100
			}, {
				text : "车主姓名",
				dataIndex : "owner",
				width : 100
			}, {
				text : "告警类型",
				dataIndex : "alarm_type",
				width : 90,
				renderer : function(f) {
					return lbs.C.ALARM_TYPE[f]
				}
			}, {
				text : "定位结果",
				dataIndex : "loc_status",
				width : 80,
				renderer : function(f) {
					if (lbs.U.isEmpty(f)) {
						return "无"
					} else {
						return lbs.C.LOC_RESULT[f]
					}
				}
			}, {
				text : "速度(km/h)",
				dataIndex : "speed",
				width : 80,
				renderer : function(f) {
					return lbs.U.isEmpty(f) ? "无" : f
				}
			}, {
				text : "终端状态",
				dataIndex : "tmn_status",
				width : 90,
				renderer : function(f) {
					if (lbs.U.isEmpty(f)) {
						return "无"
					} else {
						return lbs.C.TMN_STATUS[f]
					}
				}
			}, {
				text : "告警时间",
				dataIndex : "alarm_time",
				width : 140
			} ]
		});
		lbs.G.AlarmListWin = Ext.create("Ext.window.Window", {
			title : "最近告警列表",
			height : 360,
			width : 800,
			layout : "fit",
			closable : true,
			closeAction : "hide",
			modal : true,
			renderTo : Ext.getBody(),
			items : c
		})
	}
	lbs.G.AlarmListWin.show();
	var b = Ext.data.StoreManager.lookup(a);
	b.loadRecords([]);
	b.load({
		params : {
			terminalSerialNo : d.terminalSerialNo
		}
	})
};